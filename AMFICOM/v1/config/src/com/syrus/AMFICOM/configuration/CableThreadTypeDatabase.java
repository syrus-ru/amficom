/*
 * $Id: CableThreadTypeDatabase.java,v 1.3 2004/12/07 15:32:33 max Exp $
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
 * @version $Revision: 1.3 $, $Date: 2004/12/07 15:32:33 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class CableThreadTypeDatabase extends StorableObjectDatabase {
    // codename VARCHAR2(32) NOT NULL,
    public static final String COLUMN_CODENAME      = "codename";
    // description VARCHAR2(256),
    public static final String COLUMN_DESCRIPTION   = "description";
    // mark VARCHAR2(64),
    public static final String COLUMN_MARK  = "mark";
    // color VARCHAR2(32),
    public static final String COLUMN_COLOR = "color";
    // cable_link_type_id VARCHAR2(32),
    public static final String COLUMN_LINK_TYPE_ID    = "link_type_id";
	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return ObjectEntities.CABLETHREADTYPE_ENTITY;
	}
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
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
				+ COLUMN_MARK + COMMA
				+ COLUMN_COLOR
				+ COLUMN_LINK_TYPE_ID;
		}
		return columns;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		CableThreadType cableThreadType = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getCodename()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getDescription()) + APOSTOPHE 
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getMark()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getColor()) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(cableThreadType.getLinkTypeId());
		return sql;
	}
	
	private CableThreadType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof CableThreadType)
			return (CableThreadType)storableObject;
		throw new IllegalDataException("CableThreadTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		super.retrieveEntity(cableThreadType);
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement, int mode) throws IllegalDataException,
			UpdateObjectException {
		CableThreadType cableThreadType = fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			preparedStatement.setString( ++i, cableThreadType.getCodename());
			preparedStatement.setString( ++i, cableThreadType.getDescription());
			preparedStatement.setString( ++i, cableThreadType.getMark());
			preparedStatement.setString( ++i, cableThreadType.getColor());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableThreadType.getLinkTypeId());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("CableThreadTypeDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CableThreadType cableThreadType = storableObject == null ? null : fromStorableObject(storableObject);
		if (cableThreadType == null){
			cableThreadType = new CableThreadType(DatabaseIdentifier.getIdentifier(resultSet,COLUMN_ID), null, null, null, 
												  null, null, null);			
		}
		cableThreadType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),									
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MARK)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_COLOR)),
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_LINK_TYPE_ID));

		
		return cableThreadType;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		super.insertEntity(cableThreadType);		
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
            Log.debugMessage("CableThreadTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
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
