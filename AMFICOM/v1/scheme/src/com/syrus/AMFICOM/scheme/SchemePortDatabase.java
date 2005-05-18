/*-
 * $Id: SchemePortDatabase.java,v 1.4 2005/05/18 12:03:15 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

import java.sql.*;
import java.util.Date;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/18 12:03:15 $
 * @module scheme_v1
 */
public final class SchemePortDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemePort fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemePort)
			return (SchemePort) storableObject;
		throw new IllegalDataException("SchemePortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 */
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException {
		SchemePort schemePort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + schemePort.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemePortWrapper.COLUMN_DIRECTION_TYPE + COMMA
					+ SchemePortWrapper.COLUMN_PORT_TYPE_ID + COMMA
					+ SchemePortWrapper.COLUMN_PORT_ID + COMMA
					+ SchemePortWrapper.COLUMN_MEASUREMENT_PORT_ID + COMMA
					+ SchemePortWrapper.COLUMN_PARENT_DEVICE_ID;
		}
		return columns;
	}

	protected String getEnityName() {
		return ObjectEntities.SCHEME_PORT_ENTITY;
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

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		SchemePort schemePort = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemePort.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + schemePort.getDirectionType().value() + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getPortType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getPort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getMeasurementPort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getParentSchemeDevice().getId());
		return sql;
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	protected int setEntityForPreparedStatementTmpl(
			StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		SchemePort schemePort = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePort.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePort.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemePort.getDirectionType().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getPortType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getPort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getMeasurementPort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getParentSchemeDevice().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SchemePort schemePort;
		if (storableObject == null) {
			Date created = new Date();
			schemePort = new SchemePort(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null, null, null, null);
		} else {
			schemePort = fromStorableObject(storableObject);
		}
		schemePort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				AbstractSchemePortDirectionType.from_int(resultSet.getInt(SchemePortWrapper.COLUMN_DIRECTION_TYPE)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_PORT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_MEASUREMENT_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_PARENT_DEVICE_ID));
		return schemePort;
	}
}
