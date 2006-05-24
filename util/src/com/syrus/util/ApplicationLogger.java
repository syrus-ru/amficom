/*
 * $Id: ApplicationLogger.java,v 1.20 2006/05/24 11:21:36 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import static com.syrus.util.DefaultLogger.DEFAULT_ALLOW_LEVEL_OUTPUT;
import static com.syrus.util.DefaultLogger.DEFAULT_APPLICATION_NAME;
import static com.syrus.util.DefaultLogger.DEFAULT_ECHO_DEBUG;
import static com.syrus.util.DefaultLogger.DEFAULT_ECHO_ERROR;
import static com.syrus.util.DefaultLogger.DEFAULT_FULL_STE;
import static com.syrus.util.DefaultLogger.DEFAULT_HOSTNAME;
import static com.syrus.util.DefaultLogger.DEFAULT_LOG_DEBUG_LEVEL;
import static com.syrus.util.DefaultLogger.DEFAULT_LOG_ONLY_THIS_LEVEL;
import static com.syrus.util.DefaultLogger.DEFAULT_LOG_PATH;
import static com.syrus.util.DefaultLogger.DEFAULT_STACK_TRACE_DATA_SOURCE;
import static com.syrus.util.DefaultLogger.KEY_ALLOW_LEVEL_OUTPUT;
import static com.syrus.util.DefaultLogger.KEY_APPLICATION_NAME;
import static com.syrus.util.DefaultLogger.KEY_ECHO_DEBUG;
import static com.syrus.util.DefaultLogger.KEY_ECHO_ERROR;
import static com.syrus.util.DefaultLogger.KEY_FULL_STE;
import static com.syrus.util.DefaultLogger.KEY_HOSTNAME;
import static com.syrus.util.DefaultLogger.KEY_LOG_DEBUG_LEVEL;
import static com.syrus.util.DefaultLogger.KEY_LOG_ONLY_THIS_LEVEL;
import static com.syrus.util.DefaultLogger.KEY_LOG_PATH;
import static com.syrus.util.DefaultLogger.KEY_STACK_TRACE_DATA_SOURCE;
import static com.syrus.util.DefaultLogger.PROPERTY_NAME_PREFIX;

import java.util.Properties;
import java.util.logging.Level;

/**
 * @author Tashoyan Arseniy Feliksovich
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.20 $, $Date: 2006/05/24 11:21:36 $
 * @module util
 */
final class ApplicationLogger implements Logger {
	private final Properties properties = new Properties();

	private final Logger logger;

	{
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_APPLICATION_NAME,
				ApplicationProperties.getString(
						KEY_APPLICATION_NAME, 
						DEFAULT_APPLICATION_NAME));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_HOSTNAME,
				ApplicationProperties.getString(
						KEY_HOSTNAME,
						DEFAULT_HOSTNAME));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_ECHO_DEBUG,
				ApplicationProperties.getString(
						KEY_ECHO_DEBUG,
						DEFAULT_ECHO_DEBUG));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_ECHO_ERROR,
				ApplicationProperties.getString(
						KEY_ECHO_ERROR,
						DEFAULT_ECHO_ERROR));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_LOG_ONLY_THIS_LEVEL,
				ApplicationProperties.getString(
						KEY_LOG_ONLY_THIS_LEVEL,
						DEFAULT_LOG_ONLY_THIS_LEVEL));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_LOG_DEBUG_LEVEL,
				ApplicationProperties.getString(
						KEY_LOG_DEBUG_LEVEL,
						DEFAULT_LOG_DEBUG_LEVEL));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_LOG_PATH,
				ApplicationProperties.getString(
						KEY_LOG_PATH,
						DEFAULT_LOG_PATH));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_FULL_STE,
				ApplicationProperties.getString(
						KEY_FULL_STE,
						DEFAULT_FULL_STE));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_ALLOW_LEVEL_OUTPUT,
				ApplicationProperties.getString(
						KEY_ALLOW_LEVEL_OUTPUT,
						DEFAULT_ALLOW_LEVEL_OUTPUT));
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_STACK_TRACE_DATA_SOURCE,
				ApplicationProperties.getString(
						KEY_STACK_TRACE_DATA_SOURCE,
						DEFAULT_STACK_TRACE_DATA_SOURCE));
	}

	/**
	 * Creates a logger with {@code applicationName} and {@code hostame}
	 * taken from properties file.
	 */
	public ApplicationLogger() {
		final DefaultLogger defaultLogger = new DefaultLogger(this.properties);
		defaultLogger.incrementStackDepth(1);
		this.logger = defaultLogger;
	}

	/**
	 * Creates a logger with {@code applicationName} and {@code hostame}
	 * explicitly specified by user.
	 *
	 * @param applicationName
	 * @param hostname
	 */
	public ApplicationLogger(final String applicationName, final String hostname) {
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_APPLICATION_NAME, applicationName);
		this.properties.put(PROPERTY_NAME_PREFIX + KEY_HOSTNAME, hostname);

		final DefaultLogger defaultLogger = new DefaultLogger(this.properties);
		defaultLogger.incrementStackDepth(1);
		this.logger = defaultLogger;
	}

	/**
	 * @param t
	 * @param debugLevel
	 * @see com.syrus.util.Logger#debugException(java.lang.Throwable, java.util.logging.Level)
	 */
	public void debugException(final Throwable t, final Level debugLevel) {
		this.logger.debugException(t, debugLevel);
	}

	/**
	 * @param message
	 * @param debugLevel
	 * @see com.syrus.util.Logger#debugMessage(java.lang.String, java.util.logging.Level)
	 */
	public void debugMessage(final String message, final Level debugLevel) {
		this.logger.debugMessage(message, debugLevel);
	}

	/**
	 * @param t
	 * @see com.syrus.util.Logger#errorException(java.lang.Throwable)
	 */
	public void errorException(final Throwable t) {
		this.logger.errorException(t);
	}

	/**
	 * @param message
	 * @see com.syrus.util.Logger#errorMessage(java.lang.String)
	 */
	public void errorMessage(final String message) {
		this.logger.errorMessage(message);
	}

	/**
	 * @see com.syrus.util.Logger#getLevel()
	 */
	public Level getLevel() {
		return this.logger.getLevel();
	}

	/**
	 * @param level
	 * @see com.syrus.util.Logger#isLoggable(java.util.logging.Level)
	 */
	public boolean isLoggable(final Level level) {
		return this.logger.isLoggable(level);
	}

	/**
	 * @param reverseIntValue
	 * @see com.syrus.util.Logger#setLevel(int)
	 */
	public void setLevel(final int reverseIntValue) {
		this.logger.setLevel(reverseIntValue);
	}

	/**
	 * @param level
	 * @see com.syrus.util.Logger#setLevel(java.util.logging.Level)
	 */
	public void setLevel(final Level level) {
		this.logger.setLevel(level);
	}
}
