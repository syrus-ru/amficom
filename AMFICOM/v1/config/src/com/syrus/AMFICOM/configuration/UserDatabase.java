/*
 * $Id: UserDatabase.java,v 1.14 2004/09/16 07:57:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.14 $, $Date: 2004/09/16 07:57:11 $
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
    
    private String updateColumns;
    private String updateMultiplySQLValues;
    
	private User fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof User)
			return (User) storableObject;
		throw new IllegalDataException("UserDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	protected String getEnityName() {		
		return "User";
	}	
	
	protected String getTableName() {		
		return ObjectEntities.USER_ENTITY;
	}
	
	protected String getUpdateColumns() {		
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_LOGIN + COMMA
				+ COLUMN_SORT + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION;		
		}
		return this.updateColumns;
	}	
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;		
		}
		return this.updateMultiplySQLValues;
	}	
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		User user = fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + user.getLogin() + APOSTOPHE + COMMA
			+ Integer.toString(user.getSort().value()) + COMMA
			+ APOSTOPHE + user.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + user.getDescription() + APOSTOPHE;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		User user = this.fromStorableObject(storableObject);
		this.retrieveEntity(user);	
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_LOGIN + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.USER_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		User user = (storableObject == null)?
				new User(new Identifier(resultSet.getString(COLUMN_ID)), null, null, 0, null, null) :
					fromStorableObject(storableObject);
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
		return user;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		User user = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}	
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		User user = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {			
			preparedStatement.setString(++i, user.getLogin());
			preparedStatement.setInt(++i, user.getSort().value());
			preparedStatement.setString(++i, user.getName());
			preparedStatement.setString(++i, user.getDescription());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		User user = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(user);
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
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		User user = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);		
				return;
		}
	}
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		switch (updateKind) {	
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
		
	}	

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, condition);
		return retrieveByIdsOneQuery(ids, condition);
	}

}
