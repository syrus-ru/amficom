/*
 * $Id: MeasurementDatabase.java,v 1.62 2005/02/11 18:39:52 arseniy Exp $
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
import java.util.Collection;
import java.util.Iterator;

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
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.62 $, $Date: 2005/02/11 18:39:52 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementDatabase extends StorableObjectDatabase {
	public static final String LINK_COLUMN_MEASUREMENT_ID = "measurement_id";
	public static final String LINK_SORT = "sort";

	private static String columns;	
	private static String updateMultiplySQLValues;

	private static final int SIZE_LOCAL_ADDRESS_COLUMN = 64;

	protected String getEnityName() {		
		return ObjectEntities.MEASUREMENT_ENTITY;
	}	

	private Measurement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Measurement)
			return (Measurement)storableObject;
		throw new IllegalDataException("MeasurementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
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

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getType().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getMonitoredElementId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurement.getName(), SIZE_NAME_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getSetup().getId()) + COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getStartTime()) + COMMA
			+ Long.toString(measurement.getDuration()) + COMMA
			+ Integer.toString(measurement.getStatus().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurement.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getTestId());
		return values;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getType().getId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getMonitoredElementId());
			DatabaseString.setString(preparedStatement, ++i, measurement.getName(), SIZE_NAME_COLUMN); 
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getSetup().getId());
			preparedStatement.setTimestamp(++i, new Timestamp(measurement.getStartTime().getTime()));
			preparedStatement.setLong(++i, measurement.getDuration());
			preparedStatement.setInt(++i, measurement.getStatus().value());
			DatabaseString.setString(preparedStatement, ++i, measurement.getLocalAddress(), SIZE_LOCAL_ADDRESS_COLUMN);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getTestId());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurement);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		Measurement measurement = (storableObject == null) ?
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
			measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
			name = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_NAME));
			measurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, MeasurementWrapper.COLUMN_SETUP_ID), true);
		}
		catch (ApplicationException ae) {
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
	
	protected String retrieveQuery(String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(MeasurementWrapper.COLUMN_START_TIME, DatabaseDate.toQuerySubString(MeasurementWrapper.COLUMN_START_TIME));
		return query;
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Measurement.RETRIEVE_RESULT:
				return this.retrieveResult(measurement, (ResultSort)arg);
			default:
				return null;
		}
	}

	private Result retrieveResult(Measurement measurement, ResultSort resultSort) throws ObjectNotFoundException, RetrieveObjectException {
		String measurementIdStr = DatabaseIdentifier.toSQLString(measurement.getId());
		int resultSortNum = resultSort.value();
		String sql = SQL_SELECT
			+ StorableObjectWrapper.COLUMN_ID
			+ SQL_FROM + ObjectEntities.RESULT_ENTITY
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
					return (Result)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
			throw new ObjectNotFoundException("No result of sort: " + resultSortNum + " for measurement " + measurementIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.retrieveResult | Cannot retrieve result of sort " + resultSortNum + " for measurement '" + measurementIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.insertEntity(measurement);
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws  IllegalDataException, VersionCollisionException, UpdateObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Measurement.UPDATE_STATUS:
				this.updateStatus(measurement);
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);					
				return;
		}
	}	

	public void update(Collection storableObjects, Identifier modifierId, int updateKind) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case Measurement.UPDATE_STATUS:
				for(Iterator it=storableObjects.iterator();it.hasNext();){
					/**
					 * FIXME recast using one step
					 */
					Measurement measurement = (Measurement)it.next();
					this.updateStatus(measurement);
				}				
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);					
				return;
		}

	}

	private void updateStatus(Measurement measurement) throws UpdateObjectException {
		String measurementIdStr = DatabaseIdentifier.toSQLString(measurement.getId());
		String sql = SQL_UPDATE
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_SET
			+ MeasurementWrapper.COLUMN_STATUS + EQUALS + Integer.toString(measurement.getStatus().value()) + COMMA
			+ StorableObjectWrapper.COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(measurement.getModified()) + COMMA
			+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(measurement.getModifierId())
			+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + measurementIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementDatabase.updateStatus | Cannot update status of measurement '" + measurementIdStr + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
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
//		String condition = MeasurementWrapper.COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
//				+ SQL_SELECT + StorableObjectWrapper.COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
//				+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
//				+ CLOSE_BRACKET;
//
//		try {
//			list = retrieveButIds(ids, condition);
//		}
//		catch (IllegalDataException ide) {           
//			Log.debugMessage("MeasurementDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
//		}
//
//		return list;
//	}

}
