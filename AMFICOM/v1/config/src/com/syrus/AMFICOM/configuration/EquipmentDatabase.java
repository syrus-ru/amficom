/*
 * $Id: EquipmentDatabase.java,v 1.56 2004/12/29 15:25:46 arseniy Exp $
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.56 $, $Date: 2004/12/29 15:25:46 $
 * @author $Author: arseniy $
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
    // supplier VARCHAR2(128)
    private static final int SIZE_SUPPLIER_COLUMN   = 128;
    public static final String COLUMN_SUPPLIER      = "supplier";
    // supplier_code VARCHAR2(128)
    private static final int SIZE_SUPPLIER_CODE_COLUMN = 128;
    public static final String COLUMN_SUPPLIER_CODE = "supplier_code";
    // latitude NUMBER
    public static final String COLUMN_LATITUDE      = "latitude";
    // longitude NUMBER
    public static final String COLUMN_LONGITUDE     = "longitude";
    // hwSerial VARCHAR2(64)
    private static final int SIZE_HW_SERIAL_COLUMN  = 64;
    public static final String COLUMN_HW_SERIAL     = "hw_serial";
    // hwVersion VARCHAR2(64)
    private static final int SIZE_HW_VERSION_COLUMN = 64;
    public static final String COLUMN_HW_VERSION    = "hw_version";
    // swSerial VARCHAR2(64)
    private static final int SIZE_SW_SERIAL_COLUMN  = 64;
    public static final String COLUMN_SW_SERIAL     = "sw_serial";
    // swVersion VARCHAR2(64)
    private static final int SIZE_SW_VERSION_COLUMN = 64;
    public static final String COLUMN_SW_VERSION    = "sw_version";
    // inventory_number VARCHAR2(64)
    private static final int SIZE_INVENTOY_NUMBER_COLUMN = 64;
    public static final String COLUMN_INVENTORY_NUMBER = "inventory_number"; 
    
    // table :: EquipmentMELink
    // equipment_id Identifier,
    public static final String LINK_COLUMN_EQUIPMENT_ID  = "equipment_id";
    // monitored_element_id Identifier,
    public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	private Equipment fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Equipment)
			return (Equipment)storableObject;
		throw new IllegalDataException("EquipmentDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.EQUIPMENT_ENTITY;
	}
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_IMAGE_ID + COMMA
				+ COLUMN_SUPPLIER + COMMA
				+ COLUMN_SUPPLIER_CODE + COMMA
                + COLUMN_LATITUDE + COMMA
                + COLUMN_LONGITUDE + COMMA
				+ COLUMN_HW_SERIAL + COMMA
				+ COLUMN_HW_VERSION + COMMA
				+ COLUMN_SW_SERIAL + COMMA
				+ COLUMN_SW_VERSION + COMMA
				+ COLUMN_INVENTORY_NUMBER;
		}
		return columns;
	}
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA 
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
                + QUESTION + COMMA
                + QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(equipment.getDomainId()) + COMMA
			+ DatabaseIdentifier.toSQLString(equipment.getType().getId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(equipment.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
            + DatabaseIdentifier.toSQLString(equipment.getImageId()) + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSupplier(), SIZE_SUPPLIER_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + equipment.getLatitude() + APOSTOPHE + COMMA
            + APOSTOPHE + equipment.getLongitude() + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(equipment.getHwSerial(), SIZE_HW_SERIAL_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(equipment.getHwVersion(), SIZE_HW_VERSION_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSwSerial(), SIZE_SW_SERIAL_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(equipment.getSwVersion(), SIZE_SW_VERSION_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(equipment.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN) + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode) throws IllegalDataException,
			UpdateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipment.getDomainId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipment.getType().getId());
			DatabaseString.setString(preparedStatement, ++i, equipment.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, equipment.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, equipment.getImageId());
            DatabaseString.setString(preparedStatement, ++i, equipment.getSupplier(), SIZE_SUPPLIER_COLUMN);
            DatabaseString.setString(preparedStatement, ++i, equipment.getSupplierCode(), SIZE_SUPPLIER_CODE_COLUMN);
            preparedStatement.setFloat(++i, equipment.getLatitude());
            preparedStatement.setFloat(++i, equipment.getLongitude());
            DatabaseString.setString(preparedStatement, ++i, equipment.getHwSerial(), SIZE_HW_SERIAL_COLUMN);
            DatabaseString.setString(preparedStatement, ++i, equipment.getHwVersion(), SIZE_HW_VERSION_COLUMN);
            DatabaseString.setString(preparedStatement, ++i, equipment.getSwSerial(), SIZE_SW_SERIAL_COLUMN);
            DatabaseString.setString(preparedStatement, ++i, equipment.getSwVersion(), SIZE_SW_VERSION_COLUMN);
            DatabaseString.setString(preparedStatement, ++i, equipment.getInventoryNumber(), SIZE_INVENTOY_NUMBER_COLUMN);
		} catch (SQLException sqle) {
			throw new UpdateObjectException("EquipmentDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Equipment equipment = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (equipment == null){
			equipment = new Equipment(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null,
									   null, null, "", null, 0, 0 , null ,null ,null ,null,null);			
		}
		String name = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME));
		String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		EquipmentType equipmentType;
		try {
			equipmentType = (EquipmentType)ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		equipment.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),		
								DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
								equipmentType,
								(name != null) ? name : "",
								(description != null) ? description : "",
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_IMAGE_ID),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_SUPPLIER)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_SUPPLIER_CODE)),
                                resultSet.getFloat(COLUMN_LONGITUDE),
                                resultSet.getFloat(COLUMN_LATITUDE),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_HW_VERSION)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_HW_SERIAL)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_SW_VERSION)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_SW_SERIAL)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_INVENTORY_NUMBER)));
		
		return equipment;
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Equipment equipment = this.fromStorableObject(storableObject);
		super.retrieveEntity(equipment);
		this.retrieveEquipmentPortIds(equipment);
		this.retrieveEquipmentMEIdsByOneQuery(Collections.singletonList(equipment));
		equipment.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(equipment.getId(), CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT));
	}

	private void retrieveEquipmentPortIds(Equipment equipment) throws RetrieveObjectException {
		List portIds = new ArrayList();
		String eqIdStr = DatabaseIdentifier.toSQLString(equipment.getId());

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
				portIds.add(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID));				
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
                DatabaseConnection.releaseConnection(connection);
            }
		}
		equipment.setPortIds0(portIds);
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
            sql.append(DatabaseIdentifier.toSQLString(equipment.getId()));
            if (it.hasNext()){
                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
                    sql.append(COMMA);
                else {
                    sql.append(CLOSE_BRACKET);
                    sql.append(SQL_OR);
                    sql.append(PortDatabase.COLUMN_EQUIPMENT_ID);
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
                Identifier equipmentId = DatabaseIdentifier.getIdentifier(resultSet, PortDatabase.COLUMN_EQUIPMENT_ID);
                for (Iterator it = equipments.iterator(); it.hasNext();) {
                    Equipment equipmentToCompare = (Equipment) it.next();
                    if (equipmentToCompare.getId().equals(equipmentId)){
                        equipment = equipmentToCompare;
                        break;
                    }                   
                }
                
                if (equipment == null){
                    String mesg = "EquipmentDatabase.retrieveEquipmentPortIdsByOneQuery | Cannot found correspond result for '" + equipmentId.getIdentifierString() +"'" ;
                    throw new RetrieveObjectException(mesg);
                }                    
                
                Identifier epId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
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
                equipment.setPortIds0(epIds);
            }
            
        } catch (SQLException sqle) {
            String mesg = "EquipmentDatabase.retrieveEquipmentPortIdsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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
                DatabaseConnection.releaseConnection(connection);
            }
        }
    }

	private void retrieveEquipmentMEIdsByOneQuery(List equipments) throws RetrieveObjectException, IllegalDataException {
    	if ((equipments == null) || (equipments.isEmpty()))
            return;     

    	Map map = super.retrieveLinkedEntities(equipments, ObjectEntities.EQUIPMENTMELINK_ENTITY, LINK_COLUMN_EQUIPMENT_ID, LINK_COLUMN_MONITORED_ELEMENT_ID);
		for (Iterator it = map.keySet().iterator(); it.hasNext();) {
			Equipment equipment = (Equipment) it.next();
			List meIds = (List)map.get(equipment);
			equipment.setMonitoredElementIds0(meIds);
		}        
    }

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Equipment equipment = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		super.insertEntity(equipment);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		try {
			this.updateEquipmentMELinks(Collections.singletonList(equipment));
			characteristicDatabase.updateCharacteristics(equipment);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}		
	}
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);		
		try {
			this.updateEquipmentMELinks(storableObjects);
			characteristicDatabase.updateCharacteristics(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}
	
	private void updateEquipmentMELinks(List equipments) throws IllegalDataException, UpdateObjectException{
			if (equipments == null || equipments.isEmpty())
				return;
			java.util.Map collectorIdPhysicalLinkIdsMap = new HashMap();
			for (Iterator it = equipments.iterator(); it.hasNext();) {
				Equipment equipment = this.fromStorableObject((StorableObject)it.next());
				List physicalLinkIds = equipment.getMonitoredElementIds();
				collectorIdPhysicalLinkIdsMap.put(equipment.getId(), physicalLinkIds);
			}		
			super.updateLinkedEntities(collectorIdPhysicalLinkIdsMap, ObjectEntities.EQUIPMENTMELINK_ENTITY, LINK_COLUMN_EQUIPMENT_ID, LINK_COLUMN_MONITORED_ELEMENT_ID);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Equipment equipment = this.fromStorableObject(storableObject);
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(equipment, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(equipment, false);
			break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(equipment);
		this.updateEquipmentMELinks(Collections.singletonList(equipment));
	}
	
	public void update(List storableObjects, int updateKind, Object arg)
		throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}		
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		this.updateEquipmentMELinks(storableObjects);
		characteristicDatabase.updateCharacteristics(storableObjects);
	}

	private void setModified(Equipment equipment) throws UpdateObjectException {		
		String eqIdStr = DatabaseIdentifier.toSQLString(equipment.getId());
		String sql = SQL_UPDATE
			+ ObjectEntities.EQUIPMENT_ENTITY + SQL_SET
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(equipment.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(equipment.getModifierId())
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
                DatabaseConnection.releaseConnection(connection);
            }
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = this.retrieveByIds(null, null);
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
			list = this.retrieveByIdsOneQuery(null, condition);
		else list = this.retrieveByIdsOneQuery(ids, condition);
		
        if (list != null) {
            retrieveEquipmentPortIdsByOneQuery(list);
            retrieveEquipmentMEIdsByOneQuery(list); 
            
            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
            Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT);
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Equipment equipment = (Equipment) iter.next();
                List characteristics = (List)characteristicMap.get(equipment);
                equipment.setCharacteristics0(characteristics);
            }
        }
		return list;
	}
	
	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId());
        
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
