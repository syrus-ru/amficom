/*
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.43 2005/03/04 13:34:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.43 $, $Date: 2005/03/04 13:34:22 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class DatabaseMeasurementObjectLoader implements MeasurementObjectLoader {

	public MeasurementType loadMeasurementType(Identifier id) throws ApplicationException {
		return new MeasurementType(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws ApplicationException {
		return new AnalysisType(id);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws ApplicationException {
		return new EvaluationType(id);
	}

	public Set loadSet(Identifier id) throws ApplicationException {
		return new Set(id);
	}

	public Modeling loadModeling(Identifier id) throws ApplicationException {
		return new Modeling(id);
	}

	public ModelingType loadModelingType(Identifier id) throws ApplicationException {
		return new ModelingType(id);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException {
		return new MeasurementSetup(id);
	}

	public Measurement loadMeasurement(Identifier id) throws ApplicationException {
		return new Measurement(id);
	}

	public Analysis loadAnalysis(Identifier id) throws ApplicationException {
		return new Analysis(id);
	}

	public Evaluation loadEvaluation(Identifier id) throws ApplicationException {
		return new Evaluation(id);
	}

	public Test loadTest(Identifier id) throws ApplicationException {
		return new Test(id);
	}

	public Result loadResult(Identifier id) throws ApplicationException {
		return new Result(id);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws ApplicationException {
		return new TemporalPattern(id);
	}





	// for multiple objects

	public Collection loadAnalyses(Collection ids) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.analysisDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadAnalysisTypes(Collection ids) throws ApplicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.analysisTypeDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadEvaluations(Collection ids) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.evaluationDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadEvaluationTypes(Collection ids) throws ApplicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.evaluationTypeDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluationTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadEvaluationTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadModelings(Collection ids) throws ApplicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.modelingDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log
					.errorMessage("DatabaseMeasumentObjectLoader.loadModelings | Illegal Storable Object: "
							+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadModelings | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadModelingTypes(Collection ids) throws ApplicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.modelingTypeDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadModelingTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadModelingTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadMeasurements(Collection ids) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.measurementDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurements | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadMeasurements | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadMeasurementSetups(Collection ids) throws ApplicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.measurementSetupDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadMeasurementTypes(Collection ids) throws ApplicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.measurementTypeDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadResults(Collection ids) throws ApplicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.resultDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadResults | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadResults | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadSets(Collection ids) throws ApplicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.setDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadSets | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadTemporalPatterns(Collection ids) throws ApplicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase) MeasurementDatabaseContext.temporalPatternDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadTests(Collection ids) throws ApplicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.testDatabase;
		Collection list = null;
		try {
			list = database.retrieveByIds(ids, null);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTests | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTests | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}





	public Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.analysisDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadAnalysisTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.analysisTypeDatabase;
		Collection list;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadAnalysisTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadAnalysisTypesButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.evaluationDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadEvaluationTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.evaluationTypeDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadEvaluationTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadEvaluationTypesButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadModelingsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.modelingDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadModelingsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadModelingsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.modelingTypeDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadModelingTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadModelingTypesButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.measurementDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.measurementSetupDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementSetupsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadMeasurementSetupsButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.measurementTypeDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadMeasurementTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadMeasurementTypesButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadResultsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.resultDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadResultsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadResultsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadSetsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.setDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadSetsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadSetsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}

	public Collection loadTemporalPatternsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase) MeasurementDatabaseContext.temporalPatternDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTemporalPatternsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.loadTemporalPatternsButIds | Illegal Storable Object: "
												+ e.getMessage());
		}
		return list;
	}

	public Collection loadTestsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.testDatabase;
		Collection list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.loadTestsButIds | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.loadTestsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return list;
	}




	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.measurementTypeDatabase;
		database.update(measurementType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws ApplicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.analysisTypeDatabase;
		database.update(analysisType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.evaluationTypeDatabase;
		database.update(evaluationType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSet(Set set, boolean force) throws ApplicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.setDatabase;
		database.update(set, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws ApplicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.measurementSetupDatabase;
		database.update(measurementSetup, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModeling(Modeling modeling, boolean force) throws ApplicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.modelingDatabase;
		database.update(modeling, SessionContext.getAccessIdentity().getUserId(), 
			force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.modelingTypeDatabase;
		database.update(modelingType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);

	}

	public void saveMeasurement(Measurement measurement, boolean force) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.measurementDatabase;
		database.update(measurement, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.analysisDatabase;
		database.update(analysis, SessionContext.getAccessIdentity().getUserId(), 
			force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.evaluationDatabase;
		database.update(evaluation, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTest(Test test, boolean force) throws ApplicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.testDatabase;
		database.update(test, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveResult(Result result, boolean force) throws ApplicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.resultDatabase;
		database.update(result, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws ApplicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase) MeasurementDatabaseContext.temporalPatternDatabase;
		database.update(temporalPattern, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}





	public void saveMeasurementTypes(Collection list, boolean force) throws ApplicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.measurementTypeDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysisTypes(Collection list, boolean force) throws ApplicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.analysisTypeDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluationTypes(Collection list, boolean force) throws ApplicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.evaluationTypeDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSets(Collection list, boolean force) throws ApplicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.setDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelings(Collection list, boolean force) throws ApplicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.modelingDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}
	
	public void saveModelingTypes(Collection list, boolean force) throws ApplicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.modelingTypeDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementSetups(Collection list, boolean force) throws ApplicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.measurementSetupDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurements(Collection list, boolean force) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.measurementDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalyses(Collection list, boolean force) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.analysisDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluations(Collection list, boolean force) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.evaluationDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTests(Collection list, boolean force) throws ApplicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.testDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveResults(Collection list, boolean force) throws ApplicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.resultDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTemporalPatterns(Collection list, boolean force) throws ApplicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase) MeasurementDatabaseContext.temporalPatternDatabase;
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}


	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}





	public void delete(Identifier id) throws IllegalDataException {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = MeasurementDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(Collection objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Collection entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identifiable)
					identifier = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("DatabaseMeasumentObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identifiable");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Collection) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new LinkedList();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Collection) map.get(entityCode);
			storableObjectDatabase = MeasurementDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
