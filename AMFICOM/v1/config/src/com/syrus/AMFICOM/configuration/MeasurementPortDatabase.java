/*
 * $Id: MeasurementPortDatabase.java,v 1.3 2004/08/16 09:02:05 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.3 $, $Date: 2004/08/16 09:02:05 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public class MeasurementPortDatabase extends StorableObjectDatabase {
	// table :: MeasurementPort

    // type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // kis_id VARCHAR2(32),
    public static final String COLUMN_KIS_ID        = "kis_id";
    // port_id VARCHAR2(32),
    public static final String COLUMN_PORT_ID       = "port_id";
    
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
    
	private MeasurementPort fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPort)
			return (MeasurementPort)storableObject;
		throw new IllegalDataException("MeasurementPortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		this.retrieveMeasurementPort(measurementPort);	
	}

	private void retrieveMeasurementPort(MeasurementPort measurementPort) throws ObjectNotFoundException, RetrieveObjectException{
		String sql;
		String mpIdStr = measurementPort.getId().toSQLString();
		{
		StringBuffer buffer = new StringBuffer();
		buffer.append(StorableObjectDatabase.SQL_SELECT);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_CREATED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_MODIFIED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_CREATOR_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_MODIFIER_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_TYPE_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_KIS_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_PORT_ID);
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(ObjectEntities.MEASUREMENTPORT_ENTITY);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(mpIdStr);
		sql = buffer.toString();
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortDatabase.retrieveMeasurementPort | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String measurementPortTypeIdCode = resultSet.getString(COLUMN_TYPE_ID);				
				
				String name = resultSet.getString(COLUMN_NAME);
				
				String description = resultSet.getString(COLUMN_DESCRIPTION);
				measurementPort.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),								  
								  
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/												
								  new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
								  
								  (measurementPortTypeIdCode != null) ? (MeasurementPortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(measurementPortTypeIdCode), true) : null,
								  (name != null) ? name : "",
								  (description != null) ? description : "",
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_KIS_ID)),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_PORT_ID)));
			}
			else
				throw new ObjectNotFoundException("No such measurement port: " + mpIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortDatabase.retrieveMeasurementPort | Cannot retrieve measurement port " + mpIdStr;
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
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
		
		try {
			this.insertMeasurementPort(measurementPort);			
		} catch (CreateObjectException e) {
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

	private void insertMeasurementPort(MeasurementPort measurementPort) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String mpIdCode = measurementPort.getId().getCode();	

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier typeId = measurementPort.getType().getId();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier kisId = measurementPort.getKISId();
		
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier portId = measurementPort.getPortId();


		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MEASUREMENTPORT_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA	
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_KIS_ID + COMMA
			+ COLUMN_PORT_ID  			
			+ CLOSE_BRACKET
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
			+ QUESTION 
			+ CLOSE_BRACKET;
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(1, mpIdCode);
			preparedStatement.setDate(2, new java.sql.Date(measurementPort.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(measurementPort.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, measurementPort.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, measurementPort.getModifierId().getCode());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(6, (typeId != null)?typeId.getCode():Identifier.getNullSQLString());
			
			preparedStatement.setString(7, measurementPort.getName());
			
			preparedStatement.setString(8, measurementPort.getDescription());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(9, (kisId != null)?kisId.getCode():Identifier.getNullSQLString());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(10, (portId != null)?portId.getCode():Identifier.getNullSQLString());
										
			Log.debugMessage("MeasurementPortDatabase.insertMeasurementPort | Trying: " + sql, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortDatabase.insertMeasurementPort | Cannot insert measurement port " + mpIdCode;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		MeasurementPort measurementPort = this.fromStorableObject(storableObject);
			switch (updateKind) {
				default:
					return;
			}
	}
	
	public static List retrieveAll() throws RetrieveObjectException {
		List ports = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementPortDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				ports.add(new MeasurementPort(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementPortDatabase.retrieveAll | Cannot retrieve measurement port";
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
		return ports;
	}

	public static void delete(MeasurementPort measurementPort) {
		String mpIdStr = measurementPort.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.MEASUREMENTPORT_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ mpIdStr;
			Log.debugMessage("MeasurementPortDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}


}
