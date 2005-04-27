/*
 * $Id: MCMServantManager.java,v 1.1 2005/04/27 15:08:07 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.eventserver.corba.EventServer;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.EventServerConnectionManager;
import com.syrus.AMFICOM.general.IGSConnectionManager;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LoginServerConnectionManager;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.loginserver.corba.LoginServer;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 15:08:07 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
public final class MCMServantManager extends RunnableVerifiedConnectionManager implements LoginServerConnectionManager, EventServerConnectionManager, IGSConnectionManager {

	private static final String KEY_SERVANT_NAME_LOGINSERVER = "LoginServerServantName";
	private static final String KEY_SERVANT_NAME_EVENTSERVER = "EventServerServantName";
	private static final String KEY_SERVANT_NAME_MSERVER = "MServerServantName";
	private static final String KEY_SERVANT_CHECK_TIMEOUT = "ServantCheckTimeout";

	private static final String SERVANT_NAME_LOGINSERVER = "LoginServer";
	private static final String SERVANT_NAME_EVENTSERVER = "EventServer";
	private static final String SERVANT_NAME_MSERVER = "MServer";
	public static final int SERVANT_CHECK_TIMEOUT = 10;		//min

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

		String loginServerServantName = ApplicationProperties.getString(KEY_SERVANT_NAME_LOGINSERVER, SERVANT_NAME_LOGINSERVER);
		String eventServerServantName = ApplicationProperties.getString(KEY_SERVANT_NAME_EVENTSERVER, SERVANT_NAME_EVENTSERVER);
		String mServerServantName = ApplicationProperties.getString(KEY_SERVANT_NAME_MSERVER, SERVANT_NAME_MSERVER);

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
