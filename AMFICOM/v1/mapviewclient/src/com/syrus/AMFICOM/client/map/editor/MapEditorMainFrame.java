/**
 * $Id: MapEditorMainFrame.java,v 1.42 2005/06/09 11:31:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.editor;

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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorCloseMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorCloseViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorNewViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorOpenViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveMapAsCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveMapCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveViewAsCommand;
import com.syrus.AMFICOM.client.map.command.editor.MapEditorSaveViewCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewAdditionalPropertiesCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewCharacteristicsCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewGeneralPropertiesCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapAllCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapControlsCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapViewNavigatorCommand;
import com.syrus.AMFICOM.client.map.command.editor.ViewMapWindowCommand;
import com.syrus.AMFICOM.client.map.command.map.CreateMapReportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapAddExternalNodeCommand;
import com.syrus.AMFICOM.client.map.command.map.MapAddMapCommand;
import com.syrus.AMFICOM.client.map.command.map.MapExportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapImportCommand;
import com.syrus.AMFICOM.client.map.command.map.MapRemoveMapCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewAddSchemeCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewRemoveSchemeCommand;
import com.syrus.AMFICOM.client.map.ui.MapAdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapCharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.map.ui.MapGeneralPropertiesFrame;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.client.UI.StatusBarModel;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.CloseAllInternalCommand;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.ExitCommand;
import com.syrus.AMFICOM.client.model.HelpAboutCommand;
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.model.OpenSessionCommand;
import com.syrus.AMFICOM.client.model.SessionChangePasswordCommand;
import com.syrus.AMFICOM.client.model.SessionCloseCommand;
import com.syrus.AMFICOM.client.model.SessionConnectionCommand;
import com.syrus.AMFICOM.client.model.SessionDomainCommand;
import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.SchemeSampleData;

/**
 * Основное окно модуля Редактор топологической схемы
 * 
 * 
 * 
 * @version $Revision: 1.42 $, $Date: 2005/06/09 11:31:22 $
 * @module mapviewclient_v1
 * @author $Author: krupenn $
 */
public class MapEditorMainFrame extends JFrame 
	implements PropertyChangeListener
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
		Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
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

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModelGeneral.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModelGeneral.getString("statusNoConnection"));
		this.statusBar.setText(StatusBarModel.FIELD_SESSION, LangModelGeneral.getString("statusNoSession"));
		this.statusBar.setText(StatusBarModel.FIELD_USER, LangModelGeneral.getString("statusNoUser"));
		this.statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModelGeneral.getString("statusNoDomain"));
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

		Environment.getDispatcher().addPropertyChangeListener(ContextChangeEvent.TYPE, this);

		setDefaultModel(aModel);

		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_NEW, 
				new OpenSessionCommand(Environment.getDispatcher()));
		aModel.setCommand(MapEditorApplicationModel.ITEM_SESSION_CLOSE, 
				new SessionCloseCommand(Environment.getDispatcher()));
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

//		if (ClientSessionEnvironment.getInstance().sessionEstablished()) {
//            this.internalDispatcher.firePropertyChange(new ContextChangeEvent(
//					this,
//					ContextChangeEvent.SESSION_OPENED_EVENT), true);
//		}
	}

	public void setContext(ApplicationContext aContext)
	{
		if(this.aContext != null)
			if(this.aContext.getDispatcher() != null)
			{
				this.aContext.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
				this.aContext.getDispatcher().removePropertyChangeListener(MapEditorWindowArranger.EVENT_ARRANGE, this.arranger);				
				this.statusBar.removeDispatcher(this.aContext.getDispatcher());
			}

		if(aContext != null)
		{
			this.aContext = aContext;
			if(aContext.getApplicationModel() == null)
				aContext.setApplicationModel(ApplicationModel.getInstance());
			setModel(aContext.getApplicationModel());

			aContext.getDispatcher().addPropertyChangeListener(ContextChangeEvent.TYPE, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEvent.MAP_VIEW_CLOSED, this);
			aContext.getDispatcher().addPropertyChangeListener(MapEditorWindowArranger.EVENT_ARRANGE, this.arranger);			
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

	public void propertyChange(PropertyChangeEvent pce)
	{
		if(pce.getPropertyName().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			this.mapFrame = (MapFrame)pce.getSource();
		 }
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED))
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
			setTitle(LangModelMap.getString("MapView") + ": " + ((MapView )pce.getSource()).getName());
		}
		else
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_CLOSED))
		{
			for(int i = 0; i < this.desktopPane.getComponents().length; i++)
			{
				Component comp = this.desktopPane.getComponent(i);
				if (comp instanceof MapFrame)
				{
					((MapFrame)comp).setVisible(false);
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
		if(pce.getPropertyName().equals(ContextChangeEvent.TYPE))
		{
			ContextChangeEvent cce = (ContextChangeEvent )pce;
			System.out.println(
					"perform context change \"" 
					+ cce.getNewValue() 
					+ "\" at " 
					+ this.getTitle());
			if(cce.isSessionOpened())
			{
				setSessionOpened();
			}
			if(cce.isSessionClosed())
			{
				setSessionClosed();
			}
			if(cce.isSessionChanging())
			{
				this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModelGeneral.getString("statusSettingSession"));
			}
//			if(cce.SESSION_CHANGED)
//			{
//			}
			if(cce.isDomainSelected())
			{
				setDomainSelected();
			}
			if(cce.isConnectionOpened())
			{
				setConnectionOpened();
			}
			if(cce.isConnectionClosed())
			{
				setConnectionClosed();
			}
			if(cce.isConnectionFailed())
			{
				setConnectionFailed();
			}
			if(cce.isConnectionChanging())
			{
				this.statusBar.setText(
						StatusBarModel.FIELD_STATUS, 
						LangModelGeneral.getString("statusConnecting"));
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

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModelGeneral.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModelGeneral.getString("statusConnected"));
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

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModelGeneral.getString("statusDisconnected"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModelGeneral.getString("statusNoConnection"));
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

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModelGeneral.getString("statusError"));
		this.statusBar.setText(StatusBarModel.FIELD_SERVER, LangModelGeneral.getString("statusConnectionError"));
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
		this.domainId = LoginManager.getDomainId();
		if (this.domainId != null) 
		{
			setDomainSelected();
		}
		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModelGeneral.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SESSION, MapPropertiesManager.getDateFormat().format(ClientSessionEnvironment.getInstance().getSessionEstablishDate()));

		try
		{
			Identifier userId = LoginManager.getUserId();
			User user = (User )StorableObjectPool.getStorableObject(userId, true);
			this.statusBar.setText(StatusBarModel.FIELD_USER, user.getName());
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
		}
	}

	public void setDomainSelected()
	{
		new CloseAllInternalCommand(this.desktopPane).execute();

		this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
				this,
				StatusMessageEvent.STATUS_MESSAGE, 
				LangModelGeneral.getString("Initiating")));

		this.aContext.getDispatcher().firePropertyChange(new StatusMessageEvent(
				this,
				StatusMessageEvent.STATUS_MESSAGE,
				LangModelGeneral.getString("DataLoaded")));

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
			this.domainId = LoginManager.getDomainId();
			Domain domain = (Domain )StorableObjectPool.getStorableObject(
					this.domainId, true);
			this.statusBar.setText("domain", domain.getName());
		}
		catch(ApplicationException ex)
		{
			ex.printStackTrace();
		}

		try {
			SchemeSampleData.populate(
					LoginManager.getUserId(),
					LoginManager.getDomainId());
		} catch(DatabaseException e) {
			e.printStackTrace();
		} catch(IllegalObjectEntityException e) {
			e.printStackTrace();
		} catch(IdentifierGenerationException e) {
			e.printStackTrace();
		} catch(ApplicationException e) {
			e.printStackTrace();
		}
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

		this.statusBar.setText(StatusBarModel.FIELD_STATUS, LangModelGeneral.getString("statusReady"));
		this.statusBar.setText(StatusBarModel.FIELD_SESSION, LangModelGeneral.getString("statusNoSession"));
		this.statusBar.setText(StatusBarModel.FIELD_USER, LangModelGeneral.getString("statusNoUser"));
		this.statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModelGeneral.getString("statusNoDomain"));
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
