/*-
 * $Id: DatabaseObjectLoader.java,v 1.7 2005/05/27 11:13:49 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.7 $, $Date: 2005/05/27 11:13:49 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public abstract class DatabaseObjectLoader extends ObjectLoader {
	protected static Identifier userId;

	public static void init(Identifier userId1) {
		userId = userId1;
	}

	protected final Set retrieveFromDatabase(final StorableObjectDatabase database, final Set ids) throws RetrieveObjectException {
		try {
			return database.retrieveByIdsByCondition(ids, null);
		}
		catch (IllegalDataException idse) {
			throw new RetrieveObjectException("Cannot retrieve objects from database; database: "
					+ database.getClass().getName() + ", ids: " + ids);
		}
	}

	protected final Set retrieveFromDatabaseButIdsByCondition(final StorableObjectDatabase database,
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

	protected final Identifier_Transferable[] createLoadIdsTransferable(final Set ids, final Set butObjects) {
		Identifier id;
		Set loadIds = new HashSet(ids);
		for (Iterator it = butObjects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadIds.remove(id);
		}

		return Identifier.createTransferables(loadIds);
	}

	protected final Identifier_Transferable[] createLoadButIdsTransferable(final Set ids, final Set alsoButObjects) {
		Identifier id;
		Set loadButIds = new HashSet(ids);
		for (Iterator it = alsoButObjects.iterator(); it.hasNext();) {
			id = ((StorableObject) it.next()).getId();
			loadButIds.add(id);
		}
		
		return Identifier.createTransferables(loadButIds);
	}

	/**
	 * Would be <code>final</code> unless overridden in
	 * <code>MServerMeasurementObjectLoader</code>.
	 *
	 * @param identifiables
	 */
	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * Separate objects by entityCode
		 */
		for (final Iterator identifiableIterator = identifiables.iterator(); identifiableIterator.hasNext();) {
			final Identifiable identifiable = (Identifiable) identifiableIterator.next();
			final Short entityCode = new Short(identifiable.getId().getMajor());
			Set entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(identifiable);
		}

		for (final Iterator entityCodeIterator = map.keySet().iterator(); entityCodeIterator.hasNext();) {
			final Short entityCode = (Short) entityCodeIterator.next();
			final Set entityObjects = (Set) map.get(entityCode);
			final StorableObjectDatabase storableObjectDatabase = DatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

	/**
	 * Would be <code>final</code> unless overridden in
	 * <code>*Server*ObjectLoader</code>.
	 *
	 * @param storableObjects
	 * @throws ApplicationException
	 */
	public Set refresh(final Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		assert StorableObject.hasSingleTypeEntities(storableObjects);
		final short entityCode = StorableObject.getEntityCodeOfIdentifiables(storableObjects);

		return DatabaseContext.getDatabase(entityCode).refresh(storableObjects);
	}

	public final Set loadStorableObjects(final Set ids) throws ApplicationException {
		assert ids != null: ErrorMessages.NON_NULL_EXPECTED;
		if (ids.isEmpty())
			return Collections.EMPTY_SET;
		assert StorableObject.hasSingleTypeEntities(ids);
		return this.retrieveFromDatabase(DatabaseContext.getDatabase(StorableObject.getEntityCodeOfIdentifiables(ids)), ids);
	}

	public final Set loadStorableObjectsButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		assert ids != null && condition != null: ErrorMessages.NON_NULL_EXPECTED;
		assert StorableObject.hasSingleTypeEntities(ids);
		final short entityCode = condition.getEntityCode().shortValue();
		assert ids.isEmpty() || entityCode == StorableObject.getEntityCodeOfIdentifiables(ids);
		return this.retrieveFromDatabaseButIdsByCondition(DatabaseContext.getDatabase(entityCode), ids, condition);
	}

	public final void saveStorableObjects(final Set storableObjects, boolean force) throws ApplicationException {
		assert storableObjects != null: ErrorMessages.NON_NULL_EXPECTED;
		if (storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);
		DatabaseContext.getDatabase(StorableObject.getEntityCodeOfIdentifiables(storableObjects)).update(storableObjects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}
}
