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

public class MeasurementSetupDatabase extends StorableObject_Database {

	public static final String	COLUMN_CRITERIA_SET_ID				= "criteriaSetId";
	public static final String	COLUMN_DESCRIPTION					= "description";
	public static final String	COLUMN_ETHALON_ID					= "etalonId";
	public static final String	COLUMN_MEASUREMENT_DURAION			= "measurementDuration";
	public static final String	COLUMN_PARAMETER_SET_ID				= "parameterSetId";
	public static final String	COLUMN_THRESHOLD_SET_ID				= "thresholdSetId";

	public static final String	LINK_COLUMN_ME_ID					= "monitoredElementId";
	public static final String	LINK_COLUMN_MEASUREMENT_SETUP_ID	= "measurementSetupId";

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
			int retrieveKind, Object arg) throws Exception {
		MeasurementSetup measurementSetup = this
				.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void update(StorableObject storableObject, int updateKind,
			Object obj) throws Exception {
		MeasurementSetup measurementSetup = this
				.fromStorableObject(storableObject);
		try {
			switch (updateKind) {
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
			Identifier monitoredElementId) throws Exception {
		String msIdStr = measurementSetup.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_INSERT_INTO + ObjectEntities.MSMELINK_ENTITY
				+ OPEN_BRACKET
				+ LINK_COLUMN_MEASUREMENT_SETUP_ID
				+ COMMA
				+ LINK_COLUMN_ME_ID
				+ CLOSE_BRACKET
				+ SQL_VALUES
				+ OPEN_BRACKET				
				+ msIdStr 
				+ COMMA 
				+ meIdStr 
				+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetupDatabase.createMEAttachment | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.createMEAttachment | Cannot attach measurement setup "
					+ msIdStr + " to monitored element " + meIdStr;
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
			Identifier monitoredElementId) throws Exception {
		String msIdStr = measurementSetup.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_DELETE_FROM 
				+ ObjectEntities.MSMELINK_ENTITY
				+ SQL_WHERE
				+ LINK_COLUMN_MEASUREMENT_SETUP_ID 
				+ EQUALS 
				+ msIdStr
				+ SQL_AND
				+ LINK_COLUMN_ME_ID
				+ EQUALS 
				+ meIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetupDatabase.deleteMEAttachment | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.deleteMEAttachment | Cannot detach measurement setup "
					+ msIdStr + " from monitored element " + meIdStr;
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
					"MeasurementSetupDatabase.fromStorableObject | Illegal Storable Object: "
							+ storableObject.getClass().getName());
	}

	private void insertMeasurementSetup(MeasurementSetup measurementSetup)
			throws Exception {
		String msIdStr = measurementSetup.getId().toSQLString();
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();		
		String criteriaSetIdSubstr = (criteriaSet != null) ? (criteriaSet
				.getId().toSQLString()) : Identifier.getNullSQLString();
		String thresholdSetIdSubstr = (thresholdSet != null) ? (thresholdSet
				.getId().toSQLString()) : Identifier.getNullSQLString();
		String etalonIdSubstr = (etalon != null) ? (etalon.getId()
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
			buffer.append(msIdStr);
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
			buffer.append(criteriaSetIdSubstr);
			buffer.append(COMMA);
			buffer.append(thresholdSetIdSubstr);
			buffer.append(COMMA);
			buffer.append(etalonIdSubstr);
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
					"MeasurementSetupDatabase.insertMeasurementSetup | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.insertMeasurementSetup | Cannot insert Measurement Setup "
					+ msIdStr;
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
		String msIdCode = measurementSetup.getId().getCode();
		ArrayList meIds = measurementSetup.getMonitoredElementIds();
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
		String meIdCode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(1, msIdCode);
				meIdCode = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, meIdCode);
				Log
						.debugMessage(
								"MeasurementSetupDatabase.insertMeasurementSetupMELinks | Inserting link for measurement setup "
										+ msIdCode
										+ " and monitored element "
										+ meIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.insertMeasurementSetupMELinks | Cannot insert link for monitored element "
					+ meIdCode + " and Measurement Setup " + msIdCode;
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
		String msIdStr = measurementSetup.getId().toSQLString();
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
			buffer.append(msIdStr);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetupDatabase.retrieveMeasurementSetup | Trying: "
							+ sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				Set parameterSet = new Set(new Identifier(resultSet
						.getString(COLUMN_PARAMETER_SET_ID)));
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				String idCode = resultSet.getString(COLUMN_CRITERIA_SET_ID);
				Set criteriaSet = (idCode != null) ? (new Set(new Identifier(
						idCode))) : null;
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				idCode = resultSet.getString(COLUMN_THRESHOLD_SET_ID);
				Set thresholdSet = (idCode != null) ? (new Set(
						new Identifier(idCode))) : null;
				/**
				 * @todo when change DB Identifier model ,change getString() to
				 *       getLong()
				 */
				idCode = resultSet.getString(COLUMN_ETHALON_ID);
				Set etalon = (idCode != null) ? (new Set(new Identifier(
						idCode))) : null;
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
						parameterSet, criteriaSet, thresholdSet, etalon,
						(description != null) ? description : "", resultSet
								.getLong(COLUMN_MEASUREMENT_DURAION));
			} else
				throw new Exception("No such measurement setup: " + msIdStr);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.retrieveMeasurementSetup | Cannot retrieve measurement setup "
					+ msIdStr;
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
		String msIdStr = measurementSetup.getId().toSQLString();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(LINK_COLUMN_ME_ID);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.MSMELINK_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(LINK_COLUMN_MEASUREMENT_SETUP_ID);
			buffer.append(EQUALS);
			buffer.append(msIdStr);
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList arraylist = new ArrayList();
		try {
			statement = connection.createStatement();
			Log.debugMessage(
					"MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Trying: "
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
			String mesg = "MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Cannot retrieve monitored element ids for measurement setup "
					+ msIdStr;
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
		String msIdStr = measurementSetup.getId().toString();
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
				+ msIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.setModified | Trying: "
					+ sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		} catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.setModified | Cannot set modified for measurement setup "
					+ msIdStr;
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