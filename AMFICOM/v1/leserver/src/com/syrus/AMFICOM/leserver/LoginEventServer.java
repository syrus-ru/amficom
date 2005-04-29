/*
 * $Id: LoginEventServer.java,v 1.6 2005/04/29 12:15:31 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/29 12:15:31 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
public final class LoginEventServer {
	public static final String APPLICATION_NAME = "leserver";

	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_SERVER_ID = "ServerID";
	public static final String KEY_LOGIN_PROCESS_CODENAME = "LoginProcessCodename";
	public static final String KEY_EVENT_PROCESS_CODENAME = "EventProcessCodename";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	public static final String DB_LOGIN_NAME = "amficom";
	public static final String SERVER_ID = "Server_1";
	public static final String LOGIN_PROCESS_CODENAME = "LoginServer";
	public static final String EVENT_PROCESS_CODENAME = "EventServer";

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

		/*	Start Event Processor*/
		EventProcessor eventProcessor = new EventProcessor();
		eventProcessor.start();

		/*	Start Login Processor*/
		LoginProcessor loginProcessor = new LoginProcessor();
		loginProcessor.start();
	}

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Retrieve info about server*/
		Identifier serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		Server server = null;
		try {
			server = new Server(serverId);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(1);
		}

		/*	Retrieve info about processes*/
		ServerProcess loginServerProcess = null;
		ServerProcess eventServerProcess = null;
		loginProcessCodename = ApplicationProperties.getString(KEY_LOGIN_PROCESS_CODENAME, LOGIN_PROCESS_CODENAME);
		eventProcessCodename = ApplicationProperties.getString(KEY_EVENT_PROCESS_CODENAME, EVENT_PROCESS_CODENAME);
		ServerProcessDatabase serverProcessDatabase = AdministrationDatabaseContext.getServerProcessDatabase();
		try {
			loginServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, loginProcessCodename);
			eventServerProcess = serverProcessDatabase.retrieveForServerAndCodename(serverId, eventProcessCodename);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(1);
		}
		// TODO something with loginServerProcess and eventServerProcess
		if (loginServerProcess == null || eventServerProcess == null) {
			Log.errorMessage("Cannot find login server process or eventt server process");
			System.exit(1);
		}

		/*	Init session environment
		 * NOTE: No logging in*/
		try {
			LEServerSessionEnvironment.create(server.getHostName());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(1);
		}

		/*	Activate servant*/
		try {
			CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
			corbaServer.activateServant(new LoginServerImplementation(), loginProcessCodename);
			corbaServer.activateServant(new EventServerImplementation(), eventProcessCodename);
			corbaServer.printNamingContext();
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(1);
		}

		/*	Add shutdown hook*/
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown();
			}
		});
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
			System.exit(1);
		}
	}

	public void run() {
		while (this.running) {
			// TODO Implement
		}
	}

	protected static void shutdown() {
		try {
			CORBAServer corbaServer = LEServerSessionEnvironment.getInstance().getLEServerServantManager().getCORBAServer();
			corbaServer.deactivateServant(loginProcessCodename);
			corbaServer.deactivateServant(eventProcessCodename);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}

		DatabaseConnection.closeConnection();
	}

}
