/*-
 * $Id: SystemServerPoolContext.java,v 1.1.1.1 2006/06/23 09:48:20 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierLRUMapSaver;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2006/06/23 09:48:20 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class SystemServerPoolContext extends PoolContext {
	private static LRUMapSaver<Identifier, StorableObject> LRU_MAP_SAVER;

	private static SystemServerPoolContext instance;

	private SystemServerPoolContext() {
		super(new DatabaseObjectLoader());
	}

	public static SystemServerPoolContext getInstance() {
		if (instance == null) {
			instance = new SystemServerPoolContext();
		}
		return instance;
	}

	@Override
	public void init() {
		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);
		final int eventPoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);
		final int resourcePoolSize = ApplicationProperties.getInt(KEY_RESOURCE_POOL_SIZE, RESOURCE_POOL_SIZE);
		final int reportPoolSize = ApplicationProperties.getInt(KEY_REPORT_POOL_SIZE, REPORT_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, SCHEME_POOL_SIZE);
		final int mapViewPoolSize = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_SIZE, MAPVIEW_POOL_SIZE);

		// All convert to ns
		final long generalPoolTimeToLive = ApplicationProperties.getInt(KEY_GENERAL_POOL_TIME_TO_LIVE, GENERAL_POOL_TIME_TO_LIVE) * 60000000000L;
		final long administrationPoolTimeToLive = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_TIME_TO_LIVE, ADMINISTRATION_POOL_TIME_TO_LIVE) * 60000000000L;
		final long configurationPoolTimeToLive = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_TIME_TO_LIVE, CONFIGURATION_POOL_TIME_TO_LIVE) * 60000000000L;
		final long measurementPoolTimeToLive = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_TIME_TO_LIVE, MEASUREMENT_POOL_TIME_TO_LIVE) * 60000000000L;
		final long eventPoolTimeToLive = ApplicationProperties.getInt(KEY_EVENT_POOL_TIME_TO_LIVE, EVENT_POOL_TIME_TO_LIVE) * 60000000000L;
		final long resourcePoolTimeToLive = ApplicationProperties.getInt(KEY_RESOURCE_POOL_TIME_TO_LIVE, RESOURCE_POOL_TIME_TO_LIVE) * 60000000000L;
		final long reportPoolTimeToLive = ApplicationProperties.getInt(KEY_REPORT_POOL_TIME_TO_LIVE, REPORT_POOL_TIME_TO_LIVE) * 60000000000L;
		final long mapPoolTimeToLive = ApplicationProperties.getInt(KEY_MAP_POOL_TIME_TO_LIVE, MAP_POOL_TIME_TO_LIVE) * 60000000000L;
		final long schemePoolTimeToLive = ApplicationProperties.getInt(KEY_SCHEME_POOL_TIME_TO_LIVE, SCHEME_POOL_TIME_TO_LIVE) * 60000000000L;
		final long mapViewPoolTimeToLive = ApplicationProperties.getInt(KEY_MAPVIEW_POOL_TIME_TO_LIVE, MAPVIEW_POOL_TIME_TO_LIVE) * 60000000000L;

		StorableObjectPool.init(super.objectLoader);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize, generalPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize, administrationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, configurationPoolSize, configurationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, eventPoolSize, eventPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.RESOURCE_GROUP_CODE, resourcePoolSize, resourcePoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.REPORT_GROUP_CODE, reportPoolSize, reportPoolTimeToLive);
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
