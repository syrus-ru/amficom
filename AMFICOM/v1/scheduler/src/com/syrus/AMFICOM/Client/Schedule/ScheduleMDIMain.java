
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Schedule;

import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Schedule.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.io.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class ScheduleMDIMain extends JFrame implements OperationListener
{
	private Dispatcher internal_dispatcher = new Dispatcher();
	public ApplicationContext aContext = new ApplicationContext();

	static IniFile iniFile;
	static String iniFileName = "Schedule.properties";

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	BorderLayout borderLayout = new BorderLayout();

	JPanel mainPanel = new JPanel();
	JPanel toolBarPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	public JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	ScheduleMenuBar menuBar = new ScheduleMenuBar();
	ScheduleToolBar toolBar = new ScheduleToolBar();

	public ScheduleMDIMain(ApplicationContext aContext)
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

	public ScheduleMDIMain()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		internal_dispatcher=getInternalDispatcher();
		internal_dispatcher.register(this,"UsualAfterExtended_RootFrame");
		internal_dispatcher.register(this,"UsualAfterFiltration_RootFrame");
		internal_dispatcher.register(this,"FiltrationAfterExtended_RootFrame");
		internal_dispatcher.register(this,"FiltrationAfterUsual_RootFrame");
		internal_dispatcher.register(this,"ExtendedAfterUsual_RootFrame");
		internal_dispatcher.register(this,"ExtendedAfterFiltration_RootFrame");
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		//enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
		setContentPane(mainPanel);
		this.setTitle(LangModelScheduleOld.String("AppTitle"));
		this.addComponentListener(new ScheduleMDIMain_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (1590, 1160);
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height - 34;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width - 10;
		}
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height - 30) / 2);

		mainPanel.setLayout(new BorderLayout());//new FlowLayout());
		mainPanel.setBackground(Color.darkGray);
		desktopPane.setLayout(null);
		//desktopPane.setBackground(Color.darkGray);
		desktopPane.setBackground(SystemColor.control.darker().darker());

		statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBarPanel.setLayout(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		statusBar.add("status");
		statusBar.add("server");
		statusBar.add("session");
		statusBar.add("user");
		statusBar.add("domain");
		statusBar.add("time");

		viewport.setView(desktopPane);
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

//		desktopPane.setPreferredSize(new Dimension(1600,1200)); //very important

		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		this.setJMenuBar(menuBar);
	}

	public void SetDefaults()
	{
	}

	public void init_module()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth("status", 300);
		statusBar.setWidth("server", 250);
		statusBar.setWidth("session", 200);
		statusBar.setWidth("user", 100);
		statusBar.setWidth("domain", 150);
		statusBar.setWidth("time", 50);

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("server", LangModel.String("statusNoConnection"));
		statusBar.setText("session", LangModel.String("statusNoSession"));
		statusBar.setText("user", LangModel.String("statusNoUser"));
		statusBar.setText("domain", LangModel.String("statusNoDomain"));
		statusBar.setText("time", " ");
		statusBar.organize();

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

		statusBar.setDispatcher(Environment.the_dispatcher);
		statusBar.setDispatcher(internal_dispatcher);
		aContext.setDispatcher(internal_dispatcher);
/*
		internal_dispatcher.register(this, "mapopenevent");
		internal_dispatcher.register(this, "mapcloseevent");
		internal_dispatcher.register(this, "contextchange");
		Environment.the_dispatcher.register(this, "contextchange");
*/
		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(internal_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuViewPlan", new MyPlanCommand(this, aContext));
		aModel.setCommand("menuViewTable", new MyTableCommand(this, aContext));
		aModel.setCommand("menuViewTree", new MyTreeCommand(this, aContext));
		aModel.setCommand("menuViewTime", new MyTimeCommand(this, aContext));
		aModel.setCommand("menuViewParam", new MyParamCommand(this, aContext));
		aModel.setCommand("menuViewSave", new MySaveCommand(this, aContext));
		aModel.setCommand("menuViewScheme", new MySchemeCommand(this, aContext));
		aModel.setCommand("menuViewMap", new MyMapCommand(this, aContext));
		aModel.setCommand("menuViewAll", new MyAllCommand(this, aContext));

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);

		aModel.fireModelChanged("");

		if(ConnectionInterface.getActiveConnection() != null)
		{
			aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
			if(aContext.getConnectionInterface().isConnected())
				internal_dispatcher.notify(new ContextChangeEvent(
						aContext.getConnectionInterface(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		else
		{
			aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
//			new CheckConnectionCommand(internal_dispatcher, aContext).execute();
		}
		if(SessionInterface.getActiveSession() != null)
		{
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			aContext.setConnectionInterface(aContext.getSessionInterface().getConnectionInterface());
			if(aContext.getSessionInterface().isOpened())
				internal_dispatcher.notify(new ContextChangeEvent(
						aContext.getSessionInterface(),
						ContextChangeEvent.SESSION_OPENED_EVENT));
		}
		else
		{
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(aContext.getConnectionInterface()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		//new MyAllCommand(this, aContext).execute();
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext!= null)
		{
			if(this.aContext.getDispatcher() != null)
			{
				internal_dispatcher.unregister(this,"UsualAfterExtended_RootFrame");
				internal_dispatcher.unregister(this,"UsualAfterFiltration_RootFrame");
				internal_dispatcher.unregister(this,"FiltrationAfterExtended_RootFrame");
				internal_dispatcher.unregister(this,"FiltrationAfterUsual_RootFrame");
				internal_dispatcher.unregister(this,"ExtendedAfterUsual_RootFrame");
				internal_dispatcher.unregister(this,"ExtendedAfterFiltration_RootFrame");
				internal_dispatcher.unregister(this, "mapopenevent");
				internal_dispatcher.unregister(this, "mapcloseevent");
				internal_dispatcher.unregister(this, "contextchange");
				Environment.the_dispatcher.unregister(this, "contextchange");
			}
		}
		if(aContext != null)
		{
			this.aContext = aContext;
			internal_dispatcher.register(this,"UsualAfterExtended_RootFrame");
			internal_dispatcher.register(this,"UsualAfterFiltration_RootFrame");
			internal_dispatcher.register(this,"FiltrationAfterExtended_RootFrame");
			internal_dispatcher.register(this,"FiltrationAfterUsual_RootFrame");
			internal_dispatcher.register(this,"ExtendedAfterUsual_RootFrame");
			internal_dispatcher.register(this,"ExtendedAfterFiltration_RootFrame");
			internal_dispatcher.register(this, "mapopenevent");
			internal_dispatcher.register(this, "mapcloseevent");
			internal_dispatcher.register(this, "contextchange");
			Environment.the_dispatcher.register(this, "contextchange");
		}
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
		aModel.addListener(menuBar);
		menuBar.setModel(aModel);
		aModel.addListener(toolBar);
		toolBar.setModel(aModel);

		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("ExtendedAfterUsual_RootFrame"))
		{
			Command temp = new MyExtendedCommand(this, aContext);
			temp.execute();
		}
		if(ae.getActionCommand().equals("FiltrationAfterUsual_RootFrame"))
		{
			DataSet ds = (DataSet )ae.getSource();
			Command temp = new MyFiltrationCommand(this, aContext, ds);
			temp.execute();
		}
		if(ae.getActionCommand().equals("UsualAfterExtended_RootFrame"))
		{
			Command temp = new MyTreeCommand(this, aContext);
			temp.execute();
		}
		if(ae.getActionCommand().equals("FiltrationAfterExtended_RootFrame"))
		{
			DataSet ds = (DataSet )ae.getSource();
			Command temp = new MyFiltrationCommand(this, aContext, ds);
			temp.execute();
		}
		if(ae.getActionCommand().equals("UsualAfterFiltration_RootFrame"))
		{
			Command temp3 = new MyParamCommand(this, aContext);
			temp3.execute();
			Command temp1 = new MyTreeCommand(this, aContext);
			temp1.execute();
			Command temp2 = new MyTimeCommand(this, aContext);
			temp2.execute();
			Command temp4 = new MySaveCommand(this, aContext);
			temp4.execute();
		}
		if(ae.getActionCommand().equals("ExtendedAfterFiltration_RootFrame"))
		{
			Command temp3 = new MyParamCommand(this, aContext);
			temp3.execute();
			Command temp1 = new MyExtendedCommand(this, aContext);
			temp1.execute();
			Command temp2 = new MyTimeCommand(this, aContext);
			temp2.execute();
			Command temp4 = new MySaveCommand(this, aContext);
			temp4.execute();
		}
		if(ae.getActionCommand().equals("myevent"))
		{
		}
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
					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));

					setSessionOpened();

//					statusBar.setText("status", LangModel.String("statusReady"));
					statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface().getUser());
				}
			}
			if(cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface)cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(null);

					setSessionClosed();

					statusBar.setText("status", LangModel.String("statusReady"));
					statusBar.setText("session", LangModel.String("statusNoSession"));
					statusBar.setText("user", LangModel.String("statusNoUser"));
				}
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
			if(cce.DOMAIN_SELECTED)
			{
//				domain_id = (String )cce.getSource();
//				statusBar.setText("domain", Pool.getName("domain", domain_id));
				this.desktopPane.removeAll();
				this.desktopPane.repaint();

				setDomainSelected();
			}
		}
		if(ae.getActionCommand().equals("mapcloseevent"))
		{
			for(int i = 0; i < desktopPane.getComponents().length; i++)
			{
				Component comp = desktopPane.getComponent(i);
				if (comp instanceof MapMainFrame)
				{
					((MapMainFrame)comp).setVisible(false);
					((MapMainFrame)comp).setMapContext(null);
					((MapMainFrame)comp).setContext(null);
				}
				else if (comp instanceof MapPropertyFrame)
				{
					((MapPropertyFrame)comp).setVisible(false);
				}
				else if (comp instanceof MapElementsFrame)
				{
					((MapElementsFrame)comp).setVisible(false);
				}
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
		aModel.setEnabled("menuSessionDomain", true);

		aModel.fireModelChanged("");
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);

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

		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusBD")));
		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		new SurveyDataSourceImage(dataSource).LoadTestTypes();
		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		new SurveyDataSourceImage(dataSource).LoadModelingTypes();
		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
		aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusBDFinish")));

		aModel.enable("menuSessionClose");
		aModel.disable("menuSessionNew");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");

		aModel.fireModelChanged("");
		String domain_id = aContext.getSessionInterface().getDomainId();
		if (domain_id != null && !domain_id.equals(""))
		{
			statusBar.setText("domain", Pool.getName("domain", domain_id));
			setDomainSelected();
		}
		new MyAllCommand(this, aContext).execute();
	}

	public void setDomainSelected()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusDomain")));
		new SchemeDataSourceImage(dataSource).LoadNetDirectory();
		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		new ConfigDataSourceImage(dataSource).LoadNet();
		new ConfigDataSourceImage(dataSource).LoadISM();
		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
		new SchemeDataSourceImage(dataSource).LoadSchemes();
		new MapDataSourceImage(dataSource).LoadProtoElements();
		new MapDataSourceImage(dataSource).LoadMaps();
		aContext.getDispatcher().notify(new StatusMessageEvent(LangModelScheduleOld.String("statusDomainFinish")));

		aModel.setEnabled("menuViewPlan", true);
		aModel.setEnabled("menuViewTable", true);
		aModel.setEnabled("menuViewTree", true);
		aModel.setEnabled("menuViewTime", true);
		aModel.setEnabled("menuViewParam", true);
		aModel.setEnabled("menuViewSave", true);
		aModel.setEnabled("menuViewScheme", true);
		aModel.setEnabled("menuViewMap", true);
		aModel.setEnabled("menuViewAll", true);

		aModel.fireModelChanged("");

		statusBar.setText("domain", Pool.getName("domain", aContext.getSessionInterface().getDomainId()));
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionClose", false);
		aModel.enable("menuSessionNew");
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.setEnabled("menuViewPlan", false);
		aModel.setEnabled("menuViewTable", false);
		aModel.setEnabled("menuViewTree", false);
		aModel.setEnabled("menuViewTime", false);
		aModel.setEnabled("menuViewParam", false);
		aModel.setEnabled("menuViewSave", false);
		aModel.setEnabled("menuViewScheme", false);
		aModel.setEnabled("menuViewMap", false);
		aModel.setEnabled("menuViewAll", false);

		aModel.fireModelChanged("");
		statusBar.setText("domain", "");
		aModel.setEnabled("menuSessionDomain", false);
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();
	}

	void this_windowClosing(WindowEvent e)
	{
		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
		internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
	}

	protected void processComponentEvent(ComponentEvent e)
	{
		if (e.getID() == ComponentEvent.COMPONENT_RESIZED)
		{
			new MyPlanCommand(this, aContext).resize();
			new MyParamCommand(this, aContext).resize();
			new MySaveCommand(this, aContext).resize();
			new MyTreeCommand(this, aContext).resize();
			new MyTableCommand(this, aContext).resize();
			new MyTimeCommand(this, aContext).resize();
			new MySchemeCommand(this, aContext).resize();
			new MyMapCommand(this, aContext).resize();
			new MyFiltrationCommand(this, aContext, null).resize();
			new MyExtendedCommand(this, aContext).resize();
		}
		super.processComponentEvent(e);
	}


	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
			//ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			//SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			internal_dispatcher.unregister(this, "contextchange");
			Environment.the_dispatcher.unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
			return;
		}
		super.processWindowEvent(e);
	}


	}

class ScheduleMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	ScheduleMDIMain adaptee;

	ScheduleMDIMain_this_componentAdapter(ScheduleMDIMain adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}

