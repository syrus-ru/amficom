/*
 * $Id: MeasurementServer.java,v 1.17 2004/12/22 12:22:44 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Date;
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
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.17 $, $Date: 2004/12/22 12:22:44 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MeasurementServer extends SleepButWorkThread {
	public static final String ID = "server_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final int TICK_TIME = 5;

	/*	Error codes for method processFall()	(abort tests, ...)*/
	public static final int FALL_CODE_RECEIVE_TESTS = 1;

	/*	Information about myself*/
	protected static Server iAm;

	private static MCMTestQueueMap mcmTestQueueMap;

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	References to MCMs*/
	protected static Map mcmRefs;	/*	Map <Identifier mcmId, com.syrus.AMFICOM.mcm.corba.MCM mcmRef>*/

	private boolean running;

	/*	Variables for method processFall()	(abort tests, ...)*/
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
		String dbLoginName = ApplicationProperties.getString("DBLoginName", DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
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
			Log.errorException(e);
		}
	}

	private static void deactivateCORBAServer() {
		try {
			corbaServer.deactivateServant(iAm.getId().toString());
		}
		catch (Exception e) {
			Log.errorException(e);
			System.err.println(e);
			System.exit(-1);
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

	protected static void activateMCMReferenceWithId(Identifier mcmId) {
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
			/*	Now Measurement Server can get new tests only from database
			 * (not through direct CORBA operation).
			 * Maybe in future remove this*/
			fillMCMTestQueueMap();

//			synchronized (mcmTestQueueMap) {
				for (Iterator it = mcmTestQueueMap.getMCMIdsIterator(); it.hasNext();) {
					mcmId = (Identifier)it.next();
					testQueue = mcmTestQueueMap.getQueue(mcmId);
					mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)mcmRefs.get(mcmId);
					if (mcmRef != null) {
						testsT = createTransferables(testQueue);
						if (testsT != null) {
							try {
								Log.debugMessage(testsT.length + " tests to send to MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
								mcmRef.receiveTests(testsT);
								updateTestsStatus(testQueue, TestStatus.TEST_STATUS_SCHEDULED);
								testQueue.clear();
								super.clearFalls();
							}
							catch (org.omg.CORBA.SystemException se) {
								Log.errorException(se);
								super.fallCode = FALL_CODE_RECEIVE_TESTS;
								this.mcmIdToAbortTests = mcmId;
								this.testsToAbort = testQueue;
								activateMCMReferenceWithId(mcmId);
								super.sleepCauseOfFall();
							}
							catch (AMFICOMRemoteException are) {
								Log.errorMessage("Cannot transmit tests: " + are.message + "; sleeping cause of fall");
								super.fallCode = FALL_CODE_RECEIVE_TESTS;
								this.mcmIdToAbortTests = mcmId;
								this.testsToAbort = testQueue;
								super.sleepCauseOfFall();
							}
						}
					}
					else
						activateMCMReferenceWithId(mcmId);
				}
//			}	//synchronized

			System.out.println(new Date(System.currentTimeMillis()));
			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}
	
	private static void fillMCMTestQueueMap() {
		TestDatabase testDatabase = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
		Identifier mcmId;
		List tests = null;
//		synchronized (mcmTestQueueMap) {
			for (Iterator it = mcmTestQueueMap.getMCMIdsIterator(); it.hasNext();) {
				mcmId = (Identifier)it.next();
				try {
					tests = testDatabase.retrieveTestsForMCM(mcmId, TestStatus.TEST_STATUS_NEW);
				}
				catch (RetrieveObjectException roe) {
					Log.errorException(roe);
				}
				if (tests != null && !tests.isEmpty()) {
					Log.debugMessage("Adding " + tests.size() + " tests for MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
					mcmTestQueueMap.addTests(mcmId, tests);
				}
			}
//		}
	}

	private static Test_Transferable[] createTransferables(List testQueue) {
		Test_Transferable[] testsT = null;
		if (! testQueue.isEmpty()) {
			testsT = new Test_Transferable[testQueue.size()];
			int i = 0;
//			synchronized (testQueue) {
				for (Iterator it = testQueue.iterator(); it.hasNext();)
					testsT[i++] = (Test_Transferable)((Test)it.next()).getTransferable();
//			}
		}
		return testsT;
	}

	private static void updateTestsStatus(List tests, TestStatus status) {
//		synchronized (tests) {
			try {
				for (Iterator it = tests.iterator(); it.hasNext();)
					((Test)it.next()).updateStatus(status, iAm.getUserId());
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
			}
//		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_RECEIVE_TESTS:
				abortTests();
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}

	private void abortTests() {
		if (this.testsToAbort != null && ! this.testsToAbort.isEmpty()) {
			Test test;
//			synchronized (this.testsToAbort) {
				for (Iterator it = this.testsToAbort.iterator(); it.hasNext();) {
					test = (Test)it.next();
					try {
						test.updateStatus(TestStatus.TEST_STATUS_ABORTED, iAm.getUserId());
					}
					catch (UpdateObjectException uoe) {
						Log.errorException(uoe);
					}
					Log.debugMessage("Test '" + test.getId() + "' set ABORTED", Log.DEBUGLEVEL08);
				}
//			}
			mcmTestQueueMap.remove(this.mcmIdToAbortTests, this.testsToAbort);
			this.mcmIdToAbortTests = null;
			this.testsToAbort = null;
		}
		else
			Log.errorMessage("abortTests | list is NULL or empty");
	}

	protected void shutdown() {
		this.running = false;
		deactivateCORBAServer();
		DatabaseConnection.closeConnection();
	}

	private static class MCMTestQueueMap {
		private Map queueMap;	//Map <Identifier mcmId, List <Test> >

		MCMTestQueueMap(List mcmIds) {
			this.queueMap = Collections.synchronizedMap(new HashMap(mcmIds.size()));
//			synchronized (mcmIds) {
				for (Iterator iterator = mcmIds.iterator(); iterator.hasNext();)
					this.queueMap.put((Identifier)iterator.next(), Collections.synchronizedList(new LinkedList()));
//			}
		}

		Iterator getMCMIdsIterator() {
			return this.queueMap.keySet().iterator();
		}

		List getQueue(Identifier mcmId) {
			return (List)this.queueMap.get(mcmId);
		}
//
//		List removeQueue(Identifier mcmId) {
//			return (List)this.queueMap.put(mcmId, Collections.synchronizedList(new LinkedList()));
//		}

		void createQueue(Identifier mcmId) {
			List queue = new ArrayList();
			this.queueMap.put(mcmId, queue);
		}

		void addTests(Identifier mcmId, List tests) {
			List queue = (List)this.queueMap.get(mcmId);
			if (queue != null) {
				Test test;
//				synchronized (tests) {
					for (Iterator it = tests.iterator(); it.hasNext();) {
						test = (Test)it.next();
						if (! queue.contains(test)) {
							try {
								MeasurementStorableObjectPool.putStorableObject(test);
							}
							catch (IllegalObjectEntityException ioee) {
								Log.errorException(ioee);
							}
							queue.add(test);
							Log.debugMessage("Added test '" + test.getId() + "' for MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
						}
						else
							Log.debugMessage("Test '" + test.getId() + "'  already in map for MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
					}
//				}
			}
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
