/*
 * $Id: MeasurementDatabase.java,v 1.41 2004/12/08 09:11:37 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identified;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.corba.ResultSort;

/**
 * @version $Revision: 1.41 $, $Date: 2004/12/08 09:11:37 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementDatabase extends StorableObjectDatabase {
	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_SETUP_ID = "setup_id";
	public static final String COLUMN_START_TIME = "start_time";
	public static final String COLUMN_DURATION = "duration";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_LOCAL_ADDRESS = "local_address";
	public static final String COLUMN_TEST_ID = "test_id";
	
	public static final String LINK_COLUMN_MEASUREMENT_ID = "measurement_id";
	public static final String LINK_SORT = "sort";
	
	private static String columns;	
	private static String updateMultiplySQLValues;	
	
	protected String getEnityName() {		
		return ObjectEntities.MEASUREMENT_ENTITY;
	}	
	
	private Measurement fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Measurement)
			return (Measurement)storableObject;
		throw new IllegalDataException("MeasurementDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	
	
	protected String getColumns(int mode) {
		if (columns == null){
			columns = super.getColumns(mode) + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ COLUMN_SETUP_ID + COMMA
				+ COLUMN_START_TIME + COMMA
				+ COLUMN_DURATION + COMMA
				+ COLUMN_STATUS + COMMA
				+ COLUMN_LOCAL_ADDRESS + COMMA
				+ COLUMN_TEST_ID;
		}
		return columns;
	}	
	
	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null){
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
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Measurement measurement = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getType().getId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurement.getName()) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getMonitoredElementId()) + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getSetup().getId()) + COMMA
			+ DatabaseDate.toUpdateSubString(measurement.getStartTime()) + COMMA
			+ Long.toString(measurement.getDuration()) + COMMA
			+ Integer.toString(measurement.getStatus().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurement.getLocalAddress()) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(measurement.getTestId());
		return values;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		Measurement measurement = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {			
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getType().getId()); 
			preparedStatement.setString(++i, measurement.getName());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getMonitoredElementId()); 
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getSetup().getId());
			preparedStatement.setTimestamp(++i, new Timestamp(measurement.getStartTime().getTime()));
			preparedStatement.setLong(++i, measurement.getDuration());
			preparedStatement.setInt(++i, measurement.getStatus().value());
			preparedStatement.setString(++i, measurement.getLocalAddress());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurement.getTestId());
		} catch (SQLException sqle) {
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
				new Measurement(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null, 
								   null, null, null, null) : 
					fromStorableObject(storableObject);		
		MeasurementType measurementType;
		String name;
		MeasurementSetup measurementSetup;
		
		try {
			measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID), true);
			name = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME));
			measurementSetup = (MeasurementSetup)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SETUP_ID), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		measurement.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
								  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
								  measurementType,
								  name,
								  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MONITORED_ELEMENT_ID),
								  measurementSetup,
								  DatabaseDate.fromQuerySubString(resultSet, COLUMN_START_TIME),
								  resultSet.getLong(COLUMN_DURATION),
								  resultSet.getInt(COLUMN_STATUS),
								  DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_LOCAL_ADDRESS)),
								  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TEST_ID));
		return measurement;
	}
	
	protected String retrieveQuery(String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(COLUMN_START_TIME, DatabaseDate.toQuerySubString(COLUMN_START_TIME));
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
			+ COLUMN_ID
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
			if (resultSet.next()){				
				try {
					return (Result)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), true);
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
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Measurement measurement = this.fromStorableObject(storableObject);
		this.insertEntity(measurement);
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws  IllegalDataException, VersionCollisionException, UpdateObjectException {
		Measurement measurement = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Measurement.UPDATE_STATUS:
				this.updateStatus(measurement);
				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);					
				return;
		}
	}	
	
	
	public void update(List storableObjects, int updateKind, Object arg) throws IllegalDataException,
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
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);					
				return;
		}

	}

	private void updateStatus(Measurement measurement) throws UpdateObjectException {
		String measurementIdStr = DatabaseIdentifier.toSQLString(measurement.getId());
		String sql = SQL_UPDATE
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_SET
			+ COLUMN_STATUS + EQUALS + Integer.toString(measurement.getStatus().value()) + COMMA
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(measurement.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(measurement.getModifierId())
			+ SQL_WHERE + COLUMN_ID + EQUALS + measurementIdStr;
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
			} finally{
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}	
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retrieveByIdsOneQuery(null, condition);
		return retrieveByIdsOneQuery(ids, condition);	
		//return retriveByIdsPreparedStatement(ids, condition);
	}
    
	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
                + SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
                + DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
            + CLOSE_BRACKET;
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("MeasurementDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }
	
	private List retrieveButIdsByTest(List ids, List testIds) throws RetrieveObjectException {
        List list = null;        
        
        if ((testIds != null) && (!testIds.isEmpty())){
        	StringBuffer buffer = new StringBuffer();
        	buffer.append(COLUMN_TEST_ID);
        	buffer.append(SQL_IN);
        	buffer.append(OPEN_BRACKET);
        	int i = 1;
			for (Iterator it = testIds.iterator(); it.hasNext(); i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identified)
					id = ((Identified)object).getId();
				else throw new RetrieveObjectException("MeasurementDatabase.retrieveButIdsByTest | Object " +
													object.getClass().getName()
													+ " isn't Identifier or Identified");

				if (id != null){
					buffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()){
						if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
							buffer.append(COMMA);
						else {
							buffer.append(CLOSE_BRACKET);
							buffer.append(SQL_OR);
							buffer.append(COLUMN_TEST_ID);
							buffer.append(SQL_IN);
							buffer.append(OPEN_BRACKET);
						}					
					}
				}
			}
        	buffer.append(CLOSE_BRACKET);
        	try {
                list = retrieveButIds(ids, buffer.toString());
            }  catch (IllegalDataException ide) {           
                Log.debugMessage("MeasurementDatabase.retrieveButIdsByTest | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
            }
        } else 
        	list = Collections.EMPTY_LIST;
        
        return list;
    }
	
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof LinkedIdsCondition){
			LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
			List testIds = linkedIdsCondition.getTestIds();
			if (testIds == null)
				testIds = Collections.singletonList(linkedIdsCondition.getIdentifier());
			list = this.retrieveButIdsByTest(ids, testIds);
		} else if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain()); 
		} else{
			Log.errorMessage("MeasurementDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
