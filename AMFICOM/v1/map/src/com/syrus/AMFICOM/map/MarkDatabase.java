/*
 * $Id: MarkDatabase.java,v 1.7 2005/02/03 08:38:02 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.7 $, $Date: 2005/02/03 08:38:02 $
 * @author $Author: bob $
 * @module map_v1
 */
public class MarkDatabase extends StorableObjectDatabase {
	 public static final int SIZE_CITY_COLUMN = 128;
    public static final int SIZE_STREET_COLUMN = 128;
    public static final int SIZE_BUILDING_COLUMN = 128;

	private static String columns;
	
	private static String updateMultiplySQLValues;

	private Mark fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Mark)
			return (Mark) storableObject;
		throw new IllegalDataException(this.getEnityName() + "Database.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Mark mark = this.fromStorableObject(storableObject);
		this.retrieveEntity(mark);
	}	
	
	protected String getEnityName() {		
		return ObjectEntities.MARK_ENTITY;
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ MarkWrapper.COLUMN_NAME + COMMA
				+ MarkWrapper.COLUMN_DESCRIPTION + COMMA
				+ MarkWrapper.COLUMN_LONGITUDE + COMMA
				+ MarkWrapper.COLUMN_LATIUDE + COMMA
				+ MarkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
				+ MarkWrapper.COLUMN_DISTANCE + COMMA 
				+ MarkWrapper.COLUMN_CITY  + COMMA
				+ MarkWrapper.COLUMN_STREET + COMMA
				+ MarkWrapper.COLUMN_BUILDING;
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
				+ QUESTION 
				+ QUESTION
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Mark mark = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseString.setString(preparedStatement, ++i, mark.getName(), SIZE_NAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, mark.getDescription(), SIZE_DESCRIPTION_COLUMN);
			preparedStatement.setDouble(++i, mark.getLocation().getX());
			preparedStatement.setDouble(++i, mark.getLocation().getY());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mark.getPhysicalLink().getId());
			preparedStatement.setDouble(++i, mark.getDistance());
			DatabaseString.setString(preparedStatement, ++i, mark.getCity(), SIZE_CITY_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, mark.getStreet(), SIZE_STREET_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, mark.getBuilding(), SIZE_BUILDING_COLUMN);
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Mark mark = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mark.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mark.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ mark.getLocation().getX() + COMMA
			+ mark.getLocation().getY() + COMMA
			+ DatabaseIdentifier.toSQLString(mark.getPhysicalLink().getId()) + COMMA
			+ mark.getDistance() + COMMA
			+ DatabaseString.toQuerySubString(mark.getCity(), SIZE_CITY_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(mark.getStreet(), SIZE_STREET_COLUMN) + COMMA
			+ DatabaseString.toQuerySubString(mark.getBuilding(), SIZE_BUILDING_COLUMN);
		return values;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		Mark mark = (storableObject == null) ? 
				new Mark(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, null, null, 0.0, 0.0, null, 0.0, null, null, null) : 
					fromStorableObject(storableObject);
				
		PhysicalLink physicalLink;
		try {
			physicalLink = (PhysicalLink) MapStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, MarkWrapper.COLUMN_PHYSICAL_LINK_ID), true);
		} catch (ApplicationException ae) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + ae.getMessage();
			throw new RetrieveObjectException(msg, ae);
		} catch (SQLException sqle) {
			String msg = this.getEnityName() + "Database.updateEntityFromResultSet | Error " + sqle.getMessage();
			throw new RetrieveObjectException(msg, sqle);
		}
		mark.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_DESCRIPTION)),
							   resultSet.getDouble(MarkWrapper.COLUMN_LONGITUDE),
							   resultSet.getDouble(MarkWrapper.COLUMN_LATIUDE),
							   physicalLink,
							   resultSet.getDouble(MarkWrapper.COLUMN_DISTANCE),
							   DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_CITY)),
							   DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_STREET)),
							   DatabaseString.fromQuerySubString(resultSet.getString(MarkWrapper.COLUMN_BUILDING)));		
		return mark;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Mark mark = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Mark mark = this.fromStorableObject(storableObject);
		this.insertEntity(mark);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		try {
			characteristicDatabase.updateCharacteristics(mark);
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
		Mark mark = this.fromStorableObject(storableObject);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)GeneralDatabaseContext.getCharacteristicDatabase();
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(mark, false);
				characteristicDatabase.updateCharacteristics(mark);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(mark, true);
				characteristicDatabase.updateCharacteristics(mark);
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
