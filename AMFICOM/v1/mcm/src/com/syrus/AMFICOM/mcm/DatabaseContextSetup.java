/*
 * $Id: DatabaseContextSetup.java,v 1.13 2004/11/24 10:22:23 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

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
 * @version $Revision: 1.13 $, $Date: 2004/11/24 10:22:23 $
 * @author $Author: max $
 * @module mcm_v1
 */

public abstract class DatabaseContextSetup {

	private DatabaseContextSetup() {
	}

	public static void initDatabaseContext() {
		ConfigurationDatabaseContext.init(new CharacteristicTypeDatabase(),
										  new EquipmentTypeDatabase(),
										  new PortTypeDatabase(),
										  new MeasurementPortTypeDatabase(),
										  new LinkTypeDatabase(),
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
										  new LinkDatabase());
		
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
		MeasurementStorableObjectPool.init(new MCMMeasurementObjectLoader());
		ConfigurationStorableObjectPool.init(new MCMConfigurationObjectLoader());
	}
}
