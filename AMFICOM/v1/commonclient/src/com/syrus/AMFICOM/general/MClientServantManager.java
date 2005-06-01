/*-
 * $Id: MClientServantManager.java,v 1.5 2005/06/01 16:55:08 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/01 16:55:08 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
public final class MClientServantManager extends ClientServantManager {
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
