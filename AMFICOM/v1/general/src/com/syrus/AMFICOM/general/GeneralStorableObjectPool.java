/*
 * $Id: GeneralStorableObjectPool.java,v 1.2 2005/01/19 20:42:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/19 20:42:59 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public final class GeneralStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 3;		/* Number of entities */

	private static final int PARAMETERTYPE_OBJECT_POOL_SIZE= 9;

	private static final int CHARACTERISTICTYPE_OBJECT_POOL_SIZE = 9;

	private static final int CHARACTERISTIC_OBJECT_POOL_SIZE = 4;

	private static GeneralObjectLoader	gObjectLoader;
	private static GeneralStorableObjectPool instance;

	private GeneralStorableObjectPool() {
		// singleton
	}

	private GeneralStorableObjectPool(Class cacheMapClass) {
		super(cacheMapClass);
	}

	public static void init(GeneralObjectLoader gObjectLoader1, final int size) {
		if (instance == null)
			instance = new GeneralStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		gObjectLoader = gObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, size);
		
		instance.populatePools();		
	}

	public static void init(GeneralObjectLoader gObjectLoader1) {
		if (instance == null)
			instance = new GeneralStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		gObjectLoader = gObjectLoader1;

		instance.addObjectPool(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, PARAMETERTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, CHARACTERISTICTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, CHARACTERISTIC_OBJECT_POOL_SIZE);
		
		instance.populatePools();
	}

	/**
	 * 
	 * @param gObjectLoader1
	 * @param cacheClass
	 *                class must extend LRUMap
	 * @param size
	 */
	public static void init(GeneralObjectLoader gObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new GeneralStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
		}
		init(gObjectLoader1, size);
	}

	public static void refresh() throws DatabaseException, CommunicationException {
		instance.refreshImpl();
	}

  protected Set refreshStorableObjects(Set storableObjects) throws CommunicationException, DatabaseException{
		return gObjectLoader.refresh(storableObjects);
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

	protected List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
		List loadedList = null;
		switch (entityCode.shortValue()) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				loadedList = gObjectLoader.loadParameterTypes(ids);
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				loadedList = gObjectLoader.loadCharacteristicTypes(ids);
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				loadedList = gObjectLoader.loadCharacteristics(ids);
				break;
			default:
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode.shortValue()) + "', entity code: " + entityCode);
				loadedList = null;
		}
		return loadedList;
	}

	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
				loadedList = gObjectLoader.loadParameterTypesButIds(condition, ids);
				break;
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				loadedList = gObjectLoader.loadCharacteristicTypesButIds(condition, ids);
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				loadedList = gObjectLoader.loadCharacteristicsButIds(condition, ids);
				break;
			default:				
				Log.errorMessage("GeneralStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedList = null;
		}
		return loadedList;
	}

	//public static void save()

	protected void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		if (!list.isEmpty()) {
			boolean alone = (list.size() == 1);
			switch (code) {
				case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
					if (alone)
						gObjectLoader.saveParameterType((ParameterType)list.get(0), force);
					else 
						gObjectLoader.saveParameterTypes(list, force);
					break;
				case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
					if (alone)
						gObjectLoader.saveCharacteristicType((CharacteristicType) list.get(0), force);
					else
						gObjectLoader.saveCharacteristicTypes(list, force);
					break;
				case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
					if (alone)
						gObjectLoader.saveCharacteristic((Characteristic) list.get(0), force);
					else
						gObjectLoader.saveCharacteristics(list, force);
					break;
				default:
					Log.errorMessage("GeneralStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(code) + "', entity code: " + code);
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

	public static void delete(List ids) throws DatabaseException, CommunicationException {
		instance.deleteImpl(ids);
	}

	protected void deleteStorableObject(Identifier id) throws DatabaseException, CommunicationException {
		try {
			gObjectLoader.delete(id);
		}
		catch (DatabaseException e) {
			Log.errorMessage("GeneralStorableObjectPool.deleteStorableObject | DatabaseException: " + e.getMessage());
			throw new DatabaseException("GeneralStorableObjectPool.deleteStorableObject", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("GeneralStorableObjectPool.deleteStorableObject | CommunicationException: " + e.getMessage());
			throw new CommunicationException("GeneralStorableObjectPool.deleteStorableObject", e);
		}
	}
	
	protected void deleteStorableObjects(List ids) throws DatabaseException, CommunicationException {
		try {
			gObjectLoader.delete(ids);
		}
		catch (DatabaseException e) {
			Log.errorMessage("GeneralStorableObjectPool.deleteStorableObjects | DatabaseException: " + e.getMessage());
			throw new DatabaseException("GeneralStorableObjectPool.deleteStorableObjects", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("GeneralStorableObjectPool.deleteStorableObjects | CommunicationException: " + e.getMessage());
			throw new CommunicationException("GeneralStorableObjectPool.deleteStorableObjects", e);
		}
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

}
