/*
 * $Id: ConfigurationDatabaseContext.java,v 1.28 2004/11/23 15:24:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.28 $, $Date: 2004/11/23 15:24:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class ConfigurationDatabaseContext {
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

	private ConfigurationDatabaseContext() {	
		// private constructor 
	}

	public static void init(StorableObjectDatabase characteristicTypeDatabase1,
													StorableObjectDatabase equipmentTypeDatabase1,
													StorableObjectDatabase portTypeDatabase1,
													StorableObjectDatabase measurementPortTypeDatabase1,
													StorableObjectDatabase linkTypeDatabase1,
													StorableObjectDatabase cableThreadTypeDatabase1,
													StorableObjectDatabase characteristicDatabase1,
													StorableObjectDatabase userDatabase1,
													StorableObjectDatabase domainDatabase1,
													StorableObjectDatabase serverDatabase1,
													StorableObjectDatabase mcmDatabase1,
													StorableObjectDatabase equipmentDatabase1,
													StorableObjectDatabase portDatabase1,
													StorableObjectDatabase measurementPortDatabase1,
													StorableObjectDatabase transmissionPathDatabase1,
													StorableObjectDatabase transmissionPathTypeDatabase1,
													StorableObjectDatabase kisDatabase1,
													StorableObjectDatabase monitoredElementDatabase1,
													StorableObjectDatabase linkDatabase1) {
		characteristicTypeDatabase = characteristicTypeDatabase1;
		equipmentTypeDatabase = equipmentTypeDatabase1;
		portTypeDatabase = portTypeDatabase1;
		measurementPortTypeDatabase = measurementPortTypeDatabase1;
		linkTypeDatabase = linkTypeDatabase1;
		cableThreadTypeDatabase = cableThreadTypeDatabase1;
		
		characteristicDatabase = characteristicDatabase1;
		userDatabase = userDatabase1;
		domainDatabase = domainDatabase1;
		serverDatabase = serverDatabase1;
		mcmDatabase = mcmDatabase1;
		equipmentDatabase = equipmentDatabase1;
		portDatabase = portDatabase1;
		measurementPortDatabase = measurementPortDatabase1;
		transmissionPathDatabase = transmissionPathDatabase1;
        transmissionPathTypeDatabase = transmissionPathTypeDatabase1;
		kisDatabase = kisDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
		linkDatabase = linkDatabase1;
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
