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
	
	private MeasurementDatabaseContext() {
	}

	public static void init(StorableObjectDatabase analysisDatabase1,
													StorableObjectDatabase analysisTypeDatabase1,
													StorableObjectDatabase evaluationDatabase1,
													StorableObjectDatabase evaluationTypeDatabase1,
													StorableObjectDatabase measurementDatabase1,
													StorableObjectDatabase measurementSetupDatabase1,
													StorableObjectDatabase measurementTypeDatabase1,
													StorableObjectDatabase parameterTypeDatabase1,
													StorableObjectDatabase resultDatabase1,
													StorableObjectDatabase setDatabase1,
													StorableObjectDatabase temporalPatternDatabase1,
													StorableObjectDatabase testDatabase1) {
		analysisDatabase = analysisDatabase1;
		analysisTypeDatabase = analysisTypeDatabase1;
		evaluationDatabase = evaluationDatabase1;
		evaluationTypeDatabase = evaluationTypeDatabase1;
		measurementDatabase = measurementDatabase1;
		measurementSetupDatabase = measurementSetupDatabase1;
		measurementTypeDatabase = measurementTypeDatabase1;
		parameterTypeDatabase = parameterTypeDatabase1;
		resultDatabase = resultDatabase1;
		setDatabase = setDatabase1;
		temporalPatternDatabase = temporalPatternDatabase1;
		testDatabase = testDatabase1;
	}

//	Эти методы должны быть в приложении с БД.
//
//	private static void loadParameterTypes() {
//		parameterTypes = new Hashtable(ParameterTypeDatabase.CHARACTER_NUMBER_OF_RECORDS);
//		try {
//			List types = ParameterTypeDatabase.retrieveAll();
//			for (Iterator iterator = types.iterator(); iterator.hasNext();)
//				addParameterType((ParameterType)iterator.next());
//		}
//		catch (RetrieveObjectException roe) {
//			Log.errorException(roe);
//		}		
//	}
//	
//	private static void loadActionTypes() {
//		actionTypes = new Hashtable(MeasurementTypeDatabase.CHARACTER_NUMBER_OF_RECORDS + AnalysisTypeDatabase.CHARACTER_NUMBER_OF_RECORDS + EvaluationTypeDatabase.CHARACTER_NUMBER_OF_RECORDS);
//		List types;
//		try {
//			types = MeasurementTypeDatabase.retrieveAll();
//			for (Iterator iterator = types.iterator(); iterator.hasNext();)
//				addActionType((MeasurementType)iterator.next());
//
//			types = AnalysisTypeDatabase.retrieveAll();
//			for (Iterator iterator = types.iterator(); iterator.hasNext();)
//				addActionType((AnalysisType)iterator.next());
//
//			types = EvaluationTypeDatabase.retrieveAll();
//			for (Iterator iterator = types.iterator(); iterator.hasNext();)
//				addActionType((EvaluationType)iterator.next());
//		}
//		catch (RetrieveObjectException roe) {
//			Log.errorException(roe);
//		}
//	}
}
