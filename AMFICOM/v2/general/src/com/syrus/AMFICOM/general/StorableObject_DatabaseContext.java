package com.syrus.AMFICOM.general;

public class StorableObject_DatabaseContext {
	/*	Object Types	*/
	public static StorableObject_Database parameterTypeDatabase;
	public static StorableObject_Database measurementTypeDatabase;
	public static StorableObject_Database analysisTypeDatabase;
	public static StorableObject_Database evaluationTypeDatabase;

	/*	Administration	*/
	public static StorableObject_Database mcmDatabase;
	public static StorableObject_Database serverDatabase;
	public static StorableObject_Database userDatabase;

	/*	Configuration	*/
	public static StorableObject_Database monitoredElementDatabase;
	public static StorableObject_Database kisDatabase;

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

													StorableObject_Database mcmDatabase1,
													StorableObject_Database serverDatabase1,
													StorableObject_Database userDatabase1,

													StorableObject_Database monitoredElementDatabase1,
													StorableObject_Database kisDatabase1,

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

		mcmDatabase = mcmDatabase1;
		serverDatabase = serverDatabase1;
		userDatabase = userDatabase1;

		monitoredElementDatabase = monitoredElementDatabase1;
		kisDatabase = kisDatabase1;

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