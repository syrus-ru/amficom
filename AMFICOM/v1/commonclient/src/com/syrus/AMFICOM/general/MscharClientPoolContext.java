/*
 * $Id: MscharClientPoolContext.java,v 1.8 2005/07/29 14:28:03 arseniy Exp $
 * 
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.8 $, $Date: 2005/07/29 14:28:03 $
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

	public MscharClientPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
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
