/*
 * $Id: Log.java,v 1.9 2005/07/11 07:53:55 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.util.logging.Level;

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

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#SEVERE
	 */
	public static final Level SEVERE = Level.SEVERE;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#WARNING
	 */
	public static final Level WARNING = Level.WARNING;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#INFO
	 */
	public static final Level INFO = Level.INFO;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#CONFIG
	 */
	public static final Level CONFIG = Level.CONFIG;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#FINE
	 */
	public static final Level FINE = Level.FINE;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#FINER
	 */
	public static final Level FINER = Level.FINER;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#FINEST
	 */
	public static final Level FINEST = Level.FINEST;

	private static Logger logger;

	private Log() {
		assert false;
	}

	public static void initialize(Logger logger1) {
		logger = logger1;
	}

	/**
	 * @param message
	 * @param debugLevel
	 * @return always true, can be used as <code>assert Log.debugMessage(...)</code>
	 */
	public static boolean debugMessage(final String message, final Level debugLevel) {
		try {
			logger.debugMessage(message, debugLevel);
		} catch (final NullPointerException npe) {
			System.out.println(message);
		}
		return true;
	}

	public static void debugException(final Throwable t, final Level debugLevel) {
		try {
			logger.debugException(t, debugLevel);
		} catch (NullPointerException npe) {
			System.out.println(t.getMessage());
			t.printStackTrace();
		}
	}

	public static void errorMessage(String mesg) {
		try {
			logger.errorMessage(mesg);
		} catch (NullPointerException npe) {
			System.out.println(mesg);
		}
	}

	public static void errorException(Throwable throwable) {
		try {
			logger.errorException(throwable);
		} catch (NullPointerException npe) {
			System.out.println(throwable.getMessage());
			throwable.printStackTrace();
		}
	}

	private static class CustomLevel extends Level {
		private static final long serialVersionUID = 8040407643584688402L;

		private CustomLevel(String name, int value) {
			super(name, value);
		}
	}
}
