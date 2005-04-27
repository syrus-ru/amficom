/*
 * $Id: DatabaseContextSetup.java,v 1.7 2005/04/27 19:36:50 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mshserver;

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
import com.syrus.AMFICOM.general.ParameterTypeDatabase;
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
import com.syrus.AMFICOM.scheme.CableChannelingItemDatabase;
import com.syrus.AMFICOM.scheme.DatabaseSchemeObjectLoader;
import com.syrus.AMFICOM.scheme.PathElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeCableLinkDatabase;
import com.syrus.AMFICOM.scheme.SchemeCablePortDatabase;
import com.syrus.AMFICOM.scheme.SchemeCableThreadDatabase;
import com.syrus.AMFICOM.scheme.SchemeDatabase;
import com.syrus.AMFICOM.scheme.SchemeDatabaseContext;
import com.syrus.AMFICOM.scheme.SchemeDeviceDatabase;
import com.syrus.AMFICOM.scheme.SchemeElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeLinkDatabase;
import com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionDatabase;
import com.syrus.AMFICOM.scheme.SchemeOptimizeInfoDatabase;
import com.syrus.AMFICOM.scheme.SchemePathDatabase;
import com.syrus.AMFICOM.scheme.SchemePortDatabase;
import com.syrus.AMFICOM.scheme.SchemeProtoElementDatabase;
import com.syrus.AMFICOM.scheme.SchemeProtoGroupDatabase;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2005/04/27 19:36:50 $
 * @module msherver_v1
 */
final class DatabaseContextSetup {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize"; //$NON-NLS-1$
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize"; //$NON-NLS-1$
	public static final String KEY_CONFIGURATION_POOL_SIZE = "ConfigurationPoolSize"; //$NON-NLS-1$
	public static final String KEY_MEASUREMENT_POOL_SIZE = "MeasurementPoolSize"; //$NON-NLS-1$
	public static final String KEY_MAP_POOL_SIZE = "MapPoolSize"; //$NON-NLS-1$
	public static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize"; //$NON-NLS-1$
	public static final String KEY_REFRESH_TIMEOUT = "RefreshTimeout"; //$NON-NLS-1$
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly"; //$NON-NLS-1$

	
	public static final int DEFAULT_GENERAL_POOL_SIZE = 1000;
	public static final int DEFAULT_ADMINISTRATION_POOL_SIZE = 1000;
	public static final int DEFAULT_CONFIGURATION_POOL_SIZE = 1000;
	public static final int DEFAULT_MEASUREMENT_POOL_SIZE = 1000;
	public static final int DEFAULT_MAP_POOL_SIZE = 1000;
	public static final int DEFAULT_SCHEME_POOL_SIZE = 1000;
	public static final int DEFAULT_REFRESH_TIMEOUT = 5;

	private DatabaseContextSetup() {
		assert false;
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

		MapDatabaseContext.init(new SiteNodeTypeDatabase(),
				new PhysicalLinkTypeDatabase(),
				new CollectorDatabase(),
				new MapDatabase(),
				new MarkDatabase(),
				new NodeLinkDatabase(),
				new PhysicalLinkDatabase(),
				new SiteNodeDatabase(),
				new TopologicalNodeDatabase());

		SchemeDatabaseContext.init(new SchemeProtoGroupDatabase(),
				new SchemeProtoElementDatabase(),
				new SchemeDatabase(),
				new SchemeElementDatabase(),
				new SchemeOptimizeInfoDatabase(),
				new SchemeMonitoringSolutionDatabase(),
				new SchemeDeviceDatabase(),
				new SchemePortDatabase(),
				new SchemeCablePortDatabase(),
				new SchemeLinkDatabase(),
				new SchemeCableLinkDatabase(),
				new SchemeCableThreadDatabase(),
				new CableChannelingItemDatabase(),
				new SchemePathDatabase(),
				new PathElementDatabase());
	}

	public static void initObjectPools() {
		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, DEFAULT_GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, DEFAULT_ADMINISTRATION_POOL_SIZE);
		final int configurationPoolSize = ApplicationProperties.getInt(KEY_CONFIGURATION_POOL_SIZE, DEFAULT_CONFIGURATION_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, DEFAULT_MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, DEFAULT_SCHEME_POOL_SIZE); 

		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), generalPoolSize);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), administrationPoolSize);
		ConfigurationStorableObjectPool.init(new DatabaseConfigurationObjectLoader(), configurationPoolSize);
		MapStorableObjectPool.init(new DatabaseMapObjectLoader(), mapPoolSize);
		SchemeStorableObjectPool.init(new DatabaseSchemeObjectLoader(), schemePoolSize);
	}
}
