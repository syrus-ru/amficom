/*
 * $Id: CoreAnalysisManager.java,v 1.77 2005/06/09 08:00:18 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.77 $, $Date: 2005/06/09 08:00:18 $
 * @module
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReliabilityModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ThreshDX;
import com.syrus.AMFICOM.analysis.dadara.ThreshDY;
import com.syrus.AMFICOM.analysis.dadara.TracePreAnalysis;
import com.syrus.AMFICOM.analysis.dadara.TracesAverages;
import com.syrus.io.BellcoreStructure;

public class CoreAnalysisManager
{
	protected CoreAnalysisManager()
	{ // empty
	}

	private static native double[] gauss(double[] y, double center,
			double amplitude, double sigma);
	
	private static native double nMedian(double[] y, int pos);

	/**
	 * @param y рефлектограмма, поступившая с рефлектометра
	 * @param dX шаг дискретизации в метрах по DX (не используется?)
	 * @param minLevel уровень чувствительности для определения границ события
	 * @param minWeld минимальное отраж. событие
	 * @param minConnector минимальное неотр. событие
	 * @param minEnd мин. уровень конца волокна
	 * @param noiseFactor множитель для уровня шума - чем больше множитель,
	 *   тем меньше чувствительность и больше достоверность.
	 *   (1.0 - макс. чувствительность, по уровню 3 сигма; рекомендованы
	 *   значения порядка 1.5 .. 3.0)
	 * @param nReflSize хар. размер неотраж. события (в точках),
     *   фактически нач. ширина вейвлета
     * @param rSACrit критическая амплитуда (дБ), выше которой возможно
     *    сильное увеличение протяженности отражательного события
     * @param rSSmall макс. длина (в точках) отражательного события
     *    с амплитудой менее rSACrit
     * @param rSBig макс. длина (в точках) отражательного события
     *    с амплитудой более rSACrit (должно быть больше rSSmall)
	 * @param traceLength длина рефлектограммы до конца волокна,
	 * может быть 0, тогда будет найдена автоматически
	 * @param noiseDB уровень шума по 3 сигма, в абс. дБ;
	 * может быть null - тогда будет найден автоматически
	 * @return массив событий
	 */
	private static native ReliabilitySimpleReflectogramEventImpl[] analyse6(
			double[] y,
			double dX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double noiseFactor,
			int nReflSize,
            double rSACrit,
            int rSSmall,
            int rSBig,
			int traceLength,
			double[] noiseDB);

	/**
	 * Вычисляет уровень шума в виде "ожидаемая амплитуда флуктуаций по
	 * уровню 1 сигма". 
	 * @param y входная рефлектограмма
	 * @param lenght длина, на которой пользователя интересует
	 * уровень шума, или 0, если шум нужен на всей y.length
	 * @return массив [length > 0 ? length : y.length]
	 */
	private static native double[] nCalcNoiseArray(double[] y, int lenght);

	/**
	 * Метод определяет длину рефлектограммы "до конца волокна".
	 * Алгоритм определения довольно прост, тип поиска "первого нуля",
	 * но его все равно имеет смысл вынести в native-код.
	 * @param y Входная рефлектограмма
	 * @return длина до "первого нуля" или по другому критерию
	 */
	private static native int nCalcTraceLength(double[] y);

	/**
	 * Расширяет верхние (или нижние) DX- и DY-пороги эталона, так,
	 * чтобы указанная кривая оказалась под (или над) пороговой кривой.
	 * Расширение нижних порогов может зависеть от состояние верхнего
	 * порога, и наоборот. Это вызвано связью верхнего и нижнего порога:
	 * в некоторых случаях верхние DX-параметры влияют на генерацию нижней
	 * кривой, и наоборот. Кроме того, расширение нижних порогов может
	 * дополнительно расширить верхние пороги, и наоборот. Это допускается
	 * просто для упрощения кода расширения порогов. (Текущая реализация
	 * расширяет парные верхние и нижние DX вместе).
	 * @param yBase базовая кривая расширяемого эталона
	 * @param yCover целевая кривая, которая должна оказаться внутри порогов
	 * @param thDX массив изменяемых DX-порогов эталога
	 * @param thDY массив изменяемых DY-порогов эталога
	 * @param softKeyToUpdate Thresh.SOFT_UP для расширения верхней кривой, Thresh.SOFT_DOWN - для нижней
	 * @param hardKeyToUpdate Thresh.HARD_UP для расширения верхней кривой, Thresh.HARD_DOWN - для нижней
	 * @param dyFactor Коэффициент запаса DY, не менее 1.0
	 */
	private static native void nExtendThreshToCoverCurve(
			double[] yBase,
			double[] yCover,
			ThreshDX[] thDX,
			ThreshDY[] thDY,
			int softKeyToUpdate,
			int hardKeyToUpdate,
			double dyFactor);

	/**
	 * Оценка уровня шума по кривой рефлектограммы.
	 * @param y Входная кривая рефлектограммы в дБ.
	 * @param length Длина волокна, > 0
	 * @return относительная величина шума (дБ) по уровню 3 сигма в каждой
     *  точке, длина массива length  
	 */
	public static double[] calcNoiseArray(double[] y, int length)
	{
		double[] ret = nCalcNoiseArray(y, length);
		for (int i = 0; i < ret.length; i++)
			ret[i] *= 3.0;
		return ret;
	}

	static {
		try {
			System.loadLibrary("dadara");
		} catch (java.lang.UnsatisfiedLinkError ex) {
			ex.printStackTrace();
		}
	}

    /**
     * фитирует кривую гауссианой y = amplitude * exp(-(x-center)**2 / sigma**2)
     * @param y входная кривая
     * @param maxIndex начальное значение положения максимума
     * @param outParams если не null, то заполняется результатами фитировки
     *   outParams[0] = center
     *   outParams[1] = amplitude
     *   outParams[2] = sigma (в sqrt(2) раз большая, чем стандартная)
     * @return фирирующая кривая
     */
	public static double[] calcGaussian(double[] y, int maxIndex, double[] outParams) {
		double[] gauss = new double[y.length];

		double maxValue = y[maxIndex];

        int width = 0;
		for (int i = 0; i < y.length; i++)
			if (y[i] > maxValue * 0.6)
				width++;

        double[] d = gauss(y, maxIndex, maxValue, width);

        if (outParams != null) {
            outParams[0] = d[0];
            outParams[1] = d[1];
            outParams[2] = d[2];
        }

        double center = d[0];
		maxValue = d[1];
		double sigmaSquared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = maxValue
					* Math.exp(-(i - center) * (i - center) / sigmaSquared);

		return gauss;
	}

    public static double[] calcGaussian(double[] y, int maxIndex) {
        return calcGaussian(y, maxIndex, null);
    }

	public static double[] calcThresholdCurve(double[] y, int max_index) {
		double[] threshold = new double[y.length];
		double max = 0;

		for (int i = 0; i < threshold.length; i++) {
			threshold[i] = 0;
			for (int j = 0; j < y.length; j++)
				if (j <= max_index - i || j >= max_index + i)
					threshold[i] += y[j];

			if (max < threshold[i])
				max = threshold[i];
		}

		for (int i = 0; i < threshold.length; i++)
			threshold[i] /= max;

		return threshold;
	}

	public static ReliabilitySimpleReflectogramEventImpl[] createSimpleEvents(
			double[] y,
			double deltaX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double noiseFactor,
			int reflSize,
			int nReflSize,
			int traceLength,
			double[] noiseArray)
	{
		return analyse6(y, deltaX,
			minLevel, minWeld, minConnector, minEnd, noiseFactor,
			nReflSize, 0.5, (int)(nReflSize * 1.5), reflSize, 
			traceLength, noiseArray);
	}

	/**
	 * Фитирует р/г
	 * @param y кривая
	 * @param traceLength длина, которую надо профитировать
	 * @param noiseArray шум (1 sigma?), заданный на длине traceLength (not null)
	 * @param sre список событий, полученный в рез. IA,
     *   используемый для более точной расстановки позиций узлов при фитировке
	 * @return mf фитированной кривой
	 */
	public static ModelFunction fitTrace(double[] y, int traceLength, double[] noiseArray, SimpleReflectogramEvent[] sre)
	{
		return ModelFunction.createFitedAsBreakL(y, 0, traceLength, noiseArray, sre);
	}

	/**
	 * Проверяет параметры для анализа.
	 * @param ap проверяемые параметры
	 * @return true, если набор корректен, false, если набор некорректен
	 */
	public static boolean checkAnalysisParameters(AnalysisParameters ap)
	{
        final double MIN_MIN_THRESHOLD = 0.001; // FIXME: debug: MIN_MIN_THRESHOLD should be 0.01 or 0.005 or 0.001 (?)
		if (ap.getMinThreshold() < MIN_MIN_THRESHOLD)
			return false;
		if (ap.getMinSplice() < ap.getMinThreshold())
			return false;
		if (ap.getMinConnector() < ap.getMinSplice())
			return false;
        if (ap.getMinEnd() < ap.getMinConnector())
            return false;
		return true;
	}

    /**
     * Проводит пред-анализ для одной рефлектограммы: <ul>
     *   <li> сохраняет traceData рефлектограммы
     *   <li> выделяет из р/г параметры тестирования, необходимые для анализа
     *     (разрешение, длина импульса, показатель преломления)
     *   <li> определяет длину рефлектограммы до "ухода в ноль"
     *   <li> если запрошено, определяет также уровень шума
     * @param bs Входная рефлектограмма
     * @param needNoise true, если нужено также вычислить уровень шума 
     * @return результаты, приемлемые для анализа
     *   {@link #makeAnalysis(TracePreAnalysis, AnalysisParameters)}
     */
    public static TracePreAnalysis makePreAnalysis(BellcoreStructure bs,
            boolean needNoise) {
        TracePreAnalysis res = new TracePreAnalysis();
        // данные рефлектограммы
        double[] y = bs.getTraceData(); // rather slow
        res.y = y;
        res.deltaX = bs.getResolution();
        res.ior = bs.getIOR();
        res.pulseWidth = bs.getPulsewidth();
        // данные анализа
        res.traceLength = calcTraceLength(y);
        if (needNoise) {
            double[] noise = calcNoiseArray(y, res.traceLength); // slow
            res.setNoise(noise);
        } else {
            res.setNoise(null);
        }
        return res;
    }

    /**
     * Проводит анализ и фитировку на основании результатов пред-анализа.
     * @param tpa результаты пред-анализа
     *   {@link #makePreAnalysis(BellcoreStructure)}
     * @param ap набор параметров для IA
     * @return результат анализа в виде mtae
     */
    public static ModelTraceAndEventsImpl makeAnalysis(
            TracePreAnalysis tpa,
            AnalysisParameters ap)
    {
        long t0 = System.currentTimeMillis();

        // определяем reflSize и nReflSize
        // FIXME: привести reflSize и nReflSize в порядок

        int reflSize = ReflectogramMath.getReflectiveEventSize(tpa.y, 0.5);
        int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
                tpa.y,
                tpa.pulseWidth,
                tpa.ior,
                tpa.deltaX);

        reflSize = tpa.traceLength / 10; // FIXME!
        if (reflSize < nReflSize * 10)
            reflSize = nReflSize * 10; // FIXME!

        long t1 = System.currentTimeMillis();

        // формирование событий по усредненной кривой

        ReliabilitySimpleReflectogramEventImpl[] rse = createSimpleEvents(
                tpa.y, tpa.deltaX,
                ap.getMinThreshold(), // XXX: 0.010
                ap.getMinSplice(),
                ap.getMinConnector(),
                ap.getMinEnd(),
                ap.getNoiseFactor(),
                reflSize, nReflSize,
                tpa.traceLength, tpa.avNoise);

        // FIX//ME: debug output of IA results
//      for (int i = 0; i < rse.length; i++)
//          System.out.println("rse[" + i + "]:"
//              + " " + rse[i].toString());

        // теперь уточняем длину рефлектограммы по концу последнего события
        // (длина может уменьшиться)

        int traceLength = rse.length > 0
            ? rse[rse.length - 1].getEnd() + 1
            : 0;

        long t2 = System.currentTimeMillis();

        // фитируем

        ModelFunction mf = fitTrace(tpa.y, traceLength, tpa.noiseAv, rse);

        long t3 = System.currentTimeMillis();

        double[] yTypical = tpa.y; // FIXME: надо брать одну, самую типовую р/г, а не усредненную, т.к. потом по этим данным будет считаться rms

        ModelTraceAndEventsImpl mtae =
            new ModelTraceAndEventsImpl(rse, mf, yTypical, tpa.deltaX);

        long t4 = System.currentTimeMillis();

        // FIXME: debug output of analysis timing
//      System.out.println("makeAnalysis: "
//          + "getDataAndLengthAndNoise: " + (t1-t0)
//          + "; IA: " + (t2-t1) + "; fit: " + (t3-t2)
//          + "; makeMTM: " + (t4-t3)
//          );

        return mtae;
    }

	/**
	 * Собирает информацию о совокупности рефлектограмм, достаточную
     * для формирования эталона.
	 * see {@link TracesAverages}
	 * @param bsColl входная совокупность р/г ({@link BellcoreStructure})
	 * @param needNoiseInfo нужна ли информация о шуме
	 * @param needMFInfo нужны ли кривые мин./макс. фитированных кривых
     * @param ap Параметры анализа, либо null если needMFInfo==false
	 * @return TracesAverages, в которой: <ul>
     *   <li>поля av.avNoise, av.noiseAv заполнены, если needNoiseInfo
     *   <li>поля maxYMF, minYMF заполнены, если needMFInfo
     *        (в таком случае проводит анализ и фитировку каждой р/г
     *         и определяет maxYMF, minYMF на основе раброса этих
     *         фитированных кривых)
     *   <li>все остальные поля заполнены в любом случае
     * </ul>
	 * @throws IncompatibleTracesException если
     *   параметры входных bs, необходимые для проведения анализа, различаются
	 * @throws IllegalArgumentException если входная совокупность р/г пуста
	 */
	public static TracesAverages findTracesAverages(Collection bsColl,
			boolean needNoiseInfo,
			boolean needMFInfo,
            AnalysisParameters ap)
	throws IncompatibleTracesException
	{
		// определяем число входных р/г
		final int N_TRACES = bsColl.size();

		if (N_TRACES == 0)
			throw new IllegalArgumentException("No traces to analyse");

		// усредняем входные р/г,
		// определяем мин. длину и типичный шум.

		boolean isFirst = true;

		TracesAverages res = new TracesAverages();
		res.nTraces = N_TRACES;

        double[] noiseAcc = null; // needs no initialization

		for (Iterator it = bsColl.iterator(); it.hasNext(); isFirst = false)
		{
            TracePreAnalysis tpa =
                makePreAnalysis((BellcoreStructure)it.next(), needNoiseInfo);

		    if (isFirst) {
                double[] y = (double[])tpa.y.clone(); // double[] array copying
                res.av = new TracePreAnalysis(tpa, y);
		    }
		    else {
				res.av.setMinLength(tpa.traceLength);
				addDoubleArray(res.av.y, tpa.y, res.av.traceLength);
                res.av.checkTracesCompatibility(tpa);
		    }

	    	if (needNoiseInfo) {
			    if (isFirst)
			    	noiseAcc = tpa.getNoise(); // клонирование необязательно, т.к. можно усреднять прямо в этом массиве 
			    else
					addDoubleArray(noiseAcc, tpa.getNoise(), res.av.traceLength);
		    }

		    if (needMFInfo) {
                // проводим анализ и берем из него модельную кривую
		    	ModelTrace mt = makeAnalysis(tpa, ap).getModelTrace();
		    	double[] yMF = mt.getYArray();
                res.av.setMinLength(mt.getLength()); // возможно, что длина модельной кривой будет меньше, чем начальное tracelength
		    	if (isFirst) {
		    		// need to make one more copy, so cloning once only
		    		res.minYMF = yMF;
		    		res.maxYMF = (double[])yMF.clone();
		    	}
		    	else {
                    // элементы массивов res.(min|max)YMF, выходящие за пределы
                    // res.av.tracelength, не будут обрабатываться (там останется мусор)
					ReflectogramMath.updateMinArray(res.minYMF, yMF);
					ReflectogramMath.updateMaxArray(res.maxYMF, yMF);
		    	}
		    }
		}

		// convert sum to average for avY
		for (int i = 0; i < res.av.traceLength; i++)
			res.av.y[i] /= N_TRACES;

		if (needNoiseInfo) {
			// convert sum to average for avNoise
			for (int i = 0; i < res.av.traceLength; i++)
				noiseAcc[i] /= N_TRACES;

            res.av.avNoise = noiseAcc;

            // make noiseAv
			res.av.noiseAv = N_TRACES == 1
			    ? (double[])noiseAcc.clone() // XXX: нужно ли клонирование? если да, то, возможно, оно делается не везде где надо
			    : calcNoiseArray(res.av.y, res.av.traceLength);
		}

        // XXX: в принципе, здесь бы неплохо провести усечение тех массивов
        // res, которые длиннее res.av.traceLength, для того, чтобы
        // не сбивать с толку пользователя этих данных. Однако, поскольку
        // это требует несколько лишних копирований, а
        // использование этих данных предполагается только в этом модуле,
        // мы этого не делаем.

        return res;
	}

	/**
	 *  note: later it may be turned to native for performance
	 */
	private static void addDoubleArray(double[] acc, double[] input, int length)
	{
		for (int i = 0; i < length; i++)
			acc[i] += input[i]; 
	}

	/**
	 * Делает анализ. Скрывает сложности, связанные с правильным
	 * порядком вызова IA, fit, calcMutualParameters и выставлением нач. порогов.
	 * @todo declare to throw "invalid parameters exception"
	 * 
	 * @param bs рефлектограмма
	 * @param ap параметры анализа
	 * @return массив событий
	 */
	public static ModelTraceAndEventsImpl makeAnalysis(
			BellcoreStructure bs,
			AnalysisParameters ap)
	{
        TracePreAnalysis tpa = makePreAnalysis(bs, true);
        return makeAnalysis(tpa, ap);
	}

	/**
	 * Создает эталонный MTM по непустому набору рефлектограмм и параметрам
	 * анализа.
	 * <ul>
	 * <li> Если в наборе только одна р/г, строит пороги по ее bs,
	 * <li> если несколько - по всем их mf,
	 * <li> если ни одной - бросает IllegalArgumentException
	 *      (see {@link #findTracesAverages(Collection, boolean, boolean, AnalysisParameters)}).
	 * </ul>
	 * @param bsColl коллекция входных р/г.
	 *   Должна быть непуста, а р/г должны иметь одинаковые параметры
     *   регистрации, используемые в анализе
	 * @param ap параметры анализа
	 * @return MTM созданного эталона
	 * @throws IllegalArgumentException если bsColl пуст
     *   ({@link #findTracesAverages})
	 * @throws IncompatibleTracesException если bsColl содержит р/г
	 *   с разными длинами, разрешением, длительностью импульса или
	 *   показателем преломления ({@link #findTracesAverages})
	 */
	public static ModelTraceManager makeEtalon(Collection bsColl,
			AnalysisParameters ap)
	throws IncompatibleTracesException
	{
		TracesAverages av = findTracesAverages(bsColl, true, true, ap);
		ModelTraceAndEventsImpl mtae = makeAnalysis(av.av, ap);
		ModelTraceManager mtm = new ModelTraceManager(mtae);
		if (bsColl.size() > 1) {
			// extend to max dev of _mf_
			mtm.updateThreshToContain(av.maxYMF, av.minYMF, 0.03, 1.2); 
		}
		else {
			// extend to a single curve: original (noisy) _bs_
			mtm.updateThreshToContain(av.av.y, av.av.y, 0.03, 3.0);
		}
		return mtm;
	}

    /**
     * Находит в коллекции рефлектограмму, ближайшую к усредненному по
     * коллекции значению. Входные р/г должны быть совместны, т.е.
     * иметь одни и те же режимы регистрации (разрешение, длина импульса и пр.)
     * @param bsColl входная коллекция рефлектограмм
     * @return самую среднюю рефлектограмму среди входных
     * @throws IncompatibleTracesException Если входные рефлектограммы
     * несовместны
     * @throws IllegalArgumentException Если входная совокупность р/г пуста
     */
    public static BellcoreStructure getMostTypicalTrace(Collection bsColl)
    throws IncompatibleTracesException
    {
        TracesAverages av = findTracesAverages(bsColl, false, false, null);

        // если входная коллекция пуста, то к этому моменту уже будет
        // выброшено исключение IllegalArgumentException,
        // т.ч. return null не произойдет.

        BellcoreStructure nearest = null;
        double bestDistance = 0;
        for (Iterator it = bsColl.iterator(); it.hasNext();)
        {
            BellcoreStructure bs = (BellcoreStructure)it.next();
            double[] yBS = bs.getTraceData();
            double distance = ReflectogramComparer.getMaxDeviation(av.av.y,
                    yBS,
                    av.av.traceLength);
            if (nearest == null || distance < bestDistance)
            {
                nearest = bs;
                bestDistance = distance;
            }
        }
        return nearest;
    }

	public static double getMedian(double[] y, int pos)
	{
        if (pos < 0 || pos >= y.length)
            throw new IllegalArgumentException("Invalid pos for getMedian");
		return nMedian(y, pos);
	}
    /**
     * @throws IllegalArgumentException is possible if ratio is outside [0,1]
     */
    public static double getMedian(double[] y, int iFrom, int iToEx, double ratio)
    {
        double[] temp = new double[iToEx - iFrom];
        System.arraycopy(y, iFrom, temp, 0, temp.length);
        return nMedian(temp, (int)(temp.length * ratio));
    }
	public static double getMedian(double[] y)
	{
		return nMedian(y, y.length / 2);
	}

	/**
	 * Уточняет параметры коннектора. Такое уточнение призвано защитить алгоритмы
	 * L-масштабирования от шумов, а также дать необх. инф. о положении максимума
	 * коннектора, чтобы можно было отличить фронт от спада.
	 * @param mf Модельная кривая
	 * @param evBegin Начальное начало события
	 * @param evEnd Начальный конец события
	 * @return int[3] { первый минимум (уточненное начала), максимум, последний минимум (уточненный конец) } 
	 */
	public static int[] getConnectorMinMaxMin(ModelFunction mf, int evBegin, int evEnd)
	{
		final int X0 = evBegin;
		final int N = evEnd - evBegin + 1;
		double[] arr = mf.funFillArray(X0, 1.0, N);
		int iMax = ReflectogramMath.getArrayMaxIndex(arr, 0, N - 1);
		int iLMin = ReflectogramMath.getArrayMinIndex(arr, 0, iMax);
		int iRMin = ReflectogramMath.getArrayMinIndex(arr, iMax, N - 1);
		return new int[] {evBegin + iLMin, evBegin + iMax, evBegin + iRMin};
	}
	
	/**
	 * See specification of {@link #nCalcTraceLength(double[])}
	 * @param y Рефлектограмма
	 * @return длина до конца волокна
	 */
	public static int calcTraceLength(double[] y)
	{
		return nCalcTraceLength(y);
	}

	/**
	 * See specification of {@link #nExtendThreshToCoverCurve(double[], double[], ThreshDX[], ThreshDY[], int, int, double)}
	 */
	public static void extendThreshToCoverCurve(
			double[] yBase,
			double[] yCover,
			ThreshDX[] thDX,
			ThreshDY[] thDY,
			int softKeyToUpdate,
			int hardKeyToUpdate,
			double dyFactor)
	{
		nExtendThreshToCoverCurve(yBase,
			yCover,
			thDX,
			thDY,
			softKeyToUpdate,
			hardKeyToUpdate,
			dyFactor);
	}

    /**
     * Проводит анализ рефлектограммы, сравнивает ее с порогом обнаружения
     * обрыва и эталонным MTM, формирует список алармов.
     * Текущая версия возвращает 0 или 1 алармов.
     * @param bs Анализируемая (текущая; пробная) рефлектограмма
     * @param ap Параметры анализа (null, если сравнение событий не нужно)
     * @param breakThresh Уровень обнаружения обрыва р/г
     * @param etMTM Эталонная р/г и события
     */
    public static List analyseCompareAndMakeAlarms(BellcoreStructure bs,
            AnalysisParameters ap,
            double breakThresh,
            ModelTraceManager etMTM) {
        // формируем выходной список
        List alarmList = new ArrayList();

        // проводим пред-анализ
        TracePreAnalysis tpa = makePreAnalysis(bs, true);

        // начало конца волокна по эталону 
        int etMinLength = etMTM.getMTAE().getSimpleEvent(
                etMTM.getMTAE().getNEvents() - 1).getBegin();

        // в любом случае - определение шума, анализ и и фитировка
        ReliabilityModelTraceAndEvents mtae = makeAnalysis(tpa, ap);
        ModelTrace mt = mtae.getModelTrace();

        // НЕ добавляем к результатам анализа найденную длину р/г и фитированную кривую - это пока не нужно
//      outParameters.put(CODENAME_DADARA_TRACELENGTH, ByteArray.toByteArray(traceLength));
//      outParameters.put(CODENAME_DARARA_MODELFUNCTION, mf.toByteArray());

        // пытаемся обнаружить обрыв волокна:
        // (1) на участке до ухода в шум (x < traceLength) - по уходу м.ф. ниже уровня breakThresh
        // XXX - надо ли было предварительно смещать р/г по вертикали?
        int breakPos = ModelTraceComparer.compareToMinLevel(mt, breakThresh);
        // (2) на участке шума (x >= traceLength) - не ушли ли в шум до начала последнего коннектора?
        if (breakPos < 0 && tpa.traceLength < etMinLength)
            breakPos = tpa.traceLength;

        if (breakPos >= 0) // если был обнаружен обрыв
        {
            ReflectogramAlarm alarm = new ReflectogramAlarm();
            alarm.level = ReflectogramAlarm.LEVEL_HARD;
            alarm.alarmType = ReflectogramAlarm.TYPE_LINEBREAK;
            alarm.pointCoord = breakPos;
            alarm.deltaX = etMTM.getMTAE().getDeltaX();
            // конечная дистанция аларма := конец эталонной р/г (но не более длины р/г)
            alarm.endPointCoord = Math.min(tpa.y.length, etMinLength);

            // XXX - если на обрыве есть заметное отражение, то дистанция будет завышена
            // мб, в таком случае не надо игнорировать HARD алармы?

            alarmList.add(alarm);
        }
        else // обрыв не обнаружен
        {
            ReflectogramAlarm alarm = null;
            if (true) { // @todo: проверять, запрошен такой тип сравнения
                // XXX: в этом случае шум вычисляется дважды
                alarm = ModelTraceComparer.compareMTAEToMTM(mtae, etMTM);
            } else {
                alarm = ModelTraceComparer.compareTraceToMTM(mt, etMTM);
            }

            if (alarm != null) {
                alarmList.add(alarm);
            }
        }
        return alarmList;
    }
}
