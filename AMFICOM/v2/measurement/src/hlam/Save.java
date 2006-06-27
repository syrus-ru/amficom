package hlam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;
import java.sql.SQLException;
import java.sql.Timestamp;
import sqlj.runtime.ref.DefaultContext;
import com.syrus.util.ByteArray;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.AMFICOM.util.Identifier;
import com.syrus.AMFICOM.util.IdentifierGenerator;
import com.syrus.AMFICOM.util.DatabaseSetup;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.PTTemporalTemplate;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.ParameterType_Database;
import com.syrus.AMFICOM.measurement.corba.SetSort;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestTimeStamps_TransferablePackage.PeriodicalTestTimeStamps;
import com.syrus.AMFICOM.measurement.corba.TestReturnType;
import com.syrus.AMFICOM.measurement.corba.PTTemporalTemplate_Transferable;
import com.syrus.AMFICOM.measurement.corba.temporal_template.DayTime_Transferable;
import com.syrus.AMFICOM.measurement.corba.temporal_template.TimeQuantum_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.measurement.SetParameter;

public class Save {

	public static void main(String[] args) {
		try {
			DatabaseConnection.establishConnection("mongol", "mongol", 60000, "amficom");
			DatabaseSetup.initDatabaseContext();
/*
			oracle.jdbc.driver.OracleConnection oc = (oracle.jdbc.driver.OracleConnection)DatabaseConnection.getConnection();
			System.out.println("prefetch: " + oc.getDefaultRowPrefetch());
			System.out.println("cache: " + oc.getStmtCacheSize());
			System.out.println("autocommit: " + oc.getAutoCommit());
			System.out.println("batching: " + dc.getExecutionContext().isBatching());*/
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		createDefaultSet();
		//checkDefaultSet();
		//createDefaultMeasurementSetup();
		//createOnetimeTest();
		//createPeriodicalTestTemporalTemplate();
		//checkPeriodicalTestTemporalTemplate();
		//createPeriodicalTest();
	}

	private static void checkPeriodicalTestTemporalTemplate() {
		try {
			PTTemporalTemplate pt = new PTTemporalTemplate(new Identifier("pttt1"));
			System.out.println("day times: " + pt.getDayTimes().size());
			System.out.println("dates: " + pt.getDates().size());
			System.out.println("description: " + pt.getDescription());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createPeriodicalTestTemporalTemplate() {
		DayTime_Transferable[] day_times = new DayTime_Transferable[2];
		day_times[0] = new DayTime_Transferable(1, 0, 0);
		day_times[1] = new DayTime_Transferable(13, 0, 0);
		TimeQuantum_Transferable[] dates = new TimeQuantum_Transferable[3];
		dates[0] = new TimeQuantum_Transferable(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		dates[1] = new TimeQuantum_Transferable(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		dates[2] = new TimeQuantum_Transferable(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		PTTemporalTemplate_Transferable pt_t =
				new PTTemporalTemplate_Transferable("pttt1",
																						"Шаблонищще",
																						System.currentTimeMillis(),
																						new TimeQuantum_Transferable(Calendar.MONTH, 2),
																						day_times,
																						dates);
		try {
			PTTemporalTemplate pt = new PTTemporalTemplate(pt_t);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createPeriodicalTest() {
		TestTimeStamps_Transferable ttst = new TestTimeStamps_Transferable();
		PeriodicalTestTimeStamps ptts =
				new PeriodicalTestTimeStamps(System.currentTimeMillis(),
																		 System.currentTimeMillis() + 24*3600*1000,
																		 "pttt1");
		ttst.ptts(ptts);
		String[] measurement_setup_ids = new String[1];
		measurement_setup_ids[0] = "msetup1";
		Test_Transferable tt = new Test_Transferable("test2",
																								 TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL,
																								 ttst,
																								 "reflectometry",
																								 "dadara",
																								 "dadara",
																								 TestStatus.TEST_STATUS_SCHEDULED,
																								 "me1",
																								 TestReturnType.TEST_RETURN_TYPE_WHOLE,
																								 System.currentTimeMillis(),
																								 "Периодический тест",
																								 measurement_setup_ids);
		try {
			Test test = new Test(tt);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createOnetimeTest() {
		TestTimeStamps_Transferable ttst = new TestTimeStamps_Transferable();
		ttst.start_time(System.currentTimeMillis());
		String[] measurement_setup_ids = new String[1];
		measurement_setup_ids[0] = "msetup1";
		Test_Transferable tt = new Test_Transferable("test1",
																								 TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
																								 ttst,
																								 "reflectometry",
																								 "",
																								 "",
																								 TestStatus.TEST_STATUS_SCHEDULED,
																								 "me1",
																								 TestReturnType.TEST_RETURN_TYPE_WHOLE,
																								 System.currentTimeMillis(),
																								 "Одноразовый тест",
																								 measurement_setup_ids);
		try {
			Test test = new Test(tt);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createAnalysis() {
		
	}

	private static void createEvaluation() {
		
	}

	private static void createDefaultMeasurementSetup() {
		MeasurementSetup_Transferable mst =
				new MeasurementSetup_Transferable("msetup1",
																					"set1",
																					"",
																					"",
																					"",
																					System.currentTimeMillis(),
																					"I am",
																					System.currentTimeMillis(),
																					"Настройка Измерений",
																					Measurement.DEFAULT_MEASUREMENT_DURATION,
																					new String[0]);
		try {
			MeasurementSetup measurementSetup = new MeasurementSetup(mst);
			measurementSetup.attachToMonitoredElement((Identifier)measurementSetup.getParameterSet().getMonitoredElementIds().get(0));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createCriteriaSet() {
		String[] parameter_ids = new String[9];
	}

	private static void createSetMeasurementArguments() {
		Identifier[] parameter_ids = new Identifier[6];
		for (int i = 0; i < parameter_ids.length; i++)
			parameter_ids[i] = IdentifierGenerator.generateIdentifier(DatabaseSetup.SETPARAMETER_TABLE);
		Identifier[] parameter_type_ids = new Identifier[6];
		byte[][] parameter_values = new byte[6][];
		try {
			parameter_type_ids[0] = ParameterType_Database.retrieveForCodename("ref_wvlen").getId();
			parameter_type_ids[1] = ParameterType_Database.retrieveForCodename("ref_trclen").getId();
			parameter_type_ids[2] = ParameterType_Database.retrieveForCodename("ref_res").getId();
			parameter_type_ids[3] = ParameterType_Database.retrieveForCodename("ref_pulswd").getId();
			parameter_type_ids[4] = ParameterType_Database.retrieveForCodename("ref_ior").getId();
			parameter_type_ids[5] = ParameterType_Database.retrieveForCodename("ref_scans").getId();
			parameter_values[0] = ByteArray.toByteArray((int)1550);
			parameter_values[1] = ByteArray.toByteArray((double)65535);
			parameter_values[2] = ByteArray.toByteArray((double)4);
			parameter_values[3] = ByteArray.toByteArray((long)1000);
			parameter_values[4] = ByteArray.toByteArray((double)1.467);
			parameter_values[5] = ByteArray.toByteArray((double)16000);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}

		Parameter_Transferable[] pts = new Parameter_Transferable[6];
		for (int i = 0; i < pts.length; i++)
			pts[i] = new Parameter_Transferable(parameter_ids[i].getCode(),
																					parameter_type_ids[i].getCode(),
																					parameter_values[i]);
		Identifier[] me_ids = new Identifier[1];
		me_ids[0] = ;
		Set_Transferable st = new Set_Transferable("set1",
																							 SetSort.SET_SORT_MEASUREMENT_PARAMETERS,
																							 System.currentTimeMillis(),
																							 "user1",
																							 System.currentTimeMillis(),
																							 "Some default set",
																							 pts,
																							 me_ids);

		try {
			Set set = new Set(st);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void checkDefaultSet() {
		Set set = null;
		try {
			set = new Set(new Identifier("set1"));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("sort: " + set.getSort().value());
		System.out.println("created: " + set.getCreated().toString());
		System.out.println("creator_id: " + set.getCreatorId().toString());
		System.out.println("modified: " + set.getModified().toString());
		System.out.println("description: " + set.getDescription());
		SetParameter[] setParameters = set.getParameters();
		try {
			System.out.println("[0] id: " + setParameters[0].getId().toString() + ", type_id: " + setParameters[0].getTypeId().toString() + ", value: " + (new ByteArray(setParameters[0].getValue())).toInt());
			System.out.println("[1] id: " + setParameters[1].getId().toString() + ", type_id: " + setParameters[1].getTypeId().toString() + ", value: " + (new ByteArray(setParameters[1].getValue())).toDouble());
			System.out.println("[2] id: " + setParameters[2].getId().toString() + ", type_id: " + setParameters[2].getTypeId().toString() + ", value: " + (new ByteArray(setParameters[2].getValue())).toDouble());
			System.out.println("[3] id: " + setParameters[3].getId().toString() + ", type_id: " + setParameters[3].getTypeId().toString() + ", value: " + (new ByteArray(setParameters[3].getValue())).toLong());
			System.out.println("[4] id: " + setParameters[4].getId().toString() + ", type_id: " + setParameters[4].getTypeId().toString() + ", value: " + (new ByteArray(setParameters[4].getValue())).toDouble());
			System.out.println("[5] id: " + setParameters[5].getId().toString() + ", type_id: " + setParameters[5].getTypeId().toString() + ", value: " + (new ByteArray(setParameters[5].getValue())).toDouble());
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		ArrayList me_ids = set.getMonitoredElementIds();
		for (Iterator iterator = me_ids.iterator(); iterator.hasNext();)
			System.out.println("me: " + iterator.next().toString());
	}
}