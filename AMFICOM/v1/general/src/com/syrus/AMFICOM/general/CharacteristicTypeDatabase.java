/*
 * $Id: CharacteristicTypeDatabase.java,v 1.6 2005/02/07 08:51:30 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/07 08:51:30 $
 * @author $Author: bob $
 * @module general_v1
 */

public class CharacteristicTypeDatabase extends StorableObjectDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.CHARACTERISTICTYPE_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns  = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ CharacteristicTypeWrapper.COLUMN_DATA_TYPE + COMMA				
				+ CharacteristicTypeWrapper.COLUMN_SORT;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues  = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject); 
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristicType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristicType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ Integer.toString(characteristicType.getDataType().value()) + COMMA
			+ Integer.toString(characteristicType.getSort().value());
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject); 
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseString.setString(preparedStatement, ++i, characteristicType.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, characteristicType.getDescription(), SIZE_DESCRIPTION_COLUMN);
			preparedStatement.setInt( ++i, characteristicType.getDataType().value());
			preparedStatement.setInt( ++i, characteristicType.getSort().value());
		}
		catch (SQLException sqle) {
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
		CharacteristicType characteristicType = storableObject == null ? null : this.fromStorableObject(storableObject);

		if (characteristicType == null) {
			characteristicType = new CharacteristicType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
																	null,
																	null,
																	null,
																	0,
																	0);			
		}
		characteristicType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
										 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
										 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
										 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
										 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
										 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
										 resultSet.getInt(CharacteristicTypeWrapper.COLUMN_DATA_TYPE),
										 resultSet.getInt(CharacteristicTypeWrapper.COLUMN_SORT));

		return characteristicType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		super.insertEntity(characteristicType);		
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
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
			list = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("CharacteristicTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids, condition);
	}	

	/**
	 * @deprecated use {@link StorableObjectDatabase.retrieveByCondion} and {@link TypicalCondition}
	 */
	public CharacteristicType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {		
		List list = null;

		try {
			list = this.retrieveByIds( null , StorableObjectWrapper.COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTOPHE);
		}
		catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}

		if ((list == null) || (list.isEmpty()))
				throw new ObjectNotFoundException("No characteristic type with codename: '" + codename + "'");

		return (CharacteristicType) list.get(0);
	}

}
