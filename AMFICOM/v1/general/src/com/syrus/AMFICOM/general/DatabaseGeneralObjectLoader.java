/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.20 2005/04/05 10:31:58 arseniy Exp $
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
 * @version $Revision: 1.20 $, $Date: 2005/04/05 10:31:58 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseGeneralObjectLoader extends AbstractObjectLoader implements GeneralObjectLoader {

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
		database.update(parameterType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws ApplicationException {
		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		database.update(characteristicType, SessionContext.getAccessIdentity().getUserId(), force
				? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException {
		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		database.update(characteristic, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}





	public void saveParameterTypes(Set objects, boolean force) throws ApplicationException {
		ParameterTypeDatabase database = GeneralDatabaseContext.getParameterTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException {
		CharacteristicTypeDatabase database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristics(Set objects, boolean force) throws ApplicationException {
		CharacteristicDatabase database = GeneralDatabaseContext.getCharacteristicDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
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





	public void delete(Identifier id) throws IllegalDataException {
		short entityCode = id.getMajor();
		StorableObjectDatabase storableObjectDatabase = GeneralDatabaseContext.getDatabase(entityCode);
		if (storableObjectDatabase != null)
			storableObjectDatabase.delete(id);
	}

	public void delete(Set objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Set entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identifiable)
					identifier = ((Identifiable) object).getId();
				else
					throw new IllegalDataException("DatabaseGeneralObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identifiable");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Set) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new HashSet();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Set) map.get(entityCode);
			storableObjectDatabase = GeneralDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
