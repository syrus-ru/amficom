/*
 * $Id: TransmissionPathTypeDatabase.java,v 1.13 2004/12/10 16:07:30 bob Exp $
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
 * @version $Revision: 1.13 $, $Date: 2004/12/10 16:07:30 $
 * @author $Author: bob $
 * @module module_name
 */

public class TransmissionPathTypeDatabase extends StorableObjectDatabase {
    public static final String COLUMN_CODENAME              = "codename";
    public static final String COLUMN_DESCRIPTION           = "description";
    public static final String COLUMN_NAME                  = "name";
    
    private static String columns;
    private static String updateMultiplySQLValues;
    
    public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
        TransmissionPathType transmissionPathType = this.fromStorableObject(storableObject);
        super.retrieveEntity(transmissionPathType);
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
        transmissionPathType.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(transmissionPathType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATHTYPE));
    }
    
    private TransmissionPathType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
        if (storableObject instanceof TransmissionPathType)
            return (TransmissionPathType)storableObject;
        throw new IllegalDataException("TransmissionPathTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
    } 
    
    protected String getEnityName() {
        return ObjectEntities.TRANSPATHTYPE_ENTITY;
    }
    
    protected String getColumns(int mode) {
        if (columns == null){
            columns  = super.getColumns(mode) + COMMA
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
        TransmissionPathType transmissionPathType = fromStorableObject(storableObject);
        String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(transmissionPathType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(transmissionPathType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(transmissionPathType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE;
        return sql;
    }
    
    protected int setEntityForPreparedStatement(StorableObject storableObject,
            PreparedStatement preparedStatement, int mode) throws IllegalDataException,
            UpdateObjectException {
        TransmissionPathType transmissionPathType = fromStorableObject(storableObject);
        int i;
        try {
            i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
            DatabaseString.setString(preparedStatement, ++i, transmissionPathType.getCodename(), SIZE_CODENAME_COLUMN);
            DatabaseString.setString(preparedStatement, ++i, transmissionPathType.getDescription(), SIZE_DESCRIPTION_COLUMN);
            DatabaseString.setString(preparedStatement, ++i, transmissionPathType.getName(), SIZE_NAME_COLUMN);
        } catch (SQLException sqle) {
            throw new UpdateObjectException("TransmissionPathTypeDatabase." +
                    "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
        }
        return i;
    }
    
    protected StorableObject updateEntityFromResultSet(
            StorableObject storableObject, ResultSet resultSet)
            throws IllegalDataException, RetrieveObjectException, SQLException {
        TransmissionPathType transmissionPathType = storableObject == null ? null : fromStorableObject(storableObject);
        if (transmissionPathType == null){            
            transmissionPathType = new TransmissionPathType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null);            
        }
        transmissionPathType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
                                    DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
                                    DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)));

        
        return transmissionPathType;
    }
    
    public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//        TransmissionPathType transmissionPathType = this.fromStorableObject(storableObject);
        switch (retrieveKind) {
            default:
                return null;
        }
    }
    
    public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
        TransmissionPathType transmissionPathType = this.fromStorableObject(storableObject);
        super.insertEntity(transmissionPathType);     
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
        try {
			characteristicDatabase.updateCharacteristics(transmissionPathType);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}

    }

    public void insert(List storableObjects) throws IllegalDataException,
            CreateObjectException {
        super.insertEntities(storableObjects);
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
        try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
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
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObject);
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
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
    }
    
    public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("TransmissionPathTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
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
            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
            Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_TRANSMISSIONPATHTYPE);
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                TransmissionPathType transmissionPathType = (TransmissionPathType) iter.next();
                List characteristics = (List)characteristicMap.get(transmissionPathType);
                transmissionPathType.setCharacteristics0(characteristics);
            }
        }
        return list;
    }
        
    public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
            IllegalDataException {
        return this.retrieveButIds(ids);
    }
}
