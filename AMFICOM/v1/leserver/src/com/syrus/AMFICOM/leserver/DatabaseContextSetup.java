/*-
 * $Id: DatabaseContextSetup.java,v 1.22.2.1 2006/03/31 08:11:17 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.MCMDatabase;
import com.syrus.AMFICOM.administration.PermissionAttributesDatabase;
import com.syrus.AMFICOM.administration.RoleDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
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
import com.syrus.AMFICOM.measurement.KISDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MonitoredElementDatabase;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.scheme.CableChannelingItemDatabase;
import com.syrus.AMFICOM.scheme.PathElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeCableLinkDatabase;
import com.syrus.AMFICOM.scheme.SchemeCablePortDatabase;
import com.syrus.AMFICOM.scheme.SchemeCableThreadDatabase;
import com.syrus.AMFICOM.scheme.SchemeDatabase;
import com.syrus.AMFICOM.scheme.SchemeDeviceDatabase;
import com.syrus.AMFICOM.scheme.SchemeElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeLinkDatabase;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionDatabase;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoDatabase;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoRtuDatabase;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoSwitchDatabase;
import com.syrus.AMFICOM.scheme.SchemePathDatabase;
import com.syrus.AMFICOM.scheme.SchemePortDatabase;
import com.syrus.AMFICOM.scheme.SchemeProtoElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupDatabase;

/**
 * @version $Revision: 1.22.2.1 $, $Date: 2006/03/31 08:11:17 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module leserver
 */
final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());
		DatabaseContext.registerDatabase(new CharacteristicDatabase());

		DatabaseContext.registerDatabase(new SystemUserDatabase());
		DatabaseContext.registerDatabase(new PermissionAttributesDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new MCMDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());
		DatabaseContext.registerDatabase(new RoleDatabase());

		DatabaseContext.registerDatabase(new EquipmentDatabase());
		DatabaseContext.registerDatabase(new ProtoEquipmentDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathTypeDatabase());
		DatabaseContext.registerDatabase(new TransmissionPathDatabase());

		DatabaseContext.registerDatabase(new KISDatabase());
		DatabaseContext.registerDatabase(new MeasurementDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementSetupDatabase());
		DatabaseContext.registerDatabase(new MonitoredElementDatabase());
		DatabaseContext.registerDatabase(new TestDatabase());

		DatabaseContext.registerDatabase(new EventTypeDatabase());
		DatabaseContext.registerDatabase(new EventDatabase());
		DatabaseContext.registerDatabase(new EventSourceDatabase());
		DatabaseContext.registerDatabase(new DeliveryAttributesDatabase());
		DatabaseContext.registerDatabase(new ReflectogramMismatchEventDatabase());
		DatabaseContext.registerDatabase(new LineMismatchEventDatabase());

		DatabaseContext.registerDatabase(new SchemeProtoGroupDatabase());
		DatabaseContext.registerDatabase(new SchemeProtoElementDatabase());
		DatabaseContext.registerDatabase(new SchemeDatabase());
		DatabaseContext.registerDatabase(new SchemeElementDatabase());
		DatabaseContext.registerDatabase(new SchemeOptimizeInfoDatabase());
		DatabaseContext.registerDatabase(new SchemeOptimizeInfoSwitchDatabase());
		DatabaseContext.registerDatabase(new SchemeOptimizeInfoRtuDatabase());
		DatabaseContext.registerDatabase(new SchemeMonitoringSolutionDatabase());
		DatabaseContext.registerDatabase(new SchemeDeviceDatabase());
		DatabaseContext.registerDatabase(new SchemePortDatabase());
		DatabaseContext.registerDatabase(new SchemeCablePortDatabase());
		DatabaseContext.registerDatabase(new SchemeLinkDatabase());
		DatabaseContext.registerDatabase(new SchemeCableLinkDatabase());
		DatabaseContext.registerDatabase(new SchemeCableThreadDatabase());
		DatabaseContext.registerDatabase(new CableChannelingItemDatabase());
		DatabaseContext.registerDatabase(new SchemePathDatabase());
		DatabaseContext.registerDatabase(new PathElementDatabase());
	}	
}
