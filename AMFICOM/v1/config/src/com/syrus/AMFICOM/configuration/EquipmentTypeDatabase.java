/*
 * $Id: EquipmentTypeDatabase.java,v 1.20 2004/12/03 18:53:12 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.20 $, $Date: 2004/12/03 18:53:12 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class EquipmentTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";
	public static final String COLUMN_NAME = "name";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return ObjectEntities.EQUIPMENTTYPE_ENTITY;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
	return updateMultiplySQLValues;
	}
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION;
		}
		return columns;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		EquipmentType equipmentType = fromStorableObject(storableObject);
		String name = DatabaseString.toQuerySubString(equipmentType.getName());
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipmentType.getCodename()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipmentType.getDescription()) + APOSTOPHE + COMMA
			+ APOSTOPHE + (name != null ? name : "") + APOSTOPHE;
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
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
        equipmentType.setCharacteristics(characteristicDatabase.retrieveCharacteristics(equipmentType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE));
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		EquipmentType equipmentType = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, equipmentType.getCodename());
			preparedStatement.setString( ++i, equipmentType.getDescription());
			preparedStatement.setString( ++i, equipmentType.getName());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("EquipmentDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		EquipmentType equipmentType = storableObject == null ? null : fromStorableObject(storableObject);
		if (equipmentType == null){
			equipmentType = new EquipmentType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null);			
		}
		equipmentType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)));

		
		return equipmentType;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		super.insertEntity(equipmentType);		
	}

	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
	}
	
	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
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
	}
	
		
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("EquipmentTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	public List retrieveByIds(List ids, String condition) 
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
        if ((ids == null) || (ids.isEmpty()))
            list = super.retrieveByIdsOneQuery(null, condition);
        list = super.retrieveByIdsOneQuery(ids, condition);	
        if (list != null) {
            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
            characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE);
        }
        return list;
	}
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		return this.retrieveButIds(ids);
	}
	
}
