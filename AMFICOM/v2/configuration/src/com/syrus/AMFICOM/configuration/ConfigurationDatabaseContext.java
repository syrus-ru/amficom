package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.StorableObject_Database;

public class ConfigurationDatabaseContext {
	public static StorableObject_Database characteristicTypeDatabase;

	public static StorableObject_Database characteristicDatabase;
	public static StorableObject_Database monitoredElementDatabase;
	public static StorableObject_Database kisDatabase;

	public static void init(StorableObject_Database characteristicTypeDatabase1,

													StorableObject_Database characteristicDatabase1,
													StorableObject_Database monitoredElementDatabase1,
													StorableObject_Database kisDatabase1) {
		characteristicTypeDatabase = characteristicTypeDatabase1;

		characteristicDatabase = characteristicDatabase1;
		monitoredElementDatabase = monitoredElementDatabase1;
		kisDatabase = kisDatabase1;
	}
}