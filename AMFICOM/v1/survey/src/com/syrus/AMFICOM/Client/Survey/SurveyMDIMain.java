// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Survey;

import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.CloseAllInternalCommand;
import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOptionsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.CreateSurveyReportCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.MapCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenAlarmsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenExtendedAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenMapEditorCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenNormsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenPrognosisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenResultsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenSchedulerCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenSchemeEditorCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.SurveyNewMapViewCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.SurveyViewAllCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.SurveyViewAllSchemeCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.SurveyViewMapSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.ViewSurveyNavigatorCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.AlarmReceivedEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.DefaultMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.DefaultPredictionApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.DefaultSchematicsApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.Module;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeTabbedPane;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Map.UI.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.Client.Map.UI.MapPropertyFrame;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Schematics.Elements.ElementsListFrame;
import com.syrus.AMFICOM.Client.Schematics.Elements.PropsFrame;
import com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmFrame;
import com.syrus.AMFICOM.Client.Survey.Alarm.AlarmPopupFrame;
import com.syrus.AMFICOM.Client.Survey.Result.ResultFrame;
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
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.WindowConstants;
//import com.syrus.AMFICOM.Client.Survey.UI.ReloadAttributes;

public class SurveyMDIMain extends JFrame 
	implements OperationListener, Module
{

	private Dispatcher			internalDispatcher	= new Dispatcher();
	public ApplicationContext	aContext			= new ApplicationContext();

	public String				domainId;

	public static final String alarmFrameDisplayed = "alarmFrameDisplayed";
	public static final String alarmPopupFrameDisplayed =
		"alarmPopupFrameDisplayed";
	public static final String resultFrameDisplayed = "resultFrameDisplayed";

	static IniFile				iniFile;
	static String				iniFileName			= "Survey.properties";

	static SimpleDateFormat		sdf					= new java.text.SimpleDateFormat(
															"dd.MM.yyyy HH:mm:ss");

	JPanel						mainPanel			= new JPanel();
	JScrollPane					scrollPane			= new JScrollPane();
	JViewport					viewport			= new JViewport();
	protected JDesktopPane		desktopPane			= new JDesktopPane();
	JPanel						statusBarPanel		= new JPanel();
	StatusBarModel				statusBar			= new StatusBarModel(0);
	SurveyMenuBar				menuBar				= new SurveyMenuBar();
	SurveyToolBar				toolBar				= new SurveyToolBar();

//	ReloadAttributes			myalarmTread		= null;

	private CreateSurveyReportCommand csrCommand = null;
	public AlarmFrame alarmsFrame = null;
	public AlarmPopupFrame alarmPopupFrame = null;
	public ResultFrame resultFrame = null;//рефлектограмма
	public SchemeViewerFrame schemeViewerFrame = null;//рефлектограмма

	//	SchemeAlarmUpdater schemeAlarmUpdater = null;
	
	SchemeTabbedPane schemePane = null;
	SchemeViewerFrame schemeFrame = null;
	ElementsListFrame elementsListFrame = null;
	PropsFrame propsFrame = null;

	public SurveyMDIMain(ApplicationContext aContext) 
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

	public SurveyMDIMain() 
	{
		this(new ApplicationContext());
	}

	private void jbInit()
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);

		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(screenSize.width,
				screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		this.setTitle(LangModelSurvey.getString("Observing_AMFICOM"));
		this.addComponentListener(new ComponentAdapter()
			{
				public void componentShown(ComponentEvent e)
				{
					thisComponentShown(e);
				}
			});

		/*
		 * //Center the window Dimension screenSize =
		 * Toolkit.getDefaultToolkit().getScreenSize(); Dimension frameSize =
		 * new Dimension (screenSize.width, screenSize.height - 24);
		 * setSize(frameSize); setLocation(0, 0);
		 */
		mainPanel.setLayout(new BorderLayout());//new FlowLayout());
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

		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(toolBar, BorderLayout.NORTH);

		this.setJMenuBar(menuBar);
	}

	void setDefaultModel(ApplicationModel aModel) {
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuStart", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuReport", true);
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
		statusBar.setWidth(StatusBarModel.FIELD_STATUS, 250);
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
		try {
			iniFile = new IniFile(iniFileName);
			System.out.println("read ini file " + iniFileName);
			//			objectName = iniFile.getValue("objectName");
		} catch (java.io.IOException e) {
			System.out.println("Error opening " + iniFileName
					+ " - setting defaults");
			//			SetDefaults();
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
		aModel.setCommand("menuSessionOptions", 
				new SessionOptionsCommand(aContext));
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

		aModel.setCommand("menuStartScheduler", 
				new OpenSchedulerCommand(
					Environment.getDispatcher(), 
					aContext));
		aModel.setCommand("menuStartAnalize", 
				new OpenAnalysisCommand(
					Environment.getDispatcher(), 
					aContext,
					new ReflectometryAnalyseApplicationModelFactory()));
		aModel.setCommand("menuStartAnalizeExt",
				new OpenExtendedAnalysisCommand(
					Environment.getDispatcher(),
					aContext,
					new ReflectometryAnalyseApplicationModelFactory()));
		aModel.setCommand("menuStartEvaluation", 
				new OpenNormsCommand(
					Environment.getDispatcher(), 
					aContext,
					new ReflectometryAnalyseApplicationModelFactory()));
		aModel.setCommand("menuStartPrognosis", 
				new OpenPrognosisCommand(
					Environment.getDispatcher(), 
					aContext,
					new DefaultPredictionApplicationModelFactory()));

		aModel.setCommand("menuViewMapOpen", 
				new SurveyNewMapViewCommand(
					desktopPane,
					aContext));
//				new SurveyViewAllCommand(
//					internalDispatcher, 
//					desktopPane, 
//					aContext));
		aModel.setCommand("menuViewMapEditor", 
				new OpenMapEditorCommand(
					Environment.getDispatcher(), 
					aContext, 
					new DefaultMapEditorApplicationModelFactory()));
		aModel.setCommand("menuViewMapClose", 
				new MapCloseCommand(
					internalDispatcher, 
					aContext));
		aModel.setCommand("menuViewMapSetup", 
				new SurveyViewMapSetupCommand(
					desktopPane, 
					aContext));
		//new SurveyNewMapViewCommand(internalDispatcher, desktopPane,
		// aContext, new MapSurveyNetApplicationModelFactory()));
		aModel.setCommand("menuViewSchemeOpen", 
				new SurveyViewAllSchemeCommand(
					internalDispatcher, 
					desktopPane, 
					aContext));
		aModel.setCommand("menuViewSchemeEditor",
				new OpenSchemeEditorCommand(
					internalDispatcher, 
					aContext,
					new DefaultSchematicsApplicationModelFactory()));
		aModel.setCommand("menuViewSchemeClose", 
				new SchemeCloseCommand(
					aContext, 
					null));
		aModel.setCommand("menuViewMeasurements",
				new ViewSurveyNavigatorCommand(
					internalDispatcher,
					desktopPane, 
					"Навигатор объектов", 
					aContext));
		aModel.setCommand("menuViewResults", 
				new OpenResultsCommand(
					internalDispatcher, 
					desktopPane, 
					aContext));
		aModel.setCommand("menuViewAlarms", 
				new OpenAlarmsCommand(
					internalDispatcher, 
					desktopPane, 
					aContext));
		aModel.setCommand("menuViewAll", 
				new SurveyViewAllCommand(
					internalDispatcher, 
					desktopPane, 
					aContext));

		csrCommand = new CreateSurveyReportCommand(aContext);
		csrCommand.setParameter(this);
		aModel.setCommand("menuTemplateReport", csrCommand);

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		aModel.fireModelChanged();

		if (ConnectionInterface.getActiveConnection() != null) {
			aContext.setConnectionInterface(ConnectionInterface
					.getActiveConnection());
			if (aContext.getConnectionInterface().isConnected())
					internalDispatcher.notify(new ContextChangeEvent(aContext
							.getConnectionInterface(),
							ContextChangeEvent.CONNECTION_OPENED_EVENT));
		} else {
			aContext.setConnectionInterface(Environment
					.getDefaultConnectionInterface());
			ConnectionInterface.setActiveConnection(aContext
					.getConnectionInterface());
			//			new CheckConnectionCommand(internalDispatcher,
			// aContext).execute();
		}
		if (SessionInterface.getActiveSession() != null) {
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			aContext.setConnectionInterface(aContext.getSessionInterface()
					.getConnectionInterface());
			if (aContext.getSessionInterface().isOpened())
					internalDispatcher.notify(new ContextChangeEvent(aContext
							.getSessionInterface(),
							ContextChangeEvent.SESSION_OPENED_EVENT));
		} else {
			aContext.setSessionInterface(Environment
					.getDefaultSessionInterface(aContext
							.getConnectionInterface()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}

	}

	public void setContext(ApplicationContext aContext) 
	{
		if (this.aContext != null)
			if (this.aContext.getDispatcher() != null) 
			{
				this.aContext.getDispatcher().unregister(this, ContextChangeEvent.type);
				Environment.getDispatcher().unregister(this, ContextChangeEvent.type);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_FRAME_SHOWN);
				this.aContext.getDispatcher().unregister(this, MapEvent.MAP_VIEW_CLOSED);
				this.aContext.getDispatcher().unregister(this, SchemeElementsEvent.type);

				this.aContext.getDispatcher().unregister(this, "addschemeelementevent");
				this.aContext.getDispatcher().unregister(this, "addschemeevent");
				this.aContext.getDispatcher().unregister(this, "mapaddschemeevent");
				this.aContext.getDispatcher().unregister(this, "mapaddschemeelementevent");

				this.aContext.getDispatcher().unregister(this, SurveyMDIMain.alarmFrameDisplayed);
				this.aContext.getDispatcher().unregister(this, SurveyMDIMain.alarmPopupFrameDisplayed);
				this.aContext.getDispatcher().unregister(this, SurveyMDIMain.resultFrameDisplayed);
				this.aContext.getDispatcher().unregister(this, SchemeViewerFrame.schemeFrameDisplayed);

				statusBar.removeDispatcher(this.aContext.getDispatcher());
			}

		if(aContext != null)
		{
			this.aContext = aContext;

			if(schemePane == null)
			{
				schemePane = new SchemeTabbedPane(aContext);
				schemeFrame = new SchemeViewerFrame(aContext, schemePane);
				schemeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
				desktopPane.add(schemeFrame);

				elementsListFrame = new ElementsListFrame(aContext, false);
				desktopPane.add(elementsListFrame);
				elementsListFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

				propsFrame = new PropsFrame(aContext, false);
				desktopPane.add(propsFrame);
				propsFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
			}

			if(aContext.getApplicationModel() == null)
				aContext.setApplicationModel(new ApplicationModel());
			setModel(aContext.getApplicationModel());

			this.aContext.getDispatcher().register(this, ContextChangeEvent.type);
			Environment.getDispatcher().register(this, ContextChangeEvent.type);
			this.aContext.getDispatcher().register(this, MapEvent.MAP_FRAME_SHOWN);
			this.aContext.getDispatcher().register(this, MapEvent.MAP_VIEW_CLOSED);
			this.aContext.getDispatcher().register(this, SchemeElementsEvent.type);

			this.aContext.getDispatcher().register(this, "addschemeelementevent");
			this.aContext.getDispatcher().register(this, "addschemeevent");
			this.aContext.getDispatcher().register(this, "mapaddschemeevent");
			this.aContext.getDispatcher().register(this, "mapaddschemeelementevent");

			this.aContext.getDispatcher().register(this, SurveyMDIMain.alarmFrameDisplayed);
			this.aContext.getDispatcher().register(this, SurveyMDIMain.alarmPopupFrameDisplayed);
			this.aContext.getDispatcher().register(this, SurveyMDIMain.resultFrameDisplayed);
			this.aContext.getDispatcher().register(this, SchemeViewerFrame.schemeFrameDisplayed);

			statusBar.addDispatcher(this.aContext.getDispatcher());
		}
	}

	public ApplicationContext getContext() {
		return aContext;
	}

	public void setModel(ApplicationModel aModel) 
	{
		aModel.addListener(toolBar);
		aModel.addListener(menuBar);
		toolBar.setModel(aModel);
		menuBar.setModel(aModel);

		aModel.fireModelChanged();
	}

	public ApplicationModel getModel() {
		return aContext.getApplicationModel();
	}

	protected void showSchemeFrames()
	{
		if(!schemeFrame.isVisible())
		{
			Dimension dim = desktopPane.getSize();
			schemeFrame.setLocation(
					dim.width / 5,
					dim.height / 4);
			schemeFrame.setSize(
					dim.width * 4 / 5,
					dim.height * 3 / 4);
			schemeFrame.setVisible(true);
	
			elementsListFrame.setSize(
					dim.width / 5, 
					2 * dim.height / 8);
			elementsListFrame.setLocation(
					0, 
					dim.height / 4);
			elementsListFrame.setVisible(true);

			propsFrame.setSize(
					dim.width / 5, 
					4 * dim.height / 8);
			propsFrame.setLocation(
					0, 
					dim.height / 2);
			propsFrame.setVisible(true);
		}
		schemeFrame.toFront();
	}

	public void operationPerformed(OperationEvent ae) 
	{
		if (ae.getActionCommand().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			JInternalFrame frame = (JInternalFrame) ae.getSource();

			Dimension dim = desktopPane.getSize();

			frame.setLocation(dim.width / 5, dim.height / 4);
			frame.setSize(dim.width * 4 / 5, dim.height * 3 / 4);

			ApplicationModel aModel = aContext.getApplicationModel();
			if(aModel != null)
			{
				aModel.setEnabled("menuViewMapEditor", true);
				aModel.setEnabled("menuViewMapClose", true);
				aModel.fireModelChanged();
			}

//				System.out.println("starting attribute reload thread");
//				myalarmTread = new ReloadAttributes((MapFrame) frame);//Запускаем
//																			// thread
//				myalarmTread.start();

		}
		else if (ae.getActionCommand().equals(SchemeElementsEvent.type))
		{
			SchemeElementsEvent see = (SchemeElementsEvent )ae;
			if(see.getID() == SchemeElementsEvent.OPEN_PRIMARY_SCHEME_EVENT)
			{
				Scheme scheme = (Scheme) see.obj;
				scheme.unpack();
				
				schemePane.openScheme(scheme);
				
				showSchemeFrames();
			}
		}
		else if (ae.getActionCommand().equals(MapEvent.MAP_VIEW_CLOSED))
		{
			for (int i = 0; i < desktopPane.getComponents().length; i++) {
				Component comp = desktopPane.getComponent(i);
				if (comp instanceof MapFrame) {
					((MapFrame) comp).setVisible(false);
					((MapFrame) comp).setMapView(null);
					((MapFrame) comp).setContext(null);
				} else if (comp instanceof MapPropertyFrame)
					((MapPropertyFrame) comp).setVisible(false);
				else if (comp instanceof MapElementsFrame)
						((MapElementsFrame) comp).setVisible(false);
			}
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuViewMapEditor", false);
			aModel.setEnabled("menuViewMapClose", false);
			aModel.fireModelChanged();

//			if (myalarmTread != null) myalarmTread.stop_running();
//			myalarmTread = null;
		}
		else if (ae.getActionCommand().equals(SchemeElementsEvent.type)) {
			SchemeElementsEvent sev = (SchemeElementsEvent) ae;
			if (sev.OPEN_PRIMARY_SCHEME)
			{
				/*
				 * if (schemeAlarmUpdater == null) { for(int i = 0; i <
				 * desktopPane.getComponents().length; i++) { Component comp =
				 * desktopPane.getComponent(i); if (comp instanceof
				 * SchemeViewerFrame) { SchemeViewerFrame frame =
				 * (SchemeViewerFrame)comp; if (frame.panel instanceof
				 * SchemePanel) { schemeAlarmUpdater = new
				 * SchemeAlarmUpdater(aContext, (SchemePanel)frame.panel);
				 * schemeAlarmUpdater.start(); // frame.startPathAnimator();
				 *
				 * break; } } } } else if (!schemeAlarmUpdater.flag)
				 * schemeAlarmUpdater.start();
				 */
				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuViewSchemeEditor", true);
				aModel.setEnabled("menuViewSchemeClose", true);
				aModel.fireModelChanged("");
			}
			if (sev.CLOSE_SCHEME)
			{
				for (int i = 0; i < desktopPane.getComponents().length; i++) {
					Component comp = desktopPane.getComponent(i);
					if (comp instanceof SchemeViewerFrame) {
						//						((SchemeViewerFrame )comp).stopPathAnimator();
						((SchemeViewerFrame) comp).setVisible(false);
					} else if (comp instanceof PropsFrame)
						((PropsFrame) comp).setVisible(false);
					else if (comp instanceof ElementsListFrame)
							((ElementsListFrame) comp).setVisible(false);
				}
				//				schemeAlarmUpdater.flag = false;

				ApplicationModel aModel = aContext.getApplicationModel();
				aModel.setEnabled("menuViewSchemeEditor", false);
				aModel.setEnabled("menuViewSchemeClose", false);
				aModel.fireModelChanged("");
			}
		}
		else if (ae.getActionCommand().equals("mapaddschemeevent"))
		{
			MapFrame fr = MapFrame.getMapMainFrame();

			if (fr != null)
				if (fr.getParent() != null)
					if (fr.getParent().equals(desktopPane)) {
						String scheme_id = (String) ae.getSource();
						Scheme scheme = (Scheme) Pool.get(
								Scheme.typ,
								scheme_id);
						scheme.unpack();
						
						schemePane.openScheme(scheme);
						
						showSchemeFrames();
					}
		}
		else if (ae.getActionCommand().equals("mapaddschemeelementevent"))
		{
			MapFrame fr = MapFrame.getMapMainFrame();

			if (fr != null)
				if (fr.getParent() != null)
					if (fr.getParent().equals(desktopPane)) 
					{
						String se_id = (String) ae.getSource();
						SchemeElement se = (SchemeElement) Pool.get(
								SchemeElement.typ, 
								se_id);
						se.unpack();

						schemePane.openSchemeElement(se);
						
						showSchemeFrames();
					}
		}
		else if (ae.getActionCommand().equals("addschemeevent"))
		{
			String scheme_id = (String) ae.getSource();
			Scheme scheme = (Scheme) Pool.get(
					Scheme.typ,
					scheme_id);
			scheme.unpack();
			
			schemePane.openScheme(scheme);
			
			showSchemeFrames();
		}
		else if (ae.getActionCommand().equals("addschemeelementevent"))
		{
			String se_id = (String) ae.getSource();
			SchemeElement se = (SchemeElement) Pool.get(
					SchemeElement.typ, 
					se_id);
			se.unpack();

			schemePane.openSchemeElement(se);
			
			showSchemeFrames();
		}
		else if(ae.getActionCommand().equals(ContextChangeEvent.type))
		{
			ContextChangeEvent cce = (ContextChangeEvent) ae;
			System.out.println("perform context change \""
					+ Long.toHexString(cce.change_type) + "\" at "
					+ this.getTitle());
			//			ApplicationModel aModel = aContext.getApplicationModel();
			if (cce.SESSION_OPENED)
			{
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi))
				{
					if (!Checker.checkCommandByUserId(aContext
							.getSessionInterface().getUserId(),
							Checker.enterObservingModul))
					{
						JOptionPane.showMessageDialog(
								null, 
								LangModel.getString("NotEnoughRights"), 
								LangModel.getString("Error"), 
								JOptionPane.OK_OPTION);
						return;
					}
					aContext.setDataSourceInterface(aContext
							.getApplicationModel().getDataSource(
									aContext.getSessionInterface()));
					setSessionOpened();
				}
			}
			if (cce.SESSION_CLOSED)
			{
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi))
				{
					aContext.setDataSourceInterface(null);

					setSessionClosed();
				}
			}
			if (cce.SESSION_CHANGING)
			{
				statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel
						.getString("statusSettingSession"));
			}
			if (cce.SESSION_CHANGED)
			{
				// nothing
			}
			if (cce.CONNECTION_OPENED)
			{
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci))
				{
					setConnectionOpened();
				}
			}
			if (cce.CONNECTION_CLOSED)
			{
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci))
				{
					setConnectionClosed();

				}
			}
			if (cce.CONNECTION_FAILED)
			{
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci))
				{
					setConnectionFailed();
				}
			}
			if (cce.CONNECTION_CHANGING)
			{
				statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel
						.getString("statusConnecting"));
			}
			if (cce.CONNECTION_CHANGED){
				// nothing ???
			}
			if (cce.DOMAIN_SELECTED)
			{
				domainId = (String) cce.getSource();
				statusBar.setText(
					StatusBarModel.FIELD_DOMAIN, 
					((ObjectResource )Pool.get(Domain.typ, domainId)).getName());
				setDomainSelected();
			}
		}
		else if (ae.getActionCommand().equals(SurveyMDIMain.alarmFrameDisplayed))
		{
			alarmsFrame = (AlarmFrame)ae.getSource();
		}
		else if (ae.getActionCommand().equals(SurveyMDIMain.alarmPopupFrameDisplayed))
		{
			alarmPopupFrame = (AlarmPopupFrame)ae.getSource();
		}
		else if (ae.getActionCommand().equals(SurveyMDIMain.resultFrameDisplayed))
		{
			resultFrame = (ResultFrame)ae.getSource();
		}
		else if (ae.getActionCommand().equals(SchemeViewerFrame.schemeFrameDisplayed))
		{
			schemeViewerFrame = (SchemeViewerFrame)ae.getSource();
		}
	}

	public void setConnectionOpened() {
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, aContext.getConnectionInterface()
				.getServiceURL());
	}

	public void setConnectionClosed() {
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);

		aModel.fireModelChanged();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusDisconnected"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusNoConnection"));
	}

	public void setConnectionFailed() {
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);

		aModel.fireModelChanged();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusError"));
		statusBar.setText(StatusBarModel.FIELD_SERVER, LangModel.getString("statusConnectionError"));
	}

	public void setSessionOpened() {
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);
		aModel.setEnabled("menuSessionDomain", true);
		aModel.setEnabled("menuReport", true);
		aModel.fireModelChanged();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SESSION, sdf.format(new Date(aContext
				.getSessionInterface().getLogonTime())));
		statusBar.setText(StatusBarModel.FIELD_USER, aContext.getSessionInterface().getUser());

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE, 
				LangModel.getString("Initiating")));
		dataSource.GetAlarmTypes();
		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("DataLoaded")));

		domainId = aContext.getSessionInterface().getDomainId();
		if (domainId != null && domainId.length() != 0) 
		{
			statusBar.setText(StatusBarModel.FIELD_DOMAIN, Pool.getName(Domain.typ, domainId));
			setDomainSelected();
		}
	}

	public void setDomainSelected() {
		/*
		 * if(alarmChecker != null) { alarmChecker.stop_running(); alarmChecker =
		 * null; }
		 */
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		new CloseAllInternalCommand(desktopPane).execute();

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE, 
				LangModel.getString("Initiating")));

		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
		new SchemeDataSourceImage(dataSource).LoadSchemes();

		aContext.getDispatcher().notify(new StatusMessageEvent(
				StatusMessageEvent.STATUS_MESSAGE,
				LangModel.getString("DataLoaded")));

		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuStartScheduler", true);
		aModel.setEnabled("menuStartAnalize", true);
		aModel.setEnabled("menuStartAnalizeExt", true);
		aModel.setEnabled("menuStartEvaluation", true);
		aModel.setEnabled("menuStartPrognosis", true);

		aModel.setEnabled("menuViewMapOpen", true);
		aModel.setEnabled("menuViewMapSetup", true);
		aModel.setEnabled("menuViewSchemeOpen", true);
		aModel.setEnabled("menuViewMeasurements", true);
		aModel.setEnabled("menuViewResults", true);
		aModel.setEnabled("menuViewAlarms", true);

		aModel.setEnabled("menuViewAll", true);

		aModel.setEnabled("menuReport", true);

		aModel.fireModelChanged();

		// update alarms list
		Pool.removeHash(Alarm.typ);
		aContext.getDispatcher().notify(new AlarmReceivedEvent(this));

		//		alarmChecker = new AlarmChecker(internalDispatcher, aContext, this,
		// desktopPane);
		//		alarmChecker.start();
	}

	public void setSessionClosed() {
		/*
		 * if(alarmChecker != null) { alarmChecker.stop_running(); alarmChecker =
		 * null; }
		 */
		ApplicationModel aModel = aContext.getApplicationModel();
		setDefaultModel(aModel);
		aModel.setEnabled("menuSessionNew", true);

		new CloseAllInternalCommand(desktopPane).execute();

		statusBar.setText(StatusBarModel.FIELD_STATUS, LangModel.getString("statusReady"));
		statusBar.setText(StatusBarModel.FIELD_SESSION, LangModel.getString("statusNoSession"));
		statusBar.setText(StatusBarModel.FIELD_USER, LangModel.getString("statusNoUser"));
		statusBar.setText(StatusBarModel.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher() {
		return internalDispatcher;
	}

	void thisComponentShown(ComponentEvent e)
	{
		initModule();
		desktopPane.setPreferredSize(desktopPane.getSize());
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			internalDispatcher.notify(new MapEvent(this, MapEvent.MAP_VIEW_CLOSED));

			Command closeCommand = aContext.getApplicationModel().getCommand("menuExit");
			this.setContext(null);
			closeCommand.execute();
			return;
		}
		super.processWindowEvent(e);
	}

}

