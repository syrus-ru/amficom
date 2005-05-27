/*-
 * $Id: ARServerPoolContext.java,v 1.4 2005/05/27 11:13:49 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/27 11:13:49 $
 * @module arserver_v1
 */
final class ARServerPoolContext extends PoolContext {
	public static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";

	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int RESOURCE_POOL_SIZE = 1000;

	public static final String DATABASE_LOADER_ONLY = "false";

	public void init() {
		ResourceStorableObjectPool.init(Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DATABASE_LOADER_ONLY)).booleanValue()
						? new DatabaseResourceObjectLoader()
						: new ARServerResourceObjectLoader(),
				StorableObjectResizableLRUMap.class,
				ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE));
	}
}
