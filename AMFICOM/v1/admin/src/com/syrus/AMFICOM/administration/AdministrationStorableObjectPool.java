/*
 * $Id: AdministrationStorableObjectPool.java,v 1.9 2005/02/22 11:17:53 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/02/22 11:17:53 $
 * @author $Author: bob $
 * @module administration_v1
 */

public final class AdministrationStorableObjectPool extends StorableObjectPool {

	private static final int			OBJECT_POOL_MAP_SIZE			= 5;		/* Number of entities */

	private static final int			USER_OBJECT_POOL_SIZE			= 4;
	private static final int			DOMAIN_OBJECT_POOL_SIZE			= 4;
	private static final int			SERVER_OBJECT_POOL_SIZE			= 4;
	private static final int			MCM_OBJECT_POOL_SIZE			= 4;
	private static final int			PERMATTR_OBJECT_POOL_SIZE		= 4;

	private static AdministrationObjectLoader	aObjectLoader;
	private static AdministrationStorableObjectPool instance;

	private AdministrationStorableObjectPool() {
		// singleton
		super(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE);
	}
	
	private AdministrationStorableObjectPool(Class cacheMapClass) {
		super(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, cacheMapClass);
	}

	public static void init(AdministrationObjectLoader aObjectLoader1, final int size) {
		if (instance == null)
			instance = new AdministrationStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		aObjectLoader = aObjectLoader1;

		instance.addObjectPool(ObjectEntities.USER_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.DOMAIN_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SERVER_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MCM_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, size);		
		
		instance.populatePools();		
	}

	public static void init(AdministrationObjectLoader aObjectLoader1) {
		if (instance == null)
			instance = new AdministrationStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		aObjectLoader = aObjectLoader1;

		instance.addObjectPool(ObjectEntities.USER_ENTITY_CODE, USER_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.DOMAIN_ENTITY_CODE, DOMAIN_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SERVER_ENTITY_CODE, SERVER_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MCM_ENTITY_CODE, MCM_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, PERMATTR_OBJECT_POOL_SIZE);
		
		instance.populatePools();
	}

	/**
	 * 
	 * @param aObjectLoader1
	 * @param cacheClass
	 *                class must extend LRUMap
	 * @param size
	 */
	public static void init(AdministrationObjectLoader aObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new AdministrationStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
		}
		init(aObjectLoader1, size);
	}

	public static void refresh() throws DatabaseException, CommunicationException {
		instance.refreshImpl();
	}

  protected Set refreshStorableObjects(Set storableObjects) throws CommunicationException, DatabaseException{
		return aObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws DatabaseException, CommunicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static Collection getStorableObjects(Collection objectIds, boolean useLoader) throws DatabaseException, CommunicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Collection getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Collection getStorableObjectsByConditionButIds(Collection ids,
								StorableObjectCondition condition,
								boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.USER_ENTITY_CODE:
				storableObject = aObjectLoader.loadUser(objectId);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				storableObject = aObjectLoader.loadDomain(objectId);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				storableObject = aObjectLoader.loadServer(objectId);
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				storableObject = aObjectLoader.loadMCM(objectId);
				break;
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				storableObject = aObjectLoader.loadPermissionAttributes(objectId);
//				break;
			default:
				Log.errorMessage("AdministrationStorableObjectPool.loadStorableObject | Unknown entity: '" + ObjectEntities.codeToString(objectId.getMajor()) + "', entity code: " + objectId.getMajor());
				storableObject = null;
		}
		return storableObject;
	}

	protected Collection loadStorableObjects(Short entityCode, Collection ids) throws DatabaseException, CommunicationException {
		Collection loadedObjects = null;
		switch (entityCode.shortValue()) {
			case ObjectEntities.USER_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadUsers(ids);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadDomains(ids);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadServers(ids);
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadMCMs(ids);
				break;
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				loadedList = aObjectLoader.loadPermissionAttributes(ids);
//				break;
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode.shortValue()) + "', entity code: " + entityCode);
				loadedObjects = null;
		}
		return loadedObjects;
	}

	protected Collection loadStorableObjectsButIds(StorableObjectCondition condition, Collection ids)
			throws DatabaseException, CommunicationException {
		Collection loadedObjects = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.USER_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadUsersButIds(condition, ids);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadDomainsButIds(condition, ids);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadServersButIds(condition, ids);
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadMCMsButIds(condition, ids);
				break;
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				loadedList = aObjectLoader.loadPermissionAttributessButIds(condition, ids);
//				break;
			default:				
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedObjects = null;
		}
		return loadedObjects;
	}

	//public static void save()

	protected void saveStorableObjects(short code, Collection collection, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		if (!collection.isEmpty()) {
			boolean alone = (collection.size() == 1);
			switch (code) {
				case ObjectEntities.USER_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveUser((User) collection.iterator().next(), force);
					else
						aObjectLoader.saveUsers(collection, force);
					break;
				case ObjectEntities.DOMAIN_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveDomain((Domain) collection.iterator().next(), force);
					else
						aObjectLoader.saveDomains(collection, force);
					break;
				case ObjectEntities.SERVER_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveServer((Server) collection.iterator().next(), force);
					else
						aObjectLoader.saveServers(collection, force);
					break;
				case ObjectEntities.MCM_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveMCM((MCM) collection.iterator().next(), force);
					else
						aObjectLoader.saveMCMs(collection, force);
					break;
				default:
					Log.errorMessage("AdministrationStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(code) + "', entity code: " + code);
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
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

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(Collection objects) throws IllegalDataException {
		instance.deleteImpl(objects);
	}

	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
		aObjectLoader.delete(id);
	}
	
	protected void deleteStorableObjects(Collection objects) throws IllegalDataException {
		aObjectLoader.delete(objects);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

}
