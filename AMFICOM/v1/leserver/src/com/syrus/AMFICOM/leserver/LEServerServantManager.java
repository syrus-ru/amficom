/*-
 * $Id: LEServerServantManager.java,v 1.13 2005/10/19 07:51:21 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import java.util.HashSet;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.EventServerConnectionManager;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.EventServerHelper;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/19 07:51:21 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LEServerServantManager extends RunnableVerifiedConnectionManager
		implements EventServerConnectionManager {
	private String eventServerServantName;

	public LEServerServantManager(final CORBAServer corbaServer,
			final String eventServerServantName,
			final long timeout) {
		super(corbaServer, new HashSet<String>(), timeout);

		this.eventServerServantName = eventServerServantName;

		assert timeout >= 10L * 60L * 1000L: ErrorMessages.TIMEOUT_TOO_SHORT; //not less then 10 min
	}

	@Override
	protected void onLoseConnection(final String servantName) {
		// TODO Remove session of client
		super.onLoseConnection(servantName);
	}

	public static LEServerServantManager createAndStart(final String serverHostName) throws CommunicationException {
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final long timeout = ApplicationProperties.getInt(BaseConnectionManager.KEY_SERVANT_CHECK_TIMEOUT,
				BaseConnectionManager.SERVANT_CHECK_TIMEOUT) * 60 * 1000;
		final String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);

		final LEServerServantManager leServerServantManager =  new LEServerServantManager(
				corbaServer,
				eventServerServantName,
				timeout);
		(new Thread(leServerServantManager, "LEServerServantManager")).start();
		return leServerServantManager;
	}

	/**
	 * @throws CommunicationException
	 * @see EventServerConnectionManager#getEventServerReference()
	 */
	public EventServer getEventServerReference() throws CommunicationException {
		try {
			return EventServerHelper.narrow(this.getVerifiableReference(this.eventServerServantName));
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}
}
