/*
 * $Id: PortTypeDatabase.java,v 1.17 2004/11/10 15:23:51 bob Exp $
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
import com.syrus.AMFICOM.general.StorableObjectCondition;
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
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.17 $, $Date: 2004/11/10 15:23:51 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class PortTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";
	public static final String COLUMN_NAME = "name";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
    private static String columns;
    private static String updateMultiplySQLValues;

	
	private PortType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof PortType)
			return (PortType)storableObject;
		throw new IllegalDataException("PortTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {		
		return ObjectEntities.PORTTYPE_ENTITY;
	}	
	
	protected String getColumns() {		
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_NAME;		
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;		
		}
		return updateMultiplySQLValues;
	}	
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		PortType portType = fromStorableObject(storableObject);
		String name = DatabaseString.toQuerySubString(portType.getName());
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(portType.getCodename()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(portType.getDescription()) + APOSTOPHE + COMMA
			+ APOSTOPHE + (name != null ? name : "") + APOSTOPHE;
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		PortType portType = this.fromStorableObject(storableObject);
		this.retrieveEntity(portType);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		PortType portType = (storableObject==null)?
				new PortType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null):
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
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
								DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)));
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
		this.insertEntity(portType);
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
			preparedStatement.setString(++i, portType.getName());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;	
	}
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		return this.retrieveButIds(ids);
	}
}
