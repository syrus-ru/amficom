/*
 * $Id: DatabaseContextSetup.java,v 1.12 2005/05/24 13:25:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mshserver;

import gnu.trove.TShortObjectHashMap;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.DatabaseAdministrationObjectLoader;
import com.syrus.AMFICOM.administration.DomainDatabase;
import com.syrus.AMFICOM.administration.ServerProcessDatabase;
import com.syrus.AMFICOM.administration.UserDatabase;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacteristicTypeDatabase;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.map.CollectorDatabase;
import com.syrus.AMFICOM.map.DatabaseMapObjectLoader;
import com.syrus.AMFICOM.map.MapDatabase;
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
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.util.ApplicationProperties;

/**
 * @author $Author: bass $
 * @version $Revision: 1.12 $, $Date: 2005/05/24 13:25:05 $
 * @module msherver_v1
 */
final class DatabaseContextSetup {
	public static final String KEY_GENERAL_POOL_SIZE = "GeneralPoolSize";
	public static final String KEY_ADMINISTRATION_POOL_SIZE = "AdministrationPoolSize";
	public static final String KEY_MAP_POOL_SIZE = "MapPoolSize";
	public static final String KEY_SCHEME_POOL_SIZE = "SchemePoolSize";
	public static final String KEY_REFRESH_TIMEOUT = "RefreshTimeout";
	public static final String KEY_DATABASE_LOADER_ONLY = "DatabaseLoaderOnly";

	
	public static final int DEFAULT_GENERAL_POOL_SIZE = 1000;
	public static final int DEFAULT_ADMINISTRATION_POOL_SIZE = 1000;
	public static final int DEFAULT_MAP_POOL_SIZE = 1000;
	public static final int DEFAULT_SCHEME_POOL_SIZE = 1000;
	public static final int DEFAULT_REFRESH_TIMEOUT = 5;

	private DatabaseContextSetup() {
		assert false;
	}

	public static void initDatabaseContext() {
		final TShortObjectHashMap entityCodeDatabaseMap = new TShortObjectHashMap();

		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new CharacteristicDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new CharacteristicTypeDatabase());

		entityCodeDatabaseMap.put(ObjectEntities.USER_ENTITY_CODE, new UserDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.DOMAIN_ENTITY_CODE, new DomainDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SERVERPROCESS_ENTITY_CODE, new ServerProcessDatabase());
		
		entityCodeDatabaseMap.put(ObjectEntities.SITE_NODE_TYPE_ENTITY_CODE, new SiteNodeTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PHYSICAL_LINK_TYPE_ENTITY_CODE, new PhysicalLinkTypeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.COLLECTOR_ENTITY_CODE, new CollectorDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MAP_ENTITY_CODE, new MapDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.MARK_ENTITY_CODE, new MarkDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.NODE_LINK_ENTITY_CODE, new NodeLinkDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PHYSICAL_LINK_ENTITY_CODE, new PhysicalLinkDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SITE_NODE_ENTITY_CODE, new SiteNodeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.TOPOLOGICAL_NODE_ENTITY_CODE, new TopologicalNodeDatabase());

		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE, new SchemeProtoGroupDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE, new SchemeProtoElementDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_ENTITY_CODE, new SchemeDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE, new SchemeElementDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE, new SchemeOptimizeInfoDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE, new SchemeOptimizeInfoSwitchDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE, new SchemeOptimizeInfoRtuDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE, new SchemeMonitoringSolutionDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_DEVICE_ENTITY_CODE, new SchemeDeviceDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_PORT_ENTITY_CODE, new SchemePortDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE, new SchemeCablePortDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_LINK_ENTITY_CODE, new SchemeLinkDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE, new SchemeCableLinkDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE, new SchemeCableThreadDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE, new CableChannelingItemDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.SCHEME_PATH_ENTITY_CODE, new SchemePathDatabase());
		entityCodeDatabaseMap.put(ObjectEntities.PATH_ELEMENT_ENTITY_CODE, new PathElementDatabase());

		DatabaseContext.init(entityCodeDatabaseMap);
	}

	public static void initObjectPools() {
		final int generalPoolSize = ApplicationProperties.getInt(KEY_GENERAL_POOL_SIZE, DEFAULT_GENERAL_POOL_SIZE);
		final int administrationPoolSize = ApplicationProperties.getInt(KEY_ADMINISTRATION_POOL_SIZE, DEFAULT_ADMINISTRATION_POOL_SIZE);
		final int mapPoolSize = ApplicationProperties.getInt(KEY_MAP_POOL_SIZE, DEFAULT_MAP_POOL_SIZE);
		final int schemePoolSize = ApplicationProperties.getInt(KEY_SCHEME_POOL_SIZE, DEFAULT_SCHEME_POOL_SIZE);

		GeneralStorableObjectPool.init(new DatabaseGeneralObjectLoader(), generalPoolSize);
		AdministrationStorableObjectPool.init(new DatabaseAdministrationObjectLoader(), administrationPoolSize);
		MapStorableObjectPool.init(new DatabaseMapObjectLoader(), mapPoolSize);
		SchemeStorableObjectPool.init(new DatabaseSchemeObjectLoader(), schemePoolSize);
	}
}
