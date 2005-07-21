/*-
 * $Id: Trace.java,v 1.5 2005/07/21 12:50:00 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.Analysis;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.SimpleApplicationException;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEventsImpl;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreStructure;

/**
 * Представляет загруженную рефлектограмму.
 * Агрегирует:
 * <ul>
 * <li> BellcoreStructure bs - собственно рефлектограмма
 * <li> double[] traceData - кэш bs.getTraceData() (полагаемся, что никто не будет изменять этот массив) 
 * <li> MTAE - результаты первого анализа (для отображения а/к на экране; для primarytrace может отсутствовать)
 *   (mtae может быть вычислен в любой момент от загрузки Trace до запроса
 *   getMTAE, в соответствии с любыми действующими на этом периоде времени
 *   парамерами анализа; может также быть загружен, если такой анализ был
 *   проведен на агенте)
 * <li> Object key - идентификатор для проверки уникальности, это может быть
 *   <ul>
 *   <li> String абсолютный путь - для файла
 *   <li> String measurementId - для р/г из БД
 *   </ul>
 * <li> Result (null, если это локальный файл) - по нему можно определить шаблон, с которым была снята р/г
 * @author $Author: saa $
 * @version $Revision: 1.5 $, $Date: 2005/07/21 12:50:00 $
 * @module
 */
public class Trace {
	private BellcoreStructure bs;
	private AnalysisParameters ap; // будет использоваться для построения mtae
	private String key;
	private Result result; // may be null

	private double[] traceData = null; // null if not cached yet
	private ModelTraceAndEventsImpl mtae = null;

	/**
	 * @param result
	 * @param ap
	 * @throws SimpleApplicationException если попытались открыть
	 * результат, не содержащий рефлектограмму
	 */
	public Trace(Result result, AnalysisParameters ap)
	throws SimpleApplicationException {
		this.bs = AnalysisUtil.getBellcoreStructureFromResult(result);
		this.ap = ap;
		this.key = result.getId().getIdentifierString();
		this.result = result;
		// @todo - автоматически загружать и mtae, если имеются результаты анализа
	}
	public Trace(BellcoreStructure bs, String key, AnalysisParameters ap) {
		this.bs = bs;
		this.ap = ap;
		this.key = key;
		this.result = null;
	}
	public ModelTraceAndEventsImpl getMTAE() {
		if (mtae == null) {
			// XXX: пользоваться traceData, если она есть
			// XXX: имеет ли смысл запоминать весь AnalysisResult, а не только MTAE?
			mtae = CoreAnalysisManager.performAnalysis(bs, ap).getMTAE();
		}
		return mtae;
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
