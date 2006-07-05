/*-
 * $Id: MscharServerPoolContext.java,v 1.13 2006/02/03 13:22:58 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierLRUMapSaver;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMapSaver;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2006/02/03 13:22:58 $
 * @module mscharserver
 */
final class MscharServerPoolContext extends PoolContext {
	private static LRUMapSaver<Identifier, StorableObject> LRU_MAP_SAVER;

	public MscharServerPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE);
		final int mapViewPoolSize = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_SIZE, MAPVIEW_POOL_SIZE);

		// All convert to ns
		final long generalPoolTimeToLive = ApplicationProperties.getInt(KEY_GENERAL_POOL_TIME_TO_LIVE, GENERAL_POOL_TIME_TO_LIVE) * 60000000000L;
		final long administrationPoolTimeToLive = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_TIME_TO_LIVE, ADMINISTRATION_POOL_TIME_TO_LIVE) * 60000000000L;
		final long resourcePoolTimeToLive = ApplicationProperties.getInt(KEY_RESOURCE_POOL_TIME_TO_LIVE, RESOURCE_POOL_TIME_TO_LIVE) * 60000000000L;
		final long mapPoolTimeToLive = ApplicationProperties.getInt(KEY_MAP_POOL_TIME_TO_LIVE, MAP_POOL_TIME_TO_LIVE) * 60000000000L;
		final long schemePoolTimeToLive = ApplicationProperties.getInt(KEY_SCHEME_POOL_TIME_TO_LIVE, SCHEME_POOL_TIME_TO_LIVE) * 60000000000L;
		final long mapViewPoolTimeToLive = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_TIME_TO_LIVE, MAPVIEW_POOL_TIME_TO_LIVE) * 60000000000L;

		StorableObjectPool.init(super.objectLoader);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize, generalPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize, administrationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, resourcePoolSize, resourcePoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAP_GROUP_CODE, mapPoolSize, mapPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, schemePoolSize, schemePoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MAPVIEW_GROUP_CODE, mapViewPoolSize, mapViewPoolTimeToLive);

		if (LRU_MAP_SAVER == null) {
			LRU_MAP_SAVER = new IdentifierLRUMapSaver(super.objectLoader);
		}
	}

	@Override
	public LRUMapSaver<Identifier, StorableObject> getLRUMapSaver() {
		return LRU_MAP_SAVER;
	}
}
