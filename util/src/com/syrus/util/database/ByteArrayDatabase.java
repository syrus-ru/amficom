/*
 * $Id: ByteArrayDatabase.java,v 1.11 2004/09/09 12:10:11 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.database;

import java.io.*;
import java.sql.*;
import com.syrus.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2004/09/09 12:10:11 $
 * @module util
 */
public class ByteArrayDatabase {
	/**
	 * @deprecated
	 */
	private byte[] bar;

	private ByteArrayDatabase() {
	}

	/**
	 * @deprecated
	 */
	public ByteArrayDatabase(byte[] bar) {
		this.bar = bar;
	}

	/**
	 * @deprecated
	 */
	public ByteArrayDatabase(ByteArray bArr) {
		this.bar = bArr.getBytes();
	}

	/**
	 * @deprecated
	 */
	public void saveAsBlob(Connection conn, String table, String column, String where) throws SQLException {
		saveAsBlob(bar, conn, table, column, where);
	}

	public static void saveAsBlob(byte bar[], Connection conn, String table, String column, String where) throws SQLException {
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
			os.close();
		}
		catch (IOException ioe) {
			throw new SQLException(ioe.getMessage());
		}
		finally {
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

	public static byte[] toByteArray(Blob blob) throws SQLException {
		return blob.getBytes(1, (int) blob.length());
	}
}
