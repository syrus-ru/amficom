/*
 * $Id: ConnectionInterface.java,v 1.3 2004/09/25 19:31:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.CORBA.AMFICOM;
import java.lang.Object;
import java.util.*;
import org.omg.CORBA.*;

/**
 * @version $Revision: 1.3 $, $Date: 2004/09/25 19:31:42 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public abstract class ConnectionInterface {
	private static final String DEFAULT_INSTANTIATING_CLASS_NAME = "com.syrus.AMFICOM.Client.General.RISDConnectionInfo";

	protected static ConnectionInterface instance = null;

	public abstract boolean isConnected();

	public abstract void setConnected(final boolean connected) throws UserException, SystemException;

	public static ConnectionInterface getInstance() {
		if (instance == null)
			synchronized (ConnectionInterface.class) {
				if (instance == null)
					try {
						Class clazz;
						ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
						String instantiatingClassName = System.getProperty("com.syrus.amficom.client.connection", DEFAULT_INSTANTIATING_CLASS_NAME);
						if (classLoader != null)
							clazz = Class.forName(instantiatingClassName, true, classLoader);
						else
							clazz = Class.forName(instantiatingClassName);
						clazz.getMethod("getInstance", new Class[0]).invoke(clazz, new Object[0]);
					} catch (Exception e) {
						System.out.println("Internal error while obtaining a connection instance... exiting.");
						e.printStackTrace();
						System.exit(-1);
					}
			}
		return instance;
	}

	public abstract AMFICOM getServer();

	public abstract String getServerName();

	/**
	 * @deprecated
	 */
	public static final int CONNECTION_CLOSED = 0;

	/**
	 * @deprecated
	 */
	public static final int CONNECTION_OPENED = 1;

	/**
	 * @deprecated
	 */
	private static Collection connections = new LinkedList();

	/**
	 * @deprecated
	 */
	private static ConnectionInterface connection;

	/**
	 * @deprecated
	 */
	public abstract void initialize();

	/**
	 * @deprecated
	 */
	public abstract void SetDefaults();

	/**
	 * @deprecated Use {@link #setConnected(boolean)} instead.
	 */
	public ConnectionInterface Connect() throws UserException, SystemException {
		setConnected(true);
		return this;
	}

	/**
	 * @deprecated Use {@link #setConnected(boolean)} instead.
	 */
	public void Disconnect() throws UserException, SystemException {
		setConnected(false);
	}

	/**
	 * @deprecated
	 */
	public abstract void setObjectName(String on);

	/**
	 * @deprecated
	 */
	public abstract String getObjectName();

	/**
	 * @deprecated Use {@link #getServerName()} instead.
	 */
	public String getServiceURL() {
		return getServerName();
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setServiceURL(String serviceUrl) {
	}

	/**
	 * @deprecated Use {@link #getServerName()} instead.
	 */
	public String getServerIP() {
		return getServerName();
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setServerIP(String serverIp) {
	}

	/**
	 * @deprecated
	 */
	public abstract void setTCPport(String p);

	/**
	 * @deprecated
	 */
	public abstract String getTCPport();

	/**
	 * @deprecated
	 */
	public abstract void setSID(String sid);

	/**
	 * @deprecated
	 */
	public abstract String getSID();

	/**
	 * @deprecated
	 */
	public abstract void setUser(String u);

	/**
	 * @deprecated
	 */
	public abstract String getUser();

	/**
	 * @deprecated
	 */
	public abstract void setPassword(String p);

	/**
	 * @deprecated
	 */
	public abstract String getPassword();

	/**
	 * @deprecated
	 */
	public static ConnectionInterface Connect(ConnectionInterface ci) throws UserException, SystemException {
		if(ci == null)
			return null;
		return ci.Connect();
	}

	/**
	 * @deprecated
	 */
	public static void Disconnect(ConnectionInterface ci) throws UserException, SystemException {
		if (ci == null)
			return;
		ci.Disconnect();
	}

	/**
	 * @deprecated Does nothing.
	 */
	public static void setActiveConnection(ConnectionInterface connection) {
	}

	/**
	 * @deprecated Use {@link #getInstance()} instead.
	 */
	public static ConnectionInterface getActiveConnection() {
		return getInstance();
	}

	/**
	 * @deprecated
	 */
	public static void add(ConnectionInterface ci) {
		connections.add(ci);
	}

	/**
	 * @deprecated
	 */
	public static void remove(ConnectionInterface ci) {
		connections.remove(ci);
	}

	/**
	 * @deprecated
	 */
	public static boolean contains(ConnectionInterface ci) {
		return connections.contains(ci);
	}

	/**
	 * @deprecated
	 */
	public static void Log(Object obj) {
		System.out.println(obj);
	}
}
