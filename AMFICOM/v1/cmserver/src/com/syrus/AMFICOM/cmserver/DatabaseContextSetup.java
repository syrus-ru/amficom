/*
 * $Id: DatabaseContextSetup.java,v 1.13 2004/12/23 11:16:11 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.DomainDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.MCMDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.ServerDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.configuration.UserDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ModelingDatabase;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.13 $, $Date: 2004/12/23 11:16:11 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public abstract class DatabaseContextSetup {
	
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";
	public static final String KEY_REFRESH_TIMEOUT = "RefreshTimeout";
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int DEFAULT_CONFIGURATION_POOL_SIZE = 1000;
	public static final int DEFAULT_MEASUREMENT_POOL_SIZE = 1000;
	public static final int DEFAULT_REFRESH_TIMEOUT = 5;
	public static final String DEFAULT_DATABASE_LOADER_ONLY = "false";

	private DatabaseContextSetup() {
		// empty
	}

	public static void initDatabaseContext() {
		ConfigurationDatabaseContext.init(new CharacteristicTypeDatabase(),
				new EquipmentTypeDatabase(),
				new PortTypeDatabase(),
				new MeasurementPortTypeDatabase(),
				new LinkTypeDatabase(),
				new CableLinkTypeDatabase(),
				new CableThreadTypeDatabase(),
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
				new LinkDatabase(),
				new CableThreadDatabase());
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
		boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DEFAULT_DATABASE_LOADER_ONLY)).booleanValue();
		int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, DEFAULT_CONFIGURATION_POOL_SIZE);
		int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, DEFAULT_MEASUREMENT_POOL_SIZE);
		if (! databaseLoaderOnly) {
			long refreshTimeout = ApplicationProperties.getInt(KEY_REFRESH_TIMEOUT, DEFAULT_REFRESH_TIMEOUT) * 1000L * 60L;
			ConfigurationStorableObjectPool.init(new CMServerConfigurationObjectLoader(refreshTimeout), configurationPoolSize);
			MeasurementStorableObjectPool.init(new CMServerMeasurementObjectLoader(refreshTimeout), measurementPoolSize);
		}
		else {
			ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(), configurationPoolSize);
			MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(), measurementPoolSize);
		}
	}
}
