/*
 * $Id: Environment.java,v 1.10 2005/06/16 09:57:05 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Window;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import javax.swing.JFrame;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.io.IniFile;

/**
 * Класс Environment используется для хранения общей для приложения информации.
 * 
 * @author $Author: bob $
 * @version $Revision: 1.10 $, $Date: 2005/06/16 09:57:05 $
 * @module commonclient_v1
 */
public final class Environment {

	private static Dispatcher	theDispatcher				= new Dispatcher();

	private static ArrayList	windows						= new ArrayList();

	private static IniFile		iniFile;
	private static String		iniFileName					= "Application.properties";

	private static JFrame		activeWindow				= null;

	private static final String	RUN_INSTALLED				= "installed";

	/** Domain */
	private static final String	FIELD_DOMAIN				= "domain";

	private static Identifier	domainId;

	/** Debug mode */
	private static final String	FIELD_DEBUG					= "gubed";

	private static boolean		debugMode					= false;

	/** Log handler */
	private static final String	FIELD_LOG_HANDLER			= "loghandler";

	private static final String	LOG_HANDLER_FILE			= "file";
	private static final String	LOG_HANDLER_MEMORY			= "memory";
	private static final String	LOG_HANDLER_SOCKET			= "socket";
	private static final String	LOG_HANDLER_CONSOLE			= "console";

	private static Handler		handler;

	/** Log level */
	private static final String	FIELD_LOG_LEVEL				= "loglevel";

	private static final String	LOG_LEVEL_ID_OFF			= "off";
	private static final String	LOG_LEVEL_ID_SEVERE			= "severe";
	private static final String	LOG_LEVEL_ID_WARNING		= "warning";
	private static final String	LOG_LEVEL_ID_INFO			= "info";
	private static final String	LOG_LEVEL_ID_CONFIG			= "config";
	private static final String	LOG_LEVEL_ID_FINE			= "fine";
	private static final String	LOG_LEVEL_ID_FINER			= "finer";
	private static final String	LOG_LEVEL_ID_FINEST			= "finest";
	private static final String	LOG_LEVEL_ID_ALL			= "all";

	private static Level		logLevel;

	/** Log formatter */
	private static final String	FIELD_LOG_FORMATTER			= "logformatter";

	private static final String	LOG_FORMATTER_XML			= "xml";
	private static final String	LOG_FORMATTER_SIMPLE		= "simple";

	private static Formatter	formatter;

	/** Log file */
	private static String		logFileName;

	public static final Level	LOG_LEVEL_SEVERE			= Level.SEVERE;
	public static final Level	LOG_LEVEL_WARNING			= Level.WARNING;
	public static final Level	LOG_LEVEL_INFO				= Level.INFO;
	public static final Level	LOG_LEVEL_CONFIG			= Level.CONFIG;
	public static final Level	LOG_LEVEL_FINE				= Level.FINE;
	public static final Level	LOG_LEVEL_FINER				= Level.FINER;
	public static final Level	LOG_LEVEL_FINEST			= Level.FINEST;

	static {

		// load values from properties file
		try {
			iniFile = new IniFile(iniFileName);

			try {
				domainId = new Identifier(iniFile.getValue(FIELD_DOMAIN));
			} catch (Exception e) {
				domainId = Identifier.VOID_IDENTIFIER;
			}

			String d_val = iniFile.getValue(FIELD_DEBUG);
			debugMode = (d_val != null);

			String lf_val = iniFile.getValue(FIELD_LOG_FORMATTER);
			String ll_val = iniFile.getValue(FIELD_LOG_LEVEL);
			String lh_val = iniFile.getValue(FIELD_LOG_HANDLER);
			initLog(lh_val, ll_val, lf_val);			
		} catch (IOException e) {
			System.out.println("Error opening " + iniFileName + " - setting defaults");
		}
	}

	private Environment() {
		assert false;
	}

	public static Identifier getDomainId() {
		return domainId;
	}

	private static void initLog(String lh,
								String ll,
								String lf) {
		try {
			if (lh.equalsIgnoreCase(LOG_HANDLER_FILE)) {
				logFileName = "./logs/" + System.currentTimeMillis() + ".log";
				handler = new FileHandler(logFileName);
			} else if (lh.equalsIgnoreCase(LOG_HANDLER_CONSOLE)) {
				handler = new ConsoleHandler();
			} else if (lh.equalsIgnoreCase(LOG_HANDLER_SOCKET)) {
				throw new UnsupportedOperationException();
			} else if (lh.equalsIgnoreCase(LOG_HANDLER_MEMORY)) { throw new UnsupportedOperationException(); }
		} catch (Exception e) {
			handler = new ConsoleHandler();
		}

		try {
			if (ll.equalsIgnoreCase(LOG_LEVEL_ID_OFF)) {
				logLevel = Level.OFF;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_SEVERE)) {
				logLevel = Level.SEVERE;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_WARNING)) {
				logLevel = Level.WARNING;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_INFO)) {
				logLevel = Level.INFO;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_CONFIG)) {
				logLevel = Level.CONFIG;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_FINE)) {
				logLevel = Level.FINE;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_FINER)) {
				logLevel = Level.FINER;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_FINEST)) {
				logLevel = Level.FINEST;
			} else if (ll.equalsIgnoreCase(LOG_LEVEL_ID_ALL)) {
				logLevel = Level.ALL;
			}
			handler.setLevel(logLevel);
		} catch (Exception e) {
			handler.setLevel(Level.WARNING);
		}

		try {
			if (lf.equalsIgnoreCase(LOG_FORMATTER_XML)) {
				formatter = new XMLFormatter();
			} else if (lf.equalsIgnoreCase(LOG_FORMATTER_SIMPLE)) {
				formatter = new SimpleFormatter();
			}
			handler.setFormatter(formatter);
		} catch (Exception e) {
			handler.setFormatter(new SimpleFormatter());
		}
	}

	public static void log(String text) {
		log(Level.CONFIG, text);
	}

	public static void log(	Level level,
							String text) {
		log(level, text, null);
	}

	public static void log(	Level level,
							String text,
							String className) {
		log(level, text, className, null);
	}

	public static void log(	Level level,
							String text,
							String className,
							String methodName) {
		log(level, text, className, methodName, null);
	}

	public static synchronized void log(Level level,
										String text,
										String className,
										String methodName,
										Throwable throwable) {
		LogRecord logRecord;
		logRecord = new LogRecord(level, text);
		logRecord.setLoggerName("");
		if (className != null) {
			logRecord.setMillis(System.currentTimeMillis());
			logRecord.setSourceClassName(className);
		}
		if (methodName != null)
			logRecord.setSourceMethodName(methodName);
		if (throwable != null)
			logRecord.setThrown(throwable);
		handler.publish(logRecord);
	}

	public static boolean isDebugMode() {
		return debugMode;
	}		

	public static Dispatcher getDispatcher() {
		return theDispatcher;
	}

	public static void addWindow(Window window) {
		System.out.println("new window " + window.getName());
		windows.add(window);
	}

	public static void disposeWindow(Window window) {
		System.out.println("close window " + window.getName());
		windows.remove(window);
		window.dispose();
		checkForExit();
	}
	
	public static void checkForExit() {
		if (windows.isEmpty()) {
			System.out.println("exit process");
			saveProperties();
			System.exit(0);
		}
	}

	private static void saveProperties() {
		try {
			domainId = LoginManager.getDomainId();
			if (domainId != null)
				iniFile.setValue(FIELD_DOMAIN, domainId);
			iniFile.saveKeys();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void setActiveWindow(JFrame window) {
		activeWindow = window;
	}

	public static JFrame getActiveWindow() {
		return activeWindow;
	}

	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_ADMINISTRATE	= 0;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_CONFIGURE	= 1;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_COMPONENTS	= 2;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_SCHEMATICS	= 3;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_MAP			= 4;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_OPTIMIZE		= 5;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_MODEL		= 6;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_SCHEDULE		= 7;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_ANALYSE		= 8;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_EVALUATE		= 9;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_SURVEY		= 10;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_OBSERVE		= 11;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_MAINTAIN		= 12;
	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	public static final int			MODULE_PROGNOSIS	= 13;

	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	protected static final String[]	code				= { "abyrvalg", "", "kirgudu", "", "", "piyavka", "merchen",
			"", "", "opanki", "iiyama", "", "", "parol"	};

	/**
	 * @deprecated as moved to {@link AbstractApplication}
	 */
	protected static final String[]	name				= { "Администрирование", "Конфигурирование",
			"Редактор компонентов", "Редактор схем", "Редакторо топологических схем", "Проектирование",
			"Моделирование", "Планирование", "Анализ", "Оценка", "Исследование", "Наблюдение", "Сопровождение",
			"Прогнозирование"							};
	
}
