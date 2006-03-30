/*-
 * $Id: ClientMeasurementServer.java,v 1.74 2006/03/30 12:35:40 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import static com.syrus.AMFICOM.administration.ServerProcessWrapper.CMSERVER_PROCESS_CODENAME;
import static com.syrus.AMFICOM.administration.ServerProcessWrapper.KEY_CMSERVER_PROCESS_CODENAME;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVERPROCESS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.74 $, $Date: 2006/03/30 12:35:40 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module cmserver
 */
final class ClientMeasurementServer {
	private static final String APPLICATION_NAME = "cmserver";

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

	private static final String DB_SID = "amficom";
	private static final int DB_CONNECTION_TIMEOUT = 120; //sec
	private static final String DB_LOGIN_NAME = "amficom";
	private static final String SERVER_ID = "Server_1";

	private static final String PASSWORD = "CMServer";

	/**
	 * Identifier of this server.
	 */
	private static Identifier serverId;

	/**
	 * Login of the corresponding user
	 */
	static String login;

	/**
	 * Process codename.
	 */
	private static String processCodename;

	private ClientMeasurementServer() {
		assert false : "Nothing";
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

		/**
		 * Add shutdown hook.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread("ClientMeasurementServer -- shutdown hook") {
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
		/*	Retrieve info about process*/
		/*	Retrieve info about user*/
		serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		processCodename = ApplicationProperties.getString(KEY_CMSERVER_PROCESS_CODENAME, CMSERVER_PROCESS_CODENAME);
		final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(SERVER_CODE);
		final Server server = serverDatabase.retrieveForId(serverId);

		final StorableObjectDatabase<ServerProcess> storableObjectDatabase = DatabaseContext.getDatabase(SERVERPROCESS_CODE);
		final ServerProcess serverProcess = ((ServerProcessDatabase) storableObjectDatabase).retrieveForServerAndCodename(serverId,
				processCodename);

		final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(SYSTEMUSER_CODE);
		final SystemUser user = ((SystemUserDatabase) systemUserDatabase).retrieveForId(serverProcess.getUserId());

		login = user.getLogin();

		/*	Create session environment*/
		CMServerSessionEnvironment.createInstance(server.getHostName(), processCodename);

		/*	Login*/
		final CMServerSessionEnvironment sessionEnvironment = CMServerSessionEnvironment.getInstance();
		sessionEnvironment.login(login, PASSWORD, server.getDomainId());
	}

	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorMessage(e);
			System.exit(0);
		}
	}

	static synchronized void shutdown() {
		DatabaseConnection.closeConnection();
	}

	static class CMServerLoginRestorer implements LoginRestorer {

		public boolean restoreLogin() {
			return true;
		}

		public String getLogin() {
			return login;
		}

		public String getPassword() {
			return PASSWORD;
		}
	}
}
