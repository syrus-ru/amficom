/*
 * $Id: ServerProcessDatabase.java,v 1.3 2005/04/29 09:55:19 arseniy Exp $
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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.3 $, $Date: 2005/04/29 09:55:19 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
public class ServerProcessDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;

	private ServerProcess fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ServerProcess)
			return (ServerProcess) storableObject;
		throw new IllegalDataException("ServerProcessDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	protected String getEnityName() {		
		return ObjectEntities.SERVERPROCESS_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
					+ ServerProcessWrapper.COLUMN_SERVER_ID + COMMA
					+ ServerProcessWrapper.COLUMN_USER_ID + COMMA
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
		ServerProcess serverProcess = this.fromStorableObject(storableObject);
		return APOSTOPHE + DatabaseString.toQuerySubString(serverProcess.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(serverProcess.getServerId()) + COMMA
			+ DatabaseIdentifier.toSQLString(serverProcess.getUserId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(serverProcess.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ServerProcess serverProcess = this.fromStorableObject(storableObject);
		this.retrieveEntity(serverProcess);	
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		ServerProcess user = (storableObject == null)?
				new ServerProcess(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null) :
					this.fromStorableObject(storableObject);
		user.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
							DatabaseIdentifier.getIdentifier(resultSet, ServerProcessWrapper.COLUMN_SERVER_ID),
							DatabaseIdentifier.getIdentifier(resultSet, ServerProcessWrapper.COLUMN_USER_ID),
							DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return user;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ServerProcess serverProcess = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  serverProcess.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public ServerProcess retrieveForServerAndCodename(Identifier serverId, String codename)
			throws RetrieveObjectException, IllegalDataException, ObjectNotFoundException {
		String condition = ServerProcessWrapper.COLUMN_SERVER_ID + EQUALS + DatabaseIdentifier.toSQLString(serverId)
				+ SQL_AND
				+ StorableObjectWrapper.COLUMN_CODENAME + EQUALS
					+ APOSTOPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTOPHE;
		Set objects = this.retrieveByCondition(condition);
		if (!objects.isEmpty())
			return (ServerProcess) objects.iterator().next();
		throw new ObjectNotFoundException("Cannot find server process '" + codename + "'");
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		ServerProcess serverProcess = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, serverProcess.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, serverProcess.getServerId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, serverProcess.getUserId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, serverProcess.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		ServerProcess serverProcess = this.fromStorableObject(storableObject);
		this.insertEntity(serverProcess);
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

}
