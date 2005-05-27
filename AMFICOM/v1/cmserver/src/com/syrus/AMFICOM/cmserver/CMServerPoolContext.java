/*-
 * $Id: CMServerPoolContext.java,v 1.5 2005/05/27 11:13:49 bass Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/05/27 11:13:49 $
 * @author $Author: bass $
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

	public void init() {
		final boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DATABASE_LOADER_ONLY)).booleanValue();

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		if (!databaseLoaderOnly) {
			/*
			 * refreshTimeout is needed for
			 * MeasurementStorableObjectPool only.
			 */
			final long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, REFRESH_TIMEOUT) * 1000L;

			GeneralStorableObjectPool.init(
					new CMServerGeneralObjectLoader(),
					lruMapClass,
					generalPoolSize);
			AdministrationStorableObjectPool.init(
					new CMServerAdministrationObjectLoader(),
					lruMapClass,
					administrationPoolSize);
			ConfigurationStorableObjectPool.init(
					new CMServerConfigurationObjectLoader(),
					lruMapClass,
					configurationPoolSize);
			MeasurementStorableObjectPool.init(
					new CMServerMeasurementObjectLoader(refreshTimeout),
					lruMapClass,
					measurementPoolSize);
		}
		else {
			GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(),
					lruMapClass,
					generalPoolSize);
			AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(),
					lruMapClass,
					administrationPoolSize);
			ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(),
					lruMapClass,
					configurationPoolSize);
			MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(),
					lruMapClass,
					measurementPoolSize);
		}
	}
}
