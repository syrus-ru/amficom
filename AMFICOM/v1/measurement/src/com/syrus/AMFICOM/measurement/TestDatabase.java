/*
 * $Id: TestDatabase.java,v 1.47 2004/11/16 15:48:45 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.measurement.corba.MeasurementStatus;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.TestTemporalType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.MonitoredElementDatabase;
import com.syrus.AMFICOM.configuration.MeasurementPortDatabase;
import com.syrus.AMFICOM.configuration.KISDatabase;

/**
 * @version $Revision: 1.47 $, $Date: 2004/11/16 15:48:45 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class TestDatabase extends StorableObjectDatabase {
	public static final String COLUMN_ANALYSIS_TYPE_ID	= "analysis_type_id";
	public static final String COLUMN_DESCRIPTION	= "description";
	public static final String COLUMN_END_TIME	= "end_time";
	public static final String COLUMN_EVALUATION_TYPE_ID	= "evaluation_type_id";
	public static final String COLUMN_MEASUREMENT_TYPE_ID	= "measurement_type_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID	= "monitored_element_id";
	public static final String COLUMN_RETURN_TYPE	= "return_type";
	public static final String COLUMN_START_TIME	= "start_time";
	public static final String COLUMN_STATUS	= "status";
	public static final String COLUMN_TEMPORAL_PATTERN_ID	= "temporal_pattern_id";
	public static final String COLUMN_TEMPORAL_TYPE	= "temporal_type";
	
	public static final String LINK_COLMN_MEASUREMENT_SETUP_ID = "measurement_setup_id";
	public static final String LINK_COLMN_TEST_ID = "test_id";
	
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
    
	private static String columns;
	private static String updateMultiplySQLValues;	
	
	protected String getEnityName() {
		return ObjectEntities.TEST_ENTITY;
	}	
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_TEMPORAL_TYPE + COMMA
				+ COLUMN_START_TIME + COMMA
				+ COLUMN_END_TIME + COMMA
				+ COLUMN_TEMPORAL_PATTERN_ID + COMMA
				+ COLUMN_MEASUREMENT_TYPE_ID + COMMA
				+ COLUMN_ANALYSIS_TYPE_ID + COMMA
				+ COLUMN_EVALUATION_TYPE_ID + COMMA
				+ COLUMN_STATUS + COMMA
				+ COLUMN_MONITORED_ELEMENT_ID + COMMA
				+ COLUMN_RETURN_TYPE + COMMA
				+ COLUMN_DESCRIPTION;
		}
		return columns;
	}	

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
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
		Test test = fromStorableObject(storableObject);
		Date startTime = test.getStartTime();
		Date endTime = test.getEndTime();
		TemporalPattern temporalPattern = test.getTemporalPattern();		
		AnalysisType analysisType = test.getAnalysisType();
		EvaluationType evaluationType = test.getEvaluationType();

		return super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ test.getTemporalType().value() + COMMA
			+ ((startTime != null) ? DatabaseDate.toUpdateSubString(startTime) : SQL_NULL ) + COMMA
			+ ((endTime != null) ? DatabaseDate.toUpdateSubString(endTime) : SQL_NULL ) + COMMA
			+ ((temporalPattern != null) ? DatabaseIdentifier.toSQLString(temporalPattern.getId()) : SQL_NULL) + COMMA
			+ DatabaseIdentifier.toSQLString(test.getMeasurementType().getId()) + COMMA
			+ ((analysisType != null) ? DatabaseIdentifier.toSQLString(analysisType.getId()): SQL_NULL) + COMMA			
			+ ((evaluationType != null) ? DatabaseIdentifier.toSQLString(evaluationType.getId()) : SQL_NULL) + COMMA
			+ test.getStatus().value() + COMMA
			+ DatabaseIdentifier.toSQLString(test.getMonitoredElement().getId()) + COMMA
			+ test.getReturnType().value() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(test.getDescription()) + APOSTOPHE;
	}
	
	
	protected String retrieveQuery(String condition) {
		String query = super.retrieveQuery(condition);
		query = query.replaceFirst(COLUMN_START_TIME, DatabaseDate.toQuerySubString(COLUMN_START_TIME));
		query = query.replaceFirst(COLUMN_END_TIME, DatabaseDate.toQuerySubString(COLUMN_END_TIME));
		return query;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		
		Test test = fromStorableObject(storableObject);
		Date startTime = test.getStartTime();
		Date endTime = test.getEndTime();
		TemporalPattern temporalPattern = test.getTemporalPattern();		
		AnalysisType analysisType = test.getAnalysisType();
		EvaluationType evaluationType = test.getEvaluationType();
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {			
			preparedStatement.setInt(++i, test.getTemporalType().value());
			preparedStatement.setTimestamp(++i, (startTime != null) ? (new Timestamp(startTime.getTime())) : null);
			preparedStatement.setTimestamp(++i, (endTime != null) ? (new Timestamp(endTime.getTime())) : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (temporalPattern != null) ? temporalPattern.getId() : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, test.getMeasurementType().getId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (analysisType != null) ? analysisType.getId() : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (evaluationType != null) ? evaluationType.getId() : null);
			preparedStatement.setInt(++i, test.getStatus().value());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, test.getMonitoredElement().getId());
			preparedStatement.setInt(++i, test.getReturnType().value());
			preparedStatement.setString(++i, test.getDescription());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	private Test fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Test)
			return (Test)storableObject;
		throw new IllegalDataException("TestDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Test test = this.fromStorableObject(storableObject);
		this.retrieveEntity(test);
		this.retrieveMeasurementSetupTestLinks(test);
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Test test = (storableObject == null)?
				new Test(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, null, TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME, 
						 null, null, null, null, 0, null, null) :
					fromStorableObject(storableObject);
		TemporalPattern temporalPattern;
		MeasurementType measurementType;
		AnalysisType analysisType;
		EvaluationType evaluationType;
		MonitoredElement monitoredElement;
		try {			
			Identifier temportalPatternId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TEMPORAL_PATTERN_ID);
			temporalPattern = (temportalPatternId != null) ? (TemporalPattern)MeasurementStorableObjectPool.getStorableObject(temportalPatternId, true) : null;
			
			measurementType = (MeasurementType)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MEASUREMENT_TYPE_ID), true);
			Identifier analysisTypeId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANALYSIS_TYPE_ID);
			analysisType = (analysisTypeId != null) ? (AnalysisType)MeasurementStorableObjectPool.getStorableObject(analysisTypeId, true) : null;
			Identifier evaluationTypeId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_EVALUATION_TYPE_ID);
			evaluationType = (evaluationTypeId != null) ? (EvaluationType)MeasurementStorableObjectPool.getStorableObject(evaluationTypeId, true) : null;
			Identifier monitoredElementId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MONITORED_ELEMENT_ID);
			monitoredElement = (MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(monitoredElementId, true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		test.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
						   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
						   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
						   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
						   resultSet.getInt(COLUMN_TEMPORAL_TYPE),
						   DatabaseDate.fromQuerySubString(resultSet, COLUMN_START_TIME),
						   DatabaseDate.fromQuerySubString(resultSet, COLUMN_END_TIME),
						   temporalPattern,
						   measurementType,
						   analysisType,
						   evaluationType,
						   resultSet.getInt(COLUMN_STATUS),
						   monitoredElement,
						   resultSet.getInt(COLUMN_RETURN_TYPE),
						   (description != null) ? description: "");

		return test;
	}
	
	private void retrieveMeasurementSetupTestLinks(Test test) throws RetrieveObjectException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ LINK_COLMN_MEASUREMENT_SETUP_ID
			+ SQL_FROM + ObjectEntities.MSTESTLINK_ENTITY
			+ SQL_WHERE + LINK_COLMN_TEST_ID + EQUALS + testIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		List msList = new LinkedList();
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementSetupTestLinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				msList.add(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLMN_MEASUREMENT_SETUP_ID));
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveMeasurementSetupTestLinks | Cannot retrieve measurement setup ids for test '" + testIdStr + "' -- " + sqle.getMessage();
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
			}  finally {
				DatabaseConnection.closeConnection(connection);
			}
		}
		if (!msList.isEmpty())
			test.setMeasurementSetupIds(msList);
		else 
			throw new RetrieveObjectException("TestDatabase.retrieveMeasurementSetupTestLinks | Measurement setup ids for test '" + testIdStr + "' not found.");
	}
    
    private void retrieveMeasurementSetupTestLinksByOneQuery(List tests) throws RetrieveObjectException {
    	if ((tests == null) || (tests.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLMN_MEASUREMENT_SETUP_ID + COMMA
                + LINK_COLMN_TEST_ID
                + SQL_FROM + ObjectEntities.MSTESTLINK_ENTITY
                + SQL_WHERE + LINK_COLMN_TEST_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = tests.iterator(); it.hasNext();i++) {
            Test test = (Test)it.next();
            sql.append(DatabaseIdentifier.toSQLString(test.getId()));
            if (it.hasNext()){
                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
                    sql.append(COMMA);
                else {
                    sql.append(CLOSE_BRACKET);
                    sql.append(SQL_OR);
                    sql.append(COLUMN_ID);
                    sql.append(SQL_IN);
                    sql.append(OPEN_BRACKET);
                }                   
            }
        }
        sql.append(CLOSE_BRACKET);
        
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = DatabaseConnection.getConnection();
        try {
            statement = connection.createStatement();
            Log.debugMessage("TestDatabase.retrieveMeasurementSetupTestLinksByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map msIdMap = new HashMap();
            while (resultSet.next()) {
                Test test = null;
                Identifier testId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLMN_TEST_ID);
                for (Iterator it = tests.iterator(); it.hasNext();) {
                    Test testToCompare = (Test) it.next();
                    if (testToCompare.getId().equals(testId)){
                        test = testToCompare;
                        break;
                    }                   
                }
                
                if (test == null){
                    String mesg = "TestDatabase.retrieveMeasurementSetupTestLinksByOneQuery | Cannot found correspond result for '" + testId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                Identifier msId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLMN_MEASUREMENT_SETUP_ID);
                List msIds = (List)msIdMap.get(test);
                if (msIds == null){
                    msIds = new LinkedList();
                    msIdMap.put(test, msIds);
                }               
                msIds.add(msId);              
            }
            
            for (Iterator iter = tests.iterator(); iter.hasNext();) {
                Test test = (Test) iter.next();
                List msIds = (List)msIdMap.get(test);
                test.setMeasurementSetupIds(msIds);
            }
            
        } catch (SQLException sqle) {
            String mesg = "TestDatabase.retrieveMeasurementSetupTestLinksByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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
                DatabaseConnection.closeConnection(connection);
            }
        }
    }

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Test test = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			case Test.RETRIEVE_MEASUREMENTS:
				return this.retrieveMeasurementsOrderByStartTime(test, (MeasurementStatus)arg);
			case Test.RETRIEVE_LAST_MEASUREMENT:
				return this.retrieveLastMeasurement(test);
			case Test.RETRIEVE_NUMBER_OF_MEASUREMENTS:
				return this.retrieveNumberOfMeasurements(test);
			case Test.RETRIEVE_NUMBER_OF_RESULTS:
				return this.retrieveNumberOfResults(test, (ResultSort)arg);
			default:
				return null;
		}
	}

	private List retrieveMeasurementsOrderByStartTime(Test test, MeasurementStatus measurementStatus) throws RetrieveObjectException {
		List measurements = new ArrayList();

		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND + MeasurementDatabase.COLUMN_STATUS + EQUALS + Integer.toString(measurementStatus.value())
			+ SQL_ORDER_BY + MeasurementDatabase.COLUMN_START_TIME + " ASC";

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveMeasurementsOrderByStartTime | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				measurements.add((Measurement)MeasurementStorableObjectPool.getStorableObject(
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), true));
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveMeasurementsOrderByStartTime | Cannot retrieve measurements for test '" + testIdStr + "' and status " + Integer.toString(measurementStatus.value()) + " -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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
				DatabaseConnection.closeConnection(connection);
			}
		}
		((ArrayList)measurements).trimToSize();
		return measurements;
	}
	
	private Measurement retrieveLastMeasurement(Test test) throws RetrieveObjectException, ObjectNotFoundException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM
			+ ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
				+ SQL_AND + MeasurementDatabase.COLUMN_START_TIME + EQUALS
					+ OPEN_BRACKET
						+ SQL_SELECT
						+ SQL_FUNCTION_MAX + OPEN_BRACKET + MeasurementDatabase.COLUMN_START_TIME + CLOSE_BRACKET
						+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
						+ SQL_WHERE + MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
					+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveLastMeasurement | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				try {
					return (Measurement)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			else
				throw new ObjectNotFoundException("No last measurement for test: " + testIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveLastMeasurement | Cannot retrieve last measurement for test '" + testIdStr + "' -- " + sqle.getMessage();
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	private Integer retrieveNumberOfMeasurements(Test test) throws RetrieveObjectException, ObjectNotFoundException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ SQL_COUNT + " count "
			+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
			+ SQL_WHERE + MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveNumberOfMeasurements | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new Integer(resultSet.getInt("count"));
			else
				throw new ObjectNotFoundException("No number of measurements for test '" + testIdStr + "'");
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveNumberOfMeasurements | Cannot retrieve number of measurements for test '" + testIdStr + "' -- " + sqle.getMessage();
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	private Integer retrieveNumberOfResults(Test test, ResultSort resultSort) throws RetrieveObjectException, ObjectNotFoundException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_SELECT
			+ SQL_COUNT + " count "
			+ SQL_FROM + ObjectEntities.RESULT_ENTITY
			+ SQL_WHERE + ResultDatabase.COLUMN_SORT + EQUALS + Integer.toString(resultSort.value())
				+ SQL_AND + ResultDatabase.COLUMN_MEASUREMENT_ID + SQL_IN
					+ OPEN_BRACKET
						+ SQL_SELECT
						+ COLUMN_ID
						+ SQL_FROM + ObjectEntities.MEASUREMENT_ENTITY
						+ SQL_WHERE + MeasurementDatabase.COLUMN_TEST_ID + EQUALS + testIdStr
					+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.retrieveNumberOfResults | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new Integer(resultSet.getInt("count"));
			else
				throw new ObjectNotFoundException("No number of results for test '" + testIdStr + "' of result sort " + resultSort.value());
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.retrieveNumberOfMeasurements | Cannot retrieve number of results for test '" + testIdStr + "' of result sort " + resultSort.value() + " -- " + sqle.getMessage();
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Test test = this.fromStorableObject(storableObject);
		this.insertEntity(test);
		this.insertMeasurementSetupTestLinks(test);
		
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		if ((storableObjects == null) || (storableObjects.size() == 0))
			return;
		
		if (storableObjects.size() == 1){
			Test test = (Test)storableObjects.get(0);
			insertEntity(test);
			insertMeasurementSetupTestLinks(test);
			return;
		}
		
		insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Test test = (Test) it.next();
			insertMeasurementSetupTestLinks(test);
		}
		
	}
	
	private void insertMeasurementSetupTestLinks(Test test) throws CreateObjectException {
		Identifier testId = test.getId();
		List msIds = test.getMeasurementSetupIds();
		String sql = SQL_INSERT_INTO + ObjectEntities.MSTESTLINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLMN_TEST_ID + COMMA
			+ LINK_COLMN_MEASUREMENT_SETUP_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		Identifier msId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = msIds.iterator(); iterator.hasNext();) {
				msId = ((Identifier)iterator.next());
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, testId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, msId);
				Log.debugMessage("TestDatabase.insertMeasurementSetupTestLinks | Inserting link for test " + testId + " and measurement setup " + msId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.insertMeasurementSetupTestLinks | Cannot insert link for measurement setup '" + msId + "' and test '" + testId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object arg) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Test test = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Test.UPDATE_STATUS:
				this.updateStatus(test);
				break;
			case Test.UPDATE_MODIFIED:
				this.updateModified(test);
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
			case Test.UPDATE_STATUS:
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					this.updateStatus(test);
				}				
				break;
			case Test.UPDATE_MODIFIED:
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					Test test = (Test) it.next();
					this.updateModified(test);
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

	private void updateStatus(Test test) throws UpdateObjectException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ COLUMN_STATUS + EQUALS + Integer.toString(test.getStatus().value()) + COMMA
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(test.getModifierId())
			+ SQL_WHERE + COLUMN_ID + EQUALS + testIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateStatus | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.updateStatus | Cannot update status of test '" + testIdStr + "' -- " + sqle.getMessage();
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
			} finally {
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	private void updateModified(Test test) throws UpdateObjectException {
		String testIdStr = DatabaseIdentifier.toSQLString(test.getId());
		String sql = SQL_UPDATE + ObjectEntities.TEST_ENTITY
			+ SQL_SET
			+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(test.getModified()) + COMMA
			+ COLUMN_MODIFIER_ID + EQUALS + DatabaseIdentifier.toSQLString(test.getModifierId())
			+ SQL_WHERE + COLUMN_ID + EQUALS + testIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("TestDatabase.updateModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "TestDatabase.updateModified | Cannot update modified of test '" + testIdStr + "' -- " + sqle.getMessage();
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
			} finally {
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public List retrieveTests(TestStatus status) throws RetrieveObjectException {
		List list = null;
		try{
		list = retrieveByIds(null, COLUMN_STATUS + EQUALS + Integer.toString(status.value())
									+ SQL_ORDER_BY + COLUMN_START_TIME + SQL_ASC);
		}catch(IllegalDataException ide){
			Log.debugMessage("TestDatabase.retrieveTests | Trying: " + ide, Log.DEBUGLEVEL09);
		}
		return list;
	}

	public List retrieveTestsForMCM(Identifier mcmId, TestStatus status) throws RetrieveObjectException {
		
		String mcmIdStr = DatabaseIdentifier.toSQLString(mcmId);
		String condition = COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.ME_ENTITY
				+ SQL_WHERE + MonitoredElementDatabase.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
					+ SQL_SELECT
					+ COLUMN_ID
					+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
					+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID + SQL_IN + OPEN_BRACKET
						+ SQL_SELECT
						+ COLUMN_ID
						+ SQL_FROM + ObjectEntities.KIS_ENTITY
						+ SQL_WHERE + KISDatabase.COLUMN_MCM_ID + EQUALS + mcmIdStr
					+ CLOSE_BRACKET
				+ CLOSE_BRACKET
			+ CLOSE_BRACKET
				+ SQL_AND + COLUMN_STATUS + EQUALS + Integer.toString(status.value())
			+ SQL_ORDER_BY + COLUMN_START_TIME + SQL_ASC;		

		List list = null;
		
		try {
			list = retrieveByIds(null, condition);
		}  catch (IllegalDataException ide) {			
			Log.debugMessage("TestDatabase.retrieveTestsForMCM | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}
		
		return list;
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		List list = null;
		
		try {
			list = retrieveByIds(null, null);
		}  catch (IllegalDataException ide) {			
			Log.debugMessage("TestDatabase.retrieveAll | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}
		
		return list;
	}
	
	private List retrieveButIdsByTimeRange(List ids, Domain domain, Date start, Date end) throws RetrieveObjectException {
		List list = null;		
		
		String condition = COLUMN_START_TIME + " <= " + DatabaseDate.toUpdateSubString(end)
			+ SQL_AND + COLUMN_END_TIME + " >= " + DatabaseDate.toUpdateSubString(start)
			+ SQL_AND 
			+ COLUMN_MONITORED_ELEMENT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
				+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
			+ CLOSE_BRACKET;
		
		try {
			list = retrieveButIds(ids, condition);
		}  catch (IllegalDataException ide) {			
			Log.debugMessage("TestDatabase.retrieveButIdsByTimeRange | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}
		
		return list;
	}
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = retrieveByIdsOneQuery(null, condition);
		else list = retrieveByIdsOneQuery(ids, condition);
		
		retrieveMeasurementSetupTestLinksByOneQuery(list);	
		
		return list;
	}	

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list;
		if (condition instanceof TemporalCondition){
			TemporalCondition testCondition = (TemporalCondition) condition;
			list = this.retrieveButIdsByTimeRange(ids, testCondition.getDomain(), testCondition.getStart(), testCondition.getEnd());
		} else				
			throw new RetrieveObjectException("TestDatabase.retrieveByCondition | Condition class doesn't support : " + condition );
		return list;
	}
	
}
