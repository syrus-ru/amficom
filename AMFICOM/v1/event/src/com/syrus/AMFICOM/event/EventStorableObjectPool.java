/*
 * $Id: EventStorableObjectPool.java,v 1.3 2005/02/07 11:59:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/07 11:59:04 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventStorableObjectPool extends StorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 4;		/* Number of entities  */

	private static final int EVENTTYPE_OBJECT_POOL_SIZE = 2;
//	private static final int ALARMTYPE_OBJECT_POOL_SIZE = 2;
	private static final int EVENT_OBJECT_POOL_SIZE = 10;
//	private static final int ALARM_OBJECT_POOL_SIZE = 10;

	private static EventObjectLoader	eObjectLoader;
	private static EventStorableObjectPool instance;

	private EventStorableObjectPool() {
		// empty
	}

	private EventStorableObjectPool(Class cacheMapClass) {
		super(cacheMapClass);
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
//		instance.addObjectPool(ObjectEntities.ALARM_ENTITY_CODE, ALARM_OBJECT_POOL_SIZE);

		instance.populatePools();
	}

	public static void refresh() throws DatabaseException, CommunicationException {
		instance.refreshImpl();
	}

  protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws CommunicationException, DatabaseException {
		return eObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws DatabaseException, CommunicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static List getStorableObjects(List objectIds, boolean useLoader) throws DatabaseException, CommunicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static List getStorableObjectsByConditionButIds(List ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
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
//			case ObjectEntities.ALARM_ENTITY_CODE:
//				storableObject = eObjectLoader.loadAlarm(objectId);
//				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObject | Unknown entity: '" + ObjectEntities.codeToString(objectId.getMajor()) + "', entity code: " + objectId.getMajor());
				storableObject = null;
		}
		return storableObject;
	}

	protected List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
		List storableObjects;
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
//			case ObjectEntities.ALARM_ENTITY_CODE:
//				storableObjects = eObjectLoader.loadAlarms(ids);
//				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode.shortValue()) + "', entity code: " + entityCode);
				storableObjects = null;
		}
		return storableObjects;
	}

	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				loadedList = eObjectLoader.loadEventTypesButIds(condition, ids);
				break;
//			case ObjectEntities.ALARMTYPE_ENTITY_CODE:
//				loadedList = eObjectLoader.loadAlarmTypesButIds(condition, ids);
//				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				loadedList = eObjectLoader.loadEventsButIds(condition, ids);
				break;
//			case ObjectEntities.ALARM_ENTITY_CODE:
//				loadedList = eObjectLoader.loadAlarmsButIds(condition, ids);
//				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedList = null;
		}
		return loadedList;
	}

	protected void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		if (!list.isEmpty()) {
			boolean alone = (list.size() == 1);

			switch (code) {
				case ObjectEntities.EVENTTYPE_ENTITY_CODE:
					if (alone)
						eObjectLoader.saveEventType((EventType)list.get(0), force);
					else 
						eObjectLoader.saveEventTypes(list, force);
					break;
//				case ObjectEntities.ALARMTYPE_ENTITY_CODE:
//					if (alone)
//						eObjectLoader.saveAlarmType((AlarmType)list.get(0), force);
//					else 
//						eObjectLoader.saveAlarmTypes(list, force);
//					break;
				case ObjectEntities.EVENT_ENTITY_CODE:
					if (alone)
						eObjectLoader.saveEvent((Event)list.get(0), force);
					else 
						eObjectLoader.saveEvents(list, force);
					break;
//				case ObjectEntities.ALARM_ENTITY_CODE:
//					if (alone)
//						eObjectLoader.saveAlarm((Alarm)list.get(0), force);
//					else 
//						eObjectLoader.saveAlarms(list, force);
//					break;
				default:
					Log.errorMessage("EventStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(code) + "', entity code: " + code);
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static void flush(boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException{		 
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}
	
	public static void delete(Identifier id) throws DatabaseException, CommunicationException {
		instance.deleteImpl(id);
	}

	public static void delete(List ids) throws DatabaseException, CommunicationException {
		instance.deleteImpl(ids);
	}

	protected void deleteStorableObject(Identifier id) throws DatabaseException, CommunicationException {
		try {
			eObjectLoader.delete(id);
		}
		catch (DatabaseException e) {
			Log.errorMessage("EventStorableObjectPool.deleteStorableObject | DatabaseException: " + e.getMessage());
			throw new DatabaseException("EventStorableObjectPool.deleteStorableObject", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("EventStorableObjectPool.deleteStorableObject | CommunicationException: " + e.getMessage());
			throw new CommunicationException("EventStorableObjectPool.deleteStorableObject", e);
		}
	}

	protected void deleteStorableObjects(List ids) throws DatabaseException, CommunicationException {
		try {
			eObjectLoader.delete(ids);
		}
		catch (DatabaseException e) {
			Log.errorMessage("EventStorableObjectPool.deleteStorableObjects | DatabaseException: " + e.getMessage());
			throw new DatabaseException("EventStorableObjectPool.deleteStorableObjects", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("EventStorableObjectPool.deleteStorableObjects | CommunicationException: " + e.getMessage());
			throw new CommunicationException("EventStorableObjectPool.deleteStorableObjects", e);
		}
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

}
