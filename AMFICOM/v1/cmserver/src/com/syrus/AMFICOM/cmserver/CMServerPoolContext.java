/*
 * $Id: CMServerPoolContext.java,v 1.1 2005/05/01 17:24:13 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/01 17:24:13 $
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
	public static final int REFRESH_TIMEOUT = 5;// min
	public static final String DATABASE_LOADER_ONLY = "false";

	public CMServerPoolContext() {
		// Nothing
	}

	public void init() {
		boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DATABASE_LOADER_ONLY)).booleanValue();

		int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);

		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(),
				StorableObjectResizableLRUMap.class,
				generalPoolSize);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(),
				StorableObjectResizableLRUMap.class,
				administrationPoolSize);		

		if (!databaseLoaderOnly) {
			long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, REFRESH_TIMEOUT) * 1000L * 60L;
			ConfigurationStorableObjectPool.init(new CMServerConfigurationObjectLoader(refreshTimeout),
					StorableObjectResizableLRUMap.class,
					configurationPoolSize);
			MeasurementStorableObjectPool.init(new CMServerMeasurementObjectLoader(refreshTimeout),
					StorableObjectResizableLRUMap.class,
					measurementPoolSize);
		}
		else {
			ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(),
					StorableObjectResizableLRUMap.class,
					configurationPoolSize);
			MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(),
					StorableObjectResizableLRUMap.class,
					measurementPoolSize);
		}
	}

	public void deserialize() {
		GeneralStorableObjectPool.deserializePool();
		AdministrationStorableObjectPool.deserializePool();
		ConfigurationStorableObjectPool.deserializePool();
		MeasurementStorableObjectPool.deserializePool();
	}

	public void serialize() {
		GeneralStorableObjectPool.serializePool();
		AdministrationStorableObjectPool.serializePool();
		ConfigurationStorableObjectPool.serializePool();
		MeasurementStorableObjectPool.serializePool();
	}
}
