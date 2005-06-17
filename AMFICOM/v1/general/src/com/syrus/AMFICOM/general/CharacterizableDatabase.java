/*-
 * $Id: CharacterizableDatabase.java,v 1.16 2005/06/17 11:00:57 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
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

/**
 * @version $Revision: 1.16 $, $Date: 2005/06/17 11:00:57 $
 * @author $Author: bass $
 * @module general_v1
 */
public abstract class CharacterizableDatabase extends StorableObjectDatabase {
	private Characterizable fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characterizable)
			return (Characterizable) storableObject;
		throw new IllegalDataException("CharacterizableDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		this.retrieveEntity(storableObject);

		this.retrieveCharacteristics(this.fromStorableObject(storableObject));
	}

	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Set collection = super.retrieveByCondition(conditionQuery);
		this.retrieveCharacteristicsByOneQuery(collection);
		return collection;
	}

	private void retrieveCharacteristics(Characterizable characterizable) throws RetrieveObjectException, IllegalDataException {
		String cdIdStr = DatabaseIdentifier.toSQLString(characterizable.getId());
		String sql = CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID + EQUALS + cdIdStr;

		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		Set characteristics = characteristicDatabase.retrieveByCondition(sql);

		characterizable.setCharacteristics0(characteristics);
	}

	private void retrieveCharacteristicsByOneQuery(final Set storableObjects)
			throws RetrieveObjectException, IllegalDataException {
		if (storableObjects == null || storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects);

		final Set characteristics = DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE)
				.retrieveByCondition(idsEnumerationString(storableObjects, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID, true)
				.toString());

		Map orderedCharacteristicsMap = new HashMap();
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			final Characteristic characteristic = (Characteristic) it.next();
			final Identifier characterizableId = characteristic.getCharacterizableId();
			Set orderedCharacteristics = (Set) orderedCharacteristicsMap.get(characterizableId);
			if (orderedCharacteristics == null) {
				orderedCharacteristics = new HashSet();
				orderedCharacteristicsMap.put(characterizableId, orderedCharacteristics);
			}
			orderedCharacteristics.add(characteristic);
		}

		Characterizable characterizable;
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			characterizable = this.fromStorableObject((StorableObject) it.next());
			final Identifier characterizableId = characterizable.getId();
			Set orderedCharacteristics = (Set) orderedCharacteristicsMap.get(characterizableId);
			if (orderedCharacteristics == null)
				orderedCharacteristics = Collections.EMPTY_SET;

			characterizable.setCharacteristics0(orderedCharacteristics);
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		super.insertEntity(storableObject);

		Characterizable characterizable = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		characteristicDatabase.insert(characterizable.getCharacteristics());
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);

		Set characteristics = new HashSet();
		Characterizable characterizable;
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			characterizable = this.fromStorableObject((StorableObject) it.next());
			characteristics.addAll(characterizable.getCharacteristics());
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		characteristicDatabase.insert(characteristics);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);

		try {
			this.updateCharacteristics(this.fromStorableObject(storableObject));
		}
		catch (IllegalDataException ide) {
			String mesg = "Illegal storable object " + this.getEntityName() + " '" + storableObject.getId() + "' -- " + ide.getMessage();
			throw new UpdateObjectException(mesg, ide);
		}
	}

	public void update(Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		this.updateCharacteristics(storableObjects);
	}

	private void updateCharacteristics(Characterizable characterizable) throws UpdateObjectException {
		Set characteristics;
		Characteristic characteristic;

		String cdIdStr = DatabaseIdentifier.toSQLString(characterizable.getId());
		String sql = CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID + EQUALS + cdIdStr;

		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		try {
			characteristics = characteristicDatabase.retrieveByCondition(sql);
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException("Cannot retrieve from database characteristics for " + this.getEntityName()
					+ " '" + cdIdStr + "' -- " + ae.getMessage(), ae);
		}

		Set dbCharacteristicIds = new HashSet(characteristics.size());
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			dbCharacteristicIds.add(characteristic.getId());
		}

		characteristics = characterizable.getCharacteristics();

		Set characteristicIds = new HashSet(characteristics.size());
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			characteristicIds.add(characteristic.getId());
		}

		Set insertCharacteristics = null;
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			if (!dbCharacteristicIds.contains(characteristic.getId())) {
				if (insertCharacteristics == null)
					insertCharacteristics = new HashSet();
				insertCharacteristics.add(characteristic);
			}
		}

		Set deleteCharacteristicIds = null;
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
			throw new UpdateObjectException("Cannot insert characteristics for " + this.getEntityName()
					+ " " + cdIdStr + " -- " + ae.getMessage(), ae);
		}

		characteristicDatabase.delete(deleteCharacteristicIds);
	}

	private void updateCharacteristics(final Set storableObjects) throws UpdateObjectException {
		assert StorableObject.hasSingleTypeEntities(storableObjects);

		Set characteristics;
		Characteristic characteristic;

		final StorableObjectDatabase characteristicDatabase = DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		try {
			characteristics = characteristicDatabase.retrieveByCondition(idsEnumerationString(storableObjects, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID, true).toString());
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException(ae);
		}

		Set dbCharacteristicIds = new HashSet(characteristics.size());
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

		Set characteristicIds = new HashSet();
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			characteristicIds.add(characteristic.getId());
		}

		Set insertCharacteristics = null;
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			characteristic = (Characteristic) it.next();
			if (!dbCharacteristicIds.contains(characteristic.getId())) {
				if (insertCharacteristics == null)
					insertCharacteristics = new HashSet();
				insertCharacteristics.add(characteristic);
			}
		}

		Set deleteCharacteristicIds = null;
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
			String mesg = "Cannot insert characteristics for multiple " + this.getEntityName() + " -- " + ae.getMessage();
			throw new UpdateObjectException(mesg, ae);
		}

		characteristicDatabase.delete(deleteCharacteristicIds);
	}
}
