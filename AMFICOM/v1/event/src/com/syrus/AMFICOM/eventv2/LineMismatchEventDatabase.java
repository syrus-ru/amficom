/*-
 * $Id: LineMismatchEventDatabase.java,v 1.10 2006/06/29 16:12:39 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_AFFECTED_PATH_ELEMENT_ID;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_ALARM_STATUS;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MISMATCH_OPTICAL_DISTANCE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MISMATCH_PHYSICAL_DISTANCE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PHYSICAL_DISTANCE_TO_END;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PHYSICAL_DISTANCE_TO_START;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PLAIN_TEXT_MESSAGE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_RICH_TEXT_MESSAGE;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static java.sql.Types.VARCHAR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.syrus.AMFICOM.eventv2.LineMismatchEvent.AlarmStatus;
import com.syrus.AMFICOM.eventv2.LineMismatchEvent.ChangeLogRecord;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionableDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2006/06/29 16:12:39 $
 * @module event
 */
public final class LineMismatchEventDatabase
		extends VersionableDatabase<DefaultLineMismatchEvent> {
	private static final int SIZE_PLAIN_TEXT_MESSAGE_COLUMN = 4000;

	private static final int SIZE_RICH_TEXT_MESSAGE_COLUMN = 4000;

	private static final String TABLE_CHANGE_LOG_RECORD = "ChangeLogRecord";

	/*-********************************************************************
	 * Columns of ChangeLogRecord table.                                  *
	 **********************************************************************/

	private static final String COLUMN_PARENT_LINE_MISMATCH_EVENT_ID = "parent_line_mismatch_event_id";

	private static final String COLUMN_MODIFIED = "modified";

	private static final String COLUMN_KEY = "key";

	private static final String COLUMN_OLD_VALUE = "old_value";

	private static final String COLUMN_NEW_VALUE = "new_value";


	private static String columns;

	private static String updateMultipleSQLValues;

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getEntityCode()
	 */
	@Override
	protected short getEntityCode() {
		return LINEMISMATCHEVENT_CODE;
	}

	/**
	 * @see VersionableDatabase#getExpectedVersion()
	 */
	@Override
	protected String getExpectedVersion() {
		return "1.7";
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getColumnsTmpl()
	 */
	@Override
	protected String getColumnsTmpl() {
		return columns == null
				? columns = COLUMN_AFFECTED_PATH_ELEMENT_ID + COMMA
						+ COLUMN_PHYSICAL_DISTANCE_TO_START + COMMA
						+ COLUMN_PHYSICAL_DISTANCE_TO_END + COMMA
						+ COLUMN_MISMATCH_OPTICAL_DISTANCE + COMMA
						+ COLUMN_MISMATCH_PHYSICAL_DISTANCE + COMMA
						+ COLUMN_PLAIN_TEXT_MESSAGE + COMMA
						+ COLUMN_RICH_TEXT_MESSAGE + COMMA
						+ COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID + COMMA
						+ COLUMN_ALARM_STATUS + COMMA
						+ LineMismatchEventWrapper.COLUMN_PARENT_LINE_MISMATCH_EVENT_ID
				: columns;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateMultipleSQLValuesTmpl()
	 */
	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		return updateMultipleSQLValues == null
				? updateMultipleSQLValues = QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION
				: updateMultipleSQLValues;
	}

	/**
	 * @param lineMismatchEvent
	 * @throws IllegalDataException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final DefaultLineMismatchEvent lineMismatchEvent)
	throws IllegalDataException {
		final AlarmStatus alarmStatus = lineMismatchEvent.alarmStatus.getValue();

		return DatabaseIdentifier.toSQLString(lineMismatchEvent.getAffectedPathElementId()) + COMMA
				+ (lineMismatchEvent.isAffectedPathElementSpacious()
						? Double.toString(lineMismatchEvent.getPhysicalDistanceToStart()) + COMMA
								+ Double.toString(lineMismatchEvent.getPhysicalDistanceToEnd()) + COMMA
						: SQL_NULL + COMMA
								+ SQL_NULL + COMMA)
				+ Double.toString(lineMismatchEvent.getMismatchOpticalDistance()) + COMMA
				+ Double.toString(lineMismatchEvent.getMismatchPhysicalDistance()) + COMMA
				+ APOSTROPHE + DatabaseString. toQuerySubString(lineMismatchEvent.getPlainTextMessage(), SIZE_PLAIN_TEXT_MESSAGE_COLUMN) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString. toQuerySubString(lineMismatchEvent.getRichTextMessage(), SIZE_RICH_TEXT_MESSAGE_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(lineMismatchEvent.getReflectogramMismatchEventId()) + COMMA
				+ (alarmStatus == null
						? SQL_NULL + COMMA
								+ DatabaseIdentifier.toSQLString(lineMismatchEvent.getParentLineMismatchEventId())
						: Integer.toString(alarmStatus.ordinal()) + COMMA
								+ DatabaseIdentifier.voidToSQLString());
	}

	/**
	 * @param lineMismatchEvent
	 * @param preparedStatement
	 * @param initialStartParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, java.sql.PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final DefaultLineMismatchEvent lineMismatchEvent,
			final PreparedStatement preparedStatement,
			final int initialStartParameterNumber)
	throws IllegalDataException, SQLException {
		final AlarmStatus alarmStatus = lineMismatchEvent.alarmStatus.getValue();

		int startParameterNumber = initialStartParameterNumber;

		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, lineMismatchEvent.getAffectedPathElementId());
		if (lineMismatchEvent.isAffectedPathElementSpacious()) {
			preparedStatement.setDouble(++startParameterNumber, lineMismatchEvent.getPhysicalDistanceToStart());
			preparedStatement.setDouble(++startParameterNumber, lineMismatchEvent.getPhysicalDistanceToEnd());
		} else {
			preparedStatement.setNull(++startParameterNumber, Types.DOUBLE);
			preparedStatement.setNull(++startParameterNumber, Types.DOUBLE);
		}
		preparedStatement.setDouble(++startParameterNumber, lineMismatchEvent.getMismatchOpticalDistance());
		preparedStatement.setDouble(++startParameterNumber, lineMismatchEvent.getMismatchPhysicalDistance());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, lineMismatchEvent.getPlainTextMessage(), SIZE_PLAIN_TEXT_MESSAGE_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, lineMismatchEvent.getRichTextMessage(), SIZE_RICH_TEXT_MESSAGE_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, lineMismatchEvent.getReflectogramMismatchEventId());
		if (alarmStatus == null) {
			preparedStatement.setNull(++startParameterNumber, Types.INTEGER);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, lineMismatchEvent.getParentLineMismatchEventId());
		} else {
			preparedStatement.setInt(++startParameterNumber, alarmStatus.ordinal());
			DatabaseIdentifier.setVoid(preparedStatement, ++startParameterNumber);
		}
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, java.sql.ResultSet)
	 */
	@Override
	protected DefaultLineMismatchEvent updateEntityFromResultSet(
			final DefaultLineMismatchEvent storableObject,
			final ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
		final DefaultLineMismatchEvent lineMismatchEvent = (storableObject == null)
				? new DefaultLineMismatchEvent(id)
				: storableObject;


		final boolean affectedPathElementSpacious;
		final double physicalDistanceToStart;
		final double physicalDistanceToEnd;
		{
			physicalDistanceToStart = resultSet.getDouble(COLUMN_PHYSICAL_DISTANCE_TO_START);
			final boolean probablyAffectedPathElementSpacious1 = !resultSet.wasNull();
			physicalDistanceToEnd = resultSet.getDouble(COLUMN_PHYSICAL_DISTANCE_TO_END);
			final boolean probablyAffectedPathElementSpacious2 = !resultSet.wasNull();
			if (probablyAffectedPathElementSpacious1 ^ probablyAffectedPathElementSpacious2) {
				throw new RetrieveObjectException("id: " + id
						+ "; physicalDistanceToStart: " + physicalDistanceToStart
						+ "; physicalDistanceToEnd: " + physicalDistanceToEnd
						+ "; probablyAffectedPathElementSpacious1: " + probablyAffectedPathElementSpacious1
						+ "; probablyAffectedPathElementSpacious2: " + probablyAffectedPathElementSpacious2);
			}
			affectedPathElementSpacious = probablyAffectedPathElementSpacious1;
			assert affectedPathElementSpacious || (physicalDistanceToStart == 0 && physicalDistanceToEnd == 0) :
					"affectedPathElementSpacious: " + affectedPathElementSpacious
					+ "; physicalDistanceToStart: " + physicalDistanceToStart
					+ "; physicalDistanceToEnd: " + physicalDistanceToEnd;
		}


		final AlarmStatus alarmStatus;
		final Identifier parentLineMismatchEventId;
		{
			/*
			 * Do not inline!
			 */
			final int ordinal = resultSet.getInt(COLUMN_ALARM_STATUS);
			alarmStatus = resultSet.wasNull()
					? null
					: AlarmStatus.valueOf(ordinal);
			parentLineMismatchEventId = DatabaseIdentifier.getIdentifier(resultSet, LineMismatchEventWrapper.COLUMN_PARENT_LINE_MISMATCH_EVENT_ID);
			if (alarmStatus != null ^ parentLineMismatchEventId.isVoid()) {
				throw new RetrieveObjectException("id: " + id
						+ "; alarmStatus: " + alarmStatus
						+ "; parentLineMismatchEventId: " + parentLineMismatchEventId);
			}
		}


		lineMismatchEvent.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_AFFECTED_PATH_ELEMENT_ID),
				affectedPathElementSpacious,
				physicalDistanceToStart,
				physicalDistanceToEnd,
				resultSet.getDouble(COLUMN_MISMATCH_OPTICAL_DISTANCE),
				resultSet.getDouble(COLUMN_MISMATCH_PHYSICAL_DISTANCE),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_PLAIN_TEXT_MESSAGE)),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_RICH_TEXT_MESSAGE)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID),
				alarmStatus,
				parentLineMismatchEventId);
		return lineMismatchEvent;
	}

	/**
	 * @param conditionQuery
	 * @throws RetrieveObjectException
	 * @throws IllegalDataException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#retrieveByCondition(String)
	 */
	@Override
	protected Set<DefaultLineMismatchEvent> retrieveByCondition(
			final String conditionQuery)
	throws RetrieveObjectException, IllegalDataException {
		try {
			final Set<DefaultLineMismatchEvent> lineMismatchEvents =
					super.retrieveByCondition(conditionQuery);
			final Map<Identifier, Set<ChangeLogRecord>> changeLogs =
					this.selectChangeLogs(lineMismatchEvents);
			for (final DefaultLineMismatchEvent lineMismatchEvent : lineMismatchEvents) {
				final Set<ChangeLogRecord> changeLog = changeLogs.get(lineMismatchEvent.getId());
				lineMismatchEvent.setChangeLog0(changeLog);
			}
			return lineMismatchEvents;
		} catch (final SQLException sqle) {
			throw new RetrieveObjectException(sqle);
		}
	}

	/**
	 * @param lineMismatchEvents
	 * @throws IllegalDataException
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#insert(Set)
	 */
	@Override
	protected void insert(
			final Set<DefaultLineMismatchEvent> lineMismatchEvents)
	throws IllegalDataException, CreateObjectException {
		try {
			super.insert(lineMismatchEvents);

			final Map<Identifier, Set<ChangeLogRecord>> pendingChangeLogs = new HashMap<Identifier, Set<ChangeLogRecord>>();
			for (final LineMismatchEvent lineMismatchEvent : lineMismatchEvents) {
				pendingChangeLogs.put(
						lineMismatchEvent.getId(),
						lineMismatchEvent.getChangeLog());
			}

			this.insertChangeLogs(pendingChangeLogs);
		} catch (final SQLException sqle) {
			throw new CreateObjectException(sqle);
		}
	}

	/**
	 * {@code ChangeLogRecord}s are immutable, so existing ones aren&apos;t
	 * updated.
	 *
	 * @param lineMismatchEvents
	 * @throws UpdateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObjectDatabase#update(Set)
	 */
	@Override
	protected void update(
			final Set<DefaultLineMismatchEvent> lineMismatchEvents)
	throws UpdateObjectException {
		try {
			super.update(lineMismatchEvents);

			final Map<Identifier, Set<ChangeLogRecord>> savedChangeLogs = selectChangeLogs(lineMismatchEvents);
			final Map<Identifier, Set<ChangeLogRecord>> pendingChangeLogs = new HashMap<Identifier, Set<ChangeLogRecord>>();
			for (final LineMismatchEvent lineMismatchEvent : lineMismatchEvents) {
				final Identifier lineMismatchEventId = lineMismatchEvent.getId();
				final Set<ChangeLogRecord> pendingChangeLog = new HashSet<ChangeLogRecord>();

				pendingChangeLog.addAll(lineMismatchEvent.getChangeLog());
				pendingChangeLog.removeAll(savedChangeLogs.get(lineMismatchEventId));

				pendingChangeLogs.put(lineMismatchEventId, pendingChangeLog);
			}

			this.insertChangeLogs(pendingChangeLogs);
		} catch (final SQLException sqle) {
			throw new UpdateObjectException(sqle);
		}
	}

	/**
	 * @param lineMismatchEvents
	 * @throws SQLException
	 */
	private Map<Identifier, Set<ChangeLogRecord>> selectChangeLogs(
			final Set<DefaultLineMismatchEvent> lineMismatchEvents)
	throws SQLException {
		if (lineMismatchEvents == null) {
			throw new NullPointerException();
		}

		if (lineMismatchEvents.isEmpty()) {
			return Collections.emptyMap();
		}

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			/*-
			 * This map is needed merely for us to be able to invoke
			 * ChangeLogRecordImpl ctor, for this class is not static.
			 */
			final Map<Identifier, DefaultLineMismatchEvent> keyMap = new HashMap<Identifier, DefaultLineMismatchEvent>();
			for (final DefaultLineMismatchEvent lineMismatchEvent : lineMismatchEvents) {
				keyMap.put(lineMismatchEvent.getId(), lineMismatchEvent);
			}

			final Map<Identifier, Set<ChangeLogRecord>> changeLogs = new HashMap<Identifier, Set<ChangeLogRecord>>();

			final String sql = SQL_SELECT
					+ COLUMN_MODIFIED + COMMA
					+ COLUMN_KEY + COMMA
					+ COLUMN_OLD_VALUE + COMMA
					+ COLUMN_NEW_VALUE
					+ SQL_FROM + TABLE_CHANGE_LOG_RECORD
					+ SQL_WHERE + idsEnumerationString(
							lineMismatchEvents,
							COLUMN_PARENT_LINE_MISMATCH_EVENT_ID,
							true);

			conn = DatabaseConnection.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				final Identifier parentLineMismatchEventId = DatabaseIdentifier.getIdentifier(
						rs,
						COLUMN_PARENT_LINE_MISMATCH_EVENT_ID);
				Set<ChangeLogRecord> changeLog = changeLogs.get(parentLineMismatchEventId);
				if (changeLog == null) {
					changeLog = new HashSet<ChangeLogRecord>();
					changeLogs.put(parentLineMismatchEventId, changeLog);
				}
				final DefaultLineMismatchEvent lineMismatchEvent =
						keyMap.get(parentLineMismatchEventId);
				/*-
				 * I don't use DatabaseString here since I need
				 * pure nulls if there're any, NOT empty strings.
				 */
				final ChangeLogRecord changeLogRecord = lineMismatchEvent.new ChangeLogRecordImpl(
						DatabaseDate.fromQuerySubString(rs, COLUMN_MODIFIED),
						rs.getString(COLUMN_KEY),
						rs.getString(COLUMN_OLD_VALUE),
						rs.getString(COLUMN_NEW_VALUE));
				changeLog.add(changeLogRecord);
			}

			return changeLogs;
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} finally {
				if (conn != null) {
					DatabaseConnection.releaseConnection(conn);
				}
			}
		}
	}

	/**
	 * @param changeLogs
	 * @throws SQLException
	 */
	private void insertChangeLogs(
			final Map<Identifier, Set<ChangeLogRecord>> changeLogs)
	throws SQLException {
		if (changeLogs == null) {
			throw new NullPointerException();
		}

		if (changeLogs.isEmpty()) {
			return;
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			final String sql = SQL_INSERT_INTO
					+ TABLE_CHANGE_LOG_RECORD + OPEN_BRACKET
					+ COLUMN_PARENT_LINE_MISMATCH_EVENT_ID + COMMA
					+ COLUMN_MODIFIED + COMMA
					+ COLUMN_KEY + COMMA
					+ COLUMN_OLD_VALUE + COMMA
					+ COLUMN_NEW_VALUE + CLOSE_BRACKET
					+ SQL_VALUES + OPEN_BRACKET
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION + CLOSE_BRACKET;

			conn = DatabaseConnection.getConnection();
			pstmt = conn.prepareStatement(sql);

			for (final Entry<Identifier, Set<ChangeLogRecord>> entry : changeLogs.entrySet()) {
				DatabaseIdentifier.setIdentifier(pstmt, 1, entry.getKey());

				for (final ChangeLogRecord changeLogRecord : entry.getValue()) {
					pstmt.setTimestamp(2, new Timestamp(changeLogRecord.getModified().getTime()));
					pstmt.setString(3, changeLogRecord.getKey());

					final Object oldValue = changeLogRecord.getOldValue();
					if (oldValue == null) {
						pstmt.setNull(4, VARCHAR);
					} else {
						pstmt.setString(4, oldValue.toString());
					}

					final Object newValue = changeLogRecord.getNewValue();
					if (newValue == null) {
						pstmt.setNull(5, VARCHAR);
					} else {
						pstmt.setString(5, newValue.toString());
					}

					pstmt.executeUpdate();
				}
			}

			conn.commit();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} finally {
				if (conn != null) {
					DatabaseConnection.releaseConnection(conn);
				}
			}
		}
	}
}
