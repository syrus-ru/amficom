/*-
 * $Id: DatabaseObjectLoader.java,v 1.12 2005/06/06 14:33:48 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;
import gnu.trove.TShortObjectIterator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/06 14:33:48 $
 * @author $Author: arseniy $
 * @module csbridge_v1
 */
public abstract class DatabaseObjectLoader extends ObjectLoader {
	protected static Identifier userId;

	public static void init(final Identifier userId1) {
		userId = userId1;
	}

	public static final Set loadStorableObjects(final Set ids) throws RetrieveObjectException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty())
			return Collections.EMPTY_SET;
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		return retrieveFromDatabase(database, ids);
	}

	public static final Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids) throws RetrieveObjectException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		return retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	private static final Set retrieveFromDatabase(final StorableObjectDatabase database, final Set ids) throws RetrieveObjectException {
		try {
			return database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException idse) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids);
		}
	}

	private static final Set retrieveFromDatabaseButIdsByCondition(final StorableObjectDatabase database,
			final Set ids,
			final StorableObjectCondition condition)
			throws RetrieveObjectException {
		try {
			return database.retrieveButIdsByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids + ", condition: " + condition);
		}
	}

	public static final void saveStorableObjects(final Set storableObjects, boolean force)
			throws UpdateObjectException, VersionCollisionException {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty())
			return;
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);
		final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
		assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
		database.update(storableObjects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	/**
	 * @todo make final
	 * Would be <code>final</code> unless overridden in
	 * <code>*Server*ObjectLoader</code>.
	 *
	 * @param storableObjects
	 * @throws ApplicationException
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
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
	 * @param identifiables
	 */
	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;

		final TShortObjectHashMap entityMap = new TShortObjectHashMap();

		for (final Iterator it = identifiables.iterator(); it.hasNext();) {
			final Identifiable identifiable = (Identifiable) it.next();
			final short entityCode = identifiable.getId().getMajor();
			Set entityObjects = (Set) entityMap.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				entityMap.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final TShortObjectIterator it = entityMap.iterator(); it.hasNext();) {
			it.advance();
			final short entityCode = it.key();
			final Set entityObjects = (Set) it.value();
			final StorableObjectDatabase database = DatabaseContext.getDatabase(entityCode);
			assert (database != null) : ErrorMessages.NON_NULL_EXPECTED;
			database.delete(entityObjects);
		}
	}

}
