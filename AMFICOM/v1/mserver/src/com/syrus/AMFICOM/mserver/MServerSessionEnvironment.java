/*-
 * $Id: MServerSessionEnvironment.java,v 1.10 2005/11/28 12:35:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerPOA;
import com.syrus.AMFICOM.mserver.corba.MServerPOATie;

/**
 * @version $Revision: 1.10 $, $Date: 2005/11/28 12:35:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerSessionEnvironment extends BaseSessionEnvironment {
	private static MServerSessionEnvironment instance;

	private MServerSessionEnvironment(final MServerServantManager mServerServantManager,
			final MServerPoolContext mServerPoolContext,
			final MServer mServer) {
		super(mServerServantManager, mServerPoolContext, mServer, new MeasurementServer.MServerLoginRestorer());
	}

	public MServerServantManager getMServerServantManager() {
		return (MServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName, final Set<Identifier> mcmIds, final String servantName)
			throws ApplicationException {
		final MServerServantManager mServerServantManager = MServerServantManager.createAndStart(serverHostName, mcmIds);

		final MServerObjectLoader mServerObjectLoader = new MServerObjectLoader();
		final MServerPoolContext mServerPoolContext = new MServerPoolContext(mServerObjectLoader);

		final CORBAServer corbaServer = mServerServantManager.getCORBAServer();
		final MServerPOA servant = new MServerPOATie(new MServerImpl(mServerServantManager), corbaServer.getPoa());
		corbaServer.activateServant(servant, servantName);
		corbaServer.printNamingContext();

		instance = new MServerSessionEnvironment(mServerServantManager, mServerPoolContext, servant._this(corbaServer.getOrb()));
	}

	public static MServerSessionEnvironment getInstance() {
		return instance;
	}
}
