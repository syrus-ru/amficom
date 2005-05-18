/*
 * $Id: AdministrationResourceServer.java,v 1.5 2005/05/18 12:56:28 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver;

import org.omg.PortableServer.POA;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.arserver.corba.ARServerPOATie;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.5 $, $Date: 2005/05/18 12:56:28 $
 * @author $Author: bass $
 * @module arserver_v1
 */
public class AdministrationResourceServer {
	public static final String APPLICATION_NAME = "arserver";

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


	private static final String PASSWORD = "ARServer";

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
		String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
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
				ServerProcessWrapper.KEY_ARSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.ARSERVER_PROCESS_CODENAME);
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
			ARServerSessionEnvironment.createInstance(server.getHostName());

			/*
			 * Login.
			 */
			final ARServerSessionEnvironment sessionEnvironment = ARServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD);
			} catch (final LoginException le) {
				Log.errorException(le);
			}

			/*
			 * Activate the servant.
			 */
			final CORBAServer corbaServer = sessionEnvironment.getARServerServantManager().getCORBAServer();
			final POA poa = corbaServer.getPoa();
			corbaServer.activateServant(
					poa.reference_to_servant((new ARServerPOATie(new ARServerImpl(), poa))._this(corbaServer.getOrb())),
					processCodename);
			corbaServer.printNamingContext();
		} catch (final Exception e) {
			Log.debugException(e, Log.SEVERE);
			System.exit(0);
		}
	}
	
	protected static synchronized void shutdown() {
		Log.debugMessage("AdministrationResourceServer.shutdown | serializing ResourceStorableObjectPool" , Log.INFO);
		ResourceStorableObjectPool.serializePool();		
	}

	static class ARServerLoginRestorer implements LoginRestorer {
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
