/*
 * $Id: ServerDatabase.java,v 1.16 2004/08/30 14:39:41 bob Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
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
 * @version $Revision: 1.16 $, $Date: 2004/08/30 14:39:41 $
 * @author $Author: bob $
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
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		Server server = this.fromStorableObject(storableObject);
		this.retrieveServer(server);
		server.setCharacteristics(characteristicDatabase.retrieveCharacteristics(server.getId(), CharacteristicSort.CHARACTERISTIC_SORT_SERVER));
	}
	
	private String retrieveServerQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ DomainMember.COLUMN_DOMAIN_ID + COMMA
		+ COLUMN_NAME + COMMA
		+ COLUMN_DESCRIPTION + COMMA
		+ COLUMN_USER_ID
		+ SQL_FROM + ObjectEntities.SERVER_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private Server updateServerFromResultSet(Server server, ResultSet resultSet) throws SQLException{
		Server server1 = server;
		if (server1 == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			server1 = new Server(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null, null);			
		}
		server1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return server1;
	}


	private void retrieveServer(Server server) throws ObjectNotFoundException, RetrieveObjectException {
		String serverIdStr = server.getId().toSQLString();
		String sql = retrieveServerQuery(COLUMN_ID + EQUALS + serverIdStr);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.retrieveServer | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateServerFromResultSet(server, resultSet);
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
			Log.debugMessage("ServerDatabase.retrieveServer | Trying: " + sql, Log.DEBUGLEVEL09);
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
			Log.debugMessage("ServerDatabase.insertServer | Trying: " + sql, Log.DEBUGLEVEL09);
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
	
	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null);
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			String condition = null;
			if (ids!=null){
				StringBuffer buffer = new StringBuffer(COLUMN_ID);
				int idsLength = ids.size();
				if (idsLength == 1){
					buffer.append(EQUALS);
					buffer.append(((Identifier)ids.iterator().next()).toSQLString());
				} else{
					buffer.append(SQL_IN);
					buffer.append(OPEN_BRACKET);
					
					int i = 1;
					for(Iterator it=ids.iterator();it.hasNext();i++){
						Identifier id = (Identifier)it.next();
						buffer.append(id.toSQLString());
						if (i < idsLength)
							buffer.append(COMMA);
					}
					
					buffer.append(CLOSE_BRACKET);
					condition = buffer.toString();
				}
			}
			sql = retrieveServerQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ServerDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateServerFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "ServerDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
		return result;
	}
	
	private List retriveByIdsPreparedStatement(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			
			int idsLength = ids.size();
			if (idsLength == 1){
				return retriveByIdsOneQuery(ids);
			}
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			buffer.append(EQUALS);							
			buffer.append(QUESTION);
			
			sql = retrieveServerQuery(buffer.toString());
		}
			
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement(sql.toString());
			for(Iterator it = ids.iterator();it.hasNext();){
				Identifier id = (Identifier)it.next(); 
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				String idStr = id.getIdentifierString();
				stmt.setString(1, idStr);
				resultSet = stmt.executeQuery();
				if (resultSet.next()){
					result.add(updateServerFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("ServerDatabase.retriveByIdsPreparedStatement | No such server: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "ServerDatabase.retriveByIdsPreparedStatement | Cannot retrieve server " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt != null)
					stmt.close();
				stmt = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}			
		
		return result;
	}

}
