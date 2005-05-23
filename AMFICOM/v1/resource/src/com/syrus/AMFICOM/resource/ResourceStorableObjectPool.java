/*
 * $Id: ResourceStorableObjectPool.java,v 1.25 2005/05/23 13:51:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

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
 * @author $Author: bass $
 * @version $Revision: 1.25 $, $Date: 2005/05/23 13:51:17 $
 * @module resource_v1
 */
public final class ResourceStorableObjectPool extends StorableObjectPool {
	
	private static final int		OBJECT_POOL_MAP_SIZE				= 14;//number of entities stored in a pool;
	
	private static final int		IMAGERESOURCE_OBJECT_POOL_SIZE		= 4;
	
	private static ResourceObjectLoader	rObjectLoader;
	private static ResourceStorableObjectPool instance;
	

	private ResourceStorableObjectPool() {
		this(LRUMap.class);
	}
	
	private ResourceStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.RESOURCE_GROUP_CODE, cacheMapClass);
	}

	public static void init(ResourceObjectLoader rObjectLoader1, final int size) {
		if (instance == null)
			instance = new ResourceStorableObjectPool();

		rObjectLoader = rObjectLoader1;

		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, size);
	}

	public static void init(ResourceObjectLoader rObjectLoader1) {
		if (instance == null)
			instance = new ResourceStorableObjectPool();

		rObjectLoader = rObjectLoader1;

		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, IMAGERESOURCE_OBJECT_POOL_SIZE);
	}

	public static void init(ResourceObjectLoader rObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new ResourceStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
			instance = new ResourceStorableObjectPool();
		}
		init(rObjectLoader1, size);
	}

	public static void init(ResourceObjectLoader rObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new ResourceStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
			instance = new ResourceStorableObjectPool();
		}
		init(rObjectLoader1);
	}

	protected Set refreshStorableObjects(Set storableObjects) throws ApplicationException{
		return rObjectLoader.refresh(storableObjects);
	}
	
	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				return rObjectLoader.loadImageResources(ids);
			default:
				Log.errorMessage("ResourceStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
				return Collections.EMPTY_SET;
		}
	}
	
	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids)
			throws ApplicationException {
		Set loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				loadedList = rObjectLoader.loadImageResourcesButIds(condition, ids);
				break;
			default:
				Log.errorMessage("ResourceStorableObjectPool.loadStorableObjectsButIds | Unknown entity: " + ObjectEntities.codeToString(entityCode));
				loadedList = null;
		}		
		return loadedList;
	}
	
	protected void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				rObjectLoader.saveImageResources(storableObjects, force);
				break;
			default:
				Log.errorMessage("ResourceStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '" + ObjectEntities.codeToString(entityCode) + "'");
		}
	}
	
	protected void deleteStorableObjects(final Set identifiables) {
		rObjectLoader.delete(identifiables);
	}

}
