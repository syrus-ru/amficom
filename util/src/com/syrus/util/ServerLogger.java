/*
 * $Id: ServerLogger.java,v 1.5 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import com.syrus.util.database.ServerProperties;

public class ServerLogger extends StolenLogger {
	private static final String DEFAULT_HOME = (System.getProperty("os.name").length() >= 7 && System.getProperty("os.name").toLowerCase().substring(0, 7).equals("windows"))?"D:\\amficom":"//home//amficom"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	private static final String DEFAULT_LOG_PATH = DEFAULT_HOME + "/logs"; //$NON-NLS-1$

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
