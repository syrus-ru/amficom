/*
 * $Id: MCMDatabase.java,v 1.15 2004/08/30 14:39:41 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.15 $, $Date: 2004/08/30 14:39:41 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MCMDatabase extends StorableObjectDatabase {

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_SERVER_ID = "server_id";
	//public static final String COLUMN_LOCATION = "location";
	//public static final String COLUMN_HOSTNAME = "hostname";
	
	private MCM fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		MCM mcm = this.fromStorableObject(storableObject);
		this.retrieveMCM(mcm);
		this.retrieveKISIds(mcm);
		mcm.setCharacteristics(characteristicDatabase.retrieveCharacteristics(mcm.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
	}
	
	private String retrieveMCMQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ DomainMember.COLUMN_DOMAIN_ID + COMMA
		+ COLUMN_TYPE_ID + COMMA
		+ COLUMN_NAME + COMMA
		+ COLUMN_DESCRIPTION + COMMA
		+ COLUMN_USER_ID + COMMA
		+ COLUMN_SERVER_ID
		+ SQL_FROM + ObjectEntities.MCM_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private MCM updateMCMFromResultSet(MCM mcm, ResultSet resultSet) throws SQLException{
		MCM mcm1 = mcm;
		if (mcm1 == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			mcm1 = new MCM(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null, null, null);			
		}
		mcm1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							/**
							 * @todo when change DB Identifier model ,change getString() to
							 *       getLong()
							 */
							 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
							/**
							 * @todo when change DB Identifier model ,change getString() to
							 *       getLong()
							 */
							 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
							/**
							 * @todo when change DB Identifier model ,change getString() to
							 *       getLong()
							 */
							 new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),
							 resultSet.getString(COLUMN_NAME),
							 resultSet.getString(COLUMN_DESCRIPTION),
							 /**
							 * @todo when change DB Identifier model ,change getString() to
							 *       getLong()
							 */
							 new Identifier(resultSet.getString(COLUMN_USER_ID)),
							 /**
							 * @todo when change DB Identifier model ,change getString() to
							 *       getLong()
							 */
							 new Identifier(resultSet.getString(COLUMN_SERVER_ID)));
		
		return mcm1;
	}


	private void retrieveMCM(MCM mcm) throws ObjectNotFoundException, RetrieveObjectException {
		String mcmIdStr = mcm.getId().toSQLString();
		String sql = retrieveMCMQuery(COLUMN_ID + EQUALS + mcmIdStr);

		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateMCMFromResultSet(mcm, resultSet);
			else
				throw new ObjectNotFoundException("No such mcm: " + mcmIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieve | Cannot retrieve mcm " + mcmIdStr;
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
	
	private void retrieveKISIds(MCM mcm) throws ObjectNotFoundException, RetrieveObjectException {
		List kisIds = new ArrayList();

		String mcmIdStr = mcm.getId().toSQLString();
		String sql = SQL_SELECT 
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.KIS_ENTITY
			+ SQL_WHERE + KISDatabase.COLUMN_MCM_ID + EQUALS + mcmIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieveKISIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())				
				kisIds.add(new Identifier(resultSet.getString(COLUMN_ID)));
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
		}
		mcm.setKISIds(kisIds);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		try {
			this.insertMCM(mcm);
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

	private void insertMCM(MCM mcm) throws CreateObjectException {
		String mcmIdStr = mcm.getId().toSQLString();		
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MCM_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_USER_ID + COMMA
			+ COLUMN_SERVER_ID 
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ mcmIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(mcm.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(mcm.getModified()) + COMMA
			+ mcm.getCreatorId().toSQLString() + COMMA
			+ mcm.getModifierId().toSQLString() + COMMA
			+ mcm.getDomainId().toSQLString() + COMMA
			+ APOSTOPHE + mcm.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + mcm.getDescription() + APOSTOPHE + COMMA
			+ mcm.getUserId().toSQLString() + COMMA
			+ mcm.getServerId().toSQLString() 
			+ CLOSE_BRACKET;

		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.insert | Cannot insert mcm " + mcmIdStr;
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
		MCM mcm = this.fromStorableObject(storableObject);
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
			sql = retrieveMCMQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateMCMFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql = retrieveMCMQuery(buffer.toString());
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
					result.add(updateMCMFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("MCMDatabase.retriveByIdsPreparedStatement | No such mcm: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "MCMDatabase.retriveByIdsPreparedStatement | Cannot retrieve mcm " + sqle.getMessage();
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
