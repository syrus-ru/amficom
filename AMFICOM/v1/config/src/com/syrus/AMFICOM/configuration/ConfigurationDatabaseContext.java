/*
 * $Id: ConfigurationDatabaseContext.java,v 1.35 2005/01/14 18:07:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.35 $, $Date: 2005/01/14 18:07:07 $
 * @author $Author: arseniy $
 * @todo Declare all fields private as<ol>
 *       <li>they have public accessors; and</li>
 *       <li>there's {@link #init(StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase)}
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

	public static void init(final StorableObjectDatabase equipmentTypeDatabase,
			final StorableObjectDatabase portTypeDatabase,
			final StorableObjectDatabase measurementPortTypeDatabase,
			final StorableObjectDatabase linkTypeDatabase,
			final StorableObjectDatabase cableLinkTypeDatabase,
			final StorableObjectDatabase cableThreadTypeDatabase,
			final StorableObjectDatabase equipmentDatabase,
			final StorableObjectDatabase portDatabase,
			final StorableObjectDatabase measurementPortDatabase,
			final StorableObjectDatabase transmissionPathDatabase,
			final StorableObjectDatabase transmissionPathTypeDatabase,
			final StorableObjectDatabase kisDatabase,
			final StorableObjectDatabase monitoredElementDatabase,
			final StorableObjectDatabase linkDatabase,
			final StorableObjectDatabase cableThreadDatabase) {
		ConfigurationDatabaseContext.equipmentTypeDatabase = equipmentTypeDatabase;
		ConfigurationDatabaseContext.portTypeDatabase = portTypeDatabase;
		ConfigurationDatabaseContext.measurementPortTypeDatabase = measurementPortTypeDatabase;
		ConfigurationDatabaseContext.linkTypeDatabase = linkTypeDatabase;
		ConfigurationDatabaseContext.cableLinkTypeDatabase = cableLinkTypeDatabase;
		ConfigurationDatabaseContext.cableThreadTypeDatabase = cableThreadTypeDatabase;
		ConfigurationDatabaseContext.equipmentDatabase = equipmentDatabase;
		ConfigurationDatabaseContext.portDatabase = portDatabase;
		ConfigurationDatabaseContext.measurementPortDatabase = measurementPortDatabase;
		ConfigurationDatabaseContext.transmissionPathDatabase = transmissionPathDatabase;
		ConfigurationDatabaseContext.transmissionPathTypeDatabase = transmissionPathTypeDatabase;
		ConfigurationDatabaseContext.kisDatabase = kisDatabase;
		ConfigurationDatabaseContext.monitoredElementDatabase = monitoredElementDatabase;
		ConfigurationDatabaseContext.linkDatabase = linkDatabase;
		ConfigurationDatabaseContext.cableThreadDatabase = cableThreadDatabase;
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
