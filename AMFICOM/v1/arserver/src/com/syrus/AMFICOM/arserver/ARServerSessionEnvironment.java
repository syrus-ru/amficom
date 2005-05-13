/*-
 * $Id: ARServerSessionEnvironment.java,v 1.1 2005/05/13 17:41:55 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.general.BaseSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/13 17:41:55 $
 * @module arserver_v1
 */
final class ARServerSessionEnvironment extends BaseSessionEnvironment {
	private static ARServerSessionEnvironment instance;

	public ARServerSessionEnvironment(
			final ARServerServantManager arServerServantManager,
			final ARServerPoolContext arServerPoolContext) {
		super(arServerServantManager, arServerPoolContext, new AdministrationResourceServer.ARServerLoginRestorer());
	}

	public ARServerServantManager getARServerServantManager() {
		return (ARServerServantManager) super.baseConnectionManager;
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		instance = new ARServerSessionEnvironment(
				ARServerServantManager.createAndStart(serverHostName),
				new ARServerPoolContext());
	}

	public static ARServerSessionEnvironment getInstance() {
		return instance;
	}
}
