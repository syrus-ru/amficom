/*
 * $Id: AdministrationStorableObjectPool.java,v 1.2 2005/02/08 09:23:32 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

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
 * @version $Revision: 1.2 $, $Date: 2005/02/08 09:23:32 $
 * @author $Author: arseniy $
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
	}
	
	private AdministrationStorableObjectPool(Class cacheMapClass) {
		super(cacheMapClass);
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

	public static List getStorableObjects(List objectIds, boolean useLoader) throws DatabaseException, CommunicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static List getStorableObjectsByConditionButIds(List ids,
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

	protected List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
		List loadedList = null;
		switch (entityCode.shortValue()) {
			case ObjectEntities.USER_ENTITY_CODE:
				loadedList = aObjectLoader.loadUsers(ids);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				loadedList = aObjectLoader.loadDomains(ids);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				loadedList = aObjectLoader.loadServers(ids);
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				loadedList = aObjectLoader.loadMCMs(ids);
				break;
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				loadedList = aObjectLoader.loadPermissionAttributes(ids);
//				break;
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode.shortValue()) + "', entity code: " + entityCode);
				loadedList = null;
		}
		return loadedList;
	}

	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.USER_ENTITY_CODE:
				loadedList = aObjectLoader.loadUsersButIds(condition, ids);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				loadedList = aObjectLoader.loadDomainsButIds(condition, ids);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				loadedList = aObjectLoader.loadServersButIds(condition, ids);
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				loadedList = aObjectLoader.loadMCMsButIds(condition, ids);
				break;
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				loadedList = aObjectLoader.loadPermissionAttributessButIds(condition, ids);
//				break;
			default:				
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedList = null;
		}
		return loadedList;
	}

	//public static void save()

	protected void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		if (!list.isEmpty()) {
			boolean alone = (list.size() == 1);
			switch (code) {
				case ObjectEntities.USER_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveUser((User) list.get(0), force);
					else
						aObjectLoader.saveUsers(list, force);
					break;
				case ObjectEntities.DOMAIN_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveDomain((Domain) list.get(0), force);
					else
						aObjectLoader.saveDomains(list, force);
					break;
				case ObjectEntities.SERVER_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveServer((Server) list.get(0), force);
					else
						aObjectLoader.saveServers(list, force);
					break;
				case ObjectEntities.MCM_ENTITY_CODE:
					if (alone)
						aObjectLoader.saveMCM((MCM) list.get(0), force);
					else
						aObjectLoader.saveMCMs(list, force);
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

	public static void delete(Identifier id) throws DatabaseException, CommunicationException {
		instance.deleteImpl(id);
	}

	public static void delete(List objects) throws DatabaseException, CommunicationException, IllegalDataException {
		instance.deleteImpl(objects);
	}

	protected void deleteStorableObject(Identifier id) throws DatabaseException, CommunicationException {
		try {
			aObjectLoader.delete(id);
		}
		catch (DatabaseException e) {
			Log.errorMessage("AdministrationStorableObjectPool.deleteStorableObject | DatabaseException: " + e.getMessage());
			throw new DatabaseException("AdministrationStorableObjectPool.deleteStorableObject", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("AdministrationStorableObjectPool.deleteStorableObject | CommunicationException: " + e.getMessage());
			throw new CommunicationException("AdministrationStorableObjectPool.deleteStorableObject", e);
		}
	}
	
	protected void deleteStorableObjects(List objects) throws DatabaseException, CommunicationException, IllegalDataException {
		try {
			aObjectLoader.delete(objects);
		}
		catch (DatabaseException e) {
			Log.errorMessage("AdministrationStorableObjectPool.deleteStorableObjects | DatabaseException: " + e.getMessage());
			throw new DatabaseException("AdministrationStorableObjectPool.deleteStorableObjects", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("AdministrationStorableObjectPool.deleteStorableObjects | CommunicationException: " + e.getMessage());
			throw new CommunicationException("AdministrationStorableObjectPool.deleteStorableObjects", e);
		}
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

}
