/*
 * $Id: TestDatabase.java,v 1.87 2005/04/05 15:40:23 arseniy Exp $
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
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.87 $, $Date: 2005/04/05 15:40:23 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class TestDatabase extends StorableObjectDatabase {
	public static final String LINK_COLMN_TEST_ID = "test_id";

	private static String columns;
	private static String updateMultipleSQLValues;	

	protected String getEnityName() {
		return ObjectEntities.TEST_ENTITY;
	}	

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = TestWrapper.COLUMN_TEMPORAL_TYPE + COMMA
				+ TestWrapper.COLUMN_START_TIME + COMMA
				+ TestWrapper.COLUMN_END_TIME + COMMA
				+ TestWrapper.COLUMN_TEMPORAL_PATTERN_ID + COMMA
				+ TestWrapper.COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ TestWrapper.COLUMN_ANALYSIS_TYPE_ID + COMMA
				+ TestWrapper.COLUMN_EVALUATION_TYPE_ID + COMMA
				+ TestWrapper.COLUMN_STATUS + COMMA
				+ TestWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ TestWrapper.COLUMN_RETURN_TYPE + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

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

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Test test = this.fromStorableObject(storableObject);
		Date startTime = test.getStartTime();
		Date endTime = test.getEndTime();
		Identifier temporalPatternId = test.getTemporalPatternId();		
		Identifier analysisTypeId = test.getAnalysisTypeId();
		Identifier evaluationTypeId = test.getEvaluationTypeId();

		return test.getTemporalType().value() + COMMA
			+ ((startTime != null) ? DatabaseDate.toUpdateSubString(startTime) : SQL_NULL ) + COMMA
			+ ((endTime != null) ? DatabaseDate.toUpdateSubString(endTime) : SQL_NULL ) + COMMA
			+ ((temporalPatternId != null) ? DatabaseIdentifier.toSQLString(temporalPatternId) : SQL_NULL) + COMMA
			+ DatabaseIdentifier.toSQLString(test.getMeasurementTypeId()) + COMMA
			+ ((analysisTypeId != null) ? DatabaseIdentifier.toSQLString(analysisTypeId): SQL_NULL) + COMMA			
			+ ((evaluationTypeId != null) ? DatabaseIdentifier.toSQLString(evaluationTypeId) : SQL_NULL) + COMMA
			+ test.getStatus().value() + COMMA
			+ DatabaseIdentifier.toSQLString(test.getMonitoredElement().getId()) + COMMA
			+ test.getReturnType().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(test.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
	}

	protected String retrieveQuery(String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(TestWrapper.COLUMN_START_TIME, DatabaseDate.toQuerySubString(TestWrapper.COLUMN_START_TIME));
		query = query.replaceFirst(TestWrapper.COLUMN_END_TIME, DatabaseDate.toQuerySubString(TestWrapper.COLUMN_END_TIME));
		return query;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {

		Test test = this.fromStorableObject(storableObject);
		Date startTime = test.getStartTime();
		Date endTime = test.getEndTime();
		Identifier temporalPatternId = test.getTemporalPatternId();		
		Identifier analysisTypeId = test.getAnalysisTypeId();
		Identifier evaluationTypeId = test.getEvaluationTypeId();
		preparedStatement.setInt(++startParameterNumber, test.getTemporalType().value());
		preparedStatement.setTimestamp(++startParameterNumber, (startTime != null) ? (new Timestamp(startTime.getTime())) : null);
		preparedStatement.setTimestamp(++startParameterNumber, (endTime != null) ? (new Timestamp(endTime.getTime())) : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (temporalPatternId != null) ? temporalPatternId : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, test.getMeasurementTypeId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (analysisTypeId != null) ? analysisTypeId : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (evaluationTypeId != null) ? evaluationTypeId : null);
		preparedStatement.setInt(++startParameterNumber, test.getStatus().value());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, test.getMonitoredElement().getId());
		preparedStatement.setInt(++startParameterNumber, test.getReturnType().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, test.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	private Test fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Test)
			return (Test)storableObject;
		throw new IllegalDataException("TestDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Test test = this.fromStorableObject(storableObject);
		this.retrieveEntity(test);
		this.retrieveMeasurementSetupTestLinks(test);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Test test = (storableObject == null)?
				new Test(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME, 
						 null, null, null, null, 0, null, null) :
					this.fromStorableObject(storableObject);

		MonitoredElement monitoredElement;
		try {			
			Identifier monitoredElementId = DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_MONITORED_ELEMENT_ID);
			monitoredElement = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(monitoredElementId, true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		test.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
						   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
						   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
						   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
						   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
						   resultSet.getInt(TestWrapper.COLUMN_TEMPORAL_TYPE),
						   DatabaseDate.fromQuerySubString(resultSet, TestWrapper.COLUMN_START_TIME),
						   DatabaseDate.fromQuerySubString(resultSet, TestWrapper.COLUMN_END_TIME),
						   DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_TEMPORAL_PATTERN_ID),
						   DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_MEASUREMENT_TYPE_ID),
						   DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_ANALYSIS_TYPE_ID),
						   DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.COLUMN_EVALUATION_TYPE_ID),
						   resultSet.getInt(TestWrapper.COLUMN_STATUS),
						   monitoredElement,
						   resultSet.getInt(TestWrapper.COLUMN_RETURN_TYPE),
						   (description != null) ? description: "");

		return test;
	}

	private void retrieveMeasurementSetupTestLinks(Test test) throws RetrieveObjectException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID
			+ SQL_FROM + ObjectEntities.MSTESTLINK_ENTITY
			+ SQL_WHERE + LINK_COLMN_TEST_ID + EQUALS + testIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		java.util.Set msList = new HashSet();
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementSetupTestLinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				msList.add(DatabaseIdentifier.getIdentifier(resultSet, TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID));
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveMeasurementSetupTestLinks | Cannot retrieve measurement setup ids for test '" + testIdStr + "' -- " + sqle.getMessage();
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
			}  finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		if (!msList.isEmpty())
			test.setMeasurementSetupIds0(msList);
		else 
			throw new RetrieveObjectException("TestDatabase.retrieveMeasurementSetupTestLinks | Measurement setup ids for test '" + testIdStr + "' not found.");
	}

	private void retrieveMeasurementSetupTestLinksByOneQuery(java.util.Set tests) throws RetrieveObjectException {
		if ((tests == null) || (tests.isEmpty()))
			return;

		Map msIdsMap = null;
		try {
			msIdsMap = this.retrieveLinkedEntityIds(tests,
					ObjectEntities.MSTESTLINK_ENTITY,
					LINK_COLMN_TEST_ID,
					TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Test test;
		Identifier testId;
		java.util.Set msIds;
		for (Iterator it = tests.iterator(); it.hasNext();) {
			test = (Test) it.next();
			testId = test.getId();
			msIds = (java.util.Set) msIdsMap.get(testId);

			test.setMeasurementSetupIds0(msIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Test test = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Test.RETRIEVE_MEASUREMENTS:
				return this.retrieveMeasurementsOrderByStartTime(test, (MeasurementStatus)arg);
			case Test.RETRIEVE_LAST_MEASUREMENT:
				return this.retrieveLastMeasurement(test);
			case Test.RETRIEVE_NUMBER_OF_MEASUREMENTS:
				return this.retrieveNumberOfMeasurements(test);
			case Test.RETRIEVE_NUMBER_OF_RESULTS:
				return this.retrieveNumberOfResults(test, (ResultSort)arg);
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  test.getId() + "'; argument: " + arg);
				return null;
		}
	}

	private java.util.Set retrieveMeasurementsOrderByStartTime(Test test, MeasurementStatus measurementStatus) throws RetrieveObjectException {
		java.util.Set measurements = new HashSet();

		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND + MeasurementWrapper.COLUMN_STATUS + EQUALS + Integer.toString(measurementStatus.value())
			+ SQL_ORDER_BY + MeasurementWrapper.COLUMN_START_TIME + " ASC";

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				measurements.add(MeasurementStorableObjectPool.getStorableObject(
								DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), true));
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveMeasurementsOrderByStartTime | Cannot retrieve measurements for test '" + testIdStr + "' and status " + Integer.toString(measurementStatus.value()) + " -- " + sqle.getMessage();
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
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		return measurements;
	}

	private Measurement retrieveLastMeasurement(Test test) throws RetrieveObjectException, ObjectNotFoundException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND + MeasurementWrapper.COLUMN_START_TIME + EQUALS
					+ OPEN_BRACKET
						+ SQL_SELECT
						+ SQL_FUNCTION_MAX + OPEN_BRACKET + MeasurementWrapper.COLUMN_START_TIME + CLOSE_BRACKET
						+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
						+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
					+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveLastMeasurement | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				try {
					return (Measurement)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			throw new ObjectNotFoundException("No last measurement for test: " + testIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveLastMeasurement | Cannot retrieve last measurement for test '" + testIdStr + "' -- " + sqle.getMessage();
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
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private Integer retrieveNumberOfMeasurements(Test test) throws RetrieveObjectException, ObjectNotFoundException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ SQL_COUNT + " count "
			+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveNumberOfMeasurements | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new Integer(resultSet.getInt("count"));
			throw new ObjectNotFoundException("No number of measurements for test '" + testIdStr + "'");
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveNumberOfMeasurements | Cannot retrieve number of measurements for test '" + testIdStr + "' -- " + sqle.getMessage();
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
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private Integer retrieveNumberOfResults(Test test, ResultSort resultSort) throws RetrieveObjectException, ObjectNotFoundException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ SQL_COUNT + " count "
			+ SQL_FROM + ObjectEntities.RESULT_ENTITY
			+ SQL_WHERE + ResultWrapper.COLUMN_SORT + EQUALS + Integer.toString(resultSort.value())
				+ SQL_AND + ResultWrapper.COLUMN_MEASUREMENT_ID + SQL_IN
					+ OPEN_BRACKET
						+ SQL_SELECT
						+ StorableObjectWrapper.COLUMN_ID
						+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
						+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
					+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveNumberOfResults | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new Integer(resultSet.getInt("count"));
			throw new ObjectNotFoundException("No number of results for test '" + testIdStr + "' of result sort " + resultSort.value());
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveNumberOfMeasurements | Cannot retrieve number of results for test '" + testIdStr + "' of result sort " + resultSort.value() + " -- " + sqle.getMessage();
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
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Test test = this.fromStorableObject(storableObject);
		this.insertEntity(test);
		try {
			this.updateMeasurementSetupIds(Collections.singleton(test));
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
		
	}

	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;

		if (storableObjects.size() == 1) {
			this.insert((StorableObject) storableObjects.iterator().next());
			return;
		}

		this.insertEntities(storableObjects);
		try {
			this.updateMeasurementSetupIds(storableObjects);
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			this.updateMeasurementSetupIds(Collections.singleton(storableObject));
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	public void update(java.util.Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		try {
			this.updateMeasurementSetupIds(storableObjects);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
	}

	private void updateMeasurementSetupIds(java.util.Set tests) throws IllegalDataException, UpdateObjectException {
		if (tests == null || tests.isEmpty())
			return;

		Map measurementSetupIdsMap = new HashMap();
		Test test;
		java.util.Set measurementSetupIds;
		for (Iterator it = tests.iterator(); it.hasNext();) {
			test = this.fromStorableObject((StorableObject) it.next());
			measurementSetupIds = test.getMeasurementSetupIds();
			measurementSetupIdsMap.put(test.getId(), measurementSetupIds);
		}

		this.updateLinkedEntityIds(measurementSetupIdsMap,
				ObjectEntities.MSTESTLINK_ENTITY,
				TestWrapper.LINK_COLUMN_TEST_ID,
				TestWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);
	}
	/*
	private void updateStatus(Test test) throws UpdateObjectException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ TestWrapper.COLUMN_STATUS + EQUALS + Integer.toString(test.getStatus().value()) + COMMA
			+ StorableObjectWrapper.COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(test.getModifierId())
			+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + testIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.updateStatus | Cannot update status of test '" + testIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void updateModified(Test test) throws UpdateObjectException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ StorableObjectWrapper.COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(test.getModifierId())
			+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + testIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.updateModified | Cannot update modified of test '" + testIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
*/
//	public java.util.Set retrieveTests(TestStatus status) throws RetrieveObjectException {
//		java.util.Set objects = null;
//		try{
//			objects = this.retrieveByIdsByCondition(null, TestWrapper.COLUMN_STATUS + EQUALS + Integer.toString(status.value())
//									+ SQL_ORDER_BY + TestWrapper.COLUMN_START_TIME + SQL_ASC);
//		}
//		catch(IllegalDataException ide) {
//			Log.debugMessage("TestDatabase.retrieveTests | Trying: " + ide, Log.DEBUGLEVEL09);
//		}
//		return objects;
//	}

//	public java.util.Set retrieveTestsForMCM(Identifier mcmId, TestStatus status) throws RetrieveObjectException {
//		
//		String mcmIdStr = DatabaseIdentifier.toSQLString(mcmId);
//		String condition = TestWrapper.COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
//				+ SQL_SELECT
//				+ StorableObjectWrapper.COLUMN_ID
//				+ SQL_FROM + ObjectEntities.ME_ENTITY
//				+ SQL_WHERE + MonitoredElementWrapper.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
//					+ SQL_SELECT
//					+ StorableObjectWrapper.COLUMN_ID
//					+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
//					+ SQL_WHERE + MeasurementPortWrapper.COLUMN_KIS_ID + SQL_IN + OPEN_BRACKET
//						+ SQL_SELECT
//						+ StorableObjectWrapper.COLUMN_ID
//						+ SQL_FROM + ObjectEntities.KIS_ENTITY
//						+ SQL_WHERE + KISWrapper.COLUMN_MCM_ID + EQUALS + mcmIdStr
//					+ CLOSE_BRACKET
//				+ CLOSE_BRACKET
//			+ CLOSE_BRACKET
//				+ SQL_AND + TestWrapper.COLUMN_STATUS + EQUALS + Integer.toString(status.value())
//			+ SQL_ORDER_BY + TestWrapper.COLUMN_START_TIME + SQL_ASC;		
//
//		java.util.Set objects = null;
//		
//		try {
//			objects = this.retrieveByIdsByCondition(null, condition);
//		}
//		catch (IllegalDataException ide) {			
//			Log.debugMessage("TestDatabase.retrieveTestsForMCM | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
//		}
//		
//		return objects;
//	}

	protected java.util.Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		java.util.Set collection = super.retrieveByCondition(conditionQuery);
		this.retrieveMeasurementSetupTestLinksByOneQuery(collection);
		return collection;
	}

	public void delete(Identifier id) throws IllegalDataException {
		throw new UnsupportedOperationException("Deleting tests is incorrect -- test '" + id + "'");
	}
	
	public void delete(java.util.Set ids) {
		throw new UnsupportedOperationException("Deleting tests is incorrect -- collection of " + ids.size() + "' tests");
	}
}
