/*
 * $Id: ParameterTypeDatabase.java,v 1.17 2005/03/04 19:50:01 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.17 $, $Date: 2005/03/04 19:50:01 $
 * @author $Author: bass $
 * @module general_v1
 */

public class ParameterTypeDatabase extends StorableObjectDatabase  {

	private static String columns;
	private static String updateMultipleSQLValues;

	protected String getEnityName() {
		return ObjectEntities.PARAMETERTYPE_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null){
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ ParameterTypeWrapper.COLUMN_DATA_TYPE;
		}
		return super.getColumns(mode) + columns;
	}	
	
	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null){
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
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
		ParameterType parameterType = (storableObject == null)
				? new ParameterType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						0,
						null,
						null,
						null,
						DataType._DATA_TYPE_RAW)
				: this.fromStorableObject(storableObject);
		parameterType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
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


	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}	
	
	public Collection retrieveAll() throws RetrieveObjectException {
		try{
			return this.retrieveByIds(null, null);
		}
		catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseString.setString(preparedStatement, ++i, parameterType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, parameterType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, parameterType.getName(), SIZE_NAME_COLUMN);
		preparedStatement.setInt(++i, parameterType.getDataType().value());
		return i;
	}
	
}
