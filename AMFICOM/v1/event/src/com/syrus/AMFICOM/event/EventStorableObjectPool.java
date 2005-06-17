/*
 * $Id: EventStorableObjectPool.java,v 1.33 2005/06/17 11:01:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.33 $, $Date: 2005/06/17 11:01:03 $
 * @author $Author: bass $
 * @module event_v1
 */

public final class EventStorableObjectPool extends StorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 4;		/* Number of entities  */

	private static final int EVENTTYPE_OBJECT_POOL_SIZE = 2;
	private static final int EVENT_OBJECT_POOL_SIZE = 10;
	private static final int EVENTSOURCE_OBJECT_POOL_SIZE = 10;

	private static EventObjectLoader	eObjectLoader;
	private static EventStorableObjectPool instance;


	private EventStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.EVENT_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.EVENT_TYPE_CODE, new EventTypeFactory());
		registerFactory(ObjectEntities.EVENT_CODE, new EventFactory());
		registerFactory(ObjectEntities.EVENTSOURCE_CODE, new EventSourceFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param eObjectLoader1
	 */
	public static void init(final EventObjectLoader eObjectLoader1) {
		init(eObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param eObjectLoader1
	 * @param size
	 */
	public static void init(final EventObjectLoader eObjectLoader1, final int size) {
		init(eObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param eObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final EventObjectLoader eObjectLoader1, final Class cacheClass) {
		assert eObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new EventStorableObjectPool(cacheClass);

		eObjectLoader = eObjectLoader1;

		instance.addObjectPool(ObjectEntities.EVENT_TYPE_CODE, EVENTTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.EVENT_CODE, EVENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EVENTSOURCE_CODE, EVENTSOURCE_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param eObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final EventObjectLoader eObjectLoader1, final Class cacheClass, final int size) {
		assert eObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new EventStorableObjectPool(cacheClass);

			eObjectLoader = eObjectLoader1;

			instance.addObjectPool(ObjectEntities.EVENT_TYPE_CODE, size);

			instance.addObjectPool(ObjectEntities.EVENT_CODE, size);
			instance.addObjectPool(ObjectEntities.EVENTSOURCE_CODE, size);
		}
		else {
			init(eObjectLoader1, cacheClass);
		}
	}


	protected java.util.Set refreshStorableObjects(final java.util.Set storableObjects) throws ApplicationException {
		return eObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.EVENT_TYPE_CODE:
				return eObjectLoader.loadEventTypes(ids);
			case ObjectEntities.EVENT_CODE:
				return eObjectLoader.loadEvents(ids);
			case ObjectEntities.EVENTSOURCE_CODE:
				return eObjectLoader.loadEventSources(ids);
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		final short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.EVENT_TYPE_CODE:
				return eObjectLoader.loadEventTypesButIds(condition, ids);
			case ObjectEntities.EVENT_CODE:
				return eObjectLoader.loadEventsButIds(condition, ids);
			case ObjectEntities.EVENTSOURCE_CODE:
				return eObjectLoader.loadEventSourcesButIds(condition, ids);
			default:
				Log.errorMessage("EventStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.EVENT_TYPE_CODE:
				eObjectLoader.saveEventTypes(storableObjects, force);
				break;
			case ObjectEntities.EVENT_CODE:
				eObjectLoader.saveEvents(storableObjects, force);
				break;
			case ObjectEntities.EVENTSOURCE_CODE:
				eObjectLoader.saveEventSources(storableObjects, force);
				break;
			default:
				Log.errorMessage("EventStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		eObjectLoader.delete(identifiables);
	}

}
