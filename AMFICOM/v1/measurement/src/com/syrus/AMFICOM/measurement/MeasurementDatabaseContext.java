/*
 * $Id: MeasurementDatabaseContext.java,v 1.21 2005/01/19 20:52:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.21 $, $Date: 2005/01/19 20:52:56 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementDatabaseContext {
	protected static StorableObjectDatabase	measurementTypeDatabase;
	protected static StorableObjectDatabase	analysisTypeDatabase;
	protected static StorableObjectDatabase	evaluationTypeDatabase;
	protected static StorableObjectDatabase	modelingTypeDatabase;
	
	protected static StorableObjectDatabase	setDatabase;
	protected static StorableObjectDatabase	modelingDatabase;
	protected static StorableObjectDatabase	measurementSetupDatabase;
	protected static StorableObjectDatabase	measurementDatabase;
	protected static StorableObjectDatabase	analysisDatabase;
	protected static StorableObjectDatabase	evaluationDatabase;
	protected static StorableObjectDatabase	testDatabase;
	protected static StorableObjectDatabase	resultDatabase;
	protected static StorableObjectDatabase	temporalPatternDatabase;

	private MeasurementDatabaseContext() {
		// empty
	}

	public static void init(final StorableObjectDatabase measurementTypeDatabase1,
													final StorableObjectDatabase analysisTypeDatabase1,
													final StorableObjectDatabase evaluationTypeDatabase1,
													final StorableObjectDatabase setDatabase1,
													final StorableObjectDatabase modelingDatabase1,
													final StorableObjectDatabase measurementSetupDatabase1,
													final StorableObjectDatabase measurementDatabase1,
													final StorableObjectDatabase analysisDatabase1,
													final StorableObjectDatabase evaluationDatabase1,
													final StorableObjectDatabase testDatabase1,
													final StorableObjectDatabase resultDatabase1,
													final StorableObjectDatabase temporalPatternDatabase1) {
		if (measurementTypeDatabase1 != null)
			measurementTypeDatabase = measurementTypeDatabase1;

		if (analysisTypeDatabase1 != null)
			analysisTypeDatabase = analysisTypeDatabase1;

		if (evaluationTypeDatabase1 != null)
			evaluationTypeDatabase = evaluationTypeDatabase1;

		if (setDatabase1 != null)
			setDatabase = setDatabase1;

		if (modelingDatabase1 != null)
			modelingDatabase = modelingDatabase1;

		if (measurementSetupDatabase1 != null)
			measurementSetupDatabase = measurementSetupDatabase1;

		if (measurementDatabase1 != null)
			measurementDatabase = measurementDatabase1;

		if (analysisDatabase1 != null)
			analysisDatabase = analysisDatabase1;

		if (evaluationDatabase1 != null)
			evaluationDatabase = evaluationDatabase1;

		if (testDatabase1 != null)
			testDatabase = testDatabase1;

		if (resultDatabase1 != null)
			resultDatabase = resultDatabase1;

		if (temporalPatternDatabase1 != null)
			temporalPatternDatabase = temporalPatternDatabase1;

	}

	public static StorableObjectDatabase getDatabase(short entityCode ) {
		switch (entityCode) {

			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				return analysisDatabase;
			
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				return analysisTypeDatabase;

			case ObjectEntities.EVALUATION_ENTITY_CODE:
				return evaluationDatabase;

			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				return evaluationTypeDatabase;

			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				return measurementDatabase;

			case ObjectEntities.MS_ENTITY_CODE:
				return measurementSetupDatabase;

			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				return measurementTypeDatabase;

			case ObjectEntities.MODELING_ENTITY_CODE:
				return modelingDatabase;

			case ObjectEntities.RESULT_ENTITY_CODE:
				return resultDatabase;

			case ObjectEntities.SET_ENTITY_CODE:
				return setDatabase;

			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				return temporalPatternDatabase;

			case ObjectEntities.TEST_ENTITY_CODE:
				return testDatabase;

			default:
				return null;
		}
	}

	public static StorableObjectDatabase getAnalysisDatabase() {
		return analysisDatabase;
	}
	public static StorableObjectDatabase getAnalysisTypeDatabase() {
		return analysisTypeDatabase;
	}
	public static StorableObjectDatabase getEvaluationDatabase() {
		return evaluationDatabase;
	}
	public static StorableObjectDatabase getEvaluationTypeDatabase() {
		return evaluationTypeDatabase;
	}	
	public static StorableObjectDatabase getModelingDatabase() {
		return modelingDatabase;
	}
	public static StorableObjectDatabase getMeasurementDatabase() {
		return measurementDatabase;
	}
	public static StorableObjectDatabase getMeasurementSetupDatabase() {
		return measurementSetupDatabase;
	}
	public static StorableObjectDatabase getMeasurementTypeDatabase() {
		return measurementTypeDatabase;
	}
	public static StorableObjectDatabase getResultDatabase() {
		return resultDatabase;
	}
	public static StorableObjectDatabase getSetDatabase() {
		return setDatabase;
	}
	public static StorableObjectDatabase getTemporalPatternDatabase() {
		return temporalPatternDatabase;
	}
	public static StorableObjectDatabase getTestDatabase() {
		return testDatabase;
	}
}
