/*
 * $Id: EvaluationDatabase.java,v 1.59 2005/07/14 19:02:39 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

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
 * @version $Revision: 1.59 $, $Date: 2005/07/14 19:02:39 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class EvaluationDatabase extends StorableObjectDatabase {

	private static String columns;	
	private static String updateMultipleSQLValues;

	private Evaluation fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Evaluation)
			return (Evaluation) storableObject;
		throw new IllegalDataException("EvaluationDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	public void retrieve(final StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Evaluation evaluation = this.fromStorableObject(storableObject);
		this.retrieveEntity(evaluation);
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.EVALUATION_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EvaluationWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ EvaluationWrapper.COLUMN_MEASUREMENT_ID + COMMA
				+ EvaluationWrapper.COLUMN_THRESHOLD_SET_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Evaluation evaluation = this.fromStorableObject(storableObject);
		final Measurement measurement = evaluation.getMeasurement();
		final String values = DatabaseIdentifier.toSQLString(evaluation.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(evaluation.getMonitoredElementId()) + COMMA
			+ DatabaseIdentifier.toSQLString((measurement != null) ? measurement.getId() : Identifier.VOID_IDENTIFIER) + COMMA
			+ DatabaseIdentifier.toSQLString(evaluation.getThresholdSet().getId());
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Evaluation evaluation = this.fromStorableObject(storableObject);
		final Measurement measurement = evaluation.getMeasurement();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, evaluation.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, evaluation.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (measurement != null) ? measurement.getId() : Identifier.VOID_IDENTIFIER);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, evaluation.getThresholdSet().getId());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Evaluation evaluation = (storableObject == null) ?
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
		ParameterSet thresholdSet;
		try {
			evaluationType = (EvaluationType)StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			final Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_MEASUREMENT_ID);
			if (measurementId != null)
				measurement = (Measurement) StorableObjectPool.getStorableObject(measurementId, true);
			thresholdSet = (ParameterSet)StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_THRESHOLD_SET_ID), true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		evaluation.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				evaluationType,
				DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_MONITORED_ELEMENT_ID),
				measurement,
				thresholdSet);
		return evaluation;
	}

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException {
		final Evaluation evaluation = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  evaluation.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		final Evaluation evaluation = this.fromStorableObject(storableObject);
		super.insertEntity(evaluation);
	}

	@Override
	public void insert(final Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

}
