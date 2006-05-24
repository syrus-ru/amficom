/*
 * $Id: ApplicationLogger.java,v 1.18 2006/05/24 10:15:48 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.Properties;

/**
 * @author Tashoyan Arseniy Feliksovich
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.18 $, $Date: 2006/05/24 10:15:48 $
 * @module util
 */
final class ApplicationLogger extends AbstractLogger {
	private static final Properties PROPERTIES = new Properties();

	static {
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_APPLICATION_NAME,
				ApplicationProperties.getString(
						KEY_APPLICATION_NAME, 
						DEFAULT_APPLICATION_NAME));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_HOSTNAME,
				ApplicationProperties.getString(
						KEY_HOSTNAME,
						DEFAULT_HOSTNAME));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_ECHO_DEBUG,
				ApplicationProperties.getString(
						KEY_ECHO_DEBUG,
						DEFAULT_ECHO_DEBUG));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_ECHO_ERROR,
				ApplicationProperties.getString(
						KEY_ECHO_ERROR,
						DEFAULT_ECHO_ERROR));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_LOG_ONLY_THIS_LEVEL,
				ApplicationProperties.getString(
						KEY_LOG_ONLY_THIS_LEVEL,
						DEFAULT_LOG_ONLY_THIS_LEVEL));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_LOG_DEBUG_LEVEL,
				ApplicationProperties.getString(
						KEY_LOG_DEBUG_LEVEL,
						DEFAULT_LOG_DEBUG_LEVEL));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_LOG_PATH,
				ApplicationProperties.getString(
						KEY_LOG_PATH,
						DEFAULT_LOG_PATH));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_FULL_STE,
				ApplicationProperties.getString(
						KEY_FULL_STE,
						DEFAULT_FULL_STE));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_ALLOW_LEVEL_OUTPUT,
				ApplicationProperties.getString(
						KEY_ALLOW_LEVEL_OUTPUT,
						DEFAULT_ALLOW_LEVEL_OUTPUT));
		PROPERTIES.put(PROPERTY_NAME_PREFIX + KEY_STACK_TRACE_DATA_SOURCE,
				ApplicationProperties.getString(
						KEY_STACK_TRACE_DATA_SOURCE,
						DEFAULT_STACK_TRACE_DATA_SOURCE));
	}

	/**
	 * Creates a logger with {@code applicationName} and {@code hostame}
	 * taken from properties file.
	 */
	public ApplicationLogger() {
		super(PROPERTIES);
	}

	/**
	 * Creates a logger with {@code applicationName} and {@code hostame}
	 * explicitly specified by user.
	 *
	 * @param applicationName
	 * @param hostname
	 * @deprecated Use {@link #ApplicationLogger()} instead.
	 */
	@Deprecated
	public ApplicationLogger(final String applicationName, final String hostname) {
		super(applicationName, hostname, PROPERTIES);
	}
}
