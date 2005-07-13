/*
 * $Id: ResultDatabase.java,v 1.94 2005/07/13 09:27:58 arseniy Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.94 $, $Date: 2005/07/13 09:27:58 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class ResultDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.RESULT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = ResultWrapper.COLUMN_MEASUREMENT_ID + COMMA
					+ ResultWrapper.COLUMN_ANALYSIS_ID + COMMA
					+ ResultWrapper.COLUMN_EVALUATION_ID + COMMA
					+ ResultWrapper.COLUMN_MODELING_ID + COMMA
					+ ResultWrapper.COLUMN_SORT;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			StringBuffer buffer = new StringBuffer(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			updateMultipleSQLValues = buffer.toString();
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Result result = this.fromStorableObject(storableObject);
		final StringBuffer buffer = new StringBuffer();
		final int resultSort = result.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(result.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(Identifier.VOID_IDENTIFIER));
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

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Result result = this.fromStorableObject(storableObject);
		final int resultSort = result.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:					
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, result.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, result.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, result.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.VOID_IDENTIFIER);					
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, result.getAction().getId());
				break;
			default:
				Log.errorMessage("ResultDatabase.insertResult | Illegal sort: " + resultSort
						+ " of result '" + result.getId().getIdentifierString() + "'");
		}
		preparedStatement.setInt(++startParameterNumber, result.getSort().value());
		return startParameterNumber;
	}

	private Result fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Result)
			return (Result) storableObject;
		throw new IllegalDataException("ResultDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Result result = this.fromStorableObject(storableObject);
		this.retrieveEntity(result);
		this.retrieveResultParametersByOneQuery(Collections.singleton(result));
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Result result = (storableObject == null)
				? new Result(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
									null,
									0L,
									null,
									0,
									null)
				: this.fromStorableObject(storableObject);
		final int resultSort = resultSet.getInt(ResultWrapper.COLUMN_SORT);
		Action action = null;
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				try {
					action = (Measurement) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							ResultWrapper.COLUMN_MEASUREMENT_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				try {
					action = (Analysis) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							ResultWrapper.COLUMN_ANALYSIS_ID), true);
				} catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_EVALUATION:
				try {
					action = (Evaluation) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							ResultWrapper.COLUMN_EVALUATION_ID), true);
				} catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_MODELING:
				try {
					action = (Modeling) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							ResultWrapper.COLUMN_MODELING_ID), true);
				} catch (Exception e) {
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

	/**	
	 * @param results
	 * @throws RetrieveObjectException
	 */
	private void retrieveResultParametersByOneQuery(final Set<Result> results) throws RetrieveObjectException {
		if ((results == null) || (results.isEmpty()))
			return;		
		
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ ResultWrapper.LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ ResultWrapper.LINK_COLUMN_RESULT_ID
				+ SQL_FROM + ObjectEntities.RESULTPARAMETER
				+ SQL_WHERE);
		sql.append(idsEnumerationString(results, ResultWrapper.LINK_COLUMN_RESULT_ID, true));

		final Map<Identifier, Set<Parameter>> resultParametersMap = new HashMap<Identifier, Set<Parameter>>();

		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ResultDatabase.retrieveResultParametersByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			while (resultSet.next()) {
				ParameterType parameterType = null;
				try {
					parameterType = (ParameterType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							StorableObjectWrapper.COLUMN_TYPE_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				final Parameter parameter = new Parameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
														parameterType,
														ByteArrayDatabase.toByteArray(resultSet.getBlob(ResultWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				final Identifier resultId = DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.LINK_COLUMN_RESULT_ID);
				Set<Parameter> resultParameters = resultParametersMap.get(resultId);
				if (resultParameters == null) {
					resultParameters = new HashSet<Parameter>();
					resultParametersMap.put(resultId, resultParameters);
				}
				resultParameters.add(parameter);
			}
		} catch (SQLException sqle) {
			String mesg = "ResultDatabase.retrieveResultParametersByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

		for (final Result result : results) {
			final Identifier resultId = result.getId();
			final Set<Parameter> resultParameters = resultParametersMap.get(resultId);

			if (resultParameters != null)
				result.setParameters0(resultParameters.toArray(new Parameter[resultParameters.size()]));
			else
				result.setParameters0(new Parameter[0]);
		}

	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final Result result = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  result.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		final Result result = this.fromStorableObject(storableObject);
		try {
			super.insertEntity(result);
			this.insertResultParameters(result);
		} catch (CreateObjectException e) {
			this.delete(result);
			throw e;
		}
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);

		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Result result = this.fromStorableObject((StorableObject) it.next());
			this.insertResultParameters(result);
		}

	}

	private void insertResultParameters(final Result result) throws CreateObjectException {
		final Identifier resultId = result.getId();
		final Parameter[] setParameters = result.getParameters();
		final String sql = SQL_INSERT_INTO
				+ ObjectEntities.RESULTPARAMETER + OPEN_BRACKET
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
		final Connection connection = DatabaseConnection.getConnection();
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
						ObjectEntities.RESULTPARAMETER,
						ResultWrapper.LINK_COLUMN_PARAMETER_VALUE,
						StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		} catch (SQLException sqle) {
			final String mesg = "ResultDatabase.insertResultParameters | Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterTypeId.toString() + "' for result '" + resultId + "' -- " + sqle.getMessage();
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

	@Override
	public void delete(final Identifier id) {
		assert (id.getMajor() != ObjectEntities.RESULT_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		final String resultIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM + ObjectEntities.RESULTPARAMETER
					+ SQL_WHERE + ResultWrapper.LINK_COLUMN_RESULT_ID + EQUALS + resultIdStr);
			statement.executeUpdate(SQL_DELETE_FROM + ObjectEntities.RESULT
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + resultIdStr);
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

	@Override
	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set objects = super.retrieveByCondition(conditionQuery);
		this.retrieveResultParametersByOneQuery(objects);
		return objects;
	}

}
