/*
 * $Id: SystemUserDatabase.java,v 1.4 2005/06/22 12:14:16 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/22 12:14:16 $
 * @author $Author: bob $
 * @module administration_v1
 */

public final class SystemUserDatabase extends CharacterizableDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;
	static final int SIZE_LOGIN_COLUMN = 32;

	private SystemUser fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SystemUser)
			return (SystemUser) storableObject;
		throw new IllegalDataException("SystemUserDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected short getEntityCode() {
		return ObjectEntities.SYSTEMUSER_CODE;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = SystemUserWrapper.COLUMN_LOGIN + COMMA
					+ SystemUserWrapper.COLUMN_SORT + COMMA
					+ StorableObjectWrapper.COLUMN_NAME + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}	

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;		
		}
		return updateMultipleSQLValues;
	}	

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		SystemUser user = this.fromStorableObject(storableObject);
		return APOSTOPHE + DatabaseString.toQuerySubString(user.getLogin(), SIZE_LOGIN_COLUMN) + APOSTOPHE + COMMA
			+ Integer.toString(user.getSort().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(user.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(user.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		SystemUser user = this.fromStorableObject(storableObject);
		this.retrieveEntity(user);	
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, SQLException {
		SystemUser user = (storableObject == null)?
				new SystemUser(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, 0, null, null) :
					this.fromStorableObject(storableObject);
		user.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							DatabaseString.fromQuerySubString(resultSet.getString(SystemUserWrapper.COLUMN_LOGIN)),
							resultSet.getInt(SystemUserWrapper.COLUMN_SORT),				
							DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return user;
	}

	public SystemUser retrieveForLogin(String login) throws RetrieveObjectException, ObjectNotFoundException {
		String condition = SystemUserWrapper.COLUMN_LOGIN
				+ EQUALS
				+ APOSTOPHE + DatabaseString.toQuerySubString(login, SIZE_LOGIN_COLUMN) + APOSTOPHE;
		try {
			Set set = this.retrieveByCondition(condition);
			if (!set.isEmpty())
				return (SystemUser) set.iterator().next();
			throw new ObjectNotFoundException("SystemUser for login '" + login + "' not found");
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException {
		SystemUser user = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  user.getId() + "'; argument: " + arg);
				return null;
		}
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		SystemUser user = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, user.getLogin(), SIZE_LOGIN_COLUMN);
		preparedStatement.setInt(++startParameterNumber, user.getSort().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, user.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, user.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		SystemUser user = this.fromStorableObject(storableObject);
		super.insertEntity(user);
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
	}

}
