/*
 * $Id: ByteArrayDatabase.java,v 1.7 2004/08/22 14:53:11 arseniy Exp $
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
import com.syrus.util.ByteArray;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.7 $, $Date: 2004/08/22 14:53:11 $
 * @deprecated This class doesn't realize <i>singleton</i> pattern, so
 *             eventually we can receive multiple unnecessary instances. Use
 *             {@link JdbcBlobManager} instead.
 * @module util
 */
public class ByteArrayDatabase {
	private byte[] bar;

	public ByteArrayDatabase(byte[] bar) {
		this.bar = bar;
	}

	public ByteArrayDatabase(ByteArray bArr) {
		this.bar = bArr.getBytes();
	}

	/**
	 * @deprecated Use {@link JdbcBlobManager#setData(Connection, String,
	 *             String, String, boolean, byte[])} instead
	 */
	public void saveAsBlob(Connection conn, String table, String column, String where) throws SQLException {
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
			os.write(this.bar);
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

	/**
	 * @deprecated Use {@link JdbcBlobManager#getData(Blob)} instead.
	 */
	public static byte[] toByteArray(BLOB blob) throws SQLException{
		return blob.getBytes(1, (int)blob.length());
	}
}
