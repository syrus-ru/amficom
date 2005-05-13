/*-
 * $Id: ClientMeasurementServer.java,v 1.42 2005/05/13 17:51:04 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.42 $, $Date: 2005/05/13 17:51:04 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public class ClientMeasurementServer {
	public static final String APPLICATION_NAME = "cmserver"; //$NON-NLS-1$

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	public static final String KEY_DB_HOST_NAME = "DBHostName"; //$NON-NLS-1$
	public static final String KEY_DB_SID = "DBSID"; //$NON-NLS-1$
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout"; //$NON-NLS-1$
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName"; //$NON-NLS-1$
	public static final String KEY_SERVER_ID = "ServerID"; //$NON-NLS-1$

	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	public static final String DB_SID = "amficom"; //$NON-NLS-1$
	/**
	 * Database connection timeout, in seconds.
	 */
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom"; //$NON-NLS-1$
	public static final String SERVER_ID = "Server_1"; //$NON-NLS-1$

	private static final String PASSWORD = "CMServer"; //$NON-NLS-1$

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

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		startup();

		/**
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
		/*	Retrieve info about process*/
		/*	Retrieve info about user*/
		serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		processCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_CMSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.CMSERVER_PROCESS_CODENAME);
		try {
			final Server server = new Server(serverId);
			final ServerProcess serverProcess = AdministrationDatabaseContext.getServerProcessDatabase().retrieveForServerAndCodename(serverId, processCodename);
			final User user = new User(serverProcess.getUserId());
			login = user.getLogin();

			/*	Init database object loader*/
			DatabaseObjectLoader.init(user.getId());
	
			/*	Create session environment*/
			CMServerSessionEnvironment.createInstance(server.getHostName());
	
			/*	Login*/
			final CMServerSessionEnvironment sessionEnvironment = CMServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD);
			} catch (final LoginException le) {
				Log.errorException(le);
			}
	
			/*	Activate servant*/
			final CORBAServer corbaServer = sessionEnvironment.getCMServerServantManager().getCORBAServer();
			corbaServer.activateServant(new CMServerImpl(), processCodename);
			corbaServer.printNamingContext();
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
			System.exit(0);
		}
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(0);
		}
	}

	protected static synchronized void shutdown() {
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
