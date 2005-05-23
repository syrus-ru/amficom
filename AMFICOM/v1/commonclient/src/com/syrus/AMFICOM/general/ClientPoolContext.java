/*
 * $Id: ClientPoolContext.java,v 1.2 2005/05/23 08:40:15 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.CORBAAdministrationObjectLoader;
import com.syrus.AMFICOM.configuration.CORBAConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/23 08:40:15 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
class ClientPoolContext extends PoolContext {
	CMServerConnectionManager cmServerConnectionManager;

	public ClientPoolContext(final CMServerConnectionManager cmServerConnectionManager) {
		this.cmServerConnectionManager = cmServerConnectionManager;
	}

	public void init() {
		GeneralStorableObjectPool.init(new CORBAGeneralObjectLoader(this.cmServerConnectionManager),
				StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new CORBAAdministrationObjectLoader(this.cmServerConnectionManager),
				StorableObjectResizableLRUMap.class);
		ConfigurationStorableObjectPool.init(new CORBAConfigurationObjectLoader(this.cmServerConnectionManager),
				StorableObjectResizableLRUMap.class);
	}	

}
