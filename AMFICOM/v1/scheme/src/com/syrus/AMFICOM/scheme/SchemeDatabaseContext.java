/*
 * $Id: SchemeDatabaseContext.java,v 1.1 2005/03/18 19:21:26 bass Exp $
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
 * @version $Revision: 1.1 $, $Date: 2005/03/18 19:21:26 $
 * @module scheme_v1
 */
public final class SchemeDatabaseContext {
	static StorableObjectDatabase schemeProtoGroupDatabase;

	private SchemeDatabaseContext() {
		assert false;
	}

	public static void init(final StorableObjectDatabase schemeProtoGroupDatabase1) {
		if (schemeProtoGroupDatabase1 != null)
			schemeProtoGroupDatabase = schemeProtoGroupDatabase1;
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode ) {
		switch (entityCode) {
			case ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE:
				return schemeProtoGroupDatabase;
			default:
				return null;
		}
	}

	public static StorableObjectDatabase getSchemeProtoGroupDatabase() {
		return schemeProtoGroupDatabase;
	}
}
