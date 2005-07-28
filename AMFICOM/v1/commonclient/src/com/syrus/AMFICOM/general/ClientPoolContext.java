/*
 * $Id: ClientPoolContext.java,v 1.12 2005/07/28 19:45:42 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.io.File;

import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/28 19:45:42 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
class ClientPoolContext extends PoolContext {

	private static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	private static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	private static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";

	private static final int GENERAL_POOL_SIZE = 1000;
	private static final int ADMINISTRATION_POOL_SIZE = 1000;
	private static final int CONFIGURATION_POOL_SIZE = 1000;

	ServerConnectionManager	clientServantManager;

	protected File xmlFile;

	public ClientPoolContext(final ServerConnectionManager clientServantManager) {
		this.clientServantManager = clientServantManager;
	}

	public ClientPoolContext(final String xmlpath) {
		this.xmlFile = new File(xmlpath);
	}

	@Override
	public void init() {
		ObjectLoader objectLoader;
		if (this.xmlFile == null) {
			objectLoader = new CORBAObjectLoader(this.clientServantManager);
		} else {
			objectLoader = new XMLObjectLoader(this.xmlFile);
		}

		final Class lruMapClass = StorableObjectResizableLRUMap.class;

		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, CONFIGURATION_POOL_SIZE);

		StorableObjectPool.init(objectLoader, lruMapClass);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.GENERAL_GROUP_CODE, generalPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.ADMINISTRATION_GROUP_CODE, administrationPoolSize);
		StorableObjectPool.addObjectPoolGroup(ObjectGroupEntities.CONFIGURATION_GROUP_CODE, configurationPoolSize);
	}

}
