/*
 * $Id: ParameterTypeDatabase.java,v 1.35 2004/11/16 15:48:45 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
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
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.35 $, $Date: 2004/11/16 15:48:45 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class ParameterTypeDatabase extends StorableObjectDatabase  {
	
	public static final String COLUMN_CODENAME = "codename";
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_NAME = "name";	
	public static final String COLUMN_SORT = "sort";
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 15;
	
	private static String columns;
	private static String updateMultiplySQLValues;
	
	protected String getEnityName() {
		return ObjectEntities.PARAMETERTYPE_ENTITY;
	}
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_NAME + COMMA
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
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		ParameterType parameterType = fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(parameterType.getCodename()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(parameterType.getDescription()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(parameterType.getName()) + APOSTOPHE + COMMA +
			+ parameterType.getSort().value();
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
					new ParameterType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null, DataType._DATA_TYPE_DATA) : 
					fromStorableObject(storableObject);
		parameterType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
									DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
									resultSet.getInt(COLUMN_SORT));
		return parameterType;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
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
		super.insertEntities(storableObjects);

	}	

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		ParameterType parameterType = this.fromStorableObject(storableObject);
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

	public ParameterType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {		
		List list = null;
		
		try {
			list = retrieveByIds( null , COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename) + APOSTOPHE);
		}  catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}
		
		if ((list == null) || (list.isEmpty()))
				throw new ObjectNotFoundException("No parameter type with codename: '" + codename + "'");
		
		return (ParameterType) list.get(0);
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		try{
			return retrieveByIds(null, null);
		}catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}

	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, condition);
		return retrieveByIdsOneQuery(ids, condition);
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		ParameterType parameterType = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {			
			preparedStatement.setString(++i, parameterType.getCodename());
			preparedStatement.setString(++i, parameterType.getDescription());
			preparedStatement.setString(++i, parameterType.getName());
			preparedStatement.setInt(++i, parameterType.getSort().value());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}	

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof StringFieldCondition){
			StringFieldCondition stringFieldCondition = (StringFieldCondition)condition;
			try{
				ParameterType type = this.retrieveForCodename(stringFieldCondition.getString());
				list = Collections.singletonList(type);
			}catch(ObjectNotFoundException e){
				String msg = getEnityName() + "Database.retrieveByCondition | Cannot found object with codename '" + stringFieldCondition.getString() + "'";
				throw new RetrieveObjectException(msg, e);
			}
		} else {
			Log.errorMessage(getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
