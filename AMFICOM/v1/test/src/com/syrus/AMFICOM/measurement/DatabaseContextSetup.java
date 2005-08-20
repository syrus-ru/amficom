/*
 * $Id: DatabaseContextSetup.java,v 1.4 2005/08/20 19:40:40 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/20 19:40:40 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public class DatabaseContextSetup {
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
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicDatabase());

		DatabaseContext.registerDatabase(new SystemUserDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new MCMDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());

		DatabaseContext.registerDatabase(new EquipmentTypeDatabase());
		DatabaseContext.registerDatabase(new PortTypeDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathTypeDatabase());
		DatabaseContext.registerDatabase(new LinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableLinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableThreadTypeDatabase());
		DatabaseContext.registerDatabase(new EquipmentDatabase());
		DatabaseContext.registerDatabase(new PortDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathDatabase());
		DatabaseContext.registerDatabase(new LinkDatabase());
		DatabaseContext.registerDatabase(new CableThreadDatabase());

		DatabaseContext.registerDatabase(new MeasurementTypeDatabase());
		DatabaseContext.registerDatabase(new AnalysisTypeDatabase());
		DatabaseContext.registerDatabase(new EvaluationTypeDatabase());
		DatabaseContext.registerDatabase(new ModelingTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementDatabase());
		DatabaseContext.registerDatabase(new AnalysisDatabase());
		DatabaseContext.registerDatabase(new EvaluationDatabase());
		DatabaseContext.registerDatabase(new ModelingDatabase());
		DatabaseContext.registerDatabase(new MeasurementSetupDatabase());
		DatabaseContext.registerDatabase(new ResultDatabase());
		DatabaseContext.registerDatabase(new ParameterSetDatabase());
		DatabaseContext.registerDatabase(new TestDatabase());
		DatabaseContext.registerDatabase(new CronTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new IntervalsTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new PeriodicalTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortDatabase());
		DatabaseContext.registerDatabase(new KISDatabase());
		DatabaseContext.registerDatabase(new MonitoredElementDatabase());
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
