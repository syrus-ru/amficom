/**
 * $Id: MapEditorMainFrame.java,v 1.36 2005/05/18 14:59:46 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Editor;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.syrus.AMFICOM.Client.General.Command.CloseAllInternalCommand;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
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
import com.syrus.AMFICOM.Client.General.Model.MapEditorApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Module;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
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
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewAdditionalPropertiesCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewCharacteristicsCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewGeneralPropertiesCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapAllCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapControlsCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapViewNavigatorCommand;
import com.syrus.AMFICOM.Client.Map.Command.Editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.CreateMapReportCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapAddExternalNodeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapAddMapCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapExportCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapImportCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapRemoveMapCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewAddSchemeCommand;
import com.syrus.AMFICOM.Client.Map.Command.Map.MapViewRemoveSchemeCommand;
import com.syrus.AMFICOM.Client.Map.UI.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapCharacteristicPropertiesFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Основное окно модуля Редактор топологической схемы
 * 
 * 
 * 
 * @version $Revision: 1.36 $, $Date: 2005/05/18 14:59:46 $
 * @module mapviewclient_v1
 * @author $Author: bass $
 */
public class MapEditorMainFrame extends JFrame 
	implements OperationListener, Module
{
	protected Dispatcher internalDispatcher = new Dispatcher();

	protected ApplicationContext aContext = new ApplicationContext();

	protected Identifier domainId;

	protected static String iniFileName = "Map.properties";

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
	
	private MapEditorWindowArranger arranger = new MapEditorWindowArranger(this.desktopPane);

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
		GraphicsEnvironment localGraphicsEnvironment = 
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = 
			localGraphicsEnvironment.getMaximumWindowBounds();
		this.setSize(new Dimension(
				maximumWindowBounds.width - maximumWindowBounds.x, 
				maximumWindowBounds.height - maximumWindowBounds.y));
		this.setLocation(maximumWindowBounds.x, maximumWindowBounds.y);

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
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE, false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CONNECTION, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_DOMAIN, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_EXIT, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, false);
		aModel.setEnabled("menuViewNavigator", false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CONTROLS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_NEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_CONTENTS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_FIND, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_FIND, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_START, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_COURSE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_HELP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_SUPPORT, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_LICENSE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_ABOUT, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CONNECTION, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_EXIT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_HELP_ABOUT, true);
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

		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_NEW, 
				new SessionOpenCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_CLOSE, 
				new SessionCloseCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_CONNECTION, 
				new SessionConnectionCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD, 
				new SessionChangePasswordCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_DOMAIN, 
				new SessionDomainCommand(
					Environment.getDispatcher(), 
					this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_EXIT, 
				new ExitCommand(this));

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_NEW, 
				new MapEditorNewMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_OPEN, 
				new MapEditorOpenMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_CLOSE, 
				new MapEditorCloseMapCommand(
						this.desktopPane, 
					this.internalDispatcher));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_SAVE, 
				new MapEditorSaveMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, 
				new MapEditorSaveMapAsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_ADD_MAP, 
				new MapAddMapCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_REMOVE_MAP, 
				new MapRemoveMapCommand(
					this.desktopPane, 
					this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_ADD_EXTERNAL, 
				new MapAddExternalNodeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_EXPORT, 
				new MapExportCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_IMPORT, 
				new MapImportCommand(
					this.desktopPane, 
					this.aContext));

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, 
				new MapViewAddSchemeCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, 
				new MapViewRemoveSchemeCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, 
				new MapEditorNewViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, 
				new MapEditorOpenViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, 
				new MapEditorCloseViewCommand(
						this.desktopPane, 
						this.internalDispatcher));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, 
				new MapEditorSaveViewCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, 
				new MapEditorSaveViewAsCommand(
						this.desktopPane, 
						this.aContext));

		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_GENERAL, 
				new ViewGeneralPropertiesCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL, 
				new ViewAdditionalPropertiesCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS, 
				new ViewCharacteristicsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_CONTROLS, 
				new ViewMapControlsCommand(
						this.desktopPane, 
						this.aContext));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_MAP, 
				new ViewMapWindowCommand(
					this.internalDispatcher, 
					this.desktopPane, 
					this.aContext, 
					new MapMapEditorApplicationModelFactory()));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR, 
				new ViewMapViewNavigatorCommand(
					this.desktopPane, 
					this.aContext ));
		aModel.setCommand(MapEditorApplicationModel.ITEM_VIEW_ALL, 
				new ViewMapAllCommand(
						this.desktopPane,
						this.aContext,
						new MapMapEditorApplicationModelFactory()));
          
		CreateMapReportCommand rc = new CreateMapReportCommand(this.aContext);
		aModel.setCommand(MapEditorApplicationModel.ITEM_REPORT_CREATE, rc);

//		aModel.setCommand("menuReportOpen", new CreateMapReportCommand(this.aContext));

		aModel.add(MapEditorApplicationModel.ITEM_HELP_ABOUT, 
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
				this.aContext.getDispatcher().unregister(this.arranger,MapEditorWindowArranger.EVENT_ARRANGE);				
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
			aContext.getDispatcher().register(this.arranger,MapEditorWindowArranger.EVENT_ARRANGE);			
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
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_OPEN).setParameter("mapFrame", mapFrame);
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_NEW).setParameter("mapFrame", mapFrame);
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_SAVE).setParameter("mapFrame", mapFrame);
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_SAVE_AS).setParameter("mapFrame", mapFrame);
//
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN).setParameter("mapFrame", mapFrame);
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW).setParameter("mapFrame", mapFrame);
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE).setParameter("mapFrame", mapFrame);
//				aModel.getCommand(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS).setParameter("mapFrame", mapFrame);
//
//				aModel.getCommand("menuSchemeAddToView").setParameter("mapFrame", mapFrame);
//				aModel.getCommand("menuSchemeRemoveFromView").setParameter("mapFrame", mapFrame);
//			}
		 }
		else
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
		{
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, true);

			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, true);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, true);

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
				else if (comp instanceof MapGeneralPropertiesFrame)
					((MapGeneralPropertiesFrame)comp).setVisible(false);
				else if (comp instanceof MapAdditionalPropertiesFrame)
					((MapAdditionalPropertiesFrame)comp).setVisible(false);
				else if (comp instanceof MapCharacteristicPropertiesFrame)
					((MapCharacteristicPropertiesFrame)comp).setVisible(false);
			}
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, false);

			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, false);
			aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, false);

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
//					if(!Checker.checkCommandByUserId(
//							((RISDSessionInfo )this.aContext.getSessionInterface()).getUserId(),
//							Checker.topologyEditing))
//					{
//						JOptionPane.showMessageDialog(null, LangModelMap.getString("NotEnoughRights"), LangModel.getString("Error"), JOptionPane.OK_OPTION);
//						return;
//					}
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

		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CONNECTION, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD, false);

		aModel.fireModelChanged();

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, ConnectionInterface.getInstance().getServerName());
	}

	public void setConnectionClosed()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE, false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_DOMAIN, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, false);

		aModel.fireModelChanged();

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusDisconnected"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));
	}

	public void setConnectionFailed()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();

		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE, false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_DOMAIN, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, false);

		aModel.fireModelChanged();

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusError"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusConnectionError"));
	}

	public void setSessionOpened()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE, true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_DOMAIN, true);
		aModel.fireModelChanged();
		this.domainId = new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().domain_id);
		if (this.domainId != null) 
		{
			setDomainSelected();
		}
		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SESSION, MapPropertiesManager.getDateFormat().format(new Date(this.aContext.getSessionInterface().getLogonTime())));

		this.statusBar.setText(StatusBarModel.FIELD_USER, this.aContext.getSessionInterface().getUser());
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

		new ViewMapAllCommand(
				this.desktopPane,
				this.aContext,
				new MapMapEditorApplicationModelFactory()).execute();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT, true);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, true);

		aModel.setEnabled("menuViewNavigator", true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CONTROLS, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL, true);
    
		aModel.setEnabled(MapEditorApplicationModel.ITEM_REPORT_CREATE, true);

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

//		try {
//			SchemeSampleData.populate(
//					new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().user_id),
//					new Identifier(this.aContext.getSessionInterface().getAccessIdentifier().domain_id));
//		} catch(DatabaseException e) {
//			e.printStackTrace();
//		} catch(IllegalObjectEntityException e) {
//			e.printStackTrace();
//		}
	}

	public void setSessionClosed()
	{
		ApplicationModel aModel = this.aContext.getApplicationModel();
		setDefaultModel(aModel);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_NEW, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CLOSE, false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_CHANGE_PASSWORD, false);	
		aModel.setEnabled(MapEditorApplicationModel.ITEM_SESSION_DOMAIN, false);

		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_NEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_OPEN, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_SAVE_AS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_CLOSE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_ADD_SCHEME, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_VIEW_REMOVE_SCHEME, false);
		
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_NEW, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_OPEN, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_SAVE_AS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_CLOSE, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_IMPORT, true);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_MAP_EXPORT, true);
		
		aModel.setEnabled("menuViewNavigator", false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_GENERAL, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ADDITIONAL, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CHARACTERISTICS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_CONTROLS, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_MAP, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_NAVIGATOR, false);
		aModel.setEnabled(MapEditorApplicationModel.ITEM_VIEW_ALL, false);
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
			Command closeCommand = this.aContext.getApplicationModel().getCommand(MapEditorApplicationModel.ITEM_SESSION_EXIT);
			this.setContext(null);
			closeCommand.execute();

			return;
		}
		super.processWindowEvent(e);
	}
}
