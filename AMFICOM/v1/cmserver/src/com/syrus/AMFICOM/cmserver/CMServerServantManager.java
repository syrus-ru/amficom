/*-
 * $Id: CMServerServantManager.java,v 1.7 2005/06/07 17:58:14 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/07 17:58:14 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
final class CMServerServantManager extends RunnableVerifiedConnectionManager implements BaseConnectionManager, ServerConnectionManager {
	private String loginServerServantName;
	private String eventServerServantName;
	private String mServerServantName;
	private DatabaseIdentifierGeneratorServer databaseIdentifierGeneratorServer;

	public CMServerServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String mServerServantName,
			final long timeout) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, mServerServantName}, timeout);

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.mServerServantName = mServerServantName;

		//NOTE CMServer never generates identifiers for himself, so this initialization is redundant.
		this.databaseIdentifierGeneratorServer = new DatabaseIdentifierGeneratorServer();

		assert timeout >= 10L * 60L * 1000L: ErrorMessages.TIMEOUT_TOO_SHORT; //not less then 10 min
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		try {
			return (LoginServer) super.getVerifiableReference(this.loginServerServantName);
		}
		catch (final IllegalDataException ide) {
			// Never
			assert false;
			return null;
		}
	}

	public EventServer getEventServerReference() throws CommunicationException {
		try {
			return (EventServer) super.getVerifiableReference(this.eventServerServantName);
		}
		catch (final IllegalDataException ide) {
			// Never
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

	public CommonServer getServerReference() throws CommunicationException {
		try {
			return (CommonServer) super.getVerifiableReference(this.mServerServantName);
		}
		catch (final IllegalDataException ide) {
			// Never
			assert false;
			return null;
		}
	}

	/**
	 * @deprecated Use getServerReference() instead.
	 * @return
	 * @throws CommunicationException
	 */
	public MServer getMServerReference() throws CommunicationException {
		try {
			return (MServer) super.getVerifiableReference(this.mServerServantName);
		}
		catch (final IllegalDataException ide) {
			// Never
			assert false;
			return null;
		}
	}

	/**
	 * @param servantName
	 * @see com.syrus.AMFICOM.general.VerifiedConnectionManager#onLoseConnection(String)
	 * @todo Generate event "Connection lost" and ask Bass to do the same
	 *       for MscharServer.
	 */
	protected void onLoseConnection(final String servantName) {
		Log.debugMessage("CMServerServantManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
	}

	/**
	 * @param servantName
	 * @see com.syrus.AMFICOM.general.VerifiedConnectionManager#onRestoreConnection(String)
	 * @todo Generate event "Connection restored" and ask Bass to do the
	 *       same for MscharServer.
	 */
	protected void onRestoreConnection(final String servantName) {
		Log.debugMessage("CMServerServantManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
	}

	public static CMServerServantManager createAndStart(final String serverHostName) throws CommunicationException {
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		final String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		final String mServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME);

		final long timeout = ApplicationProperties.getInt(KEY_SERVANT_CHECK_TIMEOUT, SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		final CMServerServantManager cmServerServantManager = new CMServerServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				mServerServantName,
				timeout);
		(new Thread(cmServerServantManager)).start();
		return cmServerServantManager;
	}
}
