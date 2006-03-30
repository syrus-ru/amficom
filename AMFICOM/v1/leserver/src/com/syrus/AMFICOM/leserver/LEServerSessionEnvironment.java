/*-
 * $Id: LEServerSessionEnvironment.java,v 1.15 2006/03/30 12:11:12 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectLoader;
import com.syrus.AMFICOM.general.PoolContext;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.15 $, $Date: 2006/03/30 12:11:12 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LEServerSessionEnvironment {
	private static LEServerSessionEnvironment instance;

	private LEServerServantManager leServerServantManager;
	private LEServerPoolContext leServerPoolContext;

	private LEServerSessionEnvironment(final LEServerServantManager leServerServantManager,
			final LEServerPoolContext leServerPoolContext,
			final Identifier systemUserId) {
		this.leServerServantManager = leServerServantManager;
		this.leServerPoolContext = leServerPoolContext;

		/* Generate identifiers using local database */
		IdentifierPool.init(new DatabaseIdentifierGeneratorServer());

		LoginManager.init(new LEServerLoginPerformer(systemUserId), null);

		this.leServerPoolContext.init();
		try {
			StorableObjectPool.deserialize(this.leServerPoolContext.getLRUMapSaver());
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			StorableObjectPool.clean();
		}
	}

	public LEServerServantManager getLEServerServantManager() {
		return this.leServerServantManager;
	}

	public PoolContext getPoolContext() {
		return this.leServerPoolContext;
	}

	public static void createInstance(final String serverHostName, final Identifier systemUserId) throws CommunicationException {
		final LEServerServantManager leServerServantManager = LEServerServantManager.createAndStart(serverHostName);
		final ObjectLoader objectLoader = new LEServerObjectLoader(leServerServantManager);
		final LEServerPoolContext leServerPoolContext = new LEServerPoolContext(objectLoader);
		instance = new LEServerSessionEnvironment(leServerServantManager, leServerPoolContext, systemUserId);
	}

	public static LEServerSessionEnvironment getInstance() {
		return instance;
	}
}
