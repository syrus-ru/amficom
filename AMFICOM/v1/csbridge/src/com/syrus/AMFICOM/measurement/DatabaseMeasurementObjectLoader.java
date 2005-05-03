/*-
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.5 2005/05/03 15:26:06 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/03 15:26:06 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */

public class DatabaseMeasurementObjectLoader extends DatabaseObjectLoader implements MeasurementObjectLoader {

	/* Load multiple objects*/

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		ModelingTypeDatabase database = MeasurementDatabaseContext.getModelingTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}



	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		ModelingDatabase database = MeasurementDatabaseContext.getModelingDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		ResultDatabase database = MeasurementDatabaseContext.getResultDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		TestDatabase database = MeasurementDatabaseContext.getTestDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getCronTemporalPatternDatabase();
		return super.retrieveFromDatabase(database, ids);
	}
	
	public java.util.Set loadIntervalsTemporalPatterns(Set ids) throws ApplicationException {
		IntervalsTemporalPatternDatabase database = MeasurementDatabaseContext.getIntervalsTemporalPatternDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public java.util.Set loadPeriodicalTemporalPatterns(Set ids) throws ApplicationException {
		PeriodicalTemporalPatternDatabase database = MeasurementDatabaseContext.getPeriodicalTemporalPatternDatabase();
		return super.retrieveFromDatabase(database, ids);
	}



	/* Load multiple objects but ids*/

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		ModelingTypeDatabase database = MeasurementDatabaseContext.getModelingTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	public java.util.Set loadMeasurementsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		ModelingDatabase database = MeasurementDatabaseContext.getModelingDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		ResultDatabase database = MeasurementDatabaseContext.getResultDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		TestDatabase database = MeasurementDatabaseContext.getTestDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition condition, java.util.Set ids)
			throws ApplicationException {
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getCronTemporalPatternDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}
	
	public Set loadIntervalsTemporalPatternsButIds(	StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		IntervalsTemporalPatternDatabase database = MeasurementDatabaseContext.getIntervalsTemporalPatternDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition condition,
													Set ids) throws ApplicationException {
		PeriodicalTemporalPatternDatabase database = MeasurementDatabaseContext.getPeriodicalTemporalPatternDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set objects, boolean force) throws ApplicationException {
		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysisTypes(java.util.Set objects, boolean force) throws ApplicationException {
		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluationTypes(java.util.Set objects, boolean force) throws ApplicationException {
		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelingTypes(java.util.Set objects, boolean force) throws ApplicationException {
		ModelingTypeDatabase database = MeasurementDatabaseContext.getModelingTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	public void saveMeasurements(java.util.Set objects, boolean force) throws ApplicationException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalyses(java.util.Set objects, boolean force) throws ApplicationException {
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluations(java.util.Set objects, boolean force) throws ApplicationException {
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelings(java.util.Set objects, boolean force) throws ApplicationException {
		ModelingDatabase database = MeasurementDatabaseContext.getModelingDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementSetups(java.util.Set objects, boolean force) throws ApplicationException {
		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSets(java.util.Set objects, boolean force) throws ApplicationException {
		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveResults(java.util.Set objects, boolean force) throws ApplicationException {
		ResultDatabase database = MeasurementDatabaseContext.getResultDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTests(java.util.Set objects, boolean force) throws ApplicationException {
		TestDatabase database = MeasurementDatabaseContext.getTestDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCronTemporalPatterns(java.util.Set objects, boolean force) throws ApplicationException {
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getCronTemporalPatternDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveIntervalsTemporalPatterns(Set objects, boolean force) throws ApplicationException{
		IntervalsTemporalPatternDatabase database = MeasurementDatabaseContext.getIntervalsTemporalPatternDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}
	
	public void savePeriodicalTemporalPatterns(Set objects, boolean force) throws ApplicationException{
		PeriodicalTemporalPatternDatabase database = MeasurementDatabaseContext.getPeriodicalTemporalPatternDatabase();
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
