/*
 * $Id: EquipmentDatabase.java,v 1.41 2004/11/04 13:33:04 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.41 $, $Date: 2004/11/04 13:33:04 $
 * @author $Author: max $
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
	
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	private Equipment fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Equipment)
			return (Equipment)storableObject;
		throw new IllegalDataException("EquipmentDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return "Equipment";
	}
	
	protected String getTableName() {
		return ObjectEntities.EQUIPMENT_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_IMAGE_ID;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateColumns == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA 
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Equipment equipment = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ equipment.getDomainId().toSQLString() + COMMA
			+ equipment.getType().getId().toSQLString() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getDescription()) + APOSTOPHE + COMMA
			+ equipment.getImageId().toSQLString();
		return sql;
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_IMAGE_ID
			+ SQL_FROM + ObjectEntities.EQUIPMENT_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		Equipment equipment = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i , equipment.getDomainId().getCode());
			preparedStatement.setString( ++i, equipment.getType().getId().getCode());
			preparedStatement.setString( ++i, equipment.getName());
			preparedStatement.setString( ++i, equipment.getDescription());
			preparedStatement.setString( ++i, equipment.getImageId().toString());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("EquipmentDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Equipment equipment = storableObject == null ? null : fromStorableObject(storableObject);
		if (equipment == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			equipment = new Equipment(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null,
									   null, null);			
		}
		String name = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		EquipmentType equipmentType;
		try {
			equipmentType = (EquipmentType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
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
														equipmentType,
														(name != null) ? name : "",
														(description != null) ? description : "",
														/**
															* @todo when change DB Identifier model ,change getString() to getLong()
															*/
														new Identifier(resultSet.getString(COLUMN_IMAGE_ID)));

		
		return equipment;
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Equipment equipment = this.fromStorableObject(storableObject);
		super.retrieveEntity(equipment);
		this.retrieveEquipmentPortIds(equipment);
		this.retrieveEquipmentMEIds(equipment);
		equipment.setCharacteristics(characteristicDatabase.retrieveCharacteristics(equipment.getId(), CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT));
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
		Connection connection = DatabaseConnection.getConnection();
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
		} finally {
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
		equipment.setPortIds(portIds);
	}
    
    private void retrieveEquipmentPortIdsByOneQuery(List equipments) throws RetrieveObjectException {
    	if ((equipments == null) || (equipments.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + COLUMN_ID + COMMA
                + PortDatabase.COLUMN_EQUIPMENT_ID
                + SQL_FROM + ObjectEntities.PORT_ENTITY
                + SQL_WHERE + PortDatabase.COLUMN_EQUIPMENT_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = equipments.iterator(); it.hasNext();i++) {
            Equipment equipment = (Equipment)it.next();
            sql.append(equipment.getId().toSQLString());
            if (it.hasNext()){
                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
                    sql.append(COMMA);
                else {
                    sql.append(CLOSE_BRACKET);
                    sql.append(SQL_OR);
                    sql.append(COLUMN_ID);
                    sql.append(SQL_IN);
                    sql.append(OPEN_BRACKET);
                }                   
            }
        }
        sql.append(CLOSE_BRACKET);
        
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = DatabaseConnection.getConnection();
        try {
            statement = connection.createStatement();
            Log.debugMessage("EquipmentDatabase.retrieveEquipmentPortIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map epIdMap = new HashMap();
            while (resultSet.next()) {
                Equipment equipment = null;
                String equipmentId = resultSet.getString(PortDatabase.COLUMN_EQUIPMENT_ID);
                for (Iterator it = equipments.iterator(); it.hasNext();) {
                    Equipment equipmentToCompare = (Equipment) it.next();
                    if (equipmentToCompare.getId().getIdentifierString().equals(equipmentId)){
                        equipment = equipmentToCompare;
                        break;
                    }                   
                }
                
                if (equipment == null){
                    String mesg = "EquipmentDatabase.retrieveEquipmentPortIdsByOneQuery | Cannot found correspond result for '" + equipmentId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                
                /**
                 * @todo when change DB Identifier model ,change getString() to
                 *       getLong()
                 */
                Identifier epId = new Identifier(resultSet.getString(COLUMN_ID));
                List epIds = (List)epIdMap.get(equipment);
                if (epIds == null){
                    epIds = new LinkedList();
                    epIdMap.put(equipment, epIds);
                }               
                epIds.add(epId);              
            }
            
            for (Iterator iter = equipments.iterator(); iter.hasNext();) {
                Equipment equipment = (Equipment) iter.next();
                List epIds = (List)epIdMap.get(equipment);
                equipment.setPortIds(epIds);
            }
            
        } catch (SQLException sqle) {
            String mesg = "TestDatabase.retrieveMeasurementSetupTestLinksByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
            throw new RetrieveObjectException(mesg, sqle);
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException sqle1) {
                Log.errorException(sqle1);
            } finally {
                DatabaseConnection.closeConnection(connection);
            }
        }
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
		Connection connection = DatabaseConnection.getConnection();
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
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
		equipment.setMonitoredElementIds(meIds);
	}
    
    private void retrieveEquipmentMEIdsByOneQuery(List equipments) throws RetrieveObjectException {
    	if ((equipments == null) || (equipments.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLUMN_MONITORED_ELEMENT_ID + COMMA
                + LINK_COLUMN_EQUIPMENT_ID
                + SQL_FROM + ObjectEntities.EQUIPMENTMELINK_ENTITY
                + SQL_WHERE + LINK_COLUMN_EQUIPMENT_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = equipments.iterator(); it.hasNext();i++) {
            Equipment equipment = (Equipment)it.next();
            sql.append(equipment.getId().toSQLString());
            if (it.hasNext()){
                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
                    sql.append(COMMA);
                else {
                    sql.append(CLOSE_BRACKET);
                    sql.append(SQL_OR);
                    sql.append(COLUMN_ID);
                    sql.append(SQL_IN);
                    sql.append(OPEN_BRACKET);
                }                   
            }
        }
        sql.append(CLOSE_BRACKET);
        
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = DatabaseConnection.getConnection();
        try {
            statement = connection.createStatement();
            Log.debugMessage("EquipmentDatabase.retrieveEquipmentMEIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map meIdMap = new HashMap();
            while (resultSet.next()) {
                Equipment equipment = null;
                String equipmentId = resultSet.getString(LINK_COLUMN_EQUIPMENT_ID);
                for (Iterator it = equipments.iterator(); it.hasNext();) {
                    Equipment equipmentToCompare = (Equipment) it.next();
                    if (equipmentToCompare.getId().getIdentifierString().equals(equipmentId)){
                        equipment = equipmentToCompare;
                        break;
                    }                   
                }
                
                if (equipment == null){
                    String mesg = "EquipmentDatabase.retrieveEquipmentMEIdsByOneQuery | Cannot found correspond result for '" + equipmentId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                
                /**
                 * @todo when change DB Identifier model ,change getString() to
                 *       getLong()
                 */
                Identifier meId = new Identifier(resultSet.getString(LINK_COLUMN_MONITORED_ELEMENT_ID));
                List meIds = (List)meIdMap.get(equipment);
                if (meIds == null){
                    meIds = new LinkedList();
                    meIdMap.put(equipment, meIds);
                }               
                meIds.add(meId);              
            }
            
            for (Iterator iter = equipments.iterator(); iter.hasNext();) {
                Equipment equipment = (Equipment) iter.next();
                List meIds = (List)meIdMap.get(equipment);
                equipment.setPortIds(meIds);
            }
            
        } catch (SQLException sqle) {
            String mesg = "EquipmentDatabase.retrieveEquipmentMEIdsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
            throw new RetrieveObjectException(mesg, sqle);
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (resultSet != null)
                    resultSet.close();
                statement = null;
                resultSet = null;
            } catch (SQLException sqle1) {
                Log.errorException(sqle1);
            } finally {
                DatabaseConnection.closeConnection(connection);
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
		super.insertEntity(equipment);
		this.insertEquipmentMELinks(equipment);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);			
		characteristicDatabase.insert(equipment.getCharacteristics());		
	}
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
			Equipment equipment = (Equipment) iter.next();
			insertEquipmentMELinks(equipment);						
			characteristicDatabase.insert(equipment.getCharacteristics());
		}
	}

	

	private void insertEquipmentMELinks(Equipment equipment)	throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String eqIdCode = equipment.getId().getCode();
		List meIds = equipment.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
					+ ObjectEntities.EQUIPMENTMELINK_ENTITY + OPEN_BRACKET
					+ LINK_COLUMN_EQUIPMENT_ID + COMMA 
					+ LINK_COLUMN_MONITORED_ELEMENT_ID
					+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
					+ QUESTION + COMMA
					+ QUESTION
					+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String meIdCode = null;
		Connection connection = DatabaseConnection.getConnection();
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
								+ meIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "EquipmentDatabase.insertEquipmentMELinks | Cannot insert link for monitored element " + meIdCode + " and Equipment " + eqIdCode;
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
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
	}


	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		//TODO Check this method on errors		
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
			break;
		}
	}
	
	public void update(List storableObjects, int updateKind, Object arg)
		throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		//TODO Check this method on errors
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
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
		Connection connection = DatabaseConnection.getConnection();
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
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("EquipmentDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, 
			RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retrieveByIdsOneQuery(null, condition);
		else list = super.retrieveByIdsOneQuery(ids, condition);
		
        if (list != null) {
            retrieveEquipmentPortIdsByOneQuery(list);
            retrieveEquipmentMEIdsByOneQuery(list); 
            
            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
            Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT);
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Equipment equipment = (Equipment) iter.next();
                List characteristics = (List)characteristicMap.get(equipment);
                equipment.setCharacteristics(characteristics);
            }
        }
		return list;
	}
	
	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + domain.getId().toSQLString();
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("EquipmentDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }	

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list;
		if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		} else {
			Log.errorMessage("EquipmentDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
