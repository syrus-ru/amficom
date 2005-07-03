/*
 * $Id: ConnectionManager.java,v 1.2 2004/06/07 15:36:09 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server;

import com.syrus.io.IniFile;
import java.io.IOException;
import java.sql.*;
import oracle.jdbc.driver.OracleDriver;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/07 15:36:09 $
 * @module servermisc
 */
public class ConnectionManager
{
	private static IniFile iniFile;

	private static final String INI_FILE_NAME = "ServerConnection.properties";

	/**
	 * @deprecated Use {@link #getConnCtx()} instead.
	 */
	public DefaultContext m_ctx;

	/**
	 * @deprecated Use {@link #getUrl()} instead.
	 */
	public String connectString;

	/**
	 * @deprecated Use {@link #getUser()} instead.
	 */
	public String login;

	/**
	 * @deprecated Use {@link #getPassword()} instead.
	 */
	public String password;

	private LogWriter logWriter;

	static
	{
		try
		{
			Class.forName(OracleDriver.class.getName());
		}
		catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}

	public ConnectionManager(LogWriter logWriter)
	{
		this.logWriter = logWriter;
		initModule();
	}

	private ConnectionManager()
	{
	}

	/**
	 * @deprecated Use {@link #initModule()} instead.
	 */
	public void init_module() {
		initModule();
	}

	public void initModule()
	{
		try
		{
			iniFile = new IniFile(INI_FILE_NAME);
		}
		catch (IOException ioe)
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
		catch (Exception e)
		{
			if (logWriter == null)
				System.out.println("could not read ini-file. Setting connection defaults...");
			else
				logWriter.log("could not read ini-file. Setting connection defaults...");
			setDefaults();
		}
	}

	public void setDefaults()
	{
		if (logWriter == null)
			System.out.println("Setting connection defaults...");
		else
			logWriter.log("Setting connection defaults...");
		connectString = "jdbc:oracle:thin:@Research:1521:Research";
		login = "AMFICOM";
		password = "amficom";
	}

	public boolean connect()
	{
		if (logWriter == null)
			System.out.println("connecting " + connectString + " as " + login + "... ");
		else
			logWriter.log("connecting " + connectString + " as " + login + "... ");
		try
		{
			m_ctx = new DefaultContext(
					  connectString,
					  login,
					  password,
					  false);
			DefaultContext.setDefaultContext(m_ctx);
		}
		catch (Exception e)
		{
			if (logWriter == null)
				System.out.println("Error connecting " + connectString + ": " + e.getMessage());
			else
				logWriter.log("Error connecting " + connectString + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		if (logWriter == null)
			System.out.println("connected!");
		else
			logWriter.log("connected!");
		return true;
	}

	public void disconnect()
	{
		if (logWriter == null)
			System.out.println("disconnect " + connectString);
		else
			logWriter.log("disconnect " + connectString);
		try
		{
			if (m_ctx != null)
				m_ctx.close();
		}
		catch (SQLException sqle)
		{
			if (logWriter == null)
				System.out.println("Error closing connection");
			else
				logWriter.log("Error closing connection");
		}
	}

	public String getUrl()
	{
		return connectString;
	}

	public String getUser()
	{
		return login;
	}

	public String getPassword()
	{
		return password;
	}

	public DefaultContext getConnCtx()
	{
		return m_ctx;
	}

	public String toString()
	{
		return connectString + "/" + login;
	}
}
