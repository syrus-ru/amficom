/*
 * $Id: Environment.java,v 1.14 2004/09/27 09:53:02 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.io.IniFile;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.plaf.metal.*;

/**
 * Класс Environment используется для хранения общей для приложения информации.
 * 
 * @author $Author: bass $
 * @version $Revision: 1.14 $, $Date: 2004/09/27 09:53:02 $
 * @module generalclient_v1
 */
public final class Environment
{
	/**
	 * @deprecated use Environment.getDispatcher()
	 */
	public static Dispatcher the_dispatcher = new Dispatcher();

	private static ArrayList windows = new ArrayList();

	private static IniFile iniFile;
	private static String iniFileName = "Application.properties";

	private static JFrame activeWindow = null;

	/**
	 * @deprecated
	 */
	private static final String FIELD_CONNECTION = "connection";

	/**
	 * @deprecated
	 */
	public static final String CONNECTION_RISD = "RISD";
	/**
	 * @deprecated
	 */
	public static final String CONNECTION_EMPTY = "Empty";
	/**
	 * @deprecated
	 */
	public static final String CONNECTION_JDBC = "JDBC";

	/**
	 * @deprecated
	 */
	private static String connection = CONNECTION_RISD;

	/** Run */
	private static final String FIELD_RUN = "run";

	private static final String RUN_INSTALLED = "installed";
	private static final String RUN_NO = "no";
	private static final String RUN_YES = "yes";

	private static String checkRun = RUN_NO;

	/** Beep */
	private static final String FIELD_BEEP = "beep";

	private static final String BEEP_DA = "da";
	private static final String BEEP_NET = "net";

	private static boolean beep = false;

	/** Look and feel */
	private static final String FIELD_LOOK_AND_FEEL = "lookAndFeel";

	private static final String LOOK_AND_FEEL_GTK = "GTK";
	private static final String LOOK_AND_FEEL_KUNSTSTOFF = "Kunststoff";
	/**
	 * @deprecated
	 */
	private static final String LOOK_AND_FEEL_KUNSTSTOFF_SHORT = "Kunst";
	private static final String LOOK_AND_FEEL_METAL = "Metal";
	private static final String LOOK_AND_FEEL_MOTIF = "Motif";
	private static final String LOOK_AND_FEEL_WINDOWS = "Windows";

	private static String lookAndFeel;

	/** Domain */
	private static final String FIELD_DOMAIN = "domain";

	private static String domainId = "";

	/** Debug mode */
	private static final String FIELD_DEBUG = "gubed";

	private static boolean debugMode = false;

	/** Log handler */
	private static final String FIELD_LOG_HANDLER = "loghandler";

	private static final String LOG_HANDLER_FILE = "file";
	private static final String LOG_HANDLER_MEMORY = "memory";
	private static final String LOG_HANDLER_SOCKET = "socket";
	private static final String LOG_HANDLER_CONSOLE = "console";

	private static Handler handler;

	/** Log level */
	private static final String FIELD_LOG_LEVEL = "loglevel";

	private static final String LOG_LEVEL_ID_OFF = "off";
	private static final String LOG_LEVEL_ID_SEVERE = "severe";
	private static final String LOG_LEVEL_ID_WARNING = "warning";
	private static final String LOG_LEVEL_ID_INFO = "info";
	private static final String LOG_LEVEL_ID_CONFIG = "config";
	private static final String LOG_LEVEL_ID_FINE = "fine";
	private static final String LOG_LEVEL_ID_FINER = "finer";
	private static final String LOG_LEVEL_ID_FINEST = "finest";
	private static final String LOG_LEVEL_ID_ALL = "all";

	private static Level logLevel;

	/** Log formatter */
	private static final String FIELD_LOG_FORMATTER = "logformatter";

	private static final String LOG_FORMATTER_XML = "xml";
	private static final String LOG_FORMATTER_SIMPLE = "simple";

	private static Formatter formatter;

	/** Log file */
	private static String logFileName;

	public static final Level LOG_LEVEL_SEVERE = Level.SEVERE;
	public static final Level LOG_LEVEL_WARNING = Level.WARNING;
	public static final Level LOG_LEVEL_INFO = Level.INFO;
	public static final Level LOG_LEVEL_CONFIG = Level.CONFIG;
	public static final Level LOG_LEVEL_FINE = Level.FINE;
	public static final Level LOG_LEVEL_FINER = Level.FINER;
	public static final Level LOG_LEVEL_FINEST = Level.FINEST;

	static
	{
		// load values from properties file
		try
		{
			iniFile = new IniFile(iniFileName);
			connection = iniFile.getValue(FIELD_CONNECTION);
			checkRun = iniFile.getValue(FIELD_RUN);
			lookAndFeel = iniFile.getValue(FIELD_LOOK_AND_FEEL);
			domainId = iniFile.getValue(FIELD_DOMAIN);
			if(domainId == null || domainId.length() == 0)
			{
				domainId = "";
			}

			String d_val = iniFile.getValue(FIELD_DEBUG);
			debugMode = (d_val != null);

			String b_val = iniFile.getValue(FIELD_BEEP);
			beep = (b_val == null) ? false : b_val.equals(BEEP_DA);
			
			System.out.println("read connection = " + connection);
			if(connection == null)
				SetDefaults();

			String lf_val = iniFile.getValue(FIELD_LOG_FORMATTER);
			String ll_val = iniFile.getValue(FIELD_LOG_LEVEL);
			String lh_val = iniFile.getValue(FIELD_LOG_HANDLER);
			initLog(lh_val, ll_val, lf_val);
		}
		catch(IOException e)
		{
			System.out.println("Error opening " + iniFileName + " - setting defaults");
			SetDefaults();
		}
	}
	
	private Environment()
	{
	}

	public static String getDomainId()
	{
		return domainId;
	}

	private static void initLog(String lh, String ll, String lf)
	{
		try
		{
			if(lh.equalsIgnoreCase(LOG_HANDLER_FILE))
			{
				logFileName = "./logs/" + System.currentTimeMillis() + ".log";
				handler = new FileHandler(logFileName);
			}
			else
			if(lh.equalsIgnoreCase(LOG_HANDLER_CONSOLE))
			{
				handler = new ConsoleHandler();
			}
			else
			if(lh.equalsIgnoreCase(LOG_HANDLER_SOCKET))
			{
				throw new UnsupportedOperationException();
			}
			else
			if(lh.equalsIgnoreCase(LOG_HANDLER_MEMORY))
			{
				throw new UnsupportedOperationException();
			}
		}
		catch(Exception e)
		{
			handler = new ConsoleHandler();
		}
		
		try
		{
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_OFF))
			{
				logLevel = Level.OFF;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_SEVERE))
			{
				logLevel = Level.SEVERE;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_WARNING))
			{
				logLevel = Level.WARNING;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_INFO))
			{
				logLevel = Level.INFO;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_CONFIG))
			{
				logLevel = Level.CONFIG;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_FINE))
			{
				logLevel = Level.FINE;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_FINER))
			{
				logLevel = Level.FINER;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_FINEST))
			{
				logLevel = Level.FINEST;
			}
			else
			if(ll.equalsIgnoreCase(LOG_LEVEL_ID_ALL))
			{
				logLevel = Level.ALL;
			}
			handler.setLevel(logLevel);
		}
		catch(Exception e)
		{
			handler.setLevel(Level.WARNING);
		}
		
		try
		{
			if(lf.equalsIgnoreCase(LOG_FORMATTER_XML))
			{
				formatter = new XMLFormatter();
			}
			else
			if(lf.equalsIgnoreCase(LOG_FORMATTER_SIMPLE))
			{
				formatter = new SimpleFormatter();
			}
			handler.setFormatter(formatter);
		}
		catch(Exception e)
		{
			handler.setFormatter(new SimpleFormatter());
		}
	}

	public static void log(String text)
	{
		log(Level.CONFIG, text);
	}

	public static void log(Level level, String text)
	{
		log(level, text, null);
	}

	public static void log(Level level, String text, String className)
	{
		log(level, text, className, null);
	}

	public static void log(Level level, String text, String className, String methodName)
	{
		log(level, text, className, methodName, null);
	}

	public static synchronized void log(Level level, String text, String className, String methodName, Throwable throwable)
	{
		LogRecord logRecord;
		logRecord = new LogRecord(level, text);
		logRecord.setLoggerName("");
		if(className != null)
		{
			logRecord.setMillis(System.currentTimeMillis());
			logRecord.setSourceClassName(className);
		}
		if(methodName != null)
			logRecord.setSourceMethodName(methodName);
		if(throwable != null)
			logRecord.setThrown(throwable);
		handler.publish(logRecord);
	}

	static public void SetDefaults()
	{
		connection = CONNECTION_EMPTY;
		checkRun = RUN_YES;
		lookAndFeel = LOOK_AND_FEEL_WINDOWS;
	}

	public static boolean isDebugMode()
	{
		return debugMode;
	}

	public static boolean canBeep()
	{
		return beep;
	}

	/**
	 * @deprecated Method body is empty, do not use anymore.
	 */
	public static void initialize()
	{
	}

	static
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		MetalTheme metalTheme = new AMFICOMMetalTheme();
		MetalLookAndFeel.setCurrentTheme(metalTheme);
		KunststoffLookAndFeel.setCurrentTheme(metalTheme);
	}

	public static LookAndFeel getLookAndFeel()
	{
		try
		{
			LookAndFeel plaf = null;
			if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_METAL))
				plaf = (LookAndFeel) (MetalLookAndFeel.class.newInstance());
			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_KUNSTSTOFF)
				|| lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_KUNSTSTOFF_SHORT))
				plaf = (LookAndFeel)
					(KunststoffLookAndFeel.class.newInstance());
			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_WINDOWS))
				plaf = (LookAndFeel) (WindowsLookAndFeel.class.newInstance());
			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_MOTIF))
				plaf = (LookAndFeel) (MotifLookAndFeel.class.newInstance());
			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_GTK))
				plaf = (LookAndFeel) (GTKLookAndFeel.class.newInstance());
			else
				return getDefaultLookAndFeel();
			if (plaf.isSupportedLookAndFeel())
				return plaf;
			else
				return getDefaultLookAndFeel();
		}
		catch (IllegalAccessException iae)
		{
			return getDefaultLookAndFeel();
		}
		catch (InstantiationException ie)
		{
			return getDefaultLookAndFeel();
		}
	}

	/**
	 * Returns a fail-safe pluggable look-and-feel.
	 */
	private static LookAndFeel getDefaultLookAndFeel()
	{
		try
		{
			return (LookAndFeel) (Class.forName(UIManager.
				getSystemLookAndFeelClassName()).newInstance());
		}
		catch (Exception e)
		{
			return UIManager.getLookAndFeel();
		}
	}

	public static Dispatcher getDispatcher()
	{
		return the_dispatcher;
	}

	/**
	 * @deprecated
	 */
	public static String getConnectionType()
	{
		return connection;
	}

	/**
	 * @deprecated Use {@link ConnectionInterface#getInstance()} instead.
	 */
	static public ConnectionInterface getDefaultConnectionInterface()
	{
		return ConnectionInterface.getInstance();
	}

	/**
	 * @deprecated
	 */
	public static SessionInterface getDefaultSessionInterface(ConnectionInterface connection)
	{
		return new RISDSessionInfo(connection);
	}

	/**
	 * @deprecated Use {@link ApplicationModel#getDataSource(SessionInterface)} instead.
	 */
	public static DataSourceInterface getDefaultDataSourceInterface(final SessionInterface session)
	{
		return ApplicationModel.getInstance().getDataSource(session);
	}

	public static void addWindow(Window window)
	{
		System.out.println("new window " + window.getName());
		windows.add(window);
	}

	public static void disposeWindow(Window window)
	{
		System.out.println("close window " + window.getName());
		windows.remove(window);
		window.dispose();
		if(windows.isEmpty())
		{
			System.out.println("exit process");
			saveProperties();
			try
			{
				SessionInterface.getActiveSession().CloseSession();
			}
			catch (Exception ex)
			{
				// no session to close
			}
			System.exit(0);
		}
	}
	
	private static void saveProperties()
	{
		try 
		{
			domainId = SessionInterface.getActiveSession().getDomainId();
			if (domainId != null)
				iniFile.setValue(FIELD_DOMAIN, domainId);
			iniFile.saveKeys();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}


	public static void setActiveWindow(JFrame window)
	{
		activeWindow = window;
	}

	public static JFrame getActiveWindow()
	{
		return activeWindow;
	}

	public static final int MODULE_ADMINISTRATE = 0;
	public static final int MODULE_CONFIGURE = 1;
	public static final int MODULE_COMPONENTS = 2;
	public static final int MODULE_SCHEMATICS = 3;
	public static final int MODULE_MAP = 4;
	public static final int MODULE_OPTIMIZE = 5;
	public static final int MODULE_MODEL = 6;
	public static final int MODULE_SCHEDULE = 7;
	public static final int MODULE_ANALYSE = 8;
	public static final int MODULE_EVALUATE = 9;
	public static final int MODULE_SURVEY = 10;
	public static final int MODULE_OBSERVE = 11;
	public static final int MODULE_MAINTAIN = 12;
	public static final int MODULE_PROGNOSIS = 13;

	protected static final String[] code =
	{
		"abyrvalg",
		"",
		"kirgudu",
		"",
		"",
		"piyavka",
		"merchen",
		"",
		"",
		"opanki",
		"iiyama",
		"",
		"",
		"parol" };

	protected static final String[] name =
	{
		"Администрирование",
		"Конфигурирование",
		"Редактор компонентов",
		"Редактор схем",
		"Редакторо топологических схем",
		"Проектирование",
		"Моделирование",
		"Планирование",
		"Анализ",
		"Оценка",
		"Исследование",
		"Наблюдение",
		"Сопровождение",
		"Прогнозирование" };

	public static boolean canRun(int module_index)
	{
		if (checkRun == null)
			return false;
		if (checkRun.equals(RUN_NO))
			return true;
		if (checkRun.equals(RUN_INSTALLED))
		{
			if (code[module_index].length() == 0)
				return true;
			ModuleCodeDialog sDialog = new ModuleCodeDialog(code[module_index], name[module_index]);

			sDialog.setModal(true);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = sDialog.getSize();
			sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

			sDialog.show();

			switch (sDialog.getRetCode())
			{
				case ModuleCodeDialog.RET_CANCEL:
					return false;
				case ModuleCodeDialog.RET_OK:
					return true;
				default:
					return false;
			}
		}
		return false;
	}
}
