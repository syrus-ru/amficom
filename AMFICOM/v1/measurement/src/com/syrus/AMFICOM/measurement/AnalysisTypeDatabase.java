/*
 * $Id: AnalysisTypeDatabase.java,v 1.21 2004/08/26 14:59:18 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.21 $, $Date: 2004/08/26 14:59:18 $
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

	private AnalysisType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof AnalysisType)
			return (AnalysisType) storableObject;
		throw new IllegalDataException("AnalysisTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		this.retrieveAnalysisType(analysisType);
		this.retrieveParameterTypes(analysisType);
	}
	
	private static String retrieveAnalysisTypeQuery(String condition){
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
	
	private static AnalysisType updateAnalysisTypeFromResultSet(AnalysisType analysisType, ResultSet resultSet) throws SQLException{
		AnalysisType analysisType1 = analysisType;
		if (analysisType == null){
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

	private void retrieveAnalysisType(AnalysisType analysisType) throws ObjectNotFoundException, RetrieveObjectException {
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		String sql = retrieveAnalysisTypeQuery(COLUMN_ID + EQUALS + analysisTypeIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveAnalysisType | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				updateAnalysisTypeFromResultSet(analysisType, resultSet);
			else
				throw new ObjectNotFoundException("No such analysis type: " + analysisTypeIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveAnalysisType | Cannot retrieve analysis type '" + analysisTypeIdStr + "' -- " + sqle.getMessage();
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
			}
		}
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
			this.insertAnalysisType(analysisType);
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

	private void insertAnalysisType(AnalysisType analysisType) throws CreateObjectException {
		String analysisTypeIdStr = analysisType.getId().toSQLString();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.ANALYSISTYPE_ENTITY + OPEN_BRACKET 
			+ COLUMN_ID + COMMA 
			+ COLUMN_CREATED + COMMA 
			+ COLUMN_MODIFIED + COMMA 
			+ COLUMN_CREATOR_ID + COMMA 
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_CODENAME + COMMA 
			+ COLUMN_DESCRIPTION
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET			
			+ analysisTypeIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(analysisType.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(analysisType.getModified()) + COMMA
			+ analysisType.getCreatorId().toSQLString() + COMMA 
			+ analysisType.getModifierId().toSQLString() + COMMA
			+ APOSTOPHE + analysisType.getCodename() + APOSTOPHE + COMMA 
			+ APOSTOPHE + analysisType.getDescription() + APOSTOPHE
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.insertAnalysisType | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.insertAnalysisType | Cannot insert analysis type '" + analysisTypeIdStr + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null) statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		AnalysisType analysisType = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}

	public static void delete(AnalysisType analysisType) {
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

	public static AnalysisType retrieveForCodename(String codename) throws ObjectNotFoundException , RetrieveObjectException {
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.ANALYSISTYPE_ENTITY
				+ SQL_WHERE + COLUMN_CODENAME + EQUALS + APOSTOPHE + codename + APOSTOPHE;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveForCodename | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				return new AnalysisType(new Identifier(resultSet.getString(COLUMN_ID)));
			throw new ObjectNotFoundException("No analysis type with codename: '" + codename + "'");
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveForCodename | Cannot retrieve analysis type with codename: '" + codename + "' -- " + sqle.getMessage();
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
			}
		}
	}
	
	public static List retrieveAll() throws RetrieveObjectException {
		List analysisTypes = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.ANALYSISTYPE_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				analysisTypes.add(new AnalysisType(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retrieveAll | Cannot retrieve analysis type -- " + sqle.getMessage();
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
			}
		}
		return analysisTypes;
	}
	
	public static List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return new LinkedList();
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private static List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			int idsLength = ids.size();
			if (idsLength == 1){
				buffer.append(EQUALS);
				buffer.append(((Identifier)ids.iterator().next()).toSQLString());
			} else{
				buffer.append(SQL_IN);
				buffer.append(OPEN_BRACKET);
				
				int i = 1;
				for(Iterator it=ids.iterator();it.hasNext();i++){
					Identifier id = (Identifier)it.next();
					buffer.append(id.toSQLString());
					if (i < idsLength)
						buffer.append(COMMA);
				}
				
				buffer.append(CLOSE_BRACKET);
			}
			sql = retrieveAnalysisTypeQuery(buffer.toString());
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisTypeDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateAnalysisTypeFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			}
		}
		return result;
	}
	
	private static List retriveByIdsPreparedStatement(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			
			int idsLength = ids.size();
			if (idsLength == 1){
				return retriveByIdsOneQuery(ids);
			}
			StringBuffer buffer = new StringBuffer(COLUMN_ID);
			buffer.append(EQUALS);							
			buffer.append(QUESTION);
			
			sql =retrieveAnalysisTypeQuery(buffer.toString());
		}
			
		PreparedStatement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = connection.prepareStatement(sql.toString());
			for(Iterator it = ids.iterator();it.hasNext();){
				Identifier id = (Identifier)it.next(); 
				/**
				 * @todo when change DB Identifier model ,change setString() to setLong()
				 */
				String idStr = id.getIdentifierString();
				stmt.setString(1, idStr);
				resultSet = stmt.executeQuery();
				if (resultSet.next()){
					result.add(updateAnalysisTypeFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("AnalysisTypeDatabase.retriveByIdsPreparedStatement | No such analysis type: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "AnalysisTypeDatabase.retriveByIdsPreparedStatement | Cannot retrieve analysis type " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt != null)
					stmt.close();
				stmt = null;
				resultSet = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}			
		
		return result;
	}
	
}
