package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.measurement.Set_Database;
import com.syrus.AMFICOM.measurement.MeasurementSetup_Database;
import com.syrus.AMFICOM.measurement.Measurement_Database;
import com.syrus.AMFICOM.measurement.Analysis_Database;
import com.syrus.AMFICOM.measurement.Evaluation_Database;
import com.syrus.AMFICOM.measurement.Test_Database;
import com.syrus.AMFICOM.measurement.Result_Database;
import com.syrus.AMFICOM.measurement.PTTemporalTemplate_Database;
import com.syrus.AMFICOM.configuration.MonitoredElement_Database;
import com.syrus.AMFICOM.configuration.KIS_Database;
import com.syrus.AMFICOM.configuration.MCM_Database;
import com.syrus.AMFICOM.configuration.Server_Database;
import com.syrus.AMFICOM.measurement.ParameterType_Database;
import com.syrus.AMFICOM.measurement.MeasurementType_Database;
import com.syrus.AMFICOM.measurement.AnalysisType_Database;
import com.syrus.AMFICOM.measurement.EvaluationType_Database;

public class DatabaseSetup {
	public static Set_Database setDatabase;
	public static MeasurementSetup_Database measurementSetupDatabase;
	public static Measurement_Database measurementDatabase;
	public static Analysis_Database analysisDatabase;
	public static Evaluation_Database evaluationDatabase;
	public static Test_Database testDatabase;
	public static Result_Database resultDatabase;
	public static PTTemporalTemplate_Database ptTemporalTemplateDatabase;

	public static MonitoredElement_Database monitoredElementDatabase;
	public static KIS_Database kisDatabase;
	public static MCM_Database mcmDatabase;
	public static Server_Database serverDatabase;

	public static ParameterType_Database parameterTypeDatabase;
	public static MeasurementType_Database measurementTypeDatabase;
	public static AnalysisType_Database analysisTypeDatabase;
	public static EvaluationType_Database evaluationTypeDatabase;
/*
	private static final String SET_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.Set_Database";
	private static final String MEASUREMENTSETUP_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.MeasurementSetup_Database";
	private static final String MEASUREMENT_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.Measurement_Database";
	private static final String ANALYSIS_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.Analysis_Database";
	private static final String EVALUATION_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.Evaluation_Database";
	private static final String TEST_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.Test_Database";
	private static final String RESULT_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.Result_Database";
	private static final String PTTT_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.PTTemporalTemplate_Database";
	private static final String MONITOREDELEMENT_DATABASE_CLASS = "com.syrus.AMFICOM.configuration.MonitoredElement_Database";
	private static final String KIS_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.KIS_Database";
	private static final String MCM_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.MCM_Database";
	private static final String SERVER_DATABASE_CLASS = "com.syrus.AMFICOM.measurement.Server_Database";*/

	public static void initDatabaseContext() {
		setDatabase = new Set_Database();
		measurementSetupDatabase = new MeasurementSetup_Database();
		measurementDatabase = new Measurement_Database();
		analysisDatabase = new Analysis_Database();
		evaluationDatabase = new Evaluation_Database();
		testDatabase = new Test_Database();
		resultDatabase = new Result_Database();
		ptTemporalTemplateDatabase = new PTTemporalTemplate_Database();

		monitoredElementDatabase = new MonitoredElement_Database();
		kisDatabase = new KIS_Database();
		mcmDatabase = new MCM_Database();
		serverDatabase = new Server_Database();

		parameterTypeDatabase = new ParameterType_Database();
		measurementTypeDatabase = new MeasurementType_Database();
		analysisTypeDatabase = new AnalysisType_Database();
		evaluationTypeDatabase = new EvaluationType_Database();
		
		StorableObject_DatabaseContext.init(setDatabase,
																				measurementSetupDatabase,
																				measurementDatabase,
																				analysisDatabase,
																				evaluationDatabase,
																				testDatabase,
																				resultDatabase,
																				ptTemporalTemplateDatabase,
																				monitoredElementDatabase,
																				kisDatabase,
																				mcmDatabase,
																				serverDatabase,
																				parameterTypeDatabase,
																				measurementTypeDatabase,
																				analysisTypeDatabase,
																				evaluationTypeDatabase);
	}
}