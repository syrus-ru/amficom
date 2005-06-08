/*
 * $Id: ServerLogger.java,v 1.7 2005/06/08 13:49:06 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

/**
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2005/06/08 13:49:06 $
 * @deprecated
 * @module util
 */
public final class ServerLogger extends AbstractLogger {
	public ServerLogger(String appName, String hostName) {
		super(appName, hostName);
	}

	void initSpec() {
		super.echoDebug = Boolean.valueOf(com.syrus.util.database.ServerProperties.getString(KEY_ECHO_DEBUG, DEFAULT_LOG_ECHO_DEBUG)).booleanValue();
		super.echoError = Boolean.valueOf(com.syrus.util.database.ServerProperties.getString(KEY_ECHO_ERROR, DEFAULT_LOG_ECHO_ERROR)).booleanValue();
		super.thisLevelOnly = Boolean.valueOf(com.syrus.util.database.ServerProperties.getString(KEY_THIS_LEVEL_ONLY, DEFAULT_LOG_ONLY_THIS_LEVEL)).booleanValue();
		super.logDebugLevel = com.syrus.util.database.ServerProperties.getInt(KEY_LOG_DEBUG_LEVEL, DEFAULT_LOG_DEBUG_LEVEL);
		super.baseLogPath = com.syrus.util.database.ServerProperties.getString(KEY_LOG_PATH, DEFAULT_LOG_PATH);
	}
}
