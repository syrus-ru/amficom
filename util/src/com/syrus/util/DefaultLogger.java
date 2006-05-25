/*-
 * $Id: DefaultLogger.java,v 1.5 2006/05/25 18:33:30 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import static com.syrus.util.Log.DEBUGLEVEL08;
import static com.syrus.util.Log.DEBUGLEVEL09;
import static com.syrus.util.Log.DEBUGLEVEL10;
import static com.syrus.util.Log.DEBUG_LEVEL_MAP;
import static java.util.logging.Level.CONFIG;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINER;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.OFF;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

/**
 * @author Tashoyan Arseniy Feliksovich
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2006/05/25 18:33:30 $
 * @module util
 */
public final class DefaultLogger implements Logger {
	private static final String DELIMITER = ".";
	private static final String ERROR = "error";
	private static final String DEBUG = "debug";

	private static final SimpleDateFormat SIMPLE_DATE_FORMAT =
			new SimpleDateFormat("HH:mm:ss.SSS");

	static final String PROPERTY_NAME_PREFIX = "amficom.logging.";

	/*-********************************************************************
	 * Keys.                                                              *
	 **********************************************************************/

	static final String KEY_APPLICATION_NAME = "ApplicationName";
	static final String KEY_HOSTNAME = "Hostname";
	static final String KEY_ECHO_DEBUG = "EchoDebug";
	static final String KEY_ECHO_ERROR = "EchoError";
	static final String KEY_LOG_ONLY_THIS_LEVEL = "LogOnlyThisLevel";
	static final String KEY_LOG_DEBUG_LEVEL = "LogDebugLevel";
	static final String KEY_LOG_PATH = "LogPath";
	static final String KEY_FULL_STE = "FullSte";
	static final String KEY_ALLOW_LEVEL_OUTPUT = "AllowLevelOutput";
	static final String KEY_ALLOW_ANSI_COLOR = "AllowAnsiColor";
	static final String KEY_STACK_TRACE_DATA_SOURCE = "StackTraceDataSource";

	/*-********************************************************************
	 * Devault values.                                                    *
	 **********************************************************************/

	static final String DEFAULT_APPLICATION_NAME = "default";
	static final String DEFAULT_HOSTNAME = "localhost";
	static final String DEFAULT_ECHO_DEBUG = Boolean.toString(false);
	static final String DEFAULT_ECHO_ERROR = Boolean.toString(false);
	static final String DEFAULT_LOG_ONLY_THIS_LEVEL = Boolean.toString(false);
	private static final int DEFAULT_LOG_DEBUG_LEVEL_INT = 5;
	static final String DEFAULT_LOG_DEBUG_LEVEL = Integer.toString(DEFAULT_LOG_DEBUG_LEVEL_INT);
	static final String DEFAULT_LOG_PATH = System.getProperty("user.home") + File.separatorChar + "logs";
	static final String DEFAULT_FULL_STE = Boolean.toString(false);
	static final String DEFAULT_ALLOW_LEVEL_OUTPUT = Boolean.toString(false);
	static final String DEFAULT_ALLOW_ANSI_COLOR = Boolean.toString(false);

	static final String STACK_TRACE_DATA_SOURCE_THREAD = "thread";
	static final String STACK_TRACE_DATA_SOURCE_THROWABLE = "throwable";
	static final String STACK_TRACE_DATA_SOURCE_NONE = "none";

	static final String DEFAULT_STACK_TRACE_DATA_SOURCE = STACK_TRACE_DATA_SOURCE_THREAD;

	/*-********************************************************************
	 * Actual values.                                                     *
	 **********************************************************************/

	private final String applicationName;

	private final String hostname;

	private final boolean echoDebug;

	private final boolean echoError;

	private final boolean logOnlyThisLevel;

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

	private final String logDir;

	/**
	 * Whether full or short form of a stack trace element should be 
	 * printed.
	 */
	private final boolean fullSte;

	/**
	 * Whether loglevel should be printed.
	 */
	private final boolean allowLevelOutput;

	/**
	 * Whether ANSI color should be used when issuing a log record.
	 */
	private final boolean allowAnsiColor;

	/**
	 * <p>For server-side applications, {@link #STACK_TRACE_DATA_SOURCE_THREAD} is
	 * the default.</p>
	 * 
	 * <p>For client-side ones (Jet-compiled to native code),
	 * {@link #STACK_TRACE_DATA_SOURCE_THROWABLE} is the default, since currently
	 * Jet can't fetch current thread's stack trace.</p>
	 *
	 * <p>This field is assumed to hold a reference to a canonical string,
	 * since further it gets compared by reference, not by value.</p>
	 */
	private final String stackTraceDataSource;

	/*-********************************************************************
	 * ANSI escape sequences.                                             *
	 **********************************************************************/

	private static final char ESC = '\033';
	private static final String CSI = "" + ESC + '[';
	@SuppressWarnings("unused")
	private static final String REVERSE_VIDEO_ON = CSI + "?5h";
	@SuppressWarnings("unused")
	private static final String REVERSE_VIDEO_OFF = CSI + "?5l";
	private static final byte ALL_ATTRIBUTES_OFF = 0;
	private static final byte BOLD_ON = 1;
	private static final byte UNDERLINE_ON = 4;
	private static final byte BLINK_ON = 5;
	private static final byte REVERSE_ON = 7;
	private static final byte BOLD_OFF = 22;
	private static final byte UNDERLINE_OFF = 24;
	private static final byte BLINK_OFF = 25;
	private static final byte REVERSE_OFF = 27;
	private static final byte FG_COLOR_BLACK = 30;
	@SuppressWarnings("unused")
	private static final byte FG_COLOR_RED = 31;
	@SuppressWarnings("unused")
	private static final byte FG_COLOR_GREEN = 32;
	@SuppressWarnings("unused")
	private static final byte FG_COLOR_YELLOW = 33;
	@SuppressWarnings("unused")
	private static final byte FG_COLOR_BLUE = 34;
	@SuppressWarnings("unused")
	private static final byte FG_COLOR_MAGENTA = 35;
	@SuppressWarnings("unused")
	private static final byte FG_COLOR_CYAN = 36;
	private static final byte FG_COLOR_WHITE = 37;
	private static final byte FG_COLOR_DEFAULT = 39;
	private static final byte BG_COLOR_BLACK = 40;
	@SuppressWarnings("unused")
	private static final byte BG_COLOR_RED = 41;
	@SuppressWarnings("unused")
	private static final byte BG_COLOR_GREEN = 42;
	@SuppressWarnings("unused")
	private static final byte BG_COLOR_YELLOW = 43;
	@SuppressWarnings("unused")
	private static final byte BG_COLOR_BLUE = 44;
	@SuppressWarnings("unused")
	private static final byte BG_COLOR_MAGENTA = 45;
	@SuppressWarnings("unused")
	private static final byte BG_COLOR_CYAN = 46;
	private static final byte BG_COLOR_WHITE = 47;
	private static final byte BG_COLOR_DEFAULT = 49;

	private static final Map<Integer, TextAttributes> TEXT_ATTRIBUTES_MAPPING =
			new HashMap<Integer, TextAttributes>();

	private static final TextAttributes DEFAULT_TEXT_ATTRIBUTES =
			new TextAttributes(false, false, false, false, FG_COLOR_DEFAULT, BG_COLOR_DEFAULT);


	private String debugLogFileName;
	private String errorLogFileName;
	private PrintWriter errorLog;
	private PrintWriter debugLog;

	private int threadStackDepth = 8;
	private int throwableStackDepth = 6;

	private long logMillis; // current milliseconds

	public DefaultLogger() {
		this(System.getProperties());
	}

	public DefaultLogger(final Properties properties) {
		this.applicationName = properties.getProperty(PROPERTY_NAME_PREFIX + KEY_APPLICATION_NAME, DEFAULT_APPLICATION_NAME);
		this.hostname = properties.getProperty(PROPERTY_NAME_PREFIX + KEY_HOSTNAME, DEFAULT_HOSTNAME);
		this.echoDebug = Boolean.parseBoolean(properties.getProperty(PROPERTY_NAME_PREFIX + KEY_ECHO_DEBUG, DEFAULT_ECHO_DEBUG));
		this.echoError = Boolean.parseBoolean(properties.getProperty(PROPERTY_NAME_PREFIX + KEY_ECHO_ERROR, DEFAULT_ECHO_ERROR));
		this.logOnlyThisLevel = Boolean.parseBoolean(properties.getProperty(PROPERTY_NAME_PREFIX + KEY_LOG_ONLY_THIS_LEVEL, DEFAULT_LOG_ONLY_THIS_LEVEL));
		try {
			this.setLevel(Integer.parseInt(properties.getProperty(PROPERTY_NAME_PREFIX + KEY_LOG_DEBUG_LEVEL, DEFAULT_LOG_DEBUG_LEVEL)));
		} catch (final NumberFormatException nfe) {
			this.setLevel(DEFAULT_LOG_DEBUG_LEVEL_INT);
		}
		this.logDir = properties.getProperty(PROPERTY_NAME_PREFIX + KEY_LOG_PATH, DEFAULT_LOG_PATH);
		this.fullSte = Boolean.parseBoolean(properties.getProperty(PROPERTY_NAME_PREFIX + KEY_FULL_STE, DEFAULT_FULL_STE));
		this.allowLevelOutput = Boolean.parseBoolean(properties.getProperty(PROPERTY_NAME_PREFIX + KEY_ALLOW_LEVEL_OUTPUT, DEFAULT_ALLOW_LEVEL_OUTPUT));
		this.allowAnsiColor = Boolean.parseBoolean(properties.getProperty(PROPERTY_NAME_PREFIX + KEY_ALLOW_ANSI_COLOR, DEFAULT_ALLOW_ANSI_COLOR));
		String probablyStackTraceDataSource = properties.getProperty(PROPERTY_NAME_PREFIX + KEY_STACK_TRACE_DATA_SOURCE, DEFAULT_STACK_TRACE_DATA_SOURCE).intern();
		if (probablyStackTraceDataSource != STACK_TRACE_DATA_SOURCE_THREAD
				&& probablyStackTraceDataSource != STACK_TRACE_DATA_SOURCE_THROWABLE
				&& probablyStackTraceDataSource != STACK_TRACE_DATA_SOURCE_NONE) {
			probablyStackTraceDataSource = DEFAULT_STACK_TRACE_DATA_SOURCE;
		}
		this.stackTraceDataSource = probablyStackTraceDataSource;

		this.logMillis = System.currentTimeMillis();

		this.debugLogFileName = this.createLogFileName(DEBUG);
		this.errorLogFileName = this.createLogFileName(ERROR);
	}

	public void debugMessage(final String message, final Level level) {
		if (this.isLoggable(level)) {
			try {
				synchronized (this) {
					this.checkLogRollover();
					if (this.debugLog == null) {
						this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
					}
					this.logMessage(this.debugLog, message, level);
					this.debugLog.flush();
					if (this.echoDebug) {
						this.logMessage(System.out, message, level);
					}
				}
			} catch (final Exception e) {
				System.out.println("Exception in debug logging: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void debugException(final Throwable t, final Level level) {
		if (this.isLoggable(level)) {
			try {
				synchronized (this) {
					this.checkLogRollover();
					if (this.debugLog == null) {
						this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
					}
					this.logThrowable(this.debugLog, t, level);
					this.debugLog.flush();
					if (this.echoDebug) {
						this.logThrowable(System.out, t, level);
					}
				}
			} catch (final Exception e) {
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
			this.logMessage(this.errorLog, message, SEVERE);
			this.errorLog.flush();

			if (this.echoError) {
				this.logMessage(System.err, message, SEVERE);
			}
		} catch (final Exception e) {
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
			this.logThrowable(this.errorLog, t, SEVERE);
			this.errorLog.flush();

			if (this.echoError) {
				this.logThrowable(System.err, t, SEVERE);
			}
		} catch (final Exception e) {
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

	private void logMessage(final PrintWriter out, final String message, final Level level) {
		if (this.allowAnsiColor) {
			final TextAttributes textAttributes = TEXT_ATTRIBUTES_MAPPING.get(Integer.valueOf(level.intValue()));
			setColor(out, textAttributes == null
					? DEFAULT_TEXT_ATTRIBUTES
					: textAttributes);
		}

		logTimestamp(out);
		this.logLevel(out, level);
		this.logStackTraceElement(out);
		out.println(message);

		if (this.allowAnsiColor) {
			resetColor(out);
		}
	}

	private void logMessage(final PrintStream out, final String message, final Level level) {
		if (this.allowAnsiColor) {
			final TextAttributes textAttributes = TEXT_ATTRIBUTES_MAPPING.get(Integer.valueOf(level.intValue()));
			setColor(out, textAttributes == null
					? DEFAULT_TEXT_ATTRIBUTES
					: textAttributes);
		}

		logTimestamp(out);
		this.logLevel(out, level);
		this.logStackTraceElement(out);
		out.println(message);

		if (this.allowAnsiColor) {
			resetColor(out);
		}
	}

	private void logThrowable(final PrintWriter out, final Throwable t, final Level level) {
		if (this.allowAnsiColor) {
			final TextAttributes textAttributes = TEXT_ATTRIBUTES_MAPPING.get(Integer.valueOf(level.intValue()));
			setColor(out, textAttributes == null
					? DEFAULT_TEXT_ATTRIBUTES
					: textAttributes);
		}

		logTimestamp(out);
		this.logLevel(out, level);
		this.logStackTraceElement(out);
		out.println("Exception (stack trace follows): " + t.getMessage());
		t.printStackTrace(out);

		if (this.allowAnsiColor) {
			resetColor(out);
		}
	}

	private void logThrowable(final PrintStream out, final Throwable t, final Level level) {
		if (this.allowAnsiColor) {
			final TextAttributes textAttributes = TEXT_ATTRIBUTES_MAPPING.get(Integer.valueOf(level.intValue()));
			setColor(out, textAttributes == null
					? DEFAULT_TEXT_ATTRIBUTES
					: textAttributes);
		}

		logTimestamp(out);
		this.logLevel(out, level);
		this.logStackTraceElement(out);
		out.println("Exception (stack trace follows): " + t.getMessage());
		t.printStackTrace(out);

		if (this.allowAnsiColor) {
			resetColor(out);
		}
	}

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
		if (this.stackTraceDataSource == STACK_TRACE_DATA_SOURCE_THREAD) {
			stackTrace = Thread.currentThread().getStackTrace();
			return (stackTrace.length > this.threadStackDepth) ? stackTrace[this.threadStackDepth] : null;
		}

		stackTrace = (new Throwable()).getStackTrace();
		return (stackTrace.length > this.throwableStackDepth) ? stackTrace[this.throwableStackDepth] : null;
	}

	/**
	 * Outputs the string representation of the level specified, padding it
	 * with spaces as necessary.
	 */
	private void logLevel(final PrintWriter out, final Level level) {
		if (this.allowLevelOutput) {
			final String levelName = level.getName();
			out.print('[' + levelName + ']');
			for (int i = levelName.length(); i < Log.MAX_LEVEL_NAME_LENGTH; i ++) {
				out.print(' ');
			}
			out.print(" | ");
		}
	}

	/**
	 * Outputs the string representation of the level specified, padding it
	 * with spaces as necessary.
	 */
	private void logLevel(final PrintStream out, final Level level) {
		if (this.allowLevelOutput) {
			final String levelName = level.getName();
			out.print('[' + levelName + ']');
			for (int i = levelName.length(); i < Log.MAX_LEVEL_NAME_LENGTH; i ++) {
				out.print(' ');
			}
			out.print(" | ");
		}
	}

	private void setColor(final PrintWriter out, final TextAttributes textAttributes) {
		out.print(CSI
				+ (textAttributes.bold ? BOLD_ON : BOLD_OFF) + ';'
				+ (textAttributes.underline ? UNDERLINE_ON : UNDERLINE_OFF) + ';'
				+ (textAttributes.blink ? BLINK_ON : BLINK_OFF) + ';'
				+ (textAttributes.reverse ? REVERSE_ON : REVERSE_OFF) + ';'
				+ textAttributes.fgColor + ';'
				+ textAttributes.bgColor
				+ 'm');
	}

	private void setColor(final PrintStream out, final TextAttributes textAttributes) {
		out.print(CSI
				+ (textAttributes.bold ? BOLD_ON : BOLD_OFF) + ';'
				+ (textAttributes.underline ? UNDERLINE_ON : UNDERLINE_OFF) + ';'
				+ (textAttributes.blink ? BLINK_ON : BLINK_OFF) + ';'
				+ (textAttributes.reverse ? REVERSE_ON : REVERSE_OFF) + ';'
				+ textAttributes.fgColor + ';'
				+ textAttributes.bgColor
				+ 'm');
	}

	private void resetColor(final PrintWriter out) {
		out.print(CSI + ALL_ATTRIBUTES_OFF + 'm');
	}

	private void resetColor(final PrintStream out) {
		out.print(CSI + ALL_ATTRIBUTES_OFF + 'm');
	}

	private String createLogFileName(final String logType) {
		return this.getLogPath(logType) +	File.separator +
				logType +	DELIMITER +
				this.applicationName + DELIMITER +
				this.hostname + ".log";
	}

	private String getLogPath(final String logType) {
		String logPath = this.logDir;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		logPath += File.separator + sdf.format(new Date(this.logMillis)) + File.separator + logType;
		final File file = new File(logPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return logPath;
	}

	void incrementStackDepth(final int increment) {
		if (increment <= 0) {
			throw new IllegalArgumentException(String.valueOf(increment));
		}

		this.threadStackDepth += increment;
		this.throwableStackDepth += increment;
	}

	/**
	 * @param reverseIntValue
	 * @see com.syrus.util.Logger#setLevel(int)
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
		return this.levelValue != offValue && (this.logOnlyThisLevel
				? level.intValue() == this.levelValue
				: level.intValue() >= this.levelValue);
	}

	static {
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(SEVERE.intValue()),
				new TextAttributes(true, false, false, false, FG_COLOR_RED, BG_COLOR_BLACK));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(WARNING.intValue()),
				new TextAttributes(true, false, false, false, FG_COLOR_YELLOW, BG_COLOR_BLACK));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(INFO.intValue()),
				new TextAttributes(true, false, false, false, FG_COLOR_GREEN, BG_COLOR_BLACK));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(CONFIG.intValue()),
				new TextAttributes(true, false, false, false, FG_COLOR_CYAN, BG_COLOR_BLACK));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(FINE.intValue()),
				new TextAttributes(true, false, false, false, FG_COLOR_BLUE, BG_COLOR_WHITE));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(FINER.intValue()),
				new TextAttributes(false, true, false, false, FG_COLOR_BLUE, BG_COLOR_WHITE));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(FINEST.intValue()),
				new TextAttributes(false, true, false, false, FG_COLOR_GREEN, BG_COLOR_WHITE));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(DEBUGLEVEL08.intValue()),
				new TextAttributes(false, false, false, false, FG_COLOR_BLUE, BG_COLOR_WHITE));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(DEBUGLEVEL09.intValue()),
				new TextAttributes(false, false, false, false, FG_COLOR_GREEN, BG_COLOR_WHITE));
		TEXT_ATTRIBUTES_MAPPING.put(
				Integer.valueOf(DEBUGLEVEL10.intValue()),
				new TextAttributes(false, false, false, false, FG_COLOR_CYAN, BG_COLOR_WHITE));
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.5 $, $Date: 2006/05/25 18:33:30 $
	 * @module util
	 */
	private static final class TextAttributes {
		final boolean bold;
		final boolean underline;
		final boolean blink;
		final boolean reverse;
		final byte fgColor;
		final byte bgColor;

		TextAttributes(final boolean bold,
				final boolean underline,
				final boolean blink,
				final boolean reverse,
				final byte fgColor,
				final byte bgColor) {
			if (fgColor != FG_COLOR_DEFAULT
					&& (fgColor < FG_COLOR_BLACK || fgColor > FG_COLOR_WHITE)) {
				throw new IllegalArgumentException("fgColor = " + fgColor);
			}
			if (bgColor != BG_COLOR_DEFAULT
					&& (bgColor < BG_COLOR_BLACK || bgColor > BG_COLOR_WHITE)) {
				throw new IllegalArgumentException("bgColor = " + bgColor);
			}

			this.bold = bold;
			this.underline = underline;
			this.blink = blink;
			this.reverse = reverse;
			this.fgColor = fgColor;
			this.bgColor = bgColor;
		}
	}
}
