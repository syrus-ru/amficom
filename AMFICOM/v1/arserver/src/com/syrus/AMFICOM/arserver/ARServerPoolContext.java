/*-
 * $Id: ARServerPoolContext.java,v 1.5 2005/06/07 12:30:28 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/07 12:30:28 $
 * @module arserver_v1
 */
final class ARServerPoolContext extends PoolContext {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize"; 

	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";

	public static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";

	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int GENERAL_POOL_SIZE = 1000;

	public static final int ADMINISTRATION_POOL_SIZE = 1000;

	public static final int RESOURCE_POOL_SIZE = 1000;

	public static final String DATABASE_LOADER_ONLY = "false";

	public void init() {
		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DATABASE_LOADER_ONLY)).booleanValue();
		final Class lruMapClass = StorableObjectResizableLRUMap.class;
		ResourceStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseResourceObjectLoader()
						: new ARServerResourceObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE));
		GeneralStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseGeneralObjectLoader()
						: new ARServerGeneralObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE));
		AdministrationStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseAdministrationObjectLoader()
						: new ARServerAdministrationObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE));
	}
}
