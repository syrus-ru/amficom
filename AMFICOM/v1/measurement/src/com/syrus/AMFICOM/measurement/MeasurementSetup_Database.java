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
import com.syrus.AMFICOM.general.ObjectEntities;

public class MeasurementSetup_Database extends StorableObject_Database {

	public static final String	COLUMN_CRITERIA_SET_ID				= "criteria_set_id";
	public static final String	COLUMN_DESCRIPTION					= "description";
	public static final String	COLUMN_ETHALON_ID					= "etalon_id";
	public static final String	COLUMN_MEASUREMENT_DURAION			= "measurement_duration";
	public static final String	COLUMN_PARAMETER_SET_ID				= "parameter_set_id";
	public static final String	COLUMN_THRESHOLD_SET_ID				= "threshold_set_id";

	public static final String	LINK_COLUMN_ME_ID					= "monitored_element_id";
	public static final String	LINK_COLUMN_MEASUREMENT_SETUP_ID	= "measurement_setup_id";

	public void insert(StorableObject storableObject) throws Exception {
		MeasurementSetup measurementSetup = this
				.fromStorableObject(storableObject);
		try {
			this.insertMeasurementSetup(measurementSetup);
			this.insertMeasurementSetupMELinks(measurementSetup);
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		MeasurementSetup measurementSetup = this
				.fromStorableObject(storableObject);
		this.retrieveMeasurementSetup(measurementSetup);
		this.retrieveMeasurementSetupMELinks(measurementSetup);
	}

	public Object retrieveObject(StorableObject storableObject,
			int retrieve_kind, Object arg) throws Exception {
		MeasurementSetup measurementSetup = this
				.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void update(StorableObject storableObject, int update_kind,
			Object obj) throws Exception {
		MeasurementSetup measurementSetup = this
				.fromStorableObject(storableObject);
		try {
			switch (update_kind) {
				case MeasurementSetup.UPDATE_ATTACH_ME:
					this.createMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
					break;
				case MeasurementSetup.UPDATE_DETACH_ME:
					this.deleteMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
					break;
			}
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		} catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
	}

	private void createMEAttachment(MeasurementSetup measurementSetup,
			Identifier monitored_element_id) throws Exception {
		String ms_id_str = measurementSetup.getId().toSQLString();
		String me_id_str = monitored_element_id.toSQLString();
		String sql = SQL_INSERT_INTO + ObjectEntities.MSMELINK_ENTITY
				+ OPEN_BRACKET
				+ LINK_COLUMN_MEASUREMENT_SETUP_ID
				+ COMMA
				+ LINK_COLUMN_ME_ID
				+ CLOSE_BRACKET
				+ SQL_VALUES
				+ OPEN_BRACKET				
				+ ms_id_str 
				+ COMMA 
				+ me_id_str 
				+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetup_Database.createMEAttachment | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.createMEAttachment | Cannot attach measurement setup "
					+ ms_id_str + " to monitored element " + me_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private void deleteMEAttachment(MeasurementSetup measurementSetup,
			Identifier monitored_element_id) throws Exception {
		String ms_id_str = measurementSetup.getId().toSQLString();
		String me_id_str = monitored_element_id.toSQLString();
		String sql = SQL_DELETE_FROM 
				+ ObjectEntities.MSMELINK_ENTITY
				+ SQL_WHERE
				+ LINK_COLUMN_MEASUREMENT_SETUP_ID 
				+ EQUALS 
				+ ms_id_str
				+ SQL_AND
				+ LINK_COLUMN_ME_ID
				+ EQUALS 
				+ me_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetup_Database.deleteMEAttachment | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.deleteMEAttachment | Cannot detach measurement setup "
					+ ms_id_str + " from monitored element " + me_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private MeasurementSetup fromStorableObject(StorableObject storableObject)
			throws Exception {
		if (storableObject instanceof MeasurementSetup)
			return (MeasurementSetup) storableObject;
		else
			throw new Exception(
					"MeasurementSetup_Database.fromStorableObject | Illegal Storable Object: "
							+ storableObject.getClass().getName());
	}

	private void insertMeasurementSetup(MeasurementSetup measurementSetup)
			throws Exception {
		String ms_id_str = measurementSetup.getId().toSQLString();
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();		
		String criteria_set_id_substr = (criteriaSet != null) ? (criteriaSet
				.getId().toSQLString()) : Identifier.getNullSQLString();
		String threshold_set_id_substr = (thresholdSet != null) ? (thresholdSet
				.getId().toSQLString()) : Identifier.getNullSQLString();
		String etalon_id_substr = (etalon != null) ? (etalon.getId()
				.toSQLString()) : Identifier.getNullSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.MS_ENTITY);
			buffer.append(OPEN_BRACKET);
			buffer.append(COLUMN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATED);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIED);
			buffer.append(COMMA);
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_PARAMETER_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CRITERIA_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_THRESHOLD_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_ETHALON_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(COMMA);
			buffer.append(COLUMN_MEASUREMENT_DURAION);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(ms_id_str);
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(measurementSetup
					.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(measurementSetup
					.getModified()));
			buffer.append(COMMA);
			buffer.append(measurementSetup.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(measurementSetup.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append(measurementSetup.getParameterSet().getId()
					.toSQLString());
			buffer.append(COMMA);
			buffer.append(criteria_set_id_substr);
			buffer.append(COMMA);
			buffer.append(threshold_set_id_substr);
			buffer.append(COMMA);
			buffer.append(etalon_id_substr);
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(measurementSetup.getDescription());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append(measurementSetup.getMeasurementDuration());
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetup_Database.insertMeasurementSetup | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.insertMeasurementSetup | Cannot insert Measurement Setup "
					+ ms_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private void insertMeasurementSetupMELinks(MeasurementSetup measurementSetup)
			throws Exception {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String ms_id_code = measurementSetup.getId().getCode();
		ArrayList me_ids = measurementSetup.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
				+ ObjectEntities.MSMELINK_ENTITY
				+ OPEN_BRACKET
				+ LINK_COLUMN_MEASUREMENT_SETUP_ID 
				+ COMMA 
				+ LINK_COLUMN_ME_ID
				+ CLOSE_BRACKET
				+ SQL_VALUES
				+ OPEN_BRACKET
				+ QUESTION
				+ COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String me_id_code = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = me_ids.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(1, ms_id_code);
				me_id_code = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, me_id_code);
				Log
						.debugMessage(
								"MeasurementSetup_Database.insertMeasurementSetupMELinks | Inserting link for measurement setup "
										+ ms_id_code
										+ " and monitored element "
										+ me_id_code, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.insertMeasurementSetupMELinks | Cannot insert link for monitored element "
					+ me_id_code + " and Measurement Setup " + ms_id_code;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private void retrieveMeasurementSetup(MeasurementSetup measurementSetup)
			throws Exception {
		String ms_id_str = measurementSetup.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer =  new StringBuffer(SQL_SELECT);
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_CREATED));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toQuerySubString(COLUMN_MODIFIED));
			buffer.append(COMMA);			
			buffer.append(COLUMN_CREATOR_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MODIFIER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_PARAMETER_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_CRITERIA_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_THRESHOLD_SET_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_ETHALON_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(COMMA);
			buffer.append(COLUMN_MEASUREMENT_DURAION); 
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.MS_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_ID);
			buffer.append(EQUALS);
			buffer.append(ms_id_str);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetup_Database.retrieveMeasurementSetup | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				Set parameter_set = new Set(new Identifier(resultSet
						.getString(COLUMN_PARAMETER_SET_ID)));
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				String id_code = resultSet.getString(COLUMN_CRITERIA_SET_ID);
				Set criteria_set = (id_code != null) ? (new Set(new Identifier(
						id_code))) : null;
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				id_code = resultSet.getString(COLUMN_THRESHOLD_SET_ID);
				Set threshold_set = (id_code != null) ? (new Set(
						new Identifier(id_code))) : null;
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				id_code = resultSet.getString(COLUMN_ETHALON_ID);
				Set etalon = (id_code != null) ? (new Set(new Identifier(
						id_code))) : null;
				String description = resultSet.getString(COLUMN_DESCRIPTION);
				measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(
						resultSet, COLUMN_CREATED), DatabaseDate
						.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
						parameter_set, criteria_set, threshold_set, etalon,
						(description != null) ? description : "", resultSet
								.getLong(COLUMN_MEASUREMENT_DURAION));
			} else
				throw new Exception("No such measurement setup: " + ms_id_str);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.retrieveMeasurementSetup | Cannot retrieve measurement setup "
					+ ms_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
			}
		}
	}

	private void retrieveMeasurementSetupMELinks(
			MeasurementSetup measurementSetup) throws Exception {
		String ms_id_str = measurementSetup.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(LINK_COLUMN_ME_ID);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.MSMELINK_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(LINK_COLUMN_MEASUREMENT_SETUP_ID);
			buffer.append(EQUALS);
			buffer.append(ms_id_str);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetup_Database.retrieveMeasurementSetupMELinks | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				arraylist.add(new Identifier(resultSet
						.getString(LINK_COLUMN_ME_ID)));
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.retrieveMeasurementSetupMELinks | Cannot retrieve monitored element ids for measurement setup "
					+ ms_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
			}
		}
		measurementSetup.setMonitoredElementIds(arraylist);
	}

	private void setModified(MeasurementSetup measurementSetup)
			throws Exception {
		String ms_id_str = measurementSetup.getId().toString();
		String sql = SQL_UPDATE
				+ ObjectEntities.MS_ENTITY
				+ SQL_SET
				+ COLUMN_MODIFIED
				+ EQUALS
				+ DatabaseDate
						.toUpdateSubString(measurementSetup.getModified())
				+ COMMA
				+ COLUMN_MODIFIER_ID
				+ EQUALS
				+ measurementSetup.getModifierId().toString()
				+ SQL_WHERE
				+ COLUMN_ID
				+ ms_id_str;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetup_Database.setModified | Trying: "
					+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetup_Database.setModified | Cannot set modified for measurement setup "
					+ ms_id_str;
			throw new Exception(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
			}
		}
	}
}