/*
 * $Id: UserDatabase.java,v 1.9 2004/08/22 18:49:19 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
 * @version $Revision: 1.9 $, $Date: 2004/08/22 18:49:19 $
 * @author $Author: arseniy $
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

	private void retrieveUser(User user) throws ObjectNotFoundException, RetrieveObjectException {
		String userIdStr = user.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_LOGIN + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.USER_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + userIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("UserDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {				
				user.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
			}
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

}
