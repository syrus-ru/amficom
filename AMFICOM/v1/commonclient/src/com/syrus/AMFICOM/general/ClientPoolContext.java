/*
 * $Id: ClientPoolContext.java,v 1.4 2005/06/01 16:55:11 arseniy Exp $
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
 * @version $Revision: 1.4 $, $Date: 2005/06/01 16:55:11 $
 * @author $Author: arseniy $
 * @module generalclient_v1
 */
class ClientPoolContext extends PoolContext {
	ServerConnectionManager serverConnectionManager;

	public ClientPoolContext(final ServerConnectionManager cmServerConnectionManager) {
		this.serverConnectionManager = cmServerConnectionManager;
	}

	public void init() {
		GeneralStorableObjectPool.init(new CORBAGeneralObjectLoader(this.serverConnectionManager),
				StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new CORBAAdministrationObjectLoader(this.serverConnectionManager),
				StorableObjectResizableLRUMap.class);
		ConfigurationStorableObjectPool.init(new CORBAConfigurationObjectLoader(this.serverConnectionManager),
				StorableObjectResizableLRUMap.class);
	}	

}
