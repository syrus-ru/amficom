/*
 * $Id: MscharClientPoolContext.java,v 1.7 2005/07/28 19:45:42 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.7 $, $Date: 2005/07/28 19:45:42 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
final class MscharClientPoolContext extends ClientPoolContext {
	public static final String KEY_RESOURCE_POOL_SIZE = "ResourcePoolSize";
	public static final String KEY_MAP_POOL_SIZE = "MapPoolSize";
	public static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";
	public static final String KEY_MAPVIEW_POOL_SIZE = "MapViewPoolSize";

	private static final int RESOURCE_POOL_SIZE = 1000;
	private static final int MAP_POOL_SIZE = 1000;
	private static final int SCHEME_POOL_SIZE = 1000;
	private static final int MAP_VIEW_POOL_SIZE = 1000;

	/**
	 * @todo Implement multi-servant work
	 */
	private MscharClientServantManager	mscharClientServantManager;

	public MscharClientPoolContext(final MClientServantManager mClientServantManager,
			final MscharClientServantManager mscharClientServantManager) {
		super(mClientServantManager);
		this.mscharClientServantManager = mscharClientServantManager;
	}

	public MscharClientPoolContext(final String xmlPath) {
		super(xmlPath);
	}

	@Override
	public void init() {
		super.init();

		final int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE);
		final int mapViewPoolSize = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_SIZE, MAP_VIEW_POOL_SIZE);

		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, resourcePoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAP_GROUP_CODE, mapPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, schemePoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAPVIEW_GROUP_CODE, mapViewPoolSize);
	}
}
