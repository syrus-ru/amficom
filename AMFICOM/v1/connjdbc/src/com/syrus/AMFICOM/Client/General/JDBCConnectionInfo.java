package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.server.*;
import com.syrus.AMFICOM.server.Object.*;
import java.util.logging.*;

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
			server = new AMFICOMServer();

//              /*
//               * если соединение установлено (не произошло исключения)
//               * то инициализируем переменную состояние как "есть соединение",
//               * запоминаем время соединения, обнуляем количество сессий для данного
//               * соединения
//               */
			connection_state = CONNECTION_OPENED;
			ConnectTime = System.currentTimeMillis();
			sessions = 0;
			add(this);

//              /*
//               * соединение становится активным соединением
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
	 * Разорвать соединение с сервером.
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
//       * соединение определяется контекстом
//       */
		if (contains(this))
		{
			disconnect();
		}

//      /*
//       * изменить состояние соединения
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