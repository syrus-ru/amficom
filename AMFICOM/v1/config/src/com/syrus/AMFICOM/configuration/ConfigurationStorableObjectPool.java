/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.44 2004/11/24 09:24:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

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
import java.util.Hashtable;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.io.LRUMapSaver;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.44 $, $Date: 2004/11/24 09:24:14 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class ConfigurationStorableObjectPool {

	private static final int			OBJECT_POOL_MAP_SIZE			= 16;		/* Number of entities */

	private static final int			CABLETHREADTYPE_OBJECT_POOL_SIZE	= 4;
	private static final int			CHARACTERISTICTYPE_OBJECT_POOL_SIZE	= 9;
	private static final int			EQUIPMENTTYPE_OBJECT_POOL_SIZE		= 1;
	private static final int			LINKTYPE_OBJECT_POOL_SIZE	= 2;
	// private static final int			KISTYPE_OBJECT_POOL_SIZE		= 1;
	private static final int			PORTTYPE_OBJECT_POOL_SIZE		= 1;
	private static final int			MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE	= 1;

	private static final int			CHARACTERISTIC_OBJECT_POOL_SIZE		= 4;
	private static final int			LINK_OBJECT_POOL_SIZE	= 2;
	private static final int			PERMATTR_OBJECT_POOL_SIZE		= 4;
	private static final int			USER_OBJECT_POOL_SIZE			= 4;
	private static final int			DOMAIN_OBJECT_POOL_SIZE			= 4;
	private static final int			SERVER_OBJECT_POOL_SIZE			= 4;
	private static final int			MCM_OBJECT_POOL_SIZE			= 4;
	private static final int			EQUIPMENT_OBJECT_POOL_SIZE		= 2;
	private static final int			PORT_OBJECT_POOL_SIZE			= 2;
	private static final int			TRANSPATH_OBJECT_POOL_SIZE		= 4;
	private static final int			KIS_OBJECT_POOL_SIZE			= 1;
	private static final int			MEASUREMENTPORT_OBJECT_POOL_SIZE	= 2;
	private static final int			ME_OBJECT_POOL_SIZE			= 2;

	private static Map				objectPoolMap;						/* Map <Short objectEntity, LRUMap objectPool>*/
	private static ConfigurationObjectLoader	cObjectLoader;
	private static Class				cacheMapClass				= LRUMap.class;

	private ConfigurationStorableObjectPool() {
		// singleton
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1, final int size) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));
		
		cObjectLoader = cObjectLoader1;
		
		addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, size);
		// addObjectPool(ObjectEntities.KISTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.LINKTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, size);		
		addObjectPool(ObjectEntities.LINK_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.USER_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.DOMAIN_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.SERVER_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MCM_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.EQUIPMENT_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.PORT_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.TRANSPATH_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.KIS_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.ME_ENTITY_CODE, size);
		
		polulatePools();		
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		cObjectLoader = cObjectLoader1;
		
		addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, CABLETHREADTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, CHARACTERISTICTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, EQUIPMENTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.LINKTYPE_ENTITY_CODE, LINKTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, PORTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE);

		addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, CHARACTERISTIC_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, PERMATTR_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.USER_ENTITY_CODE, USER_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.LINK_ENTITY_CODE, LINK_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.DOMAIN_ENTITY_CODE, DOMAIN_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.SERVER_ENTITY_CODE, SERVER_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MCM_ENTITY_CODE, MCM_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EQUIPMENT_ENTITY_CODE, EQUIPMENT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.PORT_ENTITY_CODE, PORT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TRANSPATH_ENTITY_CODE, TRANSPATH_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.KIS_ENTITY_CODE, KIS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, MEASUREMENTPORT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ME_ENTITY_CODE, ME_OBJECT_POOL_SIZE);
		
		polulatePools();
	}

	/**
	 * 
	 * @param cObjectLoader1
	 * @param cacheClass
	 *                class must extend LRUMap
	 * @param size
	 * @throws CommunicationException
	 * @throws DatabaseException
	 */
	public static void init(ConfigurationObjectLoader cObjectLoader1, Class cacheClass, final int size) {
		try {
			Class clazz = Class.forName(cacheClass.getName());
			cacheMapClass = clazz;
		} catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default '" 
							 + cacheMapClass.getName() + "'");
		}
		init(cObjectLoader1, size);
	}
    
    public static void serializePool() {
        java.util.Set entityCodeSet = objectPoolMap.keySet();
        for (Iterator it = entityCodeSet.iterator(); it.hasNext();) {
            Short entityCode = (Short) it.next();
            LRUMapSaver.save((LRUMap) objectPoolMap.get(entityCode), ObjectEntities.codeToString(entityCode.shortValue()));  
        }
    }

	private static void addObjectPool(short objectEntityCode, int poolSize) {
		try {
			LRUMap objectPool = null;
            //LRUMap objectPool = new LRUMap(poolSize);
			Constructor constructor = cacheMapClass.getConstructor(new Class[] { int.class});
			Object obj = constructor.newInstance(new Object[] { new Integer(poolSize)});
			if (obj instanceof LRUMap) {
				objectPool = (LRUMap) obj;
				objectPoolMap.put(new Short(objectEntityCode), objectPool);
            } else
            	throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
            			+ " must extends LRUMap");            
        } catch (SecurityException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " SecurityException " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " IllegalArgumentException " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " NoSuchMethodException " + e.getMessage());
		} catch (InstantiationException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " InstantiationException " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " IllegalAccessException " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new UnsupportedOperationException("CacheMapClass " + cacheMapClass.getName()
					+ " InvocationTargetException " + e.getMessage());
		}

	}
	
	private static void polulatePools(){
		try{
			for (Iterator it = objectPoolMap.keySet().iterator(); it.hasNext();) {
				short objectEntityCode = ((Short) it.next()).shortValue();
				List keys = LRUMapSaver.load(ObjectEntities.codeToString(objectEntityCode));
		        if (keys != null)
		        	getStorableObjects(keys, true);		        
			}
		} catch (CommunicationException e) {
            Log.errorException(e);
            Log.errorMessage("ConfigurationStorableObjectPool.polulatePools | Error: " + e.getMessage());
        } catch (DatabaseException e) {
            Log.errorException(e);
            Log.errorMessage("ConfigurationStorableObjectPool.polulatePools | Error: " + e.getMessage());
        }
	}
    
    public static void refresh() throws DatabaseException, CommunicationException {        
        try {         
            Log.debugMessage("ConfigurationStorableObjectPool.refresh | trying to refresh Pool...", Log.DEBUGLEVEL03);
            Set storableObjects = new HashSet();
            Set returnedStorableObjectsIds = new HashSet();
            Set entityCodes = objectPoolMap.keySet();
            
            for (Iterator it = entityCodes.iterator(); it.hasNext();) {
    			Short entityCode = (Short) it.next();
                LRUMap lruMap = (LRUMap) objectPoolMap.get(entityCode);
    			
                for (Iterator it2 = lruMap.iterator(); it2.hasNext();) {
    				storableObjects.add(it2.next());                
    			}
                if (storableObjects == null || storableObjects.isEmpty()) {
                	Log.debugMessage("ConfigurationStorableObjectPool.refresh | LRUMap for '" + ObjectEntities.codeToString(entityCode.shortValue())+ "' entity has no elements",Log.DEBUGLEVEL08);
                    continue;
                }  
                Log.debugMessage("ConfigurationStorableObjectPool.refresh | try refresh LRUMap for '" + ObjectEntities.codeToString(entityCode.shortValue())+ "' entity",Log.DEBUGLEVEL08);
                
                returnedStorableObjectsIds = cObjectLoader.refresh(storableObjects);
                
                getStorableObjects(new ArrayList(returnedStorableObjectsIds), true);
    		}
        } catch (DatabaseException e) {
            Log.errorMessage("ConfigurationStorableObjectPool.refresh | DatabaseException: " + e.getMessage());
            throw new DatabaseException("ConfigurationStorableObjectPool.refresh", e);
        } catch (CommunicationException e) {
            Log.errorMessage("ConfigurationStorableObjectPool.refresh | CommunicationException: " + e.getMessage());
            throw new CommunicationException("ConfigurationStorableObjectPool.refresh", e);
        }
    }

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws DatabaseException, CommunicationException {
		if (objectId != null) {
			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap) objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject) objectPool.get(objectId);
				if (storableObject != null)
					return storableObject;
				else {
					if (useLoader) {
						storableObject = loadStorableObject(objectId);
						if (storableObject != null)
							try {
								putStorableObject(storableObject);
							}
							catch (IllegalObjectEntityException ioee) {
								Log.errorException(ioee);
							}
					}
					return storableObject;
				}
			}
			else {
				Log.errorMessage("ConfigurationStorableObjectPool.getStorableObject | Cannot find object pool for objectId: '"
								+ objectId.toString()
								+ "' entity code: '"
								+ objectEntityCode + "'");
				return null;
			}
		}
		else {
			Log.errorMessage("ConfigurationStorableObjectPool.getStorableObject | NULL identifier supplied");
			return null;
		}
	}

	public static List getStorableObjects(List objectIds, boolean useLoader) throws DatabaseException, CommunicationException {
		List list = null;
		Map objectQueueMap = null;
		if (objectIds != null) {
			for (Iterator it = objectIds.iterator(); it.hasNext();) {
				Identifier objectId = (Identifier) it.next();
				short objectEntityCode = objectId.getMajor();
				Short entityCode = new Short(objectEntityCode);
				LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
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
					.errorMessage("ConfigurationStorableObjectPool.getStorableObjects | Cannot find object pool for objectId: '"
							+ objectId.toString()
							+ "' entity code: '"
							+ ObjectEntities.codeToString(objectEntityCode) + "'");					
				}
			}

		} else {
			Log
					.errorMessage("ConfigurationStorableObjectPool.getStorableObjects | NULL list of identifiers supplied");
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
							putStorableObject(storableObject);
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

	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return getStorableObjectsByConditionButIds(null, condition, useLoader);
	}

	public static List getStorableObjectsByConditionButIds(	List ids,
								StorableObjectCondition condition,
								boolean useLoader) throws ApplicationException {
		List list = null;
		LRUMap objectPool = (LRUMap) objectPoolMap.get(condition.getEntityCode());
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
				if (condition.isNeedMore(list)){
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


	private static StorableObject loadStorableObject(Identifier objectId) throws DatabaseException,
			CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadCharacteristicType(objectId);
				break;
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadCableThreadType(objectId);
				break;
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadEquipmentType(objectId);
				break;
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadLinkType(objectId);
				break;
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadPortType(objectId);
				break;
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadMeasurementPortType(objectId);
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				storableObject = cObjectLoader.loadCharacteristic(objectId);
				break;
			//			case ObjectEntities.PERMATTR_ENTITY_CODE:
			//				storableObject =
			// cObjectLoader.loadPermissionAttributes(objectId);
			//				break;
			case ObjectEntities.USER_ENTITY_CODE:
				storableObject = cObjectLoader.loadUser(objectId);
				break;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				storableObject = cObjectLoader.loadDomain(objectId);
				break;
			case ObjectEntities.SERVER_ENTITY_CODE:
				storableObject = cObjectLoader.loadServer(objectId);
				break;
			case ObjectEntities.MCM_ENTITY_CODE:
				storableObject = cObjectLoader.loadMCM(objectId);
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				storableObject = cObjectLoader.loadEquipment(objectId);
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				storableObject = cObjectLoader.loadPort(objectId);
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				storableObject = cObjectLoader.loadTransmissionPath(objectId);
				break;
			case ObjectEntities.KIS_ENTITY_CODE:
				storableObject = cObjectLoader.loadKIS(objectId);
				break;
			case ObjectEntities.LINK_ENTITY_CODE:
				storableObject = cObjectLoader.loadLink(objectId);
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				storableObject = cObjectLoader.loadMeasurementPort(objectId);
				break;
			case ObjectEntities.ME_ENTITY_CODE:
				storableObject = cObjectLoader.loadMonitoredElement(objectId);
				break;
			default:
				Log
						.errorMessage("ConfigurationStorableObjectPool.loadStorableObject | Unknown entity: "
								+ ObjectEntities.codeToString(objectId.getMajor()));
				storableObject = null;
		}
		return storableObject;
	}

	private static List loadStorableObjects(Short entityCode, List ids) throws DatabaseException,
			CommunicationException {
		List loadedList = null;
		switch (entityCode.shortValue()) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCableThreadTypes(ids);
					break;
				case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCharacteristicTypes(ids);
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadEquipmentTypes(ids);
					break;
				case ObjectEntities.PORTTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadPortTypes(ids);
					break;
				case ObjectEntities.LINKTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadLinkTypes(ids);
					break;
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadMeasurementPortTypes(ids);
					break;
				case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
					loadedList = cObjectLoader.loadCharacteristics(ids);
					break;
				//			case ObjectEntities.PERMATTR_ENTITY_CODE:
				//				loadedList =
				// cObjectLoader.loadPermissionAttributes(ids);
				//				break;
				case ObjectEntities.USER_ENTITY_CODE:
					loadedList = cObjectLoader.loadUsers(ids);
					break;
				case ObjectEntities.DOMAIN_ENTITY_CODE:
					loadedList = cObjectLoader.loadDomains(ids);
					break;
				case ObjectEntities.SERVER_ENTITY_CODE:
					loadedList = cObjectLoader.loadServers(ids);
					break;
				case ObjectEntities.MCM_ENTITY_CODE:
					loadedList = cObjectLoader.loadMCMs(ids);
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					loadedList = cObjectLoader.loadEquipments(ids);
					break;
				case ObjectEntities.PORT_ENTITY_CODE:
					loadedList = cObjectLoader.loadPorts(ids);
					break;
				case ObjectEntities.TRANSPATH_ENTITY_CODE:
					loadedList = cObjectLoader.loadTransmissionPaths(ids);
					break;
				case ObjectEntities.KIS_ENTITY_CODE:
					loadedList = cObjectLoader.loadKISs(ids);
					break;
				case ObjectEntities.LINK_ENTITY_CODE:
					loadedList = cObjectLoader.loadLinks(ids);
					break;
				case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
					loadedList = cObjectLoader.loadMeasurementPorts(ids);
					break;
				case ObjectEntities.ME_ENTITY_CODE:
					loadedList = cObjectLoader.loadMonitoredElements(ids);
					break;
			default:
				Log
						.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entityCode : "
								+ entityCode);
			loadedList = null;
		}
		return loadedList;
	}

	private static List loadStorableObjectsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCableThreadTypesButIds(condition, ids);
					break;
				case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCharacteristicTypesButIds(condition, ids);
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadEquipmentTypesButIds(condition, ids);
					break;
				case ObjectEntities.PORTTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadPortTypesButIds(condition, ids);
					break;
				case ObjectEntities.LINKTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadLinkTypesButIds(condition, ids);
					break;
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadMeasurementPortTypesButIds(condition, ids);
					break;
				case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
					loadedList = cObjectLoader.loadCharacteristicsButIds(condition, ids);
					break;
				//			case ObjectEntities.PERMATTR_ENTITY_CODE:
				//				loadedList =
				// cObjectLoader.loadPermissionAttributessButIds(condition, ids);
				//				break;
				case ObjectEntities.USER_ENTITY_CODE:
					loadedList = cObjectLoader.loadUsersButIds(condition, ids);
					break;
				case ObjectEntities.DOMAIN_ENTITY_CODE:
					loadedList = cObjectLoader.loadDomainsButIds(condition, ids);
					break;
				case ObjectEntities.SERVER_ENTITY_CODE:
					loadedList = cObjectLoader.loadServersButIds(condition, ids);
					break;
				case ObjectEntities.MCM_ENTITY_CODE:
					loadedList = cObjectLoader.loadMCMsButIds(condition, ids);
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					loadedList = cObjectLoader.loadEquipmentsButIds(condition, ids);
					break;
				case ObjectEntities.PORT_ENTITY_CODE:
					loadedList = cObjectLoader.loadPortsButIds(condition, ids);
					break;
				case ObjectEntities.TRANSPATH_ENTITY_CODE:
					loadedList = cObjectLoader.loadTransmissionPathsButIds(condition, ids);
					break;
				case ObjectEntities.KIS_ENTITY_CODE:
					loadedList = cObjectLoader.loadKISsButIds(condition, ids);
					break;
				case ObjectEntities.LINK_ENTITY_CODE:
					loadedList = cObjectLoader.loadLinksButIds(condition, ids);
					break;
				case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
					loadedList = cObjectLoader.loadMeasurementPortsButIds(condition, ids);
					break;
				case ObjectEntities.ME_ENTITY_CODE:
					loadedList = cObjectLoader.loadMonitoredElementsButIds(condition, ids);
					break;
			default:				
				Log
						.errorMessage("ConfigurationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
								+ ObjectEntities.codeToString(entityCode));
				loadedList = null;
		}
		return loadedList;
	}
	
	public static StorableObject putStorableObject(StorableObject storableObject)
			throws IllegalObjectEntityException {
		Identifier objectId = storableObject.getId();
		LRUMap objectPool = (LRUMap) objectPoolMap.get(new Short(objectId.getMajor()));
		if (objectPool != null) { return (StorableObject) objectPool.put(objectId, storableObject); }
		throw new IllegalObjectEntityException(
							"ConfigurationStorableObjectPool.putStorableObject | Illegal object entity: '"
									+ ObjectEntities.codeToString(objectId.getMajor()) + "'",
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
	
	public static void flush(boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException{		 
		List list = new LinkedList();
		for (Iterator it = objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
			if (objectPool != null){
				list.clear();
				for(Iterator poolIt = objectPool.iterator();poolIt.hasNext();){
					StorableObject storableObject = (StorableObject)poolIt.next();
					if (storableObject.isChanged()){
						if (!list.contains(storableObject)){
							list.add(storableObject);
							Log.debugMessage("'" + storableObject.getId() + "' is changed", Log.DEBUGLEVEL10);
						}
					}
				} 
				short code = entityCode.shortValue();
				saveStorableObjects(code, list, force);
				
			} else {
				Log
				.errorMessage("ConfigurationStorableObjectPool.flush | Cannot find object pool for entity code: '"
						+ ObjectEntities.codeToString(entityCode.shortValue())
						+ "'");
			}
		}
	}
	
	private static void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException{
		if (!list.isEmpty()){
			boolean alone = (list.size()==1);
			
			// calculate dependencies to save
			Map dependenciesMap = new HashMap();
			for (Iterator it = list.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				Log.debugMessage("ConfigurationStorableObjectPool.saveStorableObjects | calculate dependencies for '" 
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
						stObj = getStorableObject(id, true);
					} else {
						throw new IllegalDataException("ConfigurationStorableObjectPool.saveStorableObjects | Illegal dependencies Object: " + depItObj.getClass().getName());
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
					Log.debugMessage("ConfigurationStorableObjectPool.saveStorableObjects | recursieve save '" 
									 + ObjectEntities.codeToString(major.shortValue()) + "'", Log.DEBUGLEVEL08);
					// [:]/\/\/\/\/|||||||||||||||||||||||||||[:]
					saveStorableObjects(major.shortValue(), depList, force);
				}
			}
			
			for (Iterator it = list.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				Log.debugMessage("MeasurementStorableObjectPool.saveStorableObjects | save '" 
								 + storableObject.getId() + "'", Log.DEBUGLEVEL08);
			}
			
			switch (code) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader
						.saveCableThreadType(
									(CableThreadType) list
											.get(0),
									force);
				else
					cObjectLoader.saveCableThreadTypes(list, force);
				case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader
								.saveCharacteristicType(
											(CharacteristicType) list
													.get(0),
											force);
					else
						cObjectLoader.saveCharacteristicTypes(list, force);
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveEquipmentType((EquipmentType) list
								.get(0), force);
					else
						cObjectLoader.saveEquipmentTypes(list, force);
					break;
				case ObjectEntities.PORTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.savePortType((PortType) list.get(0),
										force);
					else
						cObjectLoader.savePortTypes(list, force);
					break;
				case ObjectEntities.LINKTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveLinkType((LinkType) list.get(0), force);
					else
						cObjectLoader.saveLinkTypes(list, force);
					break;
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader
								.saveMeasurementPortType(
												(MeasurementPortType) list
														.get(0),
												force);
					else
						cObjectLoader.saveMeasurementPortTypes(list, force);
					break;
				case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveCharacteristic((Characteristic) list
								.get(0), force);
					else
						cObjectLoader.saveCharacteristics(list, force);
					break;
				case ObjectEntities.USER_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveUser((User) list.get(0), force);
					else
						cObjectLoader.saveUsers(list, force);
					break;
				case ObjectEntities.DOMAIN_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveDomain((Domain) list.get(0), force);
					else
						cObjectLoader.saveDomains(list, force);
					break;
				case ObjectEntities.SERVER_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveServer((Server) list.get(0), force);
					else
						cObjectLoader.saveServers(list, force);
					break;
				case ObjectEntities.MCM_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveMCM((MCM) list.get(0), force);
					else
						cObjectLoader.saveMCMs(list, force);
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveEquipment((Equipment) list.get(0),
										force);
					else
						cObjectLoader.saveEquipments(list, force);
					break;
				case ObjectEntities.PORT_ENTITY_CODE:
					if (alone)
						cObjectLoader.savePort((Port) list.get(0), force);
					else
						cObjectLoader.savePorts(list, force);
					break;
				case ObjectEntities.LINK_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveLink((Link) list.get(0), force);
					else
						cObjectLoader.saveLinks(list, force);
					break;
				case ObjectEntities.KIS_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveKIS((KIS) list.get(0), force);
					else
						cObjectLoader.saveKISs(list, force);
					break;
				case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
					if (alone)
						cObjectLoader
								.saveMeasurementPort(
											(MeasurementPort) list
													.get(0),
											force);
					else
						cObjectLoader.saveMeasurementPorts(list, force);
					break;
				case ObjectEntities.ME_ENTITY_CODE:
					if (alone)
						cObjectLoader
								.saveMonitoredElement(
											(MonitoredElement) list
													.get(0),
											force);
					else
						cObjectLoader.saveMonitoredElements(list, force);
					break;

				default:
					Log
							.errorMessage("ConfigurationStorableObjectPool.saveStorableObjects | Unknown entity : '"
									+ ObjectEntities.codeToString(code) + "'");
			}

		}
	}


	public static void cleanChangedStorableObject(Short entityCode) {
		LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
		if (objectPool != null) {
			for (Iterator poolIt = objectPool.iterator(); poolIt.hasNext();) {
				StorableObject storableObject = (StorableObject) poolIt.next();
				if (storableObject.isChanged())
					poolIt.remove();
			}
		}
	}

	public static void cleanChangedStorableObjects() {
		for (Iterator it = objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			cleanChangedStorableObject(entityCode);
		}
	}
    
    public static void delete(Identifier id) throws DatabaseException, CommunicationException {
        Short entityCode = new Short(id.getMajor());
        LRUMap lruMap = (LRUMap) objectPoolMap.get(entityCode);
        lruMap.remove(id);
        try {
            cObjectLoader.delete(id);
        } catch (DatabaseException e) {
            Log.errorMessage("MeasurementStorableObjectPool.delete | DatabaseException: " + e.getMessage());
            throw new DatabaseException("MeasurementStorableObjectPool.refresh", e);
        } catch (CommunicationException e) {
            Log.errorMessage("MeasurementStorableObjectPool.delete | CommunicationException: " + e.getMessage());
            throw new CommunicationException("MeasurementStorableObjectPool.refresh", e);
        }
    }
    
    public static void delete(List ids) throws DatabaseException, CommunicationException {
        for (Iterator it = ids.iterator(); it.hasNext();) {
            Identifier id = (Identifier) it.next();
            Short entityCode = new Short(id.getMajor());
            LRUMap lruMap = (LRUMap) objectPoolMap.get(entityCode);
            lruMap.remove(id);
        }
        try {
            cObjectLoader.delete(ids);
        } catch (DatabaseException e) {
            Log.errorMessage("MeasurementStorableObjectPool.delete | DatabaseException: " + e.getMessage());
            throw new DatabaseException("MeasurementStorableObjectPool.refresh", e);
        } catch (CommunicationException e) {
            Log.errorMessage("MeasurementStorableObjectPool.delete | CommunicationException: " + e.getMessage());
            throw new CommunicationException("MeasurementStorableObjectPool.refresh", e);
        }
    }

}

