/*
 * $Id: MeasurementPortTypeDatabase.java,v 1.7 2004/09/16 07:57:11 bob Exp $
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
 * @version $Revision: 1.7 $, $Date: 2004/09/16 07:57:11 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MeasurementPortTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	private MeasurementPortType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPortType)
			return (MeasurementPortType)storableObject;
		throw new IllegalDataException("MeasurementPortTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return "MeasurementPortType";
	}
	
	protected String getTableName() {
		return ObjectEntities.MEASUREMENTPORTTYPE_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
    		this.updateColumns = super.getUpdateColumns() + COMMA
		    		+ COLUMN_CODENAME + COMMA
					+ COLUMN_DESCRIPTION;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
    		this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA 
					+ QUESTION + COMMA
					+ QUESTION;
    	}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		MeasurementPortType measurementPortType = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + measurementPortType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + measurementPortType.getDescription() + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		MeasurementPortType measurementPortType = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			preparedStatement.setString( ++i, measurementPortType.getCodename() );
			preparedStatement.setString( ++i, measurementPortType.getDescription());
		}catch (SQLException sqle) {
			throw new UpdateObjectException("MeasurmentPortTypeDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		super.retrieveEntity(measurementPortType);
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORTTYPE_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementPortType measurementPortType = storableObject == null ? null : fromStorableObject(storableObject);
		if (measurementPortType == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			measurementPortType = new MeasurementPortType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null);			
		}
		measurementPortType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return measurementPortType;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		try {
			super.insertEntity(measurementPortType);
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
            Log.debugMessage("MeasurementPortTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	public void delete(MeasurementPortType measurementPortType) {
		String mtIdStr = measurementPortType.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.MEASUREMENTPORTTYPE_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ mtIdStr;
			Log.debugMessage("MeasurementPortTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
			return super.retrieveByIdsOneQuery( null, condition);
		return super.retrieveByIdsOneQuery( ids, condition);	
		//return retriveByIdsPreparedStatement(ids);
	}

}
