/*-
 * $Id: ConfigurationDatabaseContext.java,v 1.41 2005/04/01 11:02:30 bass Exp $
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
 * @version $Revision: 1.41 $, $Date: 2005/04/01 11:02:30 $
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
				return getEquipmentTypeDatabase();
			case ObjectEntities.PORTTYPE_ENTITY_CODE:
				return getPortTypeDatabase();
			case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
				return getMeasurementPortTypeDatabase();
			case ObjectEntities.LINKTYPE_ENTITY_CODE:
				return getLinkTypeDatabase();
			case ObjectEntities.CABLELINKTYPE_ENTITY_CODE:
				return getCableLinkTypeDatabase();
			case ObjectEntities.CABLETHREADTYPE_ENTITY_CODE:
				return getCableThreadTypeDatabase();
			case ObjectEntities.EQUIPMENT_ENTITY_CODE:
				return getEquipmentDatabase();
			case ObjectEntities.PORT_ENTITY_CODE:
				return getPortDatabase();
			case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
				return getMeasurementPortDatabase();
			case ObjectEntities.TRANSPATH_ENTITY_CODE:
				return getTransmissionPathDatabase();
			case ObjectEntities.TRANSPATHTYPE_ENTITY_CODE:
				return getTransmissionPathTypeDatabase();
			case ObjectEntities.KIS_ENTITY_CODE:
				return getKISDatabase();
			case ObjectEntities.ME_ENTITY_CODE:
				return getMonitoredElementDatabase();
			case ObjectEntities.LINK_ENTITY_CODE:
				return getLinkDatabase();
			case ObjectEntities.CABLETHREAD_ENTITY_CODE:
				return getCableThreadDatabase();
			default:
				Log.errorMessage("ConfigurationDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;
		}
	}

	public static EquipmentDatabase getEquipmentDatabase() {
		return equipmentDatabase;
	}

	public static EquipmentTypeDatabase getEquipmentTypeDatabase() {
		return equipmentTypeDatabase;
	}

	public static KISDatabase getKISDatabase() {
		return kisDatabase;
	}

	public static LinkDatabase getLinkDatabase() {
		return linkDatabase;
	}

	public static CableThreadDatabase getCableThreadDatabase() {
		return cableThreadDatabase;
	}

	public static CableThreadTypeDatabase getCableThreadTypeDatabase() {
		return cableThreadTypeDatabase;
	}

	public static LinkTypeDatabase getLinkTypeDatabase() {
		return linkTypeDatabase;
	}

	public static CableLinkTypeDatabase getCableLinkTypeDatabase() {
		return cableLinkTypeDatabase;
	}

	public static MeasurementPortDatabase getMeasurementPortDatabase() {
		return measurementPortDatabase;
	}

	public static MeasurementPortTypeDatabase getMeasurementPortTypeDatabase() {
		return measurementPortTypeDatabase;
	}

	public static MonitoredElementDatabase getMonitoredElementDatabase() {
		return monitoredElementDatabase;
	}

	public static PortDatabase getPortDatabase() {
		return portDatabase;
	}

	public static PortTypeDatabase getPortTypeDatabase() {
		return portTypeDatabase;
	}

	public static TransmissionPathDatabase getTransmissionPathDatabase() {
		return transmissionPathDatabase;
	}

	public static TransmissionPathTypeDatabase getTransmissionPathTypeDatabase() {
		return transmissionPathTypeDatabase;
	}
}
