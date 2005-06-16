/*-
 * $Id: MapSchemeAdministrationResourceServer.java,v 1.4 2005/06/16 10:47:47 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerPOATie;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/16 10:47:47 $
 * @author $Author: bass $
 * @module mscharserver_v1
 */
public class MapSchemeAdministrationResourceServer {
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


	private static final String PASSWORD = "MscharServer";

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
				ServerProcessWrapper.KEY_MSCHARSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSCHARSERVER_PROCESS_CODENAME);
		try {
			final Server server = new Server(serverId);
			final ServerProcess serverProcess = ((ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_ENTITY_CODE)).retrieveForServerAndCodename(serverId, processCodename);
			final SystemUser user = new SystemUser(serverProcess.getUserId());
			login = user.getLogin();

			/*
			 * Init database object loader.
			 */
			DatabaseObjectLoader.init(user.getId());
			
			/*
			 * Mapinfo pool init.
			 */
			MapInfoPool.init();

			/*
			 * Create session environment.
			 */
			MscharServerSessionEnvironment.createInstance(server.getHostName());

			/*
			 * Login.
			 */
			final MscharServerSessionEnvironment sessionEnvironment = MscharServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD);
			} catch (final LoginException le) {
				Log.errorException(le);
			}

			/*
			 * Activate the servant.
			 */
			final CORBAServer corbaServer = sessionEnvironment.getMscharServerServantManager().getCORBAServer();
			corbaServer.activateServant(
					new MscharServerPOATie(new MscharServerImpl(), corbaServer.getPoa()),
					processCodename);
			corbaServer.printNamingContext();
		} catch (final Exception e) {
			Log.debugException(e, Log.SEVERE);
			System.exit(0);
		}
	}

	protected static synchronized void shutdown() {
		Log.debugMessage("MapSchemeAdministrationResourceServer.shutdown | serializing StorableObjectPool" , Log.INFO);
		StorableObjectPool.serialize();
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
