/*-
 * $Id: SchemeOptimizeInfoDatabase.java,v 1.20 2006/07/02 22:36:13 bass Exp $
 *
 * Copyright ¿ 2005-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_NAME;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_ITERATIONS;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_LEN_MARGIN;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_MUTATION_DEGREE;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_MUTATION_RATE;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_NODES_CUT_PROB;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_NODES_SPLICE_PROB;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_OPTIMIZATION_MODE;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_PARENT_SCHEME_ID;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_PRICE;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_RTU_CREATE_PROB;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_RTU_DELETE_PROB;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_SURVIVOR_RATE;
import static com.syrus.AMFICOM.scheme.SchemeOptimizeInfoWrapper.COLUMN_WAVE_LENGTH;

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
public final class SchemeOptimizeInfoDatabase extends StorableObjectDatabase<SchemeOptimizeInfo> {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_NAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_OPTIMIZATION_MODE + COMMA
					+ COLUMN_ITERATIONS + COMMA
					+ COLUMN_PRICE + COMMA
					+ COLUMN_WAVE_LENGTH + COMMA
					+ COLUMN_LEN_MARGIN + COMMA
					+ COLUMN_MUTATION_RATE + COMMA
					+ COLUMN_MUTATION_DEGREE + COMMA
					+ COLUMN_RTU_DELETE_PROB + COMMA
					+ COLUMN_RTU_CREATE_PROB + COMMA
					+ COLUMN_NODES_SPLICE_PROB + COMMA
					+ COLUMN_NODES_CUT_PROB + COMMA
					+ COLUMN_SURVIVOR_RATE + COMMA
					+ COLUMN_PARENT_SCHEME_ID;
					
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
	 * @param schemeOptimizeInfo
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeOptimizeInfo schemeOptimizeInfo) {
		return APOSTROPHE + DatabaseString.toQuerySubString(schemeOptimizeInfo.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(schemeOptimizeInfo.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ schemeOptimizeInfo.getOptimizationMode() + COMMA
				+ schemeOptimizeInfo.getIterations() + COMMA
				+ schemeOptimizeInfo.getPrice() + COMMA
				+ schemeOptimizeInfo.getWaveLength() + COMMA
				+ schemeOptimizeInfo.getLenMargin() + COMMA
				+ schemeOptimizeInfo.getMutationRate() + COMMA
				+ schemeOptimizeInfo.getMutationDegree() + COMMA
				+ schemeOptimizeInfo.getRtuDeleteProb() + COMMA
				+ schemeOptimizeInfo.getRtuCreateProb() + COMMA
				+ schemeOptimizeInfo.getNodesSpliceProb() + COMMA
				+ schemeOptimizeInfo.getNodesCutProb() + COMMA
				+ schemeOptimizeInfo.getSurvivorRate() + COMMA
				+ DatabaseIdentifier.toSQLString(schemeOptimizeInfo.getParentSchemeId());
	}

	/**
	 * @param schemeOptimizeInfo
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws SQLException
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final SchemeOptimizeInfo schemeOptimizeInfo,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws SQLException {
		int startParameterNumber = initialStartParameterNumber;
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeOptimizeInfo.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeOptimizeInfo.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemeOptimizeInfo.getOptimizationMode());
		preparedStatement.setInt(++startParameterNumber, schemeOptimizeInfo.getIterations());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getPrice());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getWaveLength());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getLenMargin());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getMutationRate());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getMutationDegree());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getRtuDeleteProb());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getRtuCreateProb());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getNodesSpliceProb());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getNodesCutProb());
		preparedStatement.setDouble(++startParameterNumber, schemeOptimizeInfo.getSurvivorRate());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeOptimizeInfo.getParentSchemeId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws SQLException
	 */
	@Override
	protected SchemeOptimizeInfo updateEntityFromResultSet(SchemeOptimizeInfo storableObject, ResultSet resultSet)
	throws SQLException {
		final Date created = new Date();
		final SchemeOptimizeInfo schemeOptimizeInfo = (storableObject == null)
				? new SchemeOptimizeInfo(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						created,
						created,
						null,
						null,
						ILLEGAL_VERSION,
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
		schemeOptimizeInfo.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
				resultSet.getInt(COLUMN_OPTIMIZATION_MODE),
				resultSet.getInt(COLUMN_ITERATIONS),
				resultSet.getDouble(COLUMN_PRICE),
				resultSet.getDouble(COLUMN_WAVE_LENGTH),
				resultSet.getDouble(COLUMN_LEN_MARGIN),
				resultSet.getDouble(COLUMN_MUTATION_RATE),
				resultSet.getDouble(COLUMN_MUTATION_DEGREE),
				resultSet.getDouble(COLUMN_RTU_DELETE_PROB),
				resultSet.getDouble(COLUMN_RTU_CREATE_PROB),
				resultSet.getDouble(COLUMN_NODES_SPLICE_PROB),
				resultSet.getDouble(COLUMN_NODES_CUT_PROB),
				resultSet.getDouble(COLUMN_SURVIVOR_RATE),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARENT_SCHEME_ID));
		return schemeOptimizeInfo;
	}
}
