/*
 * $Id: ClientMeasurementServer.java,v 1.31 2005/03/31 16:05:18 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.Date;

import org.omg.CORBA.SystemException;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.31 $, $Date: 2005/03/31 16:05:18 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public class ClientMeasurementServer extends SleepButWorkThread {

	public static final String APPLICATION_NAME = "cmserver";

	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";
	public static final String KEY_MSERVER_ID = "MServerID";
	public static final String KEY_MCM_CHECK_TIMEOUT = "MCMCheckTimeout";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final int TICK_TIME = 5;	//sec
	public static final String MSERVER_ID = "Server_1";
	public static final int MCM_CHECK_TIMEOUT = 10;		//min

// protected static MServer mServerRef;

	/* CORBA server */
	private static CORBAServer corbaServer;

	protected static MCMConnectionManager mcmConnectionManager;
//	/*	References to MCMs*/
//	protected static Map mcmRefs;	/*	Map <Identifier mcmId, com.syrus.AMFICOM.mcm.corba.MCM mcmRef>*/

	private boolean running;

	public ClientMeasurementServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		startup();
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
		/* Establish connection with database */
		establishDatabaseConnection();

		/* Create CORBA server with servant(s) */
		activateCORBAServer();

		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();


		/*	Activation*/
		activate();

		/* Start main loop */
		final ClientMeasurementServer clientMeasurementServer = new ClientMeasurementServer();
		Log.debugMessage("ClientMeasurementServer.startup | Ready.", Log.DEBUGLEVEL03);
		clientMeasurementServer.start();

		/* Add shutdown hook */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				clientMeasurementServer.shutdown();
			}
		});
	}

	private static void activateCORBAServer() {
		/* Create local CORBA server end activate servant */
		try {
			corbaServer = new CORBAServer();
			corbaServer.activateServant(new CMServerImpl(), "CMServer");
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(-1);
		}
		catch (SystemException e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void activate() {
		/*	Retrieve information about server*/
		Identifier serverId = new Identifier(ApplicationProperties.getString(KEY_MSERVER_ID, MSERVER_ID));
		Server server = null;
		try {
			server = (Server) AdministrationStorableObjectPool.getStorableObject(serverId, true);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		/*	Activate session context*/
		SessionContext.init(new AccessIdentity(new Date(System.currentTimeMillis()),
				server.getDomainId(),
				server.getUserId(),
				null));

		/*	Activate MCM connection manager*/
		long mcmCheckTimeout = ApplicationProperties.getInt(KEY_MCM_CHECK_TIMEOUT, MCM_CHECK_TIMEOUT) * 60 * 1000;
		mcmConnectionManager = new MCMConnectionManager(corbaServer, serverId, mcmCheckTimeout);
		mcmConnectionManager.start();
	}

	private static void deactivateCORBAServer() {
		try {
			corbaServer.deactivateServant("CMServer");
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.err.println(ce);
			System.exit(-1);
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
		case FALL_CODE_NO_ERROR:
			break;
		default:
			Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}

	protected synchronized void shutdown() {/* !! Need synchronization */
		this.running = false;

		deactivateCORBAServer();

		Log.debugMessage("ClientMeasurementServer.shutdown | serialize ConfigurationStorableObjectPool" , Log.DEBUGLEVEL03);
		ConfigurationStorableObjectPool.serializePool();
		Log.debugMessage("ClientMeasurementServer.shutdown | serialize MeasurementStorableObjectPool" , Log.DEBUGLEVEL03);
		MeasurementStorableObjectPool.serializePool();
		
	}

	public void run() {
		while (this.running) {
//			if (mServerRef == null)
//				resetMServerConnection();
			try {				
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}
		
}
