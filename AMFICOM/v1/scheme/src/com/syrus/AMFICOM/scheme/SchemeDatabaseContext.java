/*-
 * $Id: SchemeDatabaseContext.java,v 1.3 2005/04/01 13:59:08 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/04/01 13:59:08 $
 * @module scheme_v1
 */
public final class SchemeDatabaseContext {
	private static SchemeProtoGroupDatabase		schemeProtoGroupDatabase;

	private static SchemeProtoElementDatabase	schemeProtoElementDatabase;

	private static SchemeDatabase			schemeDatabase;

	private static SchemeElementDatabase		schemeElementDatabase;

	private static SchemeOptimizeInfoDatabase	schemeOptimizeInfoDatabase;

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
				return getSchemeProtoGroupDatabase();
			case ObjectEntities.SCHEME_PROTO_ELEMENT_ENTITY_CODE:
				return getSchemeProtoElementDatabase();
			case ObjectEntities.SCHEME_ENTITY_CODE:
				return getSchemeDatabase();
			case ObjectEntities.SCHEME_ELEMENT_ENTITY_CODE:
				return getSchemeElementDatabase();
			case ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE:
				return getSchemeOptimizeInfoDatabase();
			case ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE:
				return getSchemeMonitoringSolutionDatabase();
			case ObjectEntities.SCHEME_DEVICE_ENTITY_CODE:
				return getSchemeDeviceDatabase();
			case ObjectEntities.SCHEME_PORT_ENTITY_CODE:
				return getSchemePortDatabase();
			case ObjectEntities.SCHEME_CABLE_PORT_ENTITY_CODE:
				return getSchemeCablePortDatabase();
			case ObjectEntities.SCHEME_LINK_ENTITY_CODE:
				return getSchemeLinkDatabase();
			case ObjectEntities.SCHEME_CABLE_LINK_ENTITY_CODE:
				return getSchemeCableLinkDatabase();
			case ObjectEntities.SCHEME_CABLE_THREAD_ENTITY_CODE:
				return getSchemeCableThreadDatabase();
			case ObjectEntities.CABLE_CHANNELING_ITEM_ENTITY_CODE:
				return getCableChannelingItemDatabase();
			case ObjectEntities.SCHEME_PATH_ENTITY_CODE:
				return getSchemePathDatabase();
			case ObjectEntities.PATH_ELEMENT_ENTITY_CODE:
				return getPathElementDatabase();
			default:
				Log.errorMessage("SchemeDatabaseContext.getDatabase | Unknown entity: " + entityCode); //$NON-NLS-1$
				return null;
		}
	}

	public static SchemeProtoGroupDatabase getSchemeProtoGroupDatabase() {
		return schemeProtoGroupDatabase;
	}

	public static SchemeProtoElementDatabase getSchemeProtoElementDatabase() {
		return schemeProtoElementDatabase;
	}

	public static SchemeDatabase getSchemeDatabase() {
		return schemeDatabase;
	}

	public static SchemeElementDatabase getSchemeElementDatabase() {
		return schemeElementDatabase;
	}

	public static SchemeOptimizeInfoDatabase getSchemeOptimizeInfoDatabase() {
		return schemeOptimizeInfoDatabase;
	}

	public static SchemeMonitoringSolutionDatabase getSchemeMonitoringSolutionDatabase() {
		return schemeMonitoringSolutionDatabase;
	}

	public static SchemeDeviceDatabase getSchemeDeviceDatabase() {
		return schemeDeviceDatabase;
	}

	public static SchemePortDatabase getSchemePortDatabase() {
		return schemePortDatabase;
	}

	public static SchemeCablePortDatabase getSchemeCablePortDatabase() {
		return schemeCablePortDatabase;
	}

	public static SchemeLinkDatabase getSchemeLinkDatabase() {
		return schemeLinkDatabase;
	}

	public static SchemeCableLinkDatabase getSchemeCableLinkDatabase() {
		return schemeCableLinkDatabase;
	}

	public static SchemeCableThreadDatabase getSchemeCableThreadDatabase() {
		return schemeCableThreadDatabase;
	}

	public static CableChannelingItemDatabase getCableChannelingItemDatabase() {
		return cableChannelingItemDatabase;
	}

	public static SchemePathDatabase getSchemePathDatabase() {
		return schemePathDatabase;
	}

	public static PathElementDatabase getPathElementDatabase() {
		return pathElementDatabase;
	}
}
