/*-
 * $Id: ImportUidMapDatabase.java,v 1.2 2005/08/31 13:24:21 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.COMMA;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_AND;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_DELETE_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_INSERT_INTO;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_VALUES;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.Shitlet;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.database.DatabaseString;

/**
 * @author Andrey Kroupennikov
 * @author $Author: bass $
 * @version $Revision: 1.2 $
 * @module general
 */
@Shitlet
public final class ImportUidMapDatabase {
	private static final String TABLE_NAME_IMPORT_UID_MAP = "ImportUIDMap";
	private static final String COLUMN_IMPORT_KIND = "import_kind";
	private static final String COLUMN_FOREIGN_UID = "foreign_uid";
	private static final String COLUMN_ID = "id";

	private ImportUidMapDatabase() {
		assert false;
	}

	private static StringBuffer getWhereClause(final String importType, final XmlIdentifier id) {
		return new StringBuffer(COLUMN_IMPORT_KIND + EQUALS
				+ APOSTROPHE + importType + APOSTROPHE
				+ SQL_AND
				+ COLUMN_FOREIGN_UID + EQUALS
				+ APOSTROPHE + id.getStringValue() + APOSTROPHE);
	}

	private static StringBuffer retrieveQuery(final StringBuffer condition) {
		final StringBuffer sql = new StringBuffer(SQL_SELECT
				+ COLUMN_IMPORT_KIND + COMMA
				+ COLUMN_FOREIGN_UID + COMMA
				+ COLUMN_ID
				+ SQL_FROM + TABLE_NAME_IMPORT_UID_MAP);

		if (condition != null && condition.length() != 0) {
			sql.append(SQL_WHERE);
			sql.append(condition);
		}

		return sql;
	}

	static Identifier retrieve(final String importType, final XmlIdentifier id) throws RetrieveObjectException {
		final Set<Identifier> ids = retrieveByCondition(getWhereClause(importType, id));
		return ids.isEmpty() ? null : ids.iterator().next();
	}

	private static Set<Identifier> retrieveByCondition(final StringBuffer condition) throws RetrieveObjectException {
		final Set<Identifier> ids = new HashSet<Identifier>();

		final StringBuffer sql = retrieveQuery(condition);

		Statement statement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("ImportUidMapDatabase.retrieveByCondition | Trying: " + sql, Log.DEBUGLEVEL09);
			resultSet = statement.executeQuery(sql.toString());
			while (resultSet.next()) {
				final Identifier id = DatabaseIdentifier.getIdentifier(resultSet, COLUMN_ID);
				ids.add(id);
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
		return ids;
	}

	public static void insert(final String importType, final XmlIdentifier xmlId, final Identifier id) throws CreateObjectException {
		final StringBuffer sql = new StringBuffer(SQL_INSERT_INTO + TABLE_NAME_IMPORT_UID_MAP
				+ OPEN_BRACKET
				+ COLUMN_IMPORT_KIND + COMMA
				+ COLUMN_FOREIGN_UID + COMMA
				+ COLUMN_ID + CLOSE_BRACKET 
				+ SQL_VALUES + OPEN_BRACKET
				+ APOSTROPHE + DatabaseString.toQuerySubString(importType) + APOSTROPHE + COMMA
				+ APOSTROPHE + DatabaseString.toQuerySubString(xmlId.getStringValue()) + APOSTROPHE + COMMA
				+ DatabaseIdentifier.toSQLString(id)
				+ CLOSE_BRACKET);

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("ImportUidMapDatabase.insert | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (final SQLException sqle) {
			throw new CreateObjectException(("Cannot insert ImportUIDItem" + sqle.getMessage()), sqle);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (final SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
				DatabaseConnection.releaseConnection(connection);
			}
		}
	}

	public static void delete(final String importType, final XmlIdentifier id) {
		final StringBuffer sql = new StringBuffer(SQL_DELETE_FROM + TABLE_NAME_IMPORT_UID_MAP
				+ SQL_WHERE + getWhereClause(importType, id));

		Statement statement = null;
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			statement = connection.createStatement();
			Log.debugMessage("ImportUidMapDatabase.delete | Trying: " + sql, Log.DEBUGLEVEL09);
			statement.executeUpdate(sql.toString());
			connection.commit();
		} catch (final SQLException sqle) {
			Log.errorException(sqle);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (final SQLException sqle1) {
				Log.errorException(sqle1);
			} finally {
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
		} catch (final Exception e) {
			Log.errorException(e);
			System.exit(0);
		}
	}

	protected static void shutdown() {
		DatabaseConnection.closeConnection();
	}
}
