package com.syrus.util.prefs;

import java.net.*;
import java.util.prefs.Preferences;

/**
 * @version $Revision: 1.1 $, $Date: 2004/05/06 11:48:10 $
 * @author $Author: bass $
 * @module util
 */
public final class SMTPConnectionManager {
	private static final String EMPTY_SMTP_HOST = "";

	private static final String DEFAULT_SMTP_HOST;

	private static final int EMPTY_SMTP_PORT = 0;

	private static final int DEFAULT_SMTP_PORT = 25;

	private static final String HOST_KEY_NAME = "host";

	private static final String PORT_KEY_NAME = "port";

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
	}

	static {
		preferences = Preferences.userRoot().
			node(PreferencesManager.PREFERENCES_ROOT).node("util").
			node("connections").node("smtp");
		if (preferences.get(HOST_KEY_NAME, EMPTY_SMTP_HOST).equals(EMPTY_SMTP_HOST))
			preferences.put(HOST_KEY_NAME, DEFAULT_SMTP_HOST);
		if (preferences.getInt(PORT_KEY_NAME, EMPTY_SMTP_PORT) == EMPTY_SMTP_PORT)
			preferences.putInt(PORT_KEY_NAME, DEFAULT_SMTP_PORT);
	}

	public static String getHost() {
		return preferences.get(HOST_KEY_NAME, DEFAULT_SMTP_HOST);
	}

	public static int getPort() {
		return preferences.getInt(PORT_KEY_NAME, DEFAULT_SMTP_PORT);
	}
}
