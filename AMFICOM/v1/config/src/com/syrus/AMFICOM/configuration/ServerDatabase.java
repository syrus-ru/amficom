/*
 * $Id: ServerDatabase.java,v 1.7 2004/08/09 13:21:26 bob Exp $
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

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.7 $, $Date: 2004/08/09 13:21:26 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class ServerDatabase extends StorableObjectDatabase {

	 // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // domain_id VARCHAR2(32),
    public static final String COLUMN_DOMAIN_ID     = "domain_id";
    // name VARCHAR2(64) NOT NULL,
    public static final String COLUMN_NAME  = "name";
    // type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_TYPE_ID       = "type_id";
    // user_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_USER_ID       = "user_id";    

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Server server = this.fromStorableObject(storableObject);
		this.retrieveServer(server);	
	}

	private void retrieveServer(Server server) throws ObjectNotFoundException, RetrieveObjectException {
		String serverIdStr = server.getId().toSQLString();
		String sql;		
		{
		StringBuffer buffer = new StringBuffer();
		buffer.append(StorableObjectDatabase.SQL_SELECT);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_CREATED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DatabaseDate.toQuerySubString(StorableObjectDatabase.COLUMN_MODIFIED));
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_CREATOR_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(StorableObjectDatabase.COLUMN_MODIFIER_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(DomainMember.COLUMN_DOMAIN_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_TYPE_ID);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_NAME);
		buffer.append(StorableObjectDatabase.COMMA);
		buffer.append(COLUMN_DESCRIPTION);
		buffer.append(StorableObjectDatabase.SQL_SELECT);
		buffer.append(COLUMN_USER_ID);
		buffer.append(StorableObjectDatabase.SQL_FROM);
		buffer.append(ObjectEntities.SERVER_ENTITY);
		buffer.append(StorableObjectDatabase.SQL_WHERE);
		buffer.append(StorableObjectDatabase.COLUMN_ID);
		buffer.append(StorableObjectDatabase.EQUALS);
		buffer.append(serverIdStr);
		sql = buffer.toString();
		}
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL05);
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
													resultSet.getString(EquipmentDatabase.COLUMN_NAME),
													resultSet.getString(EquipmentDatabase.COLUMN_DESCRIPTION),													
													/**
														* @todo when change DB Identifier model ,change getString() to getLong()
														*/
													new Identifier(resultSet.getString(COLUMN_USER_ID)));
			}
			else
				throw new ObjectNotFoundException("No such server: " + serverIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "ServerDatabase.retrieve | Cannot retrieve server " + serverIdStr;
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
			default:
				return null;
		}
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
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String serverIdCode = server.getId().getCode();

		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier domainId = server.getDomainId();		
		
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		Identifier userId = server.getUserId();

		
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.SERVER_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_USER_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(1, serverIdCode);
			preparedStatement.setDate(2, new java.sql.Date(server.getCreated().getTime()));
			preparedStatement.setDate(3, new java.sql.Date(server.getModified().getTime()));
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(4, server.getCreatorId().getCode());
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(5, server.getModifierId().getCode());
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(6, (domainId != null)?domainId.getCode():Identifier.getNullSQLString());
			
			/**
			  * FIXME insert here type value
			  */			
			preparedStatement.setString(7, Identifier.getNullSQLString());

			preparedStatement.setString(8, server.getName());
			
			preparedStatement.setString(9, server.getDescription());			
			
			/**
			  * @todo when change DB Identifier model ,change setString() to setLong()
			  */
			preparedStatement.setString(10, (userId != null)?userId.getCode():Identifier.getNullSQLString());
										
			Log.debugMessage("ServerDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL05);
			preparedStatement.executeUpdate();
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "ServerDatabase.insert | Cannot insert server " + serverIdCode;
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
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

	private Server fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Server)
			return (Server) storableObject;
		throw new IllegalDataException("ServerDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
}
