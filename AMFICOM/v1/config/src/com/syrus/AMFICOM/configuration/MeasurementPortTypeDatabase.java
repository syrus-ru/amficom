/*
 * $Id: MeasurementPortTypeDatabase.java,v 1.19 2004/12/07 15:32:33 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
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
 * @version $Revision: 1.19 $, $Date: 2004/12/07 15:32:33 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class MeasurementPortTypeDatabase extends StorableObjectDatabase {
	public static final String COLUMN_CODENAME				= "codename";
	public static final String COLUMN_DESCRIPTION			= "description";
	public static final String COLUMN_NAME = "name";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	private MeasurementPortType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementPortType)
			return (MeasurementPortType)storableObject;
		throw new IllegalDataException("MeasurementPortTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MEASUREMENTPORTTYPE_ENTITY;
	}
	
	protected String getColumns(int mode) {
		if (columns == null){
    		columns = super.getColumns(mode) + COMMA
		    		+ COLUMN_CODENAME + COMMA
					+ COLUMN_DESCRIPTION + COMMA
					+ COLUMN_NAME;
		}
		return columns;
	}
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
    		updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA 
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
    	}
		return updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		MeasurementPortType measurementPortType = fromStorableObject(storableObject);
		String name = DatabaseString.toQuerySubString(measurementPortType.getName());
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementPortType.getCodename()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementPortType.getDescription()) + APOSTOPHE + COMMA
			+ APOSTOPHE + (name != null ? name : "") + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode) throws IllegalDataException,
			UpdateObjectException {
		MeasurementPortType measurementPortType = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			preparedStatement.setString( ++i, measurementPortType.getCodename() );
			preparedStatement.setString( ++i, measurementPortType.getDescription());
			preparedStatement.setString( ++i, measurementPortType.getName());
		}catch (SQLException sqle) {
			throw new UpdateObjectException("MeasurmentPortTypeDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementPortType measurementPortType = this.fromStorableObject(storableObject);
		super.retrieveEntity(measurementPortType);
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        measurementPortType.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(measurementPortType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE));
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementPortType measurementPortType = storableObject == null ? null : fromStorableObject(storableObject);
		if (measurementPortType == null){			
			measurementPortType = new MeasurementPortType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null);			
		}
		measurementPortType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
											DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
											DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
											DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
											DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
											DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)));
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
		super.insertEntity(measurementPortType);		
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
	
	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null; 
        if ((ids == null) || (ids.isEmpty()))
            list = retrieveByIdsOneQuery(null, condition);
        else 
            list = retrieveByIdsOneQuery(ids, condition);
        
        if (list != null) {
            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
            Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_MEASUREMENTPORTTYPE);
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                MeasurementPortType measurementPortType = (MeasurementPortType) iter.next();
                List characteristics = (List)characteristicMap.get(measurementPortType);
                measurementPortType.setCharacteristics(characteristics);
            }
        }
        return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		return this.retrieveButIds(ids);
	}
}
