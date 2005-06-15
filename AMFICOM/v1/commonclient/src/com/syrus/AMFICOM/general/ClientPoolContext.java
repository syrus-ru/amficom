/*
 * $Id: ClientPoolContext.java,v 1.10 2005/06/15 15:52:22 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.File;

import com.syrus.AMFICOM.administration.AdministrationObjectLoader;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.CORBAAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.XMLAdministrationObjectLoader;
import com.syrus.AMFICOM.configuration.CORBAConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.XMLConfigurationObjectLoader;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.10 $, $Date: 2005/06/15 15:52:22 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
class ClientPoolContext extends PoolContext {

	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	
	ServerConnectionManager	clientServantManager;

	protected File xmlFile;

	public ClientPoolContext(final ServerConnectionManager clientServantManager) {
		this.clientServantManager = clientServantManager;
	}

	public ClientPoolContext(final String xmlpath) {
		this.xmlFile = new File(xmlpath);
	}

	public void init() {
		GeneralObjectLoader generalObjectLoader;
		AdministrationObjectLoader administrationObjectLoader;
		ConfigurationObjectLoader configurationObjectLoader;
		if (this.xmlFile == null) {
			generalObjectLoader = new CORBAGeneralObjectLoader(this.clientServantManager);
			administrationObjectLoader = new CORBAAdministrationObjectLoader(this.clientServantManager);
			configurationObjectLoader = new CORBAConfigurationObjectLoader(this.clientServantManager);
		}
		else {
			generalObjectLoader = new XMLGeneralObjectLoader(this.xmlFile);
			administrationObjectLoader = new XMLAdministrationObjectLoader(this.xmlFile);
			configurationObjectLoader = new XMLConfigurationObjectLoader(this.xmlFile);
		}

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, -1);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, -1);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, -1);

		GeneralStorableObjectPool.init(generalObjectLoader, lruMapClass, generalPoolSize);
		AdministrationStorableObjectPool.init(administrationObjectLoader, lruMapClass, administrationPoolSize);
		ConfigurationStorableObjectPool.init(configurationObjectLoader, lruMapClass, configurationPoolSize);
	}

}
