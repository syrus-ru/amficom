/**
 * $Id: MapEditorMainFrame.java,v 1.10 2004/12/07 17:05:54 krupenn Exp $
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
import com.syrus.AMFICOM.Client.Map.Command.Map.CreateMapReportCommand;
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
import com.syrus.AMFICOM.Client.Map.Command.Map.MapEditorAddSchemeToViewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapEditorRemoveSchemeFromViewCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapExportCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapImportCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapSchemeTreeFrame;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.MapView.MapView;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.io.IniFile;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import java.text.SimpleDateFormat;

import java.util.Date;

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
 * @version $Revision: 1.10 $, $Date: 2004/12/07 17:05:54 $
 * @module map_v2
 * @author $Author: krupenn $
 * @see
 */
public class MapEditorMainFrame extends JFrame 
	implements OperationListener, Module
{
	protected Dispatcher internalDispatcher = new Dispatcher();

	protected ApplicationContext aContext = new ApplicationContext();

	protected String domainId;

	protected static IniFile iniFile;
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

		aContext.setDispatcher(internalDispatcher);
		setContext(aContext);
	}

	protected MapEditorMainFrame()
	{
		this(new ApplicationContext());
	}

	private void jbInit()
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);

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

		mainPanel.setLayout(new BorderLayout());
		desktopPane.setLayout(null);
		desktopPane.setBackground(SystemColor.control.darker().darker());

		statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBarPanel.setLayout(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		statusBar.add(StatusBarModel.FIELD_STATUS);
		statusBar.add(StatusBarModel.FIELD_SERVER);
		statusBar.add(StatusBarModel.FIELD_SESSION);
		statusBar.add(StatusBarModel.FIELD_USER);
		statusBar.add(StatusBarModel.FIELD_DOMAIN);
		statusBar.add(StatusBarModel.FIELD_TIME);

		viewport.setView(desktopPane);
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		this.setJMenuBar(menuBar);
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
		return desktopPane;
	}

	public void finalizeModule()
	{
		setContext(null);
		Environment.getDispatcher().unregister(this, ContextChangeEvent.type);
		statusBar.removeDispatcher(Environment.getDispatcher());
	}

	public void initModule()
	{
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth(StatusBarModel.FIELD_STATUS, 200);
		statusBar.setWidth(StatusBarModel.FIELD_SERVER, 250);
		statusBar.setWidth(StatusBarModel.FIELD_SESSION, 200);
		statusBar.setWidth(StatusBarModel.FIELD_USER, 100);
		statusBar.setWidth(StatusBarModel.FIELD_DOMAIN, 150);
		statusBar.setWidth(StatusBarModel.FIELD_TIME, 50);

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));
		statusBar.setText(StatusBarModel.FIELD_SESSION, LangModel.getString("statusNoSession"));
		statusBar.setText(StatusBarModel.FIELD_USER, LangModel.getString("statusNoUser"));
		statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
		statusBar.setText(StatusBarModel.FIELD_TIME, " ");
		statusBar.organize();
		statusBar.addDispatcher(Environment.getDispatcher());
		statusBar.addDispatcher(internalDispatcher);

		// load values from properties file
		try
		{
			iniFile = new IniFile(iniFileName);
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
					aContext));
		aModel.setCommand("menuSessionClose", 
				new SessionCloseCommand(
					Environment.getDispatcher(), 
					aContext));
		aModel.setCommand("menuSessionConnection", 
				new SessionConnectionCommand(
					Environment.getDispatcher(), 
					aContext));
		aModel.setCommand("menuSessionChangePassword", 
				new SessionChangePasswordCommand(
					Environment.getDispatcher(), 
					aContext));
		aModel.setCommand("menuSessionDomain", 
				new SessionDomainCommand(
					Environment.getDispatcher(), 
					aContext));
		aModel.setCommand("menuExit", 
				new ExitCommand(this));

		aModel.setCommand("menuMapNew", 
				new MapEditorNewMapCommand(
					this, 
					aContext));
		aModel.setCommand("menuMapOpen", 
				new MapEditorOpenMapCommand(
					getDesktop(), 
					aContext));
		aModel.setCommand("menuMapClose", 
				new MapEditorCloseMapCommand(
					this, 
					internalDispatcher));
		aModel.setCommand("menuMapSave", 
				new MapEditorSaveMapCommand(
					null, 
					aContext));
		aModel.setCommand("menuMapSaveAs", 
				new MapEditorSaveMapAsCommand(
					null, 
					aContext));
		aModel.setCommand("menuMapExport", 
				new MapExportCommand(null));
		aModel.setCommand("menuMapImport", 
				new MapImportCommand(null));

		aModel.setCommand("menuSchemeAddToView", 
				new MapEditorAddSchemeToViewCommand(
					null, 
					aContext));
		aModel.setCommand("menuSchemeRemoveFromView", 
				new MapEditorRemoveSchemeFromViewCommand(
					null, 
					aContext));

		aModel.setCommand("menuMapViewNew", 
				new MapEditorNewViewCommand(
					this, 
					aContext));
		aModel.setCommand("menuMapViewOpen", 
				new MapEditorOpenViewCommand(
					desktopPane, 
					aContext));
		aModel.setCommand("menuMapViewClose", 
				new MapEditorCloseViewCommand(
					this, 
					internalDispatcher));
		aModel.setCommand("menuMapViewSave", 
				new MapEditorSaveViewCommand(
					null, 
					aContext));
		aModel.setCommand("menuMapViewSaveAs", 
				new MapEditorSaveViewAsCommand(
					null, 
					aContext));

		aModel.setCommand("menuViewProto", 
				new ViewMapElementsBarCommand(
					desktopPane, 
					aContext));
		aModel.setCommand("menuViewAttributes", 
				new ViewMapPropertiesCommand(
					desktopPane, 
					aContext));
		aModel.setCommand("menuViewElements", 
				new ViewMapElementsCommand(
					desktopPane, 
					aContext));
		aModel.setCommand("menuViewSetup", 
				new ViewMapSetupCommand(
					desktopPane, 
					aContext));
		aModel.setCommand("menuViewMap", 
				new ViewMapWindowCommand(
					internalDispatcher, 
					desktopPane, 
					aContext, 
					new MapMapEditorApplicationModelFactory()));
		aModel.setCommand("menuViewMapScheme", 
				new ViewMapNavigatorCommand(
					desktopPane, 
					aContext ));
		aModel.setCommand("menuViewAll", 
				new ViewMapAllCommand(
					desktopPane, 
					aContext, 
					new MapMapEditorApplicationModelFactory()));
          
		CreateMapReportCommand rc = new CreateMapReportCommand(aContext);
		aModel.setCommand("menuReportCreate", rc);

//		aModel.setCommand("menuReportOpen", new CreateMapReportCommand(aContext));

		aModel.add("menuHelpAbout", 
				new HelpAboutCommand(this));

		aModel.fireModelChanged();

		if(ConnectionInterface.getActiveConnection() != null)
		{
			aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
			if(aContext.getConnectionInterface().isConnected())
			{
				internalDispatcher.notify(new ContextChangeEvent(
						aContext.getConnectionInterface(),
						ContextChangeEvent.CONNECTION_OPENED_EVENT));
			}
		}
		else
		{
			aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
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
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().unregister(this, ContextChangeEvent.type);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_FRAME_SHOWN);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_SELECTED);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_CLOSED);
				statusBar.removeDispatcher(this.aContext.getDispatcher());
			}

		if(aContext != null)
		{
			this.aContext = aContext;
			if(aContext.getApplicationModel() == null)
				aContext.setApplicationModel(new ApplicationModel());
			setModel(aContext.getApplicationModel());

			aContext.getDispatcher().register(this, ContextChangeEvent.type);
			aContext.getDispatcher().register(this, MapEvent.MAP_FRAME_SHOWN);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_SELECTED);
			aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_CLOSED);
			statusBar.addDispatcher(this.aContext.getDispatcher());
		}
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

		aModel.fireModelChanged();
	}

	public ApplicationModel getModel()
	{
		return aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			mapFrame = (MapFrame)ae.getSource();

			ApplicationModel aModel = aContext.getApplicationModel();
			if(aModel != null)
			{
				aModel.getCommand("menuMapOpen").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapNew").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapSave").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapSaveAs").setParameter("mapFrame", mapFrame);

				aModel.getCommand("menuMapViewOpen").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapViewNew").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapViewSave").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuMapViewSaveAs").setParameter("mapFrame", mapFrame);

				aModel.getCommand("menuSchemeAddToView").setParameter("mapFrame", mapFrame);
				aModel.getCommand("menuSchemeRemoveFromView").setParameter("mapFrame", mapFrame);
			}
		 }
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuMapSave", true);
			aModel.setEnabled("menuMapSaveAs", true);
			aModel.setEnabled("menuMapClose", true);
			aModel.setEnabled("menuMapExport", true);

			aModel.setEnabled("menuMapViewSave", true);
			aModel.setEnabled("menuMapViewSaveAs", true);
			aModel.setEnabled("menuMapViewClose", true);

			aModel.fireModelChanged();
			setTitle(LangModelMap.getString("MapView") + ": " + ((ObjectResource )ae.getSource()).getName());
		}
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_CLOSED))
		{
			for(int i = 0; i < desktopPane.getComponents().length; i++)
			{
				Component comp = desktopPane.getComponent(i);
				if (comp instanceof MapFrame)
				{
					((MapFrame)comp).setVisible(false);
					((MapFrame)comp).setMapView(null);
					((MapFrame)comp).setContext(null);
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
			aModel.setEnabled("menuMapExport", false);

			aModel.setEnabled("menuMapViewSave", false);
			aModel.setEnabled("menuMapViewSaveAs", false);
			aModel.setEnabled("menuMapViewClose", false);

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
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(aContext.getApplicationModel().getDataSource(aContext.getSessionInterface()));
					if(!Checker.checkCommandByUserId(
							aContext.getSessionInterface().getUserId(),
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
				if(aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(null);
					setSessionClosed();
				}
			}
			if(cce.SESSION_CHANGING)
			{
				statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusSettingSession"));
			}
//			if(cce.SESSION_CHANGED)
//			{
//			}
			if(cce.DOMAIN_SELECTED)
			{
				domainId = (String )cce.getSource();
				statusBar.setText(
					StatusBarModel.FIELD_DOMAIN, 
					((ObjectResource )Pool.get(Domain.typ, domainId)).getName());
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
				statusBar.setText(
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
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, aContext.getConnectionInterface().getServiceURL());
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
		aModel.setEnabled("menuScheme", false);
		aModel.setEnabled("menuMapView", false);
		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuReport", false);

		aModel.fireModelChanged();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusDisconnected"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));
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
		aModel.setEnabled("menuScheme", false);
		aModel.setEnabled("menuMapView", false);
		aModel.setEnabled("menuView", false);
		aModel.setEnabled("menuReport", false);

		aModel.fireModelChanged();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusError"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusConnectionError"));
	}

	public void setSessionOpened()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);
		aModel.setEnabled("menuSessionDomain", true);
		aModel.fireModelChanged();
		domainId = aContext.getSessionInterface().getDomainId();
		if (domainId != null && domainId.length() != 0) 
		{
			statusBar.setText(
				StatusBarModel.FIELD_DOMAIN, 
				((ObjectResource )Pool.get(Domain.typ, domainId)).getName());
			setDomainSelected();
		}
		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SESSION, sdf.format(new Date(aContext.getSessionInterface().getLogonTime())));
		statusBar.setText(StatusBarModel.FIELD_USER, aContext.getSessionInterface().getUser());
	}

	public void setDomainSelected()
	{
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		new CloseAllInternalCommand(desktopPane).execute();

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE, 
				LangModel.getString("Initiating")));

		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("DataLoaded")));

		new ViewMapAllCommand(desktopPane, aContext, new MapMapEditorApplicationModelFactory()).execute();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuMap", true);
		aModel.setEnabled("menuScheme", true);
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

	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = aContext.getApplicationModel();
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

		new CloseAllInternalCommand(desktopPane).execute();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SESSION, LangModel.getString("statusNoSession"));
		statusBar.setText(StatusBarModel.FIELD_USER, LangModel.getString("statusNoUser"));
		statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher()
	{
		return internalDispatcher;
	}

	void thisComponentShown(ComponentEvent e)
	{
		initModule();
		desktopPane.setPreferredSize(desktopPane.getSize());
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
			Command closeCommand = aContext.getApplicationModel().getCommand("menuExit");
			this.setContext(null);
			closeCommand.execute();

//			internal_dispatcher.notify(new OperationEvent(this, 0, "mapcloseevent"));
//			internal_dispatcher.unregister(this, "contextchange");
//			Environment.getDispatcher().unregister(this, "contextchange");

			return;
		}
		super.processWindowEvent(e);
	}

}

