/*
 * $Id: DatabaseContextSetup.java,v 1.1 2005/01/17 16:34:05 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.resource.ImageResourceDatabase;
import com.syrus.AMFICOM.resource.ResourceDatabaseContext;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.util.ApplicationProperties;



/**
 * @version $Revision: 1.1 $, $Date: 2005/01/17 16:34:05 $
 * @author $Author: max $
 * @module mserver_v1
 */

public abstract class DatabaseContextSetup {
	
	public static final String 	KEY_RESOURCE_POOL_SIZE = "ImageResourcePoolSize";
	public static final String 	KEY_REFRESH_TIMEOUT = "RefreshTimeout";
	public static final String 	KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int 	DEFAULT_RESOURCE_POOL_SIZE = 1000;
	public static final int 	DEFAULT_REFRESH_TIMEOUT = 5;
	public static final String 	DEFAULT_DATABASE_LOADER_ONLY = "false";

	private DatabaseContextSetup() {
		// empty
	}

	public static void initDatabaseContext() {
		ResourceDatabaseContext.init(new ImageResourceDatabase());				
	}

	public static void initObjectPools() {
		boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DEFAULT_DATABASE_LOADER_ONLY)).booleanValue();
		int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, DEFAULT_RESOURCE_POOL_SIZE);
		if (! databaseLoaderOnly) {
			long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, DEFAULT_REFRESH_TIMEOUT) * 1000L * 60L;
			ResourceStorableObjectPool.init(new ARServerResourceObjectLoader(refreshTimeout), resourcePoolSize);			
		}
		else {
			ResourceStorableObjectPool.init(new DatabaseResourceObjectLoader(), resourcePoolSize);			
		}
	}
}
