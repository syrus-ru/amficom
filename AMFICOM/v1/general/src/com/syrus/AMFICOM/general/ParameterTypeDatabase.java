/*
 * $Id: ParameterTypeDatabase.java,v 1.7 2005/02/07 14:54:31 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.7 $, $Date: 2005/02/07 14:54:31 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public class ParameterTypeDatabase extends StorableObjectDatabase  {
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 15;
	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return ObjectEntities.PARAMETERTYPE_ENTITY;
	}
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ ParameterTypeWrapper.COLUMN_DATA_TYPE;
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
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(parameterType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(parameterType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(parameterType.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA +
			+ parameterType.getDataType().value();
	}	

	private ParameterType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof ParameterType)
			return (ParameterType)storableObject;
		throw new IllegalDataException("ParameterTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		this.retrieveEntity(parameterType);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		ParameterType parameterType = (storableObject == null) ? 
					new ParameterType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, null, null, null, DataType._DATA_TYPE_RAW) : 
					this.fromStorableObject(storableObject);
		parameterType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)),
									DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
									resultSet.getInt(ParameterTypeWrapper.COLUMN_DATA_TYPE));
		return parameterType;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		ParameterType parameterType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}	
	
	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		this.insertEntity(parameterType);
	}	
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}	

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
//		ParameterType parameterType = this.fromStorableObject(storableObject);
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

	/**
	 * @deprecated use {@link StorableObjectDatabase.retrieveByCondion} and {@link TypicalCondition}
	 */
	public ParameterType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {		
		List list = null;
		
		try {
			list = this.retrieveByIds( null , StorableObjectWrapper.COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename, SIZE_CODENAME_COLUMN) + APOSTOPHE);
		}
		catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}
		
		if ((list == null) || (list.isEmpty()))
				throw new ObjectNotFoundException("No parameter type with codename: '" + codename + "'");
		
		return (ParameterType) list.get(0);
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		try{
			return this.retrieveByIds(null, null);
		}
		catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {			
			DatabaseString.setString(preparedStatement, ++i, parameterType.getCodename(), SIZE_CODENAME_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, parameterType.getDescription(), SIZE_DESCRIPTION_COLUMN);
			DatabaseString.setString(preparedStatement, ++i, parameterType.getName(), SIZE_NAME_COLUMN);
			preparedStatement.setInt(++i, parameterType.getDataType().value());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
}
