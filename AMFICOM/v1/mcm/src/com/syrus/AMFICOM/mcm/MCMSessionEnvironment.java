/*-
 * $Id: MCMSessionEnvironment.java,v 1.13 2006/05/30 11:44:01 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.AbstractSessionEnvironment;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.AMFICOM.mcm.corba.MCMPOA;
import com.syrus.AMFICOM.mcm.corba.MCMPOATie;

/**
 * @version $Revision: 1.13 $, $Date: 2006/05/30 11:44:01 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class MCMSessionEnvironment extends AbstractSessionEnvironment<MCMServantManager> {
	private static MCMSessionEnvironment instance;

	private MCMSessionEnvironment(final MCMServantManager mcmServantManager, final MCMPoolContext mcmPoolContext, final MCM mcm) {
		super(mcmServantManager, mcmPoolContext, mcm, new MeasurementControlModule.MCMLoginRestorer());
	}

	public static void createInstance(final String serverHostName, final String servantName) throws CommunicationException {
		final MCMServantManager mcmServantManager = MCMServantManager.createAndStart(serverHostName);

		final MCMObjectLoader objectLoader = new MCMObjectLoader(mcmServantManager);

		final MCMPoolContext mcmPoolContext = new MCMPoolContext(objectLoader);

		final CORBAServer corbaServer = mcmServantManager.getCORBAServer();
		final MCMPOA servant = new MCMPOATie(new MCMImpl(mcmServantManager), corbaServer.getPoa());
		corbaServer.activateServant(servant, servantName);
		corbaServer.printNamingContext();

		instance = new MCMSessionEnvironment(mcmServantManager, mcmPoolContext, servant._this(corbaServer.getOrb()));
	}

	public static MCMSessionEnvironment getInstance() {
		return instance;
	}
}
