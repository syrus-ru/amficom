/*
 * $Id: ResourceStorableObjectPool.java,v 1.11 2005/02/24 16:10:22 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.util.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author $Author: bob $
 * @version $Revision: 1.11 $, $Date: 2005/02/24 16:10:22 $
 * @module resource_v1
 */
public final class ResourceStorableObjectPool extends StorableObjectPool {
	
	private static final int		OBJECT_POOL_MAP_SIZE				= 14;//number of entities stored in a pool;
	
	private static final int		IMAGERESOURCE_OBJECT_POOL_SIZE		= 4;
	
	private static ResourceObjectLoader	rObjectLoader;
	private static ResourceStorableObjectPool instance;
	
	private ResourceStorableObjectPool() {
		super(ObjectGroupEntities.RESOURCE_GROUP_CODE);
	}
	
	private ResourceStorableObjectPool(Class cacheMapClass){
		super(ObjectGroupEntities.RESOURCE_GROUP_CODE, cacheMapClass);
	}
	
	public static void init(ResourceObjectLoader rObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new ResourceStorableObjectPool(clazz);
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default '"   //$NON-NLS-1$//$NON-NLS-2$
							 + ((clazz == null) ? "null" : clazz.getName()) + "'");  //$NON-NLS-1$//$NON-NLS-2$
		}
		init(rObjectLoader1, size);
	}
	
	public static void init(ResourceObjectLoader rObjectLoader1, final int size) {
		if (instance == null)
			instance = new ResourceStorableObjectPool();
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(size));

		rObjectLoader = rObjectLoader1;

		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, size);
				
		instance.populatePools();
	}
	
	public static void init(ResourceObjectLoader rObjectLoader1) {
		if (instance == null)
			instance = new ResourceStorableObjectPool();
		
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		rObjectLoader = rObjectLoader1;
		
		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, IMAGERESOURCE_OBJECT_POOL_SIZE);
		
		instance.populatePools();
	}
	
	public static void refresh() throws ApplicationException {        
    	instance.refreshImpl();
    }
	
	protected Set refreshStorableObjects(Set storableObjects) throws ApplicationException{
    	return rObjectLoader.refresh(storableObjects);
    }

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}
	
	public static Collection getStorableObjects(Collection objectIds, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}
	
	public static Collection getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Collection getStorableObjectsByConditionButIds(Collection ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}
	
	protected StorableObject loadStorableObject(Identifier objectId)
			throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
		case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
			storableObject = rObjectLoader.loadImageResource(objectId);
			break;
		default:
			Log.errorMessage("ResourceStorableObjectPool.loadStorableObject | Unknown entity: " //$NON-NLS-1$
							+ ObjectEntities.codeToString(objectId.getMajor()));
			storableObject = null;
		}
		return storableObject;
	}
	
	protected Collection loadStorableObjects(Short entityCode, Collection ids)
			throws ApplicationException {
		Collection storableObjects;
		switch (entityCode.shortValue()) {
		case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
			storableObjects = rObjectLoader.loadImageResources(ids);
			break;
		default:
			Log.errorMessage("ResourceStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode); //$NON-NLS-1$
			storableObjects = null;
		}
		return storableObjects;
	}
	
	protected Collection loadStorableObjectsButIds(StorableObjectCondition condition, Collection ids)
			throws ApplicationException {
		Collection loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				loadedList = rObjectLoader.loadImageResourcesButIds(condition, ids);
				break;
			default:
				Log.errorMessage("ResourceStorableObjectPool.loadStorableObjectsButIds | Unknown entity: " + ObjectEntities.codeToString(entityCode)); //$NON-NLS-1$
				loadedList = null;
		}		
		return loadedList;
	}
	
	protected void saveStorableObjects(short code, Collection list, boolean force) throws ApplicationException{
		if (!list.isEmpty()) {
			boolean alone = (list.size()==1);			
			switch (code) {
				case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
					if (alone)
						rObjectLoader.saveImageResource((AbstractImageResource)list.iterator().next(), force);
					else 
						rObjectLoader.saveImageResources(list, force);
					break;
				default:
					Log.errorMessage("ResourceStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '" + ObjectEntities.codeToString(code) + "'");  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject)
			throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
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
	
	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
		rObjectLoader.delete(id);
	}
	
	protected void deleteStorableObjects(Collection ids) throws IllegalDataException {
		rObjectLoader.delete(ids);
	}

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(Collection ids) throws IllegalDataException {
		instance.deleteImpl(ids);
	}

	public static void serializePool(){
		instance.serializePoolImpl();
	}
}
