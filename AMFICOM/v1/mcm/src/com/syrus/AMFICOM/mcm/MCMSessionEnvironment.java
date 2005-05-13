/*-
 * $Id: MCMSessionEnvironment.java,v 1.4 2005/05/13 17:57:01 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/13 17:57:01 $
 * @author $Author: bass $
 * @module mcm_v1
 */
final class MCMSessionEnvironment extends BaseSessionEnvironment {
	private static MCMSessionEnvironment instance;

	private MCMSessionEnvironment(final MCMServantManager mcmServantManager,
			final MCMPoolContext mcmPoolContext) {
		super(mcmServantManager, mcmPoolContext, new MeasurementControlModule.MCMLoginRestorer());
	}

	public MCMServantManager getMCMServantManager() {
		return (MCMServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final MCMServantManager mcmServantManager = MCMServantManager.createAndStart(serverHostName);
		instance = new MCMSessionEnvironment(mcmServantManager, new MCMPoolContext());
	}

	public static MCMSessionEnvironment getInstance() {
		return instance;
	}
}
