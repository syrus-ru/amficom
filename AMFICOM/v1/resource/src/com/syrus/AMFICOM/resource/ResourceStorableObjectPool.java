/*
 * $Id: ResourceStorableObjectPool.java,v 1.30 2005/06/16 12:58:45 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

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
 * @author $Author: arseniy $
 * @version $Revision: 1.30 $, $Date: 2005/06/16 12:58:45 $
 * @module resource_v1
 */
public final class ResourceStorableObjectPool extends StorableObjectPool {
	
	private static final int OBJECT_POOL_MAP_SIZE = 14;// number of entities stored in a pool;

	private static final int IMAGERESOURCE_OBJECT_POOL_SIZE = 4;
	
	private static ResourceObjectLoader	rObjectLoader;
	private static ResourceStorableObjectPool instance;
	
	
	private ResourceStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.RESOURCE_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, new ImageResourceFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param rObjectLoader1
	 */
	public static void init(final ResourceObjectLoader rObjectLoader1) {
		init(rObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param rObjectLoader1
	 * @param size
	 */
	public static void init(final ResourceObjectLoader rObjectLoader1, final int size) {
		init(rObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param rObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final ResourceObjectLoader rObjectLoader1, final Class cacheClass) {
		assert rObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new ResourceStorableObjectPool(cacheClass);

		rObjectLoader = rObjectLoader1;

		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, IMAGERESOURCE_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param rObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final ResourceObjectLoader rObjectLoader1, final Class cacheClass, final int size) {
		assert rObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new ResourceStorableObjectPool(cacheClass);

			rObjectLoader = rObjectLoader1;

			instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, size);
		}
		else {
			init(rObjectLoader1, cacheClass);
		}
	}


	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException{
		return rObjectLoader.refresh(storableObjects);
	}
	
	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				return rObjectLoader.loadImageResources(ids);
			default:
				Log.errorMessage("ResourceStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}
	
	protected Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids)
			throws ApplicationException {
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				return rObjectLoader.loadImageResourcesButIds(condition, ids);
			default:
				Log.errorMessage("ResourceStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				rObjectLoader.saveImageResources(storableObjects, force);
				break;
			default:
				Log.errorMessage("ResourceStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		rObjectLoader.delete(identifiables);
	}

}
