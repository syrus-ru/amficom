/*
 * $Id: JDBCConnectionInfo.java,v 1.3 2004/08/29 13:52:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Ќаучно-технический центр.
 * ѕроект: јћ‘» ќћ.
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.server.ConnectionManager;
import com.syrus.AMFICOM.server.object.AMFICOMImpl;
import java.util.logging.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/08/29 13:52:03 $
 * @author $Author: bass $
 * @module connjdbc_v1
 */
public class JDBCConnectionInfo extends RISDConnectionInfo 
{
	private boolean connected = false;
	private ConnectionManager connectionManager;

	private Handler handler;

	public JDBCConnectionInfo()
	{
		super();
//		initialize();
	}

	public void initialize()
	{
		handler = new ConsoleHandler();
		handler.setLevel(Level.CONFIG);
		connectionManager = new ConnectionManager(null);
	}
	
	private void connect()
	{
		if(!connected)
		{
			if(connectionManager.connect())
			{
				connected = true;
			}
		}
	}

	private void disconnect()
	{
		if(connected)
		{
			connectionManager.disconnect();
			connected = false;
		}
	}

	public static ConnectionInterface Connect(String objectName, String serviceURL, String user, String password)
	{
		RISDConnectionInfo ci = new RISDConnectionInfo();
		ci.serviceURL = new ServiceURL(serviceURL);
		ci.objectName = new String(objectName);
		ci.user = new String(user);
		ci.password = new String(password);
		return ci.Connect();
	}

	public ConnectionInterface Connect()
	{
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("Connect()");
		handler.publish(logRecord);

		if (!isConnected())
			try
		{
			connect();
			server = new AMFICOMImpl();

//              /*
//               * если соединение установлено (не произошло исключени€)
//               * то инициализируем переменную состо€ние как "есть соединение",
//               * запоминаем врем€ соединени€, обнул€ем количество сессий дл€ данного
//               * соединени€
//               */
			connection_state = CONNECTION_OPENED;
			ConnectTime = System.currentTimeMillis();
			sessions = 0;
			add(this);

//              /*
//               * соединение становитс€ активным соединением
//               */
			setActiveConnection(this);
		}
		catch(Exception e)
		{
			logRecord = new LogRecord(Level.SEVERE, e.toString());
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("Connect()");
			logRecord.setThrown(e);
			handler.publish(logRecord);
		}

		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("Connect()");
		handler.publish(logRecord);
		if (isConnected())
			return this;
		else
			return null;
	}

	/**
	 * –азорвать соединение с сервером.
	 */
	public void Disconnect()
	{
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("Disconnect()");
		handler.publish(logRecord);

//      /*
//       * прежде чем закрыть соединение, должны быть закрыты все сессии по
//       * данному соединению
//       */
		if ((!isConnected()) || (sessions > 0))
		{
			logRecord = new LogRecord(Level.FINEST, "Method exit");
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("Disconnect()");
			handler.publish(logRecord);
			return;
		}

//      /*
//       * соединение определ€етс€ контекстом
//       */
		if (contains(this))
		{
			disconnect();
		}

//      /*
//       * изменить состо€ние соединени€
//       */
		connection_state = CONNECTION_CLOSED;
		subcontextName = DEFAULT_SUBCONTEXT_NAME;
		remove(this);
		if (equals(getActiveConnection()))
			setActiveConnection(null);

		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("Disconnect()");
		handler.publish(logRecord);
	}

	public String toString()
	{
		return "JDBCConnectionInfo " + connectionManager.toString();
	}

	public String getServiceURL()
	{
		return connectionManager.toString();
	}

}