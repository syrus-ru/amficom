/*
 * $Id: MeasurementServer.java,v 1.4 2004/08/04 17:32:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/04 17:32:55 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MeasurementServer extends SleepButWorkThread {
	public static final String ID = "mserver_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final int TICK_TIME = 5;

	/*	Information about myself*/
	protected static Server iAm;

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	References to MCMs*/
	private static Map mcmRefs;	/*	Map <Identifier mcmId, com.syrus.AMFICOM.mcm.corba.MCM mcmRef>*/

	private boolean running;

	public MeasurementServer() {
		super(ApplicationProperties.getInt("TickTime", TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", MAX_FALLS));
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init("mserver");

		/*	Establish connection with database	*/
		establishDatabaseConnection();

//		/*	Initialize object drivers
//		 * 	for work with database*/
//		DatabaseContextSetup.initDatabaseContext();
//
//		/*	Load object types*/
//		DatabaseContextSetup.loadObjectTypes();
//
//		/*	Retrieve information abot myself*/
//		try {
//			iAm = new Server(new Identifier(ApplicationProperties.getString("ID", ID)));
//		}
//		catch (Exception e) {
//			Log.errorException(e);
//			System.exit(-1);
//		}

		/*	Create CORBA server with servant(s)	*/
		activateCORBAServer();

		/*	Create map of references to MCMs*/
		activateMCMReferences();

		/*	Start main loop	*/
		MeasurementServer measurementServer = new MeasurementServer();
		measurementServer.start();
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString("DBSID", DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT)*1000;
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void activateCORBAServer() {
		try {
			corbaServer = new CORBAServer();
			//corbaServer.activateServant(new MServerImplementation(), iAm.getId().toString());
			corbaServer.activateServant(new MServerImplementation(), "server_1");
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(-1);
		}
		catch (Exception e) {
			Log.errorMessage("ERROR");
			Log.errorException(e);
		}
	}
	
	private static void activateMCMReferences() {
//		List mcms = iAm.getMCMs();
//		mcmRefs = new Hashtable(mcms.size());
//		Identifier mcmId;
//		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
//		for (Iterator iterator = mcms.iterator(); iterator.hasNext();) {
//			mcmId = ((MCM)mcms.iterator()).getId();
//			try {
//				mcmRef = com.syrus.AMFICOM.mcm.corba.MCMHelper.narrow(corbaServer.resolveReference(mcmId.toString()));
//			}
//			catch (CommunicationException ce) {
//				Log.errorException(ce);
//				mcmRef = null;
//			}
//			if (mcmRef != null)
//				mcmRefs.put(mcmId, mcmRef);
//		}

		mcmRefs = new Hashtable(1);
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		try {
			mcmRef = com.syrus.AMFICOM.mcm.corba.MCMHelper.narrow(corbaServer.resolveReference("mcm_1"));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			mcmRef = null;
		}
		if (mcmRef != null)
			mcmRefs.put("mcm_1", mcmRef);
		else
			System.out.println("ignoring null");
	}

	public void run() {
		while (this.running) {
			System.out.println(System.currentTimeMillis());
			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}
	
	protected void shutdown() {
		this.running = false;
		DatabaseConnection.closeConnection();
	}
}
