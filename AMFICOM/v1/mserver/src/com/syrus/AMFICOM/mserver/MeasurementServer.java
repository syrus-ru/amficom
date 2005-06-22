/*-
 * $Id: MeasurementServer.java,v 1.58 2005/06/22 17:32:49 arseniy Exp $
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
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServerPOATie;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.58 $, $Date: 2005/06/22 17:32:49 $
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
	private static Map<Identifier, Set<Test>> mcmTestQueueMap;	

	private boolean running;

	/*	Variables for method processFall()	(abort tests, ...)*/
	/*	Identifiers of MCMs on which cannot transmit tests	*/
	private static Set<Identifier> mcmIdsToAbortTests;	


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
			final ServerProcess serverProcess = ((ServerProcessDatabase) DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_CODE)).retrieveForServerAndCodename(serverId, processCodename);
			final SystemUser user = new SystemUser(serverProcess.getUserId());
			final MCMDatabase database = (MCMDatabase) DatabaseContext.getDatabase(ObjectEntities.MCM_CODE);
			final Set<Identifier> mcmIds = Identifier.createIdentifiers(database.retrieveForServer(serverId));
			login = user.getLogin();
			
			/*	Create map of test queues*/
			mcmTestQueueMap = Collections.synchronizedMap(new HashMap<Identifier, Set<Test>>(mcmIds.size()));
			for (final Iterator<Identifier> it = mcmIds.iterator(); it.hasNext();)
				mcmTestQueueMap.put(it.next(), Collections.synchronizedSet(new HashSet<Test>()));
	
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
			mcmIdsToAbortTests = Collections.synchronizedSet(new HashSet<Identifier>());
	
			/*	Activate servant*/
			final CORBAServer corbaServer = sessionEnvironment.getMServerServantManager().getCORBAServer();
			corbaServer.activateServant(new MServerPOATie(new MServerImplementation(), corbaServer.getPoa()), processCodename);
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
				for (final Iterator<Identifier> it = mcmTestQueueMap.keySet().iterator(); it.hasNext();) {
					mcmId = it.next();
					testQueue = mcmTestQueueMap.get(mcmId);
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
		LinkedIdsCondition lic = new LinkedIdsCondition(mcmTestQueueMap.keySet(), ObjectEntities.TEST_CODE);
		TypicalCondition tc = new TypicalCondition(TestStatus._TEST_STATUS_NEW,
				0,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_STATUS);
		CompoundCondition cc = null;
		try {
			cc = new CompoundCondition(lic, CompoundConditionSort.AND, tc);
		}
		catch (CreateObjectException coe) {
			//Never
			Log.errorException(coe);
		}

		final Set<Identifier> addedTestIds = new HashSet<Identifier>();
		synchronized (mcmTestQueueMap) {
			for (final Iterator<Identifier> it = mcmTestQueueMap.keySet().iterator(); it.hasNext();) {
				final Identifier mcmId = it.next();
				final Set<Test> tests = mcmTestQueueMap.get(mcmId);
				synchronized (tests) {
					for (final Iterator<Test> it1 = tests.iterator(); it1.hasNext();) {
						final Test test = it1.next();
						addedTestIds.add(test.getId());
					}
				}
			}
		}

		final Set tests = StorableObjectPool.getStorableObjectsByConditionButIds(addedTestIds, cc, true, true);

		for (final Iterator it = tests.iterator(); it.hasNext();) {
			final Test test = (Test) it.next();
			final Identifier mcmId = test.getMCMId();

			final Set<Test> testQueue = mcmTestQueueMap.get(mcmId);
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
				testsT[i] = test.getTransferable();
			}
		}

		return testsT;
	}

	private static void updateTestsStatus(Set<Test> testQueue, TestStatus status) {
		synchronized (testQueue) {
			for (final Iterator<Test> it = testQueue.iterator(); it.hasNext();) {
				final Test test = it.next();
				if (test.getStatus().value() != status.value()) {
					test.setStatus(status);
				}
			}
		}

		try {
			StorableObjectPool.flush(ObjectEntities.TEST_CODE, true);
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
			synchronized (mcmIdsToAbortTests) {
				for (final Iterator<Identifier> it = mcmIdsToAbortTests.iterator(); it.hasNext();) {
					final Identifier mcmId = it.next();
					final Set<Test> testQueue = mcmTestQueueMap.get(mcmId);

					updateTestsStatus(testQueue, TestStatus.TEST_STATUS_ABORTED);

					testQueue.clear();
				}
			}

			mcmIdsToAbortTests.clear();

			StorableObjectPool.truncate(ObjectEntities.TEST_CODE);
		}
		else
			Log.errorMessage("abortTests | Collection is NULL or empty");
	}

	protected static Set<Identifier> getMCMIds() {
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
