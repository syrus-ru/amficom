/*
 * $Id: JDBCConnectionManager.java,v 1.1 2004/05/06 11:48:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.prefs;

import java.sql.*;
import java.util.prefs.Preferences;
import oracle.jdbc.OracleDriver;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 * @module util
 */
public final class JDBCConnectionManager {
	private JDBCConnectionManager() {
	}

	private static Connection conn;

	private static DefaultContext connCtx;

	private static ExecutionContext execCtx;

	static {
		try {
			/*
			 * Migrate preferences.
			 */
			Class clazz = PreferencesManager.class;
			Class.forName(OracleDriver.class.getName());
			Preferences preferences = Preferences.userRoot().
				node(PreferencesManager.PREFERENCES_ROOT).node("util").
				node("connections").node("jdbc");
			conn = DriverManager.getConnection(
				preferences.get("connectString", ""),
				preferences.get("login", ""),
				preferences.get("password", ""));
			connCtx = new DefaultContext(conn);
			execCtx = connCtx.getExecutionContext();
			DefaultContext.setDefaultContext(connCtx);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	public static Connection getConn() {
		return conn;
	}

	public static DefaultContext getConnCtx() {
		return connCtx;
	}

	public static ExecutionContext getExecCtx() {
		return execCtx;
	}
}
