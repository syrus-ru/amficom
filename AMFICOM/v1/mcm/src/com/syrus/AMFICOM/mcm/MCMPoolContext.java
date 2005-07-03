/*-
 * $Id: MCMPoolContext.java,v 1.5 2005/06/15 15:54:18 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.administration.AdministrationObjectLoader;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.GeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/15 15:54:18 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMPoolContext extends PoolContext {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";

	public static final int GENERAL_POOL_SIZE = 1000;
	public static final int ADMINISTRATION_POOL_SIZE = 1000;
	public static final int CONFIGURATION_POOL_SIZE = 1000;
	public static final int MEASUREMENT_POOL_SIZE = 1000;

	private MCMServantManager mcmServantManager;

	public MCMPoolContext(final MCMServantManager mcmServantManager) {
		this.mcmServantManager = mcmServantManager;
	}

	public void init() {
		final GeneralObjectLoader generalObjectLoader = new MCMGeneralObjectLoader(this.mcmServantManager);
		final AdministrationObjectLoader administrationObjectLoader = new MCMAdministrationObjectLoader(this.mcmServantManager);
		final ConfigurationObjectLoader configurationObjectLoader = new MCMConfigurationObjectLoader(this.mcmServantManager);
		final MeasurementObjectLoader measurementObjectLoader = new MCMMeasurementObjectLoader(this.mcmServantManager);

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);
		final int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, MEASUREMENT_POOL_SIZE);

		GeneralStorableObjectPool.init(generalObjectLoader, lruMapClass, generalPoolSize);
		AdministrationStorableObjectPool.init(administrationObjectLoader, lruMapClass, administrationPoolSize);
		ConfigurationStorableObjectPool.init(configurationObjectLoader, lruMapClass, configurationPoolSize);
		MeasurementStorableObjectPool.init(measurementObjectLoader, lruMapClass, measurementPoolSize);
	}
}
