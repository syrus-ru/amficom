/*
 * $Id: ApplicationLogger.java,v 1.7 2005/06/08 13:01:17 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.File;

public class ApplicationLogger extends StolenLogger {
	private static final String DEFAULT_HOME = System.getProperty("user.home");
	private static final String DEFAULT_LOG_PATH = DEFAULT_HOME + File.separator + "logs";


	public ApplicationLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void initSpec() {
		super.echoDebug = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.KEY_ECHO_DEBUG, StolenLogger.DEFAULT_LOG_ECHO_DEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.KEY_ECHO_ERROR, StolenLogger.DEFAULT_LOG_ECHO_ERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(ApplicationProperties.getString(StolenLogger.KEY_THIS_LEVEL_ONLY, StolenLogger.DEFAULT_LOG_ONLY_THIS_LEVEL)).booleanValue();
		super.logDebugLevel = ApplicationProperties.getInt(StolenLogger.KEY_LOG_DEBUG_LEVEL, StolenLogger.DEFAULT_LOG_DEBUG_LEVEL);
		super.baseLogPath = ApplicationProperties.getString(StolenLogger.KEY_LOG_PATH, DEFAULT_LOG_PATH);
	}
}
