/*-
 * $Id: CMServerPoolContext.java,v 1.2 2005/05/13 17:50:01 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
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
 * @version $Revision: 1.2 $, $Date: 2005/05/13 17:50:01 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
final class CMServerPoolContext extends PoolContext {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize"; //$NON-NLS-1$
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize"; //$NON-NLS-1$
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize"; //$NON-NLS-1$
	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize"; //$NON-NLS-1$
	public static final String KEY_REFRESH_TIMEOUT = "RefreshPoolTimeout"; //$NON-NLS-1$
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly"; //$NON-NLS-1$

	
	public static final int GENERAL_POOL_SIZE = 1000;
	public static final int ADMINISTRATION_POOL_SIZE = 1000;
	public static final int CONFIGURATION_POOL_SIZE = 1000;
	public static final int MEASUREMENT_POOL_SIZE = 1000;
	public static final int REFRESH_TIMEOUT = 5;// min
	public static final String DATABASE_LOADER_ONLY = "false"; //$NON-NLS-1$

	public void init() {
		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DATABASE_LOADER_ONLY)).booleanValue();

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(),
				lruMapClass,
				generalPoolSize);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(),
				lruMapClass,
				administrationPoolSize);		

		if (!databaseLoaderOnly) {
			final long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, REFRESH_TIMEOUT) * 1000L * 60L;
			ConfigurationStorableObjectPool.init(new CMServerConfigurationObjectLoader(refreshTimeout),
					lruMapClass,
					configurationPoolSize);
			MeasurementStorableObjectPool.init(new CMServerMeasurementObjectLoader(refreshTimeout),
					lruMapClass,
					measurementPoolSize);
		}
		else {
			ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(),
					lruMapClass,
					configurationPoolSize);
			MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(),
					lruMapClass,
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
