package com.syrus.util.database;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import oracle.sql.BLOB;
import oracle.jdbc.driver.OracleResultSet;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

public class ByteArrayDatabase {
  private byte[] bar;

  public ByteArrayDatabase(byte[] bar) {
    this.bar = bar;
  }

  public ByteArrayDatabase(ByteArray bArr) {
    this.bar = bArr.getBytes();
  }

  public void saveAsBlob(Connection conn, String table, String column, String where) throws SQLException {
    boolean oldAutoCommit = conn.getAutoCommit();
    if (oldAutoCommit)
      conn.setAutoCommit(false);

    Statement statement = null;
		OracleResultSet ors = null;
		String s = "SELECT " + column + " FROM " + table + " WHERE " + where + " FOR UPDATE";
		try {
		  statement = conn.createStatement();
			Log.debugMessage("Trying: " + s, Log.DEBUGLEVEL05);
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
		catch (IOException e){
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
