/*
 * $Id: EvaluationTypeDatabase.java,v 1.74 2005/04/01 08:43:32 bob Exp $
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
 * @version $Revision: 1.74 $, $Date: 2005/04/01 08:43:32 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class EvaluationTypeDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultipleSQLValues;

	private EvaluationType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType) storableObject;
		throw new IllegalDataException("EvaluationTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	

	protected String getEnityName() {
		return ObjectEntities.EVALUATIONTYPE_ENTITY;
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

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, evaluationType.getCodename(), SIZE_CODENAME_COLUMN); 
		DatabaseString.setString(preparedStatement, ++startParameterNumber, evaluationType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		String values = APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;		
		return values;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEntity(evaluationType);
		this.retrieveParameterTypes(evaluationType);
		this.retrieveMeasurementTypeIdsByOneQuery(Collections.singleton(evaluationType));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		EvaluationType evaluationType = (storableObject == null) ?
				new EvaluationType(DatabaseIdentifier.getIdentifier(
			resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null, null) :
				this.fromStorableObject(storableObject);
		evaluationType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return evaluationType;
	}

	private void retrieveParameterTypes(EvaluationType evaluationType) throws RetrieveObjectException {	
		java.util.Set inParTyps = new HashSet();
		java.util.Set thresholdParTyps = new HashSet();
		java.util.Set etalonParTyps = new HashSet();
		java.util.Set outParTyps = new HashSet();

		String evaluationTypeIdStr = DatabaseIdentifier.toSQLString(evaluationType.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.EVATYPPARTYPLINK_ENTITY
			+ SQL_WHERE + EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;			
			Identifier parameterTypeId;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(EvaluationTypeWrapper.MODE_IN))
					inParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
				else
					if (parameterMode.equals(EvaluationTypeWrapper.MODE_THRESHOLD))
						thresholdParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
					else
						if (parameterMode.equals(EvaluationTypeWrapper.MODE_ETALON))
							etalonParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
						else
							if (parameterMode.equals(EvaluationTypeWrapper.MODE_OUT))
								outParTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
							else
								Log.errorMessage("EvaluationTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode '" + parameterMode + "' for parameterTypeId " + parameterTypeId);
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for evaluation type '" + evaluationTypeIdStr + "' -- " + sqle.getMessage();
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

		evaluationType.setParameterTypes(inParTyps,
																		 thresholdParTyps,
																		 etalonParTyps,
																		 outParTyps);
	}

  private void retrieveParameterTypesByOneQuery(java.util.Set evaluationTypes) throws RetrieveObjectException {
		if ((evaluationTypes == null) || (evaluationTypes.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE + COMMA
				+ EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID
				+ SQL_FROM + ObjectEntities.EVATYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
    try {
			sql.append(idsEnumerationString(evaluationTypes, EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID, true));
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

    Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();

    try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map inParameterTypesMap = new HashMap();
			Map thresholdParameterTypesMap = new HashMap();
			Map etalonParameterTypesMap = new HashMap();
			Map outParameterTypesMap = new HashMap();
			String parameterMode;
			Identifier parameterTypeId;
			Identifier evaluationTypeId;
			java.util.Set inParameterTypes;
			java.util.Set thresholdParameterTypes;
			java.util.Set etalonParameterTypes;
			java.util.Set outParameterTypes;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				evaluationTypeId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID);

				if (parameterMode.equals(EvaluationTypeWrapper.MODE_IN)) {
					inParameterTypes = (java.util.Set)inParameterTypesMap.get(evaluationTypeId);
					if (inParameterTypes == null) {
						inParameterTypes = new HashSet();
						inParameterTypesMap.put(evaluationTypeId, inParameterTypes);
					}
					inParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
				}
				else
					if (parameterMode.equals(EvaluationTypeWrapper.MODE_THRESHOLD)) {
						thresholdParameterTypes = (java.util.Set)thresholdParameterTypesMap.get(evaluationTypeId);
						if (thresholdParameterTypes == null) {
							thresholdParameterTypes = new HashSet();
							thresholdParameterTypesMap.put(evaluationTypeId, thresholdParameterTypes);
						}
						thresholdParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
					}
					else
						if (parameterMode.equals(EvaluationTypeWrapper.MODE_ETALON)) {
							etalonParameterTypes = (java.util.Set)etalonParameterTypesMap.get(evaluationTypeId);
							if (etalonParameterTypes == null) {
								etalonParameterTypes = new HashSet();
								etalonParameterTypesMap.put(evaluationTypeId, etalonParameterTypes);
							}
							etalonParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
						}
						else
							if (parameterMode.equals(EvaluationTypeWrapper.MODE_OUT)) {
								outParameterTypes = (java.util.Set)outParameterTypesMap.get(evaluationTypeId);
								if (outParameterTypes == null) {
									outParameterTypes = new HashSet();
									outParameterTypesMap.put(evaluationTypeId, outParameterTypes);
								}
								outParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
							}
							else
								Log.errorMessage("EvaluationTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode '" + parameterMode + "' for parameterTypeId '" + parameterTypeId + "' of evaluation type '" + evaluationTypeId + "'");
			}

			EvaluationType evaluationType;
			for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
				evaluationType = (EvaluationType)it.next();
				evaluationTypeId = evaluationType.getId();
				inParameterTypes = (java.util.Set)inParameterTypesMap.get(evaluationTypeId);
				thresholdParameterTypes = (java.util.Set)thresholdParameterTypesMap.get(evaluationTypeId);
				etalonParameterTypes = (java.util.Set)etalonParameterTypesMap.get(evaluationTypeId);
				outParameterTypes = (java.util.Set)outParameterTypesMap.get(evaluationTypeId);

				evaluationType.setParameterTypes(inParameterTypes, thresholdParameterTypes, etalonParameterTypes, outParameterTypes);
			}

		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for evaluation types -- " + sqle.getMessage();
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

	private void retrieveMeasurementTypeIdsByOneQuery(java.util.Set evaluationTypes) throws RetrieveObjectException {
		if ((evaluationTypes == null) || (evaluationTypes.isEmpty()))
			return;

		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID + COMMA
				+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID
				+ SQL_FROM + ObjectEntities.MNTTYPANATYPEVATYP_ENTITY
				+ SQL_WHERE);
		try {
			sql.append(idsEnumerationString(evaluationTypes, EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID, true));
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveMeasurementTypeIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map measurementTypeIdsMap = new HashMap();
			Identifier evaluationTypeId;
			Identifier measurementTypeId;
			java.util.Set measurementTypeIds;
			while (resultSet.next()) {
				evaluationTypeId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID);
				measurementTypeId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);

				measurementTypeIds = (java.util.Set) measurementTypeIdsMap.get(evaluationTypeId);
				if (measurementTypeIds == null) {
					measurementTypeIds = new HashSet();
					measurementTypeIdsMap.put(evaluationTypeId, measurementTypeIds);
				}
				measurementTypeIds.add(measurementTypeId);
			}

			EvaluationType evaluationType;
			for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
				evaluationType = (EvaluationType) it.next();
				evaluationTypeId = evaluationType.getId();
				measurementTypeIds = (java.util.Set) measurementTypeIdsMap.get(evaluationTypeId);

				evaluationType.setMeasurementTypeIds0(measurementTypeIds);
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveMeasurementTypeIdsByOneQuery | Cannot retrieve parameter type ids for evaluation types -- " + sqle.getMessage();
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
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  evaluationType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.insertEntity(evaluationType);
		this.insertParameterTypes(evaluationType);
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			EvaluationType evaluationType = this.fromStorableObject((StorableObject)it.next());
			insertParameterTypes(evaluationType);
		}
	}

	private PreparedStatement insertParameterTypesPreparedStatement() throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVATYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID + COMMA
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
	
	private void updatePrepareStatementValues(PreparedStatement preparedStatement, EvaluationType evaluationType) throws SQLException {
		java.util.Set inParTyps = evaluationType.getInParameterTypes();
		java.util.Set thresholdParTyps = evaluationType.getThresholdParameterTypes();
		java.util.Set etalonParTyps = evaluationType.getEtalonParameterTypes();
		java.util.Set outParTyps = evaluationType.getOutParameterTypes();
		Identifier evaluationTypeId = evaluationType.getId();
		Identifier parameterTypeId = null;
		String parameterMode = null;

		for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = EvaluationTypeWrapper.MODE_IN;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for evaluation type " + evaluationTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = thresholdParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = EvaluationTypeWrapper.MODE_THRESHOLD;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for evaluation type " + evaluationTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = EvaluationTypeWrapper.MODE_ETALON;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for evaluation type " + evaluationTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = EvaluationTypeWrapper.MODE_OUT;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for evaluation type " + evaluationTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
	}
	
	private void insertParameterTypes(EvaluationType evaluationType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier evaluationTypeId = evaluationType.getId();
		try {
			preparedStatement = this.insertParameterTypesPreparedStatement();
			this.updatePrepareStatementValues(preparedStatement, evaluationType);
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.insertParameterTypes | Cannot insert parameter type for evaluation type '" + evaluationTypeId + "' -- " + sqle.getMessage();
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
		if (id.getMajor() != ObjectEntities.EVALUATIONTYPE_ENTITY_CODE)
			throw new IllegalDataException("EvaluationTypeDatabase.delete | Cannot delete object of code "
					+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'");

		String evaluationTypeIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVATYPPARTYPLINK_ENTITY
					+ SQL_WHERE + EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVALUATIONTYPE_ENTITY
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + evaluationTypeIdStr);
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

}
