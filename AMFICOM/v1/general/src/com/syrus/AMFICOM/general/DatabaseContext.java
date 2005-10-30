/*-
 * $Id: DatabaseContext.java,v 1.12 2005/10/30 15:20:43 bass Exp $
 *
 * Copyright © 2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.logging.Level;

import com.syrus.util.Log;

import gnu.trove.TShortObjectHashMap;

/**
 * @version $Revision: 1.12 $, $Date: 2005/10/30 15:20:43 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public final class DatabaseContext {
	private static final TShortObjectHashMap ENTITY_CODE_DATABASE_MAP = new TShortObjectHashMap();

	private DatabaseContext() {
		assert false;
	}

	public static void registerDatabase(final StorableObjectDatabase<?> database) {
		assert database != null: ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = database.getEntityCode();
		final String entity = '\'' + ObjectEntities.codeToString(entityCode) + "' (" + entityCode + ')';
		assert !ENTITY_CODE_DATABASE_MAP.containsKey(entityCode) :
				"DatabaseContext.registerDatabase() | Database for type: " + entity + " already registered";
		assert Log.debugMessage("Database for type: " + entity + " registered", Level.FINEST);
		ENTITY_CODE_DATABASE_MAP.put(entityCode, database);
	}

	public static <T extends StorableObject> StorableObjectDatabase<T> getDatabase(final Short entityCode) {
		return getDatabase(entityCode.shortValue());
	}

	@SuppressWarnings("unchecked")
	public static <T extends StorableObject> StorableObjectDatabase<T> getDatabase(final short entityCode ) {
		return (StorableObjectDatabase) ENTITY_CODE_DATABASE_MAP.get(entityCode);
	}
}
