/*-
 * $Id: SchemeCableLinkDatabase.java,v 1.14 2005/07/24 17:54:41 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2005/07/24 17:54:41 $
 * @module scheme
 */
public final class SchemeCableLinkDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultipleSQLValues;

	private SchemeCableLink fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeCableLink)
			return (SchemeCableLink) storableObject;
		throw new IllegalDataException("SchemeCableLinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
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

	@Override
	protected short getEntityCode() {
		return SCHEMECABLELINK_CODE;
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
		SchemeCableLink schemeCableLink = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(schemeCableLink.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeCableLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ schemeCableLink.getPhysicalLength() + COMMA
				+ schemeCableLink.getOpticalLength() + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getAbstractLinkTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getAbstractLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getSourceAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getTargetAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getParentSchemeId());
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
		SchemeCableLink schemeCableLink = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableLink.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableLink.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, schemeCableLink.getPhysicalLength());
		preparedStatement.setDouble(++startParameterNumber, schemeCableLink.getOpticalLength());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getAbstractLinkTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getAbstractLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getSourceAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getTargetAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableLink.getParentSchemeId());
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
