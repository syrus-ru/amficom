/*
 * $Id: EvaluationDatabase.java,v 1.63 2005/07/27 18:20:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.63 $, $Date: 2005/07/27 18:20:25 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class EvaluationDatabase extends StorableObjectDatabase<Evaluation> {
	private static String columns;	
	private static String updateMultipleSQLValues;

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
	protected String getUpdateSingleSQLValuesTmpl(final Evaluation storableObject) throws IllegalDataException {
		final Measurement measurement = storableObject.getMeasurement();
		final String values = DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getMonitoredElementId()) + COMMA
			+ DatabaseIdentifier.toSQLString((measurement != null) ? measurement.getId() : VOID_IDENTIFIER) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getThresholdSet().getId());
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Evaluation storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final Measurement measurement = storableObject.getMeasurement();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (measurement != null) ? measurement.getId() : VOID_IDENTIFIER);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getThresholdSet().getId());
		return startParameterNumber;
	}

	@Override
	protected Evaluation updateEntityFromResultSet(final Evaluation storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Evaluation evaluation = (storableObject == null)
				? new Evaluation(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
					: storableObject;
		EvaluationType evaluationType;
		Measurement measurement = null;
		ParameterSet thresholdSet;
		try {
			final Identifier evaluationTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID);
			evaluationType = (EvaluationType) StorableObjectPool.getStorableObject(evaluationTypeId, true);
			final Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_MEASUREMENT_ID);
			if (measurementId != VOID_IDENTIFIER) {
				measurement = (Measurement) StorableObjectPool.getStorableObject(measurementId, true);
			}
			final Identifier thresholdSetId = DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_THRESHOLD_SET_ID);
			thresholdSet = (ParameterSet) StorableObjectPool.getStorableObject(thresholdSetId, true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		evaluation.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				evaluationType,
				DatabaseIdentifier.getIdentifier(resultSet, EvaluationWrapper.COLUMN_MONITORED_ELEMENT_ID),
				measurement,
				thresholdSet);
		return evaluation;
	}

}
