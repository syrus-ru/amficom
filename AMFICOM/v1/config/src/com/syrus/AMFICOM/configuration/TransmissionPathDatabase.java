/*
 * $Id: TransmissionPathDatabase.java,v 1.22 2004/10/27 09:52:08 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.22 $, $Date: 2004/10/27 09:52:08 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class TransmissionPathDatabase extends StorableObjectDatabase {
    // table :: TransmissionPath
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
    // start_port_id VARCHAR2(32),
    public static final String COLUMN_START_PORT_ID = "start_port_id";
    // finish_port_id VARCHAR2(32),
    public static final String COLUMN_FINISH_PORT_ID        = "finish_port_id";

    public static final String COLUMN_TYPE_ID       = "type_id";
    // table :: TransmissionPathMELink
    // monitored_element_id Identifier,
    public static final String LINK_COLUMN_MONITORED_ELEMENT_ID  = "monitored_element_id";
    // transmission_path_id Identifier,
    public static final String LINK_COLUMN_TRANSMISSION_PATH_ID  = "transmission_path_id";    
    
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
    
    private String updateColumns;
    private String updateMultiplySQLValues;

	private TransmissionPath fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TransmissionPath)
			return (TransmissionPath)storableObject;
		throw new IllegalDataException("TransmissionPathDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	
	
	protected String getEnityName() {		
		return "TransmissionPath";
	}	
	
	protected String getTableName() {		
		return ObjectEntities.TRANSPATH_ENTITY;
	}	
	
	protected String getUpdateColumns() {		
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
                + COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_START_PORT_ID + COMMA
				+ COLUMN_FINISH_PORT_ID;		
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
				+ QUESTION;		
		}
		return this.updateMultiplySQLValues;
	}	
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		TransmissionPath transmissionPath = fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ transmissionPath.getDomainId().toSQLString() + COMMA
            + transmissionPath.getType().getId().toSQLString() + COMMA
			+ APOSTOPHE + transmissionPath.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + transmissionPath.getDescription() + APOSTOPHE + COMMA
			+ transmissionPath.getStartPortId().toSQLString() + COMMA
			+ transmissionPath.getFinishPortId().toSQLString();
	}
    
    protected int setEntityForPreparedStatement(StorableObject storableObject,
            PreparedStatement preparedStatement) throws IllegalDataException,
            UpdateObjectException {
        TransmissionPath transmissionPath = fromStorableObject(storableObject);
        int i;
        try {
            i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
            preparedStatement.setString( ++i, transmissionPath.getDomainId().getCode());
            preparedStatement.setString( ++i, transmissionPath.getType().getId().getCode());            
            preparedStatement.setString( ++i, transmissionPath.getName());
            preparedStatement.setString( ++i, transmissionPath.getDescription());
            preparedStatement.setString( ++i, transmissionPath.getStartPortId().toString());
            preparedStatement.setString( ++i, transmissionPath.getFinishPortId().toSQLString());            
        } catch (SQLException sqle) {
            throw new UpdateObjectException("KISDatabase." +
                    "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
        }
        return i;
    }

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.retrieveEntity(transmissionPath);
		this.retrieveTransmissionPathMELink(transmissionPath);
		transmissionPath.setCharacteristics(characteristicDatabase.retrieveCharacteristics(transmissionPath.getId(), CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH));
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
            + COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_START_PORT_ID + COMMA
			+ COLUMN_FINISH_PORT_ID
			+ SQL_FROM + ObjectEntities.TRANSPATH_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}
	
	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		TransmissionPath transmissionPath = (storableObject == null) ?
				new TransmissionPath(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null, null, null, null) :
					fromStorableObject(storableObject);
		String name = resultSet.getString(COLUMN_NAME);
		String description = resultSet.getString(COLUMN_DESCRIPTION);
		
        TransmissionPathType type;
        try {
            type = (TransmissionPathType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
        }
        catch (ApplicationException ae) {
            throw new RetrieveObjectException(ae);
        }
        transmissionPath.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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

						  (name != null) ? name : "",
						  (description != null) ? description : "",
                          type,
						  /**
							* @todo when change DB Identifier model ,change getString() to getLong()
							*/
						  new Identifier(resultSet.getString(COLUMN_START_PORT_ID)),
						  new Identifier(resultSet.getString(COLUMN_FINISH_PORT_ID)));
		return transmissionPath;
	}

	private void retrieveTransmissionPathMELink(TransmissionPath transmissionPath) throws RetrieveObjectException{
		String tpIdStr = transmissionPath.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_MONITORED_ELEMENT_ID
			+ SQL_FROM + ObjectEntities.TRANSPATHMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_TRANSMISSION_PATH_ID + EQUALS + tpIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.retrieveEquipmentMELink | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			List meLink = new ArrayList();
			while (resultSet.next()) {				
				/**
				* @todo when change DB Identifier model ,change getString() to getLong()
				*/
				Identifier meId = new Identifier(resultSet.getString(LINK_COLUMN_MONITORED_ELEMENT_ID));
				meLink.add(meId);				
			}
			transmissionPath.setMonitoredElementIds(meLink);
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.retrieveEquipmentMELink | Cannot retrieve transmission path " + tpIdStr;
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


	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException{
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		this.insertEntity(transmissionPath);
	}
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, 
			VersionCollisionException, UpdateObjectException {
		TransmissionPath transmissionPath = this.fromStorableObject(storableObject);
		switch (updateKind) {
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
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
		
	}	

	private void setModified(TransmissionPath transmissionPath) throws UpdateObjectException {		
		String tpIdStr = transmissionPath.getId().toSQLString();
		String sql = SQL_UPDATE
			+ ObjectEntities.TRANSPATH_ENTITY
			+ SQL_SET
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(transmissionPath.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + transmissionPath.getModifierId().toSQLString()
			+ SQL_WHERE + COLUMN_ID + EQUALS + tpIdStr;

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TransmissionPathDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TransmissionPathDatabase.setModified | Cannot set modified for transmission path " + tpIdStr;
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
	
	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		try {
			list = retrieveByIds(null, null);
		}  catch (IllegalDataException ide) {			
			throw new RetrieveObjectException(ide);
		}
		return list;
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = retrieveByIdsOneQuery(null, condition);
		else list = retrieveByIdsOneQuery(ids, condition);
		if (list != null){
			for (Iterator it = list.iterator(); it.hasNext();) {
				TransmissionPath transmissionPath = (TransmissionPath) it.next();
				CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
				this.retrieveTransmissionPathMELink(transmissionPath);
				transmissionPath.setCharacteristics(characteristicDatabase.retrieveCharacteristics(transmissionPath.getId(), CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATH));				
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
            Log.debugMessage("TransmissionPathDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
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
			Log.errorMessage("TransmissionPathDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
