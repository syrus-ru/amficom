/*
 * $Id: ClientPoolContext.java,v 1.5 2005/06/03 09:02:53 arseniy Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/06/03 09:02:53 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
class ClientPoolContext extends PoolContext {
	ClientServantManager clientServantManager;

	public ClientPoolContext(final ClientServantManager clientServantManager) {
		this.clientServantManager = clientServantManager;
	}

	public void init() {
		GeneralStorableObjectPool.init(new CORBAGeneralObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
		AdministrationStorableObjectPool.init(new CORBAAdministrationObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
		ConfigurationStorableObjectPool.init(new CORBAConfigurationObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
	}	

}
