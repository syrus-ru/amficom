/*
 * $Id: MeasurementDatabase.java,v 1.12 2004/08/06 16:07:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.12 $, $Date: 2004/08/06 16:07:06 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementDatabase extends StorableObjectDatabase {
	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_SETUP_ID = "setup_id";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_LOCAL_ADDRESS = "local_address";
	public static final String COLUMN_TEST_ID = "test_id";
	
	public static final String LINK_COLUMN_MEASUREMENT_ID = "measurement_id";
	public static final String LINK_SORT = "sort";

	private Measurement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Measurement)
			return (Measurement)storableObject;
		throw new IllegalDataException("MeasurementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.retrieveMeasurement(measurement);
	}

	private void retrieveMeasurement(Measurement measurement) throws RetrieveObjectException, ObjectNotFoundException {
		String measurementIdStr = measurement.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_SETUP_ID + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_START_TIME) + COMMA
			+ COLUMN_DURATION + COMMA
			+ COLUMN_STATUS + COMMA
			+ COLUMN_LOCAL_ADDRESS + COMMA
			+ COLUMN_TEST_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + measurementIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				MeasurementType measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				MeasurementSetup measurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_SETUP_ID)), true);
				measurement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
											DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
											/**
											 * @todo when change DB Identifier model ,change getString() to getLong()
											 */											
											new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
											/**
											 * @todo when change DB Identifier model ,change getString() to getLong()
											 */
											new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
											measurementType,
											/**
											 * @todo when change DB Identifier model ,change getString() to getLong()
											 */
											new Identifier(resultSet.getString(COLUMN_MONITORED_ELEMENT_ID)),
											measurementSetup,
											DatabaseDate.fromQuerySubString(resultSet, COLUMN_START_TIME),
											resultSet.getLong(COLUMN_DURATION),
											resultSet.getInt(COLUMN_STATUS),
											resultSet.getString(COLUMN_LOCAL_ADDRESS),
											/**
											 * @todo when change DB Identifier model ,change getString() to getLong()
											 */
											new Identifier(resultSet.getString(COLUMN_TEST_ID)));
			}
			else
				throw new ObjectNotFoundException("No such measurement: " + measurementIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.retrieve | Cannot retrieve measurement " + measurementIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Measurement.RETRIEVE_RESULT:
				return this.retrieveResult(measurement, (ResultSort)arg);
			default:
				return null;
		}
	}

	private Result retrieveResult(Measurement measurement, ResultSort resultSort) throws ObjectNotFoundException, RetrieveObjectException {
		String measurementIdStr = measurement.getId().toSQLString();
		int resultSortNum = resultSort.value();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.RESULT_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_ID + EQUALS + measurementIdStr
			+ SQL_AND + LINK_SORT + EQUALS + Integer.toString(resultSortNum);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.retrieveResult | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				return (Result)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ID)), true);
			}
			throw new ObjectNotFoundException("No result of sort: " + resultSortNum + " for measurement " + measurementIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.retrieveResult | Cannot retrieve result of sort " + resultSortNum + " for measurement " + measurementIdStr;
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

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Measurement measurement = this.fromStorableObject(storableObject);
		try {
			this.insertMeasurement(measurement);
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

	private void insertMeasurement(Measurement measurement) throws CreateObjectException {
		String measurementIdStr = measurement.getId().toSQLString();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.MEASUREMENT_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_SETUP_ID + COMMA
			+ COLUMN_START_TIME + COMMA
			+ COLUMN_DURATION + COMMA
			+ COLUMN_STATUS + COMMA
			+ COLUMN_LOCAL_ADDRESS + COMMA
			+ COLUMN_TEST_ID + COMMA
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ measurementIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getModified()) + COMMA
			+ measurement.getCreatorId().toSQLString() + COMMA
			+ measurement.getModifierId().toSQLString() + COMMA
			+ measurement.getType().getId().toSQLString() + COMMA
			+ measurement.getMonitoredElementId().toSQLString() + COMMA
			+ measurement.getSetup().getId().toSQLString() + COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getStartTime()) + COMMA
			+ Long.toString(measurement.getDuration()) + COMMA
			+ Integer.toString(measurement.getStatus().value()) + COMMA
			+ APOSTOPHE + measurement.getLocalAddress() + APOSTOPHE + COMMA
			+ measurement.getTestId().toSQLString()
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.insert | Cannot insert measurement " + measurementIdStr;
			throw new CreateObjectException(mesg, sqle);
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws  IllegalDataException, UpdateObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		try {
			switch (updateKind) {
				case Measurement.UPDATE_STATUS:
					this.updateStatus(measurement);
					break;
				default:
					return;
			}
		}
		catch (UpdateObjectException e) {
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

	private void updateStatus(Measurement measurement) throws UpdateObjectException {
		String measurementIdStr = measurement.getId().toSQLString();
		String sql = SQL_UPDATE
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_SET
			+ COLUMN_STATUS + EQUALS + Integer.toString(measurement.getStatus().value()) + COMMA
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(measurement.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + measurement.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS + measurementIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.updateStatus | Cannot update status of measurement " + measurementIdStr;
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
}
