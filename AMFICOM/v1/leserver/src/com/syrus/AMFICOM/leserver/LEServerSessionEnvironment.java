/*-
 * $Id: LEServerSessionEnvironment.java,v 1.11 2005/10/30 15:20:46 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.11 $, $Date: 2005/10/30 15:20:46 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LEServerSessionEnvironment {
	private static LEServerSessionEnvironment instance;

	private LEServerServantManager leServerServantManager;
	private LEServerPoolContext leServerPoolContext;

	private LEServerSessionEnvironment(final LEServerServantManager leServerServantManager,
			final LEServerPoolContext leServerPoolContext) {
		this.leServerServantManager = leServerServantManager;
		this.leServerPoolContext = leServerPoolContext;

		/* Generate identifiers using local database */
		IdentifierPool.init(new DatabaseIdentifierGeneratorServer());
		this.leServerPoolContext.init();
		try {
			StorableObjectPool.deserialize(this.leServerPoolContext.getLRUSaver());
		} catch (ApplicationException ae) {
			assert Log.errorMessage(ae);
			StorableObjectPool.clean();
		}
	}

	public LEServerServantManager getLEServerServantManager() {
		return this.leServerServantManager;
	}

	public PoolContext getPoolContext() {
		return this.leServerPoolContext;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final LEServerServantManager leServerServantManager = LEServerServantManager.createAndStart(serverHostName);
		final ObjectLoader objectLoader = new DatabaseObjectLoader();
		final LEServerPoolContext leServerPoolContext = new LEServerPoolContext(objectLoader);
		instance = new LEServerSessionEnvironment(leServerServantManager, leServerPoolContext);
	}

	public static LEServerSessionEnvironment getInstance() {
		return instance;
	}
}
