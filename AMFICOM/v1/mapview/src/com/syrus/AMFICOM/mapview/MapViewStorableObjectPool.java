/*
 * $Id: MapViewStorableObjectPool.java,v 1.15 2005/05/10 18:57:46 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Ïâèàïð-æåéïêàåõìêë çåïæô.
 * Òôðåìæ: ÂÎÖÊÌÐÎ.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2005/05/10 18:57:46 $
 * @author $Author: bass $
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

	{
		registerPool(ObjectGroupEntities.MAPVIEW_GROUP_CODE, this);
	}

	private MapViewStorableObjectPool() {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.MAPVIEW_GROUP_CODE);
	}

	private MapViewStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.MAPVIEW_GROUP_CODE, cacheMapClass);
	}

	public static void init(MapViewObjectLoader mvObjectLoader1, final int size) {
		if (instance == null)
			instance = new MapViewStorableObjectPool();

		mvObjectLoader = mvObjectLoader1;

		instance.addObjectPool(ObjectEntities.MAPVIEW_ENTITY_CODE, size);
	}

	public static void init(MapViewObjectLoader mvObjectLoader1) {
		if (instance == null)
			instance = new MapViewStorableObjectPool();

		mvObjectLoader = mvObjectLoader1;
		instance.addObjectPool(ObjectEntities.MAPVIEW_ENTITY_CODE, MAPVIEW_OBJECT_POOL_SIZE);
	}

	public static void init(MapViewObjectLoader mvObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new MapViewStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
			instance = new MapViewStorableObjectPool();
		}
		init(mvObjectLoader1, size);
	}

	public static void init(MapViewObjectLoader mvObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new MapViewStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
			instance = new MapViewStorableObjectPool();
		}
		init(mvObjectLoader1);
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

	public static Set getStorableObjects(Set objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Set getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Set getStorableObjectsByConditionButIds(	Set ids,
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

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				return mvObjectLoader.loadMapViews(ids);
			default:
				Log.errorMessage("MapViewStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		Set loadedCollection = null;
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

	protected void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				if (singleton)
					mvObjectLoader.saveMapView((MapView)storableObjects.iterator().next(), force);
				else
					mvObjectLoader.saveMapViews(storableObjects, force);
				break;
			default:
				Log.errorMessage("MapViewStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '"
						+ ObjectEntities.codeToString(entityCode) + "'");
		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static void flush(final Identifier id, final boolean force) throws ApplicationException {
		instance.flushImpl(id, force);
	}

	public static void flush(final short entityCode, final boolean force) throws ApplicationException {		 
		instance.flushImpl(entityCode, force);
	}

	public static void flush(final Short entityCode, final boolean force) throws ApplicationException {		 
		instance.flushImpl(entityCode, force);
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

	protected void deleteStorableObject(Identifier id) {
	 	mvObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(final Set identifiables) {
		mvObjectLoader.delete(identifiables);
	}

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(final Set identifiables) {
		instance.deleteImpl(identifiables);
	}

	public static void deserializePool() {
		instance.deserializePoolImpl();
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}
}
