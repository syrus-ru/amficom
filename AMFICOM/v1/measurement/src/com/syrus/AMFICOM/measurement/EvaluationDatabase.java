/*
 * $Id: EvaluationDatabase.java,v 1.45 2005/03/04 19:50:01 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

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
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.45 $, $Date: 2005/03/04 19:50:01 $
 * @author $Author: bass $
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

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EvaluationWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ EvaluationWrapper.COLUMN_MEASUREMENT_ID + COMMA
				+ EvaluationWrapper.COLUMN_THRESHOLD_SET_ID;
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultipleSQLValues() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = super.getUpdateMultipleSQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		Measurement measurement = evaluation.getMeasurement();
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(evaluation.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(evaluation.getMonitoredElementId()) + COMMA
			+ ((measurement != null) ? (DatabaseIdentifier.toSQLString(measurement.getId())) : "") + COMMA
			+ DatabaseIdentifier.toSQLString(evaluation.getThresholdSet().getId());
		return values;
	}	

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		Measurement measurement = evaluation.getMeasurement();
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, evaluation.getType().getId()); 
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, evaluation.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (measurement != null) ? measurement.getId() : null);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, evaluation.getThresholdSet().getId());
		return i;
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
			evaluationType = (EvaluationType)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_MEASUREMENT_ID);
			if (measurementId != null)
				measurement = (Measurement) MeasurementStorableObjectPool.getStorableObject(measurementId, true);
			thresholdSet = (Set)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_THRESHOLD_SET_ID), true);
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.insertEntity(evaluation);
	}
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return this.retrieveByIdsOneQuery(null, condition);
		return this.retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids, condition);
	}

//	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
//		List list = null;
//
//		String condition = EvaluationWrapper.COLUMN_MONITORED_ELEMENT_ID + SQL_IN
//				+ OPEN_BRACKET
//					+ SQL_SELECT
//					+ StorableObjectWrapper.COLUMN_ID
//					+ SQL_FROM + ObjectEntities.ME_ENTITY
//					+ SQL_WHERE + DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
//				+ CLOSE_BRACKET;
//
//		try {
//			list = retrieveButIds(ids, condition);
//		}
//		catch (IllegalDataException ide) {
//			Log.debugMessage("EvaluationDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
//		}
//
//		return list;
//	}
}
