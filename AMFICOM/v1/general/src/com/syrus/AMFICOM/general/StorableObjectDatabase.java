/*
 * $Id: StorableObjectDatabase.java,v 1.12 2004/09/02 14:57:38 bob Exp $
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
import java.util.Iterator;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @version $Revision: 1.12 $, $Date: 2004/09/02 14:57:38 $
 * @author $Author: bob $
 * @module general_v1
 */

public abstract class StorableObjectDatabase {

	public static final String	APOSTOPHE				= "'";	
	public static final String	CLOSE_BRACKET		= " ) ";

	public static final String	COLUMN_CREATED					= "created";
	public static final String	COLUMN_CREATOR_ID				= "creator_id";
	public static final String	COLUMN_ID						= "id";
	public static final String	COLUMN_MODIFIED					= "modified";
	public static final String	COLUMN_MODIFIER_ID				= "modifier_id";
	public static final String	COMMA						= " , ";
	public static final String	EQUALS					= " = ";
	public static final String	LINK_COLUMN_PARAMETER_MODE		= "parameter_mode";

	public static final String	LINK_COLUMN_PARAMETER_TYPE_ID	= "parameter_type_id";
	public static final String	OPEN_BRACKET		= " ( ";
	public static final String	QUESTION				= "?";

	public static final String	SQL_AND					= " AND ";
	public static final String	SQL_ASC					= " ASC ";
	public static final String	SQL_COUNT				= " COUNT(*) ";
	public static final String	SQL_DELETE_FROM	= " DELETE FROM ";
	public static final String	SQL_DESC				= " DESC ";
	public static final String	SQL_FROM				= " FROM ";

	public static final String	SQL_FUNCTION_MAX	= " MAX ";
	public static final String	SQL_IN					= " IN ";
	public static final String	SQL_INSERT_INTO	= " INSERT INTO ";
	public static final String	SQL_NULL 				= " NULL ";
	public static final String	SQL_OR					= " OR ";
	public static final String	SQL_ORDER_BY		= " ORDER BY ";
	public static final String	SQL_SELECT			= " SELECT ";
	public static final String	SQL_SET					= " SET ";
	public static final String	SQL_UPDATE			= " UPDATE ";
	public static final String	SQL_VALUES			= " VALUES ";
	public static final String	SQL_WHERE				= " WHERE ";

	protected static Connection	connection;

	public StorableObjectDatabase() {
		connection = DatabaseConnection.getConnection();
	}	 
	
	public void delete(StorableObject storableObject) {
		String storableObjectIdStr = storableObject.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ this.getTableName()
					+ SQL_WHERE + COLUMN_ID + EQUALS + storableObjectIdStr);
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
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
	
	public abstract void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException;

	public abstract void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;
	
	public abstract List retrieveByIds(List ids) throws RetrieveObjectException;

	public abstract Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException;

	public abstract void update(StorableObject storableObject, int updateKind, Object arg) throws IllegalDataException, UpdateObjectException;
	
	protected abstract String getEnityName();
	
	protected abstract String getTableName();	
	
	protected abstract String getUpdateColumns();
	
	protected abstract String getUpdateMultiplySQLValues();
	
	protected abstract String getUpdateSingleSQLValues(StorableObject storableObject);
	
	protected void insertEntity(StorableObject storableObject) throws CreateObjectException {
		String storableObjectIdStr = storableObject.getId().toSQLString();
		String sql = SQL_INSERT_INTO 
			+ this.getTableName() + OPEN_BRACKET 
			+ this.getUpdateColumns()
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET			
			+ storableObjectIdStr + COMMA
			+ this.getUpdateSingleSQLValues(storableObject)
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName()+"Database.insertEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.insertEntity | Cannot insert "+this.getEnityName()+" '" + storableObjectIdStr + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}
	
	protected void insertListEntity(List storableObjects) throws CreateObjectException {
		
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;
		
		if (storableObjects.size() == 1){			
			insertEntity((StorableObject)storableObjects.get(0));			
			return;
		}
		
		String sql = SQL_INSERT_INTO + this.getTableName()
			+ OPEN_BRACKET
			+ this.getUpdateColumns()
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ this.getUpdateMultiplySQLValues()
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		String testIdCode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);

			for(Iterator it=storableObjects.iterator();it.hasNext();){
				
				StorableObject storableObject = (StorableObject)it.next();
				this.setEntityForPreparedStatement(storableObject, preparedStatement);
				Log.debugMessage(this.getEnityName() + "Database.insertListEntity | Inserting  " + this.getEnityName() + " " + testIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.insertListEntity | Cannot insert " + this.getEnityName() + " '" + testIdCode + "' -- " + sqle.getMessage();
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
			}
		}
	}
	
	protected void retrieveEntity(StorableObject storableObject) throws ObjectNotFoundException, RetrieveObjectException {
		String strorableObjectTypeIdStr = storableObject.getId().toSQLString();
		String sql = retrieveQuery(COLUMN_ID + EQUALS + strorableObjectTypeIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName()+"Database.retrieveEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				updateEntityFromResultSet(storableObject, resultSet);
			else
				throw new ObjectNotFoundException("No such " + getEnityName() +": " + strorableObjectTypeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = this.getEnityName() + "Database.retrieveEntity | Cannot retrieve " + getEnityName() +" '" + strorableObjectTypeIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try{
				try {
					if (resultSet != null)
						resultSet.close();
				} finally {
					if (statement != null)
						statement.close();
				}
			}catch(SQLException sqle){
				Log.errorException(sqle);
			}
		}
	}
	
	protected abstract String retrieveQuery(final String condition);
	
	protected abstract void setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement) throws SQLException;
	
	protected void updateEntity(StorableObject localStorableObject) throws UpdateObjectException{
		/**
		 * @todo recast this method !
		 */
		String atIdStr = localStorableObject.getId().toSQLString(); 
		String sql = retrieveQuery(COLUMN_ID + EQUALS + atIdStr);
		
		StorableObject storableObject = null;
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage(getEnityName()+"Database.updateEntity | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			try{
				if (resultSet.next())
					updateEntityFromResultSet(storableObject, resultSet);
				else{
					try{
						insert(localStorableObject);
					} catch (IllegalDataException ide){
						String mesg =  getEnityName()+"Database.updateEntity | Cannot update "+ getEnityName() +" '" + atIdStr + "' -- " + ide.getMessage();
						throw new UpdateObjectException(mesg, ide);
					}
					catch(CreateObjectException coe){
						String mesg =  getEnityName()+"Database.updateEntity | Cannot update "+ getEnityName() +" '" + atIdStr + "' -- " + coe.getMessage();
						throw new UpdateObjectException(mesg, coe);
					} 
				}
			}catch(RetrieveObjectException roe){
				String mesg = getEnityName()+"Database.updateEntity | Cannot update "+getEnityName() + " '"+ atIdStr + "' -- " + roe.getMessage();
				throw new UpdateObjectException(mesg, roe);
			}
		}
		catch (SQLException sqle) {
			String mesg = getEnityName()+"Database.updateEntity | Cannot update " + getEnityName() + " '" + atIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
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
	
	protected abstract StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws RetrieveObjectException, SQLException;


}

