package com.syrus.util;

public class Log {
	public static final int DEBUGLEVEL01 = 1;
	public static final int DEBUGLEVEL02 = 2;
	public static final int DEBUGLEVEL03 = 3;
	public static final int DEBUGLEVEL04 = 4;
	public static final int DEBUGLEVEL05 = 5;
	public static final int DEBUGLEVEL06 = 6;
	public static final int DEBUGLEVEL07 = 7;
	public static final int DEBUGLEVEL08 = 8;
	public static final int DEBUGLEVEL09 = 9;
	public static final int DEBUGLEVEL10 = 10;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#SEVERE
	 */
	public static final int SEVERE = DEBUGLEVEL01;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#WARNING
	 */
	public static final int WARNING = DEBUGLEVEL02;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#INFO
	 */
	public static final int INFO = DEBUGLEVEL03;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#CONFIG
	 */
	public static final int CONFIG = DEBUGLEVEL04;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#FINE
	 */
	public static final int FINE = DEBUGLEVEL05;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#FINER
	 */
	public static final int FINER = DEBUGLEVEL06;

	/**
	 * Value: {@value}
	 *
	 * @see java.util.logging.Level#FINEST
	 */
	public static final int FINEST = DEBUGLEVEL07;

	private static Logger logger;

	private Log() {
	}

	public static void initialize(Logger logger1) {
		logger = logger1;
	}

	public static void debugMessage(String mesg, int debugLevel) {
		try { 
			logger.debugMessage(mesg, debugLevel);
		}
		catch (NullPointerException npe) {
			System.out.println(mesg);
		}
	}

	public static void debugException(Throwable throwable, int debugLevel) {
		try { 
			logger.debugException(throwable, debugLevel);
		}
		catch (NullPointerException npe) {
			System.out.println(throwable.getMessage());
			throwable.printStackTrace();
		}
	}

	public static void errorMessage(String mesg) {
		try { 
			logger.errorMessage(mesg);
		}
		catch (NullPointerException npe) {
			System.out.println(mesg);
		}
	}

	public static void errorException(Throwable throwable) {
		try { 
			logger.errorException(throwable);
		}
		catch (NullPointerException npe) {
			System.out.println(throwable.getMessage());
			throwable.printStackTrace();
		}
	}
}
