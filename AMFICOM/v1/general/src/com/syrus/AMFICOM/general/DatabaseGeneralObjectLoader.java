/*
 * $Id: DatabaseGeneralObjectLoader.java,v 1.1 2005/01/13 14:27:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/13 14:27:38 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class DatabaseGeneralObjectLoader implements GeneralObjectLoader {

	private StorableObjectDatabase getDatabase(short entityCode){
		StorableObjectDatabase database = null;
		switch (entityCode) {
			case ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE:
				database = GeneralDatabaseContext.getCharacteristicTypeDatabase();
				break;
			case ObjectEntities.CHARACTERISTIC_ENTITY_CODE:
				database = GeneralDatabaseContext.getCharacteristicDatabase();
				break;
			default:
				Log.errorMessage("DatabaseGeneralObjectLoader.getDatabase | Unknown entity: " + ObjectEntities.codeToString(entityCode));                
		}
		return database;
	}

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
			StorableObjectDatabase database = this.getDatabase(entityCode); 
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
			delete(null, ids);
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException {
		return new CharacteristicType(id);
	}

	public Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException {
		return new Characteristic(id);
	}


  // for multiple objects

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
		    StorableObjectDatabase database = this.getDatabase(entityCode);
			
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
