/*
 * $Id: MeasurementSetupDatabase.java,v 1.43 2004/11/17 08:13:30 bob Exp $
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.43 $, $Date: 2004/11/17 08:13:30 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementSetupDatabase extends StorableObjectDatabase {

	public static final String	COLUMN_CRITERIA_SET_ID				= "criteria_set_id";
	public static final String	COLUMN_DESCRIPTION					= "description";
	public static final String	COLUMN_ETALON_ID					= "etalon_id";
	public static final String	COLUMN_MEASUREMENT_DURAION			= "measurement_duration";
	public static final String	COLUMN_PARAMETER_SET_ID				= "parameter_set_id";
	public static final String	COLUMN_THRESHOLD_SET_ID				= "threshold_set_id";

	public static final String	LINK_COLUMN_ME_ID					= "monitored_element_id";
	public static final String	LINK_COLUMN_MEASUREMENT_SETUP_ID	= "measurement_setup_id";
	
    public static final String  PARAMETER_TYPE_ID                   = "parameter_type_id";
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;	
	
	private static String columns;	
	private static String updateMultiplySQLValues;	
	
	protected String getEnityName() {
		return ObjectEntities.MS_ENTITY;
	}	
	
	private MeasurementSetup fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementSetup)
			return (MeasurementSetup) storableObject;
		throw new IllegalDataException("MeasurementSetupDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementSetup);
		this.retrieveMeasurementSetupMELinks(measurementSetup);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		this.insertEntity(measurementSetup);
		this.insertMeasurementSetupMELinks(measurementSetup);
	}
	
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
			this.insertMeasurementSetupMELinks(measurementSetup);
		}

	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
			switch (updateKind) {
				case MeasurementSetup.UPDATE_ATTACH_ME:
					this.createMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
					break;
				case MeasurementSetup.UPDATE_DETACH_ME:
					this.deleteMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
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
	
	
	public void update(List storableObjects, int updateKind, Object obj) throws IllegalDataException,
			VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case MeasurementSetup.UPDATE_ATTACH_ME:
				for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
					MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
					this.createMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);					
				}
				break;
			case MeasurementSetup.UPDATE_DETACH_ME:
				for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
					MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
					this.deleteMEAttachment(measurementSetup, (Identifier) obj);
					this.setModified(measurementSetup);
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
	

	private void createMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws UpdateObjectException {
		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MSMELINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA
			+ LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ msIdStr + COMMA
			+ meIdStr
			+ CLOSE_BRACKET;

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.createMEAttachment | Cannot attach measurement setup '" + msIdStr + "' to monitored element '" + meIdStr + "' -- " + sqle.getMessage();
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	private void deleteMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws IllegalDataException {
		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_DELETE_FROM 
			+ ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr
			+ SQL_AND
				+ LINK_COLUMN_ME_ID + EQUALS + meIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.deleteMEAttachment | Cannot detach measurement setup '" + msIdStr + "' from monitored element '" + meIdStr + "' -- " + sqle.getMessage();
			throw new IllegalDataException(mesg, sqle);
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}
	
	protected String getColumns() {
		if (columns == null){			
			columns = super.getColumns() + COMMA
				+ COLUMN_PARAMETER_SET_ID + COMMA
				+ COLUMN_CRITERIA_SET_ID + COMMA
				+ COLUMN_THRESHOLD_SET_ID + COMMA
				+ COLUMN_ETALON_ID + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_MEASUREMENT_DURAION;
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
				+ QUESTION;
		}
		
		return updateMultiplySQLValues;
	}	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {		
		MeasurementSetup measurementSetup = fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(measurementSetup.getParameterSet().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString((criteriaSet != null) ? criteriaSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((thresholdSet != null) ? thresholdSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((etalon != null) ? etalon.getId() : null) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementSetup.getDescription()) + APOSTOPHE + COMMA
			+ Long.toString(measurementSetup.getMeasurementDuration());
		return values;
	}
	
	
	protected int  setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		MeasurementSetup measurementSetup = fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurementSetup.getParameterSet().getId()); 
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (criteriaSet != null) ? criteriaSet.getId() : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (thresholdSet != null) ? thresholdSet.getId() : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (etalon != null) ? etalon.getId() : null);
			preparedStatement.setString(++i, measurementSetup.getDescription());
			preparedStatement.setLong(++i, measurementSetup.getMeasurementDuration());
			
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	private void insertMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws CreateObjectException {
		Identifier msId = measurementSetup.getId();
		List meIds = measurementSetup.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
				+ ObjectEntities.MSMELINK_ENTITY + OPEN_BRACKET
				+ LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA 
				+ LINK_COLUMN_ME_ID
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier meId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				meId = (Identifier) iterator.next();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, msId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, meId);
				Log.debugMessage("MeasurementSetupDatabase.insertMeasurementSetupMELinks | Inserting link for measurement setup "
										+ msId
										+ " and monitored element "
										+ meId.getIdentifierString(), Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.insertMeasurementSetupMELinks | Cannot insert link for monitored element '" + meId.getIdentifierString() + "' and Measurement Setup '" + msId + "' -- " + sqle.getMessage();
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementSetup measurementSetup = (storableObject == null) ? 
				new MeasurementSetup(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, 
									   null, null, null, 0, null) : 
					fromStorableObject(storableObject);	
		Set parameterSet;
		Set criteriaSet;
		Set thresholdSet;
		Set etalon;
		Identifier id;
		try {
			parameterSet = (Set)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_PARAMETER_SET_ID), true);			
			id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CRITERIA_SET_ID);
			criteriaSet = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;
			id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_THRESHOLD_SET_ID);
			thresholdSet = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;			
			id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ETALON_ID);
			etalon = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									   parameterSet,
									   criteriaSet,
									   thresholdSet,
									   etalon,
									   (description != null) ? description : "",
									   resultSet.getLong(COLUMN_MEASUREMENT_DURAION));
		return measurementSetup;
	}

	private void retrieveMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws RetrieveObjectException {
		List meIds = new ArrayList();

		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String sql = SQL_SELECT
			+ LINK_COLUMN_ME_ID
			+ SQL_FROM + ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				meIds.add(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_ME_ID));
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Cannot retrieve monitored element ids for measurement setup '" + msIdStr + "' -- " + sqle.getMessage();
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
		measurementSetup.setMonitoredElementIds(meIds);
	}
    
    private void retrieveMeasurementSetupMELinksByOneQuery(List measurementSetups) throws RetrieveObjectException {
    	if ((measurementSetups == null) || (measurementSetups.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLUMN_ME_ID + COMMA
                + LINK_COLUMN_MEASUREMENT_SETUP_ID
                + SQL_FROM + ObjectEntities.MSMELINK_ENTITY
                + SQL_WHERE + LINK_COLUMN_MEASUREMENT_SETUP_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = measurementSetups.iterator(); it.hasNext();i++) {
            MeasurementSetup measurementSetup = (MeasurementSetup)it.next();
            sql.append(DatabaseIdentifier.toSQLString(measurementSetup.getId()));
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
            Log.debugMessage("MeasurementSetupDatabase.retrieveMeasurementSetupMELinksByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map meIdMap = new HashMap();
            while (resultSet.next()) {
                MeasurementSetup measurementSetup = null;
                Identifier measurementSetupId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MEASUREMENT_SETUP_ID);
                for (Iterator it = measurementSetups.iterator(); it.hasNext();) {
                    MeasurementSetup measurementSetupToCompare = (MeasurementSetup) it.next();
                    if (measurementSetupToCompare.getId().equals(measurementSetupId)){
                        measurementSetup = measurementSetupToCompare;
                        break;
                    }                   
                }
                
                if (measurementSetup == null){
                    String mesg = "MeasurementSetupDatabase.retrieveMeasurementSetupMELinksByOneQuery | Cannot found correspond result for '" + measurementSetupId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }                    
                
                Identifier meId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_ME_ID);
                List meIds = (List)meIdMap.get(measurementSetup);
                if (meIds == null){
                    meIds = new LinkedList();
                    meIdMap.put(measurementSetup, meIds);
                }               
                meIds.add(meId);              
            }
            
            for (Iterator iter = measurementSetups.iterator(); iter.hasNext();) {
                MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
                List meIds = (List)meIdMap.get(measurementSetup);
                measurementSetup.setMonitoredElementIds(meIds);
            }
            
        } catch (SQLException sqle) {
            String mesg = "ResultDatabase.retrieveResultParameters | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

	private void setModified(MeasurementSetup measurementSetup) throws UpdateObjectException {
		String msIdStr = measurementSetup.getId().toString();
		String sql = SQL_UPDATE
				+ ObjectEntities.MS_ENTITY
				+ SQL_SET
				+ COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(measurementSetup.getModified()) + COMMA
				+ COLUMN_MODIFIER_ID + EQUALS + measurementSetup.getModifierId().toString()
				+ SQL_WHERE + COLUMN_ID + msIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementSetupDatabase.setModified | Cannot set modified for measurement setup '"	+ msIdStr + "' -- " + sqle.getMessage();
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}	
	
	public List retrieveAll() throws RetrieveObjectException {
		try{
			return retrieveByIds(null, null);
		}catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}
	
	public List retrieveByIds(List ids, String condition) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = retrieveByIdsOneQuery(null, condition);
		else list = retrieveByIdsOneQuery(ids, condition);
		
		retrieveMeasurementSetupMELinksByOneQuery(list);
		
		
		return list;	
	}
	
	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
		List list = null;
		
		String condition = COLUMN_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT + LINK_COLUMN_MEASUREMENT_SETUP_ID + SQL_FROM + ObjectEntities.MSMELINK_ENTITY
				+ SQL_WHERE + LINK_COLUMN_ME_ID + SQL_IN + OPEN_BRACKET
					+ SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
					+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
					+ CLOSE_BRACKET
				+ CLOSE_BRACKET;		
		try {
			list = retrieveButIds(ids, condition);
		}  catch (IllegalDataException ide) {			
			Log.debugMessage("MeasurementSetupDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}
		
		return list;
	}

	private List retrieveButIdsByMeasurementType(List ids, MeasurementType measurementType) throws RetrieveObjectException {
		List list = null;
		
		String condition = COLUMN_PARAMETER_SET_ID + SQL_IN + OPEN_BRACKET		
							+ SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
							+ SQL_WHERE + StorableObjectDatabase.LINK_COLUMN_PARAMETER_TYPE_ID + SQL_IN + OPEN_BRACKET			
								+ SQL_SELECT + StorableObjectDatabase.LINK_COLUMN_PARAMETER_TYPE_ID
								+ SQL_FROM + ObjectEntities.MNTTYPPARTYPLINK_ENTITY + SQL_WHERE
								+ MeasurementTypeDatabase.LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + DatabaseIdentifier.toSQLString(measurementType.getId())								
							+ CLOSE_BRACKET							
			+ CLOSE_BRACKET;
		
		
		try {
			list = retrieveButIds(ids, condition);
		}  catch (IllegalDataException ide) {			
			Log.debugMessage("MeasurementSetupDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}
		
		return list;
	}
	
	private List retrieveButIdsByMonitoredElement(List ids, MonitoredElement monitoredElement) throws RetrieveObjectException {
		List list = null;
		
		String condition = COLUMN_ID + SQL_IN + OPEN_BRACKET
					+ SQL_SELECT + LINK_COLUMN_MEASUREMENT_SETUP_ID + ObjectEntities.MSMELINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_ME_ID + EQUALS + DatabaseIdentifier.toSQLString(monitoredElement.getId())
				+ CLOSE_BRACKET;		
		
		try {
			list = retrieveButIds(ids, condition);
		}  catch (IllegalDataException ide) {			
			Log.debugMessage("MeasurementSetupDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
		}
		
		return list;
	}
    
    private List retrieveButIdMeasurementIds(List ids, List measurementIds) throws RetrieveObjectException, IllegalDataException {
    	
    	if (measurementIds != null && !measurementIds.isEmpty()){
	        String condition = new String();
	        StringBuffer measurementIdsStr = new StringBuffer();
	        
	       	int i=1;
	        for (Iterator it = measurementIds.iterator(); it.hasNext();i++) {
	       		 Identifier id = (Identifier) it.next();
	       		 measurementIdsStr.append( DatabaseIdentifier.toSQLString(id) );
	       		 if (it.hasNext()){
		       		 if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
		       		 	measurementIdsStr.append(COMMA);
		       		 else {
		                measurementIdsStr.append(CLOSE_BRACKET);
		                measurementIdsStr.append(SQL_OR);
		                measurementIdsStr.append(COLUMN_ID);
		                measurementIdsStr.append(SQL_IN);
		                measurementIdsStr.append(OPEN_BRACKET);
		       		 }  
	       		 }
	       	}
	        
	        condition = COLUMN_ID + SQL_IN + OPEN_BRACKET	
	                        + SQL_SELECT + LINK_COLUMN_MEASUREMENT_SETUP_ID + SQL_FROM + ObjectEntities.MSMELINK_ENTITY
	                        + SQL_WHERE + LINK_COLUMN_ME_ID + NOT + SQL_IN + OPEN_BRACKET + measurementIdsStr.toString() 
	                        + CLOSE_BRACKET
	                    + CLOSE_BRACKET;        
	        return retrieveButIds(ids , condition);
    	}
    	return Collections.EMPTY_LIST;
    }
    
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof MeasurementSetupCondition){
			MeasurementSetupCondition measurementSetupCondition = (MeasurementSetupCondition)condition;
			MeasurementType measurementType = measurementSetupCondition.getMeasurementType();				
			MonitoredElement monitoredElement = measurementSetupCondition.getMonitoredElement();
			if (measurementType!=null)
				list = this.retrieveButIdsByMeasurementType(ids, measurementType);
			else if (monitoredElement != null)
				list = this.retrieveButIdsByMonitoredElement(ids, monitoredElement);			
        } else if ( condition instanceof LinkedIdsCondition ) {
            LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
            List linkedIds = linkedIdsCondition.getLinkedIds();
            if (linkedIds == null){
            	linkedIds = Collections.singletonList(linkedIdsCondition.getIdentifier());
            }
            list = this.retrieveButIdMeasurementIds(ids, linkedIds);
        } else {
			Log.errorMessage("MeasurementSetupDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
    
}
