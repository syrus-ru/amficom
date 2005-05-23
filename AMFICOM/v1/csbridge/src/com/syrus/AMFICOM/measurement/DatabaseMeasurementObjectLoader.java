/*-
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.6 2005/05/23 18:45:12 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/23 18:45:12 $
 * @author $Author: bass $
 * @module csbridge_v1
 */

public class DatabaseMeasurementObjectLoader extends DatabaseObjectLoader implements MeasurementObjectLoader {

	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MODELINGTYPE_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}



	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.ANALYSIS_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.EVALUATION_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MODELING_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.SET_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.RESULT_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.TEST_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		CronTemporalPatternDatabase database = (CronTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}
	
	public java.util.Set loadIntervalsTemporalPatterns(Set ids) throws ApplicationException {
		IntervalsTemporalPatternDatabase database = (IntervalsTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadPeriodicalTemporalPatterns(Set ids) throws ApplicationException {
		PeriodicalTemporalPatternDatabase database = (PeriodicalTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE);
		return super.retrieveFromDatabase(database, ids);
	}



	/* Load multiple objects but ids*/

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MODELINGTYPE_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.ANALYSIS_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.EVALUATION_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MODELING_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.SET_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.RESULT_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.TEST_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CronTemporalPatternDatabase database = (CronTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}
	
	public Set loadIntervalsTemporalPatternsButIds(	StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		IntervalsTemporalPatternDatabase database = (IntervalsTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		PeriodicalTemporalPatternDatabase database = (PeriodicalTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE);
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelingTypes(java.util.Set objects, boolean force) throws ApplicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MODELINGTYPE_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	public void saveMeasurements(java.util.Set objects, boolean force) throws ApplicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalyses(java.util.Set objects, boolean force) throws ApplicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.ANALYSIS_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluations(java.util.Set objects, boolean force) throws ApplicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.EVALUATION_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MODELING_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.SET_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveResults(java.util.Set objects, boolean force) throws ApplicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.RESULT_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTests(java.util.Set objects, boolean force) throws ApplicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.TEST_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCronTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CronTemporalPatternDatabase database = (CronTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveIntervalsTemporalPatterns(Set objects, boolean force) throws ApplicationException{
		IntervalsTemporalPatternDatabase database = (IntervalsTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}
	
	public void savePeriodicalTemporalPatterns(Set objects, boolean force) throws ApplicationException{
		PeriodicalTemporalPatternDatabase database = (PeriodicalTemporalPatternDatabase) MeasurementDatabaseContext.getDatabase(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE);
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}


	/*	Refresh*/

	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = MeasurementDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}



	/*	Delete*/

	public void delete(final java.util.Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final Short entityCode = new Short(identifiable.getId().getMajor());
			java.util.Set entityObjects = (java.util.Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final java.util.Set entityObjects = (java.util.Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = MeasurementDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
