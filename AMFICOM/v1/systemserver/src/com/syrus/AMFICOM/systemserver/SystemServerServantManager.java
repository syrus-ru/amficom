/*-
 * $Id: SystemServerServantManager.java,v 1.1 2006/06/26 10:49:41 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import static com.syrus.AMFICOM.administration.ServerWrapper.EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;

import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.VerifiedConnectionManager;
import com.syrus.AMFICOM.systemserver.corba.EventServer;
import com.syrus.AMFICOM.systemserver.corba.EventServerHelper;
import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServerHelper;
import com.syrus.AMFICOM.systemserver.corba.LoginServer;
import com.syrus.AMFICOM.systemserver.corba.LoginServerHelper;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1 $, $Date: 2006/06/26 10:49:41 $
 * @author $Author: cvsadmin $
 * @author Tashoyan Arseniy Feliksovich
 * @module systemserver
 */
final class SystemServerServantManager extends VerifiedConnectionManager implements BaseConnectionManager {
	private final String loginServerServantName;
	private final String eventServerServantName;
	private final String identifierGeneratorServerServantName;

	private static SystemServerServantManager instance;

	private SystemServerServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String identifierGeneratorServerServantName) {
		super(corbaServer, new String[] { loginServerServantName, eventServerServantName, identifierGeneratorServerServantName });

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.identifierGeneratorServerServantName = identifierGeneratorServerServantName;
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		return LoginServerHelper.narrow(this.getVerifiableReference(this.loginServerServantName));
	}

	public EventServer getEventServerReference() throws CommunicationException {
		return EventServerHelper.narrow(this.getVerifiableReference(this.eventServerServantName));
	}

	public IdentifierGeneratorServer getIdentifierGeneratorServerReference() throws CommunicationException {
		return IdentifierGeneratorServerHelper.narrow(this.getVerifiableReference(this.identifierGeneratorServerServantName));
	}

	public static SystemServerServantManager getInstance() {
		if (instance != null) {
			return instance;
		}
		throw new IllegalStateException(OBJECT_NOT_INITIALIZED);
	}

	public static void createInstance(final String serverHostName) throws CommunicationException {
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(KEY_LOGIN_SERVER_SERVICE_NAME,
				LOGIN_SERVER_SERVICE_NAME);
		final String eventServerServantName = ApplicationProperties.getString(KEY_EVENT_SERVER_SERVICE_NAME,
				EVENT_SERVER_SERVICE_NAME);
		final String identifierGeneratorServerServantName = ApplicationProperties.getString(KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME,
				IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME);

		instance = new SystemServerServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				identifierGeneratorServerServantName);
	}
}
