/*
 * $Id: MeasurementDatabase.java,v 1.90 2005/08/05 08:29:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.90 $, $Date: 2005/08/05 08:29:21 $
 * @author $Author: arseniy $
 * @module measurement_v1
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
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
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
		final String sql = DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
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
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
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
		MeasurementType measurementType;
		MeasurementSetup measurementSetup;
		try {
			final Identifier measurementTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID);
			measurementType = (MeasurementType) StorableObjectPool.getStorableObject(measurementTypeId, true);
			final Identifier measurementSetupId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementWrapper.COLUMN_SETUP_ID);
			measurementSetup = (MeasurementSetup) StorableObjectPool.getStorableObject(measurementSetupId, true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		measurement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				measurementType,
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

	public Result retrieveResult(final Measurement measurement, final ResultSort resultSort)
			throws ObjectNotFoundException, RetrieveObjectException {
		final String measurementIdStr = DatabaseIdentifier.toSQLString(measurement.getId());
		final int resultSortNum = resultSort.value();
		final String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.RESULT
			+ SQL_WHERE + ResultWrapper.COLUMN_MEASUREMENT_ID + EQUALS + measurementIdStr
			+ SQL_AND + ResultWrapper.COLUMN_SORT + EQUALS + Integer.toString(resultSortNum);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.retrieveResult | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {				
				try {
					return (Result)StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), true);
				} catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
			throw new ObjectNotFoundException("No result of sort: " + resultSortNum + " for measurement " + measurementIdStr);
		} catch (SQLException sqle) {
			final String mesg = "MeasurementDatabase.retrieveResult | Cannot retrieve result of sort " + resultSortNum + " for measurement '" + measurementIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
							statement = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

}
