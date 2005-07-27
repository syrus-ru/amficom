/*-
 * $Id: Trace.java,v 1.7 2005/07/27 07:41:29 saa Exp $
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
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreStructure;

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
 * @version $Revision: 1.7 $, $Date: 2005/07/27 07:41:29 $
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
	 * @param result результат измерения
	 * @param ar результат анализа
	 * @throws SimpleApplicationException если попытались открыть
	 * результат, не содержащую рефлектограмму 
	 */
	public Trace(Result result, AnalysisResult ar)
	throws SimpleApplicationException {
		this(result, null, ar);
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
