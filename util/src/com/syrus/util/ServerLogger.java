package com.syrus.util;

import com.syrus.util.database.ServerProperties;

public class ServerLogger extends StolenLogger {
	private static final String DEFAULTHOME = (System.getProperty("os.name").length() >= 7 && System.getProperty("os.name").toLowerCase().substring(0, 7).equals("windows"))?"D:\\amficom":"//home//amficom";
	private static final String DEFAULTLOGPATH = DEFAULTHOME + "/logs";

	public ServerLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void init_spec() {
		super.echoDebug = Boolean.valueOf(ServerProperties.getString(StolenLogger.echoDebugKey, StolenLogger.DEFAULTLOGECHODEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(ServerProperties.getString(StolenLogger.echoErrorKey, StolenLogger.DEFAULTLOGECHOERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(ServerProperties.getString(StolenLogger.thisLevelOnlyKey, StolenLogger.DEFAULTLOGONLYTHISLEVEL)).booleanValue();
		super.logDebugLevel = ServerProperties.getInt(StolenLogger.logDebugLevelKey, StolenLogger.DEFAULTLOGDEBUGLEVEL);
		super.baseLogPath = ServerProperties.getString(StolenLogger.logPathKey, DEFAULTLOGPATH);
	}
}
