package com.syrus.AMFICOM.measurement;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObject_Database;
import com.syrus.AMFICOM.general.StorableObject_DatabaseContext;
import com.syrus.AMFICOM.general.ObjectEntities;

public class MeasurementSetup_Database extends StorableObject_Database {

	public void retrieve(StorableObject storableObject) throws Exception {
		MeasurementSetup measurementSetup = null;
		if (storableObject instanceof MeasurementSetup)
			measurementSetup = (MeasurementSetup)storableObject;
		else
			throw new Exception("MeasurementSetup_Database.retrieve | Illegal Storable Object: " + storableObject.getClass().getName());

		this.retrieveMeasurementSetup(measurementSetup);
		this.retrieveMeasurementSetupMELinks(measurementSetup);
	}

	private void retrieveMeasurementSetup(MeasurementSetup measurementSetup) throws Exception {
		String ms_id_str = measurementSetup.getId().toString();
		String sql = "SELECT parameter_set_id, criteria_set_id, threshold_set_id, etalon_id, " + DatabaseDate.toQuerySubString("created") + ", creator_id, " + DatabaseDate.toQuerySubString("modified") + ", description, measurement_duration FROM " + ObjectEntities.MS_ENTITY + " WHERE id = " + ms_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetup_Database.retrieveMeasurementSetup | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				Set parameter_set = new Set(new Identifier(resultSet.getLong("parameter_set_id")));
				long id_code = 0;
				id_code = resultSet.getLong("criteria_set_id");
				Set criteria_set = (id_code != 0)?(new Set(new Identifier(id_code))):null;
				id_code = resultSet.getLong("threshold_set_id");
				Set threshold_set = (id_code != 0)?(new Set(new Identifier(id_code))):null;
				id_code = resultSet.getLong("etalon_id");
				Set etalon = (id_code != 0)?(new Set(new Identifier(id_code))):null;
				String description = resultSet.getString("description");
				measurementSetup.setAttributes(parameter_set,
																			 criteria_set,
																			 threshold_set,
																			 etalon,
																			 DatabaseDate.fromQuerySubString(resultSet, "created"),
																			 new Identifier(resultSet.getLong("creator_id")),
																			 DatabaseDate.fromQuerySubString(resultSet, "modified"),
																			 (description != null)?description:"",
																			 resultSet.getLong("measurement_duration"));
			}
			else
				throw new Exception("No such measurement setup: " + ms_id_str);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.retrieveMeasurementSetup | Cannot retrieve measurement setup " + ms_id_str;
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

	private void retrieveMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws Exception {
		String ms_id_str = measurementSetup.getId().toString();
		String sql = "SELECT monitored_element_id FROM " + ObjectEntities.MSMELINK_ENTITY + " WHERE measurement_setup_id = " + ms_id_str;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetup_Database.retrieveMeasurementSetupMELinks | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				arraylist.add(new Identifier(resultSet.getLong("monitored_element_id")));
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.retrieveMeasurementSetupMELinks | Cannot retrieve monitored element ids for measurement setup " + ms_id_str;
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
		measurementSetup.setMonitoredElementIds(arraylist);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws Exception {
		MeasurementSetup measurementSetup = null;
		if (storableObject instanceof MeasurementSetup)
			measurementSetup = (MeasurementSetup)storableObject;
		else
			throw new Exception("MeasurementSetup_Database.retrieveObject | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws Exception {
		MeasurementSetup measurementSetup = null;
		if (storableObject instanceof MeasurementSetup)
			measurementSetup = (MeasurementSetup)storableObject;
		else
			throw new Exception("MeasurementSetup_Database.insert | Illegal Storable Object: " + storableObject.getClass().getName());

		try {
			this.insertMeasurementSetup(measurementSetup);
			this.insertMeasurementSetupMELinks(measurementSetup);
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

	private void insertMeasurementSetup(MeasurementSetup measurementSetup) throws Exception {
		String ms_id_str = measurementSetup.getId().toString();
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		String criteria_set_id_substr = (criteriaSet != null)?(criteriaSet.getId().toString()):"0";
		String threshold_set_id_substr = (thresholdSet != null)?(thresholdSet.getId().toString()):"0";
		String etalon_id_substr = (etalon != null)?(etalon.getId().toString()):"0";
		String sql = "INSERT INTO " + ObjectEntities.MS_ENTITY + " (id, parameter_set_id, criteria_set_id, threshold_set_id, etalon_id, created, creator_id, modified, description, measurement_duration) VALUES ("
							+ ms_id_str + ", " + measurementSetup.getParameterSet().getId().toString() + ", " + criteria_set_id_substr + ", " + threshold_set_id_substr + ", " + etalon_id_substr + ", "
							+ DatabaseDate.toUpdateSubString(measurementSetup.getCreated()) + ", " + measurementSetup.getCreatorId().toString() + ", " + DatabaseDate.toUpdateSubString(measurementSetup.getModified()) + ", '" + measurementSetup.getDescription() + "', " + measurementSetup.getMeasurementDuration() + ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetup_Database.insertMeasurementSetup | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.insertMeasurementSetup | Cannot insert Measurement Setup " + ms_id_str;
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

	private void insertMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws Exception {
		long ms_id_code = measurementSetup.getId().getCode();
		ArrayList me_ids = measurementSetup.getMonitoredElementIds();
		String sql = "INSERT INTO " + ObjectEntities.MSMELINK_ENTITY + " (measurement_setup_id, monitored_element_id) VALUES (?, ?)";
		PreparedStatement preparedStatement = null;
		long me_id_code = 0;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = me_ids.iterator(); iterator.hasNext();) {
				preparedStatement.setLong(1, ms_id_code);
				me_id_code = ((Identifier)iterator.next()).getCode();
				preparedStatement.setLong(2, me_id_code);
				Log.debugMessage("MeasurementSetup_Database.insertMeasurementSetupMELinks | Inserting link for measurement setup " + ms_id_code + " and monitored element " + me_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.insertMeasurementSetupMELinks | Cannot insert link for monitored element " + me_id_code + " and Measurement Setup " + ms_id_code;
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

	public void update(StorableObject storableObject, int update_kind, Object obj) throws Exception {
		MeasurementSetup measurementSetup = null;
		if (storableObject instanceof MeasurementSetup)
			measurementSetup = (MeasurementSetup)storableObject;
		else
			throw new Exception("MeasurementSetup_Database.update | Illegal Storable Object: " + storableObject.getClass().getName());

		switch (update_kind) {
			case MeasurementSetup.UPDATE_ATTACH_ME:
				this.createMEAttachment(measurementSetup, (Identifier)obj);
				break;
			case MeasurementSetup.UPDATE_DETACH_ME:
				this.deleteMEAttachment(measurementSetup, (Identifier)obj);
				break;
		}
	}

	protected void createMEAttachment(MeasurementSetup measurementSetup, Identifier monitored_element_id) throws Exception {
		String ms_id_str = measurementSetup.getId().toString();
		String me_id_str = monitored_element_id.toString();
		String sql = "INSERT INTO " + ObjectEntities.MSMELINK_ENTITY + " (measurement_setup_id, monitored_element_id) VALUES (" + ms_id_str + ", " + me_id_str + ")";
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetup_Database.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeQuery(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.createMEAttachment | Cannot attach measurement setup " + ms_id_str + " to monitored element " + me_id_str;
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

	protected void deleteMEAttachment(MeasurementSetup measurementSetup, Identifier monitored_element_id) throws Exception {
		String ms_id_str = measurementSetup.getId().toString();
		String me_id_str = monitored_element_id.toString();
		String sql = "DELETE FROM " + ObjectEntities.MSMELINK_ENTITY + " WHERE measurement_setup_id = " + ms_id_str + " AND monitored_element_id = " + me_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetup_Database.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeQuery(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.deleteMEAttachment | Cannot detach measurement setup " + ms_id_str + " from monitored element " + me_id_str;
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