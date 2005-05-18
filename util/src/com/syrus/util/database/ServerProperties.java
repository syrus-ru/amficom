/*
 * $Id: ServerProperties.java,v 1.4 2005/05/18 10:49:17 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sqlj.runtime.ref.DefaultContext;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/18 10:49:17 $
 * @author $Author: bass $
 * @module util
 */
public final class ServerProperties {
	private static final Connection CONN = DefaultContext.getDefaultContext().getConnection();

	private ServerProperties() {
		assert false;
	}

	public static String getString(String key, String defaultValue) {
		try {
			return get(key);
		} catch (SQLException sqle) {
			Log.debugMessage("Cannot get resource " + key + " from database", Log.DEBUGLEVEL02);
			return defaultValue;
		}
	}

	public static int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(get(key));
		} catch (SQLException sqle) {
			Log.debugMessage("Cannot get resource " + key + " from database", Log.DEBUGLEVEL02);
			return defaultValue;
		}
	}

	private static String get(String key) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			StringBuffer sql = new StringBuffer("SELECT value FROM amficom.serverproperty WHERE key = '");
			sql.append(key);
			sql.append('\'');

			stmt = CONN.createStatement();
			resultSet = stmt.executeQuery(sql.toString());
			resultSet.next();
			return resultSet.getString("value");
		} finally {
			try {
				if (resultSet != null)
					resultSet.close();
			} finally {
				if (stmt != null)
					stmt.close();
			}
		}
	}
}
