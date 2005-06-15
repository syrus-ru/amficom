/*
 * $Id: MscharClientPoolContext.java,v 1.4 2005/06/15 15:52:22 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.map.CORBAMapObjectLoader;
import com.syrus.AMFICOM.map.MapObjectLoader;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.XMLMapObjectLoader;
import com.syrus.AMFICOM.mapview.CORBAMapViewObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewObjectLoader;
import com.syrus.AMFICOM.mapview.MapViewStorableObjectPool;
import com.syrus.AMFICOM.mapview.XMLMapViewObjectLoader;
import com.syrus.AMFICOM.resource.CORBAResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceObjectLoader;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.XMLResourceObjectLoader;
import com.syrus.AMFICOM.scheme.CORBASchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeObjectLoader;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.XMLSchemeObjectLoader;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/15 15:52:22 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MscharClientPoolContext extends ClientPoolContext {
	public static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";
	public static final String KEY_MAP_POOL_SIZE = "MapPoolSize";
	public static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";
	public static final String KEY_MAPVIEW_POOL_SIZE = "MapViewPoolSize";

	private MscharClientServantManager	mscharClientServantManager;

	public MscharClientPoolContext(
			final MClientServantManager mClientServantManager,
			final MscharClientServantManager mscharClientServantManager) {
		super(mClientServantManager);
		this.mscharClientServantManager = mscharClientServantManager;
	}

	public MscharClientPoolContext(final String xmlPath) {
		super(xmlPath);
	}

	public void init() {
		super.init();

		ResourceObjectLoader resourceObjectLoader;
		MapObjectLoader mapObjectLoader;
		SchemeObjectLoader schemeObjectLoader;
		MapViewObjectLoader mapViewObjectLoader;
		if (super.xmlFile == null) {
			resourceObjectLoader = new CORBAResourceObjectLoader(this.mscharClientServantManager);
			mapObjectLoader = new CORBAMapObjectLoader(this.mscharClientServantManager);
			schemeObjectLoader = new CORBASchemeObjectLoader(this.mscharClientServantManager);
			mapViewObjectLoader = new CORBAMapViewObjectLoader(this.mscharClientServantManager);
		}
		else {
			resourceObjectLoader = new XMLResourceObjectLoader(this.xmlFile);
			mapObjectLoader = new XMLMapObjectLoader(this.xmlFile);
			schemeObjectLoader = new XMLSchemeObjectLoader(this.xmlFile);
			mapViewObjectLoader = new XMLMapViewObjectLoader(this.xmlFile);
		}

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, -1);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, -1);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, -1);
		final int mapViewPoolSize = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_SIZE, -1);

		ResourceStorableObjectPool.init(resourceObjectLoader, lruMapClass, resourcePoolSize);
		MapStorableObjectPool.init(mapObjectLoader, lruMapClass, mapPoolSize);
		SchemeStorableObjectPool.init(schemeObjectLoader, lruMapClass, schemePoolSize);
		MapViewStorableObjectPool.init(mapViewObjectLoader, lruMapClass, mapViewPoolSize);
	}
}
