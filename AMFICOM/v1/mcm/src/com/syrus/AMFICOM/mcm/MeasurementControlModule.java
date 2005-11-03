/*
 * $Id: MeasurementControlModule.java,v 1.142 2005/11/03 13:51:29 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.mcm.corba.MCMPOATie;
import com.syrus.AMFICOM.measurement.KIS;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.TestStatus;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.142 $, $Date: 2005/11/03 13:51:29 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mcm
 */

final class MeasurementControlModule extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "mcm";
	public static final String SETUP_OPTION = "-setup";

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	public static final String KEY_MCM_ID = "MCMID";
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";
	public static final String KEY_FORWARD_PROCESSING = "ForwardProcessing";
	public static final String KEY_WAIT_MRESULT_TIMEOUT = "WaitMResultTimeout";
	public static final String KEY_MSERVER_SERVANT_NAME = "MServerServantName";
	public static final String KEY_MSERVER_CHECK_TIMEOUT = "MServerCheckTimeout";
	public static final String KEY_KIS_TICK_TIME = "KISTickTime";
	public static final String KEY_KIS_MAX_FALLS = "KISMaxFalls";
	public static final String KEY_KIS_HOST_NAME = "KISHostName";
	public static final String KEY_KIS_TCP_PORT = "KISTCPPort";
	public static final String KEY_KIS_MAX_OPENED_CONNECTIONS = "KISMaxOpenedConnections";
	public static final String KEY_KIS_CONNECTION_TIMEOUT = "KISConnectionTimeout";

	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	public static final String MCM_ID = "MCM_1";
	public static final String DB_SID = "amficom";
	/**
	 * Database connection timeout, in seconds.
	 */
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	public static final String DB_LOGIN_NAME = "amficom";
	/**
	 * Tick time, in seconds.
	 */
	public static final int TICK_TIME = 5;	//sec
	public static final int FORWARD_PROCESSING = 2;
	public static final int WAIT_MRESULT_TIMEOUT = 24 * 60 * 60;	//sec
	public static final String MSERVER_SERVANT_NAME = "MServer";
	public static final int MSERVER_CHECK_TIMEOUT = 10;		//min
	public static final int KIS_TICK_TIME = 1;	//sec
	public static final int KIS_MAX_FALLS = 10;
	public static final String KIS_HOST_NAME = "127.0.0.1";
	public static final short KIS_TCP_PORT = 7501;
	public static final int KIS_MAX_OPENED_CONNECTIONS = 1;
	public static final int KIS_CONNECTION_TIMEOUT = 120;	//sec

	private static final String PASSWORD = "mcm";


	/**
	 * Identifier of this MCM.
	 */
	protected static Identifier mcmId;

	/**
	 * Login of the corresponding user.
	 * */
	static String login;

	/**
	 * Identifier of domain to log in
	 * */
	static Identifier domainId;

	/**
	 * Scheduled tests transferred from server
	 * */
	static List<Test> testList;

	/**	
	 * key - test_id, value - corresponding test processor
	 * */
	static Map<Identifier, TestProcessor> testProcessors;

	/**
	 * Reference to KISConnectionManager
	 * */
	static KISConnectionManager kisConnectionManager;

	/**
	 * Key - kisId, value - corresponding transmitter-receiver
	 * */
	static Map<Identifier, Transceiver> transceivers;

	/**
	 * Thread with event queue
	 * */
	static EventQueue eventQueue;

	private long forwardProcessing;
	private boolean running;

	private MeasurementControlModule() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		super.setName("MeasurementControlModule");

		this.forwardProcessing = ApplicationProperties.getInt(KEY_FORWARD_PROCESSING, FORWARD_PROCESSING)*1000;
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	Parse command-line options*/
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase(SETUP_OPTION)) {
				MCMSetup.setup();
				System.exit(0);
			} else {
				Log.errorMessage("Illegal options -- " + args);
				System.exit(0);
			}
		}

		/*	All preparations on startup*/
		startup();

		/*	Start main loop	*/
		final MeasurementControlModule measurementControlModule = new MeasurementControlModule();
		measurementControlModule.start();

		/*	Add shutdown hook	*/
		Runtime.getRuntime().addShutdownHook(new Thread("MeasurementControlModule -- shutdown hook") {
			@Override
			public void run() {
				measurementControlModule.shutdown();
			}
		});
	}

	private static void startup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Retrieve information about MCM, it's user and server*/
		mcmId = new Identifier(ApplicationProperties.getString(KEY_MCM_ID, MCM_ID));
		try {
			final StorableObjectDatabase<MCM> mcmDatabase = DatabaseContext.getDatabase(ObjectEntities.MCM_CODE);
			final MCM mcm = mcmDatabase.retrieveForId(mcmId);

			final StorableObjectDatabase<SystemUser> systemUserDatabase = DatabaseContext.getDatabase(ObjectEntities.SYSTEMUSER_CODE);
			final SystemUser user = systemUserDatabase.retrieveForId(mcm.getUserId());

			final StorableObjectDatabase<Server> serverDatabase = DatabaseContext.getDatabase(ObjectEntities.SERVER_CODE);
			final Server server = serverDatabase.retrieveForId(mcm.getServerId());

			login = user.getLogin();
			domainId = mcm.getDomainId();

			/*	Create session environment*/
			MCMSessionEnvironment.createInstance(server.getHostName());

			/*	Login*/
			final MCMSessionEnvironment sessionEnvironment = MCMSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD, domainId);
			} catch (final LoginException le) {
				Log.errorMessage(le);
			}

			/*	Create map of test processors*/
			testProcessors = Collections.synchronizedMap(new HashMap<Identifier, TestProcessor>());

			/*	Create (and start - ?) KIS connection manager*/
			activateKISConnectionManager();

			/*	Create and start transceiver for every KIS*/
			activateKISTransceivers();

			/*	Create and fill testList - sheduled tests ordered by start_time;	*/
			prepareTestList();

			/*	Create and start event thread*/
			eventQueue = new EventQueue();
			eventQueue.start();

			/*	Activate servant*/
			final CORBAServer corbaServer = sessionEnvironment.getMCMServantManager().getCORBAServer();
			corbaServer.activateServant(new MCMPOATie(new MCMImpl(), corbaServer.getPoa()), mcmId.toString());
			corbaServer.printNamingContext();
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			System.exit(0);
		}
	}

	static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT) * 1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (Exception e) {
			Log.errorMessage(e);
			System.exit(0);
		}
	}

	private static void activateKISConnectionManager() {
		kisConnectionManager = new KISConnectionManager();
//		kisConnectionManager.start();
	}

	private static void activateKISTransceivers() {
		try {
			final LinkedIdsCondition lic = new LinkedIdsCondition(mcmId, ObjectEntities.KIS_CODE);
			final Set<KIS> kiss = StorableObjectPool.getStorableObjectsByCondition(lic, true, false);

			transceivers = Collections.synchronizedMap(new HashMap<Identifier, Transceiver>(kiss.size()));
			for (final KIS kis : kiss) {
				final Identifier kisId = kis.getId();
				final Transceiver transceiver = new Transceiver(kis);
				transceiver.start();
				Log.debugMessage("Started transceiver for KIS '" + kisId + "'", Log.DEBUGLEVEL07);
				transceivers.put(kisId, transceiver);
			}
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}

	private static void prepareTestList() {
		testList = Collections.synchronizedList(new ArrayList<Test>());
		TypicalCondition tc;

		final LinkedIdsCondition lic = new LinkedIdsCondition(mcmId, ObjectEntities.TEST_CODE);

		tc = new TypicalCondition(TestStatus._TEST_STATUS_SCHEDULED,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_STATUS);
		CompoundCondition cc = new CompoundCondition(lic, CompoundConditionSort.AND, tc);

		final Set<Identifier> scheduledTestIds = new HashSet<Identifier>();
		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjectsByCondition(cc, true, false);
			Log.debugMessage("Found " + tests.size() + " tests of status SCHEDULED", Log.DEBUGLEVEL07);
			sortTestsByStartTime(tests);
			testList.addAll(tests);
			scheduledTestIds.addAll(Identifier.createIdentifiers(tests));
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}

		tc = new TypicalCondition(TestStatus._TEST_STATUS_PROCESSING,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_CODE,
				TestWrapper.COLUMN_STATUS);
		cc = new CompoundCondition(lic, CompoundConditionSort.AND, tc);

		try {
			final Set<Test> tests = StorableObjectPool.getStorableObjectsButIdsByCondition(scheduledTestIds, cc, true, false);
			Log.debugMessage("Found " + tests.size() + " tests of status PROCESSING", Log.DEBUGLEVEL07);
			for (final Test test : tests) {
				startTestProcessor(test);
			}
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}

	@Override
	public void run() {
		while (this.running) {
			if (!testList.isEmpty()) {
				if (testList.get(0).getStartTime().getTime() <= System.currentTimeMillis() + this.forwardProcessing) {
					final Test test = testList.remove(0);
					final Identifier testId = test.getId();
					if (!testProcessors.containsKey(testId)) {
						Log.debugMessage("Starting test processor for test '" + testId + "'", Log.DEBUGLEVEL07);
						startTestProcessor(test);
					} else {
						Log.errorMessage("Test processor for test '" + testId + "' already started");
					}
				}
			}

			try {
				sleep(super.initialTimeToSleep);
			} catch (InterruptedException ie) {
				Log.errorMessage(ie);
			}

		}//while
	}

	static void putTestProcessor(final TestProcessor testProcessor) {
		assert testProcessor != null : ErrorMessages.NON_NULL_EXPECTED;
		testProcessors.put(testProcessor.getTestId(), testProcessor);
	}

	static void removeTestProcessor(final Identifier testId) {
		assert testId != null : ErrorMessages.NON_NULL_EXPECTED;
		assert testId.getMajor() == ObjectEntities.TEST_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;
		testProcessors.remove(testId);
	}

	private static void startTestProcessor(final Test test) {
		switch (test.getTemporalType().value()) {
			case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
				(new OnetimeTestProcessor(test)).start();
				break;
			case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
				(new PeriodicalTestProcessor(test)).start();
				break;
			default:
				Log.errorMessage("Incorrect temporal type " + test.getTemporalType().value() + " of test '" + test.getId().toString() + "'");
		}
	}

	private static class TestStartTimeComparator implements Comparator<Test> {

		public int compare(final Test test1, final Test test2) {
			return test1.getStartTime().compareTo(test2.getStartTime());
		}
	}

	private static void sortTestsByStartTime(final Collection<Test> tests) {
		List<Test> testsL;
		if (tests instanceof List) {
			testsL = (List<Test>) tests;
		} else {
			testsL = new LinkedList<Test>(tests);
		}
		Collections.sort(testsL, new TestStartTimeComparator());
	}

	static void addTests(final List<Test> newTests) {
		sortTestsByStartTime(newTests);

		synchronized (testList) {
			final ListIterator<Test> testIt = testList.listIterator();
			final ListIterator<Test> newTestIt = newTests.listIterator();
			Test test = testIt.hasNext() ? testIt.next() : null;
			Test newTest = newTestIt.hasNext() ? newTestIt.next() : null;
			while (newTest != null) {
				while (test != null && test.getStartTime().before(newTest.getStartTime())) {
					test = testIt.hasNext() ? (Test) testIt.next() : null;
				}

				if (test != null) {
					testIt.previous();
				}
				testIt.add(newTest);

				prepareTestToExecute(newTest);

				newTest = newTestIt.hasNext() ? newTestIt.next() : null;
			}

			try {
				StorableObjectPool.flush(new HashSet<Test>(newTests), LoginManager.getUserId(), false);
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}

		}	//synchronized (testList)
	}

	private static void prepareTestToExecute(final Test test) {
		test.setStatus(TestStatus.TEST_STATUS_SCHEDULED);
		try {
			if (test.getTemporalType().value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
				StorableObjectPool.getStorableObject(test.getTemporalPatternId(), true);
			}

			final Identifier groupTestId = test.getGroupTestId();
			if (!groupTestId.isVoid() && !groupTestId.equals(test.getId())) {
				StorableObjectPool.getStorableObject(groupTestId, true);
			}
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}

	static void stopTests(final Set<Identifier> testIds) {
		for (final Iterator<Identifier> it = testIds.iterator(); it.hasNext();) {
			final Identifier id = it.next();
			try {
				final Test test = (Test) StorableObjectPool.getStorableObject(id, true);
				if (test != null) {
					stopTest(test);
					test.setStatus(TestStatus.TEST_STATUS_STOPPED);
				} else {
					Log.errorMessage("Test '" + id + "' not found");
				}
			} catch (ApplicationException ae) {
				Log.errorMessage(ae);
			}
		}

		try {
			StorableObjectPool.flush(testIds, LoginManager.getUserId(), true);
		} catch (ApplicationException ae) {
			Log.errorMessage(ae);
		}
	}

	private static void stopTest(final Test test) {
		final Identifier id = test.getId();
		if (testList.contains(test)) {
			Log.debugMessage("Test '" + id + "' found in testList -- removing and stopping ", Log.DEBUGLEVEL07);
			testList.remove(test);
		} else if (testProcessors.containsKey(id)) {
			Log.debugMessage("Test '" + id + "' has test processor -- shutting down", Log.DEBUGLEVEL07);
			final TestProcessor testProcessor = testProcessors.get(id);
			testProcessor.stopTest();
		}
	}

	@Override
	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

	void shutdown() {
		this.running = false;

		for (final Identifier kisId : transceivers.keySet()) {
			transceivers.get(kisId).shutdown();
		}

		synchronized (testProcessors) {
			for (final Identifier testId : testProcessors.keySet()) {
				final TestProcessor testProcessor = testProcessors.get(testId);
				testProcessor.shutdown();
			}
		}

		eventQueue.shutdown();

		DatabaseConnection.closeConnection();
	}


	static class MCMLoginRestorer implements LoginRestorer {

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
