/*
 * $Id: PortTypeDatabase.java,v 1.12 2004/09/16 07:57:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.List;
import java.sql.PreparedStatement;
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
 * @version $Revision: 1.12 $, $Date: 2004/09/16 07:57:11 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class PortTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
    private String updateColumns;
    private String updateMultiplySQLValues;

	
	private PortType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PortType)
			return (PortType)storableObject;
		throw new IllegalDataException("PortTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {		
		return "PortType";
	}	
	
	protected String getTableName() {		
		return ObjectEntities.PORTTYPE_ENTITY;
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
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		PortType portType = fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + portType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + portType.getDescription() + APOSTOPHE;
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		PortType portType = this.fromStorableObject(storableObject);
		this.retrieveEntity(portType);
	}

	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION
			+ SQL_FROM + ObjectEntities.PORTTYPE_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		PortType portType = (storableObject==null)?
				new PortType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null):
					fromStorableObject(storableObject);
		portType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return portType;
	}	

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		PortType portType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		PortType portType = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(portType);
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
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, 
			VersionCollisionException, UpdateObjectException {
		PortType portType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);		
				return;
		}
	}
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
		VersionCollisionException, UpdateObjectException {
		switch (updateKind) {	
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
		
	}	

	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("PortTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = retrieveByIdsOneQuery(null, condition);
		else list = retrieveByIdsOneQuery(ids, condition);		
		return list;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
		throws IllegalDataException, UpdateObjectException {
		PortType portType = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {			
			preparedStatement.setString(++i, portType.getCodename());
			preparedStatement.setString(++i, portType.getDescription());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;	
	}
}
