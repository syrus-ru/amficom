/*
 * $Id: MeasurementControlModule.java,v 1.48 2004/12/17 17:20:54 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.mserver.corba.MServerHelper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.48 $, $Date: 2004/12/17 17:20:54 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public final class MeasurementControlModule extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "mcm";

	public static final String KEY_ID = "ID";
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_SETUP_SERVER_ID = "SetupServerID";
	public static final String KEY_MAX_FALLS = "MaxFalls";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_FORWARD_PROCESSING = "ForwardProcessing";
	public static final String KEY_TCP_PORT = "TCPPort";
	public static final String KEY_KIS_TICK_TIME = "KISTickTime";
	public static final String KEY_KIS_MAX_FALLS = "KISMaxFalls";
	public static final String KEY_KIS_HOST_NAME = "KISHostName";
	public static final String KEY_KIS_TCP_PORT = "KISTCPPort";
	public static final String KEY_KIS_MAX_OPENED_CONNECTIONS = "KISMaxOpenedConnections";
	public static final String KEY_KIS_CONNECTION_TIMEOUT = "KISConnectionTimeout";

	public static final String ID = "mcm_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final int TICK_TIME = 5;
	public static final int FORWARD_PROCESSING = 2;
	public static final short TCP_PORT = 7500;
	public static final int KIS_TICK_TIME = 1;
	public static final int KIS_MAX_FALLS = 10;
	public static final String KIS_HOST_NAME = "127.0.0.1";
	public static final short KIS_TCP_PORT = 7501;
	public static final int KIS_MAX_OPENED_CONNECTIONS = 1;
	public static final int KIS_CONNECTION_TIMEOUT = 120;

	/*	Error codes for method processFall()	(remove results, ...)*/
	public static final int FALL_CODE_RECEIVE_RESULTS = 1;

	/*	Information about myself*/
	protected static MCM iAm;

	/*	Scheduled tests transferred from server	*/
	protected static List testList;	//List <Test>

	/*	Results for transfer to server	*/
	protected static List resultList;	//List <Result>

	/*	key - test_id, value - corresponding test processor	*/
	protected static Map testProcessors;	//Map <Identifier testId, TestProcessor testProcessor>

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	object reference to Measurement Server	*/
	protected static MServer mServerRef;

	/*	Reference to KISConnectionManager*/
	protected static KISConnectionManager kisConnectionManager;

	/*	Key - kisId, value - corresponding transmitter-receiver*/
	protected static Map transceivers;	//Map <Identifier kisId, Transceiver transceiver>

	private long forwardProcessing;
	private boolean running;

	/*	Variables for method processFall()	(remove results, ...)*/
	private List resultsToRemove;

	public MeasurementControlModule() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties.getInt(KEY_MAX_FALLS, MAX_FALLS));
		this.forwardProcessing = ApplicationProperties.getInt(KEY_FORWARD_PROCESSING, FORWARD_PROCESSING)*1000;
		this.running = true;		
		
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);

		/*	If flag -setup is specified run in setup mode (exit after all)*/
		if (args.length > 0 && args[0].equals("-setup"))
			setup();
		else
			normalStartup();
	}

	private static void normalStartup() {
		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Load object types*/
		DatabaseContextSetup.initObjectPools();

		/*	Retrieve information abot myself*/
		try {
			iAm = new MCM(new Identifier(ApplicationProperties.getString(KEY_ID, ID)));
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		/*	Create CORBA server with servant(s)	*/
		activateCORBAServer();

		/*	Create reference to MServer*/
		activateMServerReference();

		/*	Create and fill lists: testList - sheduled tests ordered by start_time;	*/
		prepareTestList();
		prepareResultList();

		/*	Initialize pool of Identifiers*/
		IdentifierPool.init(mServerRef);

		/*	Create map of test processors*/
		testProcessors = Collections.synchronizedMap(new HashMap());

		/*	Create (and start - ?) KIS connection manager*/
		activateKISConnectionManager();

		/*	Create and start transceiver for every KIS*/
		activateKISTransceivers();

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

	private static void prepareTestList() {
		testList = Collections.synchronizedList(new ArrayList());
		List tests;
		TestDatabase testDatabase = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();

		try {
			tests = testDatabase.retrieveTestsForMCM(iAm.getId(), TestStatus.TEST_STATUS_SCHEDULED);
			Log.debugMessage("Found " + tests.size() + " tests of status SCHEDULED", Log.DEBUGLEVEL07);
			testList.addAll(tests);
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		
		try {
			tests = testDatabase.retrieveTestsForMCM(iAm.getId(), TestStatus.TEST_STATUS_PROCESSING);
			Log.debugMessage("Found " + tests.size() + " tests of status PROCESSING", Log.DEBUGLEVEL07);
			for (Iterator it = tests.iterator(); it.hasNext();)
				startTestProcessor((Test)it.next());
		}
		catch (Exception e) {
			Log.errorException(e);
		}		
	}

	private static void prepareResultList() {
		/*!!	resultList - results (return later...)	!!*/
		resultList = Collections.synchronizedList(new ArrayList());
	}

	private static void activateCORBAServer() {
		/*	Create local CORBA server end activate servant*/
		try {
			corbaServer = new CORBAServer();
			corbaServer.activateServant(new MCMImplementation(), iAm.getId().toString());
		}
		catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}
	}

	protected static void activateMServerReference() {
		/*	Obtain reference to measurement server	*/
		try {
			mServerRef = MServerHelper.narrow(corbaServer.resolveReference(iAm.getServerId().toString()));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			mServerRef = null;
		}
	}

	protected static void resetMServerConnection() {
		activateMServerReference();
		IdentifierPool.setIdentifierGeneratorServer(mServerRef);
	}

	private static void activateKISConnectionManager() {
		kisConnectionManager = new KISConnectionManager();

//		kisConnectionManager.start();
	}

	private static void activateKISTransceivers() {
		List kiss = iAm.getKISs();
		transceivers = Collections.synchronizedMap(new HashMap(kiss.size()));
		KIS kis;
		Identifier kisId;
		Transceiver transceiver;
		synchronized (kiss) {
			for (Iterator it = kiss.iterator(); it.hasNext();) {
				kis = (KIS)it.next();
				kisId = kis.getId();
				transceiver = new Transceiver(kis);
				transceiver.start();
				transceivers.put(kisId, transceiver);
				Log.debugMessage("Started transceiver for KIS '" + kisId + "'", Log.DEBUGLEVEL07);
			}
		}
	}

	public void run() {
		Test test;
		Result_Transferable[] resultsT;
		while (this.running) {
			if (! testList.isEmpty()) {
				if (((Test)testList.get(0)).getStartTime().getTime() <= System.currentTimeMillis() + this.forwardProcessing) {
						test = (Test)testList.remove(0);
						Log.debugMessage("Starting test processor for test '" + test.getId() + "'", Log.DEBUGLEVEL07);
					startTestProcessor(test);
				}
			}
			
			if (mServerRef != null) {
				synchronized (resultList) {
					resultsT = createTransferables();
					if (resultsT.length > 0) {
						try {
							mServerRef.receiveResults(resultsT, (Identifier_Transferable)iAm.getId().getTransferable());
							resultList.clear();
							super.clearFalls();
						}
						catch (org.omg.CORBA.COMM_FAILURE se) {
							Log.errorException(se);
							resetMServerConnection();
						}
						catch (AMFICOMRemoteException are) {
							Log.errorMessage("Cannot transmit results: " + are.message + "; sleeping cause of fall");
							super.fallCode = FALL_CODE_RECEIVE_RESULTS;
							this.resultsToRemove = resultList;
							super.sleepCauseOfFall();
						}
					}
				}
			}	//if (mServerRef != null)
			else
				resetMServerConnection();

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
			case TestTemporalType._TEST_TEMPORAL_TYPE_CONTINUOUS:
				testProcessor = new ContinuousTestProcessor(test);
				break;
			default:
				Log.errorMessage("Incorrect temporal type " + test.getTemporalType().value() + " of test '" + test.getId().toString() + "'");
		}
		if (testProcessor != null) {
			testProcessors.put(test.getId(), testProcessor);
			testProcessor.start();
		}
	}
	
	protected static void addTest(Test test) {
		Date startTime = test.getStartTime();
    Log.debugMessage("Adding to testList test '" + test.getId() + "' with start time = " + startTime.toString(), Log.DEBUGLEVEL07);
    if (testList.isEmpty())
      testList.add(test);
    else {
			if (startTime.after(((Test)testList.get(testList.size() - 1)).getStartTime()))
				testList.add(test);
			else {
				synchronized (testList) {
					Test test1;
					for (Iterator it = testList.iterator(); it.hasNext();) {
						test1 = (Test)it.next();
						if (startTime.before(test1.getStartTime())) {
							testList.add(testList.indexOf(test1), test);
							break;
						}
					}
				}
			}
    }

		try {
			test.updateStatus(TestStatus.TEST_STATUS_SCHEDULED, iAm.getUserId());
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
		}
	}

	protected static void abortTest(Test test) {
		Identifier id = test.getId();
		if (testList.contains(test)) {
			Log.debugMessage("Test '" + id + "' found in testList -- removing and aborting ", Log.DEBUGLEVEL07);
			testList.remove(test);
			try {
				test.updateStatus(TestStatus.TEST_STATUS_ABORTED, iAm.getUserId());
			}
			catch (UpdateObjectException uoe) {
				Log.errorException(uoe);
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

	protected void shutdown() {/*!!	Need synchronization	*/
		this.running = false;
		for (Iterator it = transceivers.keySet().iterator(); it.hasNext();)
			((Transceiver)transceivers.get(it.next())).shutdown();
		System.out.println("FINISH");
		DatabaseConnection.closeConnection();
	}


	private static void setup() {
		String setupServerId = ApplicationProperties.getString(KEY_SETUP_SERVER_ID, null);
		if (setupServerId == null) {
			Log.errorMessage("Cannot find key '" + KEY_SETUP_SERVER_ID + "' in file " + ApplicationProperties.getFileName());
			System.exit(-1);
		}

		activateCORBASetupServer();
		activateSetupServerReference(setupServerId);

		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseContextSetup.initDatabaseContext();

		/*	Load object types*/
		DatabaseContextSetup.initObjectPools();

		Identifier id;
		User user;
		Domain domain;
		Server server;
		MCM mcm;

		id = new Identifier(ApplicationProperties.getString(KEY_ID, ID));
		MCM_Transferable mcmT = null;
		try {
			Log.debugMessage("Fetching MCM '" + id + "' from server", Log.DEBUGLEVEL05);
			mcmT = mServerRef.transmitMCM((Identifier_Transferable)id.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}


		id = new Identifier(mcmT.domain_id);
		Domain_Transferable domainT = null;
		try {
			Log.debugMessage("Fetching domain '" + id + "' (mcm domain) from server", Log.DEBUGLEVEL05);
			domainT = mServerRef.transmitDomain((Identifier_Transferable)id.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}

		try {
			id = new Identifier(domainT.header.creator_id);
			Log.debugMessage("Getting user '" + id + "' (domain creator)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(domainT.header.modifier_id);
			Log.debugMessage("Getting user '" + id + "' (domain modifier)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(domainT.header.id);
			Log.debugMessage("Getting domain '" + id + "' ", Log.DEBUGLEVEL05);
			domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(id, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(-1);
		}


		id = new Identifier(mcmT.server_id);
		Server_Transferable serverT = null;
		try {
			Log.debugMessage("Fetching server '" + id + "' (mcm server) from server", Log.DEBUGLEVEL05);
			serverT = mServerRef.transmitServer((Identifier_Transferable)id.getTransferable());
		}
		catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}

		try {
			id = new Identifier(serverT.header.creator_id);
			Log.debugMessage("Getting user '" + id + "' (server creator)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(serverT.header.modifier_id);
			Log.debugMessage("Getting user '" + id + "' (server modifier)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(serverT.user_id);
			Log.debugMessage("Getting user '" + id + "' (server user)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(serverT.header.id);
			Log.debugMessage("Getting server '" + id + "' ", Log.DEBUGLEVEL05);
			server = (Server)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
	
			id = new Identifier(mcmT.header.creator_id);
			Log.debugMessage("Getting user '" + id + "' (mcm creator)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(mcmT.header.modifier_id);
			Log.debugMessage("Getting user '" + id + "' (mcm modifier)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(mcmT.user_id);
			Log.debugMessage("Getting user '" + id + "' (mcm user)", Log.DEBUGLEVEL05);
			user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);
	
			id = new Identifier(mcmT.header.id);
			Log.debugMessage("Getting MCM '" + id + "' ", Log.DEBUGLEVEL05);
			mcm = (MCM)ConfigurationStorableObjectPool.getStorableObject(id, true);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			System.exit(-1);
		}


		for (int i = 0; i < mcmT.kis_ids.length; i++) {
			try {
				KIS_Transferable kisT = mServerRef.transmitKIS(mcmT.kis_ids[i]);

				Equipment_Transferable eqT = mServerRef.transmitEquipment(kisT.equipment_id);
				try {
					(new Equipment(eqT)).insert();
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
				Port_Transferable portT;
				for (int j = 0; j < eqT.port_ids.length; j++) {
					portT = mServerRef.transmitPort(eqT.port_ids[j]);
					try {
						(new Port(portT)).insert();
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
				/*	If Equipment is monitored (i. e. - has monitored elements),
				 *	we also must retrieve it's monitored elements, it's KIS
				 *  and subsequent information. Disregard now	*/

				try {
					(new KIS(kisT)).insert();
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}

				MeasurementPort_Transferable mportT;
				for (int j = 0; j < kisT.measurement_port_ids.length; j++) {
					mportT = mServerRef.transmitMeasurementPort(kisT.measurement_port_ids[j]);
					try {
						(new MeasurementPort(mportT)).insert();
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}

				/*	Now load only transmission paths.
				 *	Other sorts of monitored element - disregard	*/
				MonitoredElement_Transferable[] mesT = mServerRef.transmitKISMonitoredElements(kisT.header.id);
				TransmissionPath_Transferable tpT;
				for (int j = 0; j < mesT.length; j++) {
					for (int k = 0; k < mesT[j].monitored_domain_member_ids.length; k++) {
						tpT = mServerRef.transmitTransmissionPath(mesT[j].monitored_domain_member_ids[k]);
						try {
							(new TransmissionPath(tpT)).insert();
						}
						catch (CreateObjectException coe) {
							Log.errorException(coe);
						}
					}
					try {
						(new MonitoredElement(mesT[j])).insert();
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
			}
			catch (Exception e) {
				Log.errorException(e);
				DatabaseConnection.closeConnection();
				System.exit(-1);
			}
		}


		/*	Close database connection*/
		DatabaseConnection.closeConnection();
	}

	private static void activateCORBASetupServer() {
		/*	Create local CORBA server*/
		try {
			corbaServer = new CORBAServer();
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void activateSetupServerReference(String setupServerId) {
		/*	Obtain reference to setup server	*/
		try {
			mServerRef = MServerHelper.narrow(corbaServer.resolveReference(setupServerId));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			System.exit(-1);
		}
	}

}
