/*
 * $Id: AnalysisEvaluationProcessor.java,v 1.4 2004/07/21 18:43:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.event.corba.AlarmLevel;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetParameter;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/21 18:43:32 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public abstract class AnalysisEvaluationProcessor {
	private static final String CODENAME_ANALYSIS_TYPE_DADARA = "dadara";
	private static final String CODENAME_EVALUATION_TYPE_DADARA = "dadara"; 

	private static Map analysisTypes;
	private static Map evaluationTypes;

	static {
		analysisTypes = new Hashtable(1);
		addAnalysisType(CODENAME_ANALYSIS_TYPE_DADARA);

		evaluationTypes = new Hashtable(1);
		addEvaluationType(CODENAME_EVALUATION_TYPE_DADARA);
	}

	public static Result[] analyseAndEvaluate(Result measurementResult,
																						Analysis analysis,
																						Evaluation evaluation,
																						Set etalon) throws AnalysisException, EvaluationException {
		AnalysisManager analysisManager;
		EvaluationManager evaluationManager;

		Identifier analysisTypeId = analysis.getTypeId();
		AnalysisType analysisType = (AnalysisType)analysisTypes.get(analysisTypeId);
		if (analysisType == null)
			throw new AnalysisException("Cannot find analysis of type '" + analysisTypeId.toString() + "'");

		Identifier evaluationTypeId = evaluation.getTypeId();
		EvaluationType evaluationType = (EvaluationType)evaluationTypes.get(evaluationTypeId);
		if (evaluationType == null)
			throw new EvaluationException("Cannot find evaluation of type '" + evaluationTypeId.toString() + "'");

		String analysisTypeCodename = analysisType.getCodename();
		String evaluationTypeCodename = evaluationType.getCodename();
		if (analysisTypeCodename.equals(CODENAME_ANALYSIS_TYPE_DADARA)) {
			analysisManager = new DadaraAnalysisManager(measurementResult,
																									analysis,
																									evaluation,
																									etalon);
			if (evaluationTypeCodename.equals(CODENAME_EVALUATION_TYPE_DADARA)) {
				evaluationManager = (EvaluationManager)analysisManager;
			}
			else
				throw new EvaluationException("Evaluation for codename '" + evaluationTypeCodename + "' not implemented");
		}
		else
			throw new AnalysisException("Analysis for codename '" + analysisTypeCodename + "' not implemented");

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

	private static void addAnalysisType(String codename) {
		try {
			AnalysisType analysisType = AnalysisTypeDatabase.retrieveForCodename(codename);
			Identifier analysisTypeId = analysisType.getId();
			if (!analysisTypes.containsKey(analysisTypeId))
				analysisTypes.put(analysisTypeId, analysisType);
			else
				Log.errorMessage("Analysis type of codename '" + codename + "' and id '" + analysisTypeId.toString() + "' already added to map");
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
	}

	private static void addEvaluationType(String codename) {
		try {
			EvaluationType evaluationType = EvaluationTypeDatabase.retrieveForCodename(codename);
			Identifier evaluationTypeId = evaluationType.getId();
			if (!evaluationTypes.containsKey(evaluationTypeId))
				evaluationTypes.put(evaluationTypeId, evaluationType);
			else
				Log.errorMessage("Evaluation type of codename '" + codename + "' and id '" + evaluationTypeId.toString() + "' already added to map");
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
	}
}
