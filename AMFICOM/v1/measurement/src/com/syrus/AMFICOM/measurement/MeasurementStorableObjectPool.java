/*
 * $Id: MeasurementStorableObjectPool.java,v 1.39 2004/11/04 09:03:45 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.39 $, $Date: 2004/11/04 09:03:45 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementStorableObjectPool {

	private static final int		OBJECT_POOL_MAP_SIZE			= 14;		/* Number of entities  */

	private static final int		PARAMETERTYPE_OBJECT_POOL_SIZE		= 9;

	private static final int		MEASUREMENTTYPE_OBJECT_POOL_SIZE	= 1;

	private static final int		ANALYSISTYPE_OBJECT_POOL_SIZE		= 1;

	private static final int		EVALUATIONTYPE_OBJECT_POOL_SIZE		= 1;

	private static final int		SET_OBJECT_POOL_SIZE			= 4;

	private static final int		SETPARAMETER_OBJECT_POOL_SIZE		= 4;

	private static final int		MODELING_OBJECT_POOL_SIZE		= 4;

	private static final int		MS_OBJECT_POOL_SIZE			= 4;

	private static final int		MEASUREMENT_OBJECT_POOL_SIZE		= 4;

	private static final int		ANALYSIS_OBJECT_POOL_SIZE		= 4;

	private static final int		EVALUATION_OBJECT_POOL_SIZE		= 4;

	private static final int		TEST_OBJECT_POOL_SIZE			= 2;

	private static final int		RESULT_OBJECT_POOL_SIZE			= 4;

	private static final int		RESULTPARAMETER_OBJECT_POOL_SIZE	= 4;

	private static final int		TEMPORALPATTERN_OBJECT_POOL_SIZE	= 2;

	/*
	 *  Map <Short objectEntity, LRUMap objectPool>
	 */
	private static Map			objectPoolMap;						
	private static MeasurementObjectLoader	mObjectLoader;
	private static Class			cacheMapClass				= LRUMap.class;

	private MeasurementStorableObjectPool() {
	}

	/**
	 * 
	 * @param mObjectLoader1
	 * @param cacheClass
	 *                class must extend LRUMap
	 * @param size
	 */
	public static void init(MeasurementObjectLoader mObjectLoader1, Class cacheClass, final int size) {
		try {
			Class clazz = Class.forName(cacheClass.getName());
			cacheMapClass = clazz;
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default '" 
							 + cacheMapClass.getName() + "'");
		}
		
		init(mObjectLoader1, size);
	}

	public static void init(MeasurementObjectLoader mObjectLoader1, final int size) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(size));

		addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, size);

		addObjectPool(ObjectEntities.SET_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MODELING_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MS_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MEASUREMENT_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.ANALYSIS_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.EVALUATION_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.TEST_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.RESULT_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE, size);

		mObjectLoader = mObjectLoader1;
	}

	public static void init(MeasurementObjectLoader mObjectLoader1) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, MEASUREMENTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, ANALYSISTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, EVALUATIONTYPE_OBJECT_POOL_SIZE);

		addObjectPool(ObjectEntities.SET_ENTITY_CODE, SET_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MODELING_ENTITY_CODE, MODELING_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MS_ENTITY_CODE, MS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENT_ENTITY_CODE, MEASUREMENT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ANALYSIS_ENTITY_CODE, ANALYSIS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EVALUATION_ENTITY_CODE, EVALUATION_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TEST_ENTITY_CODE, TEST_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.RESULT_ENTITY_CODE, RESULT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE, TEMPORALPATTERN_OBJECT_POOL_SIZE);

		mObjectLoader = mObjectLoader1;
	}

	private static void addObjectPool(short objectEntityCode, int poolSize) {
		try {
			// LRUMap objectPool = new LRUMap(poolSize);
			Constructor constructor = cacheMapClass.getConstructor(new Class[] { int.class});
			Object obj = constructor.newInstance(new Object[] { new Integer(poolSize)});
			if (obj instanceof LRUMap) {
				LRUMap objectPool = (LRUMap) obj;
				objectPoolMap.put(new Short(objectEntityCode), objectPool);
			} else
				throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
						+ " must extends LRUMap");
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " SecurityException " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " IllegalArgumentException " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " NoSuchMethodException " + e.getMessage());
		} catch (InstantiationException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " InstantiationException " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " IllegalAccessException " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " InvocationTargetException " + e.getMessage());
		}
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader)
			throws DatabaseException, CommunicationException {
		if (objectId != null) {
			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap) objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject) objectPool.get(objectId);
				if (storableObject != null)
					return storableObject;
				else {
					if (useLoader) {
						storableObject = loadStorableObject(objectId);
						if (storableObject != null)
							try {
								putStorableObject(storableObject);
							} catch (IllegalObjectEntityException ioee) {
								Log.errorMessage("MeasurementStorableObjectPool.getStorableObject | Cannot load '" + objectId.getIdentifierString() + "'");
								Log.errorException(ioee);
							}
					}
					return storableObject;
				}
			} else {
				Log
						.errorMessage("MeasurementStorableObjectPool.getStorableObject | Cannot find object pool for objectId: '"
								+ objectId.toString()
								+ "' entity code: '"
								+ ObjectEntities.codeToString(objectEntityCode) + "'");
				return null;
			}
		} else {
			Log.errorMessage("MeasurementStorableObjectPool.getStorableObject | NULL identifier supplied");
			return null;
		}
	}

	public static List getStorableObjects(List objectIds, boolean useLoader) throws DatabaseException,
			CommunicationException {
		List list = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (Iterator it = objectIds.iterator(); it.hasNext();) {
				Identifier objectId = (Identifier) it.next();
				short objectEntityCode = objectId.getMajor();
				Short entityCode = new Short(objectEntityCode);
				LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
				StorableObject storableObject = null;
				if (objectPool != null) {
					storableObject = (StorableObject) objectPool.get(objectId);
					if (storableObject != null) {
						if (list == null)
							list = new LinkedList();
						list.add(storableObject);
					}
				}
				if (storableObject == null) {
					Log
							.errorMessage("MeasurementStorableObjectPool.getStorableObjects | Cannot find object pool for objectId: '"
									+ objectId.toString()
									+ "' entity code: '"
									+ ObjectEntities.codeToString(objectEntityCode)
									+ "'");
					if (useLoader) {
						if (objectQueueMap == null)
							objectQueueMap = new HashMap();
						List objectQueue = (List) objectQueueMap.get(entityCode);
						if (objectQueue == null) {
							objectQueue = new LinkedList();
							objectQueueMap.put(entityCode, objectQueue);
						}
						objectQueue.add(objectId);
					}
				}
			}

		} else {
			Log
					.errorMessage("MeasurementStorableObjectPool.getStorableObjects | NULL list of identifiers supplied");
		}

		if (objectQueueMap != null) {
			if (list == null)
				list = new LinkedList();
			for (Iterator it = objectQueueMap.keySet().iterator(); it.hasNext();) {
				Short entityCode = (Short) it.next();
				List objectQueue = (List) objectQueueMap.get(entityCode);
				List storableObjects = loadStorableObjects(entityCode, objectQueue);
				if (storableObjects != null) {
					try {
						for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
							StorableObject storableObject = (StorableObject) iter.next();
							putStorableObject(storableObject);
							list.add(storableObject);
						}
					} catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}
		
		if (list == null)
			list = Collections.EMPTY_LIST;

		return list;
	}

	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return getStorableObjectsByConditionButIds(null, condition, useLoader);
	}

	public static List getStorableObjectsByConditionButIds(List ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		List list = null;
		LRUMap objectPool = (LRUMap) objectPoolMap.get(condition.getEntityCode());
		if (objectPool != null) {
			list = new LinkedList();
			for (Iterator it = objectPool.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if (( ids == null || !ids.contains(storableObject.getId())) && (condition.isConditionTrue(storableObject)))
					list.add(storableObject);
			}			
			
			List loadedList = null;
			
			if (useLoader){
				if (condition.isNeedMore(list)){
					List idsList = new ArrayList(list.size());
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						StorableObject storableObject = (StorableObject) iter.next();
						idsList.add(storableObject.getId());					
					}
					
					if (ids != null){
						for (Iterator iter = ids.iterator(); iter.hasNext();) {
							Identifier id = (Identifier) iter.next();
							idsList.add(id);					
						}
					}
					
					loadedList = loadStorableObjectsButIds(condition, idsList);
				}
			}
			
			for (Iterator it = list.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				objectPool.get(storableObject);				
			}
			
			if (loadedList!=null){
				for (Iterator it = loadedList.iterator(); it.hasNext();) {
					StorableObject storableObject = (StorableObject) it.next();
					objectPool.put(storableObject.getId(), storableObject);
					list.add(storableObject);
				}
			}

		}

		if (list == null)
			list = Collections.EMPTY_LIST;

		return list;
	}

	
	private static StorableObject loadStorableObject(Identifier objectId) throws DatabaseException,
			CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadParameterType(objectId);
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadMeasurementType(objectId);
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadAnalysisType(objectId);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				storableObject = mObjectLoader.loadEvaluationType(objectId);
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
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObject | Unknown entity: "
						+ objectId.getObjectEntity());
				storableObject = null;
		}
		return storableObject;
	}

	private static List loadStorableObjects(Short entityCode, List ids) throws DatabaseException,
			CommunicationException {
		List storableObjects;
		switch (entityCode.shortValue()) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadParameterTypes(ids);
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadMeasurementTypes(ids);
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadAnalysisTypes(ids);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				storableObjects = mObjectLoader.loadEvaluationTypes(ids);
				break;
			case ObjectEntities.SET_ENTITY_CODE:
				storableObjects = mObjectLoader.loadSets(ids);
				break;
			case ObjectEntities.MODELING_ENTITY_CODE:
				storableObjects = mObjectLoader.loadModelings(ids);
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				storableObjects = mObjectLoader.loadMeasurementSetups(ids);
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				storableObjects = mObjectLoader.loadAnalyses(ids);
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				storableObjects = mObjectLoader.loadEvaluations(ids);
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				storableObjects = mObjectLoader.loadMeasurements(ids);
				break;
			case ObjectEntities.TEST_ENTITY_CODE:
				storableObjects = mObjectLoader.loadTests(ids);
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				storableObjects = mObjectLoader.loadResults(ids);
				break;
			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				storableObjects = mObjectLoader.loadTemporalPatterns(ids);
				break;
			default:
				Log
						.errorMessage("MeasurementStorableObjectPool.loadStorableObjects | Unknown entityCode : "
								+ entityCode);
				storableObjects = null;
		}
		return storableObjects;
	}
	
	private static List loadStorableObjectsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
		CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				loadedList = mObjectLoader.loadParameterTypesButIds(condition, ids);
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				loadedList = mObjectLoader.loadMeasurementTypesButIds(condition, ids);
				break;
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				loadedList = mObjectLoader.loadAnalysisTypesButIds(condition, ids);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				loadedList = mObjectLoader.loadEvaluationTypesButIds(condition, ids);
				break;
			case ObjectEntities.SET_ENTITY_CODE:
				loadedList = mObjectLoader.loadSetsButIds(condition, ids);
				break;
			case ObjectEntities.MODELING_ENTITY_CODE:
				loadedList = mObjectLoader.loadModelingsButIds(condition, ids);
				break;						
			case ObjectEntities.MS_ENTITY_CODE:
				loadedList = mObjectLoader.loadMeasurementSetupsButIds(condition, ids);
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				loadedList = mObjectLoader.loadAnalysesButIds(condition, ids);
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				loadedList = mObjectLoader.loadEvaluationsButIds(condition, ids);
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				loadedList = mObjectLoader.loadMeasurementsButIds(condition, ids);
				break;
			case ObjectEntities.TEST_ENTITY_CODE:
				loadedList = mObjectLoader.loadTestsButIds(condition, ids);
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				loadedList = mObjectLoader.loadResultsButIds(condition, ids);
				break;
			case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
				loadedList = mObjectLoader.loadTemporalPatternsButIds(condition, ids);
				break;
			default:
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode));
				loadedList = null;
		}		
		return loadedList;
	}
	
	private static void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{
		if (!list.isEmpty()){
			boolean alone = (list.size()==1);
			switch (code) {
				case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveParameterType((ParameterType)list.get(0), force);
					else 
						mObjectLoader.saveParameterTypes(list, force);
					break;
				case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveMeasurementType((MeasurementType)list.get(0), force);
					else 
						mObjectLoader.saveMeasurementTypes(list, force);
					break;
				case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveAnalysisType((AnalysisType)list.get(0), force);
					else 
						mObjectLoader.saveAnalysisTypes(list, force);
					break;
				case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveEvaluationType((EvaluationType)list.get(0), force);
					else 
						mObjectLoader.saveEvaluationTypes(list, force);
					break;
				case ObjectEntities.SET_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveSet((Set)list.get(0), force);
					else 
						mObjectLoader.saveSets(list, force);
					break;
				case ObjectEntities.MS_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveMeasurementSetup((MeasurementSetup)list.get(0), force);
					else 
						mObjectLoader.saveMeasurementSetups(list, force);
					break;
				case ObjectEntities.ANALYSIS_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveAnalysis((Analysis)list.get(0), force);
					else 
						mObjectLoader.saveAnalyses(list, force);
					break;
				case ObjectEntities.EVALUATION_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveEvaluation((Evaluation)list.get(0), force);
					else 
						mObjectLoader.saveEvaluations(list, force);
					break;
				case ObjectEntities.MEASUREMENT_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveMeasurement((Measurement)list.get(0), force);
					else 
						mObjectLoader.saveMeasurements(list, force);
					break;
				case ObjectEntities.TEST_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveTest((Test)list.get(0), force);
					else 
						mObjectLoader.saveTests(list, force);
					break;
				case ObjectEntities.RESULT_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveResult((Result)list.get(0), force);
					else 
						mObjectLoader.saveResults(list, force);
					break;
				case ObjectEntities.TEMPORALPATTERN_ENTITY_CODE:
					if (alone)
						mObjectLoader.saveTemporalPattern((TemporalPattern)list.get(0), force);
					else 
						mObjectLoader.saveTemporalPatterns(list, force);
					break;
				default:
					Log
							.errorMessage("MeasurementStorableObjectPool.flush | Unknown entityCode : "
									+ code);
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject)
			throws IllegalObjectEntityException {
		StorableObject object = null;
		boolean cache = true;
		Identifier objectId = storableObject.getId();
		short entityCode = objectId.getMajor();

		// some entities such as processing and scheduled test cannot be
		// cached
		switch (entityCode) {
			case ObjectEntities.TEST_ENTITY_CODE:
				{
				/**
				 * FIXME how to cache ?
				 */
//					Test test = (Test) storableObject;
//					TestStatus status = test.getStatus();
//					cache = (status.value() == TestStatus._TEST_STATUS_ABORTED)
//							|| (status.value() == TestStatus._TEST_STATUS_ABORTED);
				}
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				{
					Measurement measurement = (Measurement)storableObject;
					MeasurementStatus status = measurement.getStatus();
					cache = ((status.value() == MeasurementStatus._MEASUREMENT_STATUS_ABORTED) ||  
							(status.value() == MeasurementStatus._MEASUREMENT_STATUS_COMPLETED));
				}
				break;
			default:
				cache = true;
				break;
		}
		if (cache) {
			LRUMap objectPool = (LRUMap) objectPoolMap.get(new Short(objectId.getMajor()));
			if (objectPool != null) {
				object = (StorableObject) objectPool.put(objectId, storableObject);
			} else {
				throw new IllegalObjectEntityException(
									"MeasurementStorableObjectPool.putStorableObject | Illegal object entity: '"
											+ objectId.getObjectEntity()
											+ "'",
									IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
			}
		}
		return object;
	}
	
	public static void flush(boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{		 
		List list = new LinkedList();
		for (Iterator it = objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
			if (objectPool != null){
				list.clear();
				for(Iterator poolIt = objectPool.iterator();poolIt.hasNext();){
					StorableObject storableObject = (StorableObject)poolIt.next();
					if (storableObject.isChanged()){
						list.add(storableObject);
						Log.debugMessage("'" + storableObject.getId() + "' is changed", Log.DEBUGLEVEL05);
					}
				} 
				short code = entityCode.shortValue();
				saveStorableObjects(code, list, force);
				
			} else {
				Log
				.errorMessage("MeasurementStorableObjectPool.flush | Cannot find object pool for entity code: '"
						+ ObjectEntities.codeToString(entityCode.shortValue())
						+ "'");
			}
		}
	}
	
	
	public static void cleanChangedStorableObject(Short entityCode){
		LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
		if (objectPool != null){
			for(Iterator poolIt = objectPool.iterator();poolIt.hasNext();){
				StorableObject storableObject = (StorableObject)poolIt.next();
				if (storableObject.isChanged())
					poolIt.remove();				
			}
		}
	}
	
	public static void cleanChangedStorableObjects(){
		for (Iterator it = objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			cleanChangedStorableObject(entityCode);
		}
	}
}
