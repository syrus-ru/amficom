/*
 * $Id: SiteNodeDatabase.java,v 1.2 2004/12/01 16:16:03 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.configuration.CharacteristicDatabase;
import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.2 $, $Date: 2004/12/01 16:16:03 $
 * @author $Author: bob $
 * @module map_v1
 */
public class SiteNodeDatabase extends StorableObjectDatabase {
	 // name VARCHAR2(128),
    public static final String COLUMN_NAME  = "name";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // longitude NUMBER(12,6),
    public static final String COLUMN_LONGITUDE     = "longitude";
    // latiude NUMBER(12,6),
    public static final String COLUMN_LATIUDE       = "latiude";
    // image_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_IMAGE_ID      = "image_id";
    // site_node_type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_SITE_NODE_TYPE_ID     = "site_node_type_id";
    // city VARCHAR2(128),
    public static final String COLUMN_CITY  = "city";
    // street VARCHAR2(128),
    public static final String COLUMN_STREET        = "street";
    // building VARCHAR2(128),
    public static final String COLUMN_BUILDING      = "building";
    
	private static String columns;
	
	private static String updateMultiplySQLValues;

	private SiteNode fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof SiteNode)
			return (SiteNode) storableObject;
		throw new IllegalDataException("SiteNodeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		SiteNode siteNode = this.fromStorableObject(storableObject);
		this.retrieveEntity(siteNode);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.SITE_NODE_ENTITY;
	}	
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_LONGITUDE + COMMA
				+ COLUMN_LATIUDE + COMMA 
				+ COLUMN_IMAGE_ID + COMMA
				+ COLUMN_SITE_NODE_TYPE_ID + COMMA
				+ COLUMN_CITY + COMMA
				+ COLUMN_STREET + COMMA
				+ COLUMN_BUILDING;
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA 
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION 
				+ QUESTION
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		SiteNode siteNode = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(siteNode.getName()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(siteNode.getDescription()));
			preparedStatement.setDouble(++i, siteNode.getLongitude());
			preparedStatement.setDouble(++i, siteNode.getLatitude());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, siteNode.getImageId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, siteNode.getType().getId());
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(siteNode.getCity()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(siteNode.getStreet()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(siteNode.getBuilding()));
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		SiteNode siteNode = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(siteNode.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(siteNode.getDescription()) + APOSTOPHE + COMMA
			+ siteNode.getLongitude() + COMMA
			+ siteNode.getLatitude() + COMMA
			+ DatabaseIdentifier.toSQLString(siteNode.getImageId()) + COMMA
			+ DatabaseIdentifier.toSQLString(siteNode.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getCity()) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getStreet()) + COMMA
			+ DatabaseString.toQuerySubString(siteNode.getBuilding());
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		SiteNode siteNode = (storableObject == null) ? 
				new SiteNode(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null, null, 0.0, 0.0, null, null, null) : 
					fromStorableObject(storableObject);
				
		SiteNodeType type;
		try {
			type = (SiteNodeType) MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SITE_NODE_TYPE_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		siteNode.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
							   resultSet.getDouble(COLUMN_LONGITUDE),
							   resultSet.getDouble(COLUMN_LATIUDE),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_IMAGE_ID),
							   type,
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CITY)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_STREET)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_BUILDING)));		
		return siteNode;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		SiteNode siteNode = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		SiteNode siteNode = this.fromStorableObject(storableObject);
		this.insertEntity(siteNode);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(siteNode);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		SiteNode siteNode = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(siteNode, false);
				characteristicDatabase.updateCharacteristics(siteNode);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(siteNode, true);
				characteristicDatabase.updateCharacteristics(siteNode);
				return;
		}
	}
	
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				characteristicDatabase.updateCharacteristics(storableObjects);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				characteristicDatabase.updateCharacteristics(storableObjects);
				return;
		}

	}
	

	public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, conditions);
		return retrieveByIdsOneQuery(ids, conditions);	
		//return retriveByIdsPreparedStatement(ids, conditions);
	}	
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		{
			Log.errorMessage(getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}

}
