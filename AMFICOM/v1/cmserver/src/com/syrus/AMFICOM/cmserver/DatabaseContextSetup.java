/*
 * $Id: DatabaseContextSetup.java,v 1.45.2.3 2006/04/28 13:27:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.PermissionAttributesDatabase;
import com.syrus.AMFICOM.administration.RoleDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.configuration.CableLinkDatabase;
import com.syrus.AMFICOM.configuration.CableLinkTypeDatabase;
import com.syrus.AMFICOM.configuration.CableThreadDatabase;
import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.LinkDatabase;
import com.syrus.AMFICOM.configuration.LinkTypeDatabase;
import com.syrus.AMFICOM.configuration.PortDatabase;
import com.syrus.AMFICOM.configuration.PortTypeDatabase;
import com.syrus.AMFICOM.configuration.ProtoEquipmentDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathDatabase;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeDatabase;
import com.syrus.AMFICOM.event.DeliveryAttributesDatabase;
import com.syrus.AMFICOM.event.EventDatabase;
import com.syrus.AMFICOM.event.EventSourceDatabase;
import com.syrus.AMFICOM.event.EventTypeDatabase;
import com.syrus.AMFICOM.eventv2.LineMismatchEventDatabase;
import com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.ActionParameterDatabase;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBindingDatabase;
import com.syrus.AMFICOM.measurement.ActionTemplateDatabase;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisResultParameterDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.KISDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementResultParameterDatabase;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ModelingDatabase;
import com.syrus.AMFICOM.measurement.ModelingResultParameterDatabase;
import com.syrus.AMFICOM.measurement.ModelingTypeDatabase;
import com.syrus.AMFICOM.measurement.MonitoredElementDatabase;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.report.AttachedTextDatabase;
import com.syrus.AMFICOM.report.DataDatabase;
import com.syrus.AMFICOM.report.ImageDatabase;
import com.syrus.AMFICOM.report.ReportTemplateDatabase;
import com.syrus.AMFICOM.report.TableDataDatabase;

/**
 * @version $Revision: 1.45.2.3 $, $Date: 2006/04/28 13:27:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module cmserver
 */

final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new ParameterTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicDatabase());

		DatabaseContext.registerDatabase(new SystemUserDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new PermissionAttributesDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new MCMDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());
		DatabaseContext.registerDatabase(new RoleDatabase());

		DatabaseContext.registerDatabase(new EquipmentTypeDatabase());
		DatabaseContext.registerDatabase(new PortTypeDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathTypeDatabase());
		DatabaseContext.registerDatabase(new LinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableLinkTypeDatabase());
		DatabaseContext.registerDatabase(new CableThreadTypeDatabase());
		DatabaseContext.registerDatabase(new ProtoEquipmentDatabase());
		DatabaseContext.registerDatabase(new EquipmentDatabase());
		DatabaseContext.registerDatabase(new PortDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathDatabase());
		DatabaseContext.registerDatabase(new LinkDatabase());
		DatabaseContext.registerDatabase(new CableLinkDatabase());
		DatabaseContext.registerDatabase(new CableThreadDatabase());

		DatabaseContext.registerDatabase(new MeasurementPortTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementTypeDatabase());
		DatabaseContext.registerDatabase(new AnalysisTypeDatabase());
		DatabaseContext.registerDatabase(new ModelingTypeDatabase());
		DatabaseContext.registerDatabase(new ActionParameterTypeBindingDatabase());
		DatabaseContext.registerDatabase(new KISDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortDatabase());
		DatabaseContext.registerDatabase(new MonitoredElementDatabase());
		DatabaseContext.registerDatabase(new ActionParameterDatabase());
		DatabaseContext.registerDatabase(new ActionTemplateDatabase());
		DatabaseContext.registerDatabase(new PeriodicalTemporalPatternDatabase());
		DatabaseContext.registerDatabase(new TestDatabase());
		DatabaseContext.registerDatabase(new MeasurementSetupDatabase());
		DatabaseContext.registerDatabase(new MeasurementDatabase());
		DatabaseContext.registerDatabase(new AnalysisDatabase());
		DatabaseContext.registerDatabase(new ModelingDatabase());
		DatabaseContext.registerDatabase(new MeasurementResultParameterDatabase());
		DatabaseContext.registerDatabase(new AnalysisResultParameterDatabase());
		DatabaseContext.registerDatabase(new ModelingResultParameterDatabase());

		DatabaseContext.registerDatabase(new EventTypeDatabase());
		DatabaseContext.registerDatabase(new EventDatabase());
		DatabaseContext.registerDatabase(new EventSourceDatabase());
		DatabaseContext.registerDatabase(new DeliveryAttributesDatabase());
		DatabaseContext.registerDatabase(new ReflectogramMismatchEventDatabase());
		DatabaseContext.registerDatabase(new LineMismatchEventDatabase());

		DatabaseContext.registerDatabase(new AttachedTextDatabase());
		DatabaseContext.registerDatabase(new DataDatabase());
		DatabaseContext.registerDatabase(new TableDataDatabase());
		DatabaseContext.registerDatabase(new ImageDatabase());
		DatabaseContext.registerDatabase(new ReportTemplateDatabase());
	}
}
