/*
 * $Id: ConfigurationDatabaseContext.java,v 1.40 2005/02/16 13:52:05 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.40 $, $Date: 2005/02/16 13:52:05 $
 * @author $Author: arseniy $
 * @todo Declare all fields private as<ol>
 *       <li>they have public accessors; and</li>
 *       <li>there's {@link #init(StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase)}
 *       method which is supposed to be the only modifier for class' fields.</li></ol>
 * @module config_v1
 */
public final class ConfigurationDatabaseContext {
	protected static StorableObjectDatabase equipmentTypeDatabase;
	protected static StorableObjectDatabase portTypeDatabase;
	protected static StorableObjectDatabase measurementPortTypeDatabase;
	protected static StorableObjectDatabase linkTypeDatabase;
	protected static StorableObjectDatabase cableLinkTypeDatabase;
	protected static StorableObjectDatabase cableThreadTypeDatabase;
	protected static StorableObjectDatabase transmissionPathTypeDatabase;
	protected static StorableObjectDatabase equipmentDatabase;
	protected static StorableObjectDatabase portDatabase;
	protected static StorableObjectDatabase measurementPortDatabase;
	protected static StorableObjectDatabase transmissionPathDatabase;
	protected static StorableObjectDatabase kisDatabase;
	protected static StorableObjectDatabase monitoredElementDatabase;
	protected static StorableObjectDatabase linkDatabase;
	protected static StorableObjectDatabase cableThreadDatabase;

	private ConfigurationDatabaseContext() {
		// private constructor 
	}

	public static void init(final StorableObjectDatabase equipmentTypeDatabase1,
			final StorableObjectDatabase portTypeDatabase1,
			final StorableObjectDatabase measurementPortTypeDatabase1,
			final StorableObjectDatabase linkTypeDatabase1,
			final StorableObjectDatabase cableLinkTypeDatabase1,
			final StorableObjectDatabase cableThreadTypeDatabase1,
			final StorableObjectDatabase equipmentDatabase1,
			final StorableObjectDatabase portDatabase1,
			final StorableObjectDatabase measurementPortDatabase1,
			final StorableObjectDatabase transmissionPathDatabase1,
			final StorableObjectDatabase transmissionPathTypeDatabase1,
			final StorableObjectDatabase kisDatabase1,
			final StorableObjectDatabase monitoredElementDatabase1,
			final StorableObjectDatabase linkDatabase1,
			final StorableObjectDatabase cableThreadDatabase1) {

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

	public static StorableObjectDatabase getDatabase(Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(short entityCode) {
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

			case ObjectEntities.ME_ENTITY_CODE:
				return monitoredElementDatabase;

			case ObjectEntities.LINK_ENTITY_CODE:
				return linkDatabase;

			case ObjectEntities.CABLETHREAD_ENTITY_CODE:
				return cableThreadDatabase;

			default:
				return null;
		}
	}

	public static StorableObjectDatabase getEquipmentDatabase() {
		return equipmentDatabase;
	}

	public static StorableObjectDatabase getEquipmentTypeDatabase() {
		return equipmentTypeDatabase;
	}

	public static StorableObjectDatabase getKISDatabase() {
		return kisDatabase;
	}

	public static StorableObjectDatabase getLinkDatabase() {
		return linkDatabase;
	}

	public static StorableObjectDatabase getCableThreadDatabase() {
		return cableThreadDatabase;
	}

	public static StorableObjectDatabase getCableThreadTypeDatabase() {
		return cableThreadTypeDatabase;
	}

	public static StorableObjectDatabase getLinkTypeDatabase() {
		return linkTypeDatabase;
	}

	public static StorableObjectDatabase getCableLinkTypeDatabase() {
		return cableLinkTypeDatabase;
	}

	public static StorableObjectDatabase getMeasurementPortDatabase() {
		return measurementPortDatabase;
	}

	public static StorableObjectDatabase getMeasurementPortTypeDatabase() {
		return measurementPortTypeDatabase;
	}

	public static StorableObjectDatabase getMonitoredElementDatabase() {
		return monitoredElementDatabase;
	}

	public static StorableObjectDatabase getPortDatabase() {
		return portDatabase;
	}

	public static StorableObjectDatabase getPortTypeDatabase() {
		return portTypeDatabase;
	}

	public static StorableObjectDatabase getTransmissionPathDatabase() {
		return transmissionPathDatabase;
	}

	public static StorableObjectDatabase getTransmissionPathTypeDatabase() {
		return transmissionPathTypeDatabase;
	}
}
