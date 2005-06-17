/*
 * $Id: AdministrationStorableObjectPool.java,v 1.39 2005/06/17 13:22:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

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
 * @version $Revision: 1.39 $, $Date: 2005/06/17 13:22:13 $
 * @author $Author: bass $
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


	private AdministrationStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.SYSTEMUSER_CODE, new SystemUserFactory());
		registerFactory(ObjectEntities.DOMAIN_CODE, new DomainFactory());
		registerFactory(ObjectEntities.SERVER_CODE, new ServerFactory());
		registerFactory(ObjectEntities.MCM_CODE, new MCMFactory());
		registerFactory(ObjectEntities.SERVERPROCESS_CODE, new ServerProcessFactory());
//		registerFactory(ObjectEntities.PERMATTR_CODE, new PermissionAttributesFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param aObjectLoader1
	 */
	public static void init(final AdministrationObjectLoader aObjectLoader1) {
		init(aObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param aObjectLoader1
	 * @param size
	 */
	public static void init(final AdministrationObjectLoader aObjectLoader1, final int size) {
		init(aObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param aObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final AdministrationObjectLoader aObjectLoader1, final Class cacheClass) {
		assert aObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new AdministrationStorableObjectPool(cacheClass);

		aObjectLoader = aObjectLoader1;

		instance.addObjectPool(ObjectEntities.SYSTEMUSER_CODE, USER_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.DOMAIN_CODE, DOMAIN_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SERVER_CODE, SERVER_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MCM_CODE, MCM_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.SERVERPROCESS_CODE, SERVERPROCESS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PERMATTR_CODE, PERMATTR_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param aObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final AdministrationObjectLoader aObjectLoader1, final Class cacheClass, final int size) {
		assert aObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new AdministrationStorableObjectPool(cacheClass);

			aObjectLoader = aObjectLoader1;

			instance.addObjectPool(ObjectEntities.SYSTEMUSER_CODE, size);
			instance.addObjectPool(ObjectEntities.DOMAIN_CODE, size);
			instance.addObjectPool(ObjectEntities.SERVER_CODE, size);
			instance.addObjectPool(ObjectEntities.MCM_CODE, size);
			instance.addObjectPool(ObjectEntities.SERVERPROCESS_CODE, size);
			instance.addObjectPool(ObjectEntities.PERMATTR_CODE, size);
		}
		else {
			init(aObjectLoader1, cacheClass);
		}
	}


	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException{
		return aObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.SYSTEMUSER_CODE:
				return aObjectLoader.loadSystemUsers(ids);
			case ObjectEntities.DOMAIN_CODE:
				return aObjectLoader.loadDomains(ids);
			case ObjectEntities.SERVER_CODE:
				return aObjectLoader.loadServers(ids);
			case ObjectEntities.MCM_CODE:
				return aObjectLoader.loadMCMs(ids);
			case ObjectEntities.SERVERPROCESS_CODE:
				return aObjectLoader.loadServerProcesses(ids);
//			case ObjectEntities.PERMATTR_CODE:
//				return aObjectLoader.loadPermissionAttributes(ids);
			default:
				Log.errorMessage("AdministrationStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		final short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.SYSTEMUSER_CODE:
				return aObjectLoader.loadSystemUsersButIds(condition, ids);
			case ObjectEntities.DOMAIN_CODE:
				return aObjectLoader.loadDomainsButIds(condition, ids);
			case ObjectEntities.SERVER_CODE:
				return aObjectLoader.loadServersButIds(condition, ids);
			case ObjectEntities.MCM_CODE:
				return aObjectLoader.loadMCMsButIds(condition, ids);
			case ObjectEntities.SERVERPROCESS_CODE:
				return aObjectLoader.loadServerProcessesButIds(condition, ids);
			case ObjectEntities.PERMATTR_CODE:
//				return aObjectLoader.loadPermissionAttributessButIds(condition, ids);
			default:				
				Log.errorMessage("AdministrationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.SYSTEMUSER_CODE:
				aObjectLoader.saveSystemUsers(storableObjects, force);
				break;
			case ObjectEntities.DOMAIN_CODE:
				aObjectLoader.saveDomains(storableObjects, force);
				break;
			case ObjectEntities.SERVER_CODE:
				aObjectLoader.saveServers(storableObjects, force);
				break;
			case ObjectEntities.MCM_CODE:
				aObjectLoader.saveMCMs(storableObjects, force);
				break;
			case ObjectEntities.SERVERPROCESS_CODE:
				aObjectLoader.saveServerProcesses(storableObjects, force);
				break;
			default:
				Log.errorMessage("AdministrationStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		aObjectLoader.delete(identifiables);
	}

}
