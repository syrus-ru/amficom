/*-
 * $Id: SchemeCablePortDatabase.java,v 1.4 2005/05/18 12:03:15 bass Exp $
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
public final class SchemeCablePortDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeCablePort fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeCablePort)
			return (SchemeCablePort) storableObject;
		throw new IllegalDataException("SchemeCablePortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#retrieveObject(com.syrus.AMFICOM.general.StorableObject, int, java.lang.Object)
	 */
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException {
		SchemeCablePort schemeCablePort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + schemeCablePort.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeCablePortWrapper.COLUMN_DIRECTION_TYPE + COMMA
					+ SchemeCablePortWrapper.COLUMN_CABLE_PORT_TYPE_ID + COMMA
					+ SchemeCablePortWrapper.COLUMN_CABLE_PORT_ID + COMMA
					+ SchemeCablePortWrapper.COLUMN_MEASUREMENT_PORT_ID + COMMA
					+ SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID;
		}
		return columns;
	}

	protected String getEnityName() {
		return ObjectEntities.SCHEME_CABLE_PORT_ENTITY;
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
		SchemeCablePort schemeCablePort = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeCablePort.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeCablePort.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeCablePort.getDirectionType().value() + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getPortType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getPort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getMeasurementPort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getParentSchemeDevice().getId());
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
		SchemeCablePort schemeCablePort = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCablePort.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCablePort.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemeCablePort.getDirectionType().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getPortType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getPort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getMeasurementPort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getParentSchemeDevice().getId());
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
		SchemeCablePort schemeCablePort;
		if (storableObject == null) {
			Date created = new Date();
			schemeCablePort = new SchemeCablePort(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null, null, null, null);
		} else {
			schemeCablePort = fromStorableObject(storableObject);
		}
		schemeCablePort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				AbstractSchemePortDirectionType.from_int(resultSet.getInt(SchemeCablePortWrapper.COLUMN_DIRECTION_TYPE)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_CABLE_PORT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_CABLE_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_MEASUREMENT_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID));
		return schemeCablePort;
	}
}
