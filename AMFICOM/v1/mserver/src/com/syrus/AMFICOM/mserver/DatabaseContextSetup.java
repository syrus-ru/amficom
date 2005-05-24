/*
 * $Id: DatabaseContextSetup.java,v 1.31 2005/05/24 13:24:56 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import gnu.trove.TShortObjectHashMap;

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
import com.syrus.AMFICOM.general.ObjectEntities;
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
 * @version $Revision: 1.31 $, $Date: 2005/05/24 13:24:56 $
 * @author $Author: bass $
 * @module mserver_v1
 */

final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		final TShortObjectHashMap entityCodeDatabaseMap = new TShortObjectHashMap();
		entityCodeDatabaseMap.put(ObjectEntities.ANALYSIS_ENTITY_CODE, new AnalysisDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.ANALYSISTYPE_ENTITY_CODE, new AnalysisTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CABLELINKTYPE_ENTITY_CODE, new CableLinkTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CABLETHREAD_ENTITY_CODE, new CableThreadDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CABLETHREADTYPE_ENTITY_CODE, new CableThreadTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new CharacteristicDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new CharacteristicTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE, new CronTemporalPatternDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.DOMAIN_ENTITY_CODE, new DomainDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EQUIPMENT_ENTITY_CODE, new EquipmentDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE, new EquipmentTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EVALUATION_ENTITY_CODE, new EvaluationDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE, new EvaluationTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE, new IntervalsTemporalPatternDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.KIS_ENTITY_CODE, new KISDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.LINK_ENTITY_CODE, new LinkDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.LINKTYPE_ENTITY_CODE, new LinkTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MCM_ENTITY_CODE, new MCMDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENT_ENTITY_CODE, new MeasurementDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENTPORT_ENTITY_CODE, new MeasurementPortDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE, new MeasurementPortTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE, new MeasurementSetupDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE, new MeasurementTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MONITORED_ELEMENT_ENTITY_CODE, new MonitoredElementDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PARAMETERTYPE_ENTITY_CODE, new ParameterTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE, new PeriodicalTemporalPatternDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PORT_ENTITY_CODE, new PortDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PORTTYPE_ENTITY_CODE, new PortTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.RESULT_ENTITY_CODE, new ResultDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SERVER_ENTITY_CODE, new ServerDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SERVERPROCESS_ENTITY_CODE, new ServerProcessDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SET_ENTITY_CODE, new SetDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.TEST_ENTITY_CODE, new TestDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.TRANSPATH_ENTITY_CODE, new TransmissionPathDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE, new TransmissionPathTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.USER_ENTITY_CODE, new UserDatabase());
		DatabaseContext.init(entityCodeDatabaseMap);
	}
}
