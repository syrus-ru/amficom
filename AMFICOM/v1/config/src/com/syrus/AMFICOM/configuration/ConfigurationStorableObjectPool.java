/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.58 2005/02/08 09:27:20 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.58 $, $Date: 2005/02/08 09:27:20 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class ConfigurationStorableObjectPool extends StorableObjectPool {

	private static final int			OBJECT_POOL_MAP_SIZE			= 16;		/* Number of entities */

	private static final int			CABLETHREADTYPE_OBJECT_POOL_SIZE	= 4;
	private static final int			EQUIPMENTTYPE_OBJECT_POOL_SIZE		= 1;
	private static final int			LINKTYPE_OBJECT_POOL_SIZE	= 2;
	// private static final int			KISTYPE_OBJECT_POOL_SIZE		= 1;
	private static final int			PORTTYPE_OBJECT_POOL_SIZE		= 1;
	private static final int			MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE	= 1;

	private static final int			LINK_OBJECT_POOL_SIZE	= 2;
	private static final int			EQUIPMENT_OBJECT_POOL_SIZE		= 2;
	private static final int			PORT_OBJECT_POOL_SIZE			= 2;
	private static final int			TRANSPATH_OBJECT_POOL_SIZE		= 4;
	private static final int			KIS_OBJECT_POOL_SIZE			= 1;
	private static final int			MEASUREMENTPORT_OBJECT_POOL_SIZE	= 2;
	private static final int			ME_OBJECT_POOL_SIZE			= 2;

	private static ConfigurationObjectLoader	cObjectLoader;
	private static ConfigurationStorableObjectPool instance;

	private ConfigurationStorableObjectPool() {
		// singleton
	}

	private ConfigurationStorableObjectPool(Class cacheMapClass) {
		super(cacheMapClass);
	}

	public static void init(ConfigurationObjectLoader cObjectLoader1, final int size) {
		if (instance == null)
			instance = new ConfigurationStorableObjectPool();

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		cObjectLoader = cObjectLoader1;

		instance.addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, size);
		instance.addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, size);
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

		instance.objectPoolMap = Collections.synchronizedMap(new Hashtable(OBJECT_POOL_MAP_SIZE));

		cObjectLoader = cObjectLoader1;

		instance.addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, CABLETHREADTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, EQUIPMENTTYPE_OBJECT_POOL_SIZE);
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

	/**
	 * 
	 * @param cObjectLoader1
	 * @param cacheClass
	 *                class must extend LRUMap
	 * @param size
	 */
	public static void init(ConfigurationObjectLoader cObjectLoader1, Class cacheClass, final int size) {
		Class clazz = null;
		try {
			clazz = Class.forName(cacheClass.getName());
			instance = new ConfigurationStorableObjectPool(clazz);
		}
		catch (ClassNotFoundException e) {
			Log.errorMessage("Cache class '" + cacheClass.getName() +"' cannot be found, use default");
		}
		init(cObjectLoader1, size);
	}

	public static void refresh() throws DatabaseException, CommunicationException {
		instance.refreshImpl();
	}

  protected Set refreshStorableObjects(Set storableObjects) throws CommunicationException, DatabaseException{
		return cObjectLoader.refresh(storableObjects);
	}

	public static StorableObject getStorableObject(Identifier objectId, boolean useLoader) throws DatabaseException, CommunicationException {
		return instance.getStorableObjectImpl(objectId, useLoader);
	}

	public static List getStorableObjects(List objectIds, boolean useLoader) throws DatabaseException, CommunicationException {
		return instance.getStorableObjectsImpl(objectIds, useLoader);
	}

	public static List getStorableObjectsByCondition(StorableObjectCondition condition, boolean useLoader)
			throws ApplicationException {
		return instance.getStorableObjectsByConditionImpl(condition, useLoader);
	}

	public static List getStorableObjectsByConditionButIds(	List ids,
								StorableObjectCondition condition,
								boolean useLoader) throws ApplicationException {
		return instance.getStorableObjectsByConditionButIdsImpl(ids, condition, useLoader);
	}

	protected StorableObject loadStorableObject(Identifier objectId) throws DatabaseException, CommunicationException {
		StorableObject storableObject;
		switch (objectId.getMajor()) {
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

	protected List loadStorableObjects(Short entityCode, List ids) throws DatabaseException, CommunicationException {
		List loadedList = null;
		switch (entityCode.shortValue()) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCableThreadTypes(ids);
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

	protected List loadStorableObjectsButIds(StorableObjectCondition condition, List ids)
			throws DatabaseException, CommunicationException {
		List loadedList = null;
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					loadedList = cObjectLoader.loadCableThreadTypesButIds(condition, ids);
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
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '" + ObjectEntities.codeToString(entityCode) + "', entity code: " + entityCode);
				loadedList = null;
		}
		return loadedList;
	}

	//public static void save()

	protected void saveStorableObjects(short code, List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException {
		if (!list.isEmpty()) {
			boolean alone = (list.size() == 1);
			switch (code) {
				case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveCableThreadType((CableThreadType) list.get(0), force);
					else
						cObjectLoader.saveCableThreadTypes(list, force);
					break;
				case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveEquipmentType((EquipmentType) list.get(0), force);
					else
						cObjectLoader.saveEquipmentTypes(list, force);
					break;
				case ObjectEntities.PORTTYPE_ENTITY_CODE:
					if (alone)
						cObjectLoader.savePortType((PortType) list.get(0), force);
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
						cObjectLoader.saveMeasurementPortType((MeasurementPortType) list.get(0), force);
					else
						cObjectLoader.saveMeasurementPortTypes(list, force);
					break;
				case ObjectEntities.EQUIPMENT_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveEquipment((Equipment) list.get(0), force);
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
						cObjectLoader.saveMeasurementPort((MeasurementPort) list.get(0), force);
					else
						cObjectLoader.saveMeasurementPorts(list, force);
					break;
				case ObjectEntities.ME_ENTITY_CODE:
					if (alone)
						cObjectLoader.saveMonitoredElement((MonitoredElement) list.get(0), force);
					else
						cObjectLoader.saveMonitoredElements(list, force);
					break;

				default:
					Log.errorMessage("ConfigurationStorableObjectPool.saveStorableObjects | Unknown entity: '" + ObjectEntities.codeToString(code) + "', entity code: " + code);
			}

		}
	}

	public static StorableObject putStorableObject(StorableObject storableObject) throws IllegalObjectEntityException {
		return instance.putStorableObjectImpl(storableObject);
	}

	public static void flush(boolean force) throws VersionCollisionException, DatabaseException, CommunicationException, IllegalDataException{		 
		instance.flushImpl(force);
	}

	public static void cleanChangedStorableObject(Short entityCode) {
		instance.cleanChangedStorableObjectImpl(entityCode);
	}

	public static void cleanChangedStorableObjects() {
		instance.cleanChangedStorableObjectsImpl();
	}

	public static void delete(Identifier id) throws DatabaseException, CommunicationException {
		instance.deleteImpl(id);
	}

	public static void delete(List objects) throws DatabaseException, CommunicationException, IllegalDataException {
		instance.deleteImpl(objects);
	}

	protected void deleteStorableObject(Identifier id) throws DatabaseException, CommunicationException {
		try {
			cObjectLoader.delete(id);
		}
		catch (DatabaseException e) {
			Log.errorMessage("ConfigurationStorableObjectPool.deleteStorableObject | DatabaseException: " + e.getMessage());
			throw new DatabaseException("ConfigurationStorableObjectPool.deleteStorableObject", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("ConfigurationStorableObjectPool.deleteStorableObject | CommunicationException: " + e.getMessage());
			throw new CommunicationException("ConfigurationStorableObjectPool.deleteStorableObject", e);
		}
	}

	protected void deleteStorableObjects(List objects) throws DatabaseException, CommunicationException, IllegalDataException {
		try {
			cObjectLoader.delete(objects);
		}
		catch (DatabaseException e) {
			Log.errorMessage("ConfigurationStorableObjectPool.deleteStorableObjects | DatabaseException: " + e.getMessage());
			throw new DatabaseException("ConfigurationStorableObjectPool.deleteStorableObjects", e);
		}
		catch (CommunicationException e) {
			Log.errorMessage("ConfigurationStorableObjectPool.deleteStorableObjects | CommunicationException: " + e.getMessage());
			throw new CommunicationException("ConfigurationStorableObjectPool.deleteStorableObjects", e);
		}
	}

	public static void serializePool() {
		instance.serializePoolImpl();
	}

}
