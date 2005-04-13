/*
 * $Id: CharacteristicDatabase.java,v 1.31 2005/04/13 15:00:29 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.31 $, $Date: 2005/04/13 15:00:29 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class CharacteristicDatabase extends StorableObjectDatabase {
	private static final int SIZE_VALUE_COLUMN = 256;

	private static String columns;
	private static String updateMultipleSQLValues;

	protected String getEnityName() {
		return ObjectEntities.CHARACTERISTIC_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
			+ StorableObjectWrapper.COLUMN_NAME + COMMA
			+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
			+ CharacteristicWrapper.COLUMN_VALUE + COMMA
			+ CharacteristicWrapper.COLUMN_EDITABLE + COMMA
			+ CharacteristicWrapper.COLUMN_VISIBLE + COMMA
			+ CharacteristicWrapper.COLUMN_SORT +	COMMA
			+ CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		int sort = characteristic.getSort().value();
		String sql = DatabaseIdentifier.toSQLString(characteristic.getType().getId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE  + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getValue(), SIZE_VALUE_COLUMN) + APOSTOPHE + COMMA
			+ (characteristic.isEditable()?"1":"0") + COMMA
			+ (characteristic.isVisible()?"1":"0") + COMMA
			+ sort + COMMA
			+ DatabaseIdentifier.toSQLString(characteristic.getCharacterizableId());
			/**
			 * check sort support
			 */
		return sql;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject,
			PreparedStatement preparedStatement, int startParameterNumber) throws IllegalDataException, SQLException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		int sort = characteristic.getSort().value();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, characteristic.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristic.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristic.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristic.getValue(), SIZE_VALUE_COLUMN);
		preparedStatement.setInt( ++startParameterNumber, characteristic.isEditable()? 1:0);
		preparedStatement.setInt( ++startParameterNumber, characteristic.isVisible()? 1:0);
		preparedStatement.setInt( ++startParameterNumber, sort);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, characteristic.getCharacterizableId());
		return startParameterNumber;
	}	

	private Characteristic fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characteristic)
			return (Characteristic)storableObject;
		throw new IllegalDataException("CharacteristicDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		super.retrieveEntity(characteristic);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws RetrieveObjectException, SQLException, IllegalDataException {
		Characteristic characteristic = (storableObject == null) ? null : this.fromStorableObject(storableObject); 
		if (characteristic == null) {
			characteristic = new Characteristic(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
																null,
																0L,
																null,
																null,
																null,
																0,
																null,
																null,
																false,
																false);			
		}

		int sort = resultSet.getInt(CharacteristicWrapper.COLUMN_SORT);
		Identifier characterizableId = DatabaseIdentifier.getIdentifier(resultSet, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID);

		CharacteristicType characteristicType;
		try {
			characteristicType = (CharacteristicType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID),
					true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String value = DatabaseString.fromQuerySubString(resultSet.getString(CharacteristicWrapper.COLUMN_VALUE));

		characteristic.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									 characteristicType,
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
									 sort,
									 value != null ? value : "",
									 characterizableId,
									 (resultSet.getInt(CharacteristicWrapper.COLUMN_EDITABLE) == 0) ? false : true,
									 (resultSet.getInt(CharacteristicWrapper.COLUMN_VISIBLE) == 0) ? false : true);
		return characteristic;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  characteristic.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		super.insertEntity(characteristic);		
	}

}
