package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObject_Database;

public abstract class MeasurementDatabaseContext {

	protected static StorableObject_Database	analysisDatabase;
	protected static StorableObject_Database	analysisTypeDatabase;
	protected static StorableObject_Database	evaluationDatabase;
	protected static StorableObject_Database	evaluationTypeDatabase;
	protected static StorableObject_Database	measurementDatabase;
	protected static StorableObject_Database	measurementSetupDatabase;
	protected static StorableObject_Database	measurementTypeDatabase;
	protected static StorableObject_Database	parameterTypeDatabase;
	protected static StorableObject_Database	resultDatabase;
	protected static StorableObject_Database	setDatabase;
	protected static StorableObject_Database	temporalPatternDatabase;
	protected static StorableObject_Database	testDatabase;

	public static void init(StorableObject_Database analysisDatabase,
			StorableObject_Database analysisTypeDatabase,
			StorableObject_Database evaluationDatabase,
			StorableObject_Database evaluationTypeDatabase,
			StorableObject_Database measurementDatabase,
			StorableObject_Database measurementSetupDatabase,
			StorableObject_Database measurementTypeDatabase,
			StorableObject_Database parameterTypeDatabase,
			StorableObject_Database resultDatabase,
			StorableObject_Database setDatabase,
			StorableObject_Database temporalPatternDatabase,
			StorableObject_Database testDatabase) {
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