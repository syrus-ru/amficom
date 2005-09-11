/*-
 * $Id: XmlIdentifierDatabase.java,v 1.3 2005/09/11 16:32:13 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.idsEnumerationString;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.MAXIMUM_EXPRESSION_NUMBER;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.QUESTION;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.LocalXmlIdentifierPool.Key;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @author max
 * @author $Author: max $
 * @version $Revision: 1.3 $, $Date: 2005/09/11 16:32:13 $
 * @module general
 */

final class XmlIdentifierDatabase {
	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";

	public static final String DB_HOST_NAME = "aldan";
	public static final String DB_SID = "hodja";
	public static final String DB_LOGIN_NAME = "amficom";
	public static final String SERVER_ID = "Server_1";
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec

	private static final String TABLE_NAME_IMPORT_UID_MAP = "ImportUIDMap";
	private static final String COLUMN_IMPORT_KIND = "import_kind";
	private static final String COLUMN_FOREIGN_UID = "foreign_uid";
	private static final String COLUMN_ID = "id";
	
	private static String columns;
	private static String updateMultipleSQLValues;
		
	private XmlIdentifierDatabase() {
		assert false;
	}
	
	static {
		establishDatabaseConnection();
		Runtime.getRuntime().addShutdownHook(new Thread() {
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
			Log.errorException(e);
			System.exit(0);
		}
	}

	protected static void shutdown() {
		DatabaseConnection.closeConnection();
	}
	
	protected static String getColumnsTmpl() {
		if (columns == null) {
			columns = COLUMN_ID + COMMA
					+ COLUMN_FOREIGN_UID + COMMA
					+ COLUMN_IMPORT_KIND;
		}
		return columns;
	}
	
	protected static String getMultipleSQLValuesTmpl() {
		if (updateMultipleSQLValues == null) {
			updateMultipleSQLValues = QUESTION + COMMA
					+ QUESTION + COMMA
					+ QUESTION;
		}
		return updateMultipleSQLValues;
	}

	public static Map<Identifier, XmlIdentifier> retrievePrefetchedMap(final String importType) throws RetrieveObjectException {
		Map<Identifier, XmlIdentifier> idXmlIdMap = new HashMap<Identifier, XmlIdentifier>(); 
		if (importType == null || importType.equals("")) {
			return idXmlIdMap;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(StorableObjectDatabase.SQL_SELECT);
		sql.append(getColumnsTmpl());
		sql.append(SQL_FROM);
		sql.append(TABLE_NAME_IMPORT_UID_MAP);
		sql.append(SQL_WHERE);
		sql.append(COLUMN_IMPORT_KIND);
		sql.append(EQUALS);
		sql.append(APOSTROPHE);
		sql.append(importType);
		sql.append(APOSTROPHE);
		
		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("XmlIdentifierDatabase.retrieveByCondition | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				if (StorableObjectDatabase.isPresentInDatabase(id)) {
					final XmlIdentifier xmlId = XmlIdentifier.Factory.newInstance();
					xmlId.setStringValue(resultSet.getString(COLUMN_FOREIGN_UID));
					idXmlIdMap.put(id, xmlId);
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
				Log.errorException(sqle);
			}
		}
		return idXmlIdMap;
		
	}

	public static void removeIds(final Set<Identifier> idsToDelete) {
		if(idsToDelete == null || idsToDelete.isEmpty()) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(StorableObjectDatabase.SQL_DELETE_FROM);
		sql.append(TABLE_NAME_IMPORT_UID_MAP);
		sql.append(SQL_WHERE);
		sql.append(idsEnumerationString(idsToDelete, COLUMN_ID, true));
		
		executeQuery(sql.toString());
		
	}
	
	public static void removeXmlIds(final Set<XmlIdentifier> xmlIdsToDelete) {
		if(xmlIdsToDelete == null || xmlIdsToDelete.isEmpty()) {
			return;
		}
		StringBuilder sql = new StringBuilder();
		sql.append(StorableObjectDatabase.SQL_DELETE_FROM);
		sql.append(TABLE_NAME_IMPORT_UID_MAP);
		sql.append(SQL_WHERE);
		sql.append(COLUMN_FOREIGN_UID);
		sql.append(SQL_IN);
		sql.append(OPEN_BRACKET);
		int i = 0;
		for (final Iterator<XmlIdentifier> it = xmlIdsToDelete.iterator(); it.hasNext(); i++) {
			final XmlIdentifier xmlId= it.next();
			sql.append(APOSTROPHE);
			sql.append(xmlId.getStringValue());
			sql.append(APOSTROPHE);
			if (it.hasNext()) {
				if (((i + 1) % MAXIMUM_EXPRESSION_NUMBER != 0)) {
					sql.append(COMMA);
				}
				else {
					sql.append(CLOSE_BRACKET);
					sql.append(SQL_OR);
					sql.append(COLUMN_FOREIGN_UID);
					sql.append(SQL_IN);
					sql.append(OPEN_BRACKET);
				}
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
			Log.debugMessage("XmlIdentifierDatabase.delete(List) | Trying: " + query, Log.DEBUGLEVEL09);
			statement.executeUpdate(query.toString());
			connection.commit();
		} catch (SQLException sqle1) {
			Log.errorException(sqle1);
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

	public static void insertKeys(Map<Key, XmlIdentifier> keysToCreate) throws CreateObjectException {
		if(keysToCreate == null || keysToCreate.isEmpty()) {
			return;
		}
		final StringBuilder sql = new StringBuilder();
		sql.append(SQL_INSERT_INTO);
		sql.append(TABLE_NAME_IMPORT_UID_MAP);
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
			Log.debugMessage("XmlIdentifierDatabase.insertEntities | Trying: " + sql, Log.DEBUGLEVEL09);
			for (final Key key : keysToCreate.keySet()) {
				Identifier id = key.getId();
				XmlIdentifier xmlId = keysToCreate.get(key);
				String importKind = key.getImportType();
				int i = 1;
				DatabaseIdentifier.setIdentifier(preparedStatement, i++, id);
				preparedStatement.setString(i++, xmlId.getStringValue());
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
				Log.errorException(sqle1);
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
				Log.errorException(sqle1);
			}
		}
	}
}
