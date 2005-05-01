/*
 * $Id: MCMServantManager.java,v 1.6 2005/05/01 17:30:10 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/05/01 17:30:10 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMServantManager extends RunnableVerifiedConnectionManager implements BaseConnectionManager {
	private String loginServerServantName;
	private String eventServerServantName;
	private String mServerServantName;

	public MCMServantManager(CORBAServer corbaServer,
			String loginServerServantName,
			String eventServerServantName,
			String mServerServantName,
			long timeout) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, mServerServantName}, timeout);

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.mServerServantName = mServerServantName;

		assert timeout >= 10 * 60 * 1000 : "Too low timeout"; //not less then 10 min
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
			return (IdentifierGeneratorServer) super.getVerifiableReference(this.mServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	protected MServer getMServerReference() throws CommunicationException {
		try {
			return (MServer) super.getVerifiableReference(this.mServerServantName);
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	protected void onLoseConnection(String servantName) {
		Log.debugMessage("MCMServantManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		//@todo Generate event "Connection lost"
	}

	protected void onRestoreConnection(String servantName) {
		Log.debugMessage("MCMServantManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		//@todo Generate event "Connection restored"
	}

	public static MCMServantManager createAndStart(String serverHostName) throws CommunicationException {
		String contextName = ContextNameFactory.generateContextName(serverHostName);
		CORBAServer corbaServer = new CORBAServer(contextName);

		String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		String mServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME);

		long timeout = ApplicationProperties.getInt(KEY_SERVANT_CHECK_TIMEOUT, SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		MCMServantManager mcmServantManager = new MCMServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				mServerServantName,
				timeout);
		(new Thread(mcmServantManager)).start();
		return mcmServantManager;
	}
}
