/*
 * $Id: TransmissionPathTypeDatabase.java,v 1.5 2004/11/16 12:33:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.5 $, $Date: 2004/11/16 12:33:17 $
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
    }
    
    private TransmissionPathType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
        if (storableObject instanceof TransmissionPathType)
            return (TransmissionPathType)storableObject;
        throw new IllegalDataException("TransmissionPathTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
    } 
    
    protected String getEnityName() {
        return ObjectEntities.TRANSPATHTYPE_ENTITY;
    }
    
    protected String getColumns() {
        if (columns == null){
            columns  = super.getColumns() + COMMA
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
    
    protected String getUpdateSingleSQLValues(StorableObject storableObject)
            throws IllegalDataException, UpdateObjectException {
        TransmissionPathType transmissionPathType = fromStorableObject(storableObject);
        String name = DatabaseString.toQuerySubString(transmissionPathType.getName());
        String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(transmissionPathType.getCodename()) + APOSTOPHE + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(transmissionPathType.getDescription()) + APOSTOPHE + COMMA
            + APOSTOPHE + (name != null ? name : "") + APOSTOPHE;
        return sql;
    }
    
    protected int setEntityForPreparedStatement(StorableObject storableObject,
            PreparedStatement preparedStatement) throws IllegalDataException,
            UpdateObjectException {
        TransmissionPathType transmissionPathType = fromStorableObject(storableObject);
        int i;
        try {
            i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
            preparedStatement.setString( ++i, transmissionPathType.getCodename());
            preparedStatement.setString( ++i, transmissionPathType.getDescription());
            preparedStatement.setString( ++i, transmissionPathType.getName());
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
    
    public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
        TransmissionPathType transmissionPathType = this.fromStorableObject(storableObject);
        switch (retrieve_kind) {
            default:
                return null;
        }
    }
    
    public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
        TransmissionPathType transmissionPathType = this.fromStorableObject(storableObject);
        super.insertEntity(transmissionPathType);      
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
            Log.debugMessage("TransmissionPathTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
    
    public void delete(TransmissionPathType transmissionPathType) {
        String transmissionPathIdStr = DatabaseIdentifier.toSQLString(transmissionPathType.getId());
        Statement statement = null;
        Connection connection = DatabaseConnection.getConnection();
        try {
            statement = connection.createStatement();
            String sql = SQL_DELETE_FROM
                        + ObjectEntities.TRANSPATHTYPE_ENTITY
                        + SQL_WHERE
                        + COLUMN_ID + EQUALS
                        + transmissionPathIdStr;
            Log.debugMessage("transmissionPathTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
            } finally {
                DatabaseConnection.closeConnection(connection);
            }
        }
    }
    
    public List retrieveByIds(List ids, String condition) 
            throws IllegalDataException, RetrieveObjectException {
        if ((ids == null) || (ids.isEmpty()))
            return super.retrieveByIdsOneQuery(null, condition);
        return super.retrieveByIdsOneQuery(ids, condition); 
        //return retriveByIdsPreparedStatement(ids);
        }
        
    public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
            IllegalDataException {
        return this.retrieveButIds(ids);
    }
}
