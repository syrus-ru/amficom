/*
 * $Id: EvaluationDatabase.java,v 1.16 2004/09/06 14:33:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
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
 * @version $Revision: 1.16 $, $Date: 2004/09/06 14:33:11 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class EvaluationDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_TYPE_ID				= "type_id";
	public static final String	COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String	COLUMN_THRESHOLD_SET_ID		= "threshold_set_id";

	private String updateColumns;	
	private String updateMultiplySQLValues;
	
	private Evaluation fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Evaluation)
			return (Evaluation) storableObject;
		throw new IllegalDataException("EvaluationDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.retrieveEntity(evaluation);
	}
	

	protected String getEnityName() {		
		return "Evaluation";
	}
	
	
	protected String getTableName() {		
		return ObjectEntities.EVALUATION_ENTITY;
	}
	
	protected String retrieveQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_TYPE_ID + COMMA
		+ COLUMN_MONITORED_ELEMENT_ID + COMMA
		+ COLUMN_THRESHOLD_SET_ID
		+ SQL_FROM + ObjectEntities.EVALUATION_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_THRESHOLD_SET_ID;
		}
		return this.updateColumns;
	}
	
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Evaluation evaluation = fromStorableObject(storableObject);
		String values = DatabaseDate.toUpdateSubString(evaluation.getCreated()) + COMMA
		+ DatabaseDate.toUpdateSubString(evaluation.getModified()) + COMMA
		+ evaluation.getCreatorId().toSQLString() + COMMA
		+ evaluation.getModifierId().toSQLString() + COMMA
		+ evaluation.getType().getId().toSQLString() + COMMA
		+ evaluation.getMonitoredElementId().toSQLString() + COMMA
		+ evaluation.getThresholdSet().getId().toSQLString();
		return values;
	}
	
	
	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#setEntityForPreparedStatement(com.syrus.AMFICOM.general.StorableObject, java.sql.PreparedStatement)
	 */
	protected void setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		Evaluation evaluation = fromStorableObject(storableObject);
		try {
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(1, evaluation.getId().getCode());
			preparedStatement.setTimestamp(2, new Timestamp(evaluation.getCreated().getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(evaluation.getModified().getTime()));
			preparedStatement.setString(4, evaluation.getCreatorId().getCode());
			preparedStatement.setString(5, evaluation.getModifierId().getCode());
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(6, evaluation.getType().getId().getCode()); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(7, evaluation.getMonitoredElementId().getCode()); 
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(8, evaluation.getThresholdSet().getId().getCode());
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(9, evaluation.getId().getCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Evaluation evaluation = fromStorableObject(storableObject);
		if (evaluation == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			evaluation = new Evaluation(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null);			
		}
		EvaluationType evaluationType;
		Set thresholdSet;
		try {
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			evaluationType = (EvaluationType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
			/**
			 * @todo when change DB Identifier model ,change getString() to
			 *       getLong()
			 */
			thresholdSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_THRESHOLD_SET_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		/**
		 * @todo when change DB Identifier model ,change getString() to
		 *       getLong()
		 */
		evaluation.setAttributes(DatabaseDate.fromQuerySubString(resultSet,COLUMN_CREATED),
														 DatabaseDate.fromQuerySubString(resultSet,COLUMN_MODIFIED),
														 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
														 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
														 evaluationType,
														 new Identifier(resultSet.getString(COLUMN_MONITORED_ELEMENT_ID)),
														 thresholdSet);


		return evaluation;
	}



	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(evaluation);
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
		Evaluation evaluation = this.fromStorableObject(storableObject);
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
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null, condition);
		return retriveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids, condition);
	}	
}
