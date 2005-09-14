/*-
 * $Id: MCMSessionEnvironment.java,v 1.8 2005/09/14 18:13:47 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.8 $, $Date: 2005/09/14 18:13:47 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */
final class MCMSessionEnvironment extends BaseSessionEnvironment {
	private static MCMSessionEnvironment instance;

	private MCMSessionEnvironment(final MCMServantManager mcmServantManager, final MCMPoolContext mcmPoolContext) {
		super(mcmServantManager, mcmPoolContext, new MeasurementControlModule.MCMLoginRestorer());
	}

	public MCMServantManager getMCMServantManager() {
		return (MCMServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final MCMServantManager mcmServantManager = MCMServantManager.createAndStart(serverHostName);
		instance = new MCMSessionEnvironment(mcmServantManager, new MCMPoolContext(mcmServantManager));
	}

	public static MCMSessionEnvironment getInstance() {
		return instance;
	}
}
