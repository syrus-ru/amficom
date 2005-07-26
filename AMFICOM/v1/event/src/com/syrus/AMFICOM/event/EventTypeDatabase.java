/*
 * $Id: EventTypeDatabase.java,v 1.34 2005/07/26 08:39:13 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
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
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.event.corba.IdlEventTypePackage.AlertKind;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.DatabaseStorableObjectCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.34 $, $Date: 2005/07/26 08:39:13 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public final class EventTypeDatabase extends StorableObjectDatabase {

	private static String columns;
	private static String updateMultipleSQLValues;

	private EventType fromStorableObject(final StorableObject storableObject) throws IllegalDataException {
		if (storableObject instanceof EventType)
			return (EventType) storableObject;
		throw new IllegalDataException("EventTypeDatabase.fromStorableObject | Illegal Storable Object: " + storableObject.getClass().getName());
	}

	@Override
	protected short getEntityCode() {		
		return ObjectEntities.EVENT_TYPE_CODE;
	}

	@Override
	protected String getColumnsTmpl() {
		if (columns == null) {
			columns = StorableObjectWrapper.COLUMN_CODENAME + COMMA
				+ StorableObjectWrapper.COLUMN_DESCRIPTION;
		}
		return columns;
	}

	@Override
	protected int setEntityForPreparedStatementTmpl(final StorableObject storableObject,
			final PreparedStatement preparedStatement,
			int startParameterNumber) throws IllegalDataException, SQLException {
		final EventType eventType = this.fromStorableObject(storableObject);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, eventType.getCodename(), SIZE_CODENAME_COLUMN);
		DatabaseString.setString(preparedStatement, ++startParameterNumber, eventType.getDescription(), SIZE_DESCRIPTION_COLUMN);
		return startParameterNumber;
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
	protected String getUpdateSingleSQLValuesTmpl(final StorableObject storableObject) throws IllegalDataException {
		final EventType eventType = this.fromStorableObject(storableObject);		
		final String sql = APOSTROPHE + DatabaseString.toQuerySubString(eventType.getCodename(), SIZE_CODENAME_COLUMN) + APOSTROPHE + COMMA
			+ APOSTROPHE + DatabaseString.toQuerySubString(eventType.getDescription(), SIZE_DESCRIPTION_COLUMN) + APOSTROPHE;
		return sql;
	}

	@Override
	protected StorableObject updateEntityFromResultSet(final StorableObject storableObject, final ResultSet resultSet)
			throws IllegalDataException,
				SQLException {
		final EventType eventType = storableObject == null
				? new EventType(DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_ID),
						null,
						StorableObjectVersion.ILLEGAL_VERSION,
						null,
						null,
						null,
						null)
					: this.fromStorableObject(storableObject);
		eventType.setAttributes(DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_CREATED),
				DatabaseDate.fromQuerySubString(resultSet, StorableObjectWrapper.COLUMN_MODIFIED),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_CREATOR_ID),
				DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.COLUMN_MODIFIER_ID),
				new StorableObjectVersion(resultSet.getLong(StorableObjectWrapper.COLUMN_VERSION)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_CODENAME)),
				DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.COLUMN_DESCRIPTION)));
		return eventType;
	}

	@Override
	public void retrieve(final StorableObject storableObject)
			throws IllegalDataException, ObjectNotFoundException, RetrieveObjectException {
		final EventType eventType = this.fromStorableObject(storableObject);
		this.retrieveEntity(eventType);
		this.retrieveParameterTypeIdsByOneQuery(Collections.singleton(eventType));
		this.retrieveUserAlertKindsByOneQuery(Collections.singleton(eventType));
	}

	private void retrieveParameterTypeIdsByOneQuery(final Set<EventType> eventTypes) throws RetrieveObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> eventParamaterTypeIdsMap = this.retrieveLinkedEntityIds(eventTypes,
				ObjectEntities.EVENTTYPPARTYPLINK,
				EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID,
				StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);

		for (final EventType eventType : eventTypes) {
			final Identifier eventTypeId = eventType.getId();
			final Set<Identifier> paramaterTypeIds = eventParamaterTypeIdsMap.get(eventTypeId);

			eventType.setParameterTypeIds0(paramaterTypeIds);
		}
	}

	private void retrieveUserAlertKindsByOneQuery(final Set<EventType> eventTypes) throws RetrieveObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		final Map<Identifier, Map<Identifier, Set<AlertKind>>> dbEventTypeUserAlertKindsMap = this.retrieveDBUserAlertKindsMap(eventTypes);

		for (final EventType eventType : eventTypes) {
			final Identifier eventTypeId = eventType.getId();
			final Map<Identifier, Set<AlertKind>> userAlertKindsMap = dbEventTypeUserAlertKindsMap.get(eventTypeId);

			eventType.setUserAlertKindsMap0(userAlertKindsMap);
		}
	}

	private Map<Identifier, Map<Identifier, Set<AlertKind>>> retrieveDBUserAlertKindsMap(final Set<EventType> eventTypes) throws RetrieveObjectException {
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ EventTypeWrapper.LINK_COLUMN_USER_ID + COMMA
				+ EventTypeWrapper.LINK_COLUMN_ALERT_KIND + COMMA
				+ EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID
				+ SQL_FROM + ObjectEntities.EVENTTYPEUSERALERT
				+ SQL_WHERE);
		sql.append(idsEnumerationString(eventTypes, EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID, true));

		final Map<Identifier, Map<Identifier, Set<AlertKind>>> dbEventTypeUserAlertKindsMap = new HashMap<Identifier, Map<Identifier, Set<AlertKind>>>();

		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveDBUserAlertKindsMap | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier userId = DatabaseIdentifier.getIdentifier(resultSet, EventTypeWrapper.LINK_COLUMN_USER_ID);
				final AlertKind alertKind = AlertKind.from_int(resultSet.getInt(EventTypeWrapper.LINK_COLUMN_ALERT_KIND));
				final Identifier eventTypeId = DatabaseIdentifier.getIdentifier(resultSet, EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID);

				Map<Identifier, Set<AlertKind>> userAlertKindsMap = dbEventTypeUserAlertKindsMap.get(eventTypeId);
				if (userAlertKindsMap == null) {
					userAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
					dbEventTypeUserAlertKindsMap.put(eventTypeId, userAlertKindsMap);
				}
				Set<AlertKind> userAlertKinds = userAlertKindsMap.get(userId);
				if (userAlertKinds == null) {
					userAlertKinds = new HashSet<AlertKind>();
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

	@Override
	public void insert(final Set storableObjects) throws IllegalDataException, CreateObjectException {
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
	@Override
	public void update(final Set storableObjects) throws UpdateObjectException {
		super.update(storableObjects);
		this.updateParameterTypeIds(storableObjects);
		this.updateUserAlertKinds(storableObjects);
	}

	private void updateParameterTypeIds(final Set<EventType> eventTypes) throws UpdateObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		final Map<Identifier, Set<Identifier>> parameterTypeIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final EventType eventType : eventTypes) {
			final Set<Identifier> parameterTypeIds = eventType.getParameterTypeIds();
			parameterTypeIdsMap.put(eventType.getId(), parameterTypeIds);
		}

		super.updateLinkedEntityIds(parameterTypeIdsMap,
				ObjectEntities.EVENTTYPPARTYPLINK,
				EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID,
				StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
	}

	private void updateUserAlertKinds(final Set<EventType> eventTypes) throws UpdateObjectException {
		if ((eventTypes == null) || (eventTypes.isEmpty()))
			return;

		Map<Identifier, Map<Identifier, Set<AlertKind>>> dbEventTypeUserAlertKindsMap = null;
		try {
			dbEventTypeUserAlertKindsMap = this.retrieveDBUserAlertKindsMap(eventTypes);
		}
		catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, Map<Identifier, Set<AlertKind>>> eventTypeUserAlertKindsMap = new HashMap<Identifier, Map<Identifier, Set<AlertKind>>>(eventTypes.size());
		for (final EventType eventType : eventTypes) {
			eventTypeUserAlertKindsMap.put(eventType.getId(), eventType.getUserAlertKindsMap());
		}

		this.updateUserAlertKinds(eventTypeUserAlertKindsMap, dbEventTypeUserAlertKindsMap);
	}

	private void updateUserAlertKinds(final Map<Identifier, Map<Identifier, Set<AlertKind>>> eventTypeUserAlertKindsMap,
			final Map<Identifier, Map<Identifier, Set<AlertKind>>> dbEventTypeUserAlertKindsMap) throws UpdateObjectException {
		final Map<Identifier, Map<Identifier, Set<AlertKind>>> insertUserAlertKindsMap = new HashMap<Identifier, Map<Identifier, Set<AlertKind>>>();
		final Map<Identifier, Map<Identifier, Set<AlertKind>>> deleteUserAlertKindsMap = new HashMap<Identifier, Map<Identifier, Set<AlertKind>>>();
		Map<Identifier, Set<AlertKind>> altUserAlertKindsMap;
		Set<AlertKind> altUserAlertKinds;
		for (final Identifier eventTypeId : eventTypeUserAlertKindsMap.keySet()) {
			final Map<Identifier, Set<AlertKind>> userAlertKindsMap = eventTypeUserAlertKindsMap.get(eventTypeId);
			final Map<Identifier, Set<AlertKind>> dbUserAlertKindsMap = dbEventTypeUserAlertKindsMap.get(eventTypeId);
			if (dbUserAlertKindsMap != null) {
				for (final Identifier userId : userAlertKindsMap.keySet()) {
					final Set<AlertKind> userAlertKinds = userAlertKindsMap.get(userId);
					final Set<AlertKind> dbUserAlertKinds = dbUserAlertKindsMap.get(userId);
					if (dbUserAlertKinds != null) {
						for (final AlertKind alertKind : userAlertKinds) {
							if (!dbUserAlertKinds.contains(alertKind)) {
								//Insert alert kind
								altUserAlertKindsMap = insertUserAlertKindsMap.get(eventTypeId);
								if (altUserAlertKindsMap == null) {
									altUserAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
									insertUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
								}
								altUserAlertKinds = altUserAlertKindsMap.get(userId);
								if (altUserAlertKinds == null) {
									altUserAlertKinds = new HashSet<AlertKind>();
									altUserAlertKindsMap.put(userId, altUserAlertKinds);
								}
								altUserAlertKinds.add(alertKind);
							}
						}
						for (final AlertKind alertKind : dbUserAlertKinds) {
							if (!userAlertKinds.contains(alertKind)) {
								//Delete alert kind
								altUserAlertKindsMap = deleteUserAlertKindsMap.get(eventTypeId);
								if (altUserAlertKindsMap == null) {
									altUserAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
									deleteUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
								}
								altUserAlertKinds = altUserAlertKindsMap.get(userId);
								if (altUserAlertKinds == null) {
									altUserAlertKinds = new HashSet<AlertKind>();
									altUserAlertKindsMap.put(userId, altUserAlertKinds);
								}
								altUserAlertKinds.add(alertKind);
							}
						}
					}
					else {
						//Insert all userAlertingKinds for this userId and this eventTypeId
						altUserAlertKindsMap = insertUserAlertKindsMap.get(eventTypeId);
						if (altUserAlertKindsMap == null) {
							altUserAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
							insertUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
						}
						altUserAlertKindsMap.put(userId, userAlertKinds);
					}
				}
				for (final Identifier userId : dbUserAlertKindsMap.keySet()) {
					if (!userAlertKindsMap.containsKey(userId)) {
						//Delete all userAlertingKinds for this userId and this eventTypeId
						altUserAlertKindsMap = deleteUserAlertKindsMap.get(eventTypeId);
						if (altUserAlertKindsMap == null) {
							altUserAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
							deleteUserAlertKindsMap.put(eventTypeId, altUserAlertKindsMap);
						}
						altUserAlertKindsMap.put(userId, dbUserAlertKindsMap.get(userId));
					}
				}
			}
			else {
				//Insert all userAlertingKinds for all userId for this eventTypeId
				altUserAlertKindsMap = new HashMap<Identifier, Set<AlertKind>>();
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

	private void insertUserAlertKinds(final Map<Identifier, Map<Identifier, Set<AlertKind>>> insertUserAlertKindsMap) throws CreateObjectException {
		if (insertUserAlertKindsMap == null || insertUserAlertKindsMap.isEmpty())
			return;

		final String sql = SQL_INSERT_INTO + ObjectEntities.EVENTTYPEUSERALERT + OPEN_BRACKET
				+ EventTypeWrapper.LINK_COLUMN_USER_ID + COMMA
				+ EventTypeWrapper.LINK_COLUMN_ALERT_KIND + COMMA
				+ EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		final Connection connection = DatabaseConnection.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);

			for (final Identifier eventTypeId : insertUserAlertKindsMap.keySet()) {
				final Map<Identifier, Set<AlertKind>> userAlertKindsMap = insertUserAlertKindsMap.get(eventTypeId);
				for (final Identifier userId : userAlertKindsMap.keySet()) {
					final Set<AlertKind> userAlertKinds = userAlertKindsMap.get(userId);
					for (final AlertKind alertKind : userAlertKinds) {
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

	private void deleteUserAlertKinds(final Map<Identifier, Map<Identifier, Set<AlertKind>>> deleteUserAlertKindsMap) {
		if (deleteUserAlertKindsMap == null || deleteUserAlertKindsMap.isEmpty())
			return;

		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + ObjectEntities.EVENTTYPEUSERALERT
				+ SQL_WHERE + DatabaseStorableObjectCondition.FALSE_CONDITION);

		for (final Identifier eventTypeId : deleteUserAlertKindsMap.keySet()) {
			final Map<Identifier, Set<AlertKind>> userAlertKindsMap = deleteUserAlertKindsMap.get(eventTypeId);
			for (final Identifier userId : userAlertKindsMap.keySet()) {
				final Set<AlertKind> userAlertKinds = userAlertKindsMap.get(userId);
				for (final AlertKind alertKind : userAlertKinds) {
					sql.append(SQL_OR + OPEN_BRACKET
							+ EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID + EQUALS + DatabaseIdentifier.toSQLString(eventTypeId) + SQL_AND
							+ EventTypeWrapper.LINK_COLUMN_USER_ID + EQUALS + DatabaseIdentifier.toSQLString(userId) + SQL_AND
							+ EventTypeWrapper.LINK_COLUMN_ALERT_KIND + EQUALS + alertKind.value()
							+ CLOSE_BRACKET);
				}
			}
		}

		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
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

	@Override
	public void delete(final Identifier id) {
		assert (id.getMajor() == ObjectEntities.EVENT_TYPE_CODE) : "Illegal entity code: "
			+ id.getMajor() + ", entity '" + ObjectEntities.codeToString(id.getMajor()) + "'";

		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENTTYPPARTYPLINK
					+ SQL_WHERE + EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID + EQUALS + id);
			statement.executeUpdate(SQL_DELETE_FROM
					+ ObjectEntities.EVENT_TYPE
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

	@Override
	public void delete(final Set<? extends Identifiable> objects) {
		final StringBuffer sql1 = new StringBuffer(SQL_DELETE_FROM
				+ ObjectEntities.EVENTTYPPARTYPLINK
				+ SQL_WHERE);
		final StringBuffer sql2 = new StringBuffer(SQL_DELETE_FROM
				+ ObjectEntities.EVENT_TYPE
				+ SQL_WHERE);
		sql1.append(idsEnumerationString(objects, EventTypeWrapper.LINK_COLUMN_EVENT_TYPE_ID, true));
		sql2.append(idsEnumerationString(objects, StorableObjectWrapper.COLUMN_ID, true));

		Statement statement = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();

			Log.debugMessage("EventTypeDatabase.delete | Trying: " + sql1, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql1.toString());

			Log.debugMessage("EventTypeDatabase.delete | Trying: " + sql2, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql2.toString());
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

	@Override
	protected Set retrieveByCondition(final String conditionQuery) throws RetrieveObjectException, IllegalDataException {
		final Set objects = super.retrieveByCondition(conditionQuery);
		this.retrieveParameterTypeIdsByOneQuery(objects);
		this.retrieveUserAlertKindsByOneQuery(objects);
		return objects;
	}

}
