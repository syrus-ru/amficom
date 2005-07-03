/*-
 * $Id: LoginEventServer.java,v 1.20 2005/06/25 17:07:42 bass Exp $
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
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.20 $, $Date: 2005/06/25 17:07:42 $
 * @author $Author: bass $
 * @module leserver_v1
 */
public final class LoginEventServer {
	public static final String APPLICATION_NAME = "leserver";

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_SERVER_ID = "ServerID";

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
		LoginProcessor loginProcessor = new LoginProcessor();
		loginProcessor.start();

		/*
		 * Add shutdown hook.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread() {
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
		Identifier serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		try {
			final Server server = new Server(serverId);

			/*	Retrieve info about processes*/
			loginProcessCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_LOGIN_PROCESS_CODENAME,
					ServerProcessWrapper.LOGIN_PROCESS_CODENAME);
			eventProcessCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_EVENT_PROCESS_CODENAME,
					ServerProcessWrapper.EVENT_PROCESS_CODENAME);
			final ServerProcessDatabase serverProcessDatabase = (ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_CODE);
			final ServerProcess loginServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, loginProcessCodename);
			final ServerProcess eventServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, eventProcessCodename);
			// TODO something with loginServerProcess and eventServerProcess
			if (loginServerProcess == null || eventServerProcess == null)
				throw new ApplicationException("Cannot find login server process or event server process");
	
			/*	Init session environment
			 * NOTE: No logging in*/
			LEServerSessionEnvironment.createInstance(server.getHostName());
	
			/*	Activate servants*/
			final CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
			corbaServer.activateServant(new LoginServerImplementation(corbaServer.getOrb()), loginProcessCodename);
			corbaServer.activateServant(new EventServerImplementation(), eventProcessCodename);
			corbaServer.printNamingContext();
		}
		catch (final ApplicationException ae) {
			Log.errorException(ae);
			System.exit(0);
		}
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(0);
		}
	}

	public void run() {
		while (this.running) {
			// TODO Implement
		}
	}

	protected static void shutdown() {
		LEServerSessionEnvironment leServerSessionEnvironment = LEServerSessionEnvironment.getInstance();
		leServerSessionEnvironment.getPoolContext().serialize();

		DatabaseConnection.closeConnection();
	}

}
