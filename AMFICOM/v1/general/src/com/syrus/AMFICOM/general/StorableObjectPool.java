/*
 * $Id: StorableObjectPool.java,v 1.15 2005/02/07 12:13:20 arseniy Exp $
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
 * @version $Revision: 1.15 $, $Date: 2005/02/07 12:13:20 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class StorableObjectPool {
	/**
	 * ‚·Í·Ó-symbol {@value} 
	 */
	public static final String ‚·Í·Ó = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	protected Class	cacheMapClass	= LRUMap.class;

	protected Map	objectPoolMap;
	
	private short 	selfGroupCode;
	
	private Map 	flushedGroup;

	public StorableObjectPool() {
		// empty
	}

	public StorableObjectPool(final Class cacheMapClass) {
		this.cacheMapClass = cacheMapClass;
	}

	/**
	 * @param objectEntityCode mustn't lie within [SCHEME_MIN_ENTITY_CODE, SCHEME_MAX_ENTITY_CODE]
	 * @param poolSize
	 */
	protected void addObjectPool(final short objectEntityCode, final int poolSize) {
		assert (objectEntityCode < ObjectEntities.SCHEME_MIN_ENTITY_CODE)
			|| (ObjectEntities.SCHEME_MAX_ENTITY_CODE < objectEntityCode)
			: "Invalid storable object pool used...";

		try {
			LRUMap objectPool = null;
			// LRUMap objectPool = new LRUMap(poolSize);
			Constructor constructor = this.cacheMapClass.getConstructor(new Class[] { int.class});
			Object obj = constructor.newInstance(new Object[] { new Integer(poolSize)});
			if (obj instanceof LRUMap) {
				objectPool = (LRUMap) obj;
				Short short1 = new Short(objectEntityCode);
				this.objectPoolMap.put(short1, objectPool);
//				Log.debugMessage("StorableObjectPool.addObjectPool | pool for " + ObjectEntities.codeToString(short1.shortValue()) + "/" + short1 + "(" + objectEntityCode +") size  " + poolSize + " added", Log.DEBUGLEVEL07);
			}
			else
				throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " must extend LRUMap");
		}
		catch (SecurityException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " SecurityException " + e.getMessage());
		}
		catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " IllegalArgumentException " + e.getMessage());
		}
		catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " NoSuchMethodException " + e.getMessage());
		}
		catch (InstantiationException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " InstantiationException " + e.getMessage());
		}
		catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " IllegalAccessException " + e.getMessage());
		}
		catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " InvocationTargetException " + e.getMessage());
		}

	}

	/**
	 * @param entityCode mustn't lie within [SCHEME_MIN_ENTITY_CODE, SCHEME_MAX_ENTITY_CODE]
	 */
	protected void cleanChangedStorableObjectImpl(final Short entityCode) {
		assert (entityCode.shortValue() < ObjectEntities.SCHEME_MIN_ENTITY_CODE)
			|| (ObjectEntities.SCHEME_MAX_ENTITY_CODE < entityCode.shortValue())
			: "Invalid storable object pool used...";

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

	protected void deleteImpl(final Identifier id) throws DatabaseException, CommunicationException {
		Short entityCode = new Short(id.getMajor());
		LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
		if (lruMap != null)
			lruMap.remove(id);
		else
			Log.errorMessage("StorableObjectPool.flushImpl | Cannot find object pool for entity '" + ObjectEntities.codeToString(entityCode.shortValue()) + "' entity code: " + entityCode);

		this.deleteStorableObject(id);
	}

	protected void deleteImpl(final List ids) throws DatabaseException, CommunicationException {
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			Short entityCode = new Short(id.getMajor());
			LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
			if (lruMap != null)
				lruMap.remove(id);
			else
				Log.errorMessage("StorableObjectPool.flushImpl | Cannot find object pool for entity '" + ObjectEntities.codeToString(entityCode.shortValue()) + "' entity code: " + entityCode);
		}

		deleteStorableObjects(ids);
	}

	protected abstract void deleteStorableObject(final Identifier id) throws DatabaseException, CommunicationException;

	protected abstract void deleteStorableObjects(final List ids) throws DatabaseException, CommunicationException;

	/**
	 * This method is only invoked by this class' descendants, using their
	 * <code>public static void flush(boolean)</code> method. It completes
	 * the following operations:
	 * <ul>
	 * <li>get modified objects from current module's pool;</li>
	 * <li>analyse dependencies of these </li>
	 * </ul>
	 *
	 * @param force
	 * @throws VersionCollisionException
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @throws IllegalDataException
	 */
	protected void flushImpl(final boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException,
				IllegalDataException {
		synchronized (this) {
			List list = new LinkedList();
			if (this.flushedGroup == null)
				this.flushedGroup = new HashMap();
			else
				this.flushedGroup.clear();
			for (final Iterator entityCodeIterator = this.objectPoolMap.keySet().iterator(); entityCodeIterator.hasNext();) {
				final Short entityCode = (Short) entityCodeIterator.next();
				LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
				if (objectPool != null) {
					list.clear();
					for (Iterator poolIt = objectPool.iterator(); poolIt.hasNext();) {
						StorableObject storableObject = (StorableObject) poolIt.next();
						this.selfGroupCode = ObjectGroupEntities.getGroupCode(storableObject.getId().getMajor());
						if (storableObject.isChanged()) {
							if (!list.contains(storableObject)) {
								list.add(storableObject);
								Log.debugMessage("StorableObjectPool.flushImpl | '" + storableObject.getId() + "' is changed", Log.DEBUGLEVEL10);
							}
						}
					}
					this.save(list, force);

				}
				else {
					Log.errorMessage("StorableObjectPool.flushImpl | Cannot find object pool for entity code: '"
							+ ObjectEntities.codeToString(entityCode)
							+ "'");
				}
			}
		}
	}

	protected StorableObject getStorableObjectImpl(final Identifier objectId, final boolean useLoader) throws DatabaseException, CommunicationException {
		if (objectId != null) {
			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject) objectPool.get(objectId);
				if (storableObject == null && useLoader) {
					storableObject = loadStorableObject(objectId);
					if (storableObject != null)
						try {
							putStorableObjectImpl(storableObject);
						}
						catch (IllegalObjectEntityException ioee) {
							Log.errorException(ioee);
						}
				}
				return storableObject;
			}

			Log.errorMessage("StorableObjectPool.getStorableObjectImpl | Cannot find object pool for objectId: '" + objectId.toString() + "' entity code: '" + objectEntityCode + "'");
			for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
				final Short entityCode = (Short) it.next();
				Log.debugMessage("StorableObjectPool.getStorableObjectImpl | available "
					+ ObjectEntities.codeToString(entityCode)
					+ " / "
					+ entityCode,
					Log.DEBUGLEVEL05);
			}
		} else
			Log.errorMessage("StorableObjectPool.getStorableObjectImpl | NULL identifier supplied");
		return null;
	}

	/**
	 * @param ids a non-null (use {@link Collections#EMPTY_LIST} in case of
	 *            emergency) list of pure-java identifiers.
	 * @param condition
	 * @param useLoader
	 * @throws ApplicationException
	 */
	protected List getStorableObjectsByConditionButIdsImpl(final List ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		assert ids != null : "Supply an empty list instead...";

		List list = null;
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(condition.getEntityCode());
		if (objectPool != null) {
			list = new LinkedList();
			for (Iterator it = objectPool.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if (!ids.contains(storableObject.getId())
						&& condition.isConditionTrue(storableObject))
					list.add(storableObject);
			}

			List loadedList = null;

			if (useLoader && condition.isNeedMore(list)) {
				List idsList = new ArrayList(list.size());
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					StorableObject storableObject = (StorableObject) iter.next();
					idsList.add(storableObject.getId());
				}

				for (Iterator iter = ids.iterator(); iter.hasNext();) {
					Identifier id = (Identifier) iter.next();
					idsList.add(id);
				}

				loadedList = this.loadStorableObjectsButIds(condition, idsList);
			}

			/*
			 * This block is only needed in order for LRUMap to
			 * rehash itself.
			 */
			for (Iterator it = list.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				objectPool.get(storableObject);
			}

			if (loadedList != null) {
				for (Iterator it = loadedList.iterator(); it.hasNext();) {
					StorableObject storableObject = (StorableObject) it.next();
					objectPool.put(storableObject.getId(), storableObject);
					list.add(storableObject);
				}
			}

		}

		if (list == null)
			list = Collections.EMPTY_LIST;

		return list;
	}

	protected List getStorableObjectsByConditionImpl(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		return this.getStorableObjectsByConditionButIdsImpl(Collections.EMPTY_LIST, condition, useLoader);
	}

	/**
	 * @param objectIds list of pure java identifiers. 
	 * @param useLoader
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @todo Check references within workspace, convert a run time warning
	 *       (if objectIds is null) into a run time error.
	 */
	protected List getStorableObjectsImpl(final List objectIds, final boolean useLoader) throws DatabaseException, CommunicationException {
		List list = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (Iterator it = objectIds.iterator(); it.hasNext();) {
				Identifier objectId = (Identifier) it.next();
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
				}
				else {
					Log.errorMessage("StorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '"
									+ objectId.toString()
									+ "', entity code: '"
									+ ObjectEntities.codeToString(entityCode) + "'");
				}
			}

		}
		else {
			Log.errorMessage("StorableObjectPool.getStorableObjectsImpl | NULL list of identifiers supplied");
		}

		if (objectQueueMap != null) {
			if (list == null)
				list = new LinkedList();
			for (Iterator it = objectQueueMap.keySet().iterator(); it.hasNext();) {
				Short entityCode = (Short) it.next();
				List objectQueue = (List) objectQueueMap.get(entityCode);
				List storableObjects = this.loadStorableObjects(entityCode, objectQueue);
				if (storableObjects != null) {
					try {
						for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
							StorableObject storableObject = (StorableObject) iter.next();
							this.putStorableObjectImpl(storableObject);
							list.add(storableObject);
						}
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}

		return list == null
			? Collections.EMPTY_LIST
			: list;
	}

	protected abstract StorableObject loadStorableObject(final Identifier objectId) throws DatabaseException, CommunicationException;

	protected abstract List loadStorableObjects(final Short entityCode, final List ids) throws DatabaseException, CommunicationException;

	protected abstract List loadStorableObjectsButIds(final StorableObjectCondition condition, final List ids) throws DatabaseException, CommunicationException;

	protected void populatePools() {
		try {
			for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
				Short objectEntityCode = (Short) it.next();
				List keys = LRUMapSaver.load(ObjectEntities.codeToString(objectEntityCode));
				if (keys != null)
					getStorableObjectsImpl(keys, true);
			}
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			Log.errorMessage("StorableObjectPool.populatePools | Error: " + ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			Log.errorMessage("StorableObjectPool.populatePools | Error: " + de.getMessage());
		}
	}

	/**
	 * @param storableObject a non-null pure java storable object.
	 * @throws IllegalObjectEntityException
	 */
	protected StorableObject putStorableObjectImpl(final StorableObject storableObject) throws IllegalObjectEntityException {
//*/
		assert storableObject != null;
/*/
		if (storableObject == null)
			return null;
//*/
		Identifier objectId = storableObject.getId();
		Short entityCode = new Short(objectId.getMajor());
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null) {
			return (StorableObject) objectPool.put(objectId, storableObject);
		}
		throw new IllegalObjectEntityException(
				"StorableObjectPool.putStorableObject | Illegal object entity: '"
				+ ObjectEntities.codeToString(entityCode) + "'",
				IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	/**
	 * Refresh only unchanged storable objects.
	 *
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected void refreshImpl() throws DatabaseException, CommunicationException {
		try {
			Log.debugMessage("StorableObjectPool.refreshImpl | trying to refresh Pool...", Log.DEBUGLEVEL03);
			final Set storableObjects = new HashSet();
			final Set entityCodes = this.objectPoolMap.keySet();

			for (final Iterator it = entityCodes.iterator(); it.hasNext();) {
				final Short entityCode = (Short) it.next();
				final LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);

				for (final Iterator it2 = lruMap.iterator(); it2.hasNext();) {
					final StorableObject storableObject = (StorableObject) it2.next();
					if (!storableObject.isChanged())
						storableObjects.add(storableObject);
				}
				if (storableObjects.isEmpty()) {
					Log.debugMessage("StorableObjectPool.refreshImpl | LRUMap for '"
							+ ObjectEntities.codeToString(entityCode) + "' entity has no elements",
							Log.DEBUGLEVEL08);
					continue;
				}
				Log.debugMessage("StorableObjectPool.refreshImpl | try refresh LRUMap for '"
						+ ObjectEntities.codeToString(entityCode) + "' entity",
						Log.DEBUGLEVEL08);

				final Set returnedStorableObjectsIds = refreshStorableObjects(storableObjects);				
				getStorableObjectsImpl(new ArrayList(returnedStorableObjectsIds), true);
			}
		} catch (DatabaseException de) {
			Log.errorMessage("StorableObjectPool.refreshImpl | DatabaseException: " + de.getMessage());
			throw new DatabaseException("StorableObjectPool.refreshImpl", de);
		} catch (CommunicationException ce) {
			Log.errorMessage("StorableObjectPool.refreshImpl | CommunicationException: " + ce.getMessage());
			throw new CommunicationException("StorableObjectPool.refreshImpl", ce);
		}
	}

	protected abstract Set refreshStorableObjects(final Set storableObjects) throws CommunicationException, DatabaseException;

	/**
	 * This method should only be invoked during assertion evaluation, and
	 * never in a release system.
	 *
	 * @param storableObjects non-null list of pure java storable objects
	 *        (empty list is ok).
	 * @return <code>true</code> if all entities within this list are of the
	 *         same type, <code>false</code> otherwise.
	 */
	private boolean hasSingleTypeEntities(final List storableObjects) {
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
	 * Code that invokes this method, should preliminarly call
	 * {@link #hasSingleTypeEntities(List)} with the same parameter and
	 * ensure that return value is <code>true</code>, e.g.:<pre>
	 * assert hasSingleTypeEntities(storableObjects) :
	 * 	"Storable objects of different type are saved separately...";
	 * </pre>
	 *
	 * @param storableObjects non-null, non-empty list of pure java storable
	 *        objects of the same type.
	 * @return common type of storable objects supplied as <code>short</code>.
	 */
	protected short getEntityCodeOfStorableObjects(final List storableObjects) {
		assert storableObjects.size() >= 1;

		return ((StorableObject) storableObjects.iterator().next()).getId().getMajor();
	}

	private void save(final List storableObjects, final boolean force)
			throws VersionCollisionException,
				DatabaseException,
				CommunicationException,
				IllegalDataException {
		assert this.hasSingleTypeEntities(storableObjects) : "Storable objects of different type are saved separately...";

		if (!storableObjects.isEmpty()) {
			// calculate dependencies to save
			final Map dependencyMap = new HashMap();
			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
				StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				Log.debugMessage("StorableObjectPool.save | calculate dependencies for '" + storableObject.getId() + "'",
						Log.DEBUGLEVEL08);
				final List dependencies = storableObject.getDependencies();
				for (final Iterator dependencyIterator = dependencies.iterator(); dependencyIterator.hasNext();) {
					final Object dependency = dependencyIterator.next();
					Identifier id;
					StorableObject stObj;
					if (dependency instanceof StorableObject) {
						stObj = (StorableObject) dependency;
						id = stObj.getId();
					}
					else
						if (dependency instanceof Identifier) {
							id = (Identifier) dependency;
							stObj = this.getStorableObjectImpl(id, true);
						}
						else {
							throw new IllegalDataException("StorableObjectPool.save | Illegal dependencies Object: "
									+ dependency.getClass().getName());
						}

					final Short entityCode = new Short(id.getMajor());
					List depList = (List) dependencyMap.get(entityCode);
					if (depList == null) {
						Short group = new Short(ObjectGroupEntities.getGroupCode(entityCode.shortValue()));
						/* invoke only for other groups that this package pool */
						if (group.shortValue() != this.selfGroupCode) {
							if (this.flushedGroup.get(group) == null)
								/* set that flush for this group wan't invoke */
								this.flushedGroup.put(group, Boolean.FALSE);
						}
						else {
							depList = new LinkedList();
							dependencyMap.put(entityCode, depList);
						}

					}
					if (stObj != null
							&& ObjectGroupEntities.getGroupCode(stObj.getId().getMajor()) == this.selfGroupCode
							&& stObj.isChanged()
							&& !depList.contains(stObj))
						depList.add(stObj);
				}
			}

			// invoke *StrorableObjectPool for other modules
			for (final Iterator entityCodeIterator = this.flushedGroup.keySet().iterator(); entityCodeIterator.hasNext();) {
				final Short groupCode = (Short) entityCodeIterator.next();
				final boolean invoked = ((Boolean) this.flushedGroup.get(groupCode)).booleanValue();

				/* there is no reason one more invoke flush because of just flushed */
				if (invoked)
					continue;

				final short group = groupCode.shortValue();
				String packageName;
				switch (group) {
					case ObjectGroupEntities.GENERAL_GROUP_CODE:
						packageName = "general.General";
						break;
					case ObjectGroupEntities.EVENT_GROUP_CODE:
						packageName = "event.Event";
						break;
					case ObjectGroupEntities.ADMINISTRATION_GROUP_CODE:
						packageName = "administration.Administration";
						break;
					case ObjectGroupEntities.CONFIGURATION_GROUP_CODE:
						packageName = "configuration.Configuration";
						break;
					case ObjectGroupEntities.MEASUREMENT_GROUP_CODE:
						packageName = "measurement.Measurement";
						break;
					case ObjectGroupEntities.MAP_GROUP_CODE:
						packageName = "map.Map";
						break;
					case ObjectGroupEntities.RESOURCE_GROUP_CODE:
						packageName = "resource.Resource";
						break;
					case ObjectGroupEntities.MAPVIEW_GROUP_CODE:
						packageName = "mapview.MapView";
						break;
					default:
						throw new IllegalDataException("StorableObjectPool.save | Illegal dependencies Object group: "
								+ ObjectGroupEntities.codeToString(group));
				}

				String className = "com.syrus.AMFICOM." + packageName + "StorableObjectPool";
				try {
					Class clazz = Class.forName(className);
					Method flushMethod = clazz.getDeclaredMethod("flush", new Class[] {boolean.class});
					flushMethod.invoke(null, new Object[] {force ? Boolean.TRUE : Boolean.FALSE});
					/* set that we have invoke flush for this group */
					this.flushedGroup.put(groupCode, Boolean.TRUE);
				}
				catch (ClassNotFoundException e) {
					Log.debugMessage("StorableObjectPool.save | Class " + className //$NON-NLS-1$
							+ " not found on the classpath" //$NON-NLS-1$
					, Log.WARNING);
				}
				catch (SecurityException e) {
					Log.debugMessage("StorableObjectPool.save | Caught a SecurityException " + className //$NON-NLS-1$
					, Log.WARNING);
				}
				catch (NoSuchMethodException e) {
					Log.debugMessage("StorableObjectPool.save | " + className + " doesn't have the flush method expected " //$NON-NLS-1$
					, Log.WARNING);
				}
				catch (IllegalArgumentException e) {
					Log.debugMessage("StorableObjectPool.save | " + className + " Caught an IllegalArgumentException" //$NON-NLS-1$
					, Log.WARNING);
				}
				catch (IllegalAccessException e) {
					Log.debugMessage("StorableObjectPool.save | " + className + " Caught an IllegalAccessException" //$NON-NLS-1$
					, Log.WARNING);
				}
				catch (InvocationTargetException e) {
					Log.debugMessage("StorableObjectPool.save | flush method throws an exception in class " + className //$NON-NLS-1$
					, Log.WARNING);
				}
			}

			// recursively save dependencies in this module
			for (final Iterator entityCodeIterator = dependencyMap.keySet().iterator(); entityCodeIterator.hasNext();) {
				final Short entityCode = (Short) entityCodeIterator.next();
				List depList = (List) dependencyMap.get(entityCode);
				if (depList != null && !depList.isEmpty()) {
					Log.debugMessage("StorableObjectPool.save | recursive save '" + ObjectEntities.codeToString(entityCode) + "'",
							Log.DEBUGLEVEL08);
					// [:]/\/\/\/\/|||||||||||||||||||||||||||[:]
					this.save(depList, force);
				}
			}

			for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
				StorableObject storableObject = (StorableObject) storableObjectIterator.next();
				Log.debugMessage("StorableObjectPool.save | save '" + storableObject.getId() + "'", Log.DEBUGLEVEL08);
			}

			final short entityCode = this.getEntityCodeOfStorableObjects(storableObjects);
			this.saveStorableObjects(entityCode, storableObjects, force);
		}
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
	 *       <code>entityCode</code>, rewrite overriding classes in order
	 *       for them to use {@link #getEntityCodeOfStorableObjects(List)}. 
	 */
	protected abstract void saveStorableObjects(final short entityCode, final List storableObjects, final boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException;

	protected void serializePoolImpl() {
		final Set entityCodeSet = this.objectPoolMap.keySet();
		for (final Iterator it = entityCodeSet.iterator(); it.hasNext();) {
			final Short entityCode = (Short) it.next();
			LRUMapSaver.save((LRUMap) this.objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode));
		}
	}
}
