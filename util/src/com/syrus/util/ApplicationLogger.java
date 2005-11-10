/*
 * $Id: ApplicationLogger.java,v 1.15 2005/11/10 11:30:43 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

/**
 * @version $Revision: 1.15 $, $Date: 2005/11/10 11:30:43 $
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
		this.setLevel(ApplicationProperties.getInt(KEY_LOG_DEBUG_LEVEL, DEFAULT_LOG_DEBUG_LEVEL));
		this.baseLogPath = ApplicationProperties.getString(KEY_LOG_PATH, DEFAULT_LOG_PATH);
		this.fullSte = ApplicationProperties.getBoolean(KEY_FULL_STE, DEFAULT_FULL_STE);

		this.stackTraceDataSource = System.getProperty(
				"amficom.stack.trace.data.source",
				DEFAULT_STACK_TRACE_DATA_SOURCE).intern();
		if (this.stackTraceDataSource != STACK_TRACE_DATA_SOURCE_THREAD
				&& this.stackTraceDataSource != STACK_TRACE_DATA_SOURCE_THROWABLE
				&& this.stackTraceDataSource != STACK_TRACE_DATA_SOURCE_NONE) {
			this.stackTraceDataSource = STACK_TRACE_DATA_SOURCE_THREAD;
		}
	}
}
