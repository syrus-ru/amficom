/*
 * $Id: DatabaseContextSetup.java,v 1.21 2005/01/17 08:25:07 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.administration.AdministrationDatabaseContext;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
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
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;



/**
 * @version $Revision: 1.21 $, $Date: 2005/01/17 08:25:07 $
 * @author $Author: bob $
 * @module mserver_v1
 */

public abstract class DatabaseContextSetup {

	private DatabaseContextSetup() {
		// empty
	}

	public static void initDatabaseContext() {
		AdministrationDatabaseContext.init(new UserDatabase(),
			new DomainDatabase(),
			new ServerDatabase(), 
			new MCMDatabase());
		
		GeneralDatabaseContext.init(new CharacteristicTypeDatabase(),
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

		MeasurementDatabaseContext.init(new ParameterTypeDatabase(),
			new MeasurementTypeDatabase(),
			new AnalysisTypeDatabase(),
			new EvaluationTypeDatabase(),
			new SetDatabase(),
			null,
			new MeasurementSetupDatabase(),
			new MeasurementDatabase(),
			new AnalysisDatabase(),
			new EvaluationDatabase(),
			new TestDatabase(),
			new ResultDatabase(),
			new TemporalPatternDatabase());
	}

	public static void initObjectPools() {
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader());
		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader());
		MeasurementStorableObjectPool.init(new MServerMeasurementObjectLoader());
		ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader());
	}
}
