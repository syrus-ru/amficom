/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.10 2005/02/11 18:40:16 arseniy Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/02/11 18:40:16 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseGeneralObjectLoader implements GeneralObjectLoader {

	public ParameterType loadParameterType(Identifier id) throws DatabaseException, CommunicationException {
		return new ParameterType(id);
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException {
		return new CharacteristicType(id);
	}

	public Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException {
		return new Characteristic(id);
	}





	// for multiple objects

	public Collection loadParameterTypes(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadCharacteristicTypes(Collection ids) throws DatabaseException, CommunicationException {
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

	public Collection loadCharacteristics(Collection ids) throws DatabaseException, CommunicationException {
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
			throws DatabaseException,
				CommunicationException {
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
			throws DatabaseException,
				CommunicationException {
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
			throws DatabaseException,
				CommunicationException {
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





	public void saveParameterType(ParameterType parameterType, boolean force) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.getParameterTypeDatabase();
		try {
			database.update(parameterType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveParameterType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveParameterType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveParameterType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveParameterType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveParameterType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveParameterType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws DatabaseException,
				CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		try {
			database.update(characteristicType, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicType | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicType | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			database.update(characteristic, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristic | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristic | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristic | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristic | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristic | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristic | VersionCollisionException: " + e.getMessage());
		}
	}





	public void saveParameterTypes(Collection objects, boolean force) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase) GeneralDatabaseContext.getParameterTypeDatabase();
		try {
			database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveParameterTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveParameterTypes | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveParameterTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveParameterTypes | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveParameterTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveParameterTypes | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCharacteristicTypes(Collection objects, boolean force) throws DatabaseException, CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase) GeneralDatabaseContext.getCharacteristicTypeDatabase();
		try {
			database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicTypes | UpdateObjectException: "
					+ e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicTypes | Illegal Storable Object: "
					+ e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicTypes | VersionCollisionException: "
					+ e.getMessage());
		}
	}

	public void saveCharacteristics(Collection objects, boolean force) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase) GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			database.update(objects, SessionContext.getAccessIdentity().getUserId(), force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristics | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristics | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristics | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristics | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristics | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristics | VersionCollisionException: "
					+ e.getMessage());
		}
	}





	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		if (storableObjects.isEmpty())
			return Collections.EMPTY_SET;

		short entityCode = ((StorableObject) storableObjects.iterator().next()).getId().getMajor();

		try {
			StorableObjectDatabase database = GeneralDatabaseContext.getDatabase(entityCode);

			if (database != null)
				return database.refresh(storableObjects);

			return Collections.EMPTY_SET;
		}
		catch (DatabaseException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.refresh | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.refresh | DatabaseException: " + e.getMessage());
		}
	}





	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		delete(id, null);
	}

	public void delete(Collection objects) throws CommunicationException, DatabaseException, IllegalDataException {
		if (objects == null || objects.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity
		 */
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
							+ object.getClass().getName()
							+ " isn't Identifier or Identified");
			Short entityCode = new Short(identifier.getMajor());
			Collection collection = (Collection) map.get(entityCode);
			if (collection == null) {
				collection = new LinkedList();
				map.put(entityCode, collection);
			}
			collection.add(object);
		}

		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			Collection collection = (Collection) map.get(entityCode);
			this.delete(null, collection);
		}
	}

	private void delete(Identifier id, Collection objects) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (objects.isEmpty())
				return;
			Object obj = objects.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier) obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified) obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = GeneralDatabaseContext.getDatabase(entityCode);
			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (objects != null && !objects.isEmpty()) {
						database.delete(objects);
					}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.delete | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.delete | DatabaseException: " + e.getMessage());
		}
	}

}
