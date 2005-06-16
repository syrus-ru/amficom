/*
 * $Id: DatabaseContextSetup.java,v 1.28 2005/05/26 08:33:30 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
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
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.CronTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;


/**
 * @version $Revision: 1.28 $, $Date: 2005/05/26 08:33:30 $
 * @author $Author: bass $
 * @module mcm_v1
 */

final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new AnalysisDatabase());
		DatabaseContext.registerDatabase(new AnalysisTypeDatabase());
		DatabaseContext.registerDatabase(new CableLinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableThreadDatabase());
		DatabaseContext.registerDatabase(new CableThreadTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicDatabase());
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());
		DatabaseContext.registerDatabase(new CronTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new EquipmentDatabase());
		DatabaseContext.registerDatabase(new EquipmentTypeDatabase());
		DatabaseContext.registerDatabase(new EvaluationDatabase());
		DatabaseContext.registerDatabase(new EvaluationTypeDatabase());
		DatabaseContext.registerDatabase(new IntervalsTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new KISDatabase());
		DatabaseContext.registerDatabase(new LinkDatabase());
		DatabaseContext.registerDatabase(new LinkTypeDatabase());
		DatabaseContext.registerDatabase(new MCMDatabase());
		DatabaseContext.registerDatabase(new MeasurementDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementSetupDatabase());
		DatabaseContext.registerDatabase(new MeasurementTypeDatabase());
		DatabaseContext.registerDatabase(new MonitoredElementDatabase());
		DatabaseContext.registerDatabase(new ParameterTypeDatabase());
		DatabaseContext.registerDatabase(new PeriodicalTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new PortDatabase());
		DatabaseContext.registerDatabase(new PortTypeDatabase());
		DatabaseContext.registerDatabase(new ResultDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());
		DatabaseContext.registerDatabase(new SetDatabase());
		DatabaseContext.registerDatabase(new TestDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathTypeDatabase());
		DatabaseContext.registerDatabase(new UserDatabase());
	}
}
