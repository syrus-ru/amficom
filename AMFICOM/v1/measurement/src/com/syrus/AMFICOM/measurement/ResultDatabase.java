/*
 * $Id: ResultDatabase.java,v 1.114 2005/11/30 14:55:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.114 $, $Date: 2005/11/30 14:55:26 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class ResultDatabase extends StorableObjectDatabase<Result> {
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
					+ ResultWrapper.COLUMN_MODELING_ID + COMMA
					+ ResultWrapper.COLUMN_SORT;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Result storableObject) throws IllegalDataException {
		final StringBuffer buffer = new StringBuffer();
		final int resultSort = storableObject.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				buffer.append(DatabaseIdentifier.toSQLString(storableObject.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(storableObject.getAction().getId()));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(VOID_IDENTIFIER));
				buffer.append(COMMA);
				buffer.append(DatabaseIdentifier.toSQLString(storableObject.getAction().getId()));
				buffer.append(COMMA);
				break;
			default:
				Log.errorMessage("Illegal sort: " + resultSort + " of result '" + storableObject.getId().getIdentifierString() + "'");
		}
		buffer.append(Integer.toString(resultSort));
		return buffer.toString();
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Result storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final int resultSort = storableObject.getSort().value();
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:					
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAction().getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				break;
			case ResultSort._RESULT_SORT_MODELING:
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);					
				DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAction().getId());
				break;
			default:
				Log.errorMessage("Illegal sort: " + resultSort + " of result '" + storableObject.getId().getIdentifierString() + "'");
		}
		preparedStatement.setInt(++startParameterNumber, storableObject.getSort().value());
		return startParameterNumber;
	}

	@Override
	protected Result updateEntityFromResultSet(final Result storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Result result = (storableObject == null)
				? new Result(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						0,
						null)
					: storableObject;
		final int resultSort = resultSet.getInt(ResultWrapper.COLUMN_SORT);
		Action action = null;
		switch (resultSort) {
			case ResultSort._RESULT_SORT_MEASUREMENT:
				try {
					final Identifier actionId = DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.COLUMN_MEASUREMENT_ID);
					action = (Measurement) StorableObjectPool.getStorableObject(actionId, true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				break;
			case ResultSort._RESULT_SORT_ANALYSIS:
				try {
					final Identifier actionId = DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.COLUMN_ANALYSIS_ID);
					action = (Analysis) StorableObjectPool.getStorableObject(actionId, true);
				} catch (Exception e) {
					throw new RetrieveObjectException(e);
				}
				break;
			case ResultSort._RESULT_SORT_MODELING:
				try {
					final Identifier actionId = DatabaseIdentifier.getIdentifier(resultSet, ResultWrapper.COLUMN_MODELING_ID);
					action = (Modeling) StorableObjectPool.getStorableObject(actionId, true);
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
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				action,
				resultSort);

		return result;
	}

	/**	
	 * @param results
	 * @throws RetrieveObjectException
	 */
	private void retrieveResultParametersByOneQuery(final Set<Result> results) throws RetrieveObjectException {
		if ((results == null) || (results.isEmpty())) {
			return;
		}
		
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_CODE + COMMA
				+ ResultWrapper.LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ ResultWrapper.LINK_COLUMN_RESULT_ID
				+ SQL_FROM + ObjectEntities.RESULTPARAMETER
				+ SQL_WHERE);
		sql.append(idsEnumerationString(results, ResultWrapper.LINK_COLUMN_RESULT_ID, true));

		final Map<Identifier, Set<Parameter>> resultParametersMap = new HashMap<Identifier, Set<Parameter>>();

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			while (resultSet.next()) {
				final ParameterType parameterType = ParameterType.valueOf(resultSet.getInt(StorableObjectWrapper.COLUMN_TYPE_CODE));
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
			final String mesg = "Cannot retrieve parameters for result -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
							statement = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}

		for (final Result result : results) {
			final Identifier resultId = result.getId();
			final Set<Parameter> resultParameters = resultParametersMap.get(resultId);

			if (resultParameters != null) {
				result.setParameters0(resultParameters.toArray(new Parameter[resultParameters.size()]));
			} else {
				result.setParameters0(new Parameter[0]);
			}
		}

	}

	@Override
	protected void insert(final Set<Result> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);

		for (final Result result : storableObjects) {
			this.insertResultParameters(result);
		}
	}

	private void insertResultParameters(final Result result) throws CreateObjectException {
		final Identifier resultId = result.getId();
		final Parameter[] setParameters = result.getParameters();
		final String sql = SQL_INSERT_INTO + ObjectEntities.RESULTPARAMETER + OPEN_BRACKET
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_CODE + COMMA
				+ ResultWrapper.LINK_COLUMN_RESULT_ID + COMMA
				+ ResultWrapper.LINK_COLUMN_PARAMETER_VALUE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION+ COMMA
				+ SQL_FUNCTION_EMPTY_BLOB
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier parameterId = null;
		ParameterType parameterType = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (final Parameter parameter : setParameters) {
				parameterId = parameter.getId();
				parameterType = parameter.getType();

				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				preparedStatement.setInt(2, parameterType.getCode());
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, resultId);

				Log.debugMessage("Inserting parameter " + parameterType.getDescription() + " for result " + resultId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				ByteArrayDatabase.saveAsBlob(parameter.getValue(),
						connection,
						ObjectEntities.RESULTPARAMETER,
						ResultWrapper.LINK_COLUMN_PARAMETER_VALUE,
						StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
			}
			connection.commit();
		} catch (SQLException sqle) {
			final String mesg = "Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterType.getDescription() + "' for result '" + resultId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	protected Set<Result> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Result> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveResultParametersByOneQuery(objects);
		return objects;
	}

	public int retrieveNumberOf(final Identifier testId, final ResultSort resultSort) throws RetrieveObjectException {
		assert testId != null : ErrorMessages.NON_NULL_EXPECTED;
		assert testId.getMajor() == ObjectEntities.TEST_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;
		assert resultSort != null : ErrorMessages.NON_NULL_EXPECTED;

		final String testIdStr = DatabaseIdentifier.toSQLString(testId);
		final String sql = SQL_SELECT + SQL_COUNT + " count "
				+ SQL_FROM + this.getEntityName()
				+ SQL_WHERE + ResultWrapper.COLUMN_SORT + EQUALS + Integer.toString(resultSort.value())
						+ SQL_AND
								+ ResultWrapper.COLUMN_MEASUREMENT_ID + SQL_IN + OPEN_BRACKET
										+ SQL_SELECT + StorableObjectWrapper.COLUMN_ID
										+ SQL_FROM + ObjectEntities.MEASUREMENT
										+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
								+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				return resultSet.getInt("count");
			}
			Log.errorMessage("ERROR: cannot select number of results for test '" + testIdStr
					+ "' of result sort " + resultSort.value() + "; returning 0");
			return 0;
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve number of results for test '" + testIdStr
				+ "' of result sort " + resultSort.value() + " -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
							statement = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

}
