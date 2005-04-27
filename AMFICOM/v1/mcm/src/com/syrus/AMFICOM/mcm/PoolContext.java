/*
 * $Id: PoolContext.java,v 1.2 2005/04/27 19:34:06 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/27 19:34:06 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class PoolContext {

	public PoolContext() {
		// singleton
		assert false;
	}

	public static void init() {
		GeneralStorableObjectPool.init(new MCMGeneralObjectLoader(), StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new MCMAdministrationObjectLoader(), StorableObjectResizableLRUMap.class);
		ConfigurationStorableObjectPool.init(new MCMConfigurationObjectLoader(), StorableObjectResizableLRUMap.class);
		MeasurementStorableObjectPool.init(new MCMMeasurementObjectLoader(), StorableObjectResizableLRUMap.class);
	}

	public static void deserialize() {
		GeneralStorableObjectPool.deserializePool();
		AdministrationStorableObjectPool.deserializePool();
		ConfigurationStorableObjectPool.deserializePool();
		MeasurementStorableObjectPool.deserializePool();
	}

	public static void serialize() {
		GeneralStorableObjectPool.serializePool();
		AdministrationStorableObjectPool.serializePool();
		ConfigurationStorableObjectPool.serializePool();
		MeasurementStorableObjectPool.serializePool();
	}
}
