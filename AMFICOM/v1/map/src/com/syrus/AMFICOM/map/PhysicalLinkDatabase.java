/*
 * $Id: PhysicalLinkDatabase.java,v 1.1 2004/12/01 15:29:41 bob Exp $
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
 * @version $Revision: 1.1 $, $Date: 2004/12/01 15:29:41 $
 * @author $Author: bob $
 * @module map_v1
 */
public class PhysicalLinkDatabase extends StorableObjectDatabase {
	//	 name VARCHAR2(128),
    public static final String COLUMN_NAME  = "name";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // physical_link_type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_PHYSICAL_LINK_TYPE_ID = "physical_link_type_id";
    // city VARCHAR2(128),
    public static final String COLUMN_CITY  = "city";
    // street VARCHAR2(128),
    public static final String COLUMN_STREET        = "street";
    // building VARCHAR2(128),
    public static final String COLUMN_BUILDING      = "building";
    // dimension_x NUMBER(12),
    public static final String COLUMN_DIMENSION_X   = "dimension_x";
    // dimension_y NUMBER(12),
    public static final String COLUMN_DIMENSION_Y   = "dimension_y";
    // topLeft NUMBER(1),
    public static final String COLUMN_TOPLEFT       = "topLeft";


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
		/**
		 * TODO retrieve startNode, endNode
		 */
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.PHYSICAL_LINK_ENTITY;
	}	
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_PHYSICAL_LINK_TYPE_ID + COMMA
				+ COLUMN_CITY + COMMA 
				+ COLUMN_STREET + COMMA
				+ COLUMN_BUILDING + COMMA
				+ COLUMN_DIMENSION_X + COMMA
				+ COLUMN_DIMENSION_Y + COMMA
				+ COLUMN_TOPLEFT;
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
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		PhysicalLink physicalLink = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(physicalLink.getName()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(physicalLink.getDescription()));
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, physicalLink.getType().getId());			
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(physicalLink.getCity()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(physicalLink.getStreet()));
			preparedStatement.setString(++i, DatabaseString.toQuerySubString(physicalLink.getBuilding()));
			preparedStatement.setDouble(++i, physicalLink.getDimensionX());
			preparedStatement.setDouble(++i, physicalLink.getDimensionY());			
			preparedStatement.setInt(++i, (physicalLink.isTopToBottom() ? TOP_BOTTOM : 0) | (physicalLink.isLeftToRight() ? LEFT_RIGHT : 0) );
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		PhysicalLink physicalLink = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(physicalLink.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(physicalLink.getDescription()) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(physicalLink.getType().getId()) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getCity()) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getStreet()) + COMMA
			+ DatabaseString.toQuerySubString(physicalLink.getBuilding()) + COMMA
			+ physicalLink.getDimensionX() + COMMA
			+ physicalLink.getDimensionY() + COMMA
			+ ((physicalLink.isTopToBottom() ? TOP_BOTTOM : 0) | (physicalLink.isLeftToRight() ? LEFT_RIGHT : 0));
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		PhysicalLink physicalLink = (storableObject == null) ? 
				new PhysicalLink(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null, null, null, 
						null, null, null, 0L, 0L, false, false) : 
					fromStorableObject(storableObject);
				
		PhysicalLinkType type;
		try {
			type = (PhysicalLinkType) MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PHYSICAL_LINK_TYPE_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		int topLeft = resultSet.getInt(COLUMN_TOPLEFT);
		physicalLink.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),							   
							   type,
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CITY)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_STREET)),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_BUILDING)),
							   resultSet.getLong(COLUMN_DIMENSION_X),
							   resultSet.getLong(COLUMN_DIMENSION_Y),
							   (topLeft & LEFT_RIGHT) == 1,
							   (topLeft & TOP_BOTTOM) == 1);		
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
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(physicalLink);
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
		PhysicalLink physicalLink = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
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
		/**
		 * TODO retrieve startNode, endNode
		 */
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
