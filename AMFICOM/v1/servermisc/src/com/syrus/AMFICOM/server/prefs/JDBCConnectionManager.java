/*
 * $Id: JDBCConnectionManager.java,v 1.2 2004/06/02 06:37:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.prefs;

import com.syrus.util.prefs.PreferencesManager;
import java.sql.*;
import java.util.prefs.Preferences;
import oracle.jdbc.OracleDriver;
import sqlj.runtime.ExecutionContext;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/02 06:37:32 $
 * @author $Author: bass $
 * @module serveradd
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
			Class clazz1 = PreferencesManager.class;
			Class clazz2 = OracleDriver.class;
			Class.forName(clazz2.getName());
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
