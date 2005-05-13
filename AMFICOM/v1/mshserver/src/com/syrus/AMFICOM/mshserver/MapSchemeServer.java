/*-
 * $Id: MapSchemeServer.java,v 1.4 2005/05/13 17:47:53 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import org.omg.PortableServer.POA;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mshserver.corba.MSHServerPOATie;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/13 17:47:53 $
 * @author $Author: bass $
 * @module cmserver_v1
 */
public class MapSchemeServer {
	public static final String APPLICATION_NAME = "mshserver"; //$NON-NLS-1$

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


	private static final String PASSWORD = "MShServer"; //$NON-NLS-1$

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
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown();
			}
		});
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME,
				Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt(
				KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME,
				DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid,
					dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorException(e);
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
		processCodename = ApplicationProperties.getString(
				ServerProcessWrapper.KEY_MSHSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSHSERVER_PROCESS_CODENAME);
		try {
			final Server server = new Server(serverId);
			final ServerProcess serverProcess = AdministrationDatabaseContext.getServerProcessDatabase().retrieveForServerAndCodename(serverId, processCodename);
			final User user = new User(serverProcess.getUserId());
			login = user.getLogin();

			/*
			 * Init database object loader.
			 */
			DatabaseObjectLoader.init(user.getId());

			/*
			 * Create session environment.
			 */
			MSHServerSessionEnvironment.createInstance(server.getHostName());

			/*
			 * Login.
			 */
			final MSHServerSessionEnvironment sessionEnvironment = MSHServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD);
			} catch (final LoginException le) {
				Log.errorException(le);
			}

			/*
			 * Activate the servant.
			 */
			final CORBAServer corbaServer = sessionEnvironment.getMSHServerServantManager().getCORBAServer();
			final POA poa = corbaServer.getPoa();
			corbaServer.activateServant(
					poa.reference_to_servant((new MSHServerPOATie(new MSHServerImpl(), poa))._this(corbaServer.getOrb())),
					processCodename);
			corbaServer.printNamingContext();
		} catch (final Exception e) {
			Log.debugException(e, Log.SEVERE);
			System.exit(0);
		}
	}

	protected static synchronized void shutdown() {
		Log.debugMessage("MapSchemeServer.shutdown | serializing MapStorableObjectPool" , Log.INFO); //$NON-NLS-1$
		MapStorableObjectPool.serializePool();
		Log.debugMessage("MapSchemeServer.shutdown | serializing SchemeStorableObjectPool" , Log.INFO); //$NON-NLS-1$
		SchemeStorableObjectPool.serializePool();
	}

	static class MSHServerLoginRestorer implements LoginRestorer {
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
