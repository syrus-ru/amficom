/*
 * $Id: CharacteristicTypeDatabase.java,v 1.29 2005/07/14 16:08:04 bass Exp $
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
 * @version $Revision: 1.29 $, $Date: 2005/07/14 16:08:04 $
 * @author $Author: bass $
 * @module general_v1
 */

public final class CharacteristicTypeDatabase extends StorableObjectDatabase {
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.CHARACTERISTIC_TYPE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns  = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ CharacteristicTypeWrapper.COLUMN_DATA_TYPE_CODE + COMMA				
				+ CharacteristicTypeWrapper.COLUMN_SORT;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues  = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(characteristicType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(characteristicType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(characteristicType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ Integer.toString(characteristicType.getDataType().getCode()) + COMMA
			+ Integer.toString(characteristicType.getSort().value());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristicType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristicType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristicType.getName(), SIZE_NAME_COLUMN);
		preparedStatement.setInt(++startParameterNumber, characteristicType.getDataType().getCode());
		preparedStatement.setInt(++startParameterNumber, characteristicType.getSort().value());
		return startParameterNumber;
	}

	private CharacteristicType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CharacteristicType)
			return (CharacteristicType)storableObject;
		throw new IllegalDataException("CharacteristicTypeDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		super.retrieveEntity(characteristicType);
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		CharacteristicType characteristicType = storableObject == null ? null : this.fromStorableObject(storableObject);

		if (characteristicType == null) {
			characteristicType = new CharacteristicType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
																	null,
																	0L,
																	null,
																	null,
																	null,
																	DataType.RAW,
																	0);			
		}
		characteristicType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DataType.fromInt(resultSet.getInt(CharacteristicTypeWrapper.COLUMN_DATA_TYPE_CODE)),
				resultSet.getInt(CharacteristicTypeWrapper.COLUMN_SORT));

		return characteristicType;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind
						+ " for " + this.getEntityName() + " '" + characteristicType.getId()
						+ "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		final CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		super.insertEntity(characteristicType);		
	}

	@Override
	public void insert(final Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

}
