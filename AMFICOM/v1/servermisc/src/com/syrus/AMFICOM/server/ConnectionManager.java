/*
 * $Id: ConnectionManager.java,v 1.1.1.1 2004/05/27 11:17:16 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server;

import com.syrus.io.IniFile;
import java.sql.*;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/05/27 11:17:16 $
 * @module serveradd
 */
public class ConnectionManager extends Object
{
	static IniFile iniFile;
	static String iniFileName = "ServerConnection.properties";

	LogWriter logWriter;

	public DefaultContext m_ctx;

	public String connectString;
	public String login;
	public String password;

	protected ConnectionManager()
	{
	}

	public ConnectionManager(LogWriter logWriter)
	{
		this.logWriter = logWriter;
		init_module();
	}

	public void init_module()
	{
		try
		{
			iniFile = new IniFile(iniFileName);
		}
		catch(java.io.IOException e)
		{
			setDefaults();
			return;
		}

		try
		{
			connectString = iniFile.getValue("connectString");
			login = iniFile.getValue("login");
			password = iniFile.getValue("password");
		}
		catch(Exception e)
		{
			if(logWriter == null)
				System.out.println("could not read ini-file. Setting connection defaults...");
			else
				logWriter.log("could not read ini-file. Setting connection defaults...");
			setDefaults();
		}
	}

	public void setDefaults()
	{
		if(logWriter == null)
			System.out.println("Setting connection defaults...");
		else
			logWriter.log("Setting connection defaults...");
		connectString = "jdbc:oracle:thin:@Research:1521:Research";
		login = "AMFICOM";
		password = "amficom";
	}

	public boolean connect( )// throws SQLException
	{
		if(logWriter == null)
			System.out.println("connecting " + connectString + " as " + login + "... ");
		else
			logWriter.log("connecting " + connectString + " as " + login + "... ");
		try
		{
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver( ));
			m_ctx = new DefaultContext(
					  connectString,
					  login,
					  password,
					  false);
			DefaultContext.setDefaultContext(m_ctx);
		}
		catch(Exception e)
		{
			if(logWriter == null)
				System.out.println("Error connecting " + connectString + ": " + e.getMessage());
			else
				logWriter.log("Error connecting " + connectString + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		if(logWriter == null)
			System.out.println("connected!");
		else
			logWriter.log("connected!");
		return true;
	}

	public void disconnect( )
	{
		if(logWriter == null)
			System.out.println("disconnect " + connectString);
		else
			logWriter.log("disconnect " + connectString);
		try
		{
			if (m_ctx != null)
				m_ctx.close();
		}
		catch (SQLException ex)
		{
			if(logWriter == null)
				System.out.println("Error closing connection");
			else
				logWriter.log("Error closing connection");
		}
	}

	public String toString()
	{
		return connectString + "/" + login;
	}
}
