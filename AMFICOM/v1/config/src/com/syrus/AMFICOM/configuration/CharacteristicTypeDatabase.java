/*
 * $Id: CharacteristicTypeDatabase.java,v 1.13 2004/09/06 11:36:22 max Exp $
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
import java.sql.Timestamp;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
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

/**
 * @version $Revision: 1.13 $, $Date: 2004/09/06 11:36:22 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class CharacteristicTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";
	public static final String COLUMN_DATA_TYPE				= "data_type";
	public static final String COLUMN_IS_EDITABLE			= "is_editable";
	public static final String COLUMN_IS_VISIBLE			= "is_visible";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private String updateColumns;
	private String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return "CharacteristicType";
	}
	
	protected String getTableName() {
		return ObjectEntities.CHARACTERISTICTYPE_ENTITY;
	}
	
	
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns  = COLUMN_ID + COMMA
				+ COLUMN_CREATED + COMMA
				+ COLUMN_MODIFIED + COMMA
				+ COLUMN_CREATOR_ID + COMMA
				+ COLUMN_MODIFIER_ID + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_DATA_TYPE + COMMA
				+ COLUMN_IS_EDITABLE + COMMA
				+ COLUMN_IS_VISIBLE;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues  = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		CharacteristicType characteristicType = fromStorableObject(storableObject); 
		String ctIdStr = characteristicType.getId().toSQLString();
		String sql = ctIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(characteristicType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(characteristicType.getModified()) + COMMA
			+ characteristicType.getCreatorId().toSQLString() + COMMA
			+ characteristicType.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + characteristicType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + characteristicType.getDescription() + APOSTOPHE + COMMA
			+ Integer.toString(characteristicType.getDataType().value()) + COMMA
			+ (characteristicType.isEditable()?"1":"0") + COMMA
			+ (characteristicType.isVisible()?"1":"0");
		return sql;
	}
	
	protected void setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		CharacteristicType characteristicType = fromStorableObject(storableObject); 
		String ctIdStr = characteristicType.getId().getCode();
		try {
			preparedStatement.setString( 1, ctIdStr);
			preparedStatement.setTimestamp( 2, new Timestamp(characteristicType.getCreated().getTime()));
			preparedStatement.setTimestamp( 3, new Timestamp(characteristicType.getModified().getTime()));
			preparedStatement.setString( 4, characteristicType.getCreatorId().getCode());
			preparedStatement.setString( 5, characteristicType.getModifierId().getCode());
			preparedStatement.setString( 6, characteristicType.getCodename());
			preparedStatement.setString( 7, characteristicType.getDescription());
			preparedStatement.setInt( 8, characteristicType.getDataType().value());
			preparedStatement.setInt( 9, characteristicType.isEditable()?'1':'0');
			preparedStatement.setInt( 10, characteristicType.isVisible()?'1':'0');
			preparedStatement.setString( 11, ctIdStr);
		} catch (SQLException sqle) {
			throw new UpdateObjectException("CharacteristicTypeDatabase.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
	}
	
	
	private CharacteristicType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CharacteristicType)
			return (CharacteristicType)storableObject;
		throw new IllegalDataException("CharacteristicTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		this.retrieveEntity(characteristicType);
	}

	protected String retrieveQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_CODENAME + COMMA
		+ COLUMN_DESCRIPTION + COMMA
		+ COLUMN_DATA_TYPE + COMMA
		+ COLUMN_IS_EDITABLE + COMMA
		+ COLUMN_IS_VISIBLE
		+ SQL_FROM + ObjectEntities.CHARACTERISTICTYPE_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CharacteristicType characteristicType = fromStorableObject(storableObject);
		
		if (characteristicType == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			characteristicType = new CharacteristicType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 0,
										   false, false);			
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
										 resultSet.getString(COLUMN_CODENAME),
										 resultSet.getString(COLUMN_DESCRIPTION),
										 resultSet.getInt(COLUMN_DATA_TYPE),
										 (resultSet.getInt(COLUMN_IS_EDITABLE) == 0)?false:true,
										 (resultSet.getInt(COLUMN_IS_VISIBLE) == 0)?false:true);

		
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
		try {
			super.insertEntity(characteristicType);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
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

	public List retrieveAll() throws IllegalDataException, RetrieveObjectException {		
		return retriveByIdsOneQuery(null, null);
	}
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null, condition);
		return retriveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids, condition);
	}	

}
