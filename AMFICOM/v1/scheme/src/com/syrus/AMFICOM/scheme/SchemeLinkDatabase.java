/*-
 * $Id: SchemeLinkDatabase.java,v 1.20 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.20 $, $Date: 2006/07/02 22:36:13 $
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
	 * @param schemeLink
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeLink schemeLink) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeLink.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ schemeLink.getPhysicalLength() + COMMA
				+ schemeLink.getOpticalLength() + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getAbstractLinkTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getAbstractLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getSiteNodeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getSourceAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getTargetAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getParentSchemeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getParentSchemeElementId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeLink.getParentSchemeProtoElementId());
	}

	/**
	 * @param schemeLink
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeLink schemeLink,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeLink.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeLink.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++startParameterNumber, schemeLink.getPhysicalLength());
		preparedStatement.setDouble(++startParameterNumber, schemeLink.getOpticalLength());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getAbstractLinkTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getAbstractLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getSiteNodeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getSourceAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getTargetAbstractSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getParentSchemeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getParentSchemeElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeLink.getParentSchemeProtoElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeLink updateEntityFromResultSet(
			SchemeLink storableObject, ResultSet resultSet)
	throws SQLException {
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
