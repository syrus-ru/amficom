/*
 * $Id: EventTypeDatabase.java,v 1.27 2005/05/27 18:38:15 arseniy Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.event.corba.AlertKind;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.DatabaseStorableObjectCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.27 $, $Date: 2005/05/27 18:38:15 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public final class EventTypeDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultipleSQLValues;

	private EventType fromStorableObject(StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventType)
			return (EventType) storableObject;
		throw new IllegalDataException("EventTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	protected short getEntityCode() {		
		return ObjectEntities.EVENTTYPE_ENTITY_CODE;
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
		final EventType eventType = this.fromStorableObject(storableObject);
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
		final EventType eventType = this.fromStorableObject(storableObject);		
		String sql = APOSTOPHE + DatabaseString.toQuerySubString(eventType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTOPHE + COMMA
			+ APOSTOPHE + DatabaseString.toQuerySubString(eventType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTOPHE;
		return sql;
	}

	protected StorableObject updateEntityFromResultSet(StorableObject storableObject, ResultSet resultSet) throws IllegalDataException, SQLException{
		EventType eventType = storableObject == null ?
				new EventType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
											null,
											0L,
											null,
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

	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		EventType eventType = this.fromStorableObject(storableObject);
		this.retrieveEntity(eventType);
		this.retrieveParameterTypeIdsByOneQuery(Collections.singleton(eventType));
		this.retrieveUserAlertKindsByOneQuery(Collections.singleton(eventType));
	}

	private void retrieveParameterTypeIdsByOneQuery(final Set eventTypes) throws RetrieveObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		final Map eventParamaterTypeIdsMap = this.retrieveLinkedEntityIds(eventTypes,
				ObjectEntities.EVENTTYPPARTYPLINK_ENTITY,
				EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID,
				StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);

		for (Iterator it = eventTypes.iterator(); it.hasNext();) {
			final EventType eventType = (EventType) it.next();
			final Identifier eventTypeId = eventType.getId();
			final Set paramaterTypeIds = (Set) eventParamaterTypeIdsMap.get(eventTypeId);

			eventType.setParameterTypeIds0(paramaterTypeIds);
		}
	}

	private void retrieveUserAlertKindsByOneQuery(final Set eventTypes) throws RetrieveObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		final Map dbEventTypeUserAlertKindsMap = this.retrieveDBUserAlertKindsMap(eventTypes);

		for (final Iterator it = eventTypes.iterator(); it.hasNext();) {
			final EventType eventType = (EventType) it.next();
			final Identifier eventTypeId = eventType.getId();
			final Map userAlertKindsMap = (Map) dbEventTypeUserAlertKindsMap.get(eventTypeId);

			eventType.setUserAlertKindsMap0(userAlertKindsMap);
		}
	}

	private Map retrieveDBUserAlertKindsMap(final Set eventTypes) throws RetrieveObjectException {
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ EventTypeWrapper.LINK_COLUMN_USER_ID + COMMA
				+ EventTypeWrapper.LINK_COLUMN_ALERT_KIND + COMMA
				+ EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID
				+ SQL_FROM + ObjectEntities.EVENTTYPEUSERALERT_ENTITY
				+ SQL_WHERE);
		sql.append(idsEnumerationString(eventTypes, EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID, true));

		final Map dbEventTypeUserAlertKindsMap = new HashMap();

		Map userAlertKindsMap;
		Set userAlertKinds;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveDBUserAlertKindsMap | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier userId = DatabaseIdentifier.getIdentifier(resultSet, EventTypeWrapper.LINK_COLUMN_USER_ID);
				final AlertKind alertKind = AlertKind.from_int(resultSet.getInt(EventTypeWrapper.LINK_COLUMN_ALERT_KIND));
				final Identifier eventTypeId = DatabaseIdentifier.getIdentifier(resultSet, EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID);

				userAlertKindsMap = (Map) dbEventTypeUserAlertKindsMap.get(eventTypeId);
				if (userAlertKindsMap == null) {
					userAlertKindsMap = new HashMap();
					dbEventTypeUserAlertKindsMap.put(eventTypeId, userAlertKindsMap);
				}
				userAlertKinds = (Set) userAlertKindsMap.get(userId);
				if (userAlertKinds == null) {
					userAlertKinds = new HashSet();
					userAlertKindsMap.put(userId, userAlertKinds);
				}
				userAlertKinds.add(alertKind);
			}

			return dbEventTypeUserAlertKindsMap;
		}
		catch (SQLException sqle) {
			String mesg = "Cannot retrieve event type user alert kinds";
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

	public Object retrieveObject(StorableObject storableObject, int retrieveKind, Object arg) throws IllegalDataException {
		EventType eventType = this.fromStorableObject(storableObject);
		switch (retrieveKind) {
			default:
				Log.errorMessage("Unknown retrieve kind: " + retrieveKind + " for " + this.getEntityName() + " '" +  eventType.getId() + "'; argument: " + arg);
				return null;
		}
	}

	public void insert(StorableObject storableObject) throws CreateObjectException, IllegalDataException {
		EventType eventType = this.fromStorableObject(storableObject);
		super.insertEntity(eventType);
		try {
			this.updateParameterTypeIds(Collections.singleton(eventType));
			this.updateUserAlertKinds(Collections.singleton(eventType));
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	public void insert(Set storableObjects) throws IllegalDataException, CreateObjectException {
		super.insertEntities(storableObjects);
		try {
			this.updateParameterTypeIds(storableObjects);
			this.updateUserAlertKinds(storableObjects);
		}
		catch (UpdateObjectException uoe) {
			throw new CreateObjectException(uoe);
		}
	}

	/**
	 * NOTE: Updates event type itself and identifiers of users, attached to it
	 * Do not updates parameter types.
	 */
	public void update(StorableObject storableObject, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObject, modifierId, updateKind);
		try {
			this.updateParameterTypeIds(Collections.singleton(storableObject));
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
		this.updateUserAlertKinds(Collections.singleton(storableObject));
	}

	/**
	 * NOTE: Updates event type itself and identifiers of users, attached to it
	 * Do not updates parameter types.
	 */
	public void update(java.util.Set storableObjects, Identifier modifierId, int updateKind)
			throws VersionCollisionException, UpdateObjectException {
		super.update(storableObjects, modifierId, updateKind);
		try {
			this.updateParameterTypeIds(storableObjects);
		}
		catch (IllegalDataException ide) {
			Log.errorException(ide);
		}
		this.updateUserAlertKinds(storableObjects);
	}

	private void updateParameterTypeIds(final Set eventTypes) throws UpdateObjectException, IllegalDataException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		Map parameterTypeIdsMap = new HashMap();
		for (final Iterator it = eventTypes.iterator(); it.hasNext();) {
			final EventType eventType = this.fromStorableObject((StorableObject) it.next());
			final Set parameterTypeIds = eventType.getParameterTypeIds();
			parameterTypeIdsMap.put(eventType.getId(), parameterTypeIds);
		}

		super.updateLinkedEntityIds(parameterTypeIdsMap,
				ObjectEntities.EVENTTYPPARTYPLINK_ENTITY,
				EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID,
				StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
	}

	private void updateUserAlertKinds(final Set eventTypes) throws UpdateObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		Map dbEventTypeUserAlertKindsMap = null;
		try {
			dbEventTypeUserAlertKindsMap = this.retrieveDBUserAlertKindsMap(eventTypes);
		}
		catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map eventTypeUserAlertKindsMap = new HashMap(eventTypes.size());
		for (final Iterator it = eventTypes.iterator(); it.hasNext();) {
			final EventType eventType = (EventType) it.next();
			eventTypeUserAlertKindsMap.put(eventType.getId(), eventType.getUserAlertKindsMap());
		}

		this.updateUserAlertKinds(eventTypeUserAlertKindsMap, dbEventTypeUserAlertKindsMap);
	}

	private void updateUserAlertKinds(final Map eventTypeUserAlertKindsMap, final Map dbEventTypeUserAlertKindsMap)
			throws UpdateObjectException {
		final Map insertUserAlertKindsMap = new HashMap();
		final Map deleteUserAlertKindsMap = new HashMap();
		Map altUserAlertKindsMap;
		Set altUserAlertKinds;
		for (final Iterator it1 = eventTypeUserAlertKindsMap.keySet().iterator(); it1.hasNext();) {
			final Identifier eventTypeId = (Identifier) it1.next();
			final Map userAlertKindsMap = (Map) eventTypeUserAlertKindsMap.get(eventTypeId);
			final Map dbUserAlertKindsMap = (Map) dbEventTypeUserAlertKindsMap.get(eventTypeId);
			if (dbUserAlertKindsMap != null) {
				for (final Iterator it2 = userAlertKindsMap.keySet().iterator(); it2.hasNext();) {
					final Identifier userId = (Identifier) it2.next();
					final Set userAlertKinds = (Set) userAlertKindsMap.get(userId);
					final Set dbUserAlertKinds = (Set) dbUserAlertKindsMap.get(userId);
					if (dbUserAlertKinds != null) {
						for (final Iterator it3 = userAlertKinds.iterator(); it3.hasNext();) {
							final AlertKind alertKind = (AlertKind) it3.next();
							if (!dbUserAlertKinds.contains(alertKind)) {
								//Insert alert kind
								altUserAlertKindsMap = (Map) insertUserAlertKindsMap.get(eventTypeId);
								if (altUserAlertKindsMap == null) {
									altUserAlertKindsMap = new HashMap();
									insertUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
								}
								altUserAlertKinds = (Set) altUserAlertKindsMap.get(userId);
								if (altUserAlertKinds == null) {
									altUserAlertKinds = new HashSet();
									altUserAlertKindsMap.put(userId, altUserAlertKinds);
								}
								altUserAlertKinds.add(alertKind);
							}
						}
						for (final Iterator it3 = dbUserAlertKinds.iterator(); it3.hasNext();) {
							final AlertKind alertKind = (AlertKind) it3.next();
							if (!userAlertKinds.contains(alertKind)) {
								//Delete alert kind
								altUserAlertKindsMap = (Map) deleteUserAlertKindsMap.get(eventTypeId);
								if (altUserAlertKindsMap == null) {
									altUserAlertKindsMap = new HashMap();
									deleteUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
								}
								altUserAlertKinds = (Set) altUserAlertKindsMap.get(userId);
								if (altUserAlertKinds == null) {
									altUserAlertKinds = new HashSet();
									altUserAlertKindsMap.put(userId, altUserAlertKinds);
								}
								altUserAlertKinds.add(alertKind);
							}
						}
					}
					else {
						//Insert all userAlertingKinds for this userId and this eventTypeId
						altUserAlertKindsMap = (Map) insertUserAlertKindsMap.get(eventTypeId);
						if (altUserAlertKindsMap == null) {
							altUserAlertKindsMap = new HashMap();
							insertUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
						}
						altUserAlertKindsMap.put(userId, userAlertKinds);
					}
				}
				for (final Iterator it2 = dbUserAlertKindsMap.keySet().iterator(); it2.hasNext();) {
					final Identifier userId = (Identifier) it2.next();
					if (!userAlertKindsMap.containsKey(userId)) {
						//Delete all userAlertingKinds for this userId and this eventTypeId
						altUserAlertKindsMap = (Map) deleteUserAlertKindsMap.get(eventTypeId);
						if (altUserAlertKindsMap == null) {
							altUserAlertKindsMap = new HashMap();
							deleteUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
						}
						altUserAlertKindsMap.put(userId, dbUserAlertKindsMap.get(userId));
					}
				}
			}
			else {
				//Insert all userAlertingKinds for all userId for this eventTypeId
				altUserAlertKindsMap = new HashMap();
				altUserAlertKindsMap.putAll(userAlertKindsMap);
				insertUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
			}
		}

		try {
			this.insertUserAlertKinds(insertUserAlertKindsMap);
		}
		catch (CreateObjectException coe) {
			throw new UpdateObjectException(coe);
		}
		this.deleteUserAlertKinds(deleteUserAlertKindsMap);

	}

	private void insertUserAlertKinds(final Map insertUserAlertKindsMap) throws CreateObjectException {
		if (insertUserAlertKindsMap == null || insertUserAlertKindsMap.isEmpty())
			return;

		String sql = SQL_INSERT_INTO + ObjectEntities.EVENTTYPEUSERALERT_ENTITY + OPEN_BRACKET
				+ EventTypeWrapper.LINK_COLUMN_USER_ID + COMMA
				+ EventTypeWrapper.LINK_COLUMN_ALERT_KIND + COMMA
				+ EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		Connection connection = DatabaseConnection.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);

			for (final Iterator it1 = insertUserAlertKindsMap.keySet().iterator(); it1.hasNext();) {
				final Identifier eventTypeId = (Identifier) it1.next();
				final Map userAlertKindsMap = (Map) insertUserAlertKindsMap.get(eventTypeId);
				for (final Iterator it2 = userAlertKindsMap.keySet().iterator(); it2.hasNext();) {
					final Identifier userId = (Identifier) it2.next();
					final Set userAlertKinds = (Set) userAlertKindsMap.get(userId);
					for (final Iterator it3 = userAlertKinds.iterator(); it3.hasNext();) {
						final AlertKind alertKind = (AlertKind) it3.next();
						DatabaseIdentifier.setIdentifier(preparedStatement, 1, userId);
						preparedStatement.setInt(2, alertKind.value());
						DatabaseIdentifier.setIdentifier(preparedStatement, 3, eventTypeId);
						Log.debugMessage(this.getEntityName() + "Database.insertUserAlertKinds | Inserting alerting kind "
								+ alertKind.value() + " for user '" + userId + "' and event type '" + eventTypeId
								+ "'", Log.DEBUGLEVEL09);
						preparedStatement.executeUpdate();
					}
				}
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			throw new CreateObjectException("Cannot insert event type user alert kinds -- " + sqle.getMessage(), sqle);
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

	private void deleteUserAlertKinds(final Map deleteUserAlertKindsMap) {
		if (deleteUserAlertKindsMap == null || deleteUserAlertKindsMap.isEmpty())
			return;

		StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + ObjectEntities.EVENTTYPEUSERALERT_ENTITY
				+ SQL_WHERE + DatabaseStorableObjectCondition.FALSE_CONDITION);

		for (final Iterator it1 = deleteUserAlertKindsMap.keySet().iterator(); it1.hasNext();) {
			final Identifier eventTypeId = (Identifier) it1.next();
			final Map userAlertKindsMap = (Map) deleteUserAlertKindsMap.get(eventTypeId);
			for (final Iterator it2 = userAlertKindsMap.keySet().iterator(); it2.hasNext();) {
				final Identifier userId = (Identifier) it2.next();
				final Set userAlertKinds = (Set) userAlertKindsMap.get(userId);
				for (final Iterator it3 = userAlertKinds.iterator(); it3.hasNext();) {
					final AlertKind alertKind = (AlertKind) it3.next();

					sql.append(SQL_OR + OPEN_BRACKET
							+ EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID + EQUALS + DatabaseIdentifier.toSQLString(eventTypeId) + SQL_AND
							+ EventTypeWrapper.LINK_COLUMN_USER_ID + EQUALS + DatabaseIdentifier.toSQLString(userId) + SQL_AND
							+ EventTypeWrapper.LINK_COLUMN_ALERT_KIND + EQUALS + alertKind.value()
							+ CLOSE_BRACKET);
				}
			}
		}

		Statement statement = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.deleteUserAlertKinds | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle1) {
			Log.errorException(sqle1);
		}
		finally {
			try {
				if (statement != null)
					statement.close();
				statement = null;
			}
			catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
			finally {
				DatabaseConnection.releaseConnection(connection);
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
		Set objects = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypeIdsByOneQuery(objects);
		this.retrieveUserAlertKindsByOneQuery(objects);
		return objects;
	}

}
