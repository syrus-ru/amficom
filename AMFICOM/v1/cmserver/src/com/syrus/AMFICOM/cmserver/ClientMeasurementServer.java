/*
 * $Id: ClientMeasurementServer.java,v 1.38 2005/05/03 19:57:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.38 $, $Date: 2005/05/03 19:57:00 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public class ClientMeasurementServer {

	public static final String APPLICATION_NAME = "cmserver";

	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_SERVER_ID = "ServerID";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final String SERVER_ID = "Server_1";
	public static final int TICK_TIME = 10; //sec

	/*	Identifier of server*/
	private static Identifier serverId;

	/*	Process codename*/
	private static String processCodename;

	public ClientMeasurementServer() {
		super();
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		startup();
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
		Server server = null;
		ServerProcess serverProcess = null;
		User user = null;
		try {
			server = new Server(serverId);
			serverProcess = AdministrationDatabaseContext.getServerProcessDatabase().retrieveForServerAndCodename(serverId, processCodename);
			user = new User(serverProcess.getUserId());
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(0);
		}

		/*	Init database object loader*/
		DatabaseObjectLoader.init(user.getId());

		/*	Create session environment*/
		try {
			CMServerSessionEnvironment.createInstance(server.getHostName());
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(0);
		}

		/*	Login*/
		CMServerSessionEnvironment sessionEnvironment = CMServerSessionEnvironment.getInstance();
		try {
			sessionEnvironment.login(user.getLogin(), "password");
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(0);
		}
		catch (LoginException le) {
			Log.errorException(le);
		}

		try {
			/*	Activate servant*/
			CMServerSessionEnvironment.getInstance().getCMServerServantManager().getCORBAServer().activateServant(new CMServerImpl(),
					processCodename);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
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

	protected synchronized void shutdown() {
		DatabaseConnection.closeConnection();
	}

}
