/*-
 * $Id: Trace.java,v 1.15 2005/12/21 14:47:04 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import java.util.logging.Level;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.PFTrace;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;

/**
 * Представляет загруженную рефлектограмму.
 * Агрегирует:
 * <ul>
 * <li> {@link PFTrace} pfTrace - собственно рефлектограмма, но уже пред-фильтрованная
 * <li> double[] traceData - кэш pfTrace.getFilteredTrace() (полагаемся, что никто не будет изменять этот массив) 
 * <li> ar - результаты первого анализа (для отображения а/к на экране; для primarytrace может отсутствовать)
 *   (ar может быть вычислен в любой момент от загрузки {@link Trace} до запросов
 *   getAR/getMTAE, в соответствии с любыми действующими на этом периоде времени
 *   парамерами анализа; может также быть загружен, если такой анализ был
 *   проведен на агенте)
 * <li> Object key - идентификатор для проверки уникальности, это может быть
 *   <ul>
 *   <li> String абсолютный путь - для файла
 *   <li> String measurementId - для р/г из БД
 *   </ul>
 * <li> Result (null, если это локальный файл) - по нему можно определить шаблон, с которым была снята р/г
 * @author $Author: saa $
 * @version $Revision: 1.15 $, $Date: 2005/12/21 14:47:04 $
 * @module
 */
public class Trace {
	private PFTrace pfTrace;
	private AnalysisParameters ap; // может использоваться для построения ar
	private String key;
	private Result result; // may be null

	private double[] traceData = null; // null if not cached yet
	private AnalysisResult ar = null;

	private boolean analysisLoaded; // true если результат анализа задан извне

	public Trace(BellcoreStructure bs, String key, AnalysisParameters ap) {
		assert(bs != null);
		assert(ap != null);
		assert(key != null);
		this.pfTrace = new PFTrace(bs);
		this.ap = ap;
		this.key = key;
		this.result = null;
		this.analysisLoaded = false;
	}

	public Trace(PFTrace trace, String key, AnalysisParameters ap) {
		assert(trace != null);
		assert(ap != null);
		assert(key != null);
		this.pfTrace = trace;
		this.ap = ap;
		this.key = key;
		this.result = null;
		this.analysisLoaded = false;
	}

	public Trace(PFTrace trace, String key, AnalysisResult ar) {
		assert(trace != null);
		assert(ar != null);
		assert(key != null);
		this.pfTrace = trace;
		this.key = key;
		this.result = null;
		this.ar = ar;
		this.analysisLoaded = true;
	}

	/**
	 * one of ap and mtae may be null
	 */
	private Trace(Result result,
			AnalysisParameters ap, AnalysisResult ar)
	throws SimpleApplicationException {
		assert(result != null);
		assert(ap != null || ar != null);
		this.pfTrace = new PFTrace(
				AnalysisUtil.getBellcoreStructureFromResult(result));
		this.ap = ap;
		this.key = result.getId().getIdentifierString();
		this.result = result;
		this.ar = ar;
		this.analysisLoaded = ar != null;
	}

	/**
	 * Открывает рефлектограмму без предварительно полученных результатов анализа
	 * XXX try to use getTraceWithARIfPossible instead
	 * @param result результат измерения
	 * @param ap параметры анализа (будут использованы в момент getMTAE())
	 * @throws SimpleApplicationException если попытались открыть
	 * результат, не содержащий рефлектограмму
	 */
	public Trace(Result result, AnalysisParameters ap)
	throws SimpleApplicationException {
		this(result, ap, null);
	}

	/**
	 * Открывает рефлектограмму с предварительно полученными результатами анализа
	 * XXX try to use getTraceWithARIfPossible instead
	 * @param result результат измерения
	 * @param ar результат анализа
	 * @throws SimpleApplicationException если попытались открыть
	 * результат, не содержащую рефлектограмму 
	 */
	public Trace(Result result, AnalysisResult ar)
	throws SimpleApplicationException {
		this(result, null, ar);
	}

	/**
	 * Создает {@link Trace} на основе результата измерения.
	 * <p>
	 * Сначала определяет, есть ли у измерения, создавшего данный результат,
	 * результат анализа.
	 * <ul>
	 * <li> Если результат анализа есть, то создает {@link Trace} c этим результатом.
	 * <li> Если результата анализа нет, но у соответствующего
	 * шаблона измерения есть параметры анализа,
	 * то загружает эти параметры анализа и создает {@link Trace}
	 * с анализом по загруженным параметрам.
	 * <li> Если нет ни результата анализа, ни параметров анализа,
	 * то загружает эти параметры анализа и создает {@link Trace}
	 * с анализом по данным на входе параметрам.
	 * </ul>
	 * @param result результат измерения, содержащий загружаемую рефлектограмму
	 * @param ap параметры анализа, которые будут использованы в случае,
	 *   если у измерения нет готовых результатов анализа
	 * @return {@link Trace}
	 * @throws ApplicationException ошибка работы с pool'ом или сервером
	 * @throws DataFormatException нарушение целостности загружаемых данных
	 * @throws SimpleApplicationException попытались открыть
	 *   результат, не содержащий рефлектограмму
	 */
	public static Trace getTraceWithARIfPossible(final Result result,
			final AnalysisParameters ap)
	throws DataFormatException, ApplicationException, SimpleApplicationException {
		final AnalysisResult ar =
			AnalysisUtil.getAnalysisResultForResultIfPresent(result);
		if (ar != null) {
			Log.debugMessage("Created Trace() on result and loaded analysisResult", Level.FINER);
			return new Trace(result, ar);
		} else {
			if (AnalysisUtil.hasMeasurementByResult(result)) {
				final Measurement m = AnalysisUtil.getMeasurementByResult(result);
				final AnalysisParameters criteriaSet =
					AnalysisUtil.getCriteriaSetByMeasurementSetup(m.getSetup());
				if (criteriaSet != null) {
					Log.debugMessage("Created Trace() on result and loaded criteriaSet", Level.FINER);
					return new Trace(result, criteriaSet);
				}
			}
			Log.debugMessage("Created Trace() on result and default ap", Level.FINER);
			return new Trace(result, ap);
		}
	}

	/**
	 * Возвращает результат анализа.
	 * Если анализ уже был загружен либо проведен, то возвращает его результат.
	 * Если анализа еще не было, то выполняет анализ с параметрами,
	 * определенными при создании this.
	 * @return результат анализа.
	 */
	public AnalysisResult getAR() {
		if (this.ar == null) {
			this.ar = CoreAnalysisManager.performAnalysis(this.pfTrace, this.ap);
		}
		return this.ar;
	}

	public ModelTraceAndEventsImpl getMTAE() {
		return getAR().getMTAE();
	}
	/**
	 * Возвращается всегда один и тот же объект,
	 * и его можно будет сравнивать по '=='.
	 * (это нужно для выбора mostTypical {@link Trace} по mostTypical PFTrace)
	 * @return объект PFTrace, один и тот же при повторных вызовах
	 */
	public PFTrace getPFTrace() {
		return this.pfTrace;
	}
	public double[] getTraceData() {
		if (this.traceData == null) {
			this.traceData = this.pfTrace.getFilteredTraceClone();
		}
		return this.traceData;
	}
	public String getKey() {
		return this.key;
	}
	public double getDeltaX() {
		return this.pfTrace.getResolution();
	}
	public Result getResult() {
		return this.result;
	}
	/**
	 * @return true, если результаты анализа были заданы в момент создания
	 * этого объекта. Фактически это обычно означает,
	 * что анализ был проведен на агенте.
	 */
	public boolean hasAnalysisLoaded() {
		return this.analysisLoaded;
	}
}
