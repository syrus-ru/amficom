/*
 * $Id: AnalysisTypeDatabase.java,v 1.25 2004/09/06 14:33:11 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

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
 * @version $Revision: 1.25 $, $Date: 2004/09/06 14:33:11 $
 * @author $Author: bob $
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
	
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;
	
	private String updateColumns;
	private String updateMultiplySQLValues;

	private AnalysisType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		throw new IllegalDataException("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	
	protected String getEnityName() {
		return "AnalysisType";
	}
	
	
	protected String getTableName() {
		return ObjectEntities.ANALYSISTYPE_ENTITY;
	}	
	
	protected String getUpdateColumns() {
		if (this.updateColumns == null){
			this.updateColumns = COLUMN_ID + COMMA 
			+ COLUMN_CREATED + COMMA 
			+ COLUMN_MODIFIED + COMMA 
			+ COLUMN_CREATOR_ID + COMMA 
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA 
			+ COLUMN_DESCRIPTION;
		}
		return this.updateColumns;
	}	

	protected String getUpdateMultiplySQLValues() {
		if (this.updateMultiplySQLValues == null){
			this.updateMultiplySQLValues = QUESTION + COMMA 
			+ QUESTION + COMMA 
			+ QUESTION + COMMA 
			+ QUESTION + COMMA 
			+ QUESTION + COMMA
			+ QUESTION + COMMA 
			+ QUESTION;
		}
		return this.updateMultiplySQLValues;
	}	
	
	
	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException,
			UpdateObjectException {
		AnalysisType analysisType = fromStorableObject(storableObject);
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		String sql = analysisTypeIdStr + COMMA
		+ DatabaseDate.toUpdateSubString(analysisType.getCreated()) + COMMA
		+ DatabaseDate.toUpdateSubString(analysisType.getModified()) + COMMA
		+ analysisType.getCreatorId().toSQLString() + COMMA 
		+ analysisType.getModifierId().toSQLString() + COMMA
		+ APOSTOPHE + analysisType.getCodename() + APOSTOPHE + COMMA 
		+ APOSTOPHE + analysisType.getDescription() + APOSTOPHE;
		return sql;
	}
	

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		super.retrieveEntity(analysisType);
		this.retrieveParameterTypes(analysisType);
	}
	
	protected String retrieveQuery(final String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_CODENAME + COMMA
		+ COLUMN_DESCRIPTION
		+ SQL_FROM + ObjectEntities.ANALYSISTYPE_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		AnalysisType analysisType1 = (AnalysisType) storableObject;
		if (storableObject == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			analysisType1 = new AnalysisType(new Identifier(resultSet.getString(COLUMN_ID)), null,null,null,null,null,null,null);
		}
		/**
		 * @todo when change DB Identifier model ,change getString() to getLong()
		 */
		analysisType1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
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
		return analysisType1;
	}

	private void retrieveParameterTypes(AnalysisType analysisType) throws RetrieveObjectException {	
		List inParTyps = new ArrayList();
		List criteriaParTyps = new ArrayList();
		List etalonParTyps = new ArrayList();
		List outParTyps = new ArrayList();
		
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		String sql = SQL_SELECT
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_MODE
			+ SQL_FROM + ObjectEntities.ANATYPPARTYPLINK_ENTITY
			+ SQL_WHERE + LINK_COLUMN_ANALYSIS_TYPE_ID + EQUALS + analysisTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			String parameterMode;
			String parameterTypeIdCode;
			while (resultSet.next()) {
				/**
				 * @todo when change DB Identifier model ,change getString() to getLong()
				 */
				parameterMode = resultSet.getString(LINK_COLUMN_PARAMETER_MODE);
				parameterTypeIdCode = resultSet.getString(LINK_COLUMN_PARAMETER_TYPE_ID);
				if (parameterMode.equals(MODE_IN))
					inParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
					else
						if (parameterMode.equals(MODE_CRITERION))
							criteriaParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
						else
							if (parameterMode.equals(MODE_ETALON))
								etalonParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
							else
								if (parameterMode.equals(MODE_OUT))
									outParTyps.add((ParameterType) MeasurementStorableObjectPool.getStorableObject(new Identifier(parameterTypeIdCode), true));
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(analysisType);
			this.insertParameterTypes(analysisType);
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
			AnalysisType analysisType = fromStorableObject((StorableObject)it.next());
			insertParameterTypes(analysisType);
		}

	}

	private void insertParameterTypes(AnalysisType analysisType) throws CreateObjectException {
		List inParTyps = analysisType.getInParameterTypes();
		List criteriaParTyps = analysisType.getCriteriaParameterTypes();
		List etalonParTyps = analysisType.getEtalonParameterTypes();
		List outParTyps = analysisType.getOutParameterTypes();
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */
		String analysisTypeIdCode = analysisType.getId().getCode();
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
		/**
		 * @todo when change DB Identifier model ,change String to long
		 */		
		String parameterTypeIdCode = null;
		String parameterMode = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator iterator = inParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysisTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_IN;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = criteriaParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysisTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_CRITERION;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = etalonParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysisTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_ETALON;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			for (Iterator iterator = outParTyps.iterator(); iterator.hasNext();) {
				preparedStatement.setString(1, analysisTypeIdCode);
				parameterTypeIdCode = ((ParameterType) iterator.next()).getId().getCode();
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				preparedStatement.setString(2, parameterTypeIdCode);
				parameterMode = MODE_OUT;
				preparedStatement.setString(3, parameterMode);
				Log.debugMessage("AnalysisTypeDatabase.insertParameterTypes | Inserting parameter type "
						+ parameterTypeIdCode + " of parameter mode '" + parameterMode + "' for analysis type "
						+ analysisTypeIdCode, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.insertParameterTypes | Cannot insert parameter type '"
					+ parameterTypeIdCode + "' of parameter mode '" + parameterMode + "' for analysis type '"
					+ analysisTypeIdCode + "' -- " + sqle.getMessage();
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
	
	public void delete(AnalysisType analysisType) {
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		Statement statement = null;
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
			list = retriveByIdsOneQuery(null, condition);
		else list = retriveByIdsOneQuery(ids, condition);
		
		for(Iterator it=list.iterator();it.hasNext();){
			AnalysisType analysisType = (AnalysisType)it.next();
			retrieveParameterTypes(analysisType);
		}
		
		return list;		
	}	
	
	
	protected void setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement)
			throws IllegalDataException, UpdateObjectException {
		AnalysisType analysisType = fromStorableObject(storableObject);
		try {
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(1, analysisType.getId().getCode());
			preparedStatement.setTimestamp(2, new Timestamp(analysisType.getCreated().getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(analysisType.getModified().getTime()));
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(4, analysisType.getCreatorId().getCode());
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(5, analysisType.getModifierId().getCode());
			preparedStatement.setString(6, analysisType.getCodename());
			preparedStatement.setString(7, analysisType.getDescription());
			/**
			 * @todo when change DB Identifier model ,change setString() to setLong()
			 */
			preparedStatement.setString(8, analysisType.getId().getCode());
		} catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
	}
}
