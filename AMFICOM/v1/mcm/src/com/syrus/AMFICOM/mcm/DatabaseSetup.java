package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;

public abstract class DatabaseSetup {
	public static void initDatabaseContext() {
		ConfigurationDatabaseContext.init(new CharacteristicDatabase(),
																			new CharacteristicTypeDatabase(),
																			null,
																			null,
																			null,
																			null);
		MeasurementDatabaseContext.init(new AnalysisDatabase(),
																		new AnalysisTypeDatabase(),
																		new EvaluationDatabase(),
																		new EvaluationTypeDatabase(),
																		new MeasurementDatabase(),
																		new MeasurementSetupDatabase(),
																		new MeasurementTypeDatabase(),
																		new ParameterTypeDatabase(),
																		new ResultDatabase(),
																		new SetDatabase(),
																		new TemporalPatternDatabase(),
																		new TestDatabase());
	}
}
