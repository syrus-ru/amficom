/*
 * $Id: MeasurementStorableObjectPool.java,v 1.79 2005/04/12 08:18:25 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashMap;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.79 $, $Date: 2005/04/12 08:18:25 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class MeasurementStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 14; /* Number of entities */

	private static final int MEASUREMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int ANALYSISTYPE_OBJECT_POOL_SIZE = 1;
	private static final int EVALUATIONTYPE_OBJECT_POOL_SIZE = 1;
	private static final int MODELINGTYPE_OBJECT_POOL_SIZE = 1;

	private static final int SET_OBJECT_POOL_SIZE = 4;
	private static final int MODELING_OBJECT_POOL_SIZE = 4;
	private static final int MS_OBJECT_POOL_SIZE = 4;
	private static final int MEASUREMENT_OBJECT_POOL_SIZE = 4;
	private static final int ANALYSIS_OBJECT_POOL_SIZE = 4;
	private static final int EVALUATION_OBJECT_POOL_SIZE = 4;
	private static final int TEST_OBJECT_POOL_SIZE = 2;
	private static final int RESULT_OBJECT_POOL_SIZE = 4;
	private static final int TEMPORALPATTERN_OBJECT_POOL_SIZE = 2;

	private static MeasurementObjectLoader mObjectLoader;
	private static MeasurementStorableObjectPool instance;

	private MeasurementStorableObjectPool() {
		// singleton
		super(ObjectGroupEntities.MEASUREMENT_GROUP_CODE);
	}

	private MeasurementStorableObjectPool(Class cacheMapClass) {
		super(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, cacheMapClass);
	}

	public static void init(MeasurementObjectLoader mObjectLoader1, final int size) {
		if (instance == null)
			instance = new MeasurementStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		mObjectLoader = mObjectLoader1;

		instance.addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MODELINGTYPE_ENTITY_CODE, size);

		instance.addObjectPool(ObjectEntities.SET_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MODELING_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MS_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MEASUREMENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.ANALYSIS_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EVALUATION_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TEST_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.RESULT_ENTITY_CODE, size);

		instance.populatePools();
	}

	public static void init(MeasurementObjectLoader mObjectLoader1) {
		if (instance == null)
			instance = new MeasurementStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		mObjectLoader = mObjectLoader1;

		instance.addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, MEASUREMENTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, ANALYSISTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, EVALUATIONTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MODELINGTYPE_ENTITY_CODE, MODELINGTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.SET_ENTITY_CODE, SET_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MODELING_ENTITY_CODE, MODELING_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MS_ENTITY_CODE, MS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENT_ENTITY_CODE, MEASUREMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.ANALYSIS_ENTITY_CODE, ANALYSIS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVALUATION_ENTITY_CODE, EVALUATION_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TEST_ENTITY_CODE, TEST_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE, TEMPORALPATTERN_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.RESULT_ENTITY_CODE, RESULT_OBJECT_POOL_SIZE);

		instance.populatePools();
	}

	public static void init(MeasurementObjectLoader mObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new MeasurementStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, use default");
			instance = new MeasurementStorableObjectPool();
		}
		init(mObjectLoader1, size);
	}

	public static void init(MeasurementObjectLoader mObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new MeasurementStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, use default");
			instance = new MeasurementStorableObjectPool();
		}
		init(mObjectLoader1);
	}

  public static void refresh() throws ApplicationException {
		instance.refreshImpl();
	}

  protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws ApplicationException {
		return mObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static java.util.Set getStorableObjects(java.util.Set objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static java.util.Set getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static java.util.Set getStorableObjectsByConditionButIds(java.util.Set ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadMeasurementType(objectId);
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadAnalysisType(objectId);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadEvaluationType(objectId);
				break;
			case ObjectEntities.MODELINGTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadModelingType(objectId);
				break;
			case ObjectEntities.SET_ENTITY_CODE:
				storableObject = mObjectLoader.loadSet(objectId);
				break;
			case ObjectEntities.MODELING_ENTITY_CODE:
				storableObject = mObjectLoader.loadModeling(objectId);
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				storableObject = mObjectLoader.loadMeasurementSetup(objectId);
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				storableObject = mObjectLoader.loadAnalysis(objectId);
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				storableObject = mObjectLoader.loadEvaluation(objectId);
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				storableObject = mObjectLoader.loadMeasurement(objectId);
				break;
			case ObjectEntities.TEST_ENTITY_CODE:
				storableObject = mObjectLoader.loadTest(objectId);
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				storableObject = mObjectLoader.loadResult(objectId);
				break;
			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				storableObject = mObjectLoader.loadTemporalPattern(objectId);
				break;
			default:
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObject | Unknown entity: '" + ObjectEntities.codeToString(objectId.getMajor()) + "', entity code: " + objectId.getMajor());
				storableObject = null;
		}
		return storableObject;
	}

	protected java.util.Set loadStorableObjects(final java.util.Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				return mObjectLoader.loadMeasurementTypes(ids);
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				return mObjectLoader.loadAnalysisTypes(ids);
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				return mObjectLoader.loadEvaluationTypes(ids);
			case ObjectEntities.MODELINGTYPE_ENTITY_CODE:
				return mObjectLoader.loadModelingTypes(ids);
			case ObjectEntities.SET_ENTITY_CODE:
				return mObjectLoader.loadSets(ids);
			case ObjectEntities.MODELING_ENTITY_CODE:
				return mObjectLoader.loadModelings(ids);
			case ObjectEntities.MS_ENTITY_CODE:
				return mObjectLoader.loadMeasurementSetups(ids);
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				return mObjectLoader.loadAnalyses(ids);
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				return mObjectLoader.loadEvaluations(ids);
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				return mObjectLoader.loadMeasurements(ids);
			case ObjectEntities.TEST_ENTITY_CODE:
				return mObjectLoader.loadTests(ids);
			case ObjectEntities.RESULT_ENTITY_CODE:
				return mObjectLoader.loadResults(ids);
			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				return mObjectLoader.loadTemporalPatterns(ids);
			default:
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected java.util.Set loadStorableObjectsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		java.util.Set loadedCollection = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadMeasurementTypesButIds(condition, ids);
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadAnalysisTypesButIds(condition, ids);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadEvaluationTypesButIds(condition, ids);
				break;
			case ObjectEntities.MODELINGTYPE_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadModelingTypesButIds(condition, ids);
				break;
			case ObjectEntities.SET_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadSetsButIds(condition, ids);
				break;
			case ObjectEntities.MODELING_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadModelingsButIds(condition, ids);
				break;						
			case ObjectEntities.MS_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadMeasurementSetupsButIds(condition, ids);
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadAnalysesButIds(condition, ids);
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadEvaluationsButIds(condition, ids);
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadMeasurementsButIds(condition, ids);
				break;
			case ObjectEntities.TEST_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadTestsButIds(condition, ids);
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadResultsButIds(condition, ids);
				break;
			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				loadedCollection = mObjectLoader.loadTemporalPatternsButIds(condition, ids);
				break;
			default:
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedCollection = null;
		}
		return loadedCollection;
	}

	protected void saveStorableObjects(final java.util.Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveMeasurementType((MeasurementType)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveMeasurementTypes(storableObjects, force);
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveAnalysisType((AnalysisType)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveAnalysisTypes(storableObjects, force);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveEvaluationType((EvaluationType)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveEvaluationTypes(storableObjects, force);
				break;
			case ObjectEntities.SET_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveSet((Set)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveSets(storableObjects, force);
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveMeasurementSetup((MeasurementSetup)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveMeasurementSetups(storableObjects, force);
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveAnalysis((Analysis)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveAnalyses(storableObjects, force);
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveEvaluation((Evaluation)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveEvaluations(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveMeasurement((Measurement)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveMeasurements(storableObjects, force);
				break;
			case ObjectEntities.TEST_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveTest((Test)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveTests(storableObjects, force);
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveResult((Result)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveResults(storableObjects, force);
				break;
			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				if (singleton)
					mObjectLoader.saveTemporalPattern((TemporalPattern)storableObjects.iterator().next(), force);
				else 
					mObjectLoader.saveTemporalPatterns(storableObjects, force);
				break;
			default:
				Log.errorMessage("MeasurementStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		return instance.fromTransferableImpl(id, transferable);
	}

	public static void flush(boolean force) throws ApplicationException{		 
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(final java.util.Set identifiables) {
		instance.deleteImpl(identifiables);
	}

	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
		mObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(final java.util.Set identifiables) throws IllegalDataException {
		mObjectLoader.delete(identifiables);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}

}
