package com.syrus.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ApplicationLogger extends StolenLogger {
	private static final String DEFAULTHOME = System.getProperty("user.home");
	private static final String DEFAULTLOGPATH = DEFAULTHOME + "/logs";


	public ApplicationLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void init_spec() {
		super.echoDebug = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.echoDebugKey, StolenLogger.DEFAULTLOGECHODEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.echoErrorKey, StolenLogger.DEFAULTLOGECHOERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.thisLevelOnlyKey, StolenLogger.DEFAULTLOGONLYTHISLEVEL)).booleanValue();
		super.logDebugLevel = ApplicationProperties.getInt(StolenLogger.logDebugLevelKey, StolenLogger.DEFAULTLOGDEBUGLEVEL);
		super.baseLogPath = ApplicationProperties.getString(StolenLogger.logPathKey, DEFAULTLOGPATH);
	}
}