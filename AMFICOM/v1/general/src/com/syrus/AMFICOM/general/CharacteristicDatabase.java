/*
 * $Id: CharacteristicDatabase.java,v 1.42 2005/07/24 17:34:32 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.42 $, $Date: 2005/07/24 17:34:32 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public final class CharacteristicDatabase extends StorableObjectDatabase {
	private static final int SIZE_VALUE_COLUMN = 256;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.CHARACTERISTIC_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
			+ StorableObjectWrapper.COLUMN_NAME + COMMA
			+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
			+ CharacteristicWrapper.COLUMN_VALUE + COMMA
			+ CharacteristicWrapper.COLUMN_EDITABLE + COMMA
			+ CharacteristicWrapper.COLUMN_VISIBLE + COMMA
			+ CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Characteristic characteristic = this.fromStorableObject(storableObject);
		final String sql = DatabaseIdentifier.toSQLString(characteristic.getType().getId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(characteristic.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(characteristic.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE  + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(characteristic.getValue(), SIZE_VALUE_COLUMN) + APOSTROPHE + COMMA
			+ (characteristic.isEditable()?"1":"0") + COMMA
			+ (characteristic.isVisible()?"1":"0") + COMMA
			+ DatabaseIdentifier.toSQLString(characteristic.getCharacterizableId());
			/**
			 * check sort support
			 */
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Characteristic characteristic = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, characteristic.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristic.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristic.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, characteristic.getValue(), SIZE_VALUE_COLUMN);
		preparedStatement.setInt( ++startParameterNumber, characteristic.isEditable()? 1:0);
		preparedStatement.setInt( ++startParameterNumber, characteristic.isVisible()? 1:0);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, characteristic.getCharacterizableId());
		return startParameterNumber;
	}	

	private Characteristic fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characteristic)
			return (Characteristic)storableObject;
		throw new IllegalDataException("CharacteristicDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws RetrieveObjectException, SQLException, IllegalDataException {
		Characteristic characteristic = (storableObject == null) ? null : this.fromStorableObject(storableObject);
		if (characteristic == null) {
			characteristic = new Characteristic(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null,
					null,
					false,
					false);			
		}

		final Identifier characterizableId = DatabaseIdentifier.getIdentifier(resultSet, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID);

		CharacteristicType characteristicType;
		try {
			characteristicType = (CharacteristicType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID),
					true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		final String value = DatabaseString.fromQuerySubString(resultSet.getString(CharacteristicWrapper.COLUMN_VALUE));

		characteristic.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				characteristicType,
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				value != null ? value : "",
				characterizableId,
				(resultSet.getInt(CharacteristicWrapper.COLUMN_EDITABLE) == 0) ? false : true,
				(resultSet.getInt(CharacteristicWrapper.COLUMN_VISIBLE) == 0) ? false : true);
		return characteristic;
	}

}
