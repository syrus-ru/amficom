/*
 * $Id: TestDatabase.java,v 1.134 2005/10/30 15:20:39 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.TableNames.MEASUREMENTSETUP_TEST_LINK;
import static com.syrus.AMFICOM.general.TableNames.TEST_STOP_LINK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.measurement.corba.IdlTestPackage.IdlTestTimeStampsPackage.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.134 $, $Date: 2005/10/30 15:20:39 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class TestDatabase extends StorableObjectDatabase<Test> {
	private static final String LINK_COLUMN_TEST_ID = "test_id";
	private static final String LINK_COLUMN_STOP_TIME = "stop_time";
	private static final String LINK_COLUMN_STOP_REASON = "stop_reason";

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
				+ TestWrapper.COLUMN_MEASUREMENT_TYPE_CODE + COMMA
				+ TestWrapper.COLUMN_ANALYSIS_TYPE_CODE + COMMA
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
			+ Integer.toString(test.getMeasurementType().getCode()) + COMMA
			+ Integer.toString(test.getAnalysisType().getCode()) + COMMA
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
		preparedStatement.setInt(++startParameterNumber, storableObject.getMeasurementType().getCode());
		preparedStatement.setInt(++startParameterNumber, storableObject.getAnalysisType().getCode());
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
				MeasurementType.valueOf(resultSet.getInt(TestWrapper.COLUMN_MEASUREMENT_TYPE_CODE)),
				AnalysisType.valueOf(resultSet.getInt(TestWrapper.COLUMN_ANALYSIS_TYPE_CODE)),
				DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_GROUP_TEST_ID),
				resultSet.getInt(TestWrapper.COLUMN_STATUS),
				monitoredElement,
				(description != null) ? description : "",
				resultSet.getInt(TestWrapper.COLUMN_NUMBER_OF_MEASUREMENTS));

		return test;
	}
	
	private void retrieveLinksByOneQuery(final Set<Test> tests) throws RetrieveObjectException {
		if ((tests == null) || (tests.isEmpty())) {
			return;
		}

		final Map<Identifier, Set<Identifier>> msIdsMap = this.retrieveLinkedEntityIds(tests,
				MEASUREMENTSETUP_TEST_LINK,
				LINK_COLUMN_TEST_ID,
				TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);

		final Map<Identifier, SortedMap<Date, String>> stops = this.retrieveStops(tests);
		
		for (final Test test : tests) {
			final Identifier testId = test.getId();
			
			final Set<Identifier> msIds = msIdsMap.get(testId);			
			test.setMeasurementSetupIds0(msIds);
			
			final SortedMap<Date, String> stopMap = stops.get(testId);
			test.setStoppingMap0(stopMap);

		}
	}

	private final Map<Identifier, SortedMap<Date, String>> retrieveStops(final Set<? extends Identifiable> identifiables)
			throws RetrieveObjectException {
		if (identifiables == null || identifiables.isEmpty()) {
			return Collections.emptyMap();
		}

		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ LINK_COLUMN_TEST_ID + COMMA
				+ DatabaseDate.toQuerySubString(LINK_COLUMN_STOP_TIME) + COMMA
				+ LINK_COLUMN_STOP_REASON 
				+ SQL_FROM + TEST_STOP_LINK
				+ SQL_WHERE);
		sql.append(idsEnumerationString(identifiables, LINK_COLUMN_TEST_ID, true));

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			assert Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			final Map<Identifier, SortedMap<Date, String>> stopsMap = new HashMap<Identifier, SortedMap<Date, String>>();
			while (resultSet.next()) {
				final Identifier storabeObjectId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_TEST_ID);
				SortedMap<Date, String> testStopsMap = stopsMap.get(storabeObjectId);
				if (testStopsMap == null) {
					testStopsMap = new TreeMap<Date, String>();
					stopsMap.put(storabeObjectId, testStopsMap);
				}
				testStopsMap.put(DatabaseDate.fromQuerySubString(resultSet, LINK_COLUMN_STOP_TIME),
					DatabaseString.fromQuerySubString(resultSet.getString(LINK_COLUMN_STOP_REASON)));
			}

			return stopsMap;
		} catch (SQLException sqle) {
			final String mesg = "Cannot retrieve linked entity identifiers for entity -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				assert Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	public void insert(final Set<Test> tests) throws IllegalDataException, CreateObjectException {
		super.insertEntities(tests);

		final Map<Identifier, Set<Identifier>> measurementSetupIdsMap = this.createMeasurementSetupIdsMap(tests);
		super.insertLinkedEntityIds(measurementSetupIdsMap,
				MEASUREMENTSETUP_TEST_LINK,
				TestWrapper.LINK_COLUMN_TEST_ID,
				TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);		
		
		final Map<Identifier, SortedMap<Date, String>> idsStopsMap = this.createStopsMap(tests);
		this.insertStops(idsStopsMap);
	}

	private final void insertStops(final Map<Identifier, SortedMap<Date, String>> stopsMap) throws CreateObjectException {
		if (stopsMap == null || stopsMap.isEmpty()) {
			return;
		}

		final String sql = SQL_INSERT_INTO + TEST_STOP_LINK + OPEN_BRACKET
				+ LINK_COLUMN_TEST_ID + COMMA
				+ LINK_COLUMN_STOP_TIME + COMMA
				+ LINK_COLUMN_STOP_REASON
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		Identifier id = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (final Iterator<Identifier> idIt = stopsMap.keySet().iterator(); idIt.hasNext();) {
				id = idIt.next();
				final SortedMap<Date, String> stopping = stopsMap.get(id);
				for (final Date stopTime : stopping.keySet()) {
					final String reason = stopping.get(stopTime);
					DatabaseIdentifier.setIdentifier(preparedStatement, 1, id);
					preparedStatement.setTimestamp(2, new Timestamp(stopTime.getTime()));
					DatabaseString.setString(preparedStatement, 3, reason, StorableObjectDatabase.SIZE_DESCRIPTION_COLUMN);
					assert Log.debugMessage("Inserting stop  '" + reason + "' at " + stopTime + " for '" + id + "'", Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
				connection.commit();
			}
		} catch (SQLException sqle) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException sqle1) {
					assert Log.errorMessage(sqle1);
				}
			}
			final String mesg = "Cannot insert stop for '" + id + "' -- " + sqle.getMessage();
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
			} catch (final SQLException sqle1) {
				assert Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	public void update(final Set<Test> tests) throws UpdateObjectException {
		super.update(tests);

		final Map<Identifier, Set<Identifier>> measurementSetupIdsMap = this.createMeasurementSetupIdsMap(tests);
		super.updateLinkedEntityIds(measurementSetupIdsMap,
				MEASUREMENTSETUP_TEST_LINK,
				TestWrapper.LINK_COLUMN_TEST_ID,
				TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);

		this.updateStops(tests);
	}

	private void updateStops(final Set<Test> tests) throws UpdateObjectException {
		if (tests == null || tests.isEmpty()) {
			return;
		}

		final Map<Identifier, SortedMap<Date, String>> stopsMap = this.createStopsMap(tests);

		Map<Identifier, SortedMap<Date, String>> dbStopsMap = null;
		try {
			dbStopsMap = this.retrieveStops(tests);
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, SortedMap<Date, String>> insertStopsMap = new HashMap<Identifier, SortedMap<Date, String>>();
		for (final Identifier id : stopsMap.keySet()) {
			final SortedMap<Date, String> objectStops = stopsMap.get(id);
			final SortedMap<Date, String> dbObjectStops = dbStopsMap.get(id);

			if (dbObjectStops != null) {
				for (final Date stopDate : objectStops.keySet()) {
					if (!dbObjectStops.containsKey(stopDate)) {
						SortedMap<Date, String> aStops = insertStopsMap.get(id);
						if (aStops == null) {
							aStops = new TreeMap<Date, String>();
							insertStopsMap.put(id, aStops);
						}
						aStops.put(stopDate, objectStops.get(stopDate));
					}
				}
			} else {
				insertStopsMap.put(id, objectStops);
			}
		}

		try {
			this.insertStops(insertStopsMap);
		} catch (CreateObjectException coe) {
			throw new UpdateObjectException(coe);
		}
	}

	private Map<Identifier, Set<Identifier>> createMeasurementSetupIdsMap(final Set<Test> tests) {
		final Map<Identifier, Set<Identifier>> measurementSetupIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Test test : tests) {
			measurementSetupIdsMap.put(test.getId(), test.getMeasurementSetupIds());
		}
		return measurementSetupIdsMap;
	}

	private Map<Identifier, SortedMap<Date, String>> createStopsMap(final Set<Test> tests) {
		final Map<Identifier, SortedMap<Date, String>> stopsMap = new HashMap<Identifier, SortedMap<Date, String>>();
		for (final Test test : tests) {
			stopsMap.put(test.getId(), test.getStoppingMap());
		}
		return stopsMap;
	}

	@Override
	protected Set<Test> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Test> tests = super.retrieveByCondition(conditionQuery);
		
		this.retrieveLinksByOneQuery(tests);
		
		return tests;
	}

}
