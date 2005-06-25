/*-
 * $Id: IntervalsTemporalPatternDatabase.java,v 1.8 2005/06/25 10:57:48 bass Exp $
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
import com.syrus.AMFICOM.general.Identifiable;
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
 * @version $Revision: 1.8 $, $Date: 2005/06/25 10:57:48 $
 * @author $Author: bass $
 * @module measurement_v1
 */
public final class IntervalsTemporalPatternDatabase extends StorableObjectDatabase {

	private static String columns;

	private static String updateMultipleSQLValues;

	private static final int	TEMPORAL_PATTERN_ID_ROW	= 0;

	private static final int	DURATION_ROW	= 1;

	private IntervalsTemporalPattern fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof IntervalsTemporalPattern)
			return (IntervalsTemporalPattern) storableObject;
		throw new IllegalDataException("IntervalsTemporalPatternDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		super.retrieveEntity(intervalsTemporalPattern);
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.INTERVALSTEMPORALPATTERN_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = IntervalsTemporalPatternWrapper.COLUMN_NAME;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		final String sql = APOSTOPHE + intervalsTemporalPattern.getName() + APOSTOPHE;
		return sql;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {		
		final IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		preparedStatement.setString(++startParameterNumber, intervalsTemporalPattern.getName());
		return startParameterNumber;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final SortedMap<Long, Identifier> absMap = new TreeMap<Long, Identifier>();
		final SortedMap<Long, Long> durMap = new TreeMap<Long, Long>();

		IntervalsTemporalPattern intervalsTemporalPattern;
		if (storableObject == null) {
			intervalsTemporalPattern = new IntervalsTemporalPattern(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_ID), null, 0L, null, null);
		} else {
			intervalsTemporalPattern = this.fromStorableObject(storableObject);
		}

		final Identifier intervalsTemporalPatternId = intervalsTemporalPattern.getId();
		final Map<Long, List<Object>> tableMap = this.getMapsFromDB(intervalsTemporalPatternId);
		for (final Long offset : tableMap.keySet()) {
			final List<Object> row = tableMap.get(offset);
			absMap.put(offset, (Identifier) row.get(TEMPORAL_PATTERN_ID_ROW));
			durMap.put(offset, (Long) row.get(DURATION_ROW));
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
	
	private Map<Long, List<Object>> getMapsFromDB(final Identifier intervalsTemporalPatternId) throws RetrieveObjectException {
		final Map<Long, List<Object>> tableMap = new HashMap<Long, List<Object>>();
		final String sql = SQL_SELECT + IntervalsTemporalPatternWrapper.COLUMN_OFFSET + COMMA
				+ IntervalsTemporalPatternWrapper.COLUMN_TEMPORAL_PATTREN_ID + COMMA
				+ IntervalsTemporalPatternWrapper.COLUMN_DURATION
				+ SQL_FROM  + IntervalsTemporalPatternWrapper.OFFSET_TEMP_PATTERN_AND_DURATION_TABLE
				+ SQL_WHERE + IntervalsTemporalPatternWrapper.COLUMN_INTERVALS_TEMPORAL_PARENT_ID
				+ EQUALS + DatabaseIdentifier.toSQLString(intervalsTemporalPatternId);
		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("IntervalsTemporalPatternDatabase.getMapsFromDB | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				long offset = resultSet.getLong(IntervalsTemporalPatternWrapper.COLUMN_OFFSET);
				long duration = resultSet.getLong(IntervalsTemporalPatternWrapper.COLUMN_DURATION);
				Identifier temporalPatternId = DatabaseIdentifier.getIdentifier(resultSet, IntervalsTemporalPatternWrapper.COLUMN_TEMPORAL_PATTREN_ID);
				List<Object> row = new ArrayList<Object>(2);
				row.add(TEMPORAL_PATTERN_ID_ROW, temporalPatternId);
				row.add(DURATION_ROW, new Long(duration));
				tableMap.put(new Long(offset), row);
			}
			return tableMap;
		} catch (SQLException sqle) {
			final String mesg = "IntervalsTemporalPatternDatabase.getMapsFromDB | Cannot retrieve " + getEntityName()
					+ " '" + intervalsTemporalPatternId + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null)
						resultSet.close();
				} finally {
					try {
						if (statement != null)
							statement.close();
					} finally {
						DatabaseConnection.releaseConnection(connection);
					}
				}
			} catch (SQLException sqle) {
				Log.errorException(sqle);
			}
		}

	}

	@Override
	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  intervalsTemporalPattern.getId() + "'; argument: " + arg);
				return null;
		}
	}

	@Override
	public void update(StorableObject storableObject, Identifier modifierId, UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			this.updateLinkedTable(storableObject);
		} catch (IllegalDataException e) {
			throw new UpdateObjectException(e.getMessage());
		}
	}

	@Override
	public void update(Set storableObjects, Identifier modifierId, UpdateKind updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			try {
				updateLinkedTable((StorableObject)it.next());
			} catch (IllegalDataException e) {
				throw new UpdateObjectException(e.getMessage(),e);
			}			
		}
	}

	@Override
	public void delete(final Identifier id) {
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
		} catch (SQLException sqle) {
			String mesg = "IntervalsTemporalPatternDatabase.remove | Cannot remove " + this.getEntityName()
					+ " '" + id + "' -- " + sqle.getMessage();
			Log.errorMessage(mesg);
		} finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	@Override
	public void delete(final Set<? extends Identifiable> identifiables) {
		super.delete(identifiables);
		for (final Identifiable identifiable : identifiables) {
			this.delete(identifiable.getId());
		}
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.delete(id);
		}
	}
	
	private void updateLinkedTable(final StorableObject storableObject) throws UpdateObjectException, IllegalDataException {
		final IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		final Identifier intervalsTemporalPatternId = intervalsTemporalPattern.getId();
		final SortedMap<Long, Identifier> abstractMap = intervalsTemporalPattern.getIntervalsAbstractTemporalPatternMap();
		final SortedMap<Long, Long> durationMap = intervalsTemporalPattern.getIntervalsDuration();

		Map<Long, List<Object>> dbTableMap;

		try {
			dbTableMap = this.getMapsFromDB(intervalsTemporalPatternId);
		} catch (RetrieveObjectException e) {
			throw new UpdateObjectException(e);
		}
		// Remove
		final Set<Long> setToRemove = new HashSet<Long>();
		for (final Long offset : dbTableMap.keySet()) {
			if (abstractMap.get(offset) == null)
				setToRemove.add(offset);
		}
		this.removeFromDB(intervalsTemporalPatternId, setToRemove);
		//Update
		final Map<Long, List<Object>> updateMap = new HashMap<Long, List<Object>>();
		final Map<Long, List<Object>> insertMap = new HashMap<Long, List<Object>>();
		for (final Long offset : abstractMap.keySet()) {
			final Identifier temporalPatternId = abstractMap.get(offset);
			final Long duration = durationMap.get(offset);
			final List<Object> dbRow = dbTableMap.get(offset);
			if (dbRow != null) {
				final Identifier dbTemporalPatternId = (Identifier) dbRow.get(TEMPORAL_PATTERN_ID_ROW);
				final Long dbDuration = (Long) dbRow.get(DURATION_ROW);
				if (!dbTemporalPatternId.equals(temporalPatternId) && !dbDuration.equals(duration)) {
					final List<Object> row = new ArrayList<Object>(2);
					row.add(TEMPORAL_PATTERN_ID_ROW, temporalPatternId);
					row.add(DURATION_ROW, duration);
					updateMap.put(offset, row);
				}
			} else {
				final List<Object> row = new ArrayList<Object>(2);
				row.add(TEMPORAL_PATTERN_ID_ROW, temporalPatternId);
				row.add(DURATION_ROW, duration);
				insertMap.put(offset, row);
			}
		}
		updateDB(intervalsTemporalPatternId, updateMap, MODE_UPDATE);
		updateDB(intervalsTemporalPatternId, insertMap, MODE_INSERT);
	}

	private void updateDB(final Identifier intervalsTemporalPatternId, final Map<Long, List<Object>> updateMap, final int updateMode)
			throws UpdateObjectException {
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
		final Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("IntervalsTemporalPatternDatabase.updateDB | Trying: " + sql, Log.DEBUGLEVEL09);
			for (final Long offset : updateMap.keySet()) {
				final List<Object> row = updateMap.get(offset);
				final Identifier temporalPatternId = (Identifier) row.get(TEMPORAL_PATTERN_ID_ROW);
				final Long duration = (Long) row.get(DURATION_ROW);
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
		} catch (SQLException sqle) {
			String mesg = "IntervalsTemporalPatternDatabase.updateDB | Cannot update " + this.getEntityName()
					+ " '" + intervalsTemporalPatternId + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void removeFromDB(final Identifier intervalsTemporalPatternId, final Set<Long> setToRemove) throws UpdateObjectException {
		final String sql = SQL_DELETE_FROM + IntervalsTemporalPatternWrapper.OFFSET_TEMP_PATTERN_AND_DURATION_TABLE
				+ SQL_WHERE + IntervalsTemporalPatternWrapper.COLUMN_INTERVALS_TEMPORAL_PARENT_ID
				+ EQUALS + DatabaseIdentifier.toSQLString(intervalsTemporalPatternId)
				+ SQL_AND + IntervalsTemporalPatternWrapper.COLUMN_OFFSET
				+ EQUALS + QUESTION;
		
		PreparedStatement preparedStatement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			Log.debugMessage("IntervalsTemporalPatternDatabase.removeFromDB | Trying: " + sql, Log.DEBUGLEVEL09);
			for (final Long offset : setToRemove) {
				preparedStatement.setLong(1, offset.longValue());
				preparedStatement.executeUpdate();
			}

			connection.commit();
		} catch (SQLException sqle) {
			String mesg = "IntervalsTemporalPatternDatabase.removeFromDB | Cannot remove " + this.getEntityName()
					+ " '" + intervalsTemporalPatternId + "' -- " + sqle.getMessage();
			throw new UpdateObjectException(mesg, sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	@Override
	public void insert(final StorableObject storableObject)
			throws IllegalDataException, CreateObjectException {
		final IntervalsTemporalPattern intervalsTemporalPattern = this.fromStorableObject(storableObject);
		super.insertEntity(intervalsTemporalPattern);
		insertInLinkedTable(intervalsTemporalPattern);
	}

	@Override
	public void insert(final Set storableObjects) throws CreateObjectException {
		for (final Iterator it = storableObjects.iterator(); it.hasNext();) {
			IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) it.next();
			insertInLinkedTable(intervalsTemporalPattern);			
		}
	}

	private void insertInLinkedTable(final IntervalsTemporalPattern intervalsTemporalPattern) throws CreateObjectException {
		final SortedMap<Long, Identifier> abstractMap = intervalsTemporalPattern.getIntervalsAbstractTemporalPatternMap();
		final SortedMap<Long, Long> durationMap = intervalsTemporalPattern.getIntervalsDuration();
		final Map<Long, List<Object>> mapTable = new HashMap<Long, List<Object>>();
		for (final Long offset : abstractMap.keySet()) {
			final List<Object> row = new ArrayList<Object>(2);
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
}	
