/*-
 * $Id: MClientServantManager.java,v 1.6 2005/06/07 13:28:24 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/07 13:28:24 $
 * @author $Author: bob $
 * @module commonclient_v1
 */
public final class MClientServantManager extends ClientServantManager implements BaseConnectionManager {
	public MClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String cmServerServantName) {
		super(corbaServer, loginServerServantName, eventServerServantName, cmServerServantName);
	}

	public static MClientServantManager create() throws CommunicationException {
		final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		final String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		final String cmServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_CMSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);

		final MClientServantManager mClientServantManager = new MClientServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				cmServerServantName);
		return mClientServantManager;
	}
}
