/*
 * $Id: DadaraAnalysisManager.java,v 1.67 2005/09/14 18:13:47 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.67 $, $Date: 2005/09/14 18:13:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
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
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.DataFormatException;
import com.syrus.util.Log;

final class DadaraAnalysisManager implements AnalysisManager {
	private final Map<ParameterType, byte[]> tracePars;
	private final Map<ParameterType, byte[]> criteriaPars;
	private final Map<ParameterType, byte[]> etalonPars;

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
		this.tracePars = new HashMap<ParameterType, byte[]>();
		this.criteriaPars = new HashMap<ParameterType, byte[]>();
		this.etalonPars = new HashMap<ParameterType, byte[]>();
		this.addSetParameters(this.tracePars, measurementResult.getParameters());
		this.addSetParameters(this.criteriaPars, analysis.getCriteriaSet().getParameters());
		if (etalon != null) {
			this.addSetParameters(this.etalonPars, etalon.getParameters());
		}
	}

	private void addSetParameters(final Map<ParameterType, byte[]> parsMap, final Parameter[] setParameters) throws AnalysisException {
		for (int i = 0; i < setParameters.length; i++) {
			this.addParameter(parsMap, setParameters[i]);
		}
	}

	private void addParameter(final Map<ParameterType, byte[]> parsMap, final Parameter parameter) throws AnalysisException {
		final ParameterType parameterType = parameter.getType();
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

	private boolean hasParameter(final Map<ParameterType, byte[]> parsMap, final ParameterType parameterType) {
		return parsMap.get(parameterType) != null;
	}

	private byte[] getParameter(final Map<ParameterType, byte[]> parsMap, final ParameterType parameterType)
			throws AnalysisException {
		byte[] rawData = parsMap.get(parameterType);
		if (rawData == null) {
			throw new AnalysisException("Cannot get parameter of codename '" + parameterType + "'");
		}
		return rawData;
	}

	// возвращает null, если эталона нет.
	private Etalon obtainEtalon() throws AnalysisException {
		if (! this.hasParameter(this.etalonPars, ParameterType.DADARA_ETALON)) {
			return null;
		}
		byte[] etalonData = this.getParameter(this.etalonPars, ParameterType.DADARA_ETALON);
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
		final byte[] bar = this.getParameter(this.criteriaPars, ParameterType.DADARA_CRITERIA);
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
		final Map<ParameterType, byte[]> outParameters = new HashMap<ParameterType, byte[]>(); // Map<String codename, byte[] rawData>
		
		// === Получаем входные данные ===

		// Получаем рефлектограмму
		final BellcoreStructure bs = (new BellcoreReader()).getData(this.getParameter(this.tracePars, ParameterType.REFLECTOGRAMMA));

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
		outParameters.put(ParameterType.DADARA_ANALYSIS_RESULT, ar.toByteArray());

		// === Формируем результаты ===

		// если эталон есть, то добавляем алармы в результаты анализа
		if (etalon != null) {
			final ReflectogramMismatch[] alarms = alarmList.toArray(new ReflectogramMismatch[alarmList.size()]);
			outParameters.put(ParameterType.DADARA_ALARMS, ReflectogramMismatch.alarmsToByteArray(alarms));
		}

		// формируем результаты анализа
		final Parameter[] ret = new Parameter[outParameters.size()];
		int i = 0;
		for (final ParameterType parameterType : outParameters.keySet()) {
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
