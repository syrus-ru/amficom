/*
 * $Id: PortDatabase.java,v 1.12 2004/08/26 11:09:26 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;


/**
 * @version $Revision: 1.12 $, $Date: 2004/08/26 11:09:26 $
 * @author $Author: bob $
 * @module configuration_v1
 */
public class PortDatabase extends StorableObjectDatabase {
	// table :: Port

    // type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
	// description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // equipment_id VARCHAR2(32),
    public static final String COLUMN_EQUIPMENT_ID  = "equipment_id";
    // sort NUMBER(2) NOT NULL,
    public static final String COLUMN_SORT  = "sort";
    
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
    
	private Port fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Port)
			return (Port)storableObject;
		throw new IllegalDataException("PortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Port port = this.fromStorableObject(storableObject);
		this.retrievePort(port);
		port.setCharacteristics(CharacteristicDatabase.retrieveCharacteristics(port.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
	}

	private void retrievePort(Port port) throws ObjectNotFoundException, RetrieveObjectException{
		String sql;
		String portIdStr = port.getId().toSQLString();
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
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_EQUIPMENT_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SORT);
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(ObjectEntities.PORT_ENTITY);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(portIdStr);
		sql = buffer.toString();
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("PortDatabase.retrievePort | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				PortType portType;
				try {
					/**
					 * @todo when change DB Identifier model ,change String to long
					 */
					String portTypeIdCode = resultSet.getString(COLUMN_TYPE_ID);
					portType = (portTypeIdCode != null) ? (PortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(portTypeIdCode), true) : null;
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				
				String description = resultSet.getString(COLUMN_DESCRIPTION);
				port.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),								  
								  
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/												
								  new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
								  
								  portType,								  
								  (description != null) ? description : "",
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_EQUIPMENT_ID)),
								  resultSet.getInt(COLUMN_SORT));
			}
			else
				throw new ObjectNotFoundException("No such port: " + portIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "PortDatabase.retrievePort | Cannot retrieve port " + portIdStr;
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
		Port port = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Port port = this.fromStorableObject(storableObject);
		
		try {
			this.insertPort(port);			
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

	private void insertPort(Port port) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String portIdCode = port.getId().getCode();	

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier typeId = port.getType().getId();

		Identifier equipmentId = port.getEquipmentId();
		
		String sql = null;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.MEASUREMENTPORT_ENTITY);
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
			buffer.append(COLUMN_TYPE_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(COMMA);
			buffer.append(COLUMN_EQUIPMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_SORT);
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);
			buffer.append(portIdCode);
			buffer.append(COMMA);			
			buffer.append(DatabaseDate.toUpdateSubString(port.getCreated()));
			buffer.append(COMMA);
			buffer.append(DatabaseDate.toUpdateSubString(port.getModified()));
			buffer.append(COMMA);
			buffer.append(port.getCreatorId().toSQLString());
			buffer.append(COMMA);
			buffer.append(port.getModifierId().toSQLString());
			buffer.append(COMMA);
			buffer.append((typeId != null)?typeId.getCode():Identifier.getNullSQLString());
			buffer.append(COMMA);
			buffer.append(APOSTOPHE);
			buffer.append(port.getDescription());
			buffer.append(APOSTOPHE);
			buffer.append(COMMA);
			buffer.append((equipmentId != null)?equipmentId.getCode():Identifier.getNullSQLString());
			buffer.append(COMMA);
			buffer.append(port.getSort());
			buffer.append(COMMA);
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("PortDatabase.insertPort | Trying: " + sql, Log.DEBUGLEVEL09);			
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "PortDatabase.insertPort | Cannot insert port " + portIdCode;
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Port port = this.fromStorableObject(storableObject);
			switch (updateKind) {
				default:
					return;
			}
	}
	
	public static List retrieveAll() throws RetrieveObjectException {
		List ports = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.PORT_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("PortDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				ports.add(new Port(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "PortDatabase.retrieveAll | Cannot retrieve port";
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

	public static void delete(Port port) {
		String portIdStr = port.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.PORT_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ portIdStr;
			Log.debugMessage("PortDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
