/*-
 * $Id: StorableObjectPool.java,v 1.155 2005/08/15 10:42:52 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;
import gnu.trove.TShortObjectIterator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.io.LRUMapSaver;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.155 $, $Date: 2005/08/15 10:42:52 $
 * @author $Author: arseniy $
 * @module general
 * @todo Этот класс не проверен. В первую очередь надо проверить работу с объектами, помеченными на удаление
 * (т. е. объектами, идентификаторы которых помещены в DELETED_IDS_MAP). Проверять так:
 * 1) заполнить кишки разнообразными объектами;
 * 2) пометить некоторые из этих объектов на удаление;
 * 3) проверить все вызовы подгрузки get<*>, при этом во входных наборах идентификаторов (как и "для", так и "кроме")
 * должны присутствовать идентификаторы помеченных на удаление объектов;
 * 4) проверить вызов refresh
 * 5) проверить все вызовы flush, убедиться что объекты, помеченные на удаление, действительно удаляются.
 */
public final class StorableObjectPool {
	private static final int OBJECT_POOL_SIZE = 10;
	private static final int MAX_OBJECT_POOL_SIZE = 1000;

	/**
	 * БАЙАН-symbol {@value}
	 */
	public static final String БАЙАН = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	/**
	 * <short entityCode, (? extends LRUMap) objectPool>
	 * Implementation of object pool itself
	 */
	private static TShortObjectHashMap objectPoolMap;

	/**
	 * Object loader -- loads objects, not found in pool
	 */
	private static ObjectLoader objectLoader;

	/**
	 * Class of object pool. Must be subclass of LRUMap
	 */
	private static Class objectPoolClass;

	private static final Map<Short, Set<Identifier>> DELETED_IDS_MAP = new HashMap<Short, Set<Identifier>>();

	private static final Set<Identifier> LOCKED_IDS = new HashSet<Identifier>();
	private static final long MAX_LOCK_TIMEOUT = 1 * 60 * 1000; // 1 minuta
	private static final long LOCK_TIME_WAIT = 5 * 1000; // 5 sec

	private static final DependencySortedContainer DEPENDENCY_SORTED_CONTAINER = new DependencySortedContainer();
	private static final Set<Identifier> SAVING_OBJECT_IDS = new HashSet<Identifier>();


	private static final class DependencySortedContainer {
		private SortedMap<Integer, Map<Short, Set<StorableObject>>> objectsMap;

		private DependencySortedContainer() {
			this.objectsMap = new TreeMap<Integer, Map<Short, Set<StorableObject>>>();
		}

		void put(final StorableObject storableObject, final int dependencyLevel) {
			final Identifier id = storableObject.getId();
			final Integer dependencyKey = new Integer(-dependencyLevel);
			Map<Short, Set<StorableObject>> levelSavingObjectsMap = this.objectsMap.get(dependencyKey);
			if (levelSavingObjectsMap == null) {
				levelSavingObjectsMap = new HashMap<Short, Set<StorableObject>>();
				this.objectsMap.put(dependencyKey, levelSavingObjectsMap);
			}
			final Short entityKey = new Short(id.getMajor());
			Set<StorableObject> levelEntitySavingObjects = levelSavingObjectsMap.get(entityKey);
			if (levelEntitySavingObjects == null) {
				levelEntitySavingObjects = new HashSet<StorableObject>();
				levelSavingObjectsMap.put(entityKey, levelEntitySavingObjects);
			}
			levelEntitySavingObjects.add(storableObject);
		}

		Set<Integer> dependencyKeySet() {
			return this.objectsMap.keySet();
		}

		Map<Short, Set<StorableObject>> getLevelEntityMap(final Integer dependencyKey) {
			return this.objectsMap.get(dependencyKey);
		}

		void moveIfAlreadyPresent(final StorableObject storableObject, final int dependencyLevel) {
			final Integer dependencyKey0 = new Integer(-(dependencyLevel - 1));
			if (!this.objectsMap.containsKey(dependencyKey0)) {
				return;
			}

			final SortedMap<Integer, Map<Short, Set<StorableObject>>> rangeObjectsMap = this.objectsMap.tailMap(dependencyKey0);
			Integer foundDependencyKey = null;
			for (final Integer dependencyKey : rangeObjectsMap.keySet()) {
				if (this.containsOnDependencyLevel(storableObject, dependencyKey)) {
					foundDependencyKey = dependencyKey;
				}
			}
			if (foundDependencyKey == null) {
				return;
			}
			this.remove(storableObject, foundDependencyKey);
			this.put(storableObject, dependencyLevel);
		}

		private boolean containsOnDependencyLevel(final StorableObject storableObject, final Integer dependencyKey) {
			final Identifier id = storableObject.getId();
			Map<Short, Set<StorableObject>> levelSavingObjectsMap = this.objectsMap.get(dependencyKey);
			if (levelSavingObjectsMap == null) {
				return false;
			}
			final Short entityKey = new Short(id.getMajor());
			final Set<StorableObject> levelEntitySavingObjects = levelSavingObjectsMap.get(entityKey);
			if (levelEntitySavingObjects == null) {
				return false;
			}
			return levelEntitySavingObjects.contains(storableObject);
		}

		boolean remove(final StorableObject storableObject, final int dependencyLevel) {
			final Integer dependencyKey = new Integer(-dependencyLevel);
			return this.remove(storableObject, dependencyKey);
		}

		private boolean remove(final StorableObject storableObject, final Integer dependencyKey) {
			final Identifier id = storableObject.getId();
			final Map<Short, Set<StorableObject>> levelSavingObjectsMap = this.objectsMap.get(dependencyKey);
			if (levelSavingObjectsMap == null) {
				return false;
			}
			final Short entityKey = new Short(id.getMajor());
			final Set<StorableObject> levelEntitySavingObjects = levelSavingObjectsMap.get(entityKey);
			if (levelEntitySavingObjects == null) {
				return false;
			}
			return levelEntitySavingObjects.remove(storableObject);
		}

		void clear() {
			this.objectsMap.clear();
		}
	}


	private StorableObjectPool() {
		// singleton
		assert false;
	}
	
	public static void init(final ObjectLoader objectLoader1) {
		init(objectLoader1, LRUMap.class);
	}

	public static void init(final ObjectLoader objectLoader1, final Class objectPoolClass1) {
		objectPoolMap = new TShortObjectHashMap();
		objectLoader = objectLoader1;
		try {
			objectPoolClass = Class.forName(objectPoolClass1.getName());
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + objectPoolClass1.getName() + "' cannot be found, using default");
			objectPoolClass = LRUMap.class;
		}
	}

	public static void addObjectPoolGroup(final short groupCode, final int size) {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE;
		final int objectPoolSize = (size <= 0 || size > MAX_OBJECT_POOL_SIZE) ? OBJECT_POOL_SIZE : size;
		final short[] entityCodes = ObjectGroupEntities.getEntityCodes(groupCode);
		for (int i = 0; i < entityCodes.length; i++) {
			addObjectPool(entityCodes[i], objectPoolSize);
		}
	}

	private static void addObjectPool(final short entityCode, final int objectPoolSize) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		try {
			final Constructor constructor = objectPoolClass.getConstructor(new Class[] { int.class});
			final Object obj = constructor.newInstance(new Object[] { new Integer(objectPoolSize)});
			if (obj instanceof LRUMap) {
				final LRUMap objectPool = (LRUMap) obj;
				objectPoolMap.put(entityCode, objectPool);
				Log.debugMessage("StorableObjectPool.addObjectPool | Pool for '" + ObjectEntities.codeToString(entityCode)
						+ "'/" + entityCode + " of size " + objectPoolSize + " added", Log.DEBUGLEVEL07);
			} else
				throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | Object pool class "
						+ objectPoolClass.getName() + " must extend LRUMap");
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass "
					+ objectPoolClass.getName() + " SecurityException " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass "
					+ objectPoolClass.getName() + " IllegalArgumentException " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass "
					+ objectPoolClass.getName() + " NoSuchMethodException " + e.getMessage());
		} catch (InstantiationException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass "
					+ objectPoolClass.getName() + " InstantiationException " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass "
					+ objectPoolClass.getName() + " IllegalAccessException " + e.getMessage());
		} catch (final InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else
				throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass "
						+ objectPoolClass.getName() + " InvocationTargetException " + ite.getMessage());
		}

	}



	/*	Get */

	public static <T extends StorableObject> T getStorableObject(final Identifier id, final boolean useLoader)
			throws ApplicationException {
		assert id != null : ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = id.getMajor();

		/*
		 * Do not load:
		 * a. anything if a void identifier is supplied;
		 * b. deleted objects.
		 */
		if (id.isVoid()) {
			return null;
		}
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null && entityDeletedIds.contains(id)) {
			return null;
		}

		final LRUMap<Identifier, T> objectPool = getLRUMap(entityCode);
		if (objectPool != null) {
			T storableObject = objectPool.get(id);
			if (storableObject == null && useLoader) {
				final Set<T> storableObjects = objectLoader.loadStorableObjects(Collections.singleton(id));
				if (!storableObjects.isEmpty()) {
					storableObject = storableObjects.iterator().next();
				}
				if (storableObject != null)
					try {
						putStorableObject(storableObject);
					} catch (final IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
			}
			return storableObject;
		}

		Log.errorMessage("StorableObjectPool.getStorableObject | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		return null;
	}

	public static <T extends StorableObject> Set<T> getStorableObjects(final Set<Identifier> ids, boolean useLoader) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		Log.debugMessage("StorableObjectPool.getStorableObjects | Requested for: " + ids, Log.DEBUGLEVEL10);
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		final LRUMap<Identifier, T> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			Log.errorMessage("StorableObjectPool.getStorableObjects | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return Collections.emptySet();
		}

		final Set<Identifier> loadIds = new HashSet<Identifier>(ids);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null) {
			Log.debugMessage("StorableObjectPool.getStorableObjects | Found among deleted (excluded): " + entityDeletedIds,
					Log.DEBUGLEVEL10);
			Identifier.subtractFromIdentifiers(loadIds, entityDeletedIds);
		}

		final Set<T> storableObjects = new HashSet<T>();

		for (final Iterator<Identifier> it = loadIds.iterator(); it.hasNext();) {
			final Identifier id = it.next();
			final T storableObject = objectPool.get(id);
			if (storableObject != null) {
				storableObjects.add(storableObject);
				it.remove();
			}
		}

		Log.debugMessage("StorableObjectPool.getStorableObjects | Found in pool " + storableObjects.size()
				+ " objects: " + Identifier.createStrings(storableObjects), Log.DEBUGLEVEL10);

		if (useLoader && !loadIds.isEmpty()) {
			final Set<T> loadedObjects = objectLoader.loadStorableObjects(loadIds);

			Log.debugMessage("StorableObjectPool.getStorableObjects | Loaded " + loadedObjects.size()
					+ " objects: " + Identifier.createStrings(loadedObjects), Log.DEBUGLEVEL10);

			for (final T storableObject : loadedObjects) {
				storableObjects.add(storableObject);
				objectPool.put(storableObject.getId(), storableObject);
			}
		}

		Log.debugMessage("StorableObjectPool.getStorableObjects | Returning " + storableObjects.size()
				+ " objects: " + Identifier.createStrings(storableObjects), Log.DEBUGLEVEL10);

		return storableObjects;
	}

	/**
	 * Break on load error
	 * @param condition
	 * @param useLoader
	 * @return Set of StorableObject matching condition 
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsByCondition(final StorableObjectCondition condition, final boolean useLoader)  throws ApplicationException {
		return getStorableObjectsByCondition(condition, useLoader, true);
	}

	/**
	 * 3-d parameter controls if break on load error
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @return Set of StorableObject matching condition
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsByCondition(final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		final Set<Identifier> emptySet = Collections.emptySet();
		return getStorableObjectsButIdsByCondition(emptySet, condition, useLoader, breakOnLoadError);
	}

	/**
	 * Break on load error
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @return Set of StorableObject matching condition with ids not in given set
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		return getStorableObjectsButIdsByCondition(ids, condition, useLoader, true);
	}

	/**
	 * 3-d parameter controls if break on load error
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @return Set of StorableObject matching condition with ids not in given set
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		assert condition != null : ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		assert (ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids)) : "Condition entity code: "
				+ condition.getEntityCode() + ", ids entity code: " + StorableObject.getEntityCodeOfIdentifiables(ids);

		Log.debugMessage("StorableObjectPool.getStorableObjectsButIdsByCondition | Requested but: " + ids
				+ ", for condition: " + condition, Log.DEBUGLEVEL10);

		final LRUMap<Identifier, T> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			Log.errorMessage("StorableObjectPool.getStorableObjectsButIdsByCondition | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return Collections.emptySet();
		}

		final Set<Identifier> loadButIds = new HashSet<Identifier>(ids);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null) {
			Log.debugMessage("StorableObjectPool.getStorableObjectsButIdsByCondition | Found among deleted (added to excluded): "
					+ entityDeletedIds, Log.DEBUGLEVEL10);
			Identifier.addToIdentifiers(loadButIds, entityDeletedIds);
		}

		final Set<T> storableObjects = new HashSet<T>();
		for (final T storableObject : objectPool) {
			final Identifier id = storableObject.getId();
			if (!loadButIds.contains(id) && condition.isConditionTrue(storableObject)) {
				storableObjects.add(storableObject);
			}
		}

		Log.debugMessage("StorableObjectPool.getStorableObjectsButIdsByCondition | Found in pool " + storableObjects.size()
				+ " objects: " + Identifier.createStrings(storableObjects), Log.DEBUGLEVEL10);

		if (useLoader && condition.isNeedMore(storableObjects)) {
			Identifier.addToIdentifiers(loadButIds, storableObjects);
			Set<T> loadedObjects = null;
			try {
				loadedObjects = objectLoader.loadStorableObjectsButIdsByCondition(loadButIds, condition);
			}
			catch (ApplicationException ae) {
				if (breakOnLoadError)
					throw ae;
				Log.errorException(ae);
				loadedObjects = Collections.emptySet();
			}
			if (loadedObjects != null) {
				Log.debugMessage("StorableObjectPool.getStorableObjectsButIdsByCondition | Loaded " + loadedObjects.size()
						+ " objects: " + Identifier.createStrings(loadedObjects), Log.DEBUGLEVEL10);

				for (final T storableObject : loadedObjects) {
					storableObjects.add(storableObject);
					objectPool.put(storableObject.getId(), storableObject);
				}
			}
		}

		/*
		 * This block is only needed in order for LRUMap to rehash
		 * itself. Since it affects performance, we've turned it off.
		 */
		if (false) {
			for (final StorableObject storableObject : storableObjects) {
				objectPool.get(storableObject.getId());
			}
		}

		Log.debugMessage("StorableObjectPool.getStorableObjectsButIdsByCondition | Returning " + storableObjects.size()
				+ " objects: " + Identifier.createStrings(storableObjects), Log.DEBUGLEVEL10);

		return storableObjects;
	}


	/*	Put */

	public static void putStorableObject(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;
		final Identifier id = storableObject.getId();
		final short entityCode = id.getMajor();
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null && entityDeletedIds.contains(id))
			return;

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool != null) {
			objectPool.put(id, storableObject);
		}
		else {
			throw new IllegalObjectEntityException("StorableObjectPool.putStorableObject | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED
					+ ": '" + ObjectEntities.codeToString(entityCode) + "'/"
					+ entityCode, IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}


	/*	Clean changed objects */

	public static void cleanChangedStorableObjects(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		DELETED_IDS_MAP.remove(new Short(entityCode));

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool != null) {
			for (final Iterator<StorableObject> it = objectPool.iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				if (storableObject.isChanged())
					it.remove();
			}
		}
		else {
			Log.errorMessage("StorableObjectPool.cleanChangedStorableObjects | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}


	/*	Delete */

	/**
	 * Mark object with given id as deleted
	 * @param id
	 */
	public static void delete(final Identifier id) {
		final short entityCode = id.getMajor();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds == null) {
			entityDeletedIds = new HashSet<Identifier>();
			DELETED_IDS_MAP.put(new Short(entityCode), entityDeletedIds);
		}
		entityDeletedIds.add(id);

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool != null) {
			objectPool.remove(id);
		}
		else {
			Log.errorMessage("StorableObjectPool.delete | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	/**
	 * Mark objects with ids from given set as deleted
	 * @param identifiables
	 */
	public static void delete(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null: ErrorMessages.NON_NULL_EXPECTED;

		/*
		 * Map<Short entityCode, Set<Identifiable> identifiables>
		 */
		final Map<Short, Set<Identifier>> deleteIdsMap = new HashMap<Short, Set<Identifier>>();

		for (final Identifiable identifiable : identifiables) {
			final Identifier id = identifiable.getId();
			final short entityCode = id.getMajor();
			assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

			final Short entityKey = new Short(entityCode);
			Set<Identifier> entityDeleteIds = deleteIdsMap.get(entityKey);
			if (entityDeleteIds == null) {
				entityDeleteIds = new HashSet<Identifier>();
				deleteIdsMap.put(entityKey, entityDeleteIds);
			}
			entityDeleteIds.add(id);
		}

		for (final Short entityKey : deleteIdsMap.keySet()) {
			final Set<Identifier> entityDeleteIds = deleteIdsMap.get(entityKey);

			Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(entityKey);
			if (entityDeletedIds == null) {
				entityDeletedIds = new HashSet<Identifier>();
				DELETED_IDS_MAP.put(entityKey, entityDeletedIds);
			}
			entityDeletedIds.addAll(entityDeleteIds);

			final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityKey.shortValue());
			if (objectPool != null) {
				for (final Identifier id : entityDeleteIds) {
					objectPool.remove(id);
				}
			}
			else {
				Log.errorMessage("StorableObjectPool.delete | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
						+ ObjectEntities.codeToString(entityKey) + "'/" + entityKey);
			}
		}

	}


	/*	Flush */

	public static void flush(final Identifiable identifiable, final Identifier modifierId, final boolean force) throws ApplicationException {
		final Identifier id = identifiable.getId();
		final short entityCode = id.getMajor();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		final Short entityKey = new Short(entityCode);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(entityKey);
		if (entityDeletedIds != null && entityDeletedIds.contains(id)) {
			objectLoader.delete(Collections.singleton(id));
			entityDeletedIds.remove(id);
			if (entityDeletedIds.isEmpty()) {
				DELETED_IDS_MAP.remove(entityKey);
			}
		}
		else {
			synchronized (DEPENDENCY_SORTED_CONTAINER) {
				SAVING_OBJECT_IDS.clear();
				DEPENDENCY_SORTED_CONTAINER.clear();
				final StorableObject storableObject = getStorableObject(id, false);
				if (storableObject != null) {
					checkChangedWithDependencies(storableObject, 0);
					saveWithDependencies(modifierId, force);
				}
			}
		}
	}

	public static void flush(final short entityCode, final Identifier modifierId, final boolean force) throws ApplicationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		flushDeleted(entityCode);

		synchronized (DEPENDENCY_SORTED_CONTAINER) {
			SAVING_OBJECT_IDS.clear();
			DEPENDENCY_SORTED_CONTAINER.clear();
			checkChangedWithDependencies(entityCode);
			saveWithDependencies(modifierId, force);
		}
	}

	private static void flushDeleted(final short entityCode) throws ApplicationException {
		final Short entityKey = new Short(entityCode);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(entityKey);
		if (entityDeletedIds != null) {
			objectLoader.delete(entityDeletedIds);
			DELETED_IDS_MAP.remove(entityKey);
		}
	}

	private static void checkChangedWithDependencies(final short entityCode) throws ApplicationException {
		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool != null) {
			synchronized (objectPool) {
				for (final StorableObject storableObject : objectPool) {
					checkChangedWithDependencies(storableObject, 0);
				}
			}
		}
		else {
			Log.errorMessage("StorableObjectPool.checkChangedWithDependencies | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	private static void checkChangedWithDependencies(final StorableObject storableObject, int dependencyLevel)
			throws ApplicationException {
		final Identifier id = storableObject.getId();
		if (SAVING_OBJECT_IDS.contains(id)) {
			DEPENDENCY_SORTED_CONTAINER.moveIfAlreadyPresent(storableObject, dependencyLevel);
			return;
		}

		SAVING_OBJECT_IDS.add(id);

		final Set<Identifiable> dependencies = storableObject.getDependencies();
		for (final Identifiable identifiable : dependencies) {
			assert identifiable != null : ErrorMessages.NON_NULL_EXPECTED;
			if (identifiable.getId().isVoid()) {
				continue;
			}

			StorableObject dependencyObject = null;
			if (identifiable instanceof Identifier) {
				dependencyObject = getStorableObject((Identifier) identifiable, false);
			} else if (identifiable instanceof StorableObject) {
				dependencyObject = (StorableObject) identifiable;
			} else {
				throw new IllegalDataException("dependency for object '" + id
						+ "' neither Identifier nor StorableObject -- " + identifiable.getClass().getName());
			}

			if (dependencyObject != null) {
				checkChangedWithDependencies(dependencyObject, dependencyLevel + 1);
			}
		}

		if (storableObject.isChanged()) {
			Log.debugMessage("StorableObjectPool.checkChangedWithDependencies | Object '" + storableObject.getId() + "' is changed",
					Log.DEBUGLEVEL10);
			DEPENDENCY_SORTED_CONTAINER.put(storableObject, dependencyLevel);
		}
	}

	private static void saveWithDependencies(final Identifier modifierId, final boolean force) throws ApplicationException {
		for (final Integer dependencyKey : DEPENDENCY_SORTED_CONTAINER.dependencyKeySet()) {
			final Map<Short, Set<StorableObject>> levelSavingObjectsMap = DEPENDENCY_SORTED_CONTAINER.getLevelEntityMap(dependencyKey);
			for (final Short entityKey : levelSavingObjectsMap.keySet()) {
				final Set<StorableObject> levelEntitySavingObjects = levelSavingObjectsMap.get(entityKey);
				saveStorableObjects(levelEntitySavingObjects, modifierId, force);
			}
		}
	}

	private static void saveStorableObjects(final Set<StorableObject> storableObjects,
			final Identifier modifierId,
			final boolean force) throws ApplicationException {
		final Set<Identifier> ids = Identifier.createIdentifiers(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		assert ObjectEntities.isEntityCodeValid(entityCode);

		lockSavingObjects(ids);

		final Set<StorableObject> setUpdatedObjects = new HashSet<StorableObject>(storableObjects.size());
		try {
			final Map<Identifier, StorableObjectVersion> versionsMap = objectLoader.getRemoteVersions(ids);
			for (final StorableObject storableObject : storableObjects) {
				final Identifier id = storableObject.getId();
				final StorableObjectVersion version = storableObject.getVersion();
				final StorableObjectVersion remoteVersion = versionsMap.get(id);
				if (remoteVersion.equals(StorableObjectVersion.ILLEGAL_VERSION) || version.equals(remoteVersion) || force) {
					storableObject.setUpdated(modifierId);
					setUpdatedObjects.add(storableObject);
				}
				else {
					throw new VersionCollisionException("Object '" + id + "'", version.longValue(), remoteVersion.longValue());
				}
			}

			Log.debugMessage("StorableObjectPool.saveStorableObjects | Saving objects: " + Identifier.createStrings(storableObjects),
					Log.DEBUGLEVEL08);
			objectLoader.saveStorableObjects(storableObjects);

			for (final StorableObject setUpdatedObject : setUpdatedObjects) {
				setUpdatedObject.cleanupUpdate();
			}
		}
		catch (final ApplicationException ae) {
			for (final StorableObject setUpdatedObject : setUpdatedObjects) {
				setUpdatedObject.rollbackUpdate();
			}
			throw ae;
		}
		finally {
			unlockSavingObjects(ids);
		}

	}

	private static void lockSavingObjects(final Set<Identifier> savingObjectsIds) throws UpdateObjectException {
		final long deadtime = System.currentTimeMillis() + MAX_LOCK_TIMEOUT;
		for(final Identifier id : savingObjectsIds) {
			while (LOCKED_IDS.contains(id) && System.currentTimeMillis() <= deadtime) {
				try {
					Thread.sleep(LOCK_TIME_WAIT);
				} catch (InterruptedException ie) {
					Log.errorException(ie);
				}
			}
			if (!LOCKED_IDS.contains(id)) {
				LOCKED_IDS.add(id);
			}
			else {
				LOCKED_IDS.removeAll(savingObjectsIds);
				throw new UpdateObjectException("Cannot obtain lock on object '" + id + "'");
			}
		}
	}

	private static void unlockSavingObjects(final Set<Identifier> savingObjectsIds) {
		LOCKED_IDS.removeAll(savingObjectsIds);
	}


	/*	From transferable*/

	/**
	 * 
	 * @param transferables
	 * @param continueOnError
	 * @return Set of Storable Objects
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> fromTransferables(final IdlStorableObject[] transferables,
			final boolean continueOnError) throws ApplicationException {
		final int length = transferables.length;
		final Set<T> storableObjects = new HashSet<T>(length);
		
		for (int i = 0; i < length; i++) {
			try {
				final T storableObject = StorableObjectPool.<T>fromTransferable(transferables[i]);
				storableObjects.add(storableObject);
			} catch (final ApplicationException ae) {
				if (continueOnError) {
					Log.debugException(ae, Level.SEVERE);
					continue;
				} // else
				throw ae;
			}
		}
		return storableObjects;
	}

	/**
	 * Gets a <code>StorableObject</code> from pool by its <code>id</code>,
	 * update its fields from <code>transferable</code> and return it. If the
	 * object is not found in pool, then a newly created from
	 * <code>transferable</code> instance is returned. <em>Never</em> returns
	 * <code>null</code>.
	 * 
	 * @param transferable
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends StorableObject> T fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		T storableObject = null;
		try {
			storableObject = StorableObjectPool.<T>getStorableObject(new Identifier(transferable.id), false);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false;
		}

		if (storableObject != null) {
			storableObject.fromTransferable(transferable);
		} else {
			try {
				storableObject = (T) transferable.getNative();
			} catch (final IdlCreateObjectException coe) {
				Log.debugException(coe, Level.SEVERE);
				throw new CreateObjectException(coe.detailMessage);
			}
		}

		return storableObject;
	}


	/*	Refresh */

	public static void refresh(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		Log.debugMessage("StorableObjectPool.refresh | Requested for: " + ids, Log.DEBUGLEVEL10);
		if (ids.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			Log.errorMessage("StorableObjectPool.refresh | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return;
		}

		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));

		final Set<StorableObject> storableObjects = new HashSet<StorableObject>();
		for (final StorableObject storableObject : objectPool) {
			final Identifier id = storableObject.getId();
			if (ids.contains(id) && !storableObject.isChanged() && (entityDeletedIds == null || !entityDeletedIds.contains(id))) {
				storableObjects.add(storableObject);
			}
		}

		if (storableObjects.isEmpty()) {
			Log.debugMessage("StorableObjectPool.refresh | LRUMap for '" + ObjectEntities.codeToString(entityCode)
					+ "' entity has no elements to refresh", Log.DEBUGLEVEL08);
			return;
		}

		Log.debugMessage("StorableObjectPool.refresh | Refreshing pool for '"
				+ ObjectEntities.codeToString(entityCode) + "'s: " + ids, Log.DEBUGLEVEL08);

		final Set<Identifier> returnedStorableObjectsIds = objectLoader.getOldVersionIds(StorableObject.createVersionsMap(storableObjects));
		if (returnedStorableObjectsIds.isEmpty()) {
			return;
		}

		final Set<StorableObject> loadedObjects = objectLoader.loadStorableObjects(returnedStorableObjectsIds);
		for (final StorableObject storableObject : loadedObjects) {
			objectPool.put(storableObject.getId(), storableObject);
			Log.debugMessage("StorableObjectPool.refresh | " + storableObject.getId() + ", " + storableObject.getVersion(),
				Log.DEBUGLEVEL08);
		}
	}

	/**
	 * 
	 * @throws ApplicationException
	 */
	public static void refresh() throws ApplicationException {
		final Set<StorableObject> storableObjects = new HashSet<StorableObject>();
		for (final TShortObjectIterator entityCodeIterator = objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);

			final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));

			storableObjects.clear();
			for (final StorableObject storableObject : objectPool) {
				if (!storableObject.isChanged() && (entityDeletedIds == null || !entityDeletedIds.contains(storableObject.getId()))) {
					storableObjects.add(storableObject);
				}
			}
			if (storableObjects.isEmpty()) {
				Log.debugMessage("StorableObjectPool.refresh | LRUMap for '" + ObjectEntities.codeToString(entityCode)
						+ "' entity has no elements to refresh", Log.DEBUGLEVEL08);
				continue;
			}

			Log.debugMessage("StorableObjectPool.refresh | Refreshing pool for entity: '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode, Log.DEBUGLEVEL08);

			final Set<Identifier> returnedStorableObjectsIds = objectLoader.getOldVersionIds(StorableObject.createVersionsMap(storableObjects));
			if (returnedStorableObjectsIds.isEmpty()) {
				continue;
			}

			final Set<StorableObject> loadedObjects = objectLoader.loadStorableObjects(returnedStorableObjectsIds);
			for (final StorableObject storableObject : loadedObjects) {
				objectPool.put(storableObject.getId(), storableObject);
				Log.debugMessage("StorableObjectPool.refresh | " + storableObject.getId() + ", " + storableObject.getVersion(),
					Log.DEBUGLEVEL08);
			}
		}
	}


	/*	Serialization */

	public static void deserialize() {
		for (final TShortObjectIterator entityCodeIterator = objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			final Set<Identifier> keys = LRUMapSaver.load(ObjectEntities.codeToString(entityCode));
			if (keys != null) {
				try {
					getStorableObjects(keys, true);
				} catch (ApplicationException ae) {
					Log.errorMessage("StorableObjectPool.deserialize | Cannot get entity '"
							+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
					Log.errorException(ae);
				}
			}
		}
	}

	public static void serialize() {
		for (final TShortObjectIterator entityCodeIterator = objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			LRUMapSaver.save((LRUMap) objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode), true);
		}
	}


	/*	Truncate */

	public static void truncate(final short entityCode) {
		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool instanceof StorableObjectResizableLRUMap) {
			((StorableObjectResizableLRUMap) objectPool).truncate(true);
		}
		else {
			Log.errorMessage("StorableObjectPool.truncateImpl | ERROR: Object pool class '" + objectPool.getClass().getName()
					+ "' not 'StorableObjectResizableLRUMap' -- cannot truncate pool");
		}
	}

	@SuppressWarnings("unchecked")
	private static <K, V> LRUMap<K, V> getLRUMap(final short entityCode) {
		return (LRUMap) objectPoolMap.get(entityCode);
	}
}
