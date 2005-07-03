/*
 * $Id: MeasurementDatabase.java,v 1.82 2005/06/27 10:37:36 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.measurement.corba.IdlResultPackage.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.82 $, $Date: 2005/06/27 10:37:36 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public final class MeasurementDatabase extends StorableObjectDatabase {
	public static final String LINK_COLUMN_MEASUREMENT_ID = "measurement_id";
	public static final String LINK_SORT = "sort";

	private static String columns;	
	private static String updateMultipleSQLValues;

	private static final int SIZE_LOCAL_ADDRESS_COLUMN = 64;

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.MEASUREMENT_CODE;
	}	

	private Measurement fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Measurement)
			return (Measurement)storableObject;
		throw new IllegalDataException("MeasurementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
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
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final Measurement measurement = this.fromStorableObject(storableObject);
		final String sql = DatabaseIdentifier.toSQLString(measurement.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getMonitoredElementId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurement.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getSetup().getId()) + COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getStartTime()) + COMMA
			+ Long.toString(measurement.getDuration()) + COMMA
			+ Integer.toString(measurement.getStatus().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurement.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getTestId());
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject, final PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		final Measurement measurement = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, measurement.getType().getId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, measurement.getMonitoredElementId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, measurement.getName(), SIZE_NAME_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, measurement.getSetup().getId());
		preparedStatement.setTimestamp(++startParameterNumber, new Timestamp(measurement.getStartTime().getTime()));
		preparedStatement.setLong(++startParameterNumber, measurement.getDuration());
		preparedStatement.setInt(++startParameterNumber, measurement.getStatus().value());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, measurement.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, measurement.getTestId());
		return startParameterNumber;
	}

	@Override
	public void retrieve(final StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final Measurement measurement = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurement);
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		final Measurement measurement = (storableObject == null) ?
				new Measurement(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
								null,
								0L,
								null,
								null,
								null,
								null,
								null,
								null,
								null) :
					this.fromStorableObject(storableObject);		

		MeasurementType measurementType;
		String name;
		MeasurementSetup measurementSetup;
		try {
			measurementType = (MeasurementType)StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
			measurementSetup = (MeasurementSetup)StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, MeasurementWrapper.COLUMN_SETUP_ID), true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		measurement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
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

	@Override
	public Object retrieveObject(final StorableObject storableObject, final int retrieveKind, final Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final Measurement measurement = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Measurement.RETRIEVE_RESULT:
				return this.retrieveResult(measurement, (ResultSort)arg);
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  measurement.getId() + "'; argument: " + arg);
				return null;
		}
	}

	private Result retrieveResult(final Measurement measurement, final ResultSort resultSort)
			throws ObjectNotFoundException, RetrieveObjectException {
		final String measurementIdStr = DatabaseIdentifier.toSQLString(measurement.getId());
		final int resultSortNum = resultSort.value();
		final String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.RESULT
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_ID + EQUALS + measurementIdStr
			+ SQL_AND + LINK_SORT + EQUALS + Integer.toString(resultSortNum);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
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
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	@Override
	public void insert(final StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		final Measurement measurement = this.fromStorableObject(storableObject);
		super.insertEntity(measurement);
	}

	@Override
	public void insert(final Set<? extends StorableObject> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
	}

}
