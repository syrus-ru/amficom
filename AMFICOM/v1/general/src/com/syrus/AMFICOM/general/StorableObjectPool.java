/*-
 * $Id: StorableObjectPool.java,v 1.222 2006/06/08 10:40:00 arseniy Exp $
 *
 * Copyright ? 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.ENTITY_POOL_NOT_REGISTERED;
import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_GROUP_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static java.util.logging.Level.SEVERE;
import gnu.trove.TShortObjectHashMap;
import gnu.trove.TShortObjectIterator;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.bugs.Crutch235;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMap;
import com.syrus.util.LRUMapSaver;
import com.syrus.util.Log;
import com.syrus.util.transport.idl.IdlConversionException;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.222 $, $Date: 2006/06/08 10:40:00 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 * ???????????????? ??????? ?????????? ?????????: 8
 * @todo ???? ????? ?? ????????. ? ?????? ??????? ???? ????????? ?????? ? ?????????, ??????????? ?? ????????
 * (?. ?. ?????????, ?????????????? ??????? ???????? ? DELETED_IDS_MAP). ????????? ???:
 * 1) ????????? ????? ?????????????? ?????????;
 * 2) ???????? ????????? ?? ???? ???????? ?? ????????;
 * 3) ????????? ??? ?????? ????????? get<*>, ??? ???? ?? ??????? ??????? ??????????????? (??? ? "???", ??? ? "?????")
 * ?????? ?????????????? ?????????????? ?????????? ?? ???????? ????????;
 * 4) ????????? ????? refresh
 * 5) ????????? ??? ?????? flush, ????????? ??? ???????, ?????????? ?? ????????, ????????????? ?????????.
 */
public final class StorableObjectPool {
	private static final int MAX_OBJECT_POOL_SIZE = 20000;

	/**
	 * ?????-symbol {@value}
	 */
	public static final String ????? = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	/**
	 * <short entityCode, LRUMap objectPool>
	 * Implementation of object pool itself
	 */
	private static TShortObjectHashMap objectPoolMap;

	/**
	 * Object loader -- loads objects, not found in pool
	 */
	private static ObjectLoader objectLoader;

	private static final Map<Short, Set<Identifier>> DELETED_IDS_MAP = new HashMap<Short, Set<Identifier>>();

	private static final Set<Identifier> LOCKED_IDS = new HashSet<Identifier>();
	private static final long MAX_LOCK_TIMEOUT = 1 * 60 * 1000L; // 1 minuta
	private static final long LOCK_TIME_WAIT = 5 * 1000L; // 5 sec

	/**
	 * ????????? ??????????? ????????. ????????? ? ?????? ??????? ??????
	 * {@link #flush(Identifiable, Identifier, boolean)},
	 * {@link #flush(Set, Identifier, boolean)} ?
	 * {@link #flush(short, Identifier, boolean)}. ?????, ??????????? ?? ????
	 * ?????? ?????? {@link #checkChangedWithDependencies(StorableObject, int)}.
	 * ??. {@link DependencySortedContainer}.
	 */
	private static final DependencySortedContainer DEPENDENCY_SORTED_CONTAINER = new DependencySortedContainer();

	/**
	 * ????? ??????????? ????????. ????????? ? ?????? ??????? ??????
	 * {@link #flush(Identifiable, Identifier, boolean)},
	 * {@link #flush(Set, Identifier, boolean)} ?
	 * {@link #flush(short, Identifier, boolean)}. ?????, ??????????? ?? ????
	 * ?????? ?????? {@link #checkChangedWithDependencies(StorableObject, int)}.
	 * ??????? ?????????????? ??????? ? ???? ?????? ??????????????? ? ???, ???
	 * ??? ????? ??????? ?????
	 * {@link #checkChangedWithDependencies(StorableObject, int)} ??? ?????????.
	 */
	private static final Set<Identifier> SAVING_OBJECT_IDS = new HashSet<Identifier>();


	/**
	 * ????????? ??????????? ????????. ???????? ????????? ???????: ???????
	 * ??????? ??????????? ?? ????? ?????????, ? ????? - ?? ?????? ???????????.
	 * ???? ??? ?????? ??????????? ??ң??? ? ???????? ??????, ????? ??? ????????
	 * ?????????? ?????????? ?? ????? ???????? ??????? ? ????? ??????.
	 * 
	 * @author Tashoyan Arseniy Feliksovich
	 */
	private static final class DependencySortedContainer {
		private SortedMap<Integer, Map<Short, Set<StorableObject>>> objectsMap;

		DependencySortedContainer() {
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

		/**
		 * ?????????, ?????????? ?? ? ????????? ??????
		 * <code>storableObject</code> ?? ?????? ??????????? ??????, ???
		 * <code>dependencyLevel</code>. ???? ?? - ?? ??????????? ???? ??????
		 * ? ???? ??????, ?? ??????? ?? ???????????, ?? ???????
		 * <code>dependencyLevel</code>.
		 * 
		 * @param storableObject
		 * @param dependencyLevel
		 */
		void moveIfAlreadyPresent(final StorableObject storableObject, final int dependencyLevel) {
			final Integer dependencyKey0 = new Integer(-(dependencyLevel - 1));
			final SortedMap<Integer, Map<Short, Set<StorableObject>>> rangeObjectsMap = this.objectsMap.tailMap(dependencyKey0);
			Integer foundDependencyKey = null;
			for (final Integer dependencyKey : rangeObjectsMap.keySet()) {
				if (this.containsOnDependencyLevel(storableObject, dependencyKey)) {
					foundDependencyKey = dependencyKey;
					break;
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
			final boolean removed = levelEntitySavingObjects.remove(storableObject);
			if (levelEntitySavingObjects.isEmpty()) {
				levelSavingObjectsMap.remove(entityKey);
			}
			if (levelSavingObjectsMap.isEmpty()) {
				this.objectsMap.remove(dependencyKey);
			}
			return removed;
		}

		void clear() {
			this.objectsMap.clear();
		}

		@Override
		public String toString() {
			return this.objectsMap.toString();
		}
	}


	private StorableObjectPool() {
		// singleton
		assert false;
	}

	/**
	 * ?????????? ? ?????????????.
	 * ????????? ???????? <code>{@link StorableObjectPool#objectLoader}</code>
	 * ???????????? ? ???????? <code>objectLoader1</code>
	 * ???? <code>objectLoader1 == null</code>,
	 * ?? ???????????? ?????????? <code>NullPointerException</code>.
	 * ????? ???????? ???????? <code>{@link StorableObjectPool#objectPoolMap}</code>
	 * ???? ?????????, ????, ???? ???  ???? ??????? ??????, ????????????.
	 * ????? ?????? ????? ?????? ????? ? ?????? ?ݣ ?? ?????,
	 * ????????? ??? ?????????????????? ???????? ????????. 
	 * @param objectLoader1
	 */
	public static void init(final ObjectLoader objectLoader1) {
		if (objectLoader1 ==null) {
			throw new NullPointerException("Object loader is null");
		}

		objectLoader = objectLoader1;
		if (objectPoolMap == null) {
			objectPoolMap = new TShortObjectHashMap();
		} else {
			objectPoolMap.clear();
		}
	}

	/**
	 * ???????????????? ????????? ???????? ???? ????????? ?????? <code>groupCode</code>.
	 * @param groupCode
	 * @param capacity
	 * @param objectTimeToLive
	 */
	public static void addObjectPoolGroup(final short groupCode, final int capacity, final long objectTimeToLive) {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ILLEGAL_GROUP_CODE;
		final int objectPoolSize = (capacity < 0) ? 0 : (capacity > MAX_OBJECT_POOL_SIZE) ? MAX_OBJECT_POOL_SIZE : capacity;
		for (final short entityCode : ObjectGroupEntities.getEntityCodes(groupCode).toArray()) {
			addObjectPool(entityCode, objectPoolSize, objectTimeToLive);
		}
	}

	/**
	 * ???????????????? ????????? ???????? ??? ???????? <code>entityCode</code>.
	 * @param entityCode
	 * @param objectPoolCapacity
	 * @param objectTimeToLive
	 */
	public static void addObjectPool(final short entityCode, final int objectPoolCapacity, final long objectTimeToLive) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;
		final LRUMap<Identifier, StorableObject> objectPool = new LRUMap<Identifier, StorableObject>(objectPoolCapacity, objectTimeToLive);
		objectPoolMap.put(entityCode, objectPool);
		Log.debugMessage("Pool for '" + ObjectEntities.codeToString(entityCode)
				+ "'/" + entityCode + " of capacity " + objectPoolCapacity + " added", Log.DEBUGLEVEL08);
	}



	/*	Get */

	/**
	 * Get {@link StorableObject} of the given {@link Identifier}.
	 * @param <T>
	 * @param id
	 * @param useLoader
	 * @return The {@link StorableObject} of the given id or null, if not found
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> T getStorableObject(final Identifier id, final boolean useLoader)
			throws ApplicationException {
		assert id != null : NON_NULL_EXPECTED;
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
						Log.errorMessage(ioee);
					}
			}
			return storableObject;
		}

		Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		return null;
	}

	/**
	 * Get {@link Set} of {@link StorableObject} for {@link Set} of {@link Identifier}
	 * @param <T>
	 * @param ids
	 * @param useLoader
	 * @return {@link Set} of {@link StorableObject} with identifiers in the given set. If none objects found, returns empty set.
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjects(final Set<Identifier> ids, boolean useLoader) throws ApplicationException {
		assert ids != null : NON_NULL_EXPECTED;
		Log.debugMessage("Requested for: " + ids, Log.DEBUGLEVEL08);
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;

		final LRUMap<Identifier, T> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return Collections.emptySet();
		}

		final Set<Identifier> loadIds = new HashSet<Identifier>(ids);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null) {
			Log.debugMessage("Found among deleted (excluded): " + entityDeletedIds, Log.DEBUGLEVEL08);
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

		Log.debugMessage("Found in pool " + storableObjects.size() + " objects: " + Identifier.toString(storableObjects),
				Log.DEBUGLEVEL08);

		if (useLoader && !loadIds.isEmpty()) {
			final Set<T> loadedObjects = objectLoader.loadStorableObjects(loadIds);

			Log.debugMessage("Loaded " + loadedObjects.size() + " objects: " + Identifier.toString(loadedObjects), Log.DEBUGLEVEL08);

			for (final T storableObject : loadedObjects) {
				storableObjects.add(storableObject);
				objectPool.put(storableObject.getId(), storableObject);
			}
		}

		Log.debugMessage("Returning " + storableObjects.size() + " objects: " + Identifier.toString(storableObjects),
				Log.DEBUGLEVEL08);

		return storableObjects;
	}

	/**
	 * Get {@link Set} of {@link StorableObject} matching the condition.
	 * This method breaks in case of load error.
	 * @param condition
	 * @param useLoader
	 * @return {@link Set} of {@link StorableObject} matching the given condition 
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsByCondition(final StorableObjectCondition condition, final boolean useLoader)  throws ApplicationException {
		return getStorableObjectsByCondition(condition, useLoader, true);
	}

	/**
	 * Get {@link Set} of {@link StorableObject} matching the condition.
	 * 3-d parameter controls if break on load error
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @return {@link Set} of {@link StorableObject} matching the given condition
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsByCondition(final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		final Set<Identifier> emptySet = Collections.emptySet();
		return getStorableObjectsButIdsByCondition(emptySet, condition, useLoader, breakOnLoadError);
	}

	/**
	 * Get {@link Set} of {@link StorableObject} matching the condition with identifiers not in the given set.
	 * This method breaks in case of load error.
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @return {@link Set} of {@link StorableObject} matching the given condition with identifiers are not in the given set.
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		return getStorableObjectsButIdsByCondition(ids, condition, useLoader, true);
	}

	/**
	 * Get {@link Set} of {@link StorableObject} matching the condition with identifiers not in the given set.
	 * 3-d parameter controls if break on load error
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @return {@link Set} of {@link StorableObject} matching the given condition with identifiers are not in the given set.
	 * @throws ApplicationException
	 */
	public static <T extends StorableObject> Set<T> getStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		assert ids != null : NON_NULL_EXPECTED;
		assert condition != null : NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		assert (ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids)) : "Condition entity code: "
				+ condition.getEntityCode() + ", ids entity code: " + StorableObject.getEntityCodeOfIdentifiables(ids);

		Log.debugMessage("Requested objects but: " + ids + ", for condition: " + condition, Log.DEBUGLEVEL08);

		long nano0 = System.nanoTime(); // performance stats

		final LRUMap<Identifier, T> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return Collections.emptySet();
		}

		final Set<Identifier> loadButIds = new HashSet<Identifier>(ids);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null) {
			Log.debugMessage("Found among deleted (added to excluded): " + entityDeletedIds, Log.DEBUGLEVEL08);
			Identifier.addToIdentifiers(loadButIds, entityDeletedIds);
		}

		final Set<T> storableObjects = new HashSet<T>();
		synchronized (objectPool) {
			for (final T storableObject : objectPool.values()) {
				final Identifier id = storableObject.getId();
				if (!loadButIds.contains(id) && condition.isConditionTrue(storableObject)) {
					storableObjects.add(storableObject);
				}
			}
		}

		final int foundLocally = storableObjects.size(); // performance stats

		/*
		 * According to contract of LRUMap move found objects to the beginging.
		 */
		for (final StorableObject storableObject : storableObjects) {
			objectPool.get(storableObject.getId());
		}

		Log.debugMessage("Found in pool " + storableObjects.size() + " objects: " + Identifier.toString(storableObjects),
				Log.DEBUGLEVEL08);

		final boolean loaderUsed; // performance stats
		final int loaded; // performance stats

		if (useLoader && condition.isNeedMore(Identifier.createSumIdentifiables(storableObjects, ids))) {
			loaderUsed = true;
			Identifier.addToIdentifiers(loadButIds, storableObjects);
			Set<T> loadedObjects;
			try {
				loadedObjects = objectLoader.loadStorableObjectsButIdsByCondition(loadButIds, condition);
			} catch (final ApplicationException ae) {
				if (breakOnLoadError) {
					throw ae;
				}
				Log.errorMessage(ae);
				loadedObjects = Collections.emptySet();
			}
			assert loadedObjects != null : NON_NULL_EXPECTED; 
			Log.debugMessage("Loaded " + loadedObjects.size() + " objects: " + Identifier.toString(loadedObjects), Log.DEBUGLEVEL08);

			loaded = loadedObjects.size();

			for (final T loadedStorableObject : loadedObjects) {
				final Identifier loadedId = loadedStorableObject.getId();
				if (!objectPool.containsKey(loadedId)) {
					objectPool.put(loadedId, loadedStorableObject);
					storableObjects.add(loadedStorableObject);
				} else {
					Log.debugMessage("Local version of object '"
							+ loadedId + "' do not match condition, but remote version matches condition; ignore this object",
							Log.DEBUGLEVEL08);
				}
			}
		} else {
			loaderUsed = false;
			loaded = 0;
		}

		long nano1 = System.nanoTime(); // performance stats

		Log.debugMessage("Returning " + storableObjects.size() + " objects: " + Identifier.toString(storableObjects),
				Log.DEBUGLEVEL08);

		Log.debugMessage("SOP stat: "
				+ (nano1 - nano0) / 1000 / 1e3 + " ms to load "
				+ foundLocally + (loaderUsed ? "+" + loaded : "=")
				+ " but " + ids.size() + " for: " + condition, Level.FINEST);

		return storableObjects;
	}

	/**
	 * Get {@link Set} of {@link Identifier} of {@link StorableObject}, matching the given condition.
	 * This method breaks in case of load error.
	 * @param condition
	 * @param useLoader
	 * @return {@link Set} of {@link Identifier} of {@link StorableObject}, matching the given condition.
	 * @throws ApplicationException
	 */
	public static Set<Identifier> getIdentifiersByCondition(final StorableObjectCondition condition, final boolean useLoader)  throws ApplicationException {
		return getIdentifiersByCondition(condition, useLoader, true);
	}

	/**
	 * Get {@link Set} of {@link Identifier} of {@link StorableObject}, matching the given condition.
	 * 3-d parameter controls if break on load error
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @return {@link Set} of {@link Identifier} of {@link StorableObject}, matching the given condition.
	 * @throws ApplicationException
	 */
	public static Set<Identifier> getIdentifiersByCondition(final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		final Set<Identifier> emptySet = Collections.emptySet();
		return getIdentifiersButIdsByCondition(emptySet, condition, useLoader, breakOnLoadError);
	}

	/**
	 * Get {@link Set} of {@link Identifier} of {@link StorableObject} matching the condition, not belonging to the given set.
	 * This method breaks in case of load error.
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @return {@link Set} of {@link Identifier} of {@link StorableObject} matching the condition, not belonging to the given set.
	 * @throws ApplicationException
	 */
	public static Set<Identifier> getIdentifiersButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		return getIdentifiersButIdsByCondition(ids, condition, useLoader, true);
	}

	/**
	 * Get {@link Set} of {@link Identifier} of {@link StorableObject} matching the condition, not belonging to the given set.
	 * 3-d parameter controls if break on load error
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @return {@link Set} of {@link Identifier} of {@link StorableObject} matching the condition, not belonging to the given set.
	 * @throws ApplicationException
	 */
	public static Set<Identifier> getIdentifiersButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		assert ids != null : NON_NULL_EXPECTED;
		assert condition != null : NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		assert (ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids)) : "Condition entity code: "
				+ condition.getEntityCode() + ", ids entity code: " + StorableObject.getEntityCodeOfIdentifiables(ids);

		Log.debugMessage("Requested identifiers but: " + ids + ", for condition: " + condition, Log.DEBUGLEVEL08);

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return Collections.emptySet();
		}

		final Set<Identifier> loadButIds = new HashSet<Identifier>(ids);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null) {
			Log.debugMessage("Found among deleted (added to excluded): " + entityDeletedIds, Log.DEBUGLEVEL08);
			Identifier.addToIdentifiers(loadButIds, entityDeletedIds);
		}

		/*
		 * We use Set of StorableObjects in addition to Set of Identifiers
		 * to improve perfomance in subsequent call of StorableObjectCondition.isNeedMore(Set).
		 * Method isNeedMore accepts Set of Identifiables and restores every object,
		 * using cast to StorableObject, if it is possible or calls the method StorableObjectPool.getStorableObject().
		 * The second way is more expensive, so it is desirable to pass into isNeedMore Set of StorableObjects.*/
		final Set<Identifier> identifiers = new HashSet<Identifier>();
		final Set<StorableObject> storableObjects = new HashSet<StorableObject>();
		synchronized (objectPool) {
			for (final StorableObject storableObject : objectPool.values()) {
				final Identifier id = storableObject.getId();
				if (!loadButIds.contains(id) && condition.isConditionTrue(storableObject)) {
					identifiers.add(id);
					storableObjects.add(storableObject);
				}
			}
		}

		Log.debugMessage("Found in pool " + identifiers.size() + " identifiers: " + Identifier.toString(identifiers),
				Log.DEBUGLEVEL08);

		if (useLoader && condition.isNeedMore(Identifier.createSumIdentifiables(storableObjects, ids))) {
			Identifier.addToIdentifiers(loadButIds, identifiers);
			Set<Identifier> loadedIdentifiers = null;
			try {
				loadedIdentifiers = objectLoader.loadIdentifiersButIdsByCondition(loadButIds, condition);
			} catch (ApplicationException ae) {
				if (breakOnLoadError) {
					throw ae;
				}
				Log.errorMessage(ae);
				loadedIdentifiers = Collections.emptySet();
			}
			assert loadedIdentifiers != null : NON_NULL_EXPECTED; 
			Log.debugMessage("Loaded " + loadedIdentifiers.size() + " identifiers: " + Identifier.toString(loadedIdentifiers),
					Log.DEBUGLEVEL08);

			for (final Identifier loadedId : loadedIdentifiers) {
				if (!objectPool.containsKey(loadedId)) {
					identifiers.add(loadedId);
				} else {
					Log.debugMessage("Local version of object '"
							+ loadedId + "' do not match condition, but remote version matches condition; ignore this object",
							Log.DEBUGLEVEL08);
				}
			}

		}

		Log.debugMessage("Returning " + identifiers.size() + " identifiers: " + Identifier.toString(identifiers), Log.DEBUGLEVEL08);

		return identifiers;
	}


	/*	Put */

	/**
	 * Put single {@link StorableObject} to pool
	 * Normally, you never use this method.
	 * @param storableObject
	 * @throws IllegalObjectEntityException
	 */
	static void putStorableObject(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;
		final Identifier id = storableObject.getId();
		final short entityCode = id.getMajor();
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null && entityDeletedIds.contains(id)) {
			return;
		}

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			throw new IllegalObjectEntityException(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode, IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

		if (!objectPool.containsKey(id)) {
			objectPool.put(id, storableObject);
		} else {
			final StorableObject poolStorableObject = objectPool.get(id);
			if (!poolStorableObject.isChanged()) {
				setStorableObjectAttributes(poolStorableObject, storableObject);
			} else {
				Log.debugMessage("Object '" + id + "' is already present in pool and it is changed -- not replacing it",
						Log.DEBUGLEVEL08);
			}
		}
	}

	/**
	 * Put {@link Set} of {@link StorableObject} to pool
	 * Normally, you never use this method.
	 * @param storableObjects
	 * @throws IllegalObjectEntityException
	 */
	public static void putStorableObjects(final Set<? extends StorableObject> storableObjects)
			throws IllegalObjectEntityException {
		for (final StorableObject storableObject : storableObjects) {
			putStorableObject(storableObject);
		}
	}


	/*	Clean objects */

	/**
	 * Clean from pool changed objects from the supplied set.
	 * Also cleans delete marks on the supplied objects (if any). 
	 * Objects must NOT belong to the same entity.
	 * @todo ????????? ??????, ????????? ? ????????? ?? {@link #DELETED_IDS_MAP}.
	 * @param identifiables
	 */
	public static void cleanChangedStorableObjects(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null : NON_NULL_EXPECTED;
		if (identifiables.isEmpty()) {
			return;
		}

		final Map<Short, Set<Identifier>> entityIdsMap = Identifier.createEntityIdsMap(identifiables);
		if (entityIdsMap.isEmpty()) {
			return;
		}

		for (final Short entityKey : entityIdsMap.keySet()) {
			final Set<Identifier> entityIds = entityIdsMap.get(entityKey);
			if (entityIds.isEmpty()) {
				continue;
			}

			final Set<Identifier> deletedIds = DELETED_IDS_MAP.get(entityKey);
			if (deletedIds != null) {
				deletedIds.removeAll(entityIds);
			}

			final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityKey);
			if (objectPool != null) {
				for (final Iterator<StorableObject> it = objectPool.values().iterator(); it.hasNext();) {
					final StorableObject storableObject = it.next();
					if (entityIds.contains(storableObject.getId()) && storableObject.isChanged()) {
						it.remove();
					}
				}
			} else {
				Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
						+ ObjectEntities.codeToString(entityKey) + "'/" + entityKey);
				continue;
			}
		}
	}

	/**
	 * Clean all changed objects of the given entity from pool
	 * @param entityCode
	 */
	public static void cleanChangedStorableObjects(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;

		DELETED_IDS_MAP.remove(new Short(entityCode));

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool != null) {
			for (final Iterator<StorableObject> it = objectPool.values().iterator(); it.hasNext();) {
				final StorableObject storableObject = it.next();
				if (storableObject.isChanged()) {
					it.remove();
				}
			}
		} else {
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	/**
	 * Clean all objects from pool, including those, marked as deleted.
	 */
	public static void clean() {
		for(final short entityCode : objectPoolMap.keys()) {
			assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;

			DELETED_IDS_MAP.remove(new Short(entityCode));

			final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
			assert objectPool != null : NON_NULL_EXPECTED + ", entity: " + ObjectEntities.codeToString(entityCode);
			objectPool.clear();
		}
	}


	/*	Delete */

	private static void markAsDeleted(final Set<? extends Identifiable> identifiables) {
		try {
			for (final Identifiable identifiable : identifiables) {
				final StorableObject storableObject = fromIdentifiable(identifiable);
				if (storableObject != null) {
					storableObject.markAsDeleted();
				} else {
					Log.debugMessage("Object '" + identifiable.getId() + "' not found in pool, probably already deleted",
							Log.DEBUGLEVEL08);
				}
			}
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false;
		}
	}

	/**
	 * Mark object with given id as deleted
	 * @param id
	 */
	public static void delete(final Identifier id) {
		assert id != null: NON_NULL_EXPECTED;

		markAsDeleted(Collections.singleton(id));

		final short entityCode = id.getMajor();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;
		Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds == null) {
			entityDeletedIds = new HashSet<Identifier>();
			DELETED_IDS_MAP.put(new Short(entityCode), entityDeletedIds);
		}
		entityDeletedIds.add(id);

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool != null) {
			objectPool.remove(id);
		} else {
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '" + ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	/**
	 * Mark objects with ids from given set as deleted
	 * 
	 * @param identifiables
	 */
	public static void delete(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null: NON_NULL_EXPECTED;

		markAsDeleted(identifiables);

		/*
		 * Map<Short entityCode, Set<Identifiable> identifiables>
		 */
		final Map<Short, Set<Identifier>> deleteIdsMap = Identifier.createEntityIdsMap(identifiables);

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
			} else {
				Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
						+ ObjectEntities.codeToString(entityKey) + "'/" + entityKey);
			}
		}

	}


	/*	Flush */

	/**
	 * Flush single object.
	 * @param identifiable
	 * @param modifierId
	 * @param force
	 * @throws ApplicationException
	 */
	public static void flush(final Identifiable identifiable, final Identifier modifierId, final boolean force) throws ApplicationException {
		final Identifier id = identifiable.getId();
		final short entityCode = id.getMajor();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;

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

	/**
	 * Flush set of objects of different entities.
	 * @param identifiables
	 * @param modifierId
	 * @param force
	 * @throws ApplicationException
	 */
	public static void flush(final Set<? extends Identifiable> identifiables, final Identifier modifierId, final boolean force)
			throws ApplicationException {
		assert identifiables != null : NON_NULL_EXPECTED;
		if (identifiables.isEmpty()) {
			return;
		}

		final Set<Identifiable> objectsToSave = flushDeleted(identifiables);
		if (objectsToSave.isEmpty()) {
			return;
		}

		synchronized (DEPENDENCY_SORTED_CONTAINER) {
			SAVING_OBJECT_IDS.clear();
			DEPENDENCY_SORTED_CONTAINER.clear();
			for (final Identifiable identifiable : objectsToSave) {
				final StorableObject storableObject = fromIdentifiable(identifiable);
				if (storableObject != null) {
					checkChangedWithDependencies(storableObject, 0);
				}
			}
			saveWithDependencies(modifierId, force);
		}

	}

	private static Set<Identifiable> flushDeleted(final Set<? extends Identifiable> identifiables) throws ApplicationException {
		final Set<Identifiable> objectsNotToDelete = new HashSet<Identifiable>();

		final Map<Short, Set<Identifier>> objectsToDeleteIdsMap = new HashMap<Short, Set<Identifier>>();
		synchronized (identifiables) {
			for (final Identifiable identifiable : identifiables) {
				final Identifier id = identifiable.getId();
				final Short entityKey = new Short(id.getMajor());
				final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(entityKey);
				if (entityDeletedIds != null && entityDeletedIds.contains(id)) {
					Set<Identifier> entityObjectsToDeleteIds = objectsToDeleteIdsMap.get(entityKey);
					if (entityObjectsToDeleteIds == null) {
						entityObjectsToDeleteIds = new HashSet<Identifier>();
						objectsToDeleteIdsMap.put(entityKey, entityObjectsToDeleteIds);
					}
					entityObjectsToDeleteIds.add(id);
				} else {
					objectsNotToDelete.add(identifiable);
				}
			}
		}

		for (final Short entityKey : objectsToDeleteIdsMap.keySet()) {
			final Set<Identifier> entityObjectsToDeleteIds = objectsToDeleteIdsMap.get(entityKey);
			final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(entityKey);
			objectLoader.delete(entityObjectsToDeleteIds);
			entityDeletedIds.removeAll(entityObjectsToDeleteIds);
		}

		return objectsNotToDelete;
	}

	/**
	 * Flush all present in pool objects of the given entity.
	 * @param entityCode
	 * @param modifierId
	 * @param force
	 * @throws ApplicationException
	 */
	public static void flush(final short entityCode, final Identifier modifierId, final boolean force) throws ApplicationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;

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
				for (final StorableObject storableObject : objectPool.values()) {
					checkChangedWithDependencies(storableObject, 0);
				}
			}
		} else {
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	private static void checkChangedWithDependencies(final StorableObject storableObject, final int dependencyLevel)
			throws ApplicationException {
		final Identifier id = storableObject.getId();
		if (SAVING_OBJECT_IDS.contains(id)) {
			if (storableObject.isChanged()) {
				DEPENDENCY_SORTED_CONTAINER.moveIfAlreadyPresent(storableObject, dependencyLevel);
				final Set<Identifiable> dependencies = storableObject.getDependencies();
				for (final Identifiable identifiable : dependencies) {
					final StorableObject dependencyObject = fromIdentifiable(identifiable);
					if (dependencyObject != null) {
						checkChangedWithDependencies(dependencyObject, dependencyLevel + 1);
					}
				}
			}
			return;
		}

		SAVING_OBJECT_IDS.add(id);

		final Set<Identifiable> dependencies = storableObject.getDependencies();
		for (final Identifiable identifiable : dependencies) {
			final StorableObject dependencyObject = fromIdentifiable(identifiable);
			if (dependencyObject != null) {
				checkChangedWithDependencies(dependencyObject, dependencyLevel + 1);
			}
		}

		if (storableObject.isChanged()) {
			Log.debugMessage("Object '" + storableObject.getId() + "' is changed", Log.DEBUGLEVEL08);
			DEPENDENCY_SORTED_CONTAINER.put(storableObject, dependencyLevel);
		}
	}

	/**
	 * ????? ?????? {@code StorableObject}, ??????????????? ?????????
	 * {@code identifiable}. ????? ???????????? ?????? ????????, ??? ?????????
	 * ??????????? {@link #objectLoader}. ???? ????? ?? ?????? ????????????
	 * ???????? (? ??????? ??
	 * {@link #getStorableObject(Identifier, boolean), ????????}).
	 * <p>
	 * ???? ???????? - ?????? ?????? {@link StorableObject}, ?? ?? ?? ?
	 * ????????????.
	 * <p>
	 * ???? ???????? - ?????? ?????? {@link Identifier}, ?? ??????
	 * ??????????????? ????? {@link LRUMap} ? ???????????? ??????, ?????????? ?
	 * ???? ????? ?? ????? {@code identifiable}. ????????? ???????? ? ????? ???
	 * ???? ?? ????????. ???? ?? ?????? ? ????? ?? ??????, ???? ?? ??????? ????
	 * ?????, ?? ???????????? {@code null}.
	 * <p>
	 * ???? ???????? ?? ???????? ???????? ?? ?????? ?? ?????????????????
	 * ???????, ???????????? ?????????? {@link IllegalDataException}.
	 * 
	 * @param identifiable
	 * @return ??????????????? ????????? {@code identifiable} ??????.
	 * @throws IllegalDataException
	 *         ???? ???????? {@code identifiable} ?? ???????? ???????? ???????
	 *         {@link StorableObject} ??? {@link Identifier}.
	 * @see LRUMap#unmodifiableGet(Object).
	 */
	private static final StorableObject fromIdentifiable(final Identifiable identifiable) throws IllegalDataException {
		assert identifiable != null : NON_NULL_EXPECTED;
		if (identifiable.getId().isVoid()) {
			return null;
		}

		if (identifiable instanceof Identifier) {
			final Identifier id = (Identifier) identifiable;
			final short entityCode = id.getMajor();
			final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
			if (objectPool != null) {
				return objectPool.unmodifiableGet(id);
			}
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return null;
		} else if (identifiable instanceof StorableObject) {
			return (StorableObject) identifiable;
		} else {
			throw new IllegalDataException("Object '" + identifiable.getId()
					+ "' neither Identifier nor StorableObject -- " + identifiable.getClass().getName());
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

	/**
	 * ????????? ???????.
	 * <p>
	 * ???? ????? ?????????? "????????" ????? ???????? ? ??????????? ????????? ?
	 * ???????????? ????????. ????? ??????? ?????????? ???????? ?????????, ???
	 * ??????? ?????? ????? ? ?????? ?????? ?? ????????? ?????-?????? ??
	 * ???????? {@code storableObjects}. ???? ??? ?? ??? - ??????
	 * {@link UpdateObjectException}. ????? - ????????? ??? ??? ???????.
	 * <p>
	 * ????? ??? ??????? ?? ??????????? ???????? ?????????? ???????? ??????.
	 * ?????????? ???????? ?????? ? ??????, ????? ?????? ?????????? ???????
	 * ????? ?????? ???̣????? ???????; ?????? ? ???? ?????? ????? ????
	 * ?????????, ??? ????? ?????? ?? ??????? ???̣???? ?????? ??? ??????
	 * ??????. ?????????????, ???????????
	 * {@code <?????????_??????>.isOlder(<???̣????_??????>) == true} ????????,
	 * ??? ??? ????????? ?????? ???????? ?????????? ?? ????????? ? ??????
	 * ???̣????? ???????. ???? ???????? ?????? ?? ??????? ???? ?? ??? ??????
	 * ??????? ?? {@code storableObjects}, ??, ? ??????????? ?? ?????????
	 * {@code force}, ???? ?????????? ?????????? ? ????????????????, ????
	 * ???????? {@link VersionCollisionException}.
	 * <p>
	 * ????? ???????? ??????? ?????? ??? ??????? ?????????? ????? ?????????
	 * {@link #objectLoader}. ????? ??????????? ?????? ??? ??????? ???????
	 * ??????????????.
	 * 
	 * @param storableObjects
	 *        ???????, ??????? ???? ?????????. ?????? ???? ????? ????????. ???
	 *        ??? ????Σ????, ?. ?. {@link StorableObject#isChanged()} ???
	 *        ??????? ?? ??? ???Σ? {@code true}.
	 * @param modifierId
	 *        ????????????? ????????????, ????????????? ??? ????????.
	 * @param force
	 *        ?????????? ????????? ??? ?????????????? ????????? ? ???̣????
	 *        ?????? ???????. ???? {@code true}, ?????? ?ӣ ????? ???????????.
	 *        ???? {@code false}, ?? ?????????
	 *        {@link VersionCollisionException}.
	 * @throws ApplicationException
	 *         {@link UpdateObjectException}, ???? ?? ??????? ?????????????
	 *         ???? ?? ???? ?????? ?? {@code storableObjects};
	 *         {@link VersionCollisionException}, ???? ????????
	 *         {@code force == true} ? ?? ??????? ???????? ?????? ???? ?? ???
	 *         ?????? ??????? ?? {@code storableObjects};
	 *         {@link ApplicationException}, ??? ?????? ?????? ???????????
	 *         {@link #objectLoader}.
	 */
	private static void saveStorableObjects(final Set<StorableObject> storableObjects,
			final Identifier modifierId,
			final boolean force) throws ApplicationException {
		final Set<Identifier> ids = Identifier.createIdentifiers(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;

		lockSavingObjects(ids);

		final Set<StorableObject> setUpdatedObjects = new HashSet<StorableObject>(storableObjects.size());
		try {
			final Map<Identifier, StorableObjectVersion> versionsMap = objectLoader.getRemoteVersions(ids);
			for (final StorableObject storableObject : storableObjects) {
				final Identifier id = storableObject.getId();
				final StorableObjectVersion version = storableObject.getVersion();
				final StorableObjectVersion remoteVersion = versionsMap.get(id);

				/*
				 * ????? ?????? ?? ?????? ????. ?????? ?????????? ??????? ?????
				 * ???? ?????? ?????? ???̣????? ??????? ?????? ? ??????????
				 * ??????? ????? 1) ?????????? ?????? ????? ????? ??????
				 * StorableObject#setUpdated(Identifier) ? 2) ??????? ?????????
				 * ???????? ????? ??????????
				 * ObjectLoader#saveStorableObjects(Set<StorableObject>).
				 */
				assert storableObject.isNew() || !version.isNewer(remoteVersion) : OBJECT_STATE_ILLEGAL
						+ "; id: '" + id + "', version: " + version + ", remoteVersion: " + remoteVersion;

				/*
				 * ???? ???? force ?? ?????????, ?? ????????? ?????? ????? ?
				 * ???? ???????:
				 * 1. ???? ?????? -- ?????, ? ???????????????? ??? ??????? ?? ???????
				 * ?? ??????????:
				 * remoteVersion.equals(ILLEGAL_VERSION) && storableObject.isNew()
				 * 2. ?????? ???????????? ?????????? ??????? ????? ??????
				 * ???????????????? ??????? ?? ???????:
				 * !version.isOlder(remoteVersion)
				 * ? ?????????ģ???? ???????? ????????? ???????? ?????????
				 * ??????????? ??????? 1 ? 2: !(1 || 2). ? ???? ?????? ?????????
				 * ?????? ????? ?????? ? ?????? force.
				 */
				if ((!remoteVersion.equals(ILLEGAL_VERSION) || !storableObject.isNew()) && version.isOlder(remoteVersion)) {
					if (force) {
						storableObject.version = remoteVersion;
					} else {
						throw new VersionCollisionException("Object '" + id + "'", version.longValue(), remoteVersion.longValue());
					}
				}
				storableObject.setUpdated(modifierId);
				setUpdatedObjects.add(storableObject);
			}

			Log.debugMessage("Saving objects: " + Identifier.toString(storableObjects), Log.DEBUGLEVEL08);
			objectLoader.saveStorableObjects(storableObjects);

			for (final StorableObject setUpdatedObject : setUpdatedObjects) {
				setUpdatedObject.cleanupUpdate();
			}
		} catch (final ApplicationException ae) {
			for (final StorableObject setUpdatedObject : setUpdatedObjects) {
				setUpdatedObject.rollbackUpdate();
			}
			throw ae;
		} finally {
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
					Log.errorMessage(ie);
				}
			}
			if (!LOCKED_IDS.contains(id)) {
				LOCKED_IDS.add(id);
			} else {
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
	 * Creates a {@link Set} of {@link StorableObject}s from the array of
	 * {@link IdlStorableObject}s. Updates every object in pool.
	 * 
	 * @param transferables
	 * @param continueOnError
	 * @return {@link Set} of {@link StorableObject}
	 * @throws IdlConversionException
	 */
	public static <T extends StorableObject> Set<T> fromTransferables(final IdlStorableObject[] transferables,
			final boolean continueOnError) throws IdlConversionException {
		final int length = transferables.length;
		final Set<T> storableObjects = new HashSet<T>(length);

		for (int i = 0; i < length; i++) {
			try {
				final T storableObject = StorableObjectPool.<T> fromTransferable(transferables[i]);
				storableObjects.add(storableObject);
			} catch (final IdlConversionException ice) {
				if (continueOnError) {
					Log.debugMessage(ice, Level.SEVERE);
					continue;
				} // else
				throw ice;
			}
		}
		return storableObjects;
	}

	/**
	 * Gets a <code>StorableObject</code> from pool by its <code>id</code>,
	 * updates its fields from <code>transferable</code> and returns it. If the
	 * object is not found in pool, then a newly created from
	 * <code>transferable</code> instance is returned. <em>Never</em>
	 * returns <code>null</code>.
	 * 
	 * @param transferable
	 * @throws IdlConversionException
	 */
	public static <T extends StorableObject> T fromTransferable(final IdlStorableObject transferable)
			throws IdlConversionException {
		T storableObject = null;
		try {
			storableObject = StorableObjectPool.<T> getStorableObject(Identifier.valueOf(transferable.id), false);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false;
		}

		if (storableObject != null) {
			if (storableObject instanceof IdlTransferableObjectExt) {
				@SuppressWarnings("unchecked")
				final IdlTransferableObjectExt<IdlStorableObject> pureJava = (IdlTransferableObjectExt) storableObject;
				pureJava.fromIdlTransferable(transferable);
			} else {
				Log.debugMessage(storableObject.getClass().getName() + " doesn't support IDL import/export; returning as is", SEVERE);
			}
		} else {
			try {
				@SuppressWarnings("unchecked")
				final T pureJava = (T) transferable.getNative();
				storableObject = pureJava;
			} catch (final IdlCreateObjectException coe) {
				Log.debugMessage(coe.detailMessage, SEVERE);
				Log.debugMessage(coe, SEVERE);
				throw new IdlConversionException(new CreateObjectException(coe.detailMessage));
			}
		}

		return storableObject;
	}


	/*	Refresh */

	/**
	 * Refresh objects with identifiers from the given set.
	 * 
	 * @param ids
	 *        Identifiers of objects to refresh
	 * @throws ApplicationException
	 */
	public static void refresh(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null : NON_NULL_EXPECTED;
		Log.debugMessage("Requested for: " + ids, Log.DEBUGLEVEL08);
		if (ids.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE + ": " + entityCode;

		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
		if (objectPool == null) {
			Log.errorMessage(ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return;
		}

		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));

		final Set<StorableObject> refreshObjects = new HashSet<StorableObject>();
		synchronized (objectPool) {
			for (final StorableObject storableObject : objectPool.values()) {
				final Identifier id = storableObject.getId();
				if (ids.contains(id) && !storableObject.isChanged() && (entityDeletedIds == null || !entityDeletedIds.contains(id))) {
					refreshObjects.add(storableObject);
				}
			}
		}

		if (refreshObjects.isEmpty()) {
			Log.debugMessage("LRUMap for '" + ObjectEntities.codeToString(entityCode) + "' entity has no elements to refresh",
					Log.DEBUGLEVEL08);
			return;
		}

		Log.debugMessage("Refreshing pool for '" + ObjectEntities.codeToString(entityCode) + "'s: " + ids, Log.DEBUGLEVEL08);

		refresh(refreshObjects, entityCode, objectPool);
	}

	/**
	 * Refresh the whole pool
	 * @throws ApplicationException
	 */
	public static void refresh() throws ApplicationException {
		final Set<StorableObject> refreshObjects = new HashSet<StorableObject>();
		synchronized (objectPoolMap) {
			for (final TShortObjectIterator entityCodeIterator = objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
				entityCodeIterator.advance();
				final short entityCode = entityCodeIterator.key();
				final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);

				final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));

				refreshObjects.clear();
				synchronized (objectPool) {
					for (final StorableObject storableObject : objectPool.values()) {
						if (!storableObject.isChanged() && (entityDeletedIds == null || !entityDeletedIds.contains(storableObject.getId()))) {
							refreshObjects.add(storableObject);
						}
					}
				}
				if (refreshObjects.isEmpty()) {
					Log.debugMessage("LRUMap for entity '" + ObjectEntities.codeToString(entityCode) + "' has no elements to refresh",
							Log.DEBUGLEVEL08);
					continue;
				}

				Log.debugMessage("Refreshing pool for entity: '" + ObjectEntities.codeToString(entityCode) + "'/" + entityCode,
						Log.DEBUGLEVEL08);

				refresh(refreshObjects, entityCode, objectPool);				
			}
		}
	}

	/**
	 * NOTE: This private method is used only from methods {@link #refresh()} and
	 * {@link #refresh(Set)} of this class. It simply contains all common for both
	 * methods code. So this method do not make any checks and asserts.
	 * 
	 * @param refreshObjects -
	 *        set of objects to refresh
	 * @param entityCode -
	 *        entity code of these objects
	 * @param objectPool -
	 *        LRUMap object pool to put into / remove from
	 * @throws ApplicationException
	 */
	private static void refresh(final Set<StorableObject> refreshObjects,
			final short entityCode,
			final LRUMap<Identifier, StorableObject> objectPool) throws ApplicationException {
		final Set<Identifier> refreshObjectIds = Identifier.createIdentifiers(refreshObjects);
		final Map<Identifier, StorableObjectVersion> remoteVersionsMap = objectLoader.getRemoteVersions(refreshObjectIds);

		final Set<Identifier> reloadObjectIds = new HashSet<Identifier>();

		for (final StorableObject storableObject : refreshObjects) {
			final Identifier id = storableObject.getId();
			final StorableObjectVersion version = storableObject.getVersion();
			final StorableObjectVersion remoteVersion = remoteVersionsMap.get(id);

			assert remoteVersion != null : NON_NULL_EXPECTED + " for object '" + id + "'";

			if (remoteVersion.equals(ILLEGAL_VERSION)) {
				Log.debugMessage("Object '" + ObjectEntities.codeToString(entityCode) + "' '" + id
						+ "' removed remotely -- removing locally", Log.DEBUGLEVEL08);
				objectPool.remove(id);
			} else if (version.isOlder(remoteVersion)) {
				reloadObjectIds.add(id);
			}
		}

		if (reloadObjectIds.isEmpty()) {
			Log.debugMessage("LRUMap for '" + ObjectEntities.codeToString(entityCode) + "' entity has no elements to reload",
					Log.DEBUGLEVEL08);
			return;
		}

		final Set<StorableObject> reloadedStorableObjects = objectLoader.loadStorableObjects(reloadObjectIds);
		for (final StorableObject reloadedStorableObject : reloadedStorableObjects) {
			final Identifier id = reloadedStorableObject.getId();
			final StorableObject poolStorableObject = objectPool.get(id);
			setStorableObjectAttributes(poolStorableObject, reloadedStorableObject);
			Log.debugMessage("Reloaded: '" + id + "' with version: " + reloadedStorableObject.getVersion(), Log.DEBUGLEVEL08);
		}
	}


	/**
	 * Crutch#235
	 * @see <a href = "http://bass.science.syrus.ru/bugzilla/show_bug.cgi?id=235">This bug&apos;s homepage</a>
	 * 
	 * This ORB only needs to call StorableObject#fromTransferable
	 */
	public static final String DEFAULT_ORB_INITIAL_HOST = "127.0.0.1";
	public static final int DEFAULT_ORB_INITIAL_PORT = 1050;
	private static final ORB CRUTCH_ORB;
	static {
		final Properties properties = System.getProperties();
		final String host = ApplicationProperties.getString("ORBInitialHost", DEFAULT_ORB_INITIAL_HOST);
		final int port = ApplicationProperties.getInt("ORBInitialPort", DEFAULT_ORB_INITIAL_PORT);
		Log.debugMessage("host: " + host + ", port: " + port, Log.DEBUGLEVEL09);
		properties.setProperty("org.omg.CORBA.ORBInitialHost", host);
		properties.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
		final String[] args = null;
		CRUTCH_ORB = ORB.init(args, properties);
	}

	/**
	 * Crutch#235
	 * 
	 * @param localStorableObject
	 * @param remoteStorableObject
	 */
	@Crutch235(notes = "Implement method StorableObject#setAttributes(StorableObject)")
	private static void setStorableObjectAttributes(final StorableObject localStorableObject,
			final StorableObject remoteStorableObject) {
		assert localStorableObject != null : NON_NULL_EXPECTED;
		assert remoteStorableObject != null : NON_NULL_EXPECTED;
		assert localStorableObject.id.equals(remoteStorableObject.id) : "Local: '"
				+ localStorableObject.id + "', remote: '" + remoteStorableObject.id + "'";

		if (!(localStorableObject instanceof IdlTransferableObjectExt)) {
			Log.debugMessage("Local object (an instance of "
					+ localStorableObject.getClass().getName()
					+ ") doesn't support IDL import/export; update aborted",
					SEVERE);
			return;
		}
		if (!(remoteStorableObject instanceof IdlTransferableObjectExt)) {
			Log.debugMessage("Remote object (an instance of "
					+ remoteStorableObject.getClass().getName()
					+ ") doesn't support IDL import/export; update aborted",
					SEVERE);
			return;
		}

		@SuppressWarnings("unchecked")
		final IdlTransferableObjectExt<IdlStorableObject> localPureJava = (IdlTransferableObjectExt) localStorableObject;
		@SuppressWarnings("unchecked")
		final IdlTransferableObjectExt<IdlStorableObject> remotePureJava = (IdlTransferableObjectExt) remoteStorableObject;

		try {
			localPureJava.fromIdlTransferable(remotePureJava.getIdlTransferable(CRUTCH_ORB));
		} catch (final IdlConversionException ice) {
			Log.errorMessage(ice);
		}
	}


	/*	Serialization */

	public static void deserialize(final LRUMapSaver<Identifier, StorableObject> lruMapSaver) throws ApplicationException {
		final long t0 = System.currentTimeMillis();
		synchronized (objectPoolMap) {
			for (final TShortObjectIterator entityCodeIterator = objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
				entityCodeIterator.advance();
				final short entityCode = entityCodeIterator.key();
				final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
				lruMapSaver.load(ObjectEntities.codeToString(entityCode), objectPool);
			}
		}
		final long t1 = System.currentTimeMillis();
		refresh();
		final long t2 = System.currentTimeMillis();
		Log.debugMessage("Deserialization time " + (t1 - t0) + " ms, refresh time " + (t2 - t1) + " ms", Log.DEBUGLEVEL08);
	}

	public static void serialize(final LRUMapSaver<Identifier, StorableObject> lruMapSaver) {
		final long t0 = System.currentTimeMillis();
		synchronized (objectPoolMap) {
			for (final TShortObjectIterator entityCodeIterator = objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
				entityCodeIterator.advance();
				final short entityCode = entityCodeIterator.key();
				final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
				lruMapSaver.save(objectPool, ObjectEntities.codeToString(entityCode), true);
			}
		}
		final long t1 = System.currentTimeMillis();
		Log.debugMessage("Serialization time " + (t1 - t0) + " ms", Log.DEBUGLEVEL08);
	}


//	/*	Truncate */
//
//	public static void truncate(final short entityCode) {
//		final LRUMap<Identifier, StorableObject> objectPool = getLRUMap(entityCode);
//		if (objectPool instanceof StorableObjectResizableLRUMap) {
//			((StorableObjectResizableLRUMap) objectPool).truncate(true);
//		}
//		else {
//			Log.errorMessage("ERROR: Object pool class '" + objectPool.getClass().getName()
//					+ "' not 'StorableObjectResizableLRUMap' -- cannot truncate pool");
//		}
//	}

	private static <T extends StorableObject> LRUMap<Identifier, T> getLRUMap(final Short entityCode) {
		return getLRUMap(entityCode.shortValue());
	}

	@SuppressWarnings("unchecked")
	private static <T extends StorableObject> LRUMap<Identifier, T> getLRUMap(final short entityCode) {
		return (LRUMap) objectPoolMap.get(entityCode);
	}
}
