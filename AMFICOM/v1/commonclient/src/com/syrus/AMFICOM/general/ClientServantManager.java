/*
 * $Id: ClientServantManager.java,v 1.3 2005/06/03 09:04:10 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/03 09:04:10 $
 * @author $Author: arseniy $
 * @module commonclient_v1
 */
abstract class ClientServantManager extends VerifiedConnectionManager implements BaseConnectionManager, ServerConnectionManager {
	static final String KEY_SERVER_HOST_NAME = "ServerHostName";
	static final String SERVER_HOST_NAME = "localhost";

	private String loginServerServantName;
	private String eventServerServantName;

	/**
	 * Currently, can hold the name of CMServer, MSHServer and ARServer
	 * servants.
	 */
	private String commonServerServantName;

	/**
	 * @param corbaServer
	 * @param loginServerServantName
	 * @param eventServerServantName
	 * @param commonServerServantName
	 */

	public ClientServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String commonServerServantName) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, commonServerServantName});

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.commonServerServantName = commonServerServantName;
	}

	public final LoginServer getLoginServerReference() throws CommunicationException {
		try {
			return (LoginServer) super.getVerifiableReference(this.loginServerServantName);
		}
		catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public final EventServer getEventServerReference() throws CommunicationException {
		try {
			return (EventServer) super.getVerifiableReference(this.eventServerServantName);
		}
		catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public final IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		try {
			return (IdentifierGeneratorServer) super.getVerifiableReference(this.commonServerServantName);
		}
		catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public final CommonServer getServerReference() throws CommunicationException {
		try {
			return (CommonServer) super.getVerifiableReference(this.commonServerServantName);
		}
		catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	protected final void onLoseConnection(final String servantName) {
		Log.debugMessage("AbstractClientServantManager.onLoseConnection() | Connection with '" + servantName + "' lost",
				Log.DEBUGLEVEL08);
		/**
		 * @todo Maybe, GUI-specific actions
		 */
	}

	protected final void onRestoreConnection(final String servantName) {
		Log.debugMessage("AbstractClientServantManager.onRestoreConnection() | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		/**
		 * @todo Maybe, GUI-specific actions
		 */
	}
}
