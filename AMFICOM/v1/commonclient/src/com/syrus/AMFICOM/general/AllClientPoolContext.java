/*
 * $Id: AllClientPoolContext.java,v 1.2 2005/12/08 15:31:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.2 $, $Date: 2005/12/08 15:31:06 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
final class AllClientPoolContext extends ClientPoolContext {

	public AllClientPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		super.init();

		final int eventPoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);
		final int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE);
		final int mapViewPoolSize = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_SIZE, MAPVIEW_POOL_SIZE);

		final long eventPoolTimeToLive = ApplicationProperties.getInt(KEY_EVENT_POOL_TIME_TO_LIVE, EVENT_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long resourcePoolTimeToLive = ApplicationProperties.getInt(KEY_RESOURCE_POOL_TIME_TO_LIVE, RESOURCE_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long mapPoolTimeToLive = ApplicationProperties.getInt(KEY_MAP_POOL_TIME_TO_LIVE, MAP_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long schemePoolTimeToLive = ApplicationProperties.getInt(KEY_SCHEME_POOL_TIME_TO_LIVE, SCHEME_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long mapViewPoolTimeToLive = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_TIME_TO_LIVE, MAP_VIEW_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;

		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, eventPoolSize, eventPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, resourcePoolSize, resourcePoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAP_GROUP_CODE, mapPoolSize, mapPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, schemePoolSize, schemePoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAPVIEW_GROUP_CODE, mapViewPoolSize, mapViewPoolTimeToLive);
	}
}
