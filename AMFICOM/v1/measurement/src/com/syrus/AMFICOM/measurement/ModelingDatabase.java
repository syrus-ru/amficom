/*
 * $Id: ModelingDatabase.java,v 1.11 2004/11/17 07:56:31 bob Exp $
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
import com.syrus.util.database.DatabaseString;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;

/**
 * @version $Revision: 1.11 $, $Date: 2004/11/17 07:56:31 $
 * @author $Author: bob $
 * @module module_name
 */

public class ModelingDatabase extends StorableObjectDatabase {

	// name VARCHAR(256),
    public static final String COLUMN_NAME  = "name";  
    // monitored_element_id VARCHAR2(32),
    public static final String COLUMN_ME_ID  = "monitored_element_id";
    
    public static final String COLUMN_SCHEME_PATH_ID  = "scheme_path_id";
    // measurement_type_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_MEASUREMENT_TYPE_ID   = "measurement_type_id";
    //  argument_set_id VARCHAR2(32) NOT NULL,
    public static final String COLUMN_ARGUMENT_SET_ID       = "argument_set_id";
    
    public static final String COLUMN_SORT = "sort";
    
    private static String columns;
    
    private static String updateMultiplySQLValues;

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
        return ObjectEntities.MODELING_ENTITY;
    }
    
    protected String getColumns() {
        if (columns == null){
            columns = super.getColumns() + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_ME_ID + COMMA
				+ COLUMN_SCHEME_PATH_ID + COMMA
				+ COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ COLUMN_ARGUMENT_SET_ID + COMMA
				+ COLUMN_SORT;
        }
        return columns;
    }   
    
    protected String getUpdateMultiplySQLValues() {
        if (updateMultiplySQLValues == null){
            updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION;
        }
        return updateMultiplySQLValues;
    }
    
    
    protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
            throws IllegalDataException, UpdateObjectException {
        Modeling modeling = fromStorableObject(storableObject);
        int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
        try {
        	Identifier monitoredElementId = modeling.getMonitoredElementId();
        	String schemePathId = modeling.getSchemePathId();
            preparedStatement.setString(++i, modeling.getName());            
            DatabaseIdentifier.setIdentifier(preparedStatement, ++i, monitoredElementId);     
            /**
             * @TODO during schemePathId use old identifier as {@link java.lang.String} use setString, fix after modifications
             */
            preparedStatement.setString(++i, (schemePathId != null) ? schemePathId : "");
            /**
             * TODO other fields!!!
             */
            DatabaseIdentifier.setIdentifier(preparedStatement, ++i, modeling.getMeasurementType().getId());
            DatabaseIdentifier.setIdentifier(preparedStatement, ++i, modeling.getArgumentSet().getId());
            preparedStatement.setInt(++i, modeling.getSort().value());
        } catch (SQLException sqle) {
            throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
        }
        return i;
    }
    
    protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
            UpdateObjectException {
        Modeling modeling = fromStorableObject(storableObject);
        Identifier monitoredElementId = modeling.getMonitoredElementId();
    	String schemePathId = modeling.getSchemePathId();
    	
        String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
            + APOSTOPHE + DatabaseString.toQuerySubString(modeling.getName()) + APOSTOPHE + COMMA
            + DatabaseIdentifier.toSQLString(monitoredElementId) + COMMA
			+ APOSTOPHE + ((schemePathId != null) ? schemePathId : "") + APOSTOPHE + COMMA
            + DatabaseIdentifier.toSQLString(modeling.getMeasurementType().getId()) + COMMA
            + DatabaseIdentifier.toSQLString(modeling.getArgumentSet().getId()) + COMMA
			+ modeling.getSort().value();
        return values;
    }

    protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
    throws IllegalDataException, RetrieveObjectException, SQLException {
        Modeling modeling = (storableObject == null) ? 
                new Modeling(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null, null, 0) : 
                    fromStorableObject(storableObject);
        MeasurementType measurementType;
        Set argumentSet;
        try {            
            measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_TYPE_ID), true);
            argumentSet = (Set)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ARGUMENT_SET_ID), true);
        }
        catch (ApplicationException ae) {
            throw new RetrieveObjectException(ae);
        }
        
        Identifier meId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ME_ID);
        modeling.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
							   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
							   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
                               meId,
                               /**
                                * @TODO during schemePathId use old identifier as {@link java.lang.String} use getString, fix after modifications
                                */
							   resultSet.getString(COLUMN_SCHEME_PATH_ID),
							   measurementType,
							   argumentSet,
							   resultSet.getInt(COLUMN_SORT));      
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
        Modeling modeling = this.fromStorableObject(storableObject);
        this.insertEntity(modeling);
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
                + DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
            + CLOSE_BRACKET;
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("ModelingDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }

    
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		} else {
			Log.errorMessage("ModelingDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}

