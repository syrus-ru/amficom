/*
 * $Id: MarkDatabase.java,v 1.18 2005/03/10 15:39:16 bob Exp $
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
import com.syrus.AMFICOM.general.CharacterizableDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
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
 * @version $Revision: 1.18 $, $Date: 2005/03/10 15:39:16 $
 * @author $Author: bob $
 * @module map_v1
 */
public class MarkDatabase extends CharacterizableDatabase {
	 public static final int SIZE_CITY_COLUMN = 128;
    public static final int SIZE_STREET_COLUMN = 128;
    public static final int SIZE_BUILDING_COLUMN = 128;

	private static String columns;
	
	private static String updateMultipleSQLValues;

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
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MarkWrapper.COLUMN_LONGITUDE + COMMA
				+ MarkWrapper.COLUMN_LATIUDE + COMMA
				+ MarkWrapper.COLUMN_PHYSICAL_LINK_ID + COMMA
				+ MarkWrapper.COLUMN_DISTANCE + COMMA 
				+ MarkWrapper.COLUMN_CITY  + COMMA
				+ MarkWrapper.COLUMN_STREET + COMMA
				+ MarkWrapper.COLUMN_BUILDING;
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
				+ QUESTION 
				+ QUESTION
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		Mark mark = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseString.setString(preparedStatement, ++i, mark.getName(), SIZE_NAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, mark.getDescription(), SIZE_DESCRIPTION_COLUMN);
		preparedStatement.setDouble(++i, mark.getLocation().getX());
		preparedStatement.setDouble(++i, mark.getLocation().getY());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mark.getPhysicalLink().getId());
		preparedStatement.setDouble(++i, mark.getDistance());
		DatabaseString.setString(preparedStatement, ++i, mark.getCity(), SIZE_CITY_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, mark.getStreet(), SIZE_STREET_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, mark.getBuilding(), SIZE_BUILDING_COLUMN);
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
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
				new Mark(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, 0.0, 0.0, null, 0.0, null, null, null) : 
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
							   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
							   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
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
		super.insertEntity(mark);
	}
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);
				return;
		}
	}
	
	
	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				return;
		}

	}	
}
