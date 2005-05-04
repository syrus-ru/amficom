/*
 * $Id: ClientSessionEnvironment.java,v 1.1 2005/05/04 14:43:53 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/04 14:43:53 $
 * @author $Author: arseniy $
 * @module generalclient_v1
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {
	public static final int SESSION_KIND_MEASUREMENT = 1;

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
			default:
				Log.errorMessage("Unknown kind of session -- " + sessionKind);
		}
	}

	private static void createMeasurementSession(LoginRestorer loginRestorer) throws CommunicationException {
		MClientServantManager clientServantManager = MClientServantManager.create();
		ClientPoolContext clientPoolContext = new MClientPoolContext(clientServantManager);
		instance = new ClientSessionEnvironment(clientServantManager, clientPoolContext, loginRestorer);
	}

	public static ClientSessionEnvironment getInstance() {
		return instance;
	}
}
