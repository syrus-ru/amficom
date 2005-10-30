/*-
 * $Id: LoginEventServer.java,v 1.30 2005/10/30 14:49:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.30 $, $Date: 2005/10/30 14:49:11 $
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

	private boolean running;

	private LoginEventServer() {
		// singleton
		assert false;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		startup();

		/*	Start Login Processor*/
		final LoginProcessor loginProcessor = new LoginProcessor();
		loginProcessor.start();

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

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Retrieve info about server*/
		final Identifier serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		try {
			final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVER_CODE);
			final Server server = serverDatabase.retrieveForId(serverId);

			/*	Retrieve info about processes*/
			loginProcessCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
					ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
			eventProcessCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
					ServerProcessWrapper.EVENT_PROCESS_CODENAME);
			final StorableObjectDatabase<ServerProcess> storableObjectDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_CODE);
			final ServerProcessDatabase serverProcessDatabase = (ServerProcessDatabase) storableObjectDatabase;
			final ServerProcess loginServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, loginProcessCodename);
			final ServerProcess eventServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, eventProcessCodename);
			// TODO something with loginServerProcess and eventServerProcess
			if (loginServerProcess == null || eventServerProcess == null) {
				throw new ApplicationException("Cannot find login server process or event server process");
			}
	
			/*	Init session environment
			 * NOTE: No logging in*/
			LEServerSessionEnvironment.createInstance(server.getHostName());
	
			/*	Activate servants*/
			final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
			corbaServer.activateServant(new LoginServerImplementation(), loginProcessCodename);
			corbaServer.activateServant(new EventServerImplementation(), eventProcessCodename);
			corbaServer.printNamingContext();
		}
		catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			System.exit(0);
		}
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

	public void run() {
		while (this.running) {
			// TODO Implement
		}
	}

	static void shutdown() {
		final LEServerSessionEnvironment leServerSessionEnvironment = LEServerSessionEnvironment.getInstance();
		StorableObjectPool.serialize(leServerSessionEnvironment.getPoolContext().getLRUSaver());
		DatabaseConnection.closeConnection();
	}

}
