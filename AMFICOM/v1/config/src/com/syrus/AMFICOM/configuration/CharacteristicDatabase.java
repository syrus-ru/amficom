/*
 * $Id: CharacteristicDatabase.java,v 1.16 2004/08/27 15:18:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;

/**
 * @version $Revision: 1.16 $, $Date: 2004/08/27 15:18:15 $
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


	private Characteristic fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Characteristic)
			return (Characteristic)storableObject;
		throw new IllegalDataException("CharacteristicDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		this.retrieveCharacteristic(characteristic);
	}

	private String retrieveCharacteristicQuery(String condition){
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
	
	private Characteristic updateCharacteristicFromResultSet(Characteristic characteristic, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		Characteristic characteristic1 = characteristic;
		if (characteristic == null){
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
	
	private void retrieveCharacteristic(Characteristic characteristic) throws ObjectNotFoundException, RetrieveObjectException {
		String cIdStr = characteristic.getId().toSQLString();
		String sql = retrieveCharacteristicQuery(COLUMN_ID + EQUALS + cIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.retrieveCharacteristic | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateCharacteristicFromResultSet(characteristic, resultSet);
			else
				throw new ObjectNotFoundException("CharacteristicDatabase.retrieveCharacteristic | No such characteristic: " + cIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.retrieveCharacteristic | Cannot retrieve characteristic '" + cIdStr + "' -- " + sqle.getMessage();
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

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		try {
			this.insertCharacteristic(characteristic);
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

	private void insertCharacteristic(Characteristic characteristic) throws CreateObjectException {
		String cIdStr = characteristic.getId().toSQLString();
		int sort = characteristic.getSort().value();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_INSERT_INTO);
			buffer.append(ObjectEntities.CHARACTERISTIC_ENTITY);
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
			buffer.append(CLOSE_BRACKET);
			buffer.append(SQL_VALUES);
			buffer.append(OPEN_BRACKET);			
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
			buffer.append(Integer.toString(sort));
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
					throw new CreateObjectException("Unknown sort: " + sort + " for characteristic: " + cIdStr);
			}
			buffer.append(CLOSE_BRACKET);
			sql = buffer.toString();
		}
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.insertCharacteristic | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.insertCharacteristic | Cannot insert characteristic " + cIdStr;
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

	public void update(StorableObject storableObject, int update_kind, Object obj) throws IllegalDataException, UpdateObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}

	public static List retrieveCharacteristics(Identifier characterizedId, CharacteristicSort sort) throws RetrieveObjectException {
		List characteristics = new ArrayList();

		String cdIdStr = characterizedId.toSQLString();
		int sortValue = sort.value();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(SQL_SELECT);
			buffer.append(COLUMN_ID);
			buffer.append(SQL_FROM);
			buffer.append(ObjectEntities.CHARACTERISTIC_ENTITY);
			buffer.append(SQL_WHERE);
			buffer.append(COLUMN_SORT);
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
			sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.retrieveCharacteristics | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				try {
					characteristics.add(new Characteristic(new Identifier(resultSet.getString(COLUMN_ID))));
				}
				catch (ObjectNotFoundException onfe) {
					Log.errorException(onfe);
				}
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
	
	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return new LinkedList();
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
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
			}
			sql = retrieveCharacteristicQuery(buffer.toString());
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateCharacteristicFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql = retrieveCharacteristicQuery(buffer.toString());
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
					result.add(updateCharacteristicFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("CharacteristicDatabase.retriveByIdsPreparedStatement | No such characteristic: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.retriveByIdsPreparedStatement | Cannot retrieve characteristic " + sqle.getMessage();
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
