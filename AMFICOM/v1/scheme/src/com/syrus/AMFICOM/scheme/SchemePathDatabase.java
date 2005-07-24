/*-
 * $Id: SchemePathDatabase.java,v 1.13 2005/07/24 17:39:16 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;

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
 * @author $Author: arseniy $
 * @version $Revision: 1.13 $, $Date: 2005/07/24 17:39:16 $
 * @module scheme_v1
 */
public final class SchemePathDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemePath fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemePath)
			return (SchemePath) storableObject;
		throw new IllegalDataException("SchemePathDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemePathWrapper.COLUMN_TRANSMISSION_PATH_ID + COMMA
					+ SchemePathWrapper.COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID + COMMA
					+ SchemePathWrapper.COLUMN_PARENT_SCHEME_ID;
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
		SchemePath schemePath = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(schemePath.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemePath.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemePath.getTransmissionPathId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePath.getParentSchemeMonitoringSolutionId()) + COMMA
				+ DatabaseIdentifier.toSQLString(schemePath.getParentSchemeId());
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
		SchemePath schemePath = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePath.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemePath.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePath.getTransmissionPathId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePath.getParentSchemeMonitoringSolutionId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemePath.getParentSchemeId());
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
		SchemePath schemePath;
		if (storableObject == null) {
			Date created = new Date();
			schemePath = new SchemePath(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null,
					null, null, null, null);
		} else {
			schemePath = fromStorableObject(storableObject);
		}
		schemePath.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePathWrapper.COLUMN_TRANSMISSION_PATH_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePathWrapper.COLUMN_PARENT_SCHEME_MONITORING_SOLUTION_ID),
				DatabaseIdentifier.getIdentifier(resultSet, SchemePathWrapper.COLUMN_PARENT_SCHEME_ID));
		return schemePath;
	}
}
