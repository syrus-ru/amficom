/*
 * $Id: CableLinkTypeDatabase.java,v 1.4 2004/12/28 13:16:46 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
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
 * @version $Revision: 1.4 $, $Date: 2004/12/28 13:16:46 $
 * @author $Author: bob $
 * @module config_v1
 */
public class CableLinkTypeDatabase extends StorableObjectDatabase {
	 // codename VARCHAR2(32) NOT NULL,
    public static final String COLUMN_CODENAME      = "codename";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    public static final String COLUMN_NAME = "name";
    // sort NUMBER(2,0),
    public static final String COLUMN_SORT  = "sort";
    // manufacturer VARCHAR2(64),
    private static final int SIZE_MANUFACTURER_COLUMN = 64; 
    public static final String COLUMN_MANUFACTURER  = "manufacturer";
    // manufacturer_code VARCHAR2(64),
    private static final int SIZE_MANUFACTURER_CODE_COLUMN = 64;
    public static final String COLUMN_MANUFACTURER_CODE     = "manufacturer_code";
    // image_id VARCHAR2(32),
    public static final String COLUMN_IMAGE_ID      = "image_id";
    
    private static final String LINK_COLUMN_CABLE_LINK_TYPE_ID = "cable_link_type_id";
    private static final String LINK_COLUMN_CABLE_THREAD_TYPE_ID = "cable_thread_type_id";
    private static final String CABLE_LINK_TYPE_LINK = "Cablethreadtypelink";
    
    private static String columns;
    private static String updateMultiplySQLValues;
    
    protected String getEnityName() {
        return ObjectEntities.LINKTYPE_ENTITY;
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
            + QUESTION;
        }
    return updateMultiplySQLValues;
    }
    
    protected String getColumns(int mode) {
        if (columns == null){
            columns = super.getColumns(mode) + COMMA
                + COLUMN_CODENAME + COMMA
                + COLUMN_DESCRIPTION + COMMA
                + COLUMN_NAME + COMMA
                + COLUMN_SORT + COMMA
                + COLUMN_MANUFACTURER + COMMA
                + COLUMN_MANUFACTURER_CODE + COMMA
                + COLUMN_IMAGE_ID;
        }
        return columns;
    }
    
    protected String getUpdateSingleSQLValues(StorableObject storableObject)
            throws IllegalDataException, UpdateObjectException {
        CableLinkType cableLinkType = fromStorableObject(storableObject);
        String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE 
            + APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
            + cableLinkType.getSort().value() + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturer(), SIZE_MANUFACTURER_COLUMN) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(cableLinkType.getManufacturerCode(), SIZE_MANUFACTURER_CODE_COLUMN) + APOSTOPHE + COMMA
            + DatabaseIdentifier.toSQLString(cableLinkType.getImageId());
        return sql;
    }
    
    private CableLinkType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
        if (storableObject instanceof CableLinkType)
            return (CableLinkType)storableObject;
        throw new IllegalDataException("CableLinkTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
    }
    
    private void retrieveCableThreadTypes(List storableObjects) throws RetrieveObjectException {
    	if (storableObjects == null || storableObjects.isEmpty())
            return;
        try {
            Map cableLinkTypeIdCableThreadTypeIds = super.retrieveLinkedEntities(storableObjects, CABLE_LINK_TYPE_LINK, LINK_COLUMN_CABLE_LINK_TYPE_ID, LINK_COLUMN_CABLE_THREAD_TYPE_ID);
            for (Iterator it = cableLinkTypeIdCableThreadTypeIds.keySet().iterator(); it.hasNext();) {
    			CableLinkType cableLinkType = (CableLinkType) it.next();
                List cableLinkTypeIds = (List)cableLinkTypeIdCableThreadTypeIds.get(cableLinkType);
    			cableLinkType.setCableThreadTypes0(ConfigurationStorableObjectPool.getStorableObjects(cableLinkTypeIds, true));               
    		}
        } catch (DatabaseException e) {
            throw new RetrieveObjectException("CableLikTypeDatabase.retreveCableThreadTypes | DatabaseException " + e);
        } catch (CommunicationException e) {
            throw new RetrieveObjectException("CableLikTypeDatabase.retreveCableThreadTypes | CommunicationException " + e);
        } catch (IllegalDataException e) {
            throw new RetrieveObjectException("CableLikTypeDatabase.retreveCableThreadTypes | IllegalDataException " + e);
        }
    }
    
    private void updateCableThreadTypes(List storableObjects) throws UpdateObjectException {
    	if (storableObjects == null || storableObjects.isEmpty())
            return;
        Map cableLinkTypeIdCableThreadTypeIds = new HashMap();
        for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			CableLinkType cableLinkType = (CableLinkType) it.next();
            List cableThreadTypes = cableLinkType.getCableThreadTypes();
            List cableThreadTypeIds = new ArrayList(cableThreadTypes.size());
            for (Iterator iter = cableThreadTypes.iterator(); iter.hasNext();) {
				CableThreadType cableThreadType = (CableThreadType) iter.next();
                cableThreadTypeIds.add(cableThreadType.getId());                				
			}
            cableLinkTypeIdCableThreadTypeIds.put(cableLinkType.getId(), cableThreadTypeIds);
		}
        super.updateLinkedEntities(cableLinkTypeIdCableThreadTypeIds, CABLE_LINK_TYPE_LINK, LINK_COLUMN_CABLE_LINK_TYPE_ID, LINK_COLUMN_CABLE_THREAD_TYPE_ID);
    }

    public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
        CableLinkType cableLinkType = this.fromStorableObject(storableObject);
        super.retrieveEntity(cableLinkType);
        this.retrieveCableThreadTypes(Collections.singletonList(cableLinkType));
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
        cableLinkType.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(cableLinkType.getId(), CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE));
    }
    
    protected int setEntityForPreparedStatement(StorableObject storableObject,
            PreparedStatement preparedStatement, int mode) throws IllegalDataException,
            UpdateObjectException {
        CableLinkType cableLinkType = fromStorableObject(storableObject);
        int i;
        try {
            i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
            preparedStatement.setString( ++i, cableLinkType.getCodename());
            preparedStatement.setString( ++i, cableLinkType.getDescription());
            preparedStatement.setString( ++i, cableLinkType.getName());
            preparedStatement.setInt( ++i, cableLinkType.getSort().value());
            preparedStatement.setString( ++i, cableLinkType.getManufacturer());
            preparedStatement.setString( ++i, cableLinkType.getManufacturerCode());
            DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableLinkType.getImageId());
        } catch (SQLException sqle) {
            throw new UpdateObjectException("LinkDatabase." +
                    "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
        }
        return i;
    }
    
    protected StorableObject updateEntityFromResultSet(
            StorableObject storableObject, ResultSet resultSet)
            throws IllegalDataException, RetrieveObjectException, SQLException {
        CableLinkType cableLinkType = storableObject == null ? null : fromStorableObject(storableObject);
        if (cableLinkType == null){
            cableLinkType = new CableLinkType(DatabaseIdentifier.getIdentifier(resultSet,COLUMN_ID), null, null, null, null, 0,
                                         null, null, null);         
        }
        cableLinkType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
                                    DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),                                    
                                    DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
                                    DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
                                    resultSet.getInt(COLUMN_SORT),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MANUFACTURER)),
                                    DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MANUFACTURER_CODE)),                                   
                                    DatabaseIdentifier.getIdentifier(resultSet, COLUMN_IMAGE_ID));

        
        return cableLinkType;
    }
    
    public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//      CableLinkType cableLinkType = this.fromStorableObject(storableObject);
        switch (retrieve_kind) {
            default:
                return null;
        }
    }

    public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
        CableLinkType cableLinkType = this.fromStorableObject(storableObject);
        super.insertEntity(cableLinkType);
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        try {
            characteristicDatabase.updateCharacteristics(storableObject);
            this.updateCableThreadTypes(Collections.singletonList(storableObject));
        } catch (UpdateObjectException e) {
            throw new CreateObjectException("LinkTypeDatabase.insert | UpdateObjectException " + e);
        }
    }

    public void insert(List storableObjects) throws IllegalDataException,
            CreateObjectException {
        super.insertEntities(storableObjects);
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        try {
            characteristicDatabase.updateCharacteristics(storableObjects);
            this.updateCableThreadTypes(storableObjects);
        } catch (UpdateObjectException e) {
            throw new CreateObjectException("LinkTypeDatabase.insert | UpdateObjectException " + e);
        }
    }
    
    public void update(StorableObject storableObject, int updateKind, Object obj)
            throws IllegalDataException, VersionCollisionException, UpdateObjectException {
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        switch (updateKind) {       
        case UPDATE_FORCE:
            super.checkAndUpdateEntity(storableObject, true);
            break;
        case UPDATE_CHECK:                  
        default:
            super.checkAndUpdateEntity(storableObject, false);            
        break;
        }
        characteristicDatabase.updateCharacteristics(storableObject);
        this.updateCableThreadTypes(Collections.singletonList(storableObject));
    }

    public void update(List storableObjects, int updateKind, Object arg)
            throws IllegalDataException, VersionCollisionException, UpdateObjectException {
        CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)ConfigurationDatabaseContext.getCharacteristicDatabase();
        switch (updateKind) {
        case UPDATE_FORCE:
            super.checkAndUpdateEntities(storableObjects, true);            
            break;
        case UPDATE_CHECK:                  
        default:
            super.checkAndUpdateEntities(storableObjects, false);
            break;
        }
        characteristicDatabase.updateCharacteristics(storableObjects);
        this.updateCableThreadTypes(storableObjects);
    }
    
        
    public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("CableLinkTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
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
            characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE);
            this.retrieveCableThreadTypes(list);
        }
        return list;
    }
    
    public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
            IllegalDataException {
        return this.retrieveButIds(ids);
    }
}
