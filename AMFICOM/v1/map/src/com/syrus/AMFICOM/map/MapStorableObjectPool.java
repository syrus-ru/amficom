/*
 * $Id: MapStorableObjectPool.java,v 1.2 2004/12/02 12:10:27 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.map;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2004/12/02 12:10:27 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public final class MapStorableObjectPool extends StorableObjectPool {

	private static final int				OBJECT_POOL_MAP_SIZE				= 14;	/*
																						 * Number
																						 * of
																						 * entities
																						 */

	private static final int				PARAMETERTYPE_OBJECT_POOL_SIZE		= 9;

	private static final int				MEASUREMENTTYPE_OBJECT_POOL_SIZE	= 1;

	private static final int				ANALYSISTYPE_OBJECT_POOL_SIZE		= 1;

	private static final int				EVALUATIONTYPE_OBJECT_POOL_SIZE		= 1;

	private static final int				SET_OBJECT_POOL_SIZE				= 4;

	private static final int				SETPARAMETER_OBJECT_POOL_SIZE		= 4;

	private static final int				MODELING_OBJECT_POOL_SIZE			= 4;

	private static final int				MS_OBJECT_POOL_SIZE					= 4;

	private static final int				MEASUREMENT_OBJECT_POOL_SIZE		= 4;

	private static final int				ANALYSIS_OBJECT_POOL_SIZE			= 4;

	private static final int				EVALUATION_OBJECT_POOL_SIZE			= 4;

	private static final int				TEST_OBJECT_POOL_SIZE				= 2;

	private static final int				RESULT_OBJECT_POOL_SIZE				= 4;

	private static final int				RESULTPARAMETER_OBJECT_POOL_SIZE	= 4;

	private static final int				TEMPORALPATTERN_OBJECT_POOL_SIZE	= 2;

	private static MapObjectLoader			mObjectLoader;
	private static MapStorableObjectPool	instance;

	private MapStorableObjectPool() {
		// empty
	}

	private MapStorableObjectPool(Class cacheMapClass) {
		super(cacheMapClass);
	}

	/**
	 * 
	 * @param mObjectLoader1
	 * @param cacheClass
	 *            class must extend LRUMap
	 * @param size
	 */
	public static void init(MapObjectLoader mObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new MapStorableObjectPool(clazz);
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, use default '"
					+ ((clazz == null) ? "null" : clazz.getName()) + "'");
		}
		init(mObjectLoader1, size);
	}

	public static void init(MapObjectLoader mObjectLoader1, final int size) {
		if (instance == null)
			instance = new MapStorableObjectPool();
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(size));

		mObjectLoader = mObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, size);

		instance.addObjectPool(ObjectEntities.SET_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MODELING_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MS_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MEASUREMENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.ANALYSIS_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EVALUATION_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TEST_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.RESULT_ENTITY_CODE, size);

		instance.polulatePools();
	}

	public static void init(MapObjectLoader mObjectLoader1) {
		if (instance == null)
			instance = new MapStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));
		mObjectLoader = mObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, MEASUREMENTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, ANALYSISTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, EVALUATIONTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.SET_ENTITY_CODE, SET_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MODELING_ENTITY_CODE, MODELING_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MS_ENTITY_CODE, MS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENT_ENTITY_CODE, MEASUREMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.ANALYSIS_ENTITY_CODE, ANALYSIS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVALUATION_ENTITY_CODE, EVALUATION_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TEST_ENTITY_CODE, TEST_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE, TEMPORALPATTERN_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.RESULT_ENTITY_CODE, RESULT_OBJECT_POOL_SIZE);

		instance.polulatePools();
	}

	public static void refresh() throws DatabaseException, CommunicationException {
		instance.refreshImpl();
	}

	protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws CommunicationException,
			DatabaseException {
		// TODO implement method
		// return mObjectLoader.refresh(storableObjects);
		return null;
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws DatabaseException,
			CommunicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static List getStorableObjects(List objectIds, boolean useLoader) throws DatabaseException,
			CommunicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static List getStorableObjectsByConditionButIds(	List ids,
															StorableObjectCondition condition,
															boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			// TODO implement method
			// case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
			// storableObject = mObjectLoader.loadParameterType(objectId);
			// break;
			// case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			// storableObject = mObjectLoader.loadMapType(objectId);
			// break;
			// case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			// storableObject = mObjectLoader.loadAnalysisType(objectId);
			// break;
			// case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			// storableObject = mObjectLoader.loadEvaluationType(objectId);
			// break;
			// case ObjectEntities.SET_ENTITY_CODE:
			// storableObject = mObjectLoader.loadSet(objectId);
			// break;
			// case ObjectEntities.MODELING_ENTITY_CODE:
			// storableObject = mObjectLoader.loadModeling(objectId);
			// break;
			// case ObjectEntities.MS_ENTITY_CODE:
			// storableObject = mObjectLoader.loadMapSetup(objectId);
			// break;
			// case ObjectEntities.ANALYSIS_ENTITY_CODE:
			// storableObject = mObjectLoader.loadAnalysis(objectId);
			// break;
			// case ObjectEntities.EVALUATION_ENTITY_CODE:
			// storableObject = mObjectLoader.loadEvaluation(objectId);
			// break;
			// case ObjectEntities.MEASUREMENT_ENTITY_CODE:
			// storableObject = mObjectLoader.loadMap(objectId);
			// break;
			// case ObjectEntities.TEST_ENTITY_CODE:
			// storableObject = mObjectLoader.loadTest(objectId);
			// break;
			// case ObjectEntities.RESULT_ENTITY_CODE:
			// storableObject = mObjectLoader.loadResult(objectId);
			// break;
			// case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
			// storableObject = mObjectLoader.loadTemporalPattern(objectId);
			// break;
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObject | Unknown entity: "
						+ ObjectEntities.codeToString(objectId.getMajor()));
				storableObject = null;
		}
		return storableObject;
	}

	protected List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
		List storableObjects;
		switch (entityCode.shortValue()) {
			// TODO implement method
			// case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadParameterTypes(ids);
			// break;
			// case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadMapTypes(ids);
			// break;
			// case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadAnalysisTypes(ids);
			// break;
			// case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadEvaluationTypes(ids);
			// break;
			// case ObjectEntities.SET_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadSets(ids);
			// break;
			// case ObjectEntities.MODELING_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadModelings(ids);
			// break;
			// case ObjectEntities.MS_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadMapSetups(ids);
			// break;
			// case ObjectEntities.ANALYSIS_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadAnalyses(ids);
			// break;
			// case ObjectEntities.EVALUATION_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadEvaluations(ids);
			// break;
			// case ObjectEntities.MEASUREMENT_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadMaps(ids);
			// break;
			// case ObjectEntities.TEST_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadTests(ids);
			// break;
			// case ObjectEntities.RESULT_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadResults(ids);
			// break;
			// case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
			// storableObjects = mObjectLoader.loadTemporalPatterns(ids);
			// break;
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
				storableObjects = null;
		}
		return storableObjects;
	}

	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			// TODO implement method
			// case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
			// loadedList = mObjectLoader.loadParameterTypesButIds(condition,
			// ids);
			// break;
			// case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			// loadedList = mObjectLoader.loadMapTypesButIds(condition, ids);
			// break;
			// case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			// loadedList = mObjectLoader.loadAnalysisTypesButIds(condition,
			// ids);
			// break;
			// case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			// loadedList = mObjectLoader.loadEvaluationTypesButIds(condition,
			// ids);
			// break;
			// case ObjectEntities.SET_ENTITY_CODE:
			// loadedList = mObjectLoader.loadSetsButIds(condition, ids);
			// break;
			// case ObjectEntities.MODELING_ENTITY_CODE:
			// loadedList = mObjectLoader.loadModelingsButIds(condition, ids);
			// break;
			// case ObjectEntities.MS_ENTITY_CODE:
			// loadedList = mObjectLoader.loadMapSetupsButIds(condition, ids);
			// break;
			// case ObjectEntities.ANALYSIS_ENTITY_CODE:
			// loadedList = mObjectLoader.loadAnalysesButIds(condition, ids);
			// break;
			// case ObjectEntities.EVALUATION_ENTITY_CODE:
			// loadedList = mObjectLoader.loadEvaluationsButIds(condition, ids);
			// break;
			// case ObjectEntities.MEASUREMENT_ENTITY_CODE:
			// loadedList = mObjectLoader.loadMapsButIds(condition, ids);
			// break;
			// case ObjectEntities.TEST_ENTITY_CODE:
			// loadedList = mObjectLoader.loadTestsButIds(condition, ids);
			// break;
			// case ObjectEntities.RESULT_ENTITY_CODE:
			// loadedList = mObjectLoader.loadResultsButIds(condition, ids);
			// break;
			// case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
			// loadedList = mObjectLoader.loadTemporalPatternsButIds(condition,
			// ids);
			// break;
			default:
				Log.errorMessage("MapStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode));
				loadedList = null;
		}
		return loadedList;
	}

	protected void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException, IllegalDataException {
		if (!list.isEmpty()) {
			boolean alone = (list.size() == 1);

			switch (code) {
				// TODO implement method
				// case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveParameterType((ParameterType)list.get(0),
				// force);
				// else
				// mObjectLoader.saveParameterTypes(list, force);
				// break;
				// case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveMapType((MapType)list.get(0), force);
				// else
				// mObjectLoader.saveMapTypes(list, force);
				// break;
				// case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveAnalysisType((AnalysisType)list.get(0),
				// force);
				// else
				// mObjectLoader.saveAnalysisTypes(list, force);
				// break;
				// case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveEvaluationType((EvaluationType)list.get(0),
				// force);
				// else
				// mObjectLoader.saveEvaluationTypes(list, force);
				// break;
				// case ObjectEntities.SET_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveSet((Set)list.get(0), force);
				// else
				// mObjectLoader.saveSets(list, force);
				// break;
				// case ObjectEntities.MS_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveMapSetup((MapSetup)list.get(0), force);
				// else
				// mObjectLoader.saveMapSetups(list, force);
				// break;
				// case ObjectEntities.ANALYSIS_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveAnalysis((Analysis)list.get(0), force);
				// else
				// mObjectLoader.saveAnalyses(list, force);
				// break;
				// case ObjectEntities.EVALUATION_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveEvaluation((Evaluation)list.get(0), force);
				// else
				// mObjectLoader.saveEvaluations(list, force);
				// break;
				// case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveMap((Map)list.get(0), force);
				// else
				// mObjectLoader.saveMaps(list, force);
				// break;
				// case ObjectEntities.TEST_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveTest((Test)list.get(0), force);
				// else
				// mObjectLoader.saveTests(list, force);
				// break;
				// case ObjectEntities.RESULT_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveResult((Result)list.get(0), force);
				// else
				// mObjectLoader.saveResults(list, force);
				// break;
				// case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				// if (alone)
				// mObjectLoader.saveTemporalPattern((TemporalPattern)list.get(0),
				// force);
				// else
				// mObjectLoader.saveTemporalPatterns(list, force);
				// break;
				default:
					Log.errorMessage("MapStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '"
							+ ObjectEntities.codeToString(code) + "'");
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static void flush(boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException, IllegalDataException {
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}

	protected void deleteStorableObject(Identifier id) throws DatabaseException, CommunicationException {
		// TODO implement method

		// try {
		// mObjectLoader.delete(id);
		// } catch (DatabaseException e) {
		// Log.errorMessage("MapStorableObjectPool.deleteStorableObject |
		// DatabaseException: " + e.getMessage());
		// throw new
		// DatabaseException("MapStorableObjectPool.deleteStorableObject", e);
		// } catch (CommunicationException e) {
		// Log.errorMessage("MapStorableObjectPool.deleteStorableObject |
		// CommunicationException: " + e.getMessage());
		// throw new
		// CommunicationException("MapStorableObjectPool.deleteStorableObject",
		// e);
		// }
	}

	protected void deleteStorableObjects(List ids) throws DatabaseException, CommunicationException {
		// TODO implement method
		// try {
		//			
		// mObjectLoader.delete(ids);
		// } catch (DatabaseException e) {
		// Log.errorMessage("MapStorableObjectPool.deleteStorableObjects |
		// DatabaseException: " + e.getMessage());
		// throw new
		// DatabaseException("MapStorableObjectPool.deleteStorableObjects", e);
		// } catch (CommunicationException e) {
		// Log.errorMessage("MapStorableObjectPool.deleteStorableObjects |
		// CommunicationException: " + e.getMessage());
		// throw new
		// CommunicationException("MapStorableObjectPool.deleteStorableObjects",
		// e);
		// }
	}

	public static void delete(Identifier id) throws DatabaseException, CommunicationException {
		instance.deleteImpl(id);
	}

	public static void delete(List ids) throws DatabaseException, CommunicationException {
		instance.deleteImpl(ids);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}
}
