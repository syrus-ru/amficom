/*
 * $Id: EvaluationTypeDatabase.java,v 1.36 2004/11/19 09:01:07 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
 * @version $Revision: 1.36 $, $Date: 2004/11/19 09:01:07 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class EvaluationTypeDatabase extends StorableObjectDatabase {

	public static final String	MODE_IN = "IN";
	public static final String	MODE_THRESHOLD = "THS";
	public static final String	MODE_ETALON = "ETA";
	public static final String	MODE_OUT = "OUT";

	public static final String	COLUMN_CODENAME = "codename";
	public static final String	COLUMN_DESCRIPTION = "description";

	public static final String	LINK_COLUMN_EVALUATION_TYPE_ID = "evaluation_type_id";

	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
    public static final String  PARAMETER_TYPE_ID = "parameter_type_id";
    public static final String  PARAMETER_MODE = "parameter_mode";
    
	private static String columns;
	private static String updateMultiplySQLValues;

	private EvaluationType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType) storableObject;
		throw new IllegalDataException("EvaluationTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	

	protected String getEnityName() {
		return ObjectEntities.EVALUATIONTYPE_ENTITY;
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

	
	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		EvaluationType evaluationType = fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement);
		try {
			preparedStatement.setString(++i, evaluationType.getCodename()); 
			preparedStatement.setString(++i, evaluationType.getDescription()); 
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		EvaluationType evaluationType = fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
		+ APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getCodename()) + APOSTOPHE + COMMA
		+ APOSTOPHE + DatabaseString.toQuerySubString(evaluationType.getDescription()) + APOSTOPHE;		
		return values;
	}
	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEntity(evaluationType);
		this.retrieveParameterTypes(evaluationType);
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		EvaluationType evaluationType = (storableObject == null) ?
				new EvaluationType(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID), null,null,null,null,null,null,null) : 
					fromStorableObject(storableObject);
		evaluationType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
									 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
									 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
									 DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
									 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_CODENAME)),
									 DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));
		return evaluationType;
	}

	private void retrieveParameterTypes(EvaluationType evaluationType) throws RetrieveObjectException {	
		List inParTyps = new ArrayList();
		List thresholdParTyps = new ArrayList();
		List etalonParTyps = new ArrayList();
		List outParTyps = new ArrayList();

		String evaluationTypeIdStr = DatabaseIdentifier.toSQLString(evaluationType.getId());
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.EVATYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;			
			Identifier parameterTypeIdCode;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				parameterTypeIdCode = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
					else
						if (parameterMode.equals(MODE_THRESHOLD))
							thresholdParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
						else
							if (parameterMode.equals(MODE_ETALON))
								etalonParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
							else
								if (parameterMode.equals(MODE_OUT))
									outParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
								else
									Log.errorMessage("EvaluationTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeIdCode);
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for evaluation type '" + evaluationTypeIdStr + "' -- " + sqle.getMessage();
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
		((ArrayList)thresholdParTyps).trimToSize();
		((ArrayList)etalonParTyps).trimToSize();
		((ArrayList)outParTyps).trimToSize();
		evaluationType.setParameterTypes(inParTyps,
																		 thresholdParTyps,
																		 etalonParTyps,
																		 outParTyps);
	}
    
    private void retrieveParameterTypesByOneQuery(List evaluationTypes) throws RetrieveObjectException {
    	
        if ((evaluationTypes == null) || (evaluationTypes.isEmpty()))
            return;
        
        StringBuffer sql = new StringBuffer(SQL_SELECT
                + LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
                + LINK_COLUMN_PARAMETER_MODE + COMMA
                + LINK_COLUMN_EVALUATION_TYPE_ID
                + SQL_FROM + ObjectEntities.EVATYPPARTYPLINK_ENTITY
                + SQL_WHERE + LINK_COLUMN_EVALUATION_TYPE_ID + SQL_IN + OPEN_BRACKET);
        int i=1;
        for (Iterator it = evaluationTypes.iterator(); it.hasNext();i++) {
            EvaluationType evaluationType = (EvaluationType)it.next();
            sql.append(DatabaseIdentifier.toSQLString(evaluationType.getId()));
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
            Log.debugMessage("EvaluationTypeDatabase.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
            resultSet = statement.executeQuery(sql.toString());
            String parameterMode;
            Identifier parameterTypeIdCode;
            Map inParametersMap = new HashMap();
            Map thresholdParametersMap = new HashMap();
            Map etalonParametersMap = new HashMap();
            Map outParametersMap = new HashMap();
            while (resultSet.next()) {
                EvaluationType evaluationType = null;
                Identifier evaluationTypeId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_EVALUATION_TYPE_ID);
                for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
                    EvaluationType evaluationTypeToCompare = (EvaluationType) it.next();
                    if (evaluationTypeToCompare.getId().equals(evaluationTypeId)){
                        evaluationType = evaluationTypeToCompare;
                        break;
                    }                   
                }
                
                if (evaluationType == null){
                    String mesg = "EvaluationTypeDatabase.retrieveParameterTypesByOneQuery | Cannot found correspond result for '" +evaluationTypeId +"'" ;
                    throw new RetrieveObjectException(mesg);
                }                    
               
                parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
                parameterTypeIdCode = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID);
                ParameterType parameterType; 
                if (parameterMode.equals(MODE_IN)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
                    List inParameters = (List)inParametersMap.get(evaluationType);
                    if (inParameters == null){
                        inParameters = new LinkedList();
                        inParametersMap.put(evaluationType, inParameters);
                    }               
                    inParameters.add(parameterType);
                } else if (parameterMode.equals(MODE_THRESHOLD)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
                    List thresholdParameters = (List)thresholdParametersMap.get(evaluationType);
                    if (thresholdParameters == null){
                        thresholdParameters = new LinkedList();
                        thresholdParametersMap.put(evaluationType, thresholdParameters);
                    }               
                    thresholdParameters.add(parameterType);
                } else if (parameterMode.equals(MODE_ETALON)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
                    List etalonParameters = (List)etalonParametersMap.get(evaluationType);
                    if (etalonParameters == null){
                        etalonParameters = new LinkedList();
                        etalonParametersMap.put(evaluationType, etalonParameters);
                    }               
                    etalonParameters.add(parameterType);
                } else if (parameterMode.equals(MODE_OUT)) {
                    parameterType = ((ParameterType) MeasurementStorableObjectPool.getStorableObject(parameterTypeIdCode, true));
                    List outParameters = (List)outParametersMap.get(evaluationType);
                    if (outParameters == null){
                        outParameters = new LinkedList();
                        outParametersMap.put(evaluationType, outParameters);
                    }
                } else {
                    Log .errorMessage("EvaluationTypeDatabase.retrieveParameterTypes | ERROR: Unknown parameter mode for parameterTypeId " + parameterTypeIdCode);
                }                
            }
            for (Iterator it = evaluationTypes.iterator(); it.hasNext();) {
                EvaluationType evaluationType = (EvaluationType) it.next();
                List inParameterpTypes = (List)inParametersMap.get(evaluationType);
                List thresholdParameterpTypes = (List)thresholdParametersMap.get(evaluationType);
                List etalonParameterpTypes = (List)etalonParametersMap.get(evaluationType);
                List outParameterpTypes = (List)inParametersMap.get(evaluationType);
                
                evaluationType.setParameterTypes(inParameterpTypes, thresholdParameterpTypes, etalonParameterpTypes, outParameterpTypes);
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
                DatabaseConnection.closeConnection(connection);
            }
        } 
    }

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.insertEntity(evaluationType);
		this.insertParameterTypes(evaluationType);
	}
	
	
	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		insertEntities(storableObjects);
		for(Iterator it=storableObjects.iterator();it.hasNext();){
			EvaluationType evaluationType = fromStorableObject((StorableObject)it.next());
			insertParameterTypes(evaluationType);
		}
	}

	private void insertParameterTypes(EvaluationType evaluationType) throws CreateObjectException {
		List inParTyps = evaluationType.getInParameterTypes();
		List thresholdParTyps = evaluationType.getThresholdParameterTypes();
		List etalonParTyps = evaluationType.getEtalonParameterTypes();
		List outParTyps = evaluationType.getOutParameterTypes();

		Identifier evaluationTypeId = evaluationType.getId();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVATYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_EVALUATION_TYPE_ID + COMMA
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
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeId
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = thresholdParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_THRESHOLD;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeId
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_ETALON;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeId
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				parameterTypeId = ((ParameterType) iterator.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, evaluationTypeId);				
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeId
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeId, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.insertParameterTypes | Cannot insert parameter type '"
					+ parameterTypeId
					+ "' of parameter mode '"
					+ parameterMode
					+ "' for evaluation type '"
					+ evaluationTypeId + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, int updateKind, Object obj)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
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
		EvaluationType evaluationType = fromStorableObject(storableObject);
		String evaluationTypeIdStr = DatabaseIdentifier.toSQLString(evaluationType.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVATYPPARTYPLINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVALUATIONTYPE_ENTITY
					+ SQL_WHERE + COLUMN_ID + EQUALS + evaluationTypeIdStr);
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
				DatabaseConnection.closeConnection(connection);
			}
		}
	}

	public EvaluationType retrieveForCodename(String codename) throws ObjectNotFoundException, RetrieveObjectException {
		List list = null;
		
		try {
			list = retrieveByIds( null , COLUMN_CODENAME + EQUALS + APOSTOPHE + DatabaseString.toQuerySubString(codename) + APOSTOPHE);
		}  catch (IllegalDataException ide) {				
			throw new RetrieveObjectException(ide);
		}
		
		if ((list == null) || (list.isEmpty()))
				throw new ObjectNotFoundException("No evaluation type with codename: '" + codename + "'");
		
		return (EvaluationType) list.get(0);
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

    private List retrieveButIdByThresholdSet(List ids, List thresholdSetIds) throws RetrieveObjectException, IllegalDataException {
        
    	if (thresholdSetIds != null && !thresholdSetIds.isEmpty()){
	        String condition = new String();        
	        StringBuffer tresholds = new StringBuffer();
	        
	        int i=1;
	        for (Iterator it = thresholdSetIds.iterator(); it.hasNext();i++) {
	            tresholds.append (DatabaseIdentifier.toSQLString((Identifier) it.next()));            
	            if (it.hasNext()){
	                if (((i+1) % MAXIMUM_EXPRESSION_NUMBER != 0))
	                    tresholds.append(COMMA);
	                else {
	                    tresholds.append(CLOSE_BRACKET);
	                    tresholds.append(SQL_OR);
	                    tresholds.append(COLUMN_ID);
	                    tresholds.append(SQL_IN);
	                    tresholds.append(OPEN_BRACKET);
	                }                   
	            }            
	        }
	        
	        condition = PARAMETER_TYPE_ID + SQL_IN + OPEN_BRACKET +
	                SQL_SELECT + SetDatabase.LINK_COLUMN_TYPE_ID + SQL_FROM + ObjectEntities.SETPARAMETER_ENTITY
	                + SQL_WHERE + SetDatabase.LINK_COLUMN_SET_ID + SQL_IN + OPEN_BRACKET + tresholds 
	                + CLOSE_BRACKET
	            + CLOSE_BRACKET 
	            + SQL_AND + PARAMETER_MODE + EQUALS + APOSTOPHE + MODE_THRESHOLD + APOSTOPHE;
	        return retrieveButIds(ids , condition);
        }
    	return Collections.EMPTY_LIST;
    	
    }    
    
	public List retrieveByCondition(List ids, StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list;
		if (condition instanceof LinkedIdsCondition){
            LinkedIdsCondition linkedIdsCondition = (LinkedIdsCondition)condition;
            list = this.retrieveButIdByThresholdSet(ids, linkedIdsCondition.getThresholdSetIds());
        } else {
            Log.errorMessage("EvaluationTypeDatabase.retrieveByCondition | Unknown condition class: " + condition);
            list = this.retrieveButIds(ids);
        }
		return list;
	}
}
