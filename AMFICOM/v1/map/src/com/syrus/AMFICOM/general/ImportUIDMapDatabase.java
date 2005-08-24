/*-
 * $Id: ImportUIDMapDatabase.java,v 1.5 2005/08/24 15:00:28 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @version $Revision: 1.5 $
 * @author $Author: bass $
 * @author krupenn
 * @module map
 */

public final class ImportUIDMapDatabase {
	private static final String TABLE_NAME_IMPORT_UID_MAP = "ImportUIDMap";
	private static final String COLUMN_IMPORT_KIND = "import_kind";
	private static final String COLUMN_FOREIGN_UID = "foreign_uid";
	private static final String COLUMN_ID = "id";

	private static StringBuffer getWhereClause(final String importType, final String uid) {
		return new StringBuffer(COLUMN_IMPORT_KIND + StorableObjectDatabase.EQUALS
				+ StorableObjectDatabase.APOSTROPHE + importType + StorableObjectDatabase.APOSTROPHE
				+ StorableObjectDatabase.SQL_AND
				+ COLUMN_FOREIGN_UID + StorableObjectDatabase.EQUALS
				+ StorableObjectDatabase.APOSTROPHE + uid + StorableObjectDatabase.APOSTROPHE);
	}

	private static StringBuffer retrieveQuery(final StringBuffer condition) {
		final StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_SELECT
				+ COLUMN_IMPORT_KIND + StorableObjectDatabase.COMMA
				+ COLUMN_FOREIGN_UID + StorableObjectDatabase.COMMA
				+ COLUMN_ID
				+ StorableObjectDatabase.SQL_FROM + TABLE_NAME_IMPORT_UID_MAP);

		if (condition != null && condition.length() != 0) {
			sql.append(StorableObjectDatabase.SQL_WHERE);
			sql.append(condition);
		}

		return sql;
	}

	public static Identifier retrieve(final String import_type, final String uid) throws RetrieveObjectException {
		final Set<Identifier> objects = retrieveByCondition(getWhereClause(import_type, uid));
		if (!objects.isEmpty()) {
			return objects.iterator().next();
		}
		return null;
	}

	private static Set<Identifier> retrieveByCondition(final StringBuffer condition) throws RetrieveObjectException {
		final Set<Identifier> objects = new HashSet<Identifier>();

		final StringBuffer sql = retrieveQuery(condition);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("ImportUIDMapDatabase.retrieveByCondition | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				objects.add(id);
			}
		}
		catch (SQLException sqle) {
			final String mesg = "Cannot retrieve ImportUIDItem" + sqle.getMessage();
			throw new RetrieveObjectException(mesg, sqle);
		}
		finally {
			try {
				try {
					if (resultSet != null) {
						resultSet.close();
					}
				}
				finally {
					try {
						if (statement != null) {
							statement.close();
						}
					}
					finally {
						DatabaseConnection.releaseConnection(connection);
					}
				}
			}
			catch (SQLException sqle) {
				Log.errorException(sqle);
			}
		}

		return objects;
	}

	public static void insert(final String import_type, String uid, Identifier id) throws CreateObjectException {
		final StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_INSERT_INTO + TABLE_NAME_IMPORT_UID_MAP
				+ StorableObjectDatabase.OPEN_BRACKET
				+ COLUMN_IMPORT_KIND + StorableObjectDatabase.COMMA
				+ COLUMN_FOREIGN_UID + StorableObjectDatabase.COMMA
				+ COLUMN_ID + StorableObjectDatabase.CLOSE_BRACKET 
				+ StorableObjectDatabase.SQL_VALUES + StorableObjectDatabase.OPEN_BRACKET
				+ StorableObjectDatabase.APOSTROPHE + DatabaseString.toQuerySubString(import_type) + StorableObjectDatabase.APOSTROPHE + StorableObjectDatabase.COMMA
				+ StorableObjectDatabase.APOSTROPHE + DatabaseString.toQuerySubString(uid) + StorableObjectDatabase.APOSTROPHE + StorableObjectDatabase.COMMA
				+ DatabaseIdentifier.toSQLString(id)
				+ StorableObjectDatabase.CLOSE_BRACKET);

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("ImportUIDMapDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			final String mesg = "Cannot insert ImportUIDItem" + sqle.getMessage();
			throw new CreateObjectException(mesg, sqle);
		}
		finally {
			try {
				if (statement != null) {
					statement.close();
				}
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

	public static void delete(final String import_type, final String uid) {
		final StringBuffer sql = new StringBuffer(StorableObjectDatabase.SQL_DELETE_FROM + TABLE_NAME_IMPORT_UID_MAP
				+ StorableObjectDatabase.SQL_WHERE + getWhereClause(import_type, uid));

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("ImportUIDMapDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		}
		catch (SQLException sqle) {
			Log.errorException(sqle);
		}
		finally {
			try {
				if (statement != null) {
					statement.close();
				}
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

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	private static final String KEY_DB_HOST_NAME = "DBHostName";
	private static final String KEY_DB_SID = "DBSID";
	private static final String KEY_DB_CONNECTION_TIMEOUT = "DBConnectionTimeout";
	private static final String KEY_DB_LOGIN_NAME = "DBLoginName";
	private static final String KEY_SERVER_ID = "ServerID";

	/*-********************************************************************
	 * Default values.                                                    *
	 **********************************************************************/

	public static final String DB_SID = "amficom";
	/**
	 * Database connection timeout, in seconds.
	 */
	public static final int DB_CONNECTION_TIMEOUT = 120;	//sec
	public static final String DB_LOGIN_NAME = "amficom";
	public static final String SERVER_ID = "Server_1";

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
		final String dbHostName = ApplicationProperties.getString(KEY_DB_HOST_NAME, Application.getInternetAddress());
		final String dbSid = ApplicationProperties.getString(KEY_DB_SID, DB_SID);
		final long dbConnTimeout = ApplicationProperties.getInt(KEY_DB_CONNECTION_TIMEOUT, DB_CONNECTION_TIMEOUT)*1000;
		final String dbLoginName = ApplicationProperties.getString(KEY_DB_LOGIN_NAME, DB_LOGIN_NAME);
		try {
			DatabaseConnection.establishConnection(dbHostName, dbSid, dbConnTimeout, dbLoginName);
		}
		catch (Exception e) {
			Log.errorException(e);
			System.exit(0);
		}
	}

	protected static void shutdown() {
		DatabaseConnection.closeConnection();
	}
}
