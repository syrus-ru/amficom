/*-
 * $Id: ClientSessionEnvironment.java,v 1.5 2005/05/18 14:01:20 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/18 14:01:20 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public final class ClientSessionEnvironment extends BaseSessionEnvironment {
	public static final int SESSION_KIND_MEASUREMENT = 1;

	private static ClientSessionEnvironment instance;

	private ClientSessionEnvironment(
			final ClientServantManager clientServantManager,
			final ClientPoolContext clientPoolContext,
			final LoginRestorer loginRestorer) {
		super(clientServantManager, clientPoolContext, loginRestorer);
	}

	public ClientServantManager getClientServantManager() {
		return (ClientServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final int sessionKind,
			final LoginRestorer loginRestorer)
			throws CommunicationException {
		switch (sessionKind) {
			case SESSION_KIND_MEASUREMENT:
				createMeasurementSession(loginRestorer);
				break;
			default:
				Log.errorMessage("Unknown kind of session -- " + sessionKind);
		}

		/*	Print available objects -- for debugging purpose*/
		if (instance != null)
			instance.baseConnectionManager.getCORBAServer().printNamingContext();
	}

	private static void createMeasurementSession(
			final LoginRestorer loginRestorer)
			throws CommunicationException {
		final MClientServantManager clientServantManager = MClientServantManager.create();
		final ClientPoolContext clientPoolContext = new MClientPoolContext(clientServantManager);
		instance = new ClientSessionEnvironment(clientServantManager, clientPoolContext, loginRestorer);
	}

	public static ClientSessionEnvironment getInstance() {
		return instance;
	}
}
