/*-
 * $Id: CMServerPoolContext.java,v 1.19 2005/12/08 15:31:24 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

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
 * @version $Revision: 1.19 $, $Date: 2005/12/08 15:31:24 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module cmserver
 */
final class CMServerPoolContext extends PoolContext {
	private static LRUMapSaver<Identifier, StorableObject> LRU_MAP_SAVER;
	
	public CMServerPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);
		final int eventPoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);
		final int reportPoolSize = ApplicationProperties.getInt(KEY_REPORT_POOL_SIZE, REPORT_POOL_SIZE);

		// All convert to ns
		final long generalPoolTimeToLive = ApplicationProperties.getInt(KEY_GENERAL_POOL_TIME_TO_LIVE, GENERAL_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long administrationPoolTimeToLive = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_TIME_TO_LIVE, ADMINISTRATION_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long configurationPoolTimeToLive = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_TIME_TO_LIVE, CONFIGURATION_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long measurementPoolTimeToLive = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_TIME_TO_LIVE, MEASUREMENT_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long eventPoolTimeToLive = ApplicationProperties.getInt(KEY_EVENT_POOL_TIME_TO_LIVE, EVENT_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;
		final long reportPoolTimeToLive = ApplicationProperties.getInt(KEY_REPORT_POOL_TIME_TO_LIVE, REPORT_POOL_TIME_TO_LIVE) * 60 * 1000 * 1000 * 1000;

		StorableObjectPool.init(super.objectLoader);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize, generalPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize, administrationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, configurationPoolSize, configurationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, eventPoolSize, eventPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.REPORT_GROUP_CODE, reportPoolSize, reportPoolTimeToLive);

		if (LRU_MAP_SAVER == null) {
			LRU_MAP_SAVER = new IdentifierLRUMapSaver(super.objectLoader);
		}
	}

	@Override
	public LRUMapSaver<Identifier, StorableObject> getLRUMapSaver() {
		return LRU_MAP_SAVER;
	}
}
