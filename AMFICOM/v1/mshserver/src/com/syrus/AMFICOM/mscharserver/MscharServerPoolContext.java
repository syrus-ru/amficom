/*-
 * $Id: MscharServerPoolContext.java,v 1.2 2005/06/15 15:55:59 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.administration.AdministrationObjectLoader;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.map.DatabaseMapObjectLoader;
import com.syrus.AMFICOM.map.MapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mapview.DatabaseMapViewObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.resource.DatabaseResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.scheme.DatabaseSchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/06/15 15:55:59 $
 * @module mscharserver_v1
 */
final class MscharServerPoolContext extends PoolContext {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize"; 
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	public static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";
	public static final String KEY_MAP_POOL_SIZE = "MapPoolSize";
	public static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";
	public static final String KEY_MAPVIEW_POOL_SIZE = "MapViewPoolSize";
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int GENERAL_POOL_SIZE = 1000;
	public static final int ADMINISTRATION_POOL_SIZE = 1000;
	public static final int RESOURCE_POOL_SIZE = 1000;
	public static final int MAP_POOL_SIZE = 1000;
	public static final int SCHEME_POOL_SIZE = 1000;
	public static final int MAPVIEW_POOL_SIZE = 1000;
	public static final String DATABASE_LOADER_ONLY = "false";

	/**
	 * @see com.syrus.AMFICOM.general.PoolContext#init()
	 */
	public void init() {
		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY,
				DATABASE_LOADER_ONLY)).booleanValue();

		GeneralObjectLoader generalObjectLoader;
		AdministrationObjectLoader administrationObjectLoader;
		ResourceObjectLoader resourceObjectLoader;
		MapObjectLoader mapObjectLoader;
		SchemeObjectLoader schemeObjectLoader;
		MapViewObjectLoader mapViewObjectLoader;
		if (!databaseLoaderOnly) {
			generalObjectLoader = new MscharServerGeneralObjectLoader();
			administrationObjectLoader = new MscharServerAdministrationObjectLoader();
			resourceObjectLoader = new MscharServerResourceObjectLoader();
			mapObjectLoader = new MscharServerMapObjectLoader();
			schemeObjectLoader = new MscharServerSchemeObjectLoader();
			mapViewObjectLoader = new MscharServerMapViewObjectLoader();
		}
		else {
			generalObjectLoader = new DatabaseGeneralObjectLoader();
			administrationObjectLoader = new DatabaseAdministrationObjectLoader();
			resourceObjectLoader = new DatabaseResourceObjectLoader();
			mapObjectLoader = new DatabaseMapObjectLoader();
			schemeObjectLoader = new DatabaseSchemeObjectLoader();
			mapViewObjectLoader = new DatabaseMapViewObjectLoader();
		}

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE);
		final int mapViewPoolSize = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_SIZE, MAPVIEW_POOL_SIZE);

		GeneralStorableObjectPool.init(generalObjectLoader, lruMapClass, generalPoolSize);
		AdministrationStorableObjectPool.init(administrationObjectLoader, lruMapClass, administrationPoolSize);
		ResourceStorableObjectPool.init(resourceObjectLoader, lruMapClass, resourcePoolSize);
		MapStorableObjectPool.init(mapObjectLoader, lruMapClass, mapPoolSize);
		SchemeStorableObjectPool.init(schemeObjectLoader, lruMapClass, schemePoolSize);
		MapViewStorableObjectPool.init(mapViewObjectLoader, lruMapClass, mapViewPoolSize);
	}
}
