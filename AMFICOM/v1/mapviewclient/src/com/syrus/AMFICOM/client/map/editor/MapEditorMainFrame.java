/**
 * $Id: MapEditorMainFrame.java,v 1.21 2005/02/07 16:09:26 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Editor;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.CloseAllInternalCommand;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Module;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorCloseMapCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorCloseViewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorNewMapCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorNewViewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorOpenMapCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorSaveMapAsCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorSaveMapCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorSaveViewAsCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.MapEditorSaveViewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapAllCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapElementsBarCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapElementsCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapNavigatorCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapPropertiesCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapSetupCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.CreateMapReportCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewAddSchemeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewRemoveSchemeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapExportCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapImportCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeFrame;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import java.io.FileInputStream;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * Основное окно модуля Редактор топологической схемы
 * 
 * 
 * 
 * @version $Revision: 1.21 $, $Date: 2005/02/07 16:09:26 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorMainFrame extends JFrame 
	implements OperationListener, Module
{
	protected Dispatcher internalDispatcher = new Dispatcher();

	protected ApplicationContext aContext = new ApplicationContext();

	protected Identifier domainId;

	protected static String iniFileName = "Map.properties";

	protected static SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	BorderLayout borderLayout = new BorderLayout();

	JPanel mainPanel = new JPanel();
	MapEditorToolBar toolBar = new MapEditorToolBar();
	JScrollPane scrollPane = new JScrollPane();
	JViewport viewport = new JViewport();
	JDesktopPane desktopPane = new JDesktopPane();
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	MapEditorMenuBar menuBar = new MapEditorMenuBar();

	protected MapFrame mapFrame = null;

	public MapFrame getMapFrame()
	{
		return this.mapFrame;
	}

	public MapEditorMainFrame(ApplicationContext aContext)
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

		aContext.setDispatcher(this.internalDispatcher);
		setContext(aContext);
	}

	protected MapEditorMainFrame()
	{
		this(new ApplicationContext());
	}

	private void jbInit()
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(this.mainPanel);

		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension (screenSize.width, screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		this.setTitle(LangModelMap.getString("Map"));
		this.addComponentListener(new ComponentAdapter()
			{
				public void componentShown(ComponentEvent e)
				{
					thisComponentShown(e);
				}
			});

		this.mainPanel.setLayout(new BorderLayout());
		this.desktopPane.setLayout(null);
		this.desktopPane.setBackground(SystemColor.control.darker().darker());

		this.statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.statusBarPanel.setLayout(new BorderLayout());
		this.statusBarPanel.add(this.statusBar, BorderLayout.CENTER);

		this.statusBar.add(StatusBarModel.FIELD_STATUS);
		this.statusBar.add(StatusBarModel.FIELD_SERVER);
		this.statusBar.add(StatusBarModel.FIELD_SESSION);
		this.statusBar.add(StatusBarModel.FIELD_USER);
		this.statusBar.add(StatusBarModel.FIELD_DOMAIN);
		this.statusBar.add(StatusBarModel.FIELD_TIME);

		this.viewport.setView(this.desktopPane);
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.mainPanel.add(this.toolBar, BorderLayout.NORTH);
		this.mainPanel.add(this.statusBarPanel, BorderLayout.SOUTH);
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);

		this.setJMenuBar(this.menuBar);
	}

	void setDefaultModel (ApplicationModel aModel)
	{
		aModel.setEnabled("menuSession", false);
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionConnection", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);
		aModel.setEnabled("menuExit", false);

		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuViewProto", false);
		aModel.setEnabled("menuViewNavigator", false);
		aModel.setEnabled("menuViewAttributes", false);
		aModel.setEnabled("menuViewElements", false);
		aModel.setEnabled("menuViewSetup", false);
		aModel.setEnabled("menuViewMap", false);
		aModel.setEnabled("menuViewMapScheme", false);
		aModel.setEnabled("menuViewAll", false);

		aModel.setEnabled("menuMap", false);
		aModel.setEnabled("menuMapNew", false);
		aModel.setEnabled("menuMapOpen", false);
		aModel.setEnabled("menuMapClose", false);
		aModel.setEnabled("menuMapSave", false);
		aModel.setEnabled("menuMapSaveAs", false);
		aModel.setEnabled("menuMapImport", false);
		aModel.setEnabled("menuMapExport", false);

		aModel.setEnabled("menuMapView", false);
		aModel.setEnabled("menuMapViewNew", false);
		aModel.setEnabled("menuMapViewOpen", false);
		aModel.setEnabled("menuMapViewClose", false);
		aModel.setEnabled("menuMapViewSave", false);
		aModel.setEnabled("menuMapViewSaveAs", false);
		aModel.setEnabled("menuMapViewAddScheme", false);
		aModel.setEnabled("menuMapViewRemoveScheme", false);

		aModel.setEnabled("menuHelp", false);
		aModel.setEnabled("menuHelpContents", false);
		aModel.setEnabled("menuHelpFind", false);
		aModel.setEnabled("menuHelpTips", false);
		aModel.setEnabled("menuHelpStart", false);
		aModel.setEnabled("menuHelpCourse", false);
		aModel.setEnabled("menuHelpHelp", false);
		aModel.setEnabled("menuHelpSupport", false);
		aModel.setEnabled("menuHelpLicense", false);
		aModel.setEnabled("menuHelpAbout", false);

		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuMap", true);
		aModel.setEnabled("menuMapView", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);
	}

	public JDesktopPane getDesktop()
	{
		return this.desktopPane;
	}

	public void finalizeModule()
	{
		setContext(null);
		Environment.getDispatcher().unregister(this, ContextChangeEvent.type);
		this.statusBar.removeDispatcher(Environment.getDispatcher());
	}

	public void initModule()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();

		this.statusBar.distribute();
		this.statusBar.setWidth(StatusBarModel.FIELD_STATUS, 200);
		this.statusBar.setWidth(StatusBarModel.FIELD_SERVER, 250);
		this.statusBar.setWidth(StatusBarModel.FIELD_SESSION, 200);
		this.statusBar.setWidth(StatusBarModel.FIELD_USER, 100);
		this.statusBar.setWidth(StatusBarModel.FIELD_DOMAIN, 150);
		this.statusBar.setWidth(StatusBarModel.FIELD_TIME, 50);

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));
		this.statusBar.setText(StatusBarModel.FIELD_SESSION, LangModel.getString("statusNoSession"));
		this.statusBar.setText(StatusBarModel.FIELD_USER, LangModel.getString("statusNoUser"));
		this.statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
		this.statusBar.setText(StatusBarModel.FIELD_TIME, " ");
		this.statusBar.organize();
		this.statusBar.addDispatcher(Environment.getDispatcher());
		this.statusBar.addDispatcher(this.internalDispatcher);

		// load values from properties file
		try
		{
			Properties properties = new Properties();
			properties.load(new FileInputStream(iniFileName));
			System.out.println("read ini file " + iniFileName);
		}
		catch(java.io.IOException e)
		{
			System.out.println("Error opening " + iniFileName + " - setting defaults");
		}

		Environment.getDispatcher().register(this, ContextChangeEvent.type);

		setDefaultModel(aModel);

		aModel.setCommand("menuSessionNew", 
				new SessionOpenCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand("menuSessionClose", 
				new SessionCloseCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand("menuSessionConnection", 
				new SessionConnectionCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand("menuSessionChangePassword", 
				new SessionChangePasswordCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand("menuSessionDomain", 
				new SessionDomainCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand("menuExit", 
				new ExitCommand(this));

		aModel.setCommand("menuMapNew", 
				new MapEditorNewMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapOpen", 
				new MapEditorOpenMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapClose", 
				new MapEditorCloseMapCommand(
						this.desktopPane, 
					this.internalDispatcher));
		aModel.setCommand("menuMapSave", 
				new MapEditorSaveMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapSaveAs", 
				new MapEditorSaveMapAsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapExport", 
				new MapExportCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapImport", 
				new MapImportCommand(
					this.desktopPane, 
					this.aContext));

		aModel.setCommand("menuMapViewAddScheme", 
				new MapViewAddSchemeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapViewRemoveScheme", 
				new MapViewRemoveSchemeCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand("menuMapViewNew", 
				new MapEditorNewViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapViewOpen", 
				new MapEditorOpenViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapViewClose", 
				new MapEditorCloseViewCommand(
						this.desktopPane, 
						this.internalDispatcher));
		aModel.setCommand("menuMapViewSave", 
				new MapEditorSaveViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuMapViewSaveAs", 
				new MapEditorSaveViewAsCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand("menuViewProto", 
				new ViewMapElementsBarCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuViewAttributes", 
				new ViewMapPropertiesCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuViewElements", 
				new ViewMapElementsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuViewSetup", 
				new ViewMapSetupCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand("menuViewMap", 
				new ViewMapWindowCommand(
					this.internalDispatcher, 
					this.desktopPane, 
					this.aContext, 
					new MapMapEditorApplicationModelFactory()));
		aModel.setCommand("menuViewMapScheme", 
				new ViewMapNavigatorCommand(
						this.desktopPane, 
					this.aContext ));
		aModel.setCommand("menuViewAll", 
				new ViewMapAllCommand(
						this.desktopPane, 
						this.aContext, 
					new MapMapEditorApplicationModelFactory()));
          
		CreateMapReportCommand rc = new CreateMapReportCommand(this.aContext);
		aModel.setCommand("menuReportCreate", rc);

//		aModel.setCommand("menuReportOpen", new CreateMapReportCommand(this.aContext));

		aModel.add("menuHelpAbout", 
				new HelpAboutCommand(this));

		aModel.fireModelChanged();

		if(ConnectionInterface.getInstance() != null)
		{
			if(ConnectionInterface.getInstance().isConnected())
				this.internalDispatcher.notify(new ContextChangeEvent(
						ConnectionInterface.getInstance(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		if(SessionInterface.getActiveSession() != null)
		{
			this.aContext.setSessionInterface(SessionInterface.getActiveSession());
			if(this.aContext.getSessionInterface().isOpened())
				this.internalDispatcher.notify(new ContextChangeEvent(
						this.aContext.getSessionInterface(),
						ContextChangeEvent.SESSION_OPENED_EVENT));
		}
		else
		{
			this.aContext.setSessionInterface(Environment.getDefaultSessionInterface(ConnectionInterface.getInstance()));
			SessionInterface.setActiveSession(this.aContext.getSessionInterface());
		}
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().unregister(this, ContextChangeEvent.type);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_FRAME_SHOWN);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_SELECTED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_CLOSED);
				this.statusBar.removeDispatcher(this.aContext.getDispatcher());
			}

		if(aContext != null)
		{
			this.aContext = aContext;
			if(aContext.getApplicationModel() == null)
				aContext.setApplicationModel(ApplicationModel.getInstance());
			setModel(aContext.getApplicationModel());

			aContext.getDispatcher().register(this, ContextChangeEvent.type);
			aContext.getDispatcher().register(this, MapEvent.MAP_FRAME_SHOWN);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_SELECTED);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_CLOSED);
			this.statusBar.addDispatcher(this.aContext.getDispatcher());
		}
	}

	public ApplicationContext getContext()
	{
		return this.aContext;
	}

	public void setModel(ApplicationModel aModel)
	{
		aModel.addListener(this.menuBar);
		aModel.addListener(this.toolBar);
		this.menuBar.setModel(aModel);
		this.toolBar.setModel(aModel);

		aModel.fireModelChanged();
	}

	public ApplicationModel getModel()
	{
		return this.aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			this.mapFrame = (MapFrame)ae.getSource();

//			ApplicationModel aModel = this.aContext.getApplicationModel();
//			if(aModel != null)
//			{
//				aModel.getCommand("menuMapOpen").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuMapNew").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuMapSave").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuMapSaveAs").setParameter("mapFrame", mapFrame);
//
//				aModel.getCommand("menuMapViewOpen").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuMapViewNew").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuMapViewSave").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuMapViewSaveAs").setParameter("mapFrame", mapFrame);
//
//				aModel.getCommand("menuSchemeAddToView").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuSchemeRemoveFromView").setParameter("mapFrame", mapFrame);
//			}
		 }
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled("menuMapSave", true);
			aModel.setEnabled("menuMapSaveAs", true);
			aModel.setEnabled("menuMapClose", true);
			aModel.setEnabled("menuMapExport", true);

			aModel.setEnabled("menuMapViewSave", true);
			aModel.setEnabled("menuMapViewSaveAs", true);
			aModel.setEnabled("menuMapViewClose", true);
			aModel.setEnabled("menuMapViewAddScheme", true);
			aModel.setEnabled("menuMapViewRemoveScheme", true);

			aModel.fireModelChanged();
			setTitle(LangModelMap.getString("MapView") + ": " + ((MapView )ae.getSource()).getName());
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CLOSED))
		{
			for(int i = 0; i < this.desktopPane.getComponents().length; i++)
			{
				Component comp = this.desktopPane.getComponent(i);
				if (comp instanceof MapFrame)
				{
					((MapFrame)comp).setVisible(false);
//					((MapFrame)comp).setMapView(null);
					((MapFrame)comp).setContext(null);
				}
				else if (comp instanceof MapPropertyFrame)
					((MapPropertyFrame)comp).setVisible(false);
				else if (comp instanceof MapElementsFrame)
					((MapElementsFrame)comp).setVisible(false);
				else if (comp instanceof MapSchemeTreeFrame)
					((MapSchemeTreeFrame)comp).setVisible(false);
			}
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled("menuMapSave", false);
			aModel.setEnabled("menuMapSaveAs", false);
			aModel.setEnabled("menuMapClose", false);
			aModel.setEnabled("menuMapExport", false);

			aModel.setEnabled("menuMapViewSave", false);
			aModel.setEnabled("menuMapViewSaveAs", false);
			aModel.setEnabled("menuMapViewClose", false);
			aModel.setEnabled("menuMapViewAddScheme", false);
			aModel.setEnabled("menuMapViewRemoveScheme", false);

			aModel.fireModelChanged();
			setTitle(LangModelMap.getString("MapView"));
		}
		else
		if(ae.getActionCommand().equals(ContextChangeEvent.type))
		{
			ContextChangeEvent cce = (ContextChangeEvent )ae;
			System.out.println(
					"perform context change \"" 
					+ Long.toHexString(cce.change_type) 
					+ "\" at " 
					+ this.getTitle());
			if(cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface )cce.getSource();
				if(this.aContext.getSessionInterface().equals(ssi))
				{
					if(!Checker.checkCommandByUserId(
							((RISDSessionInfo )this.aContext.getSessionInterface()).getUserId(),
//							((RISDSessionInfo )this.aContext.getSessionInterface()).getUserIdentifier().toString(),
							Checker.topologyEditing))
					{
						JOptionPane.showMessageDialog(null, LangModelMap.getString("NotEnoughRights"), LangModel.getString("Error"), JOptionPane.OK_OPTION);
						return;
					}
					setSessionOpened();
				}
			}
			if(cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface )cce.getSource();
				if(this.aContext.getSessionInterface().equals(ssi))
				{
					setSessionClosed();
				}
			}
			if(cce.SESSION_CHANGING)
			{
				this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusSettingSession"));
			}
//			if(cce.SESSION_CHANGED)
//			{
//			}
			if(cce.DOMAIN_SELECTED)
			{
				setDomainSelected();
			}
			if(cce.CONNECTION_OPENED)
			{
				setConnectionOpened();
			}
			if(cce.CONNECTION_CLOSED)
			{
				setConnectionClosed();
			}
			if(cce.CONNECTION_FAILED)
			{
				setConnectionFailed();
			}
			if(cce.CONNECTION_CHANGING)
			{
				this.statusBar.setText(
						StatusBarModel.FIELD_STATUS, 
						LangModel.getString("statusConnecting"));
			}
//			if(cce.CONNECTION_CHANGED)
//			{
//			}
		}
	}

	public void setConnectionOpened()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged();

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, ConnectionInterface.getInstance().getServerName());
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);

		aModel.setEnabled("menuMap", false);
		aModel.setEnabled("menuMapView", false);
		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuReport", false);

		aModel.fireModelChanged();

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusDisconnected"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);

		aModel.setEnabled("menuMap", false);
		aModel.setEnabled("menuMapView", false);
		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuReport", false);

		aModel.fireModelChanged();

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusError"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusConnectionError"));
	}

	public void setSessionOpened()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);
		aModel.setEnabled("menuSessionDomain", true);
		aModel.fireModelChanged();
		this.domainId = new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
		if (this.domainId != null) 
		{
			setDomainSelected();
		}
		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SESSION, sdf.format(new Date(this.aContext.getSessionInterface().getLogonTime())));

		try
		{
			Identifier userId = new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().user_id);
			User user = (User )AdministrationStorableObjectPool.getStorableObject(
					userId, true);
			this.statusBar.setText(StatusBarModel.FIELD_USER, this.aContext.getSessionInterface().getUser());
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
		}
	}

	public void setDomainSelected()
	{
		new CloseAllInternalCommand(this.desktopPane).execute();

		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE, 
				LangModel.getString("Initiating")));

		this.aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("DataLoaded")));

		new ViewMapAllCommand(this.desktopPane, this.aContext, new MapMapEditorApplicationModelFactory()).execute();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled("menuMap", true);
		aModel.setEnabled("menuMapView", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuReport", true);

		aModel.setEnabled("menuMapNew", true);
		aModel.setEnabled("menuMapOpen", true);
		aModel.setEnabled("menuMapImport", true);
		aModel.setEnabled("menuMapViewNew", true);
		aModel.setEnabled("menuMapViewOpen", true);

		aModel.setEnabled("menuViewProto", true);
		aModel.setEnabled("menuViewNavigator", true);
		aModel.setEnabled("menuViewAttributes", true);
		aModel.setEnabled("menuViewElements", true);
		aModel.setEnabled("menuViewSetup", true);
		aModel.setEnabled("menuViewMap", true);
		aModel.setEnabled("menuViewMapScheme", true);
		aModel.setEnabled("menuViewAll", true);
    
		aModel.setEnabled("menuReportCreate", true);

		aModel.fireModelChanged();

		try
		{
			this.domainId = new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
			Domain domain = (Domain )AdministrationStorableObjectPool.getStorableObject(
					this.domainId, true);
			this.statusBar.setText("domain", domain.getName());
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
		}
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();
		setDefaultModel(aModel);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);	
		aModel.setEnabled("menuSessionDomain", false);

		aModel.setEnabled("menuMapViewNew", false);
		aModel.setEnabled("menuMapViewOpen", false);
		aModel.setEnabled("menuMapViewSave", false);
		aModel.setEnabled("menuMapViewSaveAs", false);
		aModel.setEnabled("menuMapViewClose", false);
		aModel.setEnabled("menuMapViewAddScheme", false);
		aModel.setEnabled("menuMapViewRemoveScheme", false);
		
		aModel.setEnabled("menuMapNew", false);
		aModel.setEnabled("menuMapOpen", false);
		aModel.setEnabled("menuMapSave", false);
		aModel.setEnabled("menuMapSaveAs", false);
		aModel.setEnabled("menuMapClose", false);
		aModel.setEnabled("menuMapImport", true);
		aModel.setEnabled("menuMapExport", true);
		
		aModel.setEnabled("menuViewNavigator", false);
		aModel.setEnabled("menuViewAttributes", false);
		aModel.setEnabled("menuViewElements", false);
		aModel.setEnabled("menuViewSetup", false);
		aModel.setEnabled("menuViewMap", false);
		aModel.setEnabled("menuViewMapScheme", false);
		aModel.setEnabled("menuViewAll", false);
		aModel.fireModelChanged();

		new CloseAllInternalCommand(this.desktopPane).execute();

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SESSION, LangModel.getString("statusNoSession"));
		this.statusBar.setText(StatusBarModel.FIELD_USER, LangModel.getString("statusNoUser"));
		this.statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher()
	{
		return this.internalDispatcher;
	}

	void thisComponentShown(ComponentEvent e)
	{
		initModule();
		this.desktopPane.setPreferredSize(this.desktopPane.getSize());
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED)
		{
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			if(getMapFrame() != null)
				getMapFrame().saveConfig();
			Command closeCommand = this.aContext.getApplicationModel().getCommand("menuExit");
			this.setContext(null);
			closeCommand.execute();

			return;
		}
		super.processWindowEvent(e);
	}

}

