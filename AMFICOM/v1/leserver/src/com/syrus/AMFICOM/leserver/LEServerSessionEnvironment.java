/*-
 * $Id: LEServerSessionEnvironment.java,v 1.3 2005/05/13 17:55:50 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.PoolContext;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/13 17:55:50 $
 * @author $Author: bass $
 * @module leserver_v1
 */
final class LEServerSessionEnvironment {
	private static LEServerSessionEnvironment instance;

	private LEServerServantManager leServerServantManager;
	private LEServerPoolContext leServerPoolContext;

	private LEServerSessionEnvironment(
			final LEServerServantManager leServerServantManager,
			final LEServerPoolContext leServerPoolContext) {
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

	public PoolContext getPoolContext() {
		return this.leServerPoolContext;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final LEServerServantManager leServerServantManager = LEServerServantManager.createAndStart(serverHostName);
		instance = new LEServerSessionEnvironment(leServerServantManager, new LEServerPoolContext());
	}

	public static LEServerSessionEnvironment getInstance() {
		return instance;
	}
}
