/*
 * $Id: DatabaseContextSetup.java,v 1.5 2005/04/25 09:50:13 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
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
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.CronTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/25 09:50:13 $
 * @author $Author: bob $
 * @module cmserver_v1
 */

class DatabaseContextSetup {
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";
	public static final String KEY_REFRESH_TIMEOUT = "RefreshTimeout";
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	public static final int DEFAULT_CONFIGURATION_POOL_SIZE = 1000;
	public static final int DEFAULT_MEASUREMENT_POOL_SIZE = 1000;
	public static final int DEFAULT_REFRESH_TIMEOUT = 5;
	public static final String DEFAULT_DATABASE_LOADER_ONLY = "false";

	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		AdministrationDatabaseContext.init(new UserDatabase(),
				new DomainDatabase(),
				new ServerDatabase(),
				new MCMDatabase());
		
		GeneralDatabaseContext.init(new ParameterTypeDatabase(),
				new CharacteristicTypeDatabase(),
				new CharacteristicDatabase());

		ConfigurationDatabaseContext.init(
				new EquipmentTypeDatabase(),
				new PortTypeDatabase(),
				new MeasurementPortTypeDatabase(),
				new LinkTypeDatabase(),
				new CableLinkTypeDatabase(),
				new CableThreadTypeDatabase(),										  
				new EquipmentDatabase(),
				new PortDatabase(),
				new MeasurementPortDatabase(),
				new TransmissionPathDatabase(),
				new TransmissionPathTypeDatabase(),
				new KISDatabase(),
				new MonitoredElementDatabase(),
				new LinkDatabase(),
				new CableThreadDatabase());

		MeasurementDatabaseContext.init(new MeasurementTypeDatabase(),
				new AnalysisTypeDatabase(),
				new EvaluationTypeDatabase(),
				new ModelingTypeDatabase(),
				new SetDatabase(),
				new ModelingDatabase(),
				new MeasurementSetupDatabase(),
				new MeasurementDatabase(),
				new AnalysisDatabase(),
				new EvaluationDatabase(),
				new TestDatabase(),
				new ResultDatabase(),
				new CronTemporalPatternDatabase(),
				new PeriodicalTemporalPatternDatabase());
	}

	public static void initObjectPools() {
		int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, DEFAULT_CONFIGURATION_POOL_SIZE);
		int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, DEFAULT_MEASUREMENT_POOL_SIZE);

		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader());
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader());

		ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(), configurationPoolSize);
		MeasurementStorableObjectPool.init(new DatabaseMeasurementObjectLoader(), measurementPoolSize);
	}
}
