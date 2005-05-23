/*-
 * $Id: ConfigurationDatabaseContext.java,v 1.43 2005/05/23 18:45:19 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.43 $, $Date: 2005/05/23 18:45:19 $
 * @author $Author: bass $
 * @module config_v1
 */
public final class ConfigurationDatabaseContext {
	private static EquipmentTypeDatabase		equipmentTypeDatabase;
	private static PortTypeDatabase			portTypeDatabase;
	private static MeasurementPortTypeDatabase	measurementPortTypeDatabase;
	private static LinkTypeDatabase			linkTypeDatabase;
	private static CableLinkTypeDatabase		cableLinkTypeDatabase;
	private static CableThreadTypeDatabase		cableThreadTypeDatabase;
	private static TransmissionPathTypeDatabase	transmissionPathTypeDatabase;
	private static EquipmentDatabase		equipmentDatabase;
	private static PortDatabase			portDatabase;
	private static MeasurementPortDatabase		measurementPortDatabase;
	private static TransmissionPathDatabase		transmissionPathDatabase;
	private static KISDatabase			kisDatabase;
	private static MonitoredElementDatabase		monitoredElementDatabase;
	private static LinkDatabase			linkDatabase;
	private static CableThreadDatabase		cableThreadDatabase;

	private ConfigurationDatabaseContext() {
		assert false;
	}

	public static void init(
			final EquipmentTypeDatabase		equipmentTypeDatabase1,
			final PortTypeDatabase			portTypeDatabase1,
			final MeasurementPortTypeDatabase	measurementPortTypeDatabase1,
			final LinkTypeDatabase			linkTypeDatabase1,
			final CableLinkTypeDatabase		cableLinkTypeDatabase1,
			final CableThreadTypeDatabase		cableThreadTypeDatabase1,
			final EquipmentDatabase			equipmentDatabase1,
			final PortDatabase			portDatabase1,
			final MeasurementPortDatabase		measurementPortDatabase1,
			final TransmissionPathDatabase		transmissionPathDatabase1,
			final TransmissionPathTypeDatabase	transmissionPathTypeDatabase1,
			final KISDatabase			kisDatabase1,
			final MonitoredElementDatabase		monitoredElementDatabase1,
			final LinkDatabase			linkDatabase1,
			final CableThreadDatabase		cableThreadDatabase1) {
		if (equipmentTypeDatabase1 != null)
			equipmentTypeDatabase = equipmentTypeDatabase1;
		if (portTypeDatabase1 != null)
			portTypeDatabase = portTypeDatabase1;
		if (measurementPortTypeDatabase1 != null)
			measurementPortTypeDatabase = measurementPortTypeDatabase1;
		if (linkTypeDatabase1 != null)
			linkTypeDatabase = linkTypeDatabase1;
		if (cableLinkTypeDatabase1 != null)
			cableLinkTypeDatabase = cableLinkTypeDatabase1;
		if (cableThreadTypeDatabase1 != null)
			cableThreadTypeDatabase = cableThreadTypeDatabase1;
		if (equipmentDatabase1 != null)
			equipmentDatabase = equipmentDatabase1;
		if (portDatabase1 != null)
			portDatabase = portDatabase1;
		if (measurementPortDatabase1 != null)
			measurementPortDatabase = measurementPortDatabase1;
		if (transmissionPathDatabase1 != null)
			transmissionPathDatabase = transmissionPathDatabase1;
		if (transmissionPathTypeDatabase1 != null)
			transmissionPathTypeDatabase = transmissionPathTypeDatabase1;
		if (kisDatabase1 != null)
			kisDatabase = kisDatabase1;
		if (monitoredElementDatabase1 != null)
			monitoredElementDatabase = monitoredElementDatabase1;
		if (linkDatabase1 != null)
			linkDatabase = linkDatabase1;
		if (cableThreadDatabase1 != null)
			cableThreadDatabase = cableThreadDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE:
				return equipmentTypeDatabase;
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				return portTypeDatabase;
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				return measurementPortTypeDatabase;
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				return linkTypeDatabase;
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				return cableLinkTypeDatabase;
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				return cableThreadTypeDatabase;
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				return equipmentDatabase;
			case ObjectEntities.PORT_ENTITY_CODE:
				return portDatabase;
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				return measurementPortDatabase;
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				return transmissionPathDatabase;
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				return transmissionPathTypeDatabase;
			case ObjectEntities.KIS_ENTITY_CODE:
				return kisDatabase;
			case ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE:
				return monitoredElementDatabase;
			case ObjectEntities.LINK_ENTITY_CODE:
				return linkDatabase;
			case ObjectEntities.CABLETHREAD_ENTITY_CODE:
				return cableThreadDatabase;
			default:
				Log.errorMessage("ConfigurationDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
