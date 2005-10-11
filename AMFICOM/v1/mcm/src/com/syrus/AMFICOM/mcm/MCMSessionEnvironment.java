/*-
 * $Id: MCMSessionEnvironment.java,v 1.9 2005/10/11 14:33:51 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginRestorer;

/**
 * @version $Revision: 1.9 $, $Date: 2005/10/11 14:33:51 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class MCMSessionEnvironment extends BaseSessionEnvironment {
	private static MCMSessionEnvironment instance;

	private MCMSessionEnvironment(final MCMServantManager mcmServantManager, final MCMPoolContext mcmPoolContext) {
		super(mcmServantManager, mcmPoolContext);
	}

	public MCMServantManager getMCMServantManager() {
		return (MCMServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName, final LoginRestorer loginRestorer) throws CommunicationException {
		final MCMServantManager mcmServantManager = MCMServantManager.createAndStart(serverHostName);

		final MCMObjectLoader objectLoader = new MCMObjectLoader(mcmServantManager, loginRestorer);
		final MCMPoolContext mcmPoolContext = new MCMPoolContext(objectLoader);

		instance = new MCMSessionEnvironment(mcmServantManager, mcmPoolContext);
	}

	public static MCMSessionEnvironment getInstance() {
		return instance;
	}
}
