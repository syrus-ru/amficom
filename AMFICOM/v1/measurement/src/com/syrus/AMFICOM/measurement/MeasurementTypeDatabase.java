/*
 * $Id: MeasurementTypeDatabase.java,v 1.40 2004/11/16 15:48:45 bob Exp $
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

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identified;
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
 * @version $Revision: 1.40 $, $Date: 2004/11/16 15:48:45 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class MeasurementTypeDatabase extends StorableObjectDatabase  {
	public static final String MODE_IN = "IN";
	public static final String MODE_OUT = "OUT";
	
	public static final String	COLUMN_CODENAME = "codename";
	public static final String	COLUMN_DESCRIPTION = "description";
	
	public static final String	LINK_COLUMN_MEASUREMENT_TYPE_ID = "measurement_type_id";
	public static final String	LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID = "measurement_port_type_id";
	
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private static String columns;
	private static String updateMultiplySQLValues;

	private MeasurementType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof MeasurementType)
			return (MeasurementType)storableObject;
		throw new IllegalDataException("MeasurementTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	
	
	protected String getEnityName() {
		return ObjectEntities.MEASUREMENTTYPE_ENTITY;
	}	
	
	protected String getColumns() {
		if (columns == null){
			columns = super.getColumns() + COMMA
				+ COLUMN_CODENAME + COMMA 
				+ COLUMN_DESCRIPTION;
		}
		return columns;
	}	

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null){
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA 
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		MeasurementType measurementType = fromStorableObject(storableObject);
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementType.getCodename()) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(measurementType.getDescription()) + APOSTOPHE;
		return sql;
	}	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.retrieveEntity(measurementType);
		this.retrieveParameterTypes(measurementType);
		this.retrieveMeasurementPortTypes(measurementType);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		MeasurementType measurementType = (storableObject == null) ? 
				new MeasurementType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null, null, null, 
									   null, null, null) : 
					fromStorableObject(storableObject);
		measurementType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									  DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									  DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									  DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
									  DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return measurementType;
	}

	private void retrieveParameterTypes(MeasurementType measurementType) throws RetrieveObjectException {
		List inParTyps = new ArrayList();
		List outParTyps = new ArrayList();

		String measurementTypeIdStr = DatabaseIdentifier.toSQLString(measurementType.getId());
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.MNTTYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveParameterType | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			Identifier parameterTypeIdCode;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				parameterTypeIdCode = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
				else
					if (parameterMode.equals(MODE_OUT))
						outParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
					else
						Log.errorMessage("MeasurementTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeIdCode);
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for measurement type '" + measurementTypeIdStr + "' -- " + sqle.getMessage();
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
		((ArrayList)inParTyps).trimToSize();
		((ArrayList)outParTyps).trimToSize();
		measurementType.setParameterTypes(inParTyps,
																			outParTyps);
	}
    
    private void retrieveParameterTypesByOneQuery(List measurementTypes) throws RetrieveObjectException {
    	if ((measurementTypes == null) || (measurementTypes.isEmpty()))
            return;
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
                + LINK_COLUMN_PARAMETER_MODE + COMMA
                + LINK_COLUMN_MEASUREMENT_TYPE_ID
                + SQL_FROM + ObjectEntities.MNTTYPPARTYPLINK_ENTITY
                + SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + SQL_IN + OPEN_BRACKET);
        int i=1;
        for (Iterator it = measurementTypes.iterator(); it.hasNext();i++) {
            MeasurementType measurementType = (MeasurementType)it.next();
            sql.append(DatabaseIdentifier.toSQLString(measurementType.getId()));
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
            Log.debugMessage("MeasurementTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            String parameterMode;
            Identifier parameterTypeId;
            Map inParametersMap = new HashMap();
            Map outParametersMap = new HashMap();
            while (resultSet.next()) {
                MeasurementType measurementType = null;
                Identifier measurementTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MEASUREMENT_TYPE_ID);
                for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
                    MeasurementType measurementTypeToCompare = (MeasurementType) it.next();
                    if (measurementTypeToCompare.getId().equals(measurementTypeId)){
                        measurementType = measurementTypeToCompare;
                        break;
                    }                   
                }
                
                if (measurementType == null){
                    String mesg = "MeasurementTypeDatabase.retrieveParameterTypesByOneQuery | Cannot found correspond result for '" + measurementTypeId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
                parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
                ParameterType parameterType; 
                if (parameterMode.equals(MODE_IN)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeId, true));
                    List inParameters = (List)inParametersMap.get(measurementType);
                    if (inParameters == null){
                        inParameters = new LinkedList();
                        inParametersMap.put(measurementType, inParameters);
                    }               
                    inParameters.add(parameterType);
                } else if (parameterMode.equals(MODE_OUT)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeId, true));
                    List outParameters = (List)outParametersMap.get(measurementType);
                    if (outParameters == null){
                        outParameters = new LinkedList();
                        outParametersMap.put(measurementType, outParameters);
                    }
                } else {
                    Log .errorMessage("MeasurementTypeDatabase.retrieveParameterTypesByOneQuery | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeId);
                }                
            }
            for (Iterator iter = measurementTypes.iterator(); iter.hasNext();) {
                MeasurementType measurementType = (MeasurementType) iter.next();
                List inParameterpTypes = (List)inParametersMap.get(measurementType);
                List outParameterpTypes = (List)inParametersMap.get(measurementType);
                
                measurementType.setParameterTypes(inParameterpTypes, outParameterpTypes);
            }            
        }
        catch (SQLException sqle) {
            String mesg = "MeasurementTypeDatabase.retrieveParameterTypesByOneQuery | Cannot retrieve parameter type ids for analysis types -- " + sqle.getMessage();
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
            } finally{
                DatabaseConnection.closeConnection(connection);
            }
        }  
    }
	
	private void retrieveMeasurementPortTypes(MeasurementType measurementType) throws RetrieveObjectException {
		List measurementPortTypes = new ArrayList();

		String measurementTypeIdStr = DatabaseIdentifier.toSQLString(measurementType.getId());
		String sql = SQL_SELECT
			+ LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID
			+ SQL_FROM + ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("MeasurementTypeDatabase.retrieveMeasurementPortTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			Identifier measurementPortTypeId;
			while (resultSet.next()) {
				measurementPortTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
				measurementPortTypes.add((MeasurementPortType) ConfigurationStorableObjectPool.getStorableObject(measurementPortTypeId, true));
			}
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.retrieveMeasurementPortTypes | Cannot retrieve measurement port type ids for measurement type '" + measurementTypeIdStr + "' -- " + sqle.getMessage();
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
			} finally{
				DatabaseConnection.closeConnection(connection);
			}
		}
		((ArrayList)measurementPortTypes).trimToSize();
		measurementType.setMeasurementPortTypes(measurementPortTypes);
	}
    
    private void retrieveMeasurementPortTypesByOneQuery(List measurementTypes) throws RetrieveObjectException {
    	if ((measurementTypes == null) || (measurementTypes.isEmpty()))
            return;     
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID + COMMA
                + LINK_COLUMN_MEASUREMENT_TYPE_ID
                + SQL_FROM + ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY
                + SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID
                + SQL_IN + OPEN_BRACKET);
        int i = 1;
        for (Iterator it = measurementTypes.iterator(); it.hasNext();i++) {
            MeasurementType measurementType = (MeasurementType)it.next();
            sql.append(DatabaseIdentifier.toSQLString(measurementType.getId()));
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
            Log.debugMessage("MeasurementDatabase.retrieveMeasurementPortTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            Map measurementPortTypeMap = new HashMap();
            while (resultSet.next()) {
                MeasurementType measurementType = null;
                Identifier measurementTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MEASUREMENT_TYPE_ID);
                for (Iterator it = measurementTypes.iterator(); it.hasNext();) {
                    MeasurementType measurementTypeToCompare = (MeasurementType) it.next();
                    if (measurementTypeToCompare.getId().equals(measurementTypeId)){
                        measurementType = measurementTypeToCompare;
                        break;
                    }                   
                }
                
                if (measurementType == null){
                    String mesg = "MeasurementDatabase.retrieveMeasurementPortTypesByOneQuery | Cannot found correspond result for '" + measurementTypeId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                
                Identifier measurementPortTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
                MeasurementPortType measurementPortType = (MeasurementPortType) ConfigurationStorableObjectPool.getStorableObject(measurementPortTypeId, true);
                List measurementPortTypes = (List)measurementPortTypeMap.get(measurementType);
                if (measurementPortTypes == null){
                    measurementPortTypes = new LinkedList();
                    measurementPortTypeMap.put(measurementType, measurementPortTypes);
                }               
                measurementPortTypes.add(measurementPortType);              
            }
            
            for (Iterator iter = measurementTypes.iterator(); iter.hasNext();) {
                MeasurementType measurementType = (MeasurementType) iter.next();
                List measurementPortTypes = (List)measurementPortTypeMap.get(measurementType);
                measurementType.setMeasurementPortTypes(measurementPortTypes);
            }
            
        } catch (SQLException sqle) {
            String mesg = "MeasurementDatabase.retrieveMeasurementPortTypesByOneQuery | Cannot retrieve parameters for result -- " + sqle.getMessage();
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
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws IllegalDataException, CreateObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		this.insertEntity(measurementType);
		this.insertParameterTypes(measurementType);
		this.insertMeasurementPortTypes(measurementType);
		
	}
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			MeasurementType measurementType = fromStorableObject((StorableObject)it.next());
			this.insertParameterTypes(measurementType);
			this.insertMeasurementPortTypes(measurementType);
		}

	}

	private PreparedStatement insertParameterTypesPreparedStatement() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
		String sql = SQL_INSERT_INTO
		+ ObjectEntities.MNTTYPPARTYPLINK_ENTITY + OPEN_BRACKET
		+ LINK_COLUMN_MEASUREMENT_TYPE_ID + COMMA
		+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
		+ LINK_COLUMN_PARAMETER_MODE
		+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
		+ QUESTION + COMMA
		+ QUESTION + COMMA
		+ QUESTION
		+ CLOSE_BRACKET;
		preparedStatement = connection.prepareStatement(sql);
		} finally{
			DatabaseConnection.closeConnection(connection);
		}
		return preparedStatement;
	}
	
	private PreparedStatement insertMeasurementPortTypePreparedStatement() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
		String sql = SQL_INSERT_INTO
		+ ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY + OPEN_BRACKET
		+ LINK_COLUMN_MEASUREMENT_TYPE_ID + COMMA
		+ LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID
		+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
		+ QUESTION + COMMA
		+ QUESTION
		+ CLOSE_BRACKET;
		preparedStatement = connection.prepareStatement(sql);
		} finally{
			DatabaseConnection.closeConnection(connection);
		}
		return preparedStatement;
	}
	
	private void updatePrepareStatementValues(PreparedStatement preparedStatement,MeasurementType measurementType) throws SQLException{
		List inParTyps = measurementType.getInParameterTypes();
		List outParTyps = measurementType.getOutParameterTypes();
		Identifier measurementTypeId = measurementType.getId();
		Identifier parameterTypeId = null;
		String parameterMode = null;
		
		for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, measurementTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);			
			parameterMode = MODE_IN;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("MeasurementTypeDatabase.insertParameterTypes | Inserting parameter type " + parameterTypeId + " of parameter mode '" + parameterMode + "' for measurement type " + measurementTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, measurementTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
			parameterMode = MODE_OUT;
			preparedStatement.setString(3, parameterMode);
			Log.debugMessage("MeasurementTypeDatabase.insertParameterTypes | Inserting parameter type '" + parameterTypeId + "' of parameter mode '" + parameterMode + "' for measurement type '" + measurementTypeId + "'", Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}

	}

	private void updateMeasurementPortTypePrepareStatementValues(PreparedStatement preparedStatement,MeasurementType measurementType) throws SQLException{
		List measurementPortTypes = measurementType.getMeasurementPortTypes();
		Identifier measurementTypeId = measurementType.getId();
		Identifier measurementPortTypeId = null;
		
		for (Iterator iterator = measurementPortTypes.iterator(); iterator.hasNext();) {			
			measurementPortTypeId = ((MeasurementPortType) iterator.next()).getId();			
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, measurementTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, measurementPortTypeId);
			Log.debugMessage("MeasurementTypeDatabase.updateMeasurementPortTypePrepareStatementValues | Inserting measurement port type " + measurementPortTypeId + "' for measurement type " + measurementTypeId, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}

	}

	private void insertParameterTypes(MeasurementType measurementType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier measurementTypeId = measurementType.getId();
		try {
			preparedStatement = insertParameterTypesPreparedStatement();
			updatePrepareStatementValues(preparedStatement, measurementType);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertParameterTypes | Cannot insert parameter type for measurement type '" + measurementTypeId.getIdentifierString() + "' -- " + sqle.getMessage();
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
			}
		}
	}
	
	private void insertMeasurementPortTypes(MeasurementType measurementType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier measurementTypeId = measurementType.getId();
		try {
			preparedStatement = insertMeasurementPortTypePreparedStatement();
			updateMeasurementPortTypePrepareStatementValues(preparedStatement, measurementType);
		}
		catch (SQLException sqle) {
			String mesg = "MeasurementTypeDatabase.insertMeasurementPortTypes | Cannot insert measurement port type for measurement type '" + measurementTypeId.getIdentifierString() + "' -- " + sqle.getMessage();
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
			}
		}
	}

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		MeasurementType measurementType = this.fromStorableObject(storableObject);
		switch (updateKind) {
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
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
	}

	public void delete(MeasurementType measurementType) {
		String measurementTypeIdStr = DatabaseIdentifier.toSQLString(measurementType.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
									+ ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY
									+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MNTTYPPARTYPLINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_TYPE_ID + EQUALS + measurementTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.MEASUREMENTTYPE_ENTITY 
					+ SQL_WHERE + COLUMN_ID + EQUALS + measurementTypeIdStr);

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

	public MeasurementType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {
		List list = null;
		
		try {
			list = retrieveByIds( null , COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename) + APOSTOPHE);
		}  catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}
		
		if ((list == null) || (list.isEmpty()))
				throw new ObjectNotFoundException("No analysis type with codename: '" + codename + "'");
		
		return (MeasurementType) list.get(0);
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
		
		retrieveParameterTypesByOneQuery(list);
		retrieveMeasurementPortTypesByOneQuery(list);
		
		
		return list;
	}
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
		throws IllegalDataException, UpdateObjectException {
		MeasurementType measurementType = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
			try {				
				preparedStatement.setString(++i, measurementType.getCodename());
				preparedStatement.setString(++i, measurementType.getDescription());
			} catch (SQLException sqle) {
				throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
			}
		return i;
		}
	
	private List retrieveButIdsByMeasurementPortType(List ids, List measurementPortTypes) throws RetrieveObjectException {
		
		List list = null;
		
		if (measurementPortTypes != null && !measurementPortTypes.isEmpty()){
			StringBuffer buffer = new StringBuffer(COLUMN_ID + SQL_IN + OPEN_BRACKET
					+ SQL_SELECT + LINK_COLUMN_MEASUREMENT_TYPE_ID + ObjectEntities.MNTTYMEASPORTTYPELINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID + SQL_IN + OPEN_BRACKET);
			int i = 1;
			for (Iterator it = measurementPortTypes.iterator(); it.hasNext(); i++) {
				Object object = it.next();
				Identifier id = null;
				if (object instanceof Identifier)
					id = (Identifier) object;
				else if (object instanceof Identified)
					id = ((Identified) object).getId();
				else
					throw new RetrieveObjectException(
								getEnityName() + "Database.retrieveButIdsByMeasurementPortType | Object "
								+ object.getClass().getName()
												+ " isn't Identifier or Identified");
	
				if (id != null) {
					buffer.append(DatabaseIdentifier.toSQLString(id));
					if (it.hasNext()){
						if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
							buffer.append(COMMA);
						else {
							buffer.append(CLOSE_BRACKET);
							buffer.append(SQL_OR);
							buffer.append(LINK_COLUMN_MEASUREMENT_PORT_TYPE_ID);
							buffer.append(SQL_IN);
							buffer.append(OPEN_BRACKET);
						}					
					}
				}
			}
			buffer.append(CLOSE_BRACKET);
			buffer.append(CLOSE_BRACKET);	
			String condition = buffer.toString();
			try {
				Log.debugMessage(getEnityName() + "Database.retrieveButIdsByMeasurementPortType | Try with additional condition: " + condition, Log.DEBUGLEVEL09);
				list = retrieveButIds(ids, condition);
			}  catch (IllegalDataException ide) {			
				Log.debugMessage(getEnityName() + "Database.retrieveButIdsByMeasurementPortType | Error: " + ide.getMessage(), Log.DEBUGLEVEL09);
			}
		}
		else list = Collections.EMPTY_LIST;
		
		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws IllegalDataException, RetrieveObjectException  {
			List list = null;
			if (condition instanceof LinkedIdsCondition){
				LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
				list = this.retrieveButIdsByMeasurementPortType(ids, linkedIdsCondition.getMeasurementPortTypeIds());
			} else if (condition instanceof StringFieldCondition){
				StringFieldCondition stringFieldCondition = (StringFieldCondition)condition;
				try {
					list = Collections.singletonList(this.retrieveForCodename(stringFieldCondition.getString()));
				} catch (ObjectNotFoundException e) {
					String msg = getEnityName() + "Database.retrieveByCondition | object not found: " + e.getMessage();
					throw new RetrieveObjectException(msg, e);
				} 
			} else{
				Log.errorMessage(getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition);
				list = this.retrieveButIds(ids);
			}
			return list;
	}
}
