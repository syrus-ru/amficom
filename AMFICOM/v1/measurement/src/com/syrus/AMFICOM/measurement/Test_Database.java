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
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.configuration.MonitoredElement;

public class Test_Database extends StorableObject_Database {
//	 analysis_type_id VARCHAR2(32),
	public static final String COLUMN_ANALYSIS_TYPE_ID	= "analysis_type_id";
	// description VARCHAR2(256),
	public static final String COLUMN_DESCRIPTION	= "description";
	// end_time DATE,
	public static final String COLUMN_END_TIME	= "end_time";
	// evaluation_type_id VARCHAR2(32),
	public static final String COLUMN_EVALUATION_TYPE_ID	= "evaluation_type_id";
	// measurement_type_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MEASUREMENT_TYPE_ID	= "measurement_type_id";
	// monitored_element_id VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	// return_type NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_RETURN_TYPE	= "return_type";
	// start_time DATE,
	public static final String COLUMN_START_TIME	= "start_time";
	// status NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_STATUS	= "status";
	// temporal_pattern_id VARCHAR2(32),
	public static final String COLUMN_TEMPORAL_PATTERN_ID	= "temporal_pattern_id";
	// temporal_type NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_TEMPORAL_TYPE	= "temporal_type";
	
	public static final String LINK_COLMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";
	public static final String LINK_COLMN_TEST_ID = "test_id";

	
	private Test fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Test)
			return (Test)storableObject;
		else
			throw new Exception("Test_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Test test = this.fromStorableObject(storableObject);
		this.retrieveTest(test);
		this.retrieveMeasurementSetupTestLinks(test);
	}

	private void retrieveTest(Test test) throws Exception {
		String test_id_str = test.getId().toSQLString();
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
			+ test_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.retrieveTest | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				//String pt_template_id_code = resultSet.getString("pt_template_id");
				//Date[] tt_timestamps = ((TimeStampArray)(((OracleResultSet)resultSet).getORAData("tt_timestamps", TimeStampArray.getORADataFactory()))).getArray();
				String temportalPatternID = resultSet.getString(COLUMN_TEMPORAL_PATTERN_ID);
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String analysis_type_id_code = resultSet.getString(COLUMN_ANALYSIS_TYPE_ID);
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String evaluation_type_id_code = resultSet.getString(COLUMN_EVALUATION_TYPE_ID);
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String monitored_element_id_code = resultSet.getString(COLUMN_MONITORED_ELEMENT_ID);
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
													 (analysis_type_id_code != null)?(new Identifier(analysis_type_id_code)):null,
													 (evaluation_type_id_code != null)?(new Identifier(evaluation_type_id_code)):null,
													 resultSet.getInt(COLUMN_STATUS),
													 new MonitoredElement(new Identifier(monitored_element_id_code)),
													 resultSet.getInt(COLUMN_RETURN_TYPE),
													 (description != null)?description:"");
			}
			else
				throw new Exception("No such test: " + test_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Test_Database.retrieveTest | Cannot retrieve test " + test_id_str;
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
		String test_id_str = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLMN_MEASUREMENT_SETUP_ID
			+ SQL_FROM
			+ ObjectEntities.MSTESTLINK_ENTITY
			+ SQL_WHERE + LINK_COLMN_TEST_ID + EQUALS
			+ test_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.retrieveMeasurementSetupTestLinks | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				  * @todo when change DB Identifier model ,change getString() to getLong()
				  */
				arraylist.add(new Identifier(resultSet.getString(LINK_COLMN_MEASUREMENT_SETUP_ID)));
			}
		}
		catch (SQLException sqle) {
			String mesg = "Test_Database.retrieveMeasurementSetupTestLinks | Cannot retrieve measurement setup ids for test " + test_id_str;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Test test = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case Test.RETRIEVE_MEASUREMENTS:
				return this.retrieveMeasurementsOrderByStartTime(test, (MeasurementStatus)arg);
			default:
				return null;
		}
	}

	private ArrayList retrieveMeasurementsOrderByStartTime(Test test, MeasurementStatus measurement_status) throws Exception {
		String test_id_str = test.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE
			+ Measurement_Database.COLUMN_TEST_ID + EQUALS + test_id_str
			+ SQL_AND + Measurement_Database.COLUMN_STATUS + EQUALS
			+ SQL_ORDER_BY + Measurement_Database.COLUMN_START_TIME
			+ " ASC";
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.retrieveMeasurementsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				  * @todo when change DB Identifier model ,change getString() to getLong()
				  */
				arraylist.add(new Measurement(new Identifier(resultSet.getString(COLUMN_ID))));
			}
		}
		catch (SQLException sqle) {
			String mesg = "Test_Database.retrieveMeasurementsOrderByStartTime | Cannot retrieve measurements for test " + test_id_str + " and status " + Integer.toString(measurement_status.value());
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
		String test_id_code = test.getId().getCode();
		Date start_time = test.getStartTime();
		Date end_time = test.getEndTime();
		Identifier temporalPatternId = test.getTemporalPatternId();		
		Identifier analysis_id = test.getAnalysisTypeId();
		Identifier evaluation_id = test.getEvaluationTypeId();
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
			preparedStatement.setString(1, test_id_code);
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
			preparedStatement.setDate(7, (start_time != null)?(new java.sql.Date(start_time.getTime())):null);
			preparedStatement.setDate(8, (end_time != null)?(new java.sql.Date(end_time.getTime())):null);
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
			preparedStatement.setString(11, (analysis_id != null)?analysis_id.getCode():Identifier.getNullSQLString());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(12, (evaluation_id != null)?evaluation_id.getCode():Identifier.getNullSQLString());
			preparedStatement.setInt(13, test.getStatus().value());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(14, test.getMonitoredElement().getId().getCode());
			preparedStatement.setInt(15, test.getReturnType().value());
			preparedStatement.setString(16, test.getDescription());
			Log.debugMessage("Test_Database.insertTest | Inserting  test " + test_id_code, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
		}
		catch (SQLException sqle) {
			String mesg = "Test_Database.insertTest | Cannot insert test " + test_id_code;
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
		String test_id_code = test.getId().getCode();
		ArrayList ms_ids = test.getMeasurementSetupIds();
		String sql = SQL_INSERT_INTO + ObjectEntities.MSTESTLINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLMN_TEST_ID + COMMA + LINK_COLMN_MEASUREMENT_SETUP_ID + CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET + QUESTION + COMMA + QUESTION + CLOSE_BRACKET;			
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String ms_id_code = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = ms_ids.iterator(); iterator.hasNext();) {
				/**
				  * @todo when change DB Identifier model ,change setString() to setLong()
				  */
				preparedStatement.setString(1, test_id_code);
				ms_id_code = ((Identifier)iterator.next()).getCode();
				/**
				  * @todo when change DB Identifier model ,change setString() to setLong()
				  */
				preparedStatement.setString(2, ms_id_code);
				Log.debugMessage("Test_Database.insertMeasurementSetupTestLinks | Inserting link for test " + test_id_code + " and measurement setup " + ms_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "Test_Database.insertMeasurementSetupTestLinks | Cannot insert link for measurement setup " + ms_id_code + " and test " + test_id_code;
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

	public void update(StorableObject storableObject, int update_kind, Object arg) throws Exception {
		Test test = this.fromStorableObject(storableObject);
		switch (update_kind) {
			case Test.UPDATE_STATUS:
				this.updateStatus(test);
				break;
			case Test.UPDATE_MODIFIED:
				this.updateModified(test);
				break;
		}
	}

	private void updateStatus(Test test) throws Exception {
		String test_id_str = test.getId().toSQLString();
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ COLUMN_STATUS + EQUALS
			+ Integer.toString(test.getStatus().value()) + COMMA
			+ COLUMN_MODIFIED + EQUALS
			+ DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID +EQUALS
			+ test.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS
			+ test_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Test_Database.updateStatus | Cannot update status of test " + test_id_str;
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
		String test_id_str = test.getId().toSQLString();
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SELECT
			+ COLUMN_MODIFIED + EQUALS
			+ DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS
			+ test.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS
			+ test_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Test_Database.updateStatus | Cannot update modified of test " + test_id_str;
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