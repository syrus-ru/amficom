/*
 * $Id: EvaluationTypeDatabase.java,v 1.61 2005/02/16 13:18:28 arseniy Exp $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.61 $, $Date: 2005/02/16 13:18:28 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class EvaluationTypeDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultiplySQLValues;

	private EvaluationType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType) storableObject;
		throw new IllegalDataException("EvaluationTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	

	protected String getEnityName() {
		return ObjectEntities.EVALUATIONTYPE_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}

		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}		
		return updateMultiplySQLValues;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, evaluationType.getCodename(), SIZE_CODENAME_COLUMN); 
			DatabaseString.setString(preparedStatement, ++i, evaluationType.getDescription(), SIZE_DESCRIPTION_COLUMN); 
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(this.getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;		
		return values;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEntity(evaluationType);
		this.retrieveParameterTypes(evaluationType);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		EvaluationType evaluationType = (storableObject == null) ?
				new EvaluationType(DatabaseIdentifier.getIdentifier(
			resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, null) : this
				.fromStorableObject(storableObject);
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
		List inParTyps = new ArrayList();
		List thresholdParTyps = new ArrayList();
		List etalonParTyps = new ArrayList();
		List outParTyps = new ArrayList();

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

		((ArrayList)inParTyps).trimToSize();
		((ArrayList)thresholdParTyps).trimToSize();
		((ArrayList)etalonParTyps).trimToSize();
		((ArrayList)outParTyps).trimToSize();
		evaluationType.setParameterTypes(inParTyps,
																		 thresholdParTyps,
																		 etalonParTyps,
																		 outParTyps);
	}

  private void retrieveParameterTypesByOneQuery(Collection evaluationTypes) throws RetrieveObjectException {
		if ((evaluationTypes == null) || (evaluationTypes.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE + COMMA
				+ EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID
				+ SQL_FROM + ObjectEntities.EVATYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
    try {
			sql.append(this.idsEnumerationString(evaluationTypes, EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID, true));
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
			List inParameterTypes;
			List thresholdParameterTypes;
			List etalonParameterTypes;
			List outParameterTypes;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE);
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				evaluationTypeId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationTypeWrapper.LINK_COLUMN_EVALUATION_TYPE_ID);

				if (parameterMode.equals(EvaluationTypeWrapper.MODE_IN)) {
					inParameterTypes = (List)inParameterTypesMap.get(evaluationTypeId);
					if (inParameterTypes == null) {
						inParameterTypes = new ArrayList();
						inParameterTypesMap.put(evaluationTypeId, inParameterTypes);
					}
					inParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
				}
				else
					if (parameterMode.equals(EvaluationTypeWrapper.MODE_THRESHOLD)) {
						thresholdParameterTypes = (List)thresholdParameterTypesMap.get(evaluationTypeId);
						if (thresholdParameterTypes == null) {
							thresholdParameterTypes = new ArrayList();
							thresholdParameterTypesMap.put(evaluationTypeId, thresholdParameterTypes);
						}
						thresholdParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
					}
					else
						if (parameterMode.equals(EvaluationTypeWrapper.MODE_ETALON)) {
							etalonParameterTypes = (List)etalonParameterTypesMap.get(evaluationTypeId);
							if (etalonParameterTypes == null) {
								etalonParameterTypes = new ArrayList();
								etalonParameterTypesMap.put(evaluationTypeId, etalonParameterTypes);
							}
							etalonParameterTypes.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
						}
						else
							if (parameterMode.equals(EvaluationTypeWrapper.MODE_OUT)) {
								outParameterTypes = (List)outParameterTypesMap.get(evaluationTypeId);
								if (outParameterTypes == null) {
									outParameterTypes = new ArrayList();
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
				inParameterTypes = (List)inParameterTypesMap.get(evaluationTypeId);
				thresholdParameterTypes = (List)thresholdParameterTypesMap.get(evaluationTypeId);
				etalonParameterTypes = (List)etalonParameterTypesMap.get(evaluationTypeId);
				outParameterTypes = (List)outParameterTypesMap.get(evaluationTypeId);

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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.insertEntity(evaluationType);
		this.insertParameterTypes(evaluationType);
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
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
		Collection inParTyps = evaluationType.getInParameterTypes();
		Collection thresholdParTyps = evaluationType.getThresholdParameterTypes();
		Collection etalonParTyps = evaluationType.getEtalonParameterTypes();
		Collection outParTyps = evaluationType.getOutParameterTypes();
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

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
//		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);		
				return;
		}
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				return;
		}
	}

	public void delete(StorableObject storableObject) throws IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		String evaluationTypeIdStr = DatabaseIdentifier.toSQLString(evaluationType.getId());
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
//
//	public EvaluationType retrieveForCodename(String codename) throws ObjectNotFoundException, RetrieveObjectException {
//		Collection objects = null;
//		try {
//			objects = this.retrieveByIds(null, StorableObjectWrapper.COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTOPHE);
//		}
//		catch (IllegalDataException ide) {				
//			throw new RetrieveObjectException(ide);
//		}
//
//		if ((objects == null) || (objects.isEmpty()))
//			throw new ObjectNotFoundException("No evaluation type with codename: '" + codename + "'");
//
//		return (EvaluationType) objects.iterator().next();
//	}

	public Collection retrieveAll() throws RetrieveObjectException {
		try {
			return this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null; 
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);

		this.retrieveParameterTypesByOneQuery(objects);

		return objects;	
	}
	 
//	private List retrieveButIdByThresholdSet(List ids, List thresholdSetIds) throws RetrieveObjectException, IllegalDataException {
//		if (thresholdSetIds != null && !thresholdSetIds.isEmpty()) {
//			String condition = new String();
//			StringBuffer thresholds = new StringBuffer();
//
//			int i = 1;
//			for (Iterator it = thresholdSetIds.iterator(); it.hasNext(); i++) {
//				thresholds.append(DatabaseIdentifier.toSQLString((Identifier) it.next()));
//				if (it.hasNext()) {
//					if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
//						thresholds.append(COMMA);
//					else {
//						thresholds.append(CLOSE_BRACKET);
//						thresholds.append(SQL_OR);
//						thresholds.append(SetWrapper.LINK_COLUMN_SET_ID);
//						thresholds.append(SQL_IN);
//						thresholds.append(OPEN_BRACKET);
//					}
//				}
//			}
//
//	    condition = PARAMETER_TYPE_ID + SQL_IN
//						+ OPEN_BRACKET
//						+ SQL_SELECT + StorableObjectWrapper.COLUMN_TYPE_ID + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
//						+ SQL_WHERE + SetWrapper.LINK_COLUMN_SET_ID + SQL_IN + OPEN_BRACKET + thresholds
//						+ CLOSE_BRACKET
//					+ CLOSE_BRACKET
//					+ SQL_AND + PARAMETER_MODE + EQUALS + APOSTOPHE + EvaluationTypeWrapper.MODE_THRESHOLD + APOSTOPHE;
//			return retrieveButIds(ids, condition);
//		}
//		return Collections.EMPTY_LIST;
//	}
}
