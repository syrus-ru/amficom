/*-
 * $Id: SchemeDeviceDatabase.java,v 1.2 2005/04/22 16:21:44 max Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

import java.sql.*;
import java.util.Date;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.2 $, $Date: 2005/04/22 16:21:44 $
 * @module scheme_v1
 */
public final class SchemeDeviceDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeDevice fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeDevice)
			return (SchemeDevice) storableObject;
		throw new IllegalDataException("SchemeDeviceDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		SchemeDevice schemeDevice = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + schemeDevice.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID + COMMA
					+ SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
		}
		return columns;
	}

	protected String getEnityName() {
		return ObjectEntities.SCHEME_DEVICE_ENTITY;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
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
		SchemeDevice schemeDevice = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeDevice.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeDevice.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeDevice.getParentSchemeProtoElement().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeDevice.getParentSchemeElement().getId());
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
		SchemeDevice schemeDevice = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeDevice.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeDevice.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeDevice.getParentSchemeProtoElement().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeDevice.getParentSchemeElement().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 */
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException,
			SQLException {
		SchemeDevice schemeDevice;
		if (storableObject == null) {
			Date created = new Date(); 
			schemeDevice = new SchemeDevice(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, null, null);
		} else {
			schemeDevice = fromStorableObject(storableObject);
		}
		schemeDevice.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeDeviceWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID));
		return schemeDevice;
	}
}
