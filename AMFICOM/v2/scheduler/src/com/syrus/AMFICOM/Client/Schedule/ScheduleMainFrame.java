
package com.syrus.AMFICOM.Client.Schedule;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.General.Command.ExitCommand;
import com.syrus.AMFICOM.Client.General.Command.HelpAboutCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionChangePasswordCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionCloseCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionConnectionCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionDomainCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOpenCommand;
import com.syrus.AMFICOM.Client.General.Command.Session.SessionOptionsCommand;
import com.syrus.AMFICOM.Client.General.Event.ContextChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Event.RefChangeEvent;
import com.syrus.AMFICOM.Client.General.Event.RefUpdateEvent;
import com.syrus.AMFICOM.Client.General.Event.StatusMessageEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.UI.StatusBarModel;
import com.syrus.AMFICOM.Client.General.UI.WindowArranger;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Schedule.UI.ElementsTreeFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.PlanFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.SaveParametersFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TableFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TestParametersFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TestRequestFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TimeParametersFrame;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;

public class ScheduleMainFrame extends JFrame implements OperationListener {

	public static final String	CONTEXT_CHANGE	= "contextchange";

	//	public static final String SCHEDULER_INI_FILE = "schedule.ini";

	ApplicationContext			aContext;
	JDesktopPane				desktopPane		= new JDesktopPane();

	Dispatcher					dispatcher;

	JPanel						mainPanel		= new JPanel();
	ScheduleMainMenuBar			menuBar			= new ScheduleMainMenuBar();
	TestParametersFrame			paramsFrame;

	PlanFrame					planFrame;
	TestRequestFrame			propsFrame;
	SaveParametersFrame			saveFrame;
	JScrollPane					scrollPane		= new JScrollPane();

	StatusBarModel				statusBar		= new StatusBarModel(0);

	JPanel						statusBarPanel	= new JPanel();
	TableFrame					tableFrame;
	TimeParametersFrame			timeFrame;

	ScheduleMainToolBar			toolBar			= new ScheduleMainToolBar();
	ElementsTreeFrame			treeFrame;
	JViewport					viewport		= new JViewport();

	public ScheduleMainFrame() {
		this(new ApplicationContext());
	}

	//TestFilterFrame testFilterFrame;

	public ScheduleMainFrame(ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();
		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				super.componentShown(e);
				ScheduleMainFrame.this.desktopPane.setPreferredSize(ScheduleMainFrame.this.desktopPane.getSize());
				new ScheduleWindowArranger(ScheduleMainFrame.this).arrange();
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				ScheduleMainFrame.this.dispatcher.unregister(ScheduleMainFrame.this, CONTEXT_CHANGE);
				Environment.the_dispatcher.unregister(ScheduleMainFrame.this, CONTEXT_CHANGE);
				ScheduleMainFrame.this.aContext.getApplicationModel().getCommand(ScheduleMainMenuBar.MENU_EXIT)
						.execute();
			}
		});

		setContentPane(this.mainPanel);
		setResizable(true);
		setTitle(LangModelSchedule.getString("Scheduling_AMFICOM"));
		setJMenuBar(this.menuBar);

		this.mainPanel.setLayout(new BorderLayout());
		this.desktopPane.setBackground(SystemColor.control.darker().darker());

		this.statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.statusBarPanel.setLayout(new BorderLayout());
		this.statusBarPanel.add(this.statusBar, BorderLayout.CENTER);

		this.statusBar.add("status");
		this.statusBar.add("server");
		this.statusBar.add("session");
		this.statusBar.add("user");
		this.statusBar.add("domain");
		this.statusBar.add("time");

		this.viewport.setView(this.desktopPane);
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.mainPanel.add(this.toolBar, BorderLayout.NORTH);
		this.mainPanel.add(this.statusBarPanel, BorderLayout.SOUTH);
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);

		//		PlanLayeredPanel panel = new PlanLayeredPanel();
		this.planFrame = new PlanFrame(aContext);

		this.desktopPane.add(this.planFrame);

		this.paramsFrame = new TestParametersFrame(aContext);
		this.desktopPane.add(this.paramsFrame);

		this.propsFrame = new TestRequestFrame(aContext);
		this.desktopPane.add(this.propsFrame);

		this.timeFrame = new TimeParametersFrame(aContext);
		this.desktopPane.add(this.timeFrame);

		this.treeFrame = new ElementsTreeFrame(aContext);
		this.desktopPane.add(this.treeFrame);

		this.saveFrame = new SaveParametersFrame(aContext);
		this.desktopPane.add(this.saveFrame);

		this.tableFrame = new TableFrame(aContext);
		this.desktopPane.add(this.tableFrame);

		//		this.testFilterFrame = new TestFilterFrame(aContext);
		//		this.desktopPane.add(this.testFilterFrame);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(screenSize.width, screenSize.height - 24);

		setSize(frameSize);
		setLocation(0, 0);

		Environment.addWindow(this);
		initModule();
		setContext(aContext);
	}

	public ApplicationContext getContext() {
		return this.aContext;
	}

	public Dispatcher getInternalDispatcher() {
		return this.dispatcher;
	}

	public ApplicationModel getModel() {
		return this.aContext.getApplicationModel();
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_CHANGE_STATUSBAR_STATE)) {
			boolean value = ((Boolean) obj).booleanValue();
			//Environment.log(Environment.LOG_LEVEL_INFO,
			// "progressBar:" +
			// value);
			this.statusBar.setProgressBarEnable(value);
		}
		if (commandName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
			StatusMessageEvent sme = (StatusMessageEvent) ae;
			this.statusBar.setText("status", sme.getText());
		} else if (commandName.equals(CONTEXT_CHANGE)) {
			ContextChangeEvent cce = (ContextChangeEvent) ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at "
					+ this.getTitle());
			// ApplicationModel aModel =
			// aContext.getApplicationModel();
			if (cce.SESSION_OPENED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (this.aContext.getSessionInterface().equals(ssi)) {
					setSessionOpened();

					this.statusBar.setText("status", LangModel.getString("statusReady"));
					this.statusBar.setText("session", ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(this.aContext
							.getSessionInterface().getLogonTime())));
					this.statusBar.setText("user", this.aContext.getSessionInterface().getUser());
				}
			}
			if (cce.SESSION_CLOSED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (this.aContext.getSessionInterface().equals(ssi)) {

					setSessionClosed();

					this.statusBar.setText("status", LangModel.getString("statusReady"));
					this.statusBar.setText("session", LangModel.getString("statusNoSession"));
					this.statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if (cce.CONNECTION_OPENED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (ConnectionInterface.getInstance().equals(cci)) {
					setConnectionOpened();

					this.statusBar.setText("status", LangModel.getString("statusReady"));
					this.statusBar.setText("server", ConnectionInterface.getInstance().getServerName());
				}
			}
			if (cce.CONNECTION_CLOSED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (this.aContext.getConnectionInterface().equals(cci)) {
					this.statusBar.setText("status", LangModel.getString("statusError"));
					this.statusBar.setText("server", LangModel.getString("statusConnectionError"));

					this.statusBar.setText("status", LangModel.getString("statusDisconnected"));
					this.statusBar.setText("server", LangModel.getString("statusNoConnection"));

					setConnectionClosed();

				}
			}
			if (cce.CONNECTION_FAILED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (this.aContext.getConnectionInterface().equals(cci)) {
					this.statusBar.setText("status", LangModel.getString("statusError"));
					this.statusBar.setText("server", LangModel.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if (cce.DOMAIN_SELECTED) {
				setDomainSelected();
			}
		}

	}

	public void setConnectionClosed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PLAN, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TREE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TIME, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TABLE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, false);
		aModel.fireModelChanged("");
	}

	public void setConnectionFailed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.fireModelChanged("");
	}

	public void setConnectionOpened() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CONNECTION, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PLAN, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TREE, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TIME, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TABLE, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, true);

		aModel.fireModelChanged("");
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		aContext.setDispatcher(this.dispatcher);
		setModel(aContext.getApplicationModel());
	}

	public void setDomainSelected() {
		//DataSourceInterface dataSource = this.aContext.getDataSource();
		//new ConfigDataSourceImage(dataSource).LoadISM();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CLOSE, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_OPTIONS, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, true);

		aModel.fireModelChanged("");

		
		try {
			RISDSessionInfo sessionInterface = (RISDSessionInfo) this.aContext.getSessionInterface();
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(sessionInterface
					.getDomainIdentifier(), true);
			this.statusBar.setText("domain", domain.getName());
		} catch (DatabaseException e) {
			SchedulerModel.showErrorMessage(this, e);
		} catch (CommunicationException e) {
			SchedulerModel.showErrorMessage(this, e);
		}
		

		this.treeFrame.init();

		MeasurementStorableObjectPool.cleanChangedStorableObjects();

		this.dispatcher.notify(new OperationEvent(this, 0, SchedulerModel.COMMAND_CLEAN));

		this.paramsFrame.setVisible(true);
		this.propsFrame.setVisible(true);
		this.treeFrame.setVisible(true);
		this.timeFrame.setVisible(true);
		this.planFrame.setVisible(true);
		this.saveFrame.setVisible(true);
		this.tableFrame.setVisible(true);
		//		testFilterFrame.setVisible(true);
	}

	public void setModel(ApplicationModel aModel) {
		this.toolBar.setModel(aModel);
		this.menuBar.setModel(aModel);
		aModel.addListener(this.menuBar.getApplicationModelListener());
		aModel.addListener(this.toolBar);
		aModel.fireModelChanged("");
	}

	public void setSessionClosed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_DOMAIN, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW, true);

		aModel.fireModelChanged("");

		this.statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	public void setSessionOpened() {
		//this.checker = new
		// Checker(aContext.getDataSourceInterface());

		DataSourceInterface dataSource = this.aContext.getDataSource();

		this.aContext.getDispatcher().notify(
												new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE,
																		LangModelSchedule.getString("Loading_BD")));
		//		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		//		new SurveyDataSourceImage(dataSource).LoadTestTypes();
		//		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		//		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		//		new SurveyDataSourceImage(dataSource).LoadModelingTypes();
		//		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();

		//		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		//		SurveyDataSourceImage sdsi = new SurveyDataSourceImage(dataSource);
		//		sdsi.LoadTestTypes();
		//		sdsi.LoadAnalysisTypes();
		//		sdsi.LoadEvaluationTypes();

		this.aContext.getDispatcher().notify(
												new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE,
																		LangModelSchedule
																				.getString("Loding_BD_finished")));

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_DOMAIN, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW, false);
		aModel.fireModelChanged("");
		String domainId = this.aContext.getSessionInterface().getDomainId();
		if (domainId != null && !domainId.equals(""))
			this.dispatcher.notify(new ContextChangeEvent(domainId, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			//cManager.saveIni();
			this.dispatcher.unregister(this, CONTEXT_CHANGE);
			Environment.getDispatcher().unregister(this, CONTEXT_CHANGE);
			this.aContext.getApplicationModel().getCommand(ScheduleMainMenuBar.MENU_EXIT).execute();
			return;
		}
		super.processWindowEvent(e);
	}

	void initModule() {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		this.statusBar.distribute();
		//		statusBar.setWidth("status", 100);
		//		statusBar.setWidth("server", 250);
		//		statusBar.setWidth("session", 200);
		//		statusBar.setWidth("user", 100);
		//		statusBar.setWidth("time", 50);
		this.statusBar.setWidth("status", 300);
		this.statusBar.setWidth("server", 250);
		this.statusBar.setWidth("session", 200);
		this.statusBar.setWidth("user", 100);
		this.statusBar.setWidth("domain", 150);
		this.statusBar.setWidth("time", 50);

		this.statusBar.setText("status", LangModel.getString("statusReady"));
		this.statusBar.setText("server", LangModel.getString("statusNoConnection"));
		this.statusBar.setText("session", LangModel.getString("statusNoSession"));
		this.statusBar.setText("user", LangModel.getString("statusNoUser"));
		this.statusBar.setText("domain", LangModel.getString("statusNoDomain"));
		this.statusBar.setText("time", " ");
		this.statusBar.organize();

		this.aContext.setDispatcher(this.dispatcher);
		this.dispatcher.register(this, StatusMessageEvent.STATUS_MESSAGE);
		this.dispatcher.register(this, RefChangeEvent.typ);
		this.dispatcher.register(this, RefUpdateEvent.typ);
		this.dispatcher.register(this, SchedulerModel.COMMAND_CHANGE_STATUSBAR_STATE);

		this.dispatcher.register(this, CONTEXT_CHANGE);
		Dispatcher dispatcher = Environment.getDispatcher();
		dispatcher.register(this, CONTEXT_CHANGE);

		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_NEW, new SessionOpenCommand(dispatcher, this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_CLOSE, new SessionCloseCommand(dispatcher, this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_OPTIONS, new SessionOptionsCommand(this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_CONNECTION, new SessionConnectionCommand(dispatcher,
																									this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD,
							new SessionChangePasswordCommand(dispatcher, this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_DOMAIN, new SessionDomainCommand(dispatcher, this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_EXIT, new ExitCommand(this));

		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PLAN, this.planFrame.getCommand());
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TREE, this.treeFrame.getCommand());
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, this.paramsFrame.getCommand());
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, this.saveFrame.getCommand());
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, this.propsFrame.getCommand());
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TIME, this.timeFrame.getCommand());
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TABLE, this.tableFrame.getCommand());

		/**
		 * TODO remove when all ok
		 */
		//CreateScheduleReportCommand csrCommand = new
		// CreateScheduleReportCommand(aContext);
		//csrCommand.setParameter(this);
		//aModel.setCommand(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT,
		// csrCommand);
		aModel.setCommand(ScheduleMainMenuBar.MENU_HELP_ABOUT, new HelpAboutCommand(this));

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

		ConnectionInterface connectionInterface = ConnectionInterface.getInstance();
		if (connectionInterface != null) {
			if (connectionInterface.isConnected())
				this.dispatcher.notify(new ContextChangeEvent(connectionInterface,
																ContextChangeEvent.CONNECTION_OPENED_EVENT));
		}
		if (SessionInterface.getActiveSession() != null) {
			this.aContext.setSessionInterface(SessionInterface.getActiveSession());
			if (this.aContext.getSessionInterface().isOpened())
				this.dispatcher.notify(new ContextChangeEvent(this.aContext.getSessionInterface(),
																ContextChangeEvent.SESSION_OPENED_EVENT));
		} else {
			this.aContext.setSessionInterface(Environment.getDefaultSessionInterface(connectionInterface));
			SessionInterface.setActiveSession(this.aContext.getSessionInterface());
		}
	}

	void setDefaultModel(ApplicationModel aModel) {
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CONNECTION, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PLAN, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TREE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TIME, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_TABLE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_HELP, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_HELP_ABOUT, true);

	}

	public PlanFrame getPlanFrame() {
		return this.planFrame;
	}
}

class ScheduleWindowArranger extends WindowArranger {

	public ScheduleWindowArranger(ScheduleMainFrame mainframe) {
		super(mainframe);
	}

	public void arrange() {
		ScheduleMainFrame f = (ScheduleMainFrame) this.mainframe;

		int w = f.desktopPane.getSize().width;
		int h = f.desktopPane.getSize().height;

		int minWidth = Math.max(250, w / 5);

		normalize(f.planFrame);
		normalize(f.paramsFrame);
		normalize(f.propsFrame);
		normalize(f.treeFrame);
		normalize(f.timeFrame);
		normalize(f.saveFrame);
		normalize(f.tableFrame);

		f.treeFrame.setSize(minWidth, h / 2);
		f.propsFrame.setSize(minWidth, h / 4);
		f.timeFrame.setSize(minWidth, Math.max(230, 2 * h / 5));
		f.saveFrame.setSize(minWidth, 3 * h / 4 - f.timeFrame.getHeight());
		f.paramsFrame.setSize(minWidth, h / 2);
		f.planFrame.setSize(w - 2 * minWidth, h / 4 + f.timeFrame.getHeight());
		f.tableFrame.setSize(w - 2 * minWidth, h - f.planFrame.getHeight());

		f.treeFrame.setLocation(0, 0);
		f.planFrame.setLocation(minWidth, 0);
		f.propsFrame.setLocation(w - minWidth, 0);
		f.timeFrame.setLocation(w - minWidth, h / 4);
		f.saveFrame.setLocation(w - minWidth, h / 4 + f.timeFrame.getHeight());
		f.paramsFrame.setLocation(0, f.treeFrame.getHeight());
		f.tableFrame.setLocation(minWidth, f.planFrame.getHeight());

	}
}