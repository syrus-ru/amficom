/*-
 * $Id: Log.java,v 1.16 2005/10/30 16:17:47 bass Exp $
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
 * @version $Revision: 1.16 $, $Date: 2005/10/30 16:17:47 $
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

	public static boolean debugMessage(final Level debugLevel) {
		return debugMessage("", debugLevel);
	}

	public static boolean debugMessage(final boolean b, final Level debugLevel) {
		return debugMessage(Boolean.toString(b), debugLevel);
	}

	public static boolean debugMessage(final byte b, final Level debugLevel) {
		return debugMessage(Byte.toString(b), debugLevel);
	}

	public static boolean debugMessage(final char c, final Level debugLevel) {
		return debugMessage(Character.toString(c), debugLevel);
	}

	public static boolean debugMessage(final short s, final Level debugLevel) {
		return debugMessage(Short.toString(s), debugLevel);
	}

	public static boolean debugMessage(final int i, final Level debugLevel) {
		return debugMessage(Integer.toString(i), debugLevel);
	}

	public static boolean debugMessage(final long l, final Level debugLevel) {
		return debugMessage(Long.toString(l), debugLevel);
	}

	public static boolean debugMessage(final float f, final Level debugLevel) {
		return debugMessage(Float.toString(f), debugLevel);
	}
	
	public static boolean debugMessage(final double d, final Level debugLevel) {
		return debugMessage(Double.toString(d), debugLevel);
	}

	public static boolean debugMessage(final char c[], final Level debugLevel) {
		return debugMessage(String.valueOf(c), debugLevel);
	}

	public static boolean debugMessage(final Object object, final Level debugLevel) {
		return debugMessage(String.valueOf(object), debugLevel);
	}

	/**
	 * @param message
	 * @param debugLevel
	 * @return always true, can be used as <code>assert Log.debugMessage(...)</code>
	 */
	public static boolean debugMessage(final String message, final Level debugLevel) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.debugMessage(message, debugLevel);
		return true;
	}

	public static boolean debugMessage(final Throwable t, final Level debugLevel) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.debugException(t, debugLevel);
		return true;
	}

	/*-********************************************************************
	 * errorMessage(...) methods                                          *
	 **********************************************************************/

	public static boolean errorMessage() {
		return errorMessage("");
	}

	public static boolean errorMessage(final boolean b) {
		return errorMessage(Boolean.toString(b));
	}

	public static boolean errorMessage(final byte b) {
		return errorMessage(Byte.toString(b));
	}

	public static boolean errorMessage(final char c) {
		return errorMessage(Character.toString(c));
	}

	public static boolean errorMessage(final short s) {
		return errorMessage(Short.toString(s));
	}

	public static boolean errorMessage(final int i) {
		return errorMessage(Integer.toString(i));
	}

	public static boolean errorMessage(final long l) {
		return errorMessage(Long.toString(l));
	}

	public static boolean errorMessage(final float f) {
		return errorMessage(Float.toString(f));
	}
	
	public static boolean errorMessage(final double d) {
		return errorMessage(Double.toString(d));
	}

	public static boolean errorMessage(final char c[]) {
		return errorMessage(String.valueOf(c));
	}

	public static boolean errorMessage(final Object object) {
		return errorMessage(String.valueOf(object));
	}

	public static boolean errorMessage(final String message) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.errorMessage(message);
		return true;
	}

	public static boolean errorMessage(final Throwable t) {
		if (logger == null) {
			setDefaultLogger();
		}
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
		return debugMessage(t, level);
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static boolean errorException(final Throwable t) {
		return errorMessage(t);
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.16 $, $Date: 2005/10/30 16:17:47 $
	 * @module util
	 */
	private static class CustomLevel extends Level {
		private static final long serialVersionUID = 8040407643584688402L;

		private CustomLevel(final String name, final int value) {
			super(name, value);
		}
	}
}
