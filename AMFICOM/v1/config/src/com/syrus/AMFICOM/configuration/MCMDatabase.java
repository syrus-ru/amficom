/*
 * $Id: MCMDatabase.java,v 1.38 2004/12/10 15:39:32 bob Exp $
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
import java.util.Map;

import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.configuration.corba.CharacteristicSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.38 $, $Date: 2004/12/10 15:39:32 $
 * @author $Author: bob $
 * @module configuration_v1
 */

public class MCMDatabase extends StorableObjectDatabase {

	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_USER_ID = "user_id";
	public static final String COLUMN_SERVER_ID = "server_id";
	// tcp_port NUMBER(5,0),
	public static final String COLUMN_TCP_PORT  		= "tcp_port";
	//public static final String COLUMN_LOCATION = "location";
	//public static final String COLUMN_HOSTNAME = "hostname";
	private static String columns;
	private static String updateMultiplySQLValues;

	private MCM fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MCM)
			return (MCM) storableObject;
		throw new IllegalDataException("MCMDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.MCM_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
    		columns = super.getColumns(mode) + COMMA
				+ DomainMember.COLUMN_DOMAIN_ID + COMMA
				+ COLUMN_NAME + COMMA
				+ COLUMN_DESCRIPTION + COMMA
				+ COLUMN_USER_ID + COMMA
				+ COLUMN_SERVER_ID + COMMA
				+ COLUMN_TCP_PORT;
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
					+ QUESTION;
    	}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException, UpdateObjectException {
		MCM mcm = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getDomainId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getName(), 64) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(mcm.getDescription(), 256) + APOSTOPHE + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getUserId()) + COMMA
			+ DatabaseIdentifier.toSQLString(mcm.getServerId()); 
		return sql;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, UpdateObjectException {
		MCM mcm = fromStorableObject(storableObject);
		int i;
		try {
			i  = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getDomainId());
			DatabaseString.setString(preparedStatement, ++i, mcm.getName(), 64);
			DatabaseString.setString(preparedStatement, ++i, mcm.getDescription(), 256);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getUserId());
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, mcm.getServerId());
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException("MCMDatabase.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
		MCM mcm = this.fromStorableObject(storableObject);
		this.retrieveEntity(mcm);
		this.retrieveKISs(mcm);
		mcm.setCharacteristics0(characteristicDatabase.retrieveCharacteristics(mcm.getId(), CharacteristicSort.CHARACTERISTIC_SORT_MCM));
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		MCM mcm = storableObject == null ? null : fromStorableObject(storableObject);
		if (mcm == null){
			mcm = new MCM(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
																	 null,
																	 null,
																	 null,
																	 null,
																	 null,
																	 null,
																	 (short)0);			
		}
		mcm.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
						  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
						  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
						  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
						  DatabaseIdentifier.getIdentifier(resultSet, DomainMember.COLUMN_DOMAIN_ID),
						  DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_NAME)),
						  DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)),
						  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_USER_ID),
						  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_SERVER_ID),
						  resultSet.getShort(COLUMN_TCP_PORT));
		
		return mcm;
	}
		
	private void retrieveKISs(MCM mcm) throws RetrieveObjectException {
		List kiss = new ArrayList();
		String mcmIdStr = DatabaseIdentifier.toSQLString(mcm.getId());
		String sql = SQL_SELECT 
			+ COLUMN_ID
			+ SQL_FROM + ObjectEntities.KIS_ENTITY
			+ SQL_WHERE + KISDatabase.COLUMN_MCM_ID + EQUALS + mcmIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
    try {
			statement = connection.createStatement();
			Log.debugMessage("MCMDatabase.retrieveKISIds | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				kiss.add(ConfigurationStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), true));
		}
		catch (SQLException sqle) {
			String mesg = "MCMDatabase.retrieveKISIds | Cannot retrieve kis ids for mcm " + mcmIdStr;
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (DatabaseException e) {
			String mesg = "MCMDatabase.retrieveKISIds | Cannot retrieve kis ids for mcm " + mcmIdStr;
			throw new RetrieveObjectException(mesg, e);
		}
		catch (CommunicationException e) {
			String mesg = "MCMDatabase.retrieveKISIds | Cannot retrieve kis ids for mcm " + mcmIdStr;
			throw new RetrieveObjectException(mesg, e);
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
		mcm.setKISs0(kiss);
	}
    
    private void retrieveKISIdsByOneQuery(List mcms) throws RetrieveObjectException {
    	
    	if ((mcms == null) || (mcms.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + COLUMN_ID + COMMA
                + KISDatabase.COLUMN_MCM_ID
                + SQL_FROM + ObjectEntities.KIS_ENTITY
                + SQL_WHERE + KISDatabase.COLUMN_MCM_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = mcms.iterator(); it.hasNext();i++) {
            MCM mcm = (MCM)it.next();
            sql.append(DatabaseIdentifier.toSQLString(mcm.getId()));
            if (it.hasNext()){
                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
                    sql.append(COMMA);
                else {
                    sql.append(CLOSE_BRACKET);
                    sql.append(SQL_OR);
                    sql.append(KISDatabase.COLUMN_MCM_ID);
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
            Log.debugMessage("MCMDatabase.retrieveKISIdsByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map kisMap = new HashMap();
            while (resultSet.next()) {
                MCM mcm = null;
                Identifier mcmId = DatabaseIdentifier.getIdentifier(resultSet, KISDatabase.COLUMN_MCM_ID);
                for (Iterator it = mcms.iterator(); it.hasNext();) {
                    MCM mcmToCompare = (MCM) it.next();
                    if (mcmToCompare.getId().equals(mcmId)){
                        mcm = mcmToCompare;
                        break;
                    }                   
                }
                
                if (mcm == null){
                    String mesg = "MCMDatabase.retrieveKISIdsByOneQuery | Cannot found correspond result for '" + mcmId.getIdentifierString() +"'" ;
                    throw new RetrieveObjectException(mesg);
                }                    
                
                                
                Identifier kisId = null;
				try {
					kisId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
					KIS kis = (KIS)ConfigurationStorableObjectPool.getStorableObject(kisId, true);
					List kiss = (List)kisMap.get(mcm);
	                if (kiss == null){
	                    kiss = new LinkedList();
	                    kisMap.put(mcm, kiss);
	                }               
	                kiss.add(kis);
				} catch (DatabaseException e) {
					 String mesg = "MCMDatabase.retrieveKISIdsByOneQuery | Cannot retrieve kis " + ((kisId == null) ? "null" : kisId.getIdentifierString()) + " for result -- " + e.getMessage();
			         throw new RetrieveObjectException(mesg, e);
				} catch (CommunicationException e) {
					 String mesg = "MCMDatabase.retrieveKISIdsByOneQuery | Cannot retrieve kis " + ((kisId == null) ? "null" : kisId.getIdentifierString()) + " for result -- " + e.getMessage();
			         throw new RetrieveObjectException(mesg, e);
				}				              
            }
            
            for (Iterator iter = mcms.iterator(); iter.hasNext();) {
                MCM mcm = (MCM) iter.next();
                List kiss = (List)kisMap.get(mcm);
                mcm.setKISs0(kiss);
            }
            
        } catch (SQLException sqle) {
            String mesg = "MCMDatabase.retrieveKISIdsByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		MCM mcm = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		super.insertEntity(mcm);	
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(mcm);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}
	
	public void insert(List storableObjects) throws IllegalDataException,
			CreateObjectException {
		super.insertEntities(storableObjects);
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		try {
			characteristicDatabase.updateCharacteristics(storableObjects);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) 
			throws IllegalDataException, VersionCollisionException, 
			UpdateObjectException {
		MCM mcm = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_FORCE:
				super.checkAndUpdateEntity(mcm, true);
				break;
			case UPDATE_CHECK: 					
			default:
				super.checkAndUpdateEntity(mcm, false);
				break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(mcm);
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		switch (updateKind) {
		case UPDATE_FORCE:
			super.checkAndUpdateEntities(storableObjects, true);
			break;
		case UPDATE_CHECK: 					
		default:
			super.checkAndUpdateEntities(storableObjects, false);
			break;
		}
		CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.getCharacteristicDatabase());
		characteristicDatabase.updateCharacteristics(storableObjects);
	}
	
	public List retrieveByIds(List ids, String condition)
			throws IllegalDataException, RetrieveObjectException {
		List list = null;
		if ((ids == null) || (ids.isEmpty()))
			list = super.retrieveByIdsOneQuery(null, condition);
		else 
			list = super.retrieveByIdsOneQuery(ids, condition);
		
        if (list != null) {
            retrieveKISIdsByOneQuery(list);
            
            CharacteristicDatabase characteristicDatabase = (CharacteristicDatabase)(ConfigurationDatabaseContext.characteristicDatabase);
            Map characteristicMap = characteristicDatabase.retrieveCharacteristicsByOneQuery(list, CharacteristicSort.CHARACTERISTIC_SORT_MCM);
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                MCM mcm = (MCM) iter.next();
                List characteristics = (List)characteristicMap.get(mcm);
                mcm.setCharacteristics0(characteristics);
            }
        }
		return list;	
		//return retriveByIdsPreparedStatement(ids);
	}

	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = DomainMember.COLUMN_DOMAIN_ID + EQUALS + DatabaseIdentifier.toSQLString(domain.getId());
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage(this.getEnityName() + "Database.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
        }
        
        return list;
    }

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		if (condition instanceof DomainCondition){
			DomainCondition domainCondition = (DomainCondition)condition;
			list = this.retrieveButIdsByDomain(ids, domainCondition.getDomain());
		} else {
			Log.errorMessage(this.getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition.getClass().getName());
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
