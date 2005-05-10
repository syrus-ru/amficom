/*
 * $Id: StorableObjectPool.java,v 1.78 2005/05/10 16:21:57 bass Exp $
 *
 * Copyright ø 2004 Syrus Systems.
 * Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“.
 * “œ≈À‘: ·ÌÊÈÎÔÌ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.io.LRUMapSaver;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.78 $, $Date: 2005/05/10 16:21:57 $
 * @author $Author: bass $
 * @module general_v1
 */
public abstract class StorableObjectPool {

	/**
	 * ‚·Í·Ó-symbol {@value}
	 */
	public static final String ‚·Í·Ó = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]"; //$NON-NLS-1$

	protected Map objectPoolMap;

	private short selfGroupCode;
	private String selfGroupName;

	protected Class cacheMapClass;

	private Map savingObjectsMap; // Map <Integer dependencyLevel, Map <Short entityCode, Set <StorableObject> levelEntitySavingObjects > >
	private Set savingObjectIds; // HashSet <Identifier>

	private Set deletedIds; // Set <Identifier>

	public StorableObjectPool(final int objectPoolMapSize, final short selfGroupCode) {
		this(objectPoolMapSize, selfGroupCode, LRUMap.class);
	}

	public StorableObjectPool(final int objectPoolMapSize, final short selfGroupCode, final Class cacheMapClass) {
		this.objectPoolMap = Collections.synchronizedMap(new HashMap(objectPoolMapSize));

		this.selfGroupCode = selfGroupCode;
		this.selfGroupName = ObjectGroupEntities.codeToString(this.selfGroupCode).replaceAll("Group$", ""); //$NON-NLS-1$ //$NON-NLS-2$

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
				throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass " //$NON-NLS-1$
						+ this.cacheMapClass.getName() + " must extend LRUMap"); //$NON-NLS-1$
		}
		catch (SecurityException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass " //$NON-NLS-1$
					+ this.cacheMapClass.getName() + " SecurityException " + e.getMessage()); //$NON-NLS-1$
		}
		catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass " //$NON-NLS-1$
					+ this.cacheMapClass.getName() + " IllegalArgumentException " + e.getMessage()); //$NON-NLS-1$
		}
		catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass " //$NON-NLS-1$
					+ this.cacheMapClass.getName() + " NoSuchMethodException " + e.getMessage()); //$NON-NLS-1$
		}
		catch (InstantiationException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass " //$NON-NLS-1$
					+ this.cacheMapClass.getName() + " InstantiationException " + e.getMessage()); //$NON-NLS-1$
		}
		catch (IllegalAccessException e) {
			throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass " //$NON-NLS-1$
					+ this.cacheMapClass.getName() + " IllegalAccessException " + e.getMessage()); //$NON-NLS-1$
		}
		catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			}
			else
				throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass " //$NON-NLS-1$
						+ this.cacheMapClass.getName() + " InvocationTargetException " + e.getMessage()); //$NON-NLS-1$
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
					+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '" //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode.shortValue()) + "' entity code: " + entityCode); //$NON-NLS-1$

		this.deletedIds.add(id);
		/* do not delete object immediatly, delete during flushing */
		// this.deleteStorableObject(id);
	}

	protected final void deleteImpl(final Set identifiables) {
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifier id = ((Identifiable) identifiableIterator.next()).getId();

			this.deletedIds.add(id);

			Short entityCode = new Short(id.getMajor());
			LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
			if (lruMap != null)
				lruMap.remove(id);
			else
				Log.errorMessage(this.selfGroupName
						+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '" //$NON-NLS-1$
						+ ObjectEntities.codeToString(entityCode.shortValue()) + "' entity code: " + entityCode); //$NON-NLS-1$
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
		Log.debugMessage("StorableObjectPool.flushDeleted | ########### deleted objects: " + this.deletedIds.size(), Log.DEBUGLEVEL09); //$NON-NLS-1$
		for (Iterator it = this.deletedIds.iterator(); it.hasNext();)
			Log.debugMessage("StorableObjectPool.flushDeleted | " + it.next(), Log.DEBUGLEVEL09); //$NON-NLS-1$

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
			Log.errorMessage(this.selfGroupName + "StorableObjectPool.flushImpl | Cannot find object pool for entity code: '" //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode) + "'"); //$NON-NLS-1$
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
				throw new IllegalDataException("dependency for object '" + id //$NON-NLS-1$
						+ "' neither Identifier nor StorableObject"); //$NON-NLS-1$

			if (dependencyObject != null)
				this.checkChangedWithDependencies(dependencyObject, dependencyLevel + 1);
		}

		if (storableObject.isChanged()) {
			Log.debugMessage("StorableObjectPool.checkChangedWithDependencies | Object '" + storableObject.getId() + "' is changed",  //$NON-NLS-1$//$NON-NLS-2$
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
						Log.errorMessage("Cannot find levelEntitySavingObjects for entity code " + entityCode //$NON-NLS-1$
								+ ", dependency level " + (-dependencyKey.intValue())); //$NON-NLS-1$

				}
			}
			else
				Log.errorMessage("Cannot find levelSavingMap for dependency level " + (-dependencyKey.intValue())); //$NON-NLS-1$

		}
	}

	private StorableObject getStorableObjectExt(Identifier id, boolean useLoader) throws ApplicationException {
		short groupCode = ObjectGroupEntities.getGroupCode(id.getMajor());
		if (groupCode == this.selfGroupCode)
			return this.getStorableObjectImpl(id, useLoader);

		return getStorableObjectOfGroup(id, useLoader, groupCode);
	}

	protected static StorableObject getStorableObjectOfGroup(Identifier id, boolean useLoader, short groupCode)
			throws ApplicationException {
		final String groupName = ObjectGroupEntities.codeToString(groupCode).replaceAll("Group$", ""); //$NON-NLS-1$ //$NON-NLS-2$
		String className = "com.syrus.AMFICOM." + groupName.toLowerCase() + "." + groupName + "StorableObjectPool"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		Class clazz;

		StorableObject storableObject = null;
		try {
			clazz = Class.forName(className);
			Method getStorableObjectMethod = clazz.getDeclaredMethod("getStorableObject", new Class[] {Identifier.class, boolean.class}); //$NON-NLS-1$
			storableObject = (StorableObject) getStorableObjectMethod.invoke(null, new Object[] {id,
					useLoader ? Boolean.TRUE : Boolean.FALSE});
		}
		catch (ClassNotFoundException e) {
			Log.errorException(e);
		}
		catch (SecurityException e) {
			Log.errorException(e);
		}
		catch (NoSuchMethodException e) {
			Log.errorException(e);
		}
		catch (IllegalArgumentException e) {
			Log.errorException(e);
		}
		catch (IllegalAccessException e) {
			Log.errorException(e);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof ApplicationException)
				throw (ApplicationException) cause;

			Log.errorException(e);
		}
		return storableObject;
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

		Log.errorMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectImpl | Cannot find object pool for objectId: '" //$NON-NLS-1$
				+ objectId + "' entity code: '" + ObjectEntities.codeToString(objectEntityCode) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
		for (final Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
			final Short entityCode = (Short) it.next();
			Log.debugMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectImpl | available " //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode) + " / " + entityCode, Log.DEBUGLEVEL05); //$NON-NLS-1$
		}
		return null;
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
		assert ids != null : "Supply an empty set instead"; //$NON-NLS-1$
		assert condition != null : "Supply EquivalentCondition instead"; //$NON-NLS-1$

		Short entityCode = condition.getEntityCode();
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);

		assert objectPool != null : "Cannot find object pool for entity code " + condition.getEntityCode() //$NON-NLS-1$
				+ ", entity: '" + ObjectEntities.codeToString(entityCode) + " , condition class:" + condition.getClass().getName(); //$NON-NLS-1$ //$NON-NLS-2$

		final Set soSet = new HashSet();
		for (Iterator it = objectPool.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			Identifier id = storableObject.getId();
			if (!ids.contains(id)
					&& !this.deletedIds.contains(id)
					&& condition.isConditionTrue(storableObject))
				soSet.add(storableObject);
		}

		Set loadedSet = null;

		if (useLoader && condition.isNeedMore(soSet)) {
			Set idsSet = new HashSet(soSet.size());
			for (Iterator iter = soSet.iterator(); iter.hasNext();) {
				StorableObject storableObject = (StorableObject) iter.next();
				idsSet.add(storableObject.getId());
			}

			idsSet.addAll(ids);

			/* do not load deleted object with entityCode */
			short code = condition.getEntityCode().shortValue();
			for (Iterator iter = this.deletedIds.iterator(); iter.hasNext();) {
				Identifier id = (Identifier) iter.next();
				if (id.getMajor() == code)
					idsSet.add(id);
			}
			
			/* logging */
			{
				StringBuffer buffer = new StringBuffer();
				for (Iterator it = idsSet.iterator(); it.hasNext();) {
					Identifier identifier = (Identifier) it.next();
					if (buffer.length() != 0)
						buffer.append(", "); //$NON-NLS-1$
					buffer.append(identifier.getIdentifierString());
				}
				Log.debugMessage(
					"StorableObjectPool.getStorableObjectsByConditionButIdsImpl | before loadStorableObjectsButIds : " + buffer.toString(), //$NON-NLS-1$
					Log.DEBUGLEVEL10);
			}

			loadedSet = this.loadStorableObjectsButIds(condition, idsSet);
			
			/* logging */
			{
				StringBuffer buffer = new StringBuffer();
				for (Iterator it = loadedSet.iterator(); it.hasNext();) {
					StorableObject storableObject = (StorableObject) it.next();
					if (buffer.length() != 0)
						buffer.append(", "); //$NON-NLS-1$
					buffer.append(storableObject.getId().getIdentifierString());
				}
				Log.debugMessage(
					"StorableObjectPool.getStorableObjectsByConditionButIdsImpl | loaded : " + buffer.toString(), //$NON-NLS-1$
					Log.DEBUGLEVEL10);
			}
		}

//		/*
//		 * This block is only needed in order for LRUMap to rehash itself.
//		 */
//		for (Iterator it = collection.iterator(); it.hasNext();) {
//			StorableObject storableObject = (StorableObject) it.next();
//			objectPool.get(storableObject);
//		}

		if (loadedSet != null) {
			for (Iterator it = loadedSet.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				objectPool.put(storableObject.getId(), storableObject);
			}
			soSet.addAll(loadedSet);
		}

		return soSet;
	}

	protected final Set getStorableObjectsByConditionImpl(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		return this.getStorableObjectsByConditionButIdsImpl(Collections.EMPTY_SET, condition, useLoader);
	}

	/**
	 * @param objectIds
	 *          set of pure java identifiers.
	 * @param useLoader
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @todo Check references within workspace, convert a run time warning (if
	 *       objectIds is null) into a run time error.
	 */
	protected final Set getStorableObjectsImpl(final Set objectIds, final boolean useLoader) throws ApplicationException {
		Set set = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (Iterator it = objectIds.iterator(); it.hasNext();) {
				Identifier objectId = (Identifier) it.next();

				/* do not operate with deleted objects */
				if (this.deletedIds.contains(objectId))
					continue;

				Short entityCode = new Short(objectId.getMajor());
				LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
				StorableObject storableObject = null;
				if (objectPool != null) {
					storableObject = (StorableObject) objectPool.get(objectId);
					if (storableObject != null) {
						if (set == null)
							set = new HashSet();
						set.add(storableObject);
					}
					if (storableObject == null && useLoader) {
						if (objectQueueMap == null)
							objectQueueMap = new HashMap();
						Set objectQueue = (Set) objectQueueMap.get(entityCode);
						if (objectQueue == null) {
							objectQueue = new HashSet();
							objectQueueMap.put(entityCode, objectQueue);
						}
						objectQueue.add(objectId);
					}
				}
				else {
					Log.errorMessage(this.selfGroupName
							+ "StorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '" //$NON-NLS-1$
							+ objectId.toString() + "', entity code: '" + ObjectEntities.codeToString(entityCode) + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}

		}
		else {
			Log.errorMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectsImpl | NULL set of identifiers supplied"); //$NON-NLS-1$
		}

		if (objectQueueMap != null) {
			if (set == null)
				set = new HashSet();
			for (Iterator it = objectQueueMap.keySet().iterator(); it.hasNext();) {
				final Short entityCode = (Short) it.next();
				Set objectQueue = (Set) objectQueueMap.get(entityCode);

				/* do not load deleted object with entityCode */
				for (Iterator iter = this.deletedIds.iterator(); iter.hasNext();) {
					Identifier id = (Identifier) iter.next();
					if (id.getMajor() == entityCode.shortValue())
						objectQueue.add(id);
				}

				Set storableObjects = this.loadStorableObjects(objectQueue);
				if (storableObjects != null) {
					try {
						for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
							StorableObject storableObject = (StorableObject) iter.next();
							this.putStorableObjectImpl(storableObject);
							set.add(storableObject);
						}
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}

		return set == null ? Collections.EMPTY_SET : set;
	}

	protected abstract Set loadStorableObjects(final Set ids) throws ApplicationException;

	protected abstract Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids)
			throws ApplicationException;

	/**
	 * @param storableObject
	 *            a non-null pure java storable object.
	 * @throws IllegalObjectEntityException
	 */
	protected final StorableObject putStorableObjectImpl(final StorableObject storableObject) throws IllegalObjectEntityException {
		// */
		assert storableObject != null;
		/*
		 * / if (storableObject == null) return null; //
		 */
		Identifier objectId = storableObject.getId();
		if (this.deletedIds.contains(objectId))
			return null;

		Short entityCode = new Short(objectId.getMajor());
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			return (StorableObject) objectPool.put(objectId, storableObject);
		}
		throw new IllegalObjectEntityException(this.selfGroupName
				+ "StorableObjectPool.putStorableObject | Illegal object entity: '" //$NON-NLS-1$
				+ ObjectEntities.codeToString(entityCode)
				+ "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE); //$NON-NLS-1$
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

	/**
	 * Refresh only unchanged storable objects.
	 * 
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected final void refreshImpl() throws ApplicationException {
		Log.debugMessage(this.selfGroupName + "StorableObjectPool.refreshImpl | trying to refresh Pool...", Log.DEBUGLEVEL03); //$NON-NLS-1$
		final Set storableObjects = new HashSet();
		final Set entityCodes = this.objectPoolMap.keySet();

		for (final Iterator it = entityCodes.iterator(); it.hasNext();) {
			final Short entityCode = (Short) it.next();
			final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);

			storableObjects.clear();
			for (final Iterator it2 = lruMap.iterator(); it2.hasNext();) {
				final StorableObject storableObject = (StorableObject) it2.next();
				if (!storableObject.isChanged())
					if (!this.deletedIds.contains(storableObject.getId()))
						storableObjects.add(storableObject);
			}
			if (storableObjects.isEmpty()) {
				Log.debugMessage(this.selfGroupName
						+ "StorableObjectPool.refreshImpl | LRUMap for '" //$NON-NLS-1$
						+ ObjectEntities.codeToString(entityCode) + "' entity has no elements", Log.DEBUGLEVEL08); //$NON-NLS-1$
				continue;
			}
			Log.debugMessage(this.selfGroupName
					+ "StorableObjectPool.refreshImpl | try refresh LRUMap for '" //$NON-NLS-1$
					+ ObjectEntities.codeToString(entityCode) + "' entity", Log.DEBUGLEVEL08); //$NON-NLS-1$

			final Set returnedStorableObjectsIds = this.refreshStorableObjects(storableObjects);
			if (returnedStorableObjectsIds.isEmpty())
				continue;

			Set loadedRefreshedObjects = this.loadStorableObjects(returnedStorableObjectsIds);
			for (Iterator iter = loadedRefreshedObjects.iterator(); iter.hasNext();) {
				try {
					this.putStorableObjectImpl((StorableObject) iter.next());
				}
				catch (IllegalObjectEntityException e) {
					Log.errorException(e);
				}
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
			Log.errorMessage(this.selfGroupName + "StorableObjectPool.deserializePoolImpl | Error: " + ae.getMessage()); //$NON-NLS-1$
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
			Log.errorMessage("StorableObjectPool.truncateObjectPoolImpl | ERROR: Object pool class '" + objectPool.getClass().getName() //$NON-NLS-1$
					+ "' not 'StorableObjectResizableLRUMap' -- cannot truncate pool");  //$NON-NLS-1$
	}
}
