/*
 * $Id: AnalysisDatabase.java,v 1.22 2004/09/09 09:21:47 bob Exp $
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
 * @version $Revision: 1.22 $, $Date: 2004/09/09 09:21:47 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class AnalysisDatabase extends StorableObjectDatabase {

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_CRITERIA_SET_ID = "criteria_set_id";
	
	private String updateColumns;
	
	private String updateMultiplySQLValues;

	private Analysis fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
		throw new IllegalDataException("AnalysisDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveEntity(analysis);
	}	
	
	protected String getEnityName() {		
		return "Analysis";
	}	
	
	protected String getTableName() {
		return ObjectEntities.ANALYSIS_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_CRITERIA_SET_ID;
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
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		Analysis analysis = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(++i, analysis.getType().getId().getCode()); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(++i, analysis.getMonitoredElementId().getCode()); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(++i, analysis.getCriteriaSet().getId().getCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Analysis analysis = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ analysis.getType().getId().toSQLString() + COMMA
			+ analysis.getMonitoredElementId().toSQLString() + COMMA
			+ analysis.getCriteriaSet().getId().toSQLString();
		return values;
	}

	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
		+ COLUMN_TYPE_ID + COMMA
		+ COLUMN_MONITORED_ELEMENT_ID + COMMA
		+ COLUMN_CRITERIA_SET_ID
		+ SQL_FROM + ObjectEntities.ANALYSIS_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		Analysis analysis = (storableObject == null) ? 
				new Analysis(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null) : 
					fromStorableObject(storableObject);
		AnalysisType analysisType;
		Set criteriaSet;
		try {
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			analysisType = (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			criteriaSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_CRITERIA_SET_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
													 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
													/**
													 * @todo when change DB Identifier model ,change getString() to getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
													/**
													 * @todo when change DB Identifier model ,change getString() to getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
													 analysisType,
													/**
													 * @todo when change DB Identifier model ,change getString() to getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_MONITORED_ELEMENT_ID)),
													 criteriaSet);		
		return analysis;
	}

	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Analysis analysis = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(analysis);
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
		insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
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
			return retriveByIdsOneQuery(null, conditions);
		return retriveByIdsOneQuery(ids, conditions);	
		//return retriveByIdsPreparedStatement(ids, conditions);
	}

}
