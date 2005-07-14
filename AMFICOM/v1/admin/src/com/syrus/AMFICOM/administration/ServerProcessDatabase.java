/*
 * $Id: ServerProcessDatabase.java,v 1.10 2005/07/14 18:04:11 arseniy Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/07/14 18:04:11 $
 * @author $Author: arseniy $
 * @module admin_v1
 */
public final class ServerProcessDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;

	private ServerProcess fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ServerProcess)
			return (ServerProcess) storableObject;
		throw new IllegalDataException("ServerProcessDatabase.fromStorableObject | Illegal Storable Object: "
				+ storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.SERVERPROCESS_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
					+ ServerProcessWrapper.COLUMN_SERVER_ID + COMMA
					+ ServerProcessWrapper.COLUMN_USER_ID + COMMA
					+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}	

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;		
		}
		return updateMultipleSQLValues;
	}	

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final ServerProcess serverProcess = this.fromStorableObject(storableObject);
		return APOSTROPHE + DatabaseString.toQuerySubString(serverProcess.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(serverProcess.getServerId()) + COMMA
			+ DatabaseIdentifier.toSQLString(serverProcess.getUserId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(serverProcess.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
	}

	@Override
	public void retrieve(final StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final ServerProcess serverProcess = this.fromStorableObject(storableObject);
		this.retrieveEntity(serverProcess);	
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, SQLException {
		final ServerProcess user = (storableObject == null) ? new ServerProcess(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null) : this.fromStorableObject(storableObject);
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

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final ServerProcess serverProcess = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  serverProcess.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public ServerProcess retrieveForServerAndCodename(final Identifier serverId, final String codename)
			throws RetrieveObjectException, IllegalDataException, ObjectNotFoundException {
		final String condition = ServerProcessWrapper.COLUMN_SERVER_ID + EQUALS + DatabaseIdentifier.toSQLString(serverId)
				+ SQL_AND
				+ StorableObjectWrapper.COLUMN_CODENAME + EQUALS
					+ APOSTROPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTROPHE;
		final Set objects = this.retrieveByCondition(condition);
		if (!objects.isEmpty())
			return (ServerProcess) objects.iterator().next();
		throw new ObjectNotFoundException("Cannot find server process '" + codename + "'");
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final ServerProcess serverProcess = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, serverProcess.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, serverProcess.getServerId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, serverProcess.getUserId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, serverProcess.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	public void insert(final StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		final ServerProcess serverProcess = this.fromStorableObject(storableObject);
		super.insertEntity(serverProcess);
	}

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

}
