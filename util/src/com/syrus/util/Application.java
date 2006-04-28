/*
 * $Id: Application.java,v 1.12 2006/04/28 10:54:18 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @version $Revision: 1.12 $, $Date: 2006/04/28 10:54:18 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public final class Application {
	private static String applicationName;
	private static String internetAddress;

	private Application() {
		assert false;
	}

	public static void init(final String appName) {
		applicationName = appName;
		ApplicationProperties.init(applicationName);
		initializeLogging();
	}

	public static void init(final String appName, final String inetAddress) {
		applicationName = appName;
		internetAddress = inetAddress;
		ApplicationProperties.init(applicationName);
		initializeLogging();
	}

	private static void initializeLogging() {
		if (internetAddress == null)
			try {
				internetAddress = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				internetAddress = "INETADDR_UNKNOWN";
			}
		Log.setLogger(new ApplicationLogger(applicationName, internetAddress));
		Log.debugMessage(applicationName + " -- started", Log.DEBUGLEVEL01);
	}

	public static String getApplicationName() {
		return applicationName;
	}

	public static String getInternetAddress() {
		return internetAddress;
	}
}
