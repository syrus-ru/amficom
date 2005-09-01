/*-
 * $Id: Trace.java,v 1.9 2005/09/01 12:08:33 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.DataFormatException;

/**
 * Представляет загруженную рефлектограмму.
 * Агрегирует:
 * <ul>
 * <li> BellcoreStructure bs - собственно рефлектограмма
 * <li> double[] traceData - кэш bs.getTraceData() (полагаемся, что никто не будет изменять этот массив) 
 * <li> ar - результаты первого анализа (для отображения а/к на экране; для primarytrace может отсутствовать)
 *   (ar может быть вычислен в любой момент от загрузки Trace до запросов
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
 * @version $Revision: 1.9 $, $Date: 2005/09/01 12:08:33 $
 * @module
 */
public class Trace {
	private BellcoreStructure bs;
	private AnalysisParameters ap; // может использоваться для построения mtae
	private String key;
	private Result result; // may be null

	private double[] traceData = null; // null if not cached yet
	private AnalysisResult ar = null;

	public Trace(BellcoreStructure bs, String key, AnalysisParameters ap) {
		this.bs = bs;
		this.ap = ap;
		this.key = key;
		this.result = null;
	}
	/**
	 * one of ap and mtae may be null
	 */
	private Trace(Result result,
			AnalysisParameters ap, AnalysisResult ar)
	throws SimpleApplicationException {
		this.bs = AnalysisUtil.getBellcoreStructureFromResult(result);
		this.ap = ap;
		this.key = result.getId().getIdentifierString();
		this.result = result;
		this.ar = ar;
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
	 * Создает Trace на основе результата измерения.
	 * <p>
	 * Определяет, есть ли у измерения, создавшего данный результат,
	 * результат анализа.
	 * <ul>
	 * <li> Если результат анализа есть, то создает Trace c этим результатом.
	 * <li> Если результата анализа нет, то создает Trace с проведением анализа
	 *      и использованием указанных AnalysisParameters.
	 * </ul>
	 * @param result результат измерения, содержащий загружаемую рефлектограмму
	 * @param ap параметры анализа, которые будут использованы в случае,
	 *   если у измерения нет готовых результатов анализа
	 * @return Trace
	 * @throws ApplicationException ошибка работы с pool'ом или сервером
	 * @throws DataFormatException нарушение целостности загружаемых данных
	 * @throws SimpleApplicationException попытались открыть
	 *   результат, не содержащий рефлектограмму
	 */
	public static Trace getTraceWithARIfPossible(Result result,
			AnalysisParameters ap)
	throws DataFormatException, ApplicationException, SimpleApplicationException {
		AnalysisResult ar =
			AnalysisUtil.getAnalysisResultForResultIfPresent(result);
		if (ar != null)
			return new Trace(result, ar);
		else
			return new Trace(result, ap);
	}

	public AnalysisResult getAR() {
		if (ar == null) {
			ar = CoreAnalysisManager.performAnalysis(bs, ap);
		}
		return ar;
	}

	public ModelTraceAndEventsImpl getMTAE() {
		return getAR().getMTAE();
	}
	/**
	 * Возвращается всегда один и тот же объект,
	 * и его можно будет сравнивать по '=='.
	 * (это нужно для выбора mostTypical Trace по mostTypical BS)
	 * @return объект BellcoreStructure, один и тот же при повторных вызовах
	 */
	public BellcoreStructure getBS() {
		return bs;
	}
	public double[] getTraceData() {
		if (traceData == null) {
			traceData = bs.getTraceData();
		}
		return traceData;
	}
	public String getKey() {
		return key;
	}
	public double getDeltaX() {
		return this.bs.getResolution();
	}
	public Result getResult() {
		return result;
	}
}
