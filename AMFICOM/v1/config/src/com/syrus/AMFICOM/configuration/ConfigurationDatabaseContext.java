package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObjectDatabase;

public abstract class ConfigurationDatabaseContext {
	protected static StorableObjectDatabase characteristicTypeDatabase;

	protected static StorableObjectDatabase characteristicDatabase;
	protected static StorableObjectDatabase portDatabase;
	protected static StorableObjectDatabase monitoredElementDatabase;
	protected static StorableObjectDatabase kisDatabase;
	protected static StorableObjectDatabase mcmDatabase;

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