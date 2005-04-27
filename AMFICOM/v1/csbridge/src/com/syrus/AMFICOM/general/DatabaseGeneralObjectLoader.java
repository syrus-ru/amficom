/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.1 2005/04/27 13:38:20 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 13:38:20 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseGeneralObjectLoader extends DatabaseObjectLoader implements GeneralObjectLoader {

	public ParameterType loadParameterType(Identifier id) throws ApplicationException {
		return new ParameterType(id);
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws ApplicationException {
		return new CharacteristicType(id);
	}

	public Characteristic loadCharacteristic(Identifier id) throws ApplicationException {
		return new Characteristic(id);
	}





	// for multiple objects

	public Set loadParameterTypes(Set ids) throws ApplicationException {
		ParameterTypeDatabase database = GeneralDatabaseContext.getParameterTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		return super.retrieveFromDatabase(database, ids);
	}

	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		ParameterTypeDatabase database = GeneralDatabaseContext.getParameterTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		return super.retrieveFromDatabaseButIdsByCondition(database, ids, condition);
	}





	public void saveParameterType(ParameterType parameterType, boolean force) throws ApplicationException {
		ParameterTypeDatabase database = GeneralDatabaseContext.getParameterTypeDatabase();
		database.update(parameterType, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws ApplicationException {
		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		database.update(characteristicType, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException {
		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		database.update(characteristic, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}





	public void saveParameterTypes(Set objects, boolean force) throws ApplicationException {
		ParameterTypeDatabase database = GeneralDatabaseContext.getParameterTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException {
		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristics(Set objects, boolean force) throws ApplicationException {
		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		database.update(objects, userId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}





	public Set refresh(Set storableObjects) throws ApplicationException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		StorableObjectDatabase database = GeneralDatabaseContext.getDatabase(entityCode);

		if (database != null)
			return database.refresh(storableObjects);

		return Collections.EMPTY_SET;
	}





	public void delete(final Identifier id) {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = GeneralDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(final Set identifiables) {
		if (identifiables == null || identifiables.isEmpty())
			return;
		/**
		 * @todo: use Trove collection instead of java.util.Map
		 */
		final Map map = new HashMap();

		/**
		 * separate objects by kind of entity
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
			final StorableObjectDatabase storableObjectDatabase = GeneralDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
