/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.12 2005/02/24 14:59:36 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/02/24 14:59:36 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseGeneralObjectLoader implements GeneralObjectLoader {

	public ParameterType loadParameterType(Identifier id) throws DatabaseException {
		return new ParameterType(id);
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException {
		return new CharacteristicType(id);
	}

	public Characteristic loadCharacteristic(Identifier id) throws DatabaseException {
		return new Characteristic(id);
	}





	// for multiple objects

	public Collection loadParameterTypes(Collection ids) throws DatabaseException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.parameterTypeDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}

	public Collection loadCharacteristicTypes(Collection ids) throws DatabaseException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		Collection collection = null;
		try {
			collection = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristicTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		return collection;
	}

	public Collection loadCharacteristics(Collection ids) throws DatabaseException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		Collection collection = null;
		try {
			collection = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristics | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristics | Illegal Storable Object: " + e.getMessage());
		}
		return collection;
	}





	public Collection loadParameterTypesButIds(StorableObjectCondition condition, Collection ids)
			throws DatabaseException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.parameterTypeDatabase;
		Collection collection = null;
		try {
			collection = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadParameterTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadParameterTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return collection;
	}

	public Collection loadCharacteristicTypesButIds(StorableObjectCondition condition, Collection ids)
			throws DatabaseException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		Collection collection = null;
		try {
			collection = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristicTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristicTypesButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return collection;
	}

	public Collection loadCharacteristicsButIds(StorableObjectCondition condition, Collection ids)
			throws DatabaseException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		Collection collection = null;
		try {
			collection = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristicsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristicsButIds | Illegal Storable Object: "
					+ e.getMessage());
		}
		return collection;
	}





	public void saveParameterType(ParameterType parameterType, boolean force) throws DatabaseException, VersionCollisionException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.getParameterTypeDatabase();
		database.update(parameterType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws DatabaseException, VersionCollisionException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		database.update(characteristicType, SessionContext.getAccessIdentity().getUserId(), force
				? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws DatabaseException, VersionCollisionException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		database.update(characteristic, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}





	public void saveParameterTypes(Collection objects, boolean force) throws DatabaseException, VersionCollisionException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.getParameterTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristicTypes(Collection objects, boolean force) throws DatabaseException, VersionCollisionException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}

	public void saveCharacteristics(Collection objects, boolean force) throws DatabaseException, VersionCollisionException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
	}





	public Set refresh(Set storableObjects) throws DatabaseException {
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

	public void delete(Collection objects) throws IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
		Collection entityObjects;
		Short entityCode;
		for (Iterator it = objects.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier) object;
			else
				if (object instanceof Identified)
					identifier = ((Identified) object).getId();
				else
					throw new IllegalDataException("DatabaseGeneralObjectLoader.delete | Object "
							+ object.getClass().getName() + " isn't Identifier or Identified");

			entityCode = new Short(identifier.getMajor());
			entityObjects = (Collection) map.get(entityCode);
			if (entityObjects == null) {
				entityObjects = new LinkedList();
				map.put(entityCode, entityObjects);
			}
			entityObjects.add(object);
		}

		StorableObjectDatabase storableObjectDatabase;
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			entityCode = (Short) it.next();
			entityObjects = (Collection) map.get(entityCode);
			storableObjectDatabase = GeneralDatabaseContext.getDatabase(entityCode);
			if (storableObjectDatabase != null)
				storableObjectDatabase.delete(entityObjects);
		}
	}

}
