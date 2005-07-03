/*
 * $Id: JdbcConnectionManager.java,v 1.1 2004/09/14 08:29:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.prefs;

import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.prefs.PreferencesManager;
import java.sql.Connection;
import java.util.prefs.Preferences;
import javax.sql.DataSource;
import oracle.jdbc.OracleDriver;
import oracle.jdbc.pool.OracleDataSource;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/14 08:29:57 $
 * @author $Author: bass $
 * @module servermisc
 */
public final class JdbcConnectionManager {
	private JdbcConnectionManager() {
	}

	private static final DataSource DATA_SOURCE;

	static {
		DataSource dataSource = null;
		try {
			/*
			 * Migrate preferences.
			 */
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader != null) {
				Class.forName(OracleDriver.class.getName(), true, classLoader);
				Class.forName(PreferencesManager.class.getName(), true, classLoader);
			} else {
				Class.forName(OracleDriver.class.getName());
				Class.forName(PreferencesManager.class.getName());
			}

			Preferences preferences = Preferences.userRoot().node(PreferencesManager.PREFERENCES_ROOT).node("util").node("connections").node("jdbc");

			final OracleDataSource oracleDataSource = new OracleDataSource();

			oracleDataSource.setURL(preferences.get("connectString", ""));
			oracleDataSource.setUser(preferences.get("login", ""));
			oracleDataSource.setPassword(preferences.get("password", ""));

			oracleDataSource.setConnectionCachingEnabled(true);

			/*
			 * Default connection.
			 */
			final Connection conn = oracleDataSource.getConnection();
			conn.setAutoCommit(false);
			DefaultContext.setDefaultContext(new DefaultContext(conn));
			DatabaseConnection.setConnection(conn);

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						System.err.print("Closing the default connection... ");
						conn.close();
						System.err.println("done.");
					} catch (Exception e) {
						System.err.println("failed.");
						e.printStackTrace();
					}
					try {
						System.err.print("Closing the data source... ");
						oracleDataSource.close();
						System.err.println("done.");
					} catch (Exception e) {
						System.err.println("failed.");
						e.printStackTrace();
					}
				}
			});

			dataSource = oracleDataSource;
		} catch (Exception e) {
			System.err.println("Error occured during data source initialization... exiting.");
			e.printStackTrace();
			System.exit(-1);
		}
		DATA_SOURCE = dataSource;
	}

	public static DataSource getDataSource() {
		return DATA_SOURCE;
	}
}
