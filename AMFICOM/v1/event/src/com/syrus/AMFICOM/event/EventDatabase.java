/*
 * $Id: EventDatabase.java,v 1.5 2005/02/02 15:09:47 arseniy Exp $
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
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.ByteArrayDatabase;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.5 $, $Date: 2005/02/02 15:09:47 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventDatabase extends StorableObjectDatabase {

	public static final String LINK_COLUMN_EVENT_ID	= "event_id";
	public static final String LINK_COLUMN_PARAMETER_TYPE_ID	= "type_id";
	public static final String LINK_COLUMN_PARAMETER_SORT	= "sort";
	public static final String LINK_COLUMN_PARAMETER_VALUE_NUMBER	= "value_number";
	public static final String LINK_COLUMN_PARAMETER_VALUE_STRING	= "value_string";
	public static final String LINK_COLUMN_PARAMETER_VALUE_RAW	= "value_raw";
	public static final String LINK_COLUMN_SOURCE_ID	= "source_id";
	public static final String LINK_COLUMN_SOURCE_ENTITY_CODE	= "source_entity_code";
	public static final String LINK_COLUMN_SOURCE_PORT_ID	= "port_id";
	public static final String LINK_COLUMN_SOURCE_EQUIPMENT_ID	= "equipment_id";
	public static final String LINK_COLUMN_SOURCE_LINK_ID	= "link_id";
	public static final String LINK_COLUMN_SOURCE_MONITOREDELEMENT_ID	= "monitored_element_id";

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
		this.retrieveEventParameters(event);
		this.retrieveEventSources(event);
	}	

	protected String getEnityName() {		
		return ObjectEntities.EVENT_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = super.getColumns(mode) + COMMA
				+ EventWrapper.COLUMN_TYPE_ID + COMMA
				+ EventWrapper.COLUMN_STATUS + COMMA
				+ EventWrapper.COLUMN_DESCRIPTION;
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
			throws IllegalDataException, UpdateObjectException {
		Event event = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		try {
			DatabaseIdentifier.setIdentifier(preparedStatement, ++i, event.getType().getId());
			preparedStatement.setInt(++i, event.getStatus().value());
			DatabaseString.setString(preparedStatement, ++i, event.getDescription(), SIZE_DESCRIPTION_COLUMN);
		}
		catch (SQLException sqle) {
			throw new UpdateObjectException(getEnityName() + "Database.setEntityForPreparedStatement | Error " + sqle.getMessage(), sqle);
		}
		return i;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject)
			throws IllegalDataException, UpdateObjectException {
		Event event = this.fromStorableObject(storableObject);
		String values = super.getUpdateSingleSQLValues(storableObject) + COMMA
			+ DatabaseIdentifier.toSQLString(event.getType().getId()) + COMMA
			+ Integer.toString(event.getStatus().value()) + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(event.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return values;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet)
			throws IllegalDataException, RetrieveObjectException, SQLException {
		Event event = (storableObject == null) ? new Event(DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID),
				null,
				null,
				null,
				null,
				null) : this.fromStorableObject(storableObject);
		EventType eventType;
		try {
			eventType = (EventType) EventStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet,
					EventWrapper.COLUMN_TYPE_ID), true);			
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
		}
		event.setAttributes(DatabaseDate.fromQuerySubString(resultSet, COLUMN_CREATED),
								DatabaseDate.fromQuerySubString(resultSet, COLUMN_MODIFIED),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_CREATOR_ID),
								DatabaseIdentifier.getIdentifier(resultSet, COLUMN_MODIFIER_ID),
								eventType,
								resultSet.getInt(EventWrapper.COLUMN_STATUS),
								DatabaseString.fromQuerySubString(resultSet.getString(EventWrapper.COLUMN_DESCRIPTION)));		
		return event;
	}

	private void retrieveEventParameters(Event event) throws RetrieveObjectException {
		List parameters = new ArrayList();

		String eventIdStr = DatabaseIdentifier.toSQLString(event.getId());
		String sql = SQL_SELECT
			+ COLUMN_ID + COMMA			
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_PARAMETER_SORT + COMMA
			+ LINK_COLUMN_PARAMETER_VALUE_NUMBER + COMMA
			+ LINK_COLUMN_PARAMETER_VALUE_STRING + COMMA
			+ LINK_COLUMN_PARAMETER_VALUE_RAW
			+ SQL_FROM
			+ ObjectEntities.EVENTPARAMETER_ENTITY
			+ SQL_WHERE
			+ LINK_COLUMN_EVENT_ID + EQUALS
			+ eventIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EventDatabase.retrieveEventParameters | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			Identifier parameterId;
			int sort;
			EventParameter parameter;
			ParameterType parameterType;
			while (resultSet.next()) {
				parameterId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				sort = resultSet.getInt(LINK_COLUMN_PARAMETER_SORT);
				switch (sort) {
					case EventParameterSort._PARAMETER_SORT_NUMBER:
						parameter = new EventParameter(parameterId,
								parameterType,
								resultSet.getInt(LINK_COLUMN_PARAMETER_VALUE_NUMBER));
						parameters.add(parameter);
						break;
					case EventParameterSort._PARAMETER_SORT_STRING:
						parameter = new EventParameter(parameterId,
								parameterType,
								resultSet.getString(LINK_COLUMN_PARAMETER_VALUE_STRING));
						parameters.add(parameter);
						break;
					case EventParameterSort._PARAMETER_SORT_RAW:
						parameter = new EventParameter(parameterId,
								parameterType,
								ByteArrayDatabase.toByteArray(resultSet.getBlob(LINK_COLUMN_PARAMETER_VALUE_RAW)));
						parameters.add(parameter);
						break;
					default:
						Log.errorMessage("EventDatabase.retrieveEventParameters | Unknown sort: " + sort + " of parameter '" + parameterId + "', event '" + eventIdStr + "'");
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.retrieveEventParameters | Cannot retrieve parameters for event '" + eventIdStr + "' -- " + sqle.getMessage();
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
		event.setParameters0((EventParameter[]) parameters.toArray(new EventParameter[parameters.size()]));
	}

	private void retrieveEventParametersByOneQuery(List events) throws RetrieveObjectException {
    if ((events == null) || (events.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
  			+ COLUMN_ID + COMMA			
  			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
  			+ LINK_COLUMN_PARAMETER_SORT + COMMA
  			+ LINK_COLUMN_PARAMETER_VALUE_NUMBER + COMMA
  			+ LINK_COLUMN_PARAMETER_VALUE_STRING + COMMA
  			+ LINK_COLUMN_PARAMETER_VALUE_RAW
  			+ SQL_FROM
  			+ ObjectEntities.EVENTPARAMETER_ENTITY
  			+ SQL_WHERE
  			+ LINK_COLUMN_EVENT_ID + SQL_IN + OPEN_BRACKET);
    int i = 1;
		for (Iterator it = events.iterator(); it.hasNext(); i++) {
			Event event = (Event) it.next();
			sql.append(DatabaseIdentifier.toSQLString(event.getId()));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(LINK_COLUMN_EVENT_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
			}
		}
		sql.append(CLOSE_BRACKET);

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
				parameterId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				try {
					parameterType = (ParameterType) GeneralStorableObjectPool.getStorableObject(DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_PARAMETER_TYPE_ID), true);
				}
				catch (ApplicationException ae) {
					throw new RetrieveObjectException(ae);
				}
				eventId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_EVENT_ID);
				sort = resultSet.getInt(LINK_COLUMN_PARAMETER_SORT);
				switch (sort) {
					case EventParameterSort._PARAMETER_SORT_NUMBER:
						parameter = new EventParameter(parameterId,
								parameterType,
								resultSet.getInt(LINK_COLUMN_PARAMETER_VALUE_NUMBER));
						break;
					case EventParameterSort._PARAMETER_SORT_STRING:
						parameter = new EventParameter(parameterId,
								parameterType,
								resultSet.getString(LINK_COLUMN_PARAMETER_VALUE_STRING));
						break;
					case EventParameterSort._PARAMETER_SORT_RAW:
						parameter = new EventParameter(parameterId,
								parameterType,
								ByteArrayDatabase.toByteArray(resultSet.getBlob(LINK_COLUMN_PARAMETER_VALUE_RAW)));
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
					event.setParameters0((EventParameter[]) eventParameters.toArray(new EventParameter[eventParameters.size()]));
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

	private void retrieveEventSources(Event event) throws RetrieveObjectException {
		List eventSources = new ArrayList();

		String eventIdStr = DatabaseIdentifier.toSQLString(event.getId());
		String sql = SQL_SELECT
			+ COLUMN_ID + COMMA
			+ LINK_COLUMN_SOURCE_ENTITY_CODE + COMMA
			+ LINK_COLUMN_SOURCE_PORT_ID + COMMA
			+ LINK_COLUMN_SOURCE_EQUIPMENT_ID + COMMA
			+ LINK_COLUMN_SOURCE_LINK_ID + COMMA
			+ LINK_COLUMN_SOURCE_MONITOREDELEMENT_ID
			+ SQL_FROM + ObjectEntities.EVENT_SOURCE_ENTITY
			+ SQL_WHERE + COLUMN_ID + SQL_IN + OPEN_BRACKET
				+ SQL_SELECT
				+ LINK_COLUMN_SOURCE_ID
				+ SQL_FROM + ObjectEntities.EVENT_SOURCE_LINK_ENTITY
				+ SQL_WHERE + LINK_COLUMN_EVENT_ID + EQUALS + eventIdStr
			+ CLOSE_BRACKET;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EventDatabase.retrieveEventSources | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			Identifier eventSourceId;
			short sourceEntityCode;
			while (resultSet.next()) {
				eventSourceId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				sourceEntityCode = resultSet.getShort(LINK_COLUMN_SOURCE_ENTITY_CODE);
				switch (sourceEntityCode) {
					case ObjectEntities.PORT_ENTITY_CODE:
						eventSources.add(new EventSource(eventSourceId,
								DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_PORT_ID)));
						break;
					case ObjectEntities.EQUIPMENT_ENTITY_CODE:
						eventSources.add(new EventSource(eventSourceId,
								DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_EQUIPMENT_ID)));
						break;
					case ObjectEntities.LINK_ENTITY_CODE:
						eventSources.add(new EventSource(eventSourceId,
								DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_LINK_ID)));
						break;
					case ObjectEntities.ME_ENTITY_CODE:
						eventSources.add(new EventSource(eventSourceId,
								DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_MONITOREDELEMENT_ID)));
						break;
					default:
						Log.errorMessage("EventDatabase.retrieveEventSources | Unknown entity code of event source '" + eventSourceId + "', event '" + eventIdStr + "'");
				}
			}
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.retrieveEventSources | Cannot retrieve parameters for event '" + eventIdStr + "' -- " + sqle.getMessage();
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
		event.setEventSources0(eventSources);
	}

	private void retrieveEventSourcesByOneQuery(List events) throws RetrieveObjectException {
    if ((events == null) || (events.isEmpty()))
			return;

    StringBuffer sql = new StringBuffer(SQL_SELECT
  			+ ObjectEntities.EVENT_SOURCE_ENTITY + DOT + COLUMN_ID + COMMA
  			+ ObjectEntities.EVENT_SOURCE_ENTITY + DOT + LINK_COLUMN_SOURCE_ENTITY_CODE + COMMA
  			+ ObjectEntities.EVENT_SOURCE_ENTITY + DOT + LINK_COLUMN_SOURCE_PORT_ID + COMMA
  			+ ObjectEntities.EVENT_SOURCE_ENTITY + DOT + LINK_COLUMN_SOURCE_EQUIPMENT_ID + COMMA
  			+ ObjectEntities.EVENT_SOURCE_ENTITY + DOT + LINK_COLUMN_SOURCE_LINK_ID + COMMA
  			+ ObjectEntities.EVENT_SOURCE_ENTITY + DOT + LINK_COLUMN_SOURCE_MONITOREDELEMENT_ID + COMMA
  			+ ObjectEntities.EVENT_SOURCE_LINK_ENTITY + DOT + LINK_COLUMN_EVENT_ID
  			+ SQL_FROM + ObjectEntities.EVENT_SOURCE_ENTITY + COMMA + ObjectEntities.EVENT_SOURCE_LINK_ENTITY
  			+ SQL_WHERE + ObjectEntities.EVENT_SOURCE_ENTITY + DOT + COLUMN_ID + SQL_IN + OPEN_BRACKET
  				+ SQL_SELECT
  				+ LINK_COLUMN_SOURCE_ID
  				+ SQL_FROM + ObjectEntities.EVENT_SOURCE_LINK_ENTITY
  				+ SQL_WHERE + LINK_COLUMN_EVENT_ID + SQL_IN + OPEN_BRACKET);
    int i = 1;
		for (Iterator it = events.iterator(); it.hasNext(); i++) {
			Event event = (Event) it.next();
			sql.append(DatabaseIdentifier.toSQLString(event.getId()));
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0))
					sql.append(COMMA);
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(LINK_COLUMN_EVENT_ID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
			}
		}
		sql.append(CLOSE_BRACKET);
		sql.append(CLOSE_BRACKET);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EventDatabase.retrieveEventSourcesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			Map eventSourcesMap = new HashMap();
			Identifier eventId;
			Identifier eventSourceId;
			short sourceEntityCode;
			EventSource eventSource;
			List eventSources;
			while (resultSet.next()) {
				eventId = DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_EVENT_ID);
				eventSourceId = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				sourceEntityCode = resultSet.getShort(LINK_COLUMN_SOURCE_ENTITY_CODE);
				eventSource = null;
				switch (sourceEntityCode) {
					case ObjectEntities.PORT_ENTITY_CODE:
						eventSource = new EventSource(eventSourceId, DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_PORT_ID));
						break;
					case ObjectEntities.EQUIPMENT_ENTITY_CODE:
						eventSource = new EventSource(eventSourceId, DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_EQUIPMENT_ID));
						break;
					case ObjectEntities.LINK_ENTITY_CODE:
						eventSource = new EventSource(eventSourceId, DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_LINK_ID));
						break;
					case ObjectEntities.ME_ENTITY_CODE:
						eventSource = new EventSource(eventSourceId, DatabaseIdentifier.getIdentifier(resultSet, LINK_COLUMN_SOURCE_MONITOREDELEMENT_ID));
						break;
					default:
						Log.errorMessage("EventDatabase.retrieveEventSourcesByOneQuery | Unknown entity code of event source '" + eventSourceId + "', event '" + eventId + "'");
				}
				if (eventSource != null) {
					eventSources = (List) eventSourcesMap.get(eventId);
					if (eventSources == null) {
						eventSources = new ArrayList();
						eventSourcesMap.put(eventId, eventSources);
					}
					eventSources.add(eventSource);
				}
			}

			Event event;
			for (Iterator it = events.iterator(); it.hasNext();) {
				event = (Event) it.next();
				eventId = event.getId();
				eventSources = (List) eventSourcesMap.get(eventId);

				if (eventSources != null)
					event.setEventSources0(eventSources);
			}
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.retrieveEventSourcesByOneQuery | Cannot retrieve sources for event -- "
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
			this.insertEventSources(event);
		}
		catch (CreateObjectException coe) {
			this.delete(event);
			throw coe;
		}
	}

	public void insert(List storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			Event event = (Event) it.next();
			this.insertEventParameters(event);
			this.insertEventSources(event);
		}
	}

	private void insertEventParameters(Event event) throws CreateObjectException {
		String eventIdStr = event.getId().toString();
		EventParameter[] eventParameters = event.getParameters();
		String sql = SQL_INSERT_INTO 
			+ ObjectEntities.EVENTPARAMETER_ENTITY
			+ OPEN_BRACKET
			+ COLUMN_ID  + COMMA
			+ LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
			+ LINK_COLUMN_EVENT_ID + COMMA
			+ LINK_COLUMN_PARAMETER_SORT + COMMA
			+ LINK_COLUMN_PARAMETER_VALUE_NUMBER + COMMA
			+ LINK_COLUMN_PARAMETER_VALUE_STRING + COMMA
			+ LINK_COLUMN_PARAMETER_VALUE_RAW
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
						preparedStatement.setBlob(7, BLOB.empty_lob());
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
												 LINK_COLUMN_PARAMETER_VALUE_RAW,
												 COLUMN_ID + EQUALS + DatabaseIdentifier.toSQLString(parameterId));
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

	private void insertEventSources(Event event) throws CreateObjectException {
		String eventIdStr = event.getId().toString();
		List eventSources = event.getEventSources();
		if (eventSources == null || eventSources.isEmpty())
			return;

		String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVENT_SOURCE_LINK_ENTITY + OPEN_BRACKET
			+ LINK_COLUMN_EVENT_ID + COMMA
			+ LINK_COLUMN_SOURCE_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
		PreparedStatement preparedStatement = null;
		Identifier eventSourceId = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			for (Iterator it = eventSources.iterator(); it.hasNext();) {
				eventSourceId = ((EventSource) it.next()).getId();
				DatabaseIdentifier.setIdentifier(preparedStatement, 1, event.getId());
				DatabaseIdentifier.setIdentifier(preparedStatement, 2, eventSourceId);
				Log.debugMessage("EventDatabase.insertEventSources | Inserting event source '" + eventSourceId + "' for event " + eventIdStr, Log.DEBUGLEVEL09);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			String mesg = "EventDatabase.insertEventSources | Cannot insert event source '" + eventSourceId + "' for event '" + eventIdStr + "' -- " + sqle.getMessage();
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

	public void update(StorableObject storableObject, int updateKind, Object obj) throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		Event event = this.fromStorableObject(storableObject);
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntity(storableObject, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntity(storableObject, true);		
				return;
		}
	}

	public void update(List storableObjects, int updateKind, Object arg)
			throws IllegalDataException, VersionCollisionException, UpdateObjectException {
		switch (updateKind) {
			case UPDATE_CHECK:
				super.checkAndUpdateEntities(storableObjects, false);
				break;
			case UPDATE_FORCE:					
			default:
				super.checkAndUpdateEntities(storableObjects, true);		
				return;
		}
	}

	public List retrieveByIds(List ids, String conditions) throws IllegalDataException, RetrieveObjectException {
		List list = null; 
		if ((ids == null) || (ids.isEmpty()))
			list = this.retrieveByIdsOneQuery(null, conditions);
		else
			list = this.retrieveByIdsOneQuery(ids, conditions);

		this.retrieveEventParametersByOneQuery(list);
		this.retrieveEventSourcesByOneQuery(list);

		return list;
	}

	public List retrieveByCondition(List ids, StorableObjectCondition condition)
			throws RetrieveObjectException, IllegalDataException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void delete(StorableObject storableObject) throws IllegalDataException {
		Event event = this.fromStorableObject(storableObject);
		String eventIdStr = DatabaseIdentifier.toSQLString(event.getId());
		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENT_SOURCE_LINK_ENTITY
					+ SQL_WHERE + LINK_COLUMN_EVENT_ID + EQUALS + eventIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENTPARAMETER_ENTITY
					+ SQL_WHERE + LINK_COLUMN_EVENT_ID + EQUALS + eventIdStr);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENT_ENTITY
					+ SQL_WHERE + COLUMN_ID + EQUALS + eventIdStr);
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
