package hlam;

import com.syrus.AMFICOM.util.Identifier;
import com.syrus.AMFICOM.util.IdentifierGenerator;
import com.syrus.AMFICOM.util.DatabaseSetup;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterType_Database;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementType_Database;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisType_Database;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationType_Database;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.configuration.Server;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.corba.Server_Transferable;
import com.syrus.AMFICOM.configuration.corba.MCM_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.util.database.DatabaseConnection;

public class Setup {

	public Setup() {
	}

	public static void main(String[] args) {
		try {
			DatabaseConnection.establishConnection("mongol", "mongol", 60000, "amficom");
			DatabaseSetup.initDatabaseContext();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		//createAllParameterTypes();
		//createAllMeasurementTypes();
		//createAllAnalysisTypes();
		//createAllEvaluationTypes();
		//createConfiguration();
	}

	private static void createAllMeasurementTypes() {
		String[] in_par_codenames = new String[6];
		in_par_codenames[0] = "ref_wvlen";
		in_par_codenames[1] = "ref_trclen";
		in_par_codenames[2] = "ref_res";
		in_par_codenames[3] = "ref_pulswd";
		in_par_codenames[4] = "ref_ior";
		in_par_codenames[5] = "ref_scans";
		String[] out_par_codenames = new String[1];
		out_par_codenames[0] = "reflectogramma";
		createMeasurementType("reflectometry",
													"Рефлектометрия, все дела, вся фигня",
													in_par_codenames,
													out_par_codenames);
	}

	private static void createAllAnalysisTypes() {
		String[] in_par_codenames = new String[1];
		in_par_codenames[0] = "reflectogramma";
		String[] criteria_par_codenames = new String[9];
		criteria_par_codenames[0] = "dadara_tactic";
		criteria_par_codenames[1] = "dadara_eventsize";
		criteria_par_codenames[2] = "dadara_conn_fall_params";
		criteria_par_codenames[3] = "dadara_min_level";
		criteria_par_codenames[4] = "dadara_max_level_noise";
		criteria_par_codenames[5] = "dadara_min_level_to_find_end";
		criteria_par_codenames[6] = "dadara_min_weld";
		criteria_par_codenames[7] = "dadara_min_connector";
		criteria_par_codenames[8] = "dadara_strategy";
		String[] out_par_codenames = new String[1];
		out_par_codenames[0] = "dadara_event_array";
		createAnalysisType("dadara",
											 "Дескрайб эз Саша хрен его знает как",
											 in_par_codenames,
											 criteria_par_codenames,
											 out_par_codenames);
	}

	private static void createAllEvaluationTypes() {
		String[] in_par_codenames = new String[1];
		in_par_codenames[0] = "reflectogramma";
		String[] threshold_par_codenames = new String[1];
		threshold_par_codenames[0] = "dadara_event_array";
		String[] etalon_par_codenames = new String[1];
		etalon_par_codenames[0] = "dadara_etalon_event_array";
		String[] out_par_codenames = new String[1];
		out_par_codenames[0] = "dadara_alarm_array";
		createEvaluationType("dadara",
												 "Дескрайб эз Саша хрен его знает как",
												 in_par_codenames,
												 threshold_par_codenames,
												 etalon_par_codenames,
												 out_par_codenames);
	}

	private static void createAllParameterTypes() {
		createParameterType("ref_wvlen", "Длина волны", "Для рефлектометрических измерений; нм");
		createParameterType("ref_trclen", "Длина рефлектограммы", "Для рефлектометрических измерений; км");
		createParameterType("ref_res", "Разрешение", "Для рефлектометрических измерений; м");
		createParameterType("ref_pulswd", "Ширина импульса", "Для рефлектометрических измерений; нс");
		createParameterType("ref_ior", "Показатель преломления", "Для рефлектометрических измерений");
		createParameterType("ref_scans", "Количество усреднений", "Для рефлектометрических измерений");

		createParameterType("reflectogramma", "Рефлектограмма", "Получена в результате рефлектометрических измерений");

		createParameterType("dadara_tactic", "Тактика анализа", "DADARA");
		createParameterType("dadara_eventsize", "Характерный размер события", "DADARA");
		createParameterType("dadara_conn_fall_params", "Характерный спад на коннекторе", "DADARA");
		createParameterType("dadara_min_level", "Минимальный уровень", "DADARA");
		createParameterType("dadara_max_level_noise", "Максимальный уровень шума", "DADARA");
		createParameterType("dadara_min_level_to_find_end", "Минимальный уровень для определения конца", "DADARA");
		createParameterType("dadara_min_weld", "Минимальный уровень для определения сварки", "DADARA");
		createParameterType("dadara_min_connector", "Минимальный уровень для определения коннектора", "DADARA");
		createParameterType("dadara_strategy", "Стратегия анализа", "DADARA");

		createParameterType("dadara_event_array", "Массив событий на рефлектограмме", "Получены в результате анализа DADARA");

		createParameterType("dadara_thresholds", "Пороги для рефлектограммы", "DADARA");
		createParameterType("dadara_etalon_event_array", "Эталонный массив событий на рефлектограмме", "DADARA");

		createParameterType("dadara_alarm_array", "Массив отклонений от эталона на рефлектограмме", "DADARA");

	}

	private static void deleteMeasurementType(String codename) {
		try {
			MeasurementType mt = MeasurementType_Database.retrieveForCodename(codename);
			(new MeasurementType_Database()).delete(mt);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createMeasurementType(String codename,
																						String description,
																						String[] in_par_codenames,
																						String[] out_par_codenames) {
		try {
			long[] in_par_typ_id_codes = new long[in_par_codenames.length];
			for (int i = 0; i < in_par_typ_id_codes.length; i++)
				in_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(in_par_codenames[i]).getId().getCode();

			long[] out_par_typ_id_codes = new long[out_par_codenames.length];
			for (int i = 0; i < out_par_typ_id_codes.length; i++)
				out_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(out_par_codenames[i]).getId().getCode();

			Identifier id = IdentifierGenerator.generateIdentifier(DatabaseSetup.MEASUREMENTTYPE_TABLE);

			MeasurementType_Transferable mtt = new MeasurementType_Transferable(id.getCode(),
																																					codename,
																																					description,
																																					in_par_typ_id_codes,
																																					out_par_typ_id_codes);
			MeasurementType measurementType = new MeasurementType(mtt);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createAnalysisType(String codename,
																				 String description,
																				 String[] in_par_codenames,
																				 String[] criteria_par_codenames,
																				 String[] out_par_codenames) {
		try {
			long[] in_par_typ_id_codes = new long[in_par_codenames.length];
			for (int i = 0; i < in_par_typ_id_codes.length; i++)
				in_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(in_par_codenames[i]).getId().getCode();

			long[] criteria_par_typ_id_codes = new long[criteria_par_codenames.length];
			for (int i = 0; i < criteria_par_typ_id_codes.length; i++)
				criteria_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(criteria_par_codenames[i]).getId().getCode();

			long[] out_par_typ_id_codes = new long[out_par_codenames.length];
			for (int i = 0; i < out_par_typ_id_codes.length; i++)
				out_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(out_par_codenames[i]).getId().getCode();

			Identifier id = IdentifierGenerator.generateIdentifier(DatabaseSetup.ANALYSISTYPE_TABLE);

			AnalysisType_Transferable att = new AnalysisType_Transferable(id.getCode(),
																																		codename,
																																		description,
																																		in_par_typ_id_codes,
																																		criteria_par_typ_id_codes,
																																		out_par_typ_id_codes);
			AnalysisType analysisType = new AnalysisType(att);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createEvaluationType(String codename,
																					 String description,
																					 String[] in_par_codenames,
																					 String[] threshold_par_codenames,
																					 String[] etalon_par_codenames,
																					 String[] out_par_codenames) {
		try {
			long[] in_par_typ_id_codes = new long[in_par_codenames.length];
			for (int i = 0; i < in_par_typ_id_codes.length; i++)
				in_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(in_par_codenames[i]).getId().getCode();

			long[] threshold_par_typ_id_codes = new long[threshold_par_codenames.length];
			for (int i = 0; i < threshold_par_typ_id_codes.length; i++)
				threshold_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(threshold_par_codenames[i]).getId().getCode();

			long[] etalon_par_typ_id_codes = new long[etalon_par_codenames.length];
			for (int i = 0; i < etalon_par_typ_id_codes.length; i++)
				etalon_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(etalon_par_codenames[i]).getId().getCode();

			long[] out_par_typ_id_codes = new long[out_par_codenames.length];
			for (int i = 0; i < out_par_typ_id_codes.length; i++)
				out_par_typ_id_codes[i] = ParameterType_Database.retrieveForCodename(out_par_codenames[i]).getId().getCode();

			Identifier id = IdentifierGenerator.generateIdentifier(DatabaseSetup.EVALUATIONTYPE_TABLE);

			EvaluationType_Transferable ett = new EvaluationType_Transferable(id.getCode(),
																																				codename,
																																				description,
																																				in_par_typ_id_codes,
																																				threshold_par_typ_id_codes,
																																				etalon_par_typ_id_codes,
																																				out_par_typ_id_codes);
			EvaluationType evaluationType = new EvaluationType(ett);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createParameterType(String codename,
																					String name,
																					String description) {
		Identifier id = IdentifierGenerator.generateIdentifier(DatabaseSetup.PARAMETERTYPE_TABLE);
		ParameterType_Transferable ptt = new ParameterType_Transferable(id.getCode(),
																																		codename,
																																		name,
																																		description);
		try {
			ParameterType parameterType = new ParameterType(ptt);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void createConfiguration() {
		try {
			Identifier server_id = IdentifierGenerator.generateIdentifier(DatabaseSetup.SERVER_TABLE);
			Server_Transferable st = new Server_Transferable(server_id.getCode(),
																											 "localhost",
																											 "server-1",
																											 "Первый сервер");
			Server server = new Server(st);

			Identifier mcm_id = IdentifierGenerator.generateIdentifier(DatabaseSetup.MCM_TABLE);
			MCM_Transferable mt = new MCM_Transferable(mcm_id.getCode(),
																								 (new Server(server_id)).getId().getCode(),
																								 "localhost",
																								 "mcm-1",
																								 "Модуль управления измерениями");
			MCM mcm = new MCM(mt);

			Identifier kis_id = IdentifierGenerator.generateIdentifier(DatabaseSetup.KIS_TABLE);
			KIS_Transferable kt = new KIS_Transferable(kis_id.getCode(),
																								 (new MCM(mcm_id)).getId().getCode(),
																								 "kis-1",
																								 "Для тестирования");
			KIS kis = new KIS(kt);

			Identifier me_id = IdentifierGenerator.generateIdentifier(DatabaseSetup.ME_TABLE);
			MonitoredElement_Transferable met = new MonitoredElement_Transferable(me_id.getCode(),
																																						(new KIS(kis_id)).getId().getCode(),
																																						"SW=01:06");
			MonitoredElement me = new MonitoredElement(met);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
