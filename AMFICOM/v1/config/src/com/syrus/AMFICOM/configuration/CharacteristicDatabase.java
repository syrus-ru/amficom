/*
 * $Id: CharacteristicDatabase.java,v 1.35 2004/10/29 15:03:39 max Exp $
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
import com.syrus.util.database.DatabaseString;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
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
 * @version $Revision: 1.35 $, $Date: 2004/10/29 15:03:39 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class CharacteristicDatabase extends StorableObjectDatabase {
	// table :: Characteristic
    // type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       	= "type_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME 				 = "name";
	// description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION		= "description";
    // value VARCHAR2(256),
    public static final String COLUMN_VALUE 			= "value";
    public static final String COLUMN_IS_EDITABLE		= "is_editable";
	public static final String COLUMN_IS_VISIBLE		= "is_visible";
    // sort NUMBER(2) NOT NULL,
    public static final String COLUMN_SORT  			= "sort";
    //  characterized_id VARCHAR2(32),
    public static final String COLUMN_CHARACTERIZED_ID	= "characterized_id";
    
    
    private String updateColumns;
       private String updateMultiplySQLValues;
    
    protected String getEnityName() {
		return ObjectEntities.CHARACTERISTIC_ENTITY;
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
				+ COLUMN_IS_EDITABLE + COMMA
				+ COLUMN_IS_VISIBLE + COMMA
				+ COLUMN_SORT +	COMMA
				+ COLUMN_CHARACTERIZED_ID;
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
				+ QUESTION;
    	}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		Characteristic characteristic = fromStorableObject(storableObject);
		int sort = characteristic.getSort().value();
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ characteristic.getType().getId().toSQLString() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getDescription()) + APOSTOPHE  + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getValue()) + APOSTOPHE + COMMA
			+ (characteristic.isEditable()?"1":"0") + COMMA
			+ (characteristic.isVisible()?"1":"0") + COMMA
			+ sort + COMMA
			+ characteristic.getCharacterizedId().toSQLString();
			/**
			 * check sort support
			 */
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException, UpdateObjectException{
		Characteristic characteristic = fromStorableObject(storableObject);
		int sort = characteristic.getSort().value();
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject , preparedStatement);
			preparedStatement.setString( ++i, characteristic.getType().getId().getCode());
			preparedStatement.setString( ++i, characteristic.getName());
			preparedStatement.setString( ++i, characteristic.getDescription());
			preparedStatement.setString( ++i, characteristic.getValue());
			preparedStatement.setInt( ++i, characteristic.isEditable()?'1':'0');
			preparedStatement.setInt( ++i, characteristic.isVisible()?'1':'0');
			preparedStatement.setInt( ++i, sort);
			preparedStatement.setString( ++i, characteristic.getCharacterizedId().toSQLString());
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
		+ COLUMN_IS_EDITABLE + COMMA
		+ COLUMN_IS_VISIBLE + COMMA
		+ COLUMN_SORT + COMMA
		+ COLUMN_CHARACTERIZED_ID
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
										   0, null, null, false, false);			
		}
		
		int sort = resultSet.getInt(COLUMN_SORT);
		Identifier characterizedId = new Identifier(resultSet.getString(COLUMN_CHARACTERIZED_ID));

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
									  * @todo when change DB Identifier model ,change getString() to getLong()
									  */
									 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
									 /**
									  * @todo when change DB Identifier model ,change getString() to getLong()
									  */
									 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
									 characteristicType,
                                     DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
                                     DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
									 sort,
                                     DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_VALUE)),
									 characterizedId,
									 (resultSet.getInt(COLUMN_IS_EDITABLE) == 0) ? false : true,
									 (resultSet.getInt(COLUMN_IS_VISIBLE) == 0) ? false : true);
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
			buffer.append(COLUMN_CHARACTERIZED_ID);
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
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof LinkedIdsCondition){
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
			Identifier id = linkedIdsCondition.getIdentifier();
			CharacteristicSort sort = Characteristic.getSortForId(id);
			
			if (sort != null){
				list = retrieveCharacteristics(id, sort);
			}
		} else list =  this.retrieveButIds(ids);
		return list;
	}
	
}
