/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.6 2004/07/28 16:02:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2004/07/28 16:02:00 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class AnalysisEvaluationProcessor {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";
	private static final String CODENAME_EVALUATION_TYPE_DADARA = "dadara"; 

	public static Result[] analyseAndEvaluate(Result measurementResult,
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
			Identifier analysisResultId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY, 10);
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
			Identifier evaluationResultId = NewIdentifierPool.getGeneratedIdentifier(ObjectEntities.RESULT_ENTITY, 10);
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
