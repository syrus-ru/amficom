/*
 * $Id: ClientPoolContext.java,v 1.17 2005/09/07 14:11:42 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.io.LRUSaver;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.17 $, $Date: 2005/09/07 14:11:42 $
 * @author $Author: arseniy $
 * @module commonclient
 */
class ClientPoolContext implements PoolContext {

	private static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	private static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	private static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	protected static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";

	private static final int GENERAL_POOL_SIZE = 1000;
	private static final int ADMINISTRATION_POOL_SIZE = 1000;
	private static final int CONFIGURATION_POOL_SIZE = 1000;
	protected static final int MEASUREMENT_POOL_SIZE = 1000;

	ObjectLoader objectLoader;

	public ClientPoolContext(final ObjectLoader objectLoader) {
		this.objectLoader = objectLoader;
	}

	public void init() {
		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);

		StorableObjectPool.init(this.objectLoader, lruMapClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, configurationPoolSize);
	}

	public LRUSaver<Identifier, StorableObject> getLRUSaver() {
		return ClientLRUMapSaver.getInstance();
	}
}
