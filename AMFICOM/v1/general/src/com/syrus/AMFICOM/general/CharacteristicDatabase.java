/*
 * $Id: CharacteristicDatabase.java,v 1.4 2005/01/24 15:29:27 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;
import com.syrus.AMFICOM.general.corba.CharacteristicSort;

/**
 * @version $Revision: 1.4 $, $Date: 2005/01/24 15:29:27 $
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
			columns = super.getColumns(mode) + COMMA
				+ CharacteristicWrapper.COLUMN_TYPE_ID + COMMA
			+ CharacteristicWrapper.COLUMN_NAME + COMMA
			+ CharacteristicWrapper.COLUMN_DESCRIPTION + COMMA
			+ CharacteristicWrapper.COLUMN_VALUE + COMMA
			+ CharacteristicWrapper.COLUMN_EDITABLE + COMMA
			+ CharacteristicWrapper.COLUMN_VISIBLE + COMMA
			+ CharacteristicWrapper.COLUMN_SORT +	COMMA
			+ CharacteristicWrapper.COLUMN_CHARACTERIZED_ID;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA 
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

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
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
			PreparedStatement preparedStatement, int mode) throws IllegalDataException, UpdateObjectException{
		Characteristic characteristic = this.fromStorableObject(storableObject);
		int sort = characteristic.getSort().value();
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, characteristic.getType().getId());
			DatabaseString.setString(preparedStatement, ++i, characteristic.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, characteristic.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, characteristic.getValue(), SIZE_VALUE_COLUMN);
			preparedStatement.setInt( ++i, characteristic.isEditable()? 1:0);
			preparedStatement.setInt( ++i, characteristic.isVisible()? 1:0);
			preparedStatement.setInt( ++i, sort);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, characteristic.getCharacterizedId());
		}
		catch (SQLException sqle) {
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

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws RetrieveObjectException, SQLException, IllegalDataException {
		Characteristic characteristic = storableObject == null ? null : this.fromStorableObject(storableObject); 
		if (characteristic == null){
			characteristic = new Characteristic(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
																null,
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
			characteristicType = (CharacteristicType)GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, CharacteristicWrapper.COLUMN_TYPE_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		characteristic.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									 characteristicType,
									 DatabaseString.fromQuerySubString(resultSet.getString(CharacteristicWrapper.COLUMN_NAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(CharacteristicWrapper.COLUMN_DESCRIPTION)),
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

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
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

	public Map retrieveCharacteristicsByOneQuery(List list, CharacteristicSort sort) throws RetrieveObjectException, IllegalDataException {
		int sortValue = sort.value();
		String sql;
		StringBuffer buff = new StringBuffer(CharacteristicWrapper.COLUMN_SORT
							+ EQUALS + sortValue
							+ SQL_AND + CharacteristicWrapper.COLUMN_CHARACTERIZED_ID
							+ SQL_IN + OPEN_BRACKET);
		int i = 1;
		for (Iterator it = list.iterator(); it.hasNext();i++) {
			StorableObject storableObject = (StorableObject)it.next();
			Identifier id = storableObject.getId();
			// check items for Characterized
			if (!(storableObject instanceof Characterized)) {
				throw new IllegalDataException("CharacteristicDatabase.retrieveCharacteristicsByOneQuery | Illegal entity: entity " + id + " is not characterized ");
			}
			buff.append(DatabaseIdentifier.toSQLString(id));
			if (it.hasNext()) {
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					buff.append(COMMA);
				else {
					buff.append(CLOSE_BRACKET);
					buff.append(SQL_OR);
					buff.append(CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);
					buff.append(SQL_IN);
					buff.append(OPEN_BRACKET);
				}                   
			}
		}
		buff.append(CLOSE_BRACKET);        
		sql = retrieveQuery(buff.toString());

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicDatabase.retrieveCharacteristicsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			Map characteristicMap = new HashMap();
			while (resultSet.next()) {
				StorableObject storableObject = null;
				Identifier storableObjectId = DatabaseIdentifier.getIdentifier(resultSet, CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);
				for (Iterator it = list.iterator(); it.hasNext();) {
					StorableObject storableObjectToCompare = (StorableObject) it.next();
					if (storableObjectToCompare.getId().equals(storableObjectId)){
						storableObject = storableObjectToCompare;
						break;
					}                   
				}

				if (storableObject == null) {
					String mesg = "CharacteristicDatabase.retrieveCharacteristicsByOneQuery | Cannot found correspond result for '" + storableObjectId +"'" ;
					throw new RetrieveObjectException(mesg);
				}
				StorableObject characteristic = updateEntityFromResultSet(null, resultSet);
				List characteristics = (List)characteristicMap.get(storableObject);
				if (characteristics == null) {
					characteristics = new LinkedList();
					characteristicMap.put(storableObject, characteristics);
				}               
				characteristics.add(characteristic);              
			}

//      for (Iterator it = characteristicMap.keySet().iterator(); it.hasNext();) {
//				Characterized characterized = (Characterized) it.next();
//				List characteristics = (List)characteristicMap.get(characterized);
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

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list = null;
		if (condition instanceof LinkedIdsCondition) {
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
			Identifier id = linkedIdsCondition.getIdentifier();
			CharacteristicSort sort = Characteristic.getSortForId(id);

			if (sort != null) {
				list = retrieveCharacteristics(id, sort);
			}
		}
		else
			list =  this.retrieveButIds(ids);
		return list;
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
				if(!characteristicIds.contains(dbCharacteristicId)) {
					super.delete(dbCharacteristicId);
				}
			}

			//  insert or update
			super.checkAndUpdateEntities(characteristics, true);
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

  public void updateCharacteristics(List storableObjects) throws UpdateObjectException {
    // Construction of Map <StorableObjectIdentifier> <List <CharacteristicIdentifier> >
		if(storableObjects == null || storableObjects.isEmpty())
			return;

    Map storableObjectIdCharIdsMap = new HashMap();
		List characteristics = new LinkedList();
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			if (!(storableObject instanceof Characterized)) {
				String mesg = "CharacteristicDatabase.updateCharacteristics(List) | Storable object " + 
								storableObject.getClass().getName() + " is not a type of Characterized";
				throw new UpdateObjectException(mesg);                
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
		StringBuffer buff = new StringBuffer();
		buff.append(CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);
		buff.append(SQL_IN);
		buff.append(OPEN_BRACKET);
		int i = 0;
		for (Iterator it = storableObjects.iterator(); it.hasNext();i++) {
			StorableObject storableObject = (StorableObject) it.next();
			buff.append(APOSTOPHE);
			buff.append(storableObject.getId());
			buff.append(APOSTOPHE);
			if (it.hasNext()) {
				if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					buff.append(COMMA);
				else {
					buff.append(CLOSE_BRACKET);
					buff.append(SQL_OR);
					buff.append(CharacteristicWrapper.COLUMN_CHARACTERIZED_ID);
					buff.append(SQL_IN);
					buff.append(OPEN_BRACKET);
				}                   
			}
		}
		buff.append(CLOSE_BRACKET);        
		String sql = retrieveQuery(buff.toString());
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
			super.checkAndUpdateEntities(characteristics, true);
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
