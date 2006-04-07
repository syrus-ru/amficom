/*-
 * $Id: SchemeOptimizeInfoDatabase.java,v 1.19 2005/12/02 11:24:17 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.19 $, $Date: 2005/12/02 11:24:17 $
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
	 * @param storableObject
	 * @throws IllegalDataException
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			SchemeOptimizeInfo storableObject)
			throws IllegalDataException {
		return APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
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
