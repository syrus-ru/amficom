/*-
 * $Id: DatabaseConnection.java,v 1.27 2006/06/27 11:48:21 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import static com.syrus.util.Log.DEBUGLEVEL07;
import static com.syrus.util.Log.DEBUGLEVEL10;
import static java.util.logging.Level.SEVERE;

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.27 $, $Date: 2006/06/27 11:48:21 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class DatabaseConnection {
	private static final String JDBC_DRIVER_TYPE = "thin";
	private static final int DBPORT = 1521;
	public static final String USERNAME = "amficom";
	private static final String PASSWORD = "amficom";

	private static volatile int openConnections = 0;

	private static OracleDataSource dataSource;

	private DatabaseConnection() {
		assert false;
	}

	public static void establishConnection(final String dbHostname, final String dbSID, final long dbConnTimeout)
			throws SQLException {
		establishConnection(dbHostname, dbSID, dbConnTimeout, USERNAME);
	}

	public static void establishConnection(final String dbHostname,
			final String dbSID,
			final long dbConnTimeout,
			final String dbLoginName) throws SQLException {
		if (dataSource == null) {
			Log.debugMessage("Attempting to connect to database '" + dbSID
					+ "' on host '" + dbHostname
					+ "' as user '" + dbLoginName + "'",
					DEBUGLEVEL07);

			dataSource = new OracleDataSource();
			dataSource.setDriverType(JDBC_DRIVER_TYPE);
			dataSource.setServerName(dbHostname);
			dataSource.setPortNumber(DBPORT);
			dataSource.setDatabaseName(dbSID);
			dataSource.setUser(dbLoginName);
			dataSource.setPassword(PASSWORD);
			dataSource.setLoginTimeout((int) (dbConnTimeout / 1000));
			dataSource.setConnectionCachingEnabled(true);

			Log.debugMessage("Connected", DEBUGLEVEL07);
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

	public static void releaseConnection(final Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				openConnections--;
				Log.debugMessage("releaseConnection(Connection)", DEBUGLEVEL10);
			} catch (Exception e) {
				Log.errorMessage(e);
			}
		}
	}

	/**
	 * @deprecated Этот метод не нужен.
	 */
	@Deprecated
	public static void closeConnection() {
		if (dataSource != null) {
			Log.debugMessage("Disconnecting from database...", DEBUGLEVEL07);
			try {
				dataSource.close();
				dataSource = null;
				if (openConnections != 0) {
					Log.debugMessage("There remains " + openConnections + " connection(s) open", SEVERE);
				}
				Log.debugMessage("Disconnected from database", DEBUGLEVEL07);
			} catch (Exception e) {
				Log.errorMessage(e);
			}
		}
	}
}
