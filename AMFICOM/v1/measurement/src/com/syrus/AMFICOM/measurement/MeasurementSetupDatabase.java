/*
 * $Id: MeasurementSetupDatabase.java,v 1.67 2005/02/11 18:39:52 arseniy Exp $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.67 $, $Date: 2005/02/11 18:39:52 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */

public class MeasurementSetupDatabase extends StorableObjectDatabase {

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
//		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
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
	
	
	
	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
			this.insertMeasurementSetupMELinks(measurementSetup);
		}

	}

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
			switch (updateKind) {
//				case MeasurementSetup.UPDATE_ATTACH_ME:
//					this.createMEAttachment(measurementSetup, (Identifier) obj);
//					this.setModified(measurementSetup);
//					break;
//				case MeasurementSetup.UPDATE_DETACH_ME:
//					this.deleteMEAttachment(measurementSetup, (Identifier) obj);
//					this.setModified(measurementSetup);
//					break;
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
//			case MeasurementSetup.UPDATE_ATTACH_ME:
//				for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
//					MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
//					this.createMEAttachment(measurementSetup, (Identifier) obj);
//					this.setModified(measurementSetup);					
//				}
//				break;
//			case MeasurementSetup.UPDATE_DETACH_ME:
//				for (Iterator iter = storableObjects.iterator(); iter.hasNext();) {
//					MeasurementSetup measurementSetup = (MeasurementSetup) iter.next();
//					this.deleteMEAttachment(measurementSetup, (Identifier) obj);
//					this.setModified(measurementSetup);
//				}
//				break;
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);					
				return;
		}

	}
	

	private void createMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws UpdateObjectException {
		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.MSMELINK_ENTITY + OPEN_BRACKET
			+ MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA
			+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID
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
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void deleteMEAttachment(MeasurementSetup measurementSetup, Identifier monitoredElementId) throws IllegalDataException {
		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String meIdStr = DatabaseIdentifier.toSQLString(monitoredElementId);
		String sql = SQL_DELETE_FROM 
			+ ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE
			+ MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr
			+ SQL_AND
				+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID + EQUALS + meIdStr;
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
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	protected String getColumns(int mode) {
		if (columns == null){			
			columns = super.getColumns(mode) + COMMA
				+ MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID + COMMA
				+ MeasurementSetupWrapper.COLUMN_ETALON_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION + COMMA
				+ MeasurementSetupWrapper.COLUMN_MEASUREMENT_DURAION;
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
				+ QUESTION;
		}
		
		return updateMultiplySQLValues;
	}	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {		
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(measurementSetup.getParameterSet().getId()) + COMMA
			+ DatabaseIdentifier.toSQLString((criteriaSet != null) ? criteriaSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((thresholdSet != null) ? thresholdSet.getId() : null) + COMMA
			+ DatabaseIdentifier.toSQLString((etalon != null) ? etalon.getId() : null) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE + COMMA
			+ Long.toString(measurementSetup.getMeasurementDuration());
		return values;
	}
	
	
	protected int  setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		MeasurementSetup measurementSetup = this.fromStorableObject(storableObject);
		Set criteriaSet = measurementSetup.getCriteriaSet();
		Set thresholdSet = measurementSetup.getThresholdSet();
		Set etalon = measurementSetup.getEtalon();
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, measurementSetup.getParameterSet().getId()); 
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (criteriaSet != null) ? criteriaSet.getId() : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (thresholdSet != null) ? thresholdSet.getId() : null);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, (etalon != null) ? etalon.getId() : null);
			DatabaseString.setString(preparedStatement, ++i, measurementSetup.getDescription(), SIZE_DESCRIPTION_COLUMN);
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
				+ MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + COMMA 
				+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID
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
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementSetup measurementSetup = (storableObject == null) ? 
				new MeasurementSetup(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID), null, 0L, null, null, 
									   null, null, null, 0, null) : 
					this.fromStorableObject(storableObject);	
		Set parameterSet;
		Set criteriaSet;
		Set thresholdSet;
		Set etalon;
		Identifier id;
		try {
			parameterSet = (Set)MeasurementStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID), true);			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_CRITERIA_SET_ID);
			criteriaSet = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_THRESHOLD_SET_ID);
			thresholdSet = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;			
			id = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.COLUMN_ETALON_ID);
			etalon = (id != null) ? (Set)MeasurementStorableObjectPool.getStorableObject(id, true) : null;
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}

		String description = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION));
		measurementSetup.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
									   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
									   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
									   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
									   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
									   parameterSet,
									   criteriaSet,
									   thresholdSet,
									   etalon,
									   (description != null) ? description : "",
									   resultSet.getLong(MeasurementSetupWrapper.COLUMN_MEASUREMENT_DURAION));
		return measurementSetup;
	}

	private void retrieveMeasurementSetupMELinks(MeasurementSetup measurementSetup) throws RetrieveObjectException {
		List meIds = new ArrayList();

		String msIdStr = DatabaseIdentifier.toSQLString(measurementSetup.getId());
		String sql = SQL_SELECT
			+ MeasurementSetupWrapper.LINK_COLUMN_ME_ID
			+ SQL_FROM + ObjectEntities.MSMELINK_ENTITY
			+ SQL_WHERE + MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + EQUALS + msIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementSetupDatabase.retrieveMeasurementSetupMELinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				meIds.add(DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.LINK_COLUMN_ME_ID));
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
				DatabaseConnection.releaseConnection(connection);
			}
		}
		measurementSetup.setMonitoredElementIds0(meIds);
	}
    
    private void retrieveMeasurementSetupMELinksByOneQuery(Collection measurementSetups) throws RetrieveObjectException {
    	if ((measurementSetups == null) || (measurementSetups.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + MeasurementSetupWrapper.LINK_COLUMN_ME_ID + COMMA
                + MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID
                + SQL_FROM + ObjectEntities.MSMELINK_ENTITY
                + SQL_WHERE + MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID
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
                    sql.append(MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);
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
                Identifier measurementSetupId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID);
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
                
                Identifier meId = DatabaseIdentifier.getIdentifier(resultSet, MeasurementSetupWrapper.LINK_COLUMN_ME_ID);
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
                measurementSetup.setMonitoredElementIds0(meIds);
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
                DatabaseConnection.releaseConnection(connection);
            }
        }
    }

	private void setModified(MeasurementSetup measurementSetup) throws UpdateObjectException {
		String msIdStr = measurementSetup.getId().toString();
		String sql = SQL_UPDATE
				+ ObjectEntities.MS_ENTITY
				+ SQL_SET
				+ StorableObjectWrapper.COLUMN_MODIFIED + EQUALS + DatabaseDate.toUpdateSubString(measurementSetup.getModified()) + COMMA
				+ StorableObjectWrapper.COLUMN_MODIFIER_ID + EQUALS + measurementSetup.getModifierId().toString()
				+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + msIdStr;
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
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}	
	
	public Collection retrieveAll() throws RetrieveObjectException {
		try{
			return this.retrieveByIds(null, null);
		}
		catch(IllegalDataException ide){
			throw new RetrieveObjectException(ide);
		}
	}
	
	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection objects = null; 
		if ((ids == null) || (ids.isEmpty()))
			objects = this.retrieveByIdsOneQuery(null, condition);
		else
			objects = this.retrieveByIdsOneQuery(ids, condition);
		
		this.retrieveMeasurementSetupMELinksByOneQuery(objects);
		
		
		return objects;	
	}

//	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
//		List list = null;
//		
//		String condition = StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET
//				+ SQL_SELECT + MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + SQL_FROM + ObjectEntities.MSMELINK_ENTITY
//				+ SQL_WHERE + MeasurementSetupWrapper.LINK_COLUMN_ME_ID + SQL_IN + OPEN_BRACKET
//					+ SQL_SELECT + StorableObjectWrapper.COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
//					+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId())
//					+ CLOSE_BRACKET
//				+ CLOSE_BRACKET;		
//		try {
//			list = retrieveButIds(ids, condition);
//		}  catch (IllegalDataException ide) {			
//			Log.debugMessage("MeasurementSetupDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
//		}
//		
//		return list;
//	}

//	private List retrieveButIdsByMeasurementType(List ids, List measurementTypeIds) throws RetrieveObjectException {
//		List list = null;
//		
//		int i=1;
//		StringBuffer mtIdsStr = new StringBuffer();
//        for (Iterator it = measurementTypeIds.iterator(); it.hasNext();i++) {
//       		 Identifier id = (Identifier) it.next();
//       		 mtIdsStr.append( DatabaseIdentifier.toSQLString(id) );
//       		 if (it.hasNext()){
//	       		 if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
//	       		 	mtIdsStr.append(COMMA);
//	       		 else {
//	                mtIdsStr.append(CLOSE_BRACKET);
//	                mtIdsStr.append(SQL_AND);
//                    mtIdsStr.append(SQL_OR);
//	                mtIdsStr.append(MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID);
//	                mtIdsStr.append(SQL_IN);
//	                mtIdsStr.append(OPEN_BRACKET);
//	       		 }  
//       		 }
//       	}
//		
//		String condition = MeasurementSetupWrapper.COLUMN_PARAMETER_SET_ID + SQL_IN + OPEN_BRACKET		
//							+ SQL_SELECT + StorableObjectWrapper.COLUMN_ID + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
//							+ SQL_WHERE + StorableObjectWrapper.COLUMN_TYPE_ID + SQL_IN + OPEN_BRACKET			
//								+ SQL_SELECT + StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID
//								+ SQL_FROM + ObjectEntities.MNTTYPPARTYPLINK_ENTITY + SQL_WHERE
//								+ MeasurementTypeWrapper.LINK_COLUMN_MEASUREMENT_TYPE_ID 
//								+ SQL_IN
//								+ OPEN_BRACKET
//								+ mtIdsStr.toString()
//								+ CLOSE_BRACKET
//							+ CLOSE_BRACKET							
//			+ CLOSE_BRACKET;
//		
//		
//		try {
//			list = retrieveButIds(ids, condition);
//		}  catch (IllegalDataException ide) {			
//			Log.debugMessage("MeasurementSetupDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
//		}
//		
//		return list;
//	}

//	private List retrieveButIdsByMonitoredElement(List ids, List monitoredElementIds) throws RetrieveObjectException {
//		if (monitoredElementIds == null || monitoredElementIds.isEmpty())
//			return Collections.EMPTY_LIST;
//		List list = null;
//		
//		int i=1;
//		StringBuffer meIdsStr = new StringBuffer();
//        for (Iterator it = monitoredElementIds.iterator(); it.hasNext();i++) {
//       		 Identifier id = (Identifier) it.next();
//       		 meIdsStr.append( DatabaseIdentifier.toSQLString(id) );
//       		 if (it.hasNext()){
//	       		 if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
//	       		 	meIdsStr.append(COMMA);
//	       		 else {
//	                meIdsStr.append(CLOSE_BRACKET);
//	                meIdsStr.append(SQL_OR);
//                    meIdsStr.append(SQL_IN);
//	                meIdsStr.append(MeasurementSetupWrapper.LINK_COLUMN_ME_ID);
//	                meIdsStr.append(SQL_IN);
//	                meIdsStr.append(OPEN_BRACKET);
//	       		 }  
//       		 }
//       	}
//        
//		String condition = StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET
//					+ SQL_SELECT + MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + SQL_FROM + ObjectEntities.MSMELINK_ENTITY
//					+ SQL_WHERE + MeasurementSetupWrapper.LINK_COLUMN_ME_ID + SQL_IN 
//					+ OPEN_BRACKET
//					+ meIdsStr.toString()
//					+ CLOSE_BRACKET
//				+ CLOSE_BRACKET;		
//		
//		try {
//			list = retrieveButIds(ids, condition);
//		}  catch (IllegalDataException ide) {			
//			Log.debugMessage("MeasurementSetupDatabase.retrieveButIdsByMonitoredElement | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
//		}
//		
//		return list;
//	}

//    private List retrieveButIdMeasurementIds(List ids, List measurementIds) throws RetrieveObjectException, IllegalDataException {
//    	
//    	if (measurementIds != null && !measurementIds.isEmpty()){
//	        StringBuffer measurementIdsStr = new StringBuffer();
//	        
//	       	int i=1;
//	        for (Iterator it = measurementIds.iterator(); it.hasNext();i++) {
//	       		 Identifier id = (Identifier) it.next();
//	       		 measurementIdsStr.append( DatabaseIdentifier.toSQLString(id) );
//	       		 if (it.hasNext()){
//		       		 if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
//		       		 	measurementIdsStr.append(COMMA);
//		       		 else {
//		                measurementIdsStr.append(CLOSE_BRACKET);
//		                measurementIdsStr.append(SQL_AND);
//                        measurementIdsStr.append(NOT);
//                        measurementIdsStr.append(SQL_IN);
//		                measurementIdsStr.append(MeasurementSetupWrapper.LINK_COLUMN_ME_ID);
//		                measurementIdsStr.append(SQL_IN);
//		                measurementIdsStr.append(OPEN_BRACKET);
//		       		 }  
//	       		 }
//	       	}
//	        
//	        String condition = StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET	
//	                        + SQL_SELECT + MeasurementSetupWrapper.LINK_COLUMN_MEASUREMENT_SETUP_ID + SQL_FROM + ObjectEntities.MSMELINK_ENTITY
//	                        + SQL_WHERE + MeasurementSetupWrapper.LINK_COLUMN_ME_ID + NOT + SQL_IN + OPEN_BRACKET + measurementIdsStr.toString() 
//	                        + CLOSE_BRACKET
//	                    + CLOSE_BRACKET;        
//	        return retrieveButIds(ids , condition);
//    	}
//    	return Collections.EMPTY_LIST;
//    }
}
