/*
 * $Id: AnalysisDatabase.java,v 1.68 2005/07/25 20:50:06 arseniy Exp $
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
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.68 $, $Date: 2005/07/25 20:50:06 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class AnalysisDatabase extends StorableObjectDatabase {
	private static String columns;
	private static String updateMultipleSQLValues;

	private Analysis fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
		throw new IllegalDataException("AnalysisDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.ANALYSIS_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
					+ AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
					+ AnalysisWrapper.COLUMN_MEASUREMENT_ID + COMMA
					+ StorableObjectWrapper.COLUMN_NAME + COMMA
					+ AnalysisWrapper.COLUMN_CRITERIA_SET_ID;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Analysis analysis = this.fromStorableObject(storableObject);
		final Measurement measurement = analysis.getMeasurement();
		final String values = DatabaseIdentifier.toSQLString(analysis.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(analysis.getMonitoredElementId()) + COMMA
			+ DatabaseIdentifier.toSQLString((measurement != null) ? measurement.getId() : Identifier.VOID_IDENTIFIER) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(analysis.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(analysis.getCriteriaSet().getId());
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final Analysis analysis = this.fromStorableObject(storableObject);
		final Measurement measurement = analysis.getMeasurement();
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, analysis.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, analysis.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, (measurement != null) ? measurement.getId() : Identifier.VOID_IDENTIFIER);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, analysis.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, analysis.getCriteriaSet().getId());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Analysis analysis = (storableObject == null)
				? new Analysis(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null)
					: this.fromStorableObject(storableObject);
		AnalysisType analysisType;
		Measurement measurement = null;
		ParameterSet criteriaSet;
		try {
			analysisType = (AnalysisType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			final Identifier measurementId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MEASUREMENT_ID);
			if (measurementId != null)
				measurement = (Measurement) StorableObjectPool.getStorableObject(measurementId, true);
			criteriaSet = (ParameterSet) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_CRITERIA_SET_ID), true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				analysisType,
				DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID),
				measurement,
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				criteriaSet);
		return analysis;
	}

}
