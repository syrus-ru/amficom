/*
 * $Id: EquipmentTypeDatabase.java,v 1.32 2005/02/03 08:37:00 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.32 $, $Date: 2005/02/03 08:37:00 $
 * @author $Author: bob $
 * @module config_v1
 */

public class EquipmentTypeDatabase extends StorableObjectDatabase {
	private static final int SIZE_MANUFACTURER_COLUMN = 64; 

	private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.EQUIPMENTTYPE_ENTITY;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
            + QUESTION + COMMA
            + QUESTION + COMMA
			+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ EquipmentTypeWrapper.COLUMN_CODENAME + COMMA
				+ EquipmentTypeWrapper.COLUMN_DESCRIPTION + COMMA
				+ EquipmentTypeWrapper.COLUMN_NAME + COMMA
				+ EquipmentTypeWrapper.COLUMN_MANUFACTURER + COMMA
				+ EquipmentTypeWrapper.COLUMN_MANUFACTURER_CODE;
		}
		return columns;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipmentType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipmentType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipmentType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipmentType.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipmentType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTOPHE;
		return sql;
	}

	private EquipmentType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EquipmentType)
			return (EquipmentType)storableObject;
		throw new IllegalDataException("EquipmentTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		super.retrieveEntity(equipmentType);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		equipmentType.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(equipmentType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE));
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseString.setString(preparedStatement, ++i, equipmentType.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, equipmentType.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, equipmentType.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, equipmentType.getManufacturer(), SIZE_MANUFACTURER_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, equipmentType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("EquipmentDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		EquipmentType equipmentType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (equipmentType == null) {
			equipmentType = new EquipmentType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
															null,
															null,
															null,
															null,
															null,
															null);			
		}
		equipmentType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
											DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
											DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
											DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
											DatabaseString.fromQuerySubString(resultSet.getString(EquipmentTypeWrapper.COLUMN_CODENAME)),
											DatabaseString.fromQuerySubString(resultSet.getString(EquipmentTypeWrapper.COLUMN_DESCRIPTION)),
											DatabaseString.fromQuerySubString(resultSet.getString(EquipmentTypeWrapper.COLUMN_NAME)),
											DatabaseString.fromQuerySubString(resultSet.getString(EquipmentTypeWrapper.COLUMN_MANUFACTURER)),
											DatabaseString.fromQuerySubString(resultSet.getString(EquipmentTypeWrapper.COLUMN_MANUFACTURER_CODE)));

		return equipmentType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		super.insertEntity(equipmentType);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(equipmentType);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(equipmentType, true);
				characteristicDatabase.updateCharacteristics(equipmentType);
				break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(equipmentType, false);
			characteristicDatabase.updateCharacteristics(equipmentType);
			break;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(GeneralDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {           
			Log.debugMessage("EquipmentTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);

		if (list != null) {
			CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase) (GeneralDatabaseContext.getCharacteristicDatabase());
			Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list,
					CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE);
			if (characteristicMap != null)
				for (Iterator iter = list.iterator(); iter.hasNext();) {
					EquipmentType equipmentType = (EquipmentType) iter.next();
					List characteristics = (List) characteristicMap.get(equipmentType.getId());
					equipmentType.setCharacteristics0(characteristics);
				}
		}
		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		return this.retrieveButIds(ids);
	}

}
