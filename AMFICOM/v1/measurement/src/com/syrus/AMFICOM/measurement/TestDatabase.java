/*
 * $Id: TestDatabase.java,v 1.113 2005/08/20 19:25:23 arseniy Exp $
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
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.measurement.corba.IdlMeasurementPackage.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.113 $, $Date: 2005/08/20 19:25:23 $
 * @author $Author: arseniy $
 * @module measurement
 */

public final class TestDatabase extends StorableObjectDatabase<Test> {
	public static final String LINK_COLMN_TEST_ID = "test_id";

	private static String columns;
	private static String updateMultipleSQLValues;	

	@Override
	protected short getEntityCode() {
		return ObjectEntities.TEST_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = TestWrapper.COLUMN_TEMPORAL_TYPE + COMMA
				+ TestWrapper.COLUMN_START_TIME + COMMA
				+ TestWrapper.COLUMN_END_TIME + COMMA
				+ TestWrapper.COLUMN_TEMPORAL_PATTERN_ID + COMMA
				+ TestWrapper.COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ TestWrapper.COLUMN_ANALYSIS_TYPE_ID + COMMA
				+ TestWrapper.COLUMN_EVALUATION_TYPE_ID + COMMA
				+ TestWrapper.COLUMN_GROUP_TEST_ID + COMMA
				+ TestWrapper.COLUMN_STATUS + COMMA
				+ TestWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ TestWrapper.COLUMN_NUMBER_OF_MEASUREMENTS;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues =  QUESTION + COMMA
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

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Test storableObject) throws IllegalDataException {
		final Test test = storableObject;
		final Date startTime = test.getStartTime();
		final Date endTime = test.getEndTime();
		return test.getTemporalType().value() + COMMA
			+ ((startTime != null) ? DatabaseDate.toUpdateSubString(startTime) : SQL_NULL ) + COMMA
			+ ((endTime != null) ? DatabaseDate.toUpdateSubString(endTime) : SQL_NULL ) + COMMA
			+ DatabaseIdentifier.toSQLString(test.getTemporalPatternId()) + COMMA
			+ DatabaseIdentifier.toSQLString(test.getMeasurementTypeId()) + COMMA
			+ DatabaseIdentifier.toSQLString(test.getAnalysisTypeId()) + COMMA			
			+ DatabaseIdentifier.toSQLString(test.getEvaluationTypeId()) + COMMA
			+ DatabaseIdentifier.toSQLString(test.getGroupTestId()) + COMMA
			+ test.getStatus().value() + COMMA
			+ DatabaseIdentifier.toSQLString(test.getMonitoredElement().getId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(test.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
			+ test.getNumberOfMeasurements();
	}

	@Override
	protected String retrieveQuery(final String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(TestWrapper.COLUMN_START_TIME, DatabaseDate.toQuerySubString(TestWrapper.COLUMN_START_TIME));
		query = query.replaceFirst(TestWrapper.COLUMN_END_TIME, DatabaseDate.toQuerySubString(TestWrapper.COLUMN_END_TIME));
		return query;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Test storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {

		final Date startTime = storableObject.getStartTime();
		final Date endTime = storableObject.getEndTime();
		preparedStatement.setInt(++startParameterNumber, storableObject.getTemporalType().value());
		preparedStatement.setTimestamp(++startParameterNumber, (startTime != null) ? (new Timestamp(startTime.getTime())) : null);
		preparedStatement.setTimestamp(++startParameterNumber, (endTime != null) ? (new Timestamp(endTime.getTime())) : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTemporalPatternId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAnalysisTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getEvaluationTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getGroupTestId());
		preparedStatement.setInt(++startParameterNumber, storableObject.getStatus().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMonitoredElement().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setInt(++startParameterNumber, storableObject.getNumberOfMeasurements());
		return startParameterNumber;
	}

	@Override
	protected Test updateEntityFromResultSet(final Test storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Test test = (storableObject == null) ? new Test(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID),
				null,
				StorableObjectVersion.ILLEGAL_VERSION,
				null,
				null,
				null,
				TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME,
				null,
				null,
				null,
				null,
				null,
				null,
				null) : storableObject;

		MonitoredElement monitoredElement;
		try {			
			final Identifier monitoredElementId = DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
			monitoredElement = (MonitoredElement) StorableObjectPool.getStorableObject(monitoredElementId, true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		final String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		test.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				resultSet.getInt(TestWrapper.COLUMN_TEMPORAL_TYPE),
				DatabaseDate.fromQuerySubString(resultSet, TestWrapper.COLUMN_START_TIME),
				DatabaseDate.fromQuerySubString(resultSet, TestWrapper.COLUMN_END_TIME),
				DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_TEMPORAL_PATTERN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_MEASUREMENT_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_ANALYSIS_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_EVALUATION_TYPE_ID),
				DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_GROUP_TEST_ID),
				resultSet.getInt(TestWrapper.COLUMN_STATUS),
				monitoredElement,
				(description != null) ? description : "",
				resultSet.getInt(TestWrapper.COLUMN_NUMBER_OF_MEASUREMENTS));

		return test;
	}

	@Override
	public void retrieve(final Test storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		this.retrieveEntity(storableObject);
		this.retrieveMeasurementSetupTestLinksByOneQuery(Collections.singleton(storableObject));
	}

	private void retrieveMeasurementSetupTestLinksByOneQuery(final Set<Test> tests) throws RetrieveObjectException {
		if ((tests == null) || (tests.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> msIdsMap = this.retrieveLinkedEntityIds(tests,
				ObjectEntities.MSTESTLINK,
				LINK_COLMN_TEST_ID,
				TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);

		for (final Test test : tests) {
			final Identifier testId = test.getId();
			final Set<Identifier> msIds = msIdsMap.get(testId);

			test.setMeasurementSetupIds0(msIds);
		}
	}

	public Set<Measurement> retrieveMeasurementsOrderByStartTime(final Test test, final MeasurementStatus measurementStatus)
			throws RetrieveObjectException {
		final Set<Measurement> measurements = new HashSet<Measurement>();

		final String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		final String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENT
			+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND + MeasurementWrapper.COLUMN_STATUS + EQUALS + Integer.toString(measurementStatus.value())
			+ SQL_ORDER_BY + MeasurementWrapper.COLUMN_START_TIME + " ASC";

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				final Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID);
				final Measurement measurement = (Measurement) StorableObjectPool.getStorableObject(measurementId, true);
				measurements.add(measurement);
			}
		} catch (SQLException sqle) {
			final String mesg = "TestDatabase.retrieveMeasurementsOrderByStartTime | Cannot retrieve measurements for test '" + testIdStr
					+ "' and status " + Integer.toString(measurementStatus.value()) + " -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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
				if (connection != null) {
					DatabaseConnection.releaseConnection(connection);
				}
			}
		}
		return measurements;
	}

	public Measurement retrieveLastMeasurement(final Test test) throws RetrieveObjectException, ObjectNotFoundException {
		final String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		final String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.MEASUREMENT
			+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND + MeasurementWrapper.COLUMN_START_TIME + EQUALS
					+ OPEN_BRACKET
						+ SQL_SELECT
						+ SQL_FUNCTION_MAX + OPEN_BRACKET + MeasurementWrapper.COLUMN_START_TIME + CLOSE_BRACKET
						+ SQL_FROM + ObjectEntities.MEASUREMENT
						+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
					+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveLastMeasurement | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				try {
					return (Measurement) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
							StorableObjectWrapper.COLUMN_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			throw new ObjectNotFoundException("No last measurement for test: " + testIdStr);
		} catch (SQLException sqle) {
			final String mesg = "TestDatabase.retrieveLastMeasurement | Cannot retrieve last measurement for test '"
					+ testIdStr + "' -- " + sqle.getMessage();
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
				Log.errorException(sqle1);
			}
		}
	}

	public int retrieveNumberOfResults(final Test test, final ResultSort resultSort)
			throws RetrieveObjectException,
				ObjectNotFoundException {
		final String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		final String sql = SQL_SELECT
			+ SQL_COUNT + " count "
			+ SQL_FROM + ObjectEntities.RESULT
			+ SQL_WHERE + ResultWrapper.COLUMN_SORT + EQUALS + Integer.toString(resultSort.value())
				+ SQL_AND + ResultWrapper.COLUMN_MEASUREMENT_ID + SQL_IN
					+ OPEN_BRACKET
						+ SQL_SELECT
						+ StorableObjectWrapper.COLUMN_ID
						+ SQL_FROM + ObjectEntities.MEASUREMENT
						+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
					+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveNumberOfResults | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return resultSet.getInt("count");
			throw new ObjectNotFoundException("No number of results for test '" + testIdStr + "' of result sort " + resultSort.value());
		} catch (SQLException sqle) {
			final String mesg = "TestDatabase.retrieveNumberOfMeasurements | Cannot retrieve number of results for test '" + testIdStr
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
				Log.errorException(sqle1);
			}
		}
	}

	@Override
	public void insert(final Set<Test> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			this.updateMeasurementSetupIds(storableObjects);
		} catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	@Override
	public void update(final Set<Test> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateMeasurementSetupIds(storableObjects);
	}

	private void updateMeasurementSetupIds(final Set<Test> tests) throws UpdateObjectException {
		if (tests == null || tests.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> measurementSetupIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Test test : tests) {
			final Set<Identifier> measurementSetupIds = test.getMeasurementSetupIds();
			measurementSetupIdsMap.put(test.getId(), measurementSetupIds);
		}

		super.updateLinkedEntityIds(measurementSetupIdsMap,
				ObjectEntities.MSTESTLINK,
				TestWrapper.LINK_COLUMN_TEST_ID,
				TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);
	}

	@Override
	protected Set<Test> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Test> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveMeasurementSetupTestLinksByOneQuery(objects);
		return objects;
	}

}
