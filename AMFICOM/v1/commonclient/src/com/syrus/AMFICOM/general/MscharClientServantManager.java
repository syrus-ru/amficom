/*-
 * $Id: MscharClientServantManager.java,v 1.1 2005/06/07 17:58:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1 $, $Date: 2005/06/07 17:58:14 $
 * @author $Author: bass $
 * @module commonclient_v1
 */
public final class MscharClientServantManager extends ClientServantManager {
	public MscharClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String mscharServerServantName) {
		super(corbaServer, loginServerServantName, eventServerServantName, mscharServerServantName);
	}

	public static MscharClientServantManager create() throws CommunicationException {
		final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		final String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		final String mscharServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSCHAR_SERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSCHAR_SERVER_PROCESS_CODENAME);

		final MscharClientServantManager mscharClientServantManager = new MscharClientServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				mscharServerServantName);
		return mscharClientServantManager;
	}
}
