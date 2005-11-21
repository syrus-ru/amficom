/*
 * $Id: CoreAnalysisManager.java,v 1.135 2005/11/21 13:23:34 saa Exp $
 * 
 * Copyright © Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.135 $, $Date: 2005/11/21 13:23:34 $
 * @module
 */

import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType.TYPE_LINEBREAK;
import static com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity.SEVERITY_HARD;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.EvaluationPerEventResult;
import com.syrus.AMFICOM.analysis.dadara.IncompatibleTracesException;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTrace;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.QualityComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramComparer;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatchImpl;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReliabilitySimpleReflectogramEventImpl;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.analysis.dadara.ThreshDX;
import com.syrus.AMFICOM.analysis.dadara.ThreshDY;
import com.syrus.AMFICOM.analysis.dadara.TracePreAnalysis;
import com.syrus.AMFICOM.analysis.dadara.TracesAverages;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class CoreAnalysisManager
{
	/**
	 * Постоянная компонета запаса (дБ) генерируемых DY-порогов
	 */
	private static final double MTM_DY_MARGIN = 0.03;
	/**
	 * Фактор запаса (разы) DY-порогов, генерируемых по набору (2 и более) м.ф.
	 */
	private static final double MTM_DY_FACTOR_MF_BASED = 1.2;
	/**
	 * Фактор запаса (разы) DY-порогов, генерируемых на основе одной р/г и ее м.ф.
	 */
	private static final double MTM_DY_FACTOR_TR_BASED = 3.0;

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
	 * @param minEnd мин. амплитуда отражения конца волокна
	 * @param minEotLevel мин. абс. уровень отражения конца волокна
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
	 * @param scaleFactor - множитель для прогрессии используемых масштабов.
	 * @return массив событий
	 */
	private static native ReliabilitySimpleReflectogramEventImpl[] analyse8(
			double[] y,
			double dX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double minEotLevel,
			double noiseFactor,
			int nReflSize,
			double rSACrit,
			int rSSmall,
			int rSBig,
			int traceLength,
			double[] noiseDB,
			double scaleFactor);

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
	 * Вычисляет абсолютный уровень шума в дБ.
	 * @param y входная рефлектограмма
	 * @param lenght длина, на которой пользователя интересует
	 * уровень шума, или 0, если шум нужен на всей y.length
	 * @return массив [length > 0 ? length : y.length]
	 */
	private static native double[] nCalcAbsNoiseArray(double[] y, int lenght);

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
	 * @param softKeyToUpdate Thresh.SOFT_UP для расширения верхней кривой,
	 *   Thresh.SOFT_DOWN - для нижней
	 * @param hardKeyToUpdate Thresh.HARD_UP для расширения верхней кривой,
	 *   Thresh.HARD_DOWN - для нижней
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
	protected static double[] calcNoiseArray(double[] y, int length)
	{
		double[] ret = nCalcNoiseArray(y, length);
		for (int i = 0; i < ret.length; i++)
			ret[i] *= 3.0;
		return ret;
	}

	//@todo: performance: generata noise and AbsNoise together
	protected static double[] calcAbsNoiseArray(double[] y, int length) {
		return nCalcAbsNoiseArray(y, length);
	}

	/**
	 * Загружает native-библиотеку dadara.
	 * Поскольку эта библиотека используется и в некоторых
	 * других классах модуля dadara, этот метод сделан public. 
	 */
	public static void loadDLL() {
		try {
			System.loadLibrary("dadara");
		} catch (final UnsatisfiedLinkError ule) {
			Log.errorMessage(ule);
		}
	}

	static {
		loadDLL();
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
	public static double[] calcGaussian(double[] y,
			int maxIndex,
			double[] outParams) {
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

	protected static
	ReliabilitySimpleReflectogramEventImpl[] createSimpleEvents(
			double[] y,
			double deltaX,
			double minLevel,
			double minWeld,
			double minConnector,
			double minEnd,
			double minEotLevel,
			double noiseFactor,
			int nReflSize,
			double rSACrit,
			int rSSmall,
			int rSBig,
			int traceLength,
			double[] noiseArray,
			double scaleFactor
			) {
		return analyse8(y, deltaX,
			minLevel, minWeld, minConnector, minEnd, minEotLevel,
			noiseFactor,
			nReflSize, rSACrit, rSSmall, rSBig,
			traceLength, noiseArray, scaleFactor);
	}

	/**
	 * Фитирует р/г
	 * @param y кривая
	 * @param traceLength длина, которую надо профитировать
	 * @param noiseArray шум (1 sigma?), заданный на длине traceLength, not null
	 * @param sre список событий, полученный в рез. IA,
	 *   используемый для более точной расстановки позиций узлов при фитировке
	 * @return mf фитированной кривой
	 */
	protected static ModelFunction fitTrace(double[] y,
			int traceLength,
			double[] noiseArray,
			SimpleReflectogramEvent[] sre) {
		return ModelFunction.createFitedAsBreakL(
				y, 0, traceLength - 1, noiseArray, sre);
	}

	/**
	 * Проводит пред-анализ для одной пред-фильтрованной рефлектограммы: <ul>
	 *   <li> сохраняет traceData рефлектограммы
	 *   <li> выделяет из р/г параметры тестирования, необходимые для анализа
	 *     (разрешение, длина импульса, показатель преломления)
	 *   <li> определяет длину рефлектограммы до "ухода в ноль"
	 *   <li> если запрошено, определяет также уровень шума
	 * @param trace Входная рефлектограмма после пред-фильтрации
	 * @param needNoise true, если нужено также вычислить уровень шума 
	 * @return результаты, приемлемые для анализа
	 *   {@link #makeAnalysis(TracePreAnalysis, AnalysisParameters)}
	 */
	protected static TracePreAnalysis makePreAnalysis(PFTrace trace,
			boolean needNoise) {
		TracePreAnalysis res = new TracePreAnalysis();
		// данные рефлектограммы
		double[] yRaw = trace.getRawTrace();
		double[] yFilteredClone = trace.getFilteredTraceClone();
		res.yTrace = yFilteredClone;
		res.deltaX = trace.getResolution();
		res.ior = trace.getIOR();
		res.pulseWidth = trace.getPulsewidth();
		// данные пред-анализа
		res.traceLength = calcTraceLength(yRaw);
		if (needNoise) {
			double[] noise = calcNoiseArray(yRaw, res.traceLength); // slow
			double[] absNoise = calcAbsNoiseArray(yRaw, res.traceLength); // slow // FIXME: call one of them only
			res.setNoise(noise);
			res.yCorr = calcYCorr(res.yTrace, absNoise, res.traceLength);
		} else {
			res.setNoise(null);
		}
		Log.debugMessage("raw.length=" + trace.getRawTrace().length
				+ " filtered.traceLength=" + trace.getFilteredTrace().length
				+ " res.traceLength=" + res.traceLength, FINER);

		return res;
	}

	// Корректируем рефлектограмму по уровню шума.
	// Это частичная компенсация нелинейности логарифмической шкалы.
	// XXX: надо бы реализовать полноценный учет логарифмической шкалы.
	private static double[] calcYCorr(double[]yRaw, double[]absNoise, int len) {
		double[] yCorr = new double[len];
		final double DELTA = 0.0;
		for (int i = 0; i < len; i++) {
			yCorr[i] = yRaw[i];
			if (yCorr[i] < absNoise[i] + DELTA) {
//				System.out.println("yCorr[" + i + "] changed from " + yCorr[i] + " to " + (absNoise[i] + DELTA)); // FIXME
				yCorr[i] = absNoise[i] + DELTA;
			}
		}
		return yCorr;
	}

	/**
	 * Проводит анализ и фитировку на основании результатов пред-анализа.
	 * @param tpa результаты пред-анализа
	 *   {@link #makePreAnalysis}
	 * @param ap набор параметров для IA
	 * @return результат анализа в виде mtae
	 */
	protected static ModelTraceAndEventsImpl makeAnalysis(
			TracePreAnalysis tpa,
			AnalysisParameters ap)
	{
		long t0 = System.nanoTime();

		// определяем reflSize и nReflSize
		// FIXME: привести reflSize и nReflSize в порядок

//		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
//				tpa.y,
//				tpa.pulseWidth,
//				tpa.ior,
//				tpa.deltaX);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				tpa.pulseWidth,
				tpa.ior,
				tpa.deltaX);

		int rsBig = (int) (tpa.traceLength * ap.getL2rsaBig());
		if (rsBig < nReflSize * ap.getNrs2rsaBig())
			rsBig = (int) (nReflSize * ap.getNrs2rsaBig());

		long t1 = System.nanoTime();

		// формирование событий по усредненной кривой

		ReliabilitySimpleReflectogramEventImpl[] rse = createSimpleEvents(
				tpa.yCorr,
				tpa.deltaX,
				ap.getEventTh(),
				ap.getSpliceTh(),
				ap.getConnectorTh(),
				ap.getEndTh(),
				ap.getLevelEot(),
				ap.getNoiseFactor(),
				nReflSize,
				ap.getRsaCrit(),
				(int)(nReflSize * ap.getNrs2rsaSmall()),
				rsBig,
				tpa.traceLength,
				tpa.avNoise,
				ap.getScaleFactor());

		for (int i = 0; i < rse.length; i++) {
			Log.debugMessage("rse[" + i + "]:" + " " + rse[i], FINEST);
		}

		// теперь уточняем длину рефлектограммы по концу последнего события
		// (длина может уменьшиться)

		int traceLength = rse.length > 0
			? rse[rse.length - 1].getEnd() + 1
			: 0;

		Log.debugMessage("y.length=" + tpa.yTrace.length
				+ " tpa.traceLength=" + tpa.traceLength
				+ " rse.traceLength=" + traceLength, FINER);

		long t2 = System.nanoTime();

		// фитируем

		// Фитируем по yCorr, а не tpa.yTrace.

		// --- размышления, которые удалим попозже ---
		// Против этого были такие возражения, смысл которых я уже забыл:
		// 1. создавать MTAEI желательно по той же р/г, что и фитировать.
		// 2. MTAEI может создаваться не только здесь, но и в других местах,
		// в которых про нашу yCorr ничего не знают (т.к. она зависит от шума).
		// На них сейчас видятся такие ответы,
		// 1. зачем?
		// 2. больше нигде MTAEI создаваться не должен (реально он создается
		// только здесь и при редактировании списка событий - но там
		// а.к. не изменяется), а ComplexInfo вычисляется только
		// при создании MTAEI

		ModelFunction mf = fitTrace(tpa.yCorr, traceLength, tpa.noiseAv, rse);

		long t3 = System.nanoTime();

		// XXX: надо знать не только усредненную р/г, но и самую типовую,
		// чтобы по типовой определять rmsDev,maxDev и т.п.
		// На данный момент это не критично, т.к. результаты анализа
		// многих р/г используются только для эталона, а у него maxDev/rmsDev
		// не используются.
		ModelTraceAndEventsImpl mtae =
			new ModelTraceAndEventsImpl(rse, mf, tpa.yTrace, tpa.deltaX);

		long t4 = System.nanoTime();

		Log.debugMessage("makeAnalysis: "
				+ "getDataAndLengthAndNoise: " + (t1-t0)/1e6
				+ "; IA: " + (t2-t1)/1e6 + "; fit: " + (t3-t2)/1e6
				+ "; makeMTAE: " + (t4-t3)/1e6,
				FINEST);

		return mtae;
	}

	/**
	 * Собирает информацию о совокупности рефлектограмм, достаточную
	 * для формирования эталона.
	 * see {@link TracesAverages}
	 * @param trColl входная совокупность р/г
	 *   после пред-фильтрации ({@link PFTrace})
	 * @param needNoiseInfo нужна ли информация о шуме
	 * @param needMFInfo нужны ли кривые мин./макс. фитированных кривых
	 * @param ap Параметры анализа, либо null если needMFInfo==false
	 * @return TracesAverages, в которой: <ul>
	 *   <li>поля av.avNoise, av.noiseAv заполнены, если needNoiseInfo
	 *   <li>поля maxYMF, minYMF заполнены, если needMFInfo
	 *		(в таком случае проводит анализ и фитировку каждой р/г
	 *		 и определяет maxYMF, minYMF на основе раброса этих
	 *		 фитированных кривых)
	 *   <li>все остальные поля заполнены в любом случае
	 * </ul>
	 * @throws IncompatibleTracesException если
	 *   параметры входных р/г, необходимые для проведения анализа, различаются
	 * @throws IllegalArgumentException если входная совокупность р/г пуста
	 */
	protected static TracesAverages findTracesAverages(
			Collection<PFTrace> trColl,
			boolean needNoiseInfo,
			boolean needMFInfo,
			AnalysisParameters ap)
	throws IncompatibleTracesException {
		// определяем число входных р/г
		final int N_TRACES = trColl.size();

		if (N_TRACES == 0)
			throw new IllegalArgumentException("No traces to analyse");

		// усредняем входные р/г,
		// определяем мин. длину и типичный шум.

		boolean isFirst = true;

		TracesAverages res = new TracesAverages();
		res.nTraces = N_TRACES;

		double[] noiseAcc = null; // needs no initialization

		for (Iterator<PFTrace> it = trColl.iterator(); it.hasNext(); isFirst = false) {
			TracePreAnalysis tpa =
				makePreAnalysis(it.next(), needNoiseInfo);

			if (isFirst) {
				res.av = new TracePreAnalysis(tpa);
			} else {
				res.av.setMinLength(tpa.traceLength);
				addDoubleArray(res.av.yTrace, tpa.yTrace, res.av.traceLength);
				addDoubleArray(res.av.yCorr, tpa.yCorr, res.av.traceLength);
				res.av.checkTracesCompatibility(tpa);
			}

			if (needNoiseInfo) {
				if (isFirst) {
					noiseAcc = tpa.getNoise(); // клонирование необязательно, т.к. можно усреднять прямо в этом массиве 
				} else {
					addDoubleArray(noiseAcc, tpa.getNoise(), res.av.traceLength);
				}
			}

			if (needMFInfo) {
				// проводим анализ и берем из него модельную кривую
				ModelTrace mt = makeAnalysis(tpa, ap).getModelTrace();
				double[] yMF = mt.getYArray();
				res.av.setMinLength(mt.getLength()); // возможно, что длина модельной кривой будет меньше, чем начальное tracelength
				if (isFirst) {
					// need to make one more copy, so cloning once only
					res.minYMF = yMF;
					res.maxYMF = yMF.clone();
				} else {
					// элементы массивов res.(min|max)YMF, выходящие за пределы
					// res.av.tracelength, не будут обрабатываться (там останется мусор)
					ReflectogramMath.updateMinArray(res.minYMF, yMF);
					ReflectogramMath.updateMaxArray(res.maxYMF, yMF);
				}
			}
		}

		// convert sum to average for avY
		for (int i = 0; i < res.av.traceLength; i++) {
			res.av.yTrace[i] /= N_TRACES;
			res.av.yCorr[i] /= N_TRACES;
		}

		if (needNoiseInfo) {
			// convert sum to average for avNoise
			for (int i = 0; i < res.av.traceLength; i++) {
				noiseAcc[i] /= N_TRACES;
			}

			res.av.avNoise = noiseAcc;

			// make noiseAv
			res.av.noiseAv = N_TRACES == 1
				? (double[])noiseAcc.clone() // XXX: нужно ли клонирование? если да, то, возможно, оно делается не везде где надо
				: calcNoiseArray(res.av.yTrace, res.av.traceLength);
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
	 * Выполняет анализ одной рефлектограммы
	 * @param trace пред-фильтрованная рефлектограмма
	 * @param ap параметры анализа
	 * @return результаты анализа {@link AnalysisResult}
	 */
	public static AnalysisResult performAnalysis(
			PFTrace trace,
			AnalysisParameters ap) {
		TracePreAnalysis tpa = makePreAnalysis(trace, true);
		ModelTraceAndEventsImpl mtae = makeAnalysis(tpa, ap);
		return new AnalysisResult(tpa.yTrace.length, tpa.traceLength, mtae);
	}

	/**
	 * Выполняет пре-фильтрацию, а затем анализ одной рефлектограммы
	 * XXX: пометить deprecated? используется в mcm
	 * @param bs исходная р/г
	 * @param ap параметры анализа
	 * @return результаты анализа {@link AnalysisResult}
	 */
	public static AnalysisResult performAnalysis(
			BellcoreStructure bs,
			AnalysisParameters ap) {
		TracePreAnalysis tpa = makePreAnalysis(new PFTrace(bs), true);
		ModelTraceAndEventsImpl mtae = makeAnalysis(tpa, ap);
		return new AnalysisResult(tpa.yTrace.length, tpa.traceLength, mtae);
	}

	/**
	 * Создает эталонный MTM по непустому набору рефлектограмм и параметрам
	 * анализа.
	 * <ul>
	 * <li> Если в наборе только одна р/г,
	 *   строит пороги по исходной р/г (PFTrace),
	 * <li> если несколько - по всем их mf,
	 * <li> если ни одной - бросает IllegalArgumentException
	 *      (see {@link #findTracesAverages(Collection, boolean, boolean,
	 *        AnalysisParameters)}).
	 * </ul>
	 * @todo использовать на входе Collection{Trace}
	 * вместо Collection{PFTrace}, а не проводить анализ заново
	 * @param trColl коллекция входных р/г.
	 *   Должна быть непуста, а р/г должны иметь одинаковые параметры
	 *   регистрации, используемые в анализе
	 * @param ap параметры анализа
	 * @return MTM созданного эталона
	 * @throws IllegalArgumentException если коллекция входных р/г пуста
	 *   ({@link #findTracesAverages})
	 * @throws IncompatibleTracesException если коллекция входных р/г
	 *   содержит р/г с разными длинами, разрешением, длительностью импульса
	 *   или показателем преломления ({@link #findTracesAverages})
	 */
	public static ModelTraceManager makeEtalon(
			Collection<PFTrace> trColl,
			AnalysisParameters ap)
	throws IncompatibleTracesException
	{
		// FIXME: lot of debug output
		Log.debugMessage("makeEtalon: trColl: " + trColl, Level.FINEST);
		TracesAverages av = findTracesAverages(trColl, true, true, ap);
		ModelTraceAndEventsImpl mtae = makeAnalysis(av.av, ap);
		Log.debugMessage("makeEtalon: initial MTAE: " + mtae, Level.FINEST);
		ModelTraceManager mtm = new ModelTraceManager(mtae);
		Log.debugMessage("makeEtalon: initial MTM: " + mtm, Level.FINEST);
		if (trColl.size() > 1) {
			// extend to max dev of _mf_
			Log.debugMessage("makeEtalon: av.maxYMF: " + av.maxYMF.length
					+ ":" + getDoubleArrayHash(av.maxYMF), Level.FINEST);
			Log.debugMessage("makeEtalon: av.minYMF: " + av.minYMF.length
					+ ":" + getDoubleArrayHash(av.minYMF), Level.FINEST);
			mtm.updateThreshToContain(av.maxYMF, av.minYMF, MTM_DY_MARGIN,
					MTM_DY_FACTOR_MF_BASED);
		}
		else {
			// extend to a single curve: original (noisy) _trace_
			mtm.updateThreshToContain(av.av.yCorr, av.av.yCorr, MTM_DY_MARGIN,
					MTM_DY_FACTOR_TR_BASED);
		}
		Log.debugMessage("makeEtalon: rslting MTM: " + mtm, Level.FINEST);
		return mtm;
	}

	private static int getDoubleArrayHash(double[] arr) {
		int result = 17;
		for (int i = 0; i < arr.length; i++) {
			long bits = Double.doubleToLongBits(arr[i]);
			result = 37 * result + (int)(bits ^ (bits >>> 32));
		}
		return result;
	}

	/**
	 * Расширяет эталонный MTM по непустому набору рефлектограмм и параметрам
	 * анализа, чтобы включить все аналитические кривые.
	 * <p>
	 * XXX Было бы неплохо использовать на входе Collection{Trace}
	 * вместо Collection{PFTrace}, чтобы не проводить анализ заново,
	 * однако здесь нам важно, чтобы скорректированный MTM строго покрывал
	 * а/к, построенные при <b>текущих</b> значениях параметров анализа.
	 * Поэтому использовать результаты анализа, имеющиеся в Trace, можно будет
	 * только когда Trace сможет как-то поддерживать изменяющиеся AP.
	 * </p>
	 * @param mtm расширяемый MTM
	 * @param trColl коллекция пред-фильтрованных р/г
	 * @param ap параметры, с которыми анализировать эти р/г
	 * @throws IllegalArgumentException если trColl пуст
	 *   ({@link #findTracesAverages})
	 * @throws IncompatibleTracesException если trColl содержит р/г
	 *   с разными длинами, разрешением, длительностью импульса или
	 *   показателем преломления ({@link #findTracesAverages})
	 */
	public static void updateEtalon(ModelTraceManager mtm,
			Collection<PFTrace> trColl,
			AnalysisParameters ap)
	throws IncompatibleTracesException, IllegalArgumentException {
		TracesAverages av = findTracesAverages(trColl, true, true, ap);
		// extend to max dev of _mf_
		mtm.updateThreshToContain(
				av.maxYMF, av.minYMF, MTM_DY_MARGIN, MTM_DY_FACTOR_MF_BASED);
	}

	/**
	 * Находит в коллекции рефлектограмму, ближайшую к усредненному по
	 * коллекции значению. Входные р/г должны быть совместны, т.е.
	 * иметь одни и те же режимы регистрации (разрешение, длина импульса и пр.)
	 * @param trColl непустая входная коллекция рефлектограмм
	 * @param av заранее найденное значение по этой коллекции TracesAverages
	 *   (в нем не нужны ни noiseInfo, ни MFInfo)
	 * @return самую среднюю рефлектограмму среди входных
	 */
	protected static PFTrace getMostTypicalTrace(
			Collection<PFTrace> trColl,
			TracesAverages av) {
		PFTrace nearest = null;
		double bestDistance = 0;
		for (PFTrace tr: trColl) {
			double[] y = tr.getFilteredTrace();
			double distance = ReflectogramComparer.getMaxDeviation(av.av.yCorr,
					y,
					av.av.traceLength);
			if (nearest == null || distance < bestDistance) {
				nearest = tr;
				bestDistance = distance;
			}
		}
		return nearest;
	}

	/**
	 * Находит в коллекции рефлектограмму, ближайшую к усредненному по
	 * коллекции значению. Входные р/г должны быть совместны, т.е.
	 * иметь одни и те же режимы регистрации (разрешение, длина импульса и пр.)
	 * @param trColl входная коллекция пред-фильтрованных рефлектограмм
	 * @return самую среднюю рефлектограмму среди входных
	 * @throws IncompatibleTracesException Если входные рефлектограммы
	 * несовместны
	 * @throws IllegalArgumentException Если входная совокупность р/г пуста
	 */
	public static PFTrace getMostTypicalTrace(
			Collection<PFTrace> trColl)
	throws IncompatibleTracesException {
		// если входная коллекция пуста, то к этому моменту уже будет
		// выброшено исключение IllegalArgumentException,
		// т.ч. return null не произойдет.
		TracesAverages av = findTracesAverages(trColl, false, false, null);
		return getMostTypicalTrace(trColl, av);
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
	public static double getMedian(double[] y, int iFrom, int iToEx,
			double ratio)
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
	 * See specification of {@link #nCalcTraceLength(double[])}
	 * @param y Рефлектограмма
	 * @return длина до конца волокна
	 */
	protected static int calcTraceLength(double[] y)
	{
		return nCalcTraceLength(y);
	}

	/**
	 * Используется в MTM для создания порогов.
	 * See specification of {@link #nExtendThreshToCoverCurve(double[],
	 *   double[], ThreshDX[], ThreshDY[], int, int, double)}
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
	 * Проводит анализ рефлектограммы вызовом
	 *   {@link #performAnalysis(PFTrace, AnalysisParameters)},
	 * затем сравнивает ее с помощью
	 *   {@link #compareAndMakeAlarms(AnalysisResult, Etalon)}
	 * @param trace пред-фильтрованная {@link PFTrace} рефлектограмма
	 * @param ap параметры анализа
	 * @param breakThresh
	 * @param etMTM
	 * @param anchorer
	 * @return список алармов
	 */
	@Deprecated
	public static List analyseCompareAndMakeAlarms(PFTrace trace,
			AnalysisParameters ap,
			double breakThresh,
			ModelTraceManager etMTM,
			EventAnchorer anchorer) {
		AnalysisResult ar = performAnalysis(trace, ap);
		Etalon etalon = new Etalon(etMTM, breakThresh, anchorer);
		return compareAndMakeAlarms(ar, etalon);
	}

	/**
	 * Сравнивает результаты анализа с порогом обнаружения
	 * обрыва и эталонным MTM, формирует список алармов.
	 * Текущая версия возвращает 0 или 1 алармов.
	 * <p>
	 * Кроме того, если в эталоне определен EventAnchorer,
	 * а при сравнении не обнаружено обрыва,
	 * результаты анализа дополняются привязкой EventAnchorer на основе эталона.
	 * <p>
	 * Реализован через {@link #compareToEtalon(AnalysisResult, Etalon)}
	 * путем отбрасывания параметров качества.
	 * <p>
	 * XXX: нужен ли? На пол-пути к состоянию deprecated
	 * @param ar Результаты анализа
	 * @param etalon параметры эталона
	 * @return список полученных несоответствий
	 */
	public static List<ReflectogramMismatchImpl> compareAndMakeAlarms(
			AnalysisResult ar,
			Etalon etalon) {
		return compareToEtalon(ar, etalon).getAlarms();
	}

	/**
	 * Сравнивает результаты анализа с эталоном, формируя все результаты
	 * сравнения в виде {@link EtalonComparison}.
	 * @param ar Результаты анализа
	 * @param etalon параметры эталона
	 * @return результат сравнения {@link EtalonComparison}
	 */
	public static EtalonComparison compareToEtalon(
			AnalysisResult ar,
			Etalon etalon) {
		// формируем выходной список
		final List<ReflectogramMismatchImpl> alarmList =
			new ArrayList<ReflectogramMismatchImpl>();

		// получаем параметры эталона
		ModelTraceManager etMTM = etalon.getMTM();
		double breakThresh = etalon.getMinTraceLevel();

		final ModelTraceAndEventsImpl mtae = ar.getMTAE();
		ModelTrace mt = mtae.getModelTrace();

		// начало конца волокна по эталону
		int etMinLength = etMTM.getMTAE().getNEvents() > 0
				? etMTM.getMTAE().getSimpleEvent(
						etMTM.getMTAE().getNEvents() - 1).getBegin()
				: 0; // если в эталоне нет событий, считаем его длину нулевой

		// НЕ добавляем к результатам анализа найденную длину р/г и фитированную кривую - это пока не нужно
//	  outParameters.put(CODENAME_DADARA_TRACELENGTH, ByteArray.toByteArray(traceLength));
//	  outParameters.put(CODENAME_DARARA_MODELFUNCTION, mf.toByteArray());

		// пытаемся обнаружить обрыв волокна:
		// (1) на участке (x < traceLength) - по уходу м.ф. ниже breakThresh
		// XXX - надо ли было предварительно смещать р/г по вертикали?
		int breakPos = ModelTraceComparer.compareToMinLevel(mt, breakThresh);
		// (2) на участке (x >= traceLength) - не ушли ли в шум до начала EOT ?

		// FIXME: пока что сравнивать придется с длиной mt, а не длиной исходной кривой,
		//    чтобы дистанция обрыва определялась верно хотя бы на коннекторах
//		if (breakPos < 0 && ar.getTraceLength() < etMinLength)
//		breakPos = ar.getTraceLength();
		if (breakPos < 0 && mt.getLength() < etMinLength) {
			// устанавливаем breakPos в начала конца волокна
			final int nEvents = mtae.getNEvents();
			breakPos = nEvents > 0
				? mtae.getSimpleEvent(nEvents - 1).getBegin() : 0;
		}

		// [fixed] проблема - breakPos случится при первом же уходе ниже minTraceLevel,
		// что очень вероятно на последних километрах абс. нормальной р/г
		// при работе на пределе динамического дипазона (see traces #38, #65)
		// Решено предварительной фильтрацией [/fixed]
		if (breakPos >= 0 && breakPos < etMinLength) // если был обнаружен обрыв до начала EOT
		{
			ReflectogramMismatchImpl alarm = new ReflectogramMismatchImpl();
			alarm.setSeverity(SEVERITY_HARD);
			alarm.setAlarmType(TYPE_LINEBREAK);
			alarm.setCoord(etMTM.fixAlarmPos(breakPos, false)); // корректируем с учетом событий
			alarm.setDeltaX(etMTM.getMTAE().getDeltaX());
			// конечная дистанция аларма := конец эталонной р/г (но не более длины р/г)
			alarm.setEndCoord(Math.min(ar.getDataLength(), etMinLength));

			// устанавливаем привязку аларма к событиям
			ModelTraceComparer.setAlarmAnchors(alarm, etalon);

			// XXX - если на обрыве есть заметное отражение, то дистанция будет завышена
			// мб, в таком случае не надо игнорировать HARD алармы?

			alarmList.add(alarm);

			final QualityComparer qcomp =
				new QualityComparer(mtae, etMTM, null, true);
			return new EtalonComparison() {
				public List<ReflectogramMismatchImpl> getAlarms() {
					return alarmList;
				}
				public ReflectometryEvaluationOverallResult getOverallResult() {
					return qcomp;
				}
				public EvaluationPerEventResult getPerEventResult() {
					return qcomp;
				}
			};
		}
		else // обрыв не обнаружен
		{
			ReliabilitySimpleReflectogramEvent[] arEvents =
				mtae.getSimpleEvents();
			ReliabilitySimpleReflectogramEvent[] etEvents =
				etMTM.getMTAE().getSimpleEvents();
			SimpleReflectogramEventComparer rcomp =
				new SimpleReflectogramEventComparer(arEvents, etEvents);
			ReflectogramMismatchImpl alarm =
					ModelTraceComparer.compareMTAEToMTM(mtae,
							etMTM, rcomp);

			// обеспечиваем EventAnchorer-привязку результатов анализа
			ModelTraceComparer.createEventAnchor(ar, etalon, rcomp);

			// устанавливаем привязку аларма к событиям
			if (alarm != null) {
				ModelTraceComparer.setAlarmAnchors(alarm, etalon);
			}

			if (alarm != null) {
				alarmList.add(alarm);
			}

			final QualityComparer qcomp =
				new QualityComparer(mtae, etMTM, rcomp, false);
			return new EtalonComparison() {
				public List<ReflectogramMismatchImpl> getAlarms() {
					return alarmList;
				}
				public ReflectometryEvaluationOverallResult getOverallResult() {
					return qcomp;
				}
				public EvaluationPerEventResult getPerEventResult() {
					return qcomp;
				}
			};
		}
	}
}
