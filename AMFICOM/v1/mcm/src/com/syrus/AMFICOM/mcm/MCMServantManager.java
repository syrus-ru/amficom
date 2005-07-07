/*-
 * $Id: MCMServantManager.java,v 1.12 2005/07/07 19:39:54 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.ServerConnectionManager;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.CommonServerHelper;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServerHelper;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.EventServerHelper;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.leserver.corba.LoginServerHelper;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/07/07 19:39:54 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMServantManager extends RunnableVerifiedConnectionManager implements BaseConnectionManager, ServerConnectionManager {
	private String loginServerServantName;
	private String eventServerServantName;
	private String mServerServantName;

	public MCMServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final String mServerServantName,
			final long timeout) {
		super(corbaServer, new String[] {loginServerServantName, eventServerServantName, mServerServantName}, timeout);

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;
		this.mServerServantName = mServerServantName;

		assert timeout >= 10L * 60L * 1000L: ErrorMessages.TIMEOUT_TOO_SHORT; //not less then 10 min
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		try {
			return LoginServerHelper.narrow(super.getVerifiableReference(this.loginServerServantName));
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public EventServer getEventServerReference() throws CommunicationException {
		try {
			return EventServerHelper.narrow(super.getVerifiableReference(this.eventServerServantName));
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
		try {
			return IdentifierGeneratorServerHelper.narrow(super.getVerifiableReference(this.mServerServantName));
		}
		catch (IllegalDataException e) {
			// Never
			assert false;
			return null;
		}
	}

	public CommonServer getServerReference() throws CommunicationException {
		try {
			return CommonServerHelper.narrow(super.getVerifiableReference(this.mServerServantName));
		}
		catch (final IllegalDataException ide) {
			// Never
			assert false;
			return null;
		}
	}

	protected MServer getMServerReference() throws CommunicationException {
		return (MServer) this.getServerReference();
	}

	@Override
	protected void onLoseConnection(final String servantName) {
		Log.debugMessage("MCMServantManager.onLoseConnection | Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		//@todo Generate event "Connection lost"
	}

	@Override
	protected void onRestoreConnection(final String servantName) {
		Log.debugMessage("MCMServantManager.onRestoreConnection | Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		//@todo Generate event "Connection restored"
	}

	public static MCMServantManager createAndStart(final String serverHostName) throws CommunicationException {
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		final String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		final String mServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME);

		final long timeout = ApplicationProperties.getInt(KEY_SERVANT_CHECK_TIMEOUT, SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		final MCMServantManager mcmServantManager = new MCMServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				mServerServantName,
				timeout);
		(new Thread(mcmServantManager)).start();
		return mcmServantManager;
	}
}
