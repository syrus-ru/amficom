/*
 * $Id: ClientSessionEnvironment.java,v 1.4 2005/06/03 09:01:44 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/03 09:01:44 $
 * @author $Author: arseniy $
 * @module generalclient_v1
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {
	//TODO enum SessionKind
	public static final int SESSION_KIND_MEASUREMENT = 1;
	public static final int SESSION_KIND_MAPSCHEME = 2;
	//Other kinds -- Map, Scheme, Resources, etc.

	private static ClientSessionEnvironment instance;

	private ClientSessionEnvironment(ClientServantManager clientServantManager,
			ClientPoolContext clientPoolContext,
			LoginRestorer loginRestorer) {
		super(clientServantManager, clientPoolContext, loginRestorer);
	}

	public ClientServantManager getClientServantManager() {
		return (ClientServantManager) super.baseConnectionManager;
	}

	public static void createInstance(int sessionKind, LoginRestorer loginRestorer) throws CommunicationException {
		switch (sessionKind) {
			case SESSION_KIND_MEASUREMENT:
				createMeasurementSession(loginRestorer);
				break;
			case SESSION_KIND_MAPSCHEME:
				createMapSchemeSession(loginRestorer);
				break;
			default:
				Log.errorMessage("Unknown kind of session -- " + sessionKind);
		}

		/*	Print available objects -- for debugging purpose*/
		if (instance != null)
			instance.baseConnectionManager.getCORBAServer().printNamingContext();
	}

	private static void createMeasurementSession(LoginRestorer loginRestorer) throws CommunicationException {
		MClientServantManager mClientServantManager = MClientServantManager.create();
		ClientPoolContext clientPoolContext = new MClientPoolContext(mClientServantManager);
		instance = new ClientSessionEnvironment(mClientServantManager, clientPoolContext, loginRestorer);
	}

	private static void createMapSchemeSession(LoginRestorer loginRestorer) throws CommunicationException {
		MSHClientServantManager mshClientServantManager = MSHClientServantManager.create();
		ClientPoolContext clientPoolContext = new MSHClientPoolContext(mshClientServantManager);
		instance = new ClientSessionEnvironment(mshClientServantManager, clientPoolContext, loginRestorer);
	}

	public static ClientSessionEnvironment getInstance() {
		return instance;
	}
}
