/*
 * $Id: MCMDatabase.java,v 1.23 2005/03/05 21:35:39 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.administration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;

import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.23 $, $Date: 2005/03/05 21:35:39 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public class MCMDatabase extends CharacterizableDatabase {

	protected static final int SIZE_HOSTNAME_COLUMN = 64;

	private static String columns;
	private static String updateMultipleSQLValues;

	private MCM fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MCM_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
    		columns = COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MCMWrapper.COLUMN_HOSTNAME + COMMA
				+ MCMWrapper.COLUMN_USER_ID + COMMA
				+ MCMWrapper.COLUMN_SERVER_ID;
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultipleSQLValues() {
    	if (updateMultipleSQLValues == null) {
    		updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
    	}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		MCM mcm = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getUserId()) + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getServerId()); 
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		MCM mcm = this.fromStorableObject(storableObject);
		int i;
		i  = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getDomainId());
		DatabaseString.setString(preparedStatement, ++i, mcm.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, mcm.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, mcm.getHostName(), SIZE_HOSTNAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getUserId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getServerId());
		return i;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MCM mcm = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (mcm == null) {
			mcm = new MCM(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null,
					0L,
					null,
					null,
					null,
					null,
					null,
					null);
		}
		mcm.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
				DatabaseString.fromQuerySubString(resultSet.getString(MCMWrapper.COLUMN_HOSTNAME)),
				DatabaseIdentifier.getIdentifier(resultSet, MCMWrapper.COLUMN_USER_ID),
				DatabaseIdentifier.getIdentifier(resultSet, MCMWrapper.COLUMN_SERVER_ID));

		return mcm;
	}

	public Collection retrieveKISIds(MCM mcm) throws RetrieveObjectException {
		Collection kisIds = new HashSet();
		String mcmIdStr = DatabaseIdentifier.toSQLString(mcm.getId());
		String sql = SQL_SELECT 
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.KIS_ENTITY
			+ SQL_WHERE + MCMWrapper.LINK_COLUMN_MCM_ID + EQUALS + mcmIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
    try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieveKISIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				kisIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieveKISIds | Cannot retrieve kis ids for mcm " + mcmIdStr;
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

		return kisIds;
	}

  public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  mcm.getId() + "'; argument: " + arg);
				return null;
		}
	}

}
