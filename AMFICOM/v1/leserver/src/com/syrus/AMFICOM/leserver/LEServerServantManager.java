/*-
 * $Id: LEServerServantManager.java,v 1.19 2006/06/09 16:31:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.administration.ServerProcessWrapper.EVENT_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME;

import java.util.Collections;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.EventServerConnectionManager;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.EventServerHelper;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.19 $, $Date: 2006/06/09 16:31:17 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LEServerServantManager extends RunnableVerifiedConnectionManager implements EventServerConnectionManager {
	private String eventServerServantName;

	public LEServerServantManager(final CORBAServer corbaServer, final String eventServerServantName, final long timeout) {
		super(corbaServer, Collections.singleton(eventServerServantName), timeout);

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

		final String eventServerServantName = ApplicationProperties.getString(KEY_EVENT_PROCESS_CODENAME, EVENT_PROCESS_CODENAME);

		final long timeout = ApplicationProperties.getInt(KEY_SERVANT_CHECK_TIMEOUT, SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		final LEServerServantManager leServerServantManager = new LEServerServantManager(corbaServer,
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
		return EventServerHelper.narrow(this.getVerifiableReference(this.eventServerServantName));
	}
}
