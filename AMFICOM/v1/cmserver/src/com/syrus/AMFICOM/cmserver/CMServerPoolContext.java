/*-
 * $Id: CMServerPoolContext.java,v 1.9 2005/07/28 10:25:34 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.9 $, $Date: 2005/07/28 10:25:34 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
final class CMServerPoolContext extends PoolContext {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";
	public static final String KEY_REFRESH_TIMEOUT = "RefreshPoolTimeout";
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int GENERAL_POOL_SIZE = 1000;
	public static final int ADMINISTRATION_POOL_SIZE = 1000;
	public static final int CONFIGURATION_POOL_SIZE = 1000;
	public static final int MEASUREMENT_POOL_SIZE = 1000;
	/**
	 * Refresh timeout in seconds.
	 */
	public static final int REFRESH_TIMEOUT = 30;
	public static final String DATABASE_LOADER_ONLY = "false";

	private CMServerServantManager cmServerServantManager;

	public CMServerPoolContext(final CMServerServantManager cmServerServantManager) {
		this.cmServerServantManager = cmServerServantManager;
	}

	@Override
	public void init() {
		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY,
				DATABASE_LOADER_ONLY)).booleanValue();

		ObjectLoader objectLoader;
		if (!databaseLoaderOnly) {
			final long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, REFRESH_TIMEOUT) * 1000L;
			objectLoader = new CMServerObjectLoader(refreshTimeout, this.cmServerServantManager);
		}
		else {
			objectLoader = new DatabaseObjectLoader();
		}

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);

		StorableObjectPool.init(objectLoader, lruMapClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, configurationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.MEASUREMENT_GROUP_CODE, measurementPoolSize);
	}
}
