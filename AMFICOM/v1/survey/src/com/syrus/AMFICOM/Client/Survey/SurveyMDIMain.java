// Copyright (c) Syrus Systems 2000 Syrus Systems
package com.syrus.AMFICOM.Client.Survey;

import com.syrus.AMFICOM.Client.Map.MapElementsFrame;
import com.syrus.AMFICOM.Client.Map.MapMainFrame;
import com.syrus.AMFICOM.Client.Map.MapPropertyFrame;
import com.syrus.AMFICOM.Client.General.Checker;
import com.syrus.AMFICOM.Client.General.Command.CloseAllInternalCommand;
import com.syrus.AMFICOM.Client.General.Command.Config.OpenMapEditorCommand;
import com.syrus.AMFICOM.Client.General.Command.Config.OpenSchemeEditorCommand;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;
import com.syrus.AMFICOM.Client.General.Command.Scheme.SchemeCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOptionsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.MapCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenAlarmsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenExtendedAnalysisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenModelingCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenNormsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenPrognosisCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenResultsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenSchedulerCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.OpenTestsCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.SurveyViewAllCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.SurveyViewAllSchemeCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.SurveyViewMapSetupCommand;
import com.syrus.AMFICOM.Client.General.Command.Survey.ViewSurveyNavigatorCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowArrangeCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowArrangeIconsCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowCascadeCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowCloseAllCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowListCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowMinimizeAllCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowRestoreAllCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowTileHorizontalCommand;
import com.syrus.AMFICOM.Client.General.Command.Window.WindowTileVerticalCommand;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.Event.AlarmReceivedEvent;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.SchemeElementsEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.DefaultAlarmApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.DefaultModelApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.DefaultPredictionApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.DefaultResultApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.DefaultScheduleApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.MapSurveyISMApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.ReflectometryAnalyseApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Model.SchematicsApplicationModelFactory;
import com.syrus.AMFICOM.Client.General.Scheme.SchemePanelNoEdition;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.Resource.Alarm.Alarm;
import com.syrus.AMFICOM.Client.Resource.ConfigDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.MapDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.SchemeDataSourceImage;
import com.syrus.AMFICOM.Client.Resource.SurveyDataSourceImage;
import com.syrus.AMFICOM.Client.Schematics.Elements.ElementsListFrame;
import com.syrus.AMFICOM.Client.Schematics.Elements.PropsFrame;
import com.syrus.AMFICOM.Client.Schematics.Scheme.SchemeViewerFrame;
import com.syrus.AMFICOM.Client.Survey.UI.ReloadAttributes;
import com.syrus.io.IniFile;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.WindowConstants;

public class SurveyMDIMain extends JFrame implements OperationListener {

	private Dispatcher			internal_dispatcher	= new Dispatcher();
	public ApplicationContext	aContext			= new ApplicationContext();

	public String				domain_id;

	//	public AlarmChecker alarmChecker = null;

	static IniFile				iniFile;
	static String				iniFileName			= "Survey.properties";

	static SimpleDateFormat		sdf					= new java.text.SimpleDateFormat(
															"dd.MM.yyyy HH:mm:ss");

	//	public ConnectionInterface ci;
	//	public SessionInterface si;
	//	public DataSourceInterface dataSource;

	JPanel						mainPanel			= new JPanel();
	JScrollPane					scrollPane			= new JScrollPane();
	JViewport					viewport			= new JViewport();
	protected JDesktopPane		desktopPane			= new JDesktopPane();
	JPanel						statusBarPanel		= new JPanel();
	StatusBarModel				statusBar			= new StatusBarModel(0);
	SurveyMenuBar				menuBar				= new SurveyMenuBar();
	SurveyToolBar				toolBar				= new SurveyToolBar();

	ReloadAttributes			myalarmTread		= null;

	//	SchemeAlarmUpdater schemeAlarmUpdater = null;

	public SurveyMDIMain(ApplicationContext aContext) {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Environment.addWindow(this);
		setContext(aContext);
	}

	public SurveyMDIMain() {
		this(new ApplicationContext());
	}

	private void jbInit() throws Exception {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		setContentPane(mainPanel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(screenSize.width,
				screenSize.height - 24);
		setSize(frameSize);
		setLocation(0, 0);

		//		setSize(new Dimension(960, 650));
		this.setTitle(LangModelSurvey.getString("Observing_AMFICOM"));
		this
				.addComponentListener(new SurveyMDIMain_this_componentAdapter(
						this));
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});

		/*
		 * //Center the window Dimension screenSize =
		 * Toolkit.getDefaultToolkit().getScreenSize(); Dimension frameSize =
		 * new Dimension (screenSize.width, screenSize.height - 24);
		 * setSize(frameSize); setLocation(0, 0);
		 */
		mainPanel.setLayout(new BorderLayout());//new FlowLayout());
		//		mainPanel.setBackground(Color.lightGray);
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

		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(toolBar, BorderLayout.NORTH);

		this.setJMenuBar(menuBar);
	}

	void setDefaultModel(ApplicationModel aModel) {
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuExit", true);
		aModel.setEnabled("menuHelp", true);
		aModel.setEnabled("menuHelpAbout", true);
		aModel.setEnabled("menuView", true);
		aModel.setEnabled("menuEvaluate", true);
		aModel.setEnabled("menuVisualize", true);
		aModel.setEnabled("menuReport", true);
		aModel.setEnabled("menuWindow", true);

		aModel.setVisible("menuEvaluateRequest", false);
		aModel.setVisible("menuEvaluateTrackRequest", false);
		aModel.setVisible("menuEvaluateTrackTask", false);
		aModel.setVisible("menuMaintain", false);

	}

	public void init_module() {
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		statusBar.setWidth("status", 250);
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
		try {
			iniFile = new IniFile(iniFileName);
			System.out.println("read ini file " + iniFileName);
			//			objectName = iniFile.getValue("objectName");
		} catch (java.io.IOException e) {
			System.out.println("Error opening " + iniFileName
					+ " - setting defaults");
			//			SetDefaults();
		}

		aContext.setDispatcher(internal_dispatcher);
		/*
		 * internal_dispatcher.register(this, "contextchange");
		 * Environment.the_dispatcher.register(this, "contextchange");
		 * 
		 * internal_dispatcher.register(this, "mapframeshownevent");
		 * internal_dispatcher.register(this, "mapjframeshownevent");
		 * internal_dispatcher.register(this, "mapcloseevent");
		 * internal_dispatcher.register(this, SchemeElementsEvent.type);
		 * internal_dispatcher.register(this, "addschemeelementevent");
		 * internal_dispatcher.register(this, "addschemeevent");
		 * Environment.the_dispatcher.register(this, "mapaddschemeevent");
		 * Environment.the_dispatcher.register(this,
		 * "mapaddschemeelementevent");
		 */
		aModel.setCommand("menuSessionNew", new SessionOpenCommand(
				Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(
				Environment.the_dispatcher, aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(
				aContext));
		aModel.setCommand("menuSessionConnection",
				new SessionConnectionCommand(Environment.the_dispatcher,
						aContext));
		aModel.setCommand("menuSessionChangePassword",
				new SessionChangePasswordCommand(Environment.the_dispatcher,
						aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(
				Environment.the_dispatcher, aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		aModel.setCommand("menuEvaluateArchive",
				new ViewSurveyNavigatorCommand(internal_dispatcher,
						desktopPane, "Навигатор объектов", aContext));

		aModel.setCommand("menuEvaluateScheduler", new OpenSchedulerCommand(
				Environment.the_dispatcher, aContext,
				new DefaultScheduleApplicationModelFactory()));
		//		aModel.setCommand("menuEvaluateRequest", new
		// TestRequestCommand(aContext));

		aModel.setCommand("menuEvaluateTrackRequest", new OpenTestsCommand(
				internal_dispatcher, desktopPane, aContext, "testrequest"));
		aModel.setCommand("menuEvaluateTrackTask", new OpenTestsCommand(
				internal_dispatcher, desktopPane, aContext, "test"));

		aModel.setCommand("menuEvaluateResult", new OpenResultsCommand(
				internal_dispatcher, desktopPane, aContext,
				new DefaultResultApplicationModelFactory()));
		aModel.setCommand("menuEvaluateAnalize", new OpenAnalysisCommand(
				Environment.the_dispatcher, aContext,
				new ReflectometryAnalyseApplicationModelFactory()));
		aModel.setCommand("menuEvaluateAnalizeExt",
				new OpenExtendedAnalysisCommand(Environment.the_dispatcher,
						aContext,
						new ReflectometryAnalyseApplicationModelFactory()));
		aModel.setCommand("menuEvaluateNorms", new OpenNormsCommand(
				Environment.the_dispatcher, aContext,
				new ReflectometryAnalyseApplicationModelFactory()));
		aModel.setCommand("menuEvaluateModeling", new OpenModelingCommand(
				Environment.the_dispatcher, aContext,
				new DefaultModelApplicationModelFactory()));
		aModel.setCommand("menuEvaluatePrognosis", new OpenPrognosisCommand(
				Environment.the_dispatcher, aContext,
				new DefaultPredictionApplicationModelFactory()));
		aModel.setCommand("menuEvaluateViewAll", new SurveyViewAllCommand(
				internal_dispatcher, desktopPane, aContext));

		aModel.setCommand("menuVisualizeNet", new SurveyViewAllSchemeCommand(
				internal_dispatcher, desktopPane, aContext));
		aModel.setCommand("menuVisualizeNetGIS", new SurveyViewAllCommand(
				internal_dispatcher, desktopPane, aContext));
		aModel.setCommand("menuVisualizeMapEdit", new OpenMapEditorCommand(
				Environment.the_dispatcher, aContext, null));
		aModel.setCommand("menuVisualizeMapClose", new MapCloseCommand(
				internal_dispatcher, aContext));
		//new SurveyNewMapViewCommand(internal_dispatcher, desktopPane,
		// aContext, new MapSurveyNetApplicationModelFactory()));
		aModel.setCommand("menuVisualizeSchemeEdit",
				new OpenSchemeEditorCommand(internal_dispatcher, aContext,
						new SchematicsApplicationModelFactory()));
		aModel.setCommand("menuVisualizeSchemeClose", new SchemeCloseCommand(
				aContext, null));
		aModel.setCommand("menuViewAll", new SurveyViewAllCommand(
				internal_dispatcher, desktopPane, aContext));
		aModel.setCommand("menuViewMapSetup", new SurveyViewMapSetupCommand(
				desktopPane, aContext));

		aModel.setCommand("menuMaintainAlarm", new OpenAlarmsCommand(
				internal_dispatcher, desktopPane, aContext,
				new DefaultAlarmApplicationModelFactory()));

		aModel.setCommand("menuWindow", new WindowCloseCommand(desktopPane));
		aModel.setCommand("menuWindowClose",
				new WindowCloseCommand(desktopPane));
		aModel.setCommand("menuWindowCloseAll", new WindowCloseAllCommand(
				desktopPane));
		aModel.setCommand("menuWindowTileHorizontal",
				new WindowTileHorizontalCommand(desktopPane));
		aModel.setCommand("menuWindowTileVertical",
				new WindowTileVerticalCommand(desktopPane));
		aModel.setCommand("menuWindowCascade", new WindowCascadeCommand(
				desktopPane));
		aModel.setCommand("menuWindowArrange", new WindowArrangeCommand(
				desktopPane));
		aModel.setCommand("menuWindowArrangeIcons",
				new WindowArrangeIconsCommand(desktopPane));
		aModel.setCommand("menuWindowMinimizeAll",
				new WindowMinimizeAllCommand(desktopPane));
		aModel.setCommand("menuWindowRestoreAll", new WindowRestoreAllCommand(
				desktopPane));
		aModel.setCommand("menuWindowList", new WindowListCommand(desktopPane));

		aModel.add("menuHelpAbout", new HelpAboutCommand(this));

		setDefaultModel(aModel);
		aModel.fireModelChanged("");

		if (ConnectionInterface.getActiveConnection() != null) {
			aContext.setConnectionInterface(ConnectionInterface
					.getActiveConnection());
			if (aContext.getConnectionInterface().isConnected())
					internal_dispatcher.notify(new ContextChangeEvent(aContext
							.getConnectionInterface(),
							ContextChangeEvent.CONNECTION_OPENED_EVENT));
		} else {
			aContext.setConnectionInterface(Environment
					.getDefaultConnectionInterface());
			ConnectionInterface.setActiveConnection(aContext
					.getConnectionInterface());
			//			new CheckConnectionCommand(internal_dispatcher,
			// aContext).execute();
		}
		if (SessionInterface.getActiveSession() != null) {
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			aContext.setConnectionInterface(aContext.getSessionInterface()
					.getConnectionInterface());
			if (aContext.getSessionInterface().isOpened())
					internal_dispatcher.notify(new ContextChangeEvent(aContext
							.getSessionInterface(),
							ContextChangeEvent.SESSION_OPENED_EVENT));
		} else {
			aContext.setSessionInterface(Environment
					.getDefaultSessionInterface(aContext
							.getConnectionInterface()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}

	}

	public void setContext(ApplicationContext aContext) {
		if (this.aContext != null)
				if (this.aContext.getDispatcher() != null) {
					internal_dispatcher.unregister(this, "contextchange");
					Environment.the_dispatcher
							.unregister(this, "contextchange");

					internal_dispatcher.unregister(this, "mapframeshownevent");
					internal_dispatcher.unregister(this, "mapjframeshownevent");
					internal_dispatcher.unregister(this, "mapcloseevent");
					internal_dispatcher.unregister(this,
							SchemeElementsEvent.type);
					internal_dispatcher.unregister(this,
							"addschemeelementevent");
					internal_dispatcher.unregister(this, "addschemeevent");
					Environment.the_dispatcher.unregister(this,
							"mapaddschemeevent");
					Environment.the_dispatcher.unregister(this,
							"mapaddschemeelementevent");
				}
		this.aContext = aContext;
		internal_dispatcher.register(this, "contextchange");
		Environment.the_dispatcher.register(this, "contextchange");

		internal_dispatcher.register(this, "mapframeshownevent");
		internal_dispatcher.register(this, "mapjframeshownevent");
		internal_dispatcher.register(this, "mapcloseevent");
		internal_dispatcher.register(this, SchemeElementsEvent.type);
		internal_dispatcher.register(this, "addschemeelementevent");
		internal_dispatcher.register(this, "addschemeevent");
		Environment.the_dispatcher.register(this, "mapaddschemeevent");
		Environment.the_dispatcher.register(this, "mapaddschemeelementevent");
		if (aContext.getApplicationModel() == null)
				aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());
	}

	public ApplicationContext getContext() {
		return aContext;
	}

	public void setModel(ApplicationModel aModel) {
		//		aModel = a;
		toolBar.setModel(aModel);
		menuBar.setModel(aModel);
		aModel.addListener(toolBar);
		aModel.addListener(menuBar);

		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel() {
		return aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae) {
		if (ae.getActionCommand().equals("mapframeshownevent")) {
			JInternalFrame frame = (JInternalFrame) ae.getSource();

			Dimension dim = desktopPane.getSize();

			frame.setLocation(dim.width / 5, dim.height / 4);
			frame.setSize(dim.width * 4 / 5, dim.height * 3 / 4);

			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuVisualizeMapEdit", true);
			aModel.setEnabled("menuVisualizeMapClose", true);
			aModel.fireModelChanged("");

			//			if (aModel.isEnabled("mapActionReload"))
			{
				System.out.println("starting attribute reload thread");
				myalarmTread = new ReloadAttributes((MapMainFrame) frame);//Запускаем
																		  // thread
				myalarmTread.start();
			}

		} else if (ae.getActionCommand().equals("mapjframeshownevent")) {
			JInternalFrame frame = (JInternalFrame) ae.getSource();

			Dimension dim = desktopPane.getPreferredSize();

			frame.setLocation(dim.width / 5, dim.height / 4);
			frame.setSize(dim.width * 3 / 5, dim.height * 3 / 4);
		} else if (ae.getActionCommand().equals("mapcloseevent")) {
			for (int i = 0; i < desktopPane.getComponents().length; i++) {
				Component comp = desktopPane.getComponent(i);
				if (comp instanceof MapMainFrame) {
					((MapMainFrame) comp).setVisible(false);
					((MapMainFrame) comp).setMapContext(null);
					((MapMainFrame) comp).setContext(null);
				} else if (comp instanceof MapPropertyFrame)
					((MapPropertyFrame) comp).setVisible(false);
				else if (comp instanceof MapElementsFrame)
						((MapElementsFrame) comp).setVisible(false);
			}
			ApplicationModel aModel = aContext.getApplicationModel();
			aModel.setEnabled("menuVisualizeMapEdit", false);
			aModel.setEnabled("menuVisualizeMapClose", false);
			aModel.fireModelChanged("");

			if (myalarmTread != null) myalarmTread.stop_running();
			myalarmTread = null;
		} else if (ae.getActionCommand().equals(SchemeElementsEvent.type)) {
			SchemeElementsEvent sev = (SchemeElementsEvent) ae;
			if (sev.OPEN_PRIMARY_SCHEME) {
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
				aModel.setEnabled("menuVisualizeSchemeEdit", true);
				aModel.setEnabled("menuVisualizeSchemeClose", true);
				aModel.fireModelChanged("");
			}
			if (sev.CLOSE_SCHEME) {
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
				aModel.setEnabled("menuVisualizeSchemeEdit", false);
				aModel.setEnabled("menuVisualizeSchemeClose", false);
				aModel.fireModelChanged("");
			}
		} else if (ae.getActionCommand().equals("mapaddschemeevent")) {
			MapMainFrame fr = (MapMainFrame) Pool.get("environment",
					"mapmainframe");
			if (fr != null)
					if (fr.getParent() != null)
							if (fr.getParent().equals(desktopPane)) {
								Dimension dim = desktopPane.getSize();

								String scheme_id = (String) ae.getSource();
								Scheme scheme = (Scheme) Pool.get(Scheme.typ,
										scheme_id);
								scheme.unpack();

								SchemePanelNoEdition panel = new SchemePanelNoEdition(
										aContext);
								panel.ignore_loading = true;
								//ElementsEditorFrame frame = new
								// ElementsEditorFrame(aContext, panel);
								SchemeViewerFrame frame = new SchemeViewerFrame(
										aContext, panel);
								frame
										.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
								frame.setTitle(scheme.getName());
								desktopPane.add(frame);

								frame
										.setLocation(dim.width / 5,
												dim.height / 4);
								frame.setSize(dim.width * 4 / 5,
										dim.height * 3 / 4);
								frame.setVisible(true);
								frame.toFront();

								panel.openScheme(scheme);
							}
		} else if (ae.getActionCommand().equals("mapaddschemeelementevent")) {
			MapMainFrame fr = (MapMainFrame) Pool.get("environment",
					"mapmainframe");
			if (fr != null)
					if (fr.getParent() != null)
							if (fr.getParent().equals(desktopPane)) {
								String se_id = (String) ae.getSource();
								SchemeElement se = (SchemeElement) Pool.get(
										SchemeElement.typ, se_id);
								se.unpack();

								SchemePanelNoEdition panel = new SchemePanelNoEdition(
										aContext);
								panel.setGraphSize(new Dimension());
								SchemeViewerFrame frame = new SchemeViewerFrame(
										aContext, panel);
								frame
										.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
								frame.setTitle(se.getName());
								desktopPane.add(frame);

								Dimension dim = desktopPane.getSize();

								frame
										.setLocation(dim.width / 5,
												dim.height / 4);
								frame.setSize(dim.width * 4 / 5,
										dim.height * 3 / 4);
								frame.setVisible(true);
								frame.toFront();
								panel.openSchemeElement(se);
							}
		} else if (ae.getActionCommand().equals("addschemeevent")) {
			Dimension dim = desktopPane.getSize();

			String scheme_id = (String) ae.getSource();
			Scheme scheme = (Scheme) Pool.get(Scheme.typ, scheme_id);
			scheme.unpack();

			SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
			panel.ignore_loading = true;
			//ElementsEditorFrame frame = new ElementsEditorFrame(aContext,
			// panel);
			SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setTitle(scheme.getName());
			desktopPane.add(frame);

			frame.setLocation(dim.width / 5, dim.height / 4);
			frame.setSize(dim.width * 4 / 5, dim.height * 3 / 4);
			frame.setVisible(true);
			frame.toFront();

			panel.openScheme(scheme);
		} else if (ae.getActionCommand().equals("addschemeelementevent")) {
			String se_id = (String) ae.getSource();
			SchemeElement se = (SchemeElement) Pool.get(SchemeElement.typ,
					se_id);
			se.unpack();

			SchemePanelNoEdition panel = new SchemePanelNoEdition(aContext);
			panel.setGraphSize(new Dimension());
			SchemeViewerFrame frame = new SchemeViewerFrame(aContext, panel);
			frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			frame.setTitle(se.getName());
			desktopPane.add(frame);

			Dimension dim = desktopPane.getSize();

			frame.setLocation(dim.width / 5, dim.height / 4);
			frame.setSize(dim.width * 4 / 5, dim.height * 3 / 4);
			frame.setVisible(true);
			frame.toFront();
			panel.openSchemeElement(se);
		} else if (ae.getActionCommand().equals("contextchange")) {
			ContextChangeEvent cce = (ContextChangeEvent) ae;
			System.out.println("perform context change \""
					+ Long.toHexString(cce.change_type) + "\" at "
					+ this.getTitle());
			//			ApplicationModel aModel = aContext.getApplicationModel();
			if (cce.SESSION_OPENED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					if (!Checker.checkCommandByUserId(aContext
							.getSessionInterface().getUserId(),
							Checker.enterObservingModul)) {
						new SessionCloseCommand(Environment.the_dispatcher,
								aContext).execute();
						return;
					}
					aContext.setDataSourceInterface(aContext
							.getApplicationModel().getDataSource(
									aContext.getSessionInterface()));
					setSessionOpened();
				}
			}
			if (cce.SESSION_CLOSED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					aContext.setDataSourceInterface(null);

					setSessionClosed();
				}
			}
			if (cce.SESSION_CHANGING) {
				statusBar.setText("status", LangModel
						.String("statusSettingSession"));
			}
			if (cce.SESSION_CHANGED) {
				// nothing
			}
			if (cce.CONNECTION_OPENED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					setConnectionOpened();
				}
			}
			if (cce.CONNECTION_CLOSED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					setConnectionClosed();

				}
			}
			if (cce.CONNECTION_FAILED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					setConnectionFailed();
				}
			}
			if (cce.CONNECTION_CHANGING) {
				statusBar.setText("status", LangModel
						.String("statusConnecting"));
			}
			if (cce.CONNECTION_CHANGED) {
			}
			if (cce.DOMAIN_SELECTED) {
				domain_id = (String) cce.getSource();
				statusBar.setText("domain", Pool.getName("domain", domain_id));
				setDomainSelected();
			}
		}
	}

	public void setConnectionOpened() {
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionConnection", true);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("server", aContext.getConnectionInterface()
				.getServiceURL());
	}

	public void setConnectionClosed() {
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.String("statusDisconnected"));
		statusBar.setText("server", LangModel.String("statusNoConnection"));
	}

	public void setConnectionFailed() {
		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);

		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.String("statusError"));
		statusBar.setText("server", LangModel.String("statusConnectionError"));
	}

	public void setSessionOpened() {
		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.disable("menuSessionNew");
		aModel.enable("menuSessionClose");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");
		aModel.enable("menuSessionDomain");
		aModel.fireModelChanged("");

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("session", sdf.format(new Date(aContext
				.getSessionInterface().getLogonTime())));
		statusBar.setText("user", aContext.getSessionInterface().getUser());

		aContext.getDispatcher().notify(
				new StatusMessageEvent("Инициализация данных..."));
		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		new SurveyDataSourceImage(dataSource).LoadTestTypes();
		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		new SurveyDataSourceImage(dataSource).LoadModelingTypes();
		dataSource.GetAlarmTypes();
		aContext.getDispatcher().notify(
				new StatusMessageEvent("Данные загружены"));

		domain_id = aContext.getSessionInterface().getDomainId();
		if (domain_id != null && !domain_id.equals("")) {
			statusBar.setText("domain", Pool.getName("domain", domain_id));
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

		aContext.getDispatcher().notify(
				new StatusMessageEvent("Инициализация данных..."));
		new SchemeDataSourceImage(dataSource).LoadNetDirectory();
		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		new ConfigDataSourceImage(dataSource).LoadNet();
		new ConfigDataSourceImage(dataSource).LoadISM();
		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();
		new SchemeDataSourceImage(dataSource).LoadSchemeProto();
		new SchemeDataSourceImage(dataSource).LoadSchemes();
		new MapDataSourceImage(dataSource).LoadProtoElements();
		new MapDataSourceImage(dataSource).LoadMaps();
		aContext.getDispatcher().notify(
				new StatusMessageEvent("Данные загружены"));

		ApplicationModel aModel = aContext.getApplicationModel();

		aModel.setEnabled("menuEvaluateScheduler", true);
		aModel.setEnabled("menuEvaluateAnalize", true);
		aModel.setEnabled("menuEvaluateAnalizeExt", true);
		aModel.setEnabled("menuEvaluateNorms", true);
		aModel.setEnabled("menuEvaluateModeling", true);
		aModel.setEnabled("menuEvaluatePrognosis", true);

		aModel.setEnabled("menuEvaluateArchive", true);
		aModel.setEnabled("menuEvaluateResult", true);
		aModel.setEnabled("menuMaintainAlarm", true);

		aModel.enable("menuVisualizeNetGIS");
		aModel.enable("menuVisualizeNet");
		aModel.enable("menuViewAll");

		aModel.enable("menuViewMapSetup");

		aModel.fireModelChanged("");

		// update alarms list
		Pool.removeHash(Alarm.typ);
		//		new SurveyDataSourceImage(dataSource).GetAlarms();
		aContext.getDispatcher().notify(new AlarmReceivedEvent(this));

		//		alarmChecker = new AlarmChecker(internal_dispatcher, aContext, this,
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
		aModel.enable("menuSessionNew");
		aModel.fireModelChanged("");

		new CloseAllInternalCommand(desktopPane).execute();

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("session", LangModel.String("statusNoSession"));
		statusBar.setText("user", LangModel.String("statusNoUser"));
		statusBar.setText("domain", LangModel.String("statusNoDomain"));
	}

	public Dispatcher getInternalDispatcher() {
		return internal_dispatcher;
	}

	void this_componentShown(ComponentEvent e) {
		init_module();
		//		desktopPane.setPreferredSize(desktopPane.getSize());
	}

	void this_windowClosing(WindowEvent e) {
		internal_dispatcher
				.notify(new OperationEvent(this, 0, "mapcloseevent"));
		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
			//			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			//			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			internal_dispatcher.notify(new OperationEvent(this, 0,
					"mapcloseevent"));
			internal_dispatcher.unregister(this, "contextchange");
			Environment.the_dispatcher.unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}

	public void dispose() {
		internal_dispatcher
				.notify(new OperationEvent(this, 0, "mapcloseevent"));
		internal_dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");

		super.dispose();
	}
}

class SurveyMDIMain_this_componentAdapter extends
		java.awt.event.ComponentAdapter {

	SurveyMDIMain	adaptee;

	SurveyMDIMain_this_componentAdapter(SurveyMDIMain adaptee) {
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e) {
		adaptee.this_componentShown(e);
	}
}

