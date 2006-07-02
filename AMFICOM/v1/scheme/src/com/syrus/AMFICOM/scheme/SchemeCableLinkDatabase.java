/*-
 * $Id: SchemeCableLinkDatabase.java,v 1.20 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLELINK_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper.COLUMN_CABLE_LINK_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper.COLUMN_CABLE_LINK_TYPE_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper.COLUMN_OPTICAL_LENGTH;
import static com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper.COLUMN_PARENT_SCHEME_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper.COLUMN_PHYSICAL_LENGTH;
import static com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper.COLUMN_SOURCE_SCHEME_CABLE_PORT_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableLinkWrapper.COLUMN_TARGET_SCHEME_CABLE_PORT_ID;

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
public final class SchemeCableLinkDatabase extends StorableObjectDatabase<SchemeCableLink> {

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_PHYSICAL_LENGTH + COMMA
					+ COLUMN_OPTICAL_LENGTH + COMMA
					+ COLUMN_CABLE_LINK_TYPE_ID + COMMA
					+ COLUMN_CABLE_LINK_ID + COMMA
					+ COLUMN_SOURCE_SCHEME_CABLE_PORT_ID + COMMA
					+ COLUMN_TARGET_SCHEME_CABLE_PORT_ID + COMMA
					+ COLUMN_PARENT_SCHEME_ID;
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
	 * @param schemeCableLink
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeCableLink schemeCableLink) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeCableLink.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeCableLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ schemeCableLink.getPhysicalLength() + COMMA
				+ schemeCableLink.getOpticalLength() + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getAbstractLinkTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getAbstractLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getSourceAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getTargetAbstractSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableLink.getParentSchemeId());
	}

	/**
	 * @param schemeCableLink
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeCableLink schemeCableLink,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
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
	 * @throws SQLException
	 */
	@Override
	protected SchemeCableLink updateEntityFromResultSet(
			SchemeCableLink storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemeCableLink schemeCableLink = (storableObject == null)
				? new SchemeCableLink(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
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
						null)
				: storableObject;
		schemeCableLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				resultSet.getDouble(COLUMN_PHYSICAL_LENGTH),
				resultSet.getDouble(COLUMN_OPTICAL_LENGTH),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CABLE_LINK_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CABLE_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SOURCE_SCHEME_CABLE_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TARGET_SCHEME_CABLE_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ID));
		return schemeCableLink;
	}
}
