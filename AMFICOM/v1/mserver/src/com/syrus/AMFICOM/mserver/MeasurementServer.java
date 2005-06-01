/*-
 * $Id: MeasurementServer.java,v 1.50 2005/06/01 13:29:33 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.50 $, $Date: 2005/06/01 13:29:33 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MeasurementServer extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "mserver";

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_SERVER_ID = "ServerID";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";

	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	public static final String DB_SID = "amficom";
	/**
	 * Database connection timeout, in seconds.
	 */
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final String SERVER_ID = "Server_1";
	/**
	 * Tick time, in seconds.
	 */
	public static final int TICK_TIME = 5;	//sec

	private static final String PASSWORD = "MServer";

	/*	Error codes for method processFall()	(abort tests, ...)*/
	public static final int FALL_CODE_RECEIVE_TESTS = 1;


	/**
	 * Identifier of this server.
	 */
	private static Identifier serverId;

	/**
	 * Login of the corresponding user.
	 */
	static String login;

	/*	Process codename*/
	private static String processCodename;

	/*	Map of tests to transmit to MCMs	*/
	private static Map mcmTestQueueMap;	// Map <Identifier mcmId, Set <Test> testQueue >

	private boolean running;

	/*	Variables for method processFall()	(abort tests, ...)*/
	/*	Identifiers of MCMs on which cannot transmit tests	*/
	private static Set mcmIdsToAbortTests;	//Set <Identifier mcmId>


	public MeasurementServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		startup();

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

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Retrieve info about server*/
		/*	Retrieve info about process*/
		/*	Retrieve info about user*/
		/*	Retrieve MCM ids for this server*/
		serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		processCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME);
		try {
			final Server server = new Server(serverId);
			final ServerProcess serverProcess = ((ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_ENTITY_CODE)).retrieveForServerAndCodename(serverId, processCodename);
			final User user = new User(serverProcess.getUserId());
			final Set mcmIds = Identifier.getIdentifiers(((MCMDatabase) DatabaseContext.getDatabase(ObjectEntities.MCM_ENTITY_CODE)).retrieveForServer(serverId));
			login = user.getLogin();
			
			/*	Create map of test queues*/
			mcmTestQueueMap = Collections.synchronizedMap(new HashMap(mcmIds.size()));
			for (Iterator it = mcmIds.iterator(); it.hasNext();)
				mcmTestQueueMap.put(it.next(), Collections.synchronizedSet(new HashSet()));
	
			/*	Init database object loader*/
			DatabaseObjectLoader.init(user.getId());
	
			/*	Create session environment*/
			MServerSessionEnvironment.createInstance(server.getHostName(), mcmIds);
	
			/*	Login*/
			final MServerSessionEnvironment sessionEnvironment = MServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD);
			}
			catch (final LoginException le) {
				Log.errorException(le);
			}
	
			/*	Create collection of MCM identifiers for aborting tests*/
			mcmIdsToAbortTests = Collections.synchronizedSet(new HashSet());
	
			/*	Activate servant*/
			final CORBAServer corbaServer = sessionEnvironment.getMServerServantManager().getCORBAServer();
			corbaServer.activateServant(new MServerImplementation(), processCodename);
			corbaServer.printNamingContext();
		}
		catch (final ApplicationException ae) {
			Log.errorException(ae);
			System.exit(0);
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
			System.exit(0);
		}
	}

	public void run() {
		MServerServantManager servantManager = MServerSessionEnvironment.getInstance().getMServerServantManager();
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
							mcmRef = servantManager.getVerifiedMCMReference(mcmId);
						}
						catch (ApplicationException ae) {
							Log.errorException(ae);
							continue;
						}

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

		tests = StorableObjectPool.getStorableObjectsByConditionButIds(addedTestIds, cc, true);

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
						StorableObjectPool.putStorableObject(test);
					}
					catch (IllegalObjectEntityException ioee) {
						Log.errorException(ioee);
					}
				}
			}
		}

		try {
			StorableObjectPool.flush(ObjectEntities.TEST_ENTITY_CODE, true);
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

			StorableObjectPool.truncate(ObjectEntities.TEST_ENTITY_CODE);
		}
		else
			Log.errorMessage("abortTests | Collection is NULL or empty");
	}

	protected static Set getMCMIds() {
		return Collections.unmodifiableSet(mcmTestQueueMap.keySet());
	}

	protected void shutdown() {
		this.running = false;
		DatabaseConnection.closeConnection();
	}


	static class MServerLoginRestorer implements LoginRestorer {

		public boolean restoreLogin() {
			return true;
		}

		public String getLogin() {
			return login;
		}

		public String getPassword() {
			return PASSWORD;
		}
	}

}
