/*
 * $Id: DadaraAnalysisManager.java,v 1.51 2005/06/24 16:10:48 saa Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.51 $, $Date: 2005/06/24 16:10:48 $
 * @author $Author: saa $
 * @module mcm_v1
 */

//*
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DataFormatException;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramAlarm;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodenames;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.Log;

public class DadaraAnalysisManager implements AnalysisManager {
	// input SetParameters codenames
	public static final String CODENAME_REFLECTOGRAMMA = ParameterTypeCodenames.REFLECTOGRAMMA;
	public static final String CODENAME_DADARA_ETALON = ParameterTypeCodenames.DADARA_ETALON;
	public static final String CODENAME_DADARA_CRITERIA = ParameterTypeCodenames.DADARA_CRITERIA;

	// output SetParameters codenames
	public static final String CODENAME_ALARMS = ParameterTypeCodenames.DADARA_ALARMS;
	public static final String CODENAME_ANALYSIS_RESULT = ParameterTypeCodenames.DADARA_ANALYSIS_RESULT;

	private static final Map OUT_PARAMETER_TYPE_IDS_MAP;	//Map <String parameterTypeCodename, Identifier parameterTypeId>

	private final Map tracePars;	//Map <String codename, Parameter parameter>
	private final Map criteriaPars;	//Map <String codename, Parameter parameter>
	private final Map etalonPars;	//Map <String codename, Parameter parameter>

	static {
		OUT_PARAMETER_TYPE_IDS_MAP = new HashMap();
		addParameterTypeIds(new String[] {CODENAME_ALARMS});
	}

	private static void addParameterTypeIds(final String[] codenames) {
		assert codenames != null : ErrorMessages.NON_NULL_EXPECTED;
		assert codenames.length > 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		final java.util.Set typicalConditions = new HashSet(codenames.length);
		for (int i = 0; i < codenames.length; i++) {
			typicalConditions.add(new TypicalCondition(codenames[i],
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETER_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME));
		}

		try {
			final StorableObjectCondition condition;
			if (typicalConditions.size() == 1)
				condition = (StorableObjectCondition) typicalConditions.iterator().next();
			else
				condition = new CompoundCondition(typicalConditions, CompoundConditionSort.OR);
			final java.util.Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true, true);
			for (final Iterator it = parameterTypes.iterator(); it.hasNext();) {
				final ParameterType parameterType = (ParameterType) it.next();
				OUT_PARAMETER_TYPE_IDS_MAP.put(parameterType.getCodename(), parameterType.getId());
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	/**
	 * This constructor is called only by Reflection API
	 * @param measurementResult
	 * @param analysis
	 * @param etalon
	 * @throws AnalysisException
	 */
	private DadaraAnalysisManager(final Result measurementResult,
			final Analysis analysis,
			final ParameterSet etalon) throws AnalysisException {
		this.tracePars = new HashMap();
		this.criteriaPars = new HashMap();
		this.etalonPars = new HashMap();
		this.addSetParameters(this.tracePars, measurementResult.getParameters());
		this.addSetParameters(this.criteriaPars, analysis.getCriteriaSet().getParameters());
		this.addSetParameters(this.etalonPars, etalon.getParameters());
	}

	private void addSetParameters(final Map parsMap, final Parameter[] setParameters) throws AnalysisException {
		for (int i = 0; i < setParameters.length; i++)
			this.addParameter(parsMap, setParameters[i]);
	}

	private void addParameter(final Map parsMap, final Parameter parameter) throws AnalysisException {
		String codename = parameter.getType().getCodename();
		if (codename != null) {
			if (! parsMap.containsKey(codename))
				parsMap.put(codename, parameter.getValue());
			else
				throw new AnalysisException("Parameter of codename '" + codename + "' already loaded");
		}
		else
			throw new AnalysisException("Codename of parameter: '" + parameter.getId() + "' is NULL");
	}

	private boolean hasParameter(final Map parsMap, final String codename) {
		return parsMap.get(codename) != null;
	}

	private byte[] getParameter(final Map parsMap, final String codename) throws AnalysisException {
		byte[] rawData = (byte[]) parsMap.get(codename);
		if (rawData == null)
			throw new AnalysisException("Cannot get parameter of codename '" + codename + "'");
		return rawData;
	}

	private Etalon obtainEtalon() throws AnalysisException {
		byte[] etalonData = this.getParameter(this.etalonPars, CODENAME_DADARA_ETALON);
		try {
			Etalon et = (Etalon) DataStreamableUtil.
			readDataStreamableFromBA(etalonData, Etalon.getDSReader());
			return et;
		}
		catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}

	// may return null if no such parameter
	private AnalysisParameters obtainAnalysisParameters() throws AnalysisException {
		if (!hasParameter(this.criteriaPars, CODENAME_DADARA_CRITERIA))
			return null;
		byte[] bar = this.getParameter(this.criteriaPars, CODENAME_DADARA_CRITERIA);
		try {
			return (AnalysisParameters) DataStreamableUtil.readDataStreamableFromBA(bar, AnalysisParameters.getReader());
		}
		catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}

	public Parameter[] analyse() throws AnalysisException {
		// output parameters (not Parameter[] yet)
		Map outParameters = new HashMap(); // Map <String codename, byte[] rawData>
		
		// === Получаем входные данные ===

		// Получаем рефлектограмму
		BellcoreStructure bs = (new BellcoreReader()).getData(this.getParameter(this.tracePars, CODENAME_REFLECTOGRAMMA));

		// Получаем параметры анализа (могут быть null, тогда анализ не проводим)
		AnalysisParameters ap = this.obtainAnalysisParameters();

		// Получаем эталон
		Etalon etalon = obtainEtalon();

		// === Обрабатываем входные данные, анализируем, сравниваем ===

		// Достаем из эталона уровень обнаружения обрыва
		double breakThresh = etalon.getMinTraceLevel();
		// Достаем  эталонный MTM (пороговые кривые и события)
		ModelTraceManager etMTM = etalon.getMTM();

		// проводим анализ
		AnalysisResult ar = CoreAnalysisManager.performAnalysis(bs, ap);

		// сравниваем
		List alarmList = CoreAnalysisManager.compareAndMakeAlarms(ar, breakThresh, etMTM);

		// добавляем AnalysisResult в результаты анализа
		outParameters.put(CODENAME_ANALYSIS_RESULT, ar.toByteArray());

		// === Формируем результаты ===

		// добавляем алармы в результаты анализа
		ReflectogramAlarm[] alarms = (ReflectogramAlarm[]) alarmList.toArray(new ReflectogramAlarm[alarmList.size()]);
		outParameters.put(CODENAME_ALARMS, ReflectogramAlarm.alarmsToByteArray(alarms));

		// формируем результаты анализа
		Parameter[] ret = new Parameter[outParameters.size()];
		int i = 0;
		try {
			for (final Iterator it = outParameters.keySet().iterator(); it.hasNext(); i++) {
				final String codename = (String) it.next();
				final Identifier parameterTypeId = (Identifier) OUT_PARAMETER_TYPE_IDS_MAP.get(codename);
				final ParameterType parameterType = (ParameterType) StorableObjectPool.getStorableObject(parameterTypeId, true);
				if (parameterType != null) {
					try {
						ret[i] = Parameter.createInstance(parameterType, (byte[]) outParameters.get(codename));
					}
					catch (CreateObjectException coe) {
						throw new AnalysisException("Cannot create parameter -- " + coe.getMessage(), coe);
					}
				}
				else
					throw new AnalysisException("Cannot find parameter type of codename: '" + codename + "'");
			}
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot load parameter types -- " + ae.getMessage(), ae);
		}

		return ret;
	}
}
