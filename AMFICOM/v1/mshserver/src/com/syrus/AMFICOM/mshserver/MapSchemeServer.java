/*
 * $Id: MapSchemeServer.java,v 1.1 2004/12/09 10:19:20 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mshserver;

import java.net.InetAddress;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.mshserver.corba.MSHServer;
import com.syrus.AMFICOM.mshserver.corba.MSHServerPOATie;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.corba.JavaSoftORBUtil;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/09 10:19:20 $
 * @author $Author: cvsadmin $
 * @module cmserver_v1
 */
public class MapSchemeServer extends SleepButWorkThread {

	public static final String	APPLICATION_NAME			= "mshserver";

	public static final String	KEY_DB_HOST_NAME			= "DBHostName";

	public static final String	KEY_DB_SID					= "DBSID";

	public static final String	KEY_DB_CONNECTION_TIMEOUT	= "DBConnectionTimeout";

	public static final String	KEY_DB_LOGIN_NAME			= "DBLoginName";

	public static final String	KEY_TICK_TIME				= "TickTime";

	public static final String	KEY_MAX_FALLS				= "MaxFalls";

	public static final String	KEY_MSERVER_ID				= "MServerID";

	public static final String	MSERVER_ID					= "Server_1";

	public static final String	DB_SID						= "amficom";

	public static final int		DB_CONNECTION_TIMEOUT		= 120;

	public static final String	DB_LOGIN_NAME				= "amficom";

	public static final int		TICK_TIME					= 5;

	/* CORBA server */
	private static CORBAServer	corbaServer;

	private boolean				running;

	public MapSchemeServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		startup();
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
		/* Establish connection with database */
		establishDatabaseConnection();

		/* Create CORBA server with servant(s) */
		activateCORBAServer();

		DatabaseContextSetup.initDatabaseContext();
		DatabaseContextSetup.initObjectPools();
		/* Start main loop */
		final MapSchemeServer clientMeasurementServer = new MapSchemeServer();
		Log.debugMessage("MapSchemeServer.startup | Ready.", Log.DEBUGLEVEL03);
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
			ORB orb = JavaSoftORBUtil.getInstance().getORB();

			POA rootPOA = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));

			final String hostName = InetAddress.getLocalHost()
					.getCanonicalHostName().replaceAll("\\.", "_");

			NameComponent childPath[] = rootNamingCtx.to_name(hostName);

			NamingContextExt childNamingCtx;
			try {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx
						.bind_new_context(childPath));
			} catch (AlreadyBound ab) {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx
						.resolve_str(hostName));
			}

			MSHServer server = (new MSHServerPOATie(new MSHServerImpl(), rootPOA))
					._this(orb);
			NameComponent serverPath[] = rootNamingCtx.to_name("MSHServer");
			childNamingCtx.rebind(serverPath, server);
			corbaServer = new CORBAServer();

		} catch (Exception e) {
			DatabaseConnection.closeConnection();
			Log.errorException(e);
			System.err.println(e);
			System.exit(-1);
		}
	}

	private static void stopCORBAServer() {
		try {
			ORB orb = JavaSoftORBUtil.getInstance().getORB();

			POA rootPOA = POAHelper.narrow(orb
					.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));

			final String hostName = InetAddress.getLocalHost()
					.getCanonicalHostName().replaceAll("\\.", "_");

			NameComponent childPath[] = rootNamingCtx.to_name(hostName);

			NamingContextExt childNamingCtx;
			try {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx
						.bind_new_context(childPath));
			} catch (AlreadyBound ab) {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx
						.resolve_str(hostName));
			}

			NameComponent serverPath[] = rootNamingCtx.to_name("MSHServer");

			childNamingCtx.unbind(serverPath);			

		} catch (Exception e) {
			Log.errorException(e);
			System.err.println(e);
			System.exit(-1);
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
		case FALL_CODE_NO_ERROR:
			break;
		default:
			Log.errorMessage("processError | Unknown error code: "
					+ super.fallCode);
		}
		super.clearFalls();
	}

	protected synchronized void shutdown() {/* !! Need synchronization */
		this.running = false;
		Log.debugMessage("MapSchemeServer.shutdown | serialize ConfigurationStorableObjectPool" , Log.DEBUGLEVEL03);
		ConfigurationStorableObjectPool.serializePool();
		Log.debugMessage("MapSchemeServer.shutdown | serialize MeasurementStorableObjectPool" , Log.DEBUGLEVEL03);
		MapStorableObjectPool.serializePool();
	}

	public void run() {
		while (this.running) {
			try {				
				sleep(super.initialTimeToSleep);
			} catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
		stopCORBAServer();
	}

}
