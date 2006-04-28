/*-
 * $Id: MeasurementServer.java,v 1.97 2006/04/28 11:00:54 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.97 $, $Date: 2006/04/28 11:00:54 $
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
	private static Map<Identifier, Set<Identifier>> startTestIdMap;

	/**
	 * Map of test ids to stop
	 */
	private static Map<Identifier, Set<Identifier>> stopTestIdMap;

	/**
	 * Condition to load tests. Must be re-created in case of changing set of available MCMs.
	 */
	private static StorableObjectCondition testLoadCondition;

	/*	Variables for method processFall()	(abort tests, ...)*/
	/*	Identifiers of MCMs on which cannot transmit tests	*/
	private static Set<Identifier> mcmIdsToAbortTests;	


	private MeasurementServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		super.setName("MeasurementServer");
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		try {
			startup();
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
			Log.errorMessage("Cannot start -- exiting");
			System.exit(0);
		}

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

	private static void startup() throws ApplicationException {
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

		/*	Create map of test ids to start*/
		startTestIdMap = Collections.synchronizedMap(new HashMap<Identifier, Set<Identifier>>(mcmIds.size()));
		for (final Identifier mcmId : mcmIds) {
			startTestIdMap.put(mcmId, Collections.synchronizedSet(new HashSet<Identifier>()));
		}

		/*	Create map of test ids to stop*/
		stopTestIdMap = Collections.synchronizedMap(new HashMap<Identifier, Set<Identifier>>());

		/*	Create condition for load tests*/
		createTestLoadCondition();

		/*	Create collection of MCM identifiers for aborting tests*/
		mcmIdsToAbortTests = Collections.synchronizedSet(new HashSet<Identifier>());

		/*	Create session environment*/
		MServerSessionEnvironment.createInstance(server.getHostName(), mcmIds, processCodename);

		/*	Login*/
		final MServerSessionEnvironment sessionEnvironment = MServerSessionEnvironment.getInstance();
		sessionEnvironment.login(login, PASSWORD, domainId);
	}

	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorMessage(e);
			System.exit(0);
		}
	}

	private static void createTestLoadCondition() {
		final LinkedIdsCondition lic = new LinkedIdsCondition(startTestIdMap.keySet(), ObjectEntities.TEST_CODE);
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

		while (!interrupted()) {
			/*	Now Measurement Server can get new tests only from database
			 * (not through direct CORBA operation).
			 * Maybe in future change such behaviour*/
			try {
				fillTestIdMaps();
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}

			synchronized (startTestIdMap) {
				for (final Identifier mcmId : startTestIdMap.keySet()) {
					final Set<Identifier> startTestIds = startTestIdMap.get(mcmId);
					final Set<Identifier> stopTestIds = stopTestIdMap.get(mcmId);
					if (!startTestIds.isEmpty() || stopTestIds != null) {
						com.syrus.AMFICOM.mcm.corba.MCM mcmRef = null;
						try {
							mcmRef = servantManager.getVerifiedMCMReference(mcmId);
						} catch (ApplicationException ae) {
							Log.errorMessage(ae);
							super.fallCode = FALL_CODE_RECEIVE_TESTS;
							mcmIdsToAbortTests.add(mcmId);
							try {
								super.sleepCauseOfFall();
							} catch (InterruptedException e) {
								return;
							}
							continue;
						}

						try {
							final IdlSessionKey sessionKey = LoginManager.getSessionKey().getIdlTransferable();

							//- Send ids of starting tests to MCM, if any
							if (!startTestIds.isEmpty()) {
								Log.debugMessage("Starting on MCM '" + mcmId + "' " + startTestIds.size() + " test(s): " + startTestIds,
										Log.DEBUGLEVEL08);
								mcmRef.startTests(Identifier.createTransferables(startTestIds), sessionKey);
								startTestIds.clear();
							}

							//- Send ids of stopping tests to MCM, if any
							if (stopTestIds != null) {
								Log.debugMessage("Stopping on MCM '" + mcmId + "' " + stopTestIds.size() + " test(s): " + stopTestIds,
										Log.DEBUGLEVEL08);
								mcmRef.stopTests(Identifier.createTransferables(stopTestIds), sessionKey);
								stopTestIdMap.remove(mcmId);
							}

							super.clearFalls();
						} catch (AMFICOMRemoteException are) {
							if (are.errorCode.value() == IdlErrorCode._ERROR_NOT_LOGGED_IN) {
								try {
									LoginManager.restoreLogin();
								} catch (ApplicationException ae) {
									Log.errorMessage(ae);
								}
							}
							Log.errorMessage("Cannot transmit: " + are.message + "; sleeping cause of fall");
							super.fallCode = FALL_CODE_RECEIVE_TESTS;
							mcmIdsToAbortTests.add(mcmId);
							try {
								super.sleepCauseOfFall();
							} catch (InterruptedException e) {
								return;
							}
						} catch (Throwable throwable) {
							Log.errorMessage(throwable);
						}

					}	//if (!startTestIds.isEmpty() || stopTestIds != null)
				}	//for (final Identifier mcmId : startTestIdMap.keySet())
			}	//synchronized (startTestIdMap)

			try {
				sleep(super.initialTimeToSleep);
			} catch (InterruptedException ie) {
				Log.errorMessage(ie);
			}
		}
	}

	private static void fillTestIdMaps() throws ApplicationException {
		final Set<Identifier> addedTestIds = new HashSet<Identifier>();
		synchronized (startTestIdMap) {
			for (final Identifier mcmId : startTestIdMap.keySet()) {
				final Set<Identifier> testIds = startTestIdMap.get(mcmId);
				addedTestIds.addAll(Identifier.createIdentifiers(testIds));
			}
		}

		StorableObjectPool.refresh(addedTestIds);
		final Set<Test> tests = StorableObjectPool.getStorableObjectsButIdsByCondition(addedTestIds, testLoadCondition, true);

		for (final Test test : tests) {
			final Identifier mcmId = test.getMCMId();
			final int status = test.getStatus().value();

			switch (status) {
				case TestStatus._TEST_STATUS_NEW:
					addToStartTestIdMap(test.getId(), mcmId);
					break;
				case TestStatus._TEST_STATUS_STOPPING:
					addToStopTestIdMap(test.getId(), mcmId);
					break;
				default:
					Log.errorMessage("Illegal status: " + status + " of test '" + test.getId() + "'");
			}
		}
	}

	private static void addToStartTestIdMap(final Identifier testId, final Identifier mcmId) {
		final Set<Identifier> testIds = startTestIdMap.get(mcmId);
		if (testIds != null) {
			if (!testIds.contains(testId)) {
				Log.debugMessage("Adding test '" + testId + "' to start on MCM '" + mcmId + "'", Log.DEBUGLEVEL04);
				testIds.add(testId);
			} else {
				Log.errorMessage("Test '" + testId + "' already added to start on MCM '" + mcmId + "'");
			}
		} else {
			Log.errorMessage("MCM '" + mcmId + "' not registered");
		}
	}

	private static void addToStopTestIdMap(final Identifier testId, final Identifier mcmId) {
		Set<Identifier> stopTestIds = stopTestIdMap.get(mcmId);
		if (stopTestIds == null) {
			stopTestIds = new HashSet<Identifier>();
			stopTestIdMap.put(mcmId, stopTestIds);
		}
		stopTestIds.add(testId);
	}

	private static void updateTestsStatus(final Set<Identifier> testIds, final TestStatus status) {
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjects(testIds, true);
			for (final Test test : tests) {
				if (test.getStatus().value() != status.value()) {
					test.setStatus(status);
				}
			}

			StorableObjectPool.flush(testIds, LoginManager.getUserId(), true);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
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
				for (final Identifier mcmId : mcmIdsToAbortTests) {
					final Set<Identifier> testIds = startTestIdMap.get(mcmId);

					updateTestsStatus(testIds, TestStatus.TEST_STATUS_ABORTED);

					testIds.clear();
				}
			}

			mcmIdsToAbortTests.clear();
		} else {
			Log.errorMessage("abortTests | Collection is NULL or empty");
		}
	}

	static Set<Identifier> getMCMIds() {
		return Collections.unmodifiableSet(startTestIdMap.keySet());
	}

	static void addMCMId(final Identifier mcmId) {
		assert mcmId != null : ErrorMessages.NON_NULL_EXPECTED;
		assert mcmId.getMajor() == ObjectEntities.MCM_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;

		if (!startTestIdMap.containsKey(mcmId)) {
			startTestIdMap.put(mcmId, Collections.synchronizedSet(new HashSet<Identifier>()));
		}

		createTestLoadCondition();
	}

	void shutdown() {
		this.interrupt();
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
