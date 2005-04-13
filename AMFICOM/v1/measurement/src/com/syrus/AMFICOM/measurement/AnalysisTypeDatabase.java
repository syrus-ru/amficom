/*
 * $Id: AnalysisTypeDatabase.java,v 1.82 2005/04/13 10:03:39 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.82 $, $Date: 2005/04/13 10:03:39 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class AnalysisTypeDatabase extends StorableObjectDatabase {	

	private static String columns;
	private static String updateMultipleSQLValues;

	private AnalysisType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		throw new IllegalDataException("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.ANALYSISTYPE_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA 
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA 
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);		
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException{
		AnalysisType analysisType = storableObject == null ? 
				new AnalysisType(DatabaseIdentifier.getIdentifier(
			resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null, null) : 
					this.fromStorableObject(storableObject);
		analysisType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
								   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
								   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
								   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
								   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
								   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
								   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return analysisType;
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		super.retrieveEntity(analysisType);
		this.retrieveParameterTypesByOneQuery(Collections.singleton(analysisType));
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(analysisType));
	}

	private void retrieveParameterTypesByOneQuery(java.util.Set analysisTypes) throws RetrieveObjectException {
		if ((analysisTypes == null) || (analysisTypes.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE + COMMA
				+ AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID
				+ SQL_FROM + ObjectEntities.ANATYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
    sql.append(idsEnumerationString(analysisTypes, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map inParameterTypeIdsMap = new HashMap();
			Map criteriaParameterTypeIdsMap = new HashMap();
			Map etalonParameterTypeIdsMap = new HashMap();
			Map outParameterTypeIdsMap = new HashMap();
			String parameterMode;
			Identifier parameterTypeId;
			Identifier analysisTypeId;
			java.util.Set inParameterTypeIds;
			java.util.Set criteriaParameterTypeIds;
			java.util.Set etalonParameterTypeIds;
			java.util.Set outParameterTypeIds;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				analysisTypeId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID);

				if (parameterMode.equals(AnalysisTypeWrapper.MODE_IN)) {
					inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(analysisTypeId);
					if (inParameterTypeIds == null) {
						inParameterTypeIds = new HashSet();
						inParameterTypeIdsMap.put(analysisTypeId, inParameterTypeIds);
					}
					inParameterTypeIds.add(parameterTypeId);
				}
				else
					if (parameterMode.equals(AnalysisTypeWrapper.MODE_CRITERION)) {
						criteriaParameterTypeIds = (java.util.Set) criteriaParameterTypeIdsMap.get(analysisTypeId);
						if (criteriaParameterTypeIds == null) {
							criteriaParameterTypeIds = new HashSet();
							criteriaParameterTypeIdsMap.put(analysisTypeId, criteriaParameterTypeIds);
						}
						criteriaParameterTypeIds.add(parameterTypeId);
					}
					else
						if (parameterMode.equals(AnalysisTypeWrapper.MODE_ETALON)) {
							etalonParameterTypeIds = (java.util.Set) etalonParameterTypeIdsMap.get(analysisTypeId);
							if (etalonParameterTypeIds == null) {
								etalonParameterTypeIds = new HashSet();
								etalonParameterTypeIdsMap.put(analysisTypeId, etalonParameterTypeIds);
							}
							etalonParameterTypeIds.add(parameterTypeId);
						}
						else
							if (parameterMode.equals(AnalysisTypeWrapper.MODE_OUT)) {
								outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(analysisTypeId);
								if (outParameterTypeIds == null) {
									outParameterTypeIds = new HashSet();
									outParameterTypeIdsMap.put(analysisTypeId, outParameterTypeIds);
								}
								outParameterTypeIds.add(parameterTypeId);
							}
							else
								Log.errorMessage("AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | ERROR: Unknown parameter mode '"
										+ parameterMode + "' for parameterTypeId '" + parameterTypeId + "' of analysis type '" + analysisTypeId + "'");
			}

			AnalysisType analysisType;
			for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
				analysisType = (AnalysisType) it.next();
				analysisTypeId = analysisType.getId();
				inParameterTypeIds = (java.util.Set) inParameterTypeIdsMap.get(analysisTypeId);
				criteriaParameterTypeIds = (java.util.Set) criteriaParameterTypeIdsMap.get(analysisTypeId);
				etalonParameterTypeIds = (java.util.Set) etalonParameterTypeIdsMap.get(analysisTypeId);
				outParameterTypeIds = (java.util.Set) outParameterTypeIdsMap.get(analysisTypeId);

				analysisType.setParameterTypes(GeneralStorableObjectPool.getStorableObjects(inParameterTypeIds, true),
						GeneralStorableObjectPool.getStorableObjects(criteriaParameterTypeIds, true),
						GeneralStorableObjectPool.getStorableObjects(etalonParameterTypeIds, true),
						GeneralStorableObjectPool.getStorableObjects(outParameterTypeIds, true));
			}

		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Cannot retrieve parameter type ids for analysis types -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void retrieveMeasurementTypeIdsByOneQuery(java.util.Set analysisTypes) throws RetrieveObjectException {
		if ((analysisTypes == null) || (analysisTypes.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID + COMMA
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID
				+ SQL_FROM + ObjectEntities.MNTTYPANATYPEVATYP_ENTITY
				+ SQL_WHERE);
		sql.append(idsEnumerationString(analysisTypes, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveMeasurementTypeIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map measurementTypeIdsMap = new HashMap();
			Identifier analysisTypeId;
			Identifier measurementTypeId;
			java.util.Set measurementTypeIds;
			while (resultSet.next()) {
				analysisTypeId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID);
				measurementTypeId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

				measurementTypeIds = (java.util.Set) measurementTypeIdsMap.get(analysisTypeId);
				if (measurementTypeIds == null) {
					measurementTypeIds = new HashSet();
					measurementTypeIdsMap.put(analysisTypeId, measurementTypeIds);
				}
				measurementTypeIds.add(measurementTypeId);
			}

			AnalysisType analysisType;
			for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
				analysisType = (AnalysisType) it.next();
				analysisTypeId = analysisType.getId();
				measurementTypeIds = (java.util.Set) measurementTypeIdsMap.get(analysisTypeId);

				analysisType.setMeasurementTypeIds0(measurementTypeIds);
			}
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveMeasurementTypeIdsByOneQuery | Cannot retrieve parameter type ids for analysis types -- "
					+ sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName()
						+ " '" + analysisType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		this.insertEntity(analysisType);
		this.insertParameterTypes(analysisType);
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for(Iterator it = storableObjects.iterator(); it.hasNext();) {
			AnalysisType analysisType = this.fromStorableObject((StorableObject)it.next());
			insertParameterTypes(analysisType);
		}
	}

	private PreparedStatement insertParameterTypesPreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			String sql = SQL_INSERT_INTO
			+ ObjectEntities.ANATYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID + COMMA
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		}
		finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}

	private void updatePrepareStatementValues(PreparedStatement preparedStatement, AnalysisType analysisType) throws SQLException {
		java.util.Set inParTyps = analysisType.getInParameterTypes();
		java.util.Set criteriaParTyps = analysisType.getCriteriaParameterTypes();
		java.util.Set etalonParTyps = analysisType.getEtalonParameterTypes();
		java.util.Set outParTyps = analysisType.getOutParameterTypes();
		Identifier analysisTypeId = analysisType.getId();
		Identifier parameterTypeId = null;
		String parameterMode = null;

		for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = AnalysisTypeWrapper.MODE_IN;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("AnalysisTypeDatabase.updatePrepareStatementValues | Inserting parameter type " + parameterTypeId
					+ " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = criteriaParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = AnalysisTypeWrapper.MODE_CRITERION;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("AnalysisTypeDatabase.updatePrepareStatementValues | Inserting parameter type " + parameterTypeId
					+ " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = AnalysisTypeWrapper.MODE_ETALON;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("AnalysisTypeDatabase.updatePrepareStatementValues | Inserting parameter type "+ parameterTypeId
					+ " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = AnalysisTypeWrapper.MODE_OUT;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("AnalysisTypeDatabase.updatePrepareStatementValues | Inserting parameter type " + parameterTypeId
					+ " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
	}

	private void insertParameterTypes(AnalysisType analysisType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier analysisTypeId = analysisType.getId();
		try {
			preparedStatement = this.insertParameterTypesPreparedStatement();
			this.updatePrepareStatementValues(preparedStatement, analysisType);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.insertParameterTypes | Cannot insert parameter type for analysis type '"
					+ analysisTypeId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void delete(Identifier id) {
		assert (id.getMajor() != ObjectEntities.ANALYSISTYPE_ENTITY_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		String analysisTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANATYPPARTYPLINK_ENTITY
					+ SQL_WHERE + AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID + EQUALS + analysisTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANALYSISTYPE_ENTITY
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + analysisTypeIdStr);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	protected java.util.Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		java.util.Set collection = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypesByOneQuery(collection);
		this.retrieveMeasurementTypeIdsByOneQuery(collection);
		return collection;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysisType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysisType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

}
