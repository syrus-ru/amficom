/*
 * $Id: DatabaseContextSetup.java,v 1.7 2004/11/18 12:25:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KISTypeDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.configuration.UserDatabase;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.ServerDatabase;
import com.syrus.AMFICOM.configuration.MCMDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;

import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.ModelingDatabase;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.7 $, $Date: 2004/11/18 12:25:17 $
 * @author $Author: bob $
 * @module mserver_v1
 */

public abstract class DatabaseContextSetup {
	
	public static final String CONFIGURATION_POOL_SIZE_KEY = "ConfigurationPoolSize";
	public static final String MEASUREMENT_POOL_SIZE_KEY = "MeasurementPoolSize";

	private DatabaseContextSetup() {
		// empty
	}

	public static void initDatabaseContext() {
		ConfigurationDatabaseContext.init(new CharacteristicTypeDatabase(),
																			new EquipmentTypeDatabase(),
																			new PortTypeDatabase(),
																			new MeasurementPortTypeDatabase(),
                                                                            new LinkTypeDatabase(),
                                                                            new KISTypeDatabase(),
																			new CharacteristicDatabase(),
																			new UserDatabase(),
																			new DomainDatabase(),
																			new ServerDatabase(), 
																			new MCMDatabase(),
																			new EquipmentDatabase(),
																			new PortDatabase(),
																			new MeasurementPortDatabase(),
																			new TransmissionPathDatabase(),
                                                                            new TransmissionPathTypeDatabase(),
																			new KISDatabase(),
																			new MonitoredElementDatabase(),
                                                                            new LinkDatabase());
		MeasurementDatabaseContext.init(new ParameterTypeDatabase(),
																		new MeasurementTypeDatabase(),
																		new AnalysisTypeDatabase(),
																		new EvaluationTypeDatabase(),
																		new SetDatabase(),
																		new ModelingDatabase(),
																		new MeasurementSetupDatabase(),
																		new MeasurementDatabase(),
																		new AnalysisDatabase(),
																		new EvaluationDatabase(),
																		new TestDatabase(),
																		new ResultDatabase(),
																		new TemporalPatternDatabase());
	}

	public static void initObjectPools() {
		ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(), ApplicationProperties.getInt(CONFIGURATION_POOL_SIZE_KEY, 1000));
		MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(), ApplicationProperties.getInt(MEASUREMENT_POOL_SIZE_KEY, 1000));		
	}
}
