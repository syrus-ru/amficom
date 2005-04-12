/*
 * $Id: GeneralStorableObjectPool.java,v 1.18 2005/04/12 16:24:48 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $, $Date: 2005/04/12 16:24:48 $
 * @author $Author: arseniy $
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
		// singleton
		super(ObjectGroupEntities.GENERAL_GROUP_CODE);
	}

	private GeneralStorableObjectPool(Class cacheMapClass) {
		super(ObjectGroupEntities.GENERAL_GROUP_CODE, cacheMapClass);
	}

	public static void init(GeneralObjectLoader gObjectLoader1, final int size) {
		if (instance == null)
			instance = new GeneralStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		gObjectLoader = gObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, size);

		instance.populatePools();
	}

	public static void init(GeneralObjectLoader gObjectLoader1) {
		if (instance == null)
			instance = new GeneralStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		gObjectLoader = gObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, CHARACTERISTICTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, CHARACTERISTIC_OBJECT_POOL_SIZE);

		instance.populatePools();
	}

	public static void init(GeneralObjectLoader gObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new GeneralStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, use default");
			instance = new GeneralStorableObjectPool();
		}
		init(gObjectLoader1, size);
	}

	public static void init(GeneralObjectLoader gObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new GeneralStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, use default");
			instance = new GeneralStorableObjectPool();
		}
		init(gObjectLoader1);
	}

	public static void refresh() throws ApplicationException {
		instance.refreshImpl();
	}

  protected Set refreshStorableObjects(Set storableObjects) throws ApplicationException {
		return gObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static Set getStorableObjects(Set objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Set getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Set getStorableObjectsByConditionButIds(Set ids,
												StorableObjectCondition condition,
												boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				storableObject = gObjectLoader.loadParameterType(objectId);
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				storableObject = gObjectLoader.loadCharacteristicType(objectId);
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				storableObject = gObjectLoader.loadCharacteristic(objectId);
				break;
			default:
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObject | Unknown entity: '" + ObjectEntities.codeToString(objectId.getMajor()) + "', entity code: " + objectId.getMajor());
				storableObject = null;
		}
		return storableObject;
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				return gObjectLoader.loadParameterTypes(ids);
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				return gObjectLoader.loadCharacteristicTypes(ids);
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				return gObjectLoader.loadCharacteristics(ids);
			default:
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode); //$NON-NLS-1$ //$NON-NLS-2$
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

	//public static void save()

	protected void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				if (singleton)
					gObjectLoader.saveParameterType((ParameterType)storableObjects.iterator().next(), force);
				else 
					gObjectLoader.saveParameterTypes(storableObjects, force);
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				if (singleton)
					gObjectLoader.saveCharacteristicType((CharacteristicType) storableObjects.iterator().next(), force);
				else
					gObjectLoader.saveCharacteristicTypes(storableObjects, force);
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				if (singleton)
					gObjectLoader.saveCharacteristic((Characteristic) storableObjects.iterator().next(), force);
				else
					gObjectLoader.saveCharacteristics(storableObjects, force);
				break;
			default:
				Log.errorMessage("GeneralStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		return instance.fromTransferableImpl(id, transferable);
	}

	public static void flush(boolean force) throws ApplicationException {		 
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

	public static void delete(final Set identifiables) {
		instance.deleteImpl(identifiables);
	}

	protected void deleteStorableObject(Identifier id) {
		gObjectLoader.delete(id);
	}
	
	protected void deleteStorableObjects(final Set identifiables) {
		gObjectLoader.delete(identifiables);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}

}
