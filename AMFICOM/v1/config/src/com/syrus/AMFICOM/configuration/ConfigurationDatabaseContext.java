/*
 * $Id: ConfigurationDatabaseContext.java,v 1.37 2005/02/03 14:38:45 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.37 $, $Date: 2005/02/03 14:38:45 $
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
		equipmentTypeDatabase = equipmentTypeDatabase1;
		portTypeDatabase = portTypeDatabase1;
		measurementPortTypeDatabase = measurementPortTypeDatabase1;
		linkTypeDatabase = linkTypeDatabase1;
		cableLinkTypeDatabase = cableLinkTypeDatabase1;
		cableThreadTypeDatabase = cableThreadTypeDatabase1;
		equipmentDatabase = equipmentDatabase1;
		portDatabase = portDatabase1;
		measurementPortDatabase = measurementPortDatabase1;
		transmissionPathDatabase = transmissionPathDatabase1;
		transmissionPathTypeDatabase = transmissionPathTypeDatabase1;
		kisDatabase = kisDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
		linkDatabase = linkDatabase1;
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
