/*
 * $Id: SessionInterface.java,v 1.6 2004/11/02 08:06:35 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.CORBA.Admin.AccessIdentity_Transferable;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import java.util.Vector;

/**
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2004/11/02 08:06:35 $
 * @module generalclient_v1
 */
public abstract class SessionInterface {
	public abstract AccessIdentity_Transferable getAccessIdentity();

	public abstract AccessIdentifier_Transferable getAccessIdentifier();

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

	/**
	 * @deprecated
	 */
	public abstract String getUserId();

	/**
	 * @deprecated
	 */
	public abstract void setDomainId(final String domainId);

	/**
	 * @deprecated
	 */
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
