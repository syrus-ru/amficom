/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.7 2004/09/14 15:49:09 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2004/09/14 15:49:09 $
 * @author $Author: max $
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

	private ConfigurationStorableObjectPool() {
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

	private static void addObjectPool(short objectEntityCode, int poolSize) {
		LRUMap objectPool = new LRUMap(poolSize);
		objectPoolMap.put(new Short(objectEntityCode), objectPool);
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
                LRUMap objectPool = (LRUMap)objectPoolMap.get(objectId);
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
    
	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		Identifier objectId = storableObject.getId();
		LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectId.getMajor()));
		if (objectPool != null) {
			return (StorableObject)objectPool.put(objectId, storableObject);
		}
		throw new IllegalObjectEntityException("ConfigurationStorableObjectPool.putStorableObject | Illegal object entity: '" + objectId.getObjectEntity() + "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
