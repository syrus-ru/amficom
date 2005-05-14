/*
 * $Id: ActionTypeDatabase.java,v 1.2 2005/05/14 09:43:14 arseniy Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/14 09:43:14 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
public abstract class ActionTypeDatabase extends StorableObjectDatabase {

	abstract String getParameterTypeLinkTableName();

	abstract String getActionTypeColumnName();

	void retrieveParameterTypesByOneQuery(java.util.Set actionTypes)
			throws RetrieveObjectException {

		String tableName = this.getParameterTypeLinkTableName();
		String actionTypeColumnName = this.getActionTypeColumnName();
    StringBuffer sql = new StringBuffer(SQL_SELECT
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE + COMMA
				+ actionTypeColumnName
				+ SQL_FROM + tableName
				+ SQL_WHERE);
    sql.append(idsEnumerationString(actionTypes, actionTypeColumnName, true));

    Map parameterTypeIdsMap = new HashMap();
    Map parameterTypeIdsModeMap;
    java.util.Set parameterTypeIds;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.retrieveParameterTypesByOneQuery | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final String parameterMode = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE));
				final Identifier parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet, StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
				final Identifier actionTypeId = DatabaseIdentifier.getIdentifier(resultSet, actionTypeColumnName);

				parameterTypeIdsModeMap = (Map) parameterTypeIdsMap.get(actionTypeId);
				if (parameterTypeIdsModeMap == null) {
					parameterTypeIdsModeMap = new HashMap();
					parameterTypeIdsMap.put(actionTypeId, parameterTypeIdsModeMap);
				}
				parameterTypeIds = (java.util.Set) parameterTypeIdsModeMap.get(parameterMode);
				if (parameterTypeIds == null) {
					parameterTypeIds = new HashSet();
					parameterTypeIdsModeMap.put(parameterMode, parameterTypeIds);
				}
				parameterTypeIds.add(parameterTypeId);
			}
		}
		catch (SQLException sqle) {
			String mesg = "Cannot retrieve parameter type ids";
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

		for (Iterator it = actionTypes.iterator(); it.hasNext();) {
			final ActionType actionType = (ActionType) it.next();
			final Identifier actionTypeId = actionType.getId();
			parameterTypeIdsModeMap = (Map) parameterTypeIdsMap.get(actionTypeId);

			actionType.setParameterTypeIds(parameterTypeIdsModeMap);
		}
	}

	void updateParameterTypes(java.util.Set actionTypes) throws UpdateObjectException {
		if (actionTypes == null || actionTypes.isEmpty())
			return;

		Map dbParameterTypeIdsMap = null;
		try {
			dbParameterTypeIdsMap = this.retrieveDBParameterTypeIdsMap(actionTypes);
		}
		catch (RetrieveObjectException roe) {
			throw new UpdateObjectException(roe);
		}

		Map parameterTypeIdsMap = new HashMap(actionTypes.size());
		for (Iterator it = actionTypes.iterator(); it.hasNext();) {
			final ActionType actionType = (ActionType) it.next();
			parameterTypeIdsMap.put(actionType.getId(), actionType.getParameterTypeIdsModeMap());
		}

		this.updateParameterTypes(parameterTypeIdsMap, dbParameterTypeIdsMap);
	}

	private void updateParameterTypes(Map parameterTypeIdsMap, Map dbParameterTypeIdsMap) throws UpdateObjectException {
		Map insertParameterTypeIdsMap = new HashMap();
		Map deleteParameterTypeIdsMap = new HashMap();
		Map altParameterTypeIdsModeMap;
		java.util.Set altParameterTypeIds;
		for (Iterator it1 = parameterTypeIdsMap.keySet().iterator(); it1.hasNext();) {
			final Identifier actionTypeId = (Identifier) it1.next();
			final Map parameterTypeIdsModeMap = (Map) parameterTypeIdsMap.get(actionTypeId);
			final Map dbParameterTypeIdsModeMap = (Map) dbParameterTypeIdsMap.get(actionTypeId);
			if (dbParameterTypeIdsModeMap != null) {
				for (Iterator it2 = parameterTypeIdsModeMap.keySet().iterator(); it2.hasNext();) {
					final String parameterMode = (String) it2.next();
					final java.util.Set parameterTypeIds = (java.util.Set) parameterTypeIdsModeMap.get(parameterMode);
					final java.util.Set dbParameterTypeIds = (java.util.Set) dbParameterTypeIdsModeMap.get(parameterMode);
					if (dbParameterTypeIds != null) {
						for (Iterator it3 = parameterTypeIds.iterator(); it3.hasNext();) {
							final Identifier parameterTypeId = (Identifier) it3.next();
							if (!dbParameterTypeIds.contains(parameterTypeId)) {
								//Insert parameter type id
								altParameterTypeIdsModeMap = (Map) insertParameterTypeIdsMap.get(actionTypeId);
								if (altParameterTypeIdsModeMap == null) {
									altParameterTypeIdsModeMap = new HashMap();
									insertParameterTypeIdsMap.put(actionTypeId, altParameterTypeIdsModeMap);
								}
								altParameterTypeIds = (java.util.Set) altParameterTypeIdsModeMap.get(parameterMode);
								if (altParameterTypeIds == null) {
									altParameterTypeIds = new HashSet();
									altParameterTypeIdsModeMap.put(parameterMode, altParameterTypeIds);
								}
								altParameterTypeIds.add(parameterTypeId);
							}
						}
						for (Iterator it3 = dbParameterTypeIds.iterator(); it3.hasNext();) {
							final Identifier parameterTypeId = (Identifier) it3.next();
							if (!parameterTypeIds.contains(parameterTypeId)) {
								//Delete parameter type id
								altParameterTypeIds = (java.util.Set) deleteParameterTypeIdsMap.get(actionTypeId);
								if (altParameterTypeIds == null) {
									altParameterTypeIds = new HashSet();
									deleteParameterTypeIdsMap.put(actionTypeId, altParameterTypeIds);
								}
								altParameterTypeIds.add(parameterTypeId);
							}
						}
					}
					else {
						//Insert all par typ ids for this mode and this action type id
						altParameterTypeIdsModeMap = (Map) insertParameterTypeIdsMap.get(actionTypeId);
						if (altParameterTypeIdsModeMap == null) {
							altParameterTypeIdsModeMap = new HashMap();
							insertParameterTypeIdsMap.put(actionTypeId, altParameterTypeIdsModeMap);
						}
						altParameterTypeIdsModeMap.put(parameterMode, parameterTypeIds);
					}
				}
				for (Iterator it2 = dbParameterTypeIdsModeMap.keySet().iterator(); it2.hasNext();) {
					final String parameterMode = (String) it2.next();
					if (!parameterTypeIdsModeMap.containsKey(parameterMode)) {
						//Delete all par typ ids for this mode and this action type id
						altParameterTypeIds = (java.util.Set) deleteParameterTypeIdsMap.get(actionTypeId);
						if (altParameterTypeIds == null) {
							altParameterTypeIds = new HashSet();
							deleteParameterTypeIdsMap.put(actionTypeId, altParameterTypeIds);
						}
						altParameterTypeIds.addAll((java.util.Set) dbParameterTypeIdsModeMap.get(parameterMode));
					}
				}
			}
			else {
				//Insert all par type ids for this action type id
				altParameterTypeIdsModeMap = new HashMap();
				altParameterTypeIdsModeMap.putAll(parameterTypeIdsModeMap);
				insertParameterTypeIdsMap.put(actionTypeId, altParameterTypeIdsModeMap);
			}
		}

		try {
			this.insertParameterTypes(insertParameterTypeIdsMap);
			super.deleteLinkedEntityIds(deleteParameterTypeIdsMap,
					this.getParameterTypeLinkTableName(),
					this.getActionTypeColumnName(),
					StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);
		}
		catch (CreateObjectException coe) {
			throw new UpdateObjectException(coe);
		}
	}

	private void insertParameterTypes(Map parameterTypeIdsMap)
			throws CreateObjectException {
		if (parameterTypeIdsMap == null || parameterTypeIdsMap.isEmpty())
			return;

		String tableName = this.getParameterTypeLinkTableName();
		String actionTypeColumnName = this.getActionTypeColumnName();
		String sql = SQL_INSERT_INTO + tableName
				+ OPEN_BRACKET
				+ actionTypeColumnName + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
				+ CLOSE_BRACKET + SQL_VALUES + OPEN_BRACKET
				+ QUESTION + COMMA
				+ QUESTION + COMMA
				+ QUESTION
				+ CLOSE_BRACKET;

		Connection connection = DatabaseConnection.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);

			for (Iterator it1 = parameterTypeIdsMap.keySet().iterator(); it1.hasNext();) {
				final Identifier actionTypeId = (Identifier) it1.next();
				final Map parameterTypeIdsModeMap = (Map) parameterTypeIdsMap.get(actionTypeId);
				for (Iterator it2 = parameterTypeIdsModeMap.keySet().iterator(); it2.hasNext();) {
					final String parameterMode = (String) it2.next();
					final java.util.Set insertParameterTypeIds = (java.util.Set) parameterTypeIdsModeMap.get(parameterMode);
					for (Iterator it3 = insertParameterTypeIds.iterator(); it3.hasNext();) {
						final Identifier parameterTypeId = (Identifier) it3.next();
						DatabaseIdentifier.setIdentifier(preparedStatement, 1, actionTypeId);
						DatabaseIdentifier.setIdentifier(preparedStatement, 2, parameterTypeId);
						DatabaseString.setString(preparedStatement, 3, parameterMode, 3);
						Log.debugMessage(this.getEnityName() + "Database.insertParameterTypes | Inserting parameter type '"
								+ parameterTypeId + "' of mode '" + parameterMode + "' for action type '" + actionTypeId
								+ "'", Log.DEBUGLEVEL09);
						preparedStatement.executeUpdate();
					}
				}
			}
			connection.commit();
		}
		catch (SQLException sqle) {
			throw new CreateObjectException("Cannot insert parameter type ids -- " + sqle.getMessage(), sqle);
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

	private Map retrieveDBParameterTypeIdsMap(java.util.Set actionTypes) throws RetrieveObjectException {
		String tableName = this.getParameterTypeLinkTableName();
		String actionTypeColumnName = this.getActionTypeColumnName();
		StringBuffer sql = new StringBuffer(SQL_SELECT
				+ actionTypeColumnName+ COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID + COMMA
				+ StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE
				+ SQL_FROM + tableName
				+ SQL_WHERE);
		sql.append(idsEnumerationString(actionTypes, actionTypeColumnName, true));

		Map dbParTypeIdsMap = new HashMap();	//Map <Identifier actionTypeId, Map dbParIdsModeMap>

		Map parTypeIdsModeMap;	//Map <String parameterMode, java.util.Set parameterTypeIds>
		java.util.Set parameterTypeIds;

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = DatabaseConnection.getConnection();
		try {
			statement = connection.createStatement();
			Log.debugMessage(this.getEnityName() + "Database.retrieveDBParameterTypeIdsMap | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());

			while (resultSet.next()) {
				final Identifier actionTypeId = DatabaseIdentifier.getIdentifier(resultSet, actionTypeColumnName);
				final String parameterMode = DatabaseString.fromQuerySubString(resultSet.getString(StorableObjectWrapper.LINK_COLUMN_PARAMETER_MODE));
				final Identifier parameterTypeId = DatabaseIdentifier.getIdentifier(resultSet,
						StorableObjectWrapper.LINK_COLUMN_PARAMETER_TYPE_ID);

				parTypeIdsModeMap = (Map) dbParTypeIdsMap.get(actionTypeId);
				if (parTypeIdsModeMap == null) {
					parTypeIdsModeMap = new HashMap();
					dbParTypeIdsMap.put(actionTypeId, parTypeIdsModeMap);
				}

				parameterTypeIds = (java.util.Set) parTypeIdsModeMap.get(parameterMode);
				if (parameterTypeIds == null) {
					parameterTypeIds = new HashSet();
					parTypeIdsModeMap.put(parameterMode, parameterTypeIds);
				}

				parameterTypeIds.add(parameterTypeId);
			}

			return dbParTypeIdsMap;
		}
		catch (SQLException sqle) {
			String mesg = "Cannot retrieve parameter types -- " + sqle.getMessage();
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

}
