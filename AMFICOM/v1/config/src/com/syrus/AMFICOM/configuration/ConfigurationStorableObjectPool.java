/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.15 2004/10/03 12:43:06 bob Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2004/10/03 12:43:06 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class ConfigurationStorableObjectPool {
	private static final int OBJECT_POOL_MAP_SIZE = 16;	/*	Number of entities*/

	private static final int CHARACTERISTICTYPE_OBJECT_POOL_SIZE = 9;
	private static final int EQUIPMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int PORTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE = 1;

	private static final int CHARACTERISTIC_OBJECT_POOL_SIZE = 4;
	private static final int PERMATTR_OBJECT_POOL_SIZE = 4;
	private static final int USER_OBJECT_POOL_SIZE = 4;
	private static final int DOMAIN_OBJECT_POOL_SIZE = 4;
	private static final int SERVER_OBJECT_POOL_SIZE = 4;
	private static final int MCM_OBJECT_POOL_SIZE = 4;
	private static final int EQUIPMENT_OBJECT_POOL_SIZE = 2;
	private static final int PORT_OBJECT_POOL_SIZE = 2;
	private static final int TRANSPATH_OBJECT_POOL_SIZE = 4;
	private static final int KIS_OBJECT_POOL_SIZE = 1;
	private static final int MEASUREMENTPORT_OBJECT_POOL_SIZE = 2;
	private static final int ME_OBJECT_POOL_SIZE = 2;

	private static Map objectPoolMap; /*	Map <String objectEntity, LRUMap objectPool>	*/
	private static ConfigurationObjectLoader cObjectLoader;
	private static Class cacheMapClass = LRUMap.class;

	private ConfigurationStorableObjectPool() {
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1, final int size) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, size);
		addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, size);
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

		cObjectLoader = cObjectLoader1;
	}

	
	public static void init(ConfigurationObjectLoader cObjectLoader1) {
		objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		addObjectPool(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, CHARACTERISTICTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, EQUIPMENTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, PORTTYPE_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE);

		addObjectPool(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, CHARACTERISTIC_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.PERMATTR_ENTITY_CODE, PERMATTR_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.USER_ENTITY_CODE, USER_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.DOMAIN_ENTITY_CODE, DOMAIN_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.SERVER_ENTITY_CODE, SERVER_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MCM_ENTITY_CODE, MCM_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.EQUIPMENT_ENTITY_CODE, EQUIPMENT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.PORT_ENTITY_CODE, PORT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.TRANSPATH_ENTITY_CODE, TRANSPATH_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.KIS_ENTITY_CODE, KIS_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, MEASUREMENTPORT_OBJECT_POOL_SIZE);
		addObjectPool(ObjectEntities.ME_ENTITY_CODE, ME_OBJECT_POOL_SIZE);

		cObjectLoader = cObjectLoader1;
	}
	
	/**
	 * 
	 * @param cObjectLoader1
	 * @param cacheClass class must extend LRUMap 
	 * @param size
	 */
	public static void init(ConfigurationObjectLoader cObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
		} catch (ClassNotFoundException e) {
			// empty
		}
		if (clazz != null)
			cacheMapClass = clazz;
		init(cObjectLoader1, size);
	}

	private static void addObjectPool(short objectEntityCode, int poolSize) {
		try {
			// LRUMap objectPool = new LRUMap(poolSize);
			Constructor constructor = cacheMapClass.getConstructor(new Class[] { int.class});
			Object obj = constructor
					.newInstance(new Object[] { new Integer(poolSize)});
			if (obj instanceof LRUMap){
				LRUMap objectPool = (LRUMap)obj;
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

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws DatabaseException, CommunicationException {
		if (objectId != null) {
			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject)objectPool.get(objectId);
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
				Log.errorMessage("ConfigurationStorableObjectPool.getStorableObject | Cannot find object pool for objectId: '" + objectId.toString() + "' entity code: '" + objectEntityCode + "'");
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
                LRUMap objectPool = (LRUMap)objectPoolMap.get(entityCode);
                StorableObject storableObject = null;
                if (objectPool != null) {
                    storableObject = (StorableObject)objectPool.get(objectId);
                    if (storableObject != null){
                        if (list == null)
                            list = new LinkedList();
                        list.add(storableObject);
                    }
                }
                if (storableObject == null) {
                    Log.errorMessage("ConfigurationStorableObjectPool.getStorableObjects | Cannot find object pool for objectId: '" + objectId.toString() + "' entity code: '" + objectEntityCode + "'");
                    if (useLoader) {
                        if (objectQueueMap == null)
                            objectQueueMap = new HashMap();
                        List objectQueue = (List)objectQueueMap.get(entityCode);
                        if (objectQueue == null){
                            objectQueue = new LinkedList();
                            objectQueueMap.put(entityCode, objectQueue);
                        }
                        objectQueue.add(objectId);              
                    }
                }           
            }
            
        }
        else {
            Log.errorMessage("ConfigurationStorableObjectPool.getStorableObjects | NULL list of identifiers supplied");
        }

        if (objectQueueMap != null){
        	  if (list == null)
                list = new LinkedList();
            for (Iterator it = objectQueueMap.keySet().iterator(); it.hasNext();) {
                Short entityCode = (Short) it.next();
                List objectQueue = (List)objectQueueMap.get(entityCode);
                List storableObjects = loadStorableObjects(entityCode, objectQueue);
                if (storableObjects != null) {
                    try {
                        for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
                            StorableObject storableObject = (StorableObject) iter.next();
                            putStorableObject(storableObject);
                            list.add(storableObject);
                        }                       
                    }
                    catch (IllegalObjectEntityException ioee) {
                        Log.errorException(ioee);
                    }
                }
            }
        }
        
        return list;
    }
	
	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader) throws ApplicationException {
		List list = null;
		LRUMap objectPool = (LRUMap) objectPoolMap.get(condition.getEntityCode());
		if (objectPool != null) {
			list = new LinkedList();
			for (Iterator it = objectPool.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if (condition.isConditionTrue(storableObject))
					list.add(storableObject);
			}			
			
			List loadedList = null;
			
			if (useLoader){								
				List idsList = new ArrayList(list.size());
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					StorableObject storableObject = (StorableObject) iter.next();
					idsList.add(storableObject.getId());					
				}
				
				loadedList = loadStorableObjectsButIds(condition, idsList);
			}
			
			for (Iterator it = list.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				objectPool.get(storableObject);				
			}
			
			if (loadedList!=null){
				for (Iterator it = loadedList.iterator(); it.hasNext();) {
					StorableObject storableObject = (StorableObject) it.next();
					objectPool.put(storableObject.getId(), storableObject);
					list.add(storableObject);
				}
			}

		}

		return list;
	}


    
	public static List getStorableObjectsByDomain(short entityCode, Domain domain) throws DatabaseException,
			CommunicationException {
		List list = null;
		LRUMap objectPool = (LRUMap) objectPoolMap.get(new Short(entityCode));
		if (objectPool != null) {
			list = new LinkedList();
			for (Iterator it = objectPool.iterator(); it.hasNext();) {
				StorableObject storableObject = (StorableObject) it.next();
				if (domain != null) {
					/**
					 * TODO check for entites
					 */
					switch (entityCode) {
						case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
							{
								CharacteristicType characteristicType = (CharacteristicType)storableObject;
								list.add(characteristicType);
							}
							break;
						case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
							{
								EquipmentType equipmentType = (EquipmentType)storableObject;
								list.add(equipmentType);
							}
							break;
						case ObjectEntities.PORTTYPE_ENTITY_CODE:
							{
								PortType portType = (PortType)storableObject;
								list.add(portType);
							}
							break;
						case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
							{
								MeasurementPortType measurementPortType = (MeasurementPortType)storableObject;
								list.add(measurementPortType);
							}
							break;
						case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
							{
								Characteristic characteristic = (Characteristic)storableObject;
								list.add(characteristic);
							}
							break;
//						case ObjectEntities.PERMATTR_ENTITY_CODE:
//							storableObject = cObjectLoader.loadPermissionAttributes(objectId);
//							break;
						case ObjectEntities.USER_ENTITY_CODE:
							User user = (User)storableObject;
							list.add(user);
							break;
						case ObjectEntities.DOMAIN_ENTITY_CODE:
							Domain domain2 = (Domain)storableObject;
							if (domain2.isChild(domain))
								list.add(domain2);
							break;
						case ObjectEntities.SERVER_ENTITY_CODE:
							{
								Server server = (Server) storableObject;
								Domain serverDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(server.getDomainId(), true);
								if (serverDomain.isChild(domain))
									list.add(server);
							}
							break;
						case ObjectEntities.MCM_ENTITY_CODE:	
							{
								MCM mcm = (MCM)storableObject;
								Domain mcmDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(mcm.getDomainId(), true);
								if (mcmDomain.isChild(domain))
									list.add(mcm);
							}
							break;
						case ObjectEntities.EQUIPMENT_ENTITY_CODE:
							{
								Equipment equipment = (Equipment)storableObject;
								Domain eqDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(equipment.getDomainId(), true);
								if (eqDomain.isChild(domain))
									list.add(equipment);								
							}
							break;
						case ObjectEntities.PORT_ENTITY_CODE:
							{
								Port port = (Port)storableObject;
								Equipment equipment = (Equipment)ConfigurationStorableObjectPool.getStorableObject(port.getEquipmentId(), true);
								Domain eqDomain = (Domain)ConfigurationStorableObjectPool.getStorableObject(equipment.getDomainId(), true);
								if (eqDomain.isChild(domain))
									list.add(port);								
							}
							break;
						case ObjectEntities.TRANSPATH_ENTITY_CODE:
							{
								TransmissionPath transmissionPath = (TransmissionPath) storableObject;
								Domain pathDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(transmissionPath.getDomainId(), true);
								if (pathDomain.isChild(domain))
									list.add(transmissionPath);
								
							}
							break;
						case ObjectEntities.KIS_ENTITY_CODE:
							{
								KIS kis = (KIS)storableObject;
								Domain kisDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(kis.getDomainId(), true);
								if (kisDomain.isChild(domain))
									list.add(kis);
							}
							break;
						case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
							{
								MeasurementPort measurementPort = (MeasurementPort)storableObject;
								KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
								Domain kisDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(kis.getDomainId(), true);
								if (kisDomain.isChild(domain))
									list.add(measurementPort);
							}
							break;
						case ObjectEntities.ME_ENTITY_CODE:
							{
								MonitoredElement me = (MonitoredElement)storableObject;
								Domain meDomain = (Domain) ConfigurationStorableObjectPool.getStorableObject(me.getDomainId(), true);
								if (meDomain.isChild(domain)) 
									list.add(me);
							}
							break;
						default:
							list.add(storableObject);
							break;
		
					}
		
				} else {
					list.add(storableObject);
				}
		
			}
		}
		return list;
		}
	
    private static StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadCharacteristicType(objectId);
				break;
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadEquipmentType(objectId);
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
//				storableObject = cObjectLoader.loadPermissionAttributes(objectId);
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
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				storableObject = cObjectLoader.loadMeasurementPort(objectId);
				break;
			case ObjectEntities.ME_ENTITY_CODE:
				storableObject = cObjectLoader.loadMonitoredElement(objectId);
				break;
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObject | Unknown entity: " + objectId.getObjectEntity());
				storableObject = null;
		}
		return storableObject;
	}
    
    private static List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
        List storableObjects;
        switch (entityCode.shortValue()) {
            case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
                storableObjects = cObjectLoader.loadCharacteristicTypes(ids);
                break;
            case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
                storableObjects = cObjectLoader.loadEquipmentTypes(ids);
                break;
            case ObjectEntities.PORTTYPE_ENTITY_CODE:
                storableObjects = cObjectLoader.loadPortTypes(ids);
                break;
            case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
                storableObjects = cObjectLoader.loadMeasurementPortTypes(ids);
                break;
            case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
                storableObjects = cObjectLoader.loadCharacteristics(ids);
                break;
            case ObjectEntities.USER_ENTITY_CODE:
                storableObjects = cObjectLoader.loadUsers(ids);
                break;
            case ObjectEntities.DOMAIN_ENTITY_CODE:
                storableObjects = cObjectLoader.loadDomains(ids);
                break;
            case ObjectEntities.SERVER_ENTITY_CODE:
                storableObjects = cObjectLoader.loadServers(ids);
                break;
            case ObjectEntities.MCM_ENTITY_CODE:
                storableObjects = cObjectLoader.loadMCMs(ids);
                break;
            case ObjectEntities.EQUIPMENT_ENTITY_CODE:
                storableObjects = cObjectLoader.loadEquipments(ids);
                break;
            case ObjectEntities.PORT_ENTITY_CODE:
                storableObjects = cObjectLoader.loadPorts(ids);
                break;
            case ObjectEntities.KIS_ENTITY_CODE:
                storableObjects = cObjectLoader.loadKISs(ids);
                break;
            case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
                storableObjects = cObjectLoader.loadMeasurementPorts(ids);
                break;
            case ObjectEntities.ME_ENTITY_CODE:
                storableObjects = cObjectLoader.loadMonitoredElements(ids);
                break;
            default:
                Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entityCode : " + entityCode);
                storableObjects = null;
        }
        return storableObjects;
    }

	private static List loadStorableObjectsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
		CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
	        case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
	            loadedList = cObjectLoader.loadCharacteristicTypesButIds(condition, ids);
	            break;
	        case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
	            loadedList = cObjectLoader.loadEquipmentTypesButIds(condition, ids);
	            break;
	        case ObjectEntities.PORTTYPE_ENTITY_CODE:
	            loadedList = cObjectLoader.loadPortTypesButIds(condition, ids);
	            break;
	        case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
	            loadedList = cObjectLoader.loadMeasurementPortTypesButIds(condition, ids);
	            break;
	        case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
	            loadedList = cObjectLoader.loadCharacteristicsButIds(condition, ids);
	            break;
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
	        case ObjectEntities.KIS_ENTITY_CODE:
	            loadedList = cObjectLoader.loadKISsButIds(condition, ids);
	            break;
	        case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
	            loadedList = cObjectLoader.loadMeasurementPortsButIds(condition, ids);
	            break;
	        case ObjectEntities.ME_ENTITY_CODE:
	            loadedList = cObjectLoader.loadMonitoredElementsButIds(condition, ids);
	            break;
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: "
						+ ObjectEntities.codeToString(entityCode));
				loadedList = null;
		}		
		return loadedList;
	}

    
	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {		
		Identifier objectId = storableObject.getId();
		LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectId.getMajor()));
		if (objectPool != null) {
			return (StorableObject)objectPool.put(objectId, storableObject);
		}
		throw new IllegalObjectEntityException("ConfigurationStorableObjectPool.putStorableObject | Illegal object entity: '" + objectId.getObjectEntity() + "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
	
	public static void flush(boolean force) throws VersionCollisionException, DatabaseException, CommunicationException{		 
		List list = new LinkedList();
		for (Iterator it = objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
			if (objectPool != null){
				list.clear();
				for(Iterator poolIt = objectPool.iterator();poolIt.hasNext();){
					StorableObject storableObject = (StorableObject)poolIt.next();
					if (storableObject.isChanged())
						list.add(storableObject);				
				}
				
				short code = entityCode.shortValue();
				if (!list.isEmpty()){
					boolean alone = (list.size()==1);
					switch (code) {
			            case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveCharacteristicType((CharacteristicType)list.get(0), force);
			            	else 
			            		cObjectLoader.saveCharacteristicTypes(list, force);
			                break;
			            case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveEquipmentType((EquipmentType)list.get(0), force);
			            	else 
			            		cObjectLoader.saveEquipmentTypes(list, force);
			                break;
			            case ObjectEntities.PORTTYPE_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.savePortType((PortType)list.get(0), force);
			            	else 
			            		cObjectLoader.savePortTypes(list, force);
			                break;
			            case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveMeasurementPortType((MeasurementPortType)list.get(0), force);
			            	else 
			            		cObjectLoader.saveMeasurementPortTypes(list, force);
			                break;
			            case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveCharacteristic((Characteristic)list.get(0), force);
			            	else 
			            		cObjectLoader.saveCharacteristics(list, force);
			                break;
			            case ObjectEntities.USER_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveUser((User)list.get(0), force);
			            	else 
			            		cObjectLoader.saveUsers(list, force);
			                break;
			            case ObjectEntities.DOMAIN_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveDomain((Domain)list.get(0), force);
			            	else 
			            		cObjectLoader.saveDomains(list, force);
			                break;
			            case ObjectEntities.SERVER_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveServer((Server)list.get(0), force);
			            	else 
			            		cObjectLoader.saveServers(list, force);
			                break;
			            case ObjectEntities.MCM_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveMCM((MCM)list.get(0), force);
			            	else 
			            		cObjectLoader.saveMCMs(list, force);
			                break;
			            case ObjectEntities.EQUIPMENT_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveEquipment((Equipment)list.get(0), force);
			            	else 
			            		cObjectLoader.saveEquipments(list, force);
			                break;
			            case ObjectEntities.PORT_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.savePort((Port)list.get(0), force);
			            	else 
			            		cObjectLoader.savePorts(list, force);
			                break;
			            case ObjectEntities.KIS_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveKIS((KIS)list.get(0), force);
			            	else 
			            		cObjectLoader.saveKISs(list, force);
			                break;
			            case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveMeasurementPort((MeasurementPort)list.get(0), force);
			            	else 
			            		cObjectLoader.saveMeasurementPorts(list, force);
			                break;
			            case ObjectEntities.ME_ENTITY_CODE:
			            	if (alone)
			            		cObjectLoader.saveMonitoredElement((MonitoredElement)list.get(0), force);
			            	else 
			            		cObjectLoader.saveMonitoredElements(list, force);
			                break;

						default:
							Log
									.errorMessage("ConfigurationStorableObjectPool.flush | Unknown entityCode : "
											+ entityCode);
					}

				}
			}
		}
	}
	
	public static void cleanChangedStorableObject(Short entityCode){
		LRUMap objectPool = (LRUMap) objectPoolMap.get(entityCode);
		if (objectPool != null){
			for(Iterator poolIt = objectPool.iterator();poolIt.hasNext();){
				StorableObject storableObject = (StorableObject)poolIt.next();
				if (storableObject.isChanged())
					poolIt.remove();				
			}
		}
	}
	
	public static void cleanChangedStorableObjects(){
		for (Iterator it = objectPoolMap.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			cleanChangedStorableObject(entityCode);
		}
	}

}
