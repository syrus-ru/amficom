/*
 * $Id: EvaluationTypeDatabase.java,v 1.25 2004/09/09 09:21:47 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.25 $, $Date: 2004/09/09 09:21:47 $
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
	
	private String updateColumns;
	private String updateMultiplySQLValues;

	private EvaluationType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EvaluationType)
			return (EvaluationType) storableObject;
		throw new IllegalDataException("EvaluationTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}	

	protected String getEnityName() {
		return "EvaluationType";
	}
	
	protected String getTableName() {
		return ObjectEntities.EVALUATIONTYPE_ENTITY;
	}
	
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = super.getUpdateColumns() + COMMA
			+ COLUMN_CODENAME + COMMA
			+ COLUMN_DESCRIPTION;
		}
		
		return this.updateColumns;
	}	
	
	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
			+ QUESTION + COMMA
			+ QUESTION;
		}		
		return this.updateMultiplySQLValues;
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
		+ APOSTOPHE + evaluationType.getCodename() + APOSTOPHE + COMMA
		+ APOSTOPHE + evaluationType.getDescription() + APOSTOPHE;		
		return values;
	}
	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		this.retrieveEntity(evaluationType);
		this.retrieveParameterTypes(evaluationType);
	}
	
	protected String retrieveQuery(String condition){
		return super.retrieveQuery(condition) + COMMA
		+ COLUMN_CODENAME + COMMA
		+ COLUMN_DESCRIPTION
		+ SQL_FROM + ObjectEntities.EVALUATIONTYPE_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
		throws IllegalDataException, RetrieveObjectException, SQLException {
		EvaluationType evaluationType = (storableObject == null) ?
				new EvaluationType(new Identifier(resultSet.getString(COLUMN_ID)), null,null,null,null,null,null,null) : 
					fromStorableObject(storableObject);
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
		evaluationType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
												DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
												/**
												 * @todo when change DB Identifier model ,change getString() to
												 *       getLong()
												 */
												 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
												 /**
												  * @todo when change DB Identifier model ,change getString() to
												  *       getLong()
												  */
												 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
												 resultSet.getString(COLUMN_CODENAME),
												 resultSet.getString(COLUMN_DESCRIPTION));
		return evaluationType;
	}

	private void retrieveParameterTypes(EvaluationType evaluationType) throws RetrieveObjectException {	
		List inParTyps = new ArrayList();
		List thresholdParTyps = new ArrayList();
		List etalonParTyps = new ArrayList();
		List outParTyps = new ArrayList();

		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.EVATYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_EVALUATION_TYPE_ID + EQUALS + evaluationTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("EvaluationTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			/**
			 * @todo when change DB Identifier model ,change String to long
			 */
			String parameterTypeIdCode;
			while (resultSet.next()) {
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				/**
				 * @todo when change DB Identifier model ,change setString() to
				 *       setLong()
				 */
				parameterTypeIdCode = resultSet.getString(LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
					else
						if (parameterMode.equals(MODE_THRESHOLD))
							thresholdParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
						else
							if (parameterMode.equals(MODE_ETALON))
								etalonParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
							else
								if (parameterMode.equals(MODE_OUT))
									outParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		EvaluationType evaluationType = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(evaluationType);
			this.insertParameterTypes(evaluationType);
		}
		catch (CreateObjectException e) {
			try {
				connection.rollback();
			}
			catch (SQLException sqle) {
				Log.errorMessage("Exception in rolling back");
				Log.errorException(sqle);
			}
			throw e;
		}
		try {
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorMessage("Exception in commiting");
			Log.errorException(sqle);
		}
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
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String evaluationTypeIdCode = evaluationType.getId().getCode();
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
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String parameterTypeIdCode = null;
		String parameterMode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = thresholdParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_THRESHOLD;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_ETALON;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, evaluationTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("EvaluationTypeDatabase.insertParameterTypes | Inserting parameter type "
								+ parameterTypeIdCode
								+ " of parameter mode '" + parameterMode
								+ "' for evaluation type "
								+ evaluationTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "EvaluationTypeDatabase.insertParameterTypes | Cannot insert parameter type '"
					+ parameterTypeIdCode
					+ "' of parameter mode '"
					+ parameterMode
					+ "' for evaluation type '"
					+ evaluationTypeIdCode + "' -- " + sqle.getMessage();
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

	public void delete(EvaluationType evaluationType) {
		String evaluationTypeIdStr = evaluationType.getId().toSQLString();
		Statement statement = null;
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
			}
		}
	}

	public EvaluationType retrieveForCodename(String codename) throws ObjectNotFoundException, RetrieveObjectException {
		List list = null;
		
		try {
			list = retrieveByIds( null , COLUMN_CODENAME + EQUALS + APOSTOPHE + codename + APOSTOPHE);
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
			list = retriveByIdsOneQuery(null, condition);
		else list = retriveByIdsOneQuery(ids, condition);
		
		for(Iterator it=list.iterator();it.hasNext();){
			EvaluationType evaluationType = (EvaluationType)it.next();
			retrieveParameterTypes(evaluationType);
		}
		
		return list;	
	}
	
}
