/*
 * $Id: DatabaseContextSetup.java,v 1.1.1.1 2004/12/09 12:31:41 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mshserver;

import com.syrus.AMFICOM.configuration.CableThreadTypeDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
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
import com.syrus.AMFICOM.map.CollectorDatabase;
import com.syrus.AMFICOM.map.DatabaseMapObjectLoader;
import com.syrus.AMFICOM.map.MapDatabase;
import com.syrus.AMFICOM.map.MapDatabaseContext;
import com.syrus.AMFICOM.map.MapStorableObjectPool;
import com.syrus.AMFICOM.map.MarkDatabase;
import com.syrus.AMFICOM.map.NodeLinkDatabase;
import com.syrus.AMFICOM.map.PhysicalLinkDatabase;
import com.syrus.AMFICOM.map.PhysicalLinkTypeDatabase;
import com.syrus.AMFICOM.map.SiteNodeDatabase;
import com.syrus.AMFICOM.map.SiteNodeTypeDatabase;
import com.syrus.AMFICOM.map.TopologicalNodeDatabase;
import com.syrus.util.ApplicationProperties;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/12/09 12:31:41 $
 * @author $Author: cvsadmin $
 * @module mserver_v1
 */

public abstract class DatabaseContextSetup {

	public static final String	CONFIGURATION_POOL_SIZE_KEY	= "ConfigurationPoolSize";
	public static final String	MEASUREMENT_POOL_SIZE_KEY	= "MeasurementPoolSize";

	private DatabaseContextSetup() {
		// empty
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
		MapDatabaseContext.init(new SiteNodeTypeDatabase(),
						new PhysicalLinkTypeDatabase(),
						new CollectorDatabase(),
						new MapDatabase(),
						new MarkDatabase(),
						new NodeLinkDatabase(),
						new PhysicalLinkDatabase(),
						new SiteNodeDatabase(),
						new TopologicalNodeDatabase());
		
	}	



	public static void initObjectPools() {
		ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(), ApplicationProperties.getInt(
			CONFIGURATION_POOL_SIZE_KEY, 1000));
		MapStorableObjectPool.init(new DatabaseMapObjectLoader(), ApplicationProperties.getInt(
			MEASUREMENT_POOL_SIZE_KEY, 1000));
	}
}
