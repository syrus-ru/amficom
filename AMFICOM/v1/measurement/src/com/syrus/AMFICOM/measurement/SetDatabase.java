/*
 * $Id: SetDatabase.java,v 1.34 2004/11/03 12:04:52 bob Exp $
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;

import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
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
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.34 $, $Date: 2004/11/03 12:04:52 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class SetDatabase extends StorableObjectDatabase {
	
	public static final String	COLUMN_SORT			= "sort";
	public static final String	COLUMN_DESCRIPTION	= "description";	
	public static final String LINK_COLUMN_SET_ID	= "set_id";
	public static final String LINK_COLUMN_ME_ID 	= "monitored_element_id";
	public static final String LINK_COLUMN_TYPE_ID	= "type_id";
	public static final String LINK_COLUMN_VALUE	= "value";
	
    public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
    
	private String updateColumns;
	private String updateMultiplySQLValues;   
    
	protected String getEnityName() {		
		return "Set";
	}
	
	protected String getTableName() {
		return ObjectEntities.SET_ENTITY;
	}
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
				+ COLUMN_SORT  + COMMA
				+ COLUMN_DESCRIPTION;
		}
		return this.updateColumns;
	}
	
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION  + COMMA
				+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		Set set = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ Integer.toString(set.getSort().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(set.getDescription()) + APOSTOPHE;
		return values;
	}
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		Set set = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setInt(++i, set.getSort().value());
			preparedStatement.setString(++i, set.getDescription());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	private Set fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Set)
			return (Set)storableObject;
		throw new IllegalDataException("SetDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Set set = this.fromStorableObject(storableObject);
		this.retrieveEntity(set);
		this.retrieveSetParameters(set);
		this.retrieveSetMELinks(set);
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
			+ COLUMN_SORT + COMMA
			+ COLUMN_DESCRIPTION 
			+ SQL_FROM 
			+ ObjectEntities.SET_ENTITY
			+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);
	}	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Set set = (storableObject == null) ?
				new Set(new Identifier(resultSet.getString(COLUMN_ID)), null, 0, null, null, null) :			
				fromStorableObject(storableObject);
		String description = DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION));
		set.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
						  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
						  /**
							* @todo when change DB Identifier model ,change getString() to getLong()
							*/
						  new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
						  /**
						    * @todo when change DB Identifier model ,change getString() to getLong()
							*/
						  new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
						  resultSet.getInt(COLUMN_SORT),
						  (description != null) ? description : "");
		return set;
	}


	private void retrieveSetParameters(Set set) throws RetrieveObjectException {
		List parameters = new ArrayList();

		String setIdStr = set.getId().toSQLString();
		String sql = SQL_SELECT
			+ COLUMN_ID + COMMA			
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_VALUE
			+ SQL_FROM
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_SET_ID +EQUALS
			+ setIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSetParameters | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			SetParameter parameter;
			ParameterType parameterType;
			while (resultSet.next()) {
				try {
					/**
					 * @todo when change DB Identifier model ,change getString() to getLong()
					 */
					parameterType = (ParameterType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(LINK_COLUMN_TYPE_ID)), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				parameter = new SetParameter(/**
				 							  * @todo when change DB Identifier model ,change getString() to getLong()
			 								  */
											 new Identifier(resultSet.getString(COLUMN_ID)),
											 /**
											   * @todo when change DB Identifier model ,change getString() to getLong()
											   */
											 parameterType,
											 ByteArrayDatabase.toByteArray((BLOB)resultSet.getBlob(LINK_COLUMN_VALUE)));
				parameters.add(parameter);
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetParameters | Cannot retrieve parameters for set '" + setIdStr + "' -- " + sqle.getMessage();
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
		set.setParameters((SetParameter[])parameters.toArray(new SetParameter[parameters.size()]));
	}
    
	private void retrieveSetParametersByOneQuery(List sets) throws RetrieveObjectException {
		
        if ((sets == null) || (sets.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + COLUMN_ID + COMMA         
                + LINK_COLUMN_TYPE_ID + COMMA
                + LINK_COLUMN_VALUE + COMMA
                + LINK_COLUMN_SET_ID 
                + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
                + SQL_WHERE + LINK_COLUMN_SET_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = sets.iterator(); it.hasNext();i++) {
            Set set = (Set)it.next();
            sql.append(set.getId().toSQLString());
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
            Log.debugMessage("SetDatabase.retrieveSetParametersByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            SetParameter parameter;
            ParameterType parameterType;
            Map setParametersMap = new HashMap();
            while (resultSet.next()) {
                Set set = null;
                String setId = resultSet.getString(LINK_COLUMN_SET_ID);
                for (Iterator it = sets.iterator(); it.hasNext();) {
                    Set setToCompare = (Set) it.next();
                    if (setToCompare.getId().getIdentifierString().equals(setId)){
                        set = setToCompare;
                        break;
                    }                   
                }
                
                if (set == null){
                    String mesg = "SetDatabase.retrieveSetParametersByOneQuery | Cannot found correspond result for '" + setId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                try {
                    /**
                     * @todo when change DB Identifier model
                     *       ,change getString() to
                     *       getLong()
                     */
                    parameterType = (ParameterType) MeasurementStorableObjectPool
                            .getStorableObject(new Identifier(resultSet
                                    .getString(LINK_COLUMN_TYPE_ID)), true);
                } catch (ApplicationException ae) {
                    throw new RetrieveObjectException(ae);
                }
                parameter = new SetParameter(
                		/**
                		 * @todo when change DB Identifier model, change
                		 *       getString() to getLong()
                		 */
                		new Identifier(resultSet.getString(COLUMN_ID)),
						/**
						 * @todo when change DB Identifier model ,change
						 *       getString() to getLong()
						 */
						parameterType, ByteArrayDatabase.toByteArray(resultSet
								.getBlob(LINK_COLUMN_VALUE)));
                List parameters = (List)setParametersMap.get(set);
                if (parameters == null){
                    parameters = new LinkedList();
                    setParametersMap.put(set, parameters);
                }               
                parameters.add(parameter);              
            }
            
            for (Iterator iter = sets.iterator(); iter.hasNext();) {
                Set set = (Set) iter.next();
                List parameters = (List)setParametersMap.get(set);
                set.setParameters((SetParameter[]) parameters.toArray(new SetParameter[parameters.size()]));
            }
            
        } catch (SQLException sqle) {
            String mesg = "SetDatabase.retrieveSetParametersByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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

	private void retrieveSetMELinks(Set set) throws RetrieveObjectException {
		List meIds = new ArrayList();

		String setIdStr = set.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_ME_ID
			+ SQL_FROM + ObjectEntities.SETMELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_SET_ID + EQUALS
			+ setIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.retrieveSetMELinks | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				meIds.add(new Identifier(resultSet.getString(LINK_COLUMN_ME_ID)));
			}
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.retrieveSetMELinks | Cannot retrieve monitored element ids for set '" + setIdStr + "' -- " + sqle.getMessage();
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
		set.setMonitoredElementIds(meIds);
	}
    
    private void retrieveSetMELinksByOneQuery(List sets) throws RetrieveObjectException {
    	if ((sets == null) || (sets.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLUMN_ME_ID + COMMA
                + LINK_COLUMN_SET_ID
                + SQL_FROM + ObjectEntities.SETMELINK_ENTITY
                + SQL_WHERE + LINK_COLUMN_SET_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = sets.iterator(); it.hasNext();i++) {
            Set set = (Set)it.next();
            sql.append(set.getId().toSQLString());
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
            Log.debugMessage("SetDatabase.retrieveSetMELinksByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map meLinkMap = new HashMap();
            while (resultSet.next()) {
                Set set = null;
                String setId = resultSet.getString(LINK_COLUMN_SET_ID);
                for (Iterator it = sets.iterator(); it.hasNext();) {
                    Set setToCompare = (Set) it.next();
                    if (setToCompare.getId().getIdentifierString().equals(setId)){
                        set = setToCompare;
                        break;
                    }                   
                }
                
                if (set == null){
                    String mesg = "SetDatabase.retrieveSetMELinksByOneQuery | Cannot found correspond result for '" + setId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                
                Identifier meId = new Identifier(resultSet.getString(LINK_COLUMN_ME_ID));
                    
                          /**
                     * @todo when change DB Identifier model
                     *       ,change getString() to
                     *       getLong()
                     */
                List meIds = (List)meLinkMap.get(set);
                if (meIds == null){
                    meIds = new LinkedList();
                    meLinkMap.put(set, meIds);
                }               
                meIds.add(meId);              
            }
            
            for (Iterator iter = sets.iterator(); iter.hasNext();) {
                Set set = (Set) iter.next();
                List meIds = (List)meLinkMap.get(set);
                set.setMonitoredElementIds(meIds);
            }
            
        } catch (SQLException sqle) {
            String mesg = "SetDatabase.retrieveSetMELinksByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
            throw new RetrieveObjectException(mesg, sqle);
        } catch (ApplicationException ae) {
            throw new RetrieveObjectException(ae);
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
		Set set = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		Set set = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(set);
			this.insertSetParameters(set);
			this.insertSetMELinks(set);
		}
		catch (CreateObjectException coe) {
			this.delete(set);
			throw coe;
		}
	}
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Set set = (Set) it.next();
			this.insertSetParameters(set);
			this.insertSetMELinks(set);			
		}

	}

	private void insertSetParameters(Set set) throws CreateObjectException {
		String setIdStr = set.getId().toString();
		SetParameter[] setParameters = set.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID  + COMMA
			+ LINK_COLUMN_TYPE_ID + COMMA
			+ LINK_COLUMN_SET_ID + COMMA
			+ LINK_COLUMN_VALUE + CLOSE_BRACKET
			+ SQL_VALUES 
			+ OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		int i = 0;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < setParameters.length; i++) {
				parameterId = setParameters[i].getId();
				parameterTypeId = setParameters[i].getType().getId();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(1, parameterId.getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeId.getCode());
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(3, set.getId().getCode());
				preparedStatement.setBlob(4, BLOB.empty_lob());
				Log.debugMessage("SetDatabase.insertSetParameters | Inserting parameter " + parameterTypeId.toString() + " for set " + setIdStr, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
//				ByteArrayDatabase badb = new ByteArrayDatabase(setParameters[i].getValue());
				ByteArrayDatabase.saveAsBlob(setParameters[i].getValue(),
																		 connection,
																		 ObjectEntities.SETPARAMETER_ENTITY,
																		 LINK_COLUMN_VALUE,
																		 COLUMN_ID + EQUALS + parameterId.toSQLString());
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetParameters | Cannot insert parameter '" + parameterId.toString() + "' of type '" + parameterTypeId.toString() + "' for set '" + setIdStr + "' -- " + sqle.getMessage();
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

	private void insertSetMELinks(Set set) throws CreateObjectException {
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String setIdCode = set.getId().getCode();
		List meIds = set.getMonitoredElementIds();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLUMN_SET_ID + COMMA 
			+ LINK_COLUMN_ME_ID 
			+ CLOSE_BRACKET
			+ SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION 
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String meIdCode = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = meIds.iterator(); iterator.hasNext();) {
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(1, setIdCode);
				meIdCode = ((Identifier)iterator.next()).getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, meIdCode);
				Log.debugMessage("SetDatabase.insertSetMELinks | Inserting link for set " + setIdCode + " and monitored element " + meIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.insertSetMELinks | Cannot insert link for monitored element '" + meIdCode + "' and set '" + setIdCode + "' -- " + sqle.getMessage();
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
			}  finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Set set = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case Set.UPDATE_ATTACH_ME:
				this.createMEAttachment(set, (Identifier)obj);
				this.setModified(set);
				break;
			case Set.UPDATE_DETACH_ME:
				this.deleteMEAttachment(set, (Identifier)obj);
				this.setModified(set);
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
			case Set.UPDATE_ATTACH_ME:
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					Set set = (Set) it.next();
					this.createMEAttachment(set, (Identifier)obj);
					this.setModified(set);
				}
				break;
			case Set.UPDATE_DETACH_ME:
				for (Iterator it = storableObjects.iterator(); it.hasNext();) {
					Set set = (Set) it.next();
					this.deleteMEAttachment(set, (Identifier)obj);
					this.setModified(set);
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

	private void createMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = set.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.SETMELINK_ENTITY
			+ OPEN_BRACKET
			+ LINK_COLUMN_SET_ID + COMMA
			+ LINK_COLUMN_ME_ID
			+ CLOSE_BRACKET
			+ SQL_VALUES
			+ OPEN_BRACKET			
			+ setIdStr + COMMA
			+ meIdStr
			+ CLOSE_BRACKET;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("Set.createMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "Set.createMEAttachment | Cannot attach set '" + setIdStr + "' to monitored element '" + meIdStr + "' -- " + sqle.getMessage();
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

	private void deleteMEAttachment(Set set, Identifier monitoredElementId) throws UpdateObjectException {
		String setIdStr = set.getId().toSQLString();
		String meIdStr = monitoredElementId.toSQLString();
		String sql = SQL_DELETE_FROM 
					+ ObjectEntities.SETMELINK_ENTITY
					+ SQL_WHERE 
					+ LINK_COLUMN_SET_ID + EQUALS
					+ setIdStr
					+ SQL_AND
					+ LINK_COLUMN_ME_ID + EQUALS
					+ meIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.deleteMEAttachment | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.deleteMEAttachment | Cannot detach set '" + setIdStr + "' from monitored element '" + meIdStr + "' -- " + sqle.getMessage();
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

	private void setModified(Set set) throws UpdateObjectException {
		String setIdStr = set.getId().toSQLString();
		String sql = SQL_UPDATE
					+ ObjectEntities.SET_ENTITY
					+ SQL_SET
					+ COLUMN_MODIFIED + EQUALS
					+ DatabaseDate.toUpdateSubString(set.getModified()) + COMMA
					+ COLUMN_MODIFIER_ID + EQUALS + set.getModifierId().toSQLString()
					+ SQL_WHERE + EQUALS + setIdStr;
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("SetDatabase.setModified | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "SetDatabase.setModified | Cannot set modified for set '" + setIdStr + "' -- " + sqle.getMessage();
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

	public void delete(Set set) {
		String setIdStr = set.getId().toSQLString();
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
									+ ObjectEntities.SETMELINK_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_SET_ID + EQUALS
									+ setIdStr);
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SETPARAMETER_ENTITY
									+ SQL_WHERE
									+ LINK_COLUMN_SET_ID + EQUALS
									+ setIdStr);									
			statement.executeUpdate(SQL_DELETE_FROM 
									+ ObjectEntities.SET_ENTITY
									+ SQL_WHERE
									+ COLUMN_ID + EQUALS
									+ setIdStr);
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
		
		retrieveSetParametersByOneQuery(list);
		retrieveSetMELinksByOneQuery(list);
		
		
		return list;
	}
	
	private List retrieveButIdsByDomain(List ids, Domain domain) throws RetrieveObjectException {
        List list = null;
        
        String condition = COLUMN_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT + LINK_COLUMN_SET_ID + SQL_FROM + ObjectEntities.SETMELINK_ENTITY
				+ SQL_WHERE + LINK_COLUMN_ME_ID + SQL_IN + OPEN_BRACKET
                	+ SQL_SELECT + COLUMN_ID + SQL_FROM + ObjectEntities.ME_ENTITY + SQL_WHERE
					+ DomainMember.COLUMN_DOMAIN_ID + EQUALS + domain.getId().toSQLString()
					+ CLOSE_BRACKET
			+ CLOSE_BRACKET;
        
        try {
            list = retrieveButIds(ids, condition);
        }  catch (IllegalDataException ide) {           
            Log.debugMessage("MeasurementDatabase.retrieveButIdsByDomain | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
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
			Log.errorMessage("MeasurementDatabase.retrieveByCondition | Unknown condition class: " + condition);
			list = this.retrieveButIds(ids);
		}
		return list;
	}
}
