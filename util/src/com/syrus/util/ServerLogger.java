package com.syrus.util;

import com.syrus.util.database.ServerProperties;

public class ServerLogger extends StolenLogger {
	private static final String DEFAULT_HOME = (System.getProperty("os.name").length() >= 7 && System.getProperty("os.name").toLowerCase().substring(0, 7).equals("windows"))?"D:\\amficom":"//home//amficom";
	private static final String DEFAULT_LOG_PATH = DEFAULT_HOME + "/logs";

	public ServerLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void initSpec() {
		super.echoDebug = Boolean.valueOf(ServerProperties.getString(StolenLogger.KEY_ECHO_DEBUG, StolenLogger.DEFAULT_LOG_ECHO_DEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(ServerProperties.getString(StolenLogger.KEY_ECHO_ERROR, StolenLogger.DEFAULT_LOG_ECHO_ERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(ServerProperties.getString(StolenLogger.KEY_THIS_LEVEL_ONLY, StolenLogger.DEFAULT_LOG_ONLY_THIS_LEVEL)).booleanValue();
		super.logDebugLevel = ServerProperties.getInt(StolenLogger.KEY_LOG_DEBUG_LEVEL, StolenLogger.DEFAULT_LOG_DEBUG_LEVEL);
		super.baseLogPath = ServerProperties.getString(StolenLogger.KEY_LOG_PATH, DEFAULT_LOG_PATH);
	}
}
