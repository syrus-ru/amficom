/*
 * $Id: DadaraAnalysisManager.java,v 1.55 2005/07/14 13:27:16 saa Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.55 $, $Date: 2005/07/14 13:27:16 $
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
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMismatch;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.ParameterTypeCodename;
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
	public static final String CODENAME_REFLECTOGRAMMA = ParameterTypeCodename.REFLECTOGRAMMA.stringValue();
	public static final String CODENAME_DADARA_ETALON = ParameterTypeCodename.DADARA_ETALON.stringValue();
	public static final String CODENAME_DADARA_CRITERIA = ParameterTypeCodename.DADARA_CRITERIA.stringValue();

	// output SetParameters codenames
	public static final String CODENAME_ALARMS = ParameterTypeCodename.DADARA_ALARMS.stringValue();
	public static final String CODENAME_ANALYSIS_RESULT = ParameterTypeCodename.DADARA_ANALYSIS_RESULT.stringValue();

	private static final Map<String,Identifier> OUT_PARAMETER_TYPE_IDS_MAP;

	private final Map<String,byte[]> tracePars;
	private final Map<String,byte[]> criteriaPars;
	private final Map<String,byte[]> etalonPars;

	static {
		OUT_PARAMETER_TYPE_IDS_MAP = new HashMap<String,Identifier>();
		addParameterTypeIds(new String[] {CODENAME_ALARMS});
	}

	private static void addParameterTypeIds(final String[] codenames) {
		assert codenames != null : ErrorMessages.NON_NULL_EXPECTED;
		assert codenames.length > 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		final java.util.Set<TypicalCondition> typicalConditions = new HashSet<TypicalCondition>(codenames.length);
		for (int i = 0; i < codenames.length; i++) {
			typicalConditions.add(new TypicalCondition(codenames[i],
					OperationSort.OPERATION_EQUALS,
					ObjectEntities.PARAMETER_TYPE_CODE,
					StorableObjectWrapper.COLUMN_CODENAME));
		}

		try {
			final StorableObjectCondition condition;
			if (typicalConditions.size() == 1)
				condition = typicalConditions.iterator().next();
			else
				condition = new CompoundCondition(typicalConditions, CompoundConditionSort.OR);
			final java.util.Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true, true);
			for (final Iterator<ParameterType> it = parameterTypes.iterator(); it.hasNext();) {
				final ParameterType parameterType = it.next();
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
		this.tracePars = new HashMap<String,byte[]>();
		this.criteriaPars = new HashMap<String,byte[]>();
		this.etalonPars = new HashMap<String,byte[]>();
		this.addSetParameters(this.tracePars, measurementResult.getParameters());
		this.addSetParameters(this.criteriaPars, analysis.getCriteriaSet().getParameters());
		this.addSetParameters(this.etalonPars, etalon.getParameters());
	}

	private void addSetParameters(final Map<String,byte[]> parsMap, final Parameter[] setParameters) throws AnalysisException {
		for (int i = 0; i < setParameters.length; i++)
			this.addParameter(parsMap, setParameters[i]);
	}

	private void addParameter(final Map<String,byte[]> parsMap, final Parameter parameter) throws AnalysisException {
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

	private boolean hasParameter(final Map<String,byte[]> parsMap, final String codename) {
		return parsMap.get(codename) != null;
	}

	private byte[] getParameter(final Map<String,byte[]> parsMap, final String codename) throws AnalysisException {
		byte[] rawData = parsMap.get(codename);
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
		Map<String,byte[]> outParameters = new HashMap<String,byte[]>(); // Map <String codename, byte[] rawData>
		
		// === Получаем входные данные ===

		// Получаем рефлектограмму
		BellcoreStructure bs = (new BellcoreReader()).getData(this.getParameter(this.tracePars, CODENAME_REFLECTOGRAMMA));

		// Получаем параметры анализа (могут быть null, тогда анализ не проводим)
		AnalysisParameters ap = this.obtainAnalysisParameters();

		// Получаем эталон
		Etalon etalon = obtainEtalon();

		// === Обрабатываем входные данные, анализируем, сравниваем ===

		// проводим анализ
		AnalysisResult ar = CoreAnalysisManager.performAnalysis(bs, ap);

		// сравниваем: дополняем ar результатами сравнения и получаем алармы
		List<ReflectogramMismatch> alarmList = CoreAnalysisManager.compareAndMakeAlarms(ar, etalon);

		// добавляем AnalysisResult в результаты анализа
		outParameters.put(CODENAME_ANALYSIS_RESULT, ar.toByteArray());

		// === Формируем результаты ===

		// добавляем алармы в результаты анализа
		ReflectogramMismatch[] alarms = (ReflectogramMismatch[]) alarmList.toArray(new ReflectogramMismatch[alarmList.size()]);
		outParameters.put(CODENAME_ALARMS, ReflectogramMismatch.alarmsToByteArray(alarms));

		// формируем результаты анализа
		Parameter[] ret = new Parameter[outParameters.size()];
		int i = 0;
		try {
			for (final Iterator<String> it = outParameters.keySet().iterator(); it.hasNext(); i++) {
				final String codename = it.next();
				final Identifier parameterTypeId = OUT_PARAMETER_TYPE_IDS_MAP.get(codename);
				final ParameterType parameterType = (ParameterType) StorableObjectPool.getStorableObject(parameterTypeId, true);
				if (parameterType != null) {
					try {
						ret[i] = Parameter.createInstance(parameterType, outParameters.get(codename));
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
