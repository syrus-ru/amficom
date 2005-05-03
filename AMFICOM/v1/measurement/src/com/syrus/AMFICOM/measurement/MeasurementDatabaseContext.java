/*-
 * $Id: MeasurementDatabaseContext.java,v 1.29 2005/05/03 13:56:30 max Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.29 $, $Date: 2005/05/03 13:56:30 $
 * @author $Author: max $
 * @module measurement_v1
 */
public final class MeasurementDatabaseContext {
	private static MeasurementTypeDatabase	measurementTypeDatabase;
	private static AnalysisTypeDatabase	analysisTypeDatabase;
	private static EvaluationTypeDatabase	evaluationTypeDatabase;
	private static ModelingTypeDatabase	modelingTypeDatabase;
	
	private static SetDatabase		setDatabase;
	private static ModelingDatabase		modelingDatabase;
	private static MeasurementSetupDatabase	measurementSetupDatabase;
	private static MeasurementDatabase	measurementDatabase;
	private static AnalysisDatabase		analysisDatabase;
	private static EvaluationDatabase	evaluationDatabase;
	private static TestDatabase		testDatabase;
	private static ResultDatabase		resultDatabase;
	private static CronTemporalPatternDatabase	cronTemporalPatternDatabase;
	private static IntervalsTemporalPatternDatabase intervalsTemporalPatternDatabase;
	private static PeriodicalTemporalPatternDatabase periodicalTemporalPatternDatabase;

	private MeasurementDatabaseContext() {
		assert false;
	}

	public static void init(
			final MeasurementTypeDatabase	measurementTypeDatabase1,
			final AnalysisTypeDatabase	analysisTypeDatabase1,
			final EvaluationTypeDatabase	evaluationTypeDatabase1,
			final ModelingTypeDatabase	modelingTypeDatabase1,
			final SetDatabase		setDatabase1,
			final ModelingDatabase		modelingDatabase1,
			final MeasurementSetupDatabase	measurementSetupDatabase1,
			final MeasurementDatabase	measurementDatabase1,
			final AnalysisDatabase		analysisDatabase1,
			final EvaluationDatabase	evaluationDatabase1,
			final TestDatabase		testDatabase1,
			final ResultDatabase		resultDatabase1,
			final CronTemporalPatternDatabase	cronTemporalPatternDatabase1,
			final IntervalsTemporalPatternDatabase intervalsTemporalPatternDatabase1,
			final PeriodicalTemporalPatternDatabase periodicalTemporalPatternDatabase1) {
		if (measurementTypeDatabase1 != null)
			measurementTypeDatabase = measurementTypeDatabase1;
		if (analysisTypeDatabase1 != null)
			analysisTypeDatabase = analysisTypeDatabase1;
		if (evaluationTypeDatabase1 != null)
			evaluationTypeDatabase = evaluationTypeDatabase1;
		if (modelingTypeDatabase1 != null)
			modelingTypeDatabase = modelingTypeDatabase1;
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
		if (cronTemporalPatternDatabase1 != null)
			cronTemporalPatternDatabase = cronTemporalPatternDatabase1;
		if (intervalsTemporalPatternDatabase1 != null)
			intervalsTemporalPatternDatabase = intervalsTemporalPatternDatabase1;
		if (periodicalTemporalPatternDatabase1 != null)
			periodicalTemporalPatternDatabase = periodicalTemporalPatternDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				return getAnalysisDatabase();
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				return getAnalysisTypeDatabase();
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				return getEvaluationDatabase();
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				return getEvaluationTypeDatabase();
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				return getMeasurementDatabase();
			case ObjectEntities.MS_ENTITY_CODE:
				return getMeasurementSetupDatabase();
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				return getMeasurementTypeDatabase();
			case ObjectEntities.MODELING_ENTITY_CODE:
				return getModelingDatabase();
			case ObjectEntities.MODELINGTYPE_ENTITY_CODE:
				return getModelingTypeDatabase();
			case ObjectEntities.RESULT_ENTITY_CODE:
				return getResultDatabase();
			case ObjectEntities.SET_ENTITY_CODE:
				return getSetDatabase();
			case ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE:
				return getCronTemporalPatternDatabase();
			case ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE:
				return getIntervalsTemporalPatternDatabase();
			case ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE:
				return getPeriodicalTemporalPatternDatabase();
			case ObjectEntities.TEST_ENTITY_CODE:
				return getTestDatabase();
			default:
				Log.errorMessage("MeasurementDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;
		}
	}

	public static AnalysisDatabase getAnalysisDatabase() {
		return analysisDatabase;
	}

	public static AnalysisTypeDatabase getAnalysisTypeDatabase() {
		return analysisTypeDatabase;
	}

	public static EvaluationDatabase getEvaluationDatabase() {
		return evaluationDatabase;
	}

	public static EvaluationTypeDatabase getEvaluationTypeDatabase() {
		return evaluationTypeDatabase;
	}

	public static ModelingDatabase getModelingDatabase() {
		return modelingDatabase;
	}

	public static ModelingTypeDatabase getModelingTypeDatabase() {
		return modelingTypeDatabase;
	}

	public static MeasurementDatabase getMeasurementDatabase() {
		return measurementDatabase;
	}

	public static MeasurementSetupDatabase getMeasurementSetupDatabase() {
		return measurementSetupDatabase;
	}

	public static MeasurementTypeDatabase getMeasurementTypeDatabase() {
		return measurementTypeDatabase;
	}

	public static ResultDatabase getResultDatabase() {
		return resultDatabase;
	}

	public static SetDatabase getSetDatabase() {
		return setDatabase;
	}

	public static CronTemporalPatternDatabase getCronTemporalPatternDatabase() {
		return cronTemporalPatternDatabase;
	}

	public static IntervalsTemporalPatternDatabase getIntervalsTemporalPatternDatabase() {
		return intervalsTemporalPatternDatabase;
	}
	
	public static PeriodicalTemporalPatternDatabase getPeriodicalTemporalPatternDatabase() {
		return periodicalTemporalPatternDatabase;
	}
	
	public static TestDatabase getTestDatabase() {
		return testDatabase;
	}
}
