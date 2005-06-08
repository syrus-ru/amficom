/*
 * $Id: Application.java,v 1.6 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Application {
	private static String applicationName;
	private static String internetAddress;

	private Application() {
		assert false;
	}

	public static void init(String appName) {
		applicationName = appName;
		ApplicationProperties.init(applicationName);
		initializeLogging();
	}

	public static void init(String appName, String inetAddress) {
		applicationName = appName;
		internetAddress = inetAddress;
		ApplicationProperties.init(applicationName);
		initializeLogging();
	}

	private static void initializeLogging() {
		if (internetAddress == null)
			try {
				internetAddress = InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException e) {
				internetAddress = "INETADDR_UNKNOWN";
			}
		Log.initialize(new ApplicationLogger(applicationName, internetAddress));
		Log.debugMessage(applicationName + " -- started", Log.DEBUGLEVEL01);
	}

	public static String getApplicationName () {
		return applicationName;
	}
	
	public static String getInternetAddress() {
		return internetAddress;
	}
}
