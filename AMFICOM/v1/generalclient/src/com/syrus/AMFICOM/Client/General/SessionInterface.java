/*
 * $Id: SessionInterface.java,v 1.4 2004/10/19 13:45:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General;

import java.util.Vector;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/10/19 13:45:52 $
 * @module generalclient_v1
 */
public abstract class SessionInterface {
	public static final int SESSION_CLOSED = 0;	// вспомогательные константы
	public static final int SESSION_OPENED = 1;	// состояния сессии

	private static Vector sessions = new Vector();	// открытые сессии

	private static SessionInterface active_session;	// активная сессия

	public void initialize() {
	}

	// открыть сессию с установленными для нее параметрами
	public abstract SessionInterface OpenSession();
	// закрыть сессию
	public abstract void CloseSession();

	public void SetDefaults() {
	}

	// есть ли открытая сессия
	public abstract boolean isOpened();

	public abstract void setUser(String u);
	public abstract String getUser();
	public abstract void setPassword(String p);
	public abstract String getPassword();
	public abstract void setLogonTime(long t);
	public abstract long getLogonTime();

	public abstract ConnectionInterface getConnectionInterface();

	public abstract String getUserId();

	public abstract void setDomainId(String domain_id);
	public abstract String getDomainId();

	public static void CloseSession(SessionInterface si)
	{
		if(si == null)
			return;
		si.CloseSession();
	}

	public static void setActiveSession(SessionInterface si)
	{
		if(si != null)
			active_session = si;
	}

	public static SessionInterface getActiveSession()
	{
		return active_session;
	}

	public static void add(SessionInterface si)
	{
		sessions.add(si);
	}

	public static void remove(SessionInterface si)
	{
		sessions.remove(si);
	}

	public static boolean contains(SessionInterface si)
	{
		return sessions.contains(si);
	}

	public static boolean isEmpty()
	{
		return sessions.isEmpty();
	}

	public static void Log(String s)
	{
		System.out.println (s);
	}

	public static void Log(Object s)
	{
		System.out.println (s.toString());
	}
}
