package com.syrus.AMFICOM.Client.General.Model;

import com.incors.plaf.kunststoff.KunststoffLookAndFeel;
//import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.io.IniFile;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.plaf.metal.*;

public class Environment extends Singleton
{
	static private Vector windows = new Vector();
	static public Dispatcher the_dispatcher = new Dispatcher();

	static private IniFile iniFile;
	static private String iniFileName = "Application.properties";

	static private String connection = "RISD";
	static private String checkRun = "no";

	static private boolean initiated = false;

	static private JFrame activeWindow = null;

	static private boolean beep = false;

	private static final String BEEP_DA = "da";
	private static final String BEEP_NET = "net";

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

	private static boolean debugMode = false;

	static
	{
		LangModel.initialize();
//		LangModelReport.initialize();
		// load values from properties file
		try
		{
			iniFile = new IniFile(iniFileName);
			System.out.println("read ini file " + iniFileName);
			connection = iniFile.getValue("connection");
			checkRun = iniFile.getValue("run");
			lookAndFeel = iniFile.getValue("lookAndFeel");

			String d_val = iniFile.getValue("gubed");
			debugMode = (d_val != null);

			String b_val = iniFile.getValue("beep");
			beep = (b_val == null) ? false : b_val.equals(BEEP_DA);
			
			System.out.println("read connection = " + connection);
			if(connection == null)
				SetDefaults();
		}
		catch(IOException e)
		{
			System.out.println("Error opening " + iniFileName + " - setting defaults");
			SetDefaults();
		}
	}

	protected Environment()
	{
	}

	static public void SetDefaults()
	{
		connection = "Empty";
		checkRun = "yes";
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

	static public void initialize()
	{
		if(!initiated)
		{
//			SystemLogWriter.initialize();
			initiated = true;
		}
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
			LookAndFeel plaf;
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
			else
				return getDefaultLookAndFeel();
			if (plaf.isSupportedLookAndFeel())
				return plaf;
			else
				return getDefaultLookAndFeel();
		} 
		catch (NullPointerException npe)
		{
			/*
			 * Thrown if no "lookAndFeel" key in the config file was found.
			 */
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
			return (LookAndFeel) (UIManager.getLookAndFeel());
		}
	}

	static public String getConnectionType()
	{
		return connection;
	}

	static public ConnectionInterface getDefaultConnectionInterface()
	{
		System.out.println("	connection = " + connection);
		if(connection.equals("JDBC"))
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
				connection = "RISD";
				return ci;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				return new EmptyConnectionInfo();
			}
		}
		else
		if(connection.equals("RISD"))
			return new RISDConnectionInfo();
		else
		if(connection.equals("Empty"))
			return new EmptyConnectionInfo();
		else
			return new EmptyConnectionInfo();
	}

	static public SessionInterface getDefaultSessionInterface(ConnectionInterface ci)
	{
		if(ci instanceof RISDConnectionInfo)
			return new RISDSessionInfo(ci);
		else
		if(ci instanceof EmptyConnectionInfo)
			return new EmptySessionInfo(ci);
		return null;
	}

	static public DataSourceInterface getDefaultDataSourceInterface(SessionInterface si)
	{
		if(si instanceof RISDSessionInfo)
			return new RISDDataSource(si);
		else
		if(si instanceof EmptySessionInfo)
			return new EmptyDataSource(si);
		return null;
	}

	static public void addWindow(Window window)
	{
		System.out.println("new window " + window.getName());
		windows.add(window);
	}

	static public void disposeWindow(Window window)
	{
		System.out.println("close window " + window.getName());
		windows.remove(window);
		window.dispose();
		if(windows.isEmpty())
		{
			System.out.println("exit process");
			try
			{
				RISDSessionInfo.getActiveSession().CloseSession();
			}
			catch (Exception ex)
			{
				// no session to close
			}
			System.exit(0);
		}
	}

	static public void setActiveWindow(JFrame window)
	{
		activeWindow = window;
	}

	static public JFrame getActiveWindow()
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
		"�����������������",
		"����������������",
		"�������� �����������",
		"�������� ����",
		"��������� �������������� ����",
		"��������������",
		"�������������",
		"������������",
		"������",
		"������",
		"������������",
		"����������",
		"�������������",
		"���������������" };

	public static boolean canRun(int module_index)
	{
		if (checkRun == null)
			return false;
		if (checkRun.equals("no"))
			return true;
		if (checkRun.equals("installed"))
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
