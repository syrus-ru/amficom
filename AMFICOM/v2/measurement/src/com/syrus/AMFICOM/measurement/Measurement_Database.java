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

	public void retrieve(StorableObject storableObject) throws Exception {
		Measurement measurement = null;
		if (storableObject instanceof Measurement)
			measurement = (Measurement)storableObject;
		else
			throw new Exception("Measurement_Database.retrieve | Illegal Storable Object: " + storableObject.getClass().getName());

		String measurement_id_str = measurement.getId().toString();
		String sql = "SELECT type_id, setup_id, " + DatabaseDate.toQuerySubString("start_time") + ", duration, status, local_address, monitored_element_id, test_id, " + DatabaseDate.toQuerySubString("modified") + " FROM " + ObjectEntities.MEASUREMENT_ENTITY + " WHERE id = " + measurement_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Measurement_Database.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				MeasurementSetup measurementSetup = new MeasurementSetup(new Identifier(resultSet.getLong("setup_id")));
				measurement.setAttributes(new Identifier(resultSet.getLong("type_id")),
																	measurementSetup,
																	DatabaseDate.fromQuerySubString(resultSet, "start_time"),
																	resultSet.getLong("duration"),
																	resultSet.getInt("status"),
																	resultSet.getString("local_address"),
																	new Identifier(resultSet.getLong("monitored_element_id")),
																	new Identifier(resultSet.getLong("test_id")),
																	DatabaseDate.fromQuerySubString(resultSet, "modified"));
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
		Measurement measurement = null;
		if (storableObject instanceof Measurement)
			measurement = (Measurement)storableObject;
		else
			throw new Exception("Measurement_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

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
		String sql = "SELECT id FROM " + ObjectEntities.RESULT_ENTITY + " WHERE measurement_id = " + measurement_id_str + " AND sort = " + Integer.toString(result_sort_num);
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
		Measurement measurement = null;
		if (storableObject instanceof Measurement)
			measurement = (Measurement)storableObject;
		else
			throw new Exception("Measurement_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		String measurement_id_str = measurement.getId().toString();
		String sql = "INSERT INTO " + ObjectEntities.MEASUREMENT_ENTITY + " (id, type_id, setup_id, start_time, duration, status, local_address, monitored_element_id, test_id, modified) VALUES ("
								+ measurement_id_str + ", " + measurement.getTypeId().toString() + ", " + measurement.getSetup().getId().toString() + ", " + DatabaseDate.toUpdateSubString(measurement.getStartTime()) + ", " + measurement.getDuration() + ", " + measurement.getStatus().value() + ", '" + measurement.getLocalAddress() + "', " + measurement.getMonitoredElementId().toString() + ", " + measurement.getTestId().toString() + ", " + DatabaseDate.toUpdateSubString(measurement.getModified()) + ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Measurement_Database.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
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
		Measurement measurement = null;
		if (storableObject instanceof Measurement)
			measurement = (Measurement)storableObject;
		else
			throw new Exception("Measurement_Database.update| Illegal Storable Object: " + storableObject.getClass().getName());

		switch (update_kind) {
			case Measurement.UPDATE_STATUS:
				this.updateStatus(measurement);
				break;
		}
	}

	private void updateStatus(Measurement measurement) throws Exception {
		String measurement_id_str = measurement.getId().toString();
		String sql = "UPDATE " + ObjectEntities.MEASUREMENT_ENTITY + " SET status = " + measurement.getStatus().value() + ", modified = " + DatabaseDate.toUpdateSubString(measurement.getModified()) + " WHERE id = " + measurement_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("Measurement_Database.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
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