/*-
 * $Id: CharacterizableDatabase.java,v 1.21 2005/06/21 14:26:05 arseniy Exp $
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
 * @version $Revision: 1.21 $, $Date: 2005/06/21 14:26:05 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public abstract class CharacterizableDatabase extends StorableObjectDatabase {
	private Characterizable fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characterizable)
			return (Characterizable) storableObject;
		throw new IllegalDataException("CharacterizableDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		this.retrieveEntity(storableObject);

		this.retrieveCharacteristics(this.fromStorableObject(storableObject));
	}

	@Override
	protected Set<? extends StorableObject> retrieveByCondition(final String conditionQuery)
			throws RetrieveObjectException, IllegalDataException {
		Set<? extends StorableObject> collection = super.retrieveByCondition(conditionQuery);
		this.retrieveCharacteristicsByOneQuery(collection);
		return collection;
	}

	private void retrieveCharacteristics(final Characterizable characterizable) throws RetrieveObjectException, IllegalDataException {
		final String cdIdStr = DatabaseIdentifier.toSQLString(characterizable.getId());
		final String sql = CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID + EQUALS + cdIdStr;

		final CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		final Set<? extends StorableObject> soCharacteristics = characteristicDatabase.retrieveByCondition(sql);
		//@todo Find standart way
		final Set<Characteristic> characteristics = new HashSet<Characteristic>(soCharacteristics.size());
		for (final StorableObject storableObject : soCharacteristics) {
			characteristics.add((Characteristic) storableObject);
		}
		
		//final Set<Characteristic> characteristics = characteristicDatabase.retrieveByCondition(sql);

		characterizable.setCharacteristics0(characteristics);
	}

	private void retrieveCharacteristicsByOneQuery(final Set<? extends StorableObject> storableObjects)
			throws RetrieveObjectException, IllegalDataException {
		if (storableObjects == null || storableObjects.isEmpty())
			return;
		assert StorableObject.hasSingleTypeEntities(storableObjects) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final StorableObjectDatabase database = DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		final String conditionString = idsEnumerationString(storableObjects, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID, true).toString();
		final Set<? extends StorableObject> soCharacteristics = database.retrieveByCondition(conditionString);
		//@todo Find standart way
		final Set<Characteristic> characteristics = new HashSet<Characteristic>(soCharacteristics.size());
		for (final StorableObject storableObject : storableObjects) {
			characteristics.add((Characteristic) storableObject);
		}

		final Map<Identifier, Set<Characteristic>> orderedCharacteristicsMap = new HashMap<Identifier, Set<Characteristic>>();
		for (final Iterator<? extends StorableObject> it = characteristics.iterator(); it.hasNext();) {
			final Characteristic characteristic = (Characteristic) it.next();
			final Identifier characterizableId = characteristic.getCharacterizableId();
			Set<Characteristic> orderedCharacteristics = orderedCharacteristicsMap.get(characterizableId);
			if (orderedCharacteristics == null) {
				orderedCharacteristics = new HashSet<Characteristic>();
				orderedCharacteristicsMap.put(characterizableId, orderedCharacteristics);
			}
			orderedCharacteristics.add(characteristic);
		}

		for (final Iterator<? extends StorableObject> it = storableObjects.iterator(); it.hasNext();) {
			final Characterizable characterizable = this.fromStorableObject(it.next());
			final Identifier characterizableId = characterizable.getId();
			Set<Characteristic> orderedCharacteristics = orderedCharacteristicsMap.get(characterizableId);
			if (orderedCharacteristics == null)
				orderedCharacteristics = Collections.emptySet();

			characterizable.setCharacteristics0(orderedCharacteristics);
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		super.insertEntity(storableObject);

		final Characterizable characterizable = this.fromStorableObject(storableObject);
		final CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		characteristicDatabase.insert(characterizable.getCharacteristics());
	}

	@Override
	public void insert(final Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);

		final Set<Characteristic> characteristics = new HashSet<Characteristic>();
		Characterizable characterizable;
		for (final Iterator<? extends StorableObject> it = storableObjects.iterator(); it.hasNext();) {
			characterizable = this.fromStorableObject(it.next());
			characteristics.addAll(characterizable.getCharacteristics());
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		characteristicDatabase.insert(characteristics);
	}

	@Override
	public void update(final StorableObject storableObject, final Identifier modifierId, final int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);

		try {
			this.updateCharacteristics(this.fromStorableObject(storableObject));
		} catch (IllegalDataException ide) {
			String mesg = "Illegal storable object " + this.getEntityName() + " '" + storableObject.getId() + "' -- " + ide.getMessage();
			throw new UpdateObjectException(mesg, ide);
		}
	}

	@Override
	public void update(final Set<? extends StorableObject> storableObjects, final Identifier modifierId, final int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		this.updateCharacteristics(storableObjects);
	}

	private void updateCharacteristics(final Characterizable characterizable) throws UpdateObjectException {
		final Set<Identifier> dbCharacteristicIds = new HashSet<Identifier>();

		final String cdIdStr = DatabaseIdentifier.toSQLString(characterizable.getId());
		final String sql = CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID + EQUALS + cdIdStr;
		final CharacteristicDatabase database = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		try {
			final Set<? extends StorableObject> soCharacteristics = database.retrieveByCondition(sql);
			dbCharacteristicIds.addAll(Identifier.createIdentifiers(soCharacteristics));
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException("Cannot retrieve from database characteristics for " + this.getEntityName()
					+ " '" + cdIdStr + "' -- " + ae.getMessage(), ae);
		}

		final Set<Characteristic> characteristics = characterizable.getCharacteristics();
		final Set<Identifier> characteristicIds = Identifier.createIdentifiers(characteristics);

		Set<Characteristic> insertCharacteristics = null;
		for (final Characteristic characteristic : characteristics) {
			if (!dbCharacteristicIds.contains(characteristic.getId())) {
				if (insertCharacteristics == null)
					insertCharacteristics = new HashSet<Characteristic>();
				insertCharacteristics.add(characteristic);
			}
		}

		Set<Identifier> deleteCharacteristicIds = null;
		for (final Identifier id : dbCharacteristicIds) {
			if (!characteristicIds.contains(id)) {
				if (deleteCharacteristicIds == null)
					deleteCharacteristicIds = new HashSet<Identifier>();
				deleteCharacteristicIds.add(id);
			}
		}

		try {
			database.insert(insertCharacteristics);
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException("Cannot insert characteristics for " + this.getEntityName()
					+ " " + cdIdStr + " -- " + ae.getMessage(), ae);
		}

		database.delete(deleteCharacteristicIds);
	}

	private void updateCharacteristics(final Set<? extends StorableObject> storableObjects) throws UpdateObjectException {
		assert StorableObject.hasSingleTypeEntities(storableObjects) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final Set<Identifier> dbCharacteristicIds = new HashSet<Identifier>();

		final CharacteristicDatabase database = (CharacteristicDatabase) DatabaseContext.getDatabase(ObjectEntities.CHARACTERISTIC_CODE);
		final String sql = idsEnumerationString(storableObjects, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID, true).toString();
		try {
			final Set<? extends StorableObject> soCharacteristics = database.retrieveByCondition(sql);
			dbCharacteristicIds.addAll(Identifier.createIdentifiers(soCharacteristics));
		}
		catch (ApplicationException ae) {
			throw new UpdateObjectException(ae);
		}

		final Set<Characteristic> characteristics = new HashSet<Characteristic>();

		for (final StorableObject storableObject : storableObjects) {
			try {
				final Characterizable characterizable = this.fromStorableObject(storableObject);
				characteristics.addAll(characterizable.getCharacteristics());
			}
			catch (IllegalDataException ide) {
				throw new UpdateObjectException(ide);
			}
		}

		final Set<Identifier> characteristicIds = Identifier.createIdentifiers(characteristics);

		Set<Characteristic> insertCharacteristics = null;
		for (final Characteristic characteristic : characteristics) {
			if (!dbCharacteristicIds.contains(characteristic.getId())) {
				if (insertCharacteristics == null)
					insertCharacteristics = new HashSet<Characteristic>();
				insertCharacteristics.add(characteristic);
			}
		}

		Set<Identifier> deleteCharacteristicIds = null;
		for (final Identifier id : dbCharacteristicIds) {
			if (!characteristicIds.contains(id)) {
				if (deleteCharacteristicIds == null)
					deleteCharacteristicIds = new HashSet<Identifier>();
				deleteCharacteristicIds.add(id);
			}
		}

		try {
			database.insert(insertCharacteristics);
		}
		catch (ApplicationException ae) {
			String mesg = "Cannot insert characteristics for multiple " + this.getEntityName() + " -- " + ae.getMessage();
			throw new UpdateObjectException(mesg, ae);
		}

		database.delete(deleteCharacteristicIds);
	}
}
