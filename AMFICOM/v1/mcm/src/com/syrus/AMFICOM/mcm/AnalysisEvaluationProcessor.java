/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.15 2005/01/12 13:34:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

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
 * @version $Revision: 1.15 $, $Date: 2005/01/12 13:34:57 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class AnalysisEvaluationProcessor {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";
	private static final String CODENAME_EVALUATION_TYPE_DADARA = "dadara";

	public static Result[] analyseEvaluate(Result measurementResult) throws AnalysisException, EvaluationException {
		Measurement measurement = (Measurement)measurementResult.getAction();
		Test test = null;
		try {
			test = (Test)MeasurementStorableObjectPool.getStorableObject(measurement.getTestId(), true);
		}
		catch (ApplicationException ae) {
			throw new AnalysisException("Cannot find test -- " + ae.getMessage(), ae);
		}
		Identifier monitoredElementId = test.getMonitoredElement().getId();
		MeasurementSetup measurementSetup = measurement.getSetup();

		AnalysisType analysisType = test.getAnalysisType();
		EvaluationType evaluationType = test.getEvaluationType();
		if (analysisType != null) {
			Analysis analysis = createAnalysis(analysisType,
																				 monitoredElementId,
																				 measurementSetup.getCriteriaSet());
			if (evaluationType != null) {
				Evaluation evaluation = createEvaluation(evaluationType,
																								 monitoredElementId,
																								 measurementSetup.getThresholdSet());
				return analyseAndEvaluate(measurementResult, analysis, evaluation, measurementSetup.getEtalon());
					
			}
			else
				return new Result[1];//return analyse(measurementResult, analysis, measurementSetup.getEtalon());
		}
		else {
			if (evaluationType != null) {
				Evaluation evaluation = createEvaluation(evaluationType,
																								 monitoredElementId,
																								 measurementSetup.getThresholdSet());
				return new Result[1];//return evaluate(measurementResult, evaluation, measurementSetup.getEtalon());
			}
			else
				return new Result[0];
		}
	}

	private static Analysis createAnalysis(AnalysisType analysisType,
																				 Identifier monitoredElementId,
																				 Set criteriaSet) throws AnalysisException {
		if (criteriaSet == null)
			throw new AnalysisException("Criteria set is NULL");

		try {
			Analysis analysis = Analysis.createInstance(MeasurementControlModule.iAm.getUserId(),
																		 analysisType,
																		 monitoredElementId,
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
																						 Set thresholdSet) throws EvaluationException {
		if (thresholdSet == null)
			throw new EvaluationException("Threshold set is NULL");

		try {
			Evaluation evaluation = Evaluation.createInstance(MeasurementControlModule.iAm.getUserId(),
																			 evaluationType,
																			 monitoredElementId,
																			 thresholdSet);
			evaluation.insert();
			return evaluation;
		}
		catch (CreateObjectException coe) {
			throw new EvaluationException("Cannot create evaluation", coe);
		}
	}

	private static Result[] analyseAndEvaluate(Result measurementResult,
			Analysis analysis,
			Evaluation evaluation,
			Set etalon)
		throws AnalysisException, EvaluationException
	{
		AnalysisManager analysisManager;
		EvaluationManager evaluationManager;

		String analysisCodename = analysis.getType().getCodename();
		String evaluationCodename = evaluation.getType().getCodename();
		
		if (analysisCodename.equals(CODENAME_ANALYSIS_TYPE_DADARA)) {
			analysisManager = new DadaraAnalysisManager(measurementResult,
																									analysis,
																									evaluation,
																									etalon);
			if (evaluationCodename.equals(CODENAME_EVALUATION_TYPE_DADARA)) {
				evaluationManager = (EvaluationManager)analysisManager;
			}
			else
				throw new EvaluationException("Evaluation for codename '" + evaluationCodename + "' not implemented");
		}
		else
			throw new AnalysisException("Analysis for codename '" + analysisCodename + "' not implemented");

		SetParameter[] arParameters = analysisManager.analyse();
		Result analysisResult;
		try {
			analysisResult = analysis.createResult(MeasurementControlModule.iAm.getUserId(),
																						 measurementResult.getMeasurement(),
																						 arParameters);
		}
		catch (CreateObjectException coe) {
			Log.errorException(coe);
			analysisResult = null;
		}
			
		SetParameter[] erParameters = evaluationManager.evaluate();
		Result evaluationResult;
		try {
			evaluationResult = evaluation.createResult(MeasurementControlModule.iAm.getUserId(),
																								 measurementResult.getMeasurement(),
																								 erParameters);
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

//	public static Result analyse(Result measurementResult,
//															 Analysis analysis,
//															 Set etalon) throws AnalysisException {
//		
//	}
//
//	public static Result evaluate(Result measurementResult,
//																Result analysisResult,
//																Evaluation evaluation,
//																Set etalon) throws EvaluationException {
//	}
//
//	public static Result evaluate(Result measurementResult,
//																Evaluation evaluation,
//																Set etalon) throws EvaluationException {
//	}

}
