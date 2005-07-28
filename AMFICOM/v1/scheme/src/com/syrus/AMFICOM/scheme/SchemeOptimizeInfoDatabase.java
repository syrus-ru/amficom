/*-
 * $Id: SchemeOptimizeInfoDatabase.java,v 1.16 2005/07/28 10:04:34 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/07/28 10:04:34 $
 * @module scheme
 */
public final class SchemeOptimizeInfoDatabase extends StorableObjectDatabase<SchemeOptimizeInfo> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_OPTIMIZATION_MODE + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_ITERATIONS + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_PRICE + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_WAVE_LENGTH + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_LEN_MARGIN + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_MUTATION_RATE + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_MUTATION_DEGREE + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_RTU_DELETE_PROB + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_RTU_CREATE_PROB + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_NODES_SPLICE_PROB + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_NODES_CUT_PROB + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_SURVIVOR_RATE + COMMA
					+ SchemeOptimizeInfoWrapper.COLUMN_PARENT_SCHEME_ID;
					
		}
		return columns;
	}

	@Override
	protected short getEntityCode() {
		return SCHEMEOPTIMIZEINFO_CODE;
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
			SchemeOptimizeInfo storableObject)
			throws IllegalDataException {
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ storableObject.getOptimizationMode() + COMMA
				+ storableObject.getIterations() + COMMA
				+ storableObject.getPrice() + COMMA
				+ storableObject.getWaveLength() + COMMA
				+ storableObject.getLenMargin() + COMMA
				+ storableObject.getMutationRate() + COMMA
				+ storableObject.getMutationDegree() + COMMA
				+ storableObject.getRtuDeleteProb() + COMMA
				+ storableObject.getRtuCreateProb() + COMMA
				+ storableObject.getNodesSpliceProb() + COMMA
				+ storableObject.getNodesCutProb() + COMMA
				+ storableObject.getSurvivorRate() + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getParentSchemeId());
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
			SchemeOptimizeInfo storableObject,
			PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException,
			SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getOptimizationMode());
		preparedStatement.setInt(++startParameterNumber, storableObject.getIterations());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getPrice());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getWaveLength());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getLenMargin());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getMutationRate());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getMutationDegree());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getRtuDeleteProb());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getRtuCreateProb());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getNodesSpliceProb());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getNodesCutProb());
		preparedStatement.setDouble(++startParameterNumber, storableObject.getSurvivorRate());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getParentSchemeId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected SchemeOptimizeInfo updateEntityFromResultSet(SchemeOptimizeInfo storableObject, ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		Date created = new Date();
		SchemeOptimizeInfo schemeOptimizeInfo = storableObject == null
				? new SchemeOptimizeInfo(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						created,
						created,
						null,
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						0,
						0,
						0d,
						0d,
						0d,
						0d,
						0d,
						0d,
						0d,
						0d,
						0d,
						0d,
						null)
				: storableObject;
		schemeOptimizeInfo.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getInt(SchemeOptimizeInfoWrapper.COLUMN_OPTIMIZATION_MODE),
				resultSet.getInt(SchemeOptimizeInfoWrapper.COLUMN_ITERATIONS),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_PRICE),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_WAVE_LENGTH),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_LEN_MARGIN),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_MUTATION_RATE),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_MUTATION_DEGREE),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_RTU_DELETE_PROB),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_RTU_CREATE_PROB),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_NODES_SPLICE_PROB),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_NODES_CUT_PROB),
				resultSet.getDouble(SchemeOptimizeInfoWrapper.COLUMN_SURVIVOR_RATE),
				DatabaseIdentifier.getIdentifier(resultSet, SchemeOptimizeInfoWrapper.COLUMN_PARENT_SCHEME_ID));
		return schemeOptimizeInfo;
	}
}
