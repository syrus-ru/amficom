package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.configuration.MonitoredElement;

public class TestDatabase extends StorableObjectDatabase {
//	 analysisTypeId VARCHAR2(32),
	public static final String COLUMN_ANALYSIS_TYPE_ID	= "analysisTypeId";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION	= "description";
	// endTime DATE,
	public static final String COLUMN_END_TIME	= "endTime";
	// evaluationTypeId VARCHAR2(32),
	public static final String COLUMN_EVALUATION_TYPE_ID	= "evaluationTypeId";
	// measurementTypeId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MEASUREMENT_TYPE_ID	= "measurementTypeId";
	// monitoredElementId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MONITORED_ELEMENT_ID	= "monitoredElementId";
	// returnType NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_RETURN_TYPE	= "returnType";
	// startTime DATE,
	public static final String COLUMN_START_TIME	= "startTime";
	// status NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_STATUS	= "status";
	// temporalPatternId VARCHAR2(32),
	public static final String COLUMN_TEMPORAL_PATTERN_ID	= "temporalPatternId";
	// temporalType NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_TEMPORAL_TYPE	= "temporalType";
	
	public static final String LINK_COLMN_MEASUREMENT_SETUP_ID = "measurementSetupId";
	public static final String LINK_COLMN_TEST_ID = "testId";

	
	private Test fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Test)
			return (Test)storableObject;
		else
			throw new Exception("TestDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Test test = this.fromStorableObject(storableObject);
		this.retrieveTest(test);
		this.retrieveMeasurementSetupTestLinks(test);
	}

	private void retrieveTest(Test test) throws Exception {
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
			+ SQL_WHERE + COLUMN_ID + EQUALS
			+ testIdStr;
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
				//String ptTemplateIdCode = resultSet.getString("ptTemplateId");
				//Date[] ttTimestamps = ((TimeStampArray)(((OracleResultSet)resultSet).getORAData("ttTimestamps", TimeStampArray.getORADataFactory()))).getArray();
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
													 (temportalPatternID != null)?(new Identifier(temportalPatternID)):null,													 
													 /**
													   * @todo when change DB Identifier model ,change getString() to getLong()
													   */
													 new Identifier(resultSet.getString(COLUMN_MEASUREMENT_TYPE_ID)),
													 (analysisTypeIdCode != null)?(new Identifier(analysisTypeIdCode)):null,
													 (evaluationTypeIdCode != null)?(new Identifier(evaluationTypeIdCode)):null,
													 resultSet.getInt(COLUMN_STATUS),
													 new MonitoredElement(new Identifier(monitoredElementIdCode)),
													 resultSet.getInt(COLUMN_RETURN_TYPE),
													 (description != null)?description:"");
			}
			else
				throw new Exception("No such test: " + testIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveTest | Cannot retrieve test " + testIdStr;
			throw new Exception(mesg, sqle);
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
//				 nothing yet ???
				}
		}
	}

	private void retrieveMeasurementSetupTestLinks(Test test) throws Exception {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLMN_MEASUREMENT_SETUP_ID
			+ SQL_FROM
			+ ObjectEntities.MSTESTLINK_ENTITY
			+ SQL_WHERE + LINK_COLMN_TEST_ID + EQUALS
			+ testIdStr;
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
			String mesg = "TestDatabase.retrieveMeasurementSetupTestLinks | Cannot retrieve measurement setup ids for test " + testIdStr;
			throw new Exception(mesg, sqle);
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
//				 nothing yet ???
				}
		}
		test.setMeasurementSetupIds(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws Exception {
		Test test = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Test.RETRIEVE_MEASUREMENTS:
				return this.retrieveMeasurementsOrderByStartTime(test, (MeasurementStatus)arg);
			default:
				return null;
		}
	}

	private ArrayList retrieveMeasurementsOrderByStartTime(Test test, MeasurementStatus measurementStatus) throws Exception {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE
			+ MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
			+ SQL_AND + MeasurementDatabase.COLUMN_STATUS + EQUALS
			+ SQL_ORDER_BY + MeasurementDatabase.COLUMN_START_TIME
			+ " ASC";
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				  * @todo when change DB Identifier model ,change getString() to getLong()
				  */
				arraylist.add(new Measurement(new Identifier(resultSet.getString(COLUMN_ID))));
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveMeasurementsOrderByStartTime | Cannot retrieve measurements for test " + testIdStr + " and status " + Integer.toString(measurementStatus.value());
			throw new Exception(mesg, sqle);
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
//				 nothing yet ???
				}
		}
		return arraylist;
	}

	public void insert(StorableObject storableObject) throws Exception {
		Test test = this.fromStorableObject(storableObject);
		try {
			this.insertTest(test);
			this.insertMeasurementSetupTestLinks(test);
		}
		catch (Exception e) {
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

	private void insertTest(Test test) throws Exception {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String testIdCode = test.getId().getCode();
		Date startTime = test.getStartTime();
		Date endTime = test.getEndTime();
		Identifier temporalPatternId = test.getTemporalPatternId();		
		Identifier analysisId = test.getAnalysisTypeId();
		Identifier evaluationId = test.getEvaluationTypeId();
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
			preparedStatement.setDate(2, new java.sql.Date(test.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(test.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, test.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, test.getModifierId().getCode());
			preparedStatement.setInt(6, test.getTemporalType().value());
			preparedStatement.setDate(7, (startTime != null)?(new java.sql.Date(startTime.getTime())):null);
			preparedStatement.setDate(8, (endTime != null)?(new java.sql.Date(endTime.getTime())):null);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(9, (temporalPatternId != null)?temporalPatternId.getCode():Identifier.getNullSQLString());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(10, test.getMeasurementTypeId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(11, (analysisId != null)?analysisId.getCode():Identifier.getNullSQLString());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(12, (evaluationId != null)?evaluationId.getCode():Identifier.getNullSQLString());
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
			String mesg = "TestDatabase.insertTest | Cannot insert test " + testIdCode;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
//				 nothing yet ???
				}
		}
	}

	private void insertMeasurementSetupTestLinks(Test test) throws Exception {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String testIdCode = test.getId().getCode();
		ArrayList msIds = test.getMeasurementSetupIds();
		String sql = SQL_INSERT_INTO + ObjectEntities.MSTESTLINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLMN_TEST_ID + COMMA + LINK_COLMN_MEASUREMENT_SETUP_ID + CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET + QUESTION + COMMA + QUESTION + CLOSE_BRACKET;			
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
			String mesg = "TestDatabase.insertMeasurementSetupTestLinks | Cannot insert link for measurement setup " + msIdCode + " and test " + testIdCode;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
//				 nothing yet ???
				}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object arg) throws Exception {
		Test test = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Test.UPDATE_STATUS:
				this.updateStatus(test);
				break;
			case Test.UPDATE_MODIFIED:
				this.updateModified(test);
				break;
		}
	}

	private void updateStatus(Test test) throws Exception {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ COLUMN_STATUS + EQUALS
			+ Integer.toString(test.getStatus().value()) + COMMA
			+ COLUMN_MODIFIED + EQUALS
			+ DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID +EQUALS
			+ test.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS
			+ testIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.updateStatus | Cannot update status of test " + testIdStr;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
//				 nothing yet ???
				}
		}
	}

	private void updateModified(Test test) throws Exception {
		String testIdStr = test.getId().toSQLString();
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SELECT
			+ COLUMN_MODIFIED + EQUALS
			+ DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS
			+ test.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS
			+ testIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.updateStatus | Cannot update modified of test " + testIdStr;
			throw new Exception(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				// nothing yet ???
				}
		}
	}
}