
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.CloseAllInternalCommand;
import com.syrus.AMFICOM.Client.General.Command.Config.MapCatalogueCommand;
import com.syrus.AMFICOM.Client.General.Command.Config.NewMapViewCommand;
import com.syrus.AMFICOM.Client.General.Command.Config.ViewMapNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.CreateMapReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.MapMapCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.MapMapNewCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.MapMapOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.MapMapSaveAsCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.MapMapSaveCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapAllCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapSchemeNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOptionsCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapConfigureApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Map.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Map.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Map.MapSchemeTreeFrame;
import com.syrus.AMFICOM.Client.Resource.ConfigDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.MapContext;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Schematics.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.io.IniFile;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

public class MapMDIMain extends JFrame implements OperationListener
{
	protected Dispatcher internal_dispatcher = new Dispatcher();
	public ApplicationContext aContext = new ApplicationContext();

	public String domain_id;

	static IniFile iniFile;
	static String iniFileName = "Configure.properties";

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	BorderLayout borderLayout = new BorderLayout();

	JPanel mainPanel = new JPanel();
	MapToolBar toolBar = new MapToolBar();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	public JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	MapMenuBar menuBar = new MapMenuBar();
  
  CreateMapReportCommand cmrCommand = null;

	public MapMainFrame mapFrame = null;

	public MapMDIMain(ApplicationContext aContext)
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

	public MapMDIMain()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);

		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		this.setTitle(LangModelConfig.getString("MapTitle"));
		this.addComponentListener(new MapMDIMain_this_componentAdapter(this));
		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});

		mainPanel.setLayout(new BorderLayout());//new FlowLayout());
//		mainPanel.setBackground(Color.darkGray);
		desktopPane.setLayout(null);
//		desktopPane.setBackground(Color.darkGray);
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

	void setDefaultModel (ApplicationModel aModel)
	{
		aModel.disable("menuSession");
		aModel.disable("menuSessionNew");
		aModel.disable("menuSessionClose");
		aModel.disable("menuSessionOptions");
		aModel.disable("menuSessionConnection");
		aModel.disable("menuSessionChangePassword");
		aModel.disable("menuSessionDomain");
		aModel.disable("menuExit");

		aModel.disable("menuView");
		aModel.disable("menuViewNavigator");
		aModel.disable("menuViewAttributes");
		aModel.disable("menuViewElements");
		aModel.disable("menuViewSetup");
		aModel.disable("menuViewMap");
		aModel.disable("menuViewMapScheme");
		aModel.disable("menuViewAll");

		aModel.disable("menuMap");
		aModel.disable("menuMapNew");
		aModel.disable("menuMapOpen");
		aModel.disable("menuMapClose");
		aModel.disable("menuMapSave");
		aModel.disable("menuMapSaveAs");
		aModel.disable("menuMapOptions");
		aModel.disable("menuMapCatalogue");

		aModel.disable("menuReport");
		aModel.disable("menuReportOpen");

		aModel.disable("menuHelp");
		aModel.disable("menuHelpContents");
		aModel.disable("menuHelpFind");
		aModel.disable("menuHelpTips");
		aModel.disable("menuHelpStart");
		aModel.disable("menuHelpCourse");
		aModel.disable("menuHelpHelp");
		aModel.disable("menuHelpSupport");
		aModel.disable("menuHelpLicense");
		aModel.disable("menuHelpAbout");

//		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuMap", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);
		aModel.setEnabled("menuReport", true);

//		aModel.setVisible("menuMapSaveAs", false);
	}


	public JDesktopPane getDesktop()
	{
		return desktopPane;
	}

	public void init_module()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth("status", 200);
		statusBar.setWidth("server", 250);
		statusBar.setWidth("session", 200);
		statusBar.setWidth("user", 100);
		statusBar.setWidth("domain", 150);
		statusBar.setWidth("time", 50);

		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("server", LangModel.getString("statusNoConnection"));
		statusBar.setText("session", LangModel.getString("statusNoSession"));
		statusBar.setText("user", LangModel.getString("statusNoUser"));
		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
		statusBar.setText("time", " ");
		statusBar.organize();
		statusBar.setDispatcher(Environment.the_dispatcher);
		statusBar.setDispatcher(internal_dispatcher);

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
		}

		aContext.setDispatcher(internal_dispatcher);
		internal_dispatcher.register(this, "contextchange");
		Environment.the_dispatcher.register(this, "contextchange");

		internal_dispatcher.register(this, "mapframeshownevent");
		internal_dispatcher.register(this, "mapselectevent");
		internal_dispatcher.register(this, "mapcloseevent");
		Environment.the_dispatcher.register(this, "mapaddschemeelementevent");
		Environment.the_dispatcher.register(this, "mapaddschemeevent");

		setDefaultModel(aModel);

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuViewNavigator", new ViewMapNavigatorCommand(desktopPane, aContext, null));
		aModel.setCommand("menuViewAttributes", new ViewMapPropertiesCommand(desktopPane, aContext));
		aModel.setCommand("menuViewElements", new ViewMapElementsCommand(desktopPane, aContext));
		aModel.setCommand("menuViewSetup", new ViewMapSetupCommand(desktopPane, aContext));
		aModel.setCommand("menuViewMap", new NewMapViewCommand(internal_dispatcher, desktopPane, aContext, new MapMapEditorApplicationModelFactory()));
		aModel.setCommand("menuViewMapScheme", new ViewMapSchemeNavigatorCommand(desktopPane, aContext ));
		aModel.setCommand("menuViewAll", new ViewMapAllCommand(desktopPane, aContext, new MapMapEditorApplicationModelFactory()));

		aModel.setCommand("menuMapNew", new MapMapNewCommand(this, aContext));
		aModel.setCommand("menuMapClose", new MapMapCloseCommand(this, internal_dispatcher));
		aModel.setCommand("menuMapOpen", new MapMapOpenCommand(desktopPane, aContext));
		aModel.setCommand("menuMapSave", new MapMapSaveCommand(null, aContext));
		aModel.setCommand("menuMapSaveAs", new MapMapSaveAsCommand(null, aContext));
		aModel.setCommand("menuMapCatalogue", new MapCatalogueCommand(internal_dispatcher, desktopPane, aContext, new MapConfigureApplicationModelFactory()));

    cmrCommand = new CreateMapReportCommand(aContext);
		aModel.setCommand("menuReportOpen", cmrCommand);

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		aModel.fireModelChanged("");

		if(ConnectionInterface.getActiveConnection() != null)
		{
			aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
			if(aContext.getConnectionInterface().isConnected())
			{
//				setConnectionOpened();
				internal_dispatcher.notify(new ContextChangeEvent(
						aContext.getConnectionInterface(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
			}
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
//				setSessionOpened();
				internal_dispatcher.notify(new ContextChangeEvent(
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
		aModel.addListener(menuBar);
		aModel.addListener(toolBar);
		menuBar.setModel(aModel);
		toolBar.setModel(aModel);

		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("mapframeshownevent"))
		{
			mapFrame = (MapMainFrame)ae.getSource();
      
      cmrCommand.setParameter(CreateMapReportCommand.MAP,mapFrame);
      
			ApplicationModel aModel = aContext.getApplicationModel();
			if(aModel != null)
			{
//				aModel.getCommand("menuMapOpen").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapSave").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapSaveAs").setParameter("mapFrame", mapFrame);
			}
		 }
		else
		if(ae.getActionCommand().equals("mapselectevent"))
		{
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuMapSave", true);
			aModel.setEnabled("menuMapSaveAs", true);
			aModel.setEnabled("menuMapClose", true);
			aModel.setEnabled("menuReportOpen", true);

			aModel.fireModelChanged("");
			setTitle(LangModelConfig.getString("MapTitle") + ": " + ((MapContext )ae.getSource()).getName());
		}
		else
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
					((MapPropertyFrame)comp).setVisible(false);
				else if (comp instanceof MapElementsFrame)
					((MapElementsFrame)comp).setVisible(false);
				else if (comp instanceof MapSchemeTreeFrame)
					((MapSchemeTreeFrame)comp).setVisible(false);
			}
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuMapSave", false);
			aModel.setEnabled("menuMapSaveAs", false);
			aModel.setEnabled("menuMapClose", false);
			aModel.setEnabled("menuReportOpen", false);
			aModel.fireModelChanged("");
			setTitle(LangModelConfig.getString("MapTitle"));
		}
		else
		if (ae.getActionCommand().equals("mapaddschemeevent"))
        {
			MapMainFrame fr = (MapMainFrame )Pool.get("environment", "mapmainframe");
			if(fr != null)
				if(fr.getParent() != null)
					if(fr.getParent().equals(desktopPane))
					{
						Dimension dim = desktopPane.getSize();

						String scheme_id = (String)ae.getSource();
						Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
						scheme.unpack();

						SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
						panel.ignore_loading = true;
						panel.setGraphSize(new Dimension());

						SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
						frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
						frame.setTitle(scheme.getName());
						desktopPane.add(frame);

						frame.setLocation(0, 0);
						frame.setSize(dim.width * 4 / 5, dim.height);
						frame.setVisible(true);
						frame.toFront();

						panel.openScheme(scheme);
					}
		}
		else
		if (ae.getActionCommand().equals("mapaddschemeelementevent"))
		{
			MapMainFrame fr = (MapMainFrame )Pool.get("environment", "mapmainframe");
			if(fr != null)
				if(fr.getParent() != null)
					if(fr.getParent().equals(desktopPane))
					{
						String se_id = (String)ae.getSource();
						SchemeElement se = (SchemeElement)Pool.get(SchemeElement.typ, se_id);
						se.unpack();

						SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
						panel.setGraphSize(new Dimension());
						SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
						frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
						frame.setTitle(se.getName());
						desktopPane.add(frame);

						Dimension dim = desktopPane.getSize();

						frame.setLocation(0, 0);
						frame.setSize(dim.width * 4 / 5, dim.height);
						frame.setVisible(true);
						frame.toFront();
						panel.openSchemeElement(se);
					}
        }
		else 
		if(ae.getActionCommand().equals("contextchange"))
		{
			ContextChangeEvent cce = (ContextChangeEvent)ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at " + this.getTitle());
			ApplicationModel aModel = aContext.getApplicationModel();
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface )cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));
					if(!Checker.checkCommandByUserId(
							aContext.getSessionInterface().getUserId(),
							Checker.topologyEditing))
					{
						JOptionPane.showMessageDialog(null, "Недостаточно прав для работы с редактором топологии.", "Ошибка", JOptionPane.OK_OPTION);
//						new SessionCloseCommand(Environment.the_dispatcher, aContext).execute();
						return;
					}
					setSessionOpened();
				}
			}
			if(cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface )cce.getSource();
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(null);
					setSessionClosed();
				}
			}
			if(cce.SESSION_CHANGING)
			{
				statusBar.setText("status", LangModel.getString("statusSettingSession"));
			}
			if(cce.SESSION_CHANGED)
			{
			}
			if(cce.DOMAIN_SELECTED)
			{
				domain_id = (String )cce.getSource();
				statusBar.setText("domain", Pool.getName("domain", domain_id));
				setDomainSelected();
			}
			if(cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					setConnectionOpened();
				}
			}
			if(cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					setConnectionClosed();

				}
			}
			if(cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface)cce.getSource();
				if(aContext.getConnectionInterface().equals(cci))
				{
					setConnectionFailed();
				}
			}
			if(cce.CONNECTION_CHANGING)
			{
				statusBar.setText("status", LangModel.getString("statusConnecting"));
			}
			if(cce.CONNECTION_CHANGED)
			{
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

		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);

		aModel.setEnabled("menuMap", false);
		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuReport", false);

		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.getString("statusDisconnected"));
		statusBar.setText("server", LangModel.getString("statusNoConnection"));
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);

		aModel.setEnabled("menuMap", false);
		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuReport",false);

		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.getString("statusError"));
		statusBar.setText("server", LangModel.getString("statusConnectionError"));
	}

	public void setSessionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.disable("menuSessionNew");
		aModel.enable("menuSessionClose");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");
		aModel.enable("menuSessionDomain");
		aModel.fireModelChanged("");
		domain_id = aContext.getSessionInterface().getDomainId();
		if (domain_id != null && !domain_id.equals(""))
		{
			statusBar.setText("domain", Pool.getName("domain", domain_id));
			setDomainSelected();
		}
		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
		statusBar.setText("user", aContext.getSessionInterface().getUser());
	}

	public void setDomainSelected()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		new CloseAllInternalCommand(desktopPane).execute();

		aContext.getDispatcher().notify(new StatusMessageEvent("Инициализация данных..."));
		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
		new SchemeDataSourceImage(dataSource).LoadSchemes();
		new MapDataSourceImage(dataSource).LoadProtoElements();
		new MapDataSourceImage(dataSource).LoadMaps();
		new ConfigDataSourceImage(dataSource).LoadNet();
		new ConfigDataSourceImage(dataSource).LoadISM();
		aContext.getDispatcher().notify(new StatusMessageEvent("Данные загружены"));

		new ViewMapAllCommand(desktopPane, aContext, new MapMapEditorApplicationModelFactory()).execute();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuMapNew", true);
		aModel.setEnabled("menuMapOpen", true);
		aModel.setEnabled("menuViewNavigator", true);
		aModel.setEnabled("menuViewAttributes", true);
		aModel.setEnabled("menuViewElements", true);
		aModel.setEnabled("menuViewSetup", true);
		aModel.setEnabled("menuViewMap", true);
		aModel.setEnabled("menuViewMapScheme", true);
		aModel.setEnabled("menuViewAll", true);
		aModel.setEnabled("menuReport", true);

		aModel.fireModelChanged("");

	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		setDefaultModel(aModel);
		aModel.enable("menuSessionNew");
		aModel.disable("menuSessionClose");
		aModel.disable("menuSessionOptions");
		aModel.disable("menuSessionChangePassword");	
		aModel.disable("menuSessionDomain");

		aModel.setEnabled("menuMapNew", false);
		aModel.setEnabled("menuMapOpen", false);
		aModel.setEnabled("menuMapSave", false);
		aModel.setEnabled("menuMapSaveAs", false);
		aModel.setEnabled("menuMapClose", false);
		aModel.setEnabled("menuViewNavigator", false);
		aModel.setEnabled("menuViewAttributes", false);
		aModel.setEnabled("menuViewElements", false);
		aModel.setEnabled("menuViewSetup", false);
		aModel.setEnabled("menuViewMap", false);
		aModel.setEnabled("menuViewMapScheme", false);
		aModel.setEnabled("menuViewAll", false);
		aModel.setEnabled("menuReport", false);
		aModel.setEnabled("menuReportOpen", false);
		aModel.fireModelChanged("");

		new CloseAllInternalCommand(desktopPane).execute();

		statusBar.setText("status", LangModel.getString("statusReady"));
		statusBar.setText("session", LangModel.getString("statusNoSession"));
		statusBar.setText("user", LangModel.getString("statusNoUser"));
		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();
		desktopPane.setPreferredSize(desktopPane.getSize());
	}

	void this_windowClosing(WindowEvent e)
	{
		internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
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
			internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
			internal_dispatcher.unregister(this, "contextchange");
			Environment.the_dispatcher.unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}

    public void dispose()
	{
		internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");

		super.dispose();
	}
}

class MapMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	MapMDIMain adaptee;

	MapMDIMain_this_componentAdapter(MapMDIMain adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}


