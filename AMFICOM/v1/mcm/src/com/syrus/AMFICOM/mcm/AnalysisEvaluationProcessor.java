/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.9 2004/08/22 19:10:57 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
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
 * @version $Revision: 1.9 $, $Date: 2004/08/22 19:10:57 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class AnalysisEvaluationProcessor {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";
	private static final String CODENAME_EVALUATION_TYPE_DADARA = "dadara";

	public static Result[] analyseEvaluate(Result measurementResult) throws AnalysisException, EvaluationException {
		Measurement measurement = (Measurement)measurementResult.getAction();
		Test test = (Test)MeasurementStorableObjectPool.getStorableObject(measurement.getTestId(), true);
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
			throw new AnalysisException("Cirteria set is NULL");

		Identifier analysisId = null;
		try {
			analysisId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.ANALYSIS_ENTITY_CODE, 10);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new AnalysisException("Cannot generate identifier for analysis", ioee);
		}
		catch (AMFICOMRemoteException are) {
			throw new AnalysisException("Cannot generate identifier for analysis -- " + are.message);
		}

		try {
			return Analysis.createInstance(analysisId,
																		 MeasurementControlModule.iAm.getUserId(),
																		 analysisType,
																		 monitoredElementId,
																		 criteriaSet);
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

		Identifier evaluationId = null;
		try {
			evaluationId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.EVALUATION_ENTITY_CODE, 10);
		}
		catch (IllegalObjectEntityException ioee) {
			throw new EvaluationException("Cannot generate identifier for evaluation", ioee);
		}
		catch (AMFICOMRemoteException are) {
			throw new EvaluationException("Cannot generate identifier for evaluation -- " + are.message);
		}

		try {
			return Evaluation.createInstance(evaluationId,
																			 MeasurementControlModule.iAm.getUserId(),
																			 evaluationType,
																			 monitoredElementId,
																			 thresholdSet);
		}
		catch (CreateObjectException coe) {
			throw new EvaluationException("Cannot create evaluation", coe);
		}
	}

	private static Result[] analyseAndEvaluate(Result measurementResult,
																						 Analysis analysis,
																						 Evaluation evaluation,
																						 Set etalon) throws AnalysisException, EvaluationException {
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
			Identifier analysisResultId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE, 10);
			analysisResult = analysis.createResult(analysisResultId,
																						 MeasurementControlModule.iAm.getUserId(),
																						 measurementResult.getMeasurement(),
																						 AlarmLevel.ALARM_LEVEL_NONE,
																						 arParameters);
		}
		catch (Exception coe) {
			Log.errorException(coe);
			analysisResult = null;
		}
			
		SetParameter[] erParameters = evaluationManager.evaluate();
		Result evaluationResult;
		try {
			Identifier evaluationResultId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY_CODE, 10);
			evaluationResult = evaluation.createResult(evaluationResultId,
																								 MeasurementControlModule.iAm.getUserId(),
																								 measurementResult.getMeasurement(),
																								 evaluationManager.getAlarmLevel(),
																								 erParameters);
		}
		catch (Exception coe) {
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
