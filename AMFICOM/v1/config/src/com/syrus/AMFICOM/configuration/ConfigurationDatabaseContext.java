/*
 * $Id: ConfigurationDatabaseContext.java,v 1.32 2004/12/09 15:04:32 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.32 $, $Date: 2004/12/09 15:04:32 $
 * @author $Author: max $
 * @todo Declare all fields private as<ol>
 *       <li>they have public accessors; and</li>
 *       <li>there's {@link #init(StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase)}
 *       method which is supposed to be the only modifier for class' fields.</li></ol>
 * @module configuration_v1
 */
public final class ConfigurationDatabaseContext {
	protected static StorableObjectDatabase characteristicTypeDatabase;
	protected static StorableObjectDatabase equipmentTypeDatabase;
	protected static StorableObjectDatabase portTypeDatabase;
	protected static StorableObjectDatabase measurementPortTypeDatabase;
	protected static StorableObjectDatabase linkTypeDatabase;
	protected static StorableObjectDatabase cableThreadTypeDatabase;
	protected static StorableObjectDatabase transmissionPathTypeDatabase;
	protected static StorableObjectDatabase characteristicDatabase;
	protected static StorableObjectDatabase userDatabase;
	protected static StorableObjectDatabase domainDatabase;
	protected static StorableObjectDatabase serverDatabase;
	protected static StorableObjectDatabase mcmDatabase;
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

	public static void init(final StorableObjectDatabase characteristicTypeDatabase,
			final StorableObjectDatabase equipmentTypeDatabase,
			final StorableObjectDatabase portTypeDatabase,
			final StorableObjectDatabase measurementPortTypeDatabase,
			final StorableObjectDatabase linkTypeDatabase,
			final StorableObjectDatabase cableThreadTypeDatabase,
			final StorableObjectDatabase characteristicDatabase,
			final StorableObjectDatabase userDatabase,
			final StorableObjectDatabase domainDatabase,
			final StorableObjectDatabase serverDatabase,
			final StorableObjectDatabase mcmDatabase,
			final StorableObjectDatabase equipmentDatabase,
			final StorableObjectDatabase portDatabase,
			final StorableObjectDatabase measurementPortDatabase,
			final StorableObjectDatabase transmissionPathDatabase,
			final StorableObjectDatabase transmissionPathTypeDatabase,
			final StorableObjectDatabase kisDatabase,
			final StorableObjectDatabase monitoredElementDatabase,
			final StorableObjectDatabase linkDatabase,
            final StorableObjectDatabase cableThreadDatabase) {
		ConfigurationDatabaseContext.characteristicTypeDatabase = characteristicTypeDatabase;
		ConfigurationDatabaseContext.equipmentTypeDatabase = equipmentTypeDatabase;
		ConfigurationDatabaseContext.portTypeDatabase = portTypeDatabase;
		ConfigurationDatabaseContext.measurementPortTypeDatabase = measurementPortTypeDatabase;
		ConfigurationDatabaseContext.linkTypeDatabase = linkTypeDatabase;
		ConfigurationDatabaseContext.cableThreadTypeDatabase = cableThreadTypeDatabase;
		ConfigurationDatabaseContext.characteristicDatabase = characteristicDatabase;
		ConfigurationDatabaseContext.userDatabase = userDatabase;
		ConfigurationDatabaseContext.domainDatabase = domainDatabase;
		ConfigurationDatabaseContext.serverDatabase = serverDatabase;
		ConfigurationDatabaseContext.mcmDatabase = mcmDatabase;
		ConfigurationDatabaseContext.equipmentDatabase = equipmentDatabase;
		ConfigurationDatabaseContext.portDatabase = portDatabase;
		ConfigurationDatabaseContext.measurementPortDatabase = measurementPortDatabase;
		ConfigurationDatabaseContext.transmissionPathDatabase = transmissionPathDatabase;
		ConfigurationDatabaseContext.transmissionPathTypeDatabase = transmissionPathTypeDatabase;
		ConfigurationDatabaseContext.kisDatabase = kisDatabase;
		ConfigurationDatabaseContext.monitoredElementDatabase = monitoredElementDatabase;
		ConfigurationDatabaseContext.linkDatabase = linkDatabase;
        ConfigurationDatabaseContext.linkDatabase = cableThreadDatabase;
	}

	public static StorableObjectDatabase getCharacteristicDatabase() {
		return characteristicDatabase;
	}

	public static StorableObjectDatabase getCharacteristicTypeDatabase() {
		return characteristicTypeDatabase;
	}

	public static StorableObjectDatabase getDomainDatabase() {
		return domainDatabase;
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

	public static StorableObjectDatabase getMCMDatabase() {
		return mcmDatabase;
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

	public static StorableObjectDatabase getServerDatabase() {
		return serverDatabase;
	}

	public static StorableObjectDatabase getTransmissionPathDatabase() {
		return transmissionPathDatabase;
	}

	public static StorableObjectDatabase getTransmissionPathTypeDatabase() {
		return transmissionPathTypeDatabase;
	}

	public static StorableObjectDatabase getUserDatabase() {
		return userDatabase;
	}
}
