/*-
 * $Id: AbstractLogger.java,v 1.15 2006/02/09 14:36:30 saa Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import static com.syrus.util.Log.DEBUG_LEVEL_MAP;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.OFF;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * @author $Author: saa $
 * @version $Revision: 1.15 $, $Date: 2006/02/09 14:36:30 $
 * @module util
 */
abstract class AbstractLogger implements Logger {
	static final String DELIMITER = ".";
	static final String ERROR = "error";
	static final String DEBUG = "debug";
	
	static final String KEY_LOG_DEBUG_LEVEL = "LogDebugLevel";
	static final String KEY_ECHO_DEBUG = "EchoDebug";
	static final String KEY_ECHO_ERROR = "EchoError";
	static final String KEY_LOG_ONLY_THIS_LEVEL = "LogOnlyThisLevel";
	static final String KEY_LOG_PATH = "LogPath";
	static final String KEY_FULL_STE = "FullSte";

	static final String DEFAULT_APPNAME = "defaultApp";
	static final String DEFAULT_HOSTNAME = "defaultHost";

	static final int DEFAULT_LOG_DEBUG_LEVEL = 5;
	static final boolean DEFAULT_ECHO_DEBUG = false;
	static final boolean DEFAULT_ECHO_ERROR = false;
	static final boolean DEFAULT_LOG_ONLY_THIS_LEVEL = false;
	static final String DEFAULT_LOG_PATH = System.getProperty("user.home") + File.separatorChar + "logs";
	static final boolean DEFAULT_FULL_STE = false;

	private String appName;
	private String hostName;
	boolean echoDebug;
	boolean echoError;
	boolean thisLevelOnly;

	/**
	 * @see java.util.logging.Logger#levelObject
	 */
	private Level levelObject;

	/**
	 * @see java.util.logging.Logger#levelValue
	 */
	private volatile int levelValue;

	/**
	 * @see java.util.logging.Logger#offValue
	 */
	private static final int offValue = OFF.intValue();

	String baseLogPath;
	private long logMillis; // current milliseconds
	private String debugLogFileName;
	private String errorLogFileName;
	private PrintWriter errorLog;
	private PrintWriter debugLog;
	/**
	 * Whether full or short form of a stack trace element should be 
	 * printed.
	 */
	boolean fullSte;

	/*-********************************************************************
	 *  StackTraceDataSource                                              *
	 **********************************************************************/

	static final String STACK_TRACE_DATA_SOURCE_THREAD = "thread";
	static final String STACK_TRACE_DATA_SOURCE_THROWABLE = "throwable";
	static final String STACK_TRACE_DATA_SOURCE_NONE = "none";

	static final String KEY_STACK_TRACE_DATA_SOURCE = "StackTraceDataSource";
	static final String DEFAULT_STACK_TRACE_DATA_SOURCE = STACK_TRACE_DATA_SOURCE_THREAD;

	/**
	 * <p>For server-side applications, STACK_TRACE_DATA_SOURCE_THREAD is
	 * the default.</p>
	 * 
	 * <p>For client-side ones (Jet-compiled to native code),
	 * STACK_TRACE_DATA_SOURCE_THROWABLE is the default, since currently
	 * Jet can't fetch current thread's stack trace.</p>
	 *
	 * <p>This field is assumed to hold a reference to a canonical string,
	 * since further it gets compared by reference, not by value.</p>
	 */
	String stackTraceDataSource;


	public AbstractLogger(final String appName, final String hostName) {
		this.appName = (appName != null) ? appName : DEFAULT_APPNAME;
		this.hostName = (hostName != null) ? hostName : DEFAULT_HOSTNAME;
		this.init();
	}

	private void init() {
		this.initSpec();
		this.logMillis = System.currentTimeMillis();
		this.debugLogFileName = this.createLogFileName(DEBUG);
		this.errorLogFileName = this.createLogFileName(ERROR);
	}

	abstract void initSpec();

	public void debugMessage(final String message, final Level debugLevel) {
		if (this.isLoggable(debugLevel)) {
			try {
				synchronized (this) {
					this.checkLogRollover();
					if (this.debugLog == null) {
						this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
					}
					this.logMessage(this.debugLog, message);
					this.debugLog.flush();
					if (this.echoDebug) {
						this.logMessage(System.out, message);
					}
				}
			} catch (Exception e) {
				System.out.println("Exception in debug logging: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void debugException(final Throwable t, final Level debugLevel) {
		if (this.isLoggable(debugLevel)) {
			try {
				synchronized(this) {
					this.checkLogRollover();
					if (this.debugLog == null) {
						this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
					}
					this.logThrowable(this.debugLog, t);
					this.debugLog.flush();
					if (this.echoDebug) {
						this.logThrowable(System.out, t);
					}
				}
			} catch (Exception e) {
				System.out.println("Exception in debug logging: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public synchronized void errorMessage(final String message) {
		this.checkLogRollover();
		try {
			if (this.errorLog == null) {
				this.errorLog = new PrintWriter(new FileWriter(this.errorLogFileName, true), true);
			}
			this.logMessage(this.errorLog, message);
			this.errorLog.flush();

			if (this.echoError) {
				this.logMessage(System.err, message);
			}
		} catch (Exception e) {
			System.out.println("Exception in error logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public synchronized void errorException(final Throwable t) {
		this.checkLogRollover();
		try {
			if (this.errorLog == null) {
				this.errorLog = new PrintWriter(new FileWriter(this.errorLogFileName, true), true);
			}
			this.logThrowable(this.errorLog, t);
			this.errorLog.flush();

			if (this.echoError) {
				this.logThrowable(System.err, t);
			}
		} catch (Exception e) {
			System.out.println("Exception in error logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * should be invoked from synchronized methods only
	 */
	private void checkLogRollover() {
		// Check rollover once per minute only
		final long MINUTE = 60000;
		final long currentTimeMillis = System.currentTimeMillis();
		if (this.logMillis / MINUTE == currentTimeMillis / MINUTE) {
			return;
		}
		// There is no crime to create 3 objects (2 of them are unnecessary) per minute 
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String currd = sdf.format(new Date(this.logMillis));
		final String d = sdf.format(new Date(currentTimeMillis));
		if (currd.equals(d)) {
			return;
		}
		// Date changed. Create new files
		this.logMillis = currentTimeMillis;
		this.errorLogFileName = this.createLogFileName(ERROR);
		this.debugLogFileName = this.createLogFileName(DEBUG);
		this.debugLog = null;
		this.errorLog = null;
	}

	private void logMessage(final PrintWriter out, final String message) {
		logTimestamp(out);
		this.logStackTraceElement(out);
		out.println(message);
	}

	private void logMessage(final PrintStream out, final String message) {
		logTimestamp(out);
		this.logStackTraceElement(out);
		out.println(message);
	}

	private void logThrowable(final PrintWriter out, final Throwable t) {
		logTimestamp(out);
		this.logStackTraceElement(out);
		out.println("Exception (stack trace follows): " + t.getMessage());
		t.printStackTrace(out);
	}

	private void logThrowable(final PrintStream out, final Throwable t) {
		logTimestamp(out);
		this.logStackTraceElement(out);
		out.println("Exception (stack trace follows): " + t.getMessage());
		t.printStackTrace(out);
	}

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

	private static void logTimestamp(final PrintWriter out) {
		out.print(SIMPLE_DATE_FORMAT.format(new Date()) + "| ");
	}

	private static void logTimestamp(final PrintStream out) {
		out.print(SIMPLE_DATE_FORMAT.format(new Date()) + "| ");
	}

	private void logStackTraceElement(final PrintWriter out) {
		final StackTraceElement stackTraceElement = getStackTraceData();
		if (stackTraceElement != null) {
			final String className = stackTraceElement.getClassName();
			out.print((this.fullSte
					? stackTraceElement
					: className.substring(className.lastIndexOf('.') + 1)
							+ '.' + stackTraceElement.getMethodName() + "()")
					+ " | ");
		}
	}

	private void logStackTraceElement(final PrintStream out) {
		final StackTraceElement stackTraceElement = getStackTraceData();
		if (stackTraceElement != null) {
			final String className = stackTraceElement.getClassName();
			out.print((this.fullSte
					? stackTraceElement
					: className.substring(className.lastIndexOf('.') + 1)
							+ '.' + stackTraceElement.getMethodName() + "()")
					+ " | ");
		}
	}

	private StackTraceElement getStackTraceData() {
		if (this.stackTraceDataSource == STACK_TRACE_DATA_SOURCE_NONE) {
			return null;
		}

		final StackTraceElement stackTrace[];
		final int depth;
		if (this.stackTraceDataSource == STACK_TRACE_DATA_SOURCE_THREAD) {
			depth = 8;
			stackTrace = Thread.currentThread().getStackTrace();
		} else {
			depth = 6;
			stackTrace = (new Throwable()).getStackTrace();
		}
		return (stackTrace.length > depth) ? stackTrace[depth] : null;
	}

	private String createLogFileName(final String logType) {
		return this.getLogPath(logType) +	File.separator +
				logType +	DELIMITER +
				this.appName + DELIMITER +
				this.hostName + ".log";
	}

	private String getLogPath(final String logType) {
		String logPath = this.baseLogPath;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		logPath += File.separator + sdf.format(new Date(this.logMillis)) + File.separator + logType;
		final File file = new File(logPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return logPath;
	}

	/**
	 * @param reverseIntValue AMFICOM-standard debug level, ranging from 1
	 *        to 10. Higher <code>reverseIntValue</code>s mean more verbose
	 *        output. This behaviour is opposite to that of
	 *        {@link Level Level}<code>.</code>{@link Level#intValue() intValue()},
	 *        that's the reason why parameter is named
	 *        <code>reverseIntValue</code>.
	 * @see #setLevel(Level)
	 * @see java.util.logging.Logger#setLevel(Level)
	 */
	public void setLevel(final int reverseIntValue) {
		if (10 < reverseIntValue || reverseIntValue < 1) {
			throw new IllegalArgumentException(String.valueOf(reverseIntValue));
		}

		this.setLevel(DEBUG_LEVEL_MAP.get(new Integer(reverseIntValue)));
	}

	/**
	 * @see java.util.logging.Logger#getLevel()
	 */
	public final Level getLevel() {
		return this.levelObject;
	}

	/**
	 * @see java.util.logging.Logger#setLevel(Level)
	 */
	public final void setLevel(final Level newLevel) {
		this.levelObject = newLevel;
		this.updateEffectiveLevel();
	}

	/**
	 * @see java.util.logging.Logger#updateEffectiveLevel()
	 */
	private void updateEffectiveLevel() {
		this.levelValue = (this.levelObject == null)
				? INFO.intValue()
				: this.levelObject.intValue();
	}

	/**
	 * @see java.util.logging.Logger#isLoggable(Level)
	 */
	public final boolean isLoggable(final Level level) {
		return this.levelValue != offValue && (this.thisLevelOnly
				? level.intValue() == this.levelValue
				: level.intValue() >= this.levelValue);
	}
}
