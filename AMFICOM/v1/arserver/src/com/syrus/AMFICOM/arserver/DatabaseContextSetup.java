/*
 * $Id: DatabaseContextSetup.java,v 1.4 2005/04/08 09:09:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;
import com.syrus.AMFICOM.resource.ImageResourceDatabase;
import com.syrus.AMFICOM.resource.ResourceDatabaseContext;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.4 $, $Date: 2005/04/08 09:09:26 $
 * @author $Author: bass $
 * @module mserver_v1
 */

class DatabaseContextSetup {
	public static final String 	KEY_RESOURCE_POOL_SIZE = "ImageResourcePoolSize"; //$NON-NLS-1$
	public static final String 	KEY_GENERAL_POOL_SIZE = "GeneralPoolSize"; //$NON-NLS-1$
	public static final String 	KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize"; //$NON-NLS-1$
	public static final String 	KEY_REFRESH_TIMEOUT = "RefreshTimeout"; //$NON-NLS-1$
	public static final String 	KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly"; //$NON-NLS-1$

	public static final int 	DEFAULT_RESOURCE_POOL_SIZE = 1000;
	public static final int 	DEFAULT_GENERAL_POOL_SIZE = 1000;
	public static final int 	DEFAULT_ADMINISTRATION_POOL_SIZE = 1000;
	public static final int 	DEFAULT_REFRESH_TIMEOUT = 5;
	public static final String 	DEFAULT_DATABASE_LOADER_ONLY = "false"; //$NON-NLS-1$

	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		ResourceDatabaseContext.init(new ImageResourceDatabase());
		AdministrationDatabaseContext.init(null, new DomainDatabase(), null, null);
		GeneralDatabaseContext.init(null , null, new CharacteristicDatabase());
	}

	public static void initObjectPools() {
		boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DEFAULT_DATABASE_LOADER_ONLY)).booleanValue();
		int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, DEFAULT_RESOURCE_POOL_SIZE);
		int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, DEFAULT_GENERAL_POOL_SIZE);
		int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, DEFAULT_ADMINISTRATION_POOL_SIZE);
		if (! databaseLoaderOnly) {
			long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, DEFAULT_REFRESH_TIMEOUT) * 1000L * 60L;
			ResourceStorableObjectPool.init(new ARServerResourceObjectLoader(refreshTimeout), resourcePoolSize);			
		}
		else {
			ResourceStorableObjectPool.init(new DatabaseResourceObjectLoader(), resourcePoolSize);			
		}
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), generalPoolSize);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), administrationPoolSize);
	}
}
