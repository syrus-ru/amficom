package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

public class MeasurementDatabase extends StorableObjectDatabase {
//   typeId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_TYPE_ID				=	"typeId";
//	 monitoredElementId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_MONITORED_ELEMENT_ID	=	"monitoredElementId";
//	 setupId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_SETUP_ID				= 	"setupId";
//	 startTime DATE NOT NULL,
	public static final String COLUMN_START_TIME			= 	"startTime";
//	 duration NUMBER(20) NOT NULL,
	public static final String COLUMN_DURATION				=	"duration";
//	 status NUMBER(2, 0) NOT NULL,
	public static final String COLUMN_STATUS				=	"status";
//	 localAddress VARCHAR2(64) NOT NULL,
	public static final String COLUMN_LOCAL_ADDRESS			=	"localAddress";
//	 testId VARCHAR2(32) NOT NULL,
	public static final String COLUMN_TEST_ID				=	"testId";
	
	public static final String LINK_COLUMN_MEASUREMENT_ID	=	"measurementId";
	public static final String LINK_SORT					=	"sort";

	private Measurement fromStorableObject(StorableObject storableObject) throws Exception {
		if (storableObject instanceof Measurement)
			return (Measurement)storableObject;
		else
			throw new Exception("MeasurementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws Exception {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.retrieveMeasurement(measurement);
	}

	private void retrieveMeasurement(Measurement measurement) throws Exception {
		String measurementIdStr = measurement.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) 
			+ COMMA 
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) 
			+ COMMA
			+ COLUMN_CREATOR_ID
			+ COMMA
			+ COLUMN_MODIFIER_ID
			+ COMMA
			+ COLUMN_TYPE_ID
			+ COMMA
			+ COLUMN_MONITORED_ELEMENT_ID
			+ COMMA
			+ COLUMN_SETUP_ID
			+ COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_START_TIME)
			+ COMMA
			+ COLUMN_DURATION
			+ COMMA
			+ COLUMN_STATUS
			+ COMMA
			+ COLUMN_LOCAL_ADDRESS
			+ COMMA
			+ COLUMN_TEST_ID
			+ SQL_WHERE
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE
			+ COLUMN_ID
			+ EQUALS
			+ measurementIdStr;
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
				MeasurementSetup measurementSetup = new MeasurementSetup(new Identifier(resultSet.getString(COLUMN_SETUP_ID)));
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
											/**
											 * @todo when change DB Identifier model ,change getString() to getLong()
											 */
											new Identifier(resultSet.getString(COLUMN_TYPE_ID)),
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
				throw new Exception("No such measurement: " + measurementIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.retrieve | Cannot retrieve measurement " + measurementIdStr;
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws Exception {
		Measurement measurement = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Measurement.RETRIEVE_RESULT:
				return this.retrieveResult(measurement, (ResultSort)arg);
			default:
				return null;
		}
	}

	private Result retrieveResult(Measurement measurement, ResultSort resultSort) throws Exception {
		String measurementIdStr = measurement.getId().toSQLString();
		int resultSortNum = resultSort.value();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.RESULT_ENTITY
			+ SQL_WHERE		
			+ LINK_COLUMN_MEASUREMENT_ID
			+ EQUALS
			+ measurementIdStr
			+ SQL_AND
			+ LINK_SORT
			+ EQUALS
			+ Integer.toString(resultSortNum);
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
				return new Result(new Identifier(resultSet.getString(COLUMN_ID)));
			}
			else	
				throw new Exception("No result of sort: " + resultSortNum + " for measurement " + measurementIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.retrieveResult | Cannot retrieve result of sort " + resultSortNum + " for measurement " + measurementIdStr;
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
		String measurementIdStr = measurement.getId().toSQLString();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID
			+ COMMA
			+ COLUMN_CREATED
			+ COMMA
			+ COLUMN_MODIFIED
			+ COMMA
			+ COLUMN_CREATOR_ID
			+ COMMA
			+ COLUMN_MODIFIER_ID
			+ COMMA
			+ COLUMN_TYPE_ID
			+ COMMA
			+ COLUMN_MONITORED_ELEMENT_ID
			+ COMMA
			+ COLUMN_SETUP_ID
			+ COMMA
			+ COLUMN_START_TIME
			+ COMMA
			+ COLUMN_DURATION
			+ COMMA
			+ COLUMN_STATUS
			+ COMMA
			+ COLUMN_LOCAL_ADDRESS
			+ COMMA
			+ COLUMN_TEST_ID
			+ COMMA
			+ CLOSE_BRACKET		
			+ SQL_VALUES
			+ OPEN_BRACKET
			+ measurementIdStr 
			+ COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getCreated()) 
			+ COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getModified()) 
			+ COMMA
			+ measurement.getCreatorId().toSQLString() 
			+ COMMA
			+ measurement.getModifierId().toSQLString() 
			+ COMMA
			+ measurement.getTypeId().toSQLString() 
			+ COMMA
			+ measurement.getMonitoredElementId().toSQLString() 
			+ COMMA
			+ measurement.getSetup().getId().toSQLString() 
			+ COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getStartTime()) 
			+ COMMA
			+ Long.toString(measurement.getDuration()) 
			+ COMMA
			+ Integer.toString(measurement.getStatus().value())
			+ COMMA
			+ APOSTOPHE			
			+ measurement.getLocalAddress()
			+ APOSTOPHE
			+ COMMA
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws Exception {
		Measurement measurement = this.fromStorableObject(storableObject);
		try {
			switch (updateKind) {
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
		String measurementIdStr = measurement.getId().toSQLString();
		String sql = SQL_UPDATE
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_SET
			+ COLUMN_STATUS
			+ EQUALS
			+ Integer.toString(measurement.getStatus().value()) + COMMA
			+ COLUMN_MODIFIED 
			+ EQUALS
			+ DatabaseDate.toUpdateSubString(measurement.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID			
			+ measurement.getModifierId().toSQLString()
			+ SQL_WHERE
			+ COLUMN_ID + EQUALS			
			+ measurementIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.updateStatus | Cannot update status of measurement " + measurementIdStr;
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