/*-
 * $Id: DatabaseObjectLoader.java,v 1.40 2006/03/09 17:23:58 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.ILLEGAL_ENTITY_CODE;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.40 $, $Date: 2006/03/09 17:23:58 $
 * @author $Author: arseniy $
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
		assert ids != null: NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptySet();
		}
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
		assert database != null : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);
		return database.retrieveByIdsByCondition(ids, null);
	}

	/**
	 * Overridden in:
	 * CMServerObjectLoader
	 * MServerObjectLoader
	 */
	public <T extends StorableObject<T>> Set<T> loadStorableObjectsButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: NON_NULL_EXPECTED;
		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids) : ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<T> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);
		return database.retrieveButIdsByCondition(ids, condition);
	}

	public Set<Identifier> loadIdentifiersButIdsByCondition(final Set<Identifier> ids, final StorableObjectCondition condition) throws ApplicationException {
		assert ids != null && condition != null: NON_NULL_EXPECTED;
		final short entityCode = condition.getEntityCode().shortValue();
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids) : ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);
		return database.retrieveIdentifiersButIdsByCondition(ids, condition);
	}

	public final Map<Identifier, StorableObjectVersion> getRemoteVersions(final Set<Identifier> ids) throws ApplicationException {
		assert ids != null: NON_NULL_EXPECTED;
		if (ids.isEmpty()) {
			return Collections.emptyMap();
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);
		final Map<Identifier, StorableObjectVersion> versionsMap = database.retrieveVersions(ids);
		return versionsMap;
	}

	public final void saveStorableObjects(final Set<StorableObject> storableObjects) throws ApplicationException {
		assert storableObjects != null : NON_NULL_EXPECTED;
		if (storableObjects.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<? extends StorableObject<?>> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);
		((StorableObjectDatabase) database).save(storableObjects);
	}

	public final void delete(final Set<? extends Identifiable> identifiables) {
		if (identifiables == null || identifiables.isEmpty()) {
			return;
		}

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(identifiables);
		assert ObjectEntities.isEntityCodeValid(entityCode) : ILLEGAL_ENTITY_CODE;
		final StorableObjectDatabase<?> database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : NON_NULL_EXPECTED + "; entity: " + ObjectEntities.codeToString(entityCode);
		database.delete(identifiables);
	}

}
