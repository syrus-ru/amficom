package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.StorableObject_Database;

public class AdministrationDatabaseContext {
	public static StorableObject_Database permissionAttributesDatabase;
	public static StorableObject_Database domainDatabase;
	public static StorableObject_Database mcmDatabase;
	public static StorableObject_Database serverDatabase;
	public static StorableObject_Database userDatabase;

	public static void init(StorableObject_Database permissionAttributesDatabase1,
													StorableObject_Database domainDatabase1,
													StorableObject_Database mcmDatabase1,
													StorableObject_Database serverDatabase1,
													StorableObject_Database userDatabase1) {
		permissionAttributesDatabase = permissionAttributesDatabase1;
		domainDatabase = domainDatabase1;
		mcmDatabase = mcmDatabase1;
		serverDatabase = serverDatabase1;
		userDatabase = userDatabase1;
	}
}