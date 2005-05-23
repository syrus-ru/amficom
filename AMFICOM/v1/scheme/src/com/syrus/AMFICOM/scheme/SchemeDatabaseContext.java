/*-
 * $Id: SchemeDatabaseContext.java,v 1.5 2005/05/23 18:45:16 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/05/23 18:45:16 $
 * @module scheme_v1
 */
public final class SchemeDatabaseContext {
	private static SchemeProtoGroupDatabase		schemeProtoGroupDatabase;

	private static SchemeProtoElementDatabase	schemeProtoElementDatabase;

	private static SchemeDatabase			schemeDatabase;

	private static SchemeElementDatabase		schemeElementDatabase;

	private static SchemeOptimizeInfoDatabase	schemeOptimizeInfoDatabase;

	private static SchemeOptimizeInfoSwitchDatabase	schemeOptimizeInfoSwitchDatabase;

	private static SchemeOptimizeInfoRtuDatabase	schemeOptimizeInfoRtuDatabase;

	private static SchemeMonitoringSolutionDatabase	schemeMonitoringSolutionDatabase;

	private static SchemeDeviceDatabase		schemeDeviceDatabase;

	private static SchemePortDatabase		schemePortDatabase;

	private static SchemeCablePortDatabase		schemeCablePortDatabase;

	private static SchemeLinkDatabase		schemeLinkDatabase;

	private static SchemeCableLinkDatabase		schemeCableLinkDatabase;

	private static SchemeCableThreadDatabase	schemeCableThreadDatabase;

	private static CableChannelingItemDatabase	cableChannelingItemDatabase;

	private static SchemePathDatabase		schemePathDatabase;

	private static PathElementDatabase		pathElementDatabase;

	private SchemeDatabaseContext() {
		assert false;
	}

	public static void init(
			final SchemeProtoGroupDatabase		schemeProtoGroupDatabase1,
			final SchemeProtoElementDatabase	schemeProtoElementDatabase1,
			final SchemeDatabase			schemeDatabase1,
			final SchemeElementDatabase		schemeElementDatabase1,
			final SchemeOptimizeInfoDatabase	schemeOptimizeInfoDatabase1,
			final SchemeOptimizeInfoSwitchDatabase	schemeOptimizeInfoSwitchDatabase1,
			final SchemeOptimizeInfoRtuDatabase	schemeOptimizeInfoRtuDatabase1,
			final SchemeMonitoringSolutionDatabase	schemeMonitoringSolutionDatabase1,
			final SchemeDeviceDatabase		schemeDeviceDatabase1,
			final SchemePortDatabase		schemePortDatabase1,
			final SchemeCablePortDatabase		schemeCablePortDatabase1,
			final SchemeLinkDatabase		schemeLinkDatabase1,
			final SchemeCableLinkDatabase		schemeCableLinkDatabase1,
			final SchemeCableThreadDatabase		schemeCableThreadDatabase1,
			final CableChannelingItemDatabase	cableChannelingItemDatabase1,
			final SchemePathDatabase		schemePathDatabase1,
			final PathElementDatabase		pathElementDatabase1) {
		if (schemeProtoGroupDatabase1 != null)
			schemeProtoGroupDatabase = schemeProtoGroupDatabase1;
		if (schemeProtoElementDatabase1 != null)
			schemeProtoElementDatabase = schemeProtoElementDatabase1;
		if (schemeDatabase1 != null)
			schemeDatabase = schemeDatabase1;
		if (schemeElementDatabase1 != null)
			schemeElementDatabase = schemeElementDatabase1;
		if (schemeOptimizeInfoDatabase1 != null)
			schemeOptimizeInfoDatabase = schemeOptimizeInfoDatabase1;
		if (schemeOptimizeInfoSwitchDatabase1 != null)
			schemeOptimizeInfoSwitchDatabase = schemeOptimizeInfoSwitchDatabase1;
		if (schemeOptimizeInfoRtuDatabase1 != null)
			schemeOptimizeInfoRtuDatabase = schemeOptimizeInfoRtuDatabase1;
		if (schemeMonitoringSolutionDatabase1 != null)
			schemeMonitoringSolutionDatabase = schemeMonitoringSolutionDatabase1;
		if (schemeDeviceDatabase1 != null)
			schemeDeviceDatabase = schemeDeviceDatabase1;
		if (schemePortDatabase1 != null)
			schemePortDatabase = schemePortDatabase1;
		if (schemeCablePortDatabase1 != null)
			schemeCablePortDatabase = schemeCablePortDatabase1;
		if (schemeLinkDatabase1 != null)
			schemeLinkDatabase = schemeLinkDatabase1;
		if (schemeCableLinkDatabase1 != null)
			schemeCableLinkDatabase = schemeCableLinkDatabase1;
		if (schemeCableThreadDatabase1 != null)
			schemeCableThreadDatabase = schemeCableThreadDatabase1;
		if (cableChannelingItemDatabase1 != null)
			cableChannelingItemDatabase = cableChannelingItemDatabase1;
		if (schemePathDatabase1 != null)
			schemePathDatabase = schemePathDatabase1;
		if (pathElementDatabase1 != null)
			pathElementDatabase = pathElementDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode ) {
		switch (entityCode) {
			case ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE:
				return schemeProtoGroupDatabase;
			case ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE:
				return schemeProtoElementDatabase;
			case ObjectEntities.SCHEME_ENTITY_CODE:
				return schemeDatabase;
			case ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE:
				return schemeElementDatabase;
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE:
				return schemeOptimizeInfoDatabase;
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_SWITCH_ENTITY_CODE:
				return schemeOptimizeInfoSwitchDatabase;
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_RTU_ENTITY_CODE:
				return schemeOptimizeInfoRtuDatabase;
			case ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE:
				return schemeMonitoringSolutionDatabase;
			case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
				return schemeDeviceDatabase;
			case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
				return schemePortDatabase;
			case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
				return schemeCablePortDatabase;
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				return schemeLinkDatabase;
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				return schemeCableLinkDatabase;
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				return schemeCableThreadDatabase;
			case ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE:
				return cableChannelingItemDatabase;
			case ObjectEntities.SCHEME_PATH_ENTITY_CODE:
				return schemePathDatabase;
			case ObjectEntities.PATH_ELEMENT_ENTITY_CODE:
				return pathElementDatabase;
			default:
				Log.errorMessage("SchemeDatabaseContext.getDatabase | Unknown entity: " + entityCode);
				return null;
		}
	}
}
