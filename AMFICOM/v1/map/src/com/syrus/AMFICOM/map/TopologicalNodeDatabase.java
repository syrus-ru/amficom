/*
 * $Id: TopologicalNodeDatabase.java,v 1.1 2004/11/30 14:26:55 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/11/30 14:26:55 $
 * @author $Author: bob $
 * @module map_v1
 */
public class TopologicalNodeDatabase extends StorableObjectDatabase {
	//	 name VARCHAR2(128),
    public static final String COLUMN_NAME  = "name";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // longitude NUMBER(12,6),
    public static final String COLUMN_LONGITUDE     = "longitude";
    // latiude NUMBER(12,6),
    public static final String COLUMN_LATIUDE       = "latiude";
    // active NUMBER(1),
    public static final String COLUMN_ACTIVE        = "active";

    
	private static String columns;
	
	private static String updateMultiplySQLValues;

	private TopologicalNode fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TopologicalNode)
			return (TopologicalNode) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		this.retrieveEntity(topologicalNode);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.TOPOLOGICAL_NODE_ENTITY;
	}	
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_LONGITUDE + COMMA
				+ COLUMN_LATIUDE + COMMA 
				+ COLUMN_ACTIVE;
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
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		TopologicalNode topologicalNode = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(topologicalNode.getName()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(topologicalNode.getDescription()));
			preparedStatement.setDouble(++i, topologicalNode.getLongitude());
			preparedStatement.setDouble(++i, topologicalNode.getLatitude());
			preparedStatement.setInt(++i, topologicalNode.isActive() ? 1 : 0);
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		TopologicalNode topologicalNode = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(topologicalNode.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(topologicalNode.getDescription()) + APOSTOPHE + COMMA
			+ topologicalNode.getLongitude() + COMMA
			+ topologicalNode.getLatitude() + COMMA
			+ (topologicalNode.isActive() ? 1 : 0);
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		TopologicalNode topologicalNode = (storableObject == null) ? 
				new TopologicalNode(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, 0.0, 0.0, false) : 
					fromStorableObject(storableObject);
				
		topologicalNode.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
							   resultSet.getDouble(COLUMN_LONGITUDE),
							   resultSet.getDouble(COLUMN_LATIUDE),
							   resultSet.getInt(COLUMN_ACTIVE) == 1);		
		return topologicalNode;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		this.insertEntity(topologicalNode);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(topologicalNode);
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
		TopologicalNode topologicalNode = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(topologicalNode, false);
				characteristicDatabase.updateCharacteristics(topologicalNode);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(topologicalNode, true);
				characteristicDatabase.updateCharacteristics(topologicalNode);
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
