/*-
 * $Id: MServerSessionEnvironment.java,v 1.12 2006/05/30 11:44:00 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.AbstractSessionEnvironment;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerPOA;
import com.syrus.AMFICOM.mserver.corba.MServerPOATie;

/**
 * @version $Revision: 1.12 $, $Date: 2006/05/30 11:44:00 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerSessionEnvironment extends AbstractSessionEnvironment<MServerServantManager> {
	private static MServerSessionEnvironment instance;

	private MServerSessionEnvironment(final MServerServantManager mServerServantManager,
			final MServerPoolContext mServerPoolContext,
			final MServer mServer) {
		super(mServerServantManager, mServerPoolContext, mServer, new MeasurementServer.MServerLoginRestorer());
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
