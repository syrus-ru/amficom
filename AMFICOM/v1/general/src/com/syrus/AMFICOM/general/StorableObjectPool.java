/*
 * $Id: StorableObjectPool.java,v 1.82 2005/05/20 08:25:30 bass Exp $
 *
 * Copyright ø 2004 Syrus Systems.
 * Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“.
 * “œ≈À‘: ·ÌÊÈÎÔÌ.
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
 * @version $Revision: 1.82 $, $Date: 2005/05/20 08:25:30 $
 * @author $Author: bass $
 * @module general_v1
 */
public abstract class StorableObjectPool {

	/**
	 * ‚·Í·Ó-symbol {@value}
	 */
	public static final String ‚·Í·Ó = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	protected Map objectPoolMap;

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
		this.objectPoolMap = Collections.synchronizedMap(new HashMap(objectPoolMapSize));

		this.selfGroupCode = selfGroupCode;
		this.selfGroupName = ObjectGroupEntities.codeToString(this.selfGroupCode).replaceAll("Group$", "");

		this.cacheMapClass = cacheMapClass;

		this.savingObjectsMap = Collections.synchronizedMap(new HashMap());
		this.savingObjectIds = Collections.synchronizedSet(new HashSet());

		this.deletedIds = Collections.synchronizedSet(new HashSet());
	}

	protected void addObjectPool(	final short objectEntityCode, final int poolSize) {
		try {
			LRUMap objectPool = null;
			// LRUMap objectPool = new LRUMap(poolSize);
			Constructor constructor = this.cacheMapClass.getConstructor(new Class[] { int.class});
			Object obj = constructor.newInstance(new Object[] { new Integer(poolSize)});
			if (obj instanceof LRUMap) {
				objectPool = (LRUMap) obj;
				Short short1 = new Short(objectEntityCode);
				this.objectPoolMap.put(short1, objectPool);
//				 Log.debugMessage("StorableObjectPool.addObjectPool | pool for" + ObjectEntities.codeToString(short1.shortValue())
//						+ "/" + short1 + "(" + objectEntityCode + ") size " + poolSize + "added", Log.DEBUGLEVEL07);
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
		} catch (final InvocationTargetException ite) {
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

	protected final void cleanChangedStorableObjectsImpl() {
		synchronized (this.objectPoolMap) {
			for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
				Short entityCode = (Short) it.next();
				this.cleanChangedStorableObjectImpl(entityCode);
			}
		}
	}

	protected final void cleanChangedStorableObjectImpl(final Short entityCode) {
		this.cleanDeleted(entityCode.shortValue());

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

	protected final void deleteImpl(final Identifier id) {
		Short entityCode = new Short(id.getMajor());
		LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
		if (lruMap != null)
			lruMap.remove(id);
		else
			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '"
					+ ObjectEntities.codeToString(entityCode.shortValue()) + "' entity code: " + entityCode);

		this.deletedIds.add(id);
		/* do not delete object immediatly, delete during flushing */
		// this.deleteStorableObject(id);
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
			if (pool == null) {
				Log.debugMessage("StorableObjectPool.delete() | Unable to delete identifiables of group: "
						+ ObjectGroupEntities.codeToString(groupCode)
						+ '(' + groupCode
						+ ") since the corresponding pool is not registered",
						Log.SEVERE);
			} else {
				pool.deleteImpl(identifiables);
			}
		}
	}

	protected final void deleteImpl(final Set identifiables) {
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifier id = ((Identifiable) identifiableIterator.next()).getId();

			this.deletedIds.add(id);

			final Short entityCode = new Short(id.getMajor());
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

	protected abstract void deleteStorableObjects(final Set identifiables);

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
		Log.debugMessage("StorableObjectPool.flushDeleted | ########### deleted objects: " + this.deletedIds.size(), Log.DEBUGLEVEL09);
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
	 * This method is invoked only by this class' descendants
	 * from their
	 * <code>public static void flush</code>
	 * @param id
	 * @param force
	 * @throws ApplicationException
	 */
	protected final void flushImpl(final Identifier id, final boolean force) throws ApplicationException {
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
	protected final void flushImpl(final short entityCode, final boolean force) throws ApplicationException {
		this.flushImpl(new Short(entityCode), force);
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
	protected final void flushImpl(final Short entityCode, final boolean force) throws ApplicationException {
		/* delete objects ! */
		this.flushDeleted(entityCode.shortValue());

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
	protected final void flushImpl(final boolean force) throws ApplicationException {
		/* delete objects ! */
		this.flushDeleted();

		synchronized (this.savingObjectsMap) {
			this.prepareSavingObjectsMap();
			this.saveWithDependencies(force);
		}
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
	private void prepareSavingObjectsMap(final Short entityCode) throws ApplicationException {
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
			for (final Iterator entityCodeIterator = this.objectPoolMap.keySet().iterator(); entityCodeIterator.hasNext();) {
				final Short entityCode = (Short) entityCodeIterator.next();
				this.checkChangedWithDependenciesEntity(entityCode);
			}
		}
	}

	/**
	 * For the given entity searchs changed objects with dependencies
	 * @param entityCode
	 * @throws ApplicationException
	 */
	private void checkChangedWithDependenciesEntity(final Short entityCode) throws ApplicationException {
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			for (Iterator poolIterator = objectPool.iterator(); poolIterator.hasNext();)
				this.checkChangedWithDependencies((StorableObject) poolIterator.next(), 0);
		}
		else
			Log.errorMessage(this.selfGroupName + "StorableObjectPool.flushImpl | Cannot find object pool for entity code: '"
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
				dependencyObject = this.getStorableObjectExt((Identifier) object, false);
			else if (object instanceof StorableObject)
				dependencyObject = (StorableObject) object;
			else
				throw new IllegalDataException("dependency for object '" + id
						+ "' neither Identifier nor StorableObject");

			if (dependencyObject != null)
				this.checkChangedWithDependencies(dependencyObject, dependencyLevel + 1);
		}

		if (storableObject.isChanged()) {
			Log.debugMessage("StorableObjectPool.checkChangedWithDependencies | Object '" + storableObject.getId() + "' is changed",
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
						Log.errorMessage("Cannot find levelEntitySavingObjects for entity code " + entityCode
								+ ", dependency level " + (-dependencyKey.intValue()));

				}
			}
			else
				Log.errorMessage("Cannot find levelSavingMap for dependency level " + (-dependencyKey.intValue()));

		}
	}

	private StorableObject getStorableObjectExt(Identifier id, boolean useLoader) throws ApplicationException {
		assert id != null && !id.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		final short groupCode = ObjectGroupEntities.getGroupCode(id.getMajor());
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool == null)
			throw new ApplicationException("StorableObjectPool.getStorableObjectOfGroup() | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode)
					+ " is not initialized");
		return pool.getStorableObjectImpl(id, useLoader);
	}

	protected final StorableObject getStorableObjectImpl(final Identifier objectId, final boolean useLoader) throws ApplicationException {
		assert objectId != null: ErrorMessages.NON_NULL_EXPECTED;

		/*
		 * Do not load:
		 * a. anything if a void identifier is supplied;
		 * b. deleted objects.
		 */
		if (objectId.isVoid() || this.deletedIds.contains(objectId))
			return null;

		final short objectEntityCode = objectId.getMajor();
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(objectEntityCode));
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
		for (final Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
			final Short entityCode = (Short) it.next();
			Log.debugMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectImpl | available "
					+ ObjectEntities.codeToString(entityCode) + " / " + entityCode, Log.DEBUGLEVEL05);
		}
		return null;
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
		if (pool == null)
			throw new ApplicationException("StorableObjectPool.getStorableObjectsByConditionButIds() | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode)
					+ " is not initialized");
		return pool.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	/**
	 * @param ids
	 *            a non-null (use {@link Collections#EMPTY_SET} in case of
	 *            emergency) set of pure-java identifiers.
	 * @param condition
	 * @param useLoader
	 * @throws ApplicationException
	 */
	protected final Set getStorableObjectsByConditionButIdsImpl(final Set ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		assert ids != null: "Supply an empty set instead";
		assert condition != null: "Supply an EquivalentCondition instead";

		final Short entityCode = condition.getEntityCode();
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

	public static Set getStorableObjectsByCondition(
			final StorableObjectCondition condition,
			final boolean useLoader)
			throws ApplicationException {
		assert condition != null: ErrorMessages.NON_NULL_EXPECTED;
		final short groupCode = ObjectGroupEntities.getGroupCode(condition.getEntityCode());
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool == null)
			throw new ApplicationException("StorableObjectPool.getStorableObjectsByCondition() | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode)
					+ " is not initialized");
		return pool.getStorableObjectsByConditionImpl(condition,
				useLoader);
	}

	protected final Set getStorableObjectsByConditionImpl(
			final StorableObjectCondition condition,
			final boolean useLoader)
			throws ApplicationException {
		return this.getStorableObjectsByConditionButIdsImpl(Collections.EMPTY_SET, condition, useLoader);
	}

	public static Set getStorableObjects(final Set ids, boolean useLoader) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty())
			return Collections.EMPTY_SET;

		assert StorableObject.hasSingleGroupEntities(ids);
		final short groupCode = StorableObject.getGroupCodeOfIdentifiables(ids);
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool == null)
			throw new ApplicationException("StorableObjectPool.getStorableObjects() | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode)
					+ " is not initialized");
		return pool.getStorableObjectsImpl(ids, useLoader);
	}

	/**
	 * @param ids
	 *          set of pure java identifiers.
	 * @param useLoader
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected final Set getStorableObjectsImpl(final Set ids, final boolean useLoader) throws ApplicationException {
		assert ids != null;

		final Set storableObjects = new HashSet();
		Map objectQueueMap = null;

		for (final Iterator idIterator = ids.iterator(); idIterator.hasNext();) {
			final Identifier id = (Identifier) idIterator.next();

			/* do not operate with deleted objects */
			if (this.deletedIds.contains(id))
				continue;

			final Short entityCode = new Short(id.getMajor());
			final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
			if (objectPool != null) {
				final StorableObject storableObject = (StorableObject) objectPool.get(id);
				if (storableObject != null)
					storableObjects.add(storableObject);
				else if (useLoader) {
					/*
					 * Local object not found and useLoader
					 * is true.
					 */
					if (objectQueueMap == null)
						objectQueueMap = new HashMap();
					Set objectQueue = (Set) objectQueueMap.get(entityCode);
					if (objectQueue == null) {
						objectQueue = new HashSet();
						objectQueueMap.put(entityCode, objectQueue);
					}
					objectQueue.add(id);
				}
			} else
				Log.errorMessage(this.selfGroupName
						+ "StorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '"
						+ id + "', entity code: '" + ObjectEntities.codeToString(entityCode) + "'");
		}

		if (objectQueueMap != null) {
			for (final Iterator entityCodeIterator = objectQueueMap.keySet().iterator(); entityCodeIterator.hasNext();) {
				final Short entityCode = (Short) entityCodeIterator.next();
				final Set objectQueue = (Set) objectQueueMap.get(entityCode);

				try {
					for (final Iterator storableObjectIterator = this.loadStorableObjects(objectQueue).iterator(); storableObjectIterator.hasNext();) {
						StorableObject storableObject = (StorableObject) storableObjectIterator.next();
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

	protected abstract Set loadStorableObjects(final Set ids) throws ApplicationException;

	protected abstract Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids)
			throws ApplicationException;

	public static void putStorableObject(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;
		final short groupCode = ObjectGroupEntities.getGroupCode(storableObject.getId().getMajor());
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		final StorableObjectPool pool = (StorableObjectPool) GROUP_CODE_POOL_MAP.get(groupCode);
		if (pool == null)
			throw new IllegalObjectEntityException("StorableObjectPool.putStorableObject() | The pool for group: "
					+ ObjectGroupEntities.codeToString(groupCode)
					+ " is not initialized",
					IllegalObjectEntityException.OTHER_CODE);
		pool.putStorableObjectImpl(storableObject);
	}

	/**
	 * @param storableObject
	 *            a non-null pure java storable object.
	 * @throws IllegalObjectEntityException
	 */
	protected final void putStorableObjectImpl(final StorableObject storableObject) throws IllegalObjectEntityException {
		assert storableObject != null;

		final Identifier id = storableObject.getId();
		if (this.deletedIds.contains(id))
			return;

		final Short entityCode = new Short(id.getMajor());
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null)
			objectPool.put(id, storableObject);
		else
			throw new IllegalObjectEntityException(this.selfGroupName
					+ "StorableObjectPool.putStorableObject | Illegal object entity: '"
					+ ObjectEntities.codeToString(entityCode)
					+ "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
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
	protected final StorableObject fromTransferableImpl(Identifier id, IDLEntity transferable) throws ApplicationException {
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

	public static void refresh() throws ApplicationException {
		final RefreshProcedure refreshProcedure = new RefreshProcedure();
		if (!GROUP_CODE_POOL_MAP.forEachValue(refreshProcedure))
			throw refreshProcedure.applicationException;
	}

	/**
	 * Aborts execution at first <code>ApplicationException</code> caught.
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.82 $, $Date: 2005/05/20 08:25:30 $
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
	protected final void refreshImpl() throws ApplicationException {
		Log.debugMessage(this.selfGroupName + "StorableObjectPool.refreshImpl | trying to refresh Pool...", Log.DEBUGLEVEL03);
		final Set storableObjects = new HashSet();

		for (final Iterator entityCodeIterator = this.objectPoolMap.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);

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
				} catch (final IllegalObjectEntityException ioee) {
					Log.errorException(ioee);
				}
		}
	}

	protected abstract Set refreshStorableObjects(final Set storableObjects) throws ApplicationException;

	/**
	 * @param storableObjects
	 * @param force
	 * @throws ApplicationException
	 */
	protected abstract void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException;

	protected final void deserializePoolImpl() {
		try {
			for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
				Short objectEntityCode = (Short) it.next();
				Set keys = LRUMapSaver.load(ObjectEntities.codeToString(objectEntityCode));
				if (keys != null)
					this.getStorableObjectsImpl(keys, true);
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			Log.errorMessage(this.selfGroupName + "StorableObjectPool.deserializePoolImpl | Error: " + ae.getMessage());
		}
	}

	protected final void serializePoolImpl() {
		final Set entityCodeSet = this.objectPoolMap.keySet();
		for (final Iterator it = entityCodeSet.iterator(); it.hasNext();) {
			final Short entityCode = (Short) it.next();
			LRUMapSaver.save((LRUMap) this.objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode));
		}
	}

	protected final void truncateObjectPoolImpl(final short entityCode) {
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(entityCode));
		if (objectPool instanceof StorableObjectResizableLRUMap)
			((StorableObjectResizableLRUMap) objectPool).truncate(true);
		else
			Log.errorMessage("StorableObjectPool.truncateObjectPoolImpl | ERROR: Object pool class '" + objectPool.getClass().getName()
					+ "' not 'StorableObjectResizableLRUMap' -- cannot truncate pool");
	}

	protected static final void registerPool(final short groupCode, final StorableObjectPool pool) {
		assert ObjectGroupEntities.isGroupCodeValid(groupCode);
		GROUP_CODE_POOL_MAP.put(groupCode, pool);
	}
}
