/*-
 * $Id: LEServerPoolContext.java,v 1.20 2006/03/30 12:11:12 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_TYPE_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PARAMETERSET_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSMISSIONPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TRANSPATH_TYPE_CODE;

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
 * @version $Revision: 1.20 $, $Date: 2006/03/30 12:11:12 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LEServerPoolContext extends PoolContext {
	private static LRUMapSaver<Identifier, StorableObject> LRU_MAP_SAVER;

	public LEServerPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);
		final int eventPoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_EVENT_POOL_SIZE, EVENT_POOL_SIZE);

		// All convert to ns
		final long generalPoolTimeToLive = ApplicationProperties.getInt(KEY_GENERAL_POOL_TIME_TO_LIVE, GENERAL_POOL_TIME_TO_LIVE) * 60000000000L;
		final long administrationPoolTimeToLive = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_TIME_TO_LIVE, ADMINISTRATION_POOL_TIME_TO_LIVE) * 60000000000L;
		final long configurationPoolTimeToLive = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_TIME_TO_LIVE, CONFIGURATION_POOL_TIME_TO_LIVE) * 60000000000L;
		final long measurementPoolTimeToLive = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_TIME_TO_LIVE, MEASUREMENT_POOL_TIME_TO_LIVE) * 60000000000L;
		final long eventPoolTimeToLive = ApplicationProperties.getInt(KEY_EVENT_POOL_TIME_TO_LIVE, EVENT_POOL_TIME_TO_LIVE) * 60000000000L;
		final long schemePoolTimeToLive = ApplicationProperties.getInt(KEY_SCHEME_POOL_TIME_TO_LIVE, SCHEME_POOL_TIME_TO_LIVE) * 60000000000L;

		StorableObjectPool.init(super.objectLoader);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize, generalPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize, administrationPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.EVENT_GROUP_CODE, eventPoolSize, eventPoolTimeToLive);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.SCHEME_GROUP_CODE, schemePoolSize, schemePoolTimeToLive);

		StorableObjectPool.addObjectPool(TRANSPATH_TYPE_CODE, configurationPoolSize, configurationPoolTimeToLive);
		StorableObjectPool.addObjectPool(TRANSMISSIONPATH_CODE, configurationPoolSize, configurationPoolTimeToLive);

		StorableObjectPool.addObjectPool(KIS_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPool(MEASUREMENT_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPool(MEASUREMENTPORT_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPool(MEASUREMENTPORT_TYPE_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPool(MEASUREMENTSETUP_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPool(MONITOREDELEMENT_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPool(PARAMETERSET_CODE, measurementPoolSize, measurementPoolTimeToLive);
		StorableObjectPool.addObjectPool(TEST_CODE, measurementPoolSize, measurementPoolTimeToLive);

		if (LRU_MAP_SAVER == null) {
			LRU_MAP_SAVER = new IdentifierLRUMapSaver(super.objectLoader);
		}
	}

	@Override
	public LRUMapSaver<Identifier, StorableObject> getLRUMapSaver() {
		return LRU_MAP_SAVER;
	}
}
