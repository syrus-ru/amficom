/*
 * $Id: ResourceStorableObjectPool.java,v 1.4 2005/01/17 17:17:46 max Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.util.Log;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * @author $Author: max $
 * @version $Revision: 1.4 $, $Date: 2005/01/17 17:17:46 $
 * @module resource_v1
 */
public final class ResourceStorableObjectPool extends StorableObjectPool {
	
	private static final int		OBJECT_POOL_MAP_SIZE				= 14;//number of entities stored in a pool;
	
	private static final int		IMAGERESOURCE_OBJECT_POOL_SIZE		= 4;
	
	private static DatabaseResourceObjectLoader	rObjectLoader;
	private static ResourceStorableObjectPool instance;
	
	private ResourceStorableObjectPool() {
		//empty
	}
	
	private ResourceStorableObjectPool(Class cacheMapClass){
		super(cacheMapClass);
	}
	
	public static void init(DatabaseResourceObjectLoader rObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new ResourceStorableObjectPool(clazz);
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default '" 
							 + ((clazz == null) ? "null" : clazz.getName()) + "'");
		}
		init(rObjectLoader1, size);
	}
	
	public static void init(DatabaseResourceObjectLoader rObjectLoader1, final int size) {
		if (instance == null)
			instance = new ResourceStorableObjectPool();
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(size));

		rObjectLoader = rObjectLoader1;

		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, size);
				
		instance.populatePools();
	}
	
	public static void init(DatabaseResourceObjectLoader rObjectLoader1) {
		if (instance == null)
			instance = new ResourceStorableObjectPool();
		
		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		rObjectLoader = rObjectLoader1;
		
		instance.addObjectPool(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE, IMAGERESOURCE_OBJECT_POOL_SIZE);
		
		instance.populatePools();
	}
	
	public static void refresh() throws DatabaseException, CommunicationException {        
    	instance.refreshImpl();
    }
	
	protected Set refreshStorableObjects(Set storableObjects) throws CommunicationException, DatabaseException{
    	return rObjectLoader.refresh(storableObjects);
    }

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader)
			throws DatabaseException, CommunicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}
	
	public static List getStorableObjects(List objectIds, boolean useLoader)
			throws DatabaseException, CommunicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}
	
	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static List getStorableObjectsByConditionButIds(List ids, StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}
	
	protected StorableObject loadStorableObject(Identifier objectId)
			throws DatabaseException, CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
		case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
			storableObject = rObjectLoader.loadImageResource(objectId);
			break;
		default:
			Log.errorMessage("ResourceStorableObjectPool.loadStorableObject | Unknown entity: "
							+ ObjectEntities.codeToString(objectId.getMajor()));
			storableObject = null;
		}
		return storableObject;
	}
	
	protected List loadStorableObjects(Short entityCode, List ids)
			throws DatabaseException, CommunicationException {
		List storableObjects;
		switch (entityCode.shortValue()) {
		case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
			storableObjects = rObjectLoader.loadImageResources(ids);
			break;
		default:
			Log.errorMessage("ResourceStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
			storableObjects = null;
		}
		return storableObjects;
	}
	
	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		List loadedList = null;
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
	
	protected void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException{
		if (!list.isEmpty()) {
			boolean alone = (list.size()==1);			
			switch (code) {
			case ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE:
				if (alone)
					rObjectLoader.saveImageResource((AbstractImageResource)list.get(0), force);
				else 
					rObjectLoader.saveImageResources(list, force);
				break;
			default:
				Log.errorMessage("ResourceStorableObjectPool.saveStorableObjects | Unknown Unknown entity : '" + ObjectEntities.codeToString(code) + "'");
			}
		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject)
		throws IllegalObjectEntityException {
	return instance.putStorableObjectImpl(storableObject);
	}
	
	public static void flush(boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException{		 
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}
	
	protected void deleteStorableObject(Identifier id) throws DatabaseException, CommunicationException {
		try {
            rObjectLoader.delete(id);
        } catch (DatabaseException e) {
            Log.errorMessage("ResourceStorableObjectPool.deleteStorableObject | DatabaseException: " + e.getMessage());
            throw new DatabaseException("ResourceStorableObjectPool.deleteStorableObject", e);
        }
	}
	
	protected void deleteStorableObjects(List ids) throws DatabaseException, CommunicationException {
		try {
			rObjectLoader.delete(ids);
        } catch (DatabaseException e) {
            Log.errorMessage("ResourceStorableObjectPool.deleteStorableObjects | DatabaseException: " + e.getMessage());
            throw new DatabaseException("ResourceStorableObjectPool.deleteStorableObjects", e);
        }
	}
	
	public static void delete(Identifier id) throws DatabaseException, CommunicationException {
        instance.deleteImpl(id);
	}
    
    public static void delete(List ids) throws DatabaseException, CommunicationException {
    	instance.deleteImpl(ids);
    }
    
    public static void serializePool(){
    	instance.serializePoolImpl();
    }
}
