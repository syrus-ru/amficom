/*
 * $Id: CableThreadDatabase.java,v 1.6 2005/01/26 15:09:21 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
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
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.6 $, $Date: 2005/01/26 15:09:21 $
 * @author $Author: bob $
 * @module config_v1
 */
public class CableThreadDatabase extends StorableObjectDatabase  {
    
	private static String columns;
    private static String updateMultiplySQLValues;
    
    private CableThread fromStorableObject(StorableObject storableObject) throws IllegalDataException {
        if (storableObject instanceof CableThread)
            return (CableThread)storableObject;
        throw new IllegalDataException("LinkDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
    }
    
    protected String getEnityName() {
        return ObjectEntities.CABLE_THREAD_ENTITY;
    }
    
    protected String getColumns(int mode) {
        if (columns == null){
            columns = super.getColumns(mode) + COMMA
                + DomainMember.COLUMN_DOMAIN_ID + COMMA
                + CableThreadWrapper.COLUMN_TYPE_ID + COMMA
                + CableThreadWrapper.COLUMN_NAME + COMMA
                + CableThreadWrapper.COLUMN_DESCRIPTION;
        }
        return columns;
    }
    
    protected String getUpdateMultiplySQLValues(int mode) {
        if (updateMultiplySQLValues == null){
            updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA 
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION + COMMA
                + QUESTION;
        }
        return updateMultiplySQLValues;
    }
    
    protected String getUpdateSingleSQLValues(StorableObject storableObject)
    throws IllegalDataException, UpdateObjectException {
        CableThread cableThread = this.fromStorableObject(storableObject);
		
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
				+ DatabaseIdentifier.toSQLString(cableThread.getDomainId()) + COMMA
				+ DatabaseIdentifier.toSQLString(cableThread.getType().getId())
				+ APOSTOPHE	+ DatabaseString.toQuerySubString(cableThread.getName(), SIZE_NAME_COLUMN) + APOSTOPHE+ COMMA 
                + APOSTOPHE	+ DatabaseString.toQuerySubString(cableThread.getDescription(), SIZE_DESCRIPTION_COLUMN)	+ APOSTOPHE;
		return sql;
    }
    
    protected int setEntityForPreparedStatement(StorableObject storableObject,
            PreparedStatement preparedStatement, int mode) throws IllegalDataException,
            UpdateObjectException {
        CableThread cableThread = this.fromStorableObject(storableObject);
        int i;
        try {
            i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
            DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableThread.getDomainId());
            DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableThread.getType().getId());
            preparedStatement.setString( ++i, cableThread.getName());
            preparedStatement.setString( ++i, cableThread.getDescription());            
        } catch (SQLException sqle) {
            throw new UpdateObjectException("LinkDatabase." +
                    "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
        }
        return i;
    }
    
    protected StorableObject updateEntityFromResultSet(
            StorableObject storableObject, ResultSet resultSet)
            throws IllegalDataException, RetrieveObjectException, SQLException {
        CableThread cableThread = this.fromStorableObject(storableObject);
        if (cableThread == null){          
            cableThread = new CableThread(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null,
                                       null, null);            
        }
        String name = DatabaseString.fromQuerySubString(resultSet.getString(CableThreadWrapper.COLUMN_NAME));
        String description = DatabaseString.fromQuerySubString(resultSet.getString(CableThreadWrapper.COLUMN_DESCRIPTION));        
        CableThreadType cableThreadType;
        try {
            cableThreadType = (CableThreadType)ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, CableThreadWrapper.COLUMN_TYPE_ID), true);
        }
        catch (ApplicationException ae) {
            throw new RetrieveObjectException(ae);
        }
        cableThread.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
                                                        DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
                                                        DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
                                                        DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
                                                        DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
                                                        (name != null) ? name : "",
                                                        (description != null) ? description : "",
                                                        cableThreadType);

        
        return cableThread;
    }
    
    public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
        CableThread cableThread = this.fromStorableObject(storableObject);
        super.retrieveEntity(cableThread);        
    }
    
    public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//        CableThread cableThread = this.fromStorableObject(storableObject);
        switch (retrieveKind) {
            default:
                return null;
        }
    }
    
    public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
        CableThread cableThread = this.fromStorableObject(storableObject);
        super.insertEntity(cableThread);               
    }
    
    public void insert(List storableObjects) throws IllegalDataException,
            CreateObjectException {
		super.insertEntities(storableObjects);		
	}
    
    public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
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
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
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
            list = this.retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("CableThreadDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
    
    public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, condition);
		else
			list = this.retrieveByIdsOneQuery(ids, condition);
		
		return list;
	}
    
    public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		List list;
		{
			Log
					.errorMessage("CableThreadDatabase.retrieveByCondition | Unknown condition class: "
							+ condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
