/*
 * $Id: MCMDatabase.java,v 1.16 2004/09/08 16:34:37 max Exp $
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
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.16 $, $Date: 2004/09/08 16:34:37 $
 * @author $Author: max $
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
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	private MCM fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return "MCM";
	}
	
	protected String getTableName() {
		return ObjectEntities.MCM_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
    		this.updateColumns = super.getUpdateColumns() 
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_USER_ID + COMMA
				+ COLUMN_SERVER_ID;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
    	if (this.updateMultiplySQLValues == null){
    		this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() 
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
    	}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		MCM mcm = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject)
			+ mcm.getDomainId().toSQLString() + COMMA
			+ APOSTOPHE + mcm.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + mcm.getDescription() + APOSTOPHE + COMMA
			+ mcm.getUserId().toSQLString() + COMMA
			+ mcm.getServerId().toSQLString(); 
		return sql;
	}
	
	protected void setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		MCM mcm = fromStorableObject(storableObject);
		String mcmIdStr = mcm.getId().getCode();
		try {
			super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( 6, mcmIdStr);
			preparedStatement.setString( 7, mcm.getName());
			preparedStatement.setString( 8, mcm.getDescription());
			preparedStatement.setString( 9, mcm.getUserId().toString());
			preparedStatement.setString( 10, mcm.getServerId().toString());
			preparedStatement.setString( 11, mcmIdStr);
		}catch (SQLException sqle) {
			throw new UpdateObjectException("MCMDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
	}	
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		MCM mcm = this.fromStorableObject(storableObject);
		super.retrieveEntity(mcm);
		this.retrieveKISIds(mcm);
		mcm.setCharacteristics(characteristicDatabase.retrieveCharacteristics(mcm.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
	}
	
	protected String retrievQeuery(String condition){
		return super.retrieveQuery(condition)
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_USER_ID + COMMA
			+ COLUMN_SERVER_ID
			+ SQL_FROM + ObjectEntities.MCM_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MCM mcm = storableObject == null ? null : fromStorableObject(storableObject);
		if (mcm == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			mcm = new MCM(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null, null, null);			
		}
		mcm.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		
		return mcm;
	}
		
	private void retrieveKISIds(MCM mcm) throws RetrieveObjectException {
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
			this.insertEntity(mcm);
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
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
		
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) 
			throws IllegalDataException, VersionCollisionException, 
			UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
			break;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
	}
	
	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retriveByIdsOneQuery(null, condition);
		else 
			list = super.retriveByIdsOneQuery(ids, condition);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			MCM mcm = (MCM) iter.next();
			this.retrieveKISIds(mcm);
			mcm.setCharacteristics(characteristicDatabase.retrieveCharacteristics(mcm.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
		}
		return list;	
		//return retriveByIdsPreparedStatement(ids);
	}

}
