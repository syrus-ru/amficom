/*
 * $Id: CharacteristicDatabase.java,v 1.21 2005/02/28 14:11:56 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.21 $, $Date: 2005/02/28 14:11:56 $
 * @author $Author: bob $
 * @module general_v1
 */

public class CharacteristicDatabase extends StorableObjectDatabase {
	private static final int SIZE_VALUE_COLUMN = 256;

	private static String columns;
	private static String updateMultiplySQLValues;

	protected String getEnityName() {
		return ObjectEntities.CHARACTERISTIC_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
			+ StorableObjectWrapper.COLUMN_NAME + COMMA
			+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
			+ CharacteristicWrapper.COLUMN_VALUE + COMMA
			+ CharacteristicWrapper.COLUMN_EDITABLE + COMMA
			+ CharacteristicWrapper.COLUMN_VISIBLE + COMMA
			+ CharacteristicWrapper.COLUMN_SORT +	COMMA
			+ CharacteristicWrapper.COLUMN_CHARACTERIZED_ID;
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA 
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

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		int sort = characteristic.getSort().value();
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(characteristic.getType().getId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE  + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(characteristic.getValue(), SIZE_VALUE_COLUMN) + APOSTOPHE + COMMA
			+ (characteristic.isEditable()?"1":"0") + COMMA
			+ (characteristic.isVisible()?"1":"0") + COMMA
			+ sort + COMMA
			+ DatabaseIdentifier.toSQLString(characteristic.getCharacterizedId());
			/**
			 * check sort support
			 */
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode) throws IllegalDataException, SQLException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		int sort = characteristic.getSort().value();
		int i;
		i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, characteristic.getType().getId());
		DatabaseString.setString(preparedStatement, ++i, characteristic.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, characteristic.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, characteristic.getValue(), SIZE_VALUE_COLUMN);
		preparedStatement.setInt( ++i, characteristic.isEditable()? 1:0);
		preparedStatement.setInt( ++i, characteristic.isVisible()? 1:0);
		preparedStatement.setInt( ++i, sort);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, characteristic.getCharacterizedId());
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

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws RetrieveObjectException, SQLException, IllegalDataException {
		Characteristic characteristic = (storableObject == null) ? null : this.fromStorableObject(storableObject); 
		if (characteristic == null) {
			characteristic = new Characteristic(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
																null,
																0L,
																null,
																null,
																null,
																0,
																null,
																null,
																false,
																false);			
		}

		int sort = resultSet.getInt(CharacteristicWrapper.COLUMN_SORT);
		Identifier characterizedId = DatabaseIdentifier.getIdentifier(resultSet, CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);

		CharacteristicType characteristicType;
		try {
			characteristicType = (CharacteristicType)GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		characteristic.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									 characteristicType,
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
									 sort,
									 DatabaseString.fromQuerySubString(resultSet.getString(CharacteristicWrapper.COLUMN_VALUE)),
									 characterizedId,
									 (resultSet.getInt(CharacteristicWrapper.COLUMN_EDITABLE) == 0) ? false : true,
									 (resultSet.getInt(CharacteristicWrapper.COLUMN_VISIBLE) == 0) ? false : true);
		return characteristic;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Characteristic characteristic = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Characteristic characteristic = this.fromStorableObject(storableObject);
		super.insertEntity(characteristic);		
	}

	public List retrieveCharacteristics(Identifier characterizedId, CharacteristicSort sort) throws RetrieveObjectException, IllegalDataException {
		List characteristics = new LinkedList();

		String cdIdStr = DatabaseIdentifier.toSQLString(characterizedId);
		int sortValue = sort.value();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(CharacteristicWrapper.COLUMN_SORT);
			buffer.append(EQUALS);
			buffer.append(sortValue);
			buffer.append(SQL_AND);
			buffer.append(CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);
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
			while (resultSet.next()) {
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
		return characteristics;
	}

	public Map retrieveCharacteristicsByOneQuery(Collection objects, CharacteristicSort sort) throws RetrieveObjectException, IllegalDataException {
		if (objects == null || objects.size() == 0)
			return null;

		int sortValue = sort.value();
		StringBuffer stringBuffer = new StringBuffer(CharacteristicWrapper.COLUMN_SORT + EQUALS + Integer.toString(sortValue)
							+ SQL_AND);
		stringBuffer.append(this.idsEnumerationString(objects, CharacteristicWrapper.COLUMN_CHARACTERIZED_ID, true));
		String sql = retrieveQuery(stringBuffer.toString());

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.retrieveCharacteristicsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			
			Map characteristicMap = new HashMap();
			Identifier characterizedId;
			Characteristic characteristic;
			List characteristics;
			while (resultSet.next()) {
				characterizedId = DatabaseIdentifier.getIdentifier(resultSet, CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);
				characteristic = (Characteristic) this.updateEntityFromResultSet(null, resultSet);
				characteristics = (List) characteristicMap.get(characterizedId);
				if (characteristics == null) {
					characteristics = new LinkedList();
					characteristicMap.put(characterizedId, characteristics);
				}
				characteristics.add(characteristic);
			}

			// NOTE
			// The below code is not correct, because method
			// Characterized.setCharacteristics(List characteristics)
			// updates version of Characterized StorableObject.
			// Instead, one must set characteristics from returned characteristicMap,
			// using method setCharacteristics0(List characteristics).
			// For every Characterized from incoming list, be sure, that corresponding
			// list of characteristic
			// is available, i. e. characteristicMap.get(characterizedId) != null for
			// a given characterizedId.
			// Also, be sure, that returned map characteristicMap != null
//
//			Characterized characterized;
//      for (Iterator it = characteristicMap.keySet().iterator(); it.hasNext();) {
//				characterized = (Characterized) it.next();
//				characterizedId = characterized.getId();
//				characteristics = (List) characteristicMap.get(characterizedId);
//
//				characterized.setCharacteristics(characteristics);				
//			}
			return characteristicMap;

		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.retrieveCharacteristicsByOneQuery | Cannot retrieve characteristics for characterized object -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}	

	public void updateCharacteristics(StorableObject storableObject) throws UpdateObjectException {
		if (!(storableObject instanceof Characterized)) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | Storable object " +
				storableObject.getClass().getName() + " is not a type of Characterized";
			throw new UpdateObjectException(mesg);           
		}
		Characterized characterizedStorableObject = (Characterized) storableObject;
		List characteristics = characterizedStorableObject.getCharacteristics();
		List characteristicIds = new ArrayList(characteristics.size());
		for (Iterator it = characteristics.iterator(); it.hasNext();) {
			Characteristic characteristic = (Characteristic) it.next();
			characteristicIds.add(characteristic.getId());
		}

    Map databaseIdCharacteristics = new HashMap();
		String sql;
		StringBuffer buff = new StringBuffer();
		buff.append(CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);
		buff.append(EQUALS);
		buff.append(APOSTOPHE);
		buff.append(storableObject.getId());
		buff.append(APOSTOPHE);
		sql = retrieveQuery(buff.toString());
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.updateCharacteristics | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				Characteristic characteristic = (Characteristic)updateEntityFromResultSet(null, resultSet);
				databaseIdCharacteristics.put(characteristic.getId(), characteristic);
			}

			//  delete
			for (Iterator it = databaseIdCharacteristics.keySet().iterator(); it.hasNext();) {
				Identifier dbCharacteristicId = (Identifier) it.next();
				if(!characteristicIds.contains(dbCharacteristicId))
					this.delete(dbCharacteristicId);
			}

			//  insert or update
			super.checkAndUpdateEntities(characteristics, storableObject.modifierId, true);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | SQLException: " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		catch (VersionCollisionException vce) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | VersionCollisionException: " + vce.getMessage();
			throw new UpdateObjectException(mesg, vce);
		}
		catch (IllegalDataException ide) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | IllegalDataException: " + ide.getMessage();
			throw new UpdateObjectException(mesg, ide);
		}
		catch (RetrieveObjectException roe) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | RetrieveObjectException: " + roe.getMessage();
			throw new UpdateObjectException(mesg, roe);
		}

	}

	public void updateCharacteristics(Collection storableObjects) throws UpdateObjectException {
    // Construction of Map <StorableObjectIdentifier> <List <CharacteristicIdentifier> >
		if(storableObjects == null || storableObjects.isEmpty())
			return;

		Map storableObjectIdCharIdsMap = new HashMap();
		Map modifierIdCharacteristics = new HashMap();
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			if (!(storableObject instanceof Characterized)) {
				String mesg = "CharacteristicDatabase.updateCharacteristics(List) | Storable object " + 
								storableObject.getClass().getName() + " is not a type of Characterized";
				throw new UpdateObjectException(mesg);                
			}
			List characteristics = (List) modifierIdCharacteristics.get(storableObject.getModifierId());
			if (characteristics == null) {
				characteristics = new LinkedList();
				modifierIdCharacteristics.put(storableObject.getModifierId(), characteristics);
			}
				
			for (Iterator iter = ((Characterized) storableObject).getCharacteristics().iterator(); iter.hasNext();) {
				Characteristic characteristic = (Characteristic) iter.next();
				characteristics.add(characteristic);
				List charIdList = (List) storableObjectIdCharIdsMap.get(storableObject.getId());
				if (charIdList == null) {
					charIdList = new LinkedList();
					storableObjectIdCharIdsMap.put(storableObject.getId(), charIdList);
				}
				charIdList.add(characteristic.getId());				
			}
		}

    // creating sql query. This query gets all Characteristics whose characterized_id contained in storableObjects
		StringBuffer stringBuffer = new StringBuffer();
		try {
			stringBuffer.append(this.idsEnumerationString(storableObjects, CharacteristicWrapper.COLUMN_CHARACTERIZED_ID, true));
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
		}
		String sql = retrieveQuery(stringBuffer.toString());
		Map dbStorableObjectIdCharIdsMap = new HashMap();
		List listIdToDelete = new LinkedList();       
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.updateCharacteristics(List) | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			//  Construction of Map <CharacterizedIdentifier> <List <CharacteristicIdentifier> >              
			while (resultSet.next()) {
				Characteristic characteristic = (Characteristic)updateEntityFromResultSet(null, resultSet);
				List charIdList = (List) dbStorableObjectIdCharIdsMap.get(characteristic.getCharacterizedId());
				if (charIdList == null) {
					charIdList = new LinkedList();
					dbStorableObjectIdCharIdsMap.put(characteristic.getCharacterizedId(), charIdList);
				}
				charIdList.add(characteristic.getId());
			}
			//  delete. Iterating through DBMap and matching it with DBMap InMap  
			for (Iterator it = dbStorableObjectIdCharIdsMap.keySet().iterator(); it.hasNext();) {
				Identifier storableObljectId = (Identifier) it.next();
				List dbCharIds = (List) dbStorableObjectIdCharIdsMap.get(storableObljectId);
				List charIds = (List) storableObjectIdCharIdsMap.get(storableObljectId);
				if (charIds == null || charIds.isEmpty()) {
					for (Iterator iter = dbCharIds.iterator(); iter.hasNext();) {
						Identifier dbCharacteristicId = (Identifier) iter.next();
						listIdToDelete.add(dbCharacteristicId);                        
					}
					continue;
				}
				for (Iterator iter = dbCharIds.iterator(); iter.hasNext();) {
					Identifier dbCharacteristicId = (Identifier) iter.next();
					if (!charIds.contains(dbCharacteristicId))
						listIdToDelete.add(dbCharacteristicId);
				}       
			}
			super.delete(listIdToDelete);
			// insert and update. Iterating through InMap and matching it with DBMap 
			for (Iterator it = modifierIdCharacteristics.keySet().iterator(); it.hasNext();) {
				Identifier modifierId = (Identifier) it.next();
				List characteristics = (List) modifierIdCharacteristics.get(modifierId);
				if (characteristics != null && !characteristics.isEmpty())
					super.checkAndUpdateEntities(characteristics, modifierId, true);
			}
			
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | SQLException: " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);        
		}
		catch (IllegalDataException ide) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | IllegalDataException: " + ide.getMessage();
			throw new UpdateObjectException(mesg, ide);
		}
		catch (RetrieveObjectException roe) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | RetrieveObjectException: " + roe.getMessage();
			throw new UpdateObjectException(mesg, roe);        
		}
		catch (VersionCollisionException vce) {
			String mesg = "CharacteristicDatabase.updateCharacteristics | VersionCollisionException: " + vce.getMessage();
			throw new UpdateObjectException(mesg, vce);
		}
	}
}
