/*
 * $Id: EquipmentDatabase.java,v 1.28 2004/08/30 14:39:41 bob Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.28 $, $Date: 2004/08/30 14:39:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class EquipmentDatabase extends StorableObjectDatabase {
	// table :: Equipment
	 // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // image_id Identifier,
    public static final String COLUMN_IMAGE_ID      = "image_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
    // type_id Identifier NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    
    // table :: EquipmentMELink
    // equipment_id Identifier,
    public static final String LINK_COLUMN_EQUIPMENT_ID  = "equipment_id";
    // monitored_element_id Identifier,
    public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private Equipment fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Equipment)
			return (Equipment)storableObject;
		throw new IllegalDataException("EquipmentDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	private String retrieveEquipmentQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ DomainMember.COLUMN_DOMAIN_ID + COMMA
		+ COLUMN_TYPE_ID + COMMA
		+ COLUMN_NAME + COMMA
		+ COLUMN_DESCRIPTION + COMMA
		+ COLUMN_IMAGE_ID
		+ SQL_FROM + ObjectEntities.EQUIPMENT_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private Equipment updateEquipmentFromResultSet(Equipment equipment, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		Equipment equipment1 = equipment;
		if (equipment == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			equipment1 = new Equipment(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null,
									   null, null);			
		}
		String name = resultSet.getString(COLUMN_NAME);
		String description = resultSet.getString(COLUMN_DESCRIPTION);
		EquipmentType equipmentType;
		try {
			equipmentType = (EquipmentType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		equipment1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
														equipmentType,
														(name != null) ? name : "",
														(description != null) ? description : "",
														/**
															* @todo when change DB Identifier model ,change getString() to getLong()
															*/
														new Identifier(resultSet.getString(COLUMN_IMAGE_ID)));

		
		return equipment1;
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Equipment equipment = this.fromStorableObject(storableObject);
		this.retrieveEquipment(equipment);
		this.retrieveEquipmentPortIds(equipment);
		this.retrieveEquipmentMEIds(equipment);
		equipment.setCharacteristics(characteristicDatabase.retrieveCharacteristics(equipment.getId(), CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT));
	}

	private void retrieveEquipment(Equipment equipment) throws ObjectNotFoundException, RetrieveObjectException {
		String eqIdStr = equipment.getId().toSQLString();

		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_IMAGE_ID
			+ SQL_FROM + ObjectEntities.EQUIPMENT_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + eqIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.retrieveEquipment | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateEquipmentFromResultSet(equipment, resultSet);
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
	
	private void retrieveEquipmentPortIds(Equipment equipment) throws RetrieveObjectException {
		List portIds = new ArrayList();
		String eqIdStr = equipment.getId().toSQLString();

		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.PORT_ENTITY
			+ SQL_WHERE + PortDatabase.COLUMN_EQUIPMENT_ID + EQUALS + eqIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.retrieveEquipmentPortIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {				
				/**
				* @todo when change DB Identifier model ,change getString() to getLong()
				*/
				portIds.add(new Identifier(resultSet.getString(COLUMN_ID)));				
			}
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.retrieveEquipmentPortIds | Cannot retrieve port ids for equipment " + eqIdStr;
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
		equipment.setPortIds(portIds);
	}

	private void retrieveEquipmentMEIds(Equipment equipment) throws RetrieveObjectException {
		List meIds = new ArrayList();
		String eqIdStr = equipment.getId().toSQLString();

		String sql = SQL_SELECT
			+ LINK_COLUMN_MONITORED_ELEMENT_ID
			+ SQL_FROM + ObjectEntities.EQUIPMENTMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_EQUIPMENT_ID + EQUALS + eqIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.retrieveEquipmentMEIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				/**
				* @todo when change DB Identifier model ,change getString() to getLong()
				*/
				meIds.add(new Identifier(resultSet.getString(LINK_COLUMN_MONITORED_ELEMENT_ID)));				
			}
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.retrieveEquipmentMEIds | Cannot retrieve equipment " + eqIdStr;
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
		equipment.setMonitoredElementIds(meIds);
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

	private void insertEquipment(Equipment equipment) throws CreateObjectException {
		String eqIdStr = equipment.getId().toSQLString();

		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EQUIPMENT_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_IMAGE_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ eqIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(equipment.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(equipment.getModified()) + COMMA
			+ equipment.getCreatorId().toSQLString() + COMMA
			+ equipment.getModifierId().toSQLString() + COMMA
			+ equipment.getDomainId().toSQLString() + COMMA
			+ equipment.getType().getId().toSQLString() + COMMA
			+ APOSTOPHE + equipment.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + equipment.getDescription() + APOSTOPHE + COMMA
			+ equipment.getImageId().toSQLString()
			+ CLOSE_BRACKET;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.insertEquipment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.insertEquipment | Cannot insert equipment " + eqIdStr;
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
//
//	private void insertEquipmentMELinks(Equipment equipment)	throws CreateObjectException {
//		/**
//		 * @todo when change DB Identifier model ,change String to long
//		 */
//		String eqIdCode = equipment.getId().getCode();
//		List meIds = equipment.getMonitoredElementIds();
//		String sql = SQL_INSERT_INTO 
//					+ ObjectEntities.EQUIPMENTMELINK_ENTITY + OPEN_BRACKET
//					+ LINK_COLUMN_EQUIPMENT_ID + COMMA 
//					+ LINK_COLUMN_MONITORED_ELEMENT_ID
//					+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
//					+ QUESTION + COMMA
//					+ QUESTION
//					+ CLOSE_BRACKET;
//		PreparedStatement preparedStatement = null;
//		/**
//		 * @todo when change DB Identifier model ,change String to long
//		 */
//		String meIdCode = null;
//		try {
//			preparedStatement = connection.prepareStatement(sql);
//			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
//				/**
//				 * @todo when change DB Identifier model ,change setString() to
//				 *       setLong()
//				 */
//				preparedStatement.setString(1, eqIdCode);
//				meIdCode = ((Identifier) iterator.next()).getCode();
//				/**
//				 * @todo when change DB Identifier model ,change setString() to
//				 *       setLong()
//				 */
//				preparedStatement.setString(2, meIdCode);
//				Log.debugMessage("EquipmentDatabase.insertEquipmentMELinks | Inserting link for equipment "
//								+ eqIdCode
//								+ " and monitored element "
//								+ meIdCode, Log.DEBUGLEVEL09);
//				preparedStatement.executeUpdate();
//			}
//		}
//		catch (SQLException sqle) {
//			String mesg = "EquipmentDatabase.insertEquipmentMELinks | Cannot insert link for monitored element " + meIdCode + " and Equipment " + eqIdCode;
//			throw new CreateObjectException(mesg, sqle);
//		}
//		finally {
//			try {
//				if (preparedStatement != null)
//					preparedStatement.close();
//				preparedStatement = null;
//			}
//			catch (SQLException sqle1) {
//				Log.errorException(sqle1);
//			}
//		}
//	}


	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	private void setModified(Equipment equipment) throws UpdateObjectException {		
		String eqIdStr = equipment.getId().toSQLString();
		String sql = SQL_UPDATE
			+ ObjectEntities.EQUIPMENT_ENTITY + SQL_SET
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(equipment.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + equipment.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS + eqIdStr;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
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

	public void delete(Equipment equipment) {
		String eqIdStr = equipment.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
				+ ObjectEntities.EQUIPMENT_ENTITY
				+ SQL_WHERE
					+ COLUMN_ID + EQUALS + eqIdStr;
			Log.debugMessage("EquipmentDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
	
	public List retrieveAll() throws RetrieveObjectException {		
		return retriveByIdsOneQuery(null);
	}
	
	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null);
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			String condition = null;
			if (ids!=null){
				StringBuffer buffer = new StringBuffer(COLUMN_ID);
				int idsLength = ids.size();
				if (idsLength == 1){
					buffer.append(EQUALS);
					buffer.append(((Identifier)ids.iterator().next()).toSQLString());
				} else{
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);
					
					int i = 1;
					for(Iterator it=ids.iterator();it.hasNext();i++){
						Identifier id = (Identifier)it.next();
						buffer.append(id.toSQLString());
						if (i < idsLength)
							buffer.append(COMMA);
					}
					
					buffer.append(CLOSE_BRACKET);
					condition = buffer.toString();
				}
			}
			sql = retrieveEquipmentQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EquipmentDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateEquipmentFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
		return result;
	}
	
	private List retriveByIdsPreparedStatement(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			
			int idsLength = ids.size();
			if (idsLength == 1){
				return retriveByIdsOneQuery(ids);
			}
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			buffer.append(EQUALS);							
			buffer.append(QUESTION);
			
			sql = retrieveEquipmentQuery(buffer.toString());
		}
			
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement(sql.toString());
			for(Iterator it = ids.iterator();it.hasNext();){
				Identifier id = (Identifier)it.next(); 
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				String idStr = id.getIdentifierString();
				stmt.setString(1, idStr);
				resultSet = stmt.executeQuery();
				if (resultSet.next()){
					result.add(updateEquipmentFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("EquipmentDatabase.retriveByIdsPreparedStatement | No such equipment: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.retriveByIdsPreparedStatement | Cannot retrieve equipment " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt != null)
					stmt.close();
				stmt = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}			
		
		return result;
	}

}
