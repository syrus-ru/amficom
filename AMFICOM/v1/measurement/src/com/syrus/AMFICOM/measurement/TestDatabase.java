/*
 * $Id: TestDatabase.java,v 1.23 2004/08/20 13:16:41 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;

/**
 * @version $Revision: 1.23 $, $Date: 2004/08/20 13:16:41 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class TestDatabase extends StorableObjectDatabase {
	public static final String COLUMN_ANALYSIS_TYPE_ID	= "analysis_type_id";
	public static final String COLUMN_DESCRIPTION	= "description";
	public static final String COLUMN_END_TIME	= "end_time";
	public static final String COLUMN_EVALUATION_TYPE_ID	= "evaluation_type_id";
	public static final String COLUMN_MEASUREMENT_TYPE_ID	= "measurement_type_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String COLUMN_RETURN_TYPE	= "return_type";
	public static final String COLUMN_START_TIME	= "start_time";
	public static final String COLUMN_STATUS	= "status";
	public static final String COLUMN_TEMPORAL_PATTERN_ID	= "temporal_pattern_id";
	public static final String COLUMN_TEMPORAL_TYPE	= "temporal_type";
	
	public static final String LINK_COLMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";
	public static final String LINK_COLMN_TEST_ID = "test_id";
	
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private Test fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Test)
			return (Test)storableObject;
		throw new IllegalDataException("TestDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Test test = this.fromStorableObject(storableObject);
		this.retrieveTest(test);
		this.retrieveMeasurementSetupTestLinks(test);
	}

	private void retrieveTest(Test test) throws ObjectNotFoundException, RetrieveObjectException {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TEMPORAL_TYPE + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_START_TIME) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_END_TIME) + COMMA
			+ COLUMN_TEMPORAL_PATTERN_ID + COMMA
			+ COLUMN_MEASUREMENT_TYPE_ID + COMMA			
			+ COLUMN_ANALYSIS_TYPE_ID + COMMA
			+ COLUMN_EVALUATION_TYPE_ID + COMMA
			+ COLUMN_STATUS + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_RETURN_TYPE + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.TEST_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + testIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveTest | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String temportalPatternID = resultSet.getString(COLUMN_TEMPORAL_PATTERN_ID);
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String analysisTypeIdCode = resultSet.getString(COLUMN_ANALYSIS_TYPE_ID);
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String evaluationTypeIdCode = resultSet.getString(COLUMN_EVALUATION_TYPE_ID);
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String monitoredElementIdCode = resultSet.getString(COLUMN_MONITORED_ELEMENT_ID);
				String description = resultSet.getString(COLUMN_DESCRIPTION);
				test.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
													 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
													 /**
													   * @todo when change DB Identifier model ,change getString() to getLong()
													   */
													 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
													 /**
													   * @todo when change DB Identifier model ,change getString() to getLong()
													   */
													 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
													 resultSet.getInt(COLUMN_TEMPORAL_TYPE),
													 DatabaseDate.fromQuerySubString(resultSet, COLUMN_START_TIME),
													 DatabaseDate.fromQuerySubString(resultSet, COLUMN_END_TIME),
													 (temportalPatternID != null) ? (new Identifier(temportalPatternID)) : null,													 
													 /**
													   * @todo when change DB Identifier model ,change getString() to getLong()
													   */
													 (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_MEASUREMENT_TYPE_ID)), true),
													 (analysisTypeIdCode != null) ? (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier(analysisTypeIdCode), true) : null,
													 (evaluationTypeIdCode != null) ? (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(evaluationTypeIdCode), true) : null,
													 resultSet.getInt(COLUMN_STATUS),
													 (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(new Identifier(monitoredElementIdCode), true),
													 resultSet.getInt(COLUMN_RETURN_TYPE),
													 (description != null) ? description: "");
			}
			else
				throw new ObjectNotFoundException("No such test: " + testIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveTest | Cannot retrieve test '" + testIdStr + "' -- " + sqle.getMessage();
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
		}
	}

	private void retrieveMeasurementSetupTestLinks(Test test) throws RetrieveObjectException {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLMN_MEASUREMENT_SETUP_ID
			+ SQL_FROM + ObjectEntities.MSTESTLINK_ENTITY
			+ SQL_WHERE + LINK_COLMN_TEST_ID + EQUALS + testIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementSetupTestLinks | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				  * @todo when change DB Identifier model ,change getString() to getLong()
				  */
				arraylist.add(new Identifier(resultSet.getString(LINK_COLMN_MEASUREMENT_SETUP_ID)));
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
			}
		}
		test.setMeasurementSetupIds(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Test test = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Test.RETRIEVE_MEASUREMENTS:
				return this.retrieveMeasurementsOrderByStartTime(test, (MeasurementStatus)arg);
			case Test.RETRIEVE_LAST_MEASUREMENT:
				return this.retrieveLastMeasurement(test);
			default:
				return null;
		}
	}

	private List retrieveMeasurementsOrderByStartTime(Test test, MeasurementStatus measurementStatus) throws RetrieveObjectException {
		List measurements = new ArrayList();

		String testIdStr = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
			+ SQL_AND + MeasurementDatabase.COLUMN_STATUS + EQUALS + Integer.toString(measurementStatus.value())
			+ SQL_ORDER_BY + MeasurementDatabase.COLUMN_START_TIME + " ASC";

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				  * @todo when change DB Identifier model ,change getString() to getLong()
				  */
				measurements.add((Measurement)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ID)), true));
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveMeasurementsOrderByStartTime | Cannot retrieve measurements for test '" + testIdStr + "' and status " + Integer.toString(measurementStatus.value()) + " -- " + sqle.getMessage();
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
		}
		((ArrayList)measurements).trimToSize();
		return measurements;
	}
	
	private Measurement retrieveLastMeasurement(Test test) throws RetrieveObjectException, ObjectNotFoundException {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE
			+ MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
			+ SQL_AND + MeasurementDatabase.COLUMN_START_TIME + EQUALS
			+ OPEN_BRACKET
				+ SQL_SELECT
				+ SQL_FUNCTION_MAX + OPEN_BRACKET + MeasurementDatabase.COLUMN_START_TIME + CLOSE_BRACKET
				+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
				+ SQL_WHERE + MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
			+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveLastMeasurement | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return (Measurement)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ID)), true);
			else
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
			}
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Test test = this.fromStorableObject(storableObject);
		try {
			this.insertTest(test);
			this.insertMeasurementSetupTestLinks(test);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void insertTest(Test test) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String testIdCode = test.getId().getCode();
		Date startTime = test.getStartTime();
		Date endTime = test.getEndTime();
		Identifier temporalPatternId = test.getTemporalPatternId();		
		AnalysisType analysisType = test.getAnalysisType();
		EvaluationType evaluationType = test.getEvaluationType();
		
		String sql = SQL_INSERT_INTO + ObjectEntities.TEST_ENTITY
			+ OPEN_BRACKET 
			+ COLUMN_ID + COMMA 
			+ COLUMN_CREATED + COMMA 
			+ COLUMN_MODIFIED + COMMA 
			+ COLUMN_CREATOR_ID + COMMA 
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TEMPORAL_TYPE + COMMA
			+ COLUMN_START_TIME + COMMA
			+ COLUMN_END_TIME + COMMA
			+ COLUMN_TEMPORAL_PATTERN_ID + COMMA			
			+ COLUMN_MEASUREMENT_TYPE_ID + COMMA 
			+ COLUMN_ANALYSIS_TYPE_ID + COMMA
			+ COLUMN_EVALUATION_TYPE_ID + COMMA
			+ COLUMN_STATUS + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_RETURN_TYPE + COMMA
			+ COLUMN_DESCRIPTION + CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
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
			+ QUESTION + COMMA			
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(1, testIdCode);
			preparedStatement.setTimestamp(2, new Timestamp(test.getCreated().getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(test.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, test.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, test.getModifierId().getCode());
			preparedStatement.setInt(6, test.getTemporalType().value());
			preparedStatement.setTimestamp(7, (startTime != null)?(new Timestamp(startTime.getTime())):null);
			preparedStatement.setTimestamp(8, (endTime != null)?(new Timestamp(endTime.getTime())):null);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(9, (temporalPatternId != null)?temporalPatternId.getCode():"");
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(10, test.getMeasurementType().getId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(11, (analysisType != null)?analysisType.getId().getCode():"");
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(12, (evaluationType != null)?evaluationType.getId().getCode():"");
			preparedStatement.setInt(13, test.getStatus().value());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(14, test.getMonitoredElement().getId().getCode());
			preparedStatement.setInt(15, test.getReturnType().value());
			preparedStatement.setString(16, test.getDescription());
			Log.debugMessage("TestDatabase.insertTest | Inserting  test " + testIdCode, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.insertTest | Cannot insert test '" + testIdCode + "' -- " + sqle.getMessage();
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

	private void insertMeasurementSetupTestLinks(Test test) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String testIdCode = test.getId().getCode();
		List msIds = test.getMeasurementSetupIds();
		String sql = SQL_INSERT_INTO + ObjectEntities.MSTESTLINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLMN_TEST_ID + COMMA
			+ LINK_COLMN_MEASUREMENT_SETUP_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String msIdCode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = msIds.iterator(); iterator.hasNext();) {
				/**
				  * @todo when change DB Identifier model ,change setString() to setLong()
				  */
				preparedStatement.setString(1, testIdCode);
				msIdCode = ((Identifier)iterator.next()).getCode();
				/**
				  * @todo when change DB Identifier model ,change setString() to setLong()
				  */
				preparedStatement.setString(2, msIdCode);
				Log.debugMessage("TestDatabase.insertMeasurementSetupTestLinks | Inserting link for test " + testIdCode + " and measurement setup " + msIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.insertMeasurementSetupTestLinks | Cannot insert link for measurement setup '" + msIdCode + "' and test '" + testIdCode + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, int updateKind, Object arg) throws IllegalDataException, UpdateObjectException {
		Test test = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Test.UPDATE_STATUS:
				this.updateStatus(test);
				break;
			case Test.UPDATE_MODIFIED:
				this.updateModified(test);
				break;
			default:
				return;
		}
	}

	private void updateStatus(Test test) throws UpdateObjectException {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ COLUMN_STATUS + EQUALS + Integer.toString(test.getStatus().value()) + COMMA
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + test.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS + testIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
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
			}
		}
	}

	private void updateModified(Test test) throws UpdateObjectException {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + test.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS + testIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateModified | Trying: " + sql, Log.DEBUGLEVEL05);
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
			}
		}
	}

	public static List retrieveTests(TestStatus status) throws RetrieveObjectException {
		List tests = new ArrayList();

		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.TEST_ENTITY
			+ SQL_WHERE + COLUMN_STATUS + EQUALS + Integer.toString(status.value())
			+ SQL_ORDER_BY + COLUMN_START_TIME + SQL_ASC;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveTestsForMCM | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				  * @todo when change DB Identifier model, change getString() to getLong()
				  */
				try {
					tests.add(new Test(new Identifier(resultSet.getString(COLUMN_ID))));
				}
				catch (ObjectNotFoundException onfe) {
					Log.errorException(onfe);
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveTestsForMCM | Cannot retrieve test of status " + status.value() + " -- " + sqle.getMessage();
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
		}
		return tests;
	}

	public static List retrieveTestsForMCM(Identifier mcmId, TestStatus status) throws RetrieveObjectException {
		List tests = new ArrayList();

		String mcmIdStr = mcmId.toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.TEST_ENTITY
			+ SQL_WHERE + COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.ME_ENTITY
				+ SQL_WHERE + MonitoredElementDatabase.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
					+ SQL_SELECT
					+ COLUMN_ID
					+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
					+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID + SQL_IN + OPEN_BRACKET
						+ SQL_SELECT
						+ COLUMN_ID
						+ SQL_FROM + ObjectEntities.KIS_ENTITY
						+ SQL_WHERE + KISDatabase.COLUMN_MCM_ID + EQUALS + mcmIdStr
					+ CLOSE_BRACKET
				+ CLOSE_BRACKET
			+ CLOSE_BRACKET
				+ SQL_AND + COLUMN_STATUS + EQUALS + Integer.toString(status.value())
			+ SQL_ORDER_BY + COLUMN_START_TIME + SQL_ASC;
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveTestsForMCM | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				  * @todo when change DB Identifier model, change getString() to getLong()
				  */
				try {
					tests.add(new Test(new Identifier(resultSet.getString(COLUMN_ID))));
				}
				catch (ObjectNotFoundException onfe) {
					Log.errorException(onfe);
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveTestsForMCM | Cannot retrieve test of status " + status.value() + " for mcm '" + mcmIdStr + "' -- " + sqle.getMessage();
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
		}
		return tests;
	}
	
	public static List retrieveAll() throws RetrieveObjectException {
		return retrieveAll(null);
	}
	
	public static List retrieveAll(String condition) throws RetrieveObjectException {
		List tests = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(COLUMN_ID);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.TEST_ENTITY);
			if ((condition!=null)&&(condition.length()>0)){
				buffer.append(SQL_WHERE);
				buffer.append(condition);
			}
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				tests.add(new Test(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveAll | Cannot retrieve test -- " + sqle.getMessage();
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
		}
		return tests;
	}

	public static void delete(Test test) {
		String testIdStr = test.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.TEST_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ testIdStr;
			Log.debugMessage("TestDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}


}
