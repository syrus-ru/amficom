/*
 * $Id: ModelingDatabase.java,v 1.4 2004/10/13 09:49:50 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.4 $, $Date: 2004/10/13 09:49:50 $
 * @author $Author: bob $
 * @module module_name
 */

public class ModelingDatabase extends StorableObjectDatabase {

	// name VARCHAR(256),
    public static final String COLUMN_NAME  = "name";  
    // monitored_element_id VARCHAR2(32),
    public static final String COLUMN_SCHEME_PATH_ID  = "scheme_path_id";
    // measurement_type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_MEASUREMENT_TYPE_ID   = "measurement_type_id";
    //  argument_set_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_ARGUMENT_SET_ID       = "argument_set_id";
    
    private String updateColumns;
    
    private String updateMultiplySQLValues;

    private Modeling fromStorableObject(StorableObject storableObject) throws IllegalDataException {
        if (storableObject instanceof Modeling)
            return (Modeling) storableObject;
        throw new IllegalDataException("ModelingDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
    }

    public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
        Modeling modeling = this.fromStorableObject(storableObject);
        this.retrieveEntity(modeling);
    }   
    
    protected String getEnityName() {       
        return "Modeling";
    }   
    
    protected String getTableName() {
        return ObjectEntities.MODELING_ENTITY;
    }
    
    protected String getUpdateColumns() {
        if (this.updateColumns == null){
            this.updateColumns = super.getUpdateColumns() + COMMA
            + COLUMN_NAME + COMMA
            + COLUMN_SCHEME_PATH_ID + COMMA
            + COLUMN_MEASUREMENT_TYPE_ID + COMMA
            + COLUMN_ARGUMENT_SET_ID;
        }
        return this.updateColumns;
    }   
    
    protected String getUpdateMultiplySQLValues() {
        if (this.updateMultiplySQLValues == null){
            this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION;
        }
        return this.updateMultiplySQLValues;
    }
    
    
    protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
            throws IllegalDataException, UpdateObjectException {
        Modeling Modeling = fromStorableObject(storableObject);
        int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
        try {
            /**
             * @todo when change DB Identifier model ,change setString() to setLong()
             */
            preparedStatement.setString(++i, Modeling.getName()); 
            /**
             * @todo when change DB Identifier model ,change setString() to setLong()
             */
            preparedStatement.setString(++i, Modeling.getMonitoredElementId().getCode()); 
            /**
             * @todo when change DB Identifier model ,change setString() to setLong()
             */
            preparedStatement.setString(++i, Modeling.getMeasurementType().getId().getCode());
            preparedStatement.setString(++i, Modeling.getArgumentSet().getId().getCode());
        } catch (SQLException sqle) {
            throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
        }
        return i;
    }
    
    protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
            UpdateObjectException {
        Modeling Modeling = fromStorableObject(storableObject);
        String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
            + APOSTOPHE + Modeling.getName() + APOSTOPHE + COMMA
            + Modeling.getMonitoredElementId().toSQLString() + COMMA
            + Modeling.getMeasurementType().getId().toSQLString() + COMMA
            + Modeling.getArgumentSet().getId().toSQLString();
        return values;
    }

    protected String retrieveQuery(String condition){
        return super.retrieveQuery(condition) + COMMA
        + COLUMN_NAME + COMMA
        + COLUMN_SCHEME_PATH_ID + COMMA
        + COLUMN_MEASUREMENT_TYPE_ID + COMMA
        + COLUMN_ARGUMENT_SET_ID
        + SQL_FROM + ObjectEntities.MODELING_ENTITY
        + ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

    }   
    
    protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
    throws IllegalDataException, RetrieveObjectException, SQLException {
        Modeling modeling = (storableObject == null) ? 
                new Modeling(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null) : 
                    fromStorableObject(storableObject);
        MeasurementType measurementType;
        Set argumentSet;
        try {
            /**
             * @todo when change DB Identifier model ,change getString() to getLong()
             */
            measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_MEASUREMENT_TYPE_ID)), true);
            /**
             * @todo when change DB Identifier model ,change getString() to getLong()
             */
            argumentSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ARGUMENT_SET_ID)), true);
        }
        catch (ApplicationException ae) {
            throw new RetrieveObjectException(ae);
        }
        modeling.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
                                                     DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
                                                    /**
                                                     * @todo when change DB Identifier model ,change getString() to getLong()
                                                     */
                                                     new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
                                                    /**
                                                     * @todo when change DB Identifier model ,change getString() to getLong()
                                                     */
                                                     new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
                                                     resultSet.getString(COLUMN_NAME),
                                                     /**
                                                      * @todo when change DB Identifier model ,change getString() to getLong()
                                                      */
                                                     resultSet.getString(COLUMN_SCHEME_PATH_ID),												 
                                                     
                                                     measurementType,													 
                                                     argumentSet);      
        return modeling;
    }

    
    public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
        Modeling modeling = this.fromStorableObject(storableObject);
        switch (retrieveKind) {
            default:
                return null;
        }
    }

    public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
        Modeling Modeling = this.fromStorableObject(storableObject);
        this.insertEntity(Modeling);
    }
    
    
    public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
        insertEntities(storableObjects);
    }

    public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
        Modeling modeling = this.fromStorableObject(storableObject);
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
    

    public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
        if ((ids == null) || (ids.isEmpty()))
            return retrieveByIdsOneQuery(null, conditions);
        return retrieveByIdsOneQuery(ids, conditions);  
        //return retriveByIdsPreparedStatement(ids, conditions);
    }
    
    public List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = COLUMN_SCHEME_PATH_ID + SQL_IN + OPEN_BRACKET
                + SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
                + DomainMember.COLUMN_DOMAIN_ID + EQUALS + domain.getId().toSQLString()
            + CLOSE_BRACKET;
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("ModelingDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }

}

