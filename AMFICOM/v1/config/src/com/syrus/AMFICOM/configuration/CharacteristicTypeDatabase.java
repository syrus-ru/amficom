/*
 * $Id: CharacteristicTypeDatabase.java,v 1.24 2004/11/10 15:23:51 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.24 $, $Date: 2004/11/10 15:23:51 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class CharacteristicTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";
	public static final String COLUMN_DATA_TYPE				= "data_type";
	public static final String COLUMN_SORT				= "sort";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return ObjectEntities.CHARACTERISTICTYPE_ENTITY;
	}
	
	protected String getColumns() {
		if (columns == null){
			columns  = super.getColumns() + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_DATA_TYPE + COMMA				
				+ COLUMN_SORT;
		}
		return columns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues  = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		CharacteristicType characteristicType = fromStorableObject(storableObject); 
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristicType.getCodename()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristicType.getDescription()) + APOSTOPHE + COMMA
			+ Integer.toString(characteristicType.getDataType().value()) + COMMA
			+ Integer.toString(characteristicType.getSort().value());
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		CharacteristicType characteristicType = fromStorableObject(storableObject); 
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, characteristicType.getCodename());
			preparedStatement.setString( ++i, characteristicType.getDescription());
			preparedStatement.setInt( ++i, characteristicType.getDataType().value());
			preparedStatement.setInt( ++i, characteristicType.getSort().value());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("CharacteristicTypeDatabase.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	
	private CharacteristicType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CharacteristicType)
			return (CharacteristicType)storableObject;
		throw new IllegalDataException("CharacteristicTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		super.retrieveEntity(characteristicType);
	}

	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CharacteristicType characteristicType = storableObject == null ? null : fromStorableObject(storableObject);
		
		if (characteristicType == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			characteristicType = new CharacteristicType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 0,
										   0);			
		}
		characteristicType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
										 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
										 /**
											* @todo when change DB Identifier model ,change getString() to
											*       getLong()
											*/
										 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
										 /**
											* @todo when change DB Identifier model ,change getString() to
											*       getLong()
											*/
										 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
										 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
										 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
										 resultSet.getInt(COLUMN_DATA_TYPE),
										 resultSet.getInt(COLUMN_SORT));

		
		return characteristicType;
	}

	
	

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		super.insertEntity(characteristicType);		
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
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
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
            Log.debugMessage("CharacteristicTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return super.retrieveByIdsOneQuery(null, condition);
		return super.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids, condition);
	}	

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		return this.retrieveButIds(ids);
	}
}
