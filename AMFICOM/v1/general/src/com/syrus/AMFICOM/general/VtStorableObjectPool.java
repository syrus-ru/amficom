/*
 * $Id: VtStorableObjectPool.java,v 1.1 2005/01/18 15:09:37 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.general.corba.StorableObject;
import com.syrus.io.LRUMapSaver;
import com.syrus.util.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A replacement of {@link StorableObjectPool} for valuetyped entities, such as
 * scheme objects.
 *
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/01/18 15:09:37 $
 * @module general_v1
 * @see StorableObjectPool
 */
public abstract class VtStorableObjectPool {
	protected Class cacheMapClass = LRUMap.class;

	protected Map objectPoolMap;

	public VtStorableObjectPool() {
		// empty ctor
	}

	public VtStorableObjectPool(final Class cacheMapClass) {
		this.cacheMapClass = cacheMapClass;
	}

	/**
	 * @param ids list of valuetyped identifiers.
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected void deleteImpl(final List ids) throws DatabaseException, CommunicationException {
		for (final Iterator iterator = ids.iterator(); iterator.hasNext();) {
			final Identifier id = (Identifier) iterator.next();
			((LRUMap) this.objectPoolMap.get(new Short(id.major()))).remove(id);
		}
		deleteStorableObjects(ids);
	}

	/**
	 * @param objectEntityCode must lie within [SCHEME_MIN_ENTITY_CODE, SCHEME_MAX_ENTITY_CODE]
	 * @param poolSize
	 */
	protected void addObjectPool(final short objectEntityCode, final int poolSize) {
		assert (ObjectEntities.SCHEME_MIN_ENTITY_CODE <= objectEntityCode)
			&& (objectEntityCode <= ObjectEntities.SCHEME_MAX_ENTITY_CODE)
			: "Invalid storable object pool used...";

		try {
			final Object obj = this.cacheMapClass
				.getConstructor(new Class[]{int.class})
				.newInstance(new Object[]{new Integer(poolSize)});
			if (obj instanceof LRUMap)
				this.objectPoolMap.put(new Short(objectEntityCode), obj);
			else
				throw new UnsupportedOperationException("VtStorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " must extend LRUMap");
		} catch (SecurityException se) {
			throw new UnsupportedOperationException("VtStorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " SecurityException " + se.getMessage());
		} catch (IllegalArgumentException iae) {
			throw new UnsupportedOperationException("VtStorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " IllegalArgumentException " + iae.getMessage());
		} catch (NoSuchMethodException nsme) {
			throw new UnsupportedOperationException("VtStorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " NoSuchMethodException " + nsme.getMessage());
		} catch (InstantiationException ie) {
			throw new UnsupportedOperationException("VtStorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " InstantiationException " + ie.getMessage());
		} catch (IllegalAccessException iae) {
			throw new UnsupportedOperationException("VtStorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " IllegalAccessException " + iae.getMessage());
		} catch (InvocationTargetException ite) {
			throw new UnsupportedOperationException("VtStorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName() + " InvocationTargetException " + ite.getMessage());
		}
	}

	/**
	 * @param entityCode must lie within [SCHEME_MIN_ENTITY_CODE, SCHEME_MAX_ENTITY_CODE]
	 */
	protected void cleanChangedStorableObjectImpl(final Short entityCode) {
		assert (ObjectEntities.SCHEME_MIN_ENTITY_CODE <= entityCode.shortValue())
			&& (entityCode.shortValue() <= ObjectEntities.SCHEME_MAX_ENTITY_CODE)
			: "Invalid storable object pool used...";

		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null)
			for (final Iterator objectPoolIterator = objectPool.iterator(); objectPoolIterator.hasNext();)
				if (((StorableObject) objectPoolIterator.next()).changed())
					objectPoolIterator.remove();
	}

	protected void cleanChangedStorableObjectsImpl() {
		for (final Iterator iterator = this.objectPoolMap.keySet().iterator(); iterator.hasNext();)
			cleanChangedStorableObjectImpl((Short) iterator.next());
	}

	protected void deleteImpl(final Identifier id) throws DatabaseException, CommunicationException {
		((LRUMap) this.objectPoolMap.get(new Short(id.major()))).remove(id);
		deleteStorableObject(id);
	}

	protected abstract void deleteStorableObject(final Identifier id) throws DatabaseException, CommunicationException;

	/**
	 * @param ids list of valuetyped identifiers.
	 * @throws DatabaseException
	 * @throws CommunicationException
	 */
	protected abstract void deleteStorableObjects(final List ids) throws DatabaseException, CommunicationException;

	protected void flushImpl(final boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		final List list = new LinkedList();
		for (final Iterator objectPoolMapKeyIterator = this.objectPoolMap.keySet().iterator(); objectPoolMapKeyIterator.hasNext();) {
			final Short entityCode = (Short) objectPoolMapKeyIterator.next();
			final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
			if (objectPool != null) {
				list.clear();
				for (final Iterator objectPoolIterator = objectPool.iterator(); objectPoolIterator.hasNext();) {
					final StorableObject storableObject = (StorableObject) objectPoolIterator.next();
					if (storableObject.changed() && !list.contains(storableObject)) {
						list.add(storableObject);
						Log.debugMessage("VtStorableObjectPool.flushImpl | '" + storableObject.id() + "' is changed", Log.DEBUGLEVEL10);
					}
				}
				save(list, force);

			} else
				Log.errorMessage("VtStorableObjectPool.flushImpl | Cannot find object pool for entity code: '" + ObjectEntities.codeToString(entityCode.shortValue()) + "'");
		}
	}

	protected StorableObject getStorableObjectImpl(final Identifier objectId, final boolean useLoader) throws DatabaseException, CommunicationException {
		if (objectId != null) {
			final short objectEntityCode = objectId.major();
			final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject) objectPool.get(objectId);
				if (storableObject == null && useLoader) {
					storableObject = loadStorableObject(objectId);
					if (storableObject != null)
						try {
							putStorableObjectImpl(storableObject);
						} catch (IllegalObjectEntityException ioee) {
							Log.errorException(ioee);
						}
				}
				return storableObject;
			}

			Log.errorMessage("VtStorableObjectPool.getStorableObjectImpl | Cannot find object pool for objectId: '" + objectId.toString() + "' entity code: '" + objectEntityCode + "'");
			for (final Iterator objectPoolMapKeyIterator = this.objectPoolMap.keySet().iterator(); objectPoolMapKeyIterator.hasNext();) {
				final short entityCode = ((Short) objectPoolMapKeyIterator.next()).shortValue();
				Log.debugMessage("VtStorableObjectPool.getStorableObjectImpl | available " + ObjectEntities.codeToString(entityCode) + " / " + entityCode, Log.DEBUGLEVEL05);
			}
		} else
			Log.errorMessage("VtStorableObjectPool.getStorableObjectImpl | NULL identifier supplied");
		return null;
	}

	/**
	 * @param ids a non-null (use {@link Collections#EMPTY_LIST} in case of
	 *            emergency) list of valuetyped identifiers.
	 * @param condition
	 * @param useLoader
	 * @throws ApplicationException
	 */
	protected List getStorableObjectsByConditionButIdsImpl(final List ids,
			final StorableObjectCondition condition,
			final boolean useLoader) throws ApplicationException {
		assert ids != null : "Supply an empty list instead...";

		List storableObjectList = null;
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(condition.getEntityCode());
		if (objectPool != null) {
			storableObjectList = new LinkedList();
			for (final Iterator objectPoolIterator = objectPool.iterator(); objectPoolIterator.hasNext();) {
				final StorableObject storableObject = (StorableObject) objectPoolIterator.next();
				if (!ids.contains(storableObject.id())
						&& condition.isConditionTrue(storableObject))
					storableObjectList.add(storableObject);
			}

			List loadedStorableObjectList = null;

			if (useLoader && condition.isNeedMore(storableObjectList)) {
				final List idList = new ArrayList(storableObjectList.size());
				for (Iterator storableObjectIterator = storableObjectList.iterator(); storableObjectIterator.hasNext();)
					idList.add(((StorableObject) storableObjectIterator.next()).id());

				for (Iterator idIterator = ids.iterator(); idIterator.hasNext();)
					idList.add(idIterator.next());

				loadedStorableObjectList = loadStorableObjectsButIds(condition, idList);
			}

			/*
			 * This block is only needed in order for LRUMap to
			 * rehash itself.
			 */
			for (final Iterator storableObjectIterator = storableObjectList.iterator(); storableObjectIterator.hasNext();)
				objectPool.get(storableObjectIterator.next());

			if (loadedStorableObjectList != null)
				for (final Iterator storableObjectIterator = loadedStorableObjectList.iterator(); storableObjectIterator.hasNext();) {
					final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
					objectPool.put(storableObject.id(), storableObject);
					storableObjectList.add(storableObject);
				}
		}

		if (storableObjectList == null)
			storableObjectList = Collections.EMPTY_LIST;

		return storableObjectList;
	}

	protected List getStorableObjectsByConditionImpl(final StorableObjectCondition condition, final boolean useLoader)
			throws ApplicationException {
		return getStorableObjectsByConditionButIdsImpl(Collections.EMPTY_LIST, condition, useLoader);
	}

	/**
	 * @param objectIds list of valuetyped identifiers.
	 * @param useLoader
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @todo Check references within workspace, convert a run time warning
	 *       (if objectIds is null) into a run time error.
	 */
	protected List getStorableObjectsImpl(final List objectIds, final boolean useLoader) throws DatabaseException, CommunicationException {
		List storableObjectList = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (final Iterator objectIdIterator = objectIds.iterator(); objectIdIterator.hasNext();) {
				final Identifier objectId = (Identifier) objectIdIterator.next();
				final Short entityCode = new Short(objectId.major());
				final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
				StorableObject storableObject = null;
				if (objectPool != null) {
					storableObject = (StorableObject) objectPool.get(objectId);
					if (storableObject != null) {
						if (storableObjectList == null)
							storableObjectList = new LinkedList();
						storableObjectList.add(storableObject);
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
				} else
					Log.errorMessage("VtStorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '"
							+ objectId.toString()
							+ "', entity code: '"
							+ ObjectEntities.codeToString(entityCode) + "'");
			}
		} else
			Log.errorMessage("VtStorableObjectPool.getStorableObjectsImpl | NULL list of identifiers supplied");

		if (objectQueueMap != null) {
			if (storableObjectList == null)
				storableObjectList = new LinkedList();
			for (final Iterator objectQueueMapKeyIterator = objectQueueMap.keySet().iterator(); objectQueueMapKeyIterator.hasNext();) {
				final Short entityCode = (Short) objectQueueMapKeyIterator.next();
				final List storableObjects = loadStorableObjects(
						entityCode,
						(List) objectQueueMap.get(entityCode));
				if (storableObjects != null)
					try {
						for (final Iterator storableObjectIterator = storableObjects.iterator(); storableObjectIterator.hasNext();) {
							final StorableObject storableObject = (StorableObject) storableObjectIterator.next();
							putStorableObjectImpl(storableObject);
							storableObjectList.add(storableObject);
						}
					} catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
			}
		}

		return storableObjectList == null 
			? Collections.EMPTY_LIST
			: storableObjectList;
	}

	protected abstract StorableObject loadStorableObject(final Identifier objectId) throws DatabaseException, CommunicationException;

	protected abstract List loadStorableObjects(final Short entityCode, final List ids) throws DatabaseException, CommunicationException;

	protected abstract List loadStorableObjectsButIds(final StorableObjectCondition condition, final List ids) throws DatabaseException, CommunicationException;

	protected void populatePools() {
		try {
			for (final Iterator objectPoolMapKeyIterator = this.objectPoolMap.keySet().iterator(); objectPoolMapKeyIterator.hasNext();) {
				List keys = LRUMapSaver.load(ObjectEntities.codeToString((Short) objectPoolMapKeyIterator.next()));
				if (keys != null)
					getStorableObjectsImpl(keys, true);
			}
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			Log.errorMessage("VtStorableObjectPool.populatePools | Error: " + ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			Log.errorMessage("VtStorableObjectPool.populatePools | Error: " + de.getMessage());
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
		final Identifier objectId = storableObject.id();
		final Short entityCode = new Short(objectId.major());
		final LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
		if (objectPool != null)
			return (StorableObject) objectPool.put(objectId, storableObject);
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
			Log.debugMessage("VtStorableObjectPool.refreshImpl | trying to refresh Pool...", Log.DEBUGLEVEL03);
			final Set storableObjects = new HashSet();

			for (final Iterator entityCodeIterator = this.objectPoolMap.keySet().iterator(); entityCodeIterator.hasNext();) {
				final Short entityCode = (Short) entityCodeIterator.next();

				for (final Iterator lruMapIterator = ((LRUMap) this.objectPoolMap.get(entityCode)).iterator(); lruMapIterator.hasNext();) {
					final StorableObject storableObject = (StorableObject) lruMapIterator.next();
					if (!storableObject.changed())
						storableObjects.add(storableObject);
				}
				if (storableObjects.isEmpty()) {
					Log.debugMessage("VtStorableObjectPool.refreshImpl | LRUMap for '"
							+ ObjectEntities.codeToString(entityCode) + "' entity has no elements",
							Log.DEBUGLEVEL08);
					continue;
				}
				Log.debugMessage("VtStorableObjectPool.refreshImpl | try refresh LRUMap for '"
						+ ObjectEntities.codeToString(entityCode) + "' entity",
						Log.DEBUGLEVEL08);

				getStorableObjectsImpl(new ArrayList(refreshStorableObjects(storableObjects)), true);
			}
		} catch (DatabaseException de) {
			Log.errorMessage("VtStorableObjectPool.refreshImpl | DatabaseException: " + de.getMessage());
			throw new DatabaseException("VtStorableObjectPool.refreshImpl", de);
		} catch (CommunicationException ce) {
			Log.errorMessage("VtStorableObjectPool.refreshImpl | CommunicationException: " + ce.getMessage());
			throw new CommunicationException("VtStorableObjectPool.refreshImpl", ce);
		}
	}

	protected abstract Set refreshStorableObjects(final Set storableObjects) throws CommunicationException, DatabaseException;

	/**
	 * This method should only be invoked during assertion evaluation, and
	 * never in a release system.
	 *
	 * @param storableObjects non-null list of valuetyped storable objects
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
		final short entityCode = ((StorableObject) storableObjectIterator.next()).id().major();
		while (storableObjectIterator.hasNext())
			if (entityCode != ((StorableObject) storableObjectIterator.next()).id().major())
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
	 * @param storableObjects non-null, non-empty list of valuetyped storable
	 *        objects of the same type.
	 * @return common type of storable objects supplied as <code>short</code>.
	 */
	protected short getEntityCodeOfStorableObjects(final List storableObjects) {
		assert storableObjects.size() >= 1;

		return ((StorableObject) storableObjects.iterator().next()).id().major();
	}

	/**
	 * @param storableObjects
	 * @param force
	 * @throws VersionCollisionException
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @throws IllegalDataException
	 * @todo implement {@link #save(List, boolean)}
	 */
	private void save(final List storableObjects, final boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjects
	 * @param force
	 * @throws VersionCollisionException
	 * @throws DatabaseException
	 * @throws CommunicationException
	 * @throws IllegalDataException
	 */
	protected abstract void saveStorableObjects(final List storableObjects, final boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException;

	protected void serializePoolImpl() {
		for (final Iterator entityCodeIterator = this.objectPoolMap.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			LRUMapSaver.save((LRUMap) this.objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode));
		}
	}
}
