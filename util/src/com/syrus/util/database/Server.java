/*
 * $Id: Server.java,v 1.3 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.syrus.util.Log;
import com.syrus.util.ServerLogger;

public class Server {
	private static String applicationName;
	private static String internetAddress;

	private Server() {
		assert false;
	}

	public static void init() {
		init("server"); //$NON-NLS-1$
	}

	public static void init(String appName) {
		applicationName = appName;
		initializeLogging();
	}

	public static void init(String appName, String inetAddress) {
		applicationName = appName;
		internetAddress = inetAddress;
		initializeLogging();
	}

	private static void initializeLogging() {
		if (internetAddress == null)
			try {
				internetAddress = InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException e) {
				internetAddress = "INETADDR_UNKNOWN"; //$NON-NLS-1$
			}
		Log.initialize(new ServerLogger(applicationName, internetAddress));
		Log.debugMessage(applicationName + " -- started", Log.DEBUGLEVEL01); //$NON-NLS-1$
	}
}
