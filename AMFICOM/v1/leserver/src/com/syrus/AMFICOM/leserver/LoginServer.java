/*
 * $Id: LoginServer.java,v 1.1 2005/04/28 07:35:33 cvsadmin Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.loginserver;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/28 07:35:33 $
 * @author $Author: cvsadmin $
 * @module loginserver_v1
 */
public final class LoginServer extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "loginserver";

	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_PROCESS_CODENAME = "ProcessCodename";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	public static final String DB_LOGIN_NAME = "amficom";
	public static final String PROCESS_CODENAME = "LoginServer";
	public static final int TICK_TIME = 5;	//sec

	private static String codename;

	private boolean running;

	public LoginServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		startup();

		/*	Start main loop	*/
		final LoginServer loginServer = new LoginServer();
		loginServer.start();

		/*	Add shutdown hook	*/
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				loginServer.shutdown();
			}
		});
	}

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Retrieve info about process*/
		ServerProcess serverProcess = null;
		codename = ApplicationProperties.getString(KEY_PROCESS_CODENAME, PROCESS_CODENAME);
		try {
			serverProcess = AdministrationDatabaseContext.getServerProcessDatabase().retrieveForCodename(codename);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(1);
		}

		/*	Retrieve info about server, on which to run
		 * and user, on behave of which to run*/
		Server server = null;
//		User user = null;
		try {
			server = new Server(serverProcess.getServerId());
//			user = new User(serverProcess.getUserId());
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(1);
		}

		/*	Init session environment
		 * NOTE: No logging in*/
		try {
			SessionEnvironment.init(server.getHostName());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(1);
		}

		/*	Activate servant*/
		try {
			SessionEnvironment.getLoginServerServantManager().getCORBAServer().activateServant(new LoginServerImplementation(), codename);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(1);
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
			System.exit(1);
		}
	}

	public void run() {
		while (this.running) {
			// TODO Implement
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

	protected void shutdown() {
		this.running = false;

		try {
			SessionEnvironment.getLoginServerServantManager().getCORBAServer().deactivateServant(codename);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
		}

		DatabaseConnection.closeConnection();
	}

}
