/*
 * $Id: EquipmentDatabase.java,v 1.17 2004/08/11 12:37:30 bob Exp $
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
import java.util.Iterator;
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
 * @version $Revision: 1.17 $, $Date: 2004/08/11 12:37:30 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class EquipmentDatabase extends StorableObjectDatabase {
	// table :: Equipment
	 // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // image_id Identifier,
    public static final String COLUMN_IMAGE_ID      = "image_id";
    // kis_id Identifier,
    public static final String COLUMN_KIS_ID        = "kis_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
    // sort NUMBER(2) NOT NULL,
    public static final String COLUMN_SORT  = "sort";
    // type_id Identifier NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    
    // table :: EquipmentMELink
    // equipment_id Identifier,
    public static final String LINK_COLUMN_EQUIPMENT_ID  = "equipment_id";
    // monitored_element_id Identifier,
    public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";


	
	private Equipment fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Equipment)
			return (Equipment)storableObject;
		throw new IllegalDataException("EquipmentDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Equipment eq = this.fromStorableObject(storableObject);
		this.retrieveEquipment(eq);
		this.retrieveEquipmentMELink(eq);
	}

	private void retrieveEquipment(Equipment equipment) throws ObjectNotFoundException, RetrieveObjectException{
		String sql;
		String eqIdStr = equipment.getId().toSQLString();
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
		buffer.append(DomainMember.COLUMN_DOMAIN_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_TYPE_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_IMAGE_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_SORT);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_KIS_ID);
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(ObjectEntities.EQUIPMENT_ENTITY);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(eqIdStr);
		sql = buffer.toString();
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.retrieveEquipment | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change String to long
				 */
				String equipmentTypeIdCode = resultSet.getString(COLUMN_TYPE_ID);
				
				String name = resultSet.getString(COLUMN_NAME);
				String description = resultSet.getString(COLUMN_DESCRIPTION);
				equipment.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
								  new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),
								  
								  (equipmentTypeIdCode != null)?((EquipmentType)ConfigurationObjectTypePool.getObjectType(new Identifier(equipmentTypeIdCode))):null,
								  (name != null)?name:"",
								  (description != null)?description:"",
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_IMAGE_ID)),
								  resultSet.getInt(COLUMN_SORT),
								  /**
									* @todo when change DB Identifier model ,change getString() to getLong()
									*/
								  new Identifier(resultSet.getString(COLUMN_KIS_ID)));
			}
			else
				throw new ObjectNotFoundException("No such equipment: " + eqIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.retrieveEquipment | Cannot retrieve equipment " + eqIdStr;
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

	
	private void retrieveEquipmentMELink(Equipment equipment) throws RetrieveObjectException{
		String sql;
		String eqIdStr = equipment.getId().toSQLString();
		{
		StringBuffer buffer = new StringBuffer();
		buffer.append(StorableObjectDatabase.SQL_SELECT);		
		buffer.append(LINK_COLUMN_MONITORED_ELEMENT_ID);
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(ObjectEntities.EQUIPMENTMELINK_ENTITY);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(LINK_COLUMN_EQUIPMENT_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(eqIdStr);
		sql = buffer.toString();
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.retrieveEquipmentMELink | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			List meLink = new ArrayList();
			while (resultSet.next()) {				
				/**
				* @todo when change DB Identifier model ,change getString() to getLong()
				*/
				Identifier meId = new Identifier(resultSet.getString(LINK_COLUMN_MONITORED_ELEMENT_ID));
				meLink.add(meId);				
			}
			equipment.setMonitoredElementIds(meLink);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.retrieveEquipmentMELink | Cannot retrieve equipment " + eqIdStr;
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
		Equipment equipment = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		
		try {
			this.insertEquipment(equipment);	
			this.insertEquipmentMELinks(equipment);
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

	private void insertEquipment(Equipment equipment) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String eqIdCode = equipment.getId().getCode();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier domainId = equipment.getDomainId();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier typeId = equipment.getType().getId();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier imageId = equipment.getImageId();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier kisId = equipment.getKISId();
		
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EQUIPMENT_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_IMAGE_ID + COMMA
			+ COLUMN_SORT  + COMMA
			+ COLUMN_KIS_ID
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
			preparedStatement.setString(1, eqIdCode);
			preparedStatement.setDate(2, new java.sql.Date(equipment.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(equipment.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, equipment.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, equipment.getModifierId().getCode());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(6, (domainId != null)?domainId.getCode():Identifier.getNullSQLString());

			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(7, (typeId != null)?typeId.getCode():Identifier.getNullSQLString());

			preparedStatement.setString(8, equipment.getName());
			
			preparedStatement.setString(9, equipment.getDescription());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(10, (imageId != null)?imageId.getCode():Identifier.getNullSQLString());
			
			preparedStatement.setInt(11, equipment.getSort());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(12, (kisId != null)?kisId.getCode():Identifier.getNullSQLString());
			
										
			Log.debugMessage("EquipmentDatabase.insertEquipment | Trying: " + sql, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.insertEquipment | Cannot insert equipment " + eqIdCode;
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
	
	private void insertEquipmentMELinks(Equipment equipment)	throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String eqIdCode = equipment.getId().getCode();
		List meIds = equipment.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
					+ ObjectEntities.EQUIPMENTMELINK_ENTITY
					+ OPEN_BRACKET
					+ LINK_COLUMN_EQUIPMENT_ID 
					+ COMMA 
					+ LINK_COLUMN_MONITORED_ELEMENT_ID
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
				preparedStatement.setString(1, eqIdCode);
				meIdCode = ((Identifier) iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				preparedStatement.setString(2, meIdCode);
				Log.debugMessage("EquipmentDatabase.insertEquipmentMELinks | Inserting link for equipment "
								+ eqIdCode
								+ " and monitored element "
								+ meIdCode, Log.DEBUGLEVEL05);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.insertEquipmentMELinks | Cannot insert link for monitored element "
							+ meIdCode + " and Equipment " + eqIdCode;
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

	
	private void createMEAttachment(Equipment equipment,
									Identifier monitoredElementId) throws UpdateObjectException {
		String eqIdStr = equipment.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_INSERT_INTO + ObjectEntities.EQUIPMENTMELINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLUMN_EQUIPMENT_ID
			+ COMMA
			+ LINK_COLUMN_MONITORED_ELEMENT_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET				
			+ eqIdStr 
			+ COMMA 
			+ meIdStr 
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.createMEAttachment | Cannot attach equipment "
				+ eqIdStr + " to monitored element " + meIdStr;
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

	private void deleteMEAttachment(Equipment equipment,
									Identifier monitoredElementId) throws IllegalDataException {
		String eqIdStr = equipment.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_DELETE_FROM 
					+ ObjectEntities.EQUIPMENTMELINK_ENTITY
					+ SQL_WHERE
					+ LINK_COLUMN_EQUIPMENT_ID 
					+ EQUALS 
					+ eqIdStr
					+ SQL_AND
					+ LINK_COLUMN_MONITORED_ELEMENT_ID
					+ EQUALS 
					+ meIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.deleteMEAttachment | Cannot detach equipment "
				+ eqIdStr + " from monitored element " + meIdStr;
			throw new IllegalDataException(mesg, sqle);
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
		Equipment equipment = this.fromStorableObject(storableObject);
		try {
			switch (updateKind) {
				case Equipment.UPDATE_ATTACH_ME:
					this.createMEAttachment(equipment, (Identifier) obj);
					this.setModified(equipment);
					break;
				case Equipment.UPDATE_DETACH_ME:
					this.deleteMEAttachment(equipment, (Identifier) obj);
					this.setModified(equipment);
					break;
				default:
					return;
			}
		} catch (UpdateObjectException e) {
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

	private void setModified(Equipment equipment) throws UpdateObjectException {		
		String eqIdStr = equipment.getId().toSQLString();
		String sql = SQL_UPDATE
					+ ObjectEntities.EQUIPMENT_ENTITY
					+ SQL_SET
					+ COLUMN_MODIFIED + EQUALS
					+ DatabaseDate.toUpdateSubString(equipment.getModified()) + COMMA
					+ COLUMN_MODIFIER_ID + EQUALS + equipment.getModifierId().toSQLString()
					+ SQL_WHERE + EQUALS + eqIdStr;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.setModified | Cannot set modified for equipment " + eqIdStr;
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
