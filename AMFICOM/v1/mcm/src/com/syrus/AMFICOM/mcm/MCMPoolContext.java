/*-
 * $Id: MCMPoolContext.java,v 1.4 2005/06/01 20:56:02 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/01 20:56:02 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMPoolContext extends PoolContext {
	private MCMServantManager mcmServantManager;

	public MCMPoolContext(final MCMServantManager mcmServantManager) {
		this.mcmServantManager = mcmServantManager;
	}

	public void init() {
		GeneralStorableObjectPool.init(new MCMGeneralObjectLoader(this.mcmServantManager), StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new MCMAdministrationObjectLoader(this.mcmServantManager),
				StorableObjectResizableLRUMap.class);
		ConfigurationStorableObjectPool.init(new MCMConfigurationObjectLoader(this.mcmServantManager),
				StorableObjectResizableLRUMap.class);
		MeasurementStorableObjectPool.init(new MCMMeasurementObjectLoader(this.mcmServantManager),
				StorableObjectResizableLRUMap.class);
	}
}
