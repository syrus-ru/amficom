/*
 * $Id: StolenLogger.java,v 1.7 2005/03/04 08:05:49 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class StolenLogger implements Logger {
	static final String DELIMITER = "."; //$NON-NLS-1$
	static final String ERROR = "error"; //$NON-NLS-1$
	static final String DEBUG = "debug"; //$NON-NLS-1$
	static final String KEY_LOG_DEBUG_LEVEL = "LogDebugLevel"; //$NON-NLS-1$
	static final String KEY_ECHO_DEBUG = "EchoDebug"; //$NON-NLS-1$
	static final String KEY_ECHO_ERROR = "EchoError"; //$NON-NLS-1$
	static final String KEY_THIS_LEVEL_ONLY = "LogOnlyThisLevel"; //$NON-NLS-1$
	static final String KEY_LOG_PATH = "LogPath"; //$NON-NLS-1$

	static final String DEFAULT_APPNAME = "defaultApp"; //$NON-NLS-1$
	static final String DEFAULT_HOSTNAME = "defaultHost"; //$NON-NLS-1$
	static final String DEFAULT_LOG_ECHO_DEBUG = "false"; //$NON-NLS-1$
	static final String DEFAULT_LOG_ECHO_ERROR = "false"; //$NON-NLS-1$
	static final String DEFAULT_LOG_ONLY_THIS_LEVEL = "false"; //$NON-NLS-1$
	static final int DEFAULT_LOG_DEBUG_LEVEL = 5;

	private String appName;
	private String hostName;
	boolean echoDebug;
	boolean echoError;
	boolean thisLevelOnly;
	int logDebugLevel;
	String baseLogPath;
	private Date logDate;
	private String debugLogFileName;
	private String errorLogFileName;
	private PrintWriter errorLog;
	private PrintWriter debugLog;

	public StolenLogger(String appName, String hostName) {
		this.appName = (appName != null)?appName:DEFAULT_APPNAME;
		this.hostName = (hostName != null)?hostName:DEFAULT_HOSTNAME;
		this.init();
	}

	private void init() {
		this.initSpec();
		this.logDate = new Date();
		this.debugLogFileName = this.createLogFileName(DEBUG);
		this.errorLogFileName = this.createLogFileName(ERROR);
	}

	abstract void initSpec();

	public synchronized void debugMessage(String mesg, int debugLevel) {
		this.checkLogRollover();
		try {
			if (!this.thisLevelOnly && debugLevel <= this.logDebugLevel || debugLevel == this.logDebugLevel) {
				this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
				logTimeStamp(this.debugLog);
				this.debugLog.println(mesg);
				this.debugLog.close();
				if (this.echoDebug) {
					echoTimeStamp();
					System.out.println(mesg);
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception in debug logging: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	public synchronized void debugException(Throwable throwable, int debugLevel) {
		this.checkLogRollover();
		try {
			if (!this.thisLevelOnly && debugLevel <= this.logDebugLevel || debugLevel == this.logDebugLevel) {
				this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
				logTimeStamp(this.debugLog);
				this.debugLog.println("Exception: " + throwable.getMessage()); //$NON-NLS-1$
				throwable.printStackTrace(this.debugLog);
				this.debugLog.close();
				if (this.echoDebug) {
					echoTimeStamp();
					System.out.println("Exception: " + throwable.getMessage()); //$NON-NLS-1$
					throwable.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception in debug logging: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	public synchronized void errorMessage(String mesg) {
		this.checkLogRollover();
		try {
			this.errorLog = new PrintWriter(new FileWriter(this.errorLogFileName, true), true);
			logTimeStamp(this.errorLog);
			this.errorLog.println(mesg);
			this.errorLog.close();
			this.debugMessage(mesg, Log.DEBUGLEVEL01);
			if (this.echoError) {
				echoTimeStamp();
				System.out.println(mesg);
			}
		}
		catch (Exception e) {
			System.out.println("Exception in error logging: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	public synchronized void errorException(Throwable throwable) {
		this.checkLogRollover();
		try {
			this.errorLog = new PrintWriter(new FileWriter(this.errorLogFileName, true), true);
			logTimeStamp(this.errorLog);
			this.errorLog.println("Exception: " + throwable.getMessage()); //$NON-NLS-1$
				throwable.printStackTrace(this.errorLog);
				this.errorLog.close();
				if (this.echoError) {
					echoTimeStamp();
					System.out.println("Exception: " + throwable.getMessage()); //$NON-NLS-1$
					throwable.printStackTrace();
				}
		}
		catch (Exception e) {
			System.out.println("Exception in error logging: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
	}

	private void checkLogRollover() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
		String currd = sdf.format(this.logDate);
		String d = sdf.format(new Date());
		if(!currd.equals(d)) {
			this.logDate = new Date();
			this.errorLogFileName = this.createLogFileName(StolenLogger.ERROR);
			this.debugLogFileName = this.createLogFileName(StolenLogger.DEBUG);
		}
	}

	private static void logTimeStamp(PrintWriter pw) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
		pw.print(sdf.format(new Date()) + "| "); //$NON-NLS-1$
	}

	private static void echoTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); //$NON-NLS-1$
		System.out.print(sdf.format(new Date()) + "| "); //$NON-NLS-1$
	}

	private String createLogFileName(String logType) {
				return this.getLogPath(logType) +	File.separator +
								logType +	DELIMITER +
								this.appName + DELIMITER +
								this.hostName + ".log"; //$NON-NLS-1$
	}

	private String getLogPath(String logType) {
		String logPath = this.baseLogPath;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); //$NON-NLS-1$
		logPath += File.separator + sdf.format(this.logDate) + File.separator + logType;
		File file = new File(logPath);
		if (!file.exists())
			file.mkdirs();
		return logPath;
	}
}
