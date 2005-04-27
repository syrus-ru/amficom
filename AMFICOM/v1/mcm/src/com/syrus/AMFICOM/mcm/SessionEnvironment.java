/*
 * $Id: SessionEnvironment.java,v 1.1 2005/04/27 15:08:29 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 15:08:29 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
public final class SessionEnvironment {
	private static MCMServantManager mcmServantManager;

	private SessionEnvironment() {
		// singleton
		assert false;
	}

	public static void init(String serverHostName) throws CommunicationException {
		mcmServantManager = MCMServantManager.createAndStart(serverHostName);

		LoginManager.init(mcmServantManager);
		IdentifierPool.init(mcmServantManager);
		PoolContext.init();
	}

	public static void login(String login, String password) throws CommunicationException, LoginException {
		LoginManager.login(login, password);
		PoolContext.deserialize();
	}

	public static void logout() throws CommunicationException, LoginException {
		LoginManager.logout();
		PoolContext.serialize();
	}

	public static MCMServantManager getMCMServantManager() {
		return mcmServantManager;
	}
}
