/*-
 * $Id: ARClientServantManager.java,v 1.1 2005/05/30 15:13:02 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.util.ApplicationProperties;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/30 15:13:02 $
 * @module commonclient_v1
 */
public final class ARClientServantManager extends AbstractClientServantManager {
	public ARClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String arServerServantName) {
		super(corbaServer, loginServerServantName, eventServerServantName, arServerServantName);
	}

	public static ARClientServantManager create() throws CommunicationException {
		final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		final String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		final String arServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_ARSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.ARSERVER_PROCESS_CODENAME);

		final ARClientServantManager arClientServantManager = new ARClientServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				arServerServantName);
		return arClientServantManager;
	}
}
