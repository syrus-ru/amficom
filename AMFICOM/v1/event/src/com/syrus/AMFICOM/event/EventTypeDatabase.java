/*
 * $Id: EventTypeDatabase.java,v 1.15 2005/02/28 14:12:41 bob Exp $
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
import java.util.Iterator;
import java.util.List;
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
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.15 $, $Date: 2005/02/28 14:12:41 $
 * @author $Author: bob $
 * @module event_v1
 */

public class EventTypeDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultiplySQLValues;

	private EventType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventType)
			return (EventType) storableObject;
		throw new IllegalDataException("EventTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected String getEnityName() {
		return ObjectEntities.EVENTTYPE_ENTITY;
	}

	protected String getColumns(int mode) {
		if (columns == null) {
			columns = COMMA
				+ StorableObjectWrapper.COLUMN_CODENAME + COMMA 
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return super.getColumns(mode) + columns;
	}

	protected int setEntityForPreparedStatement(StorableObject storableObject, PreparedStatement preparedStatement, int mode)
			throws IllegalDataException, SQLException {
		EventType eventType = this.fromStorableObject(storableObject);
		int i = super.setEntityForPreparedStatement(storableObject, preparedStatement, mode);
		DatabaseString.setString(preparedStatement, ++i, eventType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++i, eventType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return i;
	}

	protected String getUpdateMultiplySQLValues() {
		if (updateMultiplySQLValues == null) {
			updateMultiplySQLValues = super.getUpdateMultiplySQLValues() + COMMA
				+ QUESTION + COMMA 
				+ QUESTION;
		}
		return updateMultiplySQLValues;
	}

	protected String getUpdateSingleSQLValues(StorableObject storableObject) throws IllegalDataException {
		EventType eventType = this.fromStorableObject(storableObject);		
		String sql = super.getUpdateSingleSQLValues(storableObject) + COMMA 
			+ APOSTOPHE + DatabaseString.toQuerySubString(eventType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA 
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
		List parTyps = new ArrayList();
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

		((ArrayList)parTyps).trimToSize();
		eventType.setParameterTypes(parTyps);
	}

	private void retrieveParameterTypesByOneQuery(Collection eventTypes) throws RetrieveObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		Map eventParamaterTypeIdsMap = null;
		try {
			eventParamaterTypeIdsMap = this.retrieveLinkedEntityIds(eventTypes,
					ObjectEntities.EVENTTYPPARTYPLINK_ENTITY,
					EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID,
					StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
		}
		catch (IllegalDataException e) {
			throw new RetrieveObjectException(e);
		}

		EventType eventType;
		Identifier eventTypeId;
		Collection paramaterTypeIds;
		for (Iterator it = eventTypes.iterator(); it.hasNext();) {
			eventType = (EventType) it.next();
			eventTypeId = eventType.getId();
			paramaterTypeIds = (Collection) eventParamaterTypeIdsMap.get(eventTypeId);

			try {
				eventType.setParameterTypes0(GeneralStorableObjectPool.getStorableObjects(paramaterTypeIds, true));
			}
			catch (ApplicationException ce) {
				throw new RetrieveObjectException(ce);
			}
		}
	}

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
//		EventType eventType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		EventType eventType = this.fromStorableObject(storableObject);
		this.insertEntity(eventType);
		this.insertParameterTypes(eventType);
	}

	public void insert(Collection storableObjects) throws IllegalDataException, CreateObjectException {
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
		Collection parTyps = eventType.getParameterTypes();
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

	public void delete(Identifier id) throws IllegalDataException {
		if (id.getMajor() != ObjectEntities.EVENTTYPE_ENTITY_CODE)
			throw new IllegalDataException("EventTypeDatabase.delete | Cannot delete object of code "
					+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'");

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

	public void delete(Collection objects) throws IllegalDataException {
		StringBuffer sql1 = new StringBuffer(SQL_DELETE_FROM
				+ ObjectEntities.EVENTTYPPARTYPLINK_ENTITY
				+ SQL_WHERE);
		sql1.append(this.idsEnumerationString(objects, EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID, true));
		StringBuffer sql2 = new StringBuffer(SQL_DELETE_FROM
				+ ObjectEntities.EVENTTYPE_ENTITY
				+ SQL_WHERE);
		sql2.append(this.idsEnumerationString(objects, StorableObjectWrapper.COLUMN_ID, true));

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

	public Collection retrieveByIds(Collection ids, String condition) throws IllegalDataException, RetrieveObjectException {
		Collection collection = null; 
		if ((ids == null) || (ids.isEmpty()))
			collection = this.retrieveByIdsOneQuery(null, condition);
		else
			collection = this.retrieveByIdsOneQuery(ids, condition);

		this.retrieveParameterTypesByOneQuery(collection);

		return collection;		
	}

}
