/*-
 * $Id: DatabaseConnection.java,v 1.20 2005/10/11 14:13:30 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import static java.util.logging.Level.SEVERE;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleDriver;
import oracle.jdbc.pool.OracleDataSource;

import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.20 $, $Date: 2005/10/11 14:13:30 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class DatabaseConnection {
	private static final String JDBCDRIVER = OracleDriver.class.getName();
	private static final String URLPREFIX = "jdbc:oracle:thin:@";
	private static final int DBPORT = 1521;
	private static final String USERNAME = "amficom";
	private static final String PASSWORD = "amficom";

	private static volatile int openConnections = 0;

	private static OracleDataSource dataSource;

	private DatabaseConnection() {
		assert false;
	}

	public static void establishConnection(final String db_hostname,
			 final String db_sid,
			 final long db_conn_timeout) throws SQLException {
		establishConnection(db_hostname,
				db_sid,
				db_conn_timeout,
				USERNAME);
	}

	public static void establishConnection(final String db_hostname,
			final String db_sid,
			final long db_conn_timeout,
			final String db_login_name) throws SQLException {
		if (dataSource == null) {
			final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			try {
				if (classLoader == null) {
					Class.forName(JDBCDRIVER);
				} else {
					Class.forName(JDBCDRIVER, true, classLoader);
				}
			} catch (final ClassNotFoundException cnfe) {
				throw new SQLException("Cannot locate driver: " + JDBCDRIVER + ", " + cnfe.getMessage());
			}
			
			final String url = URLPREFIX + db_hostname + ":" + Integer.toString(DBPORT) + ":" + db_sid;
			final long deadtime = System.currentTimeMillis() + db_conn_timeout;
			boolean connected = false;
			for (connected = false; System.currentTimeMillis() < deadtime && !connected;) {
				Log.debugMessage("DatabaseConnection.establishConnection() | Attempting to connect to database: " + url + " as " + db_login_name, Log.DEBUGLEVEL07);
				try {
					dataSource = new OracleDataSource();
					
					dataSource.setURL(url);
					dataSource.setUser(db_login_name);
					dataSource.setPassword(PASSWORD);

					dataSource.setConnectionCachingEnabled(true);

					connected = true;
				} catch (SQLException e) {
					Log.debugMessage("Cannot connect to database: " + url + ", " + e.getMessage(), Log.DEBUGLEVEL07);
					final Object obj = new Object();
					try {
						synchronized (obj) {
							obj.wait(5 * 1000);
						}
					} catch (InterruptedException ex) {
						Log.errorException(ex);
					}
				}
			}
			if (!connected) {
				throw new SQLException("Unable to connect to database: " + url);
			}
			Log.debugMessage("Connected!", Log.DEBUGLEVEL03);
		}
	}

	public static void closeConnection() {
		if (dataSource != null) {
			Log.debugMessage("Disconnecting from database...", Log.DEBUGLEVEL07);
			try {
				dataSource.close();
				dataSource = null;
				if (openConnections != 0) {
					Log.debugMessage("\u0410\u0440\u0441\u0435\u043d\u0438\u0439.\u0442\u044b\u0413\u0434\u0435-\u0442\u043e\u041d\u0430\u0435\u0431\u0430\u043b\u0441\u044f() | There remains " + openConnections + " connection(s) open", SEVERE);
				}
				Log.debugMessage("Disconnected from database", Log.DEBUGLEVEL07);
			} catch (Exception e) {
				Log.errorException(e);
			}
		}
	}

	public static void releaseConnection(final Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				openConnections--;
				Log.debugMessage("DatabaseConnection | releaseConnection(Connection)", Log.DEBUGLEVEL10);
			} catch (Exception e) {
				Log.errorException(e);
			}
		}
	}


	public static Connection getConnection() throws SQLException {
		if (dataSource != null) {
			final Connection connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			openConnections++;
			return connection;
		}
		throw new SQLException("Connection with database closed");
	}
}
