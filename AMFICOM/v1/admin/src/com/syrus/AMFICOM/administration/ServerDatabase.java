/*
 * $Id: ServerDatabase.java,v 1.19 2005/03/05 21:35:39 arseniy Exp $
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
import java.util.ArrayList;
import java.util.List;

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
 * @version $Revision: 1.19 $, $Date: 2005/03/05 21:35:39 $
 * @author $Author: arseniy $
 * @module administration_v1
 */

public class ServerDatabase extends CharacterizableDatabase {

	protected static final int SIZE_HOSTNAME_COLUMN = 64;

  private static String columns;
	private static String updateMultipleSQLValues;
  
	private Server fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Server)
			return (Server) storableObject;
		throw new IllegalDataException("ServerDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {		
		return ObjectEntities.SERVER_ENTITY;
	}

	protected String getColumns(int mode) {		
		if (columns == null){
			columns = COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ ServerWrapper.COLUMN_HOSTNAME + COMMA
				+ ServerWrapper.COLUMN_USER_ID;		
		}
		return super.getColumns(mode) + columns;
	}	
	
	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;		
		}
		return updateMultipleSQLValues;
	}	

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Server server = this.fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(server.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(server.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(server.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(server.getHostName(), SIZE_HOSTNAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(server.getUserId());
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Server server = (storableObject==null)?
				new Server(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								0L,
								null,
								null,
								null,
								null,
								null) :
					this.fromStorableObject(storableObject);
		server.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
								DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
								DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
								resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
								DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),													
								DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
								DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
								DatabaseString.fromQuerySubString(resultSet.getString(ServerWrapper.COLUMN_HOSTNAME)),
								DatabaseIdentifier.getIdentifier(resultSet, ServerWrapper.COLUMN_USER_ID));
		return server;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
		throws IllegalDataException, SQLException {
		Server server = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, server.getDomainId());
		DatabaseString.setString(preparedStatement, ++i, server.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, server.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, server.getHostName(), SIZE_HOSTNAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, server.getUserId());
		return i;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Server server = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Server.RETRIEVE_MCM_IDS:
				return this.retrieveMCMIds(server);
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  server.getId() + "'; argument: " + arg);
				return null;
		}
	}

	private List retrieveMCMIds(Server server) throws RetrieveObjectException {
		List mcmIds = new ArrayList();

		String serverIdStr = DatabaseIdentifier.toSQLString(server.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.MCM_ENTITY
			+ SQL_WHERE + MCMWrapper.COLUMN_SERVER_ID + EQUALS + serverIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.retrieveServer | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				mcmIds.add(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID));
			}
		}
		catch (SQLException sqle) {
			String mesg = "ServerDatabase.retrieveServer | Cannot retrieve server " + serverIdStr;
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
		return mcmIds;
	}

}
