/*
 * $Id: MClientServantManager.java,v 1.3 2005/05/05 12:33:16 bob Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/05 12:33:16 $
 * @author $Author: bob $
 * @module generalclient_v1
 */
public final class MClientServantManager extends VerifiedConnectionManager implements ClientServantManager, CMServerConnectionManager {
	private String loginServerServantName;
	private String eventServerServantName;
	private String cmServerServantName;

	public MClientServantManager(CORBAServer corbaServer,
			String loginServerServantName,
			String eventServerServantName,
			String cmServerServantName) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, cmServerServantName});

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.cmServerServantName = cmServerServantName;
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		try {
			return (LoginServer) super.getVerifiableReference(this.loginServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public EventServer getEventServerReference() throws CommunicationException {
		try {
			return (EventServer) super.getVerifiableReference(this.eventServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		try {
			return (IdentifierGeneratorServer) super.getVerifiableReference(this.cmServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public CMServer getCMServerReference() throws CommunicationException {
		try {
			return (CMServer) super.getVerifiableReference(this.cmServerServantName);
		}
		catch (IllegalDataException ide) {
			// Never
			assert false;
			return null;
		}
	}

	protected void onLoseConnection(String servantName) {
		Log.debugMessage("CMServerConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		// @todo Maybe, GUI-specific actions
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("CMServerConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		// @todo Maybe, GUI-specific actions
	}

	public static MClientServantManager create() throws CommunicationException {
		String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		String contextName = ContextNameFactory.generateContextName(serverHostName);
		CORBAServer corbaServer = new CORBAServer(contextName);

		String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		String cmServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_CMSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);

		MClientServantManager mClientServantManager = new MClientServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				cmServerServantName);
		return mClientServantManager;
	}
}
