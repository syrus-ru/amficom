
// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Configure;

import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Map.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Map.MapElementsFrame;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.SessionInterface;

import com.syrus.AMFICOM.Client.General.Command.CloseAllInternalCommand;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;

import com.syrus.AMFICOM.Client.General.Command.Config.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Command.Window.*;

import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapSchemeNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Command.Map.ViewMapSetupCommand;

import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;

import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapConfigureApplicationModelFactory;

import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;

import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.ConfigDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;

import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;

import com.syrus.AMFICOM.Client.Resource.ISM.KIS;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.KISType;

import com.syrus.AMFICOM.Client.Resource.Network.Equipment;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;

import com.syrus.AMFICOM.Client.General.Scheme.SchemePanelNoEdition;
import com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeViewerFrame;

import com.syrus.io.IniFile;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.SystemColor;

import java.awt.event.WindowEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

public class ConfigureMDIMain extends JFrame implements OperationListener
{
	protected Dispatcher internal_dispatcher = new Dispatcher();
	public ApplicationContext aContext = new ApplicationContext();

	public String domain_id = null;

	static IniFile iniFile;
	static String iniFileName = "Configure.properties";

	static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	BorderLayout borderLayout = new BorderLayout();

	JPanel mainPanel = new JPanel();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	protected JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	ConfigureMenuBar menuBar = new ConfigureMenuBar();
	ConfigureToolBar toolBar = new ConfigureToolBar();

	public ConfigureMDIMain(ApplicationContext aContext)
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

	public ConfigureMDIMain()
	{
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		setTitle(LangModelConfig.String("AppTitle"));
		addComponentListener(new ConfigureMDIMain_this_componentAdapter(this));

/*
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				this_windowClosing(e);
			}
		});
*/
		mainPanel.setLayout(new BorderLayout());//new FlowLayout());
		mainPanel.setBackground(Color.darkGray);
		desktopPane.setLayout(null);
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

		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	    mainPanel.add(toolBar, BorderLayout.NORTH);

		setJMenuBar(menuBar);

		Component c = this.getRootPane();

		c.addKeyListener(new KeyListener()
			{
				public void keyPressed(KeyEvent e)
				{
					if(e.getKeyCode() == e.VK_T &&
						e.getModifiers() == e.CTRL_MASK + e.ALT_MASK)
						{
							new ImportCatalogCommand(aContext).execute();
						}
				}
				public void keyReleased(KeyEvent e) {}
				public void keyTyped(KeyEvent e) {}
			});

	}

	public void SetDefaults()
	{
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

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("server", LangModel.String("statusNoConnection"));
		statusBar.setText("session", LangModel.String("statusNoSession"));
		statusBar.setText("user", LangModel.String("statusNoUser"));
		statusBar.setText("domain", LangModel.String("statusNoDomain"));
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
			SetDefaults();
		}

		aContext.setDispatcher(internal_dispatcher);
		internal_dispatcher.register(this, "contextchange");
		Environment.the_dispatcher.register(this, "contextchange");
		internal_dispatcher.register(this, "mapcloseevent");
		internal_dispatcher.register(this, "addschemeevent");
		internal_dispatcher.register(this, "addschemeelementevent");
		Environment.the_dispatcher.register(this, "mapaddschemeevent");
		Environment.the_dispatcher.register(this, "mapaddschemeelementevent");

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.the_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuViewNavigator", new ViewConfNavigatorCommand(desktopPane, aContext, null));
		aModel.setCommand("menuViewAttributes", new ViewMapPropertiesCommand(desktopPane, aContext));
		aModel.setCommand("menuViewElements", new ViewMapElementsCommand(desktopPane, aContext));
		aModel.setCommand("menuViewMapScheme", new ViewMapSchemeNavigatorCommand(desktopPane, aContext ));
//		aModel.setCommand("menuViewMapScheme", new ViewMapSetupCommand(desktopPane, aContext));

		aModel.setCommand("menuSchemeNetScheme", new OpenSchemeEditorCommand(Environment.the_dispatcher, aContext, null));
		aModel.setCommand("menuSchemeNetElement", new OpenComponentEditorCommand(Environment.the_dispatcher, aContext, null));
		aModel.setCommand("menuSchemeNetView", new OpenMapEditorCommand(Environment.the_dispatcher, aContext, null));
		aModel.setCommand("menuSchemeNetCatalogue", new MapCatalogueCommand(internal_dispatcher, desktopPane, aContext, new MapConfigureApplicationModelFactory()));
//		aModel.setCommand("menuSchemeNetView", new ConfigNewMapViewCommand(internal_dispatcher, desktopPane, aContext, new MapConfigureApplicationModelFactory()));
		aModel.setCommand("menuSchemeNetOpen", new MapConfigViewOpenCommand(internal_dispatcher, desktopPane, aContext, new MapConfigureApplicationModelFactory()));
		aModel.setCommand("menuSchemeNetOpenScheme", new SchemeViewOpenCommand(desktopPane, aContext));

		aModel.setCommand("menuSchemeJCatalogue", new MapJCatalogueCommand(/*Environment.the_dispatcher*/internal_dispatcher, desktopPane, aContext, new MapConfigureApplicationModelFactory()));
//		aModel.setCommand("menuSchemeJLayout", new NewJMapViewCommand(/*Environment.the_dispatcher*/internal_dispatcher, desktopPane, aContext, new MapConfigureApplicationModelFactory()));
//		aModel.setCommand("menuSchemeJOpen", new MapJViewOpenCommand(/*Environment.the_dispatcher*/internal_dispatcher, desktopPane, aContext, new MapConfigureApplicationModelFactory()));

//		aModel.setCommand("menuObjectNavigator", new OpenObjectFrameCommand(
		aModel.setCommand("menuObjectDomain", new OpenDomainsCommand(desktopPane, aContext, "domain"));
		aModel.setCommand("menuObjectNetDir", new OpenCatalogCommand(desktopPane, aContext, EquipmentType.typ, EquipmentType.class, Checker.catalogTCviewing));
		aModel.setCommand("menuObjectNetCat", new OpenCatalogCommand(desktopPane, aContext, Equipment.typ, Equipment.class, Checker.catalogTCviewing));
		aModel.setCommand("menuObjectJDir", new OpenCatalogCommand(desktopPane, aContext, KISType.typ, KISType.class, Checker.catalogCMviewing));
		aModel.setCommand("menuObjectJCat", new OpenCatalogCommand(desktopPane, aContext, KIS.typ, KIS.class, Checker.catalogCMviewing));
/*
		aModel.setCommand("menuObjectNetDir", new OpenNetDirectoryCommand(desktopPane, aContext, "equipment"));
		aModel.setCommand("menuObjectNetCat", new OpenNetCatalogCommand(desktopPane, aContext, "equipment"));
		aModel.setCommand("menuObjectJDir", new OpenJDirectoryCommand(desktopPane, aContext, "KIS"));
		aModel.setCommand("menuObjectJCat", new OpenJCatalogCommand(desktopPane, aContext, "KIS"));

		aModel.setCommand("menuNetDirEquipment", new OpenNetDirectoryCommand(desktopPane, aContext, "equipment"));
		aModel.setCommand("menuNetCatEquipment", new OpenNetCatalogCommand(desktopPane, aContext, "equipment"));
		aModel.setCommand("menuNetCatLink", new OpenNetCatalogCommand(desktopPane, aContext, "link"));
		aModel.setCommand("menuNetCatCable", new OpenNetCatalogCommand(desktopPane, aContext, "cable"));
//		aModel.setCommand("menuNetCatResource", new OpenNetCatalogCommand(desktopPane, aContext, "resource"));
//		aModel.setCommand("menuNetCatTestPoint", new OpenJCatalogCommand(desktopPane, aContext, "testpoint"));
//		aModel.setCommand("menuNetCatTPGroup", new OpenJCatalogCommand(desktopPane, aContext, "testpointgroup"));

		aModel.setCommand("menuJDirKIS", new OpenJDirectoryCommand(desktopPane, aContext, "KIS"));
		aModel.setCommand("menuJCatKIS", new OpenJCatalogCommand(desktopPane, aContext, "KIS"));
		aModel.setCommand("menuJCatPath", new OpenJCatalogCommand(desktopPane, aContext, "path"));
//		aModel.setCommand("menuJCatAccessPoint", new OpenJCatalogCommand(desktopPane, aContext, "accesspoint"));
*/
//		aModel.setCommand("menuMaintainAlertRule", new OpenMaintenanceCommand(desktopPane, aContext, this));

		aModel.setCommand("menuWindow", new WindowCloseCommand(desktopPane));
		aModel.setCommand("menuWindowClose", new WindowCloseCommand(desktopPane));
		aModel.setCommand("menuWindowCloseAll", new WindowCloseAllCommand(desktopPane));
		aModel.setCommand("menuWindowTileHorizontal", new WindowTileHorizontalCommand(desktopPane));
		aModel.setCommand("menuWindowTileVertical", new WindowTileVerticalCommand(desktopPane));
		aModel.setCommand("menuWindowCascade", new WindowCascadeCommand(desktopPane));
		aModel.setCommand("menuWindowArrange", new WindowArrangeCommand(desktopPane));
		aModel.setCommand("menuWindowArrangeIcons", new WindowArrangeIconsCommand(desktopPane));
		aModel.setCommand("menuWindowMinimizeAll", new WindowMinimizeAllCommand(desktopPane));
		aModel.setCommand("menuWindowRestoreAll", new WindowRestoreAllCommand(desktopPane));
		aModel.setCommand("menuWindowList", new WindowListCommand(desktopPane));

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuViewNavigator", false);
		aModel.setEnabled("menuScheme", true);
		aModel.disable("menuSchemeNetElement");
		aModel.disable("menuSchemeNetScheme");
		aModel.disable("menuSchemeNetView");
		aModel.disable("menuSchemeNetOpenScheme");
		aModel.disable("menuSchemeNetOpen");
/*    aModel.setEnabled("menuSchemeNetElement", true);
    aModel.setEnabled("menuSchemeNetScheme", true);
    aModel.setEnabled("menuSchemeNetView", true);
    aModel.setEnabled("menuSchemeNetOpenScheme", true);
    aModel.setEnabled("menuSchemeNetOpen", true);*/

		aModel.setEnabled("menuObject", true);
		aModel.setEnabled("menuObjectDomain", false);
		aModel.setEnabled("menuObjectNetDir", false);
		aModel.setEnabled("menuObjectNetCat", false);
		aModel.setEnabled("menuObjectJDir", false);
		aModel.setEnabled("menuObjectJCat", false);
		aModel.setEnabled("menuNet", false);
		aModel.setEnabled("menuJ", false);
		aModel.setEnabled("menuMaintenance", true);
		aModel.enable("menuMaintain");
		aModel.setEnabled("menuTools", false);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);

		aModel.fireModelChanged("");

		if(ConnectionInterface.getActiveConnection() != null)
		{
			aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
			if(aContext.getConnectionInterface().isConnected())
//				setConnectionOpened();
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
//		aModel = a;
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
//					if(aContext.getSessionInterface() instanceof RISDSessionInfo)
//						aContext.setDataSourceInterface(new RISDMapDataSource(aContext.getSessionInterface()));
//					else
//						aContext.setDataSourceInterface(new EmptyMapDataSource(aContext.getSessionInterface()));
                    if(!Checker.checkCommandByUserId(
							aContext.getSessionInterface().getUserId(),
							Checker.enterConfiguringModul))
                    {
						new SessionCloseCommand(Environment.the_dispatcher, aContext).execute();
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
				statusBar.setText("status", LangModel.String("statusSettingSession"));
			}
			if(cce.SESSION_CHANGED)
			{
			}
			if(cce.DOMAIN_SELECTED)
			{
				domain_id = (String )cce.getSource();
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
				statusBar.setText("status", LangModel.String("statusConnecting"));
			}
			if(cce.CONNECTION_CHANGED)
			{
			}
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
						//ElementsEditorFrame frame = new ElementsEditorFrame(aContext, panel);
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
		if (ae.getActionCommand().equals("addschemeevent"))
		{
			Dimension dim = desktopPane.getSize();

			String scheme_id = (String)ae.getSource();
			Scheme scheme = (Scheme)Pool.get(Scheme.typ, scheme_id);
			scheme.unpack();

			SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
			panel.ignore_loading = true;
			//ElementsEditorFrame frame = new ElementsEditorFrame(aContext, panel);
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
		else
		if (ae.getActionCommand().equals("addschemeelementevent"))
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

	public void setConnectionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

    aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.String("statusDisconnected"));
		statusBar.setText("server", LangModel.String("statusNoConnection"));
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.String("statusError"));
		statusBar.setText("server", LangModel.String("statusConnectionError"));
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

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("session", sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
		statusBar.setText("user", aContext.getSessionInterface().getUser());
	}

	public void setDomainSelected()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		new CloseAllInternalCommand(desktopPane).execute();

		aContext.getDispatcher().notify(new StatusMessageEvent(LangModelConfig.String("status_initing_data")));
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		new SchemeDataSourceImage(dataSource).LoadNetDirectory();
		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		new ConfigDataSourceImage(dataSource).LoadNet();
		new ConfigDataSourceImage(dataSource).LoadISM();
		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
		new SchemeDataSourceImage(dataSource).LoadSchemes();
		new MapDataSourceImage(dataSource).LoadProtoElements();
		new MapDataSourceImage(dataSource).LoadMaps();
		aContext.getDispatcher().notify(new StatusMessageEvent(LangModelConfig.String("status_init_data_complete")));
/*
		new DirectoryToFile().writeAll();
*/

//		aModel.enable("menuView");
		aModel.enable("menuViewNavigator");

//		aModel.enable("menuScheme");
		aModel.enable("menuSchemeNetElement");
		aModel.enable("menuSchemeNetScheme");
		aModel.enable("menuSchemeNetView");
		aModel.enable("menuSchemeNetOpenScheme");
		aModel.enable("menuSchemeNetOpen");

//		aModel.enable("menuMaintain");
		aModel.enable("menuMaintainType");
		aModel.enable("menuMaintainEvent");
		aModel.enable("menuMaintainAlarmRule");
		aModel.enable("menuMaintainMessageRule");
		aModel.enable("menuMaintainAlertRule");
		aModel.enable("menuMaintainReactRule");
		aModel.enable("menuMaintainRule");
		aModel.enable("menuMaintainCorrectRule");

//			aModel.setEnabled("menuObject", true);
		aModel.setEnabled("menuObjectDomain", true);
		aModel.setEnabled("menuObjectNetDir", true);
		aModel.setEnabled("menuObjectNetCat", true);
		aModel.setEnabled("menuObjectJDir", true);
		aModel.setEnabled("menuObjectJCat", true);

//		aModel.enable("menuNet");
		aModel.enable("menuNetCat");
		aModel.enable("menuNetCatEquipment");
		aModel.enable("menuNetCatLink");
		aModel.enable("menuNetCatResource");
		aModel.enable("menuNetCatTestPoint");
		aModel.enable("menuNetCatTPGroup");

//		aModel.enable("menuJ");
		aModel.enable("menuJCat");
		aModel.enable("menuJCatKIS");
		aModel.enable("menuJCatPath");
		aModel.enable("menuJCatAccessPoint");

		aModel.fireModelChanged("");

		statusBar.setText("domain", Pool.getName("domain", domain_id));

	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.enable("menuSessionNew");
		aModel.disable("menuSessionClose");
		aModel.disable("menuSessionOptions");
		aModel.disable("menuSessionChangePassword");
		aModel.disable("menuSessionDomain");

//		aModel.disable("menuView");
		aModel.disable("menuViewNavigator");

//		aModel.disable("menuScheme");
		aModel.disable("menuSchemeNetElement");
		aModel.disable("menuSchemeNetScheme");
		aModel.disable("menuSchemeNetView");
		aModel.disable("menuSchemeNetOpenScheme");
		aModel.disable("menuSchemeNetOpen");

//		aModel.disable("menuMaintain");
		aModel.disable("menuMaintainType");
		aModel.disable("menuMaintainEvent");
		aModel.disable("menuMaintainAlarmRule");
		aModel.disable("menuMaintainMessageRule");
		aModel.disable("menuMaintainAlertRule");
		aModel.disable("menuMaintainReactRule");
		aModel.disable("menuMaintainRule");
		aModel.disable("menuMaintainCorrectRule");

//		aModel.setEnabled("menuObject", false);
		aModel.setEnabled("menuObjectDomain", false);
		aModel.setEnabled("menuObjectNetDir", false);
		aModel.setEnabled("menuObjectNetCat", false);
		aModel.setEnabled("menuObjectJDir", false);
		aModel.setEnabled("menuObjectJCat", false);

//		aModel.disable("menuNet");
		aModel.disable("menuNetCat");
		aModel.disable("menuNetCatEquipment");
		aModel.disable("menuNetCatLink");
		aModel.disable("menuNetCatResource");
		aModel.disable("menuNetCatTestPoint");
		aModel.disable("menuNetCatTPGroup");

//		aModel.disable("menuJ");
		aModel.disable("menuJCat");
		aModel.disable("menuJCatKIS");
		aModel.disable("menuJCatPath");
		aModel.disable("menuJCatAccessPoint");

		aModel.fireModelChanged("");

		new CloseAllInternalCommand(desktopPane).execute();

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("session", LangModel.String("statusNoSession"));
		statusBar.setText("user", LangModel.String("statusNoUser"));
		statusBar.setText("domain", LangModel.String("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher()
	{
		return internal_dispatcher;
	}

	void this_componentShown(ComponentEvent e)
	{
		init_module();
	}
/*
	void this_windowClosing(WindowEvent e)
	{
		internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}
*/
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

class ConfigureMDIMain_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	ConfigureMDIMain adaptee;

	ConfigureMDIMain_this_componentAdapter(ConfigureMDIMain adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e)
	{
		adaptee.this_componentShown(e);
	}
}


