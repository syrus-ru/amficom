/*-
 * $Id: SchemeMonitoringSolutionDatabase.java,v 1.18 2005/12/02 11:24:17 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE;
import static com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionWrapper.COLUMN_PARENT_SCHEME_ID;
import static com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionWrapper.COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID;
import static com.syrus.AMFICOM.scheme.SchemeMonitoringSolutionWrapper.COLUMN_PRICE_USD;

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
 * @version $Revision: 1.18 $, $Date: 2005/12/02 11:24:17 $
 * @module scheme
 */
public final class SchemeMonitoringSolutionDatabase extends StorableObjectDatabase<SchemeMonitoringSolution> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_PRICE_USD + COMMA
					+ COLUMN_ACTIVE + COMMA
					+ COLUMN_PARENT_SCHEME_ID + COMMA
					+ COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID;
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEMONITORINGSOLUTION_CODE;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
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
			SchemeMonitoringSolution storableObject)
			throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getPrice() + COMMA
				+ (storableObject.isActive() ? 1 : 0) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeOptimizeInfoId());
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
			SchemeMonitoringSolution storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getPrice());
		preparedStatement.setInt(++startParameterNumber, storableObject.isActive() ? 1 : 0);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeOptimizeInfoId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected SchemeMonitoringSolution updateEntityFromResultSet(SchemeMonitoringSolution storableObject, ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final Date created = new Date();
		final SchemeMonitoringSolution schemeMonitoringSolution = (storableObject == null)
				? new SchemeMonitoringSolution(DatabaseIdentifier.getIdentifier(resultSet,
						COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
						null,
						null,
						0,
						false,
						null,
						null)
				: storableObject;
		schemeMonitoringSolution.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				resultSet.getInt(COLUMN_PRICE_USD),
				resultSet.getInt(COLUMN_ACTIVE) != 0,
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_OPTIMIZE_INFO_ID));
		return schemeMonitoringSolution;
	}
}
