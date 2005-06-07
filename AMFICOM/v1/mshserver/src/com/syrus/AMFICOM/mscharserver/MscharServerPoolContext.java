/*-
 * $Id: MscharServerPoolContext.java,v 1.1 2005/06/07 16:47:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.map.DatabaseMapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.DatabaseMapViewObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.scheme.DatabaseSchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/06/07 16:47:00 $
 * @module mscharserver_v1
 */
final class MscharServerPoolContext extends PoolContext {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize"; 

	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";

	public static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";

	public static final String KEY_MAP_POOL_SIZE = "MapPoolSize";

	public static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";

	public static final String KEY_MAP_VIEW_POOL_SIZE = "MapViewPoolSize";

	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int GENERAL_POOL_SIZE = 1000;

	public static final int ADMINISTRATION_POOL_SIZE = 1000;

	public static final int RESOURCE_POOL_SIZE = 1000;

	public static final int MAP_POOL_SIZE = 1000;

	public static final int SCHEME_POOL_SIZE = 1000;

	public static final int MAP_VIEW_POOL_SIZE = 1000;

	public static final String DATABASE_LOADER_ONLY = "false";

	/**
	 * @see com.syrus.AMFICOM.general.PoolContext#init()
	 */
	public void init() {
		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DATABASE_LOADER_ONLY)).booleanValue();
		final Class lruMapClass = StorableObjectResizableLRUMap.class;
		GeneralStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseGeneralObjectLoader()
						: new MscharServerGeneralObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE));
		AdministrationStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseAdministrationObjectLoader()
						: new MscharServerAdministrationObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE));
		ResourceStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseResourceObjectLoader()
						: new MscharServerResourceObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE));
		MapStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseMapObjectLoader()
						: new MscharServerMapObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE));
		SchemeStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseSchemeObjectLoader()
						: new MscharServerSchemeObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE));
		MapViewStorableObjectPool.init(databaseLoaderOnly
						? new DatabaseMapViewObjectLoader()
						: new MscharServerMapViewObjectLoader(),
				lruMapClass,
				ApplicationProperties.getInt(KEY_MAP_VIEW_POOL_SIZE, MAP_VIEW_POOL_SIZE));
	}
}
