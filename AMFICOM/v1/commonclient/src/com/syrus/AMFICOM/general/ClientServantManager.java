/*
 * $Id: ClientServantManager.java,v 1.21.2.1 2006/06/27 15:45:26 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.administration.ServerWrapper.EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_EVENT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.KEY_STORABLE_OBJECT_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.LOGIN_SERVER_SERVICE_NAME;
import static com.syrus.AMFICOM.administration.ServerWrapper.STORABLE_OBJECT_SERVER_SERVICE_NAME;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.AbstractMainFrame;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.systemserver.corba.EventServer;
import com.syrus.AMFICOM.systemserver.corba.EventServerHelper;
import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServerHelper;
import com.syrus.AMFICOM.systemserver.corba.LoginServer;
import com.syrus.AMFICOM.systemserver.corba.LoginServerHelper;
import com.syrus.AMFICOM.systemserver.corba.StorableObjectServer;
import com.syrus.AMFICOM.systemserver.corba.StorableObjectServerHelper;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.21.2.1 $, $Date: 2006/06/27 15:45:26 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module commonclient
 */
public class ClientServantManager extends VerifiedConnectionManager
		implements BaseConnectionManager, StorableObjectServerConnectionManager {
	static final String KEY_SERVER_HOST_NAME = "ServerHostName";
	static final String SERVER_HOST_NAME = "localhost";

	private final String loginServerServantName;
	private final String eventServerServantName;
	private final String identifierGeneratorServerServantName;
	private final String storableObjectServerServantName;

	private Map<String, Boolean> connectionLostMap;

	public ClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String identifierGeneratorServerServantName,
			final String storableObjectServerServantName) {
		super(corbaServer, new String[] { loginServerServantName,
				eventServerServantName,
				identifierGeneratorServerServantName,
				storableObjectServerServantName });

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.identifierGeneratorServerServantName = identifierGeneratorServerServantName;
		this.storableObjectServerServantName = storableObjectServerServantName;

		this.connectionLostMap = new HashMap<String, Boolean>();
	}

	public final LoginServer getLoginServerReference() throws CommunicationException {
		return LoginServerHelper.narrow(this.getVerifiableReference(this.loginServerServantName));
	}

	public final EventServer getEventServerReference() throws CommunicationException {
		return EventServerHelper.narrow(this.getVerifiableReference(this.eventServerServantName));
	}

	public final IdentifierGeneratorServer getIdentifierGeneratorServerReference() throws CommunicationException {
		return IdentifierGeneratorServerHelper.narrow(this.getVerifiableReference(this.identifierGeneratorServerServantName));
	}

	public final StorableObjectServer getStorableObjectServerReference() throws CommunicationException {
		return StorableObjectServerHelper.narrow(this.getVerifiableReference(this.storableObjectServerServantName));
	}

	@Override
	protected final void onLoseConnection(final String servantName) {
		this.connectionLostMap.put(servantName, Boolean.valueOf(true));
		Log.debugMessage("Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		final String msg = I18N.getString("Error.ConnectionWith.ConnectionWith")
				+ " '" + servantName + "' " + I18N.getString("Error.ConnectionWith.Lost");
		final Boolean lost = this.connectionLostMap.get(servantName);
		if (lost != null && !lost.booleanValue()) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					msg,
					I18N.getString("Error.ErrorOccur"),
					JOptionPane.ERROR_MESSAGE);
		}
		this.connectionLostMap.put(servantName, Boolean.TRUE);
	}

	@Override
	protected final void onRestoreConnection(final String servantName) {
		Log.debugMessage("Connection with '" + servantName + "' restored", Log.DEBUGLEVEL08);
		final String msg = I18N.getString("Common.ClientServantManager.ConnectionWith.ConnectionWith")
				+ " '" + servantName + "' " + I18N.getString("Common.ClientServantManager.ConnectionWith.Restored");

		final Boolean lost = this.connectionLostMap.get(servantName);
		if (lost != null && lost.booleanValue()) {
			JOptionPane.showMessageDialog(AbstractMainFrame.getActiveMainFrame(),
					msg,
					I18N.getString("Common.ClientServantManager.Title"),
					JOptionPane.INFORMATION_MESSAGE);
			this.connectionLostMap.put(servantName, Boolean.valueOf(false));
		}

	}

	public static ClientServantManager create() throws CommunicationException {
		final String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(KEY_LOGIN_SERVER_SERVICE_NAME,
				LOGIN_SERVER_SERVICE_NAME);
		final String eventServerServantName = ApplicationProperties.getString(KEY_EVENT_SERVER_SERVICE_NAME,
				EVENT_SERVER_SERVICE_NAME);
		final String identifierGeneratorServerServantName = ApplicationProperties.getString(KEY_IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME,
				IDENTIFIER_GENERATOR_SERVER_SERVICE_NAME);
		final String storableObjectServerServantName = ApplicationProperties.getString(KEY_STORABLE_OBJECT_SERVER_SERVICE_NAME,
				STORABLE_OBJECT_SERVER_SERVICE_NAME);

		final ClientServantManager clientServantManager = new ClientServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				identifierGeneratorServerServantName,
				storableObjectServerServantName);
		return clientServantManager;
	}
}
