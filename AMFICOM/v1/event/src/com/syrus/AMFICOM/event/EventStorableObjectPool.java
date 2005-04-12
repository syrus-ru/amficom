/*
 * $Id: EventStorableObjectPool.java,v 1.17 2005/04/12 08:13:17 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.17 $, $Date: 2005/04/12 08:13:17 $
 * @author $Author: bass $
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

		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		eObjectLoader = eObjectLoader1;

		instance.addObjectPool(ObjectEntities.EVENTTYPE_ENTITY_CODE, size);

		instance.addObjectPool(ObjectEntities.EVENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EVENTSOURCE_ENTITY_CODE, size);

		instance.populatePools();
	}

	public static void init(EventObjectLoader eObjectLoader1) {
		if (instance == null)
			instance = new EventStorableObjectPool();
		
		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		eObjectLoader = eObjectLoader1;

		instance.addObjectPool(ObjectEntities.EVENTTYPE_ENTITY_CODE, EVENTTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.EVENT_ENTITY_CODE, EVENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVENTSOURCE_ENTITY_CODE, EVENTSOURCE_OBJECT_POOL_SIZE);

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

	public static Set getStorableObjects(Set objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Set getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Set getStorableObjectsByConditionButIds(Set ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				storableObject = eObjectLoader.loadEventType(objectId);
				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				storableObject = eObjectLoader.loadEvent(objectId);
				break;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				storableObject = eObjectLoader.loadEventSource(objectId);
				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObject | Unknown entity: '" + ObjectEntities.codeToString(objectId.getMajor()) + "', entity code: " + objectId.getMajor());
				storableObject = null;
		}
		return storableObject;
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				return eObjectLoader.loadEventTypes(ids);
			case ObjectEntities.EVENT_ENTITY_CODE:
				return eObjectLoader.loadEvents(ids);
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				return eObjectLoader.loadEventSources(ids);
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		Set loadedObjects = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				loadedObjects = eObjectLoader.loadEventTypesButIds(condition, ids);
				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				loadedObjects = eObjectLoader.loadEventsButIds(condition, ids);
				break;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				loadedObjects = eObjectLoader.loadEventSourcesButIds(condition, ids);
				break;
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedObjects = null;
		}
		return loadedObjects;
	}

	protected void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				if (singleton)
					eObjectLoader.saveEventType((EventType)storableObjects.iterator().next(), force);
				else 
					eObjectLoader.saveEventTypes(storableObjects, force);
				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				if (singleton)
					eObjectLoader.saveEvent((Event)storableObjects.iterator().next(), force);
				else 
					eObjectLoader.saveEvents(storableObjects, force);
				break;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				if (singleton)
					eObjectLoader.saveEventSource((EventSource)storableObjects.iterator().next(), force);
				else 
					eObjectLoader.saveEventSources(storableObjects, force);
				break;
			default:
				Log.errorMessage("EventStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
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

	public static void delete(final Set identifiables) {
		instance.deleteImpl(identifiables);
	}

	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
		eObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(final Set identifiables) {
		eObjectLoader.delete(identifiables);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}

}
