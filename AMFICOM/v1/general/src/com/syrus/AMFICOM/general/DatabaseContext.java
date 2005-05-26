/*-
 * $Id: DatabaseContext.java,v 1.4 2005/05/26 14:48:37 arseniy Exp $
 *
 * Copyright � 2005 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import com.syrus.util.Log;

import gnu.trove.TShortObjectHashMap;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/26 14:48:37 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class DatabaseContext {
	private static final TShortObjectHashMap ENTITY_CODE_DATABASE_MAP = new TShortObjectHashMap();

	private DatabaseContext() {
		assert false;
	}

	public static void registerDatabase(final StorableObjectDatabase database) {
		assert database != null: ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = database.getEntityCode();
		assert !ENTITY_CODE_DATABASE_MAP.containsKey(entityCode) : "Database for '"
				+ ObjectEntities.codeToString(entityCode)
				+ "' already registered";
		Log.debugMessage("DatabaseContext.registerDatabase() | Database for type: "
				+ ObjectEntities.codeToString(entityCode)
				+ '(' + entityCode + ") registered",
				Log.FINEST);
		ENTITY_CODE_DATABASE_MAP.put(entityCode, database);
	}

	public static StorableObjectDatabase getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	public static StorableObjectDatabase getDatabase(final short entityCode ) {
		return (StorableObjectDatabase) ENTITY_CODE_DATABASE_MAP.get(entityCode);
	}
}
