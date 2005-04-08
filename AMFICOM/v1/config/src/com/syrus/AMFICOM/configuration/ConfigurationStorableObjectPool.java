/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.73 2005/04/08 14:12:16 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

/**
 * @version $Revision: 1.73 $, $Date: 2005/04/08 14:12:16 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class ConfigurationStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 16; /* Number of entities */

	private static final int CABLETHREADTYPE_OBJECT_POOL_SIZE = 4;
	private static final int CABLELINKTYPE_OBJECT_POOL_SIZE = 4;
	private static final int EQUIPMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int TRANSPATHTYPE_OBJECT_POOL_SIZE = 1;
	private static final int LINKTYPE_OBJECT_POOL_SIZE = 2;
	// private static final int KISTYPE_OBJECT_POOL_SIZE = 1;
	private static final int PORTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE = 1;

	private static final int LINK_OBJECT_POOL_SIZE = 2;
	private static final int EQUIPMENT_OBJECT_POOL_SIZE = 2;
	private static final int PORT_OBJECT_POOL_SIZE = 2;
	private static final int TRANSPATH_OBJECT_POOL_SIZE = 4;
	private static final int KIS_OBJECT_POOL_SIZE = 1;
	private static final int MEASUREMENTPORT_OBJECT_POOL_SIZE = 2;
	private static final int ME_OBJECT_POOL_SIZE = 2;

	private static ConfigurationObjectLoader cObjectLoader;
	private static ConfigurationStorableObjectPool instance;

	private ConfigurationStorableObjectPool() {
		// singleton
		super(ObjectGroupEntities.CONFIGURATION_GROUP_CODE);
	}

	private ConfigurationStorableObjectPool(Class cacheMapClass) {
		super(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, cacheMapClass);
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1, final int size) {
		if (instance == null)
			instance = new ConfigurationStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		cObjectLoader = cObjectLoader1;

		instance.addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, size);
		// instance.addObjectPool(ObjectEntities.KISTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.LINKTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.LINK_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EQUIPMENT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.PORT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.TRANSPATH_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.KIS_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.ME_ENTITY_CODE, size);

		instance.populatePools();
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1) {
		if (instance == null)
			instance = new ConfigurationStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new HashMap(OBJECT_POOL_MAP_SIZE));

		cObjectLoader = cObjectLoader1;

		instance.addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, CABLETHREADTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, CABLELINKTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, EQUIPMENTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, TRANSPATHTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.LINKTYPE_ENTITY_CODE, LINKTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, PORTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.LINK_ENTITY_CODE, LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EQUIPMENT_ENTITY_CODE, EQUIPMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PORT_ENTITY_CODE, PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TRANSPATH_ENTITY_CODE, TRANSPATH_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.KIS_ENTITY_CODE, KIS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, MEASUREMENTPORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.ME_ENTITY_CODE, ME_OBJECT_POOL_SIZE);

		instance.populatePools();
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new ConfigurationStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
			instance = new ConfigurationStorableObjectPool();
		}
		init(cObjectLoader1, size);
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1, Class cacheClass) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new ConfigurationStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
			instance = new ConfigurationStorableObjectPool();
		}
		init(cObjectLoader1);
	}

	public static void refresh() throws ApplicationException {
		instance.refreshImpl();
	}

  protected Set refreshStorableObjects(Set storableObjects) throws ApplicationException{
		return cObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static Set getStorableObjects(Set objectIds, boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static Set getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static Set getStorableObjectsByConditionButIds(Set ids,
								StorableObjectCondition condition,
								boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws ApplicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadCableThreadType(objectId);
				break;
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadCableLinkType(objectId);
				break;
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadEquipmentType(objectId);
				break;
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				storableObject = cObjectLoader.loadTransmissionPathType(objectId);
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
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObject | Unknown entity: '" + ObjectEntities.codeToString(objectId.getMajor()) + "', entity code: " + objectId.getMajor());
				storableObject = null;
		}
		return storableObject;
	}

	protected Set loadStorableObjects(Short entityCode, Set ids) throws ApplicationException {
		Set loadedList = null;
		switch (entityCode.shortValue()) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCableThreadTypes(ids);
					break;
				case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCableLinkTypes(ids);
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadEquipmentTypes(ids);
					break;
				case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadTransmissionPathTypes(ids);
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
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode.shortValue()) + "', entity code: " + entityCode);
				loadedList = null;
		}
		return loadedList;
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		Set loadedCollection = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadCableThreadTypesButIds(condition, ids);
					break;
				case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadCableLinkTypesButIds(condition, ids);
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadEquipmentTypesButIds(condition, ids);
					break;
				case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadTransmissionPathTypesButIds(condition, ids);
					break;
				case ObjectEntities.PORTTYPE_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadPortTypesButIds(condition, ids);
					break;
				case ObjectEntities.LINKTYPE_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadLinkTypesButIds(condition, ids);
					break;
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadMeasurementPortTypesButIds(condition, ids);
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadEquipmentsButIds(condition, ids);
					break;
				case ObjectEntities.PORT_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadPortsButIds(condition, ids);
					break;
				case ObjectEntities.TRANSPATH_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadTransmissionPathsButIds(condition, ids);
					break;
				case ObjectEntities.KIS_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadKISsButIds(condition, ids);
					break;
				case ObjectEntities.LINK_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadLinksButIds(condition, ids);
					break;
				case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadMeasurementPortsButIds(condition, ids);
					break;
				case ObjectEntities.ME_ENTITY_CODE:
					loadedCollection = cObjectLoader.loadMonitoredElementsButIds(condition, ids);
					break;
			default:				
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedCollection = null;
		}
		return loadedCollection;
	}

	//public static void save()

	protected void saveStorableObjects(short code, Set collection, boolean force) throws ApplicationException {
		if (!collection.isEmpty()) {
			boolean alone = (collection.size() == 1);
			switch (code) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveCableThreadType((CableThreadType) collection.iterator().next(), force);
					else
						cObjectLoader.saveCableThreadTypes(collection, force);
					break;
				case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveCableLinkType((CableLinkType) collection.iterator().next(), force);
					else
						cObjectLoader.saveCableLinkTypes(collection, force);
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveEquipmentType((EquipmentType) collection.iterator().next(), force);
					else
						cObjectLoader.saveEquipmentTypes(collection, force);
					break;
				case ObjectEntities.PORTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.savePortType((PortType) collection.iterator().next(), force);
					else
						cObjectLoader.savePortTypes(collection, force);
					break;
				case ObjectEntities.LINKTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveLinkType((LinkType) collection.iterator().next(), force);
					else
						cObjectLoader.saveLinkTypes(collection, force);
					break;
				case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveMeasurementPortType((MeasurementPortType) collection.iterator().next(), force);
					else
						cObjectLoader.saveMeasurementPortTypes(collection, force);
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveEquipment((Equipment) collection.iterator().next(), force);
					else
						cObjectLoader.saveEquipments(collection, force);
					break;
				case ObjectEntities.PORT_ENTITY_CODE:
					if (alone)
						cObjectLoader.savePort((Port) collection.iterator().next(), force);
					else
						cObjectLoader.savePorts(collection, force);
					break;
				case ObjectEntities.LINK_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveLink((Link) collection.iterator().next(), force);
					else
						cObjectLoader.saveLinks(collection, force);
					break;
				case ObjectEntities.KIS_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveKIS((KIS) collection.iterator().next(), force);
					else
						cObjectLoader.saveKISs(collection, force);
					break;
				case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveMeasurementPort((MeasurementPort) collection.iterator().next(), force);
					else
						cObjectLoader.saveMeasurementPorts(collection, force);
					break;
				case ObjectEntities.ME_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveMonitoredElement((MonitoredElement) collection.iterator().next(), force);
					else
						cObjectLoader.saveMonitoredElements(collection, force);
					break;
				case ObjectEntities.TRANSPATH_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveTransmissionPath((TransmissionPath) collection.iterator().next(), force);
					else
						cObjectLoader.saveTransmissionPaths(collection, force);
					break;
				case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveTransmissionPathType((TransmissionPathType) collection.iterator().next(), force);
					else
						cObjectLoader.saveTransmissionPathTypes(collection, force);
					break;

				default:
					Log.errorMessage("ConfigurationStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(code) + "', entity code: " + code);
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		return instance.fromTransferableImpl(id, transferable);
	}

	public static void flush(boolean force) throws ApplicationException {		 
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}

	public static void delete(Identifier id) {
		instance.deleteImpl(id);
	}

	public static void delete(Set objects) throws IllegalDataException {
		instance.deleteImpl(objects);
	}

	protected void deleteStorableObject(Identifier id) throws IllegalDataException {
		cObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(Set objects) throws IllegalDataException {
		cObjectLoader.delete(objects);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}

}
