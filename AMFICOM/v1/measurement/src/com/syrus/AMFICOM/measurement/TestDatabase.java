/*
 * $Id: TestDatabase.java,v 1.137.2.2 2006/02/14 00:26:59 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.general.TableNames.TEST_ANATMPL_LINK;
import static com.syrus.AMFICOM.general.TableNames.TEST_MEASTMPL_LINK;
import static com.syrus.AMFICOM.general.TableNames.TEST_STOP_LINK;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_ANALYSIS_TYPE_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_END_TIME;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_GROUP_TEST_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_MEASUREMENT_TYPE_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_NUMBER_OF_MEASUREMENTS;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_START_TIME;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_STATUS;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_TEMPORAL_PATTERN_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.COLUMN_TEMPORAL_TYPE;
import static com.syrus.AMFICOM.measurement.TestWrapper.LINK_COLUMN_ANALYSIS_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.LINK_COLUMN_MEASUREMENT_TEMPLATE_ID;
import static com.syrus.AMFICOM.measurement.TestWrapper.LINK_COLUMN_TEST_ID;

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

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.measurement.Test.TestStatus;
import com.syrus.AMFICOM.measurement.Test.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.137.2.2 $, $Date: 2006/02/14 00:26:59 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class TestDatabase extends StorableObjectDatabase<Test> {
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
			columns = COLUMN_DESCRIPTION + COMMA
					+ COLUMN_GROUP_TEST_ID + COMMA
					+ COLUMN_MONITORED_ELEMENT_ID + COMMA
					+ COLUMN_STATUS + COMMA
					+ COLUMN_TEMPORAL_TYPE + COMMA
					+ COLUMN_START_TIME + COMMA
					+ COLUMN_END_TIME + COMMA
					+ COLUMN_TEMPORAL_PATTERN_ID + COMMA
					+ COLUMN_MEASUREMENT_TYPE_ID + COMMA
					+ COLUMN_NUMBER_OF_MEASUREMENTS + COMMA
					+ COLUMN_ANALYSIS_TYPE_ID;
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
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getGroupTestId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMonitoredElementId()) + COMMA
				+ Integer.toString(storableObject.getStatus().ordinal()) + COMMA
				+ Integer.toString(storableObject.getTemporalType().ordinal()) + COMMA
				+ DatabaseDate.toUpdateSubString(storableObject.getStartTime()) + COMMA
				+ DatabaseDate.toUpdateSubString(storableObject.getEndTime()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getTemporalPatternId()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementTypeId()) + COMMA
				+ Integer.toString(storableObject.getNumberOfMeasurements()) + COMMA
				+ DatabaseIdentifier.toSQLString(storableObject.getAnalysisTypeId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Test storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getGroupTestId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMonitoredElementId());
		preparedStatement.setInt(++startParameterNumber, storableObject.getStatus().ordinal());
		preparedStatement.setInt(++startParameterNumber, storableObject.getTemporalType().ordinal());
		preparedStatement.setTimestamp(++startParameterNumber, new Timestamp(storableObject.getStartTime().getTime()));
		preparedStatement.setTimestamp(++startParameterNumber, new Timestamp(storableObject.getEndTime().getTime()));
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTemporalPatternId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementTypeId());
		preparedStatement.setInt(++startParameterNumber, storableObject.getNumberOfMeasurements());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getAnalysisTypeId());
		return startParameterNumber;
	}

	@Override
	protected Test updateEntityFromResultSet(final Test storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Test test = (storableObject == null)
				? new Test(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						null,
						null,
						null)
					: storableObject;
		final String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		test.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				(description != null) ? description : "",
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_GROUP_TEST_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MONITORED_ELEMENT_ID),
				TestStatus.valueOf(resultSet.getInt(COLUMN_STATUS)),
				TestTemporalType.valueOf(resultSet.getInt(COLUMN_TEMPORAL_TYPE)),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_START_TIME),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_END_TIME),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TEMPORAL_PATTERN_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_TYPE_ID),
				resultSet.getInt(COLUMN_NUMBER_OF_MEASUREMENTS),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANALYSIS_TYPE_ID));
		return test;
	}

	@Override
	protected String retrieveQuery(final String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(COLUMN_START_TIME, DatabaseDate.toQuerySubString(COLUMN_START_TIME));
		query = query.replaceFirst(COLUMN_END_TIME, DatabaseDate.toQuerySubString(COLUMN_END_TIME));
		return query;
	}

	@Override
	protected Set<Test> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Test> tests = super.retrieveByCondition(conditionQuery);

		this.retrieveLinksByOneQuery(tests);

		return tests;
	}

	private void retrieveLinksByOneQuery(final Set<Test> tests) throws RetrieveObjectException {
		if ((tests == null) || (tests.isEmpty())) {
			return;
		}

		final Map<Identifier, Set<Identifier>> measurementTemplateIdsMap = this.retrieveLinkedEntityIds(tests,
				TEST_MEASTMPL_LINK,
				LINK_COLUMN_TEST_ID,
				LINK_COLUMN_MEASUREMENT_TEMPLATE_ID);

		final Map<Identifier, Set<Identifier>> analysisTemplateIdsMap = this.retrieveLinkedEntityIds(tests,
				TEST_ANATMPL_LINK,
				LINK_COLUMN_TEST_ID,
				LINK_COLUMN_ANALYSIS_TEMPLATE_ID);

		final Map<Identifier, SortedMap<Date, String>> stops = this.retrieveStops(tests);
		
		for (final Test test : tests) {
			final Identifier testId = test.getId();

			final Set<Identifier> measurementTemplateIds = measurementTemplateIdsMap.get(testId);
			test.setMeasurementTemplateIds0(measurementTemplateIds);

			final Set<Identifier> analysisTemplateIds = analysisTemplateIdsMap.get(testId);
			test.setAnalysisTemplateIds0(analysisTemplateIds);
			
			final SortedMap<Date, String> stopMap = stops.get(testId);
			test.setStopMap0(stopMap);

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
			Log.debugMessage("Trying: " + sql, Log.DEBUGLEVEL09);
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
				Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	protected void insert(final Set<Test> tests) throws IllegalDataException, CreateObjectException {
		super.insert(tests);

		final Map<Identifier, Set<Identifier>> measurementTemplateIdsMap = this.createMeasurementTemplateIdsMap(tests);
		super.insertLinkedEntityIds(measurementTemplateIdsMap,
				TEST_MEASTMPL_LINK,
				LINK_COLUMN_TEST_ID,
				LINK_COLUMN_MEASUREMENT_TEMPLATE_ID);		

		final Map<Identifier, Set<Identifier>> analysisTemplateIdsMap = this.createAnalysisTemplateIdsMap(tests);
		super.insertLinkedEntityIds(analysisTemplateIdsMap,
				TEST_ANATMPL_LINK,
				LINK_COLUMN_TEST_ID,
				LINK_COLUMN_ANALYSIS_TEMPLATE_ID);		

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
					Log.debugMessage("Inserting stop  '" + reason + "' at " + stopTime + " for '" + id + "'", Log.DEBUGLEVEL09);
					preparedStatement.executeUpdate();
				}
				connection.commit();
			}
		} catch (SQLException sqle) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException sqle1) {
					Log.errorMessage(sqle1);
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
				Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	protected void update(final Set<Test> tests) throws UpdateObjectException {
		super.update(tests);

		final Map<Identifier, Set<Identifier>> measurementTemplateIdsMap = this.createMeasurementTemplateIdsMap(tests);
		super.updateLinkedEntityIds(measurementTemplateIdsMap,
				TEST_MEASTMPL_LINK,
				LINK_COLUMN_TEST_ID,
				LINK_COLUMN_MEASUREMENT_TEMPLATE_ID);		

		final Map<Identifier, Set<Identifier>> analysisTemplateIdsMap = this.createAnalysisTemplateIdsMap(tests);
		super.updateLinkedEntityIds(analysisTemplateIdsMap,
				TEST_ANATMPL_LINK,
				LINK_COLUMN_TEST_ID,
				LINK_COLUMN_ANALYSIS_TEMPLATE_ID);		

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

	private Map<Identifier, Set<Identifier>> createMeasurementTemplateIdsMap(final Set<Test> tests) {
		final Map<Identifier, Set<Identifier>> measurementTemplateIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Test test : tests) {
			measurementTemplateIdsMap.put(test.getId(), test.getMeasurementTemplateIds());
		}
		return measurementTemplateIdsMap;
	}

	private Map<Identifier, Set<Identifier>> createAnalysisTemplateIdsMap(final Set<Test> tests) {
		final Map<Identifier, Set<Identifier>> analysisTemplateIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Test test : tests) {
			analysisTemplateIdsMap.put(test.getId(), test.getAnalysisTemplateIds());
		}
		return analysisTemplateIdsMap;
	}

	private Map<Identifier, SortedMap<Date, String>> createStopsMap(final Set<Test> tests) {
		final Map<Identifier, SortedMap<Date, String>> stopsMap = new HashMap<Identifier, SortedMap<Date, String>>();
		for (final Test test : tests) {
			stopsMap.put(test.getId(), test.getStopMap());
		}
		return stopsMap;
	}

}
