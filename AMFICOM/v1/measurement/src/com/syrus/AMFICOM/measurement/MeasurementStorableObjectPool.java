/*
 * $Id: MeasurementStorableObjectPool.java,v 1.12 2004/09/16 12:54:38 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2004/09/16 12:54:38 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementStorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 14;	/*	Number of entities*/

	private static final int PARAMETERTYPE_OBJECT_POOL_SIZE = 9;
	private static final int MEASUREMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int ANALYSISTYPE_OBJECT_POOL_SIZE = 1;
	private static final int EVALUATIONTYPE_OBJECT_POOL_SIZE = 1;

	private static final int SET_OBJECT_POOL_SIZE = 4;
	private static final int SETPARAMETER_OBJECT_POOL_SIZE = 4;
	private static final int MS_OBJECT_POOL_SIZE = 4;
	private static final int MEASUREMENT_OBJECT_POOL_SIZE = 4;
	private static final int ANALYSIS_OBJECT_POOL_SIZE = 4;
	private static final int EVALUATION_OBJECT_POOL_SIZE = 4;
	private static final int TEST_OBJECT_POOL_SIZE = 2;
	private static final int RESULT_OBJECT_POOL_SIZE = 4;
	private static final int RESULTPARAMETER_OBJECT_POOL_SIZE = 4;
	private static final int TEMPORALPATTERN_OBJECT_POOL_SIZE = 2;

	private static Map objectPoolMap; /*	Map <String objectEntity, LRUMap objectPool>	*/
	private static MeasurementObjectLoader mObjectLoader;

	private MeasurementStorableObjectPool() {
	}

	public static void init(MeasurementObjectLoader mObjectLoader1) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, MEASUREMENTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, ANALYSISTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, EVALUATIONTYPE_OBJECT_POOL_SIZE);

		addObjectPool(ObjectEntities.SET_ENTITY_CODE, SET_OBJECT_POOL_SIZE);
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
		LRUMap objectPool = new LRUMap(poolSize);
		objectPoolMap.put(new Short(objectEntityCode), objectPool);
	}
	
	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws DatabaseException, CommunicationException {
		if (objectId != null) {
			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject)objectPool.get(objectId);
				if (storableObject != null)
					return storableObject;
				else {
					if (useLoader) {
						storableObject = loadStorableObject(objectId);
						if (storableObject != null)
							try {
								putStorableObject(storableObject);
							}
							catch (IllegalObjectEntityException ioee) {
								Log.errorException(ioee);
							}
					}
					return storableObject;
				}
			}
			else {
				Log.errorMessage("MeasurementStorableObjectPool.getStorableObject | Cannot find object pool for objectId: '" + objectId.toString() + "' entity code: '" + objectEntityCode + "'");
				return null;
			}
		}
		else {
			Log.errorMessage("MeasurementStorableObjectPool.getStorableObject | NULL identifier supplied");
			return null;
		}
	}
	
	public static List getStorableObjects(List objectIds, boolean useLoader) throws DatabaseException, CommunicationException {
		List list = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (Iterator it = objectIds.iterator(); it.hasNext();) {
				Identifier objectId = (Identifier) it.next();
				short objectEntityCode = objectId.getMajor();
				Short entityCode = new Short(objectEntityCode);
				LRUMap objectPool = (LRUMap)objectPoolMap.get(objectId);
				StorableObject storableObject = null;
				if (objectPool != null) {
					storableObject = (StorableObject)objectPool.get(objectId);
					if (storableObject != null){
						if (list == null)
							list = new LinkedList();
						list.add(storableObject);
					}
				}
				if (storableObject == null) {
					Log.errorMessage("MeasurementStorableObjectPool.getStorableObjects | Cannot find object pool for objectId: '" + objectId.toString() + "' entity code: '" + objectEntityCode + "'");
					if (useLoader) {
						if (objectQueueMap == null)
							objectQueueMap = new HashMap();
						List objectQueue = (List)objectQueueMap.get(entityCode);
						if (objectQueue == null){
							objectQueue = new LinkedList();
							objectQueueMap.put(entityCode, objectQueue);
						}
						objectQueue.add(objectId);				
					}
				}			
			}
			
		}
		else {
			Log.errorMessage("MeasurementStorableObjectPool.getStorableObjects | NULL list of identifiers supplied");
		}

		if (objectQueueMap != null){
			for (Iterator it = objectQueueMap.keySet().iterator(); it.hasNext();) {
				Short entityCode = (Short) it.next();
				List objectQueue = (List)objectQueueMap.get(entityCode);
				List storableObjects = loadStorableObjects(entityCode, objectQueue);
				if (storableObjects != null) {
					try {
						for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
							StorableObject storableObject = (StorableObject) iter.next();
							putStorableObject(storableObject);
							list.add(storableObject);
						}						
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}
		
		return list;
	}

	public static List getStorableObjectsByDomain(short entityCode, Domain domain) throws DatabaseException, CommunicationException {
		List list = null;
		/**
		 * TODO method isn't complete
		 */
		LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(entityCode));
		if (objectPool != null){
			list = new LinkedList();
			for (Iterator it = objectPool.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();				
				if (domain != null){
					{
						/**
						 * TODO check for entites
						 */
						switch(entityCode){
							case ObjectEntities.SET_ENTITY_CODE:
								Set set = (Set)storableObject;
								{
									List meList = set.getMonitoredElementIds();
									if (meList != null){
										for(Iterator iter = meList.iterator();iter.hasNext();){
											Identifier id = (Identifier)iter.next();
											MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(id, true);
											if (me.getDomainId().equals(domain)){
												// here we can simple add set to list, 
												// but must put element to start of LRU 
												Object obj = objectPool.get(set.getId());
												list.add(obj);
												break;
											}
										}
									} else 
										list.add(set);
								}						
								break;
							case ObjectEntities.MS_ENTITY_CODE:
								MeasurementSetup measurementSetup = (MeasurementSetup)storableObject;
								{
									List meList = measurementSetup.getMonitoredElementIds();
									if (meList != null){
										for(Iterator iter = meList.iterator();iter.hasNext();){
											Identifier id = (Identifier)iter.next();
											MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(id, true);
											if (me.getDomainId().equals(domain)){
												// here we can simple add measurementSetup to list, 
												// but must put element to start of LRU 
												Object obj = objectPool.get(measurementSetup.getId());
												list.add(obj);
												break;
											}
										}
									} else 
										list.add(measurementSetup);
								}
								break;
							case ObjectEntities.ANALYSIS_ENTITY_CODE:
								Analysis analysis = (Analysis)storableObject;
								{
									MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(analysis.getMonitoredElementId(), true);
									if (me.getDomainId().equals(domain)){
										// here we can simple add analysis to list, 
										// but must put element to start of LRU 
										Object obj = objectPool.get(analysis.getId());
										list.add(obj);
									}
								}							
								break;
							case ObjectEntities.EVALUATION_ENTITY_CODE:
								Evaluation evaluation = (Evaluation)storableObject;
								{
									MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(evaluation.getMonitoredElementId(), true);
									if (me.getDomainId().equals(domain)){
										// here we can simple add evaluation to list, 
										// but must put element to start of LRU 
										Object obj = objectPool.get(evaluation.getId());
										list.add(obj);
									}
								}
								break;
							case ObjectEntities.MEASUREMENT_ENTITY_CODE:
								Measurement measurement = (Measurement)storableObject;
								{
									MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(measurement.getMonitoredElementId(), true);
									if (me.getDomainId().equals(domain)){
										// here we can simple add measurement to list, 
										// but must put element to start of LRU 
										Object obj = objectPool.get(measurement.getId());
										list.add(obj);
									}
								}
								break;
							case ObjectEntities.TEST_ENTITY_CODE:
								Test test = (Test)storableObject;
								{
									MonitoredElement me = test.getMonitoredElement();
									if (me.getDomainId().equals(domain)){
										// here we can simple add test to list, 
										// but must put element to start of LRU 
										Object obj = objectPool.get(test.getId());
										list.add(obj);
									}
								}
								break;
							case ObjectEntities.RESULT_ENTITY_CODE:
								Result result = (Result) storableObject;								
								Measurement measurement2 = result.getMeasurement();
								{
									MonitoredElement me = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(measurement2.getMonitoredElementId(), true);
									if (me.getDomainId().equals(domain)){
										// here we can simple add result to list, 
										// but must put element to start of LRU 
										Object obj = objectPool.get(result.getId());
										list.add(obj);
									}
								}							
								break;
							default: 
								list.add(storableObject);
								break;
								
						}
					} 
				}
				else {
					list.add(storableObject);
				}
				
			}
		}
		return list;
	}
	
	public static List getTestsByTimeRange(Domain domain, Date start, Date end){
		List list = null;
		/**
		 * TODO method isn't complete
		 */
		return list;
	}


	private static StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
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
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObject | Unknown entity: " + objectId.getObjectEntity());
				storableObject = null;
		}
		return storableObject;
	}
	
	private static List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
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
				Log.errorMessage("MeasurementStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
				storableObjects = null;
		}
		return storableObjects;
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		StorableObject object = null;
		boolean cache = true;		
		Identifier objectId = storableObject.getId();
		short entityCode = objectId.getMajor();
		
		// some entities such as processing and scheduled test cannot be cached 
		switch(entityCode){
			case ObjectEntities.TEST_ENTITY_CODE:
				Test test = (Test)storableObject;
				TestStatus status = test.getStatus();
				cache =  (status.value() == TestStatus._TEST_STATUS_ABORTED) || 
					(status.value() == TestStatus._TEST_STATUS_ABORTED);
				break;
			default:
				cache = true;
				break;
		}
		if (cache){
			LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectId.getMajor()));
			if (objectPool != null) {
				object = (StorableObject)objectPool.put(objectId, storableObject);
			}
			throw new IllegalObjectEntityException("MeasurementStorableObjectPool.putStorableObject | Illegal object entity: '" + objectId.getObjectEntity() + "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return object;
	}
}
