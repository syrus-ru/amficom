/*
 * $Id: DadaraAnalysisManager.java,v 1.41 2005/06/04 16:56:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.41 $, $Date: 2005/06/04 16:56:18 $
 * @author $Author: bass $
 * @module mcm_v1
 */

//*
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
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
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_TransferablePackage.TypicalCondition_TransferablePackage.OperationSort;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

public class DadaraAnalysisManager implements AnalysisManager
{
	// input SetParameters codenames
	public static final String CODENAME_REFLECTOGRAMMA = ParameterTypeCodenames.REFLECTOGRAMMA;
	public static final String CODENAME_DADARA_ETALON_MTM = ParameterTypeCodenames.DADARA_ETALON_MTM;
	public static final String CODENAME_DADARA_ETALON_BREAK_THRESH = ParameterTypeCodenames.DADARA_MIN_TRACE_LEVEL;
	public static final String CODENAME_DADARA_CRITERIA = ParameterTypeCodenames.DADARA_CRITERIA;

	// output SetParameters codenames
//	public static final String CODENAME_DADARA_TRACELENGTH = "tracelength";
//	public static final String CODENAME_DARARA_MODELFUNCTION = "modelfunction";
//	public static final String CODENAME_DARARA_SIMPLEEVENTS = "simpleevents";
	public static final String CODENAME_ALARMS = ParameterTypeCodenames.DADARA_ALARMS;

	private static final Map OUT_PARAMETER_TYPE_IDS_MAP;	//Map <String parameterTypeCodename, Identifier parameterTypeId>

	private final Map parametersMap;	//Map <String codename, SetParameter parameter>

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
					ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
					StorableObjectWrapper.COLUMN_CODENAME));
		}

		try {
			final StorableObjectCondition condition;
			if (typicalConditions.size() == 1)
				condition = (StorableObjectCondition) typicalConditions.iterator().next();
			else
				condition = new CompoundCondition(typicalConditions, CompoundConditionSort.OR);
			final java.util.Set parameterTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			for (final Iterator it = parameterTypes.iterator(); it.hasNext();) {
				final ParameterType parameterType = (ParameterType) it.next();
				OUT_PARAMETER_TYPE_IDS_MAP.put(parameterType.getCodename(), parameterType.getId());
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	public DadaraAnalysisManager(final Result measurementResult,
			final Analysis analysis,
			final Set etalon) throws AnalysisException {
		this.parametersMap = new HashMap();
		this.addSetParameters(measurementResult.getParameters());
		this.addSetParameters(analysis.getCriteriaSet().getParameters());
		this.addSetParameters(etalon.getParameters());
	}

	private void addSetParameters(final SetParameter[] setParameters) throws AnalysisException {
		for (int i = 0; i < setParameters.length; i++)
			this.addParameter(setParameters[i]);
	}

	private void addParameter(final SetParameter parameter) throws AnalysisException {
		String codename = parameter.getType().getCodename();
		if (codename != null) {
			if (! this.parametersMap.containsKey(codename))
				this.parametersMap.put(codename, parameter.getValue());
			else
				throw new AnalysisException("Parameter of codename '" + codename + "' already loaded");
		}
		else
			throw new AnalysisException("Codename of parameter: '" + parameter.getId() + "' is NULL");
	}

	private boolean hasParameter(final String codename) {
		return this.parametersMap.get(codename) != null;
	}

	private byte[] getParameter(final String codename) throws AnalysisException {
		byte[] rawData = (byte[]) this.parametersMap.get(codename);
		if (rawData == null)
			throw new AnalysisException("Cannot get parameter of codename '" + codename + "'");
		return rawData;
	}

	private ByteArray getParBA(final String codename) throws AnalysisException {
		byte[] rawData = this.getParameter(codename);
		return new ByteArray(rawData);
	}

	private ModelTraceManager obtainEtalonMTM() throws AnalysisException {
		// read etalon r/g and its thresholds
		byte[] etalonData = this.getParameter(CODENAME_DADARA_ETALON_MTM);
		try {
			return (ModelTraceManager) DataStreamableUtil.readDataStreamableFromBA(etalonData, ModelTraceManager.getReader());
		}
		catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}

	// may return null if no such parameter
	private AnalysisParameters obtainAnalysisParameters() throws AnalysisException {
		if (!hasParameter(CODENAME_DADARA_CRITERIA))
			return null;
		byte[] bar = this.getParameter(CODENAME_DADARA_CRITERIA);
		try {
			return (AnalysisParameters) DataStreamableUtil.readDataStreamableFromBA(bar, AnalysisParameters.getReader());
		}
		catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}

	public SetParameter[] analyse() throws AnalysisException {
		// output parameters (not SetParameter[] yet)
		Map outParameters = new HashMap(); // Map <String codename, byte[] rawData>

		// Получаем рефлектограмму
		BellcoreStructure bs = (new BellcoreReader()).getData(this.getParameter(CODENAME_REFLECTOGRAMMA));

		// Получаем из эталона уровень обнаружения обрыва
		double breakThresh = 0;
		try {
			breakThresh = this.getParBA(CODENAME_DADARA_ETALON_BREAK_THRESH).toDouble();
		}
		catch (IOException ioe) {
			throw new AnalysisException("Couldn't get " + CODENAME_DADARA_ETALON_BREAK_THRESH + ": " + ioe + ", " + ioe.getMessage());
		}

		// Получаем эталонный MTM (пороговые кривые и события)
		ModelTraceManager etMTM = this.obtainEtalonMTM();

		// Получаем параметры анализа (могут быть null, тогда анализ не проводим)
		AnalysisParameters ap = this.obtainAnalysisParameters();

		// сравниваем
		List alarmList = CoreAnalysisManager.analyseCompareAndMakeAlarms(bs, ap, breakThresh, etMTM);

		// добавляем алармы в результаты анализа
		ReflectogramAlarm[] alarms = (ReflectogramAlarm[]) alarmList.toArray(new ReflectogramAlarm[alarmList.size()]);
		outParameters.put(CODENAME_ALARMS, ReflectogramAlarm.alarmsToByteArray(alarms));

		// формируем результаты анализа
		SetParameter[] ret = new SetParameter[outParameters.size()];
		int i = 0;
		try {
			for (final Iterator it = outParameters.keySet().iterator(); it.hasNext(); i++) {
				final String codename = (String) it.next();
				final Identifier parameterTypeId = (Identifier) OUT_PARAMETER_TYPE_IDS_MAP.get(codename);
				final ParameterType parameterType = (ParameterType) StorableObjectPool.getStorableObject(parameterTypeId, true);
				if (parameterType != null) {
					try {
						ret[i] = SetParameter.createInstance(parameterType, (byte[]) outParameters.get(codename));
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
