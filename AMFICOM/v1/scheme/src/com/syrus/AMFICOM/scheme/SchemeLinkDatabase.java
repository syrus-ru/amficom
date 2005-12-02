/*-
 * $Id: SchemeLinkDatabase.java,v 1.19 2005/12/02 11:24:16 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMELINK_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_LINK_ID;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_LINK_TYPE_ID;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_OPTICAL_LENGTH;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ELEMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_PARENT_SCHEME_ID;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_PHYSICAL_LENGTH;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_SITE_NODE_ID;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_SOURCE_SCHEME_PORT_ID;
import static com.syrus.AMFICOM.scheme.SchemeLinkWrapper.COLUMN_TARGET_SCHEME_PORT_ID;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.19 $, $Date: 2005/12/02 11:24:16 $
 * @module scheme
 */
public final class SchemeLinkDatabase extends StorableObjectDatabase<SchemeLink> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_PHYSICAL_LENGTH + COMMA
					+ COLUMN_OPTICAL_LENGTH + COMMA
					+ COLUMN_LINK_TYPE_ID + COMMA
					+ COLUMN_LINK_ID + COMMA
					+ COLUMN_SITE_NODE_ID + COMMA
					+ COLUMN_SOURCE_SCHEME_PORT_ID + COMMA
					+ COLUMN_TARGET_SCHEME_PORT_ID + COMMA
					+ COLUMN_PARENT_SCHEME_ID + COMMA
					+ COLUMN_PARENT_SCHEME_ELEMENT_ID + COMMA
					+ COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID;
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
			SchemeLink storableObject)
			throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getPhysicalLength() + COMMA
				+ storableObject.getOpticalLength() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getAbstractLinkTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getAbstractLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getSourceAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getTargetAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeElementId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeProtoElementId());
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
			SchemeLink storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, storableObject.getPhysicalLength());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getOpticalLength());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAbstractLinkTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAbstractLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSourceAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTargetAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeProtoElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected SchemeLink updateEntityFromResultSet(
			SchemeLink storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final Date created = new Date();
		final SchemeLink schemeLink = (storableObject == null)
				? new SchemeLink(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						0d,
						0d,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		schemeLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				resultSet.getDouble(COLUMN_PHYSICAL_LENGTH),
				resultSet.getDouble(COLUMN_OPTICAL_LENGTH),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_LINK_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SITE_NODE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SOURCE_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TARGET_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_PROTO_ELEMENT_ID));
		return schemeLink;
	}
}
