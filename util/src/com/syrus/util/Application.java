package com.syrus.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Application {
	private static String applicationName;
	private static String internetAddress;

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

	public static String getInternetAddress() {
		return internetAddress;
	}
}