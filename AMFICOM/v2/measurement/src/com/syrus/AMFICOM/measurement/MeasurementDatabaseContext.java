package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.StorableObject_Database;

public class MeasurementDatabaseContext {
	/*	Object Types	*/
	public static StorableObject_Database parameterTypeDatabase;
	public static StorableObject_Database measurementTypeDatabase;
	public static StorableObject_Database analysisTypeDatabase;
	public static StorableObject_Database evaluationTypeDatabase;

	/*	Measurement	*/
	public static StorableObject_Database setDatabase;
	public static StorableObject_Database measurementSetupDatabase;
	public static StorableObject_Database measurementDatabase;
	public static StorableObject_Database analysisDatabase;
	public static StorableObject_Database evaluationDatabase;
	public static StorableObject_Database testDatabase;
	public static StorableObject_Database resultDatabase;
	public static StorableObject_Database ptTemporalTemplateDatabase;

	public static void init(StorableObject_Database parameterTypeDatabase1,
													StorableObject_Database measurementTypeDatabase1,
													StorableObject_Database analysisTypeDatabase1,
													StorableObject_Database evaluationTypeDatabase1,

													StorableObject_Database setDatabase1, 
													StorableObject_Database measurementSetupDatabase1,
													StorableObject_Database measurementDatabase1,
													StorableObject_Database analysisDatabase1,
													StorableObject_Database evaluationDatabase1,
													StorableObject_Database testDatabase1,
													StorableObject_Database resultDatabase1,
													StorableObject_Database ptTemporalTemplateDatabase1) {
		parameterTypeDatabase = parameterTypeDatabase1;
		measurementTypeDatabase = measurementTypeDatabase1;
		analysisTypeDatabase = analysisTypeDatabase1;
		evaluationTypeDatabase = evaluationTypeDatabase1;

		setDatabase = setDatabase1;
		measurementSetupDatabase = measurementSetupDatabase1;
		measurementDatabase = measurementDatabase1;
		analysisDatabase = analysisDatabase1;
		evaluationDatabase = evaluationDatabase1;
		testDatabase = testDatabase1;
		resultDatabase = resultDatabase1;
		ptTemporalTemplateDatabase = ptTemporalTemplateDatabase1;
	}
}