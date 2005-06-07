/*
 * $Id: ClientPoolContext.java,v 1.6 2005/06/07 13:28:24 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.File;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.CORBAAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.XMLAdministrationObjectLoader;
import com.syrus.AMFICOM.configuration.CORBAConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.XMLConfigurationObjectLoader;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/07 13:28:24 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
class ClientPoolContext extends PoolContext {

	ServerConnectionManager	clientServantManager;

	protected File xmlFile;

	public ClientPoolContext(final ServerConnectionManager clientServantManager) {
		this.clientServantManager = clientServantManager;
	}
	
	public ClientPoolContext(final String xmlpath) {
		this.xmlFile = new File(xmlpath);
		
	}

	public void init() {
		if (this.xmlFile != null) {
			GeneralStorableObjectPool.init(new XMLGeneralObjectLoader(this.xmlFile), 
				StorableObjectResizableLRUMap.class);
			AdministrationStorableObjectPool.init(new XMLAdministrationObjectLoader(this.xmlFile), 
				StorableObjectResizableLRUMap.class);
			ConfigurationStorableObjectPool.init(new XMLConfigurationObjectLoader(this.xmlFile),
				StorableObjectResizableLRUMap.class);
		} else {
			GeneralStorableObjectPool.init(new CORBAGeneralObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
			AdministrationStorableObjectPool.init(new CORBAAdministrationObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
			ConfigurationStorableObjectPool.init(new CORBAConfigurationObjectLoader(this.clientServantManager),
				StorableObjectResizableLRUMap.class);
		}
	}

}
