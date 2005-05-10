/*
 * $Id: GeneralStorableObjectPool.java,v 1.24 2005/05/10 18:54:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2005/05/10 18:54:13 $
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

	{
		registerPool(ObjectGroupEntities.GENERAL_GROUP_CODE, this);
	}

	private GeneralStorableObjectPool() {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.GENERAL_GROUP_CODE);
	}

	private GeneralStorableObjectPool(Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.GENERAL_GROUP_CODE, cacheMapClass);
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

	public static void init(GeneralObjectLoader gObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new GeneralStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, using default");
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
			Log.errorMessage("Cache class '" + cacheClass.getName() + "' cannot be found, using default");
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

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		assert StorableObject.hasSingleTypeEntities(storableObjects);

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

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		return instance.fromTransferableImpl(id, transferable);
	}

	public static void flush(final Identifier id, final boolean force) throws ApplicationException {
		instance.flushImpl(id, force);
	}

	public static void flush(final short entityCode, final boolean force) throws ApplicationException {		 
		instance.flushImpl(entityCode, force);
	}

	public static void flush(final Short entityCode, final boolean force) throws ApplicationException {		 
		instance.flushImpl(entityCode, force);
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

	protected void deleteStorableObjects(final Set identifiables) {
		gObjectLoader.delete(identifiables);
	}

	public static void deserializePool() {
		instance.deserializePoolImpl();
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}

}
