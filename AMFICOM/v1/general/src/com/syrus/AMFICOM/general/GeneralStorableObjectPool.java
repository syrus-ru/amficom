/*
 * $Id: GeneralStorableObjectPool.java,v 1.31 2005/06/16 08:23:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Set;

import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.31 $, $Date: 2005/06/16 08:23:10 $
 * @author $Author: bass $
 * @module general_v1
 */

public final class GeneralStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 3; /* Number of entities */

	private static final int PARAMETERTYPE_OBJECT_POOL_SIZE = 9;
	private static final int CHARACTERISTICTYPE_OBJECT_POOL_SIZE = 9;
	private static final int CHARACTERISTIC_OBJECT_POOL_SIZE = 4;

	private static GeneralObjectLoader gObjectLoader;
	private static GeneralStorableObjectPool instance;


	private GeneralStorableObjectPool() {
		this(LRUMap.class);
	}

	private GeneralStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.GENERAL_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, new ParameterTypeFactory());
		registerFactory(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new CharacteristicTypeFactory());
		registerFactory(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new CharacteristicFactory());
	}

	public static void init(GeneralObjectLoader gObjectLoader1, final int size) {
		if (instance == null)
			instance = new GeneralStorableObjectPool();

		gObjectLoader = gObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, size);
	}

	public static void init(GeneralObjectLoader gObjectLoader1) {
		if (instance == null)
			instance = new GeneralStorableObjectPool();

		gObjectLoader = gObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, CHARACTERISTICTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, CHARACTERISTIC_OBJECT_POOL_SIZE);
	}

	/**
	 * @param objectLoader
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final GeneralObjectLoader objectLoader,
			final Class cacheClass, final int size) {
		if (size > 0) {
			instance = cacheClass == null
					? new GeneralStorableObjectPool()
					: new GeneralStorableObjectPool(cacheClass);
			init(objectLoader, size);
		}
		else {
			init(objectLoader, cacheClass);
		}
	}

	public static void init(GeneralObjectLoader gObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new GeneralStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, using default");
			instance = new GeneralStorableObjectPool();
		}
		init(gObjectLoader1);
	}

	protected Set refreshStorableObjects(Set storableObjects) throws ApplicationException {
		return gObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				return gObjectLoader.loadParameterTypes(ids);
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				return gObjectLoader.loadCharacteristicTypes(ids);
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				return gObjectLoader.loadCharacteristics(ids);
			default:
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		Set loadedObjects = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				loadedObjects = gObjectLoader.loadParameterTypesButIds(condition, ids);
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				loadedObjects = gObjectLoader.loadCharacteristicTypesButIds(condition, ids);
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				loadedObjects = gObjectLoader.loadCharacteristicsButIds(condition, ids);
				break;
			default:				
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedObjects = null;
		}
		return loadedObjects;
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				gObjectLoader.saveParameterTypes(storableObjects, force);
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				gObjectLoader.saveCharacteristicTypes(storableObjects, force);
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				gObjectLoader.saveCharacteristics(storableObjects, force);
				break;
			default:
				Log.errorMessage("GeneralStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		gObjectLoader.delete(identifiables);
	}

}
