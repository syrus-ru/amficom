//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Основное окно запуска модулей АМФИКОМ                      * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        Main\StarterFrame.java                                        * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import oracle.jdeveloper.layout.*;

import com.syrus.io.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Command.Main.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class StarterFrame extends JFrame implements OperationListener
{
	private Dispatcher internalDispatcher = new Dispatcher();
	public ApplicationContext aContext = new ApplicationContext();

	static IniFile iniFile;
	static String iniFileName = "Main.properties";

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

//	public ConnectionInterface ci;
//	public SessionInterface si;

	public BorderLayout borderLayout1 = new BorderLayout();
	public XYLayout xYLayout1 = new XYLayout();

	public StarterPanel starterPanel = new StarterPanel();
	public StarterMenuBar menuBar = new StarterMenuBar();
	public StatusBarModel statusBar = new StatusBarModel(0);
	public StarterToolBar toolBar = new StarterToolBar();
	public JScrollPane starterScrollPane = new JScrollPane();

	public int normWidth;
	public int normHeight;
	public Dimension normDim;
	public Dimension kurzDim;
	public Insets normInsets;
	public Insets kurzInsets;
	public Rectangle normBounds;
	public Point normLoc;
	public Point kurzLoc;

	public StarterFrame(ApplicationContext aContext)
	{
		super();

		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Environment.addWindow(this);
		setContext(aContext);
	}

	public StarterFrame()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		this.getContentPane().setLayout(borderLayout1);
		this.setSize(new Dimension(530, 400));
		this.setTitle(LangModelMain.getString("AppTitle"));
		this.addComponentListener(new StarterFrame_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});


		this.setJMenuBar(menuBar);

		statusBar.add("status");
		statusBar.add("server");
		statusBar.add("session");
		statusBar.add("user");
		statusBar.add("time");

		starterScrollPane.setAutoscrolls(true);

		this.getContentPane().add(statusBar, BorderLayout.SOUTH);
		this.getContentPane().add(starterScrollPane, BorderLayout.CENTER);
		starterScrollPane.setViewportView(starterPanel);

		toolBar.setVisible(false);
		toolBar.setFloatable(false);
		statusBar.setVisible(true);
		starterScrollPane.setVisible(true);
	}

	public void SetDefaults()
	{
	}

	public void initModule()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
	
		statusBar.distribute();
//		statusBar.setWidth("status", 200);
//		statusBar.setWidth("server", 100);
//		statusBar.setWidth("session", 100);
//		statusBar.setWidth("user", 50);
//		statusBar.setWidth("time", 50);

    statusBar.setWidth("status", 100);
    statusBar.setWidth("server", 220);
    statusBar.setWidth("session", 140);
    statusBar.setWidth("user", 80);
    statusBar.setWidth("time", 50);

		statusBar.setText("status", LangModelMain.getString("statusReady"));
		statusBar.setText("server", LangModelMain.getString("statusNoConnection"));
		statusBar.setText("session", LangModelMain.getString("statusNoSession"));
		statusBar.setText("user", LangModelMain.getString("statusNoUser"));
		statusBar.setText("time", " ");
		statusBar.organize();
		statusBar.setDispatcher(Environment.getDispatcher());
		statusBar.setDispatcher(internalDispatcher);

		// load values from properties file
		try
		{
			iniFile = new IniFile(iniFileName);
			System.out.println("read ini file " + iniFileName);
//			objectName = iniFile.getValue("objectName");
		}
		catch(java.io.IOException e)
		{
			System.out.println("Error opening " + iniFileName + " - setting defaults");
			SetDefaults();
		}

		aContext.setDispatcher(internalDispatcher);
		internalDispatcher.register(this, "contextchange");
		Environment.getDispatcher().register(this, "contextchange");

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.getDispatcher(), aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuViewPanel", new ViewPanelCommand(internalDispatcher));

		aModel.setCommand("menuToolsAdmin", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Administrate.Administrate", LangModelMain.getString("menuToolsAdmin")));
		aModel.setCommand("menuToolsConfig", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Configure.Configure", LangModelMain.getString("menuToolsConfig")));
		aModel.setCommand("menuToolsComponents", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Schematics.Elements.ElementsEditor", LangModelMain.getString("menuToolsComponents")));
		aModel.setCommand("menuToolsScheme", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeEditor", LangModelMain.getString("menuToolsScheme")));
		aModel.setCommand("menuToolsMap", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Map.Editor.MapEditor", LangModelMain.getString("menuToolsMap")));
		aModel.setCommand("menuToolsTrace", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Optimize.Optimize", LangModelMain.getString("menuToolsTrace")));
		aModel.setCommand("menuToolsSchedule", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Schedule.Schedule", LangModelMain.getString("menuToolsSchedule")));
		aModel.setCommand("menuToolsSurvey", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Survey.Survey", LangModelMain.getString("menuToolsSurvey")));
		aModel.setCommand("menuToolsModel", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Model.Model", LangModelMain.getString("menuToolsModel")));
		aModel.setCommand("menuToolsMonitor", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Analysis.Reflectometry.Analyse", LangModelMain.getString("menuToolsMonitor")));
		aModel.setCommand("menuToolsAnalyse", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Analysis.Reflectometry.AnalyseExt", LangModelMain.getString("menuToolsAnalyse")));
		aModel.setCommand("menuToolsNorms", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Analysis.Reflectometry.Evaluation", LangModelMain.getString("menuToolsNorms")));
		aModel.setCommand("menuToolsPrognosis", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Prediction.Prediction", LangModelMain.getString("menuToolsPrognosis")));
		aModel.setCommand("menuToolsMaintain", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.Maintenance.Maintenance", LangModelMain.getString("menuToolsMaintain")));
		aModel.setCommand("menuToolsReportBuilder", new ToolStartCommand(internalDispatcher, "com.syrus.AMFICOM.Client.ReportBuilder.ReportMain", LangModelMain.getString("menuToolsReportBuilder")));

		aModel.setCommand("menuHelpAbout", new HelpAboutCommand(this));

		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuViewPanel", true);
		aModel.setEnabled("menuTools", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);

		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuExit", true);

		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuViewPanel", true);

		aModel.setEnabled("menuTools", true);
		aModel.setEnabled("menuToolsAdmin", true);
		aModel.setEnabled("menuToolsConfig", true);
		aModel.setEnabled("menuToolsComponents", true);
		aModel.setEnabled("menuToolsScheme", true);
		aModel.setEnabled("menuToolsMap", true);
		aModel.setEnabled("menuToolsTrace", true);
		aModel.setEnabled("menuToolsSchedule", true);
		aModel.setEnabled("menuToolsSurvey", true);
		aModel.setEnabled("menuToolsModel", true);
		aModel.setEnabled("menuToolsMonitor", true);
		aModel.setEnabled("menuToolsAnalyse", true);
		aModel.setEnabled("menuToolsNorms", true);
		aModel.setEnabled("menuToolsMaintain", true);
		aModel.setEnabled("menuToolsPrognosis", true);
		aModel.setEnabled("menuToolsReportBuilder", true);

		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpContents", false);
		aModel.setEnabled("menuHelpFind", false);
		aModel.setEnabled("menuHelpTips", false);
		aModel.setEnabled("menuHelpStart", false);
		aModel.setEnabled("menuHelpCourse", false);
		aModel.setEnabled("menuHelpHelp", false);
		aModel.setEnabled("menuHelpSupport", false);
		aModel.setEnabled("menuHelpLicense", false);
		aModel.setEnabled("menuHelpAbout", true);

		aModel.fireModelChanged("");

		if(ConnectionInterface.getActiveConnection() != null)
		{
			aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
			if(aContext.getConnectionInterface().isConnected())
				internalDispatcher.notify(new ContextChangeEvent(
						aContext.getConnectionInterface(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		else
		{
			aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
//			new CheckConnectionCommand(internalDispatcher, aContext).execute();
		}
		if(SessionInterface.getActiveSession() != null)
		{
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			aContext.setConnectionInterface(aContext.getSessionInterface().getConnectionInterface());
			if(aContext.getSessionInterface().isOpened())
				internalDispatcher.notify(new ContextChangeEvent(
						aContext.getSessionInterface(),
						ContextChangeEvent.SESSION_OPENED_EVENT));
		}
		else
		{
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(aContext.getConnectionInterface()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		if(aContext.getApplicationModel() == null)
			aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());
	}

	public ApplicationContext getContext()
	{
		return aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
//		aModel = a;
		aModel.addListener(menuBar);
		aModel.addListener(toolBar);
		aModel.addListener(starterPanel);
		menuBar.setModel(aModel);
		toolBar.setModel(aModel);
		starterPanel.setModel(aModel);

		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("contextchange"))
		{
			ContextChangeEvent cce = (ContextChangeEvent)ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
			ApplicationModel aModel = aContext.getApplicationModel();
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
//					aContext.setSessionInterface(ssi);
					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));

//					aContext.getDataSourceInterface().LoadUserDescriptors();
/*
					DataSourceInterface dataSource = aContext.getDataSourceInterface();
					
					dataSource.LoadUserDescriptors();
					dataSource.LoadExecs();
*/
					setSessionOpened();

					statusBar.setText("status", LangModel.String("statusReady"));
					statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface().getUser());
				}
			}
			if(cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					setSessionClosed();
					
					statusBar.setText("status", LangModel.String("statusReady"));
					statusBar.setText("session", LangModel.String("statusNoSession"));
					statusBar.setText("user", LangModel.String("statusNoUser"));
				}
			}
			if(cce.SESSION_CHANGING)
			{
				statusBar.setText("status", LangModelMain.getString("statusSettingSession"));
			}
			if(cce.SESSION_CHANGED)
			{
			}
			if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					setConnectionOpened();

					statusBar.setText("status", LangModel.String("statusReady"));
					statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
				}
			}
			if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					statusBar.setText("status", LangModel.String("statusError"));
					statusBar.setText("server", LangModel.String("statusConnectionError"));

					statusBar.setText("status", LangModel.String("statusDisconnected"));
					statusBar.setText("server", LangModel.String("statusNoConnection"));

					setConnectionClosed();

				}
			}
			if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					statusBar.setText("status", LangModel.String("statusError"));
					statusBar.setText("server", LangModel.String("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if(cce.CONNECTION_CHANGING)
			{
				statusBar.setText("status", LangModel.String("statusConnecting"));
			}
			if(cce.CONNECTION_CHANGED)
			{
			}
			if(cce.VIEW_CHANGED)
			{
				if(! aModel.isSelected("menuViewPanel"))
				{
					normWidth = starterPanel.getWidth();
					normHeight = starterPanel.getHeight();
					normDim = this.getSize();
					normLoc = this.getLocationOnScreen();
					if(kurzLoc == null)
						kurzLoc = new Point(normLoc);
					aModel.setSelected("menuViewPanel", true);
					aModel.fireModelChanged("");
					this.getContentPane().add(toolBar, BorderLayout.NORTH);
					toolBar.setVisible(true);
//					this.getContentPane().remove(starterPanel);
					this.getContentPane().remove(starterScrollPane);
					this.getContentPane().remove(statusBar);
					setJMenuBar(null);
					setResizable(false);
					pack();
					this.setLocation(kurzLoc);
				}
				else
				if(aModel.isSelected("menuViewPanel"))
				{
					kurzLoc = this.getLocationOnScreen();
					this.getContentPane().setLayout(borderLayout1);
					aModel.setSelected("menuViewPanel", false);
					aModel.fireModelChanged("");
					this.getContentPane().remove(toolBar);
//					this.getContentPane().add(starterPanel, BorderLayout.CENTER);
					this.getContentPane().add(starterScrollPane, BorderLayout.CENTER);
					this.getContentPane().add(statusBar, BorderLayout.SOUTH);
					setJMenuBar(menuBar);
					this.setSize(normDim);
					starterPanel.setSize(normWidth, normHeight);
					setResizable(true);
					pack();
					this.setLocation(normLoc);
				}
			}
			if(cce.PASSWORD_CHANGING)
			{
				statusBar.setText("status", LangModel.String("statusChangingPassword"));
			}
			if(cce.PASSWORD_CHANGED)
			{
				statusBar.setText("status", LangModel.String("statusReady"));
			}

		}
	}

	public void setConnectionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}
	
	public void setConnectionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}
	
	public void setConnectionFailed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");
	}
	
	public void setSessionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.disable("menuSessionNew");
		aModel.enable("menuSessionClose");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");
/*
		aModel.enable("menuToolsAdmin");
		aModel.enable("menuToolsConfig");
		aModel.enable("menuToolsTrace");
		aModel.enable("menuToolsSchedule");
		aModel.enable("menuToolsSurvey");
		aModel.enable("menuToolsModel");	
		aModel.enable("menuToolsMonitor");	
		aModel.enable("menuToolsAnalyse");
		aModel.enable("menuToolsNorms");	
*/
		aModel.fireModelChanged("");
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.enable("menuSessionNew");
		aModel.disable("menuSessionClose");
		aModel.disable("menuSessionOptions");
		aModel.disable("menuSessionChangePassword");	
/*
		aModel.setEnabled("menuToolsAdmin", false);
		aModel.setEnabled("menuToolsConfig", false);
		aModel.setEnabled("menuToolsTrace", false);
		aModel.setEnabled("menuToolsSchedule", false);
		aModel.setEnabled("menuToolsSurvey", false);
		aModel.setEnabled("menuToolsModel", false);
		aModel.setEnabled("menuToolsMonitor", false);
		aModel.setEnabled("menuToolsAnalyse", false);
		aModel.setEnabled("menuToolsNorms", false);
*/
		aModel.fireModelChanged("");
		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("session", LangModel.String("statusNoSession"));
		statusBar.setText("user", LangModel.String("statusNoUser"));
	}

	public Dispatcher getInternalDispatcher()
	{
		return internalDispatcher;
	}

	void this_componentShown(ComponentEvent e)
	{
		initModule();
	}

	void this_windowClosing(WindowEvent e)
	{
		internalDispatcher.unregister(this, "contextchange");
		Environment.getDispatcher().unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e) 
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) 
		{
			Environment.setActiveWindow(this);
//			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
//			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) 
		{
			internalDispatcher.unregister(this, "contextchange");
			Environment.getDispatcher().unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}
}

class StarterFrame_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	StarterFrame adaptee;

	StarterFrame_this_componentAdapter(StarterFrame adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}

