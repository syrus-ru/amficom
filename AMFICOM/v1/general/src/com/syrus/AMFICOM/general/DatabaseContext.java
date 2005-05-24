/*-
 * $Id: DatabaseContext.java,v 1.1 2005/05/24 13:24:59 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;
import gnu.trove.TShortObjectIterator;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/24 13:24:59 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class DatabaseContext {
	private static final TShortObjectHashMap ENTITY_CODE_DATABASE_MAP = new TShortObjectHashMap();

	private DatabaseContext() {
		assert false;
	}

	public static void init(final TShortObjectHashMap entityCodeDatabaseMap) {
		assert entityCodeDatabaseMap != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final TShortObjectIterator entityCodeDatabaseIterator = entityCodeDatabaseMap.iterator(); entityCodeDatabaseIterator.hasNext();) {
			entityCodeDatabaseIterator.advance();
			final short key = entityCodeDatabaseIterator.key();
			assert !ENTITY_CODE_DATABASE_MAP.containsKey(key);
			ENTITY_CODE_DATABASE_MAP.put(key, entityCodeDatabaseIterator.value());
		}
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode ) {
		return (StorableObjectDatabase) ENTITY_CODE_DATABASE_MAP.get(entityCode);
	}
}
