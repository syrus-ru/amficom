/*
 * $Id: MeasurementServer.java,v 1.24 2005/03/18 18:23:17 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SessionContext;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
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
 * @version $Revision: 1.24 $, $Date: 2005/03/18 18:23:17 $
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

	//private static MCMTestQueueMap mcmTestQueueMap;
	private static Map mcmTestQueueMap;

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	References to MCMs*/
	protected static Map mcmRefs;	/*	Map <Identifier mcmId, com.syrus.AMFICOM.mcm.corba.MCM mcmRef>*/

	private boolean running;

	/*	Variables for method processFall()	(abort tests, ...)*/
	private Map abortTestsMap;
//	private Identifier mcmIdToAbortTests;
//	private Collection testsToAbort;

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

		/*	Activate session context*/
		SessionContext.init(new AccessIdentity(new Date(System.currentTimeMillis()),
				iAm.getDomainId(),
				iAm.getUserId(),
				null));


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
			corbaServer = new CORBAServer(iAm.getHostName());
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
		LinkedIdsCondition lic = new LinkedIdsCondition(Collections.singleton(iAm.getId()), ObjectEntities.MCM_ENTITY_CODE);
		Collection mcms = null;
		try {
			mcms = AdministrationStorableObjectPool.getStorableObjectsByCondition(lic, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(-1);
		}

		mcmTestQueueMap = new HashMap(mcms.size());
		mcmRefs = new HashMap(mcms.size());
		for (Iterator it = mcms.iterator(); it.hasNext();) {
			MCM mcm = (MCM) it.next();
			Identifier mcmId = mcm.getId();

			mcmTestQueueMap.put(mcmId, new HashMap());

			activateMCMReferenceWithId(mcmId);
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
		Map testQueue;
		Test_Transferable[] testsT;
		com.syrus.AMFICOM.mcm.corba.MCM mcmRef;
		while (this.running) {
			/*	Now Measurement Server can get new tests only from database
			 * (not through direct CORBA operation).
			 * Maybe in future remove this*/
			try {
				fillMCMTestQueueMap();
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			for (Iterator it = mcmTestQueueMap.keySet().iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				testQueue = (Map) mcmTestQueueMap.get(mcmId);
				mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM) mcmRefs.get(mcmId);
				if (mcmRef != null) {
					updateTestsStatus(testQueue, TestStatus.TEST_STATUS_SCHEDULED);
					testsT = createTransferables(testQueue);
					if (testsT != null) {
						try {
							Log.debugMessage(testsT.length + " tests to send to MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
							mcmRef.receiveTests(testsT);
							testQueue.clear();
							super.clearFalls();
						}
						catch (org.omg.CORBA.SystemException se) {
							Log.errorException(se);
							super.fallCode = FALL_CODE_RECEIVE_TESTS;
							if (this.abortTestsMap == null)
								this.abortTestsMap = new HashMap();
							this.abortTestsMap.put(mcmId, testQueue);
							activateMCMReferenceWithId(mcmId);
							super.sleepCauseOfFall();
						}
						catch (AMFICOMRemoteException are) {
							Log.errorMessage("Cannot transmit tests: " + are.message + "; sleeping cause of fall");
							super.fallCode = FALL_CODE_RECEIVE_TESTS;
							if (this.abortTestsMap == null)
								this.abortTestsMap = new HashMap();
							this.abortTestsMap.put(mcmId, testQueue);
							super.sleepCauseOfFall();
						}
					}
				}
				else
					activateMCMReferenceWithId(mcmId);
			}

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

		Collection addedTestIds = new HashSet();
		Identifier mcmId;
		Map testQueue;
		for (Iterator it = mcmTestQueueMap.keySet().iterator(); it.hasNext();) {
			mcmId = (Identifier) it.next();
			testQueue = (Map) mcmTestQueueMap.get(mcmId);
			addedTestIds.addAll(testQueue.keySet());
		}

		Collection tests = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(addedTestIds, cc, true);

		Test test;
		for (Iterator it = tests.iterator(); it.hasNext();) {
			test = (Test) it.next();
			mcmId = test.getMCMId();

			testQueue = (Map) mcmTestQueueMap.get(mcmId);
			if (testQueue != null) {
				Identifier testId = test.getId();
				if (!testQueue.containsKey(testId)) {
					Log.debugMessage("Adding test '" + testId + "' for MCM '" + mcmId + "'", Log.DEBUGLEVEL04);
					testQueue.put(testId, test);
				}
				else
					Log.errorMessage("Test '" + testId + "' already added to queue");
			}
			else
				Log.errorMessage("Test queue for mcm id '" + mcmId + "' not found");
		}
	}

	private static Test_Transferable[] createTransferables(Map testQueue) {
		Test_Transferable[] testsT = null;
		if (!testQueue.isEmpty()) {
			testsT = new Test_Transferable[testQueue.size()];
			int i = 0;
			Identifier testId;
			Test test;
			for (Iterator it = testQueue.keySet().iterator(); it.hasNext();) {
				testId = (Identifier) it.next();
				test = (Test) testQueue.get(testId);
				testsT[i++] = (Test_Transferable) test.getTransferable();
			}
		}
		return testsT;
	}

	private static void updateTestsStatus(Map testQueue, TestStatus status) {
		Identifier testId;
		Test test;
		Collection changedTests = new ArrayList(testQueue.size());
		for (Iterator it = testQueue.keySet().iterator(); it.hasNext();) {
			testId = (Identifier) it.next();
			test = (Test) testQueue.get(testId);
			if (test.getStatus().value() != status.value()) {
				test.setStatus(status);
				changedTests.add(test);
			}
		}

		StorableObjectDatabase database = MeasurementDatabaseContext.getTestDatabase();
		try {
			database.update(changedTests, SessionContext.getAccessIdentity().getUserId(), StorableObjectDatabase.UPDATE_CHECK);
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
		if (this.abortTestsMap != null && ! this.abortTestsMap.isEmpty()) {
			Identifier mcmId;
			Map abortTestQueue;
			Map testQueue;
			for (Iterator it = this.abortTestsMap.keySet().iterator(); it.hasNext();) {
				mcmId = (Identifier) it.next();
				abortTestQueue = (Map) this.abortTestsMap.get(mcmId);
				testQueue = (Map) mcmTestQueueMap.get(mcmId);

				Identifier testId;
				Test test;
				for (Iterator it1 = abortTestQueue.keySet().iterator(); it1.hasNext();) {
					testId = (Identifier) it1.next();

					test = (Test) abortTestQueue.get(testId);
					test.setStatus(TestStatus.TEST_STATUS_ABORTED);
					Log.debugMessage("Test '" + test.getId() + "' set ABORTED", Log.DEBUGLEVEL08);

					testQueue.remove(testId);
				}
			}

			try {
				MeasurementStorableObjectPool.flush(true);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			this.abortTestsMap.clear();
		}
		else
			Log.errorMessage("abortTests | Map is NULL or empty");
	}

	protected void shutdown() {
		this.running = false;
		deactivateCORBAServer();
		DatabaseConnection.closeConnection();
	}
}
