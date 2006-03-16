/*-
 * $Id: MapSchemeAdministrationResourceServer.java,v 1.26 2006/03/16 11:32:48 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import static com.syrus.AMFICOM.general.ObjectEntities.SERVERPROCESS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SERVER_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SYSTEMUSER_CODE;

import java.util.logging.Level;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.26 $, $Date: 2006/03/16 11:32:48 $
 * @author $Author: arseniy $
 * @module mscharserver
 */
final class MapSchemeAdministrationResourceServer {
	public static final String APPLICATION_NAME = "mscharserver";

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
	public static final int DB_CONNECTION_TIMEOUT = 120;

	public static final String DB_LOGIN_NAME = "amficom";

	public static final String SERVER_ID = "Server_1";


	private static final String PASSWORD = "MSchARServer";

	/**
	 * Identifier of this server.
	 */
	private static Identifier serverId;

	/**
	 * Login of the corresponding user.
	 */
	static String login;

	/**
	 * Process codename.
	 */
	private static String processCodename;

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		startup();

		/**
		 * Add shutdown hook.
		 */
		Runtime.getRuntime().addShutdownHook(new Thread("MapSchemeAdministrationResourceServer -- shutdown hook") {
			@Override
			public void run() {
				shutdown();
			}
		});
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
			System.exit(-1);
		}
	}

	private static void startup() {
		/*
		 * Establish connection with database.
		 */
		establishDatabaseConnection();

		DatabaseContextSetup.initDatabaseContext();
		serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		processCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSCHARSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME);
		try {
			final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(SERVER_CODE);
			final Server server = serverDatabase.retrieveForId(serverId);

			final StorableObjectDatabase<ServerProcess> storableObjectDatabase = DatabaseContext.getDatabase(SERVERPROCESS_CODE);
			final ServerProcess serverProcess = ((ServerProcessDatabase) storableObjectDatabase).retrieveForServerAndCodename(serverId,
					processCodename);

			final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(SYSTEMUSER_CODE);
			final SystemUser user = ((SystemUserDatabase) systemUserDatabase).retrieveForId(serverProcess.getUserId());

			login = user.getLogin();

			/*
			 * Mapinfo pool init.
			 */
			MapInfoPool.init();

			/*
			 * Create session environment.
			 */
			MscharServerSessionEnvironment.createInstance(server.getHostName(), processCodename);

			/*
			 * Login.
			 */
			final MscharServerSessionEnvironment sessionEnvironment = MscharServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD, server.getDomainId());
			} catch (final LoginException le) {
				Log.errorMessage(le);
			}
		} catch (final Exception e) {
			Log.debugMessage(e, Level.SEVERE);
			System.exit(0);
		}
	}

	static synchronized void shutdown() {
		Log.debugMessage("serializing StorableObjectPool" , Level.INFO);
		DatabaseConnection.closeConnection();
	}

	static class MscharServerLoginRestorer implements LoginRestorer {

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
