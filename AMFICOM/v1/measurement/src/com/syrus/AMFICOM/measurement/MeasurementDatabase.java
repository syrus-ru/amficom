/*
 * $Id: MeasurementDatabase.java,v 1.27 2004/10/05 13:02:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.27 $, $Date: 2004/10/05 13:02:17 $
 * @author $Author: bob $
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
	
	private String updateColumns;	
	private String updateMultiplySQLValues;	
	
	protected String getEnityName() {		
		return "Measurement";
	}
	
	
	protected String getTableName() {
		return ObjectEntities.MEASUREMENT_ENTITY;
	}	

	private Measurement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Measurement)
			return (Measurement)storableObject;
		throw new IllegalDataException("MeasurementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ COLUMN_SETUP_ID + COMMA
				+ COLUMN_START_TIME + COMMA
				+ COLUMN_DURATION + COMMA
				+ COLUMN_STATUS + COMMA
				+ COLUMN_LOCAL_ADDRESS + COMMA
				+ COLUMN_TEST_ID;
		}
		return this.updateColumns;
	}	
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Measurement measurement = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ measurement.getType().getId().toSQLString() + COMMA
			+ measurement.getMonitoredElementId().toSQLString() + COMMA
			+ measurement.getSetup().getId().toSQLString() + COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getStartTime()) + COMMA
			+ Long.toString(measurement.getDuration()) + COMMA
			+ Integer.toString(measurement.getStatus().value()) + COMMA
			+ APOSTOPHE + measurement.getLocalAddress() + APOSTOPHE + COMMA
			+ measurement.getTestId().toSQLString();
		return values;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		Measurement measurement = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {			
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(++i, measurement.getType().getId().getCode()); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(++i, measurement.getMonitoredElementId().getCode()); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(++i, measurement.getSetup().getId().getCode());
			preparedStatement.setTimestamp(++i, new Timestamp(measurement.getStartTime().getTime()));
			preparedStatement.setLong(++i, measurement.getDuration());
			preparedStatement.setInt(++i, measurement.getStatus().value());
			preparedStatement.setString(++i, measurement.getLocalAddress());
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(++i, measurement.getTestId().getCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurement);
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_SETUP_ID + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_START_TIME) + COMMA
			+ COLUMN_DURATION + COMMA
			+ COLUMN_STATUS + COMMA
			+ COLUMN_LOCAL_ADDRESS + COMMA
			+ COLUMN_TEST_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		Measurement measurement = (storableObject == null) ?
				new Measurement(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 
								   null, null, null, null) : 
					fromStorableObject(storableObject);		
		MeasurementType measurementType;
		MeasurementSetup measurementSetup;
		try {
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			measurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_SETUP_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
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
		return measurement;
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
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.retrieveResult | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				try {
					return (Result)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ID)), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
			throw new ObjectNotFoundException("No result of sort: " + resultSortNum + " for measurement " + measurementIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.retrieveResult | Cannot retrieve result of sort " + resultSortNum + " for measurement '" + measurementIdStr + "' -- " + sqle.getMessage();
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
			} finally {
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.insertEntity(measurement);
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws  IllegalDataException, VersionCollisionException, UpdateObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Measurement.UPDATE_STATUS:
				this.updateStatus(measurement);
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);					
				return;
		}
	}	
	
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case Measurement.UPDATE_STATUS:
				for(Iterator it=storableObjects.iterator();it.hasNext();){
					/**
					 * FIXME recast using one step
					 */
					Measurement measurement = (Measurement)it.next();
					this.updateStatus(measurement);
				}				
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);					
				return;
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
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.updateStatus | Cannot update status of measurement '" + measurementIdStr + "' -- " + sqle.getMessage();
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
	}	
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, condition);
		return retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids, condition);
	}
    
	public List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
                + SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
                + DomainMember.COLUMN_DOMAIN_ID + EQUALS + domain.getId().toSQLString()
            + CLOSE_BRACKET;
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("MeasurementDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }
	
	public List retrieveButIdsByTest(List ids, List testIds) throws RetrieveObjectException {
        List list = null;
        
        StringBuffer buffer = new StringBuffer();
        int idsLength = testIds.size();
        if ((testIds != null) && (idsLength > 0)){
        	buffer.append(COLUMN_TEST_ID);
        	buffer.append(SQL_IN);
        	buffer.append(OPEN_BRACKET);
        	int i = 1;         
			for (Iterator it = testIds.iterator(); it.hasNext(); i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identified)
					id = ((Identified)object).getId();
				else throw new RetrieveObjectException("MeasurementDatabase.retrieveButIdsByTest | Object " +
													object.getClass().getName()
													+ " isn't Identifier or Identified");

				if (id != null){
					buffer.append(id.toSQLString());
					if (i < idsLength)
						buffer.append(COMMA);
				}
			}
        	buffer.append(CLOSE_BRACKET);
        }
        
        try {
            list = retrieveButIds(ids, buffer.toString());
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("MeasurementDatabase.retrieveButIdsByTest | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }

}
