/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.91 2005/06/16 12:35:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.LRUMap;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.91 $, $Date: 2005/06/16 12:35:43 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public final class ConfigurationStorableObjectPool extends StorableObjectPool {

	private static final int OBJECT_POOL_MAP_SIZE = 16; /* Number of entities */

	private static final int EQUIPMENTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int PORTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE = 1;
	private static final int TRANSPATHTYPE_OBJECT_POOL_SIZE = 1;
	private static final int LINKTYPE_OBJECT_POOL_SIZE = 2;
	private static final int CABLELINKTYPE_OBJECT_POOL_SIZE = 4;
	private static final int CABLETHREADTYPE_OBJECT_POOL_SIZE = 4;
	// private static final int KISTYPE_OBJECT_POOL_SIZE = 1;

	private static final int EQUIPMENT_OBJECT_POOL_SIZE = 2;
	private static final int PORT_OBJECT_POOL_SIZE = 2;
	private static final int MEASUREMENTPORT_OBJECT_POOL_SIZE = 2;
	private static final int TRANSPATH_OBJECT_POOL_SIZE = 4;
	private static final int KIS_OBJECT_POOL_SIZE = 1;
	private static final int ME_OBJECT_POOL_SIZE = 2;
	private static final int LINK_OBJECT_POOL_SIZE = 2;
	private static final int CABLETHREAD_OBJECT_POOL_SIZE = 2;

	private static ConfigurationObjectLoader cObjectLoader;
	private static ConfigurationStorableObjectPool instance;


	private ConfigurationStorableObjectPool(final Class cacheMapClass) {
		super(OBJECT_POOL_MAP_SIZE, ObjectGroupEntities.CONFIGURATION_GROUP_CODE, cacheMapClass);

		registerFactory(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, new EquipmentTypeFactory());
		registerFactory(ObjectEntities.PORTTYPE_ENTITY_CODE, new PortTypeFactory());
		registerFactory(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, new MeasurementPortTypeFactory());
		registerFactory(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, new TransmissionPathTypeFactory());
		registerFactory(ObjectEntities.LINKTYPE_ENTITY_CODE, new LinkTypeFactory());
		registerFactory(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, new CableLinkTypeFactory());
		registerFactory(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, new CableThreadTypeFactory());
		//registerFactory(ObjectEntities.KISTYPE_ENTITY_CODE, new KisTypeFactory());

		registerFactory(ObjectEntities.EQUIPMENT_ENTITY_CODE, new EquipmentFactory());
		registerFactory(ObjectEntities.PORT_ENTITY_CODE, new PortFactory());
		registerFactory(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, new MeasurementPortFactory());
		registerFactory(ObjectEntities.TRANSPATH_ENTITY_CODE, new TransmissionPathFactory());
		registerFactory(ObjectEntities.KIS_ENTITY_CODE, new KisFactory());
		registerFactory(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE, new MonitoredElementFactory());
		registerFactory(ObjectEntities.LINK_ENTITY_CODE, new LinkFactory());
		registerFactory(ObjectEntities.CABLETHREAD_ENTITY_CODE, new CableThreadFactory());
	}


	/**
	 * Init with default pool class and default pool sizes
	 * @param cObjectLoader1
	 */
	public static void init(final ConfigurationObjectLoader cObjectLoader1) {
		init(cObjectLoader1, LRUMap.class);
	}

	/**
	 * Init with default pool class and given pool sizes
	 * @param cObjectLoader1
	 * @param size
	 */
	public static void init(final ConfigurationObjectLoader cObjectLoader1, final int size) {
		init(cObjectLoader1, LRUMap.class, size);
	}

	/**
	 * Init with given pool class and default pool sizes
	 * @param cObjectLoader1
	 * @param cacheClass
	 */
	public static void init(final ConfigurationObjectLoader cObjectLoader1, final Class cacheClass) {
		assert cObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (instance == null)
			instance = new ConfigurationStorableObjectPool(cacheClass);

		cObjectLoader = cObjectLoader1;

		instance.addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, EQUIPMENTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, PORTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, TRANSPATHTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.LINKTYPE_ENTITY_CODE, LINKTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, CABLELINKTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, CABLETHREADTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.EQUIPMENT_ENTITY_CODE, EQUIPMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PORT_ENTITY_CODE, PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, MEASUREMENTPORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TRANSPATH_ENTITY_CODE, TRANSPATH_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.KIS_ENTITY_CODE, KIS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE, ME_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.LINK_ENTITY_CODE, LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLETHREAD_ENTITY_CODE, CABLETHREAD_OBJECT_POOL_SIZE);
	}

	/**
	 * Init with given pool class and given pool sizes
	 * @param cObjectLoader1
	 * @param cacheClass
	 * @param size
	 */
	public static void init(final ConfigurationObjectLoader cObjectLoader1, final Class cacheClass, final int size) {
		assert cObjectLoader1 != null : ErrorMessages.NON_NULL_EXPECTED;
		assert cacheClass != null : ErrorMessages.NON_NULL_EXPECTED;

		if (size > 0) {
			if (instance == null)
				instance = new ConfigurationStorableObjectPool(cacheClass);

			cObjectLoader = cObjectLoader1;

			instance.addObjectPool(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.PORTTYPE_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.LINKTYPE_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, size);
			// instance.addObjectPool(ObjectEntities.KISTYPE_ENTITY_CODE, size);

			instance.addObjectPool(ObjectEntities.EQUIPMENT_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.PORT_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.TRANSPATH_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.KIS_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.MONITOREDELEMENT_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.LINK_ENTITY_CODE, size);
			instance.addObjectPool(ObjectEntities.CABLETHREAD_ENTITY_CODE, size);
		}
		else {
			init(cObjectLoader1, cacheClass);
		}
	}


	protected Set refreshStorableObjects(final Set storableObjects) throws ApplicationException {
		return cObjectLoader.refresh(storableObjects);
	}

	protected Set loadStorableObjects(final Set ids) throws ApplicationException {
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		switch (entityCode) {
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				return cObjectLoader.loadEquipmentTypes(ids);
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				return cObjectLoader.loadPortTypes(ids);
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				return cObjectLoader.loadMeasurementPortTypes(ids);
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				return cObjectLoader.loadTransmissionPathTypes(ids);
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				return cObjectLoader.loadLinkTypes(ids);
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				return cObjectLoader.loadCableLinkTypes(ids);
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				return cObjectLoader.loadCableThreadTypes(ids);

			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				return cObjectLoader.loadEquipments(ids);
			case ObjectEntities.PORT_ENTITY_CODE:
				return cObjectLoader.loadPorts(ids);
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				return cObjectLoader.loadMeasurementPorts(ids);
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				return cObjectLoader.loadTransmissionPaths(ids);
			case ObjectEntities.KIS_ENTITY_CODE:
				return cObjectLoader.loadKISs(ids);
			case ObjectEntities.MONITOREDELEMENT_ENTITY_CODE:
				return cObjectLoader.loadMonitoredElements(ids);
			case ObjectEntities.LINK_ENTITY_CODE:
				return cObjectLoader.loadLinks(ids);
			case ObjectEntities.CABLETHREAD_ENTITY_CODE:
				return cObjectLoader.loadCableThreads(ids);
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				return cObjectLoader.loadEquipmentTypesButIds(condition, ids);
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				return cObjectLoader.loadPortTypesButIds(condition, ids);
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				return cObjectLoader.loadMeasurementPortTypesButIds(condition, ids);
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				return cObjectLoader.loadTransmissionPathTypesButIds(condition, ids);
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				return cObjectLoader.loadLinkTypesButIds(condition, ids);
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				return cObjectLoader.loadCableLinkTypesButIds(condition, ids);
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				return cObjectLoader.loadCableThreadTypesButIds(condition, ids);

			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				return cObjectLoader.loadEquipmentsButIds(condition, ids);
			case ObjectEntities.PORT_ENTITY_CODE:
				return cObjectLoader.loadPortsButIds(condition, ids);
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				return cObjectLoader.loadMeasurementPortsButIds(condition, ids);
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				return cObjectLoader.loadTransmissionPathsButIds(condition, ids);
			case ObjectEntities.KIS_ENTITY_CODE:
				return cObjectLoader.loadKISsButIds(condition, ids);
			case ObjectEntities.MONITOREDELEMENT_ENTITY_CODE:
				return cObjectLoader.loadMonitoredElementsButIds(condition, ids);
			case ObjectEntities.LINK_ENTITY_CODE:
				return cObjectLoader.loadLinksButIds(condition, ids);
			case ObjectEntities.CABLETHREAD_ENTITY_CODE:
				return cObjectLoader.loadCableThreadsButIds(condition, ids);
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjectsButIds | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
			return Collections.EMPTY_SET;
		}
	}

	protected void saveStorableObjects(final Set storableObjects, final boolean force) throws ApplicationException {
		if (storableObjects.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		switch (entityCode) {
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				cObjectLoader.saveEquipmentTypes(storableObjects, force);
				break;
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				cObjectLoader.savePortTypes(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				cObjectLoader.saveMeasurementPortTypes(storableObjects, force);
				break;
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				cObjectLoader.saveTransmissionPathTypes(storableObjects, force);
				break;
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				cObjectLoader.saveLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				cObjectLoader.saveCableLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				cObjectLoader.saveCableThreadTypes(storableObjects, force);
				break;

			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				cObjectLoader.saveEquipments(storableObjects, force);
				break;
			case ObjectEntities.PORT_ENTITY_CODE:
				cObjectLoader.savePorts(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				cObjectLoader.saveMeasurementPorts(storableObjects, force);
				break;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				cObjectLoader.saveTransmissionPaths(storableObjects, force);
				break;
			case ObjectEntities.KIS_ENTITY_CODE:
				cObjectLoader.saveKISs(storableObjects, force);
				break;
			case ObjectEntities.MONITOREDELEMENT_ENTITY_CODE:
				cObjectLoader.saveMonitoredElements(storableObjects, force);
				break;
			case ObjectEntities.LINK_ENTITY_CODE:
				cObjectLoader.saveLinks(storableObjects, force);
				break;
			case ObjectEntities.CABLETHREAD_ENTITY_CODE:
				cObjectLoader.saveCableThreads(storableObjects, force);
				break;
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.saveStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
		}
	}

	protected void deleteStorableObjects(final Set identifiables) {
		cObjectLoader.delete(identifiables);
	}

}
