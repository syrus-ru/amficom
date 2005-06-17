/*-
 * $Id: SchemeCableLinkDatabase.java,v 1.6 2005/06/17 11:01:18 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/06/17 11:01:18 $
 * @module scheme_v1
 */
public final class SchemeCableLinkDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeCableLink fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeCableLink)
			return (SchemeCableLink) storableObject;
		throw new IllegalDataException("SchemeCableLinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
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
		SchemeCableLink schemeCableLink = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemeCableLink.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeCableLinkWrapper.COLUMN_PHYSICAL_LENGTH + COMMA
					+ SchemeCableLinkWrapper.COLUMN_OPTICAL_LENGTH + COMMA
					+ SchemeCableLinkWrapper.COLUMN_CABLE_LINK_TYPE_ID + COMMA
					+ SchemeCableLinkWrapper.COLUMN_CABLE_LINK_ID + COMMA
					+ SchemeCableLinkWrapper.COLUMN_SOURCE_SCHEME_CABLE_PORT_ID + COMMA
					+ SchemeCableLinkWrapper.COLUMN_TARGET_SCHEME_CABLE_PORT_ID + COMMA
					+ SchemeCableLinkWrapper.COLUMN_PARENT_SCHEME_ID;
		}
		return columns;
	}

	protected short getEntityCode() {
		return ObjectEntities.SCHEMECABLELINK_CODE;
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
		SchemeCableLink schemeCableLink = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeCableLink.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeCableLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeCableLink.getPhysicalLength() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeCableLink.getOpticalLength() + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getCableLinkType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getLink().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getSourceSchemeCablePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getTargetSchemeCablePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getParentScheme().getId());
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
		SchemeCableLink schemeCableLink = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableLink.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableLink.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, schemeCableLink.getPhysicalLength());
		preparedStatement.setDouble(++startParameterNumber, schemeCableLink.getOpticalLength());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getCableLinkType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getSourceSchemeCablePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getTargetSchemeCablePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getParentScheme().getId());
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
		SchemeCableLink schemeCableLink;
		if (storableObject == null) {
			Date created = new Date();
			schemeCableLink = new SchemeCableLink(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, 0d, 0d, null, null, null, null, null);
		} else {
			schemeCableLink = fromStorableObject(storableObject);
		}
		schemeCableLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getDouble(SchemeCableLinkWrapper.COLUMN_PHYSICAL_LENGTH),
				resultSet.getDouble(SchemeCableLinkWrapper.COLUMN_OPTICAL_LENGTH),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableLinkWrapper.COLUMN_CABLE_LINK_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableLinkWrapper.COLUMN_CABLE_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableLinkWrapper.COLUMN_SOURCE_SCHEME_CABLE_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableLinkWrapper.COLUMN_TARGET_SCHEME_CABLE_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeCableLinkWrapper.COLUMN_PARENT_SCHEME_ID));
		return schemeCableLink;
	}
}
