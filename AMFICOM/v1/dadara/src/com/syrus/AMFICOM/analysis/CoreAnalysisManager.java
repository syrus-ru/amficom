/*
 * $Id: CoreAnalysisManager.java,v 1.61 2005/05/03 06:27:20 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.61 $, $Date: 2005/05/03 06:27:20 $
 * @module
 */

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.ThreshDX;
import com.syrus.AMFICOM.analysis.dadara.ThreshDY;
import com.syrus.AMFICOM.analysis.dadara.TracesAverages;

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
	 * @param reflSize хар. размер отраж. события (в точках),
	 *   влияет на макс. длину коннектора 
	 * @param nReflSize хар. размер неотраж. события (в точках)
	 * @param traceLength длина рефлектограммы до конца волокна,
	 * может быть 0, тогда будет найдена автоматически
	 * @param noiseDB уровень шума по 3 сигма, в абс. дБ;
	 * может быть null - тогда будет найден автоматически
	 * @return массив событий
	 */
	private static native ReliabilitySimpleReflectogramEventImpl[] analyse5(
			double[] y,
			double dX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double noiseFactor,
			int reflSize,
			int nReflSize,
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
	 * Алгоритм может пользоваться или не пользоваться данными
	 * предварительного IA анализа (ev). Не исключено, что после
	 * определения шума будет сделан уточненный IA.
	 * @param y  Входная кривая рефлектограммы в дБ.
	 * @param length Длина волокна, > 0
	 * @return относительная величина шума (дБ) по уровню 3 сигма, длина массива length  
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

	public static double[] calcGaussian(double[] y, int maxIndex) {
		double[] gauss = new double[y.length];
		int width = 0;

		double maxValue = y[maxIndex];

		maxValue *= .2;
		for (int i = 0; i < y.length; i++)
			if (y[i] > maxValue)
				width++;
		maxValue /= .2;

		double[] d = gauss(y, maxIndex, maxValue, width);
		double center = d[0];
		maxValue = d[1];
		double sigmaSquared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = maxValue
					* Math.exp(-(i - center) * (i - center) / sigmaSquared);

		return gauss;
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
		// FIXME: debug output of IA params
		System.out.println("cSE: "
			+ minLevel
			+ "/" + minWeld
			+ "/" + minConnector
			+ "/" + minEnd
			+ "; nf " + noiseFactor
			+ "; rs/nrs " + reflSize
			+ "/" + nReflSize);
		return analyse5(y, deltaX,
			minLevel, minWeld, minConnector, minEnd, noiseFactor,
			reflSize, nReflSize,
			traceLength, noiseArray);
	}

	/**
	 * Фитирует р/г
	 * @param y кривая
	 * @param traceLength длина, которую надо профитировать
	 * @param noiseArray шум (1 sigma?), заданный на длине traceLength (not null)
	 * @return mf фитированной кривой
	 */
	public static ModelFunction fitTrace(double[] y, int traceLength, double[] noiseArray)
	{
		return ModelFunction.createFitedAsBreakL(y, 0, traceLength, noiseArray);
	}

	/**
	 * Проверяет параметры для анализа.
	 * @param ap проверяемые параметры
	 * @return true, если набор корректен, false, если набор некорректен
	 */
	public static boolean checkAnalysisParameters(AnalysisParameters ap)
	{
        final double MIN_MIN_THRESHOLD = 0.001; // FIXME: debug: MIN_MIN_THRESHOLD should be 0.01 or 0.005 (?)
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
	 * Собирает информацию о совокупности рефлектограмм
	 * see {@link TracesAverages}
	 * @param bsColl входная совокупность р/г
	 * @param needNoiseInfo нужна ли информация о шуме
	 * @param needMFInfo нужны ли кривые мин./макс. фитированных кривых
	 * @param needBSInfo нужна ли инф. о параметрах bs
	 * @return структура типа TracesAverages с заполненными полями типа
	 * general Info и запрошенных дополнительны типов
	 * @throws IncompatibleTracesException если needBSInfo, но
	 * соответствующие параметры входных bs различаются
	 * @throws IllegalArgumentException если входная совокупность р/г пуста
	 */
	public static TracesAverages findTracesAverages(Collection bsColl,
			boolean needNoiseInfo,
			boolean needMFInfo,
			boolean needBSInfo)
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

		for (Iterator it = bsColl.iterator(); it.hasNext(); isFirst = false)
		{
			BellcoreStructure bs = (BellcoreStructure)it.next();

			// general info
		    double[] yCur = bs.getTraceData(); // is rather slow, typ. 25% of loop time
		    int curLength = calcTraceLength(yCur);

		    // general info
		    if (isFirst) {
				res.minTraceLength = curLength;
				res.avY = (double[])yCur.clone(); // double[] array copying
		    }
		    else {
				res.minTraceLength = Math.min(res.minTraceLength, curLength);
				addDoubleArray(res.avY, yCur, res.minTraceLength);
		    }

		    // NB: noiseData может немного зависеть от traceLength, поэтому
		    // при расчете noiseArray используем curLength, а не traceLength,
		    // хотя расчет на traceLength мог бы быть немного быстрее
		    double[] noiseData = needNoiseInfo || needMFInfo
		    	? calcNoiseArray(yCur, curLength) // typ., 50% of loop time
		    	: null;

	    	if (needNoiseInfo) {
			    if (isFirst)
			    	res.avNoise = noiseData; // cloning is not necessary
			    else
					addDoubleArray(res.avNoise, noiseData, res.minTraceLength);
		    }

		    if (needMFInfo) {
		    	ModelFunction mf = fitTrace(yCur, curLength, noiseData);
		    	double[] yMF = mf.funFillArray(0, 1, res.minTraceLength);
		    	if (isFirst) {
		    		// need to make one more copy, so close once only 
		    		res.minYMF = yMF;
		    		res.maxYMF = (double[])yMF.clone();
		    	}
		    	else {
					ReflectogramMath.updateMinArray(res.minYMF, yMF);
					ReflectogramMath.updateMaxArray(res.maxYMF, yMF);
		    	}
		    }

		    if (needBSInfo) {
		    	double deltaXCur = bs.getResolution(); // метры
		    	double pulseWidthCur = bs.getPulsewidth(); // нс
		    	double iorCur = bs.getIOR();
		    	if (isFirst) {
					res.deltaX = deltaXCur;
					res.pulseWidth = pulseWidthCur;
					res.ior = iorCur;
		    	}
		    	else {
					if (deltaXCur != res.deltaX)
						throw new IncompatibleTracesException("different deltaX");
					if (pulseWidthCur != res.pulseWidth)
						throw new IncompatibleTracesException("different pulse width");
					if (iorCur != res.ior)
						throw new IncompatibleTracesException("different IOR");
		    	}
			}
		}

		// convert sum to average for avY
		for (int i = 0; i < res.minTraceLength; i++)
			res.avY[i] /= N_TRACES;

		if (needNoiseInfo) {
			// convert sum to average for avNoise
			for (int i = 0; i < res.minTraceLength; i++)
				res.avNoise[i] /= N_TRACES;

			// make noiseAv
			res.noiseAv = N_TRACES == 1
			? (double[])res.avNoise.clone()
			: calcNoiseArray(res.avY, res.minTraceLength);
		}

        return res;
	}

	/**
	 * Проводит анализ усредненной р/г.
	 * В качестве базовой кривой берется усредненная р/г,
	 * точность фитировки определяется флуктуациями усредненной р/г,
	 * а точность определения событий - усредненными флуктуациями исходных
	 * р/г.
	 * @param av информация о наборе р/г с заполненными noise и bs полями, 
	 * @param ap набор параметров для IA
	 * @return результат анализа в виде mtae
	 */
	public static ModelTraceAndEventsImpl makeAnalysis(
			TracesAverages av,
			AnalysisParameters ap
			)
	{
		long t0 = System.currentTimeMillis();

		// определяем reflSize и nReflSize
        // FIXME: привести reflSize и nReflSize в порядок

		int reflSize = ReflectogramMath.getReflectiveEventSize(av.avY, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				av.avY,
				av.pulseWidth,
				av.ior,
				av.deltaX);

		if (nReflSize > 3 * reflSize / 5)
			nReflSize = 3 * reflSize / 5;
		reflSize *= 5;

		long t1 = System.currentTimeMillis();

		// формирование событий по усредненной кривой

		ReliabilitySimpleReflectogramEventImpl[] rse = createSimpleEvents(
				av.avY, av.deltaX,
				ap.getMinThreshold(),
				ap.getMinSplice(),
				ap.getMinConnector(),
				ap.getMinEnd(),
				ap.getNoiseFactor(),
				reflSize, nReflSize,
				av.minTraceLength, av.avNoise);

		// FIX//ME: debug output of IA results
//		for (int i = 0; i < rse.length; i++)
//			System.out.println("rse[" + i + "]:"
//				+ " begin=" + rse[i].getBegin()
//				+ " end=" + rse[i].getEnd()
//				+ " type=" + rse[i].getEventType()
//				+ " r=" + (rse[i].hasReliability() ?
//					String.valueOf(rse[i].getReliability()) : "<undefined>")
//				);

		// теперь уточняем длину рефлектограммы по концу последнего события
		// (длина может уменьшиться)

		int traceLength = rse.length > 0
			? rse[rse.length - 1].getEnd() + 1
			: 0;

		long t2 = System.currentTimeMillis();

		// фитируем

		ModelFunction mf = fitTrace(av.avY, traceLength, av.noiseAv);

		long t3 = System.currentTimeMillis();

		ModelTraceAndEventsImpl mtae = new ModelTraceAndEventsImpl(rse, mf, av.deltaX);

		long t4 = System.currentTimeMillis();

        // FIXME: debug output of analysis timing
		System.out.println("makeAnalysis: "
			+ "getDataAndLengthAndNoise: " + (t1-t0)
			+ "; IA: " + (t2-t1) + "; fit: " + (t3-t2)
			+ "; makeMTM: " + (t4-t3)
			);

		return mtae;
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
	 * @todo: declare to throw "invalid parameters exception"
	 * 
	 * @param bs рефлектограмма
	 * @param ap параметры анализа
	 * @return массив событий
	 */
	public static ModelTraceAndEventsImpl makeAnalysis(
			BellcoreStructure bs,
			AnalysisParameters ap)
	{
		Set bsSet = new HashSet(1);
		bsSet.add(bs);
		try	{
			// определяем все необходимые нам параметры совокупности р/г
            long t0 = System.currentTimeMillis();
			TracesAverages av = findTracesAverages(bsSet, true, false, true);
            long t1 = System.currentTimeMillis();
            System.out.println("makeAnalysis: findTracesAverages: dt " + (t1-t0)); // FIXME: debug: analysis timing
			return makeAnalysis(av, ap);
		} catch (IncompatibleTracesException e) {
			// одна рефлектограмма всегда совместима с самой собой
			throw new InternalError("Unexpected exception: " + e);
		}
	}

	/**
	 * Создает эталонный MTM по непустому набору рефлектограмм и параметрам
	 * анализа.
	 * <ul>
	 * <li> Если в наборе только одна р/г, строит пороги по ее bs,
	 * <li> если несколько - по всем их mf,
	 * <li> если ни одной - возникает IllegalArgumentException
	 * (see {@link #findTracesAverages(Collection, boolean, boolean, boolean)}).
	 * </ul>
	 * @param bsColl коллекция входных р/г.
	 *   Должна быть непуста, а р/г должны иметь одинаковую длину.
	 * @param ap параметры анализа
	 * @return MTM созданного эталона
	 * @throws IllegalArgumentException если bsColl пуст
	 * @throws IncompatibleTracesException если bsColl содержит р/г
	 *   с разными длинами, разрешением, длительностью импульса или
	 *   показателем преломления.
	 */
	public static ModelTraceManager makeEtalon(Collection bsColl,
			AnalysisParameters ap)
	throws IncompatibleTracesException
	{
		TracesAverages av = findTracesAverages(bsColl, true, true, true);
		ModelTraceAndEventsImpl mtae = makeAnalysis(av, ap);
		ModelTraceManager mtm = new ModelTraceManager(mtae);
		if (bsColl.size() > 1) {
			// extend to max dev of mf
			mtm.updateThreshToContain(av.maxYMF, av.minYMF, 0.03, 1.2); 
		}
		else {
			// extend to a single curve: original bs
			mtm.updateThreshToContain(av.avY, av.avY, 0.03, 3.0);
		}
		return mtm;
	}

    /**
     * Находит в коллекции рефлектограмму, ближайшую к усредненному по
     * коллекции значению. Входные р/г должны быть совместны, т.е.
     * иметь одни и те же режимы регистрации (разрешение, длина импульса и пр.)
     * @param bsColl входная коллекция рефлектограмм
     * @return самю среднюю рефлектограмму из числа существующих
     * @throws IncompatibleTracesException Если входные рефлектограммы
     * несовместны
     * @throws IllegalArgumentException Если входная совокупность р/г пуста
     */
    public static BellcoreStructure getMostTypicalTrace(Collection bsColl)
    throws IncompatibleTracesException
    {
        TracesAverages av = findTracesAverages(bsColl, false, false, false);

        // если входная коллекция пуста, то к этому моменту уже будет
        // выброшено исключение IllegalArgumentException,
        // т.ч. return null не произойдет.

        BellcoreStructure nearest = null;
        double bestDistance = 0;
        for (Iterator it = bsColl.iterator(); it.hasNext();)
        {
            BellcoreStructure bs = (BellcoreStructure)it.next();
            double[] yBS = bs.getTraceData();
            double distance = ReflectogramComparer.getMaxDeviation(av.avY,
                    yBS,
                    av.minTraceLength);
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
		return nMedian(y, pos);
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
}
