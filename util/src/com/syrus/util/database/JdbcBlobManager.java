/*
 * $Id: JdbcBlobManager.java,v 1.3 2004/08/03 14:38:20 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.database;

import java.io.*;
import java.sql.*;

/**
 * This class is a simple helper for operations with <i>Binary Large Objects</i>
 * (<i>BLOB</i>s). It allows to save byte&nbsp;array/input&nbsp;stream contents
 * as a BLOB and vice versa, to represent a BLOB
 * as a byte&nbsp;array/input&nbsp;stream.
 *
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/08/03 14:38:20 $
 * @module util
 */
public final class JdbcBlobManager {
	/**
	 * Singleton.
	 */
	private JdbcBlobManager() {
	}

	/**
	 * Returns a byte-array representation of the BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param colName the name of the table column.
	 * @param table the name of the database table.
	 * @param where the trailing <code>WHERE</code> clause of the SQL query.
	 * @return byte array which contains data stored in the BLOB.
	 * @throws SQLException if a database error occurs.
	 */
	public static byte[] getData(final Connection conn,
			final String colName, final String table,
			final String where) throws SQLException {
		return getData(conn, "SELECT "  + colName + " FROM " + table
			+ " WHERE " + where);
	}

	/**
	 * Returns an input-stream representation of the BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param colName the name of the table column.
	 * @param table the name of the database table.
	 * @param where the trailing <code>WHERE</code> clause of the SQL query.
	 * @return input stream which data stored in the BLOB can be read from. 
	 * @throws SQLException if a database error occurs.
	 */
	public static InputStream getDataAsStream(final Connection conn,
			final String colName, final String table,
			final String where) throws SQLException {
		return getDataAsStream(conn, "SELECT "  + colName + " FROM "
			+ table + " WHERE " + where);
	}

	/**
	 * Stores the contents of the byte array as a BLOB. The same as {@link
	 * #setData(Connection, String, String, String, boolean, byte[])}; the
	 * only difference is that this method issues <code>COMMIT</code>
	 * automatically.
	 *
	 * @param conn the database connection to use.
	 * @param colName the name of the table column.
	 * @param table the name of the database table.
	 * @param where the trailing <code>WHERE</code> clause of the SQL query.
	 * @param data the byte array which contains the data to be stored.
	 * @throws SQLException if a database error occurs.
	 * @see #setData(Connection, String, String, String, boolean, byte[])
	 */
	public static void setData(final Connection conn, final String colName,
			final String table, final String where,
			final byte data[]) throws SQLException {
		setData(conn, colName, table, where, true, data);
	}

	/**
	 * Stores the contents of the byte array as a BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param colName the name of the table column.
	 * @param table the name of the database table.
	 * @param where the trailing <code>WHERE</code> clause of the SQL query.
	 * @param executeCommit if <code>true</code>, then <code>COMMIT</code>
	 *        is issued automatically before method returns; otherwise it
	 *        lies on programmer's conscience to issue <code>COMMIT</code>. 
	 * @param data the byte array which contains the data to be stored.
	 * @throws SQLException if a database error occurs.
	 */
	public static void setData(final Connection conn, final String colName,
			final String table, final String where,
			final boolean executeCommit, final byte data[])
			throws SQLException {
		setData(conn, "SELECT " + colName + " FROM " + table + " WHERE "
			+ where + " FOR UPDATE", executeCommit, data);
	}

	/**
	 * Stores the contents of the input stream as a BLOB. The same as {@link
	 * #setDataAsStream(Connection, String, String, String, boolean,
	 * InputStream)}; the only difference is that this method issues
	 * <code>COMMIT</code> automatically.
	 * 
	 * @param conn the database connection to use.
	 * @param colName the name of the table column.
	 * @param table the name of the database table.
	 * @param where the trailing <code>WHERE</code> clause of the SQL query.
	 * @param in the input stream which the data to be stored will be read
	 *        from.
	 * @throws SQLException if a database error occurs.
	 * @see #setDataAsStream(Connection, String, String, String, boolean,
	 *      InputStream)
	 */
	public static void setDataAsStream(final Connection conn,
			final String colName, final String table,
			final String where, final InputStream in)
			throws SQLException {
		setDataAsStream(conn, colName, table, where, true, in);
	}

	/**
	 * Stores the contents of the input stream as a BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param colName the name of the table column.
	 * @param table the name of the database table.
	 * @param where the trailing <code>WHERE</code> clause of the SQL query.
	 * @param executeCommit if <code>true</code>, then <code>COMMIT</code>
	 *        is issued automatically before method returns; otherwise it
	 *        lies on programmer's conscience to issue <code>COMMIT</code>. 
	 * @param in the input stream which the data to be stored will be read
	 *        from.
	 * @throws SQLException if a database error occurs.
	 */
	public static void setDataAsStream(final Connection conn,
			final String colName, final String table,
			final String where, final boolean executeCommit,
			final InputStream in) throws SQLException {
		setDataAsStream(conn, "SELECT " + colName + " FROM " + table +
			" WHERE " + where + " FOR UPDATE", executeCommit, in);
	}

	/**
	 * Returns a byte-array representation of the BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param sql full SQL query. Must return 1 row, the 1st column of this
	 *        row being a BLOB.
	 * @return byte array which contains data stored in the BLOB. 
	 * @throws SQLException if a database error occurs.
	 */
	public static byte[] getData(final Connection conn, final String sql)
			throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			resultSet.next();
			return getData(resultSet.getBlob(1));
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (stmt != null)
				stmt.close();
		}
	}

	/**
	 * Returns an input-stream representation of the BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param sql full SQL query. Must return 1 row, the 1st column of this
	 *        row being a BLOB.
	 * @return input stream which data stored in the BLOB can be read from. 
	 * @throws SQLException if a database error occurs.
	 */
	public static InputStream getDataAsStream(final Connection conn,
			final String sql) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			resultSet.next();
			return getDataAsStream(resultSet.getBlob(1));
		} finally {
			if (resultSet != null)
				resultSet.close();
			if (stmt != null)
				stmt.close();
		}
	}

	/**
	 * Stores the contents of the byte array as a BLOB. The same as {@link
	 * #setData(Connection, String, boolean, byte[])}; the only difference
	 * is that this method issues <code>COMMIT</code> automatically.
	 * 
	 * @param conn the database connection to use.
	 * @param sql full SQL query. Must return 1 row, the 1st column of this
	 *        row being a <i>locked</i> BLOB
	 *        (<code>SELECT ... FROM ... WHERE ... FOR UPDATE</code>).
	 * @param data the byte array which contains the data to be stored.
	 * @throws SQLException if a database error occurs.
	 * @see #setData(Connection, String, boolean, byte[])
	 */
	public static void setData(final Connection conn, final String sql,
			final byte data[]) throws SQLException {
		setData(conn, sql, true, data);
	}

	/**
	 * Stores the contents of the byte array as a BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param sql full SQL query. Must return 1 row, the 1st column of this
	 *        row being a <i>locked</i> BLOB
	 *        (<code>SELECT ... FROM ... WHERE ... FOR UPDATE</code>).
	 * @param executeCommit if <code>true</code>, then <code>COMMIT</code>
	 *        is issued automatically before method returns; otherwise it
	 *        lies on programmer's conscience to issue <code>COMMIT</code>. 
	 * @param data the byte array which contains the data to be stored.
	 * @throws SQLException if a database error occurs.
	 */
	public static void setData(final Connection conn, final String sql,
			final boolean executeCommit, final byte data[])
			throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		boolean autoCommit = conn.getAutoCommit();
		try {
			if (autoCommit)
				conn.setAutoCommit(false);

			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			resultSet.next();
			setData(resultSet.getBlob(1), data);
		} finally {
			if (executeCommit)
				executeCommit(conn);

			if (autoCommit)
				conn.setAutoCommit(true);

			if (resultSet != null)
				resultSet.close();
			if (stmt != null)
				stmt.close();
		}
	}

	/**
	 * Stores the contents of the input stream as a BLOB.
	 * The same as {@link #setDataAsStream(Connection, String,
	 * boolean, InputStream)}; the only difference is that this
	 * method issues <code>COMMIT</code> automatically.
	 * 
	 * @param conn the database connection to use.
	 * @param sql full SQL query. Must return 1 row, the 1st column of this
	 *        row being a <i>locked</i> BLOB
	 *        (<code>SELECT ... FROM ... WHERE ... FOR UPDATE</code>).
	 * @param in the input stream which the data to be stored will be read
	 *        from.
	 * @throws SQLException if a database error occurs.
	 * @see #setDataAsStream(Connection, String, boolean, InputStream)
	 */
	public static void setDataAsStream(final Connection conn,
			final String sql, final InputStream in)
			throws SQLException {
		setDataAsStream(conn, sql, true, in);
	}

	/**
	 * Stores the contents of the input stream as a BLOB.
	 *
	 * @param conn the database connection to use.
	 * @param sql full SQL query. Must return 1 row, the 1st column of this
	 *        row being a <i>locked</i> BLOB
	 *        (<code>SELECT ... FROM ... WHERE ... FOR UPDATE</code>).
	 * @param executeCommit if <code>true</code>, then <code>COMMIT</code>
	 *        is issued automatically before method returns; otherwise it
	 *        lies on programmer's conscience to issue <code>COMMIT</code>. 
	 * @param in the input stream which the data to be stored will be read
	 *        from.
	 * @throws SQLException if a database error occurs.
	 */
	public static void setDataAsStream(final Connection conn,
			final String sql, final boolean executeCommit,
			final InputStream in) throws SQLException {
		Statement stmt = null;
		ResultSet resultSet = null;
		boolean autoCommit = conn.getAutoCommit();
		try {
			if (autoCommit)
				conn.setAutoCommit(false);

			stmt = conn.createStatement();
			resultSet = stmt.executeQuery(sql);
			resultSet.next();
			setDataAsStream(resultSet.getBlob(1), in);
		} finally {
			if (executeCommit)
				executeCommit(conn);

			if (autoCommit)
				conn.setAutoCommit(true);

			if (resultSet != null)
				resultSet.close();
			if (stmt != null)
				stmt.close();
		}
	}

	/**
	 * Returns a byte-array representation of the BLOB.
	 *
	 * @param blob the BLOB to read data from.
	 * @return byte array which contains data stored in the BLOB. 
	 * @throws SQLException if a database error occurs.
	 */
	public static byte[] getData(final Blob blob) throws SQLException {
		long blobLength = blob.length();
		if (blobLength > Integer.MAX_VALUE)
			throw new SQLException("Blob too large to fit into an array");
		return blob.getBytes(1L, (int) blobLength);
	}

	/**
	 * Returns an input-stream representation of the BLOB.
	 *
	 * @param blob the BLOB to read data from.
	 * @return input stream which data stored in the BLOB can be read from. 
	 * @throws SQLException if a database error occurs.
	 */
	public static InputStream getDataAsStream(final Blob blob)
			throws SQLException {
		return blob.getBinaryStream();
	}

	/**
	 * Stores the contents of the byte array as a BLOB.
	 *
	 * @param blob the BLOB to store data to.
	 * @param data the byte array which contains the data to be stored.
	 * @throws SQLException if a database error occurs.
	 */
	public static void setData(final Blob blob, final byte data[])
			throws SQLException {
		setDataAsStream(blob, new ByteArrayInputStream(data));
	}

	/**
	 * Stores the contents of the input stream as a BLOB.
	 *
	 * @param blob the BLOB to store data to.
	 * @param in the input stream which the data to be stored will be read
	 *        from.
	 * @throws SQLException if a database error occurs.
	 */
	public static void setDataAsStream(final Blob blob,
			final InputStream in) throws SQLException {
		try {
			int b;
			long position = 0;
//			OutputStream out = blob.setBinaryStream(0L);
			OutputStream out = ((oracle.sql.BLOB) blob).getBinaryOutputStream();
			while (true) {
				b = in.read();
				if (b == -1)
					break;
				out.write(b);
				position++;
			}
			out.flush();
			out.close();
//			blob.truncate(position);
			((oracle.sql.BLOB) blob).trim(position);
		} catch (IOException ioe) {
			SQLException sqle = new SQLException(ioe.toString());
//			sqle.initCause(ioe);
			throw sqle;
		}
	}

	/**
	 * Issues <code>COMMIT</code>.
	 *
	 * @param conn the database connection to use.
	 * @throws SQLException if a database error occurs.
	 */
	private static void executeCommit(final Connection conn)
			throws SQLException {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute("COMMIT");
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
}
