/*
 * $Id: EventDatabase.java,v 1.12 2005/02/19 20:33:52 arseniy Exp $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import oracle.sql.BLOB;

import com.syrus.AMFICOM.event.corba.EventParameter_TransferablePackage.EventParameterSort;
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
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.12 $, $Date: 2005/02/19 20:33:52 $
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
			columns = super.getColumns(mode) + COMMA
				+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
				+ EventWrapper.COLUMN_STATUS + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected String getUpdateMultiplySQLValues(int mode) {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues(mode) + COMMA
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

    StringBuffer sql = new StringBuffer(SQL_SELECT
  			+ StorableObjectWrapper.COLUMN_ID + COMMA			
  			+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
  			+ EventWrapper.LINK_COLUMN_EVENT_ID
  			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_SORT + COMMA
  			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_NUMBER + COMMA
  			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_STRING + COMMA
  			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_RAW
  			+ SQL_FROM + ObjectEntities.EVENTPARAMETER_ENTITY
  			+ SQL_WHERE);
    try {
			sql.append(this.idsEnumerationString(events, EventWrapper.LINK_COLUMN_EVENT_ID, true));
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EventDatabase.retrieveEventParametersByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map eventParametersMap = new HashMap();
			Identifier eventId;
			Identifier parameterId;
			ParameterType parameterType;
			int sort;
			EventParameter parameter;
			List eventParameters;
			while (resultSet.next()) {
				parameter = null;
				parameterId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID);
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				eventId = DatabaseIdentifier.getIdentifier(resultSet, EventWrapper.LINK_COLUMN_EVENT_ID);
				sort = resultSet.getInt(EventWrapper.LINK_COLUMN_EVENT_PARAMETER_SORT);
				switch (sort) {
					case EventParameterSort._PARAMETER_SORT_NUMBER:
						parameter = new EventParameter(parameterId,
								parameterType,
								resultSet.getInt(EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_NUMBER));
						break;
					case EventParameterSort._PARAMETER_SORT_STRING:
						parameter = new EventParameter(parameterId,
								parameterType,
								resultSet.getString(EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_STRING));
						break;
					case EventParameterSort._PARAMETER_SORT_RAW:
						parameter = new EventParameter(parameterId,
								parameterType,
								ByteArrayDatabase.toByteArray(resultSet.getBlob(EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_RAW)));
						break;
					default:
						Log.errorMessage("EventDatabase.retrieveEventParameters | Unknown sort: " + sort + " of parameter '" + parameterId + "', event '" + eventId + "'");
				}
				if (parameter != null) {
					eventParameters = (List) eventParametersMap.get(eventId);
					if (eventParameters == null) {
						eventParameters = new ArrayList();
						eventParametersMap.put(eventId, eventParameters);
					}
					eventParameters.add(parameter);					
				}
			}

			Event event;
			for (Iterator it = events.iterator(); it.hasNext();) {
				event = (Event) it.next();
				eventId = event.getId();
				eventParameters = (List) eventParametersMap.get(eventId);

				if (eventParameters != null)
					event.setEventParameters0((EventParameter[]) eventParameters.toArray(new EventParameter[eventParameters.size()]));
			}
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.retrieveEventParametersByOneQuery | Cannot retrieve parameters for event -- "
					+ sqle.getMessage();
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
	}

	private void retrieveEventSourceIdsByOneQuery(Collection events) throws RetrieveObjectException, IllegalDataException {
		if ((events == null) || (events.isEmpty()))
			return;

		Map eventSourceIdsMap = this.retrieveLinkedEntityIds(events,
				ObjectEntities.EVENTSOURCE_LINK_ENTITY,
				EventWrapper.LINK_COLUMN_EVENT_ID,
				EventWrapper.LINK_COLUMN_SOURCE_ID);

		Event event;
		List eventSourceIds;
		for (Iterator it = events.iterator(); it.hasNext();) {
			event = (Event) it.next();
			eventSourceIds = (List) eventSourceIdsMap.get(event.getId());

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
			Event event = (Event) it.next();
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
		String eventIdStr = event.getId().toString();
		EventParameter[] eventParameters = event.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.EVENTPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ StorableObjectWrapper.COLUMN_ID  + COMMA
			+ StorableObjectWrapper.COLUMN_TYPE_ID + COMMA
			+ EventWrapper.LINK_COLUMN_EVENT_ID + COMMA
			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_SORT + COMMA
			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_NUMBER + COMMA
			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_STRING + COMMA
			+ EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_RAW
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		int i = 0;
		Identifier parameterId = null;
		Identifier parameterTypeId = null;
		int sort;
		Connection connection = DatabaseConnection.getConnection();
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (i = 0; i < eventParameters.length; i++) {
				parameterId = eventParameters[i].getId();
				parameterTypeId = eventParameters[i].getType().getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, parameterId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
				DatabaseIdentifier.setIdentifier(preparedStatement, 3, event.getId());
				sort = eventParameters[i].getSort().value();
				preparedStatement.setInt(4, sort);
				switch (sort) {
					case EventParameterSort._PARAMETER_SORT_NUMBER:
						preparedStatement.setInt(5, eventParameters[i].getValueNumber());
						preparedStatement.setString(6, "");
						preparedStatement.setBlob(7, null);
						break;
					case EventParameterSort._PARAMETER_SORT_STRING:
						preparedStatement.setInt(5, 0);
						preparedStatement.setString(6, eventParameters[i].getValueString());
						preparedStatement.setBlob(7, null);
						break;
					case EventParameterSort._PARAMETER_SORT_RAW:
						preparedStatement.setInt(5, 0);
						preparedStatement.setString(6, "");
						preparedStatement.setBlob(7, BLOB.empty_lob());//TODO: use APOSTOPHE + SQL_EMPTY_BLOB + APOSTOPHE (See ResultDatabase )
						break;
					default:
						Log.errorMessage("EventDatabase.insertEventParameters | Unknown sort: " + sort + " of parameter '" + parameterId + "', event '" + eventIdStr + "'");
				}
				Log.debugMessage("EventDatabase.insertEventParameters | Inserting parameter '" + parameterId.toString() + "' of type '" + parameterTypeId.toString() + "' for event " + eventIdStr, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
				if (sort == EventParameterSort._PARAMETER_SORT_RAW) {
					ByteArrayDatabase.saveAsBlob(eventParameters[i].getValueRaw(),
												 connection,
												 ObjectEntities.EVENTPARAMETER_ENTITY,
												 EventWrapper.LINK_COLUMN_EVENT_PARAMETER_VALUE_RAW,
												 StorableObjectWrapper.COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
				}
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.insertEventParameters | Cannot insert parameter '" + parameterId.toString() + "' of type '" + parameterTypeId.toString() + "' for event '" + eventIdStr + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, Identifier modifierId, int updateKind) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Event event = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, modifierId, true);		
				return;
		}
	}

	public void update(Collection storableObjects, Identifier modifierId, int updateKind)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, modifierId, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, modifierId, true);		
				return;
		}
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
