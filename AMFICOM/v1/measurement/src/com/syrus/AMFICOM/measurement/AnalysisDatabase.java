/*
 * $Id: AnalysisDatabase.java,v 1.77 2006/03/27 10:10:07 bass Exp $
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
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.77 $, $Date: 2006/03/27 10:10:07 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class AnalysisDatabase extends StorableObjectDatabase<Analysis> {
	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.ANALYSIS_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_CODE + COMMA
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
	protected String getUpdateSingleSQLValuesTmpl(final Analysis storableObject) throws IllegalDataException {
		final String values = Integer.toString(storableObject.getType().ordinal()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getMonitoredElementId()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getMeasurementId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getCriteriaSet().getId());
		return values;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Analysis storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, storableObject.getType().ordinal());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMonitoredElementId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMeasurementId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getCriteriaSet().getId());
		return startParameterNumber;
	}

	@Override
	protected Analysis updateEntityFromResultSet(final Analysis storableObject, final ResultSet resultSet)
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
					: storableObject;
		ParameterSet criteriaSet;
		try {
			final Identifier criteriaSetId = DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_CRITERIA_SET_ID);
			criteriaSet = (ParameterSet) StorableObjectPool.getStorableObject(criteriaSetId, true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				AnalysisType.valueOf(resultSet.getInt(StorableObjectWrapper.COLUMN_TYPE_CODE)),
				DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MONITORED_ELEMENT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, AnalysisWrapper.COLUMN_MEASUREMENT_ID),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME)),
				criteriaSet);
		return analysis;
	}

}
