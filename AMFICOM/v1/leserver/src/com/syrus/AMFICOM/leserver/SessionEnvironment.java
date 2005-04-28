/*
 * $Id: SessionEnvironment.java,v 1.1 2005/04/28 07:35:33 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.loginserver;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.IdentifierPool;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/28 07:35:33 $
 * @author $Author: cvsadmin $
 * @module loginserver_v1
 */
public final class SessionEnvironment {
	private static LoginServerServantManager loginServerServantManager;

	private SessionEnvironment() {
		// singleton
		assert false;
	}

	public static void init(String serverHostName) throws CommunicationException {
		loginServerServantManager = LoginServerServantManager.createAndStart(serverHostName);

		/*	Generate identifiers using local database*/
		IdentifierPool.init(new DatabaseIdentifierGeneratorServer());
		PoolContext.init();
		PoolContext.deserialize();
	}

	public static LoginServerServantManager getLoginServerServantManager() {
		return loginServerServantManager;
	}
}
