/*
 * $Id: SchemeDatabaseContext.java,v 1.2 2005/03/21 16:46:50 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/03/21 16:46:50 $
 * @module scheme_v1
 */
public final class SchemeDatabaseContext {
	static StorableObjectDatabase schemeProtoGroupDatabase;

	static StorableObjectDatabase schemeProtoElementDatabase;

	private SchemeDatabaseContext() {
		assert false;
	}

	public static void init(
			final StorableObjectDatabase schemeProtoGroupDatabase1,
			final StorableObjectDatabase schemeProtoElementDatabase1) {
		if (schemeProtoGroupDatabase1 != null)
			schemeProtoGroupDatabase = schemeProtoGroupDatabase1;
		if (schemeProtoElementDatabase1 != null)
			schemeProtoElementDatabase = schemeProtoElementDatabase1;
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
			default:
				return null;
		}
	}

	public static StorableObjectDatabase getSchemeProtoGroupDatabase() {
		return schemeProtoGroupDatabase;
	}

	public static StorableObjectDatabase getSchemeProtoElementDatabase() {
		return schemeProtoElementDatabase;
	}
}
