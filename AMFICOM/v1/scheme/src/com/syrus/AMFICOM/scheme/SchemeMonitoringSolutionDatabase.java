/*-
 * $Id: SchemeMonitoringSolutionDatabase.java,v 1.7 2005/06/17 11:01:17 bass Exp $
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
 * @version $Revision: 1.7 $, $Date: 2005/06/17 11:01:17 $
 * @module scheme_v1
 */
public final class SchemeMonitoringSolutionDatabase extends StorableObjectDatabase {
	
	private static String columns;
	private static String updateMultipleSQLValues;
	
	private SchemeMonitoringSolution fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if(storableObject instanceof SchemeMonitoringSolution)
			return (SchemeMonitoringSolution) storableObject;
		throw new IllegalDataException("SchemeMonitoringSolutionDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	/**
	 * @param storableObjects
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 */
	public void insert(Set storableObjects)
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
		SchemeMonitoringSolution schemeMonitoringSolution = fromStorableObject(storableObject);
		super.insertEntity(schemeMonitoringSolution);
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
		SchemeMonitoringSolution schemeMonitoringSolution = fromStorableObject(storableObject);
		super.retrieveEntity(schemeMonitoringSolution);
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
		SchemeMonitoringSolution schemeMonitoringSolution = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName()
						+ " '" + schemeMonitoringSolution.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
					+ SchemeMonitoringSolutionWrapper.COLUMN_PRICE_USD + COMMA
					+ SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE + COMMA
					+ SchemeMonitoringSolutionWrapper.COLUMN_SCHEME_OPTIMIZE_INFO_ID;
		}
		return columns;
	}

	protected short getEntityCode() {
		return ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
	}

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
	protected String getUpdateSingleSQLValuesTmpl(
			StorableObject storableObject)
			throws IllegalDataException {
		SchemeMonitoringSolution schemeMonitoringSolution = fromStorableObject(storableObject);
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(schemeMonitoringSolution.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
				+ APOSTOPHE + DatabaseString.toQuerySubString(schemeMonitoringSolution.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
				+ schemeMonitoringSolution.getPrice() + COMMA
				+ (schemeMonitoringSolution.isActive() ? 1 : 0) + COMMA
				+ DatabaseIdentifier.toSQLString(schemeMonitoringSolution.getParentSchemeOptimizeInfo().getId());
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
		SchemeMonitoringSolution schemeMonitoringSolution = fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeMonitoringSolution.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, schemeMonitoringSolution.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, schemeMonitoringSolution.getPrice());
		preparedStatement.setInt(++startParameterNumber, schemeMonitoringSolution.isActive() ? 1 : 0);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, schemeMonitoringSolution.getParentSchemeOptimizeInfo().getId());
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
		SchemeMonitoringSolution schemeMonitoringSolution;
		if (storableObject == null) {
			Date created = new Date();
			schemeMonitoringSolution = new SchemeMonitoringSolution(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					created, created, null, null, 0L, null, null, 0, false, null);
		} else {
			schemeMonitoringSolution = fromStorableObject(storableObject);
		}
		schemeMonitoringSolution.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				resultSet.getInt(SchemeMonitoringSolutionWrapper.COLUMN_PRICE_USD),
				resultSet.getInt(SchemeMonitoringSolutionWrapper.COLUMN_ACTIVE) != 0,
				DatabaseIdentifier.getIdentifier(resultSet, SchemeMonitoringSolutionWrapper.COLUMN_SCHEME_OPTIMIZE_INFO_ID));
		return schemeMonitoringSolution;
	}
}
