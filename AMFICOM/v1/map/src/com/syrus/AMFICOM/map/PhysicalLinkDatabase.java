/*
 * $Id: PhysicalLinkDatabase.java,v 1.17 2005/03/09 14:49:53 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.map;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CharacteristicDatabase;
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralDatabaseContext;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.17 $, $Date: 2005/03/09 14:49:53 $
 * @author $Author: bass $
 * @module map_v1
 */
public class PhysicalLinkDatabase extends CharacterizableDatabase {
	private static final int LEFT_RIGHT = 0x01;
    private static final int TOP_BOTTOM = 0x02;
    
	private static String columns;
	
	private static String updateMultipleSQLValues;
	
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
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
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
		return super.getColumns(mode) + columns;
	}	
	
	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
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
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		PhysicalLink physicalLink = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
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
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
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
				new PhysicalLink(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, null, null, 
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
		physicalLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),							   
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
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				characteristicDatabase.updateCharacteristics(storableObject);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				characteristicDatabase.updateCharacteristics(storableObject);
				return;
		}
	}
	
	
	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				characteristicDatabase.updateCharacteristics(storableObjects);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				characteristicDatabase.updateCharacteristics(storableObjects);
				return;
		}

	}
	

	public Collection retrieveByIds(Collection ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, conditions);
		return retrieveByIdsOneQuery(ids, conditions);	
		//return retriveByIdsPreparedStatement(ids, conditions);
	}	
	
}
