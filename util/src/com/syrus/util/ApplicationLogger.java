/*
 * $Id: ApplicationLogger.java,v 1.9 2005/06/16 10:48:15 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

public final class ApplicationLogger extends AbstractLogger {
	public ApplicationLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void initSpec() {
		super.echoDebug = Boolean.valueOf(ApplicationProperties.getString(KEY_ECHO_DEBUG, DEFAULT_LOG_ECHO_DEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(ApplicationProperties.getString(KEY_ECHO_ERROR, DEFAULT_LOG_ECHO_ERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(ApplicationProperties.getString(KEY_THIS_LEVEL_ONLY, DEFAULT_LOG_ONLY_THIS_LEVEL)).booleanValue();
		super.logDebugLevel = ApplicationProperties.getInt(KEY_LOG_DEBUG_LEVEL, DEFAULT_LOG_DEBUG_LEVEL);
		super.baseLogPath = ApplicationProperties.getString(KEY_LOG_PATH, DEFAULT_LOG_PATH);
		System.out.println("ApplicationLogger.initSpec | logDebugLevel is " + super.logDebugLevel);
	}
}
