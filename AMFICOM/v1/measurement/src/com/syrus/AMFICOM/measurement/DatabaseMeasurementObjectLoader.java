/*-
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.55 2005/04/22 16:12:48 arseniy Exp $
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

import com.syrus.AMFICOM.general.AbstractObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.55 $, $Date: 2005/04/22 16:12:48 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class DatabaseMeasurementObjectLoader extends AbstractObjectLoader implements MeasurementObjectLoader {

	/* Load single object*/

	public MeasurementType loadMeasurementType(Identifier id) throws ApplicationException {
		return new MeasurementType(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws ApplicationException {
		return new AnalysisType(id);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws ApplicationException {
		return new EvaluationType(id);
	}

	public ModelingType loadModelingType(Identifier id) throws ApplicationException {
		return new ModelingType(id);
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

	public Modeling loadModeling(Identifier id) throws ApplicationException {
		return new Modeling(id);
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException {
		return new MeasurementSetup(id);
	}

	public Set loadSet(Identifier id) throws ApplicationException {
		return new Set(id);
	}

	public Result loadResult(Identifier id) throws ApplicationException {
		return new Result(id);
	}

	public Test loadTest(Identifier id) throws ApplicationException {
		return new Test(id);
	}

	public CronTemporalPattern loadCronTemporalPattern(Identifier id) throws ApplicationException {
		return new CronTemporalPattern(id);
	}



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
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getTemporalPatternDatabase();
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
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getTemporalPatternDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}



	/* Save single object*/

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException {
		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		database.update(measurementType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws ApplicationException {
		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		database.update(analysisType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws ApplicationException {
		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		database.update(evaluationType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws ApplicationException {
		ModelingTypeDatabase database = MeasurementDatabaseContext.getModelingTypeDatabase();
		database.update(modelingType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}



	public void saveMeasurement(Measurement measurement, boolean force) throws ApplicationException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		database.update(measurement, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws ApplicationException {
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		database.update(analysis, SessionContext.getAccessIdentity().getUserId(), 
			force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws ApplicationException {
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		database.update(evaluation, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModeling(Modeling modeling, boolean force) throws ApplicationException {
		ModelingDatabase database = MeasurementDatabaseContext.getModelingDatabase();
		database.update(modeling, SessionContext.getAccessIdentity().getUserId(), 
			force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws ApplicationException {
		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		database.update(measurementSetup, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSet(Set set, boolean force) throws ApplicationException {
		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		database.update(set, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveResult(Result result, boolean force) throws ApplicationException {
		ResultDatabase database = MeasurementDatabaseContext.getResultDatabase();
		database.update(result, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTest(Test test, boolean force) throws ApplicationException {
		TestDatabase database = MeasurementDatabaseContext.getTestDatabase();
		database.update(test, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCronTemporalPattern(CronTemporalPattern cronTemporalPattern, boolean force) throws ApplicationException {
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getTemporalPatternDatabase();
		database.update(cronTemporalPattern, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
				: StorableObjectDatabase.UPDATE_CHECK);
	}



	/* Save multiple objects*/

	public void saveMeasurementTypes(java.util.Set list, boolean force) throws ApplicationException {
		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalysisTypes(java.util.Set list, boolean force) throws ApplicationException {
		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluationTypes(java.util.Set list, boolean force) throws ApplicationException {
		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelingTypes(java.util.Set list, boolean force) throws ApplicationException {
		ModelingTypeDatabase database = MeasurementDatabaseContext.getModelingTypeDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}



	public void saveMeasurements(java.util.Set list, boolean force) throws ApplicationException {
		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveAnalyses(java.util.Set list, boolean force) throws ApplicationException {
		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveEvaluations(java.util.Set list, boolean force) throws ApplicationException {
		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveModelings(java.util.Set list, boolean force) throws ApplicationException {
		ModelingDatabase database = MeasurementDatabaseContext.getModelingDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveMeasurementSetups(java.util.Set list, boolean force) throws ApplicationException {
		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveSets(java.util.Set list, boolean force) throws ApplicationException {
		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveResults(java.util.Set list, boolean force) throws ApplicationException {
		ResultDatabase database = MeasurementDatabaseContext.getResultDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveTests(java.util.Set list, boolean force) throws ApplicationException {
		TestDatabase database = MeasurementDatabaseContext.getTestDatabase();
		database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCronTemporalPatterns(java.util.Set list, boolean force) throws ApplicationException {
		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getTemporalPatternDatabase();
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



	public void delete(Identifier id) {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = MeasurementDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

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
