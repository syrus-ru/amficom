/*
 * $Id: Environment.java,v 1.13 2004/09/03 08:17:18 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.General.Model;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;

import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.EmptyConnectionInfo;
import com.syrus.AMFICOM.Client.General.EmptySessionInfo;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.RISDConnectionInfo;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Singleton;
import com.syrus.AMFICOM.Client.General.UI.AMFICOMMetalTheme;
import com.syrus.AMFICOM.Client.General.UI.ModuleCodeDialog;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDDataSource;
import com.syrus.io.IniFile;

import java.awt.Dimension;
import java.awt.Toolkit;
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

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

/**
 * Класс $RCSfile: Environment.java,v $ используется для хранения общей для приложения информации
 * 
 * 
 * 
 * @version $Revision: 1.13 $, $Date: 2004/09/03 08:17:18 $
 * @author $Author: krupenn $
 * @see
 */
public class Environment extends Singleton
{
	/**
	 * @deprecated use Environment.getDispatcher()
	 */
	public static Dispatcher the_dispatcher = new Dispatcher();

	private static ArrayList windows = new ArrayList();

	private static IniFile iniFile;
	private static String iniFileName = "Application.properties";

	private static JFrame activeWindow = null;

	/** Connection */
	private static final String FIELD_CONNECTION = "connection";

	public static final String CONNECTION_RISD = "RISD";
	public static final String CONNECTION_EMPTY = "Empty";
	public static final String CONNECTION_JDBC = "JDBC";

	private static String connection = CONNECTION_RISD;

	/** Run */
	private static final String FIELD_RUN = "run";

	private static final String RUN_INSTALLED = "installed";
	private static final String RUN_NO = "no";
	private static final String RUN_YES = "yes";

	private static String checkRun = RUN_NO;

	/** Beep */
	private static final String FIELD_BEEP = "neep";

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
			System.out.println("read ini file " + iniFileName);
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

	protected Environment()
	{
		// nothing
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
		LookAndFeel laf = getDefaultLookAndFeel();
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
//			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_GTK))
//				plaf = (LookAndFeel) (GTKLookAndFeel.class.newInstance());
			if ((plaf!=null) && (plaf.isSupportedLookAndFeel()))
				laf = plaf;
//			else
//				return getDefaultLookAndFeel();
		} 
		catch (NullPointerException npe)
		{
			/*
			 * Thrown if no "lookAndFeel" key in the config file was found.
			 */
			//return getDefaultLookAndFeel();
		}
		catch (IllegalAccessException iae)
		{
			//return getDefaultLookAndFeel();
		}
		catch (InstantiationException ie)
		{
			//return getDefaultLookAndFeel();
		}
		return laf;
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

	public static String getConnectionType()
	{
		return connection;
	}

	static public ConnectionInterface getDefaultConnectionInterface()
	{
		System.out.println("	connection = " + connection);
		if(connection.equals(CONNECTION_JDBC))
		{
			try
			{
				Class cl = Class.forName(
						"com.syrus.AMFICOM.Client.General.JDBCConnectionInfo",
						true,
						Environment.class.getClassLoader());
//				Constructor cons = cl.getConstructor(new Class[] {});
				ConnectionInterface ci = (ConnectionInterface )cl.newInstance();
//				ConnectionInterface ci = (ConnectionInterface )(cons.newInstance(new Object[] {}));
				connection = CONNECTION_RISD;
				return ci;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return new EmptyConnectionInfo();
			}
		}
		else
		if(connection.equals(CONNECTION_RISD))
			return new RISDConnectionInfo();
		else
		if(connection.equals(CONNECTION_EMPTY))
			return new EmptyConnectionInfo();
		else
			return new EmptyConnectionInfo();
	}

	public static SessionInterface getDefaultSessionInterface(ConnectionInterface ci)
	{
		if(ci instanceof RISDConnectionInfo)
			return new RISDSessionInfo(ci);
		else
		if(ci instanceof EmptyConnectionInfo)
			return new EmptySessionInfo(ci);
		return null;
	}

	public static DataSourceInterface getDefaultDataSourceInterface(SessionInterface si)
	{
		if(si instanceof RISDSessionInfo)
			return new RISDDataSource(si);
		else
		if(si instanceof EmptySessionInfo)
			return new EmptyDataSource(si);
		return null;
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
