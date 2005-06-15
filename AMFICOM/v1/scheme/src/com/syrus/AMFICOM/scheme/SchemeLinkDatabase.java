/*-
 * $Id: SchemeLinkDatabase.java,v 1.5 2005/06/15 13:17:17 bass Exp $
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
 * @version $Revision: 1.5 $, $Date: 2005/06/15 13:17:17 $
 * @module scheme_v1
 */
public final class SchemeLinkDatabase extends CharacterizableDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeLink fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeLink)
			return (SchemeLink) storableObject;
		throw new IllegalDataException("SchemeLinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
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
		SchemeLink schemeLink = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemeLink.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeLinkWrapper.COLUMN_PHYSICAL_LENGTH + COMMA
					+ SchemeLinkWrapper.COLUMN_OPTICAL_LENGTH + COMMA
					+ SchemeLinkWrapper.COLUMN_LINK_TYPE_ID + COMMA
					+ SchemeLinkWrapper.COLUMN_LINK_ID + COMMA
					+ SchemeLinkWrapper.COLUMN_SITE_NODE_ID + COMMA
					+ SchemeLinkWrapper.COLUMN_SOURCE_SCHEME_PORT_ID + COMMA
					+ SchemeLinkWrapper.COLUMN_TARGET_SCHEME_PORT_ID + COMMA
					+ SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ID + COMMA
					+ SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID + COMMA
					+ SchemeLinkWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;
		}
		return columns;
	}

	protected short getEntityCode() {
		return ObjectEntities.SCHEME_LINK_ENTITY_CODE;
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
		SchemeLink schemeLink = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeLink.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeLink.getPhysicalLength() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeLink.getOpticalLength() + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getLinkType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getLink().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getSiteNode().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getSourceSchemePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getTargetSchemePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getParentScheme().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getParentSchemeElement().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getParentSchemeProtoElement().getId());
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
		SchemeLink schemeLink = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeLink.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeLink.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, schemeLink.getPhysicalLength());
		preparedStatement.setDouble(++startParameterNumber, schemeLink.getOpticalLength());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getLinkType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getSiteNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getSourceSchemePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getTargetSchemePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getParentScheme().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getParentSchemeElement().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getParentSchemeProtoElement().getId());
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
		SchemeLink schemeLink;
		if (storableObject == null) {
			Date created = new Date();
			schemeLink = new SchemeLink(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, 0d, 0d, null, null, null, null, null, null, null, null);
		} else {
			schemeLink = fromStorableObject(storableObject);
		}
		schemeLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getDouble(SchemeLinkWrapper.COLUMN_PHYSICAL_LENGTH),
				resultSet.getDouble(SchemeLinkWrapper.COLUMN_OPTICAL_LENGTH),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_LINK_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_SOURCE_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_TARGET_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeLinkWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID));
		return schemeLink;
	}
}
