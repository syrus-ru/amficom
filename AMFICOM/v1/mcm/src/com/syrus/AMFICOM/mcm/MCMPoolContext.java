/*-
 * $Id: MCMPoolContext.java,v 1.9 2005/09/07 14:31:58 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LRUMapSaver;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.io.LRUSaver;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.9 $, $Date: 2005/09/07 14:31:58 $
 * @author $Author: arseniy $
 * @module mcm
 */
final class MCMPoolContext implements PoolContext {
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
		final ObjectLoader objectLoader = new MCMObjectLoader(this.mcmServantManager);

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

	public LRUSaver<Identifier, StorableObject> getLRUSaver() {
		return LRUMapSaver.getInstance();
	}
}
