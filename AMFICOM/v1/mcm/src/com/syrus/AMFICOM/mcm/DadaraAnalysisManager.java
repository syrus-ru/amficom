/*
 * $Id: DadaraAnalysisManager.java,v 1.62 2005/08/08 19:36:40 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.62 $, $Date: 2005/08/08 19:36:40 $
 * @author $Author: arseniy $
 * @module mcm
 */

//*
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.measurement.ParameterSet;
import com.syrus.AMFICOM.measurement.Result;
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

	private final Map<String, byte[]> tracePars;
	private final Map<String, byte[]> criteriaPars;
	private final Map<String, byte[]> etalonPars;

	static {
		OUT_PARAMETER_TYPE_IDS_MAP = new HashMap<String,Identifier>();
		addParameterTypeIds(new String[] { CODENAME_ALARMS, CODENAME_ANALYSIS_RESULT });
	}

	private static void addParameterTypeIds(final String[] codenames) {
		assert codenames != null : ErrorMessages.NON_NULL_EXPECTED;
		assert codenames.length > 0 : ErrorMessages.NON_EMPTY_EXPECTED;

		final Set<StorableObjectCondition> typicalConditions = new HashSet<StorableObjectCondition>(codenames.length);
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
			final Set<ParameterType> parameterTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true, true);
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
	 * @param analysis (not null)
	 * @param etalon may be null
	 * @throws AnalysisException
	 */
	@SuppressWarnings("unused")
	private DadaraAnalysisManager(final Result measurementResult,
			final Analysis analysis,
			final ParameterSet etalon) throws AnalysisException {
		this.tracePars = new HashMap<String,byte[]>();
		this.criteriaPars = new HashMap<String,byte[]>();
		this.etalonPars = new HashMap<String,byte[]>();
		this.addSetParameters(this.tracePars, measurementResult.getParameters());
		this.addSetParameters(this.criteriaPars, analysis.getCriteriaSet().getParameters());
		if(etalon != null) {
			this.addSetParameters(this.etalonPars, etalon.getParameters());
		}
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

	private byte[] getParameter(final Map<String,byte[]> parsMap, final String codename)
	throws AnalysisException {
		byte[] rawData = parsMap.get(codename);
		if (rawData == null)
			throw new AnalysisException("Cannot get parameter of codename '" + codename + "'");
		return rawData;
	}

	// возвращает null, если эталона нет.
	private Etalon obtainEtalon() throws AnalysisException {
		if (! hasParameter(this.etalonPars, CODENAME_DADARA_ETALON))
			return null;
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

	// will not return null
	private AnalysisParameters obtainAnalysisParameters()
	throws AnalysisException {
		byte[] bar = this.getParameter(this.criteriaPars, CODENAME_DADARA_CRITERIA);
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
		Map<String,byte[]> outParameters = new HashMap<String,byte[]>(); // Map <String codename, byte[] rawData>
		
		// === Получаем входные данные ===

		// Получаем рефлектограмму
		BellcoreStructure bs = (new BellcoreReader()).getData(this.getParameter(this.tracePars, CODENAME_REFLECTOGRAMMA));

		// Получаем параметры анализа
		AnalysisParameters ap = this.obtainAnalysisParameters();

		// Получаем эталон (может быть null, тогда сравнение не проводим)
		Etalon etalon = obtainEtalon();

		Log.debugMessage("DadaraAnalysisManager.analyse | bs = " + bs, Log.DEBUGLEVEL08);
		Log.debugMessage("DadaraAnalysisManager.analyse | ap = " + ap, Log.DEBUGLEVEL08);
		Log.debugMessage("DadaraAnalysisManager.analyse | etalon = " + etalon, Log.DEBUGLEVEL08);

		// === Обрабатываем входные данные, анализируем, сравниваем ===
		Log.debugMessage("DadaraAnalysisManager.analyse | starting analysis", Log.DEBUGLEVEL07);

		// проводим анализ
		AnalysisResult ar = CoreAnalysisManager.performAnalysis(bs, ap);

		// если есть эталон, то сравниваем:
		// дополняем ar результатами сравнения и получаем алармы
		List<ReflectogramMismatch> alarmList;
		if (etalon != null)
			alarmList = CoreAnalysisManager.compareAndMakeAlarms(ar, etalon);
		else
			alarmList = null;

		Log.debugMessage("DadaraAnalysisManager.analyse | alarmList = " + alarmList, Log.DEBUGLEVEL08);

		// добавляем AnalysisResult в результаты анализа
		outParameters.put(CODENAME_ANALYSIS_RESULT, ar.toByteArray());

		// === Формируем результаты ===

		// если эталон есть, то добавляем алармы в результаты анализа
		if (etalon != null) {
			ReflectogramMismatch[] alarms = alarmList.toArray(new ReflectogramMismatch[alarmList.size()]);
			outParameters.put(CODENAME_ALARMS, ReflectogramMismatch.alarmsToByteArray(alarms));
		}

		// формируем результаты анализа
		final Parameter[] ret = new Parameter[outParameters.size()];
		int i = 0;
		try {
			for (final String codename : outParameters.keySet()) {
				Log.debugMessage("DadaraAnalysisManager.analyse | processing output parameter " + codename, Log.DEBUGLEVEL07);
				final Identifier parameterTypeId = OUT_PARAMETER_TYPE_IDS_MAP.get(codename);
				final ParameterType parameterType = (ParameterType) StorableObjectPool.getStorableObject(parameterTypeId, true);
				if (parameterType != null) {
					try {
						ret[i++] = Parameter.createInstance(parameterType, outParameters.get(codename));
					}
					catch (CreateObjectException coe) {
						throw new AnalysisException("Cannot create parameter -- " + coe.getMessage(), coe);
					}
				}
				else {
					throw new AnalysisException("Cannot find parameter type of codename: '" + codename + "'");
				}
			}
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot load parameter types -- " + ae.getMessage(), ae);
		}

		Log.debugMessage("DadaraAnalysisManager.analyse | done, returning Parameter[" + ret.length + "]", Log.DEBUGLEVEL07);
		return ret;
	}
}
