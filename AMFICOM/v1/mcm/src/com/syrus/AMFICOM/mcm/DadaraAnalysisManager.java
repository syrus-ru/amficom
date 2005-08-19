/*
 * $Id: DadaraAnalysisManager.java,v 1.64 2005/08/19 14:21:42 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.64 $, $Date: 2005/08/19 14:21:42 $
 * @author $Author: arseniy $
 * @module mcm
 */

//*
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ParameterTypeEnum;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

final class DadaraAnalysisManager implements AnalysisManager {
	private final Map<ParameterTypeEnum, byte[]> tracePars;
	private final Map<ParameterTypeEnum, byte[]> criteriaPars;
	private final Map<ParameterTypeEnum, byte[]> etalonPars;

	/**
	 * This constructor is called only by Reflection API
	 * @param measurementResult
	 * @param analysis (not null)
	 * @param etalon may be null
	 * @throws AnalysisException
	 */
	@SuppressWarnings("unused")
	private DadaraAnalysisManager(final Result measurementResult,
			final Analysis analysis,
			final ParameterSet etalon) throws AnalysisException {
		this.tracePars = new HashMap<ParameterTypeEnum, byte[]>();
		this.criteriaPars = new HashMap<ParameterTypeEnum, byte[]>();
		this.etalonPars = new HashMap<ParameterTypeEnum, byte[]>();
		this.addSetParameters(this.tracePars, measurementResult.getParameters());
		this.addSetParameters(this.criteriaPars, analysis.getCriteriaSet().getParameters());
		if (etalon != null) {
			this.addSetParameters(this.etalonPars, etalon.getParameters());
		}
	}

	private void addSetParameters(final Map<ParameterTypeEnum, byte[]> parsMap, final Parameter[] setParameters) throws AnalysisException {
		for (int i = 0; i < setParameters.length; i++) {
			this.addParameter(parsMap, setParameters[i]);
		}
	}

	private void addParameter(final Map<ParameterTypeEnum, byte[]> parsMap, final Parameter parameter) throws AnalysisException {
		final ParameterTypeEnum parameterType = parameter.getType();
		if (parameterType != null) {
			if (! parsMap.containsKey(parameterType)) {
				parsMap.put(parameterType, parameter.getValue());
			}
			else {
				throw new AnalysisException("Parameter of codename '" + parameterType + "' already loaded");
			}
		}
		else {
			throw new AnalysisException("Codename of parameter: '" + parameter.getId() + "' is NULL");
		}
	}

	private boolean hasParameter(final Map<ParameterTypeEnum, byte[]> parsMap, final ParameterTypeEnum parameterType) {
		return parsMap.get(parameterType) != null;
	}

	private byte[] getParameter(final Map<ParameterTypeEnum, byte[]> parsMap, final ParameterTypeEnum parameterType)
			throws AnalysisException {
		byte[] rawData = parsMap.get(parameterType);
		if (rawData == null) {
			throw new AnalysisException("Cannot get parameter of codename '" + parameterType + "'");
		}
		return rawData;
	}

	// возвращает null, если эталона нет.
	private Etalon obtainEtalon() throws AnalysisException {
		if (! this.hasParameter(this.etalonPars, ParameterTypeEnum.DADARA_ETALON)) {
			return null;
		}
		byte[] etalonData = this.getParameter(this.etalonPars, ParameterTypeEnum.DADARA_ETALON);
		try {
			Etalon et = (Etalon) DataStreamableUtil.
			readDataStreamableFromBA(etalonData, Etalon.getDSReader());
			return et;
		}
		catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}

	// will not return null
	private AnalysisParameters obtainAnalysisParameters() throws AnalysisException {
		final byte[] bar = this.getParameter(this.criteriaPars, ParameterTypeEnum.DADARA_CRITERIA);
		try {
			return (AnalysisParameters) DataStreamableUtil.readDataStreamableFromBA(bar, AnalysisParameters.getReader());
		}
		catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}

	public Parameter[] analyse() throws AnalysisException {
		Log.debugMessage("DadaraAnalysisManager.analyse | entered", Log.DEBUGLEVEL07);

		// output parameters (not Parameter[] yet)
		final Map<ParameterTypeEnum, byte[]> outParameters = new HashMap<ParameterTypeEnum, byte[]>(); // Map<String codename, byte[] rawData>
		
		// === Получаем входные данные ===

		// Получаем рефлектограмму
		final BellcoreStructure bs = (new BellcoreReader()).getData(this.getParameter(this.tracePars, ParameterTypeEnum.REFLECTOGRAMMA));

		// Получаем параметры анализа
		final AnalysisParameters ap = this.obtainAnalysisParameters();

		// Получаем эталон (может быть null, тогда сравнение не проводим)
		final Etalon etalon = obtainEtalon();

		Log.debugMessage("DadaraAnalysisManager.analyse | bs = " + bs, Log.DEBUGLEVEL08);
		Log.debugMessage("DadaraAnalysisManager.analyse | ap = " + ap, Log.DEBUGLEVEL08);
		Log.debugMessage("DadaraAnalysisManager.analyse | etalon = " + etalon, Log.DEBUGLEVEL08);

		// === Обрабатываем входные данные, анализируем, сравниваем ===
		Log.debugMessage("DadaraAnalysisManager.analyse | starting analysis", Log.DEBUGLEVEL07);

		// проводим анализ
		final AnalysisResult ar = CoreAnalysisManager.performAnalysis(bs, ap);

		// если есть эталон, то сравниваем:
		// дополняем ar результатами сравнения и получаем алармы
		final List<ReflectogramMismatch> alarmList = (etalon != null) ? CoreAnalysisManager.compareAndMakeAlarms(ar, etalon) : null;
		Log.debugMessage("DadaraAnalysisManager.analyse | alarmList = " + alarmList, Log.DEBUGLEVEL08);

		// добавляем AnalysisResult в результаты анализа
		outParameters.put(ParameterTypeEnum.DADARA_ANALYSIS_RESULT, ar.toByteArray());

		// === Формируем результаты ===

		// если эталон есть, то добавляем алармы в результаты анализа
		if (etalon != null) {
			final ReflectogramMismatch[] alarms = alarmList.toArray(new ReflectogramMismatch[alarmList.size()]);
			outParameters.put(ParameterTypeEnum.DADARA_ALARMS, ReflectogramMismatch.alarmsToByteArray(alarms));
		}

		// формируем результаты анализа
		final Parameter[] ret = new Parameter[outParameters.size()];
		int i = 0;
		for (final ParameterTypeEnum parameterType : outParameters.keySet()) {
			try {
				ret[i++] = Parameter.createInstance(parameterType, outParameters.get(parameterType));
			} catch (CreateObjectException coe) {
				throw new AnalysisException("Cannot create parameter -- " + coe.getMessage(), coe);
			}
		}

		Log.debugMessage("DadaraAnalysisManager.analyse | done, returning Parameter[" + ret.length + "]", Log.DEBUGLEVEL07);
		return ret;
	}
}
