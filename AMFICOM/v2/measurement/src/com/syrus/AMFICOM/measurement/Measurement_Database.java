package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

public class Measurement_Database extends StorableObject_Database {

	private Measurement fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Measurement)
			return (Measurement)storableObject;
		else
			throw new Exception("Measurement_Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.retrieveMeasurement(measurement);
	}

	private void retrieveMeasurement(Measurement measurement) throws Exception {
		String measurement_id_str = measurement.getId().toString();
		String sql = "SELECT "
			+ DatabaseDate.toQuerySubString("created") + ", " 
			+ DatabaseDate.toQuerySubString("modified") + ", "
			+ "creator_id, "
			+ "modifier_id, "
			+ "type_id, "
			+ "monitored_element_id, "
			+ "setup_id, "
			+ DatabaseDate.toQuerySubString("start_time") + ", "
			+ "duration, "
			+ "status, "
			+ "local_address, "
			+ "test_id"
			+ " FROM " + ObjectEntities.MEASUREMENT_ENTITY
			+ " WHERE id = " + measurement_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Measurement_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				MeasurementSetup measurementSetup = new MeasurementSetup(new Identifier(resultSet.getLong("setup_id")));
				measurement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, "created"),
																	DatabaseDate.fromQuerySubString(resultSet, "modified"),
																	new Identifier(resultSet.getLong("creator_id")),
																	new Identifier(resultSet.getLong("modifier_id")),
																	new Identifier(resultSet.getLong("type_id")),
																	new Identifier(resultSet.getLong("monitored_element_id")),
																	measurementSetup,
																	DatabaseDate.fromQuerySubString(resultSet, "start_time"),
																	resultSet.getLong("duration"),
																	resultSet.getInt("status"),
																	resultSet.getString("local_address"),
																	new Identifier(resultSet.getLong("test_id")));
			}
			else
				throw new Exception("No such measurement: " + measurement_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Measurement_Database.retrieve | Cannot retrieve measurement " + measurement_id_str;
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		Measurement measurement = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case Measurement.RETRIEVE_RESULT:
				return this.retrieveResult(measurement, (ResultSort)arg);
			default:
				return null;
		}
	}

	private Result retrieveResult(Measurement measurement, ResultSort result_sort) throws Exception {
		String measurement_id_str = measurement.getId().toString();
		int result_sort_num = result_sort.value();
		String sql = "SELECT "
			+ "id"
			+ " FROM " + ObjectEntities.RESULT_ENTITY
			+ " WHERE measurement_id = " + measurement_id_str
				+ " AND sort = " + Integer.toString(result_sort_num);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Measurement_Database.retrieveResult | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new Result(new Identifier(resultSet.getLong("id")));
			else	
				throw new Exception("No result of sort: " + result_sort_num + " for measurement " + measurement_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "Measurement_Database.retrieveResult | Cannot retrieve result of sort " + result_sort_num + " for measurement " + measurement_id_str;
			throw new Exception(mesg , sqle);
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

	public void insert(StorableObject storableObject) throws Exception {
		Measurement measurement = this.fromStorableObject(storableObject);
		try {
			this.insertMeasurement(measurement);
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

	private void insertMeasurement(Measurement measurement) throws Exception {
		String measurement_id_str = measurement.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.MEASUREMENT_ENTITY
			+ " (id, created, modified, creator_id, modifier_id, type_id, monitored_element_id, setup_id, start_time, duration, status, local_address, test_id)"
			+ " VALUES ("
			+ measurement_id_str + ", "
			+ DatabaseDate.toUpdateSubString(measurement.getCreated()) + ", "
			+ DatabaseDate.toUpdateSubString(measurement.getModified()) + ", "
			+ measurement.getCreatorId().toString() + ", "
			+ measurement.getModifierId().toString() + ", "
			+ measurement.getTypeId().toString() + ", "
			+ measurement.getMonitoredElementId().toString() + ", "
			+ measurement.getSetup().getId().toString() + ", "
			+ DatabaseDate.toUpdateSubString(measurement.getStartTime()) + ", "
			+ Long.toString(measurement.getDuration()) + ", "
			+ Integer.toString(measurement.getStatus().value()) + ", '"
			+ measurement.getLocalAddress() + "', "
			+ measurement.getTestId().toString()
			+ ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Measurement_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Measurement_Database.insert | Cannot insert measurement " + measurement_id_str;
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

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		Measurement measurement = this.fromStorableObject(storableObject);
		try {
			switch (update_kind) {
				case Measurement.UPDATE_STATUS:
					this.updateStatus(measurement);
					break;
			}
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

	private void updateStatus(Measurement measurement) throws Exception {
		String measurement_id_str = measurement.getId().toString();
		String sql = "UPDATE " + ObjectEntities.MEASUREMENT_ENTITY
			+ " SET "
			+ "status = " + Integer.toString(measurement.getStatus().value()) + ", "
			+ "modified = " + DatabaseDate.toUpdateSubString(measurement.getModified()) + ", "
			+ "modifier_id = " + measurement.getModifierId().toString()
			+ " WHERE id = " + measurement_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Measurement_Database.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "Measurement_Database.updateStatus | Cannot update status of measurement " + measurement_id_str;
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