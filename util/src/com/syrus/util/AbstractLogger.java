/*-
 * $Id: AbstractLogger.java,v 1.9 2005/10/30 20:16:45 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import static com.syrus.util.Log.DEBUG_LEVEL_MAP;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/10/30 20:16:45 $
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
	private Level logDebugLevel;
	String baseLogPath;
	private Date logDate;
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
		this.logDate = new Date();
		this.debugLogFileName = this.createLogFileName(DEBUG);
		this.errorLogFileName = this.createLogFileName(ERROR);
	}

	abstract void initSpec();

	public synchronized void debugMessage(final String message, final Level debugLevel) {
		this.checkLogRollover();
		try {
			if ((!this.thisLevelOnly && debugLevel.intValue() >= this.logDebugLevel.intValue())
					|| debugLevel.intValue() == this.logDebugLevel.intValue()) {
				this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
				this.logMessage(this.debugLog, message);
				this.debugLog.close();
				if (this.echoDebug) {
					this.logMessage(System.out, message);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in debug logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public synchronized void debugException(final Throwable t, final Level debugLevel) {
		this.checkLogRollover();
		try {
			if ((!this.thisLevelOnly && debugLevel.intValue() >= this.logDebugLevel.intValue())
					|| debugLevel.intValue() == this.logDebugLevel.intValue()) {
				this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
				this.logThrowable(this.debugLog, t);
				this.debugLog.close();
				if (this.echoDebug) {
					this.logThrowable(System.out, t);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in debug logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public synchronized void errorMessage(final String message) {
		this.checkLogRollover();
		try {
			this.errorLog = new PrintWriter(new FileWriter(this.errorLogFileName, true), true);
			this.logMessage(this.errorLog, message);
			this.errorLog.close();

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
			this.errorLog = new PrintWriter(new FileWriter(this.errorLogFileName, true), true);
			this.logThrowable(this.errorLog, t);
			this.errorLog.close();

			if (this.echoError) {
				this.logThrowable(System.err, t);
			}
		} catch (Exception e) {
			System.out.println("Exception in error logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void checkLogRollover() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final String currd = sdf.format(this.logDate);
		final String d = sdf.format(new Date());
		if(!currd.equals(d)) {
			this.logDate = new Date();
			this.errorLogFileName = this.createLogFileName(ERROR);
			this.debugLogFileName = this.createLogFileName(DEBUG);
		}
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
		logPath += File.separator + sdf.format(this.logDate) + File.separator + logType;
		final File file = new File(logPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return logPath;
	}

	/**
	 * @param debugLevel AMFICOM-standard debug level, ranging from 1 to 10.
	 */
	void setDebugLevel(final int debugLevel) {
		this.logDebugLevel = DEBUG_LEVEL_MAP.get(new Integer(debugLevel));
	}
}
