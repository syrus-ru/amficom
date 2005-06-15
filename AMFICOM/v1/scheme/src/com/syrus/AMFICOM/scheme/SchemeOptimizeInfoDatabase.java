/*-
 * $Id: SchemeOptimizeInfoDatabase.java,v 1.5 2005/06/15 13:17:17 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

import java.sql.*;
import java.util.Date;
import java.util.Set;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/15 13:17:17 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfoDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeOptimizeInfo fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeOptimizeInfo)
			return (SchemeOptimizeInfo) storableObject;
		throw new IllegalDataException("SchemeOptimizeInfoDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObjects
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	public void insert(final Set storableObjects)
			throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	public void insert(StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		SchemeOptimizeInfo schemeOptimizeInfo = fromStorableObject(storableObject);
		super.insertEntity(schemeOptimizeInfo);
	}

	/**
	 * @param storableObject
	 * @throws IllegalDataException
	 * @throws ObjectNotFoundException
	 * @throws RetrieveObjectException
	 */
	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		SchemeOptimizeInfo schemeOptimizeInfo = fromStorableObject(storableObject);
		super.retrieveEntity(schemeOptimizeInfo);
	}

	/**
	 * @param storableObject
	 * @param retrieveKind
	 * @param arg
	 * @throws IllegalDataException
	 */
	public Object retrieveObject(StorableObject storableObject,
			int retrieveKind, Object arg)
			throws IllegalDataException {
		SchemeOptimizeInfo schemeOptimizeInfo = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemeOptimizeInfo.getId() + "'; argument: " + arg);
				return null;
		}
	}

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
					+ SchemeOptimizeInfoWrapper.COLUMN_SCHEME_ID;
					
		}
		return columns;
	}

	protected short getEntityCode() {
		return ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE;
	}

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
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		SchemeOptimizeInfo schemeOptimizeInfo = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeOptimizeInfo.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeOptimizeInfo.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getOptimizationMode() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getIterations() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getPrice() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getWaveLength() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getLenMargin() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getMutationRate() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getMutationDegree() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getRtuDeleteProb() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getRtuCreateProb() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getNodesSpliceProb() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getNodesCutProb() + APOSTOPHE + COMMA
				+ APOSTOPHE + schemeOptimizeInfo.getSurvivorRate() + APOSTOPHE + COMMA
				+ DatabaseIdentifier.toSQLString(schemeOptimizeInfo.getParentScheme().getId());
		return sql;
	}

	/**
	 * @param storableObject
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
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
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeOptimizeInfo.getParentScheme().getId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws SQLException
	 */
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SchemeOptimizeInfo schemeOptimizeInfo;
		if (storableObject == null) {
			Date created = new Date();
			schemeOptimizeInfo = new SchemeOptimizeInfo(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, 0, 0, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, null);
		} else {
			schemeOptimizeInfo = fromStorableObject(storableObject);
		}
		schemeOptimizeInfo.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
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
				DatabaseIdentifier.getIdentifier(resultSet, SchemeOptimizeInfoWrapper.COLUMN_SCHEME_ID));
		return schemeOptimizeInfo;
	}
}
