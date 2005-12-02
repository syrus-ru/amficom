/*
 * $Id: ClientPoolContext.java,v 1.21 2005/12/02 15:21:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.LRUMapSaver;

/**
 * @version $Revision: 1.21 $, $Date: 2005/12/02 15:21:21 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
class ClientPoolContext extends PoolContext {

	private static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	private static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	private static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	private static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";
	private static final String KEY_REPORT_POOL_SIZE = "ReportPoolSize";

	private static final int GENERAL_POOL_SIZE = 1000;
	private static final int ADMINISTRATION_POOL_SIZE = 1000;
	private static final int CONFIGURATION_POOL_SIZE = 1000;
	private static final int MEASUREMENT_POOL_SIZE = 1000;
	private static final int REPORT_POOL_SIZE = 1000;

	private static final LRUMapSaver<Identifier, StorableObject> LRU_MAP_SAVER = new StorableObjectLRUMapSaver();

	public ClientPoolContext(final ObjectLoader objectLoader) {
		super(objectLoader);
	}

	@Override
	public void init() {
		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);
		final int reportPoolSize = ApplicationProperties.getInt(KEY_REPORT_POOL_SIZE, REPORT_POOL_SIZE);

		StorableObjectPool.init(super.objectLoader, lruMapClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, configurationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, measurementPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.REPORT_GROUP_CODE, reportPoolSize);
	}

	@Override
	public LRUMapSaver<Identifier, StorableObject> getLRUMapSaver() {
		return LRU_MAP_SAVER;
	}
}
