/*
 * $Id: CableThreadTypeDatabase.java,v 1.17 2005/02/11 18:40:02 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.17 $, $Date: 2005/02/11 18:40:02 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public class CableThreadTypeDatabase extends StorableObjectDatabase {
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
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ CableThreadTypeWrapper.COLUMN_COLOR
				+ CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID;
		}
		return columns;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE
            + APOSTOPHE + DatabaseString.toQuerySubString(cableThreadType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE
			+ APOSTOPHE + cableThreadType.getColor() + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(cableThreadType.getLinkType().getId());
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
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		int i;
		try {
			i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			preparedStatement.setString( ++i, cableThreadType.getCodename());
			preparedStatement.setString( ++i, cableThreadType.getDescription());
            preparedStatement.setString( ++i, cableThreadType.getName());
			preparedStatement.setInt( ++i, cableThreadType.getColor());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, cableThreadType.getLinkType().getId());
		} catch (SQLException sqle) {
			throw new UpdateObjectException("CableThreadTypeDatabase." +
					"setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		CableThreadType cableThreadType = storableObject == null ? null : this.fromStorableObject(storableObject);
		if (cableThreadType == null){
			cableThreadType = new CableThreadType(DatabaseIdentifier.getIdentifier(resultSet,
				StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, null, 0, null);			
		}
        LinkType linkType;
        try {
            linkType = (LinkType)ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, CableThreadTypeWrapper.COLUMN_LINK_TYPE_ID), true);
        }
        catch (ApplicationException ae) {
            throw new RetrieveObjectException(ae);
        }
        
		cableThreadType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),									
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
                                    DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
									resultSet.getInt(CableThreadTypeWrapper.COLUMN_COLOR),
									linkType);

		
		return cableThreadType;
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		//CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		CableThreadType cableThreadType = this.fromStorableObject(storableObject);
		super.insertEntity(cableThreadType);		
	}

	public void insert(Collection storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
	}
	
	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, modifierId, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, modifierId, false);
		break;
		}
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, modifierId, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, modifierId, false);
			break;
		}
	}
	
		
	public Collection retrieveAll() throws RetrieveObjectException {
		Collection objects = null;
		try {
			objects = this.retrieveByIds(null, null);
		}
		catch (IllegalDataException ide) {
			Log.debugMessage("CableThreadTypeDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
			throw new RetrieveObjectException(ide);
		}
		return objects;
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);
		//return retriveByIdsPreparedStatement(ids);
	}	

}
