/*
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.31 2005/01/19 20:52:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2005/01/19 20:52:56 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class DatabaseMeasurementObjectLoader implements MeasurementObjectLoader {

	/**
	 * delete storable objects of one kind of entity
	 * @param id
	 * @param ids
	 * @throws DatabaseException
	 */
	private void delete(Identifier id, List ids) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (ids.isEmpty())
				return;
			Object obj = ids.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier)obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified)obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(entityCode);
			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (ids != null && !ids.isEmpty()) {
						database.delete(ids);
					}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.delete | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.delete | DatabaseException: " + e.getMessage());
		}
	}
	
	public void delete(Identifier id) throws DatabaseException, CommunicationException {
		delete(id, null);		
	}

	public void delete(List ids) throws DatabaseException, CommunicationException {
		if (ids == null || ids.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map 
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity 
		 */
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier)object;
			else
				if (object instanceof Identified)
					identifier = ((Identified)object).getId();
				else
					throw new DatabaseException("DatabaseMeasumentObjectLoader.delete | Object " + object.getClass().getName() + " isn't Identifier or Identified");
			Short entityCode = new Short(identifier.getMajor());
			List list = (List)map.get(entityCode);
			if (list == null) {
				list = new LinkedList();
				map.put(entityCode, list);
			}
			list.add(object);
		}

		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			List list = (List)map.get(entityCode);
			delete(null, list);
		}
	}


	public MeasurementType loadMeasurementType(Identifier id) throws DatabaseException, CommunicationException {
		return new MeasurementType(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException {
		return new AnalysisType(id);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws DatabaseException, CommunicationException {
		return new EvaluationType(id);
	}

	public Set loadSet(Identifier id) throws DatabaseException, CommunicationException {
		return new Set(id);
	}
	
	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		return new Modeling(id);
	}
	
	public MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException, CommunicationException {
		return new MeasurementSetup(id);
	}

	public Measurement loadMeasurement(Identifier id) throws DatabaseException, CommunicationException {
		return new Measurement(id);
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException, CommunicationException {
		return new Analysis(id);
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException, CommunicationException {
		return new Evaluation(id);
	}

	public Test loadTest(Identifier id) throws DatabaseException, CommunicationException {
		return new Test(id);
	}

	public Result loadResult(Identifier id) throws DatabaseException, CommunicationException {
		return new Result(id);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException, CommunicationException {
		return new TemporalPattern(id);
	}


  // for multiple objects

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadEvaluations(List ids) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
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
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadModelings | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadModelings | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurements(List ids) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurements | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurements | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadResults(List ids) throws DatabaseException, CommunicationException {
		ResultDatabase database = (ResultDatabase)MeasurementDatabaseContext.getResultDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadResults | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadResults | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadSets(List ids) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadTests(List ids) throws DatabaseException, CommunicationException {
		TestDatabase database = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTests | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTests | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadAnalysesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
		    Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + e.getMessage());
		    throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadAnalysisTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
		List list;
		try {
			list =  database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalysisTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalysisTypesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadEvaluationsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadEvaluationTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluationTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadEvaluationTypesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadModelingsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		ModelingDatabase database = (ModelingDatabase)MeasurementDatabaseContext.getModelingDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadModelingsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadModelingsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementSetupsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementSetupsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurementSetupsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadMeasurementTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurementTypesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}


	public List loadResultsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		ResultDatabase database = (ResultDatabase)MeasurementDatabaseContext.getResultDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadResultsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadResultsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadSetsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadSetsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadSetsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadTemporalPatternsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTemporalPatternsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTemporalPatternsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadTestsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		TestDatabase database = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTestsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTestsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}


	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws DatabaseException, CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
		try {
			database.update(measurementType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws DatabaseException, CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
		try {
			database.update(analysisType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws DatabaseException, CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase();
		try {
			database.update(evaluationType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveSet(Set set, boolean force) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
		try {
			database.update(set, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws DatabaseException, CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
		try {
			database.update(measurementSetup, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveModeling(Modeling modeling, boolean force) throws DatabaseException, CommunicationException {
		ModelingDatabase database = (ModelingDatabase)MeasurementDatabaseContext.getModelingDatabase();
		try {
			database.update(modeling, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurement(Measurement measurement, boolean force) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		try {
			database.update(measurement, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		try {
			database.update(analysis, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		try {
			database.update(evaluation, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveTest(Test test, boolean force) throws DatabaseException, CommunicationException {
		TestDatabase database = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
		try {
			database.update(test, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveResult(Result result, boolean force) throws DatabaseException, CommunicationException {
		ResultDatabase database = (ResultDatabase)MeasurementDatabaseContext.getResultDatabase();
		try {
			database.update(result, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws DatabaseException, CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
		try {
			database.update(temporalPattern, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: " + e.getMessage());
		}
	}


	public void saveMeasurementTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementTypes | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementTypes | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementTypes | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveAnalysisTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisTypes | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisTypes | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisTypes | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveEvaluationTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveSets(List list, boolean force) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveModelings(List list, boolean force) throws DatabaseException, CommunicationException {
		ModelingDatabase database = (ModelingDatabase)MeasurementDatabaseContext.getModelingDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurementSetups(List list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveMeasurements(List list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveAnalyses(List list, boolean force) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveEvaluations(List list, boolean force) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveTests(List list, boolean force) throws DatabaseException, CommunicationException {
		TestDatabase database = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveResults(List list, boolean force) throws DatabaseException, CommunicationException {
		ResultDatabase database = (ResultDatabase)MeasurementDatabaseContext.getResultDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveTemporalPatterns(List list, boolean force) throws DatabaseException, CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: " + e.getMessage());
		}
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws DatabaseException, CommunicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		try {
			StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(entityCode);

			if (database != null)
				return database.refresh(storableObjects);

      return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.refresh | DatabaseException: " + e.getMessage(), e);
		}
	}

}
