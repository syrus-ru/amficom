package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

public abstract class ConfigurationDatabaseContext {
	public static StorableObjectDatabase characteristicTypeDatabase;

	public static StorableObjectDatabase characteristicDatabase;
	public static StorableObjectDatabase portDatabase;
	public static StorableObjectDatabase monitoredElementDatabase;
	public static StorableObjectDatabase kisDatabase;
	public static StorableObjectDatabase mcmDatabase;

	public static void init(StorableObjectDatabase characteristicTypeDatabase1,

													StorableObjectDatabase characteristicDatabase1,
													StorableObjectDatabase portDatabase1,
													StorableObjectDatabase monitoredElementDatabase1,
													StorableObjectDatabase kisDatabase1,
													StorableObjectDatabase mcmDatabase1) {
		characteristicTypeDatabase = characteristicTypeDatabase1;

		characteristicDatabase = characteristicDatabase1;
		portDatabase = portDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
		kisDatabase = kisDatabase1;
		mcmDatabase = mcmDatabase1;
	}
}