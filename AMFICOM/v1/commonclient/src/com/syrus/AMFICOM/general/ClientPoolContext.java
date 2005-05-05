/*
 * $Id: ClientPoolContext.java,v 1.1.1.1 2005/05/05 08:56:37 cvsadmin Exp $
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
 * @version $Revision: 1.1.1.1 $, $Date: 2005/05/05 08:56:37 $
 * @author $Author: cvsadmin $
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

	public void deserialize() {
		GeneralStorableObjectPool.deserializePool();
		AdministrationStorableObjectPool.deserializePool();
		ConfigurationStorableObjectPool.deserializePool();
	}

	public void serialize() {
		GeneralStorableObjectPool.serializePool();
		AdministrationStorableObjectPool.serializePool();
		ConfigurationStorableObjectPool.serializePool();
	}

}
