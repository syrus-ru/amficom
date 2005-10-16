/*
 * $Id: DatabaseContextSetup.java,v 1.8 2005/10/16 11:13:22 max Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.ServerDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.SystemUserDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.map.CollectorDatabase;
import com.syrus.AMFICOM.map.MapDatabase;
import com.syrus.AMFICOM.map.MapLibraryDatabase;
import com.syrus.AMFICOM.map.MarkDatabase;
import com.syrus.AMFICOM.map.NodeLinkDatabase;
import com.syrus.AMFICOM.map.PhysicalLinkDatabase;
import com.syrus.AMFICOM.map.PhysicalLinkTypeDatabase;
import com.syrus.AMFICOM.map.PipeBlockDatabase;
import com.syrus.AMFICOM.map.SiteNodeDatabase;
import com.syrus.AMFICOM.map.SiteNodeTypeDatabase;
import com.syrus.AMFICOM.map.TopologicalNodeDatabase;
import com.syrus.AMFICOM.mapview.MapViewDatabase;
import com.syrus.AMFICOM.measurement.KISDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortDatabase;
import com.syrus.AMFICOM.measurement.MeasurementPortTypeDatabase;
import com.syrus.AMFICOM.measurement.MonitoredElementDatabase;
import com.syrus.AMFICOM.resource.ImageResourceDatabase;
import com.syrus.AMFICOM.resource.LayoutItemDatabase;
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
 * @author $Author: max $
 * @version $Revision: 1.8 $, $Date: 2005/10/16 11:13:22 $
 * @module mscharserver
 */
final class DatabaseContextSetup {
	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		DatabaseContext.registerDatabase(new CharacteristicDatabase());
		DatabaseContext.registerDatabase(new CharacteristicTypeDatabase());

		DatabaseContext.registerDatabase(new SystemUserDatabase());
		DatabaseContext.registerDatabase(new DomainDatabase());
		DatabaseContext.registerDatabase(new ServerDatabase());
		DatabaseContext.registerDatabase(new ServerProcessDatabase());

		DatabaseContext.registerDatabase(new MeasurementPortTypeDatabase());
		DatabaseContext.registerDatabase(new MeasurementPortDatabase());
		DatabaseContext.registerDatabase(new KISDatabase());
		DatabaseContext.registerDatabase(new MonitoredElementDatabase());

		DatabaseContext.registerDatabase(new ImageResourceDatabase());
		DatabaseContext.registerDatabase(new LayoutItemDatabase());

		DatabaseContext.registerDatabase(new SiteNodeTypeDatabase());
		DatabaseContext.registerDatabase(new PhysicalLinkTypeDatabase());
		DatabaseContext.registerDatabase(new CollectorDatabase());
		DatabaseContext.registerDatabase(new MapDatabase());
		DatabaseContext.registerDatabase(new MarkDatabase());
		DatabaseContext.registerDatabase(new NodeLinkDatabase());
		DatabaseContext.registerDatabase(new PhysicalLinkDatabase());
		DatabaseContext.registerDatabase(new SiteNodeDatabase());
		DatabaseContext.registerDatabase(new TopologicalNodeDatabase());
		DatabaseContext.registerDatabase(new MapLibraryDatabase());
		DatabaseContext.registerDatabase(new PipeBlockDatabase());

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

		DatabaseContext.registerDatabase(new MapViewDatabase());
	}
}
