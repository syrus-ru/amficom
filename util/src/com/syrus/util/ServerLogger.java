package com.syrus.util;

import com.syrus.util.database.ServerProperties;

public class ServerLogger extends StolenLogger {
	private static final String DEFAULTHOME = (System.getProperty("os.name").length() >= 7 && System.getProperty("os.name").toLowerCase().substring(0, 7).equals("windows"))?"D:\\amficom":"//home//amficom";
	private static final String DEFAULTLOGPATH = DEFAULTHOME + "/logs";

	public ServerLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void initSpec() {
		super.echoDebug = Boolean.valueOf(ServerProperties.getString(StolenLogger.ECHO_DEBUG_KEY, StolenLogger.DEFAULTLOGECHODEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(ServerProperties.getString(StolenLogger.ECHO_ERROR_KEY, StolenLogger.DEFAULTLOGECHOERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(ServerProperties.getString(StolenLogger.THIS_LEVEL_ONLY_KEY, StolenLogger.DEFAULTLOGONLYTHISLEVEL)).booleanValue();
		super.logDebugLevel = ServerProperties.getInt(StolenLogger.LOG_DEBUG_LEVEL_KEY, StolenLogger.DEFAULTLOGDEBUGLEVEL);
		super.baseLogPath = ServerProperties.getString(StolenLogger.LOG_PATH_KEY, DEFAULTLOGPATH);
	}
}
