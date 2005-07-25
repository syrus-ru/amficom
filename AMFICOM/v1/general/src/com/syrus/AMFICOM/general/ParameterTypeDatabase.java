/*
 * $Id: ParameterTypeDatabase.java,v 1.32 2005/07/25 20:47:00 arseniy Exp $
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
 * @version $Revision: 1.32 $, $Date: 2005/07/25 20:47:00 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class ParameterTypeDatabase extends StorableObjectDatabase  {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.PARAMETER_TYPE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null){
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ ParameterTypeWrapper.COLUMN_DATA_TYPE_CODE;
		}
		return columns;
	}	
	
	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final ParameterType parameterType = this.fromStorableObject(storableObject);
		return APOSTROPHE + DatabaseString.toQuerySubString(parameterType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(parameterType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(parameterType.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ Integer.toString(parameterType.getDataType().getCode());
	}	

	private ParameterType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ParameterType)
			return (ParameterType)storableObject;
		throw new IllegalDataException("ParameterTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final ParameterType parameterType = (storableObject == null)
				? new ParameterType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						DataType.RAW)
				: this.fromStorableObject(storableObject);
		parameterType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DataType.fromInt(resultSet.getInt(ParameterTypeWrapper.COLUMN_DATA_TYPE_CODE)));
		return parameterType;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final ParameterType parameterType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, parameterType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, parameterType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, parameterType.getName(), SIZE_NAME_COLUMN);
		preparedStatement.setInt(++startParameterNumber, parameterType.getDataType().getCode());
		return startParameterNumber;
	}

}
