/*
 * $Id: PhysicalLinkDatabase.java,v 1.7 2005/01/25 14:36:58 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.7 $, $Date: 2005/01/25 14:36:58 $
 * @author $Author: bob $
 * @module map_v1
 */
public class PhysicalLinkDatabase extends StorableObjectDatabase {
	private static final int LEFT_RIGHT = 0x01;
    private static final int TOP_BOTTOM = 0x02;
    
	private static String columns;
	
	private static String updateMultiplySQLValues;
	
	private PhysicalLink fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PhysicalLink)
			return (PhysicalLink) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		PhysicalLink physicalLink = this.fromStorableObject(storableObject);
		this.retrieveEntity(physicalLink);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.PHYSICAL_LINK_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ PhysicalLinkWrapper.COLUMN_NAME + COMMA
				+ PhysicalLinkWrapper.COLUMN_DESCRIPTION + COMMA
				+ PhysicalLinkWrapper.COLUMN_PHYSICAL_LINK_TYPE_ID + COMMA
				+ PhysicalLinkWrapper.COLUMN_CITY + COMMA 
				+ PhysicalLinkWrapper.COLUMN_STREET + COMMA
				+ PhysicalLinkWrapper.COLUMN_BUILDING + COMMA
				+ PhysicalLinkWrapper.COLUMN_DIMENSION_X + COMMA
				+ PhysicalLinkWrapper.COLUMN_DIMENSION_Y + COMMA
				+ PhysicalLinkWrapper.COLUMN_TOPLEFT + COMMA
				+ PhysicalLinkWrapper.COLUMN_START_NODE_ID + COMMA
				+ PhysicalLinkWrapper.COLUMN_END_NODE_ID;
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
				+ QUESTION + COMMA
				+ QUESTION + COMMA 
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
		PhysicalLink physicalLink = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, physicalLink.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, physicalLink.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, physicalLink.getType().getId());
			DatabaseString.setString(preparedStatement, ++i, physicalLink.getCity(), MarkDatabase.SIZE_CITY_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, physicalLink.getStreet(), MarkDatabase.SIZE_STREET_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, physicalLink.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN);
			preparedStatement.setInt(++i, physicalLink.getDimensionX());
			preparedStatement.setInt(++i, physicalLink.getDimensionY());			
			preparedStatement.setInt(++i, (physicalLink.isTopToBottom() ? TOP_BOTTOM : 0) | (physicalLink.isLeftToRight() ? LEFT_RIGHT : 0) );
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, physicalLink.getStartNode().getId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, physicalLink.getEndNode().getId());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		PhysicalLink physicalLink = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(physicalLink.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(physicalLink.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(physicalLink.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getCity(), MarkDatabase.SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getStreet(), MarkDatabase.SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getBuilding(), MarkDatabase.SIZE_BUILDING_COLUMN) + COMMA
			+ physicalLink.getDimensionX() + COMMA
			+ physicalLink.getDimensionY() + COMMA
			+ ((physicalLink.isTopToBottom() ? TOP_BOTTOM : 0) | (physicalLink.isLeftToRight() ? LEFT_RIGHT : 0)) + COMMA
			+ DatabaseIdentifier.toSQLString(physicalLink.getStartNode().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(physicalLink.getEndNode().getId());
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		PhysicalLink physicalLink = (storableObject == null) ? 
				new PhysicalLink(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null, null, null, 
						null, null, null, 0, 0, false, false) : 
					fromStorableObject(storableObject);
				
		PhysicalLinkType type;
		AbstractNode startNode;
		AbstractNode endNode;
		try {
			type = (PhysicalLinkType) MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkWrapper.COLUMN_PHYSICAL_LINK_TYPE_ID), true);
			startNode = (AbstractNode) MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkWrapper.COLUMN_START_NODE_ID), true);
			endNode = (AbstractNode) MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, PhysicalLinkWrapper.COLUMN_END_NODE_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		int topLeft = resultSet.getInt(PhysicalLinkWrapper.COLUMN_TOPLEFT);
		physicalLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_DESCRIPTION)),							   
							   type,
							   DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_CITY)),
							   DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_STREET)),
							   DatabaseString.fromQuerySubString(resultSet.getString(PhysicalLinkWrapper.COLUMN_BUILDING)),
							   resultSet.getInt(PhysicalLinkWrapper.COLUMN_DIMENSION_X),
							   resultSet.getInt(PhysicalLinkWrapper.COLUMN_DIMENSION_Y),
							   (topLeft & LEFT_RIGHT) == 1,
							   (topLeft & TOP_BOTTOM) == 1,
							   startNode,
							   endNode);		
		return physicalLink;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Mark mark = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		PhysicalLink physicalLink = this.fromStorableObject(storableObject);
		this.insertEntity(physicalLink);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(physicalLink);
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
		PhysicalLink physicalLink = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(physicalLink, false);
				characteristicDatabase.updateCharacteristics(physicalLink);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(physicalLink, true);
				characteristicDatabase.updateCharacteristics(physicalLink);
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
