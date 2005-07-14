/*-
 * $Id: DatabaseObjectLoader.java,v 1.19 2005/07/14 11:31:00 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.StorableObjectDatabase.UpdateKind;

/**
 * @version $Revision: 1.19 $, $Date: 2005/07/14 11:31:00 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class DatabaseObjectLoader {
	protected static Identifier userId;

	public static void init(final Identifier userId1) {
		userId = userId1;
	}

	public static final Set loadStorableObjects(final Set<Identifier> ids)
			throws RetrieveObjectException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty())
			return Collections.emptySet();
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		return retrieveFromDatabase(database, ids);
	}

	public static final Set loadStorableObjectsButIdsByCondition(final StorableObjectCondition condition,
			final Set<Identifier> ids) throws RetrieveObjectException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		return retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	private static final Set retrieveFromDatabase(final StorableObjectDatabase database,
			final Set<Identifier> ids) throws RetrieveObjectException {
		assert database != null : ErrorMessages.NON_NULL_EXPECTED;
		try {
			return database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids, ide);
		}
	}

	private static final Set retrieveFromDatabaseButIdsByCondition(final StorableObjectDatabase database,
			final Set<Identifier> ids,
			final StorableObjectCondition condition)
			throws RetrieveObjectException {
		assert database != null : ErrorMessages.NON_NULL_EXPECTED;
		try {
			return database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids + ", condition: " + condition);
		}
	}

	public static final void saveStorableObjects(final Set<? extends StorableObject> storableObjects, final boolean force)
			throws UpdateObjectException, VersionCollisionException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty())
			return;
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		database.update(storableObjects, userId, force ? UpdateKind.UPDATE_FORCE : UpdateKind.UPDATE_CHECK);
	}

	/**
	 * @todo make final
	 * Would be <code>final</code> unless overridden in
	 * <code>*Server*ObjectLoader</code>.
	 *
	 * @param storableObjects
	 * @throws ApplicationException
	 */
	public Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		return database.refresh(storableObjects);
	}

	/**
	 * @todo make final
	 * Would be <code>final</code> unless overridden in
	 * <code>MServerMeasurementObjectLoader</code>.
	 *
	 * @param identifiables of the same entity
	 */
	public void delete(final Set<? extends Identifiable> identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;

		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(identifiables);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		database.delete(identifiables);
	}

}
