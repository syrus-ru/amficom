/*-
 * $Id: ReflectogramMismatchEventDatabase.java,v 1.2 2006/03/28 10:17:19 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_ALARM_TYPE;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_ANCHOR1_COORD;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_ANCHOR1_ID;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_ANCHOR2_COORD;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_ANCHOR2_ID;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_COORD;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_DELTA_X;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_END_COORD;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_MAX_MISMATCH;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_MIN_MISMATCH;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_MONITORED_ELEMENT_ID;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_RESULT_ID;
import static com.syrus.AMFICOM.eventv2.ReflectogramMismatchEventWrapper.COLUMN_SEVERITY;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.REFLECTOGRAMMISMATCHEVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.AlarmType;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.database.DatabaseDate;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2006/03/28 10:17:19 $
 * @module event
 */
public final class ReflectogramMismatchEventDatabase
		extends StorableObjectDatabase<DefaultReflectogramMismatchEvent> {
	private static String columns;

	private static String updateMultipleSQLValues;

	/**
	 * @see StorableObjectDatabase#getEntityCode()
	 */
	@Override
	protected short getEntityCode() {
		return REFLECTOGRAMMISMATCHEVENT_CODE;
	}

	/**
	 * @see StorableObjectDatabase#getColumnsTmpl()
	 */
	@Override
	protected String getColumnsTmpl() {
		return columns == null
				? columns = COLUMN_ALARM_TYPE + COMMA
						+ COLUMN_SEVERITY + COMMA
						+ COLUMN_COORD + COMMA
						+ COLUMN_END_COORD + COMMA
						+ COLUMN_DELTA_X + COMMA
						+ COLUMN_MIN_MISMATCH + COMMA
						+ COLUMN_MAX_MISMATCH + COMMA
						+ COLUMN_ANCHOR1_ID + COMMA
						+ COLUMN_ANCHOR2_ID + COMMA
						+ COLUMN_ANCHOR1_COORD + COMMA
						+ COLUMN_ANCHOR2_COORD + COMMA
						+ COLUMN_RESULT_ID + COMMA
						+ COLUMN_MONITORED_ELEMENT_ID
				: columns;
	}

	/**
	 * @see StorableObjectDatabase#getUpdateMultipleSQLValuesTmpl()
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
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION + COMMA
						+ QUESTION
				: updateMultipleSQLValues;
	}

	/**
	 * @param reflectogramMismatchEvent
	 * @throws IllegalDataException
	 * @see StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final DefaultReflectogramMismatchEvent reflectogramMismatchEvent)
	throws IllegalDataException {
		return Integer.toString(reflectogramMismatchEvent.getAlarmType().ordinal()) + COMMA
				+ Integer.toString(reflectogramMismatchEvent.getSeverity().ordinal()) + COMMA
				+ Integer.toString(reflectogramMismatchEvent.getCoord()) + COMMA
				+ Integer.toString(reflectogramMismatchEvent.getEndCoord()) + COMMA
				+ Double.toString(reflectogramMismatchEvent.getDeltaX()) + COMMA
				+ (reflectogramMismatchEvent.hasMismatch()
						? Double.toString(reflectogramMismatchEvent.getMinMismatch()) + COMMA
								+ Double.toString(reflectogramMismatchEvent.getMaxMismatch()) + COMMA
						: SQL_NULL + COMMA
								+ SQL_NULL + COMMA)
				+ (reflectogramMismatchEvent.hasAnchors()
						? DatabaseIdentifier.toSQLString(Identifier.valueOf(reflectogramMismatchEvent.getAnchor1Id().getValue())) + COMMA
								+ DatabaseIdentifier.toSQLString(Identifier.valueOf(reflectogramMismatchEvent.getAnchor2Id().getValue())) + COMMA
								+ reflectogramMismatchEvent.getAnchor1Coord() + COMMA
								+ reflectogramMismatchEvent.getAnchor2Coord() + COMMA
						: DatabaseIdentifier.toSQLString(VOID_IDENTIFIER) + COMMA
								+ DatabaseIdentifier.toSQLString(VOID_IDENTIFIER) + COMMA
								+ SQL_NULL + COMMA
								+ SQL_NULL + COMMA)
				+ DatabaseIdentifier.toSQLString(reflectogramMismatchEvent.getResultId()) + COMMA
				+ DatabaseIdentifier.toSQLString(reflectogramMismatchEvent.getMonitoredElementId());
	}

	/**
	 * @param reflectogramMismatchEvent
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final DefaultReflectogramMismatchEvent reflectogramMismatchEvent,
			final PreparedStatement preparedStatement,
			int startParameterNumber)
	throws IllegalDataException, SQLException {
		preparedStatement.setInt(++startParameterNumber, reflectogramMismatchEvent.getAlarmType().ordinal());
		preparedStatement.setInt(++startParameterNumber, reflectogramMismatchEvent.getSeverity().ordinal());
		preparedStatement.setInt(++startParameterNumber, reflectogramMismatchEvent.getCoord());
		preparedStatement.setInt(++startParameterNumber, reflectogramMismatchEvent.getEndCoord());
		preparedStatement.setDouble(++startParameterNumber, reflectogramMismatchEvent.getDeltaX());
		if (reflectogramMismatchEvent.hasMismatch()) {
			preparedStatement.setDouble(++startParameterNumber, reflectogramMismatchEvent.getMinMismatch());
			preparedStatement.setDouble(++startParameterNumber, reflectogramMismatchEvent.getMaxMismatch());
		} else {
			preparedStatement.setNull(++startParameterNumber, Types.DOUBLE);
			preparedStatement.setNull(++startParameterNumber, Types.DOUBLE);
		}
		if (reflectogramMismatchEvent.hasAnchors()) {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.valueOf(reflectogramMismatchEvent.getAnchor1Id().getValue()));
			DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, Identifier.valueOf(reflectogramMismatchEvent.getAnchor2Id().getValue()));
			preparedStatement.setInt(++startParameterNumber, reflectogramMismatchEvent.getAnchor1Coord());
			preparedStatement.setInt(++startParameterNumber, reflectogramMismatchEvent.getAnchor2Coord());
		} else {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
			DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, VOID_IDENTIFIER);
			preparedStatement.setNull(++startParameterNumber, Types.INTEGER);
			preparedStatement.setNull(++startParameterNumber, Types.INTEGER);
		}
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, reflectogramMismatchEvent.getResultId());
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, reflectogramMismatchEvent.getMonitoredElementId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 * @see StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, ResultSet)
	 */
	@Override
	protected DefaultReflectogramMismatchEvent updateEntityFromResultSet(
			final DefaultReflectogramMismatchEvent storableObject,
			final ResultSet resultSet)
	throws IllegalDataException, RetrieveObjectException, SQLException {
		final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
		final DefaultReflectogramMismatchEvent reflectogramMismatchEvent = (storableObject == null)
				? new DefaultReflectogramMismatchEvent(id)
				: storableObject;


		/*
		 * Mismatch.
		 */
		final boolean mismatch;
		final double minMismatch;
		final double maxMismatch;
		{
			minMismatch = resultSet.getDouble(COLUMN_MIN_MISMATCH);
			final boolean probablyMismatch1 = !resultSet.wasNull();
			maxMismatch = resultSet.getDouble(COLUMN_MAX_MISMATCH);
			final boolean probablyMismatch2 = !resultSet.wasNull();
			if (probablyMismatch1 ^ probablyMismatch2) {
				throw new RetrieveObjectException("id: " + id
						+ "; minMismatch: " + minMismatch
						+ "; maxMismatch: " + maxMismatch
						+ "; probablyMismatch1: " + probablyMismatch1
						+ "; probablyMismatch2: " + probablyMismatch2);
			}
			mismatch = probablyMismatch1;
			assert mismatch || (minMismatch == 0 && maxMismatch == 0) :
					"mismatch: " + mismatch
					+ "; minMismatch: " + minMismatch
					+ "; maxMismatch: " + maxMismatch;
		}


		/*
		 * Anchors.
		 */
		final boolean anchor1;
		final Identifier anchor1Id;
		final int anchor1Coord;
		{
			anchor1Id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANCHOR1_ID);
			final boolean probablyAnchor11 = !anchor1Id.isVoid();
			anchor1Coord = resultSet.getInt(COLUMN_ANCHOR1_COORD);
			final boolean probablyAnchor12 = !resultSet.wasNull();
			if (probablyAnchor11 ^ probablyAnchor12) {
				throw new RetrieveObjectException("id: " + id
						+ "; anchor1Id: " + anchor1Id
						+ "; anchor1Coord: " + anchor1Coord
						+ "; probablyAnchor11: " + probablyAnchor11
						+ "; probablyAnchor12: " + probablyAnchor12);
			}
			anchor1 = probablyAnchor11;
			assert anchor1 || (anchor1Id.isVoid() && anchor1Coord == 0) :
					"anchor1: " + anchor1
					+ "; anchor1Id: " + anchor1Id
					+ "; anchor1Coord: " + anchor1Coord; 
		}

		final boolean anchor2;
		final Identifier anchor2Id;
		final int anchor2Coord;
		{
			anchor2Id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ANCHOR2_ID);
			final boolean probablyAnchor21 = !anchor2Id.isVoid();
			anchor2Coord = resultSet.getInt(COLUMN_ANCHOR2_COORD);
			final boolean probablyAnchor22 = !resultSet.wasNull();
			if (probablyAnchor21 ^ probablyAnchor22) {
				throw new RetrieveObjectException("id: " + id
						+ "; anchor2Id: " + anchor2Id
						+ "; anchor2Coord: " + anchor2Coord
						+ "; probablyAnchor21: " + probablyAnchor21
						+ "; probablyAnchor22: " + probablyAnchor22);
			}
			anchor2 = probablyAnchor21;
			assert anchor2 || (anchor2Id.isVoid() && anchor2Coord == 0) :
					"anchor2: " + anchor2
					+ "; anchor2Id: " + anchor2Id
					+ "; anchor2Coord: " + anchor2Coord; 
		}


		reflectogramMismatchEvent.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				AlarmType.valueOf(resultSet.getInt(COLUMN_ALARM_TYPE)),
				Severity.valueOf(resultSet.getInt(COLUMN_SEVERITY)),
				resultSet.getInt(COLUMN_COORD),
				resultSet.getInt(COLUMN_END_COORD),
				resultSet.getDouble(COLUMN_DELTA_X),
				mismatch,
				minMismatch,
				maxMismatch,
				anchor1 && anchor2,
				anchor1Id,
				anchor2Id,
				anchor1Coord,
				anchor2Coord,
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_RESULT_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MONITORED_ELEMENT_ID));
		return reflectogramMismatchEvent;
	}
}
