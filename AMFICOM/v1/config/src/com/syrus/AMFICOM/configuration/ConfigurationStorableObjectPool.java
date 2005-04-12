/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.75 2005/04/12 16:25:09 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.75 $, $Date: 2005/04/12 16:25:09 $
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

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				return cObjectLoader.loadCableThreadTypes(ids);
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				return cObjectLoader.loadCableLinkTypes(ids);
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				return cObjectLoader.loadEquipmentTypes(ids);
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				return cObjectLoader.loadTransmissionPathTypes(ids);
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				return cObjectLoader.loadPortTypes(ids);
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				return cObjectLoader.loadLinkTypes(ids);
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				return cObjectLoader.loadMeasurementPortTypes(ids);
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				return cObjectLoader.loadEquipments(ids);
			case ObjectEntities.PORT_ENTITY_CODE:
				return cObjectLoader.loadPorts(ids);
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				return cObjectLoader.loadTransmissionPaths(ids);
			case ObjectEntities.KIS_ENTITY_CODE:
				return cObjectLoader.loadKISs(ids);
			case ObjectEntities.LINK_ENTITY_CODE:
				return cObjectLoader.loadLinks(ids);
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				return cObjectLoader.loadMeasurementPorts(ids);
			case ObjectEntities.ME_ENTITY_CODE:
				return cObjectLoader.loadMonitoredElements(ids);
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				return Collections.EMPTY_SET;
		}
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

	protected void saveStorableObjects(final Set storableObjects,
			final boolean force)
			throws ApplicationException {
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final boolean singleton = storableObjects.size() == 1;
		switch (entityCode) {
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveCableThreadType((CableThreadType) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveCableThreadTypes(storableObjects, force);
				break;
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveCableLinkType((CableLinkType) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveCableLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveEquipmentType((EquipmentType) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveEquipmentTypes(storableObjects, force);
				break;
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				if (singleton)
					cObjectLoader.savePortType((PortType) storableObjects.iterator().next(), force);
				else
					cObjectLoader.savePortTypes(storableObjects, force);
				break;
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveLinkType((LinkType) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveMeasurementPortType((MeasurementPortType) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveMeasurementPortTypes(storableObjects, force);
				break;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveEquipment((Equipment) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveEquipments(storableObjects, force);
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				if (singleton)
					cObjectLoader.savePort((Port) storableObjects.iterator().next(), force);
				else
					cObjectLoader.savePorts(storableObjects, force);
				break;
			case ObjectEntities.LINK_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveLink((Link) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveLinks(storableObjects, force);
				break;
			case ObjectEntities.KIS_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveKIS((KIS) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveKISs(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveMeasurementPort((MeasurementPort) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveMeasurementPorts(storableObjects, force);
				break;
			case ObjectEntities.ME_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveMonitoredElement((MonitoredElement) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveMonitoredElements(storableObjects, force);
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveTransmissionPath((TransmissionPath) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveTransmissionPaths(storableObjects, force);
				break;
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				if (singleton)
					cObjectLoader.saveTransmissionPathType((TransmissionPathType) storableObjects.iterator().next(), force);
				else
					cObjectLoader.saveTransmissionPathTypes(storableObjects, force);
				break;

			default:
				Log.errorMessage("ConfigurationStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
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

	public static void delete(final Set identifiables) {
		instance.deleteImpl(identifiables);
	}

	protected void deleteStorableObject(Identifier id) {
		cObjectLoader.delete(id);
	}

	protected void deleteStorableObjects(final Set identifiables) {
		cObjectLoader.delete(identifiables);
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

	public static void truncateObjectPool(final short entityCode) {
		instance.truncateObjectPoolImpl(entityCode);
	}

}
