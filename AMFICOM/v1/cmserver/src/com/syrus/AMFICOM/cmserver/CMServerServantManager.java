/*-
 * $Id: CMServerServantManager.java,v 1.16 2005/10/30 15:20:44 bass Exp $
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
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.CommonServerHelper;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.EventServerHelper;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.leserver.corba.LoginServerHelper;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2005/10/30 15:20:44 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module cmserver
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
		return LoginServerHelper.narrow(this.getVerifiableReference(this.loginServerServantName));
	}

	public EventServer getEventServerReference() throws CommunicationException {
		return EventServerHelper.narrow(this.getVerifiableReference(this.eventServerServantName));
	}

	/**
	 * @see com.syrus.AMFICOM.general.IGSConnectionManager#getIGSReference()
	 * @todo The reference returned is not initialized anywhere.
	 */
	public IdentifierGeneratorServer getIGSReference() {
		return this.databaseIdentifierGeneratorServer;
	}

	public CommonServer getServerReference() throws CommunicationException {
		return CommonServerHelper.narrow(this.getVerifiableReference(this.mServerServantName));
	}

	/**
	 * @param servantName
	 * @see com.syrus.AMFICOM.general.VerifiedConnectionManager#onLoseConnection(String)
	 * @todo Generate event "Connection lost" and ask Bass to do the same
	 *       for MscharServer.
	 */
	@Override
	protected void onLoseConnection(final String servantName) {
		assert Log.debugMessage("Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
	}

	/**
	 * @param servantName
	 * @see com.syrus.AMFICOM.general.VerifiedConnectionManager#onRestoreConnection(String)
	 * @todo Generate event "Connection restored" and ask Bass to do the
	 *       same for MscharServer.
	 */
	@Override
	protected void onRestoreConnection(final String servantName) {
		assert Log.debugMessage("Connection with '" + servantName + "' restored",
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
		(new Thread(cmServerServantManager, "CMServerServantManager")).start();
		return cmServerServantManager;
	}
}
