/*-
 * $Id: LEServerServantManager.java,v 1.7 2005/06/07 16:37:40 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import java.util.HashSet;

import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/07 16:37:40 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LEServerServantManager extends RunnableVerifiedConnectionManager {

	public LEServerServantManager(final CORBAServer corbaServer, final long timeout) {
		super(corbaServer, new HashSet(), timeout);

		assert timeout >= 10L * 60L * 1000L: ErrorMessages.TIMEOUT_TOO_SHORT; //not less then 10 min
	}

	protected void onLoseConnection(final String servantName) {
		// TODO Remove session of client
		super.onLoseConnection(servantName);
	}

	public static LEServerServantManager createAndStart(final String serverHostName) throws CommunicationException {
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final long timeout = ApplicationProperties.getInt(BaseConnectionManager.KEY_SERVANT_CHECK_TIMEOUT,
				BaseConnectionManager.SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		final LEServerServantManager leServerServantManager =  new LEServerServantManager(corbaServer, timeout);
		(new Thread(leServerServantManager)).start();
		return leServerServantManager;
	}

}
