/*
 * $Id: MeasurementServer.java,v 1.9 2004/08/13 17:44:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.Server;
//import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.9 $, $Date: 2004/08/13 17:44:27 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MeasurementServer extends SleepButWorkThread {
	public static final String ID = "server_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final int TICK_TIME = 5;
	public static final int ERROR_CODE_TRANSMIT_TESTS = 1;

	/*	Information about myself*/
	protected static Server iAm;

	private static MCMTestQueueMap mcmTestQueueMap;

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	References to MCMs*/
	private static Map mcmRefs;	/*	Map <Identifier mcmId, com.syrus.AMFICOM.mcm.corba.MCM mcmRef>*/

	private boolean running;

	/*	Variables for method processError()	(abort tests, ...)*/
	private int errorCode;
	private Identifier mcmIdToAbortTests;
	private List testsToAbort;

	public MeasurementServer() {
		super(ApplicationProperties.getInt("TickTime", TICK_TIME) * 1000, ApplicationProperties.getInt("MaxFalls", MAX_FALLS));
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init("mserver");

		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Initialize object pools*/
		DatabaseContextSetup.initObjectPools();

		/*	Retrieve information abot myself*/
		try {
			iAm = new Server(new Identifier(ApplicationProperties.getString("ID", ID)));
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		/*	Create CORBA server with servant(s)	*/
		activateCORBAServer();

		/*	Create map of references to MCMs	*/
		activateMCMQueueMapAndReferences();

		/*	Start main loop	*/
		final MeasurementServer measurementServer = new MeasurementServer();
		measurementServer.start();

		/*	Add shutdown hook	*/
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				measurementServer.shutdown();
			}
		});
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
			corbaServer.activateServant(new MServerImplementation(), iAm.getId().toString());
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
	
	private static void activateMCMQueueMapAndReferences() {
		List mcmIds = null;
		try {
			mcmIds = iAm.retrieveMCMIds();
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		mcmTestQueueMap = new MCMTestQueueMap(mcmIds);

		mcmRefs = new Hashtable(mcmIds.size());
		for (Iterator iterator = mcmIds.iterator(); iterator.hasNext();) {
			activateMCMReferenceWithId((Identifier)iterator.next());
		}
	}

	private static void activateMCMReferenceWithId(Identifier mcmId) {
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		try {
			mcmRef = com.syrus.AMFICOM.mcm.corba.MCMHelper.narrow(corbaServer.resolveReference(mcmId.toString()));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			mcmRef = null;
		}
		if (mcmRef != null)
			mcmRefs.put(mcmId, mcmRef);
	}

	public void run() {
		Identifier mcmId;
		List testQueue;
		Test_Transferable[] testsT;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		while (this.running) {
			for (Iterator it = mcmTestQueueMap.getMCMIdsIterator(); it.hasNext();) {
				mcmId = (Identifier)it.next();
				testQueue = mcmTestQueueMap.getQueue(mcmId);
				testsT = createTransferables(testQueue);
				if (testsT != null) {
					mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)mcmRefs.get(mcmId);
					if (mcmRef != null) {
						try {
							mcmRef.transmitTests(testsT);
							updateTestsStatus(testQueue, TestStatus.TEST_STATUS_SCHEDULED);
							super.clearFalls();
						}
						catch (org.omg.CORBA.SystemException se) {
							Log.errorException(se);
							activateMCMReferenceWithId(mcmId);
						}
						catch (AMFICOMRemoteException are) {
							Log.errorException(are);
							this.mcmIdToAbortTests = mcmId;
							this.testsToAbort = testQueue;
							super.sleepCauseOfFall();
						}
					}
					else {
						activateMCMReferenceWithId(mcmId);
					}
				}
			}


			System.out.println(System.currentTimeMillis());
			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}
	
	private static void fillMCMTestQueueMap() {
		Identifier mcmId;
		List tests = null;
		for (Iterator it = mcmTestQueueMap.getMCMIdsIterator(); it.hasNext();) {
			mcmId = (Identifier)it.next();
			try {
				tests = TestDatabase.retrieveTestsForMCM(mcmId, TestStatus.TEST_STATUS_NEW);
			}
			catch (RetrieveObjectException roe) {
				Log.errorException(roe);
			}
			if (tests != null && !tests.isEmpty()) {
				mcmTestQueueMap.addTests(mcmId, tests);
			}
		}
	}

	private static Test_Transferable[] createTransferables(List testQueue) {
		Test_Transferable[] testsT = null;
		if (! testQueue.isEmpty()) {
			testsT = new Test_Transferable[testQueue.size()];
			int i = 0;
			for (Iterator it = testQueue.iterator(); it.hasNext();)
				testsT[i++] = (Test_Transferable)((Test)it.next()).getTransferable();
		}
		return testsT;
	}
	
	private static void updateTestsStatus(List tests, TestStatus status) {
		for (Iterator it = tests.iterator(); it.hasNext();)
			((Test)it.next()).setStatus(status);
	}
	
	protected void shutdown() {
		this.running = false;
		DatabaseConnection.closeConnection();
	}
	
	protected void processError() {
		switch (this.errorCode) {
			case ERROR_CODE_TRANSMIT_TESTS:
				abortTests();
				break;
			default:
				Log.errorMessage("Unknown error code: " + this.errorCode);
		}
	}
	
	private void abortTests() {
		if (! this.testsToAbort.isEmpty()) {
			mcmTestQueueMap.remove(this.mcmIdToAbortTests, this.testsToAbort);
			Test test;
			for (Iterator it = this.testsToAbort.iterator(); it.hasNext();) {
				test = (Test)it.next();
				test.setStatus(TestStatus.TEST_STATUS_ABORTED);
			}
			this.mcmIdToAbortTests = null;
			this.testsToAbort = null;
		}
		else
			Log.errorMessage("abortTests | list is NULL");
	}
	
	private static class MCMTestQueueMap {
		private Map queueMap;	//Map <Identifier mcmId, List <Test> >

		MCMTestQueueMap(List mcmIds) {
			this.queueMap = Collections.synchronizedMap(new HashMap(mcmIds.size()));
			for (Iterator iterator = mcmIds.iterator(); iterator.hasNext();)
				this.queueMap.put((Identifier)iterator.next(), Collections.synchronizedList(new LinkedList()));
		}

		Iterator getMCMIdsIterator() {
			return this.queueMap.keySet().iterator();
		}

		List getQueue(Identifier mcmId) {
			return (List)this.queueMap.get(mcmId);
		}

		void createQueue(Identifier mcmId) {
			List queue = new ArrayList();
			this.queueMap.put(mcmId, queue);
		}

		void addTests(Identifier mcmId, List tests) {
			List queue = (List)this.queueMap.get(mcmId);
			if (queue != null)
				queue.addAll(tests);
			else
				Log.errorMessage("Test queue for mcm id '" + mcmId + "' not found");
		}
		
		void remove(Identifier mcmId, Test test) {
			List queue = (List)this.queueMap.get(mcmId);
			if (queue != null)
				queue.remove(test);
			else
				Log.errorMessage("Test queue for mcm id '" + mcmId + "' not found");
		}

		void remove(Identifier mcmId, List tests) {
			List queue = (List)this.queueMap.get(mcmId);
			if (queue != null)
				queue.removeAll(tests);
			else
				Log.errorMessage("Test queue for mcm id '" + mcmId + "' not found");
		}
	}
}
