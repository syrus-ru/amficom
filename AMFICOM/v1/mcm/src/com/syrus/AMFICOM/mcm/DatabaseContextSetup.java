/*
 * $Id: DatabaseContextSetup.java,v 1.2 2004/08/02 05:36:28 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.List;
import java.util.ArrayList;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.configuration.ConfigurationObjectTypePool;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.MCMDatabase;
import com.syrus.AMFICOM.measurement.MeasurementObjectTypePool;
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2004/08/02 05:36:28 $
 * @author $Author: bob $
 * @module mcm_v1
 */

public abstract class DatabaseContextSetup {

	private DatabaseContextSetup() {
	}

	public static void initDatabaseContext() {
		ConfigurationDatabaseContext.init(new CharacteristicDatabase(),
																			new CharacteristicTypeDatabase(),
																			null, // portDatabase
																			new MonitoredElementDatabase(),
																			new KISDatabase(),
																			new EquipmentTypeDatabase(),
																			new MCMDatabase(),
																			null // serverDatabase
																			);
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
	
	public static void loadObjectTypes() {
		loadConfigurationObjectTypes();
		loadMeasurementObjectTypes();
	}

	private static void loadConfigurationObjectTypes() {
		List objectTypes = new ArrayList();
		List types;

		types = loadCharacteristicTypes();
		if (types != null)
			objectTypes.addAll(types);

		types = loadEquipmentTypes();
		if (types != null)
			objectTypes.addAll(types);

		ConfigurationObjectTypePool.init(objectTypes);
	}

	private static void loadMeasurementObjectTypes() {
		List objectTypes = new ArrayList();
		List types;

		types = loadParameterTypes();
		if (types != null)
			objectTypes.addAll(types);

		types = loadMeasurementTypes();
		if (types != null)
			objectTypes.addAll(types);

		types = loadAnalysisTypes();
		if (types != null)
			objectTypes.addAll(types);

		types = loadEvaluationTypes();
		if (types != null)
			objectTypes.addAll(types);

		MeasurementObjectTypePool.init(objectTypes);
	}

	private static List loadCharacteristicTypes() {
		List characteristicTypes = null;
		try {
			characteristicTypes = CharacteristicTypeDatabase.retrieveAll();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		return characteristicTypes;
	}

	private static List loadEquipmentTypes() {
		List equipmentTypes = null;
		try {
			equipmentTypes = EquipmentTypeDatabase.retrieveAll();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		return equipmentTypes;
	}

	private static List loadParameterTypes() {
		List parameterTypes = null;
		try {
			parameterTypes = ParameterTypeDatabase.retrieveAll();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		return parameterTypes;
	}

	private static List loadMeasurementTypes() {
		List measurementTypes = null;
		try {
			measurementTypes = MeasurementTypeDatabase.retrieveAll();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		return measurementTypes;
	}

	private static List loadAnalysisTypes() {
		List analysisTypes = null;
		try {
			analysisTypes = AnalysisTypeDatabase.retrieveAll();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		return analysisTypes;
	}

	private static List loadEvaluationTypes() {
		List evaluationTypes = null;
		try {
			evaluationTypes = EvaluationTypeDatabase.retrieveAll();
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
		}
		return evaluationTypes;
	}
}
