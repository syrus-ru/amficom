/*
 * $Id: EventDatabase.java,v 1.21 2005/03/11 10:58:19 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.21 $, $Date: 2005/03/11 10:58:19 $
 * @author $Author: bob $
 * @module event_v1
 */

public class EventDatabase extends StorableObjectDatabase {
	protected static final int SIZE_PARAMETER_VALUE_COLUMN = 256;

	private static String columns;
	private static String updateMultipleSQLValues;

	private Event fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof Event)
			return (Event) storableObject;
		throw new IllegalDataException("EventDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	public void retrieve(StorableObject storableObject) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Event event = this.fromStorableObject(storableObject);
		this.retrieveEntity(event);
		this.retrieveEventParametersByOneQuery(Collections.singletonList(event));
		this.retrieveEventSourceIdsByOneQuery(Collections.singletonList(event));
	}	

	protected String getEnityName() {		
		return ObjectEntities.EVENT_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		Event event = this.fromStorableObject(storableObject);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++startParameterNumber, event.getType().getId());
		DatabaseString.setString(preparedStatement, ++startParameterNumber, event.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		Event event = this.fromStorableObject(storableObject);
		String values = DatabaseIdentifier.toSQLString(event.getType().getId()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(event.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Event event = (storableObject == null) ? new Event(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
				null,
				0L,
				null,
				null,
				null,
				null) : this.fromStorableObject(storableObject);
		EventType eventType;
		try {
			eventType = (EventType) EventStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					StorableObjectWrapper.COLUMN_TYPE_ID), true);			
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		event.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
								DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
								DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
							  resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
								eventType,
								DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));		
		return event;
	}

	private void retrieveEventParametersByOneQuery(Collection events) throws RetrieveObjectException {
    if ((events == null) || (events.isEmpty()))
			return;

    StringBuffer stringBuffer = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EventWrapper.LINK_COLUMN_PARAMETER_VALUE
				+ EventWrapper.LINK_COLUMN_EVENT_ID
				+ SQL_FROM + ObjectEntities.EVENTPARAMETER_ENTITY
				+ SQL_WHERE);
    try {
			stringBuffer.append(idsEnumerationString(events, EventWrapper.LINK_COLUMN_EVENT_ID, true));
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}

    Map eventParametersMap = new HashMap();
    Identifier eventId;
    Collection eventParameters;

    Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EventDatabase.retrieveEventParametersByOneQuery | Trying: " + stringBuffer, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(stringBuffer.toString());

			ParameterType parameterType;
			EventParameter eventParameter;
			while (resultSet.next()) {
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				eventParameter = new EventParameter(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						parameterType,
						DatabaseString.fromQuerySubString(resultSet.getString(EventWrapper.LINK_COLUMN_PARAMETER_VALUE)));
				eventId = DatabaseIdentifier.getIdentifier(resultSet, EventWrapper.LINK_COLUMN_EVENT_ID);
				eventParameters = (Collection) eventParametersMap.get(eventId);
				if (eventParameters == null) {
					eventParameters = new HashSet();
					eventParametersMap.put(eventId, eventParameters);
				}
				eventParameters.add(eventParameter);
			}
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.retrieveEventParametersByOneQuery | Cannot retrieve parameters for event -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

		Event event;
		for (Iterator it = events.iterator(); it.hasNext();) {
			event = (Event) it.next();
			eventId = event.getId();
			eventParameters = (Collection) eventParametersMap.get(eventId);

			event.setEventParameters0(eventParameters);
		}
	}

	private void retrieveEventSourceIdsByOneQuery(Collection events) throws RetrieveObjectException, IllegalDataException {
		if ((events == null) || (events.isEmpty()))
			return;

		Map eventSourceIdsMap = this.retrieveLinkedEntityIds(events,
				ObjectEntities.EVENTSOURCE_LINK_ENTITY,
				EventWrapper.LINK_COLUMN_EVENT_ID,
				EventWrapper.LINK_COLUMN_SOURCE_ID);

		Event event;
		Collection eventSourceIds;
		for (Iterator it = events.iterator(); it.hasNext();) {
			event = (Event) it.next();
			eventSourceIds = (Collection) eventSourceIdsMap.get(event.getId());

			event.setEventSourceIds0(eventSourceIds);
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		Event event = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  event.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Event event = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(event);
			this.insertEventParameters(event);
			this.updateEventSources(Collections.singletonList(event));
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
		catch (CreateObjectException coe) {
			this.delete(event);
			throw coe;
		}
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);

		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Event event = this.fromStorableObject((StorableObject) it.next());
			this.insertEventParameters(event);
		}

		try {
			this.updateEventSources(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
		}
	}

	private void insertEventParameters(Event event) throws CreateObjectException {
		Identifier eventId = event.getId();
		Collection eventParameters = event.getParameters();
		String sql = SQL_INSERT_INTO + ObjectEntities.EVENTPARAMETER_ENTITY
				+ OPEN_BRACKET
				+ StorableObjectWrapper.COLUMN_ID + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EventWrapper.LINK_COLUMN_EVENT_ID + COMMA
				+ EventWrapper.LINK_COLUMN_PARAMETER_VALUE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		PreparedStatement preparedStatement = null;
		EventParameter eventParameter;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (Iterator it = eventParameters.iterator(); it.hasNext();) {
				eventParameter = (EventParameter) it.next();
				parameterId = eventParameter.getId();
				parameterTypeId = eventParameter.getType().getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, eventId);
				DatabaseString.setString(preparedStatement, 4, eventParameter.getValue(), SIZE_PARAMETER_VALUE_COLUMN);

				Log.debugMessage("EventDatabase.insertEventParameters | Inserting parameter " + parameterTypeId.toString()
						+ " for event '" + eventId + "'", Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.insertEventParameters | Cannot insert parameter '" + parameterId.toString()
					+ "' of type '" + parameterTypeId.toString() + "' for event '" + eventId + "' -- " + sqle.getMessage();
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	private void updateEventSources(Collection events) throws IllegalDataException, UpdateObjectException {
		if (events == null || events.isEmpty())
			return;

		Map eventSourceIdsMap = new HashMap();
		for (Iterator it = events.iterator(); it.hasNext();) {
			Event event = this.fromStorableObject((StorableObject) it.next());
			Collection eventSourceIds = event.getEventSourceIds();
			eventSourceIdsMap.put(event.getId(), eventSourceIds);
		}

		this.updateLinkedEntities(eventSourceIdsMap,
				ObjectEntities.EVENTSOURCE_LINK_ENTITY,
				EventWrapper.LINK_COLUMN_EVENT_ID,
				EventWrapper.LINK_COLUMN_SOURCE_ID);
	}

	protected Collection retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Collection collection = super.retrieveByCondition(conditionQuery);
		this.retrieveEventParametersByOneQuery(collection);
		this.retrieveEventSourceIdsByOneQuery(collection);
		return collection;
	}

	public void delete(Identifier id) throws IllegalDataException {
		//Event event = this.fromStorableObject(storableObject);
		if (id.getMajor() != ObjectEntities.EVENT_ENTITY_CODE)
			throw new IllegalDataException("EventDatabase.delete | Cannot delete object of code "
					+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'");

		String eventIdStr = DatabaseIdentifier.toSQLString(id);
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENTSOURCE_LINK_ENTITY
					+ SQL_WHERE + EventWrapper.LINK_COLUMN_EVENT_ID + EQUALS + eventIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENTPARAMETER_ENTITY
					+ SQL_WHERE + EventWrapper.LINK_COLUMN_EVENT_ID + EQUALS + eventIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENT_ENTITY
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + eventIdStr);
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
			finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}
}
