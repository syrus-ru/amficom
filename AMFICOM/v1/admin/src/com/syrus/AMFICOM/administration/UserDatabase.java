/*
 * $Id: UserDatabase.java,v 1.9 2005/02/11 07:50:02 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.administration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.9 $, $Date: 2005/02/11 07:50:02 $
 * @author $Author: bob $
 * @module administration_v1
 */

public class UserDatabase extends StorableObjectDatabase {
	private static String columns;
    private static String updateMultiplySQLValues;
    private static final int SIZE_LOGIN_COLUMN = 32; 
    
	private User fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof User)
			return (User) storableObject;
		throw new IllegalDataException("UserDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	protected String getEnityName() {		
		return '"' + ObjectEntities.USER_ENTITY + '"';
	}	

	protected String getColumns(int mode) {		
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ UserWrapper.COLUMN_LOGIN + COMMA
				+ UserWrapper.COLUMN_SORT + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;		
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;		
		}
		return updateMultiplySQLValues;
	}	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		User user = this.fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(user.getLogin(), SIZE_LOGIN_COLUMN) + APOSTOPHE + COMMA
			+ Integer.toString(user.getSort().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(user.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(user.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		User user = this.fromStorableObject(storableObject);
		this.retrieveEntity(user);	
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		User user = (storableObject == null)?
				new User(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, 0, null, null) :
					this.fromStorableObject(storableObject);
		user.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							DatabaseString.fromQuerySubString(resultSet.getString(UserWrapper.COLUMN_LOGIN)),
							resultSet.getInt(UserWrapper.COLUMN_SORT),				
							DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return user;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		User user = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}	
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement,int mode)
			throws IllegalDataException, UpdateObjectException {
		User user = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {			
			DatabaseString.setString(preparedStatement, ++i, user.getLogin(), SIZE_LOGIN_COLUMN);
			preparedStatement.setInt(++i, user.getSort().value());
			DatabaseString.setString(preparedStatement, ++i, user.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, user.getDescription(), SIZE_DESCRIPTION_COLUMN);
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		User user = this.fromStorableObject(storableObject);
		this.insertEntity(user);
	}
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		User user = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(user, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(user, modifierId, true);		
				return;
		}
	}
	
	public void update(List storableObjects, Identifier modifierId, int updateKind) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		switch (updateKind) {	
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				return;
		}
		
	}	

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);
	}

}
