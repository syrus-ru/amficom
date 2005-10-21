/*
 * $Id: Log.java,v 1.14 2005/10/21 15:09:07 bass Exp $
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
 * @version $Revision: 1.14 $, $Date: 2005/10/21 15:09:07 $
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

	public static boolean debugException(final Throwable t, final Level debugLevel) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.debugException(t, debugLevel);
		return true;
	}

	public static boolean errorMessage(final String mesg) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.errorMessage(mesg);
		return true;
	}

	public static boolean errorException(final Throwable throwable) {
		if (logger == null) {
			setDefaultLogger();
		}
		logger.errorException(throwable);
		return true;
	}

	private static class CustomLevel extends Level {
		private static final long serialVersionUID = 8040407643584688402L;

		private CustomLevel(final String name, int value) {
			super(name, value);
		}
	}
}
