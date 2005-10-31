/*-
 * $Id: MServerServantManager.java,v 1.18 2005/10/31 10:48:40 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Set;

import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.BaseConnectionManager;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.ContextNameFactory;
import com.syrus.AMFICOM.general.DatabaseIdentifierGeneratorServer;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RunnableVerifiedConnectionManager;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.Verifiable;
import com.syrus.AMFICOM.leserver.corba.EventServer;
import com.syrus.AMFICOM.leserver.corba.EventServerHelper;
import com.syrus.AMFICOM.leserver.corba.LoginServer;
import com.syrus.AMFICOM.leserver.corba.LoginServerHelper;
import com.syrus.AMFICOM.mcm.corba.MCM;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.18 $, $Date: 2005/10/31 10:48:40 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */

final class MServerServantManager extends RunnableVerifiedConnectionManager implements BaseConnectionManager {
	private String loginServerServantName;
	private String eventServerServantName;
	private DatabaseIdentifierGeneratorServer databaseIdentifierGeneratorServer;

	public MServerServantManager(final CORBAServer corbaServer,
			final String loginServerServantName,
			final String eventServerServantName,
			final Set<String> mcmIdStrings,
			final long timeout) {
		super(corbaServer, mcmIdStrings, timeout);
		super.addServantName(loginServerServantName);
		super.addServantName(eventServerServantName);

		this.loginServerServantName = loginServerServantName;
		this.eventServerServantName = eventServerServantName;

		this.databaseIdentifierGeneratorServer = new DatabaseIdentifierGeneratorServer();

		assert timeout >= 10L * 60L * 1000L : "Too low timeout"; //not less then 10 min
	}

	public LoginServer getLoginServerReference() throws CommunicationException {
		return LoginServerHelper.narrow(this.getVerifiableReference(this.loginServerServantName));
	}

	public EventServer getEventServerReference() throws CommunicationException {
		return EventServerHelper.narrow(this.getVerifiableReference(this.eventServerServantName));
	}

	public IdentifierGeneratorServer getIGSReference() {
		return this.databaseIdentifierGeneratorServer;
	}

	public MCM getVerifiedMCMReference(final Identifier mcmId) throws CommunicationException {
		final Verifiable reference = this.getVerifiableReference(mcmId.toString());
		return (MCM) reference;
	}

	@Override
	protected void onLoseConnection(final String servantName) {
		Log.debugMessage("Connection with '" + servantName + "' lost", Log.DEBUGLEVEL08);
		//@todo Generate event "Connection with servantName lost"
	}

	@Override
	protected void onRestoreConnection(final String servantName) {
		Log.debugMessage("Connection with '" + servantName + "' restored",
				Log.DEBUGLEVEL08);
		//@todo Generate event "Connection with servantName restored"
	}

	public static MServerServantManager createAndStart(final String serverHostName, final Set<Identifier> mcmIds) throws ApplicationException {
		final String contextName = ContextNameFactory.generateContextName(serverHostName);
		final CORBAServer corbaServer = new CORBAServer(contextName);

		final String loginServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		final String eventServerServantName = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);

		final Set<String> mcmIdStrings = Identifier.createStrings(mcmIds);

		final long timeout = ApplicationProperties.getInt(KEY_SERVANT_CHECK_TIMEOUT, SERVANT_CHECK_TIMEOUT) * 60 * 1000;

		final MServerServantManager mServerServantManager = new MServerServantManager(corbaServer,
				loginServerServantName,
				eventServerServantName,
				mcmIdStrings,
				timeout);
		(new Thread(mServerServantManager, "MServerServantManager")).start();
		return mServerServantManager;
	}
}

