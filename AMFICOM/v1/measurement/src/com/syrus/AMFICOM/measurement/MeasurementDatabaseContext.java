/*
 * $Id: MeasurementDatabaseContext.java,v 1.17 2004/10/25 14:56:33 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;



import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.17 $, $Date: 2004/10/25 14:56:33 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementDatabaseContext {
	protected static StorableObjectDatabase	parameterTypeDatabase;
	protected static StorableObjectDatabase	measurementTypeDatabase;
	protected static StorableObjectDatabase	analysisTypeDatabase;
	protected static StorableObjectDatabase	evaluationTypeDatabase;
	
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

	public static void init(StorableObjectDatabase parameterTypeDatabase1,
													StorableObjectDatabase measurementTypeDatabase1,
													StorableObjectDatabase analysisTypeDatabase1,
													StorableObjectDatabase evaluationTypeDatabase1,
													StorableObjectDatabase setDatabase1,
													StorableObjectDatabase modelingDatabase1,
													StorableObjectDatabase measurementSetupDatabase1,
													StorableObjectDatabase measurementDatabase1,
													StorableObjectDatabase analysisDatabase1,
													StorableObjectDatabase evaluationDatabase1,
													StorableObjectDatabase testDatabase1,
													StorableObjectDatabase resultDatabase1,
													StorableObjectDatabase temporalPatternDatabase1) {
		parameterTypeDatabase = parameterTypeDatabase1;
		measurementTypeDatabase = measurementTypeDatabase1;
		analysisTypeDatabase = analysisTypeDatabase1;
		evaluationTypeDatabase = evaluationTypeDatabase1;

		setDatabase = setDatabase1;
		modelingDatabase = modelingDatabase1;
		measurementSetupDatabase = measurementSetupDatabase1;
		measurementDatabase = measurementDatabase1;
		analysisDatabase = analysisDatabase1;
		evaluationDatabase = evaluationDatabase1;
		testDatabase = testDatabase1;
		resultDatabase = resultDatabase1;
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
                
            case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
                return parameterTypeDatabase;
                
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
	public static StorableObjectDatabase getParameterTypeDatabase() {
		return parameterTypeDatabase;
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
