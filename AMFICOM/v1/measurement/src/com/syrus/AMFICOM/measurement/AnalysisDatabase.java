/*
 * $Id: AnalysisDatabase.java,v 1.17 2004/08/29 11:47:05 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
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

/**
 * @version $Revision: 1.17 $, $Date: 2004/08/29 11:47:05 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class AnalysisDatabase extends StorableObjectDatabase {

	public static final String COLUMN_TYPE_ID = "type_id";
	public static final String COLUMN_MONITORED_ELEMENT_ID = "monitored_element_id";
	public static final String COLUMN_CRITERIA_SET_ID = "criteria_set_id";

	private Analysis fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Analysis)
			return (Analysis) storableObject;
		throw new IllegalDataException("AnalysisDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		this.retrieveAnalysis(analysis);
	}

	private String retrieveAnalysisQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_TYPE_ID + COMMA
		+ COLUMN_MONITORED_ELEMENT_ID + COMMA
		+ COLUMN_CRITERIA_SET_ID
		+ SQL_FROM + ObjectEntities.ANALYSIS_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private Analysis updateAnalysisFromResultSet(Analysis analysis, ResultSet resultSet) throws RetrieveObjectException, SQLException{
		Analysis analysis1 = analysis;
		if (analysis == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			analysis1 = new Analysis(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null, null);			
		}
		AnalysisType analysisType;
		Set criteriaSet;
		try {
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			analysisType = (AnalysisType)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_TYPE_ID)), true);
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			criteriaSet = (Set)MeasurementStorableObjectPool.getStorableObject(new Identifier(resultSet.getString(COLUMN_CRITERIA_SET_ID)), true);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		analysis.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
													 DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
													/**
													 * @todo when change DB Identifier model ,change getString() to getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
													/**
													 * @todo when change DB Identifier model ,change getString() to getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
													 analysisType,
													/**
													 * @todo when change DB Identifier model ,change getString() to getLong()
													 */
													 new Identifier(resultSet.getString(COLUMN_MONITORED_ELEMENT_ID)),
													 criteriaSet);		return analysis1;
	}

	
	private void retrieveAnalysis(Analysis analysis) throws ObjectNotFoundException, RetrieveObjectException {
		String analysisIdStr = analysis.getId().toSQLString();
		String sql = retrieveAnalysisQuery(COLUMN_ID + EQUALS + analysisIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisDatabase.retrieve | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateAnalysisFromResultSet(analysis, resultSet);
			else
				throw new ObjectNotFoundException("No such analysis: " + analysisIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.retrieve | Cannot retrieve analysis '" + analysisIdStr + "' -- " + sqle.getMessage();
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
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Analysis analysis = this.fromStorableObject(storableObject);
		try {
			this.insertAnalysis(analysis);
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

	private void insertAnalysis(Analysis analysis) throws CreateObjectException {
		String analysisIdStr = analysis.getId().toSQLString();
		String sql = SQL_INSERT_INTO
			+ ObjectEntities.ANALYSIS_ENTITY + OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_TYPE_ID + COMMA
			+ COLUMN_MONITORED_ELEMENT_ID + COMMA
			+ COLUMN_CRITERIA_SET_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ analysisIdStr + COMMA
			+ DatabaseDate.toUpdateSubString(analysis.getCreated()) + COMMA
			+ DatabaseDate.toUpdateSubString(analysis.getModified()) + COMMA
			+ analysis.getCreatorId().toSQLString() + COMMA
			+ analysis.getModifierId().toSQLString() + COMMA
			+ analysis.getType().getId().toSQLString() + COMMA
			+ analysis.getMonitoredElementId().toSQLString() + COMMA
			+ analysis.getCriteriaSet().getId().toSQLString()
			+ CLOSE_BRACKET;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.insert | Cannot insert analysis '" + analysisIdStr + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, UpdateObjectException {
		Analysis analysis = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				return;
		}
	}
	
	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return retriveByIdsOneQuery(null);
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
		List result = new LinkedList();
		String sql;
		{
			String condition = null;
			if (ids!=null){
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
					condition = buffer.toString();
				}
			}
			sql = retrieveAnalysisQuery(condition);
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("AnalysisDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateAnalysisFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
	
	private List retriveByIdsPreparedStatement(List ids) throws RetrieveObjectException {
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
			
			sql =retrieveAnalysisQuery(buffer.toString());
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
					result.add(updateAnalysisFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("AnalysisDatabase.retriveByIdsPreparedStatement | No such analysis: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "AnalysisDatabase.retriveByIdsPreparedStatement | Cannot retrieve analysis " + sqle.getMessage();
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
