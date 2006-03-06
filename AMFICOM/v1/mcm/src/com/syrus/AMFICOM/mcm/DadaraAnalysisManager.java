/*
 * $Id: DadaraAnalysisManager.java,v 1.75.2.1 2006/03/06 14:15:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

/**
 * @version $Revision: 1.75.2.1 $, $Date: 2006/03/06 14:15:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

//*
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ALARMS;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ANALYSIS_RESULT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_CRITERIA;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_ETALON;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_QUALITY_OVERALL_D;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_QUALITY_OVERALL_Q;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.DADARA_QUALITY_PER_EVENT;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.REFLECTOGRAMMA;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.analysis.CoreAnalysisManager;
import com.syrus.AMFICOM.analysis.Etalon;
import com.syrus.AMFICOM.analysis.EtalonComparison;
import com.syrus.AMFICOM.analysis.dadara.AnalysisParameters;
import com.syrus.AMFICOM.analysis.dadara.AnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DadaraReflectometryAnalysisResult;
import com.syrus.AMFICOM.analysis.dadara.DataStreamableUtil;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisResultParameter;
import com.syrus.AMFICOM.measurement.MeasurementResultParameter;
import com.syrus.AMFICOM.measurement.Parameter;
import com.syrus.AMFICOM.reflectometry.ReflectometryEvaluationOverallResult;
import com.syrus.io.BellcoreReader;
import com.syrus.io.BellcoreStructure;
import com.syrus.io.DataFormatException;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

final class DadaraAnalysisManager implements AnalysisManager {
	private final MeasurementResultParameter measurementResultParameter;
	private final Analysis analysis;
	private final Map<String, byte[]> analysisParametersMap;

	/**
	 * This constructor is called only by Reflection API
	 * @param measurementResult
	 * @param analysis (not null)
	 * @param etalon may be null
	 * @throws AnalysisException
	 */
	@SuppressWarnings("unused")
	private DadaraAnalysisManager(final MeasurementResultParameter measurementResultParameter,
			final Analysis analysis) throws AnalysisException, ApplicationException {
		this.measurementResultParameter = measurementResultParameter;

		this.analysis = analysis;
		this.analysisParametersMap = Parameter.getTypeCodenameValueMap(analysis.getActionTemplate().getActionParameters());
	}

	public Set<AnalysisResultParameter> analyse() throws AnalysisException, ApplicationException {
		Log.debugMessage("entered", Level.FINER);

		if (!this.measurementResultParameter.getTypeCodename().equals(REFLECTOGRAMMA)) {
			return Collections.emptySet();
		}

		// output parameters map
		final Map<String, byte[]> analysisResultParameterValuesMap = new HashMap<String, byte[]>();

		// === Получаем входные данные ===

		// Получаем рефлектограмму
		final BellcoreReader bellcoreReader = new BellcoreReader();
		final BellcoreStructure bellcoreStructure = bellcoreReader.getData(this.measurementResultParameter.getValue());

		// Получаем параметры анализа
		final AnalysisParameters analysisParameters = this.obtainAnalysisParameters();

		// Получаем эталон (может быть null, тогда сравнение не проводим)
		final Etalon etalon = this.obtainEtalon();

		Log.debugMessage("bellcoreStructure = " + bellcoreStructure, Level.FINER);
		Log.debugMessage("analysisParameters = " + analysisParameters, Level.FINER);
		Log.debugMessage("etalon = " + etalon, Level.FINER);

		// === Обрабатываем входные данные, анализируем, сравниваем ===
		Log.debugMessage("starting analysis", Level.FINER);

		// проводим анализ
		final AnalysisResult analysisResult = CoreAnalysisManager.performAnalysis(bellcoreStructure, analysisParameters);

		// добавляем AnalysisResult в результаты анализа
		analysisResultParameterValuesMap.put(DADARA_ANALYSIS_RESULT.stringValue(), analysisResult.toByteArray());

		if (etalon != null) {
			// сравниваем
			final EtalonComparison etalonComparison = CoreAnalysisManager.compareToEtalon(analysisResult, etalon);
			final DadaraReflectometryAnalysisResult reflectometryAnalysisResult = new DadaraReflectometryAnalysisResult(analysisResult, etalonComparison);

			// сохраняем результаты сравнения
			analysisResultParameterValuesMap.put(DADARA_ALARMS.stringValue(), reflectometryAnalysisResult.getDadaraReflectogramMismatchBytes());
			analysisResultParameterValuesMap.put(DADARA_QUALITY_PER_EVENT.stringValue(), reflectometryAnalysisResult.getDadaraEvaluationPerEventResultBytes());
			final ReflectometryEvaluationOverallResult overallResult = reflectometryAnalysisResult.getReflectometryEvaluationOverallResult();
			if (overallResult.hasDQ()) {
				analysisResultParameterValuesMap.put(DADARA_QUALITY_OVERALL_D.stringValue(), ByteArray.toByteArray(overallResult.getD()));
				analysisResultParameterValuesMap.put(DADARA_QUALITY_OVERALL_Q.stringValue(), ByteArray.toByteArray(overallResult.getQ()));
			}
		}

		final Set<String> analysisResultParametersTypeCodenames = analysisResultParameterValuesMap.keySet();
		final Map<String, Identifier> analysisResultParametersTypeCodenameIdMap = ParameterType.getCodenameIdentifierMap(analysisResultParametersTypeCodenames);
		final Set<AnalysisResultParameter> analysisResultParameters = new HashSet<AnalysisResultParameter>();
		for (final String codename : analysisResultParametersTypeCodenames) {
			analysisResultParameters.add(this.analysis.createActionResultParameter(LoginManager.getUserId(),
					analysisResultParameterValuesMap.get(codename),
					analysisResultParametersTypeCodenameIdMap.get(codename)));
		}
		Log.debugMessage("done, returning " + analysisResultParameters.size() + " AnalysisResultParameters", Level.FINER);
		return analysisResultParameters;
	}

	// will not return null
	private AnalysisParameters obtainAnalysisParameters() throws AnalysisException {
		final byte[] bar = this.analysisParametersMap.get(DADARA_CRITERIA.stringValue());
		try {
			return (AnalysisParameters) DataStreamableUtil.readDataStreamableFromBA(bar, AnalysisParameters.getReader());
		}
		catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}

	// возвращает null, если эталона нет.
	private Etalon obtainEtalon() throws AnalysisException {
		final byte[] bar = this.analysisParametersMap.get(DADARA_ETALON.stringValue());
		if (bar == null) {
			return null;
		}

		try {
			final Etalon et = (Etalon) DataStreamableUtil.readDataStreamableFromBA(bar, Etalon.getDSReader());
			return et;
		} catch (DataFormatException e) {
			throw new AnalysisException("DataFormatException: " + e.toString());
		}
	}
}
