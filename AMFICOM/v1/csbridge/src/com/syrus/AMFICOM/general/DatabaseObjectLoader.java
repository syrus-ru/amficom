/*-
 * $Id: DatabaseObjectLoader.java,v 1.36 2005/11/30 14:56:07 bass Exp $
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
 * @version $Revision: 1.36 $, $Date: 2005/11/30 14:56:07 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public class DatabaseObjectLoader implements ObjectLoader {

	/**
	 * Overridden in:
	 * CMServerObjectLoader
	 * MServerObjectLoader
	 */
	public <T extends StorableObject<T>> Set<T> loadStorableObjects(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
		assert database != null : ErrorMessages.NON_NULL_EXPECTED;
		return database.retrieveByIdsByCondition(ids, null);
	}

	/**
	 * Overridden in:
	 * CMServerObjectLoader
	 * MServerObjectLoader
	 */
	public <T extends StorableObject<T>> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition)
			throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		return database.retrieveButIdsByCondition(ids, condition);
	}

	public final Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
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
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		database.save(storableObjects);
	}

	public final void delete(final Set<? extends Identifiable> identifiables) {
		if (identifiables == null || identifiables.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(identifiables);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ErrorMessages.ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		database.delete(identifiables);
	}

}
