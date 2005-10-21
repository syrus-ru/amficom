/*
 * $Id: ApplicationLogger.java,v 1.13 2005/10/21 15:09:07 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/21 15:09:07 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
final class ApplicationLogger extends AbstractLogger {
	public ApplicationLogger(final String appName, final String hostName) {
		super(appName, hostName);
	}

	@Override
	void initSpec() {
		this.echoDebug = ApplicationProperties.getBoolean(KEY_ECHO_DEBUG, DEFAULT_ECHO_DEBUG);
		this.echoError = ApplicationProperties.getBoolean(KEY_ECHO_ERROR, DEFAULT_ECHO_ERROR);
		this.thisLevelOnly = ApplicationProperties.getBoolean(KEY_LOG_ONLY_THIS_LEVEL, DEFAULT_LOG_ONLY_THIS_LEVEL);
		this.setDebugLevel(ApplicationProperties.getInt(KEY_LOG_DEBUG_LEVEL, DEFAULT_LOG_DEBUG_LEVEL));
		this.baseLogPath = ApplicationProperties.getString(KEY_LOG_PATH, DEFAULT_LOG_PATH);
		this.fullSte = ApplicationProperties.getBoolean(KEY_FULL_STE, DEFAULT_FULL_STE);
	}
}
