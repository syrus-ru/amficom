/*
 * $Id: MeasurementControlModule.java,v 1.23 2004/08/18 18:10:46 arseniy Exp $
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
import java.util.Hashtable;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.NewIdentifierPool;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
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

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
//import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
//import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
//import com.syrus.AMFICOM.measurement.AnalysisType;
//import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

/**
 * @version $Revision: 1.23 $, $Date: 2004/08/18 18:10:46 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */

public class MeasurementControlModule extends SleepButWorkThread {
	public static final String APPLICATION_NAME = "mcm";

	public static final String KEY_ID = "ID";
	public static final String KEY_DB_HOST_NAME = "DBHostName";
	public static final String KEY_DB_SID = "DBSID";
	public static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	public static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	public static final String KEY_SETUP_SERVER_ID = "SetupServerID";
	public static final String KEY_TICK_TIME = "TickTime";
	public static final String KEY_MAX_FALLS = "MaxFalls";
	public static final String KEY_FORWARD_PROCESSING = "ForwardProcessing";

	public static final String ID = "mcm_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final String DB_LOGIN_NAME = "amficom";
	public static final int TICK_TIME = 5;
	public static final int FORWARD_PROCESSING = 2;

	/*	Error codes for method processFall()	(remove results, ...)*/
	public static final int FALL_CODE_RECEIVE_RESULTS = 1;

	/*	Information about myself*/
	protected static MCM iAm;

	/*	Scheduled tests transferred from server	*/
	protected static List testList;	//List <Test>

	/*	Results for transfer to server	*/
	protected static List resultList;	//List <Result>

	/*	key - kis_id, value - corresponding transmitter-receiver	*/
	protected static Map transceivers;	//Map <Identifier kisId, Transceiver transceiver>

	/*	key - test_id, value - corresponding test processor	*/
	protected static Map testProcessors;	//Map <Identifier testId, TestProcessor testProcessor>

	/*	CORBA server	*/
	private static CORBAServer corbaServer;

	/*	object reference to Measurement Server	*/
	protected static MServer mServerRef;

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

		/*	Create and start transceiver for every KIS*/
		activateKISTransceivers();

		/*	Create and fill lists: testList - sheduled tests ordered by start_time;	*/
		prepareTestList();
		prepareResultList();

		/*	Create map of test processors*/
		testProcessors = new Hashtable(Collections.synchronizedMap(new Hashtable()));

		/*	Create CORBA server with servant(s)	*/
		activateCORBAServer();

		/*	Create reference to MServer*/
		activateMServerReference();

		/*	Initialize pool of Identifiers*/
		NewIdentifierPool.init(mServerRef);

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

	private static void activateKISTransceivers() {
		List kisIds = iAm.getKISIds();
		transceivers = new Hashtable(kisIds.size());
		Identifier kisId;
		Transceiver transceiver;
		synchronized (kisIds) {
			for (Iterator it = kisIds.iterator(); it.hasNext();) {
				kisId = (Identifier)it.next();
				transceiver = new Transceiver(kisId);
				transceiver.start();
				transceivers.put(kisId, transceiver);
				Log.debugMessage("Started transceiver for kis '" + kisId.toString() + "'", Log.DEBUGLEVEL03);
			}
		}
	}

	private static void prepareTestList() {
		testList = Collections.synchronizedList(new ArrayList());

		try {
			List tests = TestDatabase.retrieveTestsForMCM(iAm.getId(), TestStatus.TEST_STATUS_SCHEDULED);
			testList.addAll(tests);
		}
		catch (Exception e) {
			Log.errorException(e);
		}

/*	Below - load tests for iAm
 * 	It's ness to fix database schema before completing this part of code*/
//		try {
//			testList.addAll(iAm.retrieveTestsOrderByStartTime(TestStatus.TEST_STATUS_SCHEDULED));
//		}
//		catch (Exception e) {
//			Log.errorException(e);
//		}
//
//		/*	Processing tests - process right NOW! */
//		try {
//			List tests = iAm.retrieveTestsOrderByStartTime(TestStatus.TEST_STATUS_PROCESSING);
//			for (Iterator it = tests.iterator(); it.hasNext();)
//				startTestProcessor((Test)it.next());
//		}
//		catch (Exception e) {
//			Log.errorException(e);
//		}
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

	private static void activateMServerReference() {
		/*	Obtain reference to measurement server	*/
		try {
			mServerRef = MServerHelper.narrow(corbaServer.resolveReference(iAm.getServerId().toString()));
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			mServerRef = null;
		}
	}

	public void run() {
		Test test;
		Result_Transferable[] resultsT;
		while (this.running) {
			if (! testList.isEmpty()) {
				if (((Test)testList.get(0)).getStartTime().getTime() <= System.currentTimeMillis() + this.forwardProcessing) {
						test = (Test)testList.remove(0);
						Log.debugMessage("Starting test processor for test '" + test.getId() + "'", Log.DEBUGLEVEL08);
//					startTestProcessor(test);
				}
			}
			
			if (mServerRef != null) {
				resultsT = createTransferables();
				if (resultsT != null) {
					try {
						mServerRef.receiveResults(resultsT);
						super.clearFalls();
					}
					catch (org.omg.CORBA.COMM_FAILURE se) {
						Log.errorException(se);
						activateMServerReference();
					}
					catch (AMFICOMRemoteException are) {
						Log.errorMessage("Cannot transmit results: " + are.message + "; sleeping cause of fall");
						super.fallCode = FALL_CODE_RECEIVE_RESULTS;
						this.resultsToRemove = resultList;
						super.sleepCauseOfFall();
					}
				}
			}
			else
				activateMServerReference();

//--------------
//			System.out.println(System.currentTimeMillis());
//			AnalysisType at = (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier("AnalysisType_3"), true);
//			System.out.println("Received from Pool: " + at.getCodename() + ", criteria: " + at.getCriteriaParameterTypeIds().size());
//			try {
//				AnalysisType_Transferable att = mServerRef.transmitAnalysisType(new Identifier_Transferable("AnalysisType_3"));
//				System.out.println("Received: " + att.codename);
//			}
//			catch (org.omg.CORBA.SystemException se) {
//				Log.errorException(se);
//				activateMServerReference();
//			}
//			catch (AMFICOMRemoteException are) {
//				Log.errorMessage("Cannot receive analysis type -- " + are.message);
//			}
//--------------
			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}//while
	}

	private static Result_Transferable[] createTransferables() {
		Result_Transferable[] resultsT = null;
		if (! resultList.isEmpty()) {
			resultsT = new Result_Transferable[resultList.size()];
			int i = 0;
			synchronized (resultList) {
				for (Iterator it = resultList.iterator(); it.hasNext();)
					resultsT[i++] = (Result_Transferable)((Result)it.next()).getTransferable();
			}
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
    Log.debugMessage("Adding to testList test '" + test.getId() + "' with start time = " + startTime.toString(), Log.DEBUGLEVEL05);
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
		super.clearFalls();
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

		id = new Identifier(domainT.creator_id);
		Log.debugMessage("Getting user '" + id + "' (domain creator)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(domainT.modifier_id);
		Log.debugMessage("Getting user '" + id + "' (domain modifier)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(domainT.id);
		Log.debugMessage("Getting domain '" + id + "' ", Log.DEBUGLEVEL05);
		domain = (Domain)ConfigurationStorableObjectPool.getStorableObject(id, true);


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

		id = new Identifier(serverT.creator_id);
		Log.debugMessage("Getting user '" + id + "' (server creator)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(serverT.modifier_id);
		Log.debugMessage("Getting user '" + id + "' (server modifier)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(serverT.user_id);
		Log.debugMessage("Getting user '" + id + "' (server user)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(serverT.id);
		Log.debugMessage("Getting server '" + id + "' ", Log.DEBUGLEVEL05);
		server = (Server)ConfigurationStorableObjectPool.getStorableObject(id, true);


		id = new Identifier(mcmT.creator_id);
		Log.debugMessage("Getting user '" + id + "' (mcm creator)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(mcmT.modifier_id);
		Log.debugMessage("Getting user '" + id + "' (mcm modifier)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(mcmT.user_id);
		Log.debugMessage("Getting user '" + id + "' (mcm user)", Log.DEBUGLEVEL05);
		user = (User)ConfigurationStorableObjectPool.getStorableObject(id, true);

		id = new Identifier(mcmT.id);
		Log.debugMessage("Getting MCM '" + id + "' ", Log.DEBUGLEVEL05);
		mcm = (MCM)ConfigurationStorableObjectPool.getStorableObject(id, true);


		for (int i = 0; i < mcmT.kis_ids.length; i++) {
			try {
				KIS_Transferable kisT = mServerRef.transmitKIS(mcmT.kis_ids[i]);
				Equipment_Transferable eqT = mServerRef.transmitEquipment(kisT.equipment_id);
				Equipment equipment = (Equipment)ConfigurationStorableObjectPool.getStorableObject(new Identifier(eqT.id), true);
				for (int j = 0; j < eqT.port_ids.length; j++)
					ConfigurationStorableObjectPool.getStorableObject(new Identifier(eqT.port_ids[j]), true);

				KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(new Identifier(kisT.id), true);

				for (int j = 0; j < kisT.measurement_port_ids.length; j++)
					ConfigurationStorableObjectPool.getStorableObject(new Identifier(kisT.measurement_port_ids[j]), true);

				MonitoredElement_Transferable[] mesT = mServerRef.transmitKISMonitoredElements(kisT.id);
				for (int j = 0; j < mesT.length; j++)
					ConfigurationStorableObjectPool.getStorableObject(new Identifier(mesT[j].id), true);
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
