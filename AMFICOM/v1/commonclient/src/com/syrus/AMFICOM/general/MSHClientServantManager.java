/*
 * $Id: MSHClientServantManager.java,v 1.2 2005/05/23 08:19:13 max Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/23 08:19:13 $
 * @author $Author: max $
 * @module commonclient_v1
 */
public final class MSHClientServantManager extends VerifiedConnectionManager implements ClientServantManager, MSHServerConnectionManager {
	private String loginServerServantName;
	private String eventServerServantName;
	private String mshServerServantName;

	public MSHClientServantManager(CORBAServer corbaServer,
			String loginServerServantName,
			String eventServerServantName,
			String mshServerServantName) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, mshServerServantName});

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.mshServerServantName = mshServerServantName;
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
			return (IdentifierGeneratorServer) super.getVerifiableReference(this.mshServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}
	
	public MSHServer getMSHServerReference() throws CommunicationException {
		try {
			return (MSHServer) super.getVerifiableReference(this.mshServerServantName);
		}
		catch (IllegalDataException ide) {
			// Never
			assert false;
			return null;
		}
	}
	
	protected void onLoseConnection(String servantName) {
		Log.debugMessage("MSHServerConnectionManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		// @todo Maybe, GUI-specific actions
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("MSHServerConnectionManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		// @todo Maybe, GUI-specific actions
	}
	
	public static MSHClientServantManager create() throws CommunicationException {
		String serverHostName = ApplicationProperties.getString(KEY_SERVER_HOST_NAME, SERVER_HOST_NAME);
		String contextName = ContextNameFactory.generateContextName(serverHostName);
		CORBAServer corbaServer = new CORBAServer(contextName);

		String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		String mshServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSHSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSHSERVER_PROCESS_CODENAME);

		MSHClientServantManager mshClientServantManager = new MSHClientServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				mshServerServantName);
		return mshClientServantManager;
	}
}
