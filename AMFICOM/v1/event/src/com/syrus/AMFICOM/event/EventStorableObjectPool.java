/*
 * $Id: EventStorableObjectPool.java,v 1.12 2005/03/21 16:18:47 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;

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
 * @version $Revision: 1.12 $, $Date: 2005/03/21 16:18:47 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventStorableObjectPool extends StorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 4;		/* Number of entities  */

	private static final int EVENTTYPE_OBJECT_POOL_SIZE = 2;
//	private static final int ALARMTYPE_OBJECT_POOL_SIZE = 2;
	private static final int EVENT_OBJECT_POOL_SIZE = 10;
	private static final int EVENTSOURCE_OBJECT_POOL_SIZE = 10;
//	private static final int ALARM_OBJECT_POOL_SIZE = 10;

	private static EventObjectLoader	eObjectLoader;
	private static EventStorableObjectPool instance;

	private EventStorableObjectPool() {
		// empty
		super(ObjectGroupEntities.EVENT_GROUP_CODE);
	}

	private EventStorableObjectPool(Class cacheMapClass) {
		super(ObjectGroupEntities.EVENT_GROUP_CODE, cacheMapClass);
	}

	/**
	 * 
	 * @param eObjectLoader1
	 * @param cacheClass
	 *                class must extend LRUMap
	 * @param size
	 */
	public static void init(EventObjectLoader eObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new EventStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
		}
		init(eObjectLoader1, size);
	}

	public static void init(EventObjectLoader eObjectLoader1, final int size) {
		if (instance == null)
			instance = new EventStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		eObjectLoader = eObjectLoader1;

		instance.addObjectPool(ObjectEntities.EVENTTYPE_ENTITY_CODE, size);
//		instance.addObjectPool(ObjectEntities.ALARMTYPE_ENTITY_CODE, size);

		instance.addObjectPool(ObjectEntities.EVENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EVENTSOURCE_ENTITY_CODE, size);
//		instance.addObjectPool(ObjectEntities.ALARM_ENTITY_CODE, size);

		instance.populatePools();
	}

	public static void init(EventObjectLoader eObjectLoader1) {
		if (instance == null)
			instance = new EventStorableObjectPool();
		
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		eObjectLoader = eObjectLoader1;

		instance.addObjectPool(ObjectEntities.EVENTTYPE_ENTITY_CODE, EVENTTYPE_OBJECT_POOL_SIZE);
//		instance.addObjectPool(ObjectEntities.ALARMTYPE_ENTITY_CODE, ALARMTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.EVENT_ENTITY_CODE, EVENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVENTSOURCE_ENTITY_CODE, EVENTSOURCE_OBJECT_POOL_SIZE);
//		instance.addObjectPool(ObjectEntities.ALARM_ENTITY_CODE, ALARM_OBJECT_POOL_SIZE);

		instance.populatePools();
	}

	public static void refresh() throws ApplicationException {
		instance.refreshImpl();
	}

  protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws ApplicationException {
		return eObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static Collection getStorableObjects(Collection objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Collection getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Collection getStorableObjectsByConditionButIds(Collection ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				storableObject = eObjectLoader.loadEventType(objectId);
				break;
//			case ObjectEntities.ALARMTYPE_ENTITY_CODE:
//				storableObject = eObjectLoader.loadAlarmType(objectId);
//				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				storableObject = eObjectLoader.loadEvent(objectId);
				break;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				storableObject = eObjectLoader.loadEventSource(objectId);
				break;
//			case ObjectEntities.ALARM_ENTITY_CODE:
//				storableObject = eObjectLoader.loadAlarm(objectId);
//				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObject | Unknown entity: '" + ObjectEntities.codeToString(objectId.getMajor()) + "', entity code: " + objectId.getMajor());
				storableObject = null;
		}
		return storableObject;
	}

	protected Collection loadStorableObjects(Short entityCode, Collection ids) throws ApplicationException {
		Collection storableObjects;
		switch (entityCode.shortValue()) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				storableObjects = eObjectLoader.loadEventTypes(ids);
				break;
//			case ObjectEntities.ALARMTYPE_ENTITY_CODE:
//				storableObjects = eObjectLoader.loadAlarmTypes(ids);
//				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				storableObjects = eObjectLoader.loadEvents(ids);
				break;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				storableObjects = eObjectLoader.loadEventSources(ids);
				break;
//			case ObjectEntities.ALARM_ENTITY_CODE:
//				storableObjects = eObjectLoader.loadAlarms(ids);
//				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode.shortValue()) + "', entity code: " + entityCode);
				storableObjects = null;
		}
		return storableObjects;
	}

	protected Collection loadStorableObjectsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		Collection loadedObjects = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				loadedObjects = eObjectLoader.loadEventTypesButIds(condition, ids);
				break;
//			case ObjectEntities.ALARMTYPE_ENTITY_CODE:
//				loadedObjects = eObjectLoader.loadAlarmTypesButIds(condition, ids);
//				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				loadedObjects = eObjectLoader.loadEventsButIds(condition, ids);
				break;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				loadedObjects = eObjectLoader.loadEventSourcesButIds(condition, ids);
				break;
//			case ObjectEntities.ALARM_ENTITY_CODE:
//				loadedObjects = eObjectLoader.loadAlarmsButIds(condition, ids);
//				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedObjects = null;
		}
		return loadedObjects;
	}

	protected void saveStorableObjects(short code, Collection storableObjects, boolean force) throws ApplicationException {
		if (!storableObjects.isEmpty()) {
			boolean alone = (storableObjects.size() == 1);

			switch (code) {
				case ObjectEntities.EVENTTYPE_ENTITY_CODE:
					if (alone)
						eObjectLoader.saveEventType((EventType)storableObjects.iterator().next(), force);
					else 
						eObjectLoader.saveEventTypes(storableObjects, force);
					break;
//				case ObjectEntities.ALARMTYPE_ENTITY_CODE:
//					if (alone)
//						eObjectLoader.saveAlarmType((AlarmType)storableObjects.iterator().next(), force);
//					else 
//						eObjectLoader.saveAlarmTypes(storableObjects, force);
//					break;
				case ObjectEntities.EVENT_ENTITY_CODE:
					if (alone)
						eObjectLoader.saveEvent((Event)storableObjects.iterator().next(), force);
					else 
						eObjectLoader.saveEvents(storableObjects, force);
					break;
				case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
					if (alone)
						eObjectLoader.saveEventSource((EventSource)storableObjects.iterator().next(), force);
					else 
						eObjectLoader.saveEventSources(storableObjects, force);
					break;
//				case ObjectEntities.ALARM_ENTITY_CODE:
//					if (alone)
//						eObjectLoader.saveAlarm((Alarm)storableObjects.iterator().next(), force);
//					else 
//						eObjectLoader.saveAlarms(storableObjects, force);
//					break;
				default:
					Log.errorMessage("EventStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(code) + "', entity code: " + code);
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
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

	public static void delete(Collection objects) throws IllegalDataException {
		instance.deleteImpl(objects);
	}

	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
		eObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(Collection objects) throws IllegalDataException {
		eObjectLoader.delete(objects);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}

}
