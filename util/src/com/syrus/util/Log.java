/*-
 * $Id: Log.java,v 1.18 2005/10/31 12:29:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * @version $Revision: 1.18 $, $Date: 2005/10/31 12:29:58 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class Log {
	public static final Level DEBUGLEVEL01 = new CustomLevel("DEBUGLEVEL01", Level.SEVERE.intValue());
	public static final Level DEBUGLEVEL02 = new CustomLevel("DEBUGLEVEL02", Level.WARNING.intValue());
	public static final Level DEBUGLEVEL03 = new CustomLevel("DEBUGLEVEL03", Level.INFO.intValue());
	public static final Level DEBUGLEVEL04 = new CustomLevel("DEBUGLEVEL04", Level.CONFIG.intValue());
	public static final Level DEBUGLEVEL05 = new CustomLevel("DEBUGLEVEL05", Level.FINE.intValue());
	public static final Level DEBUGLEVEL06 = new CustomLevel("DEBUGLEVEL06", Level.FINER.intValue());
	public static final Level DEBUGLEVEL07 = new CustomLevel("DEBUGLEVEL07", Level.FINEST.intValue());
	public static final Level DEBUGLEVEL08 = new CustomLevel("DEBUGLEVEL08", 200);
	public static final Level DEBUGLEVEL09 = new CustomLevel("DEBUGLEVEL09", 100);
	public static final Level DEBUGLEVEL10 = new CustomLevel("DEBUGLEVEL10", 50);

	static final Map<Integer, Level> DEBUG_LEVEL_MAP;

	private static Logger logger;

	static {
		DEBUG_LEVEL_MAP = new HashMap<Integer, Level>(10);
		DEBUG_LEVEL_MAP.put(new Integer(1), DEBUGLEVEL01);
		DEBUG_LEVEL_MAP.put(new Integer(2), DEBUGLEVEL02);
		DEBUG_LEVEL_MAP.put(new Integer(3), DEBUGLEVEL03);
		DEBUG_LEVEL_MAP.put(new Integer(4), DEBUGLEVEL04);
		DEBUG_LEVEL_MAP.put(new Integer(5), DEBUGLEVEL05);
		DEBUG_LEVEL_MAP.put(new Integer(6), DEBUGLEVEL06);
		DEBUG_LEVEL_MAP.put(new Integer(7), DEBUGLEVEL07);
		DEBUG_LEVEL_MAP.put(new Integer(8), DEBUGLEVEL08);
		DEBUG_LEVEL_MAP.put(new Integer(9), DEBUGLEVEL09);
		DEBUG_LEVEL_MAP.put(new Integer(10), DEBUGLEVEL10);
	}

	private Log() {
		assert false;
	}

	private static void setDefaultLogger() {
		setLogger(new Logger() {
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
		});
	}

	public static void setLogger(final Logger logger) {
		if (logger == null) {
			throw new NullPointerException();
		}
		Log.logger = logger;
	}

	/*-********************************************************************
	 * debugMessage(...) methods                                          *
	 **********************************************************************/

	public static void debugMessage(final Level debugLevel) {
		debugMessage0("", debugLevel);
	}

	public static void debugMessage(final boolean b, final Level debugLevel) {
		debugMessage0(Boolean.toString(b), debugLevel);
	}

	public static void debugMessage(final byte b, final Level debugLevel) {
		debugMessage0(Byte.toString(b), debugLevel);
	}

	public static void debugMessage(final char c, final Level debugLevel) {
		debugMessage0(Character.toString(c), debugLevel);
	}

	public static void debugMessage(final short s, final Level debugLevel) {
		debugMessage0(Short.toString(s), debugLevel);
	}

	public static void debugMessage(final int i, final Level debugLevel) {
		debugMessage0(Integer.toString(i), debugLevel);
	}

	public static void debugMessage(final long l, final Level debugLevel) {
		debugMessage0(Long.toString(l), debugLevel);
	}

	public static void debugMessage(final float f, final Level debugLevel) {
		debugMessage0(Float.toString(f), debugLevel);
	}
	
	public static void debugMessage(final double d, final Level debugLevel) {
		debugMessage0(Double.toString(d), debugLevel);
	}

	public static void debugMessage(final char c[], final Level debugLevel) {
		debugMessage0(String.valueOf(c), debugLevel);
	}

	public static void debugMessage(final Object object, final Level debugLevel) {
		debugMessage0(String.valueOf(object), debugLevel);
	}

	public static void debugMessage(final String message, final Level debugLevel) {
		debugMessage0(message, debugLevel);
	}

	public static void debugMessage(final Throwable t, final Level debugLevel) {
		debugMessage0(t, debugLevel);
	}


	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param message
	 * @param debugLevel
	 */
	private static void debugMessage0(final String message, final Level debugLevel) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.debugMessage(message, debugLevel);
	}

	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param t
	 * @param debugLevel
	 */
	private static void debugMessage0(final Throwable t, final Level debugLevel) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.debugException(t, debugLevel);
	}

	/*-********************************************************************
	 * errorMessage(...) methods                                          *
	 **********************************************************************/

	public static void errorMessage() {
		errorMessage0("");
	}

	public static void errorMessage(final boolean b) {
		errorMessage0(Boolean.toString(b));
	}

	public static void errorMessage(final byte b) {
		errorMessage0(Byte.toString(b));
	}

	public static void errorMessage(final char c) {
		errorMessage0(Character.toString(c));
	}

	public static void errorMessage(final short s) {
		errorMessage0(Short.toString(s));
	}

	public static void errorMessage(final int i) {
		errorMessage0(Integer.toString(i));
	}

	public static void errorMessage(final long l) {
		errorMessage0(Long.toString(l));
	}

	public static void errorMessage(final float f) {
		errorMessage0(Float.toString(f));
	}
	
	public static void errorMessage(final double d) {
		errorMessage0(Double.toString(d));
	}

	public static void errorMessage(final char c[]) {
		errorMessage0(String.valueOf(c));
	}

	public static void errorMessage(final Object object) {
		errorMessage0(String.valueOf(object));
	}

	public static void errorMessage(final String message) {
		errorMessage0(message);
	}

	public static void errorMessage(final Throwable t) {
		errorMessage0(t);
	}


	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param message
	 */
	private static void errorMessage0(final String message) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.errorMessage(message);
	}

	/**
	 * The single entry point to make all stack trace depths consistent.
	 *
	 * @param t
	 */
	private static void errorMessage0(final Throwable t) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.errorException(t);
	}

	/*-********************************************************************
	 * Deprecated methods                                                 *
	 **********************************************************************/

	/**
	 * @deprecated
	 */
	@Deprecated
	public static void debugException(final Throwable t, final Level level) {
		debugMessage(t, level);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static void errorException(final Throwable t) {
		errorMessage(t);
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.18 $, $Date: 2005/10/31 12:29:58 $
	 * @module util
	 */
	private static class CustomLevel extends Level {
		private static final long serialVersionUID = 8040407643584688402L;

		private CustomLevel(final String name, final int value) {
			super(name, value);
		}
	}
}
