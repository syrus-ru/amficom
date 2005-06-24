/*
 * $Id: ActionTypeDatabase.java,v 1.7 2005/06/24 13:54:35 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/24 13:54:35 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public abstract class ActionTypeDatabase extends StorableObjectDatabase {

	abstract String getParameterTypeLinkTableName();

	abstract String getActionTypeColumnName();

	final void retrieveParameterTypeIdsByOneQuery(final Set actionTypes) throws RetrieveObjectException {
		if (actionTypes == null || actionTypes.isEmpty())
			return;

		Map dbParameterTypeIdsMap = this.retrieveDBParameterTypeIdsMap(actionTypes);

		for (final Iterator it = actionTypes.iterator(); it.hasNext();) {
			final ActionType actionType = (ActionType) it.next();
			final Identifier actionTypeId = actionType.getId();
			final Map parameterTypeIdsModeMap = (Map) dbParameterTypeIdsMap.get(actionTypeId);

			actionType.setParameterTypeIds(parameterTypeIdsModeMap);
		}
	}

	private Map<Identifier, Map<String, Set<Identifier>>> retrieveDBParameterTypeIdsMap(final Set<? extends ActionType> actionTypes)
			throws RetrieveObjectException {
		final String tableName = this.getParameterTypeLinkTableName();
		final String actionTypeColumnName = this.getActionTypeColumnName();
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ actionTypeColumnName+ COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(idsEnumerationString(actionTypes, actionTypeColumnName, true));

		final Map<Identifier, Map<String, Set<Identifier>>> dbParTypeIdsMap = new HashMap<Identifier, Map<String, Set<Identifier>>>();

		Statement statement = null;
		ResultSet resultSet = null;
		final Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveDBParameterTypeIdsMap | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier actionTypeId = DatabaseIdentifier.getIdentifier(resultSet, actionTypeColumnName);
				final String parameterMode = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE));
				final Identifier parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet,
						StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);

				Map<String, Set<Identifier>> parTypeIdsModeMap = dbParTypeIdsMap.get(actionTypeId);
				if (parTypeIdsModeMap == null) {
					parTypeIdsModeMap = new HashMap<String, Set<Identifier>>();
					dbParTypeIdsMap.put(actionTypeId, parTypeIdsModeMap);
				}

				Set<Identifier> parameterTypeIds = parTypeIdsModeMap.get(parameterMode);
				if (parameterTypeIds == null) {
					parameterTypeIds = new HashSet<Identifier>();
					parTypeIdsModeMap.put(parameterMode, parameterTypeIds);
				}

				parameterTypeIds.add(parameterTypeId);
			}

			return dbParTypeIdsMap;
		} catch (SQLException sqle) {
			String mesg = "Cannot retrieve parameter types -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				if (statement != null)
					statement.close();
				if (resultSet != null)
					resultSet.close();
				statement = null;
				resultSet = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	final void updateParameterTypeIds(final Set actionTypes) throws UpdateObjectException {
		if (actionTypes == null || actionTypes.isEmpty())
			return;

		Map<Identifier, Map<String, Set<Identifier>>> dbParameterTypeIdsMap = null;
		try {
			dbParameterTypeIdsMap = this.retrieveDBParameterTypeIdsMap(actionTypes);
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, Map<String, Set<Identifier>>> parameterTypeIdsMap = new HashMap<Identifier, Map<String, Set<Identifier>>>(actionTypes.size());
		for (Iterator it = actionTypes.iterator(); it.hasNext();) {
			final ActionType actionType = (ActionType) it.next();
			parameterTypeIdsMap.put(actionType.getId(), actionType.getParameterTypeIdsModeMap());
		}

		this.updateParameterTypeIds(parameterTypeIdsMap, dbParameterTypeIdsMap);
	}

	private void updateParameterTypeIds(final Map<Identifier, Map<String, Set<Identifier>>> parameterTypeIdsMap,
			final Map<Identifier, Map<String, Set<Identifier>>> dbParameterTypeIdsMap) throws UpdateObjectException {
		final Map<Identifier, Map<String, Set<Identifier>>> insertParameterTypeIdsMap = new HashMap<Identifier, Map<String, Set<Identifier>>>();
		final Map<Identifier, Set<Identifier>> deleteParameterTypeIdsMap = new HashMap<Identifier, Set<Identifier>>();
		for (final Identifier actionTypeId : parameterTypeIdsMap.keySet()) {
			final Map<String, Set<Identifier>> parameterTypeIdsModeMap = parameterTypeIdsMap.get(actionTypeId);
			final Map<String, Set<Identifier>> dbParameterTypeIdsModeMap = dbParameterTypeIdsMap.get(actionTypeId);
			if (dbParameterTypeIdsModeMap != null) {
				for (final String parameterMode : parameterTypeIdsModeMap.keySet()) {
					final Set<Identifier> parameterTypeIds = parameterTypeIdsModeMap.get(parameterMode);
					final Set<Identifier> dbParameterTypeIds = dbParameterTypeIdsModeMap.get(parameterMode);
					if (dbParameterTypeIds != null) {
						for (final Identifier parameterTypeId : parameterTypeIds) {
							if (!dbParameterTypeIds.contains(parameterTypeId)) {
								//Insert parameter type id
								Map<String, Set<Identifier>> altParameterTypeIdsModeMap = insertParameterTypeIdsMap.get(actionTypeId);
								if (altParameterTypeIdsModeMap == null) {
									altParameterTypeIdsModeMap = new HashMap<String, Set<Identifier>>();
									insertParameterTypeIdsMap.put(actionTypeId, altParameterTypeIdsModeMap);
								}
								Set<Identifier> altParameterTypeIds = altParameterTypeIdsModeMap.get(parameterMode);
								if (altParameterTypeIds == null) {
									altParameterTypeIds = new HashSet<Identifier>();
									altParameterTypeIdsModeMap.put(parameterMode, altParameterTypeIds);
								}
								altParameterTypeIds.add(parameterTypeId);
							}
						}	//for (final Identifier parameterTypeId : parameterTypeIds)
						for (final Identifier parameterTypeId : dbParameterTypeIds) {
							if (!parameterTypeIds.contains(parameterTypeId)) {
								//Delete if (!parameterTypeIds.contains(parameterTypeId)) {
								Set<Identifier> altParameterTypeIds = deleteParameterTypeIdsMap.get(actionTypeId);
								if (altParameterTypeIds == null) {
									altParameterTypeIds = new HashSet<Identifier>();
									deleteParameterTypeIdsMap.put(actionTypeId, altParameterTypeIds);
								}
								altParameterTypeIds.add(parameterTypeId);
							}
						}	//for (final Identifier parameterTypeId : dbParameterTypeIds)
					}	//if (dbParameterTypeIds != null)
					else {
						//Insert all par typ ids for this mode and this action type id
						Map<String, Set<Identifier>> altParameterTypeIdsModeMap = insertParameterTypeIdsMap.get(actionTypeId);
						if (altParameterTypeIdsModeMap == null) {
							altParameterTypeIdsModeMap = new HashMap<String, Set<Identifier>>();
							insertParameterTypeIdsMap.put(actionTypeId, altParameterTypeIdsModeMap);
						}
						altParameterTypeIdsModeMap.put(parameterMode, parameterTypeIds);
					}	//else if (dbParameterTypeIds != null)
				}
				for (final String parameterMode : dbParameterTypeIdsModeMap.keySet()) {
					if (!parameterTypeIdsModeMap.containsKey(parameterMode)) {
						//Delete all par typ ids for this mode and this action type id
						Set<Identifier> altParameterTypeIds = deleteParameterTypeIdsMap.get(actionTypeId);
						if (altParameterTypeIds == null) {
							altParameterTypeIds = new HashSet<Identifier>();
							deleteParameterTypeIdsMap.put(actionTypeId, altParameterTypeIds);
						}
						altParameterTypeIds.addAll(dbParameterTypeIdsModeMap.get(parameterMode));
					}
				}	//for (final String parameterMode : dbParameterTypeIdsModeMap.keySet())
			}	//if (dbParameterTypeIdsModeMap != null)
			else {
				//Insert all par type ids for this action type id
				final Map<String, Set<Identifier>> altParameterTypeIdsModeMap = new HashMap<String, Set<Identifier>>();
				altParameterTypeIdsModeMap.putAll(parameterTypeIdsModeMap);
				insertParameterTypeIdsMap.put(actionTypeId, altParameterTypeIdsModeMap);
			}
		}

		try {
			//NOTE: First delete, then insert. Remember about constraint mnttpartlnk_uniq
			super.deleteLinkedEntityIds(deleteParameterTypeIdsMap,
					this.getParameterTypeLinkTableName(),
					this.getActionTypeColumnName(),
					StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
			this.insertParameterTypes(insertParameterTypeIdsMap);
		} catch (CreateObjectException coe) {
			throw new UpdateObjectException(coe);
		}
	}

	private void insertParameterTypes(final Map<Identifier, Map<String, Set<Identifier>>> parameterTypeIdsMap)
			throws CreateObjectException {
		if (parameterTypeIdsMap == null || parameterTypeIdsMap.isEmpty())
			return;

		final String tableName = this.getParameterTypeLinkTableName();
		final String actionTypeColumnName = this.getActionTypeColumnName();
		final String sql = SQL_INSERT_INTO + tableName
				+ OPEN_BRACKET
				+ actionTypeColumnName + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		final Connection connection = DatabaseConnection.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);

			for (final Identifier actionTypeId : parameterTypeIdsMap.keySet()) {
				final Map<String, Set<Identifier>> parameterTypeIdsModeMap = parameterTypeIdsMap.get(actionTypeId);
				for (final String parameterMode : parameterTypeIdsModeMap.keySet()) {
					final Set<Identifier> insertParameterTypeIds = parameterTypeIdsModeMap.get(parameterMode);
					for (final Identifier parameterTypeId : insertParameterTypeIds) {
						DatabaseIdentifier.setIdentifier(preparedStatement, 1, actionTypeId);
						DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
						DatabaseString.setString(preparedStatement, 3, parameterMode, 3);
						Log.debugMessage(this.getEntityName() + "Database.insertParameterTypes | Inserting parameter type '"
								+ parameterTypeId + "' of mode '" + parameterMode + "' for action type '" + actionTypeId
								+ "'", Log.DEBUGLEVEL09);
						preparedStatement.executeUpdate();
					}
				}
			}
			connection.commit();
		} catch (SQLException sqle) {
			throw new CreateObjectException("Cannot insert parameter type ids -- " + sqle.getMessage(), sqle);
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}

	}
}
