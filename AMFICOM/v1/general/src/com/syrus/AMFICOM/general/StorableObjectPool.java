/*
 * $Id: StorableObjectPool.java,v 1.48 2005/03/28 16:48:06 bob Exp $
 *
 * Copyright ø 2004 Syrus Systems.
 * Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“.
 * “œ≈À‘: ·ÌÊÈÎÔÌ.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.io.LRUMapSaver;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.48 $, $Date: 2005/03/28 16:48:06 $
 * @author $Author: bob $
 * @module general_v1
 */
public abstract class StorableObjectPool {

	/**
	 * ‚·Í·Ó-symbol {@value}
	 */
	public static final String ‚·Í·Ó = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	protected Class cacheMapClass;

	protected Map objectPoolMap;

	private short selfGroupCode;
	private String selfGroupName;

	private Map savingObjectsMap; // Map <Integer dependencyLevel, Map <Short entityCode, Collection <StorableObject> levelEntitySavingObjects > >
	private HashSet savingObjectIds; // HashSet <Identifier>

	private Collection deletedIds; // Collection <Identifier>

	public StorableObjectPool(short selfGroupCode) {
		this(selfGroupCode, LRUMap.class);
	}

	public StorableObjectPool(short selfGroupCode, final Class cacheMapClass) {
		this.selfGroupCode = selfGroupCode;
		this.selfGroupName = ObjectGroupEntities.codeToString(this.selfGroupCode).replaceAll("Group$", "");
		this.cacheMapClass = cacheMapClass;
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
				throw new UnsupportedOperationException(this.selfGroupName + "StorableObjectPool.addObjectPool | CacheMapClass "
						+ this.cacheMapClass.getName() + " InvocationTargetException " + e.getMessage());
		}

	}

	protected void cleanChangedStorableObjectImpl(final Short entityCode) {
		short code = entityCode.shortValue();
		if (this.deletedIds != null) {
			for (Iterator it = this.deletedIds.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				if (id.getMajor() == code)
					it.remove();
			}
		}
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			for (Iterator poolIt = objectPool.iterator(); poolIt.hasNext();) {
				StorableObject storableObject = (StorableObject) poolIt.next();
				if (storableObject.isChanged())
					poolIt.remove();
			}
		}
	}

	protected void cleanChangedStorableObjectsImpl() {
		for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			this.cleanChangedStorableObjectImpl(entityCode);
		}
	}

	protected synchronized void deleteImpl(final Identifier id) {
		Short entityCode = new Short(id.getMajor());
		LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
		if (lruMap != null)
			lruMap.remove(id);
		else
			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '"
					+ ObjectEntities.codeToString(entityCode.shortValue()) + "' entity code: " + entityCode);

		if (this.deletedIds == null)
			this.deletedIds = Collections.synchronizedCollection(new LinkedList());

		this.deletedIds.add(id);
		/* do not delete object immediatly, delete during flushing */
		// this.deleteStorableObject(id);
	}

	protected synchronized void deleteImpl(final Collection objects) throws IllegalDataException {
		Object object;
		Identifier id;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			object = it.next();
			if (object instanceof Identifier)
				id = (Identifier) object;
			else if (object instanceof Identifiable)
				id = ((Identifiable) object).getId();
			else
				throw new IllegalDataException(this.selfGroupName + "StorableObjectPool.deleteImpl | Object "
						+ object.getClass().getName() + " isn't Identifier or Identifiable");

			if (this.deletedIds == null)
				this.deletedIds = Collections.synchronizedCollection(new LinkedList());

			this.deletedIds.add(id);

			Short entityCode = new Short(id.getMajor());
			LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
			if (lruMap != null)
				lruMap.remove(id);
			else
				Log.errorMessage(this.selfGroupName
						+ "StorableObjectPool.deleteImpl | Cannot find object pool for entity '"
						+ ObjectEntities.codeToString(entityCode.shortValue()) + "' entity code: " + entityCode);
		}

		/* do not delete object immediatly, delete during flushing */
		// this.deleteStorableObjects(objects);
	}

	protected abstract void deleteStorableObject(final Identifier id) throws IllegalDataException;

	protected abstract void deleteStorableObjects(final Collection objects) throws IllegalDataException;

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
	protected synchronized void flushImpl(final boolean force) throws ApplicationException {

		/* delete objects ! */
		if (this.deletedIds != null) {
			if (this.deletedIds.size() == 1)
				this.deleteStorableObject((Identifier) this.deletedIds.iterator().next());
			else
				this.deleteStorableObjects(this.deletedIds);

			this.deletedIds.clear();
		}

		/* save changed objects with dependencies */
		if (this.savingObjectIds != null)
			this.savingObjectIds.clear();
		else
			this.savingObjectIds = new HashSet();
		if (this.savingObjectsMap != null)
			this.savingObjectsMap.clear();
		else
			this.savingObjectsMap = new HashMap();

		/* Prepare savingObjectsMap from all objects in pool */
		for (final Iterator entityCodeIterator = this.objectPoolMap.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
			if (objectPool != null) {
				for (Iterator poolIterator = objectPool.iterator(); poolIterator.hasNext();)
					this.saveWithDependencies((StorableObject) poolIterator.next(), 0);
			} else
				Log.errorMessage(this.selfGroupName
						+ "StorableObjectPool.flushImpl | Cannot find object pool for entity code: '"
						+ ObjectEntities.codeToString(entityCode) + "'");
		}

		/* Save objects in order from savingObjectsMap */
		Integer dependencyKey;
		Short entityKey;
		Map levelSavingObjectsMap;
		Collection levelEntitySavingObjects;
		for (Iterator levelIt = this.savingObjectsMap.keySet().iterator(); levelIt.hasNext();) {
			dependencyKey = (Integer) levelIt.next();
			levelSavingObjectsMap = (Map) this.savingObjectsMap.get(dependencyKey);

			if (levelSavingObjectsMap != null) {
				for (Iterator entityIt = levelSavingObjectsMap.keySet().iterator(); entityIt.hasNext();) {
					entityKey = (Short) entityIt.next();
					levelEntitySavingObjects = (Collection) levelSavingObjectsMap.get(entityKey);

					if (levelEntitySavingObjects != null)
						this.saveStorableObjects(entityKey.shortValue(), levelEntitySavingObjects, force);
					else
						Log.errorMessage("Cannot find levelEntitySavingObjects for entity code " + entityKey
								+ ", dependency level " + (-dependencyKey.intValue()));

				}
			} else
				Log.errorMessage("Cannot find levelSavingMap for dependency level " + (-dependencyKey.intValue()));

		}
	}

	private void saveWithDependencies(	StorableObject storableObject,
										int dependencyLevel) throws ApplicationException {
		Identifier id = storableObject.getId();
		if (this.savingObjectIds.contains(id))
			return;

		this.savingObjectIds.add(id);

		Collection dependencies = storableObject.getDependencies();
		StorableObject dependencyObject = null;
		for (Iterator dIt = dependencies.iterator(); dIt.hasNext();) {
			Object object = dIt.next();
			// if (object == null)
			// continue;
			if (object instanceof Identifier)
				dependencyObject = this.getStorableObjectExt((Identifier) object);
			else if (object instanceof StorableObject)
				dependencyObject = (StorableObject) object;
			else
				throw new IllegalDataException("dependency for object '" + id
						+ "' neither Identifier nor StorableObject");

			if (dependencyObject != null)
				this.saveWithDependencies(dependencyObject, dependencyLevel + 1);
		}

		if (storableObject.isChanged()) {
			Integer dependencyKey = new Integer(-dependencyLevel);
			Map levelSavingObjectsMap = (Map) this.savingObjectsMap.get(dependencyKey);
			if (levelSavingObjectsMap == null) {
				levelSavingObjectsMap = new HashMap();
				this.savingObjectsMap.put(dependencyKey, levelSavingObjectsMap);
			}
			Short entityKey = new Short(storableObject.getId().getMajor());
			Collection levelEntitySavingObjects = (Collection) levelSavingObjectsMap.get(entityKey);
			if (levelEntitySavingObjects == null) {
				levelEntitySavingObjects = new HashSet();
				levelSavingObjectsMap.put(entityKey, levelEntitySavingObjects);
			}
			levelEntitySavingObjects.add(storableObject);
		}
	}

	private StorableObject getStorableObjectExt(Identifier id) throws ApplicationException {
		short groupCode = ObjectGroupEntities.getGroupCode(id.getMajor());
		if (groupCode == this.selfGroupCode)
			return this.getStorableObjectImpl(id, false);

		return getStorableObjectOfGroup(id, false, groupCode);
	}

	protected static StorableObject getStorableObjectOfGroup(	Identifier id,
																boolean useLoader,
																short groupCode) throws ApplicationException {
		final String groupName = ObjectGroupEntities.codeToString(groupCode).replaceAll("Group$", "");
		String className = "com.syrus.AMFICOM." + groupName.toLowerCase() + "." + groupName + "StorableObjectPool";
		Class clazz;

		StorableObject storableObject = null;
		try {
			clazz = Class.forName(className);
			Method getStorableObjectMethod = clazz.getDeclaredMethod("getStorableObject", new Class[] {
					Identifier.class, boolean.class});
			storableObject = (StorableObject) getStorableObjectMethod.invoke(null, new Object[] { id,
					useLoader ? Boolean.TRUE : Boolean.FALSE});
		} catch (ClassNotFoundException e) {
			Log.errorException(e);
		} catch (SecurityException e) {
			Log.errorException(e);
		} catch (NoSuchMethodException e) {
			Log.errorException(e);
		} catch (IllegalArgumentException e) {
			Log.errorException(e);
		} catch (IllegalAccessException e) {
			Log.errorException(e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof ApplicationException)
				throw (ApplicationException) cause;

			Log.errorException(e);
		}
		return storableObject;
	}

	protected StorableObject getStorableObjectImpl(	final Identifier objectId,
													final boolean useLoader) throws ApplicationException {
		if (objectId != null) {
			/* do not load deleted objects */
			if (this.deletedIds != null && this.deletedIds.contains(objectId)) { return null; }

			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject) objectPool.get(objectId);
				if (storableObject == null && useLoader) {
					storableObject = this.loadStorableObject(objectId);
					if (storableObject != null)
						try {
							this.putStorableObjectImpl(storableObject);
						} catch (IllegalObjectEntityException ioee) {
							Log.errorException(ioee);
						}
				}
				return storableObject;
			}

			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.getStorableObjectImpl | Cannot find object pool for objectId: '"
					+ objectId.toString() + "' entity code: '" + ObjectEntities.codeToString(objectEntityCode) + "'");
			for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
				final Short entityCode = (Short) it.next();
				Log.debugMessage(this.selfGroupName + "StorableObjectPool.getStorableObjectImpl | available "
						+ ObjectEntities.codeToString(entityCode) + " / " + entityCode, Log.DEBUGLEVEL05);
			}
		} else {
			Log
					.errorMessage(this.selfGroupName
							+ "StorableObjectPool.getStorableObjectImpl | NULL identifier supplied");
		}
		return null;
	}

	/**
	 * @param ids
	 *            a non-null (use {@link Collections#EMPTY_LIST} in case of
	 *            emergency) list of pure-java identifiers.
	 * @param condition
	 * @param useLoader
	 * @throws ApplicationException
	 */
	protected Collection getStorableObjectsByConditionButIdsImpl(	final Collection ids,
																	final StorableObjectCondition condition,
																	final boolean useLoader)
			throws ApplicationException {
		assert ids != null : "Supply an empty list instead";
		assert condition != null : "Supply EquivalentCondition instead";

		Collection collection = null;
		Short entityCode = condition.getEntityCode();
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);

		assert objectPool != null : "Cannot find object pool for entity code " + condition.getEntityCode()
				+ ", entity: '" + ObjectEntities.codeToString(entityCode) + " , condition class:" + condition.getClass().getName();

		collection = new LinkedList();
		for (Iterator it = objectPool.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			Identifier id = storableObject.getId();
			if (!ids.contains(id)
					&& (this.deletedIds == null || !this.deletedIds.contains(id))
					&& condition.isConditionTrue(storableObject))
				collection.add(storableObject);
		}

		Collection loadedList = null;

		if (useLoader && condition.isNeedMore(collection)) {
			List idsList = new ArrayList(collection.size());
			for (Iterator iter = collection.iterator(); iter.hasNext();) {
				StorableObject storableObject = (StorableObject) iter.next();
				idsList.add(storableObject.getId());
			}

			idsList.addAll(ids);

			/* do not load delete object too */
			if (this.deletedIds != null) {
				/* do not load deleted object with entityCode */
				short code = condition.getEntityCode().shortValue();
				for (Iterator iter = this.deletedIds.iterator(); iter.hasNext();) {
					Identifier id = (Identifier) iter.next();
					if (id.getMajor() == code)
						idsList.add(id);
				}
			}

			loadedList = this.loadStorableObjectsButIds(condition, idsList);
		}

//		/*
//		 * This block is only needed in order for LRUMap to rehash itself.
//		 */
//		for (Iterator it = collection.iterator(); it.hasNext();) {
//			StorableObject storableObject = (StorableObject) it.next();
//			objectPool.get(storableObject);
//		}

		if (loadedList != null) {
			for (Iterator it = loadedList.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				objectPool.put(storableObject.getId(), storableObject);
			}
			collection.addAll(loadedList);
		}

		if (collection == null)
			collection = Collections.EMPTY_LIST;

		return collection;
	}

	protected Collection getStorableObjectsByConditionImpl(	final StorableObjectCondition condition,
															final boolean useLoader) throws ApplicationException {
		return this.getStorableObjectsByConditionButIdsImpl(Collections.EMPTY_LIST, condition, useLoader);
	}

	/**
	 * @param objectIds
	 *            list of pure java identifiers.
	 * @param useLoader
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @todo Check references within workspace, convert a run time warning (if
	 *       objectIds is null) into a run time error.
	 */
	protected Collection getStorableObjectsImpl(final Collection objectIds,
												final boolean useLoader) throws ApplicationException {
		List list = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (Iterator it = objectIds.iterator(); it.hasNext();) {
				Identifier objectId = (Identifier) it.next();

				/* do not operate with deleted objects */
				if (this.deletedIds != null && this.deletedIds.contains(objectId))
					continue;

				Short entityCode = new Short(objectId.getMajor());
				LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
				StorableObject storableObject = null;
				if (objectPool != null) {
					storableObject = (StorableObject) objectPool.get(objectId);
					if (storableObject != null) {
						if (list == null)
							list = new LinkedList();
						list.add(storableObject);
					}
					if (storableObject == null && useLoader) {
						if (objectQueueMap == null)
							objectQueueMap = new HashMap();
						List objectQueue = (List) objectQueueMap.get(entityCode);
						if (objectQueue == null) {
							objectQueue = new LinkedList();
							objectQueueMap.put(entityCode, objectQueue);
						}
						objectQueue.add(objectId);
					}
				} else {
					Log
							.errorMessage(this.selfGroupName
									+ "StorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '"
									+ objectId.toString() + "', entity code: '"
									+ ObjectEntities.codeToString(entityCode) + "'");
				}
			}

		} else {
			Log.errorMessage(this.selfGroupName
					+ "StorableObjectPool.getStorableObjectsImpl | NULL list of identifiers supplied");
		}

		if (objectQueueMap != null) {
			if (list == null)
				list = new LinkedList();
			for (Iterator it = objectQueueMap.keySet().iterator(); it.hasNext();) {
				Short entityCode = (Short) it.next();
				short code = entityCode.shortValue();
				List objectQueue = (List) objectQueueMap.get(entityCode);
				if (this.deletedIds != null) {
					/* do not load deleted object with entityCode */
					for (Iterator iter = this.deletedIds.iterator(); iter.hasNext();) {
						Identifier id = (Identifier) iter.next();
						if (id.getMajor() == code)
							objectQueue.add(id);
					}
				}
				Collection storableObjects = this.loadStorableObjects(entityCode, objectQueue);
				if (storableObjects != null) {
					try {
						for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
							StorableObject storableObject = (StorableObject) iter.next();
							this.putStorableObjectImpl(storableObject);
							list.add(storableObject);
						}
					} catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}

		return list == null ? Collections.EMPTY_LIST : list;
	}

	protected abstract StorableObject loadStorableObject(final Identifier objectId) throws ApplicationException;

	protected abstract Collection loadStorableObjects(	final Short entityCode,
														final Collection ids) throws ApplicationException;

	protected abstract Collection loadStorableObjectsButIds(final StorableObjectCondition condition,
															final Collection ids) throws ApplicationException;

	protected void populatePools() {
		try {
			for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
				Short objectEntityCode = (Short) it.next();
				List keys = LRUMapSaver.load(ObjectEntities.codeToString(objectEntityCode));
				if (keys != null)
					getStorableObjectsImpl(keys, true);
			}
		} catch (ApplicationException ae) {
			Log.errorException(ae);
			Log.errorMessage(this.selfGroupName + "StorableObjectPool.populatePools | Error: " + ae.getMessage());
		}
	}

	/**
	 * @param storableObject
	 *            a non-null pure java storable object.
	 * @throws IllegalObjectEntityException
	 */
	protected StorableObject putStorableObjectImpl(final StorableObject storableObject)
			throws IllegalObjectEntityException {
		// */
		assert storableObject != null;
		/*
		 * / if (storableObject == null) return null; //
		 */
		Identifier objectId = storableObject.getId();
		if (this.deletedIds != null && this.deletedIds.contains(objectId))
			return null;
		Short entityCode = new Short(objectId.getMajor());
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) { return (StorableObject) objectPool.put(objectId, storableObject); }
		throw new IllegalObjectEntityException(this.selfGroupName
				+ "StorableObjectPool.putStorableObject | Illegal object entity: '"
				+ ObjectEntities.codeToString(entityCode) + "'",
												IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	/**
	 * Refresh only unchanged storable objects.
	 * 
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected void refreshImpl() throws ApplicationException {
		Log.debugMessage(this.selfGroupName + "StorableObjectPool.refreshImpl | trying to refresh Pool...",
			Log.DEBUGLEVEL03);
		final Set storableObjects = new HashSet();
		final Set entityCodes = this.objectPoolMap.keySet();

		for (final Iterator it = entityCodes.iterator(); it.hasNext();) {
			final Short entityCode = (Short) it.next();
			final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);

			for (final Iterator it2 = lruMap.iterator(); it2.hasNext();) {
				final StorableObject storableObject = (StorableObject) it2.next();
				if (!storableObject.isChanged())
					if (this.deletedIds == null || !this.deletedIds.contains(storableObject.getId()))
						storableObjects.add(storableObject);
			}
			if (storableObjects.isEmpty()) {
				Log.debugMessage(this.selfGroupName + "StorableObjectPool.refreshImpl | LRUMap for '"
						+ ObjectEntities.codeToString(entityCode) + "' entity has no elements", Log.DEBUGLEVEL08);
				continue;
			}
			Log.debugMessage(this.selfGroupName + "StorableObjectPool.refreshImpl | try refresh LRUMap for '"
					+ ObjectEntities.codeToString(entityCode) + "' entity", Log.DEBUGLEVEL08);

			final Set returnedStorableObjectsIds = this.refreshStorableObjects(storableObjects);
			Collection loadedRefreshedObjects = this.loadStorableObjects(entityCode, returnedStorableObjectsIds);
			for (Iterator iter = loadedRefreshedObjects.iterator(); iter.hasNext();) {
				try {
					this.putStorableObjectImpl((StorableObject) iter.next());
				} catch (IllegalObjectEntityException e) {
					Log.errorException(e);
				}
			}

		}
	}

	protected abstract Set refreshStorableObjects(final Set storableObjects) throws ApplicationException;

	/**
	 * This method should only be invoked during assertion evaluation, and never
	 * in a release system.
	 * 
	 * @param storableObjects
	 *            non-null list of pure java storable objects (empty list is
	 *            ok).
	 * @return <code>true</code> if all entities within this list are of the
	 *         same type, <code>false</code> otherwise.
	 */
	private boolean hasSingleTypeEntities(final Collection storableObjects) {
		/*
		 * Nested assertions are ok.
		 */
		assert storableObjects != null;

		if (storableObjects.size() == 0)
			return true;

		final Iterator storableObjectIterator = storableObjects.iterator();
		final short entityCode = ((StorableObject) storableObjectIterator.next()).getId().getMajor();
		while (storableObjectIterator.hasNext())
			if (entityCode != ((StorableObject) storableObjectIterator.next()).getId().getMajor())
				return false;
		return true;
	}

	/**
	 * Code that invokes this method, should preliminarily call
	 * {@link #hasSingleTypeEntities(Collection)} with the same parameter and
	 * ensure that return value is <code>true</code>, e.g.:
	 * 
	 * <pre>
	 * assert hasSingleTypeEntities(storableObjects) : &quot;Storable objects of different type are saved separately...&quot;;
	 * </pre>
	 * 
	 * @param storableObjects
	 *            non-null, non-empty list of pure java storable objects of the
	 *            same type.
	 * @return common type of storable objects supplied as <code>short</code>.
	 */
	protected short getEntityCodeOfStorableObjects(final Collection storableObjects) {
		assert storableObjects.size() >= 1;

		return ((StorableObject) storableObjects.iterator().next()).getId().getMajor();
	}

	/**
	 * @param entityCode
	 * @param storableObjects
	 * @param force
	 * @throws VersionCollisionException
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @throws IllegalDataException
	 * @todo Change signature of this method to the one without
	 *       <code>entityCode</code>, rewrite overriding classes in order for
	 *       them to use {@link #getEntityCodeOfStorableObjects(List)}.
	 */
	protected abstract void saveStorableObjects(final short entityCode,
												final Collection storableObjects,
												final boolean force) throws ApplicationException;

	protected void serializePoolImpl() {
		final Set entityCodeSet = this.objectPoolMap.keySet();
		for (final Iterator it = entityCodeSet.iterator(); it.hasNext();) {
			final Short entityCode = (Short) it.next();
			LRUMapSaver.save((LRUMap) this.objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode));
		}
	}

	protected void truncateObjectPoolImpl(final short entityCode) {
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(entityCode));
		if (objectPool instanceof StorableObjectResizableLRUMap)
			((StorableObjectResizableLRUMap) objectPool).truncate(true);
		else
			Log.errorMessage("StorableObjectPool.truncateObjectPoolImpl | ERROR: Object pool class '" + objectPool.getClass().getName()
					+ "' not 'StorableObjectResizableLRUMap' -- cannot truncate pool"); 
	}
}
