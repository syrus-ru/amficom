/*
 * $Id: StorableObjectPool.java,v 1.85 2005/05/23 07:51:19 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.io.LRUMapSaver;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.85 $, $Date: 2005/05/23 07:51:19 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class StorableObjectPool {

	/**
	 * �����-symbol {@value}
	 */
	public static final String ����� = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	protected TShortObjectHashMap objectPoolMap;

	private short selfGroupCode;
	private String selfGroupName;

	protected Class cacheMapClass;

	private Map savingObjectsMap; // Map <Integer dependencyLevel, Map <Short entityCode, Set <StorableObject> levelEntitySavingObjects > >
	private Set savingObjectIds; // HashSet <Identifier>

	private Set deletedIds; // Set <Identifier>

	/**
	 * A "group code" -- "pool" map to store initialized pools.
	 */
	private static final TShortObjectHashMap GROUP_CODE_POOL_MAP = new TShortObjectHashMap();

	public StorableObjectPool(final int objectPoolMapSize, final short selfGroupCode) {
		this(objectPoolMapSize, selfGroupCode, LRUMap.class);
	}

	public StorableObjectPool(final int objectPoolMapSize, final short selfGroupCode, final Class cacheMapClass) {
		this.objectPoolMap = new TShortObjectHashMap(objectPoolMapSize);

		this.selfGroupCode = selfGroupCode;
		this.selfGroupName = ObjectGroupEntities.codeToString(this.selfGroupCode).replaceAll("Group$", "");
		registerPool(this.selfGroupCode, this);

		this.cacheMapClass = cacheMapClass;

		this.savingObjectsMap = Collections.synchronizedMap(new HashMap());
		this.savingObjectIds = Collections.synchronizedSet(new HashSet());

		this.deletedIds = Collections.synchronizedSet(new HashSet());
	}

	private static final void registerPool(final short groupCode, final StorableObjectPool pool) {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		GROUP_CODE_POOL_MAP.put(groupCode, pool);
	}

	protected void addObjectPool(	final short objectEntityCode, final int poolSize) {
		try {
			final Constructor constructor = this.cacheMapClass.getConstructor(new Class[] { int.class});
			final Object obj = constructor.newInstance(new Object[] { new Integer(poolSize)});
			if (obj instanceof LRUMap) {
				final LRUMap objectPool = (LRUMap) obj;
				this.objectPoolMap.put(objectEntityCode, objectPool);
				Log.debugMessage("StorableObjectPool.addObjectPool | Pool for '" + ObjectEntities.codeToString(objectEntityCode)
						+ "'/" + objectEntityCode + " of size " + poolSize + " added", Log.DEBUGLEVEL07);
			}
			else
				throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
						+ this.cacheMapClass.getName() + " must extend LRUMap");
		}
		catch (SecurityException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " SecurityException " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " IllegalArgumentException " + e.getMessage());
		}
		catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " NoSuchMethodException " + e.getMessage());
		}
		catch (InstantiationException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " InstantiationException " + e.getMessage());
		}
		catch (IllegalAccessException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
					+ this.cacheMapClass.getName() + " IllegalAccessException " + e.getMessage());
		}
		catch (final InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			}
			else
				throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
						+ this.cacheMapClass.getName() + " InvocationTargetException " + ite.getMessage());
		}

	}


	/*	Get */

	public static StorableObject getStorableObject(final Identifier id, final boolean useLoader) throws ApplicationException {
		assert id != null : ErrorMessages.NON_NULL_EXPECTED;
		assert !id.isVoid(): ErrorMessages.NON_VOID_EXPECTED;

		final short groupCode = ObjectGroupEntities.getGroupCode(id.getMajor());

		assert ObjectGroupEntities.isGroupCodeValid(groupCode);

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			return pool.getStorableObjectImpl(id, useLoader);

		throw new ApplicationException("StorableObjectPool.getStorableObject | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static Set getStorableObjects(final Set ids, boolean useLoader) throws ApplicationException {
		assert ids != null : ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty())
			return Collections.EMPTY_SET;

		assert StorableObject.hasSingleGroupEntities(ids);
		final short groupCode = StorableObject.getGroupCodeOfIdentifiables(ids);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			return pool.getStorableObjectsImpl(ids, useLoader);

		throw new ApplicationException("StorableObjectPool.getStorableObjects() | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode)
				+ " is not initialized");
	}

	public static Set getStorableObjectsByCondition(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		assert condition != null : ErrorMessages.NON_NULL_EXPECTED;
		final short groupCode = ObjectGroupEntities.getGroupCode(condition.getEntityCode());
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			return pool.getStorableObjectsByConditionImpl(condition, useLoader);

		throw new ApplicationException("StorableObjectPool.getStorableObjectsByCondition | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static Set getStorableObjectsByConditionButIds(final Set ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;

		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode);

		assert StorableObject.hasSingleTypeEntities(ids);
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);

		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			return pool.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);

		throw new ApplicationException("StorableObjectPool.getStorableObjectsByConditionButIds | The pool for group: "
				+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	private final StorableObject getStorableObjectImpl(final Identifier objectId, final boolean useLoader) throws ApplicationException {
		assert objectId != null: ErrorMessages.NON_NULL_EXPECTED;

		/*
		 * Do not load:
		 * a. anything if a void identifier is supplied;
		 * b. deleted objects.
		 */
		if (objectId.isVoid() || this.deletedIds.contains(objectId))
			return null;

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
					}
					catch (final IllegalObjectEntityException ioee) {
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
	private final Set getStorableObjectsImpl(final Set ids, final boolean useLoader) throws ApplicationException {
		assert ids != null;

		final Set storableObjects = new HashSet();
		TShortObjectHashMap objectQueueMap = null;

		for (final Iterator idIterator = ids.iterator(); idIterator.hasNext();) {
			final Identifier id = (Identifier) idIterator.next();

			/* do not operate with deleted objects */
			if (this.deletedIds.contains(id))
				continue;

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
						Set objectQueue = (Set) objectQueueMap.get(entityCode);
						if (objectQueue == null) {
							objectQueue = new HashSet();
							objectQueueMap.put(entityCode, objectQueue);
						}
						objectQueue.add(id);
					}
			}
			else
				Log.errorMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '"
						+ id + "', entity code: '" + ObjectEntities.codeToString(entityCode) + "'");
		}

		if (objectQueueMap != null) {
			for (final TShortObjectIterator entityCodeIterator = objectQueueMap.iterator(); entityCodeIterator.hasNext();) {
				entityCodeIterator.advance();
				final Set objectQueue = (Set) entityCodeIterator.value();

				try {
					for (final Iterator storableObjectIterator = this.loadStorableObjects(objectQueue).iterator(); storableObjectIterator.hasNext();) {
						StorableObject storableObject = (StorableObject) storableObjectIterator.next();
						this.putStorableObjectImpl(storableObject);
						storableObjects.add(storableObject);
					}
				}
				catch (final IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
				}
			}
		}

		return storableObjects == null ? Collections.EMPTY_SET : storableObjects;
	}

	private final Set getStorableObjectsByConditionImpl(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		return this.getStorableObjectsByConditionButIdsImpl(Collections.EMPTY_SET, condition, useLoader);
	}

	/**
	 * @param ids
	 *          a non-null (use {@link Collections#EMPTY_SET}in case of
	 *          emergency) set of pure-java identifiers.
	 * @param condition
	 * @param useLoader
	 * @throws ApplicationException
	 */
	private final Set getStorableObjectsByConditionButIdsImpl(final Set ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		assert ids != null: "Supply an empty set instead";
		assert condition != null: "Supply an EquivalentCondition instead";

		final short entityCode = condition.getEntityCode().shortValue();
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);

		assert objectPool != null: "Cannot find object pool for entity code " + entityCode
				+ ", entity: '" + ObjectEntities.codeToString(entityCode) + " , condition class:" + condition.getClass().getName();

		final Set storableObjects = new HashSet();
		for (final Iterator storableObjectIterator = objectPool.iterator(); storableObjectIterator.hasNext();) {
			final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
			final Identifier id = storableObject.getId();
			if (!ids.contains(id)
					&& !this.deletedIds.contains(id)
					&& condition.isConditionTrue(storableObject))
				storableObjects.add(storableObject);
		}

		Set loadedSet = null;

		if (useLoader && condition.isNeedMore(storableObjects)) {
			Set idsSet = new HashSet(storableObjects.size());
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				idsSet.add(storableObject.getId());
			}

			idsSet.addAll(ids);

			/* do not load deleted object with entityCode */
			final short code = condition.getEntityCode().shortValue();
			for (final Iterator idIterator = this.deletedIds.iterator(); idIterator.hasNext();) {
				final Identifier id = (Identifier) idIterator.next();
				if (id.getMajor() == code)
					idsSet.add(id);
			}
			
			/* logging */
			final StringBuffer buffer1 = new StringBuffer();
			for (final Iterator idIterator = idsSet.iterator(); idIterator.hasNext();) {
				final Identifier identifier = (Identifier) idIterator.next();
				if (buffer1.length() != 0)
					buffer1.append(", ");
				buffer1.append(identifier);
			}
			Log.debugMessage("StorableObjectPool.getStorableObjectsByConditionButIdsImpl | before loadStorableObjectsButIds : "
					+ buffer1,
					Log.DEBUGLEVEL10);

			loadedSet = this.loadStorableObjectsButIds(condition, idsSet);
			
			/* logging */
			final StringBuffer buffer2 = new StringBuffer();
			for (final Iterator storableObjectIterator = loadedSet.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				if (buffer2.length() != 0)
					buffer2.append(", ");
				buffer2.append(storableObject.getId());
			}
			Log.debugMessage("StorableObjectPool.getStorableObjectsByConditionButIdsImpl | loaded : "
					+ buffer2,
					Log.DEBUGLEVEL10);
		}

		/*
		 * This block is only needed in order for LRUMap to rehash
		 * itself. Since it affects performance, we've turned it off.
		 */
		if (false)
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();)
				objectPool.get(((StorableObject) storableObjectIterator.next()).getId());

		if (loadedSet != null) {
			for (final Iterator storableObjectIterator = loadedSet.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				objectPool.put(storableObject.getId(), storableObject);
			}
			storableObjects.addAll(loadedSet);
		}

		return storableObjects;
	}

	/*	Group-specific load-methods */
	protected abstract Set loadStorableObjects(final Set ids) throws ApplicationException;

	protected abstract Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids)
			throws ApplicationException;


	/*	Put */

	public static void putStorableObject(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;
		final short groupCode = ObjectGroupEntities.getGroupCode(storableObject.getId().getMajor());
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.putStorableObjectImpl(storableObject);
		else
			throw new IllegalObjectEntityException("StorableObjectPool.putStorableObject | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized",
					IllegalObjectEntityException.OTHER_CODE);
	}

	/**
	 * @param storableObject
	 *            a non-null pure java storable object.
	 * @throws IllegalObjectEntityException
	 */
	private final void putStorableObjectImpl(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;

		final Identifier id = storableObject.getId();
		if (this.deletedIds.contains(id))
			return;

		final short entityCode = id.getMajor();
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null)
			objectPool.put(id, storableObject);
		else
			throw new IllegalObjectEntityException(this.selfGroupName
					+ "StorableObjectPool.putStorableObject | Illegal object entity: '"
					+ ObjectEntities.codeToString(entityCode)
					+ "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}


	/*	Clean changed objects */

	public static void cleanChangedGroupStorableObjects(final short groupCode) {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.cleanChangedStorableObjectsImpl();
		else
			Log.errorMessage("StorableObjectPool.cleanChangedGroupStorableObjects | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static void cleanChangedStorableObjects(final short entityCode) {
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.cleanChangedStorableObjectsImpl(entityCode);
		else
			Log.errorMessage("StorableObjectPool.cleanChangedGroupStorableObjects | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	private final void cleanChangedStorableObjectsImpl() {
		synchronized (this.objectPoolMap) {
			for (final TShortObjectIterator iterator = this.objectPoolMap.iterator(); iterator.hasNext();) {
				iterator.advance();
				final short entityCode = iterator.key();
				this.cleanChangedStorableObjectsImpl(entityCode);
			}
		}
	}

	private final void cleanChangedStorableObjectsImpl(final short entityCode) {
		this.cleanDeleted(entityCode);

		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			for (Iterator poolIt = objectPool.iterator(); poolIt.hasNext();) {
				StorableObject storableObject = (StorableObject) poolIt.next();
				if (storableObject.isChanged())
					poolIt.remove();
			}
		}
	}

	private void cleanDeleted(final short entityCode) {
		synchronized (this.deletedIds) {
			for (Iterator it = this.deletedIds.iterator(); it.hasNext();) {
				final Identifier id = (Identifier) it.next();
				if (id.getMajor() == entityCode)
					it.remove();
			}
		}
	}


	/*	Delete */

	public static void delete(Identifier id) {
		final short entityCode = id.getMajor();
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);

		assert ObjectGroupEntities.isGroupCodeValid(groupCode);

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.deleteImpl(id);
		else
			Log.debugMessage("StorableObjectPool.delete | Unable to delete object: '" + id + "' of group "
					+ ObjectGroupEntities.codeToString(groupCode) + '(' + groupCode
					+ ") since the corresponding pool is not registered", Log.SEVERE);
	}

	/**
	 * @param identifiables
	 */
	public static void delete(final Set identifiables) {
		assert identifiables != null: ErrorMessages.NON_NULL_EXPECTED;
		/*
		 * Map<short, Set<Identifiable>>
		 */
		final TShortObjectHashMap groupCodeIdentifiablesMap = new TShortObjectHashMap();
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final short entityCode = identifiable.getId().getMajor();
			final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
			Set singleGroupIdentifiables = (Set) groupCodeIdentifiablesMap.get(groupCode);
			if (singleGroupIdentifiables == null) {
				singleGroupIdentifiables = new HashSet();
				groupCodeIdentifiablesMap.put(groupCode, singleGroupIdentifiables);
			}
			singleGroupIdentifiables.add(identifiable);
		}
		for (final TShortObjectIterator groupCodeIdentifiablesIterator = groupCodeIdentifiablesMap.iterator(); groupCodeIdentifiablesIterator.hasNext();) {
			groupCodeIdentifiablesIterator.advance();

			final short groupCode = groupCodeIdentifiablesIterator.key();
			final Set singleGroupIdentifiables = (Set) groupCodeIdentifiablesIterator.value();

			assert StorableObject.hasSingleGroupEntities(singleGroupIdentifiables);
			assert groupCode == StorableObject.getGroupCodeOfIdentifiables(singleGroupIdentifiables);
			assert ObjectGroupEntities.isGroupCodeValid(groupCode);

			final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
			/*-
			 * Extra braces added upon Bob's request. Keep them, and
			 * treat them with care.
			 *
			 * --
			 * Bass, 2005.05.20 AD.
			 */
			if (pool != null) {
				pool.deleteImpl(identifiables);
			}
			else {
				Log.debugMessage("StorableObjectPool.delete | Unable to delete identifiables of group: "
						+ ObjectGroupEntities.codeToString(groupCode) + '(' + groupCode
						+ ") since the corresponding pool is not registered", Log.SEVERE);
			}
		}
	}

	private final void deleteImpl(final Identifier id) {
		short entityCode = id.getMajor();
		LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
		if (lruMap != null)
			lruMap.remove(id);
		else
			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '"
					+ ObjectEntities.codeToString(entityCode) + "' entity code: " + entityCode);

		this.deletedIds.add(id);
		/* do not delete object immediatly, delete during flushing */
		// this.deleteStorableObject(id);
	}

	private final void deleteImpl(final Set identifiables) {
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifier id = ((Identifiable) identifiableIterator.next()).getId();

			this.deletedIds.add(id);

			final short entityCode = id.getMajor();
			final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
			if (lruMap != null)
				lruMap.remove(id);
			else
				Log.errorMessage(this.selfGroupName
						+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '"
						+ ObjectEntities.codeToString(entityCode) + "' entity code: " + entityCode);
		}

		/* do not delete object immediatly, delete during flushing */
		// this.deleteStorableObjects(objects);
	}

	/*	Group-specific method */
	protected abstract void deleteStorableObjects(final Set identifiables);


	/*	Flush */

	public static void flush(final Identifier id, final boolean force) throws ApplicationException {
		short entityCode = id.getMajor();
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);

		assert ObjectGroupEntities.isGroupCodeValid(groupCode);

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.flushImpl(id, force);
		else
			throw new ApplicationException("The pool for group: " + ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static void flush(final short entityCode, final boolean force) throws ApplicationException {		
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);

		assert ObjectGroupEntities.isGroupCodeValid(groupCode);

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.flushImpl(entityCode, force);
		else
			throw new ApplicationException("The pool for group: " + ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static void flush(final Short entityCode, final boolean force) throws ApplicationException {		
		flush(entityCode.shortValue(), force);
	}

	public static void flushGroup(final short groupCode, boolean force) throws ApplicationException {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			pool.flushImpl(force);
		else
			throw new ApplicationException("The pool for group: " + ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	public static void flushGroup(final Short groupCode, boolean force) throws ApplicationException {
		flushGroup(groupCode.shortValue(), force);
	}

	/**
	 * This method is invoked only by this class' descendants
	 * from their
	 * <code>public static void flush</code>
	 * @param id
	 * @param force
	 * @throws ApplicationException
	 */
	private final void flushImpl(final Identifier id, final boolean force) throws ApplicationException {
		if (this.deletedIds.contains(id))
			this.deleteStorableObjects(Collections.singleton(id));
		else {
			synchronized (this.savingObjectsMap) {
				this.prepareSavingObjectsMap(id);
				if (!this.savingObjectsMap.isEmpty())
					this.saveWithDependencies(force);
			}
		}
	}

	/**
	 * This method is invoked only by this class' descendants
	 * from their
	 * <code>public static void flush</code>
	 *
	 * @param entityCode
	 * @param force
	 * @throws ApplicationException
	 */
	private final void flushImpl(final short entityCode, final boolean force) throws ApplicationException {
		/* delete objects ! */
		this.flushDeleted(entityCode);

		synchronized (this.savingObjectsMap) {
			this.prepareSavingObjectsMap(entityCode);
			this.saveWithDependencies(force);
		}
		
	}

	/**
	 * This method is only invoked by this class' descendants, using their
	 * <code>public static void flush(boolean)</code> method.
	 *
	 * @param force
	 * @throws VersionCollisionException
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @throws IllegalDataException
	 */
	private final void flushImpl(final boolean force) throws ApplicationException {
		/* delete objects ! */
		this.flushDeleted();

		synchronized (this.savingObjectsMap) {
			this.prepareSavingObjectsMap();
			this.saveWithDependencies(force);
		}
	}

	/**
	 * Actually delete objects of the specified entity, scheduled for deletion
	 * @param entityCode
	 */
	private final void flushDeleted(final short entityCode) {
		Set entityDeletedIds = new HashSet();
		synchronized (this.deletedIds) {
			for (Iterator it = this.deletedIds.iterator(); it.hasNext();) {
				final Identifier id = (Identifier) it.next();
				if (id.getMajor() == entityCode) {
					entityDeletedIds.add(id);
					it.remove();
				}
			}
		}

		final int size = entityDeletedIds.size();
		switch (size) {
			case 0:
				return;
			default:
				this.deleteStorableObjects(entityDeletedIds);
		}
	}

	/**
	 * Actually delete objects, scheduled for deletion
	 */
	private final void flushDeleted() {
		for (Iterator it = this.deletedIds.iterator(); it.hasNext();)
			Log.debugMessage("StorableObjectPool.flushDeleted | " + it.next(), Log.DEBUGLEVEL09);

		final int size = this.deletedIds.size();
		switch (size) {
			case 0:
				return;
			default:
				this.deleteStorableObjects(this.deletedIds);
		}

		this.deletedIds.clear();
	}

	/**
	 * Prepares savingObjectsMap to save only one object of the given id.
	 * @param id
	 * @throws ApplicationException
	 */
	private void prepareSavingObjectsMap(final Identifier id) throws ApplicationException {
		/* Objects for which method  saveWithDependencies already invoked*/
		this.savingObjectIds.clear();

		/* Specially oredered objects to save with dependencies*/
		this.savingObjectsMap.clear();

		StorableObject storableObject = this.getStorableObjectImpl(id, false);
		if (storableObject != null)
			this.checkChangedWithDependencies(storableObject, 0);
	}

	/**
	 * Prepares savingObjectsMap to save objects of the specified entity
	 * @param entityCode
	 * @throws ApplicationException
	 */
	private void prepareSavingObjectsMap(final short entityCode) throws ApplicationException {
		/* Objects for which method  saveWithDependencies already invoked*/
		this.savingObjectIds.clear();

		/* Specially oredered objects to save with dependencies*/
		this.savingObjectsMap.clear();

		/* Prepare savingObjectsMap from objects of the given entity in pool */
		this.checkChangedWithDependenciesEntity(entityCode);
	}

	/**
	 * Prepares savingObjectsMap to save the entire pool
	 * @throws ApplicationException
	 */
	private void prepareSavingObjectsMap() throws ApplicationException {
		/* Objects for which method  saveWithDependencies already invoked*/
		this.savingObjectIds.clear();

		/* Specially oredered objects to save with dependencies*/
		this.savingObjectsMap.clear();

		/* Prepare savingObjectsMap from all objects in pool */
		synchronized (this.objectPoolMap) {
			for (final TShortObjectIterator entityCodeIterator = this.objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
				entityCodeIterator.advance();
				final short entityCode = entityCodeIterator.key();
				this.checkChangedWithDependenciesEntity(entityCode);
			}
		}
	}

	/**
	 * For the given entity searchs changed objects with dependencies
	 * @param entityCode
	 * @throws ApplicationException
	 */
	private void checkChangedWithDependenciesEntity(final short entityCode) throws ApplicationException {
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			for (Iterator poolIterator = objectPool.iterator(); poolIterator.hasNext();)
				this.checkChangedWithDependencies((StorableObject) poolIterator.next(), 0);
		}
		else
			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.checkChangedWithDependenciesEntity | Cannot find object pool for entity code: '"
					+ ObjectEntities.codeToString(entityCode) + "'");
	}

	/**
	 * Check the given Storable Object with it's dependencies for changes.
	 * Populate savingObjectsMap with objects need to save.
	 * @param storableObject
	 * @param dependencyLevel
	 * @throws ApplicationException
	 */
	private void checkChangedWithDependencies(StorableObject storableObject, int dependencyLevel) throws ApplicationException {
		Identifier id = storableObject.getId();
		if (this.savingObjectIds.contains(id))
			return;

		this.savingObjectIds.add(id);

		Set dependencies = storableObject.getDependencies();
		StorableObject dependencyObject = null;
		for (Iterator dIt = dependencies.iterator(); dIt.hasNext();) {
			Object object = dIt.next();
			// if (object == null)
			// continue;
			if (object instanceof Identifier)
				dependencyObject = getStorableObject((Identifier) object, false);
			else if (object instanceof StorableObject)
				dependencyObject = (StorableObject) object;
			else
				throw new IllegalDataException("dependency for object '" + id
						+ "' neither Identifier nor StorableObject");

			if (dependencyObject != null)
				this.checkChangedWithDependencies(dependencyObject, dependencyLevel + 1);
		}

		if (storableObject.isChanged()) {
			Log.debugMessage(this.selfGroupName
					+ "StorableObjectPool.checkChangedWithDependencies | Object '" + storableObject.getId() + "' is changed",
					Log.DEBUGLEVEL10);
			Integer dependencyKey = new Integer(-dependencyLevel);
			Map levelSavingObjectsMap = (Map) this.savingObjectsMap.get(dependencyKey);
			if (levelSavingObjectsMap == null) {
				levelSavingObjectsMap = new HashMap();
				this.savingObjectsMap.put(dependencyKey, levelSavingObjectsMap);
			}
			Short entityKey = new Short(storableObject.getId().getMajor());
			Set levelEntitySavingObjects = (Set) levelSavingObjectsMap.get(entityKey);
			if (levelEntitySavingObjects == null) {
				levelEntitySavingObjects = new HashSet();
				levelSavingObjectsMap.put(entityKey, levelEntitySavingObjects);
			}
			levelEntitySavingObjects.add(storableObject);
		}
	}

	/**
	 * Saves objects, populating savingObjectsMap
	 * @param force
	 * @throws ApplicationException
	 */
	private void saveWithDependencies(final boolean force) throws ApplicationException {
		/* Save objects in order from savingObjectsMap */
		Integer dependencyKey;
		Map levelSavingObjectsMap;
		Set levelEntitySavingObjects;
		for (Iterator levelIt = this.savingObjectsMap.keySet().iterator(); levelIt.hasNext();) {
			dependencyKey = (Integer) levelIt.next();
			levelSavingObjectsMap = (Map) this.savingObjectsMap.get(dependencyKey);

			if (levelSavingObjectsMap != null) {
				for (Iterator entityIt = levelSavingObjectsMap.keySet().iterator(); entityIt.hasNext();) {
					final Short entityCode = (Short) entityIt.next();
					levelEntitySavingObjects = (Set) levelSavingObjectsMap.get(entityCode);

					if (levelEntitySavingObjects != null)
						this.saveStorableObjects(levelEntitySavingObjects, force);
					else
						Log.errorMessage(this.selfGroupName
								+ "StorableObjectPool.saveWithDependencies | Cannot find levelEntitySavingObjects for entity code " + entityCode
								+ ", dependency level " + (-dependencyKey.intValue()));

				}
			}
			else
				Log.errorMessage("Cannot find levelSavingMap for dependency level " + (-dependencyKey.intValue()));

		}
	}

	/*	Group specific method */
	/**
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 */
	protected abstract void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException;


	/*	From transferable*/

	public static StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		assert id != null : ErrorMessages.NON_NULL_EXPECTED;
		assert !id.isVoid(): ErrorMessages.NON_VOID_EXPECTED;

		final short groupCode = ObjectGroupEntities.getGroupCode(id.getMajor());

		assert ObjectGroupEntities.isGroupCodeValid(groupCode);

		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool != null)
			return pool.fromTransferableImpl(id, transferable);

		throw new ApplicationException("The pool for group: " + ObjectGroupEntities.codeToString(groupCode) + " is not initialized");
	}

	/**
	 * Get Storable Object from pool for id,
	 * update it's fields from transferable
	 * and return it.
	 * If not found in pool -- return null
	 * @param id
	 * @param transferable
	 * @throws ApplicationException
	 */
	private final StorableObject fromTransferableImpl(Identifier id, IDLEntity transferable) throws ApplicationException {
		StorableObject storableObject = null;
		try {
			storableObject = this.getStorableObjectImpl(id, false);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		if (storableObject != null)
			storableObject.fromTransferable(transferable);
		
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
	 * @version $Revision: 1.85 $, $Date: 2005/05/23 07:51:19 $
	 * @module general_v1
	 */
	private static final class RefreshProcedure implements TObjectProcedure {
		ApplicationException applicationException;

		public boolean execute(final Object object) {
			try {
				((StorableObjectPool) object).refreshImpl();
				return true;
			}
			catch (final ApplicationException ae) {
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
		final Set storableObjects = new HashSet();

		for (final TShortObjectIterator entityCodeIterator = this.objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			final LRUMap lruMap = (LRUMap) entityCodeIterator.value();

			storableObjects.clear();
			for (final Iterator storableObjectIterator = lruMap.iterator(); storableObjectIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				/*
				 * Not changed && not deleted.
				 */
				if (!storableObject.isChanged() && !this.deletedIds.contains(storableObject.getId()))
					storableObjects.add(storableObject);
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

			final Set returnedStorableObjectsIds = this.refreshStorableObjects(storableObjects);
			if (returnedStorableObjectsIds.isEmpty())
				continue;

			for (final Iterator storableObjectIterator = this.loadStorableObjects(returnedStorableObjectsIds).iterator(); storableObjectIterator.hasNext();)
				try {
					this.putStorableObjectImpl((StorableObject) storableObjectIterator.next());
				}
				catch (final IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
				}
		}
	}

	/*	Group specific method */
	protected abstract Set refreshStorableObjects(final Set storableObjects) throws ApplicationException;


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
		try {
			for (final TShortObjectIterator entityCodeIterator = this.objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
				entityCodeIterator.advance();
				final short entityCode = entityCodeIterator.key();
				Set keys = LRUMapSaver.load(ObjectEntities.codeToString(entityCode));
				if (keys != null)
					this.getStorableObjectsImpl(keys, true);
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			Log.errorMessage(this.selfGroupName + "StorableObjectPool.deserializeImpl | Error: " + ae.getMessage());
		}
	}

	private final void serializeImpl() {
		for (final TShortObjectIterator entityCodeIterator = this.objectPoolMap.iterator(); entityCodeIterator.hasNext();) {
			entityCodeIterator.advance();
			final short entityCode = entityCodeIterator.key();
			LRUMapSaver.save((LRUMap) this.objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode));
		}
	}


	/*	Truncate */

	public static void truncate(final short entityCode) {
		final short groupCode = ObjectGroupEntities.getGroupCode(entityCode);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
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
