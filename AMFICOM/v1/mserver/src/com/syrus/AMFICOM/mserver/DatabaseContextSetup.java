/*
 * $Id: DatabaseContextSetup.java,v 1.4 2004/08/10 19:00:15 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.configuration.ConfigurationObjectTypePool;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.UserDatabase;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.ServerDatabase;
import com.syrus.AMFICOM.configuration.MCMDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
//import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;

//import com.syrus.AMFICOM.configuration.ServerDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
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
 * @version $Revision: 1.4 $, $Date: 2004/08/10 19:00:15 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public abstract class DatabaseContextSetup {

	private DatabaseContextSetup() {
	}

	public static void initDatabaseContext() {
		ConfigurationDatabaseContext.init(new CharacteristicTypeDatabase(),
																			new EquipmentTypeDatabase(),
																			new CharacteristicDatabase(),
																			new UserDatabase(),
																			new DomainDatabase(),
																			new ServerDatabase(),
																			new MCMDatabase(),
																			new EquipmentDatabase(),
																			null,	//PortDatabase
																			new TransmissionPathDatabase(),
																			new KISDatabase(),
																			new MonitoredElementDatabase());
		MeasurementDatabaseContext.init(new ParameterTypeDatabase(),
																		new MeasurementTypeDatabase(),
																		new AnalysisTypeDatabase(),
																		new EvaluationTypeDatabase(),
																		new SetDatabase(),
																		new MeasurementSetupDatabase(),
																		new MeasurementDatabase(),
																		new AnalysisDatabase(),
																		new EvaluationDatabase(),
																		new TestDatabase(),
																		new ResultDatabase(),
																		new TemporalPatternDatabase());
	}

	public static void initObjectPools() {
		MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader());
	}
}
