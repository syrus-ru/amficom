/*
 * $Id: AdministrationResourceServer.java,v 1.1 2005/01/17 16:34:05 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver;

import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/17 16:34:05 $
 * @author $Author: max $
 * @module arserver_v1
 */
public class AdministrationResourceServer extends SleepButWorkThread {
	
	public static final String	APPLICATION_NAME			= "arserver";
	public static final String	KEY_DB_HOST_NAME			= "DBHostName";
	public static final String	KEY_DB_SID					= "DBSID";
	public static final String	KEY_DB_CONNECTION_TIMEOUT	= "DBConnectionTimeout";
	public static final String	KEY_DB_LOGIN_NAME			= "DBLoginName";
	public static final String	KEY_TICK_TIME				= "TickTime";
	public static final String	KEY_MAX_FALLS				= "MaxFalls";
	
	public static final String	DB_SID						= "amficom";
	public static final int		DB_CONNECTION_TIMEOUT		= 120;
	public static final String	DB_LOGIN_NAME				= "amficom";
	public static final int		TICK_TIME					= 5;

	/* CORBA server */
	private static CORBAServer	corbaServer;

	private boolean				running;

	public AdministrationResourceServer() {
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
		
        /* Start main loop */
		final AdministrationResourceServer server = new AdministrationResourceServer();
		Log.debugMessage("AdministrationResourceServer.startup | Ready.", Log.DEBUGLEVEL03);
		server.start();

		/* Add shutdown hook */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				server.shutdown();
			}
		});
	}
	
	private static void activateCORBAServer() {
		/* Create local CORBA server end activate servant */
		try {
			corbaServer = new CORBAServer();
			corbaServer.activateServant(new ARServerImpl(), "CMServer");
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(-1);
		}
		catch (Exception e) {
			Log.errorException(e);
		}
	}
	
	private static void deactivateCORBAServer() {
		try {
			corbaServer.deactivateServant("ARServer");
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

		Log.debugMessage("AdministrationResourceServer.shutdown | serialize ResourceStorableObjectPool" , Log.DEBUGLEVEL03);
		ResourceStorableObjectPool.serializePool();		
	}
	
	public void run() {
		while (this.running) {
			try {				
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}
}
