/*
 * $Id: IIOPConnectionManager.java,v 1.2 2004/06/01 14:09:15 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.util.prefs;

import java.net.*;
import java.util.prefs.Preferences;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/01 14:09:15 $
 * @author $Author: bass $
 * @module util
 */
public final class IIOPConnectionManager {
	private static final String EMPTY_ORB_INITIAL_HOST = "";

	private static final String DEFAULT_ORB_INITIAL_HOST;

	private static final int EMPTY_ORB_INITIAL_PORT = 0;

	private static final int DEFAULT_ORB_INITIAL_PORT = 1050;

	private static final String EMPTY_CLIENT_ID = "";

	private static final String DEFAULT_CLIENT_ID;

	private static final String ORB_INITIAL_HOST_KEY_NAME = "org.omg.CORBA.ORBInitialHost";

	private static final String ORB_INITIAL_PORT_KEY_NAME = "org.omg.CORBA.ORBInitialPort";

	private static final String CLIENT_ID_KEY_NAME = "clientId";

	private static Preferences preferences;

	private IIOPConnectionManager() {
	}

	static {
		String defaultORBInitialHost;
		String defaultClientId;
		try {
			String canonicalLocalHostName = InetAddress.getLocalHost().getCanonicalHostName();
			defaultORBInitialHost = canonicalLocalHostName;
			defaultClientId = canonicalLocalHostName;
		} catch (UnknownHostException uhe) {
			defaultORBInitialHost = "localhost";
			defaultClientId = "localhost";
		}
		DEFAULT_ORB_INITIAL_HOST = defaultORBInitialHost;
		DEFAULT_CLIENT_ID = defaultClientId;
	}

	static {
		preferences = Preferences.userRoot().
			node(PreferencesManager.PREFERENCES_ROOT).node("util").
			node("connections").node("iiop");
		if (preferences.get(ORB_INITIAL_HOST_KEY_NAME, EMPTY_ORB_INITIAL_HOST).equals(EMPTY_ORB_INITIAL_HOST))
			preferences.put(ORB_INITIAL_HOST_KEY_NAME, DEFAULT_ORB_INITIAL_HOST);
		if (preferences.getInt(ORB_INITIAL_PORT_KEY_NAME, EMPTY_ORB_INITIAL_PORT) == EMPTY_ORB_INITIAL_PORT)
			preferences.putInt(ORB_INITIAL_PORT_KEY_NAME, DEFAULT_ORB_INITIAL_PORT);
		if (preferences.get(CLIENT_ID_KEY_NAME, EMPTY_CLIENT_ID).equals(EMPTY_CLIENT_ID))
			preferences.put(CLIENT_ID_KEY_NAME, DEFAULT_CLIENT_ID);
	}

	public static String getORBInitialHost() {
		return preferences.get(ORB_INITIAL_HOST_KEY_NAME, DEFAULT_ORB_INITIAL_HOST);
	}

	public static int getORBInitialPort() {
		return preferences.getInt(ORB_INITIAL_PORT_KEY_NAME, DEFAULT_ORB_INITIAL_PORT);
	}

	public static String getClientId() {
		return preferences.get(CLIENT_ID_KEY_NAME, DEFAULT_CLIENT_ID);
	}
}
