/*
 * $Id: ResultDatabase.java,v 1.50 2004/12/29 10:11:46 arseniy Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.50 $, $Date: 2004/12/29 10:11:46 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class ResultDatabase extends StorableObjectDatabase {

	//	 measurementId VARCHAR2(32) NOT NULL,
	public static final String	COLUMN_MEASUREMENT_ID	= "measurement_id";
	//	 analysisId VARCHAR2(32),
	public static final String	COLUMN_ANALYSIS_ID	= "analysis_id";
	//	 evaluationId VARCHAR2(32),
	public static final String	COLUMN_EVALUATION_ID	= "evaluation_id";
	//	 modelingId VARCHAR2(32),
	public static final String	COLUMN_MODELING_ID	= "modeling_id";
	
	//	 sort NUMBER(2, 0) NOT NULL,
	public static final String	COLUMN_SORT		= "sort";
	//	 alarmLevel NUMBER(2, 0) NOT NULL,
	public static final String	COLUMN_ALARM_LEVEL	= "alarm_level";

	public static final String	LINK_COLUMN_TYPE_ID	= "type_id";
	public static final String	LINK_COLUMN_RESULT_ID	= "result_id";
	public static final String	LINK_COLUMN_VALUE	= "value";

	private static String			columns;
	private static String			updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.RESULT_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			StringBuffer buffer = new StringBuffer(super.getColumns(mode));
			buffer.append(COMMA);
			buffer.append(COLUMN_MEASUREMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_ANALYSIS_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_EVALUATION_ID);
			buffer.append(COMMA);			
			buffer.append(COLUMN_MODELING_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_SORT);
			buffer.append(COMMA);
			buffer.append(COLUMN_ALARM_LEVEL);
			columns = buffer.toString();
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			StringBuffer buffer = new StringBuffer(super.getUpdateMultiplySQLValues(mode));
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
			buffer.append(COMMA);
			buffer.append(QUESTION);
			updateMultiplySQLValues = buffer.toString();
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Result result = fromStorableObject(storableObject);
		StringBuffer buffer = new StringBuffer(super.getUpdateSingleSQLValues(storableObject));
		buffer.append(COMMA);
		int resultSort = result.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				buffer.append(DatabaseIdentifier.toSQLString(result.getMeasurement().getId()));
				buffer.append(COMMA);				
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				buffer.append(DatabaseIdentifier.toSQLString(result.getMeasurement().getId()));
				buffer.append(COMMA);				
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString((Identifier)null));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				buffer.append(DatabaseIdentifier.toSQLString(result.getMeasurement().getId()));
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
		buffer.append(COMMA);
		buffer.append(Integer.toString(result.getAlarmLevel().value()));
		return buffer.toString();
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Result result = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			int resultSort = result.getSort().value();
			switch (resultSort) {
				case ResultSort._RESULT_SORT_MEASUREMENT:					
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getMeasurement().getId());
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					break;
				case ResultSort._RESULT_SORT_ANALYSIS:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getMeasurement().getId());
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getAction().getId());
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, null);
					break;
				case ResultSort._RESULT_SORT_EVALUATION:
					DatabaseIdentifier.setIdentifier(preparedStatement, ++i, result.getMeasurement().getId());
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
			preparedStatement.setInt(++i, result.getAlarmLevel().value());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName()
					+ "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	private Result fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Result)
			return (Result) storableObject;
		throw new IllegalDataException("ResultDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		Result result = this.fromStorableObject(storableObject);
		this.retrieveEntity(result);
		this.retrieveResultParametersByOneQuery(Collections.singletonList(result));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Result result = (storableObject == null) ? new Result(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
									null, null, null, 0, 0, null)
				: fromStorableObject(storableObject);
		Measurement measurement = null;		
		int resultSort = resultSet.getInt(COLUMN_SORT);
		Action action = null;
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				try {
					measurement = (Measurement) MeasurementStorableObjectPool
							.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_ID),
										true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				action = measurement;
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				try {
					measurement = (Measurement) MeasurementStorableObjectPool
					.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_ID),
								true);

					action = (Analysis) MeasurementStorableObjectPool
							.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANALYSIS_ID), true);
				} catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				try {
					measurement = (Measurement) MeasurementStorableObjectPool
					.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_ID),
								true);
					action = (Evaluation) MeasurementStorableObjectPool
							.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_EVALUATION_ID), true);
				} catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_MODELING:
				try {
					action = (Modeling) MeasurementStorableObjectPool
							.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODELING_ID), true);
				} catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			default:
				Log.errorMessage("Unkown sort: " + resultSort + " of result "
						+ result.getId().getIdentifierString());
		}
		result.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED), 
							 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
							 measurement,
							 action,
							 resultSort,
							 resultSet.getInt(COLUMN_ALARM_LEVEL));

		return result;
	}
	
	private void retrieveResultParameters(Result result) throws RetrieveObjectException {
		List parameters = new LinkedList();

		String resultIdStr = DatabaseIdentifier.toSQLString(result.getId());
		String sql = SQL_SELECT + COLUMN_ID + COMMA + LINK_COLUMN_TYPE_ID + COMMA + LINK_COLUMN_VALUE
				+ SQL_FROM + ObjectEntities.RESULTPARAMETER_ENTITY + SQL_WHERE + LINK_COLUMN_RESULT_ID
				+ EQUALS + resultIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.retrieveResultParameters | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			SetParameter parameter;
			ParameterType parameterType;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType) MeasurementStorableObjectPool
							.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_TYPE_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
											 parameterType,
											 ByteArrayDatabase.toByteArray(resultSet.getBlob(LINK_COLUMN_VALUE)));
				parameters.add(parameter);
			}
		} catch (SQLException sqle) {
			String mesg = "ResultDatabase.retrieveResultParameters | Cannot retrieve parameters for result '"
					+ resultIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		result.setParameters((SetParameter[]) parameters.toArray(new SetParameter[parameters.size()]));
	}

	/**	 
	 * @param results
	 * @throws RetrieveObjectException
	 */
	private void retrieveResultParametersByOneQuery(List results) throws RetrieveObjectException {
		//List parameters = new LinkedList();
		
		if ((results == null) || (results.isEmpty()))
			return;		
		
		StringBuffer sql = new StringBuffer(SQL_SELECT + COLUMN_ID + COMMA 
											+ LINK_COLUMN_TYPE_ID + COMMA 
											+ LINK_COLUMN_VALUE + COMMA
											+ LINK_COLUMN_RESULT_ID 
				+ SQL_FROM + ObjectEntities.RESULTPARAMETER_ENTITY + SQL_WHERE + LINK_COLUMN_RESULT_ID
				+ SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = results.iterator(); it.hasNext();i++) {
			Result result = (Result)it.next();
			sql.append(DatabaseIdentifier.toSQLString(result.getId()));
			if (it.hasNext()){
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(LINK_COLUMN_RESULT_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}					
			}
		}
		sql.append(CLOSE_BRACKET);
		
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.retrieveResultParameters | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			SetParameter parameter;
			ParameterType parameterType;
			Map resultParametersMap = new HashMap();
			while (resultSet.next()) {
				Result result = null;
				Identifier resultId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_RESULT_ID);
				for (Iterator it = results.iterator(); it.hasNext();) {
					Result res = (Result) it.next();
					if (res.getId().equals(resultId)){
						result = res;
						break;
					}					
				}
				
				if (result == null){
					String mesg = "ResultDatabase.retrieveResultParameters | Cannot found correspond result for '" + resultId +"'" ;
					throw new RetrieveObjectException(mesg);
				}
					
				try {
					parameterType = (ParameterType) MeasurementStorableObjectPool
							.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_TYPE_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
											 parameterType, ByteArrayDatabase.toByteArray(resultSet.getBlob(LINK_COLUMN_VALUE)));
				List parameters = (List)resultParametersMap.get(result);
				if (parameters == null){
					parameters = new LinkedList();
					resultParametersMap.put(result, parameters);
				}				
				parameters.add(parameter);				
			}
			
			for (Iterator iter = results.iterator(); iter.hasNext();) {
				Result result = (Result) iter.next();
				List parameters = (List)resultParametersMap.get(result);
				result.setParameters((SetParameter[]) parameters.toArray(new SetParameter[parameters.size()]));
			}
			
		} catch (SQLException sqle) {
			String mesg = "ResultDatabase.retrieveResultParameters | Cannot retrieve parameters for result -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}		
	}

	//private void retrieveResultParameters(Result result) throws RetrieveObjectException {
	private void retrieveResultParameters(List results) throws RetrieveObjectException {
		List parameters = new LinkedList();

		String sql = SQL_SELECT + COLUMN_ID + COMMA
							+ LINK_COLUMN_TYPE_ID + COMMA + LINK_COLUMN_VALUE
							+ SQL_FROM + ObjectEntities.RESULTPARAMETER_ENTITY + SQL_WHERE 
							+ LINK_COLUMN_RESULT_ID + EQUALS + QUESTION;
        if ((results != null) && (!results.isEmpty())){		
			PreparedStatement preparedStatement = null;
			Connection connection = DatabaseConnection.getConnection();
			try {
				preparedStatement = connection.prepareStatement(sql);
				Log.debugMessage("ResultDatabase.retrieveResultParameters | Trying: " + sql, Log.DEBUGLEVEL09);
				SetParameter parameter;
				ParameterType parameterType;
				for (Iterator it = results.iterator(); it.hasNext();) {
					Result result = (Result) it.next();
					Identifier id = result.getId();
					DatabaseIdentifier.setIdentifier(preparedStatement, 1, id);
					ResultSet resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						try {
							parameterType = (ParameterType) MeasurementStorableObjectPool
									.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_TYPE_ID), true);
						} catch (ApplicationException ae) {
							throw new RetrieveObjectException(ae);
						}
						parameter = new SetParameter(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
													 parameterType, ByteArrayDatabase.toByteArray(resultSet.getBlob(LINK_COLUMN_VALUE)));
						parameters.add(parameter);						
					} 
					
					result.setParameters((SetParameter[]) parameters.toArray(new SetParameter[parameters.size()]));
				}

			} catch (SQLException sqle) {
				String mesg = "ResultDatabase.retrieveResultParameters | Cannot retrieve parameters for results -- " + sqle.getMessage();
				throw new RetrieveObjectException(mesg, sqle);
			} finally {
				try {
					if (preparedStatement != null)
						preparedStatement.close();
					preparedStatement = null;
				} catch (SQLException sqle1) {
					Log.errorException(sqle1);
				} finally {
					DatabaseConnection.releaseConnection(connection);
				}
			}			
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
		} catch (CreateObjectException e) {
			this.delete(result);
			throw e;
		}
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);

		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Result result = (Result) it.next();
			this.insertResultParameters(result);
		}

	}

	private void insertResultParameters(Result result) throws CreateObjectException {
		Identifier resultId = result.getId();
		SetParameter[] setParameters = result.getParameters();
		String sql = SQL_INSERT_INTO + ObjectEntities.RESULTPARAMETER_ENTITY + OPEN_BRACKET + COLUMN_ID + COMMA
				+ LINK_COLUMN_TYPE_ID + COMMA + LINK_COLUMN_RESULT_ID + COMMA + LINK_COLUMN_VALUE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET + QUESTION + COMMA + QUESTION + COMMA
				+ QUESTION + COMMA + APOSTOPHE + SQL_EMPTY_BLOB + APOSTOPHE + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		int i = 0;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				parameterId = setParameters[i].getId();
				parameterTypeId = setParameters[i].getType().getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, resultId);
				
				Log.debugMessage("ResultDatabase.insertResultParameters | Inserting parameter "
						+ parameterTypeId.toString() + " for result " + resultId,
							Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				//				ByteArrayDatabase badb = new
				// ByteArrayDatabase(setParameters[i].getValue());
				ByteArrayDatabase.saveAsBlob(setParameters[i].getValue(), connection,
								ObjectEntities.RESULTPARAMETER_ENTITY,
								LINK_COLUMN_VALUE, COLUMN_ID + EQUALS
										+ DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		} catch (SQLException sqle) {
			String mesg = "ResultDatabase.insertResultParameters | Cannot insert parameter '"
					+ parameterId.toString() + "' of type '" + parameterTypeId.toString()
					+ "' for result '" + resultId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
//		Result result = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntity(storableObject, true);
				return;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:
			default:
				super.checkAndUpdateEntities(storableObjects, true);
				return;
		}
	}

	public void delete(StorableObject storableObject) throws IllegalDataException {
		Result result = fromStorableObject(storableObject);
		String resultIdStr = DatabaseIdentifier.toSQLString(result.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + ObjectEntities.RESULTPARAMETER_ENTITY + SQL_WHERE
					+ LINK_COLUMN_RESULT_ID + EQUALS + resultIdStr);
			statement.executeUpdate(SQL_DELETE_FROM + ObjectEntities.RESULT_ENTITY + SQL_WHERE + COLUMN_ID
					+ EQUALS + resultIdStr);
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = retrieveByIdsOneQuery(null, condition);
		else
			list = retrieveByIdsOneQuery(ids, condition);

		retrieveResultParametersByOneQuery(list);
		// retrieveResultParameters(list);
		return list;
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;

		String condition = COLUMN_MEASUREMENT_ID + SQL_IN + OPEN_BRACKET + SQL_SELECT + COLUMN_ID + SQL_FROM
				+ ObjectEntities.MEASUREMENT_ENTITY + SQL_WHERE
				+ MeasurementDatabase.COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET + SQL_SELECT
				+ COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
				+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId()) + CLOSE_BRACKET
				+ CLOSE_BRACKET;

		try {
			list = retrieveButIds(ids, condition);
		} catch (IllegalDataException ide) {
			Log.debugMessage("ResultDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(),
						Log.DEBUGLEVEL09);
		}

		return list;
	}

	private List retrieveButIdsByMeasurement(List ids, List measurementIds) throws RetrieveObjectException {
		List list = null;

		StringBuffer buffer = new StringBuffer();
		if ((measurementIds != null) && (!measurementIds.isEmpty())) {
			buffer.append(COLUMN_MEASUREMENT_ID);
			buffer.append(SQL_IN);
			buffer.append(OPEN_BRACKET);
			int i = 1;
			for (Iterator it = measurementIds.iterator(); it.hasNext(); i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identified)
					id = ((Identified) object).getId();
				else
					throw new RetrieveObjectException(
										"ResultDatabase.retrieveButIdsByMeasurement | Object "
												+ object
														.getClass()
														.getName()
												+ " isn't Identifier or Identified");

				if (id != null) {
					buffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()){
						if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
							buffer.append(COMMA);
						else {
							buffer.append(CLOSE_BRACKET);
							buffer.append(SQL_OR);
							buffer.append(COLUMN_MEASUREMENT_ID);
							buffer.append(SQL_IN);
							buffer.append(OPEN_BRACKET);
						}					
					}
				}
			}
			buffer.append(CLOSE_BRACKET);

			try {
				Log.debugMessage("ResultDatabase.retrieveButIdsByMeasurement | Try additional condition: " + buffer.toString(),
									Log.DEBUGLEVEL09);
				list = retrieveButIds(ids, buffer.toString());
			} catch (IllegalDataException ide) {
				Log.debugMessage("ResultDatabase.retrieveButIdsByMeasurement | Error: " + ide.getMessage(),
							Log.DEBUGLEVEL09);
			}
			
		} else 
			list = Collections.EMPTY_LIST;

		return list;
	}

	private List retrieveButIdsByMeasurementAndSort(List ids, Identifier measurementId, ResultSort resultSort) throws RetrieveObjectException {
		List list = null;

		String sql = COLUMN_MEASUREMENT_ID + EQUALS + DatabaseIdentifier.toSQLString(measurementId)
			+ SQL_AND + COLUMN_SORT + EQUALS + resultSort.value();
		
		try{
			list = retrieveButIds(ids, sql);
		} catch (IllegalDataException ide) {
			Log.debugMessage("ResultDatabase.retrieveButIdsByMeasurement | Error: " + ide.getMessage(),
						Log.DEBUGLEVEL09);
		}
		
		return list;
	}	

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list;
		if (condition instanceof LinkedIdsCondition){
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
			List measurementIds = linkedIdsCondition.getLinkedIds();
			if (measurementIds == null)
				measurementIds = Collections.singletonList(linkedIdsCondition.getIdentifier());
			list = this.retrieveButIdsByMeasurement(ids, measurementIds);
		} else if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		} else if (condition instanceof ResultSortCondition){
			ResultSortCondition resultSortCondition = (ResultSortCondition) condition;
			list = this.retrieveButIdsByMeasurementAndSort(ids, resultSortCondition.getMeasurementId(), resultSortCondition.getResultSort());
		} else {
			Log.errorMessage("ResultDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}

}
