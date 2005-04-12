/*
 * $Id: ResourceStorableObjectPool.java,v 1.15 2005/04/12 08:14:28 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/04/12 08:14:28 $
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
		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(size));

		rObjectLoader = rObjectLoader1;

		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, size);
				
		instance.populatePools();
	}
	
	public static void init(ResourceObjectLoader rObjectLoader1) {
		if (instance == null)
			instance = new ResourceStorableObjectPool();
		
		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

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
	
	public static Set getStorableObjects(Set objectIds, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}
	
	public static Set getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Set getStorableObjectsByConditionButIds(Set ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
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
	
	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				return rObjectLoader.loadImageResources(ids);
			default:
				Log.errorMessage("ResourceStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode); //$NON-NLS-1$
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
				Log.errorMessage("ResourceStorableObjectPool.loadStorableObjectsButIds | Unknown entity: " + ObjectEntities.codeToString(entityCode)); //$NON-NLS-1$
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
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				if (singleton)
					rObjectLoader.saveImageResource((AbstractImageResource)storableObjects.iterator().next(), force);
				else 
					rObjectLoader.saveImageResources(storableObjects, force);
				break;
			default:
				Log.errorMessage("ResourceStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '" + ObjectEntities.codeToString(entityCode) + "'");  //$NON-NLS-1$//$NON-NLS-2$
		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		return instance.fromTransferableImpl(id, transferable);
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
	
	protected void deleteStorableObjects(final Set identifiables) throws IllegalDataException {
		rObjectLoader.delete(identifiables);
	}

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(final Set identifiables) {
		instance.deleteImpl(identifiables);
	}

	public static void serializePool(){
		instance.serializePoolImpl();
	}
}
