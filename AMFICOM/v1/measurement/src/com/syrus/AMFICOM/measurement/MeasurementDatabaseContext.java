package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

public abstract class MeasurementDatabaseContext {

	protected static StorableObjectDatabase	analysisDatabase;
	protected static StorableObjectDatabase	analysisTypeDatabase;
	protected static StorableObjectDatabase	evaluationDatabase;
	protected static StorableObjectDatabase	evaluationTypeDatabase;
	protected static StorableObjectDatabase	measurementDatabase;
	protected static StorableObjectDatabase	measurementSetupDatabase;
	protected static StorableObjectDatabase	measurementTypeDatabase;
	protected static StorableObjectDatabase	parameterTypeDatabase;
	protected static StorableObjectDatabase	resultDatabase;
	protected static StorableObjectDatabase	setDatabase;
	protected static StorableObjectDatabase	temporalPatternDatabase;
	protected static StorableObjectDatabase	testDatabase;

	public static void init(StorableObjectDatabase analysisDatabase,
													StorableObjectDatabase analysisTypeDatabase,
													StorableObjectDatabase evaluationDatabase,
													StorableObjectDatabase evaluationTypeDatabase,
													StorableObjectDatabase measurementDatabase,
													StorableObjectDatabase measurementSetupDatabase,
													StorableObjectDatabase measurementTypeDatabase,
													StorableObjectDatabase parameterTypeDatabase,
													StorableObjectDatabase resultDatabase,
													StorableObjectDatabase setDatabase,
													StorableObjectDatabase temporalPatternDatabase,
													StorableObjectDatabase testDatabase) {
		MeasurementDatabaseContext.analysisDatabase = analysisDatabase;
		MeasurementDatabaseContext.analysisTypeDatabase = analysisTypeDatabase;
		MeasurementDatabaseContext.evaluationDatabase = evaluationDatabase;
		MeasurementDatabaseContext.evaluationTypeDatabase = evaluationTypeDatabase;
		MeasurementDatabaseContext.measurementDatabase = measurementDatabase;
		MeasurementDatabaseContext.measurementSetupDatabase = measurementSetupDatabase;
		MeasurementDatabaseContext.measurementTypeDatabase = measurementTypeDatabase;
		MeasurementDatabaseContext.parameterTypeDatabase = parameterTypeDatabase;
		MeasurementDatabaseContext.resultDatabase = resultDatabase;
		MeasurementDatabaseContext.setDatabase = setDatabase;
		MeasurementDatabaseContext.temporalPatternDatabase = temporalPatternDatabase;
		MeasurementDatabaseContext.testDatabase = testDatabase;
	}
}
