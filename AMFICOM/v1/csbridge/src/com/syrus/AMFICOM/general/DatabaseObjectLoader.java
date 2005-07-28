/*-
 * $Id: DatabaseObjectLoader.java,v 1.30 2005/07/28 11:03:12 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.30 $, $Date: 2005/07/28 11:03:12 $
 * @author $Author: arseniy $
 * @module csbridge
 */
public class DatabaseObjectLoader implements ObjectLoader {
	protected static Identifier userId;

	public static void init(final Identifier userId1) {
		userId = userId1;
	}

	/**
	 * Overridden in:
	 * CMServerObjectLoader
	 */
	public <T extends StorableObject> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
		assert database != null : ErrorMessages.NON_NULL_EXPECTED;
		return database.retrieveByIdsByCondition(ids, null);
	}

	/**
	 * Overridden in:
	 * CMServerObjectLoader
	 */
	public <T extends StorableObject> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition)
			throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		return database.retrieveButIdsByCondition(ids, condition);
	}

	public final Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws RetrieveObjectException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		final Map<Identifier, StorableObjectVersion> versionsMap = database.retrieveVersions(ids);
		return versionsMap;
	}

	public final void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final StorableObjectDatabase<StorableObject> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		database.save(storableObjects);
	}

	/**
	 * Overridden in:
	 * CMServerObjectLoader
	 */
	public Set<Identifier> getOldVersionIds(final Map<Identifier, StorableObjectVersion> versionsMap) throws ApplicationException {
		assert versionsMap != null : ErrorMessages.NON_NULL_EXPECTED;

		if (versionsMap.isEmpty()) {
			return Collections.emptySet();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(versionsMap.keySet());
		final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		return database.getOldVersionIds(versionsMap);
	}

	public final void delete(final Set<? extends Identifiable> identifiables) {
		if (identifiables == null || identifiables.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(identifiables);
		final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		database.delete(identifiables);
	}

}
