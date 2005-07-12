/*
 * $Id: MapViewStorableObjectPool.java,v 1.26 2005/07/12 08:40:58 bass Exp $
 *
 * Copyright ? 2004 Syrus Systems.
 * Ïâèàïð-æåéïêàåõìêë çåïæô.
 * Òôðåìæ: ÂÎÖÊÌÐÎ.
 */

package com.syrus.AMFICOM.mapview;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2005/07/12 08:40:58 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public final class MapViewStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 50; /* Number of entities*/

	private static final int MAPVIEW_OBJECT_POOL_SIZE = 3;

	private static MapViewObjectLoader			mvObjectLoader;
	private static MapViewStorableObjectPool	instance;


	private MapViewStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.MAPVIEW_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.MAPVIEW_CODE, new MapViewFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param mvObjectLoader1
	 */
	public static void init(final MapViewObjectLoader mvObjectLoader1) {
		init(mvObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param mvObjectLoader1
	 * @param size
	 */
	public static void init(final MapViewObjectLoader mvObjectLoader1, final int size) {
		init(mvObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param mvObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final MapViewObjectLoader mvObjectLoader1, final Class cacheClass) {
		assert mvObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new MapViewStorableObjectPool(cacheClass);

		mvObjectLoader = mvObjectLoader1;

		instance.addObjectPool(ObjectEntities.MAPVIEW_CODE, MAPVIEW_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param mvObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final MapViewObjectLoader mvObjectLoader1, final Class cacheClass, final int size) {
		assert mvObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new MapViewStorableObjectPool(cacheClass);

			mvObjectLoader = mvObjectLoader1;

			instance.addObjectPool(ObjectEntities.MAPVIEW_CODE, size);
		} else {
			init(mvObjectLoader1, cacheClass);
		}
	}


	@Override
	protected Set<Identifier> refreshStorableObjects(final Set<? extends StorableObject> storableObjects) throws ApplicationException {
		return mvObjectLoader.refresh(storableObjects);
	}

	@Override
	protected Set loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.MAPVIEW_CODE:
				return mvObjectLoader.loadMapViews(ids);
			default:
				Log.errorMessage("MapViewStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		final short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.MAP_CODE:
				return mvObjectLoader.loadMapViewsButIds(condition, ids);
			default:
				Log.errorMessage("MapViewStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.MAPVIEW_CODE:
				mvObjectLoader.saveMapViews(storableObjects, force);
				break;
			default:
				Log.errorMessage("MapViewStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	@Override
	protected void deleteStorableObjects(final Set<? extends Identifiable> identifiables) {
		mvObjectLoader.delete(identifiables);
	}

}
