/*
 * $Id: EventDatabase.java,v 1.16 2005/02/28 15:31:47 arseniy Exp $
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.16 $, $Date: 2005/02/28 15:31:47 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultiplySQLValues;

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

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EventWrapper.COLUMN_STATUS + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return super.getColumns(mode) + columns;
	}

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		Event event = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseIdentifier.setIdentifier(preparedStatement, ++i, event.getType().getId());
		preparedStatement.setInt(++i, event.getStatus().value());
		DatabaseString.setString(preparedStatement, ++i, event.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return i;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		Event event = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(event.getType().getId()) + COMMA
			+ Integer.toString(event.getStatus().value()) + COMMA
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
								resultSet.getInt(EventWrapper.COLUMN_STATUS),
								DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));		
		return event;
	}

	private void retrieveEventParametersByOneQuery(Collection events) throws RetrieveObjectException {
    if ((events == null) || (events.isEmpty()))
			return;

    Map eventParameterIdsMap;
		try {
			eventParameterIdsMap = this.retrieveLinkedEntityIds(events,
					ObjectEntities.EVENTPARAMETER_ENTITY,
					EventWrapper.LINK_COLUMN_EVENT_ID,
					StorableObjectWrapper.COLUMN_ID);
		}
		catch (IllegalDataException ide) {
			throw new RetrieveObjectException(ide);
		}

		Event event;
		Identifier eventId;
		Collection eventParameters;
		for (Iterator it = events.iterator(); it.hasNext();) {
			event = (Event) it.next();
			eventId = event.getId();
			try {
				eventParameters = EventStorableObjectPool.getStorableObjects((Collection) eventParameterIdsMap.get(eventId), true);
			}
			catch (ApplicationException ae) {
				throw new RetrieveObjectException(ae);
			}

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
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException , IllegalDataException {
		Event event = this.fromStorableObject(storableObject);
		try {
			this.insertEntity(event);
			EventDatabaseContext.eventParameterDatabase.insert(event.getParameters());
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

		Collection eventParameters = new LinkedList();
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Event event = (Event) it.next();
			eventParameters.addAll(event.getParameters());
		}
		EventDatabaseContext.eventParameterDatabase.insert(eventParameters);

		try {
			this.updateEventSources(storableObjects);
		}
		catch (UpdateObjectException e) {
			throw new CreateObjectException(e);
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

	public Collection retrieveByIds(Collection ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		Collection collection = null; 
		if ((ids == null) || (ids.isEmpty()))
			collection = this.retrieveByIdsOneQuery(null, conditions);
		else
			collection = this.retrieveByIdsOneQuery(ids, conditions);

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
