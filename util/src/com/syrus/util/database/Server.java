package com.syrus.util.database;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.syrus.util.Log;
import com.syrus.util.ServerLogger;

public class Server {
	private static String applicationName;
	private static String internetAddress;

	private Server() {
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
		Log.initialize(new ServerLogger(applicationName, internetAddress));
		Log.debugMessage(applicationName + " -- started", Log.DEBUGLEVEL01);
	}
}
