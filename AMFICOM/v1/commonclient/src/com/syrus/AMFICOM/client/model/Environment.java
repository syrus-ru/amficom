/*
 * $Id: Environment.java,v 1.5 2005/06/08 10:48:37 bob Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

//import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;

import com.syrus.AMFICOM.client.UI.AMFICOMMetalTheme;
import com.syrus.AMFICOM.client.UI.dialogs.ModuleCodeDialog;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.io.IniFile;

/**
 * Класс Environment используется для хранения общей для приложения информации.
 * 
 * @author $Author: bob $
 * @version $Revision: 1.5 $, $Date: 2005/06/08 10:48:37 $
 * @module commonclient_v1
 */
public final class Environment {

	private static Dispatcher	the_dispatcher				= new Dispatcher();

	private static ArrayList	windows						= new ArrayList();

	private static IniFile		iniFile;
	private static String		iniFileName					= "Application.properties";

	private static JFrame		activeWindow				= null;

	/** Run */
	private static final String	FIELD_RUN					= "run";

	private static final String	RUN_INSTALLED				= "installed";
	private static final String	RUN_NO						= "no";
	private static final String	RUN_YES						= "yes";

	private static String		checkRun					= RUN_NO;

	/** Look and feel */
	private static final String	FIELD_LOOK_AND_FEEL			= "lookAndFeel";

	private static final String	LOOK_AND_FEEL_GTK			= "GTK";
	private static final String	LOOK_AND_FEEL_KUNSTSTOFF	= "Kunststoff";
	private static final String	LOOK_AND_FEEL_METAL			= "Metal";
	private static final String	LOOK_AND_FEEL_MOTIF			= "Motif";
	private static final String	LOOK_AND_FEEL_WINDOWS		= "Windows";

	private static String		lookAndFeel;

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

	private static LookAndFeel	lookAndFeel2;

	static {

		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		MetalTheme metalTheme = new AMFICOMMetalTheme();
		MetalLookAndFeel.setCurrentTheme(metalTheme);
		KunststoffLookAndFeel.setCurrentTheme(metalTheme);

		// load values from properties file
		try {
			iniFile = new IniFile(iniFileName);
			checkRun = iniFile.getValue(FIELD_RUN);
			lookAndFeel = iniFile.getValue(FIELD_LOOK_AND_FEEL);

			lookAndFeel2 = getLookAndFeel2();
			try {
				UIManager.setLookAndFeel(lookAndFeel2);
			} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

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
			initUIConstats();
		} catch (IOException e) {
			System.out.println("Error opening " + iniFileName + " - setting defaults");
			setDefaults();
		}
	}

	private Environment() {
		assert false;
	}

	private static void initUIConstats() {
		UIManager.put(ResourceKeys.SIMPLE_DATE_FORMAT, new SimpleDateFormat(LangModel
				.getString(ResourceKeys.SIMPLE_DATE_FORMAT)));
		UIManager.put(ResourceKeys.ICON_OPEN_SESSION, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/open_session.gif").getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIManager.put(ResourceKeys.ICON_GENERAL, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/general.gif")));
		UIManager.put(ResourceKeys.ICON_DELETE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/delete.gif")));

		UIManager.put(ResourceKeys.ICON_OPEN_FILE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/openfile.gif")));

		UIManager.put(ResourceKeys.ICON_ADD_FILE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/addfile.gif")));
		UIManager.put(ResourceKeys.ICON_REMOVE_FILE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/removefile.gif")));

		UIManager.put(ResourceKeys.ICON_MINI_PATHMODE, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/pathmode.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		
		UIManager.put(ResourceKeys.ICON_MINI_FOLDER, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		"images/folder.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		

		UIManager.put(ResourceKeys.ICON_MINI_PORT, new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage("images/port.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		
		UIManager.put(ResourceKeys.ICON_MINI_TESTING, new ImageIcon(Toolkit.getDefaultToolkit()
			.getImage("images/testing.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));
		
		
		UIManager.put(ResourceKeys.ICON_MINI_MEASUREMENT_SETUP, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/testsetup.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));

		UIManager.put(ResourceKeys.ICON_MINI_RESULT, new ImageIcon(Toolkit.getDefaultToolkit().getImage(
			"images/result.gif").getScaledInstance(15, 15, Image.SCALE_SMOOTH)));

		UIManager.put(ResourceKeys.IMAGE_LOGIN_LOGO, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/logo2.jpg"));
			}
		});

		UIManager.put(ResourceKeys.INSETS_NULL, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new Insets(0, 0, 0, 0);
			}
		});

		UIManager.put(ResourceKeys.INSETS_ICONED_BUTTON, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new Insets(1, 1, 1, 1);
			}
		});

		UIManager.put(ResourceKeys.TABLE_NO_FOCUS_BORDER, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new EmptyBorder(1, 2, 1, 2);
			}
		});

		UIManager.put(ResourceKeys.SIZE_BUTTON, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				return new Dimension(24, 24);
			}
		});

		UIManager.put(ResourceKeys.SIZE_NULL, new Dimension(0, 0));

		UIDefaults defaults = UIManager.getLookAndFeelDefaults();

		defaults.put("Table.background", Color.WHITE);
		defaults.put("Table.foreground", Color.BLACK);
		defaults.put("Table.gridColor", Color.BLACK);
		defaults.put("Viewport.background", Color.WHITE);

//		Font font = new Font("Dialog", Font.PLAIN, 10);

		{
			Font font = defaults.getFont("TextField.font");
			font = new Font(font.getFamily(), font.getStyle(), 12);
			defaults.put("TextField.font", font);
		}

//		defaults.put("ComboBox.font", font);
		defaults.put("ComboBox.background", defaults.get("window"));
		defaults.put("ComboBox.disabledBackground", defaults.get("window"));

		defaults.put(ResourceKeys.COLOR_GRAPHICS_BACKGROUND, Color.WHITE);
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

	public static void setDefaults() {
		checkRun = RUN_YES;
		lookAndFeel = LOOK_AND_FEEL_WINDOWS;
	}

	public static boolean isDebugMode() {
		return debugMode;
	}

	private static LookAndFeel getLookAndFeel2() {
		try {
			LookAndFeel plaf = null;
			if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_METAL))
				plaf = (LookAndFeel) (MetalLookAndFeel.class.newInstance());
			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_KUNSTSTOFF))
				plaf = (LookAndFeel) (KunststoffLookAndFeel.class.newInstance());
			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_WINDOWS))
				plaf = (LookAndFeel) (WindowsLookAndFeel.class.newInstance());
			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_MOTIF))
				plaf = (LookAndFeel) (MotifLookAndFeel.class.newInstance());
//			else if (lookAndFeel.equalsIgnoreCase(LOOK_AND_FEEL_GTK))
//				plaf = (LookAndFeel) (GTKLookAndFeel.class.newInstance());
			else
				return getDefaultLookAndFeel();
			if (plaf.isSupportedLookAndFeel())
				return plaf;
			return getDefaultLookAndFeel();
		} catch (IllegalAccessException iae) {
			return getDefaultLookAndFeel();
		} catch (InstantiationException ie) {
			return getDefaultLookAndFeel();
		}
	}

	/**
	 * Returns a fail-safe pluggable look-and-feel.
	 */
	private static LookAndFeel getDefaultLookAndFeel() {
		try {
			return (LookAndFeel) (Class.forName(UIManager.getSystemLookAndFeelClassName()).newInstance());
		} catch (Exception e) {
			return UIManager.getLookAndFeel();
		}
	}

	public static Dispatcher getDispatcher() {
		return the_dispatcher;
	}

	public static void addWindow(Window window) {
		System.out.println("new window " + window.getName());
		windows.add(window);
	}

	public static void disposeWindow(Window window) {
		System.out.println("close window " + window.getName());
		windows.remove(window);
		window.dispose();
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

	public static final int			MODULE_ADMINISTRATE	= 0;
	public static final int			MODULE_CONFIGURE	= 1;
	public static final int			MODULE_COMPONENTS	= 2;
	public static final int			MODULE_SCHEMATICS	= 3;
	public static final int			MODULE_MAP			= 4;
	public static final int			MODULE_OPTIMIZE		= 5;
	public static final int			MODULE_MODEL		= 6;
	public static final int			MODULE_SCHEDULE		= 7;
	public static final int			MODULE_ANALYSE		= 8;
	public static final int			MODULE_EVALUATE		= 9;
	public static final int			MODULE_SURVEY		= 10;
	public static final int			MODULE_OBSERVE		= 11;
	public static final int			MODULE_MAINTAIN		= 12;
	public static final int			MODULE_PROGNOSIS	= 13;

	protected static final String[]	code				= { "abyrvalg", "", "kirgudu", "", "", "piyavka", "merchen",
			"", "", "opanki", "iiyama", "", "", "parol"	};

	protected static final String[]	name				= { "Администрирование", "Конфигурирование",
			"Редактор компонентов", "Редактор схем", "Редакторо топологических схем", "Проектирование",
			"Моделирование", "Планирование", "Анализ", "Оценка", "Исследование", "Наблюдение", "Сопровождение",
			"Прогнозирование"							};

	public static boolean canRun(int module_index) {
		if (checkRun == null)
			return false;
		if (checkRun.equals(RUN_NO))
			return true;
		if (checkRun.equals(RUN_INSTALLED)) {
			if (code[module_index].length() == 0)
				return true;
			ModuleCodeDialog sDialog = new ModuleCodeDialog(code[module_index], name[module_index]);

			sDialog.setModal(true);

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = sDialog.getSize();
			sDialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

			sDialog.show();

			switch (sDialog.getRetCode()) {
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
