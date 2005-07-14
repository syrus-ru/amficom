/*-
 * $Id: SchemePortDatabase.java,v 1.11 2005/07/14 16:08:08 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPORT_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/07/14 16:08:08 $
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
	@Override
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException {
		SchemePort schemePort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemePort.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
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

	@Override
	protected short getEntityCode() {
		return SCHEMEPORT_CODE;
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
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		SchemePort schemePort = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(schemePort.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + schemePort.getDirectionType().value() + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getPortTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getMeasurementPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePort.getParentSchemeDeviceId());
		return sql;
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			StorableObject storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		SchemePort schemePort = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePort.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePort.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemePort.getDirectionType().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getPortTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getMeasurementPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePort.getParentSchemeDeviceId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
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
				DirectionType.from_int(resultSet.getInt(SchemePortWrapper.COLUMN_DIRECTION_TYPE)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_PORT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_MEASUREMENT_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePortWrapper.COLUMN_PARENT_DEVICE_ID));
		return schemePort;
	}
}
