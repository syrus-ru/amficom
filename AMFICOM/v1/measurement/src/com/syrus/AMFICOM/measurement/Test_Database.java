package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OraclePreparedStatement;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.ora.TimeStampArray;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.configuration.MonitoredElement;

public class Test_Database extends StorableObject_Database {
	public static final String COLUMN_CREATED = "created"; 

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
		String test_id_str = test.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "temporal_type, "
			+ DatabaseDate.toQuerySubString("start_time") + ", "
			+ DatabaseDate.toQuerySubString("end_time") + ", "
			+ "pt_template_id, "
			+ "tt_timestamps, "
			+ "measurement_type_id, "
			+ "analysis_type_id, "
			+ "evaluation_type_id, "
			+ "status, "
			+ "monitored_element_id, "
			+ "return_type, "
			+ "description"
			+ " FROM " + ObjectEntities.TEST_ENTITY
			+ " WHERE id = " + test_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.retrieveTest | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				long pt_template_id_code = resultSet.getLong("pt_template_id");
				Date[] tt_timestamps = ((TimeStampArray)(((OracleResultSet)resultSet).getORAData("tt_timestamps", TimeStampArray.getORADataFactory()))).getArray();
				long analysis_type_id_code = resultSet.getLong("analysis_type_id");
				long evaluation_type_id_code = resultSet.getLong("evaluation_type_id");
				long monitored_element_id_code = resultSet.getLong("monitored_element_id");
				String description = resultSet.getString("description");
				test.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
													 DatabaseDate.fromQuerySubString(resultSet, "modified"),
													 new Identifier(resultSet.getLong("creator_id")),
													 new Identifier(resultSet.getLong("modifier_id")),
													 resultSet.getInt("temporal_type"),
													 DatabaseDate.fromQuerySubString(resultSet, "start_time"),
													 DatabaseDate.fromQuerySubString(resultSet, "end_time"),
													 (pt_template_id_code != 0)?(new Identifier(pt_template_id_code)):null,
													 tt_timestamps,
													 new Identifier(resultSet.getLong("measurement_type_id")),
													 (analysis_type_id_code != 0)?(new Identifier(analysis_type_id_code)):null,
													 (evaluation_type_id_code != 0)?(new Identifier(evaluation_type_id_code)):null,
													 resultSet.getInt("status"),
													 new MonitoredElement(new Identifier(monitored_element_id_code)),
													 resultSet.getInt("return_type"),
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
			catch (SQLException sqle1) {}
		}
	}

	private void retrieveMeasurementSetupTestLinks(Test test) throws Exception {
		String test_id_str = test.getId().toString();
		String sql = "SELECT "
			+ "measurement_setup_id"
			+ " FROM " + ObjectEntities.MSTESTLINK_ENTITY
			+ " WHERE test_id = " + test_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.retrieveMeasurementSetupTestLinks | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new Identifier(resultSet.getLong("measurement_setup_id")));
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
			catch (SQLException sqle1) {}
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
		String test_id_str = test.getId().toString();
		String sql = "SELECT "
			+ "id"
			+ " FROM " + ObjectEntities.MEASUREMENT_ENTITY
			+ " WHERE test_id = " + test_id_str 
				+ " AND status = " + Integer.toString(measurement_status.value())
			+ " ORDER BY start_time ASC";
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Test_Database.retrieveMeasurementsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new Measurement(new Identifier(resultSet.getLong("id"))));
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
			catch (SQLException sqle1) {}
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
		long test_id_code = test.getId().getCode();
		Date start_time = test.getStartTime();
		Date end_time = test.getEndTime();
		Identifier pt_template_id = test.getPTTemplateId();
		Date[] tt_timestamps = test.getTTTimestamps();
		Identifier analysis_id = test.getAnalysisTypeId();
		Identifier evaluation_id = test.getEvaluationTypeId();
		String sql = "INSERT INTO " + ObjectEntities.TEST_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, temporal_type, start_time, end_time, pt_template_id, tt_timestamps, measurement_type_id, analysis_type_id, evaluation_type_id, status, monitored_element_id, return_type, description)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, test_id_code);
			preparedStatement.setDate(2, new java.sql.Date(test.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(test.getModified().getTime()));
			preparedStatement.setLong(4, test.getCreatorId().getCode());
			preparedStatement.setLong(5, test.getModifierId().getCode());
			preparedStatement.setInt(6, test.getTemporalType().value());
			preparedStatement.setDate(7, (start_time != null)?(new java.sql.Date(start_time.getTime())):null);
			preparedStatement.setDate(8, (end_time != null)?(new java.sql.Date(end_time.getTime())):null);
			preparedStatement.setLong(9, (pt_template_id != null)?pt_template_id.getCode():0);
			if (tt_timestamps != null) {
				Timestamp[] tss = new Timestamp[tt_timestamps.length];
				for (int i = 0; i < tss.length; i++)
					tss[i] = new Timestamp(tt_timestamps[i].getTime());
				((OraclePreparedStatement)preparedStatement).setORAData(10, new TimeStampArray(tss));
			}
			else
				preparedStatement.setObject(10, new TimeStampArray());
			preparedStatement.setLong(11, test.getMeasurementTypeId().getCode());
			preparedStatement.setLong(12, (analysis_id != null)?analysis_id.getCode():0);
			preparedStatement.setLong(13, (evaluation_id != null)?evaluation_id.getCode():0);
			preparedStatement.setInt(14, test.getStatus().value());
			preparedStatement.setLong(15, test.getMonitoredElement().getId().getCode());
			preparedStatement.setInt(16, test.getReturnType().value());
			preparedStatement.setString(17, test.getDescription());
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
			catch (SQLException sqle1) {}
		}
	}

	private void insertMeasurementSetupTestLinks(Test test) throws Exception {
		long test_id_code = test.getId().getCode();
		ArrayList ms_ids = test.getMeasurementSetupIds();
		String sql = "INSERT INTO " + ObjectEntities.MSTESTLINK_ENTITY
			+ " (test_id, measurement_setup_id)"
			+ " VALUES (?, ?)";
		PreparedStatement preparedStatement = null;
		long ms_id_code = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = ms_ids.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, test_id_code);
				ms_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, ms_id_code);
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
			catch (SQLException sqle1) {}
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
		String test_id_str = test.getId().toString();
		String sql = "UPDATE " + ObjectEntities.TEST_ENTITY
			+ " SET "
			+ "status = " + Integer.toString(test.getStatus().value()) + ", "
			+ "modified = " + DatabaseDate.toUpdateSubString(test.getModified()) + ", "
			+ "modifier_id = " + test.getModifierId().toString()
			+ " WHERE id = " + test_id_str;
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
			catch (SQLException sqle1) {}
		}
	}

	private void updateModified(Test test) throws Exception {
		String test_id_str = test.getId().toString();
		String sql = "UPDATE " + ObjectEntities.TEST_ENTITY
			+ " SET "
			+ "modified = " + DatabaseDate.toUpdateSubString(test.getModified()) + ", "
			+ "modifier_id = " + test.getModifierId().toString()
			+ " WHERE id = " + test_id_str;
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
			catch (SQLException sqle1) {}
		}
	}
}