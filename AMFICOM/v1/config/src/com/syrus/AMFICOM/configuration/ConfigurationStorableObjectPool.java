/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.5 2004/08/22 18:49:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Hashtable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2004/08/22 18:49:19 $
 * @author $Author: arseniy $
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

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) {
		if (objectId != null) {
			short objectEntityCode = objectId.getMajor();
			LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectEntityCode));
			if (objectPool != null) {
				StorableObject storableObject = (StorableObject)objectPool.get(objectId);
				if (storableObject != null)
					return storableObject;
				else {
					if (useLoader) {
						try {
							storableObject = loadStorableObject(objectId);
							if (storableObject != null)
								putStorableObject(storableObject);
						}
						catch (Exception e) {
							Log.errorException(e);
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

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		Identifier objectId = storableObject.getId();
		LRUMap objectPool = (LRUMap)objectPoolMap.get(new Short(objectId.getMajor()));
		if (objectPool != null) {
			return (StorableObject)objectPool.put(objectId, storableObject);
		}
		throw new IllegalObjectEntityException("ConfigurationStorableObjectPool.putStorableObject | Illegal object entity: '" + objectId.getObjectEntity() + "'", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
	}
}
