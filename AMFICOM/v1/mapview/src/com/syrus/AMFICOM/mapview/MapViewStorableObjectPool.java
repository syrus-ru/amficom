/*
 * $Id: MapViewStorableObjectPool.java,v 1.7 2005/02/24 15:57:09 bob Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Ïâèàïð-æåéïêàåõìêë çåïæô.
 * Òôðåìæ: ÂÎÖÊÌÐÎ.
 */

package com.syrus.AMFICOM.mapview;

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
 * @version $Revision: 1.7 $, $Date: 2005/02/24 15:57:09 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public final class MapViewStorableObjectPool extends StorableObjectPool {

	private static final int				OBJECT_POOL_MAP_SIZE				= 50;		/*
																							 * Number
																							 * of
																							 * entities
																							 */

	private static final int				MAPVIEW_OBJECT_POOL_SIZE			= 3;

	private static MapViewObjectLoader			mvObjectLoader;
	private static MapViewStorableObjectPool	instance;

	private MapViewStorableObjectPool() {
		super(ObjectGroupEntities.MAPVIEW_GROUP_CODE);
	}

	private MapViewStorableObjectPool(Class cacheMapClass) {
		super(ObjectGroupEntities.MAPVIEW_GROUP_CODE, cacheMapClass);
	}

	/**
	 * 
	 * @param mvObjectLoader1
	 * @param cacheClass
	 *            class must extend LRUMap
	 * @param size
	 */
	public static void init(MapViewObjectLoader mvObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new MapViewStorableObjectPool(clazz);
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, use default '"
					+ ((clazz == null) ? "null" : clazz.getName()) + "'");
		}
		init(mvObjectLoader1, size);
	}

	public static void init(MapViewObjectLoader mvObjectLoader1, final int size) {
		if (instance == null)
			instance = new MapViewStorableObjectPool();
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(size));

		mvObjectLoader = mvObjectLoader1;

		instance.addObjectPool(ObjectEntities.MAPVIEW_ENTITY_CODE, size);

		instance.populatePools();
	}

	public static void init(MapViewObjectLoader mvObjectLoader1) {
		if (instance == null)
			instance = new MapViewStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));
		mvObjectLoader = mvObjectLoader1;
		instance.addObjectPool(ObjectEntities.MAPVIEW_ENTITY_CODE, MAPVIEW_OBJECT_POOL_SIZE);

		instance.populatePools();
	}

	public static void refresh() throws ApplicationException {
		instance.refreshImpl();
	}

	protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws ApplicationException {
		return mvObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static Collection getStorableObjects(Collection objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Collection getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Collection getStorableObjectsByConditionButIds(	Collection ids,
															StorableObjectCondition condition,
															boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				storableObject = mvObjectLoader.loadMapView(objectId);
				break;		
			default:
				Log.errorMessage("MapViewStorableObjectPool.loadStorableObject | Unknown entity: "
						+ ObjectEntities.codeToString(objectId.getMajor()));
				storableObject = null;
		}
		return storableObject;
	}

	protected Collection loadStorableObjects(Short entityCode, Collection ids) throws ApplicationException {
		Collection storableObjects;
		switch (entityCode.shortValue()) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				storableObjects = mvObjectLoader.loadMapViews(ids);
				break;
			default:
				Log.errorMessage("MapViewStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
				storableObjects = null;
		}
		return storableObjects;
	}

	protected Collection loadStorableObjectsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		Collection loadedCollection = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.MAP_ENTITY_CODE:
				loadedCollection  = mvObjectLoader.loadMapViewsButIds(condition, ids);
				break;
			default:
				Log.errorMessage("MapViewStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode));
				loadedCollection = null;
		}
		return loadedCollection;
	}

	protected void saveStorableObjects(short code, Collection collection, boolean force) throws ApplicationException {
		if (!collection.isEmpty()) {
			boolean alone = (collection.size() == 1);

			switch (code) {
				case ObjectEntities.MAPVIEW_ENTITY_CODE:
					if (alone)
						mvObjectLoader.saveMapView((MapView)collection.iterator().next(), force);
					else
						mvObjectLoader.saveMapViews(collection, force);
					break;
				default:
					Log.errorMessage("MapViewStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '"
							+ ObjectEntities.codeToString(code) + "'");
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static void flush(boolean force) throws ApplicationException {
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}

	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
	 	mvObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(Collection ids) throws IllegalDataException {
		mvObjectLoader.delete(ids);
	}

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(Collection ids) throws IllegalDataException {
		instance.deleteImpl(ids);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}
}
