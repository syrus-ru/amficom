/*
 * $Id: CharacteristicDatabase.java,v 1.29 2004/10/13 12:55:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
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
 * @version $Revision: 1.29 $, $Date: 2004/10/13 12:55:15 $
 * @author $Author: bob $
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
    		this.updateColumns = super.getUpdateColumns() + COMMA
    			+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_VALUE + COMMA
				+ COLUMN_SORT +	COMMA
				+ COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_SERVER_ID + COMMA
				+ COLUMN_MCM_ID + COMMA
				+ COLUMN_EQUIPMENT_ID + COMMA
				+ COLUMN_TRANSMISSION_PATH_ID + COMMA
				+ COLUMN_PORT_ID;
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
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
    	}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		Characteristic characteristic = fromStorableObject(storableObject);
		String cIdStr = characteristic.getId().toSQLString();
		int sort = characteristic.getSort().value();
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ characteristic.getType().getId().toSQLString() + COMMA
			+ APOSTOPHE + characteristic.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + characteristic.getDescription() + APOSTOPHE  + COMMA
			+ APOSTOPHE + characteristic.getValue() + APOSTOPHE + COMMA
			+ sort + COMMA;
		String characterizedIdStr = characteristic.getCharacterizedId().toSQLString();
		switch (sort) {
			case CharacteristicSort._CHARACTERISTIC_SORT_DOMAIN:
				sql = sql + characterizedIdStr + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString();
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_SERVER:
				sql = sql + Identifier.getNullSQLString() + COMMA
					+ characterizedIdStr + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString();
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_MCM:
				sql = sql + Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ characterizedIdStr + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString();
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
				sql = sql + Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ characterizedIdStr + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString();
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_TRANSMISSIONPATH:
				sql = sql + Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ characterizedIdStr + COMMA
					+ Identifier.getNullSQLString();
				break;
			case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
				sql = sql + Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ Identifier.getNullSQLString() + COMMA
					+ characterizedIdStr;
				break;
			default:
				throw new UpdateObjectException("Unknown sort: " + sort + " for characteristic: " + cIdStr);
		}
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException, UpdateObjectException{
		Characteristic characteristic = fromStorableObject(storableObject);
		String cIdStr = characteristic.getId().getCode();
		int sort = characteristic.getSort().value();
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject , preparedStatement);
			preparedStatement.setString( ++i, characteristic.getType().getId().getCode());
			preparedStatement.setString( ++i, characteristic.getName());
			preparedStatement.setString( ++i, characteristic.getDescription());
			preparedStatement.setString( ++i, characteristic.getValue());
			preparedStatement.setInt( ++i, sort);
			String characterizedIdStr = characteristic.getCharacterizedId().toSQLString();
			switch (sort) {
				case CharacteristicSort._CHARACTERISTIC_SORT_DOMAIN:
					preparedStatement.setString( ++i, characterizedIdStr);
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_SERVER:
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, characterizedIdStr);
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_MCM:
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, characterizedIdStr);
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_EQUIPMENT:
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, characterizedIdStr);
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_TRANSMISSIONPATH:
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, characterizedIdStr);
					preparedStatement.setString( ++i, "");
					break;
				case CharacteristicSort._CHARACTERISTIC_SORT_PORT:
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, "");
					preparedStatement.setString( ++i, characterizedIdStr);
					break;
				default:
					throw new UpdateObjectException("Unknown sort: " + sort + " for characteristic: " + cIdStr);
			}
		} catch (SQLException sqle) {
			throw new UpdateObjectException("CharacteristicDatabase.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
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
		return super.retrieveQuery(condition) + COMMA
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
		Characteristic characteristic = storableObject == null ? null : fromStorableObject(storableObject); 
		if (characteristic == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			characteristic = new Characteristic(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null,
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
				throw new RetrieveObjectException("Unknown sort: " + sort + " for characteristic: " + characteristic.getId().toString());
		}

		CharacteristicType characteristicType;
		try {
			characteristicType = (CharacteristicType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		characteristic.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return characteristic;
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
		super.insertEntities(storableObjects);
	}
	
	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		super.insertEntity(characteristic);		
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
				case CharacteristicSort._CHARACTERISTIC_SORT_MONITORINGPORT:
				case CharacteristicSort._CHARACTERISTIC_SORT_MEASUREMENTPORT:
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
		Connection connection = DatabaseConnection.getConnection();
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
			} finally {
				DatabaseConnection.closeConnection(connection);
			}
		}
		return characteristics;
	}
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return super.retrieveByIdsOneQuery(null, condition);
		return super.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}
}
