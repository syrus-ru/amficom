package com.syrus.AMFICOM.server.load;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import sqlj.runtime.ref.DefaultContext;

/**
 * @deprecated Use {@link com.syrus.AMFICOM.server.ConnectionManager} from the
 *             servermisc_v1 module instead.
 */
public class ConnectionManager {
	DefaultContext m_ctx;

	String connectString;
	String login;
	String password;

	public ConnectionManager() {
		init_module();
	}

	public void init_module()	{
	    setDefaults();
	}

	public void setDefaults()	{
		connectString = "jdbc:oracle:thin:@research:1521:research";
    //connectString = "jdbc:oracle:thin:@amficom:1521:demo2";
    //connectString = "jdbc:oracle:thin:@mousebuster:1521:andrei";
		login = "AMFICOM";
		password = "amficom";
	}

	public boolean connect() {// throws SQLException
		System.out.println("Connecting as " + login + "/" + password + "@" + connectString + " ...");
		try	{
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			m_ctx = new DefaultContext(
					  connectString,
					  login,
					  password,
					  false);
			DefaultContext.setDefaultContext(m_ctx);
		}
		catch(Exception e) {
			System.out.println("Error connecting " + connectString + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		System.out.println("connected!");
		return true;
	}

  public Connection getConnection() {
    return (Connection)this.m_ctx.getOracleConnection();
  }

	public void disconnect( )	{
		System.out.println("disconnect " + connectString);
		try	{
			if (m_ctx != null)
				m_ctx.close();
		}
		catch (SQLException ex)	{
			System.out.println("Error closing connection: " + ex.getMessage());
		}
	}
}

