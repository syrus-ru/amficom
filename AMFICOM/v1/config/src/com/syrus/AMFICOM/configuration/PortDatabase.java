/*
 * $Id: PortDatabase.java,v 1.5 2004/08/11 16:45:02 arseniy Exp $
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
 * @version $Revision: 1.5 $, $Date: 2004/08/11 16:45:02 $
 * @author $Author: arseniy $
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
    
	private Port fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Port)
			return (Port)storableObject;
		throw new IllegalDataException("PortDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Port port = this.fromStorableObject(storableObject);
		this.retrievePort(port);	
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
			Log.debugMessage("PortDatabase.retrievePort | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String portTypeIdCode = resultSet.getString(COLUMN_TYPE_ID);				
				
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
								  
								  (portTypeIdCode != null) ? (PortType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(portTypeIdCode), true) : null,								  
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

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier equipmentId = port.getEquipmentId();

		String sql = SQL_INSERT_INTO
			+ ObjectEntities.PORT_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA			
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_EQUIPMENT_ID + COMMA
			+ COLUMN_SORT  			
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
			+ QUESTION 
			+ CLOSE_BRACKET;
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(1, portIdCode);
			preparedStatement.setDate(2, new java.sql.Date(port.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(port.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, port.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, port.getModifierId().getCode());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(6, (typeId != null)?typeId.getCode():Identifier.getNullSQLString());
			
			preparedStatement.setString(7, port.getDescription());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(8, (equipmentId != null)?equipmentId.getCode():Identifier.getNullSQLString());
			
			preparedStatement.setInt(9, port.getSort());			
										
			Log.debugMessage("PortDatabase.insertPort | Trying: " + sql, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "PortDatabase.insertPort | Cannot insert port " + portIdCode;
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
		Port port = this.fromStorableObject(storableObject);
			switch (updateKind) {
				default:
					return;
			}
	}

}
