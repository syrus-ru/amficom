/*
 * $Id: CharacteristicTypeDatabase.java,v 1.9 2004/08/27 15:18:15 bob Exp $
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

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.9 $, $Date: 2004/08/27 15:18:15 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class CharacteristicTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";
	public static final String COLUMN_DATA_TYPE				= "data_type";
	public static final String COLUMN_IS_EDITABLE			= "is_editable";
	public static final String COLUMN_IS_VISIBLE			= "is_visible";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private CharacteristicType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CharacteristicType)
			return (CharacteristicType)storableObject;
		throw new IllegalDataException("CharacteristicTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		this.retrieveCharacteristicType(characteristicType);
	}

	private String retrieveCharacteristicTypeQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_CODENAME + COMMA
		+ COLUMN_DESCRIPTION + COMMA
		+ COLUMN_DATA_TYPE + COMMA
		+ COLUMN_IS_EDITABLE + COMMA
		+ COLUMN_IS_VISIBLE
		+ SQL_FROM + ObjectEntities.CHARACTERISTICTYPE_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private CharacteristicType updateCharacteristicTypeFromResultSet(CharacteristicType characteristicType, ResultSet resultSet) throws SQLException{
		CharacteristicType characteristicType1 = characteristicType;
		if (characteristicType == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			characteristicType1 = new CharacteristicType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, 0,
										   false, false);			
		}
		characteristicType1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
										 resultSet.getString(COLUMN_CODENAME),
										 resultSet.getString(COLUMN_DESCRIPTION),
										 resultSet.getInt(COLUMN_DATA_TYPE),
										 (resultSet.getInt(COLUMN_IS_EDITABLE) == 0)?false:true,
										 (resultSet.getInt(COLUMN_IS_VISIBLE) == 0)?false:true);

		
		return characteristicType1;
	}

	
	private void retrieveCharacteristicType(CharacteristicType characteristicType) throws ObjectNotFoundException, RetrieveObjectException {
		String ctIdStr = characteristicType.getId().toSQLString();
		String sql = retrieveCharacteristicTypeQuery(COLUMN_ID + EQUALS + ctIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicTypeDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				updateCharacteristicTypeFromResultSet(characteristicType, resultSet);
			else
				throw new ObjectNotFoundException("No such characteristic type: " + ctIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicTypeDatabase.retrieve | Cannot retrieve characteristic type " + ctIdStr;
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
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		try {
			this.insertCharacteristicType(characteristicType);
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

	private void insertCharacteristicType(CharacteristicType characteristicType) throws CreateObjectException {
		String ctIdStr = characteristicType.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.CHARACTERISTICTYPE_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_DATA_TYPE + COMMA
			+ COLUMN_IS_EDITABLE + COMMA
			+ COLUMN_IS_VISIBLE
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET
			+ ctIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(characteristicType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(characteristicType.getModified()) + COMMA
			+ characteristicType.getCreatorId().toSQLString() + COMMA
			+ characteristicType.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + characteristicType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + characteristicType.getDescription() + APOSTOPHE + COMMA
			+ Integer.toString(characteristicType.getDataType().value()) + COMMA
			+ (characteristicType.isEditable()?"1":"0") + COMMA
			+ (characteristicType.isVisible()?"1":"0")
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicTypeDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicTypeDatabase.insert | Cannot insert characteristic type " + ctIdStr;
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
		CharacteristicType characteristicType = this.fromStorableObject(storableObject);
		switch (update_kind) {
			default:
				return;
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		List characteristicTypes = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.CHARACTERISTICTYPE_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicTypeDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				characteristicTypes.add(new CharacteristicType(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicTypeDatabase.retrieveAll | Cannot retrieve characteristic type";
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
		return characteristicTypes;
	}
	
	public void delete(CharacteristicType characteristicType) {
		String cdIdStr = characteristicType.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.CHARACTERISTICTYPE_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ cdIdStr;
			Log.debugMessage("CharacteristicTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
			sql = retrieveCharacteristicTypeQuery(buffer.toString());
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("CharacteristicTypeDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateCharacteristicTypeFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "CharacteristicTypeDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql = retrieveCharacteristicTypeQuery(buffer.toString());
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
					result.add(updateCharacteristicTypeFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("CharacteristicTypeDatabase.retriveByIdsPreparedStatement | No such characteristic type: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "CharacteristicTypeDatabase.retriveByIdsPreparedStatement | Cannot retrieve characteristic type " + sqle.getMessage();
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
