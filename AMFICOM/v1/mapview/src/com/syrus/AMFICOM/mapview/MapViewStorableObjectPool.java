/*
 * $Id: MapViewStorableObjectPool.java,v 1.20 2005/05/24 16:40:05 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Ïâèàïð-æåéïêàåõìêë çåïæô.
 * Òôðåìæ: ÂÎÖÊÌÐÎ.
 */

package com.syrus.AMFICOM.mapview;

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
 * @version $Revision: 1.20 $, $Date: 2005/05/24 16:40:05 $
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


	private MapViewStorableObjectPool() {
		this(LRUMap.class);
	}

	private MapViewStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.MAPVIEW_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.MAPVIEW_ENTITY_CODE, new MapViewFactory());
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

	protected java.util.Set refreshStorableObjects(java.util.Set storableObjects) throws ApplicationException {
		return mvObjectLoader.refresh(storableObjects);
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
		switch (entityCode) {
			case ObjectEntities.MAPVIEW_ENTITY_CODE:
				mvObjectLoader.saveMapViews(storableObjects, force);
				break;
			default:
				Log.errorMessage("MapViewStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '"
						+ ObjectEntities.codeToString(entityCode) + "'");
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		mvObjectLoader.delete(identifiables);
	}

}
