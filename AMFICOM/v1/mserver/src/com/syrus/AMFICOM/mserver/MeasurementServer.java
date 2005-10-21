/*-
 * $Id: MeasurementServer.java,v 1.83 2005/10/21 12:25:51 arseniy Exp $
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

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.ServerProcess;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.ServerProcessWrapper;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlTest;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.mserver.corba.MServerPOATie;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.83 $, $Date: 2005/10/21 12:25:51 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */

final class MeasurementServer extends SleepButWorkThread {
	private static final String APPLICATION_NAME = "mserver";

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	private static final String KEY_SERVER_ID = "ServerID";
	private static final String KEY_TICK_TIME = "TickTime";
	private static final String KEY_MAX_FALLS = "MaxFalls";

	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	private static final String DB_SID = "amficom";
	/**
	 * Database connection timeout, in seconds.
	 */
	private static final int DB_CONNECTION_TIMEOUT = 120;
	private static final String DB_LOGIN_NAME = "amficom";
	private static final String SERVER_ID = "Server_1";
	/**
	 * Tick time, in seconds.
	 */
	private static final int TICK_TIME = 5;	//sec

	private static final String PASSWORD = "MServer";

	/*	Error codes for method processFall()	(abort tests, ...)*/
	public static final int FALL_CODE_RECEIVE_TESTS = 1;

	/**
	 * Login of the corresponding user */
	static String login;

	/**
	 * Identifier of domain to log in 
	 */
	static Identifier domainId;

	/**
	 * Map of tests to transmit to MCMs	*/
	private static Map<Identifier, Set<Test>> mcmTestQueueMap;

	/**
	 * Map of test ids to stop
	 */
	private static Map<Identifier, Set<Identifier>> mcmStoppingTestIdsMap;

	/**
	 * Condition to load tests. Must be re-created in case of changing set of available MCMs.
	 */
	private static StorableObjectCondition testLoadCondition;

	private boolean running;

	/*	Variables for method processFall()	(abort tests, ...)*/
	/*	Identifiers of MCMs on which cannot transmit tests	*/
	private static Set<Identifier> mcmIdsToAbortTests;	


	private MeasurementServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		super.setName("MeasurementServer");

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
		Runtime.getRuntime().addShutdownHook(new Thread("MeasurementServer -- shutdown hook") {
			@Override
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
		final Identifier serverId = new Identifier(ApplicationProperties.getString(KEY_SERVER_ID, SERVER_ID));
		final String processCodename = ApplicationProperties.getString(ServerProcessWrapper.KEY_MSERVER_PROCESS_CODENAME,
				ServerProcessWrapper.MSERVER_PROCESS_CODENAME);
		try {
			final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVER_CODE);
			final Server server = serverDatabase.retrieveForId(serverId);

			final StorableObjectDatabase<ServerProcess> serverProcessDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVERPROCESS_CODE);
			final ServerProcess serverProcess = ((ServerProcessDatabase) serverProcessDatabase).retrieveForServerAndCodename(serverId,
					processCodename);

			final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(ObjectEntities.SYSTEMUSER_CODE);
			final SystemUser user = systemUserDatabase.retrieveForId(serverProcess.getUserId());

			final StorableObjectDatabase<MCM> mcmDatabase = DatabaseContext.getDatabase(ObjectEntities.MCM_CODE);
			final Set<Identifier> mcmIds = Identifier.createIdentifiers(((MCMDatabase) mcmDatabase).retrieveForServer(serverId));

			login = user.getLogin();
			domainId = server.getDomainId();

			/*	Create map of test queues*/
			mcmTestQueueMap = Collections.synchronizedMap(new HashMap<Identifier, Set<Test>>(mcmIds.size()));
			for (final Identifier mcmId : mcmIds) {
				mcmTestQueueMap.put(mcmId, Collections.synchronizedSet(new HashSet<Test>()));
			}

			/*	Create map of test ids to stop*/
			mcmStoppingTestIdsMap = Collections.synchronizedMap(new HashMap<Identifier, Set<Identifier>>());

			/*	Create condition for load tests*/
			createTestLoadCondition();

			/*	Create session environment*/
			MServerSessionEnvironment.createInstance(server.getHostName(), mcmIds);

			/*	Login*/
			final MServerSessionEnvironment sessionEnvironment = MServerSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD, domainId);
			}
			catch (final LoginException le) {
				Log.errorException(le);
			}

			/*	Create collection of MCM identifiers for aborting tests*/
			mcmIdsToAbortTests = Collections.synchronizedSet(new HashSet<Identifier>());

			/*	Activate servant*/
			final CORBAServer corbaServer = sessionEnvironment.getMServerServantManager().getCORBAServer();
			corbaServer.activateServant(new MServerPOATie(new MServerImpl(), corbaServer.getPoa()), processCodename);
			corbaServer.printNamingContext();
		}
		catch (final ApplicationException ae) {
			Log.errorException(ae);
			System.exit(0);
		}
	}

	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorException(e);
			System.exit(0);
		}
	}

	private static void createTestLoadCondition() {
		final LinkedIdsCondition lic = new LinkedIdsCondition(mcmTestQueueMap.keySet(), ObjectEntities.TEST_CODE);
		final TypicalCondition tc1 = new TypicalCondition(TestStatus._TEST_STATUS_NEW,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_STATUS);
		final TypicalCondition tc2 = new TypicalCondition(TestStatus._TEST_STATUS_STOPPING,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_STATUS);
		testLoadCondition = new CompoundCondition(lic,
				CompoundConditionSort.AND,
				new CompoundCondition(tc1, CompoundConditionSort.OR, tc2));
	}

	@Override
	public void run() {
		final MServerServantManager servantManager = MServerSessionEnvironment.getInstance().getMServerServantManager();

		while (this.running) {
			/*	Now Measurement Server can get new tests only from database
			 * (not through direct CORBA operation).
			 * Maybe in future change such behaviour*/
			try {
				fillMCMTestMaps();
			} catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			synchronized (mcmTestQueueMap) {
				for (final Identifier mcmId : mcmTestQueueMap.keySet()) {
					final Set<Test> testQueue = mcmTestQueueMap.get(mcmId);
					final Set<Identifier> stopTestIds = mcmStoppingTestIdsMap.get(mcmId);
					if (!testQueue.isEmpty() || stopTestIds != null) {
						com.syrus.AMFICOM.mcm.corba.MCM mcmRef = null;
						try {
							mcmRef = servantManager.getVerifiedMCMReference(mcmId);
						} catch (ApplicationException ae) {
							Log.errorException(ae);
							super.fallCode = FALL_CODE_RECEIVE_TESTS;
							mcmIdsToAbortTests.add(mcmId);
							super.sleepCauseOfFall();
							continue;
						}

						try {
							final IdlSessionKey sessionKey = LoginManager.getSessionKeyTransferable();

							//- Send new tests to MCM, if any
							if (!testQueue.isEmpty()) {
								final IdlTest[] idlTests = Test.createTransferables(testQueue, servantManager.getCORBAServer().getOrb());
								Log.debugMessage("Sending to MCM '" + mcmId + "' " + idlTests.length
										+ " test(s): " + Identifier.createIdentifiers(testQueue), Log.DEBUGLEVEL08);
								mcmRef.receiveTests(idlTests, sessionKey);
								testQueue.clear();
							}

							//- Send ids of stopping tests to MCM, if any
							if (stopTestIds != null) {
								final IdlIdentifier[] stopTestIdlIds = Identifier.createTransferables(stopTestIds);
								Log.debugMessage("Stopping on MCM '" + mcmId + "' " + stopTestIdlIds.length + " test(s): " + stopTestIds,
										Log.DEBUGLEVEL08);
								mcmRef.stopTests(stopTestIdlIds, sessionKey);
								mcmStoppingTestIdsMap.remove(mcmId);
							}

							super.clearFalls();
						} catch (AMFICOMRemoteException are) {
							if (are.errorCode.value() == IdlErrorCode._ERROR_NOT_LOGGED_IN) {
								try {
									LoginManager.restoreLogin();
								} catch (ApplicationException ae) {
									Log.errorException(ae);
								}
							}
							Log.errorMessage("Cannot transmit tests: " + are.message + "; sleeping cause of fall");
							super.fallCode = FALL_CODE_RECEIVE_TESTS;
							mcmIdsToAbortTests.add(mcmId);
							super.sleepCauseOfFall();
						} catch (Throwable throwable) {
							Log.errorException(throwable);
						}

					}	//if (!testQueue.isEmpty())
				}	//for (Iterator it = mcmTestQueueMap.keySet().iterator(); it.hasNext();)
			}	//synchronized (mcmTestQueueMap)

			System.out.println(new Date(System.currentTimeMillis()));
			try {
				sleep(super.initialTimeToSleep);
			} catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}

	private static void fillMCMTestMaps() throws ApplicationException {
		final Set<Identifier> addedTestIds = new HashSet<Identifier>();
		synchronized (mcmTestQueueMap) {
			for (final Identifier mcmId : mcmTestQueueMap.keySet()) {
				final Set<Test> tests = mcmTestQueueMap.get(mcmId);
				addedTestIds.addAll(Identifier.createIdentifiers(tests));
			}
		}

		StorableObjectPool.refresh(addedTestIds);
		final Set<Test> tests = StorableObjectPool.getStorableObjectsButIdsByCondition(addedTestIds, testLoadCondition, true, true);

		for (final Test test : tests) {
			final Identifier mcmId = test.getMCMId();
			final TestStatus status = test.getStatus();

			switch (status.value()) {
				case TestStatus._TEST_STATUS_NEW:
					addToMCMTestQueueMap(test, mcmId);
					break;
				case TestStatus._TEST_STATUS_STOPPING:
					addToMCMStoppingTestIdsMap(test, mcmId);
					break;
				default:
					Log.errorMessage("MeasurementServer.fillMCMTestQueueMap | Illegal status: " + status.value()
							+ " of test '" + test.getId() + "'");
			}
		}
	}

	private static void addToMCMTestQueueMap(final Test test, final Identifier mcmId) {
		final Set<Test> testQueue = mcmTestQueueMap.get(mcmId);
		if (testQueue != null) {
			if (!testQueue.contains(test)) {
				Log.debugMessage("Adding test '" + test.getId() + "' for MCM '" + mcmId + "'", Log.DEBUGLEVEL04);
				testQueue.add(test);
			} else {
				Log.errorMessage("Test '" + test.getId() + "' already added to queue");
			}
		} else {
			Log.errorMessage("Test queue for MCM '" + mcmId + "' not found");
		}
	}

	private static void addToMCMStoppingTestIdsMap(final Test test, final Identifier mcmId) {
		Set<Identifier> stopTestIds = mcmStoppingTestIdsMap.get(mcmId);
		if (stopTestIds == null) {
			stopTestIds = new HashSet<Identifier>();
			mcmStoppingTestIdsMap.put(mcmId, stopTestIds);
		}
		stopTestIds.add(test.getId());
	}

	private static void updateTestsStatus(final Set<Test> testQueue, final TestStatus status) {
		synchronized (testQueue) {
			for (final Test test : testQueue) {
				if (test.getStatus().value() != status.value()) {
					test.setStatus(status);
				}
			}
		}

		try {
			StorableObjectPool.flush(testQueue, LoginManager.getUserId(), true);
		} catch (ApplicationException ae) {
			Log.errorException(ae);
		}
		
	}

	@Override
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
		} else {
			Log.errorMessage("abortTests | Collection is NULL or empty");
		}
	}

	static Set<Identifier> getMCMIds() {
		return Collections.unmodifiableSet(mcmTestQueueMap.keySet());
	}

	static void addMCMId(final Identifier mcmId) {
		assert mcmId != null : ErrorMessages.NON_NULL_EXPECTED;
		assert mcmId.getMajor() == ObjectEntities.MCM_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;

		if (!mcmTestQueueMap.containsKey(mcmId)) {
			mcmTestQueueMap.put(mcmId, Collections.synchronizedSet(new HashSet<Test>()));
		}

		createTestLoadCondition();
	}

	void shutdown() {
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

		public Identifier getDomainId() {
			return domainId;
		}
	}

}
