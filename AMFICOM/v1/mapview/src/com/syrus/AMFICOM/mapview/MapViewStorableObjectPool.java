/*
 * $Id: MapViewStorableObjectPool.java,v 1.3 2005/02/08 14:07:57 bob Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Ïâèàïð-æåéïêàåõìêë çåïæô.
 * Òôðåìæ: ÂÎÖÊÌÐÎ.
 */

package com.syrus.AMFICOM.mapview;

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
 * @version $Revision: 1.3 $, $Date: 2005/02/08 14:07:57 $
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
		// empty
	}

	private MapViewStorableObjectPool(Class cacheMapClass) {
		super(cacheMapClass);
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

	public static void refresh() throws DatabaseException, CommunicationException {
		instance.refreshImpl();
	}

	protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws CommunicationException,
			DatabaseException {
		return mvObjectLoader.refresh(storableObjects);
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

	protected List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
		List storableObjects;
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

	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.MAP_ENTITY_CODE:
				loadedList  = mvObjectLoader.loadMapViewsButIds(condition, ids);
				break;
			default:
				Log.errorMessage("MapViewStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
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
				case ObjectEntities.MAPVIEW_ENTITY_CODE:
					if (alone)
						mvObjectLoader.saveMapView((MapView)list.get(0), force);
					else
						mvObjectLoader.saveMapViews(list, force);
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
	 	mvObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(List ids) throws DatabaseException, CommunicationException {
		mvObjectLoader.delete(ids);
	}

	public static void delete(Identifier id) throws DatabaseException, CommunicationException {
		instance.deleteImpl(id);
	}

	public static void delete(List ids) throws DatabaseException, CommunicationException, IllegalDataException {
		instance.deleteImpl(ids);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}
}
