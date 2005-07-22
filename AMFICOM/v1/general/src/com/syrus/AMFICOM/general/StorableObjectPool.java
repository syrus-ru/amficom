/*-
 * $Id: StorableObjectPool.java,v 1.129 2005/07/22 11:00:38 arseniy Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TObjectProcedure;
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
import java.util.logging.Level;

import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.io.LRUMapSaver;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.129 $, $Date: 2005/07/22 11:00:38 $
 * @author $Author: arseniy $
 * @module general_v1
 * @todo Этот класс не проверен. В первую очередь надо проверить работу с объектами, помеченными на удаление
 * (т. е. объектами, идентификаторы которых помещены в DELETED_IDS_MAP). Проверять так:
 * 1) заполнить кишки различными объектами;
 * 2) пометить некоторые из этих объектов на удаление;
 * 3) проверить все вызовы подгрузки get<*>, при этом во входных наборах идентификаторов (как и "для", так и "кроме")
 * должны присутствовать идентификаторы помеченных на удаление объектов;
 * 4) проверить вызов refresh
 * 5) проверить все вызовы flush, убедиться что объекты, помеченные на удаление, действительно удаляются.
 * @todo Версии успешно сохранённых объектов должны выставлятся здесь и только здесь
 * (а не в StorableObjectDatabase или в CORBAObjectLoader). Метод saveWithDependencies должен получать
 * сведения о сохранении от метода saveStorableObjects, исходя из которых именно он и должен
 * обновлять версии успешно сохранённых объектов. Учение о Сохранении подлежит переработке.
 */
public abstract class StorableObjectPool {

	/**
	 * БАЙАН-symbol {@value}
	 */
	public static final String БАЙАН = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	protected TShortObjectHashMap objectPoolMap;

	private short selfGroupCode;
	private String selfGroupName;

	protected Class cacheMapClass;

	/**
	 * A "group code" -- "pool" map to store initialized pools.
	 */
	private static final TShortObjectHashMap GROUP_CODE_POOL_MAP = new TShortObjectHashMap();

	/**
	 * An "entity code" -- "factory" map so store factories pool can use to
	 * create objects.
	 */
	static final TShortObjectHashMap ENTITY_CODE_FACTORY_MAP = new TShortObjectHashMap();

	private static final Map<Short, Set<Identifier>> DELETED_IDS_MAP = new HashMap<Short, Set<Identifier>>();

	private static final Map<Integer, Map<Short, Set<StorableObject>>> SAVING_OBJECTS_MAP = new HashMap<Integer, Map<Short, Set<StorableObject>>>();
	private static final Set<Identifier> SAVING_OBJECT_IDS = new HashSet<Identifier>();

	/**
	 * @deprecated
	 * @param objectPoolMapSize
	 * @param selfGroupCode
	 */
	@Deprecated
	public StorableObjectPool(final int objectPoolMapSize, final short selfGroupCode) {
		this(objectPoolMapSize, selfGroupCode, LRUMap.class);
	}

	/**
	 * Called only from descendants
	 * @param objectPoolMapSize
	 * @param selfGroupCode
	 * @param cacheMapClass
	 */
	protected StorableObjectPool(final int objectPoolMapSize, final short selfGroupCode, final Class cacheMapClass) {
		this.objectPoolMap = new TShortObjectHashMap(objectPoolMapSize);

		this.selfGroupCode = selfGroupCode;
		this.selfGroupName = ObjectGroupEntities.codeToString(this.selfGroupCode).replaceAll("Group$", "");
		registerPool(this.selfGroupCode, this);

		try {
			this.cacheMapClass = Class.forName(cacheMapClass.getName());
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheMapClass.getName() + "' cannot be found, using default");
			this.cacheMapClass = LRUMap.class;
		}

	}

	private static final void registerPool(final short groupCode, final StorableObjectPool pool) {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		/*
		 * Assertion below turned off, since, currently, a
		 * double-registration of pool may happen (for instance, when
		 * user performs a repeated login after an unsuccessful one, or
		 * when he logs out and back in.
		 * NOTE: Correctly written application never attempts to repeatedly register a pool,
		 * so this assert  must be turned on.
		 */
		assert true || !GROUP_CODE_POOL_MAP.containsKey(groupCode);
		GROUP_CODE_POOL_MAP.put(groupCode, pool);
	}

	protected static final void registerFactory(final short entityCode, final StorableObjectFactory factory) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		assert !ENTITY_CODE_FACTORY_MAP.containsKey(entityCode);
		ENTITY_CODE_FACTORY_MAP.put(entityCode, factory);
	}

	protected void addObjectPool(final short objectEntityCode, final int poolSize) {
		try {
			final Constructor constructor = this.cacheMapClass.getConstructor(new Class[] { int.class});
			final Object obj = constructor.newInstance(new Object[] { new Integer(poolSize)});
			if (obj instanceof LRUMap) {
				final LRUMap objectPool = (LRUMap) obj;
				this.objectPoolMap.put(objectEntityCode, objectPool);
				Log.debugMessage("StorableObjectPool.addObjectPool | Pool for '" + ObjectEntities.codeToString(objectEntityCode)
						+ "'/" + objectEntityCode + " of size " + poolSize + " added", Log.DEBUGLEVEL07);
			} else
				throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
						+ this.cacheMapClass.getName() + " must extend LRUMap");
		} catch (SecurityException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " SecurityException " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " IllegalArgumentException " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " NoSuchMethodException " + e.getMessage());
		} catch (InstantiationException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " InstantiationException " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " IllegalAccessException " + e.getMessage());
		} catch (final InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else
				throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
						+ this.cacheMapClass.getName() + " InvocationTargetException " + ite.getMessage());
		}

	}


	/*	Get */

	public static StorableObject getStorableObject(final Identifier id, final boolean useLoader) throws ApplicationException {
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

		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			return pool.getStorableObjectImpl(id, useLoader);

		throw new ApplicationException("StorableObjectPool.getStorableObject | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static <T> Set<T> getStorableObjects(final Set<Identifier> ids, boolean useLoader) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null) {
			final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
			if (entityDeletedIds != null) {
				return pool.getStorableObjectsImpl(Identifier.createSubtractionIdentifiers(ids, entityDeletedIds), useLoader);
			}
			return pool.getStorableObjectsImpl(ids, useLoader);
		}

		throw new ApplicationException("StorableObjectPool.getStorableObjects() | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode)
				+ " is not initialized");
	}

	/**
	 * Break on load error
	 * @param condition
	 * @param useLoader
	 * @return Set of StorableObject matching condition 
	 * @throws ApplicationException
	 */
	public static <T> Set<T> getStorableObjectsByCondition(final StorableObjectCondition condition, final boolean useLoader)  throws ApplicationException {
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
	public static <T> Set<T> getStorableObjectsByCondition(final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		assert condition != null : ErrorMessages.NON_NULL_EXPECTED;
		final short groupCode = ObjectGroupEntities.getGroupCode(condition.getEntityCode());
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			return pool.getStorableObjectsByConditionImpl(condition, useLoader, breakOnLoadError);

		throw new ApplicationException("StorableObjectPool.getStorableObjectsByCondition | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	/**
	 * Break on load error
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @return Set of StorableObject with identifiers not in set, matching condition
	 * @throws ApplicationException
	 */
	public static Set getStorableObjectsByConditionButIds(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		return getStorableObjectsByConditionButIds(ids, condition, useLoader, true);
	}

	/**
	 * 3-d parameter controls if break on load error
	 * @param ids
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @return Set of StorableObject with identifiers not in set, matching condition
	 * @throws ApplicationException
	 */
	public static Set getStorableObjectsByConditionButIds(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);

		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null) {
			final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
			if (entityDeletedIds != null) {
				return pool.getStorableObjectsByConditionButIdsImpl(Identifier.createSumIdentifiers(ids, entityDeletedIds),
						condition,
						useLoader,
						breakOnLoadError);
			}
			return pool.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader, breakOnLoadError);
		}

		throw new ApplicationException("StorableObjectPool.getStorableObjectsByConditionButIds | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	private final StorableObject getStorableObjectImpl(final Identifier objectId, final boolean useLoader) throws ApplicationException {
		assert objectId != null: ErrorMessages.NON_NULL_EXPECTED;

		final short objectEntityCode = objectId.getMajor();
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(objectEntityCode);
		if (objectPool != null) {
			StorableObject storableObject = (StorableObject) objectPool.get(objectId);
			if (storableObject == null && useLoader) {
				final Set storableObjects = this.loadStorableObjects(Collections.singleton(objectId));
				if (!storableObjects.isEmpty())
					storableObject = (StorableObject) storableObjects.iterator().next();
				if (storableObject != null)
					try {
						this.putStorableObjectImpl(storableObject);
					} catch (final IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
			}
			return storableObject;
		}

		Log.errorMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectImpl | Cannot find object pool for objectId: '"
				+ objectId + "' entity code: '" + ObjectEntities.codeToString(objectEntityCode) + "'");
		for (final TShortObjectIterator it = this.objectPoolMap.iterator(); it.hasNext();) {
			it.advance();
			final short entityCode = it.key();
			Log.debugMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectImpl | available "
					+ ObjectEntities.codeToString(entityCode) + " / " + entityCode, Log.DEBUGLEVEL05);
		}
		return null;
	}

	/**
	 * @param ids
	 *          set of pure java identifiers.
	 * @param useLoader
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	private final Set getStorableObjectsImpl(final Set<Identifier> ids, final boolean useLoader) throws ApplicationException {
		assert ids != null;

		final Set<StorableObject> storableObjects = new HashSet<StorableObject>();
		TShortObjectHashMap objectQueueMap = null;

		for (final Iterator<Identifier> idIterator = ids.iterator(); idIterator.hasNext();) {
			final Identifier id = idIterator.next();

			final short entityCode = id.getMajor();
			final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
			if (objectPool != null) {
				final StorableObject storableObject = (StorableObject) objectPool.get(id);
				if (storableObject != null)
					storableObjects.add(storableObject);
				else
					if (useLoader) {
						/*
						 * Local object not found and useLoader is true.
						 */
						if (objectQueueMap == null)
							objectQueueMap = new TShortObjectHashMap();
						Set<Identifier> objectQueue = (Set) objectQueueMap.get(entityCode);
						if (objectQueue == null) {
							objectQueue = new HashSet<Identifier>();
							objectQueueMap.put(entityCode, objectQueue);
						}
						objectQueue.add(id);
					}
			} else
				Log.errorMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '"
						+ id + "', entity code: '" + ObjectEntities.codeToString(entityCode) + "'");
		}

		/*	Just debug output -- nothing more*/
		Log.debugMessage("StorableObjectPool.getStorableObjectsImpl | Requested objects for set: " + ids, Log.DEBUGLEVEL10);
		final StringBuffer stringBuffer1 = new StringBuffer();
		for (final Iterator it = storableObjects.iterator(); it.hasNext();) {
			final StorableObject storableObject = (StorableObject) it.next();
			if (stringBuffer1.length() != 0)
				stringBuffer1.append(", ");
			stringBuffer1.append(storableObject.getId());
		}
		Log.debugMessage("StorableObjectPool.getStorableObjectsImpl | Found in pool "
				+ storableObjects.size() + " objects: " + stringBuffer1, Log.DEBUGLEVEL10);
		/*	^Just debug output -- nothing more^*/

		if (objectQueueMap != null) {
			for (final TShortObjectIterator entityCodeIterator = objectQueueMap.iterator(); entityCodeIterator.hasNext();) {
				entityCodeIterator.advance();
				final Set<Identifier> objectQueue = (Set) entityCodeIterator.value();

				try {
					for (final Iterator it = this.loadStorableObjects(objectQueue).iterator(); it.hasNext();) {
						final StorableObject storableObject = (StorableObject) it.next();
						this.putStorableObjectImpl(storableObject);
						storableObjects.add(storableObject);
					}
				} catch (final IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
				}
			}
		}

		return storableObjects == null ? Collections.EMPTY_SET : storableObjects;
	}

	private final <T> Set<T> getStorableObjectsByConditionImpl(final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		final Set<Identifier> emptySet = Collections.emptySet();
		return this.getStorableObjectsByConditionButIdsImpl(emptySet, condition, useLoader, breakOnLoadError);
	}

	/**
	 * @param ids
	 *          a non-null (use {@link Collections#EMPTY_SET}in case of
	 *          emergency) set of pure-java identifiers.
	 * @param condition
	 * @param useLoader
	 * @param breakOnLoadError
	 * @throws ApplicationException
	 */
	private final Set getStorableObjectsByConditionButIdsImpl(final Set<Identifier> ids,
			final StorableObjectCondition condition,
			final boolean useLoader,
			final boolean breakOnLoadError) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		assert condition != null : ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);

		assert objectPool != null: "Cannot find object pool for entity code " + entityCode
				+ ", entity: '" + ObjectEntities.codeToString(entityCode) + " , condition class:" + condition.getClass().getName();

		final Set<StorableObject> storableObjects = new HashSet<StorableObject>();
		for (final Iterator storableObjectIterator = objectPool.iterator(); storableObjectIterator.hasNext();) {
			final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
			final Identifier id = storableObject.getId();
			if (!ids.contains(id) && condition.isConditionTrue(storableObject)) {
				storableObjects.add(storableObject);
			}
		}

		/*	Just debug output -- nothing more*/
		Log.debugMessage("StorableObjectPool.getStorableObjectsByConditionButIdsImpl | Requested objects '"
				+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode, Log.DEBUGLEVEL10);
		Log.debugMessage("StorableObjectPool.getStorableObjectsByConditionButIdsImpl | But ids: " + ids, Log.DEBUGLEVEL10);
		final StringBuffer stringBuffer1 = new StringBuffer();
		for (final Iterator it = storableObjects.iterator(); it.hasNext();) {
			final StorableObject storableObject = (StorableObject) it.next();
			if (stringBuffer1.length() != 0)
				stringBuffer1.append(", ");
			stringBuffer1.append(storableObject.getId());
		}
		Log.debugMessage("StorableObjectPool.getStorableObjectsByConditionButIdsImpl | Found in pool "
				+ storableObjects.size() + " (of total " + objectPool.size() + ") objects: " + stringBuffer1, Log.DEBUGLEVEL10);
		/*	^Just debug output -- nothing more^*/

		Set<? extends StorableObject> loadedObjects = null;

		if (useLoader && condition.isNeedMore(storableObjects)) {
			final Set<Identifier> loadButIds = Identifier.createSumIdentifiers(ids, storableObjects);

			try {
				loadedObjects = this.loadStorableObjectsButIds(condition, loadButIds);
			} catch (ApplicationException ae) {
				if (breakOnLoadError)
					throw ae;
				Log.errorException(ae);
				loadedObjects = Collections.emptySet();
			}
			
			/*	Just debug output -- nothing more*/
			final StringBuffer stringBuffer2 = new StringBuffer();
			for (final Iterator storableObjectIterator = loadedObjects.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				if (stringBuffer2.length() != 0)
					stringBuffer2.append(", ");
				stringBuffer2.append(storableObject.getId());
			}
			Log.debugMessage("StorableObjectPool.getStorableObjectsByConditionButIdsImpl | Loaded : " + stringBuffer2, Log.DEBUGLEVEL10);
			/* ^Just debug output -- nothing more^ */

		}

		/*
		 * This block is only needed in order for LRUMap to rehash
		 * itself. Since it affects performance, we've turned it off.
		 */
		if (false)
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();)
				objectPool.get(((StorableObject) storableObjectIterator.next()).getId());

		if (loadedObjects != null) {
			for (final Iterator storableObjectIterator = loadedObjects.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				objectPool.put(storableObject.getId(), storableObject);
			}
			storableObjects.addAll(loadedObjects);
		}

		return storableObjects;
	}

	/*	Group-specific load-methods */
	protected abstract Set loadStorableObjects(final Set<Identifier> ids) throws ApplicationException;

	protected abstract Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set<Identifier> ids)
			throws ApplicationException;


	/*	Put */

	public static void putStorableObject(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;
		final Identifier id = storableObject.getId();
		final short entityCode = id.getMajor();
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds != null && entityDeletedIds.contains(id))
			return;

		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null) {
			pool.putStorableObjectImpl(storableObject);
		}
		else {
			throw new IllegalObjectEntityException("StorableObjectPool.putStorableObject | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized",
					IllegalObjectEntityException.OTHER_CODE);
		}
	}

	/**
	 * @param storableObject
	 *            a non-null pure java storable object.
	 * @throws IllegalObjectEntityException
	 */
	private final void putStorableObjectImpl(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;

		final Identifier id = storableObject.getId();
		final short entityCode = id.getMajor();
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			objectPool.put(id, storableObject);
		}
		else {
			throw new IllegalObjectEntityException(this.selfGroupName
					+ "StorableObjectPool.putStorableObject | Illegal object entity: '"
					+ ObjectEntities.codeToString(entityCode)
					+ "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
	}


	/*	Clean changed objects */

	public static void cleanChangedGroupStorableObjects(final short groupCode) {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		final StorableObjectPool storableObjectPool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (storableObjectPool != null) {
			synchronized (storableObjectPool.objectPoolMap) {
				for (final TShortObjectIterator it = storableObjectPool.objectPoolMap.iterator(); it.hasNext();) {
					it.advance();
					final short entityCode = it.key();
					DELETED_IDS_MAP.remove(new Short(entityCode));
					storableObjectPool.cleanChangedStorableObjectsImpl(entityCode);
				}
			}
		}
		else
			Log.errorMessage("StorableObjectPool.cleanChangedGroupStorableObjects | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static void cleanChangedStorableObjects(final short entityCode) {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		DELETED_IDS_MAP.remove(new Short(entityCode));
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		final StorableObjectPool storableObjectPool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (storableObjectPool != null)
			storableObjectPool.cleanChangedStorableObjectsImpl(entityCode);
		else
			Log.errorMessage("StorableObjectPool.cleanChangedGroupStorableObjects | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	private final void cleanChangedStorableObjectsImpl(final short entityCode) {
		final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
		if (lruMap != null) {
			for (final Iterator it = lruMap.iterator(); it.hasNext();) {
				final StorableObject storableObject = (StorableObject) it.next();
				if (storableObject.isChanged())
					it.remove();
			}
		}
	}


	/*	Delete */

	public static void delete(final Identifier id) {
		final short entityCode = id.getMajor();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
		if (entityDeletedIds == null) {
			entityDeletedIds = new HashSet<Identifier>();
			DELETED_IDS_MAP.put(new Short(entityCode), entityDeletedIds);
		}
		entityDeletedIds.add(id);

		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null) {
			pool.deleteImpl(id);
		}
		else {
			Log.debugMessage("StorableObjectPool.delete | Unable to delete object: '" + id + "' of group "
					+ ObjectGroupEntities.codeToString(groupCode) + '(' + groupCode
					+ ") since the corresponding pool is not registered", Level.SEVERE);
		}
	}

	/**
	 * @param identifiables
	 */
	public static void delete(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null: ErrorMessages.NON_NULL_EXPECTED;

		/*
		 * Map<Short groupCode, Map<Short entityCode, Set<Identifiable> identifiables>>
		 */
		final Map<Short, Map<Short, Set<Identifiable>>> deleteObjectsMap = new HashMap<Short, Map<Short, Set<Identifiable>>>();

		for (final Identifiable identifiable : identifiables) {
			final Identifier id = identifiable.getId();
			final short entityCode = id.getMajor();
			assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
			Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));
			if (entityDeletedIds == null) {
				entityDeletedIds = new HashSet<Identifier>();
				DELETED_IDS_MAP.put(new Short(entityCode), entityDeletedIds);
			}
			entityDeletedIds.add(id);

			final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
			assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
			final Short groupKey = new Short(groupCode);
			Map<Short, Set<Identifiable>> groupDeleteObjectsMap = deleteObjectsMap.get(groupKey);
			if (groupDeleteObjectsMap == null) {
				groupDeleteObjectsMap = new HashMap<Short, Set<Identifiable>>();
				deleteObjectsMap.put(groupKey, groupDeleteObjectsMap);
			}
			final Short entityKey = new Short(entityCode);
			Set<Identifiable> entityGroupDeleteObjects = groupDeleteObjectsMap.get(entityKey);
			if (entityGroupDeleteObjects == null) {
				entityGroupDeleteObjects = new HashSet<Identifiable>();
				groupDeleteObjectsMap.put(entityKey, entityGroupDeleteObjects);
			}
			entityGroupDeleteObjects.add(identifiable);
		}

		for (final Short groupKey : deleteObjectsMap.keySet()) {
			final short groupCode = groupKey.shortValue();
			final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
			if (pool != null) {
				pool.deleteImpl(deleteObjectsMap.get(groupKey));
			}
			else {
				Log.debugMessage("StorableObjectPool.delete | Unable to delete identifiables of group: "
						+ ObjectGroupEntities.codeToString(groupCode) + '(' + groupCode
						+ ") since the corresponding pool is not registered", Level.SEVERE);
			}
		}

	}

	private final void deleteImpl(final Identifier id) {
		short entityCode = id.getMajor();
		final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
		if (lruMap != null) {
			lruMap.remove(id);
		}
		else {
			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '"
					+ ObjectEntities.codeToString(entityCode) + "' entity code: " + entityCode);
		}
	}

	private final void deleteImpl(final Map<Short, Set<Identifiable>> groupDeleteObjectsMap) {
		for (final Short entityKey : groupDeleteObjectsMap.keySet()) {
			final Set<Identifiable> entityGroupDeleteObjects = groupDeleteObjectsMap.get(entityKey);
			final short entityCode = entityKey.shortValue();
			assert StorableObject.getEntityCodeOfIdentifiables(entityGroupDeleteObjects) == entityCode;
			final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
			if (lruMap != null) {
				for (final Identifiable identifiable : entityGroupDeleteObjects) {
					final Identifier id = identifiable.getId();
					lruMap.remove(id);
				}
			}
			else {
				Log.errorMessage(this.selfGroupName
						+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '"
						+ ObjectEntities.codeToString(entityCode) + "' entity code: " + entityCode);
			}
		}
	}

	/*	Group-specific method */
	protected abstract void deleteStorableObjects(final Set<? extends Identifiable> identifiables);


	/*	Flush */

	public static void flush(final Identifiable identifiable, final boolean force) throws ApplicationException {
		final Identifier id = identifiable.getId();
		final short entityCode = id.getMajor();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;

		final StorableObjectPool storableObjectPool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (storableObjectPool == null) {
			throw new IllegalDataException(ErrorMessages.GROUP_POOL_NOT_REGISTERED + ": '"
					+ ObjectGroupEntities.codeToString(groupCode) + "'/" + groupCode);
		}

		final Short entityKey = new Short(entityCode);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(entityKey);
		if (entityDeletedIds != null && entityDeletedIds.contains(id)) {
			storableObjectPool.deleteStorableObjects(Collections.singleton(id));
			entityDeletedIds.remove(id);
			if (entityDeletedIds.isEmpty()) {
				DELETED_IDS_MAP.remove(entityKey);
			}
		}
		else {
			synchronized (SAVING_OBJECTS_MAP) {
				SAVING_OBJECT_IDS.clear();
				SAVING_OBJECTS_MAP.clear();
				final StorableObject storableObject = getStorableObject(id, false);
				if (storableObject != null) {
					checkChangedWithDependencies(storableObject, 0);
					saveWithDependencies(force);
				}
			}
		}
	}

	public static void flush(final short entityCode, final boolean force) throws ApplicationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;

		final StorableObjectPool storableObjectPool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (storableObjectPool == null) {
			throw new IllegalDataException(ErrorMessages.GROUP_POOL_NOT_REGISTERED + ": '"
					+ ObjectGroupEntities.codeToString(groupCode) + "'/" + groupCode);
		}

		flushDeleted(entityCode, storableObjectPool);

		synchronized (SAVING_OBJECTS_MAP) {
			SAVING_OBJECT_IDS.clear();
			SAVING_OBJECTS_MAP.clear();
			checkChangedWithDependencies(entityCode, storableObjectPool);
			saveWithDependencies(force);
		}
	}

	public static void flushGroup(final short groupCode, final boolean force) throws ApplicationException {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;

		final StorableObjectPool storableObjectPool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (storableObjectPool == null) {
			throw new IllegalDataException(ErrorMessages.GROUP_POOL_NOT_REGISTERED + ": '"
					+ ObjectGroupEntities.codeToString(groupCode) + "'/" + groupCode);
		}

		flushDeleted(storableObjectPool);

		synchronized (SAVING_OBJECTS_MAP) {
			SAVING_OBJECT_IDS.clear();
			SAVING_OBJECTS_MAP.clear();
			checkChangedWithDependencies(storableObjectPool);
			saveWithDependencies(force);
		}
	}

	private static void flushDeleted(final short entityCode, final StorableObjectPool storableObjectPool) {
		final Short entityKey = new Short(entityCode);
		final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(entityKey);
		if (entityDeletedIds != null) {
			assert StorableObject.getGroupCodeOfIdentifiables(entityDeletedIds) == storableObjectPool.selfGroupCode;
			storableObjectPool.deleteStorableObjects(entityDeletedIds);
			DELETED_IDS_MAP.remove(entityKey);
		}
	}

	private static void flushDeleted(final StorableObjectPool storableObjectPool) {
		for (final TShortObjectIterator it = storableObjectPool.objectPoolMap.iterator(); it.hasNext();) {
			it.advance();
			final short entityCode = it.key();
			flushDeleted(entityCode, storableObjectPool);
		}
	}

	private static void checkChangedWithDependencies(final StorableObjectPool storableObjectPool) throws ApplicationException {
		synchronized (storableObjectPool.objectPoolMap) {
			for (final TShortObjectIterator it = storableObjectPool.objectPoolMap.iterator(); it.hasNext();) {
				it.advance();
				final short entityCode = it.key();
				checkChangedWithDependencies(entityCode, storableObjectPool);
			}
		}
	}

	private static void checkChangedWithDependencies(final short entityCode, final StorableObjectPool storableObjectPool) throws ApplicationException {
		final LRUMap objectPool = (LRUMap) storableObjectPool.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			synchronized (objectPool) {
				for (final Iterator it = objectPool.iterator(); it.hasNext();) {
					final StorableObject storableObject = (StorableObject) it.next();
					checkChangedWithDependencies(storableObject, 0);
				}
			}
		} else
			Log.errorMessage("StorableObjectPool.checkChangedWithDependenciesEntity | " + ErrorMessages.ENTITY_POOL_NOT_REGISTERED + ": '"
					+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
	}

	private static void checkChangedWithDependencies(final StorableObject storableObject, int dependencyLevel) throws ApplicationException {
		final Identifier id = storableObject.getId();
		if (SAVING_OBJECT_IDS.contains(id)) {
			return;
		}

		SAVING_OBJECT_IDS.add(id);

		final Set<Identifiable> dependencies = storableObject.getDependencies();
		for (final Identifiable identifiable: dependencies) {
			assert identifiable != null : ErrorMessages.NON_NULL_EXPECTED;

			StorableObject dependencyObject = null;
			if (identifiable instanceof Identifier)
				dependencyObject = getStorableObject((Identifier) identifiable, false);
			else if (identifiable instanceof StorableObject)
				dependencyObject = (StorableObject) identifiable;
			else
				throw new IllegalDataException("dependency for object '" + id
						+ "' neither Identifier nor StorableObject -- " + identifiable.getClass().getName());

			if (dependencyObject != null)
				checkChangedWithDependencies(dependencyObject, dependencyLevel + 1);
		}

		if (storableObject.isChanged()) {
			Log.debugMessage("StorableObjectPool.checkChangedWithDependenciesS | Object '" + storableObject.getId() + "' is changed",
					Log.DEBUGLEVEL10);
			final Integer dependencyKey = new Integer(-dependencyLevel);
			Map<Short, Set<StorableObject>> levelSavingObjectsMap = SAVING_OBJECTS_MAP.get(dependencyKey);
			if (levelSavingObjectsMap == null) {
				levelSavingObjectsMap = new HashMap<Short, Set<StorableObject>>();
				SAVING_OBJECTS_MAP.put(dependencyKey, levelSavingObjectsMap);
			}
			final Short entityKey = new Short(storableObject.getId().getMajor());
			Set<StorableObject> levelEntitySavingObjects = levelSavingObjectsMap.get(entityKey);
			if (levelEntitySavingObjects == null) {
				levelEntitySavingObjects = new HashSet<StorableObject>();
				levelSavingObjectsMap.put(entityKey, levelEntitySavingObjects);
			}
			levelEntitySavingObjects.add(storableObject);
		}
	}

	private static void saveWithDependencies(final boolean force) throws ApplicationException {
		for (final Integer dependencyKey : SAVING_OBJECTS_MAP.keySet()) {
			final Map<Short, Set<StorableObject>> levelSavingObjectsMap = SAVING_OBJECTS_MAP.get(dependencyKey);
			for (final Short entityKey : levelSavingObjectsMap.keySet()) {
				final Set<StorableObject> levelEntitySavingObjects = levelSavingObjectsMap.get(entityKey);
				final short groupCode = ObjectGroupEntities.getGroupCode(entityKey);
				assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
				final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
				if (pool != null) {
					pool.saveStorableObjects(levelEntitySavingObjects, force);
				}
				else {
					Log.errorMessage("The pool for group: " + ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
				}
			}
		}
	}


	/*	Group specific method */
	/**
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 */
	protected abstract void saveStorableObjects(final Set<? extends StorableObject> storableObjects, final boolean force)
			throws ApplicationException;


	/*	From transferable*/

	public static Set fromTransferables(final short entityCode,
			final IdlStorableObject transferables[],
			final boolean continueOnError) throws ApplicationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		final int length = transferables.length;
		final Set<StorableObject> storableObjects = new HashSet<StorableObject>(length);

		for (int i = 0; i < length; i++) {
			try {
				storableObjects.add(fromTransferable(entityCode, transferables[i]));
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

	public static StorableObject fromTransferable(final short entityCode, final IdlStorableObject transferable) throws ApplicationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool == null)
			throw new ApplicationException("The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode)
					+ '(' + groupCode
					+ ") is not initialized");
		return pool.fromTransferableImpl(entityCode, transferable);
	}

	/**
	 * Gets a <code>StorableObject</code> from pool by its <code>id</code>,
	 * update its fields from <code>transferable</code> and return it. If
	 * the object is not found in pool, then a newly created from
	 * <code>transferable</code> instance is returned. <em>Never</em>
	 * returns <code>null</code>.
	 * 
	 * @param entityCode to be removed as soon as migration to valuetypes is
	 *        complete.
	 * @param transferable
	 * @throws ApplicationException
	 */
	private final StorableObject fromTransferableImpl(
			final short entityCode, final IdlStorableObject transferable)
			throws ApplicationException {
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE + ": " + entityCode;

		final StorableObjectFactory factory = (StorableObjectFactory) ENTITY_CODE_FACTORY_MAP.get(entityCode);
		if (factory == null) {
			/*-
			 * Водитель -- для Веры.
			 * Фигурные скобки -- для Вовы.
			 */
			throw new IllegalDataException(
					"StorableObjectPool.fromTransferableImpl() | Don't know how to create an identifier/instance of type: "
					+ ObjectEntities.codeToString(entityCode)
					+ '(' + entityCode
					+ ") since the corresponding factory is not registered");
		}

		StorableObject storableObject = null;
		try {
			storableObject = getStorableObject(new Identifier(transferable.id), false);
		} catch (final ApplicationException ae) {
			/*
			 * Never.
			 */
			assert false;
		}

		if (storableObject == null) {
			/*
			 * Арсений, береги скобки!
			 */
			try {
				storableObject = transferable.getNative();
			} catch (final IdlCreateObjectException coe) {
				Log.debugException(coe, Level.SEVERE);
				throw new CreateObjectException(coe.detailMessage);
			}
		} else {
			/*
			 * Сохраним скобки для будущих поколений!
			 */
			storableObject.fromTransferable(transferable);
		}
		return storableObject;
	}

	/*	Refresh */

	public static void refresh() throws ApplicationException {
		final RefreshProcedure refreshProcedure = new RefreshProcedure();
		if (!GROUP_CODE_POOL_MAP.forEachValue(refreshProcedure))
			throw refreshProcedure.applicationException;
	}

	/**
	 * Aborts execution at first <code>ApplicationException</code> caught.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: arseniy $
	 * @version $Revision: 1.129 $, $Date: 2005/07/22 11:00:38 $
	 * @module general_v1
	 */
	private static final class RefreshProcedure implements TObjectProcedure {
		ApplicationException applicationException;

		public boolean execute(final Object object) {
			try {
				((StorableObjectPool) object).refreshImpl();
				return true;
			} catch (final ApplicationException ae) {
				this.applicationException = ae;
				return false;
			}
		}
	}

	/**
	 * Refresh only unchanged storable objects.
	 *
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	final void refreshImpl() throws ApplicationException {
		Log.debugMessage(this.selfGroupName + "StorableObjectPool.refreshImpl | trying to refresh Pool...", Log.DEBUGLEVEL03);
		final Set<StorableObject> storableObjects = new HashSet<StorableObject>();

		for (final TShortObjectIterator entityCodeIterator = this.objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			final LRUMap lruMap = (LRUMap) entityCodeIterator.value();

			final Set<Identifier> entityDeletedIds = DELETED_IDS_MAP.get(new Short(entityCode));

			storableObjects.clear();
			for (final Iterator storableObjectIterator = lruMap.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				/*
				 * Not changed && not deleted.
				 */
				if (!storableObject.isChanged() && entityDeletedIds != null && !entityDeletedIds.contains(storableObject.getId())) {
					storableObjects.add(storableObject);
				}
			}
			if (storableObjects.isEmpty()) {
				Log.debugMessage(this.selfGroupName
						+ "StorableObjectPool.refreshImpl | LRUMap for '"
						+ ObjectEntities.codeToString(entityCode) + "' entity has no elements", Log.DEBUGLEVEL08);
				continue;
			}
			Log.debugMessage(this.selfGroupName
					+ "StorableObjectPool.refreshImpl | try refresh LRUMap for '"
					+ ObjectEntities.codeToString(entityCode) + "' entity", Log.DEBUGLEVEL08);

			final Set<Identifier> returnedStorableObjectsIds = this.refreshStorableObjects(storableObjects);
			if (returnedStorableObjectsIds.isEmpty())
				continue;

			for (final Iterator it = this.loadStorableObjects(returnedStorableObjectsIds).iterator(); it.hasNext();) {
				final StorableObject storableObject = (StorableObject) it.next();
				try {
					this.putStorableObjectImpl(storableObject);
				} catch (final IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
				}
			}
		}
	}

	/*	Group specific method */
	protected abstract Set<Identifier> refreshStorableObjects(final Set<? extends StorableObject> storableObjects) throws ApplicationException;


	/*	Serialization */

	public static void deserialize() {
		for (final TShortObjectIterator iterator = GROUP_CODE_POOL_MAP.iterator(); iterator.hasNext();) {
			iterator.advance();
			final StorableObjectPool pool = (StorableObjectPool) iterator.value();
			pool.deserializeImpl();
		}
	}

	public static void serialize() {
		for (final TShortObjectIterator iterator = GROUP_CODE_POOL_MAP.iterator(); iterator.hasNext();) {
			iterator.advance();
			final StorableObjectPool pool = (StorableObjectPool) iterator.value();
			pool.serializeImpl();
		}
	}

	private final void deserializeImpl() {
		for (final TShortObjectIterator entityCodeIterator = this.objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			final Set<Identifier> keys = LRUMapSaver.load(ObjectEntities.codeToString(entityCode));
			if (keys != null) {
				try {
					getStorableObjects(keys, true);
				} catch (ApplicationException ae) {
					Log.errorMessage(this.selfGroupName + "StorableObjectPool.deserializeImpl | Cannot get entity '"
							+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
					Log.errorException(ae);
				}
			}
		}
	}

	private final void serializeImpl() {
		for (final TShortObjectIterator entityCodeIterator = this.objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			LRUMapSaver.save((LRUMap) this.objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode), true);
		}
	}


	/*	Truncate */

	public static void truncate(final short entityCode) {
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode) : ErrorMessages.ILLEGAL_GROUP_CODE + ": " + groupCode;
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.truncateImpl(entityCode);
		else
			Log.errorMessage("StorableObjectPool.truncate | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	private final void truncateImpl(final short entityCode) {
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool instanceof StorableObjectResizableLRUMap)
			((StorableObjectResizableLRUMap) objectPool).truncate(true);
		else
			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.truncateImpl | ERROR: Object pool class '"
					+ objectPool.getClass().getName()
					+ "' not 'StorableObjectResizableLRUMap' -- cannot truncate pool");
	}
}
