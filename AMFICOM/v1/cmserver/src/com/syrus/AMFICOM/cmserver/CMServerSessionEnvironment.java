/*-
 * $Id: CMServerSessionEnvironment.java,v 1.6 2005/08/08 11:44:39 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.6 $, $Date: 2005/08/08 11:44:39 $
 * @author $Author: arseniy $
 * @module cmserver
 */
final class CMServerSessionEnvironment extends BaseSessionEnvironment {
	private static CMServerSessionEnvironment instance;

	private CMServerSessionEnvironment(final CMServerServantManager cmServerServantManager,
			final CMServerPoolContext cmServerPoolContext) {
		super(cmServerServantManager, cmServerPoolContext, new ClientMeasurementServer.CMServerLoginRestorer());
	}

	public CMServerServantManager getCMServerServantManager() {
		return (CMServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final CMServerServantManager cmServerServantManager = CMServerServantManager.createAndStart(serverHostName);
		instance = new CMServerSessionEnvironment(cmServerServantManager, new CMServerPoolContext(cmServerServantManager));
	}

	public static CMServerSessionEnvironment getInstance() {
		return instance;
	}

}
