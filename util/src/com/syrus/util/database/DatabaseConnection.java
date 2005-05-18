/*
 * $Id: DatabaseConnection.java,v 1.12 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.syrus.util.Log;

public class DatabaseConnection {
	private static final String JDBCDRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URLPREFIX = "jdbc:oracle:thin:@";
	private static final int DBPORT = 1521;
	private static final String USERNAME = "amficom";
	private static final String PASSWORD = "amficom";

	private static Connection connection = null;

	private DatabaseConnection() {
		assert false;
	}

	public static void establishConnection(String db_hostname,
			 String db_sid,
			 long db_conn_timeout) throws SQLException {
		establishConnection(db_hostname,
				db_sid,
				db_conn_timeout,
				USERNAME,
				false);
	}

	public static void establishConnection(String db_hostname,
			 String db_sid,
			 long db_conn_timeout,
			 String db_login_name) throws SQLException {
		establishConnection(db_hostname,
				db_sid,
				db_conn_timeout,
				db_login_name,
				false);
	}

	public static void establishConnection(String db_hostname,
			 String db_sid,
			 long db_conn_timeout,
			 boolean autocommit) throws SQLException {
		establishConnection(db_hostname,
				db_sid,
				db_conn_timeout,
				USERNAME,
				autocommit);
	}

	public static void establishConnection(String db_hostname,
			 String db_sid,
			 long db_conn_timeout,
			 String db_login_name,
			 boolean autocommit) throws SQLException {
		if (connection == null) {
			try {
				Class.forName(JDBCDRIVER);
			}
			catch (ClassNotFoundException e) {
				throw new SQLException("Cannot locate driver: " + JDBCDRIVER + ", " + e.getMessage());
			}

			String url = URLPREFIX + db_hostname + ":" + Integer.toString(DBPORT) + ":" + db_sid;
			long deadtime = System.currentTimeMillis() + db_conn_timeout;
			boolean connected;
			for (connected = false; System.currentTimeMillis() < deadtime && !connected;) {
				Log.debugMessage("Attemting to connect to database: " + url + " as " + db_login_name, Log.DEBUGLEVEL07);
				try {
					connection = DriverManager.getConnection(url, db_login_name, PASSWORD);
					connection.setAutoCommit(autocommit);
					connected = true;
				}
				catch (SQLException e) {
					Log.debugMessage("Cannot connect to database: " + url + ", " + e.getMessage(), Log.DEBUGLEVEL07);
					Object obj = new Object();
					try {
						synchronized (obj) {
							obj.wait(5*1000);
						}
					}
					catch (InterruptedException ex) {
						Log.errorException(ex);
					}
				}
			}
			if (!connected)
				throw new SQLException("Unable to connect to database: " + url);
			Log.debugMessage("Connected!", Log.DEBUGLEVEL03);
		}
	}

	public static void setConnection(Connection conn) {
		if (connection == null)
			connection = conn;
	}

	public static void closeConnection() {
		if (connection != null) {
			Log.debugMessage("Disconnecting from database...", Log.DEBUGLEVEL07);
			try {
				connection.close();
				Log.debugMessage("Disconnected from database", Log.DEBUGLEVEL07);
			}
			catch (Exception e) {
				Log.errorException(e);
			}
		}
	}
	
	public static void releaseConnection(final Connection connection1) {
		if (connection1 != null) {
			try {
				// ���� �� ������������ �������������� � ConnectionPool
				// connection �� ���������
				/**
				 * FIXME release connection when Oracle Connection Pool'll have made
				 */
				// connection.close();
				Log.debugMessage("DatabaseConnection | releaseConnection(Connection)", Log.DEBUGLEVEL10);
			}
			catch (Exception e) {
				Log.errorException(e);
			}
		}
	}


	public static Connection getConnection() {
		return connection;
	}
}
