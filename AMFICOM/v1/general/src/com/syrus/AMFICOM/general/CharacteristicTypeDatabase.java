/*
 * $Id: CharacteristicTypeDatabase.java,v 1.39 2005/12/06 11:31:12 bass Exp $
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
 * @version $Revision: 1.39 $, $Date: 2005/12/06 11:31:12 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */

public final class CharacteristicTypeDatabase extends StorableObjectDatabase<CharacteristicType> {
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
	protected String getUpdateSingleSQLValuesTmpl(final CharacteristicType storableObject) throws IllegalDataException {
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ Integer.toString(storableObject.getDataType().ordinal()) + COMMA
			+ Integer.toString(storableObject.getSort().ordinal());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final CharacteristicType storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getDataType().ordinal());
		preparedStatement.setInt(++startParameterNumber, storableObject.getSort().ordinal());
		return startParameterNumber;
	}

	@Override
	protected CharacteristicType updateEntityFromResultSet(final CharacteristicType storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final CharacteristicType characteristicType = (storableObject == null)
				? new CharacteristicType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						DataType.RAW,
						CharacteristicTypeSort.OPTICAL)
					: storableObject;
		characteristicType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DataType.valueOf(resultSet.getInt(CharacteristicTypeWrapper.COLUMN_DATA_TYPE_CODE)),
				CharacteristicTypeSort.valueOf(resultSet.getInt(CharacteristicTypeWrapper.COLUMN_SORT)));

		return characteristicType;
	}

}
