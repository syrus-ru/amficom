/*
 * $Id: ByteArrayDatabase.java,v 1.16 2005/06/17 11:25:48 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.io.*;
import java.sql.*;
import com.syrus.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/06/17 11:25:48 $
 * @module util
 */
public final class ByteArrayDatabase {
	private ByteArrayDatabase() {
		assert false;
	}

	public static void saveAsBlob(final byte bar[], final Connection conn, final String table, final String column, final String where) throws SQLException {
		boolean oldAutoCommit = conn.getAutoCommit();
		if (oldAutoCommit)
			conn.setAutoCommit(false);

		Statement statement = null;
		ResultSet ors = null;
		String s = "SELECT " + column + " FROM " + table + " WHERE " + where + " FOR UPDATE";
		try {
			statement = conn.createStatement();
			Log.debugMessage("Trying: " + s, Log.DEBUGLEVEL09);
			ors = statement.executeQuery(s);
			Blob blob = null;
			if (ors.next())
				blob = ors.getBlob(column);
			else
				throw new SQLException("No record in " + table + " for '" + where + "'");
			OutputStream os = null;
			os = blob.setBinaryStream(0L);
			os.write(bar);
			os.flush();
			os.close();
		} catch (IOException ioe) {
			throw new SQLException(ioe.getMessage());
		} finally {
			try {
				if (statement != null)
					statement.close();
			} finally {
				try {
					if (ors != null)
						ors.close();
				} finally {
					if (oldAutoCommit)
						conn.setAutoCommit(true);
				}
			}
		}
	}

	public static byte[] toByteArray(final Blob blob) throws SQLException {
		return blob.getBytes(1, (int) blob.length());
	}
}
