/*
 * $Id: ServerDatabase.java,v 1.13 2004/08/18 08:46:04 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.13 $, $Date: 2004/08/18 08:46:04 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public class ServerDatabase extends StorableObjectDatabase {

	 // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
    // type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    // user_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_USER_ID       = "user_id";    

	private Server fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Server)
			return (Server) storableObject;
		throw new IllegalDataException("ServerDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Server server = this.fromStorableObject(storableObject);
		this.retrieveServer(server);
		server.setCharacteristics(CharacteristicDatabase.retrieveCharacteristics(server.getId(), CharacteristicSort.CHARACTERISTIC_SORT_SERVER));
	}

	private void retrieveServer(Server server) throws ObjectNotFoundException, RetrieveObjectException {
		String serverIdStr = server.getId().toSQLString();
		String sql = SQL_SELECT
			+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
			+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_USER_ID
			+ SQL_FROM + ObjectEntities.SERVER_ENTITY
			+ SQL_WHERE + COLUMN_ID + EQUALS + serverIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.retrieveServer | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {				
				server.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
													DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),													
													resultSet.getString(COLUMN_NAME),
													resultSet.getString(COLUMN_DESCRIPTION),													
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_USER_ID)));
			}
			else
				throw new ObjectNotFoundException("No such server: " + serverIdStr);
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
			}
		}
	}	
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Server server = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case Server.RETRIEVE_MCM_IDS:
				return this.retrieveMCMIds(server);
			default:
				return null;
		}
	}

	private List retrieveMCMIds(Server server) throws ObjectNotFoundException, RetrieveObjectException {
		List mcmIds = new ArrayList();

		String serverIdStr = server.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.MCM_ENTITY
			+ SQL_WHERE + MCMDatabase.COLUMN_SERVER_ID + EQUALS + serverIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.retrieveServer | Trying: " + sql, Log.DEBUGLEVEL05);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				mcmIds.add(new Identifier(resultSet.getString(COLUMN_ID)));
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
			}
		}
		return mcmIds;
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Server server = this.fromStorableObject(storableObject);
		try {
			this.insertServer(server);
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

	private void insertServer(Server server) throws CreateObjectException {
		String serverIdStr = server.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.SERVER_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_USER_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ serverIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(server.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(server.getModified()) + COMMA
			+ server.getCreatorId().toSQLString() + COMMA
			+ server.getModifierId().toSQLString() + COMMA
			+ server.getDomainId().toSQLString() + COMMA
			+ APOSTOPHE + server.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + server.getDescription() + APOSTOPHE + COMMA
			+ server.getUserId().toSQLString()
			+ CLOSE_BRACKET;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.insertServer | Trying: " + sql, Log.DEBUGLEVEL05);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "ServerDatabase.insertServer | Cannot insert server " + serverIdStr;
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
		Server server = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
}
