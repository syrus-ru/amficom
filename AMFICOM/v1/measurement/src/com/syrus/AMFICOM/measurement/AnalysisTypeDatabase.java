/*
 * $Id: AnalysisTypeDatabase.java,v 1.79 2005/04/01 08:43:32 bob Exp $
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
 * @version $Revision: 1.79 $, $Date: 2005/04/01 08:43:32 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class AnalysisTypeDatabase extends StorableObjectDatabase {	

	private static String columns;
	private static String updateMultipleSQLValues;

	private AnalysisType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		throw new IllegalDataException("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
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

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		super.retrieveEntity(analysisType);
		this.retrieveParameterTypes(analysisType);
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(analysisType));
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

	private void retrieveParameterTypes(AnalysisType analysisType) throws RetrieveObjectException {	
		java.util.Set inParTyps = new HashSet();
		java.util.Set criteriaParTyps = new HashSet();
		java.util.Set etalonParTyps = new HashSet();
		java.util.Set outParTyps = new HashSet();

		String analysisTypeIdStr = DatabaseIdentifier.toSQLString(analysisType.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.ANATYPPARTYPLINK_ENTITY
			+ SQL_WHERE + AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID + EQUALS + analysisTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			Identifier parameterTypeId;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(AnalysisTypeWrapper.MODE_IN))
					inParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
					else
						if (parameterMode.equals(AnalysisTypeWrapper.MODE_CRITERION))
							criteriaParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
						else
							if (parameterMode.equals(AnalysisTypeWrapper.MODE_ETALON))
								etalonParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
							else
								if (parameterMode.equals(AnalysisTypeWrapper.MODE_OUT))
									outParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
								else
									Log .errorMessage("AnalysisTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode '" + parameterMode + "' for parameterTypeId " + parameterTypeId);
			}
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for analysis type '" + analysisTypeIdStr + "' -- " + sqle.getMessage();
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

		analysisType.setParameterTypes(inParTyps, criteriaParTyps, etalonParTyps, outParTyps);
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
    try {
			sql.append(idsEnumerationString(analysisTypes, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID, true));
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map inParameterTypesMap = new HashMap();
			Map criteriaParameterTypesMap = new HashMap();
			Map etalonParameterTypesMap = new HashMap();
			Map outParameterTypesMap = new HashMap();
			String parameterMode;
			Identifier parameterTypeId;
			Identifier analysisTypeId;
			java.util.Set inParameterTypes;
			java.util.Set criteriaParameterTypes;
			java.util.Set etalonParameterTypes;
			java.util.Set outParameterTypes;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				analysisTypeId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID);

				if (parameterMode.equals(AnalysisTypeWrapper.MODE_IN)) {
					inParameterTypes = (java.util.Set) inParameterTypesMap.get(analysisTypeId);
					if (inParameterTypes == null) {
						inParameterTypes = new HashSet();
						inParameterTypesMap.put(analysisTypeId, inParameterTypes);
					}
					inParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
				}
				else
					if (parameterMode.equals(AnalysisTypeWrapper.MODE_CRITERION)) {
						criteriaParameterTypes = (java.util.Set) criteriaParameterTypesMap.get(analysisTypeId);
						if (criteriaParameterTypes == null) {
							criteriaParameterTypes = new HashSet();
							criteriaParameterTypesMap.put(analysisTypeId, criteriaParameterTypes);
						}
						criteriaParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
					}
					else
						if (parameterMode.equals(AnalysisTypeWrapper.MODE_ETALON)) {
							etalonParameterTypes = (java.util.Set) etalonParameterTypesMap.get(analysisTypeId);
							if (etalonParameterTypes == null) {
								etalonParameterTypes = new HashSet();
								etalonParameterTypesMap.put(analysisTypeId, etalonParameterTypes);
							}
							etalonParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
						}
						else
							if (parameterMode.equals(AnalysisTypeWrapper.MODE_OUT)) {
								outParameterTypes = (java.util.Set) outParameterTypesMap.get(analysisTypeId);
								if (outParameterTypes == null) {
									outParameterTypes = new HashSet();
									outParameterTypesMap.put(analysisTypeId, outParameterTypes);
								}
								outParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
							}
							else
								Log.errorMessage("AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | ERROR: Unknown parameter mode '"
										+ parameterMode + "' for parameterTypeId '" + parameterTypeId + "' of analysis type '" + analysisTypeId + "'");
			}

			AnalysisType analysisType;
			for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
				analysisType = (AnalysisType) it.next();
				analysisTypeId = analysisType.getId();
				inParameterTypes = (java.util.Set) inParameterTypesMap.get(analysisTypeId);
				criteriaParameterTypes = (java.util.Set) criteriaParameterTypesMap.get(analysisTypeId);
				etalonParameterTypes = (java.util.Set) etalonParameterTypesMap.get(analysisTypeId);
				outParameterTypes = (java.util.Set) outParameterTypesMap.get(analysisTypeId);

				analysisType.setParameterTypes(inParameterTypes, criteriaParameterTypes, etalonParameterTypes, outParameterTypes);
			}

		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Cannot retrieve parameter type ids for analysis types -- " + sqle.getMessage();
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
		try {
			sql.append(idsEnumerationString(analysisTypes, AnalysisTypeWrapper.LINK_COLUMN_ANALYSIS_TYPE_ID, true));
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

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
			String mesg = "AnalysisTypeDatabase.retrieveMeasurementTypeIdsByOneQuery | Cannot retrieve parameter type ids for analysis types -- " + sqle.getMessage();
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
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  analysisType.getId() + "'; argument: " + arg);
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
			Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = criteriaParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = AnalysisTypeWrapper.MODE_CRITERION;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = AnalysisTypeWrapper.MODE_ETALON;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = AnalysisTypeWrapper.MODE_OUT;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type " + analysisTypeId, Log.DEBUGLEVEL09);
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
			String mesg = "AnalysisTypeDatabase.insertParameterTypes | Cannot insert parameter type for analysis type '" + analysisTypeId + "' -- " + sqle.getMessage();
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

	public void delete(Identifier id) throws IllegalDataException {
		if (id.getMajor() != ObjectEntities.ANALYSISTYPE_ENTITY_CODE)
			throw new IllegalDataException("AnalysisTypeDatabase.delete | Cannot delete object of code "
					+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'");

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
