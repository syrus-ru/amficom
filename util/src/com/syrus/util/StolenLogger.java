package com.syrus.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

public abstract class StolenLogger implements Logger {
	static final String DELIMITER = ".";
	static final String ERROR = "error";
	static final String DEBUG = "debug";
	static final String KEY_LOG_DEBUG_LEVEL = "LogDebugLevel";
	static final String KEY_ECHO_DEBUG = "EchoDebug";
	static final String KEY_ECHO_ERROR = "EchoError";
	static final String KEY_THIS_LEVEL_ONLY = "LogOnlyThisLevel";
	static final String KEY_LOG_PATH = "LogPath";

	static final String DEFAULT_APPNAME = "defaultApp";
	static final String DEFAULT_HOSTNAME = "defaultHost";
	static final String DEFAULT_LOG_ECHO_DEBUG = "false";
	static final String DEFAULT_LOG_ECHO_ERROR = "false";
	static final String DEFAULT_LOG_ONLY_THIS_LEVEL = "false";
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
			System.out.println("Exception in debug logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public synchronized void debugException(Throwable throwable, int debugLevel) {
		this.checkLogRollover();
		try {
			if (!this.thisLevelOnly && debugLevel <= this.logDebugLevel || debugLevel == this.logDebugLevel) {
				this.debugLog = new PrintWriter(new FileWriter(this.debugLogFileName, true), true);
				logTimeStamp(this.debugLog);
				this.debugLog.println("Exception: " + throwable.getMessage());
				throwable.printStackTrace(this.debugLog);
				this.debugLog.close();
				if (this.echoDebug) {
					echoTimeStamp();
					System.out.println("Exception: " + throwable.getMessage());
					throwable.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			System.out.println("Exception in debug logging: " + e.getMessage());
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
			System.out.println("Exception in error logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public synchronized void errorException(Throwable throwable) {
		this.checkLogRollover();
		try {
			this.errorLog = new PrintWriter(new FileWriter(this.errorLogFileName, true), true);
			logTimeStamp(this.errorLog);
			this.errorLog.println("Exception: " + throwable.getMessage());
				throwable.printStackTrace(this.errorLog);
				this.errorLog.close();
				if (this.echoError) {
					echoTimeStamp();
					System.out.println("Exception: " + throwable.getMessage());
					throwable.printStackTrace();
				}
		}
		catch (Exception e) {
			System.out.println("Exception in error logging: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void checkLogRollover() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String currd = sdf.format(this.logDate);
		String d = sdf.format(new Date());
		if(!currd.equals(d)) {
			this.logDate = new Date();
			this.errorLogFileName = this.createLogFileName(StolenLogger.ERROR);
			this.debugLogFileName = this.createLogFileName(StolenLogger.DEBUG);
		}
	}

	private static void logTimeStamp(PrintWriter pw) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		pw.print(sdf.format(new Date()) + "| ");
	}

	private static void echoTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		System.out.print(sdf.format(new Date()) + "| ");
	}

	private String createLogFileName(String logType) {
				return this.getLogPath(logType) +	File.separator +
								logType +	DELIMITER +
								this.appName + DELIMITER +
								this.hostName + ".log";
	}

	private String getLogPath(String logType) {
		String logPath = this.baseLogPath;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		logPath += File.separator + sdf.format(this.logDate) + File.separator + logType;
		File file = new File(logPath);
		if (!file.exists())
			file.mkdirs();
		return logPath;
	}
}
