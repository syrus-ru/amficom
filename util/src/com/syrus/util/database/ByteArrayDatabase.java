/*
 * $Id: ByteArrayDatabase.java,v 1.9 2004/08/22 15:23:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.database;

import java.io.OutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;
import com.syrus.util.Log;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2004/08/22 15:23:26 $
 * @module util
 */
public class ByteArrayDatabase {

	private ByteArrayDatabase() {
	}

	public static void saveAsBlob(byte[] bar, Connection conn, String table, String column, String where) throws SQLException {
		boolean oldAutoCommit = conn.getAutoCommit();
		if (oldAutoCommit)
			conn.setAutoCommit(false);

		Statement statement = null;
		OracleResultSet ors = null;
		String s = "SELECT " + column + " FROM " + table + " WHERE " + where + " FOR UPDATE";
		try {
			statement = conn.createStatement();
			Log.debugMessage("Trying: " + s, Log.DEBUGLEVEL09);
			ors = (OracleResultSet)statement.executeQuery(s);
			BLOB blob = null;
			if (ors.next())
				blob = ors.getBLOB(column);
			else
				throw new SQLException("No record in " + table + " for '" + where + "'");
			OutputStream os = null;
			os = blob.getBinaryOutputStream();
			os.write(bar);
			os.close();
		}
		catch (IOException e) {
			throw new SQLException(e.getMessage());
		}
		finally {
			if (statement != null)
				statement.close();
			if (ors != null)
				ors.close();
			statement = null;
			ors = null;
			if (oldAutoCommit)
				conn.setAutoCommit(true);
		}
	}

	public static byte[] toByteArray(BLOB blob) throws SQLException{
		return blob.getBytes(1, (int)blob.length());
	}
}
