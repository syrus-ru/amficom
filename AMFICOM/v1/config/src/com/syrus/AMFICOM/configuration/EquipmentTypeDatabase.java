/*
 * $Id: EquipmentTypeDatabase.java,v 1.10 2004/09/10 10:19:44 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.10 $, $Date: 2004/09/10 10:19:44 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class EquipmentTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return "EquipmentType";
	}
	
	protected String getTableName() {
		return ObjectEntities.EQUIPMENTTYPE_ENTITY;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
	return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		EquipmentType equipmentType = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + equipmentType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + equipmentType.getDescription() + APOSTOPHE;
		return sql;
	}
	
	private EquipmentType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EquipmentType)
			return (EquipmentType)storableObject;
		throw new IllegalDataException("EquipmentTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		super.retrieveEntity(equipmentType);
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
		+ COLUMN_CODENAME + COMMA
		+ COLUMN_DESCRIPTION
		+ SQL_FROM + ObjectEntities.EQUIPMENTTYPE_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	 
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		EquipmentType equipmentType = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, equipmentType.getCodename());
			preparedStatement.setString( ++i, equipmentType.getDescription());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("EquipmentDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		EquipmentType equipmentType = storableObject == null ? null : fromStorableObject(storableObject);
		if (equipmentType == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			equipmentType = new EquipmentType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null);			
		}
		equipmentType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									/**
										* @todo when change DB Identifier model ,change getString() to getLong()
										*/
									new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
									/**
										* @todo when change DB Identifier model ,change getString() to getLong()
										*/
									new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
									resultSet.getString(COLUMN_CODENAME),
									resultSet.getString(COLUMN_DESCRIPTION));

		
		return equipmentType;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		EquipmentType equipmentType = this.fromStorableObject(storableObject);
		try {
			super.insertEntity(equipmentType);
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
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
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
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
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
	
		
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("EquipmentTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	public void delete(EquipmentType equipmentType) {
		String eqIdStr = equipmentType.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.EQUIPMENTTYPE_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ eqIdStr;
			Log.debugMessage("EquipmentTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}
	
	public List retrieveByIds(List ids, String condition) 
			throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return super.retriveByIdsOneQuery(null, condition);
		return super.retriveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}
}
