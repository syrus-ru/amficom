/*
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.2 2004/09/14 16:09:12 max Exp $
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

/**
 * @version $Revision: 1.2 $, $Date: 2004/09/14 16:09:12 $
 * @author $Author: max $
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
        return database.retrieveByIds(ids, null);
	}
    
	public List loadAnalysisTypes(List ids) throws DatabaseException,
			CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadEvaluations(List ids) throws DatabaseException,
			CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadEvaluationTypes(List ids) throws DatabaseException,
			CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadMeasurements(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadMeasurementSetups(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadMeasurementTypes(List ids) throws DatabaseException,
			CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
		return database.retrieveByIds(ids, null);
    }
    
	public List loadParameterTypes(List ids) throws DatabaseException,
			CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadResults(List ids) throws DatabaseException,
			CommunicationException {
		ResultDatabase database = (ResultDatabase)MeasurementDatabaseContext.getResultDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadSets(List ids) throws DatabaseException,
			CommunicationException {
		SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadTemporalPatterns(List ids) throws DatabaseException,
			CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
        return database.retrieveByIds(ids, null);
	}
    
	public List loadTests(List ids) throws DatabaseException,
			CommunicationException {
		TestDatabase database = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
        return database.retrieveByIds(ids, null);
	}
}
