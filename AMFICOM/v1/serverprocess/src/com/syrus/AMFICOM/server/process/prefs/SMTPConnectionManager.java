/*
 * $Id: SMTPConnectionManager.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process.prefs;

import com.syrus.util.prefs.PreferencesManager;
import java.net.*;
import java.util.prefs.Preferences;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @author $Author: bass $
 * @module serverprocess
 *
 * @todo Password must be stored as a byte array. Provide a GUI for editing.
 */
public final class SMTPConnectionManager {
	private static final String EMPTY_SMTP_HOST = "";

	private static final String DEFAULT_SMTP_HOST;

	private static final int EMPTY_SMTP_PORT = 0;

	private static final int DEFAULT_SMTP_PORT = 25;

	private static final boolean DEFAULT_USE_AUTH = false;

	private static final boolean EMPTY_USE_AUTH = false;

	private static final String EMPTY_USERNAME = "";

	private static final String DEFAULT_USERNAME = System.getProperty("user.name", "");

	private static final String EMPTY_PASSWORD = "";

	private static final String DEFAULT_PASSWORD = "";

	private static final String EMPTY_RETURN_ADDRESS = "";

	private static final String DEFAULT_RETURN_ADDRESS;

	private static final String HOST_KEY_NAME = "host";

	private static final String PORT_KEY_NAME = "port";

	private static final String USE_AUTH_KEY_NAME = "useAuth";

	private static final String USERNAME_KEY_NAME = "username";

	private static final String PASSWORD_KEY_NAME = "password";

	private static final String RETURN_ADDRESS_KEY_NAME = "returnAddress";

	private static Preferences preferences;

	private SMTPConnectionManager() {
	}

	static {
		String defaultSMTPHost;
		try {
			defaultSMTPHost = InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException uhe) {
			defaultSMTPHost = "localhost";
		}
		DEFAULT_SMTP_HOST = defaultSMTPHost;
		DEFAULT_RETURN_ADDRESS = DEFAULT_USERNAME + '@' + DEFAULT_SMTP_HOST;
	}

	static {
		preferences = Preferences.userRoot().
			node(PreferencesManager.PREFERENCES_ROOT).node("util").
			node("connections").node("smtp");
		if (preferences.get(HOST_KEY_NAME, EMPTY_SMTP_HOST).equals(EMPTY_SMTP_HOST))
			preferences.put(HOST_KEY_NAME, DEFAULT_SMTP_HOST);
		if (preferences.getInt(PORT_KEY_NAME, EMPTY_SMTP_PORT) == EMPTY_SMTP_PORT)
			preferences.putInt(PORT_KEY_NAME, DEFAULT_SMTP_PORT);
		if (preferences.getBoolean(USE_AUTH_KEY_NAME, EMPTY_USE_AUTH) == EMPTY_USE_AUTH)
			preferences.putBoolean(USE_AUTH_KEY_NAME, DEFAULT_USE_AUTH);
		if (preferences.get(USERNAME_KEY_NAME, EMPTY_USERNAME).equals(EMPTY_USERNAME))
			preferences.put(USERNAME_KEY_NAME, DEFAULT_USERNAME);
		if (preferences.get(PASSWORD_KEY_NAME, EMPTY_PASSWORD).equals(EMPTY_PASSWORD))
			preferences.put(PASSWORD_KEY_NAME, DEFAULT_PASSWORD);
		if (preferences.get(RETURN_ADDRESS_KEY_NAME, EMPTY_RETURN_ADDRESS).equals(EMPTY_RETURN_ADDRESS))
			preferences.put(RETURN_ADDRESS_KEY_NAME, DEFAULT_RETURN_ADDRESS);
	}

	public static String getHost() {
		return preferences.get(HOST_KEY_NAME, DEFAULT_SMTP_HOST);
	}

	public static int getPort() {
		return preferences.getInt(PORT_KEY_NAME, DEFAULT_SMTP_PORT);
	}

	public static boolean getUseAuth() {
		return preferences.getBoolean(USE_AUTH_KEY_NAME, DEFAULT_USE_AUTH);
	}

	public static String getUsername() {
		return preferences.get(USERNAME_KEY_NAME, DEFAULT_USERNAME);
	}

	public static String getPassword() {
		return preferences.get(PASSWORD_KEY_NAME, DEFAULT_PASSWORD);
	}

	public static String getReturnAddress() {
		return preferences.get(RETURN_ADDRESS_KEY_NAME, DEFAULT_RETURN_ADDRESS);
	}
}
