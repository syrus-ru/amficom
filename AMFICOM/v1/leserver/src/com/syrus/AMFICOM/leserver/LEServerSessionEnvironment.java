/*
 * $Id: LEServerSessionEnvironment.java,v 1.1 2005/04/29 12:14:03 arseniy Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/04/29 12:14:03 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LEServerSessionEnvironment {
	private static LEServerSessionEnvironment instance;

	private LEServerServantManager leServerServantManager;
	private LEServerPoolContext leServerPoolContext;

	private LEServerSessionEnvironment(LEServerServantManager leServerServantManager, LEServerPoolContext leServerPoolContext) {
		this.leServerServantManager = leServerServantManager;
		this.leServerPoolContext = leServerPoolContext;

		/*	Generate identifiers using local database*/
		IdentifierPool.init(new DatabaseIdentifierGeneratorServer());
		this.leServerPoolContext.init();
		this.leServerPoolContext.deserialize();
	}

	public LEServerServantManager getLEServerServantManager() {
		return this.leServerServantManager;
	}

	public static void create(String serverHostName) throws CommunicationException {
		LEServerServantManager leServerServantManager = LEServerServantManager.createAndStart(serverHostName);
		instance = new LEServerSessionEnvironment(leServerServantManager, new LEServerPoolContext());
	}

	public static LEServerSessionEnvironment getInstance() {
		return instance;
	}
}
