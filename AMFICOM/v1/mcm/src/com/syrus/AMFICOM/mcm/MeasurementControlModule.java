package com.syrus.AMFICOM.mcm;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.corba.CORBAServer;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.AMFICOM.util.Identifier;
import com.syrus.AMFICOM.util.DatabaseSetup;
import com.syrus.AMFICOM.util.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.server.ResourcedbInterface;
import com.syrus.AMFICOM.server.corba.MeasurementServer;
import com.syrus.AMFICOM.server.corba.MeasurementServerHelper;

public class MeasurementControlModule extends Thread {
	public static final String ID = "mcm1";
	public static final String DB_SID = "amficom";
	public static final int DB_CONNECTION_TIMEOUT = 120;
	public static final int TICK_TIME = 5;
	public static final int FORWARD_PROCESSING = 2;

	/*	Scheduled tests transferred from server	*/
	protected static List testList;
	/*	Results for transfer to server	*/
	protected static List resultList;
	/*	key - kis_id, value - corresponding transmitter-receiver	*/
	protected static Hashtable transceivers;
	/*	key - test_id, value - corresponding test processor	*/
	protected static Hashtable testProcessors;
	/*	CORBA server	*/
	private static CORBAServer corbaServer;
	/*	object reference to server	*/
	protected static MeasurementServer measurementServer;

	private long tick_time;
	private long forward_processing;
	private boolean running;

	public MeasurementControlModule() {
		this.tick_time = ApplicationProperties.getInt("TickTime", TICK_TIME)*1000;
		this.forward_processing = ApplicationProperties.getInt("ForwardProcessing", FORWARD_PROCESSING)*1000;
		this.running = true;
	}

	public static void main(String[] args) {
		Application.init("mcm");

		/*	Establish connection with database	*/
		String db_host_name = ApplicationProperties.getString("DBHostName", Application.getInternetAddress());
		String db_sid = ApplicationProperties.getString("DBSID", DB_SID);
		long db_conn_timeout = ApplicationProperties.getInt("DBConnectionTimeout", DB_CONNECTION_TIMEOUT)*1000;
		try {
			DatabaseConnection.establishConnection(db_host_name, db_sid, db_conn_timeout);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		/*	This initializes stubs 
		 * 	for working with database*/
		DatabaseSetup.initDatabaseContext();

		testProcessors = new Hashtable(Collections.synchronizedMap(new Hashtable()));

		/*	Retrieve information abot myself
		 * 	Create transceiver for every KIS*/
		transceivers = null;
		MCM i_am = null;
		try {
			i_am = new MCM(new Identifier(ApplicationProperties.getString("ID", ID)));
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}
		ArrayList kiss = i_am.getKISs();
		transceivers = new Hashtable(kiss.size());
		Identifier kis_id;
		Transceiver transceiver;
		for (Iterator it = kiss.iterator(); it.hasNext();) {
			kis_id = ((KIS)it.next()).getId();
			transceiver = new Transceiver(kis_id.toString());
			transceiver.start();
			transceivers.put(kis_id, transceiver);
			Log.debugMessage("Started transceiver for kis '" + kis_id.toString() + "'", Log.DEBUGLEVEL03);
		}

		/*	Create and fill lists: testList - sheduled tests ordered by start_time;	*/
		testList = Collections.synchronizedList(new ArrayList());
		try {
			testList.addAll(i_am.retrieveTestsOrderByStartTime(TestStatus.TEST_STATUS_SCHEDULED));
		}
		catch (Exception e) {
			Log.errorException(e);
		}
		/*!!	resultList - results (return later...)	!!*/
		resultList = Collections.synchronizedList(new ArrayList());

		/*	Processing tests - process right NOW! */
		try {
			List tests = i_am.retrieveTestsOrderByStartTime(TestStatus.TEST_STATUS_PROCESSING);
			for (Iterator it = tests.iterator(); it.hasNext();)
				startTestProcessor((Test)it.next());
		}
		catch (Exception e) {
			Log.errorException(e);
		}

		/*	Create CORBA server with servant(s)	*/
		try {
			corbaServer = new CORBAServer();
			corbaServer.activateServant(new MCMImplementation(), i_am.getId().toString());
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		/*	Obtain reference to measurement server	*/
		try {
			measurementServer = MeasurementServerHelper.narrow(corbaServer.resolveReference(i_am.getServerId().toString()));
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(-1);
		}

		/*	Start main loop	*/
		MeasurementControlModule measurementControlModule = new MeasurementControlModule();
		measurementControlModule.start();

		/*	Start ORB	*/
		corbaServer.run();
	}

	public void run() {
		Test test;
		Result_Transferable[] rts;
		while (this.running) {
			if (!testList.isEmpty())
				if (((Test)testList.get(0)).getStartTime().getTime() <= System.currentTimeMillis() + this.forward_processing)
					startTestProcessor((Test)testList.remove(0));

			if (!resultList.isEmpty()) {
				/*!!	transmit results onto server										!!
				 *!!	according to return type of corresponding tests	!!*/
				rts = new Result_Transferable[resultList.size()];
				int i = 0;
				for (Iterator it = resultList.iterator(); it.hasNext(); i++)
					rts[i] = (Result_Transferable)((Result)it.next()).getTransferable();
				try {
					measurementServer.transmitResults(rts);
					resultList.clear();
				}
				catch (Exception e) {
					Log.errorException(e);
				}
			}//if (!resultList.isEmpty())

			try {
				sleep(this.tick_time);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}//while (this.running)
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
			case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
				testProcessor = new TimetableTestProcessor(test);
				break;
			default:
				Log.errorMessage("Incorrect temporal type " + test.getTemporalType().value() + " of test '" + test.getId().toString() + "'");
		}
		testProcessors.put(test.getId(), testProcessor);
		testProcessor.start();
	}

	protected static Identifier createIdentifier(String object_sort) {
		String id_str = null;
		try {
			id_str = measurementServer.createIdentifier(object_sort);
		}
		catch (AMFICOMRemoteException are) {
			Log.errorException(are);
			id_str = ResourcedbInterface.getUId(object_sort);
		}
		return new Identifier(id_str);
	}

	public void shutdown() {/*!!	Need synchronization	*/
		this.running = false;

		Enumeration enumeration = testProcessors.elements();
		while (enumeration.hasMoreElements())
			((TestProcessor)enumeration.nextElement()).abort();

		testList.clear();
		resultList.clear();
		testProcessors.clear();

		enumeration = transceivers.elements();
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
}