/*
 * $Id: TemporalPatternDatabase.java,v 1.12 2004/08/27 12:14:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oracle.jdbc.driver.OracleResultSet;
import oracle.jdbc.driver.OraclePreparedStatement;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.measurement.ora.CronStringArray;

/**
 * @version $Revision: 1.12 $, $Date: 2004/08/27 12:14:57 $
 * @author $Author: bob $
 * @module measurement_v1
 */

public class TemporalPatternDatabase extends StorableObjectDatabase {
	public static final String COLUMN_DESCRIPTION 	= "description";
	public static final String COLUMN_VALUE 		= "value";
		
	public static final int CHARACTER_NUMBER_OF_RECORDS = 1;

	private TemporalPattern fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof TemporalPattern)
			return (TemporalPattern)storableObject;
		throw new IllegalDataException("TemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		this.retrieveTemporalPattern(temporalPattern);
	}

	private String retrieveTemporalPatternQuery(String condition){
		return SQL_SELECT
		+ COLUMN_ID + COMMA
		+ DatabaseDate.toQuerySubString(COLUMN_CREATED) + COMMA 
		+ DatabaseDate.toQuerySubString(COLUMN_MODIFIED) + COMMA
		+ COLUMN_CREATOR_ID + COMMA
		+ COLUMN_MODIFIER_ID + COMMA
		+ COLUMN_DESCRIPTION + COMMA
		+ COLUMN_VALUE
		+ SQL_FROM + ObjectEntities.TEMPORALPATTERN_ENTITY
		+ ( ((condition == null) || (condition.length() == 0) ) ? "" : SQL_WHERE + condition);

	}
	
	private TemporalPattern updateTemporalPatternFromResultSet(TemporalPattern temporalPattern, ResultSet resultSet) throws SQLException{
		TemporalPattern temporalPattern1 = temporalPattern;
		if (temporalPattern == null){
			/**
			 * @todo when change DB Identifier model ,change getString() to getLong()
			 */
			temporalPattern1 = new TemporalPattern(new Identifier(resultSet.getString(COLUMN_ID)), null, null, null);
		}
		String[] cronStrings = ((CronStringArray)(((OracleResultSet)resultSet).getORAData(COLUMN_VALUE, CronStringArray.getORADataFactory()))).getArray();
		temporalPattern1.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
																	DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
																	new Identifier(resultSet.getString(COLUMN_CREATOR_ID)),
																	new Identifier(resultSet.getString(COLUMN_MODIFIER_ID)),
																	resultSet.getString(COLUMN_DESCRIPTION),
																	cronStrings);


		return temporalPattern1;
	}

	
	private void retrieveTemporalPattern(TemporalPattern temporalPattern) throws ObjectNotFoundException, RetrieveObjectException {
		String tpIdStr = temporalPattern.getId().toSQLString();
		String sql = retrieveTemporalPatternQuery(COLUMN_ID + EQUALS + tpIdStr);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TemporalPatternDatabase.retrieveTemporalPattern | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) 
				updateTemporalPatternFromResultSet(temporalPattern, resultSet);
			else
				throw new ObjectNotFoundException("No such temporal pattern: " + tpIdStr);
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.retrieveTemporalPattern | Cannot retrieve temporal pattern '" + tpIdStr + "' -- " + sqle.getMessage();
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		try {
			this.insertTemporalPattern(temporalPattern);
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

	private void insertTemporalPattern(TemporalPattern temporalPattern) throws CreateObjectException {
		String tpIdCode = temporalPattern.getId().getCode();
		String sql = SQL_INSERT_INTO + ObjectEntities.TEMPORALPATTERN_ENTITY 
			+ OPEN_BRACKET
			+ COLUMN_ID + COMMA
			+ COLUMN_CREATED + COMMA
			+ COLUMN_MODIFIED + COMMA
			+ COLUMN_CREATOR_ID + COMMA
			+ COLUMN_MODIFIER_ID + COMMA
			+ COLUMN_DESCRIPTION + COMMA
			+ COLUMN_VALUE
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		try {			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, tpIdCode);
			preparedStatement.setTimestamp(2, new Timestamp(temporalPattern.getCreated().getTime()));
			preparedStatement.setTimestamp(3, new Timestamp(temporalPattern.getModified().getTime()));
			preparedStatement.setString(4, temporalPattern.getCreatorId().getCode());
			preparedStatement.setString(5, temporalPattern.getModifierId().getCode());
			preparedStatement.setString(6, temporalPattern.getDescription());
			((OraclePreparedStatement)preparedStatement).setORAData(7, new CronStringArray(temporalPattern.getCronStrings()));
			Log.debugMessage("TemporalPatternDatabase.insertTemporalPattern | Inserting temporal pattern " + tpIdCode, Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.insertTemporalPattern | Cannot insert temporal pattern '" + tpIdCode + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, int updateKind, Object arg) throws IllegalDataException, UpdateObjectException {
		TemporalPattern temporalPattern = this.fromStorableObject(storableObject);
		switch (updateKind) {
			default:
				break;
		}
	}
	
	public List retrieveAll() throws RetrieveObjectException {
		List temporalPatterns = new ArrayList(CHARACTER_NUMBER_OF_RECORDS);
		String sql = SQL_SELECT
				+ COLUMN_ID
				+ SQL_FROM + ObjectEntities.TEMPORALPATTERN_ENTITY;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TemporalPatternDatabase.retrieveAll | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next())
				temporalPatterns.add(new TemporalPattern(new Identifier(resultSet.getString(COLUMN_ID))));			
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.retrieveAll | Cannot retrieve temporal pattern -- " + sqle.getMessage();
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
		return temporalPatterns;
	}

	public void delete(TemporalPattern temporalPattern) {
		String tpIdStr = temporalPattern.getId().toSQLString();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = SQL_DELETE_FROM
						+ ObjectEntities.TEMPORALPATTERN_ENTITY
						+ SQL_WHERE
						+ COLUMN_ID + EQUALS
						+ tpIdStr;
			Log.debugMessage("TemporalPatternDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
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
			}
		}
	}

	public List retrieveByIds(List ids) throws RetrieveObjectException {
		if ((ids == null) || (ids.isEmpty()))
			return new LinkedList();
		return retriveByIdsOneQuery(ids);	
		//return retriveByIdsPreparedStatement(ids);
	}
	
	private List retriveByIdsOneQuery(List ids) throws RetrieveObjectException {
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
			sql = retrieveTemporalPatternQuery(buffer.toString());
		}
		
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			Log.debugMessage("TemporalPatternDatabase.retriveByIdsOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()){
				result.add(updateTemporalPatternFromResultSet(null, resultSet));
			}
		}
		catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.retriveByIdsOneQuery | Cannot execute query " + sqle.getMessage();
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
			
			sql =retrieveTemporalPatternQuery(buffer.toString());
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
					result.add(updateTemporalPatternFromResultSet(null, resultSet));
				} else{
					Log.errorMessage("TemporalPatternDatabase.retriveByIdsPreparedStatement | No such temporal pattern: " + idStr);									
				}
				
			}
		}catch (SQLException sqle) {
			String mesg = "TemporalPatternDatabase.retriveByIdsPreparedStatement | Cannot retrieve temporal pattern " + sqle.getMessage();
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
