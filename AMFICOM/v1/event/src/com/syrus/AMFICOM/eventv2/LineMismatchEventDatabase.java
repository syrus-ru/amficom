/*-
 * $Id: LineMismatchEventDatabase.java,v 1.1.2.2 2006/03/23 19:41:25 bass Exp $
 *
 * Copyright � 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.eventv2;

import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_AFFECTED_PATH_ELEMENT_ID;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MESSAGE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MISMATCH_OPTICAL_DISTANCE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_MISMATCH_PHYSICAL_DISTANCE;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PHYSICAL_DISTANCE_TO_END;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_PHYSICAL_DISTANCE_TO_START;
import static com.syrus.AMFICOM.eventv2.LineMismatchEventWrapper.COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.LINEMISMATCHEVENT_CODE;
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
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1.2.2 $, $Date: 2006/03/23 19:41:25 $
 * @module event
 */
public final class LineMismatchEventDatabase
		extends StorableObjectDatabase<DefaultLineMismatchEvent> {
	private static final int SIZE_MESSAGE_COLUMN = 4000;

	private static String columns;

	private static String updateMultipleSQLValues;

	/**
	 * @see StorableObjectDatabase#getEntityCode()
	 */
	@Override
	protected short getEntityCode() {
		return LINEMISMATCHEVENT_CODE;
	}

	/**
	 * @see StorableObjectDatabase#getColumnsTmpl()
	 */
	@Override
	protected String getColumnsTmpl() {
		return columns == null
				? columns = COLUMN_AFFECTED_PATH_ELEMENT_ID + COMMA
						+ COLUMN_PHYSICAL_DISTANCE_TO_START + COMMA
						+ COLUMN_PHYSICAL_DISTANCE_TO_END + COMMA
						+ COLUMN_MISMATCH_OPTICAL_DISTANCE + COMMA
						+ COLUMN_MISMATCH_PHYSICAL_DISTANCE + COMMA
						+ COLUMN_MESSAGE + COMMA
						+ COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID
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
						+ QUESTION
				: updateMultipleSQLValues;
	}

	/**
	 * @param lineMismatchEvent
	 * @throws IllegalDataException
	 * @see StorableObjectDatabase#getUpdateSingleSQLValuesTmpl(com.syrus.AMFICOM.general.StorableObject)
	 */
	@Override
	protected String getUpdateSingleSQLValuesTmpl(
			final DefaultLineMismatchEvent lineMismatchEvent)
	throws IllegalDataException {
		return DatabaseIdentifier.toSQLString(lineMismatchEvent.getAffectedPathElementId()) + COMMA
				+ (lineMismatchEvent.isAffectedPathElementSpacious()
						? Double.toString(lineMismatchEvent.getPhysicalDistanceToStart()) + COMMA
								+ Double.toString(lineMismatchEvent.getPhysicalDistanceToEnd()) + COMMA
						: SQL_NULL + COMMA
								+ SQL_NULL + COMMA)
				+ Double.toString(lineMismatchEvent.getMismatchOpticalDistance()) + COMMA
				+ Double.toString(lineMismatchEvent.getMismatchPhysicalDistance()) + COMMA
				+ APOSTROPHE + DatabaseString. toQuerySubString(lineMismatchEvent.getMessage(), SIZE_MESSAGE_COLUMN) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(lineMismatchEvent.getReflectogramMismatchEventId());
	}

	/**
	 * @param lineMismatchEvent
	 * @param preparedStatement
	 * @param startParameterNumber
	 * @throws IllegalDataException
	 * @throws SQLException
	 * @see StorableObjectDatabase#setEntityForPreparedStatementTmpl(com.syrus.AMFICOM.general.StorableObject, java.sql.PreparedStatement, int)
	 */
	@Override
	protected int setEntityForPreparedStatementTmpl(
			final DefaultLineMismatchEvent lineMismatchEvent,
			final PreparedStatement preparedStatement,
			int startParameterNumber)
	throws IllegalDataException, SQLException {
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
		DatabaseString.setString(preparedStatement, ++startParameterNumber, lineMismatchEvent.getMessage(), SIZE_MESSAGE_COLUMN);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, lineMismatchEvent.getReflectogramMismatchEventId());
		return startParameterNumber;
	}

	/**
	 * @param storableObject
	 * @param resultSet
	 * @throws IllegalDataException
	 * @throws RetrieveObjectException
	 * @throws SQLException
	 * @see StorableObjectDatabase#updateEntityFromResultSet(com.syrus.AMFICOM.general.StorableObject, java.sql.ResultSet)
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


		lineMismatchEvent.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_AFFECTED_PATH_ELEMENT_ID),
				affectedPathElementSpacious,
				physicalDistanceToStart,
				physicalDistanceToEnd,
				resultSet.getDouble(COLUMN_MISMATCH_OPTICAL_DISTANCE),
				resultSet.getDouble(COLUMN_MISMATCH_PHYSICAL_DISTANCE),
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_MESSAGE)),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_REFLECTOGRAM_MISMATCH_EVENT_ID));
		return lineMismatchEvent;
	}
}