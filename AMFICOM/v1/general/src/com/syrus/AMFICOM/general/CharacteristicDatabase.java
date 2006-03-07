/*
 * $Id: CharacteristicDatabase.java,v 1.49.2.1 2006/03/07 10:43:27 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.49.2.1 $, $Date: 2006/03/07 10:43:27 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public final class CharacteristicDatabase extends StorableObjectDatabase<Characteristic> {
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
	protected String getUpdateSingleSQLValuesTmpl(final Characteristic storableObject) throws IllegalDataException {
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE
				+ COMMA + APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getValue(), SIZE_VALUE_COLUMN) + APOSTROPHE + COMMA
				+ (storableObject.isEditable() ? "1" : "0") + COMMA
				+ (storableObject.isVisible() ? "1" : "0") + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentCharacterizableId());
		/**
		 * check sort support
		 */
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Characteristic storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getValue(), SIZE_VALUE_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.isEditable() ? 1 : 0);
		preparedStatement.setInt(++startParameterNumber, storableObject.isVisible() ? 1 : 0);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentCharacterizableId());
		return startParameterNumber;
	}	

	@Override
	protected Characteristic updateEntityFromResultSet(final Characteristic storableObject, final ResultSet resultSet)
			throws RetrieveObjectException, SQLException, IllegalDataException {
		final Characteristic characteristic = (storableObject == null)
				? new Characteristic(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						false,
						false)
					: storableObject;

		final Identifier characterizableId = DatabaseIdentifier.getIdentifier(resultSet, CharacteristicWrapper.COLUMN_CHARACTERIZABLE_ID);

		CharacteristicType characteristicType;
		try {
			characteristicType = StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
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
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
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
