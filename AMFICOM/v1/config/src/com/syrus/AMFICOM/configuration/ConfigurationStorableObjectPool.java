/*
 * $Id: ConfigurationStorableObjectPool.java,v 1.93 2005/06/17 11:01:10 bass Exp $
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
 * @version $Revision: 1.93 $, $Date: 2005/06/17 11:01:10 $
 * @author $Author: bass $
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

		registerFactory(ObjectEntities.EQUIPMENT_TYPE_CODE, new EquipmentTypeFactory());
		registerFactory(ObjectEntities.PORT_TYPE_CODE, new PortTypeFactory());
		registerFactory(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, new MeasurementPortTypeFactory());
		registerFactory(ObjectEntities.TRANSPATH_TYPE_CODE, new TransmissionPathTypeFactory());
		registerFactory(ObjectEntities.LINK_TYPE_CODE, new LinkTypeFactory());
		registerFactory(ObjectEntities.CABLELINK_TYPE_CODE, new CableLinkTypeFactory());
		registerFactory(ObjectEntities.CABLETHREAD_TYPE_CODE, new CableThreadTypeFactory());
		//registerFactory(ObjectEntities.KIS_TYPE_CODE, new KisTypeFactory());

		registerFactory(ObjectEntities.EQUIPMENT_CODE, new EquipmentFactory());
		registerFactory(ObjectEntities.PORT_CODE, new PortFactory());
		registerFactory(ObjectEntities.MEASUREMENTPORT_CODE, new MeasurementPortFactory());
		registerFactory(ObjectEntities.TRANSPATH_CODE, new TransmissionPathFactory());
		registerFactory(ObjectEntities.KIS_CODE, new KisFactory());
		registerFactory(ObjectEntities.MONITOREDELEMENT_CODE, new MonitoredElementFactory());
		registerFactory(ObjectEntities.LINK_CODE, new LinkFactory());
		registerFactory(ObjectEntities.CABLETHREAD_CODE, new CableThreadFactory());
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

		instance.addObjectPool(ObjectEntities.EQUIPMENT_TYPE_CODE, EQUIPMENTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PORT_TYPE_CODE, PORTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, MEASUREMENTPORTTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TRANSPATH_TYPE_CODE, TRANSPATHTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.LINK_TYPE_CODE, LINKTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLELINK_TYPE_CODE, CABLELINKTYPE_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLETHREAD_TYPE_CODE, CABLETHREADTYPE_OBJECT_POOL_SIZE);

		instance.addObjectPool(ObjectEntities.EQUIPMENT_CODE, EQUIPMENT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.PORT_CODE, PORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_CODE, MEASUREMENTPORT_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.TRANSPATH_CODE, TRANSPATH_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.KIS_CODE, KIS_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.MONITOREDELEMENT_CODE, ME_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.LINK_CODE, LINK_OBJECT_POOL_SIZE);
		instance.addObjectPool(ObjectEntities.CABLETHREAD_CODE, CABLETHREAD_OBJECT_POOL_SIZE);
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

			instance.addObjectPool(ObjectEntities.EQUIPMENT_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.PORT_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.TRANSPATH_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.LINK_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.CABLELINK_TYPE_CODE, size);
			instance.addObjectPool(ObjectEntities.CABLETHREAD_TYPE_CODE, size);
			// instance.addObjectPool(ObjectEntities.KIS_TYPE_CODE, size);

			instance.addObjectPool(ObjectEntities.EQUIPMENT_CODE, size);
			instance.addObjectPool(ObjectEntities.PORT_CODE, size);
			instance.addObjectPool(ObjectEntities.MEASUREMENTPORT_CODE, size);
			instance.addObjectPool(ObjectEntities.TRANSPATH_CODE, size);
			instance.addObjectPool(ObjectEntities.KIS_CODE, size);
			instance.addObjectPool(ObjectEntities.MONITOREDELEMENT_CODE, size);
			instance.addObjectPool(ObjectEntities.LINK_CODE, size);
			instance.addObjectPool(ObjectEntities.CABLETHREAD_CODE, size);
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
			case ObjectEntities.EQUIPMENT_TYPE_CODE:
				return cObjectLoader.loadEquipmentTypes(ids);
			case ObjectEntities.PORT_TYPE_CODE:
				return cObjectLoader.loadPortTypes(ids);
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				return cObjectLoader.loadMeasurementPortTypes(ids);
			case ObjectEntities.TRANSPATH_TYPE_CODE:
				return cObjectLoader.loadTransmissionPathTypes(ids);
			case ObjectEntities.LINK_TYPE_CODE:
				return cObjectLoader.loadLinkTypes(ids);
			case ObjectEntities.CABLELINK_TYPE_CODE:
				return cObjectLoader.loadCableLinkTypes(ids);
			case ObjectEntities.CABLETHREAD_TYPE_CODE:
				return cObjectLoader.loadCableThreadTypes(ids);

			case ObjectEntities.EQUIPMENT_CODE:
				return cObjectLoader.loadEquipments(ids);
			case ObjectEntities.PORT_CODE:
				return cObjectLoader.loadPorts(ids);
			case ObjectEntities.MEASUREMENTPORT_CODE:
				return cObjectLoader.loadMeasurementPorts(ids);
			case ObjectEntities.TRANSPATH_CODE:
				return cObjectLoader.loadTransmissionPaths(ids);
			case ObjectEntities.KIS_CODE:
				return cObjectLoader.loadKISs(ids);
			case ObjectEntities.MONITOREDELEMENT_CODE:
				return cObjectLoader.loadMonitoredElements(ids);
			case ObjectEntities.LINK_CODE:
				return cObjectLoader.loadLinks(ids);
			case ObjectEntities.CABLETHREAD_CODE:
				return cObjectLoader.loadCableThreads(ids);
			default:
				Log.errorMessage("ConfigurationStorableObjectPool.loadStorableObjects | Unknown entity: '"
						+ ObjectEntities.codeToString(entityCode) + "'/" + entityCode);
				return Collections.EMPTY_SET;
		}
	}

	protected Set loadStorableObjectsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		final short entityCode = condition.getEntityCode().shortValue();
		switch (entityCode) {
			case ObjectEntities.EQUIPMENT_TYPE_CODE:
				return cObjectLoader.loadEquipmentTypesButIds(condition, ids);
			case ObjectEntities.PORT_TYPE_CODE:
				return cObjectLoader.loadPortTypesButIds(condition, ids);
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				return cObjectLoader.loadMeasurementPortTypesButIds(condition, ids);
			case ObjectEntities.TRANSPATH_TYPE_CODE:
				return cObjectLoader.loadTransmissionPathTypesButIds(condition, ids);
			case ObjectEntities.LINK_TYPE_CODE:
				return cObjectLoader.loadLinkTypesButIds(condition, ids);
			case ObjectEntities.CABLELINK_TYPE_CODE:
				return cObjectLoader.loadCableLinkTypesButIds(condition, ids);
			case ObjectEntities.CABLETHREAD_TYPE_CODE:
				return cObjectLoader.loadCableThreadTypesButIds(condition, ids);

			case ObjectEntities.EQUIPMENT_CODE:
				return cObjectLoader.loadEquipmentsButIds(condition, ids);
			case ObjectEntities.PORT_CODE:
				return cObjectLoader.loadPortsButIds(condition, ids);
			case ObjectEntities.MEASUREMENTPORT_CODE:
				return cObjectLoader.loadMeasurementPortsButIds(condition, ids);
			case ObjectEntities.TRANSPATH_CODE:
				return cObjectLoader.loadTransmissionPathsButIds(condition, ids);
			case ObjectEntities.KIS_CODE:
				return cObjectLoader.loadKISsButIds(condition, ids);
			case ObjectEntities.MONITOREDELEMENT_CODE:
				return cObjectLoader.loadMonitoredElementsButIds(condition, ids);
			case ObjectEntities.LINK_CODE:
				return cObjectLoader.loadLinksButIds(condition, ids);
			case ObjectEntities.CABLETHREAD_CODE:
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
			case ObjectEntities.EQUIPMENT_TYPE_CODE:
				cObjectLoader.saveEquipmentTypes(storableObjects, force);
				break;
			case ObjectEntities.PORT_TYPE_CODE:
				cObjectLoader.savePortTypes(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
				cObjectLoader.saveMeasurementPortTypes(storableObjects, force);
				break;
			case ObjectEntities.TRANSPATH_TYPE_CODE:
				cObjectLoader.saveTransmissionPathTypes(storableObjects, force);
				break;
			case ObjectEntities.LINK_TYPE_CODE:
				cObjectLoader.saveLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.CABLELINK_TYPE_CODE:
				cObjectLoader.saveCableLinkTypes(storableObjects, force);
				break;
			case ObjectEntities.CABLETHREAD_TYPE_CODE:
				cObjectLoader.saveCableThreadTypes(storableObjects, force);
				break;

			case ObjectEntities.EQUIPMENT_CODE:
				cObjectLoader.saveEquipments(storableObjects, force);
				break;
			case ObjectEntities.PORT_CODE:
				cObjectLoader.savePorts(storableObjects, force);
				break;
			case ObjectEntities.MEASUREMENTPORT_CODE:
				cObjectLoader.saveMeasurementPorts(storableObjects, force);
				break;
			case ObjectEntities.TRANSPATH_CODE:
				cObjectLoader.saveTransmissionPaths(storableObjects, force);
				break;
			case ObjectEntities.KIS_CODE:
				cObjectLoader.saveKISs(storableObjects, force);
				break;
			case ObjectEntities.MONITOREDELEMENT_CODE:
				cObjectLoader.saveMonitoredElements(storableObjects, force);
				break;
			case ObjectEntities.LINK_CODE:
				cObjectLoader.saveLinks(storableObjects, force);
				break;
			case ObjectEntities.CABLETHREAD_CODE:
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
