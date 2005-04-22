/*
 * $Id: ClientSession.java,v 1.3 2005/04/22 21:09:13 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.AdministrationObjectLoader;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.CORBAAdministrationObjectLoader;
import com.syrus.AMFICOM.configuration.CORBAConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.measurement.CORBAMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/22 21:09:13 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public final class ClientSession {
	private static final String KEY_SERVER_HOSTNAME = "ServerHostname";

	private static final String SERVER_HOSTNAME = Application.getInternetAddress();

	private static ServantConnectionManager servantConnectionManager;

	private static AccessIdentity accessIdentity;
	private static AccessIdentifier_Transferable accessIdentityT;

	private ClientSession() {
		//singleton
	}

	public static void init() throws CommunicationException {
		initServantConnection();
		/*@todo here, before other operations, perform login*/
		initObjectPools();
	}

	private static void initServantConnection() throws CommunicationException {
		final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOSTNAME, SERVER_HOSTNAME);
		servantConnectionManager = new ServantConnectionManager(serverHostName);
	}

	//???
//	private static void initSessionContext() {
//		SessionContext.init();
//	}

	private static void initObjectPools() {
		/*	General	*/
		GeneralObjectLoader generalObjectLoader = new CORBAGeneralObjectLoader(servantConnectionManager);
		GeneralStorableObjectPool.init(generalObjectLoader, StorableObjectResizableLRUMap.class);

		/*	Administration	*/
		AdministrationObjectLoader administrationObjectLoader = new CORBAAdministrationObjectLoader(servantConnectionManager);
		AdministrationStorableObjectPool.init(administrationObjectLoader, StorableObjectResizableLRUMap.class);

		/*	Configuration	*/
		ConfigurationObjectLoader configurationObjectLoader = new CORBAConfigurationObjectLoader(servantConnectionManager);
		ConfigurationStorableObjectPool.init(configurationObjectLoader, StorableObjectResizableLRUMap.class);

		/*	Measurement	*/
		MeasurementObjectLoader measurementObjectLoader = new CORBAMeasurementObjectLoader(servantConnectionManager);
		MeasurementStorableObjectPool.init(measurementObjectLoader, StorableObjectResizableLRUMap.class);

		/*	Something more pools...	*/
	}

	public static AccessIdentifier_Transferable getAccessIdentifierTransferable() {
		if (accessIdentityT == null)
			accessIdentityT = (AccessIdentifier_Transferable) accessIdentity.getTransferable();
		return accessIdentityT;
	}
}
