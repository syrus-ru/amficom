/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.17 2005/01/28 12:26:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2005/01/28 12:26:59 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class AnalysisEvaluationProcessor {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";
	private static final String CODENAME_EVALUATION_TYPE_DADARA = "dadara";

	private static final String CLASS_NAME_ANALYSIS_MANAGER_DADARA = "DadaraAnalysisManager";
	private static final String CLASS_NAME_EVALUATION_MANAGER_DADARA = "DadaraEvaluationManager";

	private static AnalysisManager analysisManager;
	private static EvaluationManager evaluationManager;

	private AnalysisEvaluationProcessor() {
		//singleton
	}

	public static Result[] analyseEvaluate(Result measurementResult) throws AnalysisException, EvaluationException {
		Measurement measurement = (Measurement) measurementResult.getAction();
		Test test = null;
		try {
			test = (Test) MeasurementStorableObjectPool.getStorableObject(measurement.getTestId(), true);
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot find test -- " + ae.getMessage(), ae);
		}
		Identifier monitoredElementId = test.getMonitoredElement().getId();
		MeasurementSetup measurementSetup = measurement.getSetup();

		AnalysisType analysisType = test.getAnalysisType();
		EvaluationType evaluationType = test.getEvaluationType();
		if (analysisType != null) {
			Analysis analysis = createAnalysis(analysisType, monitoredElementId, measurement, measurementSetup.getCriteriaSet());
			if (evaluationType != null) {
				Evaluation evaluation = createEvaluation(evaluationType,
						monitoredElementId,
						measurement,
						measurementSetup.getThresholdSet());
				return analyseAndEvaluate(measurementResult, analysis, evaluation, measurementSetup.getEtalon());

			}
			return new Result[1];// return analyse(measurementResult, analysis,
														// measurementSetup.getEtalon());
		}
		if (evaluationType != null) {
			Evaluation evaluation = createEvaluation(evaluationType,
					monitoredElementId,
					measurement,
					measurementSetup.getThresholdSet());
			return new Result[1];// return evaluate(measurementResult, evaluation,
														// measurementSetup.getEtalon());
		}
		else
			return new Result[0];
	}

	private static Analysis createAnalysis(AnalysisType analysisType,
			Identifier monitoredElementId,
			Measurement measurement,
			Set criteriaSet) throws AnalysisException {
		if (criteriaSet == null)
			throw new AnalysisException("Criteria set is NULL");

		try {
			Analysis analysis = Analysis.createInstance(MeasurementControlModule.iAm.getUserId(),
					analysisType,
					monitoredElementId,
					measurement,
					criteriaSet);
			analysis.insert();
			return analysis;
		}
		catch (CreateObjectException coe) {
			throw new AnalysisException("Cannot create analysis", coe);
		}
	}

	private static Evaluation createEvaluation(EvaluationType evaluationType,
			Identifier monitoredElementId,
			Measurement measurement,
			Set thresholdSet) throws EvaluationException {
		if (thresholdSet == null)
			throw new EvaluationException("Threshold set is NULL");

		try {
			Evaluation evaluation = Evaluation.createInstance(MeasurementControlModule.iAm.getUserId(),
					evaluationType,
					monitoredElementId,
					measurement,
					thresholdSet);
			evaluation.insert();
			return evaluation;
		}
		catch (CreateObjectException coe) {
			throw new EvaluationException("Cannot create evaluation", coe);
		}
	}

	private static void loadAnalysisAndEvaluationManager(String analysisCodename,
			String evaluationCodename,
			Result measurementResult,
			Analysis analysis,
			Evaluation evaluation,
			Set etalon) throws AnalysisException {
		String className = null;
		Constructor constructor = null;

		if (analysisCodename.equals(CODENAME_ANALYSIS_TYPE_DADARA))
			className = "com.syrus.AMFICOM.mcm." + CLASS_NAME_ANALYSIS_MANAGER_DADARA;
		else
			throw new AnalysisException("Cannot find analysis manager for analysis of codename '" + analysisCodename + "'");

		try {
			constructor = Class.forName(className).getDeclaredConstructor(new Class[] {Result.class,
					Analysis.class,
					Evaluation.class,
					Set.class});
			constructor.setAccessible(true);
			analysisManager = (AnalysisManager) constructor.newInstance(new Object[] {measurementResult, analysis, evaluation, etalon});
		}
		catch (SecurityException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (NoSuchMethodException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (ClassNotFoundException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (IllegalArgumentException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (InstantiationException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (IllegalAccessException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}
		catch (InvocationTargetException e) {
			throw new AnalysisException("Cannot get constructor -- " + e.getMessage(), e);
		}

		if (evaluationCodename.equals(CODENAME_EVALUATION_TYPE_DADARA))
			evaluationManager = (EvaluationManager) analysisManager;
	}

	private static Result[] analyseAndEvaluate(Result measurementResult, Analysis analysis, Evaluation evaluation, Set etalon)
			throws AnalysisException,
				EvaluationException {

		String analysisCodename = analysis.getType().getCodename();
		String evaluationCodename = evaluation.getType().getCodename();

		loadAnalysisAndEvaluationManager(analysisCodename, evaluationCodename, measurementResult, analysis, evaluation, etalon);

		SetParameter[] arParameters = analysisManager.analyse();
		Result analysisResult;
		try {
			analysisResult = analysis.createResult(MeasurementControlModule.iAm.getUserId(), arParameters);
		}
		catch (CreateObjectException coe) {
			Log.errorException(coe);
			analysisResult = null;
		}

		SetParameter[] erParameters = evaluationManager.evaluate();
		Result evaluationResult;
		try {
			evaluationResult = evaluation.createResult(MeasurementControlModule.iAm.getUserId(), erParameters);
		}
		catch (CreateObjectException coe) {
			Log.errorException(coe);
			evaluationResult = null;
		}

		Result[] results = new Result[2];
		results[0] = analysisResult;
		results[1] = evaluationResult;
		return results;
	}

// public static Result analyse(Result measurementResult,
// Analysis analysis,
// Set etalon) throws AnalysisException {
//		
// }
//
// public static Result evaluate(Result measurementResult,
// Result analysisResult,
// Evaluation evaluation,
// Set etalon) throws EvaluationException {
// }
//
// public static Result evaluate(Result measurementResult,
// Evaluation evaluation,
//																Set etalon) throws EvaluationException {
//	}

}
