/*
 * $Id: NodeLinkDatabase.java,v 1.9 2005/02/07 10:33:10 bob Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.9 $, $Date: 2005/02/07 10:33:10 $
 * @author $Author: bob $
 * @module map_v1
 */
public class NodeLinkDatabase extends StorableObjectDatabase {
	private static String columns;
	
	private static String updateMultiplySQLValues;

	private NodeLink fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TopologicalNode)
			return (NodeLink) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		NodeLink nodeLink = this.fromStorableObject(storableObject);
		this.retrieveEntity(nodeLink);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.NODE_LINK_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
				+ NodeLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ NodeLinkWrapper.COLUMN_END_NODE_ID + COMMA 
				+ NodeLinkWrapper.COLUMN_LENGTH;
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA 
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		NodeLink nodeLink = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, nodeLink.getName(), SIZE_NAME_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, nodeLink.getPhysicalLink().getId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, nodeLink.getStartNode().getId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, nodeLink.getEndNode().getId());
			preparedStatement.setDouble(++i, nodeLink.getLength());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		NodeLink nodeLink = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(nodeLink.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getPhysicalLink().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getStartNode().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(nodeLink.getEndNode().getId()) + COMMA
			+ nodeLink.getLength();
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		NodeLink nodeLink = (storableObject == null) ? 
				new NodeLink(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, null, null, null, null, 0.0) : 
					fromStorableObject(storableObject);

		PhysicalLink physicalLink;
		AbstractNode startNode;
		AbstractNode endNode;
		
		try{
			physicalLink = (PhysicalLink)MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
			startNode = (AbstractNode)MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_START_NODE_ID), true); 
			endNode = (AbstractNode)MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, NodeLinkWrapper.COLUMN_END_NODE_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		
		nodeLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   physicalLink,
							   startNode,
							   endNode,
							   resultSet.getDouble(NodeLinkWrapper.COLUMN_LENGTH));		
		return nodeLink;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		NodeLink nodeLink = this.fromStorableObject(storableObject);
		switch (retrieveKind) {			
			default:
				return null;
		}
	}	

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		NodeLink nodeLink = this.fromStorableObject(storableObject);
		this.insertEntity(nodeLink);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(nodeLink);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		NodeLink nodeLink = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(nodeLink, false);
				characteristicDatabase.updateCharacteristics(nodeLink);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(nodeLink, true);
				characteristicDatabase.updateCharacteristics(nodeLink);
				return;
		}
	}
	
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
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

}
