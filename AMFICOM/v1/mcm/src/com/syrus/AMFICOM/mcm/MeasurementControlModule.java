package com.syrus.AMFICOM.mcm;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
//import com.syrus.AMFICOM.server.corba.MeasurementServer;
//import com.syrus.AMFICOM.server.corba.MeasurementServerHelper;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.corba.CORBAServer;
import com.syrus.util.database.DatabaseConnection;

public class MeasurementControlModule extends Thread {
	public static final String ID = "mcm_1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final int TICK_TIME = 5;
	public static final int FORWARD_PROCESSING = 2;
	public static final int RESULT_TRANSFER_DELAY_MULTIPLIER = 2;

	/*	Information about myself*/
	protected static MCM iAm;

	/*	Scheduled tests transferred from server	*/
	protected static List testList;

	/*	Results for transfer to server	*/
	protected static List resultList;

	/*	key - kis_id, value - corresponding transmitter-receiver	*/
	protected static Map transceivers;

	/*	key - test_id, value - corresponding test processor	*/
	protected static Map testProcessors;

	/*	CORBA server	*/
	private static CORBAServer corbaServer;
//	/*	object reference to server	*/
//	protected static MeasurementServer measurementServer;

	private long tickTime;
	private long forwardProcessing;
	private int resultTransferDelayMultiplier;
	private boolean running;

	public MeasurementControlModule() {
		this.tickTime = ApplicationProperties.getInt("TickTime", TICK_TIME)*1000;
		this.forwardProcessing = ApplicationProperties.getInt("ForwardProcessing", FORWARD_PROCESSING)*1000;
		this.resultTransferDelayMultiplier = ApplicationProperties.getInt("ResultTransferDelayMultiplier", RESULT_TRANSFER_DELAY_MULTIPLIER);
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init("mcm");

		/*	Establish connection with database	*/
		establishDatabaseConnection();

		/*	Initialize object drivers
		 * 	for work with database*/
		DatabaseSetup.initDatabaseContext();

		/*	Create map of test processors*/
		testProcessors = new Hashtable(Collections.synchronizedMap(new Hashtable()));

		/*	Retrieve information abot myself*/
		try {
			iAm = new MCM(new Identifier(ApplicationProperties.getString("ID", ID)));
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

		/*	Create CORBA server with servant(s)	*/
		activateCORBAServer();

		/*	Start main loop	*/
		MeasurementControlModule measurementControlModule = new MeasurementControlModule();
		measurementControlModule.start();

		/*	Start ORB	*/
		corbaServer.run();
	}

	public void run() {
		Result_Transferable[] rts;
		int numberOfIdleRounds = 1;
		int counterOfIdleRounds = 0;
		while (this.running) {
			if (!testList.isEmpty())
				if (((Test)testList.get(0)).getStartTime().getTime() <= System.currentTimeMillis() + this.forwardProcessing)
					startTestProcessor((Test)testList.remove(0));

			if (!resultList.isEmpty()) {
				if (counterOfIdleRounds == 0) {
					rts = new Result_Transferable[resultList.size()];
					int i = 0;
					for (Iterator it = resultList.iterator(); it.hasNext(); i++)
						rts[i++] = (Result_Transferable)((Result)it.next()).getTransferable();
					try {
//						measurementServer.transmitResults(rts);
						resultList.clear();
						numberOfIdleRounds = 1;
						counterOfIdleRounds = 0;
					}
					catch (Exception e) {
						Log.errorException(e);
						numberOfIdleRounds *= this.resultTransferDelayMultiplier;
						counterOfIdleRounds = numberOfIdleRounds;
					}
				}
				else
					counterOfIdleRounds --;
			}

			try {
				sleep(this.tickTime);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}//while
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
		testProcessors.put(test.getId(), testProcessor);
		testProcessor.start();
	}

	protected static Identifier getNewIdentifier(String entity) {
		/*	!!!*/
		return new Identifier(entity);
	}

	public void shutdown() {/*!!	Need synchronization	*/
		this.running = false;

		Enumeration enumeration = ((Hashtable)testProcessors).elements();
		while (enumeration.hasMoreElements())
			((TestProcessor)enumeration.nextElement()).abort();

		testList.clear();
		resultList.clear();
		testProcessors.clear();

		enumeration = ((Hashtable)transceivers).elements();
		while (enumeration.hasMoreElements())
			((Transceiver)enumeration.nextElement()).shutdown();
		transceivers.clear();

		try {
			corbaServer.shutdown();
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		DatabaseConnection.closeConnection();
	}

	private static void establishDatabaseConnection() {
		String dbHostName = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String dbSid = ApplicationProperties.getString("DBSID", DB_SID);
		long dbConnTimeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT)*1000;
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
	}

	private static void activateKISTransceivers() {
		List kiss = iAm.getKISs();
		transceivers = new Hashtable(kiss.size());
		Identifier kisId;
		Transceiver transceiver;
		for (Iterator it = kiss.iterator(); it.hasNext();) {
			kisId = ((KIS)it.next()).getId();
			transceiver = new Transceiver(kisId.toString());
			transceiver.start();
			transceivers.put(kisId, transceiver);
			Log.debugMessage("Started transceiver for kis '" + kisId.toString() + "'", Log.DEBUGLEVEL03);
		}
	}

	private static void prepareTestList() {
		testList = Collections.synchronizedList(new ArrayList());

//		try {
//			testList.addAll(i_am.retrieveTestsOrderByStartTime(TestStatus.TEST_STATUS_SCHEDULED));
//		}
//		catch (Exception e) {
//			Log.errorException(e);
//		}
//
//		/*	Processing tests - process right NOW! */
//		try {
//			List tests = i_am.retrieveTestsOrderByStartTime(TestStatus.TEST_STATUS_PROCESSING);
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
//		/*	Create local CORBA server end activate servant*/
//		try {
//			corbaServer = new CORBAServer();
//			corbaServer.activateServant(new MCMImplementation(), iAm.getId().toString());
//		}
//		catch (Exception e) {
//			Log.errorException(e);
//			System.exit(-1);
//		}
//
//		/*	Obtain reference to measurement server	*/
//		try {
//			measurementServer = MeasurementServerHelper.narrow(corbaServer.resolveReference(i_am.getServerId().toString()));
//		}
//		catch (Exception e) {
//			Log.errorException(e);
//			System.exit(-1);
//		}
	}
}