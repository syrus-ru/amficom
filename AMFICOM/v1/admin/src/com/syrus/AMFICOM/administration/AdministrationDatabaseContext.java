/*-
 * $Id: AdministrationDatabaseContext.java,v 1.5 2005/04/01 10:31:51 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/01 10:31:51 $
 * @author $Author: bass $
 * @module administration_v1
 */
public final class AdministrationDatabaseContext {
	private static UserDatabase	userDatabase;
	private static DomainDatabase	domainDatabase;
	private static ServerDatabase	serverDatabase;
	private static MCMDatabase	mcmDatabase;

	private AdministrationDatabaseContext() {
		assert false; 
	}

	public static void init(
			final UserDatabase	userDatabase1,
			final DomainDatabase	domainDatabase1,
			final ServerDatabase	serverDatabase1,
			final MCMDatabase	mcmDatabase1) {
		if (userDatabase1 != null)
			userDatabase = userDatabase1;
		if (domainDatabase1 != null)
			domainDatabase = domainDatabase1;
		if (serverDatabase1 != null)
			serverDatabase = serverDatabase1;
		if (mcmDatabase1 != null)
			mcmDatabase = mcmDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.USER_ENTITY_CODE:
				return getUserDatabase();
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				return getDomainDatabase();
			case ObjectEntities.SERVER_ENTITY_CODE:
				return getServerDatabase();
			case ObjectEntities.MCM_ENTITY_CODE:
				return getMCMDatabase();
//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//				database = AdministrationDatabaseContext.getPermissionAttributesDatabase();
//				break;
			default:
				Log.errorMessage("AdminDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;
		}
	}

	public static UserDatabase getUserDatabase() {
		return userDatabase;
	}

	public static DomainDatabase getDomainDatabase() {
		return domainDatabase;
	}

	public static ServerDatabase getServerDatabase() {
		return serverDatabase;
	}

	public static MCMDatabase getMCMDatabase() {
		return mcmDatabase;
	}
}
