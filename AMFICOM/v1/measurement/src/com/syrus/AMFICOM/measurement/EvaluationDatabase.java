/*
 * $Id: EvaluationDatabase.java,v 1.51 2005/05/20 21:11:38 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.51 $, $Date: 2005/05/20 21:11:38 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class EvaluationDatabase extends StorableObjectDatabase {

	private static String columns;	
	private static String updateMultipleSQLValues;

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
		return ObjectEntities.EVALUATION_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EvaluationWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ EvaluationWrapper.COLUMN_MEASUREMENT_ID + COMMA
				+ EvaluationWrapper.COLUMN_THRESHOLD_SET_ID;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		Measurement measurement = evaluation.getMeasurement();
		String values = DatabaseIdentifier.toSQLString(evaluation.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(evaluation.getMonitoredElementId()) + COMMA
			+ ((measurement != null) ? (DatabaseIdentifier.toSQLString(measurement.getId())) : "") + COMMA
			+ DatabaseIdentifier.toSQLString(evaluation.getThresholdSet().getId());
		return values;
	}	

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		Measurement measurement = evaluation.getMeasurement();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, evaluation.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, evaluation.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (measurement != null) ? measurement.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, evaluation.getThresholdSet().getId());
		return startParameterNumber;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Evaluation evaluation = (storableObject == null) ?
				new Evaluation(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								0L,
								null,
								null,
								null,
								null) :
					this.fromStorableObject(storableObject);
		EvaluationType evaluationType;
		Measurement measurement = null;
		Set thresholdSet;
		try {
			evaluationType = (EvaluationType)StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_MEASUREMENT_ID);
			if (measurementId != null)
				measurement = (Measurement) StorableObjectPool.getStorableObject(measurementId, true);
			thresholdSet = (Set)StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_THRESHOLD_SET_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		evaluation.setAttributes(DatabaseDate.fromQuerySubString(resultSet,StorableObjectWrapper.COLUMN_CREATED),
								 DatabaseDate.fromQuerySubString(resultSet,StorableObjectWrapper.COLUMN_MODIFIED),
								 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
								 DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
								 resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
								 evaluationType,
								 DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_MONITORED_ELEMENT_ID),
								 measurement,
								 thresholdSet);
		return evaluation;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  evaluation.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.insertEntity(evaluation);
	}
	
	
	public void insert(java.util.Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

}
