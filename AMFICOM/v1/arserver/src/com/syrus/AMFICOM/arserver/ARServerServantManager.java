/*-
 * $Id: ARServerServantManager.java,v 1.3 2005/05/18 13:03:22 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/05/18 13:03:22 $
 * @module arserver_v1
 */
public class ARServerServantManager extends RunnableVerifiedConnectionManager
		implements BaseConnectionManager {
	private String loginServerServantName;

	private String eventServerServantName;

	private DatabaseIdentifierGeneratorServer databaseIdentifierGeneratorServer;

	public ARServerServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final long timeout) {
		super(corbaServer, new String[]{loginServerServantName, eventServerServantName}, timeout);

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;

		//NOTE ARServer never generates identifiers for himself, so this initialization is redundant.
		this.databaseIdentifierGeneratorServer = new DatabaseIdentifierGeneratorServer();

		assert timeout >= 10L * 60L * 1000L: ErrorMessages.TIMEOUT_TOO_SHORT; //not less then 10 min
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		try {
			return (LoginServer) super.getVerifiableReference(this.loginServerServantName);
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	public EventServer getEventServerReference() throws CommunicationException {
		try {
			return (EventServer) super.getVerifiableReference(this.eventServerServantName);
		} catch (final IllegalDataException ide) {
			/*
			 * Never.
			 */
			assert false;
			return null;
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.IGSConnectionManager#getIGSReference()
	 * @todo The reference returned is not initialized anywhere.
	 */
	public IdentifierGeneratorServer getIGSReference() {
		return this.databaseIdentifierGeneratorServer;
	}

	/**
	 * @param servantName
	 * @see com.syrus.AMFICOM.general.VerifiedConnectionManager#onLoseConnection(String)
	 * @todo Generate event "Connection lost".
	 */
	protected void onLoseConnection(final String servantName) {
		Log.debugMessage("ARServerServantManager.onLoseConnection | Connection with '"
				+ servantName
				+ "' lost",
				Log.WARNING);
	}

	/**
	 * @param servantName
	 * @see com.syrus.AMFICOM.general.VerifiedConnectionManager#onRestoreConnection(String)
	 * @todo Generate event "Connection restored".
	 */
	protected void onRestoreConnection(final String servantName) {
		Log.debugMessage("ARServerServantManager.onRestoreConnection | Connection with '"
				+ servantName
				+ "' restored",
				Log.INFO);
				
	}

	public static ARServerServantManager createAndStart(final String serverHostName) throws CommunicationException {
		final ARServerServantManager arServerServantManager = new ARServerServantManager(
				new CORBAServer(ContextNameFactory.generateContextName(serverHostName)),
				ApplicationProperties.getString(
						ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
						ServerProcessWrapper.LOGIN_PROCESS_CODENAME),
				ApplicationProperties.getString(
						ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
						ServerProcessWrapper.EVENT_PROCESS_CODENAME),
				ApplicationProperties.getInt(
						KEY_SERVANT_CHECK_TIMEOUT,
						SERVANT_CHECK_TIMEOUT) * 60 * 1000);
		(new Thread(arServerServantManager)).start();
		return arServerServantManager;
	}
}
