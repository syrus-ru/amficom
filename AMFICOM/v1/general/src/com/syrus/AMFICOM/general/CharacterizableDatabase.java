/*-
 * $Id: CharacterizableDatabase.java,v 1.3 2005/03/23 14:59:22 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @version $Revision: 1.3 $, $Date: 2005/03/23 14:59:22 $
 * @author $Author: bass $
 * @module general_v1
 */
public abstract class CharacterizableDatabase extends StorableObjectDatabase {

	public CharacterizableDatabase() {
		super();
	}

	private Characterizable fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characterizable)
			return (Characterizable) storableObject;
		throw new IllegalDataException("CharacterizableDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		this.retrieveEntity(storableObject);

		this.retrieveCharacteristics(this.fromStorableObject(storableObject));
	}

	protected Collection retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Collection collection = super.retrieveByCondition(conditionQuery);
		this.retrieveCharacteristicsByOneQuery(collection);
		return collection;
	}

	private void retrieveCharacteristics(Characterizable characterizable) throws RetrieveObjectException, IllegalDataException {
		String cdIdStr = DatabaseIdentifier.toSQLString(characterizable.getId());
		CharacteristicSort sort = characterizable.getCharacteristicSort();
		String sql = CharacteristicWrapper.COLUMN_SORT + EQUALS + Integer.toString(sort.value())
				+ SQL_AND
				+ CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID + EQUALS + cdIdStr;

		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.characteristicDatabase;
		Collection characteristics = characteristicDatabase.retrieveByCondition(sql);

		characterizable.setCharacteristics0(characteristics);
	}

	private void retrieveCharacteristicsByOneQuery(Collection storableObjects) throws RetrieveObjectException, IllegalDataException {
		if (storableObjects == null || storableObjects.isEmpty())
			return;

		CharacteristicSort sort = this.getOnlyOneCharacteristicSort(storableObjects);
		StringBuffer stringBuffer = new StringBuffer(CharacteristicWrapper.COLUMN_SORT
				+ EQUALS
				+ Integer.toString(sort.value())
				+ SQL_AND);
		stringBuffer.append(idsEnumerationString(storableObjects, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID, true));

		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.characteristicDatabase;
		Collection characteristics = characteristicDatabase.retrieveByCondition(stringBuffer.toString());

		Map orderedCharacteristicsMap = new HashMap();
		Characteristic characteristic;
		Identifier characterizableId;
		Collection orderedCharacteristics;
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			characterizableId = characteristic.getCharacterizableId();
			orderedCharacteristics = (Collection) orderedCharacteristicsMap.get(characterizableId);
			if (orderedCharacteristics == null) {
				orderedCharacteristics = new ArrayList();
				orderedCharacteristicsMap.put(characterizableId, orderedCharacteristics);
			}
			orderedCharacteristics.add(characteristic);
		}

		Characterizable characterizable;
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			characterizable = this.fromStorableObject((StorableObject) it.next());
			characterizableId = characterizable.getId();
			orderedCharacteristics = (Collection) orderedCharacteristicsMap.get(characterizableId);
			if (orderedCharacteristics == null)
				orderedCharacteristics = Collections.EMPTY_SET;

			characterizable.setCharacteristics0(orderedCharacteristics);
		}
	}

	private CharacteristicSort getOnlyOneCharacteristicSort(Collection storableObjects) throws IllegalDataException {
		Characterizable characterizable = this.fromStorableObject((StorableObject) storableObjects.iterator().next());
		CharacteristicSort sort0 = characterizable.getCharacteristicSort();
		CharacteristicSort sort;
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			characterizable = this.fromStorableObject((StorableObject) it.next());
			sort = characterizable.getCharacteristicSort();
			if (sort.value() != sort0.value())
				throw new IllegalDataException("Objects have not the same sort of characteristics");
		}
		return sort0;
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		this.insertEntity(storableObject);

		Characterizable characterizable = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.characteristicDatabase;
		characteristicDatabase.insert(characterizable.getCharacteristics());
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);

		Collection characteristics = new HashSet();
		Characterizable characterizable;
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			characterizable = this.fromStorableObject((StorableObject) it.next());
			characteristics.addAll(characterizable.getCharacteristics());
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.characteristicDatabase;
		characteristicDatabase.insert(characteristics);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException,
				UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
		}

		try {
			this.updateCharacteristics(this.fromStorableObject(storableObject));
		}
		catch (IllegalDataException ide) {
			String mesg = "Illegal storable object " + this.getEnityName() + " '" + storableObject.getId() + "' -- " + ide.getMessage();
			throw new UpdateObjectException(mesg, ide);
		}
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);
				break;
			case UPDATE_CHECK:
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
		}

		this.updateCharacteristics(storableObjects);
	}

	private void updateCharacteristics(Characterizable characterizable) throws UpdateObjectException {
		Collection characteristics;
		Characteristic characteristic;

		String cdIdStr = DatabaseIdentifier.toSQLString(characterizable.getId());
		CharacteristicSort sort = characterizable.getCharacteristicSort();
		String sql = CharacteristicWrapper.COLUMN_SORT + EQUALS + Integer.toString(sort.value())
				+ SQL_AND
				+ CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID + EQUALS + cdIdStr;

		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.characteristicDatabase;
		try {
			characteristics = characteristicDatabase.retrieveByCondition(sql);
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException("Cannot retrieve from database characteristics for " + this.getEnityName()
					+ " '" + cdIdStr + "' -- " + ae.getMessage(), ae);
		}

		Collection dbCharacteristicIds = new HashSet(characteristics.size());
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			dbCharacteristicIds.add(characteristic.getId());
		}

		characteristics = characterizable.getCharacteristics();

		Collection characteristicIds = new HashSet(characteristics.size());
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			characteristicIds.add(characteristic.getId());
		}

		Collection insertCharacteristics = null;
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			if (!dbCharacteristicIds.contains(characteristic.getId())) {
				if (insertCharacteristics == null)
					insertCharacteristics = new HashSet();
				insertCharacteristics.add(characteristic);
			}
		}

		Collection deleteCharacteristicIds = null;
		Identifier id;
		for (Iterator it = dbCharacteristicIds.iterator(); it.hasNext();) {
			id = (Identifier) it.next();
			if (!characteristicIds.contains(id)) {
				if (deleteCharacteristicIds == null)
					deleteCharacteristicIds = new HashSet();
				deleteCharacteristicIds.add(id);
			}
		}

		try {
			characteristicDatabase.insert(insertCharacteristics);
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException("Cannot insert characteristics for " + this.getEnityName()
					+ " " + cdIdStr + " -- " + ae.getMessage(), ae);
		}

		characteristicDatabase.delete(deleteCharacteristicIds);
	}

	private void updateCharacteristics(Collection storableObjects) throws UpdateObjectException {
		CharacteristicSort sort;
		try {
			sort = this.getOnlyOneCharacteristicSort(storableObjects);
		}
		catch (IllegalDataException ide) {
			throw new UpdateObjectException(ide);
		}
		Collection characteristics;
		Characteristic characteristic;

		StringBuffer stringBuffer = new StringBuffer(CharacteristicWrapper.COLUMN_SORT + EQUALS + Integer.toString(sort.value()) + SQL_AND);
		try {
			stringBuffer.append(idsEnumerationString(storableObjects, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID, true));
		}
		catch (IllegalDataException ide) {
			throw new UpdateObjectException(ide);
		}

		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) GeneralDatabaseContext.characteristicDatabase;
		try {
			characteristics = characteristicDatabase.retrieveByCondition(stringBuffer.toString());
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException(ae);
		}

		Collection dbCharacteristicIds = new HashSet(characteristics.size());
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			dbCharacteristicIds.add(characteristic.getId());
		}

		Characterizable characterizable;
		characteristics = new HashSet();
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			try {
				characterizable = this.fromStorableObject((StorableObject) it.next());
			}
			catch (IllegalDataException ide) {
				throw new UpdateObjectException(ide);
			}
			characteristics.addAll(characterizable.getCharacteristics());
		}

		Collection characteristicIds = new HashSet();
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			characteristicIds.add(characteristic.getId());
		}

		Collection insertCharacteristics = null;
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			if (!dbCharacteristicIds.contains(characteristic.getId())) {
				if (insertCharacteristics == null)
					insertCharacteristics = new HashSet();
				insertCharacteristics.add(characteristic);
			}
		}

		Collection deleteCharacteristicIds = null;
		Identifier id;
		for (Iterator it = dbCharacteristicIds.iterator(); it.hasNext();) {
			id = (Identifier) it.next();
			if (!characteristicIds.contains(id)) {
				if (deleteCharacteristicIds == null)
					deleteCharacteristicIds = new HashSet();
				deleteCharacteristicIds.add(id);
			}
		}

		try {
			characteristicDatabase.insert(insertCharacteristics);
		}
		catch (ApplicationException ae) {
			String mesg = "Cannot insert characteristics for multiple " + this.getEnityName() + " -- " + ae.getMessage();
			throw new UpdateObjectException(mesg, ae);
		}

		characteristicDatabase.delete(deleteCharacteristicIds);
	}
}
