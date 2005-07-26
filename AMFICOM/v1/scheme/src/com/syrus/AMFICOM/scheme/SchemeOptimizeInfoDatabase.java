/*-
 * $Id: SchemeOptimizeInfoDatabase.java,v 1.15 2005/07/26 12:52:23 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: arseniy $
 * @version $Revision: 1.15 $, $Date: 2005/07/26 12:52:23 $
 * @module scheme
 */
public final class SchemeOptimizeInfoDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeOptimizeInfo fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeOptimizeInfo)
			return (SchemeOptimizeInfo) storableObject;
		throw new IllegalDataException("SchemeOptimizeInfoDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

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
			StorableObject storableObject)
			throws IllegalDataException {
		SchemeOptimizeInfo schemeOptimizeInfo = fromStorableObject(storableObject);
		String sql = APOSTROPHE + DatabaseString.toQuerySubString(schemeOptimizeInfo.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
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
		SchemeOptimizeInfo schemeOptimizeInfo = fromStorableObject(storableObject);
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
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	@Override
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		SchemeOptimizeInfo schemeOptimizeInfo;
		if (storableObject == null) {
			Date created = new Date();
			schemeOptimizeInfo = new SchemeOptimizeInfo(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
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
					null);
		} else {
			schemeOptimizeInfo = fromStorableObject(storableObject);
		}
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
