/*-
 * $Id: AdministrationDatabaseContext.java,v 1.8 2005/05/23 18:45:12 bass Exp $
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
 * @version $Revision: 1.8 $, $Date: 2005/05/23 18:45:12 $
 * @author $Author: bass $
 * @module administration_v1
 */
public final class AdministrationDatabaseContext {
	private static UserDatabase userDatabase;
	private static DomainDatabase domainDatabase;
	private static ServerDatabase serverDatabase;
	private static MCMDatabase mcmDatabase;
	private static ServerProcessDatabase serverProcessDatabase;

	private AdministrationDatabaseContext() {
		assert false;
	}

	public static void init(
			final UserDatabase	userDatabase1,
			final DomainDatabase	domainDatabase1,
			final ServerDatabase	serverDatabase1,
			final MCMDatabase	mcmDatabase1,
			final ServerProcessDatabase serverProcessDatabase1) {
		if (userDatabase1 != null)
			userDatabase = userDatabase1;
		if (domainDatabase1 != null)
			domainDatabase = domainDatabase1;
		if (serverDatabase1 != null)
			serverDatabase = serverDatabase1;
		if (mcmDatabase1 != null)
			mcmDatabase = mcmDatabase1;
		if (serverProcessDatabase1 != null)
			serverProcessDatabase = serverProcessDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode) {
		switch (entityCode) {
			case ObjectEntities.USER_ENTITY_CODE:
				return userDatabase;
			case ObjectEntities.DOMAIN_ENTITY_CODE:
				return domainDatabase;
			case ObjectEntities.SERVER_ENTITY_CODE:
				return serverDatabase;
			case ObjectEntities.MCM_ENTITY_CODE:
				return mcmDatabase;
			case ObjectEntities.SERVERPROCESS_ENTITY_CODE:
				return serverProcessDatabase;
			default:
				Log.errorMessage("AdminDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
