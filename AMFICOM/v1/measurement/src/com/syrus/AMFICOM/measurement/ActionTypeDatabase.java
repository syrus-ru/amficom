/*
 * $Id: ActionTypeDatabase.java,v 1.15 2005/08/19 15:51:01 arseniy Exp $
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseIdentifier;
import com.syrus.AMFICOM.general.DatabaseStorableObjectCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.15 $, $Date: 2005/08/19 15:51:01 $
 * @author $Author: arseniy $
 * @module measurement
 */
public abstract class ActionTypeDatabase<T extends ActionType> extends StorableObjectDatabase<T> {

	abstract String getParameterTypeLinkTableName();

	abstract String getActionTypeColumnName();

	final void retrieveParameterTypesByOneQuery(final Set<T> actionTypes) throws RetrieveObjectException {
		if (actionTypes == null || actionTypes.isEmpty()) {
			return;
		}

		final Map<Identifier, Map<String, Set<ParameterType>>> dbParameterTypesMap = this.retrieveDBParameterTypesMap(actionTypes);

		for (final ActionType actionType : actionTypes) {
			final Identifier actionTypeId = actionType.getId();
			Map<String, Set<ParameterType>> parameterTypesModeMap = dbParameterTypesMap.get(actionTypeId);
			if (parameterTypesModeMap == null) {
				parameterTypesModeMap = Collections.emptyMap();
			}

			actionType.setParameterTypes(parameterTypesModeMap);
		}
	}

	private Map<Identifier, Map<String, Set<ParameterType>>> retrieveDBParameterTypesMap(final Set<T> actionTypes)
			throws RetrieveObjectException {
		final String tableName = this.getParameterTypeLinkTableName();
		final String actionTypeColumnName = this.getActionTypeColumnName();
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ actionTypeColumnName+ COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_CODE + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(idsEnumerationString(actionTypes, actionTypeColumnName, true));

		final Map<Identifier, Map<String, Set<ParameterType>>> dbParTypesMap = new HashMap<Identifier, Map<String, Set<ParameterType>>>();

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage(this.getEntityName() + "Database.retrieveDBParameterTypesMap | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier actionTypeId = DatabaseIdentifier.getIdentifier(resultSet, actionTypeColumnName);
				final ParameterType parameterType = ParameterType.fromInt(resultSet.getInt(StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_CODE));
				final String parameterMode = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE));

				Map<String, Set<ParameterType>> parTypesModeMap = dbParTypesMap.get(actionTypeId);
				if (parTypesModeMap == null) {
					parTypesModeMap = new HashMap<String, Set<ParameterType>>();
					dbParTypesMap.put(actionTypeId, parTypesModeMap);
				}

				Set<ParameterType> parameterTypes = parTypesModeMap.get(parameterMode);
				if (parameterTypes == null) {
					parameterTypes = new HashSet<ParameterType>();
					parTypesModeMap.put(parameterMode, parameterTypes);
				}

				parameterTypes.add(parameterType);
			}

			return dbParTypesMap;
		} catch (SQLException sqle) {
			String mesg = "Cannot retrieve parameter types -- " + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (statement != null) {
						statement.close();
						statement = null;
					}
				} finally {
					try {
						if (resultSet != null) {
							resultSet.close();
							resultSet = null;
						}
					} finally {
						if (connection != null) {
							DatabaseConnection.releaseConnection(connection);
							connection = null;
						}
					}
				}
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	final void updateParameterTypes(final Set<T> actionTypes) throws UpdateObjectException {
		if (actionTypes == null || actionTypes.isEmpty()) {
			return;
		}

		Map<Identifier, Map<String, Set<ParameterType>>> dbParameterTypesMap = null;
		try {
			dbParameterTypesMap = this.retrieveDBParameterTypesMap(actionTypes);
		} catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		final Map<Identifier, Map<String, Set<ParameterType>>> parameterTypesMap = new HashMap<Identifier, Map<String, Set<ParameterType>>>(actionTypes.size());
		for (final ActionType actionType : actionTypes) {
			parameterTypesMap.put(actionType.getId(), actionType.getParameterTypesModeMap());
		}

		this.updateParameterTypes(parameterTypesMap, dbParameterTypesMap);
	}

	private void updateParameterTypes(final Map<Identifier, Map<String, Set<ParameterType>>> parameterTypesMap,
			final Map<Identifier, Map<String, Set<ParameterType>>> dbParameterTypesMap) throws UpdateObjectException {
		final Map<Identifier, Map<String, Set<ParameterType>>> insertParameterTypesMap = new HashMap<Identifier, Map<String, Set<ParameterType>>>();
		final Map<Identifier, Set<ParameterType>> deleteParameterTypesMap = new HashMap<Identifier, Set<ParameterType>>();
		for (final Identifier actionTypeId : parameterTypesMap.keySet()) {
			final Map<String, Set<ParameterType>> parameterTypesModeMap = parameterTypesMap.get(actionTypeId);
			final Map<String, Set<ParameterType>> dbParameterTypesModeMap = dbParameterTypesMap.get(actionTypeId);
			if (dbParameterTypesModeMap != null) {
				for (final String parameterMode : parameterTypesModeMap.keySet()) {
					final Set<ParameterType> parameterTypes = parameterTypesModeMap.get(parameterMode);
					final Set<ParameterType> dbParameterTypes = dbParameterTypesModeMap.get(parameterMode);
					if (dbParameterTypes != null) {
						for (final ParameterType parameterType : parameterTypes) {
							if (!dbParameterTypes.contains(parameterType)) {
								//Insert parameter type
								Map<String, Set<ParameterType>> altParameterTypesModeMap = insertParameterTypesMap.get(actionTypeId);
								if (altParameterTypesModeMap == null) {
									altParameterTypesModeMap = new HashMap<String, Set<ParameterType>>();
									insertParameterTypesMap.put(actionTypeId, altParameterTypesModeMap);
								}
								Set<ParameterType> altParameterTypes = altParameterTypesModeMap.get(parameterMode);
								if (altParameterTypes == null) {
									altParameterTypes = new HashSet<ParameterType>();
									altParameterTypesModeMap.put(parameterMode, altParameterTypes);
								}
								altParameterTypes.add(parameterType);
							}
						}	//for (final Identifier parameterType : parameterTypes)
						for (final ParameterType parameterType : dbParameterTypes) {
							if (!parameterTypes.contains(parameterType)) {
								//Delete if (!parameterTypes.contains(parameterType)) {
								Set<ParameterType> altParameterTypes = deleteParameterTypesMap.get(actionTypeId);
								if (altParameterTypes == null) {
									altParameterTypes = new HashSet<ParameterType>();
									deleteParameterTypesMap.put(actionTypeId, altParameterTypes);
								}
								altParameterTypes.add(parameterType);
							}
						}	//for (final Identifier parameterType : dbParameterTypes)
					}	//if (dbParameterTypes != null)
					else {
						//Insert all par types for this mode and this action type id
						Map<String, Set<ParameterType>> altParameterTypesModeMap = insertParameterTypesMap.get(actionTypeId);
						if (altParameterTypesModeMap == null) {
							altParameterTypesModeMap = new HashMap<String, Set<ParameterType>>();
							insertParameterTypesMap.put(actionTypeId, altParameterTypesModeMap);
						}
						altParameterTypesModeMap.put(parameterMode, parameterTypes);
					}	//else if (dbParameterTypes != null)
				}
				for (final String parameterMode : dbParameterTypesModeMap.keySet()) {
					if (!parameterTypesModeMap.containsKey(parameterMode)) {
						//Delete all par types for this mode and this action type id
						Set<ParameterType> altParameterTypes = deleteParameterTypesMap.get(actionTypeId);
						if (altParameterTypes == null) {
							altParameterTypes = new HashSet<ParameterType>();
							deleteParameterTypesMap.put(actionTypeId, altParameterTypes);
						}
						altParameterTypes.addAll(dbParameterTypesModeMap.get(parameterMode));
					}
				}	//for (final String parameterMode : dbParameterTypesModeMap.keySet())
			}	//if (dbParameterTypesModeMap != null)
			else {
				//Insert all par types for this action type id
				final Map<String, Set<ParameterType>> altParameterTypesModeMap = new HashMap<String, Set<ParameterType>>();
				altParameterTypesModeMap.putAll(parameterTypesModeMap);
				insertParameterTypesMap.put(actionTypeId, altParameterTypesModeMap);
			}
		}

		try {
			//NOTE: First delete, then insert. Remember about constraint mnttpartlnk_uniq
			this.deleteParameterTypes(deleteParameterTypesMap);
			this.insertParameterTypes(insertParameterTypesMap);
		} catch (CreateObjectException coe) {
			throw new UpdateObjectException(coe);
		}
	}

	private void deleteParameterTypes(final Map<Identifier, Set<ParameterType>> parameterTypesMap) {
		if (parameterTypesMap == null || parameterTypesMap.isEmpty()) {
			return;
		}

		final String tableName = this.getParameterTypeLinkTableName();
		final String actionTypeColumnName = this.getActionTypeColumnName();
		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + tableName
				+ SQL_WHERE + DatabaseStorableObjectCondition.FALSE_CONDITION);

		for (final Identifier actionTypeId : parameterTypesMap.keySet()) {
			final Set<ParameterType> deleteParameterTypes = parameterTypesMap.get(actionTypeId);
			sql.append(SQL_OR);
			sql.append(OPEN_BRACKET);
			sql.append(actionTypeColumnName + EQUALS + DatabaseIdentifier.toSQLString(actionTypeId)
					+ SQL_AND + StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_CODE + SQL_IN + OPEN_BRACKET);
			for (final Iterator<ParameterType> it = deleteParameterTypes.iterator(); it.hasNext();) {
				final ParameterType parameterType = it.next();
				sql.append(parameterType.getCode());
				if (it.hasNext()) {
					sql.append(COMMA);
				}
			}
			sql.append(CLOSE_BRACKET);
			sql.append(CLOSE_BRACKET);
		}

		Connection connection = null;
		Statement statement = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("ActionTypeDatabase.deleteParameterTypes | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
		} catch (SQLException sqle) {
			Log.errorException(sqle);
		} finally {
			try {
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
			} catch (SQLException sqle1) {
				Log.errorException(sqle1);
			}
		}
	}

	private void insertParameterTypes(final Map<Identifier, Map<String, Set<ParameterType>>> parameterTypesMap)
			throws CreateObjectException {
		if (parameterTypesMap == null || parameterTypesMap.isEmpty()) {
			return;
		}

		final String tableName = this.getParameterTypeLinkTableName();
		final String actionTypeColumnName = this.getActionTypeColumnName();
		final String sql = SQL_INSERT_INTO + tableName
				+ OPEN_BRACKET
				+ actionTypeColumnName + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_CODE + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql);

			for (final Identifier actionTypeId : parameterTypesMap.keySet()) {
				final Map<String, Set<ParameterType>> parameterTypesModeMap = parameterTypesMap.get(actionTypeId);
				for (final String parameterMode : parameterTypesModeMap.keySet()) {
					final Set<ParameterType> insertParameterTypes = parameterTypesModeMap.get(parameterMode);
					for (final ParameterType parameterType : insertParameterTypes) {
						DatabaseIdentifier.setIdentifier(preparedStatement, 1, actionTypeId);
						preparedStatement.setInt(2, parameterType.getCode());
						DatabaseString.setString(preparedStatement, 3, parameterMode, 3);
						Log.debugMessage(this.getEntityName() + "Database.insertParameterTypes | Inserting parameter type '"
								+ parameterType + "' of mode '" + parameterMode + "' for action type '" + actionTypeId
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
				Log.errorException(sqle1);
			}
		}

	}
}
