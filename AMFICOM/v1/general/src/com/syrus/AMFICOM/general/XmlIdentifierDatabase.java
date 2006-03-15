/*-
 * $Id: XmlIdentifierDatabase.java,v 1.19 2006/03/15 15:17:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.MAXIMUM_EXPRESSION_NUMBER;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.QUESTION;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_AND;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_NOT_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.util.Log.DEBUGLEVEL10;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.syrus.AMFICOM.general.LocalXmlIdentifierPool.Key;
import com.syrus.AMFICOM.general.LocalXmlIdentifierPool.XmlKey;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @author max
 * @author $Author: arseniy $
 * @version $Revision: 1.19 $, $Date: 2006/03/15 15:17:43 $
 * @module general
 */
final class XmlIdentifierDatabase {
	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";

	private static final String DB_HOST_NAME = "aldan";
	private static final String DB_SID = "hodja";
	private static final String DB_LOGIN_NAME = "amficom";
	private static final int DB_CONNECTION_TIMEOUT = 120;	//sec

	private static final String TABLE_NAME_XML_ID_MAP = "ImportUIDMap";
	private static final String COLUMN_IMPORT_TYPE = "import_kind";
	private static final String COLUMN_XML_ID = "foreign_uid";
	private static final String COLUMN_ID = "id";

	private static String columns;
	private static String updateMultipleSQLValues;

	private XmlIdentifierDatabase() {
		assert false;
	}

	static {
		establishDatabaseConnection();
		Runtime.getRuntime().addShutdownHook(new Thread("XmlIdentifierDatabase -- shutdown hook") {
			@Override
			public void run() {
				shutdown();
			}
		});
	}

	private static void establishDatabaseConnection() {
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, DB_HOST_NAME);
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		} catch (final Exception e) {
			Log.errorMessage(e);
			System.exit(0);
		}
	}

	static void shutdown() {
		DatabaseConnection.closeConnection();
	}
	
	private static String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_ID + COMMA
					+ COLUMN_XML_ID + COMMA
					+ COLUMN_IMPORT_TYPE;
		}
		return columns;
	}
	
	private static String getMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	static void retrievePrefetchedMap(final String importType) throws RetrieveObjectException {
		if (importType == null || importType.length() == 0) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(StorableObjectDatabase.SQL_SELECT);
		sql.append(getColumnsTmpl());
		sql.append(SQL_FROM);
		sql.append(TABLE_NAME_XML_ID_MAP);
		sql.append(SQL_WHERE);
		sql.append(COLUMN_IMPORT_TYPE);
		sql.append(EQUALS);
		sql.append(APOSTROPHE);
		sql.append(importType);
		sql.append(APOSTROPHE);
		
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement(
	                ResultSet.TYPE_FORWARD_ONLY, 
	                ResultSet.CONCUR_UPDATABLE);
			Log.debugMessage("Trying: " + sql, DEBUGLEVEL10);
			resultSet = statement.executeQuery(sql.toString());
			
			while (resultSet.next()) {
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				boolean put = false;
				/*
				 * The check is turned of since it's a lengthy
				 * operation if you have 12k objects. We really
				 * must add hooks to save/delete operations of
				 * CORBA object loader.
				 */
//				try {
//					put = StorableObjectDatabase.isObjectPresentInDatabase(id);
//				} catch (ApplicationException ae) {
//					Log.errorMessage(ae);
//				}
				put = true;
				if (put) {
					LocalXmlIdentifierPool.put(id, resultSet.getString(COLUMN_XML_ID), importType, LocalXmlIdentifierPool.KeyState.UP_TO_DATE);
				} else {
					resultSet.deleteRow();
				}
			}
		} catch (final SQLException sqle) {
			final String mesg = "Cannot retrieve ImportUIDItem" + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		} finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
						}
					} finally {
						DatabaseConnection.releaseConnection(connection);
					}
				}
			} catch (final SQLException sqle) {
				Log.errorMessage(sqle);
			}
		}
	}

	static void removeKeys(final Set<Key> keysToDelete) {
		if (keysToDelete.isEmpty()) {
			return;
		}

		final Map<String, Set<Identifier>> sortedIds = new HashMap<String, Set<Identifier>>();
		for (final Key key : keysToDelete) {
			final Identifier id = key.getId();
			if (id.isVoid()) {
				continue;
			}
			final String importType = key.getImportType();
			Set<Identifier> ids = sortedIds.get(importType);
			if (ids == null) {
				ids = new HashSet<Identifier>();
				sortedIds.put(importType, ids);
			}
			ids.add(id);
		}

		final StringBuilder sql = new StringBuilder();
		sql.append(StorableObjectDatabase.SQL_DELETE_FROM);
		sql.append(TABLE_NAME_XML_ID_MAP);
		sql.append(SQL_WHERE);

		final Set<Entry<String, Set<Identifier>>> entrySet = sortedIds.entrySet();
		final int entrySetSize = entrySet.size();
		int i = 0;
		for (final Entry<String, Set<Identifier>> entry : entrySet) {
			if (entrySetSize != 1) {
				sql.append(OPEN_BRACKET);
			}
			sql.append(COLUMN_IMPORT_TYPE);
			sql.append(EQUALS);
			sql.append(APOSTROPHE);
			sql.append(DatabaseString.toQuerySubString(entry.getKey()));
			sql.append(APOSTROPHE);
			sql.append(SQL_AND);
			sql.append(StorableObjectDatabase.idsEnumerationString(entry.getValue(), COLUMN_ID, true));
			if (entrySetSize != 1) {
				sql.append(CLOSE_BRACKET);
			}
			if (++i < entrySetSize) {
				sql.append(SQL_OR);
			}
		}
		executeQuery(sql.toString());
	}
	
	static void removeXmlKeys(final Set<XmlKey> xmlKeysToDelete) {
		if (xmlKeysToDelete.isEmpty()) {
			return;
		}

		final Map<String, Set<String>> sortedXmlIds = new HashMap<String, Set<String>>();
		for (final XmlKey xmlKey : xmlKeysToDelete) {
			final String importType = xmlKey.getImportType();
			Set<String> xmlIds = sortedXmlIds.get(importType);
			if (xmlIds == null) {
				xmlIds = new HashSet<String>();
				sortedXmlIds.put(importType, xmlIds);
			}
			xmlIds.add(xmlKey.getXmlId());
		}

		final StringBuilder sql = new StringBuilder();
		sql.append(StorableObjectDatabase.SQL_DELETE_FROM);
		sql.append(TABLE_NAME_XML_ID_MAP);
		sql.append(SQL_WHERE);

		final Set<Entry<String, Set<String>>> entrySet = sortedXmlIds.entrySet();
		final int entrySetSize = entrySet.size();
		int i = 0;
		for (final Entry<String, Set<String>> entry : entrySet) {
			if (entrySetSize != 1) {
				sql.append(OPEN_BRACKET);
			}
			sql.append(COLUMN_IMPORT_TYPE);
			sql.append(EQUALS);
			sql.append(APOSTROPHE);
			sql.append(DatabaseString.toQuerySubString(entry.getKey()));
			sql.append(APOSTROPHE);
			sql.append(SQL_AND);
			sql.append(xmlIdsEnumerationString(entry.getValue(), COLUMN_XML_ID, true));
			if (entrySetSize != 1) {
				sql.append(CLOSE_BRACKET);
			}
			if (++i < entrySetSize) {
				sql.append(SQL_OR);
			}
		}
		executeQuery(sql.toString());
	}
	
	private static void executeQuery(final String query) {
		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("XmlIdentifierDatabase.delete(List) | Trying: " + query, DEBUGLEVEL10);
			statement.executeUpdate(query.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorMessage(sqle1);
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
				Log.errorMessage(sqle1);
			}
		}
	}

	static void insertKeys(Map<Key, String> keysToCreate) throws CreateObjectException {
		if(keysToCreate == null || keysToCreate.isEmpty()) {
			return;
		}
		final StringBuilder sql = new StringBuilder();
		sql.append(SQL_INSERT_INTO);
		sql.append(TABLE_NAME_XML_ID_MAP);
		sql.append(OPEN_BRACKET);
		sql.append(getColumnsTmpl());
		sql.append(CLOSE_BRACKET);
		sql.append(SQL_VALUES);
		sql.append(OPEN_BRACKET);
		sql.append(getMultipleSQLValuesTmpl());
		sql.append(CLOSE_BRACKET);
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			preparedStatement = connection.prepareStatement(sql.toString());
			Log.debugMessage("Trying: " + sql, DEBUGLEVEL10);
			for (final Key key : keysToCreate.keySet()) {
				Identifier id = key.getId();
				String xmlId = keysToCreate.get(key);
				String importKind = key.getImportType();
				int i = 1;
				DatabaseIdentifier.setIdentifier(preparedStatement, i++, id);
				preparedStatement.setString(i++, xmlId);
				preparedStatement.setString(i++, importKind);
				preparedStatement.executeUpdate();
			}
			connection.commit();
		} catch (SQLException sqle) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException sqle1) {
				Log.errorMessage(sqle1);
			}
			final String mesg = "Cannot insert " + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
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
				Log.errorMessage(sqle1);
			}
		}
	}

	/**
	 * @param xmlIds
	 * @param idColumn
	 * @param inList
	 * @see StorableObjectDatabase#idsEnumerationString(Set, String, boolean)
	 */
	private static StringBuilder xmlIdsEnumerationString(
			final Set<String> xmlIds,
			final String idColumn,
			final boolean inList) {
		assert xmlIds != null : NON_NULL_EXPECTED;

		if (xmlIds.isEmpty()) {
			return new StringBuilder(inList
					? DatabaseStorableObjectCondition.FALSE_CONDITION
					: DatabaseStorableObjectCondition.TRUE_CONDITION);
		}

		final StringBuilder sql = new StringBuilder();
		sql.append(OPEN_BRACKET);
		sql.append(idColumn);
		sql.append(inList ? SQL_IN : SQL_NOT_IN);
		sql.append(OPEN_BRACKET);

		int i = 0;
		final int xmlIdsSize = xmlIds.size();
		for (final String xmlId : xmlIds) {
			sql.append(APOSTROPHE);
			sql.append(DatabaseString.toQuerySubString(xmlId));
			sql.append(APOSTROPHE);
			if (++i < xmlIdsSize) {
				if (i % MAXIMUM_EXPRESSION_NUMBER == 0) {
					sql.append(CLOSE_BRACKET);
					sql.append(inList ? SQL_OR : SQL_AND);
					sql.append(idColumn);
					sql.append(inList ? SQL_IN : SQL_NOT_IN);
					sql.append(OPEN_BRACKET);
				} else {
					sql.append(COMMA);
				}
			}
		}
		sql.append(CLOSE_BRACKET);
		sql.append(CLOSE_BRACKET);

		return sql;
	}
}
