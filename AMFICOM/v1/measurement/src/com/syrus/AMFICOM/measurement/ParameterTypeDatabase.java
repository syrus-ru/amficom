/*
 * $Id: ParameterTypeDatabase.java,v 1.23 2004/09/09 06:46:36 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.23 $, $Date: 2004/09/09 06:46:36 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class ParameterTypeDatabase extends StorableObjectDatabase  {
	
	public static final String COLUMN_CODENAME = "codename";
	public static final String COLUMN_DESCRIPTION = "description";	
	public static final String COLUMN_NAME = "name";	
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 15;
	
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	
	protected String getEnityName() {		
		return "ParameterType";
	}
	
	protected String getTableName() {
		return ObjectEntities.PARAMETERTYPE_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_CODENAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_NAME;
		}
		return this.updateColumns;
	}	
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		ParameterType parameterType = fromStorableObject(storableObject);
		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + parameterType.getCodename() + APOSTOPHE + COMMA
			+ APOSTOPHE + parameterType.getDescription() + APOSTOPHE + COMMA
			+ APOSTOPHE + parameterType.getName() + APOSTOPHE;
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

	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_NAME
			+ SQL_FROM + ObjectEntities.PARAMETERTYPE_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		ParameterType parameterType = (storableObject == null) ? 
					new ParameterType(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null) : 
					fromStorableObject(storableObject);
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
		parameterType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
												DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
												/**
												 * @todo when change DB Identifier model ,change getString() to
												 *       getLong()
												 */
												 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
												 /**
												  * @todo when change DB Identifier model ,change getString() to
												  *       getLong()
												  */
												 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
												 resultSet.getString(COLUMN_CODENAME),
												 resultSet.getString(COLUMN_DESCRIPTION),
												 resultSet.getString(COLUMN_NAME));
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
		try {
			this.insertEntity(parameterType);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
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
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.PARAMETERTYPE_ENTITY
			+ SQL_WHERE + COLUMN_CODENAME + EQUALS + APOSTOPHE + codename + APOSTOPHE;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("ParameterTypeDatabase.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				return new ParameterType(new Identifier(resultSet.getString(COLUMN_ID)));
			}
			throw new ObjectNotFoundException("No such parameter type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "ParameterTypeDatabase.retrieveForCodename | Cannot retrieve parameter type with codename: '" + codename + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
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
			return retriveByIdsOneQuery(null, condition);
		return retriveByIdsOneQuery(ids, condition);
	}
	
	
	protected void setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		ParameterType parameterType = fromStorableObject(storableObject);
		super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {			
			preparedStatement.setString(6, parameterType.getCodename());
			preparedStatement.setString(7, parameterType.getDescription());
			preparedStatement.setString(8, parameterType.getName());
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(9, parameterType.getId().getCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}

	}
}
