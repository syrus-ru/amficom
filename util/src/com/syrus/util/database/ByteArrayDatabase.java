/*
 * $Id: ByteArrayDatabase.java,v 1.22 2005/10/31 12:29:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2005/10/31 12:29:58 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public final class ByteArrayDatabase {
	private ByteArrayDatabase() {
		assert false;
	}

	public static void saveAsBlob(final byte bar[],
			final Connection conn,
			final String table,
			final String column,
			final String where) throws SQLException {
		final boolean oldAutoCommit = conn.getAutoCommit();
		if (oldAutoCommit) {
			conn.setAutoCommit(false);
		}

		Statement statement = null;
		ResultSet ors = null;
		final String s = "SELECT " + column + " FROM " + table + " WHERE " + where + " FOR UPDATE";
		try {
			statement = conn.createStatement();
			Log.debugMessage("Trying: " + s, Log.DEBUGLEVEL09);
			ors = statement.executeQuery(s);
			if (ors.next()) {
				final Blob blob = ors.getBlob(column);
				final OutputStream os = blob.setBinaryStream(0L);
				os.write(bar);
				os.flush();
				os.close();
			}
			else {
				throw new SQLException("No record in " + table + " for '" + where + "'");
			}
		} catch (IOException ioe) {
			throw new SQLException(ioe.getMessage());
		} finally {
			try {
				try {
					if (ors != null) {
						ors.close();
						ors = null;
					}
				} finally {
					try {
						if (statement != null) {
							statement.close();
							statement = null;
						}
					} finally {
						if (oldAutoCommit) {
							conn.setAutoCommit(true);
						}
					}
				}
			} catch (SQLException sqle) {
				Log.errorMessage(sqle);
			}
		}
	}

	public static byte[] toByteArray(final Blob blob) throws SQLException {
		return blob.getBytes(1, (int) blob.length());
	}
}
