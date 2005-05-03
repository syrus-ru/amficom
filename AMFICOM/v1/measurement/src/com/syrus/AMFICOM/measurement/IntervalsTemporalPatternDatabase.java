/*-
 * $Id: IntervalsTemporalPatternDatabase.java,v 1.1 2005/05/03 13:57:30 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/03 13:57:30 $
 * @author $Author: max $
 * @module measurement_v1
 */
public class IntervalsTemporalPatternDatabase extends StorableObjectDatabase {
	
	private static String columns;

	private static String updateMultipleSQLValues;

	private static final int	TEMPORAL_PATTERN_ID_ROW	= 0;

	private static final int	DURATION_ROW	= 1;
	
	private IntervalsTemporalPattern fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof IntervalsTemporalPattern)
			return (IntervalsTemporalPattern) storableObject;
		throw new IllegalDataException("IntervalsTemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}
	
	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		super.retrieveEntity(intervalsTemporalPattern);
	}
	
	protected String getEnityName() {		
		return ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY;
	}
	
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = IntervalsTemporalPatternWrapper.COLUMN_NAME;
		}
		return columns;
	}
	
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION;
		}
		return updateMultipleSQLValues;
	}
	
	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		String sql = APOSTOPHE + intervalsTemporalPattern.getName() + APOSTOPHE;
		return sql;
	}
	
	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {		
		IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		preparedStatement.setString(++startParameterNumber, intervalsTemporalPattern.getName());
		return startParameterNumber;
	}
	
	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		SortedMap absMap = new TreeMap();
		SortedMap durMap = new TreeMap();
		
		IntervalsTemporalPattern intervalsTemporalPattern;
		if (storableObject == null) 
			intervalsTemporalPattern = new IntervalsTemporalPattern(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
					null, 0L, null, null);
		else
			intervalsTemporalPattern = this.fromStorableObject(storableObject);
		
		Identifier intervalsTemporalPatternId = intervalsTemporalPattern.getId();
		Map tableMap = getMapsFromDB(intervalsTemporalPatternId);
		for (Iterator it = tableMap.keySet().iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			List row = (List)tableMap.get(offset);
			absMap.put(offset, row.get(TEMPORAL_PATTERN_ID_ROW));
			durMap.put(offset, row.get(DURATION_ROW));
		}
		intervalsTemporalPattern.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
				resultSet.getString(IntervalsTemporalPatternWrapper.COLUMN_NAME),
				absMap,
				durMap);
		return intervalsTemporalPattern;
	}
	
	private Map getMapsFromDB(Identifier intervalsTemporalPatternId) throws RetrieveObjectException {
		Map tableMap = new HashMap();
		String sql = SQL_SELECT + IntervalsTemporalPatternWrapper.COLUMN_OFFSET + COMMA 
				+ IntervalsTemporalPatternWrapper.COLUMN_TEMPORAL_PATTREN_ID + COMMA
				+ IntervalsTemporalPatternWrapper.COLUMN_DURATION
				+ SQL_FROM  + IntervalsTemporalPatternWrapper.OFFSET_TEMP_PATTERN_AND_DURATION_TABLE
				+ SQL_WHERE + IntervalsTemporalPatternWrapper.COLUMN_INTERVALS_TEMPORAL_PARENT_ID 
				+ EQUALS + DatabaseIdentifier.toSQLString(intervalsTemporalPatternId);
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("IntervalsTemporalPatternDatabase.getMapsFromDB | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				long offset = resultSet.getLong(IntervalsTemporalPatternWrapper.COLUMN_OFFSET);
				long duration = resultSet.getLong(IntervalsTemporalPatternWrapper.COLUMN_DURATION);
				Identifier temporalPatternId = DatabaseIdentifier.getIdentifier(resultSet, IntervalsTemporalPatternWrapper.COLUMN_TEMPORAL_PATTREN_ID);
				List row = new ArrayList(2);
				row.add(TEMPORAL_PATTERN_ID_ROW, temporalPatternId);
				row.add(DURATION_ROW, new Long(duration));
				tableMap.put(new Long(offset), row);
				
			}
		return tableMap;
		}
		catch (SQLException sqle) {
			String mesg = "IntervalsTemporalPatternDatabase.getMapsFromDB | Cannot retrieve " + getEnityName()
					+ " '" + intervalsTemporalPatternId + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (resultSet != null)
						resultSet.close();
				}
				finally {
					try {
						if (statement != null)
							statement.close();
					}
					finally {
						DatabaseConnection.releaseConnection(connection);
					}
				}
			}
			catch (SQLException sqle) {
				Log.errorException(sqle);
			}
		}
		
	}
	
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  intervalsTemporalPattern.getId() + "'; argument: " + arg);
				return null;
		}
	}
	
	public void update(StorableObject storableObject, Identifier modifierId,
			int updateKind) throws VersionCollisionException,
			UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			this.updateLinkedTable(storableObject);
		} catch (IllegalDataException e) {
			throw new UpdateObjectException(e.getMessage());
		}
	}
	
	public void delete(Identifier id) {
		super.delete(id);
		this.remove(id);
	}
	
	private void remove(Identifier id) {
		String sql = SQL_DELETE_FROM + IntervalsTemporalPatternWrapper.OFFSET_TEMP_PATTERN_AND_DURATION_TABLE 
		+ SQL_WHERE + IntervalsTemporalPatternWrapper.COLUMN_INTERVALS_TEMPORAL_PARENT_ID
		+ EQUALS + DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.prepareStatement(sql);
			Log.debugMessage("IntervalsTemporalPatternDatabase.remove | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "IntervalsTemporalPatternDatabase.remove | Cannot remove " + this.getEnityName()
					+ " '" + id + "' -- " + sqle.getMessage();
			Log.errorMessage(mesg);
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	public void delete(Set identifiables) {
		super.delete(identifiables);
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.delete(id);
		}
	}
	
	private void updateLinkedTable(StorableObject storableObject) throws UpdateObjectException, IllegalDataException {
		IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		Identifier intervalsTemporalPatternId = intervalsTemporalPattern.getId();
		SortedMap abstractMap = intervalsTemporalPattern.getIntervalsAbstractTemporalPatternMap();
		SortedMap durationMap = intervalsTemporalPattern.getIntervalsDuration();
		
		Map dbTableMap;
		
		try {
			dbTableMap = getMapsFromDB(intervalsTemporalPatternId);
		} catch (RetrieveObjectException e) {
			throw new UpdateObjectException(e);
		}
		// Remove
		Set setToRemove = new HashSet();
		for (Iterator it = dbTableMap.keySet().iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			if (abstractMap.get(offset) == null)
				setToRemove.add(offset);
		}
		removeFromDB(intervalsTemporalPatternId, setToRemove);
		//Update
		Map updateMap = new HashMap();
		Map insertMap = new HashMap();
		for (Iterator it = abstractMap.keySet().iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			List dbRow = (List) dbTableMap.get(offset);
			Identifier temporalPatternId = (Identifier) abstractMap.get(offset);
			Long duration = (Long) durationMap.get(offset);
			if(dbRow != null) {
				if(dbRow.get(TEMPORAL_PATTERN_ID_ROW) != temporalPatternId 
						|| dbRow.get(DURATION_ROW) != duration) {
					List row = new ArrayList(2);
					row.add(TEMPORAL_PATTERN_ID_ROW, temporalPatternId);
					row.add(DURATION_ROW, duration);
					updateMap.put(offset, row);
				}
					
			} else {
				List row = new ArrayList(2);
				row.add(TEMPORAL_PATTERN_ID_ROW, temporalPatternId);
				row.add(DURATION_ROW, duration);
				insertMap.put(offset, row);
			}
		}
		updateDB(intervalsTemporalPatternId, updateMap, MODE_UPDATE);
		updateDB(intervalsTemporalPatternId, insertMap, MODE_INSERT);
	}

	private void updateDB(Identifier intervalsTemporalPatternId, Map updateMap, int updateMode) throws UpdateObjectException {
		String sql;
		if(updateMode == MODE_UPDATE)
			sql = SQL_UPDATE + IntervalsTemporalPatternWrapper.OFFSET_TEMP_PATTERN_AND_DURATION_TABLE 
					+ SQL_SET 
					+ IntervalsTemporalPatternWrapper.COLUMN_TEMPORAL_PATTREN_ID 
					+ EQUALS + QUESTION + COMMA
					+ IntervalsTemporalPatternWrapper.COLUMN_DURATION
					+ EQUALS + QUESTION 
					+ SQL_WHERE + IntervalsTemporalPatternWrapper.COLUMN_INTERVALS_TEMPORAL_PARENT_ID 
					+ EQUALS + DatabaseIdentifier.toSQLString(intervalsTemporalPatternId)  
					+ SQL_AND + IntervalsTemporalPatternWrapper.COLUMN_OFFSET 
					+ EQUALS + QUESTION; 
		else if(updateMode == MODE_INSERT)
			sql = SQL_INSERT_INTO + IntervalsTemporalPatternWrapper.OFFSET_TEMP_PATTERN_AND_DURATION_TABLE 
					+ OPEN_BRACKET
					+ IntervalsTemporalPatternWrapper.COLUMN_INTERVALS_TEMPORAL_PARENT_ID + COMMA
					+ IntervalsTemporalPatternWrapper.COLUMN_TEMPORAL_PATTREN_ID + COMMA
					+ IntervalsTemporalPatternWrapper.COLUMN_DURATION + COMMA
					+ IntervalsTemporalPatternWrapper.COLUMN_OFFSET 
					+ CLOSE_BRACKET
					+ SQL_VALUES
					+ OPEN_BRACKET
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION
					+ CLOSE_BRACKET;
		else throw new UpdateObjectException("Unsupported updateMode " + updateMode);
		
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("IntervalsTemporalPatternDatabase.updateDB | Trying: " + sql, Log.DEBUGLEVEL09);
			for (Iterator it = updateMap.keySet().iterator(); it.hasNext();) {
				Long offset = (Long) it.next();
				List row = (List)updateMap.get(offset);
				Identifier temporalPatternId = (Identifier) row.get(TEMPORAL_PATTERN_ID_ROW);
				Long duration = (Long) row.get(DURATION_ROW);
				int i = 1;
				if(updateMode == MODE_INSERT)
					DatabaseIdentifier.setIdentifier(preparedStatement, i++, intervalsTemporalPatternId);
				DatabaseIdentifier.setIdentifier(preparedStatement, i++, temporalPatternId);
				if(duration == null)
					preparedStatement.setLong(i++, 0L);
				else
					preparedStatement.setLong(i++, duration.longValue());
				preparedStatement.setLong(i++, offset.longValue());
				preparedStatement.executeUpdate();
			}

			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "IntervalsTemporalPatternDatabase.updateDB | Cannot update " + this.getEnityName()
					+ " '" + intervalsTemporalPatternId + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public void update(Set storableObjects, Identifier modifierId,
			int updateKind) throws VersionCollisionException,
			UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			try {
				updateLinkedTable((StorableObject)it.next());
			} catch (IllegalDataException e) {
				throw new UpdateObjectException(e.getMessage(),e);
			}			
		}
	}

	private void removeFromDB(Identifier intervalsTemporalPatternId, Set setToRemove) throws UpdateObjectException {
		String sql = SQL_DELETE_FROM + IntervalsTemporalPatternWrapper.OFFSET_TEMP_PATTERN_AND_DURATION_TABLE 
				+ SQL_WHERE + IntervalsTemporalPatternWrapper.COLUMN_INTERVALS_TEMPORAL_PARENT_ID
				+ EQUALS + DatabaseIdentifier.toSQLString(intervalsTemporalPatternId) 
				+ SQL_AND + IntervalsTemporalPatternWrapper.COLUMN_OFFSET 
				+ EQUALS + QUESTION;
		
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("IntervalsTemporalPatternDatabase.removeFromDB | Trying: " + sql, Log.DEBUGLEVEL09);
			for (Iterator it = setToRemove.iterator(); it.hasNext();) {
				Long offset = (Long) it.next();
				preparedStatement.setLong(1, offset.longValue());
				preparedStatement.executeUpdate();
			}

			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "IntervalsTemporalPatternDatabase.removeFromDB | Cannot remove " + this.getEnityName()
					+ " '" + intervalsTemporalPatternId + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
	
	public void insert(StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		super.insertEntity(intervalsTemporalPattern);
		insertInLinkedTable(intervalsTemporalPattern);
		
	}
	
	private void insertInLinkedTable(IntervalsTemporalPattern intervalsTemporalPattern) throws CreateObjectException {

		SortedMap abstractMap = intervalsTemporalPattern.getIntervalsAbstractTemporalPatternMap();
		SortedMap durationMap = intervalsTemporalPattern.getIntervalsDuration();
		Map mapTable = new HashMap();
		for (Iterator it = abstractMap.keySet().iterator(); it.hasNext();) {
			Long offset = (Long) it.next();
			List row = new ArrayList(2);
			row.add(TEMPORAL_PATTERN_ID_ROW, abstractMap.get(offset));
			row.add(DURATION_ROW, durationMap.get(offset));
			mapTable.put(offset, row);
		}
		
		try {
			updateDB(intervalsTemporalPattern.getId(), mapTable, MODE_INSERT);
		} catch (UpdateObjectException e) {
			throw new CreateObjectException(e.getMessage(), e);
		}
	}
	
	public void insert(Set storableObjects) throws IllegalDataException,
			CreateObjectException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) it.next();
			insertInLinkedTable(intervalsTemporalPattern);			
		}
	}
}	
	