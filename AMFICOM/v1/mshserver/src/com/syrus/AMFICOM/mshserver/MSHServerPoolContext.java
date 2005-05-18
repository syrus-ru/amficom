/*-
 * $Id: MSHServerPoolContext.java,v 1.2 2005/05/18 13:34:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.map.DatabaseMapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.scheme.DatabaseSchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/05/18 13:34:16 $
 * @module mshserver_v1
 */
final class MSHServerPoolContext extends PoolContext {
	public static final String KEY_MAP_POOL_SIZE = "MapPoolSize";

	public static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";

	public static final String KEY_REFRESH_TIMEOUT = "RefreshPoolTimeout";

	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int MAP_POOL_SIZE = 1000;

	public static final int SCHEME_POOL_SIZE = 1000;

	/**
	 * Refresh timeout in minutes;
	 */
	public static final int REFRESH_TIMEOUT = 5;

	public static final String DATABASE_LOADER_ONLY = "false";

	/**
	 * @see com.syrus.AMFICOM.general.PoolContext#init()
	 */
	public void init() {
		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DATABASE_LOADER_ONLY)).booleanValue();
		final long refreshTimeoutMillis = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, REFRESH_TIMEOUT) * 1000L * 60L;
		final Class lruMapClass = StorableObjectResizableLRUMap.class;
		MapStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseMapObjectLoader()
						: new MSHServerMapObjectLoader(refreshTimeoutMillis),
				lruMapClass,
				ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE));
		SchemeStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseSchemeObjectLoader()
						: new MSHServerSchemeObjectLoader(refreshTimeoutMillis),
				lruMapClass,
				ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE));
	}

	/**
	 * @see com.syrus.AMFICOM.general.PoolContext#deserialize()
	 */
	public void deserialize() {
		MapStorableObjectPool.deserializePool();
		SchemeStorableObjectPool.deserializePool();
	}

	/**
	 * @see com.syrus.AMFICOM.general.PoolContext#serialize()
	 */
	public void serialize() {
		SchemeStorableObjectPool.serializePool();
		MapStorableObjectPool.serializePool();
	}
}
