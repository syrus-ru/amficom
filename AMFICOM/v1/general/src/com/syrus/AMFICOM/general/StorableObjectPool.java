/*
 * $Id: StorableObjectPool.java,v 1.3 2004/12/07 11:50:40 bob Exp $
 *
 * Copyright ø 2004 Syrus Systems.
 * Ó¡’ﬁŒœ-‘≈»Œ…ﬁ≈”À…  √≈Œ‘“.
 * “œ≈À‘: ·ÌÊÈÎÔÌ.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
 * @version $Revision: 1.3 $, $Date: 2004/12/07 11:50:40 $
 * @author $Author: bob $
 * @module general_v1
 */
public abstract class StorableObjectPool {
	/**
	 * ‚·Í·Ó-symbol {@value} 
	 */
	public static final String ‚·Í·Ó = "[:]/\\/\\/\\/\\/|||||||||||||||||||||||||||[:]";

	protected Class	cacheMapClass	= LRUMap.class;

	protected Map	objectPoolMap;

	public StorableObjectPool() {
		// empty
	}

	public StorableObjectPool(Class cacheMapClass) {
		this.cacheMapClass = cacheMapClass;
	}

	protected void deleteImpl(List ids) throws DatabaseException, CommunicationException {
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			Short entityCode = new Short(id.getMajor());
			LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
			lruMap.remove(id);
		}
		deleteStorableObjects(ids);
	}

	protected void addObjectPool(short objectEntityCode, int poolSize) {
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
			} else
				throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName()
						+ " must extends LRUMap");
		} catch (SecurityException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName()
					+ " SecurityException " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName()
					+ " IllegalArgumentException " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName()
					+ " NoSuchMethodException " + e.getMessage());
		} catch (InstantiationException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName()
					+ " InstantiationException " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName()
					+ " IllegalAccessException " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("StorableObjectPool.addObjectPool | CacheMapClass " + this.cacheMapClass.getName()
					+ " InvocationTargetException " + e.getMessage());
		}

	}

	protected void cleanChangedStorableObjectImpl(Short entityCode) {
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
			cleanChangedStorableObjectImpl(entityCode);
		}
	}

	protected void deleteImpl(Identifier id) throws DatabaseException, CommunicationException {
		Short entityCode = new Short(id.getMajor());
		LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);
		lruMap.remove(id);
		deleteStorableObject(id);

	}

	protected abstract void deleteStorableObject(Identifier id) throws DatabaseException, CommunicationException;

	protected abstract void deleteStorableObjects(List ids) throws DatabaseException, CommunicationException;

	protected void flushImpl(boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException, IllegalDataException {
		List list = new LinkedList();
		for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			LRUMap objectPool = (LRUMap) this.objectPoolMap.get(entityCode);
			if (objectPool != null) {
				list.clear();
				for (Iterator poolIt = objectPool.iterator(); poolIt.hasNext();) {
					StorableObject storableObject = (StorableObject) poolIt.next();
					if (storableObject.isChanged()) {
						if (!list.contains(storableObject)) {
							list.add(storableObject);
							Log.debugMessage("StorableObjectPool.flushImpl | '" + storableObject.getId() + "' is changed", Log.DEBUGLEVEL10);
						}
					}
				}
				short code = entityCode.shortValue();
				save(code, list, force);

			} else {
				Log.errorMessage("StorableObjectPool.flushImpl | Cannot find object pool for entity code: '"
						+ ObjectEntities.codeToString(entityCode.shortValue()) + "'");
			}
		}
	}

	protected StorableObject getStorableObjectImpl(Identifier objectId, boolean useLoader) throws DatabaseException,
			CommunicationException {
		if (objectId != null) {
			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject) objectPool.get(objectId);
				if (storableObject != null)
					return storableObject;
				{
					if (useLoader) {
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
			}
			
			Log
					.errorMessage("StorableObjectPool.getStorableObjectImpl | Cannot find object pool for objectId: '"
							+ objectId.toString() + "' entity code: '" + objectEntityCode + "'");
			for(Iterator it = this.objectPoolMap.keySet().iterator();it.hasNext();){
				Short entityCode = (Short)it.next();
				Log.debugMessage("StorableObjectPool.getStorableObjectImpl | available " + ObjectEntities.codeToString(entityCode.shortValue()) + " / " + entityCode, Log.DEBUGLEVEL05);
			}
			return null;

		}
		Log.errorMessage("StorableObjectPool.getStorableObjectImpl | NULL identifier supplied");
		return null;
	}

	protected List getStorableObjectsByConditionButIdsImpl(	List ids,
															StorableObjectCondition condition,
															boolean useLoader) throws ApplicationException {
		List list = null;
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(condition.getEntityCode());
		if (objectPool != null) {
			list = new LinkedList();
			for (Iterator it = objectPool.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if ((ids == null || !ids.contains(storableObject.getId()))
						&& (condition.isConditionTrue(storableObject)))
					list.add(storableObject);
			}

			List loadedList = null;

			if (useLoader) {
				if (condition.isNeedMore(list)) {
					List idsList = new ArrayList(list.size());
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						StorableObject storableObject = (StorableObject) iter.next();
						idsList.add(storableObject.getId());
					}

					if (ids != null) {
						for (Iterator iter = ids.iterator(); iter.hasNext();) {
							Identifier id = (Identifier) iter.next();
							idsList.add(id);
						}
					}

					loadedList = loadStorableObjectsButIds(condition, idsList);
				}
			}

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

	protected List getStorableObjectsByConditionImpl(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return getStorableObjectsByConditionButIdsImpl(null, condition, useLoader);
	}

	protected List getStorableObjectsImpl(List objectIds, boolean useLoader) throws DatabaseException,
			CommunicationException {
		List list = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (Iterator it = objectIds.iterator(); it.hasNext();) {
				Identifier objectId = (Identifier) it.next();
				short objectEntityCode = objectId.getMajor();
				Short entityCode = new Short(objectEntityCode);
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
							.errorMessage("StorableObjectPool.getStorableObjectsImpl | Cannot find object pool for objectId: '"
									+ objectId.toString()
									+ "' entity code: '"
									+ ObjectEntities.codeToString(objectEntityCode) + "'");
				}
			}

		} else {
			Log.errorMessage("StorableObjectPool.getStorableObjectsImpl | NULL list of identifiers supplied");
		}

		if (objectQueueMap != null) {
			if (list == null)
				list = new LinkedList();
			for (Iterator it = objectQueueMap.keySet().iterator(); it.hasNext();) {
				Short entityCode = (Short) it.next();
				List objectQueue = (List) objectQueueMap.get(entityCode);
				List storableObjects = loadStorableObjects(entityCode, objectQueue);
				if (storableObjects != null) {
					try {
						for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
							StorableObject storableObject = (StorableObject) iter.next();
							putStorableObjectImpl(storableObject);
							list.add(storableObject);
						}
					} catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}

		if (list == null)
			list = Collections.EMPTY_LIST;

		return list;
	}

	protected abstract StorableObject loadStorableObject(Identifier objectId) throws DatabaseException,
			CommunicationException;

	protected abstract List loadStorableObjects(Short entityCode, List ids) throws DatabaseException,
			CommunicationException;

	protected abstract List loadStorableObjectsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException;

	protected void polulatePools() {
		try {
			for (Iterator it = this.objectPoolMap.keySet().iterator(); it.hasNext();) {
				short objectEntityCode = ((Short) it.next()).shortValue();
				List keys = LRUMapSaver.load(ObjectEntities.codeToString(objectEntityCode));
				if (keys != null)
					getStorableObjectsImpl(keys, true);
			}
		} catch (CommunicationException e) {
			Log.errorException(e);
			Log.errorMessage("StorableObjectPool.polulatePools | Error: " + e.getMessage());
		} catch (DatabaseException e) {
			Log.errorException(e);
			Log.errorMessage("StorableObjectPool.polulatePools | Error: " + e.getMessage());
		}
	}

	protected StorableObject putStorableObjectImpl(StorableObject storableObject) throws IllegalObjectEntityException {
//		if (storableObject == null)
//			return null;
		Identifier objectId = storableObject.getId();
		LRUMap objectPool = (LRUMap) this.objectPoolMap.get(new Short(objectId.getMajor()));
		if (objectPool != null) { return (StorableObject) objectPool.put(objectId, storableObject); }
		throw new IllegalObjectEntityException(
												"StorableObjectPool.putStorableObject | Illegal object entity: '"
														+ ObjectEntities.codeToString(objectId.getMajor()) + "'",
												IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}

	protected void refreshImpl() throws DatabaseException, CommunicationException {
		try {
			Log.debugMessage("StorableObjectPool.refreshImpl | trying to refresh Pool...", Log.DEBUGLEVEL03);
			Set storableObjects = new HashSet();
			Set returnedStorableObjectsIds = new HashSet();
			Set entityCodes = this.objectPoolMap.keySet();

			for (Iterator it = entityCodes.iterator(); it.hasNext();) {
				Short entityCode = (Short) it.next();
				LRUMap lruMap = (LRUMap) this.objectPoolMap.get(entityCode);

				for (Iterator it2 = lruMap.iterator(); it2.hasNext();) {
					storableObjects.add(it2.next());
				}
				if (storableObjects == null || storableObjects.isEmpty()) {
					Log.debugMessage("StorableObjectPool.refreshImpl | LRUMap for '"
							+ ObjectEntities.codeToString(entityCode.shortValue()) + "' entity has no elements",
										Log.DEBUGLEVEL08);
					continue;
				}
				Log.debugMessage("StorableObjectPool.refreshImpl | try refresh LRUMap for '"
						+ ObjectEntities.codeToString(entityCode.shortValue()) + "' entity", Log.DEBUGLEVEL08);

				returnedStorableObjectsIds = refreshStorableObjects(storableObjects);

				getStorableObjectsImpl(new ArrayList(returnedStorableObjectsIds), true);
			}
		} catch (DatabaseException e) {
			Log.errorMessage("StorableObjectPool.refreshImpl | DatabaseException: " + e.getMessage());
			throw new DatabaseException("StorableObjectPool.refreshImpl", e);
		} catch (CommunicationException e) {
			Log.errorMessage("StorableObjectPool.refreshImpl | CommunicationException: " + e.getMessage());
			throw new CommunicationException("StorableObjectPool.refreshImpl", e);
		}
	}

	protected abstract Set refreshStorableObjects(Set storableObjects) throws CommunicationException, DatabaseException;

	
	private void save(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException{
		if (!list.isEmpty()){		
			// calculate dependencies to save
			Map dependenciesMap = new HashMap();
			for (Iterator it = list.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				Log.debugMessage("StorableObjectPool.save | calculate dependencies for '" 
								 + storableObject.getId() + "'", Log.DEBUGLEVEL08);
				List dependencies = storableObject.getDependencies();
				for (Iterator depIt = dependencies.iterator(); depIt.hasNext();) {
					Object depItObj = depIt.next();
					Identifier id;
					StorableObject stObj;
					if (depItObj instanceof StorableObject){
						stObj = (StorableObject)depItObj;
						id = stObj.getId();
					} else if (depItObj instanceof Identifier) {
						id = (Identifier) depItObj;
						stObj = getStorableObjectImpl(id, true);
					} else {
						throw new IllegalDataException("StorableObjectPool.save | Illegal dependencies Object: " + depItObj.getClass().getName());
					}
					
					Short major = new Short(id.getMajor());
					List depList = (List)dependenciesMap.get(major);
					if (depList == null){
						depList = new LinkedList();
						dependenciesMap.put(major, depList);
					}
					if (stObj != null && stObj.isChanged() && !depList.contains(stObj))
						depList.add(stObj);
				}
			}
			
			
			// recursieve save dependencies
			for (Iterator it = dependenciesMap.keySet().iterator(); it.hasNext();) {
				Short major = (Short) it.next();
				List depList = (List)dependenciesMap.get(major);
				if (depList != null && !depList.isEmpty()){
					Log.debugMessage("StorableObjectPool.save | recursieve save '" 
									 + ObjectEntities.codeToString(major.shortValue()) + "'", Log.DEBUGLEVEL08);
					// [:]/\/\/\/\/|||||||||||||||||||||||||||[:]
					save(major.shortValue(), depList, force);
				}
			}
			
			for (Iterator it = list.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				Log.debugMessage("StorableObjectPool.save | save '" 
								 + storableObject.getId() + "'", Log.DEBUGLEVEL08);
			}
			
			
			saveStorableObjects(code, list, force);

		}
	}
	protected abstract void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException, IllegalDataException;

	protected void serializePoolImpl() {
		java.util.Set entityCodeSet = this.objectPoolMap.keySet();
		for (Iterator it = entityCodeSet.iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			LRUMapSaver.save((LRUMap) this.objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode
					.shortValue()));
		}
	}
}
