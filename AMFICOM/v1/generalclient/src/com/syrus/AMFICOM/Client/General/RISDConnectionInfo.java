//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Информация соединения с сервером системы                   * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 22 jun 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\ConnectionInfo.java                            * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.io.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.naming.*;
import oracle.aurora.AuroraServices.*;
import oracle.aurora.client.*;
import oracle.aurora.jndi.sess_iiop.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/07/05 05:49:08 $
 * @author $Author: bass $
 */
public class RISDConnectionInfo extends ConnectionInterface {
	public static final boolean DEBUG = true;

	/**
	 * Инициализационный файл.
	 */
	private static IniFile iniFile;

	/**
	 * Имя инициализационного файла.
	 */
	private static final String iniFileName = "Connection.properties";

	/*
	 * значения необходимых для соединения с сервером переменных по умолчанию
	 */
	public static final String DEFAULT_objectName = "/AMFICOM/AMFICOM";
	public static final String DEFAULT_user = "AMFICOM";
	public static final String DEFAULT_password = "amficom";
	public static final String DEFAULT_SUBCONTEXT_NAME = ":default";

	/**
	 * Серверный объект.
	 */
	public AMFICOM server;

	/**
	 * Контекст соединения с сервером.
	 */
	private Context ic;

	protected ServiceURL serviceURL = null;

	/*
	 * переменные соединения с сервером
	 */
	/**
	 * Имя серверного объекта.
	 *
	 * @see #getObjectName()
	 * @see #setObjectName(java.lang.String)
	 */
	protected String objectName = "";

	/**
	 * Имя пользователя для доступа к серверному
	 * объекту.
	 *
	 * @see #getUser()
	 * @see #setUser(java.lang.String)
	 */
	protected String user = "";

	/**
	 * Пароль доступа к серверному объекту.
	 *
	 * @see #getPassword()
	 * @see #setPassword(java.lang.String)
	 */
	protected String password = "";

	/**
	 * Время подсоединения к серверу.
	 */
	public long ConnectTime;

	/**
	 * Число открытых сессий по данному
	 * соединению.
	 */
	public int sessions;

	/**
	 * Индикатор наличия/отсутствия связи.
	 */
	public int connection_state = CONNECTION_CLOSED;

	private SessionCtx sessionCtx;

	private Handler handler;

	/**
	 * Session identifier.
	 * Уникальный идентификатор сессии.
	 *
	 * @see #getSubcontextName()
	 * @bug All access attempts to this field must be synchronized.
	 */
	protected String subcontextName = DEFAULT_SUBCONTEXT_NAME;

	/**
	 * Session timeout in seconds.
	 * Время жизни сессии в секундах.
	 *
	 * @see #getSessionTimeout()
	 * @see #setSessionTimeout(int)
	 */
	protected int sessionTimeout;

	/**
	 * Static initializer. Since iniFile is a static field, no need to recreate
	 * it when any new instance is created.
	 */
	static {
		if (DEBUG)
			System.out.print("Reading .ini file " + iniFileName + "... ");
		try {
			iniFile = new IniFile(iniFileName);
			if (DEBUG)
				System.out.println("success.");
		} catch (IOException ioe) {
			iniFile = null;
			if (DEBUG)
				System.out.println("failed.");
		}
	}

	/**
	 * Базовый конструктор без параметров.
	 */
	public RISDConnectionInfo() {
		handler = new ConsoleHandler();
		handler.setLevel(Level.CONFIG);
		/*
		 * установить начальные значения параметров соединения
		 */
		initialize();
	}

	/**
	 * Инициализация параметров соединения.
	 * Параметры берутся из файла настроек или по умолчанию.
	 */
	public void initialize() {
		/*
		 * загрузить параметры из файла настроек
		 */
		if (iniFile == null)
			/*
			 * если ошибка открытия файла настроек, то установить значения
			 * по умолчанию
			 */
			SetDefaults();
		else {
			/*
			 * если файл настроек открыт, то вынуть из него значения параметров
			 */
			serviceURL = new ServiceURL(iniFile.getValue("serviceURL"));
			objectName = iniFile.getValue("objectName");
			user = iniFile.getValue("user");
			password = iniFile.getValue("password");
		}
	}

	/**
	 * @bug <code>server</code>, <code>handler</code>, <code>ic</code> and
	 *      <code>sessionCtx</code> are shared references.
	 */
	protected Object clone() throws CloneNotSupportedException {
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("clone()");
		handler.publish(logRecord);

		RISDConnectionInfo connectionInfo = null;
		try {
			connectionInfo = (RISDConnectionInfo) (super.clone());
			/*
			 * Primitive types are copied by value.
			 */
			connectionInfo.connection_state = connection_state;
			connectionInfo.sessionTimeout = sessionTimeout;
			connectionInfo.sessions = sessions;
			connectionInfo.ConnectTime = ConnectTime;
			/*
			 * ServiceURL implements Cloneable
			 */
			connectionInfo.serviceURL = (ServiceURL) (serviceURL.clone());
			/*
			 * This is the way strings are duplicated.
			 */
			connectionInfo.objectName = new String(objectName);
			connectionInfo.password = new String(password);
			connectionInfo.subcontextName = new String(subcontextName);
			connectionInfo.user = new String(user);

			connectionInfo.server = server;
			connectionInfo.handler = handler;
			connectionInfo.ic = ic;
			connectionInfo.sessionCtx = sessionCtx;
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}

		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("clone()");
		handler.publish(logRecord);

		return connectionInfo;
	}

	/**
	 * Установить значения параметров по умолчанию.
	 */
	public void SetDefaults() {
		objectName = DEFAULT_objectName;
		serviceURL = new ServiceURL();
		user = DEFAULT_user;
		password = DEFAULT_password;
	}

	/**
	 * Установление соединения с явным указанием параметров соединения.
	 */
	public static ConnectionInterface Connect(String objectName, String serviceURL, String user, String password) {
		RISDConnectionInfo ci = new RISDConnectionInfo();
		ci.serviceURL = new ServiceURL(serviceURL);
		ci.objectName = new String(objectName);
		ci.user = new String(user);
		ci.password = new String(password);
		return ci.Connect();
	}

	public ConnectionInterface Connect() {
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method entry");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("Connect()");
		handler.publish(logRecord);

		if (!isConnected())
			try {
				Hashtable env = new Hashtable();
				env.put(Context.URL_PKG_PREFIXES, "oracle.aurora.jndi");

				ic = new InitialContext(env);

				/*
				 * Get a SessionCtx that represents a database instance
				 */
				ServiceCtx serviceCtx = (ServiceCtx) ic.lookup(serviceURL.toString());

				/*
				 * Create and authenticate a named session in the instance.
				 * If we log in and out too often (say, once a second) with the
				 * same session id, the old session is not yet destroyed.
				 * 
				 * However, instead of org,omg.CORBA.NO_PERMISSION, we
				 * receive org.omg.CORBA.COMM_FAILURE or org.omg.CORBA.NO_IMPLEMENT.
				 *
				 * The correct choice is to generate a unique session id for
				 * every login process. A combination of host name and/or user name
				 * and local system time seems a reasonable one.
				 */
				fillSubcontextName(this);
				sessionCtx = (SessionCtx) serviceCtx.createSubcontext(subcontextName);
				logRecord = new LogRecord(Level.CONFIG, "Created subcontext: " + subcontextName);
				logRecord.setSourceClassName(getClass().getName());
				logRecord.setSourceMethodName("Connect()");
				handler.publish(logRecord);

				LoginServer loginServer = (LoginServer) sessionCtx.activate("/etc/login");
				Login login = new Login(loginServer);
				login.authenticate(user, password, null);
				logRecord = new LogRecord(Level.CONFIG, "Authenticated user: " + user + " with password: <hidden>");
				logRecord.setSourceClassName(getClass().getName());
				logRecord.setSourceMethodName("Connect()");
				handler.publish(logRecord);

				/* 
				 * Activate the server object in the current session.
				 */
				server = (AMFICOM) sessionCtx.activate(objectName);

				/*
				 * если соединение установлено (не произошло исключения)
				 * то инициализируем переменную состояние как "есть соединение",
				 * запоминаем время соединения, обнуляем количество сессий для данного
				 * соединения
				 */
				connection_state = CONNECTION_OPENED;
				ConnectTime = System.currentTimeMillis();
				sessions = 0;
				add(this);

				/*
				 * соединение становится активным соединением
				 */
				setActiveConnection(this);
			} catch (NamingException ne) {
				logRecord = new LogRecord(Level.SEVERE, ne.toString(true));
				logRecord.setSourceClassName(getClass().getName());
				logRecord.setSourceMethodName("Connect()");
				logRecord.setThrown(ne);
				handler.publish(logRecord);
			} catch (Exception e) {
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
	public void Disconnect() {
		LogRecord logRecord;
		logRecord = new LogRecord(Level.FINEST, "Method exit");
		logRecord.setSourceClassName(getClass().getName());
		logRecord.setSourceMethodName("Disconnect()");
		handler.publish(logRecord);

		/*
		 * прежде чем закрыть соединение, должны быть закрыты все сессии по
		 * данному соединению
		 */
		if ((!isConnected()) || (sessions > 0)) {
			logRecord = new LogRecord(Level.FINEST, "Method exit");
			logRecord.setSourceClassName(getClass().getName());
			logRecord.setSourceMethodName("Disconnect()");
			handler.publish(logRecord);
			return;
		}

		/*
		 * соединение определяется контекстом
		 */
		if (contains(this)) {
			server._release();
//			ic.unbind(name);
			try {
				LogoutServer logoutServer = (LogoutServer) sessionCtx.activate("/etc/logout");
				logoutServer.logout();
			} catch (NamingException ne) {
				logRecord = new LogRecord(Level.SEVERE, ne.toString(true));
				logRecord.setSourceClassName(getClass().getName());
				logRecord.setSourceMethodName("Disconnect()");
				logRecord.setThrown(ne);
				handler.publish(logRecord);
			} catch (Exception e) {
				logRecord = new LogRecord(Level.SEVERE, e.toString());
				logRecord.setSourceClassName(getClass().getName());
				logRecord.setSourceMethodName("Disconnect()");
				logRecord.setThrown(e);
				handler.publish(logRecord);
			}
		}

		/*
		 * изменить состояние соединения
		 */
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

	/**
	 * Generates a unique session identifier.
	 *
	 * @param connectionInfo The <code>RISDConnectionInfo</code> instance for
	 *        which a session identifier should be generated.
	 * @todo incorporate java.util.Random and/or hostname, host address and user name.
	 */
	private static synchronized void fillSubcontextName(RISDConnectionInfo connectionInfo) {
		connectionInfo.subcontextName = ':' + String.valueOf(System.currentTimeMillis());
	}

	/**
	 * Returns the session identifier.
	 * Возвращает уникальный иденитификатор сессии.
	 * 
	 * @return the session identifier.
	 * @see #subcontextName
	 */
	public String getSubcontextName() {
		return subcontextName;
	}

	/** 
	 * Returns the session timeout of the current session.
	 * Возвращает время жизни текущей сессии.
	 *
	 * @return the session timeout of the current session.
	 * @see #sessionTimeout
	 * @see #setSessionTimeout(int)
	 */
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	/** 
	 * Sets the session timeout of the current session.
	 * Устанавливает время жизни текущей сессии.
	 *
	 * @param sessionTimeout the new session timeout.
	 * @see #sessionTimeout
	 * @see #getSessionTimeout()
	 */
	public void setSessionTimeout(int sessionTimeout) {
		if (this.sessionTimeout == sessionTimeout)
			return;
		try {
//			if (DEBUG)
//				System.out.print("Setting session timeout to " + sessionTimeout + "... ");
			Timeout timeout = (Timeout) ic.lookup(serviceURL.toString() + "/etc/timeout");
			timeout.setTimeout(sessionTimeout);
			this.sessionTimeout = sessionTimeout;
//			if (DEBUG)
//				System.out.println("success.");
		} catch (NamingException ne) {
//			if (DEBUG) {
//				System.out.println("failed.");
//				System.out.println("Error setting session timeout due to NamingException: " + ne.toString(true));
//				ne.printStackTrace(System.out);
//			}
		} catch (Exception e) {
//			if (DEBUG) {
//				System.out.println("failed.");
//				System.out.println("Error setting session timeout due to " + e.getClass().getName() + ": " + e.getLocalizedMessage());
//				e.printStackTrace(System.out);
//			}
		}
	}

	public String getServicePrefix() {
		return serviceURL.getProtocol();
	}

	public void setServicePrefix(String servicePrefix) {
		serviceURL.setProtocol(servicePrefix);
	}

	public String getServerIP() {
		return serviceURL.getHost();
	}

	public void setServerIP(String serverIP) {
		serviceURL.setHost(serverIP);
	}

	public String getTCPport() {
		return serviceURL.getPort();
	}

	public void setTCPport(String TCPport) {
		serviceURL.setPort(TCPport);
	}

	public String getSID() {
		return serviceURL.getSid();
	}

	public void setSID(String SID) {
		serviceURL.setSid(SID);
	}

	public String getServiceURL() {
		return serviceURL.toString();
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = new ServiceURL(serviceURL);
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * Есть ли соединение с сервером.
	 */
	public boolean isConnected() {
		return (connection_state == CONNECTION_OPENED);
	}

	public void setConnected(boolean connected) {
		if (isConnected() && (!connected))
			Disconnect();
		else if ((!isConnected()) && connected)
			Connect();
	}

	public String toString() {
		return "RISDConnectionInfo service " + getServiceURL() + " server object " + objectName + " user " + getUser() + " connected " + isConnected();
	}

	public static class ServiceURL implements Cloneable {
		public static final String DEFAULT_PROTOCOL = "sess_iiop";
//		public static final String DEFAULT_HOST = "amficom";
		public static final String DEFAULT_HOST = "research";
		public static final String DEFAULT_PORT = "2481";
//		public static final String DEFAULT_SID = "demo2";
		public static final String DEFAULT_SID = "research";
		public static final String DEFAULT_SERVICE_URL = DEFAULT_PROTOCOL + "://" + DEFAULT_HOST + ':' + DEFAULT_PORT + ':' + DEFAULT_SID;

		/** 
		 * Holds value of property protocol.
		 */
		protected String protocol;

		/** 
		 * Holds value of property host.
		 */
		protected String host;

		/** 
		 * Holds value of property port.
		 */
		protected String port;

		/**
		 * Holds value of property sid.
		 */
		protected String sid;

		public ServiceURL() {
			this(DEFAULT_PROTOCOL, DEFAULT_HOST, DEFAULT_PORT, DEFAULT_SID);
		}

		public ServiceURL(String host, String port, String sid) {
			this(DEFAULT_PROTOCOL, host, port, sid);
		}

		public ServiceURL(String protocol, String host, String port, String sid) {
			this.protocol = protocol;
			this.host = host;
			this.port = port;
			this.sid = sid;
		}

		/**
		 * @todo Add error-handling while parsing the string.
		 */
		public ServiceURL(String str) {
			if (str == null) {
				protocol = DEFAULT_PROTOCOL;
				host = DEFAULT_HOST;
				port = DEFAULT_PORT;
				sid = DEFAULT_SID;
				return;
			}
			int i = str.indexOf(':');
			protocol = str.substring(0, i);
			/*
			 * A colon and a double slash.
			 */
			str = str.substring(i + 3);

			i = str.indexOf(':');
			host = str.substring(0, i);
			str = str.substring(i + 1);

			i = str.indexOf(':');
			port = str.substring(0, i);
			sid = str.substring(i + 1);
		}

		/** 
		 * Getter for property protocol.
		 * @return Value of property protocol.
		 */
		public String getProtocol() {
			return protocol;
		}

		/** 
		 * Setter for property protocol.
		 * @param protocol New value of property protocol.
		 */
		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		/**
		 * Getter for property host.
		 * @return Value of property host.
		 */
		public String getHost() {
			return host;
		}

		/**
		 * Setter for property host.
		 * @param host New value of property host.
		 */
		public void setHost(String host) {
			this.host = host;
		}

		/**
		 * Getter for property port.
		 * @return Value of property port.
		 */
		public String getPort() {
			return port;
		}

		/**
		 * Setter for property port.
		 * @param port New value of property port.
		 */
		public void setPort(String port) {
			this.port = port;
		}

		/**
		 * Getter for property sid.
		 * @return Value of property sid.
		 */
		public String getSid() {
			return sid;
		}

		/**
		 * Setter for property sid.
		 * @param sid New value of property sid.
		 */
		public void setSid(String sid) {
			this.sid = sid;
		}

		protected Object clone() throws CloneNotSupportedException {
			ServiceURL serviceURL = null;
			try {
				serviceURL = (ServiceURL) (super.clone());
				serviceURL.host = new String(host);
				serviceURL.port = new String(port);
				serviceURL.protocol = new String(protocol);
				serviceURL.sid = new String(sid);
			} catch (CloneNotSupportedException cnse) {
				cnse.printStackTrace();
			}
			return serviceURL;
		}

		public String toString() {
			return (protocol + "://" + host + ':' + port + ':' + sid);
		}
	}
}
