/*-
 * $Id: Server.java,v 1.5 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
public final class Server {
	private static String applicationName;
	private static String internetAddress;

	private Server() {
		assert false;
	}

	public static void init() {
		init("server");
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
				internetAddress = "INETADDR_UNKNOWN";
			}
		Log.initialize(new com.syrus.util.ServerLogger(applicationName, internetAddress));
		Log.debugMessage(applicationName + " -- started", Log.DEBUGLEVEL01);
	}
}
