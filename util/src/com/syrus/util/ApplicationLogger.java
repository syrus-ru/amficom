package com.syrus.util;

public class ApplicationLogger extends StolenLogger {
	private static final String DEFAULTHOME = System.getProperty("user.home");
	private static final String DEFAULTLOGPATH = DEFAULTHOME + "/logs";


	public ApplicationLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void initSpec() {
		super.echoDebug = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.ECHO_DEBUG_KEY, StolenLogger.DEFAULTLOGECHODEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.ECHO_ERROR_KEY, StolenLogger.DEFAULTLOGECHOERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.THIS_LEVEL_ONLY_KEY, StolenLogger.DEFAULTLOGONLYTHISLEVEL)).booleanValue();
		super.logDebugLevel = ApplicationProperties.getInt(StolenLogger.LOG_DEBUG_LEVEL_KEY, StolenLogger.DEFAULTLOGDEBUGLEVEL);
		super.baseLogPath = ApplicationProperties.getString(StolenLogger.LOG_PATH_KEY, DEFAULTLOGPATH);
	}
}
