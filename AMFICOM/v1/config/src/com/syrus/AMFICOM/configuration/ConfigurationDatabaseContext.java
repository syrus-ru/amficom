/*
 * $Id: ConfigurationDatabaseContext.java,v 1.38 2005/02/03 14:43:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.38 $, $Date: 2005/02/03 14:43:43 $
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

	public static StorableObjectDatabase getCableThreadTypeDatabase(){
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
