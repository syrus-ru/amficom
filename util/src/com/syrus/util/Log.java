/*-
 * $Id: Log.java,v 1.27 2006/05/23 17:14:59 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import static java.util.logging.Level.ALL;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @version $Revision: 1.27 $, $Date: 2006/05/23 17:14:59 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public final class Log {
	public static final Level DEBUGLEVEL01 = SEVERE;
	public static final Level DEBUGLEVEL02 = WARNING;
	public static final Level DEBUGLEVEL03 = INFO;
	public static final Level DEBUGLEVEL04 = CONFIG;
	public static final Level DEBUGLEVEL05 = FINE;
	public static final Level DEBUGLEVEL06 = FINER;
	public static final Level DEBUGLEVEL07 = FINEST;
	public static final Level DEBUGLEVEL08 = new CustomLevel("DBGLV08", 200);
	public static final Level DEBUGLEVEL09 = new CustomLevel("DBGLV09", 100);
	public static final Level DEBUGLEVEL10 = new CustomLevel("DBGLV10", 50);

	static final Map<Integer, Level> DEBUG_LEVEL_MAP;

	static final int MAX_LEVEL_NAME_LENGTH;

	private static final Logger DEFAULT_LOGGER = getDefaultLogger();

	private static Logger logger = DEFAULT_LOGGER;

	static {
		int i = 10;

		final HashMap<Integer, Level> debugLevelMap = new HashMap<Integer, Level>(i);

		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL10);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL09);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL08);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL07);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL06);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL05);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL04);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL03);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL02);
		debugLevelMap.put(Integer.valueOf(i--), DEBUGLEVEL01);

		DEBUG_LEVEL_MAP = Collections.unmodifiableMap(debugLevelMap);
	}

	static {
		int maxLevelNameLength = 0;

		for (final Level level : DEBUG_LEVEL_MAP.values()) {
			final int levelNameLength = level.getName().length();
			if (maxLevelNameLength < levelNameLength) {
				maxLevelNameLength = levelNameLength;
			}
		}

		MAX_LEVEL_NAME_LENGTH = maxLevelNameLength;
	}

	private Log() {
		assert false;
	}

	public static void setLogger(final Logger logger) {
		if (logger == null) {
			throw new NullPointerException();
		}
		Log.logger = logger;
	}

	/**
	 * @see java.util.logging.Logger#isLoggable(Level)
	 */
	public static boolean isLoggable(final Level level) {
		return logger.isLoggable(level);
	}

	/**
	 * @see java.util.logging.Logger#getLevel()
	 */
	public static Level getLevel() {
		return logger.getLevel();
	}

	/**
	 * @see java.util.logging.Logger#setLevel(Level)
	 */
	public static void setLevel(final Level newLevel) {
		logger.setLevel(newLevel);
	}

	private static Logger getDefaultLogger() {
		return new Logger() {
			public void debugMessage(final String message, final Level debugLevel) {
				System.out.println(message);
			}

			public void debugException(final Throwable t, final Level debugLevel) {
				System.out.println(t);
				t.printStackTrace(System.out);
			}

			public void errorMessage(final String message) {
				System.err.println(message);
			}

			public void errorException(final Throwable t) {
				System.err.println(t);
				t.printStackTrace(System.err);
			}

			public boolean isLoggable(final Level level) {
				return true;
			}

			public Level getLevel() {
				return ALL;
			}

			@SuppressWarnings("all")
			public void setLevel(final Level newLevel) {
			}
		};
	}

	/*-********************************************************************
	 * debugMessage(...) methods                                          *
	 **********************************************************************/

	public static boolean debugMessage(final Level debugLevel) {
		return debugMessage0("", debugLevel);
	}

	public static boolean debugMessage(final boolean b, final Level debugLevel) {
		return debugMessage0(Boolean.toString(b), debugLevel);
	}

	public static boolean debugMessage(final byte b, final Level debugLevel) {
		return debugMessage0(Byte.toString(b), debugLevel);
	}

	public static boolean debugMessage(final char c, final Level debugLevel) {
		return debugMessage0(Character.toString(c), debugLevel);
	}

	public static boolean debugMessage(final short s, final Level debugLevel) {
		return debugMessage0(Short.toString(s), debugLevel);
	}

	public static boolean debugMessage(final int i, final Level debugLevel) {
		return debugMessage0(Integer.toString(i), debugLevel);
	}

	public static boolean debugMessage(final long l, final Level debugLevel) {
		return debugMessage0(Long.toString(l), debugLevel);
	}

	public static boolean debugMessage(final float f, final Level debugLevel) {
		return debugMessage0(Float.toString(f), debugLevel);
	}
	
	public static boolean debugMessage(final double d, final Level debugLevel) {
		return debugMessage0(Double.toString(d), debugLevel);
	}

	public static boolean debugMessage(final char c[], final Level debugLevel) {
		return debugMessage0(String.valueOf(c), debugLevel);
	}

	public static boolean debugMessage(final Object object, final Level debugLevel) {
		return debugMessage0(String.valueOf(object), debugLevel);
	}

	public static boolean debugMessage(final String message, final Level debugLevel) {
		return debugMessage0(message, debugLevel);
	}

	public static boolean debugMessage(final Throwable t, final Level debugLevel) {
		return debugMessage0(t, debugLevel);
	}


	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param message
	 * @param debugLevel
	 */
	private static boolean debugMessage0(final String message, final Level debugLevel) {
		logger.debugMessage(message, debugLevel);
		return true;
	}

	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param t
	 * @param debugLevel
	 */
	private static boolean debugMessage0(final Throwable t, final Level debugLevel) {
		logger.debugException(t, debugLevel);
		return true;
	}

	/*-********************************************************************
	 * errorMessage(...) methods                                          *
	 **********************************************************************/

	public static boolean errorMessage() {
		return errorMessage0("");
	}

	public static boolean errorMessage(final boolean b) {
		return errorMessage0(Boolean.toString(b));
	}

	public static boolean errorMessage(final byte b) {
		return errorMessage0(Byte.toString(b));
	}

	public static boolean errorMessage(final char c) {
		return errorMessage0(Character.toString(c));
	}

	public static boolean errorMessage(final short s) {
		return errorMessage0(Short.toString(s));
	}

	public static boolean errorMessage(final int i) {
		return errorMessage0(Integer.toString(i));
	}

	public static boolean errorMessage(final long l) {
		return errorMessage0(Long.toString(l));
	}

	public static boolean errorMessage(final float f) {
		return errorMessage0(Float.toString(f));
	}
	
	public static boolean errorMessage(final double d) {
		return errorMessage0(Double.toString(d));
	}

	public static boolean errorMessage(final char c[]) {
		return errorMessage0(String.valueOf(c));
	}

	public static boolean errorMessage(final Object object) {
		return errorMessage0(String.valueOf(object));
	}

	public static boolean errorMessage(final String message) {
		return errorMessage0(message);
	}

	public static boolean errorMessage(final Throwable t) {
		return errorMessage0(t);
	}


	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param message
	 */
	private static boolean errorMessage0(final String message) {
		logger.errorMessage(message);
		return true;
	}

	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param t
	 */
	private static boolean errorMessage0(final Throwable t) {
		logger.errorException(t);
		return true;
	}

	/*-********************************************************************
	 * Deprecated methods                                                 *
	 **********************************************************************/

	/**
	 * @deprecated
	 */
	@Deprecated
	public static boolean debugException(final Throwable t, final Level level) {
		return debugMessage0(t, level);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static boolean errorException(final Throwable t) {
		return errorMessage0(t);
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.27 $, $Date: 2006/05/23 17:14:59 $
	 * @module util
	 */
	private static class CustomLevel extends Level {
		private static final long serialVersionUID = 8040407643584688402L;

		CustomLevel(final String name, final int value) {
			super(name, value);
		}
	}
}
