/*-
 * $Id: SchemeCablePortDatabase.java,v 1.12 2005/07/17 05:20:25 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLEPORT_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.12 $, $Date: 2005/07/17 05:20:25 $
 * @module scheme_v1
 */
public final class SchemeCablePortDatabase extends StorableObjectDatabase {
	
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
	@Override
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException {
		SchemeCablePort schemeCablePort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemeCablePort.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
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

	@Override
	protected short getEntityCode() {
		return SCHEMECABLEPORT_CODE;
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
		SchemeCablePort schemeCablePort = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(schemeCablePort.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeCablePort.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + schemeCablePort.getDirectionType().value() + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getPortTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getMeasurementPortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCablePort.getParentSchemeDeviceId());
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
		SchemeCablePort schemeCablePort = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCablePort.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCablePort.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemeCablePort.getDirectionType().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getPortTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getMeasurementPortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCablePort.getParentSchemeDeviceId());
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
				DirectionType.from_int(resultSet.getInt(SchemeCablePortWrapper.COLUMN_DIRECTION_TYPE)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_CABLE_PORT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_CABLE_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_MEASUREMENT_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCablePortWrapper.COLUMN_PARENT_DEVICE_ID));
		return schemeCablePort;
	}
}
