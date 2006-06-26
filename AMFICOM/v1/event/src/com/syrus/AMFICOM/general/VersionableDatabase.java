/*-
 * $Id: VersionableDatabase.java,v 1.1 2006/06/26 17:23:13 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.util.database.DatabaseConnection.USERNAME;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2006/06/26 17:23:13 $
 * @module event
 */
public abstract class VersionableDatabase<T extends StorableObject> extends StorableObjectDatabase<T> {
	protected VersionableDatabase() {
		try {
			final String actualVersion = this.getActualVersion();
			if (!this.getExpectedVersion().equals(actualVersion)) {
				Log.debugMessage("Actual table version: " + actualVersion
						+ " is different from the expected one: "
						+ this.getExpectedVersion() + '.',
						WARNING);
			}
		} catch (final SQLException sqle) {
			Log.debugMessage(sqle, SEVERE);
		}
	}

	/**
	 * Must return a non-{@code null} string indicating expected version
	 * of a database table.
	 */
	protected abstract String getExpectedVersion();

	/**
	 * Returns a string representation of database table&apos;s version.
	 * May return {@code null} if no version information was found.
	 *
	 * @throws SQLException
	 */
	private String getActualVersion()
	throws SQLException {
		final String tableAllTabComments = "all_tab_comments";
		final String columnComments = "comments";
		final String columnTableName = "table_name";
		final String columnOwner = "owner";
		final String keyUsername = "DBLoginName";
		final String defaultUsername = USERNAME;

		Connection conn = null;
		Statement stmt = null;
		try {
			final String entity = ObjectEntities.codeToString(this.getEntityCode());
			final String username = ApplicationProperties.getString(keyUsername, defaultUsername);
			conn = DatabaseConnection.getConnection();
			stmt = conn.createStatement();
			final ResultSet rs = stmt.executeQuery(
					SQL_SELECT + columnComments + SQL_FROM + tableAllTabComments
					+ SQL_WHERE + columnTableName + EQUALS + SQL_FUNCTION_UPPER
					+ OPEN_BRACKET + APOSTROPHE + entity + APOSTROPHE + CLOSE_BRACKET
					+ SQL_AND + columnOwner + EQUALS + SQL_FUNCTION_UPPER
					+ OPEN_BRACKET + APOSTROPHE + username + APOSTROPHE + CLOSE_BRACKET);
			if (!rs.next()) {
				Log.debugMessage("No version information found for table: "
						+ entity + '.',
						WARNING);
				return null;
			}
			final String comments = rs.getString(columnComments);
			final Pattern pattern = Pattern.compile("^\\$Id\\:\\ .*\\,v\\ (1(\\.[0-9]+)+)\\ .*$");
			final Matcher matcher = pattern.matcher(comments);
			if (!matcher.matches()) {
				Log.debugMessage("Cannot parse version information: ``"
						+ comments
						+ "'' for table: " + entity + '.',
						WARNING);
				return null;
			}
			return matcher.group(1);
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} finally {
				if (conn != null) {
					DatabaseConnection.releaseConnection(conn);
				}
			}
		}
	}
}
