/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.2 2005/01/19 20:42:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/19 20:42:59 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseGeneralObjectLoader implements GeneralObjectLoader {

	private void delete(Identifier id, List ids) throws DatabaseException {
		short entityCode = (id != null) ? id.getMajor() : 0;
		if (id == null) {
			if (ids.isEmpty())
				return;
			Object obj = ids.iterator().next();
			if (obj instanceof Identifier)
				entityCode = ((Identifier)obj).getMajor();
			else
				if (obj instanceof Identified)
					entityCode = ((Identified)obj).getId().getMajor();
		}
		try {
			StorableObjectDatabase database = GeneralDatabaseContext.getDatabase(entityCode);
			if (database != null) {
				if (id != null)
					database.delete(id);
				else
					if (ids != null && !ids.isEmpty()) {
						database.delete(ids);
					}
			}
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.delete | DatabaseException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.delete | DatabaseException: " + e.getMessage());
		}
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		delete(id, null);       
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
			if (ids == null || ids.isEmpty())
			return;
		/**
		 * TODO: use Trove collection instead java.util.Map 
		 */
		Map map = new HashMap();

		/**
		 * separate objects by kind of entity 
		 */
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Object object = it.next();
			Identifier identifier = null;
			if (object instanceof Identifier)
				identifier = (Identifier)object;
			else
				if (object instanceof Identified)
					identifier = ((Identified)object).getId();
				else
					throw new DatabaseException("DatabaseMeasumentObjectLoader.delete | Object " + object.getClass().getName() + " isn't Identifier or Identified");
			Short entityCode = new Short(identifier.getMajor());
			List list = (List)map.get(entityCode);
			if (list == null) {
				list = new LinkedList();
				map.put(entityCode, list);
			}
			list.add(object);
		}

		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Short entityCode = (Short) it.next();
			List list = (List)map.get(entityCode);
			delete(null, list);
		}
	}


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

	public List loadParameterTypes(List ids) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase)GeneralDatabaseContext.parameterTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)GeneralDatabaseContext.getCharacteristicTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadCharacteristics(List ids) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		List list = null;
		try {
			list = database.retrieveByIds(ids, null);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristics | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristics | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}


	public List loadParameterTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase)GeneralDatabaseContext.parameterTypeDatabase;
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadParameterTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadParameterTypesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)GeneralDatabaseContext.getCharacteristicTypeDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristicTypesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristicTypesButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		List list = null;
		try {
			list = database.retrieveByCondition(ids, condition);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.loadCharacteristicsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.loadCharacteristicsButIds | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}


	public void saveParameterType(ParameterType parameterType, boolean force) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase)GeneralDatabaseContext.getParameterTypeDatabase();
		try {
			database.update(parameterType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws DatabaseException, CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)GeneralDatabaseContext.getCharacteristicTypeDatabase();
		try {
			database.update(characteristicType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicType | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicType | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicType | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicType | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicType | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicType | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			database.update(characteristic, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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


	public void saveParameterTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		ParameterTypeDatabase database = (ParameterTypeDatabase)GeneralDatabaseContext.getParameterTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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

	public void saveCharacteristicTypes(List list, boolean force) throws DatabaseException, CommunicationException {
		CharacteristicTypeDatabase database = (CharacteristicTypeDatabase)GeneralDatabaseContext.getCharacteristicTypeDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
		}
		catch (UpdateObjectException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicTypes | UpdateObjectException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicTypes | UpdateObjectException: " + e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicTypes | Illegal Storable Object: " + e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorMessage("DatabaseGeneralObjectLoader.saveCharacteristicTypes | VersionCollisionException: " + e.getMessage());
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristicTypes | VersionCollisionException: " + e.getMessage());
		}
	}

	public void saveCharacteristics(List list, boolean force) throws DatabaseException, CommunicationException {
		CharacteristicDatabase database = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			database.update(list, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);
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
			throw new DatabaseException("DatabaseGeneralObjectLoader.saveCharacteristics | VersionCollisionException: " + e.getMessage());
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

}
