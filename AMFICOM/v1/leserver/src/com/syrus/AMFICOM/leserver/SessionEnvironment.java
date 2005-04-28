/*
 * $Id: SessionEnvironment.java,v 1.2 2005/04/28 10:35:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.IdentifierPool;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/28 10:35:21 $
 * @author $Author: arseniy $
 * @module loginserver_v1
 */
public final class SessionEnvironment {
	private static LEServerServantManager leServerServantManager;

	private SessionEnvironment() {
		// singleton
		assert false;
	}

	public static void init(String serverHostName) throws CommunicationException {
		leServerServantManager = LEServerServantManager.createAndStart(serverHostName);

		/*	Generate identifiers using local database*/
		IdentifierPool.init(new DatabaseIdentifierGeneratorServer());

		PoolContext.init();
		PoolContext.deserialize();
	}

	public static LEServerServantManager getLEServerServantManager() {
		return leServerServantManager;
	}
}
