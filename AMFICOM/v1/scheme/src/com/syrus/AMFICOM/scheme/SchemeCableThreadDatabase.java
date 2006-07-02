/*-
 * $Id: SchemeCableThreadDatabase.java,v 1.20 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMECABLETHREAD_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper.COLUMN_LINK_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper.COLUMN_LINK_TYPE_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper.COLUMN_PARENT_SCHEME_CABLE_LINK_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper.COLUMN_SOURCE_SCHEME_PORT_ID;
import static com.syrus.AMFICOM.scheme.SchemeCableThreadWrapper.COLUMN_TARGET_SCHEME_PORT_ID;

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
public final class SchemeCableThreadDatabase extends StorableObjectDatabase<SchemeCableThread> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_LINK_TYPE_ID + COMMA
					+ COLUMN_LINK_ID + COMMA
					+ COLUMN_PARENT_SCHEME_CABLE_LINK_ID + COMMA
					+ COLUMN_SOURCE_SCHEME_PORT_ID + COMMA
					+ COLUMN_TARGET_SCHEME_PORT_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMECABLETHREAD_CODE;
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
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	/**
	 * @param schemeCableThread
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeCableThread schemeCableThread) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeCableThread.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeCableThread.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getLinkTypeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getParentSchemeCableLinkId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getSourceSchemePortId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeCableThread.getTargetSchemePortId());
	}

	/**
	 * @param schemeCableThread
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeCableThread schemeCableThread,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableThread.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeCableThread.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getLinkTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getParentSchemeCableLinkId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getSourceSchemePortId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeCableThread.getTargetSchemePortId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeCableThread updateEntityFromResultSet(
			SchemeCableThread storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemeCableThread schemeCableThread = (storableObject == null)
				? new SchemeCableThread(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						null,
						null)
				: storableObject;
		schemeCableThread.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_LINK_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_CABLE_LINK_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SOURCE_SCHEME_PORT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TARGET_SCHEME_PORT_ID));
		return schemeCableThread;
	}
}
