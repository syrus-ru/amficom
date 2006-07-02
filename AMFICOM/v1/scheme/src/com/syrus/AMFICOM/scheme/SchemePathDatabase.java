/*-
 * $Id: SchemePathDatabase.java,v 1.21 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemePathWrapper.COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID;
import static com.syrus.AMFICOM.scheme.SchemePathWrapper.COLUMN_TRANSMISSION_PATH_ID;

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
 * @version $Revision: 1.21 $, $Date: 2006/07/02 22:36:13 $
 * @module scheme
 */
public final class SchemePathDatabase extends StorableObjectDatabase<SchemePath> {

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_TRANSMISSION_PATH_ID + COMMA
					+ COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEPATH_CODE;
	}

	@Override
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
	 * @param schemePath
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemePath schemePath) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemePath.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemePath.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemePath.getTransmissionPathId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePath.getParentSchemeMonitoringSolutionId());
	}

	/**
	 * @param schemePath
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemePath schemePath,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePath.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePath.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePath.getTransmissionPathId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePath.getParentSchemeMonitoringSolutionId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemePath updateEntityFromResultSet(
			SchemePath storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemePath schemePath = (storableObject == null)
				? new SchemePath(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
				: storableObject;
		schemePath.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TRANSMISSION_PATH_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID));
		return schemePath;
	}
}
