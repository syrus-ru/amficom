/*
 * $Id: MeasurementServer.java,v 1.29 2005/04/05 12:18:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.29 $, $Date: 2005/04/05 12:18:50 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MeasurementServer extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "mserver";

	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";
	public static final String KEY_MSERVER_ID = "MServerID";
	public static final String KEY_SERVANT_NAME = "ServantName";
	public static final String KEY_MCM_CHECK_TIMEOUT = "MCMCheckTimeout";

	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final int TICK_TIME = 5;	//sec
	public static final String MSERVER_ID = "Server_1";
	public static final String SERVANT_NAME = "MServer";
	public static final int MCM_CHECK_TIMEOUT = 10;		//min

	/*	Error codes for method processFall()	(abort tests, ...)*/
	public static final int FALL_CODE_RECEIVE_TESTS = 1;

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	MCM connection manager*/
	protected static MCMConnectionManager mcmConnectionManager;

	/*	Identifier of server*/
	private static Identifier serverId;

	/*	Map of tests to transmit to MCMs	*/
	private static Map mcmTestQueueMap;	// Map <Identifier mcmId, Set <Test> testQueue >

	private boolean running;

	/*	Variables for method processFall()	(abort tests, ...)*/
	/*	Identifiers of MCMs on which cannot transmit tests	*/
	private static Set mcmIdsToAbortTests;	//Set <Identifier mcmId>


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

	private static void activateSpecific() {
		/*	Retrieve information about server*/
		serverId = new Identifier(ApplicationProperties.getString(KEY_MSERVER_ID, MSERVER_ID));
		Server server = null;
		Set mcmIds = null;
		try {
			server = (Server) AdministrationStorableObjectPool.getStorableObject(serverId, true);
			mcmIds = server.retrieveMCMIds();
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

		/*	Create map of test queues*/
		mcmTestQueueMap = Collections.synchronizedMap(new HashMap(mcmIds.size()));
		for (Iterator it = mcmIds.iterator(); it.hasNext();)
			mcmTestQueueMap.put(it.next(), Collections.synchronizedSet(new HashSet()));

		/*	Activate MCM connection manager*/
		long mcmCheckTimeout = ApplicationProperties.getInt(KEY_MCM_CHECK_TIMEOUT, MCM_CHECK_TIMEOUT) * 60 * 1000;
		mcmConnectionManager = new MCMConnectionManager(corbaServer, mcmCheckTimeout);
		mcmConnectionManager.start();

		/*	Create collection of MCM identifiers for aborting tests*/
		mcmIdsToAbortTests = Collections.synchronizedSet(new HashSet());
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
			System.exit(-1);
		}
	}

	private static void activateCORBAServer() {
		try {
			corbaServer = new CORBAServer(SessionContext.getServerHostName());
			String servantName = ApplicationProperties.getString(KEY_SERVANT_NAME, SERVANT_NAME);
			corbaServer.activateServant(new MServerImplementation(), servantName);
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

	public MeasurementServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.running = true;
	}

	public void run() {
		Identifier mcmId;
		Set testQueue;
		Test_Transferable[] testsT;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		while (this.running) {
			/*	Now Measurement Server can get new tests only from database
			 * (not through direct CORBA operation).
			 * Maybe in future change such behaviour*/
			try {
				fillMCMTestQueueMap();
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			synchronized (mcmTestQueueMap) {
				for (Iterator it = mcmTestQueueMap.keySet().iterator(); it.hasNext();) {
					mcmId = (Identifier) it.next();
					testQueue = (Set) mcmTestQueueMap.get(mcmId);
					if (!testQueue.isEmpty()) {
						try {
							mcmRef = mcmConnectionManager.getVerifiedMCMReference(mcmId);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
							continue;
						}

						updateTestsStatus(testQueue, TestStatus.TEST_STATUS_SCHEDULED);
						testsT = createTransferables(testQueue);
						try {
							Log.debugMessage(testsT.length + " tests to send to MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
							mcmRef.receiveTests(testsT);
							testQueue.clear();
							super.clearFalls();
						}
						catch (AMFICOMRemoteException are) {
							Log.errorMessage("Cannot transmit tests: " + are.message + "; sleeping cause of fall");
							super.fallCode = FALL_CODE_RECEIVE_TESTS;
							mcmIdsToAbortTests.add(mcmId);
							super.sleepCauseOfFall();
						}
						catch (Throwable throwable) {
							Log.errorException(throwable);
						}

					}	//if (!testQueue.isEmpty())
				}	//for (Iterator it = mcmTestQueueMap.keySet().iterator(); it.hasNext();)
			}	//synchronized (mcmTestQueueMap)

			System.out.println(new Date(System.currentTimeMillis()));
			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}

	private static void fillMCMTestQueueMap() throws ApplicationException {
		LinkedIdsCondition lic = new LinkedIdsCondition(mcmTestQueueMap.keySet(), ObjectEntities.TEST_ENTITY_CODE);
		TypicalCondition tc = new TypicalCondition(TestStatus._TEST_STATUS_NEW,
				0,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_ENTITY_CODE,
				TestWrapper.COLUMN_STATUS);
		CompoundCondition cc = null;
		try {
			cc = new CompoundCondition(lic, CompoundConditionSort.AND, tc);
		}
		catch (CreateObjectException coe) {
			//Never
			Log.errorException(coe);
		}

		Identifier mcmId;
		Set tests;
		Test test;

		Set addedTestIds = new HashSet();
		synchronized (mcmTestQueueMap) {
			for (Iterator it = mcmTestQueueMap.keySet().iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				tests = (Set) mcmTestQueueMap.get(mcmId);
				synchronized (tests) {
					for (Iterator it1 = tests.iterator(); it1.hasNext();) {
						test = (Test) it1.next();
						addedTestIds.add(test.getId());
					}
				}
			}
		}

		tests = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(addedTestIds, cc, true);

		Set testQueue;
		for (Iterator it = tests.iterator(); it.hasNext();) {
			test = (Test) it.next();
			mcmId = test.getMCMId();

			testQueue = (Set) mcmTestQueueMap.get(mcmId);
			if (testQueue != null) {
				if (!testQueue.contains(test)) {
					Log.debugMessage("Adding test '" + test.getId() + "' for MCM '" + mcmId + "'", Log.DEBUGLEVEL04);
					testQueue.add(test);
				}
				else
					Log.errorMessage("Test '" + test.getId() + "' already added to queue");
			}
			else
				Log.errorMessage("Test queue for mcm id '" + mcmId + "' not found");
		}
	}

	private static Test_Transferable[] createTransferables(Set testQueue) {
		assert !testQueue.isEmpty() : "Test queue is NULL";

		Test_Transferable[] testsT = new Test_Transferable[testQueue.size()];
		int i = 0;
		Test test;
		synchronized (testQueue) {
			for (Iterator it = testQueue.iterator(); it.hasNext(); i++) {
				test = (Test) it.next();
				testsT[i] = (Test_Transferable) test.getTransferable();
			}
		}

		return testsT;
	}

	private static void updateTestsStatus(Set testQueue, TestStatus status) {
		Test test;
		synchronized (testQueue) {
			for (Iterator it = testQueue.iterator(); it.hasNext();) {
				test = (Test) it.next();
				if (test.getStatus().value() != status.value()) {
					test.setStatus(status);
					try {
						MeasurementStorableObjectPool.putStorableObject(test);
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}

		try {
			MeasurementStorableObjectPool.flush(true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
		
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
		if (! mcmIdsToAbortTests.isEmpty()) {
			Identifier mcmId;
			Set testQueue;
			synchronized (mcmIdsToAbortTests) {
				for (Iterator it = mcmIdsToAbortTests.iterator(); it.hasNext();) {
					mcmId = (Identifier) it.next();
					testQueue = (Set) mcmTestQueueMap.get(mcmId);

					updateTestsStatus(testQueue, TestStatus.TEST_STATUS_ABORTED);

					testQueue.clear();
				}
			}

			mcmIdsToAbortTests.clear();

			MeasurementStorableObjectPool.truncateObjectPool(ObjectEntities.TEST_ENTITY_CODE);
		}
		else
			Log.errorMessage("abortTests | Collection is NULL or empty");
	}



	protected static void updateMCMs() {
		try {
			Server server = (Server) AdministrationStorableObjectPool.getStorableObject(serverId, true);
			Set mcmIds = server.retrieveMCMIds();

			Identifier mcmId;

			synchronized (mcmTestQueueMap) {
				for (Iterator it = mcmTestQueueMap.keySet().iterator(); it.hasNext();) {
					mcmId = (Identifier) it.next();
					if (!mcmIds.contains(mcmId)) {
						it.remove();
					}
				}
			}

			for (Iterator it = mcmIds.iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				if (!mcmTestQueueMap.containsKey(mcmId)) {
					mcmTestQueueMap.put(mcmId, Collections.synchronizedSet(new HashSet()));
				}
			}

		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	protected static Set getMCMIds() {
		return Collections.unmodifiableSet(mcmTestQueueMap.keySet());
	}




	protected void shutdown() {
		this.running = false;

		deactivateCORBAServer();

		Log.debugMessage("MeasurementServer.shutdown | serialize GeneralStorableObjectPool" , Log.DEBUGLEVEL09);
		GeneralStorableObjectPool.serializePool();
		Log.debugMessage("MeasurementServer.shutdown | serialize MeasurementStorableObjectPool" , Log.DEBUGLEVEL09);
		AdministrationStorableObjectPool.serializePool();
		Log.debugMessage("MeasurementServer.shutdown | serialize AdministrationStorableObjectPool" , Log.DEBUGLEVEL09);
		ConfigurationStorableObjectPool.serializePool();
		Log.debugMessage("MeasurementServer.shutdown | serialize MeasurementStorableObjectPool" , Log.DEBUGLEVEL09);
		MeasurementStorableObjectPool.serializePool();

		DatabaseConnection.closeConnection();
	}
}
