/*
 * $Id: CharacteristicDatabase.java,v 1.22 2004/09/06 11:36:22 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.LinkedList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;

/**
 * @version $Revision: 1.22 $, $Date: 2004/09/06 11:36:22 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class CharacteristicDatabase extends StorableObjectDatabase {
	// table :: Characteristic
    // type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
	// description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // value VARCHAR2(256),
    public static final String COLUMN_VALUE = "value";
    // sort NUMBER(2) NOT NULL,
    public static final String COLUMN_SORT  = "sort";
		// domain_id VARCHAR2(32),
    public static final String COLUMN_DOMAIN_ID        = "domain_id";
    // mcm_id VARCHAR2(32),
    public static final String COLUMN_MCM_ID        = "mcm_id";
    // equipment_id VARCHAR2(32),
    public static final String COLUMN_EQUIPMENT_ID  = "equipment_id";
    // server_id VARCHAR2(32),
    public static final String COLUMN_SERVER_ID     = "server_id";
    // transmission_path_id VARCHAR2(32),
    public static final String COLUMN_TRANSMISSION_PATH_ID  = "transmission_path_id";
    // port_id VARCHAR2(32),
    public static final String COLUMN_PORT_ID  = "port_id";
    
    private String updateColumns;
    
    private String updateMultiplySQLValues;
    
    protected String getEnityName() {
		return "Characteristic";
	}
    
    protected String getTableName() {
		return ObjectEntities.CHARACTERISTIC_ENTITY;
	}
    
    protected String getUpdateColumns() {
    	if (this.updateColumns == null){
    		StringBuffer buffer = new StringBuffer();
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
			buffer.append(COLUMN_NAME);
			buffer.append(COMMA);
			buffer.append(COLUMN_DESCRIPTION);
			buffer.append(COMMA);
			buffer.append(COLUMN_VALUE);
			buffer.append(COMMA);
			buffer.append(COLUMN_SORT);
			buffer.append(COMMA);
			buffer.append(COLUMN_DOMAIN_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_SERVER_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_MCM_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_EQUIPMENT_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_TRANSMISSION_PATH_ID);
			buffer.append(COMMA);
			buffer.append(COLUMN_PORT_ID);
			this.updateColumns = buffer.toString();
    	}
		return this.updateColumns;
	}
    
    protected String getUpdateMultiplySQLValues() {
    	if (this.updateMultiplySQLValues == null){
    		StringBuffer buffer = new StringBuffer();
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			buffer.append(COMMA);
			buffer.append(QUESTION);
			this.updateMultiplySQLValues = buffer.toString();
    	}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		Characteristic characteristic = fromStorableObject(storableObject);
		String cIdStr = characteristic.getId().toSQLString();
		int sort = characteristic.getSort().value();
		StringBuffer buffer = new StringBuffer();
		buffer.append(cIdStr);
		buffer.append(COMMA);
		buffer.append(DatabaseDate.toUpdateSubString(characteristic.getCreated()));
		buffer.append(COMMA);
		buffer.append(DatabaseDate.toUpdateSubString(characteristic.getModified()));
		buffer.append(COMMA);
		buffer.append(characteristic.getCreatorId().toSQLString());
		buffer.append(COMMA);
		buffer.append(characteristic.getModifierId().toSQLString());
		buffer.append(COMMA);
		buffer.append(characteristic.getType().getId().toSQLString());
		buffer.append(COMMA);
		buffer.append(APOSTOPHE);
		buffer.append(characteristic.getName());
		buffer.append(APOSTOPHE);
		buffer.append(COMMA);
		buffer.append(APOSTOPHE);
		buffer.append(characteristic.getDescription());
		buffer.append(APOSTOPHE);
		buffer.append(COMMA);
		buffer.append(APOSTOPHE);
		buffer.append(characteristic.getValue());
		buffer.append(APOSTOPHE);			
		buffer.append(COMMA);
		buffer.append(sort);
		buffer.append(COMMA);
		String characterizedIdStr = characteristic.getCharacterizedId().toSQLString();
		switch (sort) {
			case CharacteristicSort._CHARACTERISTIC_SORT_DOMAIN:
				buffer.append(characterizedIdStr);
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_SERVER:
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(characterizedIdStr);
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_MCM:
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(characterizedIdStr);
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(characterizedIdStr);
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_TRANSMISSIONPATH:
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(characterizedIdStr);			
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(Identifier.getNullSQLString());
				buffer.append(COMMA);
				buffer.append(characterizedIdStr);
				break;
			default:
				throw new UpdateObjectException("Unknown sort: " + sort + " for characteristic: " + cIdStr);
		}
		return buffer.toString();
	}
	
	protected void setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException, UpdateObjectException{
		Characteristic characteristic = fromStorableObject(storableObject);
		String cIdStr = characteristic.getId().getCode();
		int sort = characteristic.getSort().value();
		try {
			preparedStatement.setString( 1, cIdStr);
			preparedStatement.setTimestamp( 2, new Timestamp(characteristic.getCreated().getTime()));
			preparedStatement.setTimestamp( 3, new Timestamp(characteristic.getModified().getTime()));
			preparedStatement.setString( 4, characteristic.getCreatorId().getCode());
			preparedStatement.setString( 5, characteristic.getModifierId().getCode());
			preparedStatement.setString( 6, characteristic.getType().getId().getCode());
			preparedStatement.setString( 7, characteristic.getName());
			preparedStatement.setString( 8, characteristic.getDescription());
			preparedStatement.setString( 9, characteristic.getValue());
			preparedStatement.setInt( 10, sort);
			String characterizedIdStr = characteristic.getCharacterizedId().toSQLString();
			switch (sort) {
				case CharacteristicSort._CHARACTERISTIC_SORT_DOMAIN:
					preparedStatement.setString( 11, characterizedIdStr);
					preparedStatement.setString( 12, "");
					preparedStatement.setString( 13, "");
					preparedStatement.setString( 14, "");
					preparedStatement.setString( 15, "");
					preparedStatement.setString( 16, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_SERVER:
					preparedStatement.setString( 11, "");
					preparedStatement.setString( 12, characterizedIdStr);
					preparedStatement.setString( 13, "");
					preparedStatement.setString( 14, "");
					preparedStatement.setString( 15, "");
					preparedStatement.setString( 16, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_MCM:
					preparedStatement.setString( 11, "");
					preparedStatement.setString( 12, "");
					preparedStatement.setString( 13, characterizedIdStr);
					preparedStatement.setString( 14, "");
					preparedStatement.setString( 15, "");
					preparedStatement.setString( 16, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
					preparedStatement.setString( 11, "");
					preparedStatement.setString( 12, "");
					preparedStatement.setString( 13, "");
					preparedStatement.setString( 14, characterizedIdStr);
					preparedStatement.setString( 15, "");
					preparedStatement.setString( 16, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_TRANSMISSIONPATH:
					preparedStatement.setString( 11, "");
					preparedStatement.setString( 12, "");
					preparedStatement.setString( 13, "");
					preparedStatement.setString( 14, "");
					preparedStatement.setString( 15, characterizedIdStr);
					preparedStatement.setString( 16, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
					preparedStatement.setString( 11, "");
					preparedStatement.setString( 12, "");
					preparedStatement.setString( 13, "");
					preparedStatement.setString( 14, "");
					preparedStatement.setString( 15, "");
					preparedStatement.setString( 16, characterizedIdStr);
					break;
				default:
					throw new UpdateObjectException("Unknown sort: " + sort + " for characteristic: " + cIdStr);
			}
		preparedStatement.setString( 17, cIdStr );
		} catch (SQLException sqle) {
			throw new UpdateObjectException("CharacteristicDatabase.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		
	}	
	
	private Characteristic fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characteristic)
			return (Characteristic)storableObject;
		throw new IllegalDataException("CharacteristicDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		super.retrieveEntity(characteristic);
	}
	
	

	protected String retrieveQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_TYPE_ID + COMMA
		+ COLUMN_NAME + COMMA
		+ COLUMN_DESCRIPTION + COMMA			
		+ COLUMN_VALUE + COMMA
		+ COLUMN_SORT + COMMA
		+ COLUMN_SERVER_ID + COMMA 
		+ COLUMN_MCM_ID + COMMA			
		+ COLUMN_EQUIPMENT_ID + COMMA
		+ COLUMN_TRANSMISSION_PATH_ID + COMMA
		+ COLUMN_PORT_ID
		+ SQL_FROM + ObjectEntities.CHARACTERISTIC_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws RetrieveObjectException, SQLException, IllegalDataException {
		Characteristic characteristic1 = fromStorableObject(storableObject);
		if (characteristic1 == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			characteristic1 = new Characteristic(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null,
										   0, null, null);			
		}
		
		int sort = resultSet.getInt(COLUMN_SORT);
		Identifier characterizedId;
		
		switch (sort) {
			case CharacteristicSort._CHARACTERISTIC_SORT_DOMAIN:
				/**
				* @todo when change DB Identifier model ,change getString() to
				*       getLong()
				*/
			characterizedId = new Identifier(resultSet.getString(COLUMN_DOMAIN_ID));
			break;
			case CharacteristicSort._CHARACTERISTIC_SORT_SERVER:
				/**
				* @todo when change DB Identifier model ,change getString() to
				*       getLong()
				*/
			characterizedId = new Identifier(resultSet.getString(COLUMN_SERVER_ID));
			break;
			
			case CharacteristicSort._CHARACTERISTIC_SORT_MCM:
				/**
				* @todo when change DB Identifier model ,change getString() to
				*       getLong()
				*/
			characterizedId = new Identifier(resultSet.getString(COLUMN_SERVER_ID));
			break;
			
			case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
				/**
					* @todo when change DB Identifier model ,change getString() to
					*       getLong()
					*/
				characterizedId = new Identifier(resultSet.getString(COLUMN_EQUIPMENT_ID));
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_TRANSMISSIONPATH:
				/**
					* @todo when change DB Identifier model ,change getString() to
					*       getLong()
					*/
				characterizedId = new Identifier(resultSet.getString(COLUMN_TRANSMISSION_PATH_ID));
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
				/**
					* @todo when change DB Identifier model ,change getString() to
					*       getLong()
					*/
				characterizedId = new Identifier(resultSet.getString(COLUMN_PORT_ID));
				break;
			default:
				throw new RetrieveObjectException("Unknown sort: " + sort + " for characteristic: " + characteristic1.getId().toString());
		}

		CharacteristicType characteristicType;
		try {
			characteristicType = (CharacteristicType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		characteristic1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																 /**
																	* @todo when change DB Identifier model ,change getString() to
																	*       getLong()
																	*/
																 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																 /**
																	* @todo when change DB Identifier model ,change getString() to
																	*       getLong()
																	*/
																 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																 /**
																	* @todo when change DB Identifier model ,change getString() to
																	*       getLong()
																	*/
																 characteristicType,
																 resultSet.getString(COLUMN_NAME),
																 resultSet.getString(COLUMN_DESCRIPTION),
																 sort,
																 resultSet.getString(COLUMN_VALUE),
																 characterizedId);
		return characteristic1;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		insertEntities(storableObjects);
	}
	
	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		try {
			super.insertEntity(characteristic);
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

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
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
	
	public List retrieveCharacteristics(Identifier characterizedId, CharacteristicSort sort) throws RetrieveObjectException, IllegalDataException {
		List characteristics = new LinkedList();

		String cdIdStr = characterizedId.toSQLString();
		int sortValue = sort.value();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(COLUMN_SORT);
			buffer.append(EQUALS);
			buffer.append(sortValue);
			buffer.append(SQL_AND);
			switch (sortValue) {
				case CharacteristicSort._CHARACTERISTIC_SORT_DOMAIN:
					buffer.append(COLUMN_DOMAIN_ID);
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_SERVER:
					buffer.append(COLUMN_SERVER_ID);
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_MCM:
					buffer.append(COLUMN_MCM_ID);
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
					buffer.append(COLUMN_EQUIPMENT_ID);
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_TRANSMISSIONPATH:
					buffer.append(COLUMN_TRANSMISSION_PATH_ID);
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
					buffer.append(COLUMN_PORT_ID);
					break;
				default:
					throw new RetrieveObjectException("Unknown sort: " + sort + " for characterized: " + cdIdStr);
			}
			buffer.append(EQUALS);
			buffer.append(cdIdStr);
			sql = retrieveQuery(buffer.toString());
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.retrieveCharacteristics | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				characteristics.add(updateEntityFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.retrieveCharacteristics | Cannot retrieve characteristics for '" + cdIdStr + "' -- " + sqle.getMessage();
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
		
		
		return characteristics;

	}
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return super.retriveByIdsOneQuery(null, condition);
		return super.retriveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}	
}
