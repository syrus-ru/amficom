/*
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.5 2004/09/27 12:24:10 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2004/09/27 12:24:10 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class DatabaseMeasurementObjectLoader implements MeasurementObjectLoader {

	public DatabaseMeasurementObjectLoader() {
	}

	public ParameterType loadParameterType(Identifier id) throws DatabaseException {
		return new ParameterType(id);
	}

	public MeasurementType loadMeasurementType(Identifier id) throws DatabaseException {
		return new MeasurementType(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException {
		return new AnalysisType(id);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws DatabaseException {
		return new EvaluationType(id);
	}

	public Set loadSet(Identifier id) throws DatabaseException {
		return new Set(id);
	}
	
	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {		
		return new Modeling(id);
	}
	
	public MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException {
		return new MeasurementSetup(id);
	}

	public Measurement loadMeasurement(Identifier id) throws DatabaseException {
		return new Measurement(id);
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException {
		return new Analysis(id);
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException {
		return new Evaluation(id);
	}

	public Test loadTest(Identifier id) throws DatabaseException {
		return new Test(id);
	}

	public Result loadResult(Identifier id) throws DatabaseException {
		return new Result(id);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException {
		return new TemporalPattern(id);
	}
    
	public List loadAnalyses(List ids) throws DatabaseException,
			CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadAnalysisTypes(List ids) throws DatabaseException,
			CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadEvaluations(List ids) throws DatabaseException,
			CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadEvaluationTypes(List ids) throws DatabaseException,
			CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluationTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadEvaluationTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadModelings(List ids) throws DatabaseException, CommunicationException {
		ModelingDatabase database = (ModelingDatabase)MeasurementDatabaseContext.getModelingDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadModelings | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadModelings | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
	
	public List loadMeasurements(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurements | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurements | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadMeasurementSetups(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadMeasurementTypes(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
		List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
	public List loadParameterTypes(List ids) throws DatabaseException,
			CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadResults(List ids) throws DatabaseException,
			CommunicationException {
		ResultDatabase database = (ResultDatabase)MeasurementDatabaseContext.getResultDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadResults | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadResults | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadSets(List ids) throws DatabaseException,
			CommunicationException {
		SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadTemporalPatterns(List ids) throws DatabaseException,
			CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
	public List loadTests(List ids) throws DatabaseException,
			CommunicationException {
		TestDatabase database = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("DatabaseMeasumentObjectLoader.loadTests | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTests | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
}
