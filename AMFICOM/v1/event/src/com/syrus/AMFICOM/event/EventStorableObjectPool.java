/*
 * $Id: EventStorableObjectPool.java,v 1.30 2005/06/15 14:55:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.30 $, $Date: 2005/06/15 14:55:04 $
 * @author $Author: bob $
 * @module event_v1
 */

public class EventStorableObjectPool extends StorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 4;		/* Number of entities  */

	private static final int EVENTTYPE_OBJECT_POOL_SIZE = 2;
	private static final int EVENT_OBJECT_POOL_SIZE = 10;
	private static final int EVENTSOURCE_OBJECT_POOL_SIZE = 10;

	private static EventObjectLoader	eObjectLoader;
	private static EventStorableObjectPool instance;


	private EventStorableObjectPool() {
		this(LRUMap.class);
	}

	private EventStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.EVENT_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.EVENTTYPE_ENTITY_CODE, new EventTypeFactory());
		registerFactory(ObjectEntities.EVENT_ENTITY_CODE, new EventFactory());
		registerFactory(ObjectEntities.EVENTSOURCE_ENTITY_CODE, new EventSourceFactory());
	}

	public static void init(EventObjectLoader eObjectLoader1, final int size) {
		if (instance == null)
			instance = new EventStorableObjectPool();

		eObjectLoader = eObjectLoader1;

		instance.addObjectPool(ObjectEntities.EVENTTYPE_ENTITY_CODE, size);

		instance.addObjectPool(ObjectEntities.EVENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EVENTSOURCE_ENTITY_CODE, size);
	}

	public static void init(EventObjectLoader eObjectLoader1) {
		if (instance == null)
			instance = new EventStorableObjectPool();

		eObjectLoader = eObjectLoader1;

		instance.addObjectPool(ObjectEntities.EVENTTYPE_ENTITY_CODE, EVENTTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.EVENT_ENTITY_CODE, EVENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVENTSOURCE_ENTITY_CODE, EVENTSOURCE_OBJECT_POOL_SIZE);
	}

	public static void init(EventObjectLoader eObjectLoader1, Class cacheClass, final int size) {
		if (size > 0) {
			Class clazz = null;
			try {
				clazz = Class.forName(cacheClass.getName());
				instance = new EventStorableObjectPool(clazz);
			}
			catch (ClassNotFoundException e) {
				Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
				instance = new EventStorableObjectPool();
			}
			init(eObjectLoader1, size);
		} else {
			init(eObjectLoader1, cacheClass);
		}
	}

	public static void init(EventObjectLoader eObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new EventStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
			instance = new EventStorableObjectPool();
		}
		init(eObjectLoader1);
	}

	protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws ApplicationException {
		return eObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
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

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.EVENTTYPE_ENTITY_CODE:
				eObjectLoader.saveEventTypes(storableObjects, force);
				break;
			case ObjectEntities.EVENT_ENTITY_CODE:
				eObjectLoader.saveEvents(storableObjects, force);
				break;
			case ObjectEntities.EVENTSOURCE_ENTITY_CODE:
				eObjectLoader.saveEventSources(storableObjects, force);
				break;
			default:
				Log.errorMessage("EventStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		eObjectLoader.delete(identifiables);
	}

}
