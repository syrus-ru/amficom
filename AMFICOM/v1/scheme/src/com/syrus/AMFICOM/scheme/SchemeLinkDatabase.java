/*-
 * $Id: SchemeLinkDatabase.java,v 1.8 2005/07/12 08:40:55 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/07/12 08:40:55 $
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
	@Override
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

	@Override
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

	@Override
	protected short getEntityCode() {
		return SCHEMELINK_CODE;
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
		SchemeLink schemeLink = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeLink.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeLink.getPhysicalLength() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeLink.getOpticalLength() + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getAbstractLinkType().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getAbstractLink().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getSiteNode().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getSourceAbstractSchemePort().getId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getTargetAbstractSchemePort().getId()) + COMMA
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
	@Override
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
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getAbstractLinkType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getAbstractLink().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getSiteNode().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getSourceAbstractSchemePort().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getTargetAbstractSchemePort().getId());
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
	@Override
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
