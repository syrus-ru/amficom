/*-
 * $Id: LoginEventServer.java,v 1.41 2006/06/29 10:05:29 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.general.ObjectEntities.SERVERPROCESS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.41 $, $Date: 2006/06/29 10:05:29 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class LoginEventServer {
	private static final String APPLICATION_NAME = "leserver";

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	private static final String KEY_SERVER_ID = "ServerID";

	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	public static final String DB_SID = "amficom";
	/**
	 * Database connection timeout, in seconds.
	 */
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	public static final String DB_LOGIN_NAME = "amficom";
	public static final String SERVER_ID = "Server_1";

	private static String loginProcessCodename;
	private static String eventProcessCodename;

	private LoginEventServer() {
		// singleton
		assert false;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		try {
			startup();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			Log.errorMessage("Cannot start -- exiting");
			System.exit(0);
		}

		/*	@todo: Start Event Processor*/

		/*
		 * Add shutdown hook.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread("LoginEventServer -- shutdown hook") {
			@Override
			public void run() {
				shutdown();
			}
		});
	}

	private static void startup() throws ApplicationException {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Retrieve info about server*/
		final Identifier serverId = Identifier.valueOf(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(SERVER_CODE);
		final Server server = serverDatabase.retrieveForId(serverId);

		/*	Retrieve info about processes*/
		loginProcessCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
				ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
		eventProcessCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
				ServerProcessWrapper.EVENT_PROCESS_CODENAME);
		final StorableObjectDatabase<ServerProcess> storableObjectDatabase = DatabaseContext.getDatabase(SERVERPROCESS_CODE);
		final ServerProcessDatabase serverProcessDatabase = (ServerProcessDatabase) storableObjectDatabase;
		final ServerProcess loginServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, loginProcessCodename);
		final ServerProcess eventServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, eventProcessCodename);
		// TODO something with loginServerProcess and eventServerProcess
		if (loginServerProcess == null || eventServerProcess == null) {
			throw new ApplicationException("Cannot find login server process or event server process");
		}

		/* Init session environment */
		LEServerSessionEnvironment.createInstance(server.getHostName(), eventServerProcess.getUserId());

		/*	Create and start Login Processor*/
		LoginProcessor.createInstance();
		LoginProcessor.getInstance().start();

		/*	Activate servants*/
		final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
		corbaServer.activateServant(new LoginServerImplementation(), loginProcessCodename);
		corbaServer.activateServant(new EventServerImplementation(), eventProcessCodename);
		corbaServer.printNamingContext();
	}

	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
			Log.errorMessage(e);
			System.exit(0);
		}
	}

	static void shutdown() {
		final LEServerSessionEnvironment leServerSessionEnvironment = LEServerSessionEnvironment.getInstance();
		StorableObjectPool.serialize(leServerSessionEnvironment.getPoolContext().getLRUMapSaver());
	}
}
