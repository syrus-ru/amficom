/*
 * $Id: ClientMeasurementServer.java,v 1.33 2005/04/01 21:23:41 arseniy Exp $
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
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.33 $, $Date: 2005/04/01 21:23:41 $
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
	public static final String KEY_CMSERVER_ID = "CMServerID";
	public static final String KEY_SERVANT_NAME = "ServantName";
	public static final String KEY_MSERVER_SERVANT_NAME = "MServerServantName";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final int TICK_TIME = 10;	//sec
	public static final String CMSERVER_ID = "Server_1";
	public static final String SERVANT_NAME = "CMServer";
	public static final String MSERVER_SERVANT_NAME = "MServer";

// protected static MServer mServerRef;

	/* CORBA server */
	private static CORBAServer corbaServer;

	/*	CORBA reference to Measurement Server*/
	private static MServer mServerRef;
	private static Object lock = new Object();

	private boolean running;

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		startup();
	}

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Initialize object pools*/
		DatabaseContextSetup.initObjectPools();

		/*	Activation, specific for this application	*/
		activateSpecific();

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

	private static void activateSpecific() {
		/*	Retrieve information about server*/
		Identifier serverId = new Identifier(ApplicationProperties.getString(KEY_CMSERVER_ID, CMSERVER_ID));
		Server server = null;
		try {
			server = (Server) AdministrationStorableObjectPool.getStorableObject(serverId, true);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		/*	Activate session context*/
		SessionContext.init(new AccessIdentity(new Date(System.currentTimeMillis()), server.getDomainId(), server.getUserId(), null),
				server.getHostName());

		/*	Create CORBA server with servant(s)	*/
		activateCORBAServer();

		/*	Create reference to MServer*/
		activateMServerReference();
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

	private static void activateCORBAServer() {
		/* Create local CORBA server and activate servant */
		try {
			corbaServer = new CORBAServer(SessionContext.getServerHostName());
			String servantName = ApplicationProperties.getString(KEY_SERVANT_NAME, SERVANT_NAME);
			corbaServer.activateServant(new CMServerImpl(), servantName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void deactivateCORBAServer() {
		try {
			String servantName = ApplicationProperties.getString(KEY_SERVANT_NAME, SERVANT_NAME);
			corbaServer.deactivateServant(servantName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.err.println(e);
			System.exit(-1);
		}
	}

	protected static void activateMServerReference() {
		/*	Obtain reference to Measurement Server	*/
		String mServerServantName = ApplicationProperties.getString(KEY_MSERVER_SERVANT_NAME, MSERVER_SERVANT_NAME);
		try {
			mServerRef = MServerHelper.narrow(corbaServer.resolveReference(mServerServantName));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			Log.errorMessage("Cannot resolve Measurement Server '" + mServerServantName + "'");
			//@todo Generate event "Cannot resolve Measurement Server"
			mServerRef = null;
		}
	}

	protected static void resetMServerConnection() {
		activateMServerReference();
	}

	protected static MServer getVerifiedMServerReference() throws CommunicationException {
		synchronized (lock) {

			if (mServerRef == null)
				resetMServerConnection();
			else {
				try {
					mServerRef.ping((byte) 1);
				}
				catch (SystemException se) {
					resetMServerConnection();
				}
			}

			if (mServerRef != null)
				return mServerRef;
			throw new CommunicationException("Cannot establish connection with Measurement Server");

		}
	}

	public ClientMeasurementServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.running = true;
	}

	public void run() {
		while (this.running) {
			synchronized (lock) {
				if (mServerRef != null) {
					try {
						mServerRef.ping((byte) 0);
					}
					catch (SystemException se) {
						Log.errorException(se);
						resetMServerConnection();
					}
				}
				else
					resetMServerConnection();
			}

			try {				
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
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

		Log.debugMessage("ClientMeasurementServer.shutdown | serialize GeneralStorableObjectPool" , Log.DEBUGLEVEL09);
		GeneralStorableObjectPool.serializePool();
		Log.debugMessage("ClientMeasurementServer.shutdown | serialize MeasurementStorableObjectPool" , Log.DEBUGLEVEL09);
		AdministrationStorableObjectPool.serializePool();
		Log.debugMessage("ClientMeasurementServer.shutdown | serialize AdministrationStorableObjectPool" , Log.DEBUGLEVEL09);
		ConfigurationStorableObjectPool.serializePool();
		Log.debugMessage("ClientMeasurementServer.shutdown | serialize MeasurementStorableObjectPool" , Log.DEBUGLEVEL09);
		MeasurementStorableObjectPool.serializePool();

		DatabaseConnection.closeConnection();
	}

}
