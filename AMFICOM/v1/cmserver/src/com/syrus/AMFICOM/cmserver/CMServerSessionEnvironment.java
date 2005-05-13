/*-
 * $Id: CMServerSessionEnvironment.java,v 1.3 2005/05/13 17:49:03 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/13 17:49:03 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
final class CMServerSessionEnvironment extends BaseSessionEnvironment {
	private static CMServerSessionEnvironment instance;

	private CMServerSessionEnvironment(
			final CMServerServantManager cmServerServantManager,
			final CMServerPoolContext cmServerPoolContext) {
		super(cmServerServantManager, cmServerPoolContext, new ClientMeasurementServer.CMServerLoginRestorer());
	}

	public CMServerServantManager getCMServerServantManager() {
		return (CMServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final CMServerServantManager cmServerServantManager = CMServerServantManager.createAndStart(serverHostName);
		instance = new CMServerSessionEnvironment(cmServerServantManager, new CMServerPoolContext());
	}

	public static CMServerSessionEnvironment getInstance() {
		return instance;
	}

}
