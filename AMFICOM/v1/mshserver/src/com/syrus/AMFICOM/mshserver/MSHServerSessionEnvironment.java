/*-
 * $Id: MSHServerSessionEnvironment.java,v 1.1 2005/05/13 17:47:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/13 17:47:53 $
 * @module mshserver_v1
 */
final class MSHServerSessionEnvironment extends BaseSessionEnvironment {
	private static MSHServerSessionEnvironment instance;

	private MSHServerSessionEnvironment(
			final MSHServerServantManager mshServerServantManager,
			final MSHServerPoolContext mshServerPoolContext) {
		super(mshServerServantManager, mshServerPoolContext, new MapSchemeServer.MSHServerLoginRestorer());
	}

	public MSHServerServantManager getMSHServerServantManager() {
		return (MSHServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		instance = new MSHServerSessionEnvironment(
				MSHServerServantManager.createAndStart(serverHostName),
				new MSHServerPoolContext());
	}

	public static MSHServerSessionEnvironment getInstance() {
		return instance;
	}
}
