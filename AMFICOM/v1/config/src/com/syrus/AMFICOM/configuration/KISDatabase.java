/*
 * $Id: KISDatabase.java,v 1.30 2004/11/04 13:33:04 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.30 $, $Date: 2004/11/04 13:33:04 $
 * @author $Author: max $
 * @module configuration_v1
 */

public class KISDatabase extends StorableObjectDatabase {
	// table :: kis
  // description VARCHAR2(256),
  public static final String COLUMN_DESCRIPTION   = "description";
	// name VARCHAR2(64) NOT NULL,
	public static final String COLUMN_NAME  = "name";
	// equipment_id Identifier NOT NULL
	public static final String COLUMN_EQUIPMENT_ID = "equipment_id";
	// mcm_id Identifier NOT NULL
	public static final String COLUMN_MCM_ID = "mcm_id";
	
    public static final String COLUMN_TYPE_ID       = "type_id";
    
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private String updateColumns;
	private String updateMultiplySQLValues;
	
	private KIS fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof KIS)
			return (KIS) storableObject;
		throw new IllegalDataException("KISDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.KIS_ENTITY;
	}
	
	protected String getTableName() {
		return ObjectEntities.KIS_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
                + COLUMN_TYPE_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_EQUIPMENT_ID + COMMA
				+ COLUMN_MCM_ID;
		}
		return this.updateColumns;
	}
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
                + QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		KIS kis = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ kis.getDomainId().toSQLString() + COMMA
	        + kis.getType().getId().toSQLString() + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getName()) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(kis.getDescription()) + APOSTOPHE + COMMA
			+ kis.getEquipmentId().toSQLString() + COMMA
			+ kis.getMCMId().toSQLString();
		return sql;
	}
	
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		super.retrieveEntity(kis);
		this.retrieveKISMeasurementPortIds(kis);
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ DomainMember.COLUMN_DOMAIN_ID + COMMA
	        + COLUMN_TYPE_ID + COMMA
			+ COLUMN_NAME + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_EQUIPMENT_ID + COMMA
			+ COLUMN_MCM_ID
			+ SQL_FROM + ObjectEntities.KIS_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}

	protected int setEntityForPreparedStatement(StorableObject storableObject,
			PreparedStatement preparedStatement) throws IllegalDataException,
			UpdateObjectException {
		KIS kis = fromStorableObject(storableObject);
        int i;
        try {
            Identifier equipmentId = kis.getEquipmentId();
            Identifier mcmId = kis.getMCMId();
            i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
            preparedStatement.setString( ++i , kis.getDomainId().getCode());
            preparedStatement.setString( ++i, kis.getType().getId().getCode());            
            preparedStatement.setString( ++i, kis.getName());
            preparedStatement.setString( ++i, kis.getDescription());
            preparedStatement.setString( ++i, equipmentId != null ? equipmentId.getCode() : "");
            preparedStatement.setString( ++i, mcmId != null ? mcmId.getCode() : "");            
        } catch (SQLException sqle) {
            throw new UpdateObjectException("KISDatabase." +
                    "setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
        }
        return i;
	}
    
    protected StorableObject updateEntityFromResultSet(
			StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		KIS kis = storableObject == null ? null : fromStorableObject(storableObject);
		if (kis == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			kis = new KIS(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null, null, null, null);			
		}
		KISType kisType;
        try {
            kisType = (KISType)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
        }
        catch (ApplicationException ae) {
            throw new RetrieveObjectException(ae);
        }
        kis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
							DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
							/**
								* @todo when change DB Identifier model ,change getString() to getLong()
								*/
							new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
							/**
								* @todo when change DB Identifier model ,change getString() to getLong()
								*/
							new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
							/**
								* @todo when change DB Identifier model ,change getString() to getLong()
								*/
							new Identifier(resultSet.getString(DomainMember.COLUMN_DOMAIN_ID)),													
							DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
							DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
                            kisType,
							/**
								* @todo when change DB Identifier model ,change getString() to getLong()
								*/
							new Identifier(resultSet.getString(COLUMN_EQUIPMENT_ID)),
							/**
								* @todo when change DB Identifier model ,change getString() to getLong()
								*/
							new Identifier(resultSet.getString(COLUMN_MCM_ID)));
		
		return kis;
	}


	private void retrieveKISMeasurementPortIds(KIS kis) throws RetrieveObjectException {
		List measurementPortIds = new ArrayList();

		String kisIdStr = kis.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
			+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID + EQUALS + kisIdStr;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveKISMeasurementPortIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				measurementPortIds.add(new Identifier(resultSet.getString(COLUMN_ID)));
			}
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveKISMeasurementPortIds | Cannot retrieve measurement port ids for kis " + kisIdStr;
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
		kis.setMeasurementPortIds(measurementPortIds);
	}

	public Object retrieveObject(StorableObject storableObject, int retrieve_kind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		switch (retrieve_kind) {
			case KIS.RETRIEVE_MONITORED_ELEMENTS:
				return this.retrieveMonitoredElements(kis);
			default:
				return null;
		}
	}
    
    private void retrieveKISMeasurementPortIdsByOneQuery(List kiss) throws RetrieveObjectException {
    	if ((kiss == null) || (kiss.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + COLUMN_ID + COMMA
                + MeasurementPortDatabase.COLUMN_KIS_ID
                + SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
                + SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = kiss.iterator(); it.hasNext();i++) {
            KIS kis = (KIS)it.next();
            sql.append(kis.getId().toSQLString());
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
            Log.debugMessage("KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map mpIdMap = new HashMap();
            while (resultSet.next()) {
                KIS kis = null;
                String kisId = resultSet.getString(MeasurementPortDatabase.COLUMN_KIS_ID);
                for (Iterator it = kiss.iterator(); it.hasNext();) {
                    KIS kisToCompare = (KIS) it.next();
                    if (kisToCompare.getId().getIdentifierString().equals(kisId)){
                        kis = kisToCompare;
                        break;
                    }                   
                }
                
                if (kis == null){
                    String mesg = "KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Cannot found correspond result for '" + kisId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                
                /**
                 * @todo when change DB Identifier model ,change getString() to
                 *       getLong()
                 */
                Identifier mpId = new Identifier(resultSet.getString(COLUMN_ID));
                List mpIds = (List)mpIdMap.get(kis);
                if (mpIds == null){
                    mpIds = new LinkedList();
                    mpIdMap.put(kis, mpIds);
                }               
                mpIds.add(mpId);              
            }
            
            for (Iterator iter = kiss.iterator(); iter.hasNext();) {
                KIS kis = (KIS) iter.next();
                List mpIds = (List)mpIdMap.get(kis);
                kis.setMeasurementPortIds(mpIds);
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

	private List retrieveMonitoredElements(KIS kis) throws RetrieveObjectException {
		List monitoredElements = new ArrayList();

		String kisIdStr = kis.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.ME_ENTITY
			+ SQL_WHERE + MonitoredElementDatabase.COLUMN_MEASUREMENT_PORT_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
				+ SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID + EQUALS + kisIdStr
			+ CLOSE_BRACKET;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			Log.debugMessage("KISDatabase.retrieveMonitoredElements | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				try {
					monitoredElements.add((MonitoredElement)ConfigurationStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_ID)), true));
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "KISDatabase.retrieveMonitoredElements | Cannot retrieve monitored elements for kis " + kisIdStr;
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
		return monitoredElements;
	}
    
    private void retrieveMonitoredElementsByOneQuery(List kiss) throws RetrieveObjectException {
    	if ((kiss == null) || (kiss.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + COLUMN_ID + COMMA
                + MeasurementPortDatabase.COLUMN_KIS_ID
                + SQL_FROM + ObjectEntities.MEASUREMENTPORT_ENTITY
                + SQL_WHERE + MeasurementPortDatabase.COLUMN_KIS_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = kiss.iterator(); it.hasNext();i++) {
            KIS kis = (KIS)it.next();
            sql.append(kis.getId().toSQLString());
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
            Log.debugMessage("KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map mpIdMap = new HashMap();
            while (resultSet.next()) {
                KIS kis = null;
                String kisId = resultSet.getString(MeasurementPortDatabase.COLUMN_KIS_ID);
                for (Iterator it = kiss.iterator(); it.hasNext();) {
                    KIS kisToCompare = (KIS) it.next();
                    if (kisToCompare.getId().getIdentifierString().equals(kisId)){
                        kis = kisToCompare;
                        break;
                    }                   
                }
                
                if (kis == null){
                    String mesg = "KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Cannot found correspond result for '" + kisId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                
                /**
                 * @todo when change DB Identifier model ,change getString() to
                 *       getLong()
                 */
                Identifier mpId = new Identifier(resultSet.getString(COLUMN_ID));
                List mpIds = (List)mpIdMap.get(kis);
                if (mpIds == null){
                    mpIds = new LinkedList();
                    mpIdMap.put(kis, mpIds);
                }               
                mpIds.add(mpId);              
            }
            
            for (Iterator iter = kiss.iterator(); iter.hasNext();) {
                KIS kis = (KIS) iter.next();
                List mpIds = (List)mpIdMap.get(kis);
                kis.setMeasurementPortIds(mpIds);
            }
            
        } catch (SQLException sqle) {
            String mesg = "KISDatabase.retrieveKISMeasurementPortIdsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		KIS kis = this.fromStorableObject(storableObject);
		super.insertEntity(kis);		
	}
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
	}	
	
	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		//TODO Check this method on errors		
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntity(storableObject, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntity(storableObject, false);
			break;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		//TODO Check this method on errors
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
	}
	
	public void delete(KIS kis) {
		String kisIdStr = kis.getId().toSQLString();
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
        try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
				+ ObjectEntities.KIS_ENTITY
				+ SQL_WHERE + COLUMN_ID + EQUALS + kisIdStr;
			Log.debugMessage("KISDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if(statement != null)
					statement.close();
				statement = null;
			}
			catch(SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
                DatabaseConnection.closeConnection(connection);
            }
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
        List list = null;
        try {
            list = retrieveByIds(null, null);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("KISDatabase.retrieveAll | Trying: " + ide, Log.DEBUGLEVEL09);
            throw new RetrieveObjectException(ide);
        }
        return list;
    }
	
	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retrieveByIdsOneQuery(null, condition);
		else 
			list = super.retrieveByIdsOneQuery(ids, condition);
		
        if (list != null) {
            retrieveKISMeasurementPortIdsByOneQuery(list);
    		retrieveMonitoredElementsByOneQuery(list);		
        }
        
		return list;
	}
	
	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + domain.getId().toSQLString();
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("KISDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list;
		if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		} else {
			Log.errorMessage("KISDatabase.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
