/*
 * $Id: MCMDatabase.java,v 1.23 2004/10/19 07:48:58 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
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
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.23 $, $Date: 2004/10/19 07:48:58 $
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
    		this.updateColumns = super.getUpdateColumns() + COMMA
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
    		this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA 
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
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ mcm.getDomainId().toSQLString() + COMMA
			+ APOSTOPHE + mcm.getName() + APOSTOPHE + COMMA
			+ APOSTOPHE + mcm.getDescription() + APOSTOPHE + COMMA
			+ mcm.getUserId().toSQLString() + COMMA
			+ mcm.getServerId().toSQLString(); 
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		MCM mcm = fromStorableObject(storableObject);
		int i;
		try {
			i  = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, mcm.getDomainId().toString());
			preparedStatement.setString( ++i, mcm.getName());
			preparedStatement.setString( ++i, mcm.getDescription());
			preparedStatement.setString( ++i, mcm.getUserId().toString());
			preparedStatement.setString( ++i, mcm.getServerId().toString());
		}catch (SQLException sqle) {
			throw new UpdateObjectException("MCMDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}	
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		MCM mcm = this.fromStorableObject(storableObject);
		this.retrieveEntity(mcm);
		this.retrieveKISIds(mcm);
		mcm.setCharacteristics(characteristicDatabase.retrieveCharacteristics(mcm.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
	}
	
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
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
		Connection connection = DatabaseConnection.getConnection();
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
			} finally {
                DatabaseConnection.closeConnection(connection);
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
		super.insertEntity(mcm);		
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
			list = super.retrieveByIdsOneQuery(null, condition);
		else 
			list = super.retrieveByIdsOneQuery(ids, condition);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			MCM mcm = (MCM) iter.next();
			this.retrieveKISIds(mcm);
			mcm.setCharacteristics(characteristicDatabase.retrieveCharacteristics(mcm.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
		}
		return list;	
		//return retriveByIdsPreparedStatement(ids);
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + domain.getId().toSQLString();
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage(this.getEnityName() + "Database.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		} else {
			Log.errorMessage(this.getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
