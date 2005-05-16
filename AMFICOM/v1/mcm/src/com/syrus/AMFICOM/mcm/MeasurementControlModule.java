/*
 * $Id: MeasurementControlModule.java,v 1.90 2005/05/16 14:53:35 arseniy Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseObjectLoader;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginException;
import com.syrus.AMFICOM.general.LoginRestorer;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestWrapper;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.90 $, $Date: 2005/05/16 14:53:35 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public final class MeasurementControlModule extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "mcm"; //$NON-NLS-1$

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	public static final String KEY_MCM_ID = "MCMID"; //$NON-NLS-1$
	public static final String KEY_DB_HOST_NAME = "DBHostName"; //$NON-NLS-1$
	public static final String KEY_DB_SID = "DBSID"; //$NON-NLS-1$
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout"; //$NON-NLS-1$
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName"; //$NON-NLS-1$
	public static final String KEY_TICK_TIME = "TickTime"; //$NON-NLS-1$
	public static final String KEY_MAX_FALLS = "MaxFalls"; //$NON-NLS-1$
	public static final String KEY_FORWARD_PROCESSING = "ForwardProcessing"; //$NON-NLS-1$
	public static final String KEY_FORGET_FRAME = "ForgetFrame"; //$NON-NLS-1$
	public static final String KEY_MSERVER_SERVANT_NAME = "MServerServantName"; //$NON-NLS-1$
	public static final String KEY_MSERVER_CHECK_TIMEOUT = "MServerCheckTimeout"; //$NON-NLS-1$
	public static final String KEY_KIS_TICK_TIME = "KISTickTime"; //$NON-NLS-1$
	public static final String KEY_KIS_MAX_FALLS = "KISMaxFalls"; //$NON-NLS-1$
	public static final String KEY_KIS_HOST_NAME = "KISHostName"; //$NON-NLS-1$
	public static final String KEY_KIS_TCP_PORT = "KISTCPPort"; //$NON-NLS-1$
	public static final String KEY_KIS_MAX_OPENED_CONNECTIONS = "KISMaxOpenedConnections"; //$NON-NLS-1$
	public static final String KEY_KIS_CONNECTION_TIMEOUT = "KISConnectionTimeout"; //$NON-NLS-1$

	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	public static final String MCM_ID = "MCM_1"; //$NON-NLS-1$
	public static final String DB_SID = "amficom"; //$NON-NLS-1$
	/**
	 * Database connection timeout, in seconds.
	 */
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	public static final String DB_LOGIN_NAME = "amficom"; //$NON-NLS-1$
	/**
	 * Tick time, in seconds.
	 */
	public static final int TICK_TIME = 5;	//sec
	public static final int FORWARD_PROCESSING = 2;
	public static final int FORGET_FRAME = 24 * 60 * 60;	//sec
	public static final String MSERVER_SERVANT_NAME = "MServer"; //$NON-NLS-1$
	public static final int MSERVER_CHECK_TIMEOUT = 10;		//min
	public static final int KIS_TICK_TIME = 1;	//sec
	public static final int KIS_MAX_FALLS = 10;
	public static final String KIS_HOST_NAME = "127.0.0.1"; //$NON-NLS-1$
	public static final short KIS_TCP_PORT = 7501;
	public static final int KIS_MAX_OPENED_CONNECTIONS = 1;
	public static final int KIS_CONNECTION_TIMEOUT = 120;	//sec

	private static final String PASSWORD = "MCM"; //$NON-NLS-1$

	/*	Error codes for method processFall()	(remove results, ...)*/
	public static final int FALL_CODE_RECEIVE_RESULTS = 1;


	/**
	 * Identifier of this MCM.
	 */
	protected static Identifier mcmId;

	/**
	 * Login of the corresponding user.
	 */
	static String login;

	/*	Scheduled tests transferred from server	*/
	protected static List testList;	//List <Test>

	/*	Results for transfer to server	*/
	protected static List resultList;	//List <Result>

	/*	key - test_id, value - corresponding test processor	*/
	protected static Map testProcessors;	//Map <Identifier testId, TestProcessor testProcessor>

	/*	Reference to KISConnectionManager*/
	protected static KISConnectionManager kisConnectionManager;

	/*	Key - kisId, value - corresponding transmitter-receiver*/
	protected static Map transceivers;	//Map <Identifier kisId, Transceiver transceiver>

	private long forwardProcessing;
	private boolean running;

	/*	Variables for method processFall()	(remove results, ...)*/
	private List resultsToRemove;

	MeasurementControlModule() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.forwardProcessing = ApplicationProperties.getInt(KEY_FORWARD_PROCESSING, FORWARD_PROCESSING)*1000;
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	All preparations on startup*/
		startup();

		/*	Start main loop	*/
		final MeasurementControlModule measurementControlModule = new MeasurementControlModule();
		measurementControlModule.start();

		/*	Add shutdown hook	*/
		Runtime.getRuntime().addShutdownHook(new Thread() {
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
			final MCM mcm = new MCM(mcmId);
			final User user = new User(mcm.getUserId());
			final Server server = new Server(mcm.getServerId());
			login = user.getLogin();
	
			/*	Init database object loader*/
			DatabaseObjectLoader.init(user.getId());
	
			/*	Create session environment*/
			MCMSessionEnvironment.createInstance(server.getHostName());
	
			/*	Login*/
			final MCMSessionEnvironment sessionEnvironment = MCMSessionEnvironment.getInstance();
			try {
				sessionEnvironment.login(login, PASSWORD);
			}
			catch (final LoginException le) {
				Log.errorException(le);
			}

			/*	Create map of test processors*/
			testProcessors = Collections.synchronizedMap(new HashMap());
	
			/*	Create (and start - ?) KIS connection manager*/
			activateKISConnectionManager();
	
			/*	Create and start transceiver for every KIS*/
			activateKISTransceivers();
	
			/*	Create and fill lists: testList - sheduled tests ordered by start_time;	*/
			prepareResultList();
			prepareTestList();
	
			/*	Activate servant*/
			final CORBAServer corbaServer = sessionEnvironment.getMCMServantManager().getCORBAServer();
			corbaServer.activateServant(new MCMImplementation(), mcmId.toString());
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

	private static void activateKISConnectionManager() {
		kisConnectionManager = new KISConnectionManager();
//		kisConnectionManager.start();
	}

	private static void activateKISTransceivers() {
		try {
			LinkedIdsCondition lic = new LinkedIdsCondition(mcmId, ObjectEntities.KIS_ENTITY_CODE);
			Collection kiss = ConfigurationStorableObjectPool.getStorableObjectsByCondition(lic, true);

			transceivers = Collections.synchronizedMap(new HashMap(kiss.size()));
			KIS kis;
			Identifier kisId;
			Transceiver transceiver;
			for (Iterator it = kiss.iterator(); it.hasNext();) {
				kis = (KIS) it.next();
				kisId = kis.getId();
				transceiver = new Transceiver(kis);
				transceiver.start();
				transceivers.put(kisId, transceiver);
				Log.debugMessage("Started transceiver for KIS '" + kisId + "'", Log.DEBUGLEVEL07);
			}
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	private static void prepareTestList() {
		testList = Collections.synchronizedList(new ArrayList());
		TypicalCondition tc;
		CompoundCondition cc = null;
		Collection tests;

		tc = new TypicalCondition(TestStatus._TEST_STATUS_SCHEDULED,
				0,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_ENTITY_CODE,
				TestWrapper.COLUMN_STATUS);
		LinkedIdsCondition lic = new LinkedIdsCondition(mcmId, ObjectEntities.TEST_ENTITY_CODE);
		try {
			cc = new CompoundCondition(lic, CompoundConditionSort.AND, tc);
		}
		catch (CreateObjectException coe) {
			//Never
			Log.errorException(coe);
		}

		try {
			tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(cc, true);
			Log.debugMessage("Found " + tests.size() + " tests of status SCHEDULED", Log.DEBUGLEVEL07);
			sortTestsByStartTime(tests);
			testList.addAll(tests);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}

		tc = new TypicalCondition(TestStatus._TEST_STATUS_PROCESSING,
				0,
				OperationSort.OPERATION_EQUALS,
				ObjectEntities.TEST_ENTITY_CODE,
				TestWrapper.COLUMN_STATUS);
		try {
			cc = new CompoundCondition(lic, CompoundConditionSort.AND, tc);
		}
		catch (CreateObjectException coe) {
			//Never
			Log.errorException(coe);
		}

		try {
			tests = MeasurementStorableObjectPool.getStorableObjectsByCondition(cc, true);
			Log.debugMessage("Found " + tests.size() + " tests of status PROCESSING", Log.DEBUGLEVEL07);
			for (Iterator it = tests.iterator(); it.hasNext();)
				startTestProcessor((Test) it.next());
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	private static void prepareResultList() {
		/*!!	resultList - results (return later...)	!!*/
		resultList = Collections.synchronizedList(new ArrayList());
	}

	public void run() {
		Test test;
		Identifier testId;
		MServer mServerRef;
		Result_Transferable[] resultsT;
		while (this.running) {
			if (!testList.isEmpty()) {
				if (((Test) testList.get(0)).getStartTime().getTime() <= System.currentTimeMillis() + this.forwardProcessing) {
					test = (Test) testList.remove(0);
					testId = test.getId();
					if (!testProcessors.containsKey(testId)) {
						Log.debugMessage("Starting test processor for test '" + testId + "'", Log.DEBUGLEVEL07);
						startTestProcessor(test);
					}
					else
						Log.errorMessage("Test processor for test '" + testId + "' already started");
				}
			}
			
			if (!resultList.isEmpty()) {
				try {
					resultsT = createTransferables();
					mServerRef = MCMSessionEnvironment.getInstance().getMCMServantManager().getMServerReference();
					mServerRef.receiveResults(resultsT, (Identifier_Transferable) mcmId.getTransferable());
					resultList.clear();
					super.clearFalls();
				}
				catch (CommunicationException ce) {
					Log.errorException(ce);
					Log.errorMessage("Cannot transmit results; sleeping cause of fall");
					super.fallCode = FALL_CODE_RECEIVE_RESULTS;
					this.resultsToRemove = resultList;
					super.sleepCauseOfFall();
				}
				catch (AMFICOMRemoteException are) {
					Log.errorMessage("Cannot transmit results: " + are.message + "; sleeping cause of fall");
					super.fallCode = FALL_CODE_RECEIVE_RESULTS;
					this.resultsToRemove = resultList;
					super.sleepCauseOfFall();
				}
			}

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}

		}//while
	}

	private static Result_Transferable[] createTransferables() {
		Result_Transferable[] resultsT = new Result_Transferable[resultList.size()];
		int i = 0;
		synchronized (resultList) {
			for (Iterator it = resultList.iterator(); it.hasNext();)
				resultsT[i++] = (Result_Transferable)((Result)it.next()).getTransferable();
		}
	
		return resultsT;
	}

	private static void startTestProcessor(Test test) {
		TestProcessor testProcessor = null;
		switch (test.getTemporalType().value()) {
			case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
				testProcessor = new OnetimeTestProcessor(test);
				break;
			case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
				testProcessor = new PeriodicalTestProcessor(test);
				break;
			default:
				Log.errorMessage("Incorrect temporal type " + test.getTemporalType().value() + " of test '" + test.getId().toString() + "'");
		}
		if (testProcessor != null) {
			testProcessors.put(test.getId(), testProcessor);
			testProcessor.start();
		}
	}

	private static class TestStartTimeComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			if (o1 instanceof Test && o2 instanceof Test) {
				return ((Test) o1).getStartTime().compareTo(((Test) o2).getStartTime());
			}
			throw new IllegalArgumentException("Objects must be of type Test. 1: " + o1.getClass().getName()
					+ ", 2: " + o2.getClass().getName());
		}
	}

	private static void sortTestsByStartTime(Collection tests) {
		List testsL;
		if (tests instanceof List)
			testsL = (List) tests;
		else
			testsL = new LinkedList(tests);
		Collections.sort(testsL, new TestStartTimeComparator());
	}

	protected static void addTests(List newTests) {
		sortTestsByStartTime(newTests);

		synchronized (testList) {
			ListIterator testIt = testList.listIterator();
			ListIterator newTestIt = newTests.listIterator();
			Test test = testIt.hasNext() ? (Test) testIt.next() : null;
			Test newTest = newTestIt.hasNext() ? (Test) newTestIt.next() : null;
			while (newTest != null) {
				while (test != null && test.getStartTime().before(newTest.getStartTime()))
					test = testIt.hasNext() ? (Test) testIt.next() : null;

				if (test != null)
					testIt.previous();
				testIt.add(newTest);

				prepareTestToExecute(newTest);

				newTest = newTestIt.hasNext() ? (Test) newTestIt.next() : null;
			}

			try {
				MeasurementStorableObjectPool.flush(ObjectEntities.TEST_ENTITY_CODE, true);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

		}	//synchronized (testList)
	}

	private static void prepareTestToExecute(Test test) {
		test.setStatus(TestStatus.TEST_STATUS_SCHEDULED);
		try {
			MeasurementStorableObjectPool.putStorableObject(test);

			if (test.getTemporalType().value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL)
				MeasurementStorableObjectPool.getStorableObject(test.getTemporalPatternId(), true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
		}
	}

	protected static void abortTest(Test test) {
		Identifier id = test.getId();
		if (testList.contains(test)) {
			Log.debugMessage("Test '" + id + "' found in testList -- removing and aborting ", Log.DEBUGLEVEL07);
			testList.remove(test);
			test.setStatus(TestStatus.TEST_STATUS_ABORTED);
			try {
				MeasurementStorableObjectPool.putStorableObject(test);
				MeasurementStorableObjectPool.flush(test.getId(), true);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}
		}
		else {
			if (testProcessors.containsKey(id)) {
				Log.debugMessage("Test '" + id + "' has test processor -- shutting down", Log.DEBUGLEVEL07);
				TestProcessor testProcessor = (TestProcessor)testProcessors.get(id);
				testProcessor.abort();
			}
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			case FALL_CODE_RECEIVE_RESULTS:
				removeResults();
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}
	
	private void removeResults() {
		if (this.resultsToRemove != null && ! this.resultsToRemove.isEmpty()) {
			resultList.removeAll(this.resultsToRemove);
			this.resultsToRemove = null;
		}
		else
			Log.errorMessage("removeResults | list is NULL or empty");
	}

	protected void shutdown() {
		this.running = false;
		for (Iterator it = transceivers.keySet().iterator(); it.hasNext();)
			((Transceiver)transceivers.get(it.next())).shutdown();

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
	}

}
