/*
 * $Id: EventTypeDatabase.java,v 1.22 2005/04/12 17:07:46 arseniy Exp $
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.22 $, $Date: 2005/04/12 17:07:46 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public class EventTypeDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultipleSQLValues;

	private EventType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventType)
			return (EventType) storableObject;
		throw new IllegalDataException("EventTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.EVENTTYPE_ENTITY;
	}

	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA 
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	protected int setEntityForPreparedStatementTmpl(StorableObject storableObject, PreparedStatement preparedStatement, int startParameterNumber)
			throws IllegalDataException, SQLException {
		EventType eventType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, eventType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, eventType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
	}

	protected String getUpdateMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA 
				+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	protected String getUpdateSingleSQLValuesTmpl(StorableObject storableObject) throws IllegalDataException {
		EventType eventType = this.fromStorableObject(storableObject);		
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(eventType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(eventType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	public void retrieve(StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EventType eventType = this.fromStorableObject(storableObject);
		this.retrieveEntity(eventType);
		this.retrieveParameterTypes(eventType);
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws IllegalDataException, RetrieveObjectException, SQLException{
		EventType eventType = storableObject == null ? 
				new EventType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
											null,
											0L,
											null,
											null,
											null) :
				this.fromStorableObject(storableObject);
		eventType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
								   DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
								   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
								   DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
								   resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION),
								   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
								   DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return eventType;
	}

	private void retrieveParameterTypes(EventType eventType) throws RetrieveObjectException {
		Set parTyps = new HashSet();
		String eventTypeIdStr = DatabaseIdentifier.toSQLString(eventType.getId());
		String sql = SQL_SELECT
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID
			+ SQL_FROM + ObjectEntities.EVENTTYPPARTYPLINK_ENTITY
			+ SQL_WHERE + EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID + EQUALS + eventTypeIdStr;
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage("EventTypeDatabase.retrieveParameterType | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql);
			Identifier parameterTypeId;
			while (resultSet.next()) {
				parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				parTyps.add(GeneralStorableObjectPool.getStorableObject(parameterTypeId, true));
			}
		}
		catch (SQLException sqle) {
			String mesg = "EventTypeDatabase.retrieveParameterTypes | Cannot retrieve parameter type ids for event type '" + eventTypeIdStr + "' -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		catch (ApplicationException ae) {
			throw new RetrieveObjectException(ae);
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

		eventType.setParameterTypes(parTyps);
	}

	private void retrieveParameterTypesByOneQuery(Set eventTypes) throws RetrieveObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		Map eventParamaterTypeIdsMap = null;
		eventParamaterTypeIdsMap = this.retrieveLinkedEntityIds(eventTypes,
				ObjectEntities.EVENTTYPPARTYPLINK_ENTITY,
				EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID,
				StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);

		EventType eventType;
		Identifier eventTypeId;
		Set paramaterTypeIds;
		for (Iterator it = eventTypes.iterator(); it.hasNext();) {
			eventType = (EventType) it.next();
			eventTypeId = eventType.getId();
			paramaterTypeIds = (Set) eventParamaterTypeIdsMap.get(eventTypeId);

			try {
				eventType.setParameterTypes0(GeneralStorableObjectPool.getStorableObjects(paramaterTypeIds, true));
			}
			catch (ApplicationException ce) {
				throw new RetrieveObjectException(ce);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EventType eventType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEnityName() + " '" +  eventType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		EventType eventType = this.fromStorableObject(storableObject);
		this.insertEntity(eventType);
		this.insertParameterTypes(eventType);
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		this.insertEntities(storableObjects);
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			EventType eventType = this.fromStorableObject((StorableObject)it.next());
			this.insertParameterTypes(eventType);
		}
	}

	private PreparedStatement insertParameterTypesPreparedStatement() throws SQLException{
		PreparedStatement preparedStatement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			String sql = SQL_INSERT_INTO
			+ ObjectEntities.EVENTTYPPARTYPLINK_ENTITY + OPEN_BRACKET
			+ EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID + COMMA
			+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID
			+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
			+ QUESTION + COMMA
			+ QUESTION
			+ CLOSE_BRACKET;
			preparedStatement = connection.prepareStatement(sql);
		}
		finally {
			DatabaseConnection.releaseConnection(connection);
		}
		return preparedStatement;
	}

	private void updatePrepareStatementValues(PreparedStatement preparedStatement, EventType eventType) throws SQLException {
		Set parTyps = eventType.getParameterTypes();
		Identifier eventTypeId = eventType.getId();
		Identifier parameterTypeId = null;

		for (Iterator iterator = parTyps.iterator(); iterator.hasNext();) {
			parameterTypeId = ((ParameterType) iterator.next()).getId();
			DatabaseIdentifier.setIdentifier(preparedStatement, 1, eventTypeId);
			DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
			Log.debugMessage("EventTypeDatabase.insertParameterTypes | Inserting parameter type '" + parameterTypeId + "' for event type '" + eventTypeId + "'", Log.DEBUGLEVEL09);
			preparedStatement.executeUpdate();
		}
	}

	private void insertParameterTypes(EventType eventType) throws CreateObjectException {
		PreparedStatement preparedStatement = null;
		Identifier eventTypeId = eventType.getId();
		try {
			preparedStatement = this.insertParameterTypesPreparedStatement();
			this.updatePrepareStatementValues(preparedStatement, eventType);
		}
		catch (SQLException sqle) {
			String mesg = "EventTypeDatabase.insertParameterTypes | Cannot insert parameter type for event type '" + eventTypeId + "' -- " + sqle.getMessage();
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
		}
	}

	public void delete(Identifier id) {
		assert (id.getMajor() == ObjectEntities.EVENTTYPE_ENTITY_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENTTYPPARTYPLINK_ENTITY
					+ SQL_WHERE + EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID + EQUALS + id);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENTTYPE_ENTITY 
					+ SQL_WHERE + StorableObjectWrapper.COLUMN_ID + EQUALS + id);

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

	public void delete(Set objects) {
		StringBuffer sql1 = new StringBuffer(SQL_DELETE_FROM
				+ ObjectEntities.EVENTTYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
		StringBuffer sql2 = new StringBuffer(SQL_DELETE_FROM
				+ ObjectEntities.EVENTTYPE_ENTITY
				+ SQL_WHERE);
		sql1.append(idsEnumerationString(objects, EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID, true));
		sql2.append(idsEnumerationString(objects, StorableObjectWrapper.COLUMN_ID, true));

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		String sql;
		try {
			statement = connection.createStatement();

			sql = sql1.toString();
			Log.debugMessage("EventTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);

			sql = sql2.toString();
			Log.debugMessage("EventTypeDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql);
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

	protected Set retrieveByCondition(String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		Set collection = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypesByOneQuery(collection);
		return collection;
	}

}
