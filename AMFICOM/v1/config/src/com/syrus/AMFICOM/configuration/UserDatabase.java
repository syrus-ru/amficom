/*
 * $Id: UserDatabase.java,v 1.10 2004/08/29 10:54:24 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
 * @version $Revision: 1.10 $, $Date: 2004/08/29 10:54:24 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class UserDatabase extends StorableObjectDatabase {
	// table :: users
	// description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // login VARCHAR2(32) NOT NULL,
    public static final String COLUMN_LOGIN = "login";
    // name VARCHAR2(64) not NULL,
    public static final String COLUMN_NAME  = "name";
    // sort NUMBER(2, 0) NOT NULL,
    public static final String COLUMN_SORT  = "sort";
    
	private User fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof User)
			return (User) storableObject;
		throw new IllegalDataException("UserDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		User user = this.fromStorableObject(storableObject);
		this.retrieveUser(user);	
	}
	
	private String retrieveUserQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_LOGIN + COMMA
		+ COLUMN_SORT + COMMA
		+ COLUMN_NAME + COMMA
		+ COLUMN_DESCRIPTION
		+ SQL_FROM + ObjectEntities.USER_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private User updateUserFromResultSet(User user, ResultSet resultSet) throws SQLException{
		User user1 = user;
		if (user1 == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			user1 = new User(new Identifier(resultSet.getString(COLUMN_ID)), null, null, 0, null, null);			
		}
		user1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							/**
								* @todo when change DB Identifier model ,change getString() to getLong()
								*/
							new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
							/**
								* @todo when change DB Identifier model ,change getString() to getLong()
								*/
							new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
							
							resultSet.getString(COLUMN_LOGIN),
							resultSet.getInt(COLUMN_SORT),				
							resultSet.getString(EquipmentDatabase.COLUMN_NAME),
							resultSet.getString(EquipmentDatabase.COLUMN_DESCRIPTION));
		return user1;
	}

	private void retrieveUser(User user) throws ObjectNotFoundException, RetrieveObjectException {
		String userIdStr = user.getId().toSQLString();
		String sql = retrieveUserQuery(COLUMN_ID + EQUALS + userIdStr);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateUserFromResultSet(user, resultSet);
			else
				throw new ObjectNotFoundException("No such user: " + userIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "UserDatabase.retrieve | Cannot retrieve user " + userIdStr;
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
	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		User user = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		User user = this.fromStorableObject(storableObject);
		try {
			this.insertUser(user);
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

	private void insertUser(User user) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String userIdStr = user.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.USER_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_LOGIN + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION 
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ userIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(user.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(user.getModified()) + COMMA
			+ user.getCreatorId().toSQLString() + COMMA
			+ user.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + user.getLogin() + APOSTOPHE + COMMA
			+ Integer.toString(user.getSort().value()) + COMMA
			+ APOSTOPHE + user.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + user.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;
			
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserDatabase.insertUser | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "UserDatabase.insertUser | Cannot insert user '" + userIdStr + "'";
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		User user = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null);
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			String condition = null;
			if (ids!=null){
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
					condition = buffer.toString();
				}
			}
			sql = retrieveUserQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateUserFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "UserDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql = retrieveUserQuery(buffer.toString());
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
					result.add(updateUserFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("UserDatabase.retriveByIdsPreparedStatement | No such user: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "UserDatabase.retriveByIdsPreparedStatement | Cannot retrieve user " + sqle.getMessage();
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
