/*
 * $Id: ResultDatabase.java,v 1.75 2005/03/04 13:34:22 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
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
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.75 $, $Date: 2005/03/04 13:34:22 $
 * @author $Author: bass $
 * @module measurement_v1
 */

public class ResultDatabase extends StorableObjectDatabase {

	private static String			columns;
	private static String			updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.RESULT_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			StringBuffer buffer = new StringBuffer(COMMA);
			buffer.append(ResultWrapper.COLUMN_MEASUREMENT_ID);
			buffer.append(COMMA);
			buffer.append(ResultWrapper.COLUMN_ANALYSIS_ID);
			buffer.append(COMMA);
			buffer.append(ResultWrapper.COLUMN_EVALUATION_ID);
			buffer.append(COMMA);			
			buffer.append(ResultWrapper.COLUMN_MODELING_ID);
			buffer.append(COMMA);
			buffer.append(ResultWrapper.COLUMN_SORT);
			columns = buffer.toString();
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null) {
			StringBuffer buffer = new StringBuffer(super.getUpdateMultiplySQLValues());
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			updateMultiplySQLValues = buffer.toString();
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Result result = this.fromStorableObject(storableObject);
		StringBuffer buffer = new StringBuffer(super.getUpdateSingleSQLValues(storableObject));
		buffer.append(COMMA);
		int resultSort = result.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
			default:
				Log.errorMessage("ResultDatabase.insertResult | Illegal sort: " + resultSort
						+ " of result '" + result.getId().getIdentifierString() + "'");
		}
		buffer.append(Integer.toString(resultSort));
		return buffer.toString();
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		Result result = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		int resultSort = result.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:					
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);					
				DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getAction().getId());
				break;
			default:
				Log.errorMessage("ResultDatabase.insertResult | Illegal sort: " + resultSort
						+ " of result '" + result.getId().getIdentifierString() + "'");
		}
		preparedStatement.setInt(++i, result.getSort().value());
		return i;
	}

	private Result fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Result)
			return (Result) storableObject;
		throw new IllegalDataException("ResultDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Result result = this.fromStorableObject(storableObject);
		this.retrieveEntity(result);
		this.retrieveResultParametersByOneQuery(Collections.singletonList(result));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Result result = (storableObject == null)
				? new Result(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
									null,
									0L,
									null,
									0,
									null)
				: this.fromStorableObject(storableObject);
		int resultSort = resultSet.getInt(ResultWrapper.COLUMN_SORT);
		Action action = null;
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				try {
					action = (Measurement) MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.COLUMN_MEASUREMENT_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				try {
					action = (Analysis) MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.COLUMN_ANALYSIS_ID), true);
				}
				catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				try {
					action = (Evaluation) MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.COLUMN_EVALUATION_ID), true);
				}
				catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_MODELING:
				try {
					action = (Modeling) MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.COLUMN_MODELING_ID), true);
				}
				catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			default:
				Log.errorMessage("Unkown sort: " + resultSort + " of result " + result.getId().getIdentifierString());
		}
		result.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED), 
							 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							 action,
							 resultSort);

		return result;
	}

//	private void retrieveResultParameters(Result result) throws RetrieveObjectException {
//		List parameters = new LinkedList();
//
//		String resultIdStr = DatabaseIdentifier.toSQLString(result.getId());
//		String sql = SQL_SELECT + COLUMN_ID + COMMA + LINK_COLUMN_TYPE_ID + COMMA + LINK_COLUMN_VALUE
//				+ SQL_FROM + ObjectEntities.RESULTPARAMETER_ENTITY + SQL_WHERE + LINK_COLUMN_RESULT_ID
//				+ EQUALS + resultIdStr;
//		Statement statement = null;
//		ResultSet resultSet = null;
//		Connection connection = DatabaseConnection.getConnection();
//		try {
//			statement = connection.createStatement();
//			Log.debugMessage("ResultDatabase.retrieveResultParameters | Trying: " + sql, Log.DEBUGLEVEL09);
//			resultSet = statement.executeQuery(sql);
//			SetParameter parameter;
//			ParameterType parameterType;
//			while (resultSet.next()) {
//				try {
//					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_TYPE_ID), true);
//				}
//				catch (ApplicationException ae) {
//					throw new RetrieveObjectException(ae);
//				}
//				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
//											 parameterType,
//											 ByteArrayDatabase.toByteArray(resultSet.getBlob(LINK_COLUMN_VALUE)));
//				parameters.add(parameter);
//			}
//		}
//		catch (SQLException sqle) {
//			String mesg = "ResultDatabase.retrieveResultParameters | Cannot retrieve parameters for result '"
//					+ resultIdStr + "' -- " + sqle.getMessage();
//			throw new RetrieveObjectException(mesg, sqle);
//		}
//		finally {
//			try {
//				if (statement != null)
//					statement.close();
//				if (resultSet != null)
//					resultSet.close();
//				statement = null;
//				resultSet = null;
//			}
//			catch (SQLException sqle1) {
//				Log.errorException(sqle1);
//			}
//			finally {
//				DatabaseConnection.releaseConnection(connection);
//			}
//		}
//		result.setParameters((SetParameter[]) parameters.toArray(new SetParameter[parameters.size()]));
//	}

	/**	 
	 * @param results
	 * @throws RetrieveObjectException
	 */
	private void retrieveResultParametersByOneQuery(Collection results) throws RetrieveObjectException {
		if ((results == null) || (results.isEmpty()))
			return;		
		
		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ ResultWrapper.LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ ResultWrapper.LINK_COLUMN_RESULT_ID 
				+ SQL_FROM + ObjectEntities.RESULTPARAMETER_ENTITY
				+ SQL_WHERE);
		try {
			sql.append(this.idsEnumerationString(results, ResultWrapper.LINK_COLUMN_RESULT_ID, true));
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Map resultParametersMap = new HashMap();
		Identifier resultId;
		Collection resultParameters;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.retrieveResultParametersByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			ParameterType parameterType;
			SetParameter parameter;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
														parameterType,
														ByteArrayDatabase.toByteArray(resultSet.getBlob(ResultWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				resultId = DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.LINK_COLUMN_RESULT_ID);
				resultParameters = (Collection) resultParametersMap.get(resultId);
				if (resultParameters == null) {
					resultParameters = new HashSet();
					resultParametersMap.put(resultId, resultParameters);
				}
				resultParameters.add(parameter);
			}
		}
		catch (SQLException sqle) {
			String mesg = "ResultDatabase.retrieveResultParametersByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

		Result result;
		for (Iterator it = results.iterator(); it.hasNext();) {
			result = (Result) it.next();
			resultId = result.getId();
			resultParameters = (Collection) resultParametersMap.get(resultId);

			if (resultParameters != null)
				result.setParameters0((SetParameter[]) resultParameters.toArray(new SetParameter[resultParameters.size()]));
			else
				result.setParameters0(new SetParameter[0]);
		}

	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Result result = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Result result = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(result);
			this.insertResultParameters(result);
		}
		catch (CreateObjectException e) {
			this.delete(result);
			throw e;
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);

		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Result result = this.fromStorableObject((StorableObject) it.next());
			this.insertResultParameters(result);
		}

	}

	private void insertResultParameters(Result result) throws CreateObjectException {
		Identifier resultId = result.getId();
		SetParameter[] setParameters = result.getParameters();
		String sql = SQL_INSERT_INTO
				+ ObjectEntities.RESULTPARAMETER_ENTITY + OPEN_BRACKET
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ ResultWrapper.LINK_COLUMN_RESULT_ID + COMMA
				+ ResultWrapper.LINK_COLUMN_PARAMETER_VALUE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION+ COMMA
				+ SQL_FUNCTION_EMPTY_BLOB + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < setParameters.length; i++) {
				parameterId = setParameters[i].getId();
				parameterTypeId = setParameters[i].getType().getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, resultId);

				Log.debugMessage("ResultDatabase.insertResultParameters | Inserting parameter " + parameterTypeId.toString()
						+ " for result " + resultId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				ByteArrayDatabase.saveAsBlob(setParameters[i].getValue(),
						connection,
						ObjectEntities.RESULTPARAMETER_ENTITY,
						ResultWrapper.LINK_COLUMN_PARAMETER_VALUE,
						StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "ResultDatabase.insertResultParameters | Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterTypeId.toString() + "' for result '" + resultId + "' -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void delete(Identifier id) throws IllegalDataException {
		if (id.getMajor() != ObjectEntities.RESULT_ENTITY_CODE)
			throw new IllegalDataException("ResultDatabase.delete | Cannot delete object of code "
					+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'");

		String resultIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + ObjectEntities.RESULTPARAMETER_ENTITY + SQL_WHERE
					+ ResultWrapper.LINK_COLUMN_RESULT_ID + EQUALS + resultIdStr);
			statement.executeUpdate(SQL_DELETE_FROM + ObjectEntities.RESULT_ENTITY + SQL_WHERE + StorableObjectWrapper.COLUMN_ID
					+ EQUALS + resultIdStr);
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

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null;
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);

		this.retrieveResultParametersByOneQuery(objects);
		return objects;
	}

//	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
//		List list = null;
//
//		String condition = ResultWrapper.COLUMN_MEASUREMENT_ID + SQL_IN + OPEN_BRACKET + SQL_SELECT + StorableObjectWrapper.COLUMN_ID + SQL_FROM
//				+ ObjectEntities.MEASUREMENT_ENTITY + SQL_WHERE
//				+ MeasurementWrapper.COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET + SQL_SELECT
//				+ StorableObjectWrapper.COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
//				+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId()) + CLOSE_BRACKET
//				+ CLOSE_BRACKET;
//
//		try {
//			list = retrieveButIds(ids, condition);
//		}
//		catch (IllegalDataException ide) {
//			Log.debugMessage("ResultDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(),
//						Log.DEBUGLEVEL09);
//		}
//
//		return list;
//	}

//	private List retrieveButIdsByMeasurement(List ids, List measurementIds) throws RetrieveObjectException {
//		List list = null;
//
//		StringBuffer buffer = new StringBuffer();
//		if ((measurementIds != null) && (!measurementIds.isEmpty())) {
//			buffer.append(ResultWrapper.COLUMN_MEASUREMENT_ID);
//			buffer.append(SQL_IN);
//			buffer.append(OPEN_BRACKET);
//			int i = 1;
//			for (Iterator it = measurementIds.iterator(); it.hasNext(); i++) {
//				Object object = it.next();
//				Identifier id = null;
//				if (object instanceof Identifier)
//					id = (Identifier) object;
//				else
//					if (object instanceof Identifiable)
//						id = ((Identifiable) object).getId();
//					else
//						throw new RetrieveObjectException(
//										"ResultDatabase.retrieveButIdsByMeasurement | Object "
//												+ object.getClass().getName() + " isn't Identifier or Identifiable");
//
//				if (id != null) {
//					buffer.append(DatabaseIdentifier.toSQLString(id));
//					if (it.hasNext()) {
//						if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
//							buffer.append(COMMA);
//						else {
//							buffer.append(CLOSE_BRACKET);
//							buffer.append(SQL_OR);
//							buffer.append(ResultWrapper.COLUMN_MEASUREMENT_ID);
//							buffer.append(SQL_IN);
//							buffer.append(OPEN_BRACKET);
//						}					
//					}
//				}
//			}
//			buffer.append(CLOSE_BRACKET);
//
//			try {
//				Log.debugMessage("ResultDatabase.retrieveButIdsByMeasurement | Trying additional condition: " + buffer.toString(),
//									Log.DEBUGLEVEL09);
//				list = retrieveButIds(ids, buffer.toString());
//			}
//			catch (IllegalDataException ide) {
//				Log.debugMessage("ResultDatabase.retrieveButIdsByMeasurement | Error: " + ide.getMessage(),
//							Log.DEBUGLEVEL09);
//			}
//			
//		}
//		else 
//			list = Collections.EMPTY_LIST;
//
//		return list;
//	}

//	private List retrieveButIdsByMeasurementAndSort(List ids, Identifier measurementId, ResultSort resultSort) throws RetrieveObjectException {
//		List list = null;
//
//		String sql = ResultWrapper.COLUMN_MEASUREMENT_ID + EQUALS + DatabaseIdentifier.toSQLString(measurementId)
//			+ SQL_AND + ResultWrapper.COLUMN_SORT + EQUALS + resultSort.value();
//		
//		try {
//			list = retrieveButIds(ids, sql);
//		}
//		catch (IllegalDataException ide) {
//			Log.debugMessage("ResultDatabase.retrieveButIdsByMeasurement | Error: " + ide.getMessage(),
//						Log.DEBUGLEVEL09);
//		}
//		
//		return list;
//	}	
}
