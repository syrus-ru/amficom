/*
 * $Id: DatabaseMeasurementObjectLoader.java,v 1.40 2005/02/18 17:59:02 arseniy Exp $
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

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.40 $, $Date: 2005/02/18 17:59:02 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class DatabaseMeasurementObjectLoader implements MeasurementObjectLoader {

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

	public ModelingType loadModelingType(Identifier id) throws DatabaseException, CommunicationException {
		return new ModelingType(id);
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

	public Collection loadAnalyses(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadAnalysisTypes(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadEvaluations(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadEvaluationTypes(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadModelings(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadModelingTypes(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadMeasurements(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadMeasurementSetups(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadMeasurementTypes(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadResults(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadSets(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadTemporalPatterns(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadTests(Collection ids) throws DatabaseException, CommunicationException {
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





	public Collection loadAnalysesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadAnalysisTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadEvaluationsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadEvaluationTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadModelingsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadModelingTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadMeasurementsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadMeasurementSetupsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadMeasurementTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadResultsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadSetsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadTemporalPatternsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public Collection loadTestsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
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

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws DatabaseException,
			CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.measurementTypeDatabase;
		try {
			database.update(measurementType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementType | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementType | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementType | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementType | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveMeasurementType | VersionCollisionException: "
												+ e.getMessage());
		}
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws DatabaseException,
			CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			database.update(analysisType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisType | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisType | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisType | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisType | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws DatabaseException,
			CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			database.update(evaluationType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: "
												+ e.getMessage());
		}
	}

	public void saveSet(Set set, boolean force) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.setDatabase;
		try {
			database.update(set, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force) throws DatabaseException,
			CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.measurementSetupDatabase;
		try {
			database.update(measurementSetup, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: "
												+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: "
												+ e.getMessage());
		}
	}

	public void saveModeling(Modeling modeling, boolean force) throws DatabaseException, CommunicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.modelingDatabase;
		try {
			database.update(modeling, SessionContext.getAccessIdentity().getUserId(), 
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.modelingTypeDatabase;
		try {
			database.update(modelingType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModelingType | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModelingType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModelingType | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModelingType | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModelingType | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModelingType | VersionCollisionException: "
					+ e.getMessage());
		}

	}

	public void saveMeasurement(Measurement measurement, boolean force) throws DatabaseException,
			CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.measurementDatabase;
		try {
			database.update(measurement, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log
					.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: "
							+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.analysisDatabase;
		try {
			database.update(analysis, SessionContext.getAccessIdentity().getUserId(), 
				force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.evaluationDatabase;
		try {
			database.update(evaluation, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTest(Test test, boolean force) throws DatabaseException, CommunicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.testDatabase;
		try {
			database.update(test, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveResult(Result result, boolean force) throws DatabaseException, CommunicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.resultDatabase;
		try {
			database.update(result, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTemporalPattern(TemporalPattern temporalPattern, boolean force) throws DatabaseException,
			CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase) MeasurementDatabaseContext.temporalPatternDatabase;
		try {
			database.update(temporalPattern, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: "
												+ e.getMessage());
		}
	}





	public void saveMeasurementTypes(Collection list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.measurementTypeDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementTypes | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementTypes | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveMeasurementTypes | Illegal Storable Object: "
												+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementTypes | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveMeasurementTypes | VersionCollisionException: "
												+ e.getMessage());
		}
	}

	public void saveAnalysisTypes(Collection list, boolean force) throws DatabaseException, CommunicationException {
		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.analysisTypeDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisTypes | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisTypes | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisTypes | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisTypes | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysisTypes | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysisTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveEvaluationTypes(Collection list, boolean force) throws DatabaseException, CommunicationException {
		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.evaluationTypeDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluationType | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveEvaluationType | VersionCollisionException: "
												+ e.getMessage());
		}
	}

	public void saveSets(Collection list, boolean force) throws DatabaseException, CommunicationException {
		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.setDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveSet | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveModelings(Collection list, boolean force) throws DatabaseException, CommunicationException {
		ModelingDatabase database = (ModelingDatabase) MeasurementDatabaseContext.modelingDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModeling | VersionCollisionException: "
					+ e.getMessage());
		}
	}
	
	public void saveModelingTypes(Collection list, boolean force) throws DatabaseException, CommunicationException {
		ModelingTypeDatabase database = (ModelingTypeDatabase) MeasurementDatabaseContext.modelingTypeDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModelingType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModelingType | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModelingType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModelingType | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveModelingType | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveModelingType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveMeasurementSetups(Collection list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.measurementSetupDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurementSetup | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveMeasurementSetup | Illegal Storable Object: "
												+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveMeasurementSetup | VersionCollisionException: "
												+ e.getMessage());
		}
	}

	public void saveMeasurements(Collection list, boolean force) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase) MeasurementDatabaseContext.measurementDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log
					.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: "
							+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveMeasurement | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveAnalyses(Collection list, boolean force) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase) MeasurementDatabaseContext.analysisDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveAnalysis | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveEvaluations(Collection list, boolean force) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase) MeasurementDatabaseContext.evaluationDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveEvaluation | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTests(Collection list, boolean force) throws DatabaseException, CommunicationException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.testDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTest | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveResults(Collection list, boolean force) throws DatabaseException, CommunicationException {
		ResultDatabase database = (ResultDatabase) MeasurementDatabaseContext.resultDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveResult | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveTemporalPatterns(Collection list, boolean force) throws DatabaseException, CommunicationException {
		TemporalPatternDatabase database = (TemporalPatternDatabase) MeasurementDatabaseContext.temporalPatternDatabase;
		try {
			database.update(list, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		} catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | UpdateObjectException: "
					+ e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: "
					+ e.getMessage());
			throw new DatabaseException("DatabaseMeasumentObjectLoader.saveTemporalPattern | Illegal Storable Object: "
					+ e.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: "
					+ e.getMessage());
			throw new DatabaseException(
										"DatabaseMeasumentObjectLoader.saveTemporalPattern | VersionCollisionException: "
												+ e.getMessage());
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
				if (object instanceof Identified)
					identifier = ((Identified) object).getId();
				else
					throw new IllegalDataException("DatabaseMeasumentObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identified");

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
