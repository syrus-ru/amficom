/*
 * $Id: ConfigurationDatabaseContext.java,v 1.30 2004/12/01 15:41:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.30 $, $Date: 2004/12/01 15:41:47 $
 * @author $Author: bass $
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

	private ConfigurationDatabaseContext() {
		// private constructor 
	}

	public static void init(final StorableObjectDatabase newCharacteristicTypeDatabase,
			final StorableObjectDatabase newEquipmentTypeDatabase,
			final StorableObjectDatabase newPortTypeDatabase,
			final StorableObjectDatabase newMeasurementPortTypeDatabase,
			final StorableObjectDatabase newLinkTypeDatabase,
			final StorableObjectDatabase newCableThreadTypeDatabase,
			final StorableObjectDatabase newCharacteristicDatabase,
			final StorableObjectDatabase newUserDatabase,
			final StorableObjectDatabase newDomainDatabase,
			final StorableObjectDatabase newServerDatabase,
			final StorableObjectDatabase newMcmDatabase,
			final StorableObjectDatabase newEquipmentDatabase,
			final StorableObjectDatabase newPortDatabase,
			final StorableObjectDatabase newMeasurementPortDatabase,
			final StorableObjectDatabase newTransmissionPathDatabase,
			final StorableObjectDatabase newTransmissionPathTypeDatabase,
			final StorableObjectDatabase newKisDatabase,
			final StorableObjectDatabase newMonitoredElementDatabase,
			final StorableObjectDatabase newLinkDatabase) {
		characteristicTypeDatabase = newCharacteristicTypeDatabase;
		equipmentTypeDatabase = newEquipmentTypeDatabase;
		portTypeDatabase = newPortTypeDatabase;
		measurementPortTypeDatabase = newMeasurementPortTypeDatabase;
		linkTypeDatabase = newLinkTypeDatabase;
		cableThreadTypeDatabase = newCableThreadTypeDatabase;
		characteristicDatabase = newCharacteristicDatabase;
		userDatabase = newUserDatabase;
		domainDatabase = newDomainDatabase;
		serverDatabase = newServerDatabase;
		mcmDatabase = newMcmDatabase;
		equipmentDatabase = newEquipmentDatabase;
		portDatabase = newPortDatabase;
		measurementPortDatabase = newMeasurementPortDatabase;
		transmissionPathDatabase = newTransmissionPathDatabase;
		transmissionPathTypeDatabase = newTransmissionPathTypeDatabase;
		kisDatabase = newKisDatabase;
		monitoredElementDatabase = newMonitoredElementDatabase;
		linkDatabase = newLinkDatabase;
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
