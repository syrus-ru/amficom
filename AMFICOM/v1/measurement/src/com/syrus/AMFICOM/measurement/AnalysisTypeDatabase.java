/*
 * $Id: AnalysisTypeDatabase.java,v 1.41 2004/12/01 10:09:58 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.41 $, $Date: 2004/12/01 10:09:58 $
 * @author $Author: max $
 * @module measurement_v1
 */

public class AnalysisTypeDatabase extends StorableObjectDatabase {	

	public static final String	MODE_IN = "IN";
	public static final String	MODE_CRITERION = "CRI";
	public static final String	MODE_ETALON = "ETA";
	public static final String	MODE_OUT = "OUT";

	public static final String	COLUMN_CODENAME = "codename";
	public static final String	COLUMN_DESCRIPTION = "description";	
	
	public static final String	LINK_COLUMN_ANALYSIS_TYPE_ID = "analysis_type_id";
    public static final String  PARAMETER_TYPE_ID = "parameter_type_id";
    public static final String  PARAMETER_MODE = "parameter_mode";
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private static String columns;
	private static String updateMultiplySQLValues;

	private AnalysisType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		throw new IllegalDataException("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	
	protected String getEnityName() {
		return ObjectEntities.ANALYSISTYPE_ENTITY;
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
		AnalysisType analysisType = fromStorableObject(storableObject);		
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getCodename()) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(analysisType.getDescription()) + APOSTOPHE;
		return sql;
	}
	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		super.retrieveEntity(analysisType);
		this.retrieveParameterTypes(analysisType);
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException{
		AnalysisType analysisType = storableObject == null ? 
				new AnalysisType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null,null,null,null,null,null,null) : 
					fromStorableObject(storableObject);
		analysisType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								   DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
								   DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
								   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
								   DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return analysisType;
	}

	private void retrieveParameterTypes(AnalysisType analysisType) throws RetrieveObjectException {	
		List inParTyps = new ArrayList();
		List criteriaParTyps = new ArrayList();
		List etalonParTyps = new ArrayList();
		List outParTyps = new ArrayList();
		
		String analysisTypeIdStr = DatabaseIdentifier.toSQLString(analysisType.getId());
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.ANATYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_ANALYSIS_TYPE_ID + EQUALS + analysisTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			Identifier parameterTypeIdCode;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				parameterTypeIdCode = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
					else
						if (parameterMode.equals(MODE_CRITERION))
							criteriaParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
						else
							if (parameterMode.equals(MODE_ETALON))
								etalonParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
							else
								if (parameterMode.equals(MODE_OUT))
									outParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
								else
									Log .errorMessage("AnalysisTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeIdCode);
			}
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for analysis type '" + analysisTypeIdStr + "' -- " + sqle.getMessage();
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
				DatabaseConnection.releaseConnection(connection);
			}
		}
		((ArrayList)inParTyps).trimToSize();
		((ArrayList)criteriaParTyps).trimToSize();
		((ArrayList)etalonParTyps).trimToSize();
		((ArrayList)outParTyps).trimToSize();
		analysisType.setParameterTypes(inParTyps,
																	 criteriaParTyps,
																	 etalonParTyps,
																	 outParTyps);
	}
    
    private void retrieveParameterTypesByOneQuery(List analysisTypes) throws RetrieveObjectException {
    	
        if ((analysisTypes == null) || (analysisTypes.isEmpty()))
            return;
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
                + LINK_COLUMN_PARAMETER_MODE + COMMA
                + LINK_COLUMN_ANALYSIS_TYPE_ID
                + SQL_FROM + ObjectEntities.ANATYPPARTYPLINK_ENTITY
                + SQL_WHERE + LINK_COLUMN_ANALYSIS_TYPE_ID + SQL_IN + OPEN_BRACKET);
        int i=1;
        for (Iterator it = analysisTypes.iterator(); it.hasNext();i++) {
            AnalysisType analysisType = (AnalysisType)it.next();
            sql.append(DatabaseIdentifier.toSQLString(analysisType.getId()));
            if (it.hasNext()){
                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
                    sql.append(COMMA);
                else {
                    sql.append(CLOSE_BRACKET);
                    sql.append(SQL_OR);
                    sql.append(LINK_COLUMN_ANALYSIS_TYPE_ID);
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
            Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            String parameterMode;
            Identifier parameterTypeId;
            Map inParametersMap = new HashMap();
            Map criteriaParametersMap = new HashMap();
            Map etalonParametersMap = new HashMap();
            Map outParametersMap = new HashMap();
            while (resultSet.next()) {
                AnalysisType analysisType = null;
                Identifier analysisTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_ANALYSIS_TYPE_ID);
                for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
                    AnalysisType analysisTypeToCompare = (AnalysisType) it.next();
                    if (analysisTypeToCompare.getId().equals(analysisTypeId)){
                        analysisType = analysisTypeToCompare;
                        break;
                    }                   
                }
                
                if (analysisType == null){
                    String mesg = "AnalysisTypeDatabase.retrieveParameterTypesByOneQuery | Cannot found correspond result for '" + analysisTypeId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }
                    
                parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
                parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
                ParameterType parameterType; 
                if (parameterMode.equals(MODE_IN)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeId, true));
                    List inParameters = (List)inParametersMap.get(analysisType);
                    if (inParameters == null){
                        inParameters = new LinkedList();
                        inParametersMap.put(analysisType, inParameters);
                    }               
                    inParameters.add(parameterType);
                } else if (parameterMode.equals(MODE_CRITERION)) {
                	parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeId, true));
                	List criteriaParameters = (List)criteriaParametersMap.get(analysisType);
                    if (criteriaParameters == null){
                        criteriaParameters = new LinkedList();
                        criteriaParametersMap.put(analysisType, criteriaParameters);
                    }               
                    criteriaParameters.add(parameterType);
                } else if (parameterMode.equals(MODE_ETALON)) {
                	parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeId, true));
                    List etalonParameters = (List)etalonParametersMap.get(analysisType);
                    if (etalonParameters == null){
                        etalonParameters = new LinkedList();
                        etalonParametersMap.put(analysisType, etalonParameters);
                    }               
                    etalonParameters.add(parameterType);
                } else if (parameterMode.equals(MODE_OUT)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeId, true));
                    List outParameters = (List)outParametersMap.get(analysisType);
                    if (outParameters == null){
                    	outParameters = new LinkedList();
                    	outParametersMap.put(analysisType, outParameters);
                    }
                } else {
                	Log .errorMessage("AnalysisTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeId);
                }                
            }
            for (Iterator iter = analysisTypes.iterator(); iter.hasNext();) {
                AnalysisType analysisType = (AnalysisType) iter.next();
                List inParameterpTypes = (List)inParametersMap.get(analysisType);
                List criteriaParameterpTypes = (List)criteriaParametersMap.get(analysisType);
                List etalonParameterpTypes = (List)etalonParametersMap.get(analysisType);
                List outParameterpTypes = (List)inParametersMap.get(analysisType);
                
                analysisType.setParameterTypes(inParameterpTypes, criteriaParameterpTypes, etalonParameterpTypes, outParameterpTypes);
            }            
        }
        catch (SQLException sqle) {
            String mesg = "AnalysisTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for analysis types -- " + sqle.getMessage();
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
                DatabaseConnection.releaseConnection(connection);
            }
        }        
    }

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		this.insertEntity(analysisType);
		this.insertParameterTypes(analysisType);
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			AnalysisType analysisType = fromStorableObject((StorableObject)it.next());
			insertParameterTypes(analysisType);
		}

	}

	private void insertParameterTypes(AnalysisType analysisType) throws CreateObjectException {
		List inParTyps = analysisType.getInParameterTypes();
		List criteriaParTyps = analysisType.getCriteriaParameterTypes();
		List etalonParTyps = analysisType.getEtalonParameterTypes();
		List outParTyps = analysisType.getOutParameterTypes();
		Identifier analysisTypeId = analysisType.getId();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.ANATYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_ANALYSIS_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier parameterTypeId = null;
		String parameterMode = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {				
				
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = criteriaParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_CRITERION;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_ETALON;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, analysisTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeId + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.insertParameterTypes | Cannot insert parameter type '"
					+ parameterTypeId + "' of parameter mode '" + parameterMode + "' for analysis type '"
					+ analysisTypeId + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
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
	
	public void delete(StorableObject storableObject) throws IllegalDataException {
		AnalysisType analysisType = fromStorableObject(storableObject);
		String analysisTypeIdStr = DatabaseIdentifier.toSQLString(analysisType.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANATYPPARTYPLINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_ANALYSIS_TYPE_ID + EQUALS + analysisTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.ANALYSISTYPE_ENTITY
					+ SQL_WHERE + COLUMN_ID + EQUALS + analysisTypeIdStr);
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	public AnalysisType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {
		
		List list = null;
		
		try {
			list = retrieveByIds( null , COLUMN_CODENAME + EQUALS + APOSTOPHE + codename + APOSTOPHE);
		}  catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}
		
		if ((list == null) || (list.isEmpty()))
				throw new ObjectNotFoundException("No analysis type with codename: '" + codename + "'");
		
		return (AnalysisType) list.get(0);
		
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
		
		return list;		
	}	
	
	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		AnalysisType analysisType = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setString(++i, analysisType.getCodename());
			preparedStatement.setString(++i, analysisType.getDescription());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

    private List retrieveButIdsByCriteriaSet(List ids, List criteriaSetIds) throws RetrieveObjectException, IllegalDataException {
        
    	
    	if (criteriaSetIds != null && !criteriaSetIds.isEmpty()){
	        String condition = new String();
	        StringBuffer criteriaSetIdNames = new StringBuffer();        
	        
	        int i=1;
	        for (Iterator it = criteriaSetIds.iterator(); it.hasNext();) {
	            criteriaSetIdNames.append(DatabaseIdentifier.toSQLString((Identifier) it.next()));
	            if (it.hasNext()){
	                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
	                    criteriaSetIdNames.append(COMMA);
	                else {
	                    criteriaSetIdNames.append(CLOSE_BRACKET);
	                    criteriaSetIdNames.append(SQL_OR);
	                    criteriaSetIdNames.append(SetDatabase.LINK_COLUMN_SET_ID);
	                    criteriaSetIdNames.append(SQL_IN);
	                    criteriaSetIdNames.append(OPEN_BRACKET);
	                }                   
	            }
	        }
	        
	        condition = PARAMETER_TYPE_ID + SQL_IN + OPEN_BRACKET 
	                + SQL_SELECT + SetDatabase.LINK_COLUMN_TYPE_ID + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
					    + SQL_WHERE + SetDatabase.LINK_COLUMN_SET_ID + SQL_IN + OPEN_BRACKET + criteriaSetIdNames 
						+ CLOSE_BRACKET
	                + CLOSE_BRACKET 
	                + SQL_AND + PARAMETER_MODE + EQUALS + APOSTOPHE + MODE_CRITERION + APOSTOPHE;
	        
	        return retrieveByIds( ids, condition);
    	} 
    		return Collections.EMPTY_LIST;
    }
    
    
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException, IllegalDataException {
		List list = null;
        if (condition instanceof LinkedIdsCondition){
            LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
            list = this.retrieveButIdsByCriteriaSet(ids, linkedIdsCondition.getCriteriaSetIds());
        } else {
            Log.errorMessage(getEnityName() + "Database.retrieveByCondition | Unknown condition class: " + condition);
            list = this.retrieveButIds(ids);
        }
        
        return list;
	}
}
