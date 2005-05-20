/*
 * $Id: AdministrationStorableObjectPool.java,v 1.29 2005/05/20 21:11:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

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
 * @version $Revision: 1.29 $, $Date: 2005/05/20 21:11:25 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public final class AdministrationStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 6; /* Number of entities */

	private static final int USER_OBJECT_POOL_SIZE = 4;
	private static final int DOMAIN_OBJECT_POOL_SIZE = 4;
	private static final int SERVER_OBJECT_POOL_SIZE = 4;
	private static final int MCM_OBJECT_POOL_SIZE = 4;
	private static final int SERVERPROCESS_OBJECT_POOL_SIZE = 1;
	private static final int PERMATTR_OBJECT_POOL_SIZE = 4;

	private static AdministrationObjectLoader aObjectLoader;
	private static AdministrationStorableObjectPool instance;


	private AdministrationStorableObjectPool() {
		this(LRUMap.class);
	}

	private AdministrationStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, cacheMapClass);
	}

	public static void init(AdministrationObjectLoader aObjectLoader1, final int size) {
		if (instance == null)
			instance = new AdministrationStorableObjectPool();

		aObjectLoader = aObjectLoader1;

		instance.addObjectPool(ObjectEntities.USER_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.DOMAIN_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SERVER_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MCM_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.SERVERPROCESS_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, size);
	}

	public static void init(AdministrationObjectLoader aObjectLoader1) {
		if (instance == null)
			instance = new AdministrationStorableObjectPool();

		aObjectLoader = aObjectLoader1;

		instance.addObjectPool(ObjectEntities.USER_ENTITY_CODE, USER_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.DOMAIN_ENTITY_CODE, DOMAIN_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SERVER_ENTITY_CODE, SERVER_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MCM_ENTITY_CODE, MCM_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SERVERPROCESS_ENTITY_CODE, SERVERPROCESS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, PERMATTR_OBJECT_POOL_SIZE);
	}

	public static void init(AdministrationObjectLoader aObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new AdministrationStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, using default");
			instance = new AdministrationStorableObjectPool();
		}
		init(aObjectLoader1, size);
	}

	public static void init(AdministrationObjectLoader aObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new AdministrationStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, using default");
			instance = new AdministrationStorableObjectPool();
		}
		init(aObjectLoader1);
	}

	protected Set refreshStorableObjects(Set storableObjects) throws ApplicationException{
		return aObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.USER_ENTITY_CODE:
				return aObjectLoader.loadUsers(ids);
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				return aObjectLoader.loadDomains(ids);
			case ObjectEntities.SERVER_ENTITY_CODE:
				return aObjectLoader.loadServers(ids);
			case ObjectEntities.MCM_ENTITY_CODE:
				return aObjectLoader.loadMCMs(ids);
			case ObjectEntities.SERVERPROCESS_ENTITY_CODE:
				return aObjectLoader.loadServerProcesses(ids);
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				return aObjectLoader.loadPermissionAttributes(ids);
			default:
				Log.errorMessage("AdministrationStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		Set loadedObjects = null;
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
			case ObjectEntities.SERVERPROCESS_ENTITY_CODE:
				loadedObjects = aObjectLoader.loadServerProcessesButIds(condition, ids);
				break;
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				loadedList = aObjectLoader.loadPermissionAttributessButIds(condition, ids);
//				break;
			default:				
				Log.errorMessage("AdministrationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedObjects = null;
		}
		return loadedObjects;
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		assert StorableObject.hasSingleTypeEntities(storableObjects);

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.USER_ENTITY_CODE:
				aObjectLoader.saveUsers(storableObjects, force);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				aObjectLoader.saveDomains(storableObjects, force);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				aObjectLoader.saveServers(storableObjects, force);
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				aObjectLoader.saveMCMs(storableObjects, force);
				break;
			case ObjectEntities.SERVERPROCESS_ENTITY_CODE:
				aObjectLoader.saveServerProcesses(storableObjects, force);
				break;
			default:
				Log.errorMessage("AdministrationStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		aObjectLoader.delete(identifiables);
	}

}
