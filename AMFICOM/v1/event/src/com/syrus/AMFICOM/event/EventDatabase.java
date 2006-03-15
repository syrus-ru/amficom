/*-
 * $Id: EventDatabase.java,v 1.55 2006/03/15 15:47:20 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.event;

import static com.syrus.AMFICOM.event.EventWrapper.LINK_COLUMN_EVENT_ID;
import static com.syrus.AMFICOM.event.EventWrapper.LINK_COLUMN_PARAMETER_VALUE;
import static com.syrus.AMFICOM.event.EventWrapper.LINK_COLUMN_SOURCE_ID;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENTPARAMETER;
import static com.syrus.AMFICOM.general.ObjectEntities.EVENT_CODE;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CREATOR_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIED;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_MODIFIER_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_TYPE_ID;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_VERSION;
import static com.syrus.AMFICOM.general.TableNames.EVENTSOURCELINK;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.55 $, $Date: 2006/03/15 15:47:20 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module event
 */

public final class EventDatabase extends StorableObjectDatabase<Event> {
	protected static final int SIZE_PARAMETER_VALUE_COLUMN = 256;

	private static String columns;
	private static String updateMultipleSQLValues;

	@Override
	protected short getEntityCode() {		
		return EVENT_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_TYPE_ID + COMMA
				+ COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final Event storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, storableObject.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	@Override
	protected String getUpdateSingleSQLValuesTmpl(final Event storableObject) throws IllegalDataException {
		final String values = DatabaseIdentifier.toSQLString(storableObject.getType().getId()) + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(storableObject.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return values;
	}

	@Override
	protected Event updateEntityFromResultSet(final Event storableObject, final ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		final Event event = (storableObject == null)
				? new Event(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						null,
						ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
					: storableObject;
		EventType eventType;
		try {
			eventType = (EventType) StorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID), true);
		} catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		event.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
				StorableObjectVersion.valueOf(resultSet.getLong(COLUMN_VERSION)),
				eventType,
				DatabaseString.fromQuerySubString(resultSet.getString(COLUMN_DESCRIPTION)));		
		return event;
	}

	private void retrieveEventParametersByOneQuery(final Set<Event> events) throws RetrieveObjectException {
    if ((events == null) || (events.isEmpty())) {
			return;
    }

    final StringBuffer stringBuffer = new StringBuffer(SQL_SELECT
				+ COLUMN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ LINK_COLUMN_PARAMETER_VALUE + COMMA
				+ LINK_COLUMN_EVENT_ID
				+ SQL_FROM + EVENTPARAMETER
				+ SQL_WHERE);
    stringBuffer.append(idsEnumerationString(events, LINK_COLUMN_EVENT_ID, true));

    final Map<Identifier, Set<EventParameter>> eventParametersMap = new HashMap<Identifier, Set<EventParameter>>();

    Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("Trying: " + stringBuffer, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(stringBuffer.toString());

			while (resultSet.next()) {
				final EventParameter eventParameter = new EventParameter(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
						DatabaseIdentifier.getIdentifier(resultSet, COLUMN_TYPE_ID),
						DatabaseString.fromQuerySubString(resultSet.getString(LINK_COLUMN_PARAMETER_VALUE)));
				final Identifier eventId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_EVENT_ID);
				Set<EventParameter> eventParameters = eventParametersMap.get(eventId);
				if (eventParameters == null) {
					eventParameters = new HashSet<EventParameter>();
					eventParametersMap.put(eventId, eventParameters);
				}
				eventParameters.add(eventParameter);
			}
		}
		catch (SQLException sqle) {
			final String mesg = "EventDatabase.retrieveEventParametersByOneQuery | Cannot retrieve parameters for event -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
						resultSet = null;
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
							statement = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}

		for (final Event event : events) {
			final Identifier eventId = event.getId();
			final Set<EventParameter> eventParameters = eventParametersMap.get(eventId);

			event.setEventParameters0(eventParameters);
		}
	}

	private void retrieveEventSourceIdsByOneQuery(final Set<Event> events) throws RetrieveObjectException {
		if ((events == null) || (events.isEmpty())) {
			return;
		}

		final Map<Identifier, Set<Identifier>> eventSourceIdsMap = this.retrieveLinkedEntityIds(events,
				EVENTSOURCELINK,
				LINK_COLUMN_EVENT_ID,
				LINK_COLUMN_SOURCE_ID);

		for (final Event event : events) {
			final Identifier eventId = event.getId();
			final Set<Identifier> eventSourceIds = eventSourceIdsMap.get(eventId);

			event.setEventSourceIds0(eventSourceIds);
		}
	}

	@Override
	protected void insert(final Set<Event> storableObjects) throws IllegalDataException, CreateObjectException {
		super.insert(storableObjects);

		for (final Event event : storableObjects) {
			this.insertEventParameters(event);
		}

		try {
			this.updateEventSources(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	private void insertEventParameters(final Event event) throws CreateObjectException {
		final Identifier eventId = event.getId();
		final Set<EventParameter> eventParameters = event.getParameters();
		final String sql = SQL_INSERT_INTO + EVENTPARAMETER + OPEN_BRACKET
				+ COLUMN_ID + COMMA
				+ COLUMN_TYPE_ID + COMMA
				+ LINK_COLUMN_EVENT_ID + COMMA
				+ LINK_COLUMN_PARAMETER_VALUE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (final EventParameter eventParameter : eventParameters) {
				parameterId = eventParameter.getId();
				parameterTypeId = eventParameter.getTypeId();

				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, eventId);
				DatabaseString.setString(preparedStatement, 4, eventParameter.getValue(), SIZE_PARAMETER_VALUE_COLUMN);

				Log.debugMessage("Inserting parameter " + parameterId + " for event '" + eventId + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			final String mesg = "EventDatabase.insertEventParameters | Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterTypeId + "' for event '" + eventId + "' -- " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (preparedStatement != null) {
						preparedStatement.close();
						preparedStatement = null;
					}
				} finally {
					if (connection != null) {
						DatabaseConnection.releaseConnection(connection);
						connection = null;
					}
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
		}
	}

	@Override
	protected void update(final Set<Event> storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateEventSources(storableObjects);
	}

	private void updateEventSources(final Set<Event> events) throws UpdateObjectException {
		if (events == null || events.isEmpty()) {
			return;
		}

		final Map<Identifier, Set<Identifier>> eventSourceIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Event event : events) {
			final Set<Identifier> eventSourceIds = event.getEventSourceIds();
			eventSourceIdsMap.put(event.getId(), eventSourceIds);
		}

		super.updateLinkedEntityIds(eventSourceIdsMap,
				EVENTSOURCELINK,
				LINK_COLUMN_EVENT_ID,
				LINK_COLUMN_SOURCE_ID);
	}

	@Override
	protected Set<Event> retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set<Event> objects = super.retrieveByCondition(conditionQuery);
		this.retrieveEventParametersByOneQuery(objects);
		this.retrieveEventSourceIdsByOneQuery(objects);
		return objects;
	}

}
