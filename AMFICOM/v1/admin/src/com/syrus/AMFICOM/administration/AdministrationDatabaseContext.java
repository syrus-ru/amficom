/*
 * $Id: AdministrationDatabaseContext.java,v 1.3 2005/02/04 12:05:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/04 12:05:31 $
 * @author $Author: arseniy $
 * @todo Declare all fields private as<ol>
 *       <li>they have public accessors; and</li>
 *       <li>there's {@link #init(StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase, StorableObjectDatabase)}
 *       method which is supposed to be the only modifier for class' fields.</li></ol>
 * @module administration_v1
 */
public final class AdministrationDatabaseContext {
	protected static StorableObjectDatabase userDatabase;
	protected static StorableObjectDatabase domainDatabase;
	protected static StorableObjectDatabase serverDatabase;
	protected static StorableObjectDatabase mcmDatabase;

	private AdministrationDatabaseContext() {
		// private constructor 
	}

	public static void init(final StorableObjectDatabase userDatabase1,
			final StorableObjectDatabase domainDatabase1,
			final StorableObjectDatabase serverDatabase1,
			final StorableObjectDatabase mcmDatabase1) {
		userDatabase = userDatabase1;
		domainDatabase = domainDatabase1;
		serverDatabase = serverDatabase1;
		mcmDatabase = mcmDatabase1;
	}

	public static StorableObjectDatabase getDatabase(short entityCode) {
		switch (entityCode) {

			case ObjectEntities.USER_ENTITY_CODE:
				return userDatabase;

			case ObjectEntities.DOMAIN_ENTITY_CODE:
				return domainDatabase;

			case ObjectEntities.SERVER_ENTITY_CODE:
				return serverDatabase;

			case ObjectEntities.MCM_ENTITY_CODE:
				return mcmDatabase;

//			case ObjectEntities.PERMATTR_ENTITY_CODE:
//			database = AdministrationDatabaseContext.getPermissionAttributesDatabase();
//			break;

			default:
				return null;
		}
	}

	public static StorableObjectDatabase getUserDatabase() {
		return userDatabase;
	}

	public static StorableObjectDatabase getDomainDatabase() {
		return domainDatabase;
	}

	public static StorableObjectDatabase getServerDatabase() {
		return serverDatabase;
	}

	public static StorableObjectDatabase getMCMDatabase() {
		return mcmDatabase;
	}

}
