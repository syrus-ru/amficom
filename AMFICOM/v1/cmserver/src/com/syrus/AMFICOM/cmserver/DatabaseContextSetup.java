/*
 * $Id: DatabaseContextSetup.java,v 1.17 2005/02/22 14:14:07 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.cmserver;

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
import com.syrus.AMFICOM.measurement.ModelingTypeDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.17 $, $Date: 2005/02/22 14:14:07 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public abstract class DatabaseContextSetup {
	
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize";
	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize";
	public static final String KEY_REFRESH_TIMEOUT = "RefreshTimeout";
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	
	public static final int DEFAULT_GENERAL_POOL_SIZE = 1000;
	public static final int DEFAULT_ADMINISTRATION_POOL_SIZE = 1000;
	public static final int DEFAULT_CONFIGURATION_POOL_SIZE = 1000;
	public static final int DEFAULT_MEASUREMENT_POOL_SIZE = 1000;
	public static final int DEFAULT_REFRESH_TIMEOUT = 5;
	public static final String DEFAULT_DATABASE_LOADER_ONLY = "false";

	private DatabaseContextSetup() {
		// singleton constructor
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
				new TemporalPatternDatabase());
	}

	public static void initObjectPools() {
		boolean databaseLoaderOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_DATABASE_LOADER_ONLY, DEFAULT_DATABASE_LOADER_ONLY)).booleanValue();
		
		int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, DEFAULT_GENERAL_POOL_SIZE);
		int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, DEFAULT_ADMINISTRATION_POOL_SIZE);
		int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, DEFAULT_CONFIGURATION_POOL_SIZE);
		int measurementPoolSize = ApplicationProperties.getInt(KEY_MEASUREMENT_POOL_SIZE, DEFAULT_MEASUREMENT_POOL_SIZE);

		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), generalPoolSize);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), administrationPoolSize);		

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
