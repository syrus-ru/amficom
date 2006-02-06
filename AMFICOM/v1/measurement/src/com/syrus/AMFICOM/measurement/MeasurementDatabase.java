/*
 * $Id: MeasurementDatabase.java,v 1.99.2.1 2006/02/06 14:46:30 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.99.2.1 $, $Date: 2006/02/06 14:46:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */

public final class MeasurementDatabase extends StorableObjectDatabase<Measurement> {
	private static String columns;	
	private static String updateMultipleSQLValues;

	private static final int SIZE_LOCAL_ADDRESS_COLUMN = 64;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MEASUREMENT_CODE;
	}	

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_CODE + COMMA
				+ MeasurementWrapper.COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ StorableObjectWrapper.COLUMN_NAME + COMMA
				+ MeasurementWrapper.COLUMN_SETUP_ID + COMMA
				+ MeasurementWrapper.COLUMN_START_TIME + COMMA
				+ MeasurementWrapper.COLUMN_DURATION + COMMA
				+ MeasurementWrapper.COLUMN_STATUS + COMMA
				+ MeasurementWrapper.COLUMN_LOCAL_ADDRESS + COMMA
				+ MeasurementWrapper.COLUMN_TEST_ID;
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
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Measurement storableObject) throws IllegalDataException {
		final String sql = Integer.toString(((Enum) storableObject.getType()).ordinal()) + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getMonitoredElementId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getName(), SIZE_NAME_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getSetup().getId()) + COMMA
			+ DatabaseDate.toUpdateSubString(storableObject.getStartTime()) + COMMA
			+ Long.toString(storableObject.getDuration()) + COMMA
			+ Integer.toString(storableObject.getStatus().value()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN) + APOSTROPHE + COMMA
			+ DatabaseIdentifier.toSQLString(storableObject.getTestId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Measurement storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, ((Enum) storableObject.getType()).ordinal());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getMonitoredElementId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getSetup().getId());
		preparedStatement.setTimestamp(++startParameterNumber, new Timestamp(storableObject.getStartTime().getTime()));
		preparedStatement.setLong(++startParameterNumber, storableObject.getDuration());
		preparedStatement.setInt(++startParameterNumber, storableObject.getStatus().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getTestId());
		return startParameterNumber;
	}

	@Override
	protected Measurement updateEntityFromResultSet(final Measurement storableObject, final ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		final Measurement measurement = (storableObject == null)
				? new Measurement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null,
						null,
						null,
						null)
					: storableObject;		

		final String name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
		MeasurementSetup measurementSetup;
		try {
			final Identifier measurementSetupId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementWrapper.COLUMN_SETUP_ID);
			measurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(measurementSetupId, true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		measurement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				MeasurementType.valueOf(resultSet.getInt(StorableObjectWrapper.COLUMN_TYPE_CODE)),
				DatabaseIdentifier.getIdentifier(resultSet, MeasurementWrapper.COLUMN_MONITORED_ELEMENT_ID),
				name,
				measurementSetup,
				DatabaseDate.fromQuerySubString(resultSet, MeasurementWrapper.COLUMN_START_TIME),
				resultSet.getLong(MeasurementWrapper.COLUMN_DURATION),
				resultSet.getInt(MeasurementWrapper.COLUMN_STATUS),
				DatabaseString.fromQuerySubString(resultSet.getString(MeasurementWrapper.COLUMN_LOCAL_ADDRESS)),
				DatabaseIdentifier.getIdentifier(resultSet, MeasurementWrapper.COLUMN_TEST_ID));
		return measurement;
	}

	@Override
	protected String retrieveQuery(final String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(MeasurementWrapper.COLUMN_START_TIME, DatabaseDate.toQuerySubString(MeasurementWrapper.COLUMN_START_TIME));
		return query;
	}

	public Measurement retrieveLast(final Identifier testId) throws RetrieveObjectException, ObjectNotFoundException {
		assert testId != null : ErrorMessages.NON_NULL_EXPECTED;
		assert testId.getMajor() == ObjectEntities.TEST_CODE : ErrorMessages.ILLEGAL_ENTITY_CODE;

		final String testIdStr = DatabaseIdentifier.toSQLString(testId);
		final String condition = MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND
						+ MeasurementWrapper.COLUMN_START_TIME + EQUALS + OPEN_BRACKET
								+ SQL_SELECT + SQL_FUNCTION_MAX + OPEN_BRACKET + MeasurementWrapper.COLUMN_START_TIME + CLOSE_BRACKET
								+ SQL_FROM + this.getEntityName()
								+ SQL_WHERE + MeasurementWrapper.COLUMN_TEST_ID + EQUALS + testIdStr
						+ CLOSE_BRACKET;
		try {
			final Set<Measurement> measurements = this.retrieveByCondition(condition);
			if (!measurements.isEmpty()) {
				return measurements.iterator().next();
			}
			throw new ObjectNotFoundException("Cannot find measurements for test '" + testId + "'");
		} catch (IllegalDataException ide) {
			//-Never
			Log.errorMessage(ide);
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
	}

}
