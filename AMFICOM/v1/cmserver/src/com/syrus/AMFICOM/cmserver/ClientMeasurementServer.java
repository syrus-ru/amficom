/*-
 * $Id: ClientMeasurementServer.java,v 1.69 2005/10/30 15:20:44 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.cmserver.corba.CMServerPOATie;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.69 $, $Date: 2005/10/30 15:20:44 $
 * @author $Author: bass $
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
	 * Identifier of domain to log in 
	 */
	static Identifier domainId;

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
		startup();

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

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Retrieve info about server*/
		/*	Retrieve info about process*/
		/*	Retrieve info about user*/
		serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		processCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_CMSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);
		try {
			final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVER_CODE);
			final Server server = serverDatabase.retrieveForId(serverId);

			final StorableObjectDatabase<ServerProcess> storableObjectDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_CODE);
			final ServerProcess serverProcess = ((ServerProcessDatabase) storableObjectDatabase).retrieveForServerAndCodename(serverId,
					processCodename);

			final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(ObjectEntities.SYSTEMUSER_CODE);
			final SystemUser user = ((SystemUserDatabase) systemUserDatabase).retrieveForId(serverProcess.getUserId());

			login = user.getLogin();
			domainId = server.getDomainId();

			/*	Create session environment*/
			CMServerSessionEnvironment.createInstance(server.getHostName());
	
			/*	Login*/
			final CMServerSessionEnvironment sessionEnvironment = CMServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD, domainId);
			} catch (final LoginException le) {
				assert Log.errorMessage(le);
			}
	
			/*	Activate servant*/
			final CORBAServer corbaServer = sessionEnvironment.getCMServerServantManager().getCORBAServer();
			corbaServer.activateServant(new CMServerPOATie(new CMServerImpl(), corbaServer.getPoa()), processCodename);
			corbaServer.printNamingContext();
		} catch (final Exception e) {
			assert Log.errorMessage(e);
			System.exit(0);
		}
	}

	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			assert Log.errorMessage(e);
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

		public Identifier getDomainId() {
			return domainId;
		}
	}
}
