package com.syrus.AMFICOM.Client.Schedule;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Schedule.UI.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.io.*;

public class ScheduleMainFrame extends JFrame implements OperationListener {

	private ApplicationContext	aContext;

	private Dispatcher			dispatcher		= new Dispatcher();

	JPanel						mainPanel		= new JPanel();

	ScheduleMainToolBar			toolBar			= new ScheduleMainToolBar();
	JScrollPane					scrollPane		= new JScrollPane();
	JViewport					viewport		= new JViewport();
	JDesktopPane				desktopPane		= new JDesktopPane();

	JPanel						statusBarPanel	= new JPanel();

	StatusBarModel				statusBar		= new StatusBarModel(0);
	ScheduleMainMenuBar			menuBar			= new ScheduleMainMenuBar();

	PlanFrame					planFrame;
	TestParametersFrame			paramsFrame;
	TestRequestFrame			propsFrame;
	TimeParametersFrame			timeFrame;
	ElementsTreeFrame			treeFrame;
	SaveParametersFrame			saveFrame;
	TableFrame					tableFrame;

	public ScheduleMainFrame(ApplicationContext aContext) {
		super();
		setContext(aContext);

		this.addComponentListener(new ScheduleMainFrame_this_componentAdapter(
				this));
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				this_windowClosing();
			}
		});

		setContentPane(mainPanel);
		setResizable(true);
		setTitle(LangModelSchedule.getString("Scheduling_AMFICOM"));
		setJMenuBar(menuBar);

		mainPanel.setLayout(new BorderLayout());
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

		mainPanel.add(toolBar, BorderLayout.NORTH);
		mainPanel.add(statusBarPanel, BorderLayout.SOUTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		//		PlanLayeredPanel panel = new PlanLayeredPanel();
		planFrame = new PlanFrame(aContext);

		desktopPane.add(planFrame);

		paramsFrame = new TestParametersFrame(aContext);
		desktopPane.add(paramsFrame);

		propsFrame = new TestRequestFrame(aContext);
		desktopPane.add(propsFrame);

		timeFrame = new TimeParametersFrame(aContext);
		desktopPane.add(timeFrame);

		treeFrame = new ElementsTreeFrame(aContext);
		desktopPane.add(treeFrame);

		saveFrame = new SaveParametersFrame(aContext);
		desktopPane.add(saveFrame);

		tableFrame = new TableFrame(aContext);
		desktopPane.add(tableFrame);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(screenSize.width,
				screenSize.height - 24);

		setSize(frameSize);
		setLocation(0, 0);

		try {
			IniFile ini = new IniFile("schedule.ini");
			Pool.put("inifile", "schedule", ini);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Environment.addWindow(this);
	}

	public ScheduleMainFrame() {
		this(new ApplicationContext());
	}

	private void initModule() {
		ApplicationModel aModel = aContext.getApplicationModel();

		statusBar.distribute();
		//		statusBar.setWidth("status", 100);
		//		statusBar.setWidth("server", 250);
		//		statusBar.setWidth("session", 200);
		//		statusBar.setWidth("user", 100);
		//		statusBar.setWidth("time", 50);
		statusBar.setWidth("status", 300);
		statusBar.setWidth("server", 250);
		statusBar.setWidth("session", 200);
		statusBar.setWidth("user", 100);
		//statusBar.setWidth("domain", 150);
		statusBar.setWidth("time", 50);

		statusBar.setText("status", LangModel.String("statusReady"));
		statusBar.setText("server", LangModel.String("statusNoConnection"));
		statusBar.setText("session", LangModel.String("statusNoSession"));
		statusBar.setText("user", LangModel.String("statusNoUser"));
		statusBar.setText("domain", LangModel.String("statusNoDomain"));
		statusBar.setText("time", " ");
		statusBar.organize();

		aContext.setDispatcher(dispatcher);
		dispatcher.register(this, StatusMessageEvent.type);
		dispatcher.register(this, RefChangeEvent.typ);
		dispatcher.register(this, RefUpdateEvent.typ);

		dispatcher.register(this, "contextchange");
		Environment.the_dispatcher.register(this, "contextchange");

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

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

		if (ConnectionInterface.getActiveConnection() != null) {
			aContext.setConnectionInterface(ConnectionInterface
					.getActiveConnection());
			if (aContext.getConnectionInterface().isConnected())
					dispatcher.notify(new ContextChangeEvent(aContext
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
					dispatcher.notify(new ContextChangeEvent(aContext
							.getSessionInterface(),
							ContextChangeEvent.SESSION_OPENED_EVENT));
		} else {
			aContext.setSessionInterface(Environment
					.getDefaultSessionInterface(aContext
							.getConnectionInterface()));
			SessionInterface.setActiveSession(aContext.getSessionInterface());
		}
	}

	void setDefaultModel(ApplicationModel aModel) {
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled("menuSession", true);
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionConnection", true);
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		aContext.setDispatcher(dispatcher);
		if (aContext.getApplicationModel() == null)
				aContext.setApplicationModel(new ApplicationModel());
		setModel(aContext.getApplicationModel());
	}

	public ApplicationContext getContext() {
		return aContext;
	}

	public void setModel(ApplicationModel aModel) {
		toolBar.setModel(aModel);
		menuBar.setModel(aModel);
		aModel.addListener(menuBar);
		aModel.addListener(toolBar);
		aModel.fireModelChanged("");
	}

	public ApplicationModel getModel() {
		return aContext.getApplicationModel();
	}

	public Dispatcher getInternalDispatcher() {
		return dispatcher;
	}

	public void operationPerformed(OperationEvent ae) {
		String commandName = ae.getActionCommand();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:"
				+ commandName, getClass().getName());
		if (commandName.equals(StatusMessageEvent.type)) {
			StatusMessageEvent sme = (StatusMessageEvent) ae;
			statusBar.setText("status", sme.getText());
		} else if (commandName.equals("contextchange")) {
			ContextChangeEvent cce = (ContextChangeEvent) ae;
			System.out.println("perform context change \""
					+ Long.toHexString(cce.change_type) + "\" at "
					+ this.getTitle());
			// ApplicationModel aModel = aContext.getApplicationModel();
			if (cce.SESSION_OPENED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					aContext.setDataSourceInterface(aContext
							.getApplicationModel().getDataSource(
									aContext.getSessionInterface()));

					setSessionOpened();

					statusBar
							.setText("status", LangModel.String("statusReady"));
					statusBar.setText("session",
							ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
									aContext.getSessionInterface()
											.getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface()
							.getUser());
				}
			}
			if (cce.SESSION_CLOSED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					aContext.setDataSourceInterface(null);

					setSessionClosed();

					statusBar
							.setText("status", LangModel.String("statusReady"));
					statusBar.setText("session", LangModel
							.String("statusNoSession"));
					statusBar.setText("user", LangModel.String("statusNoUser"));
				}
			}
			if (cce.CONNECTION_OPENED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					setConnectionOpened();

					statusBar
							.setText("status", LangModel.String("statusReady"));
					statusBar.setText("server", aContext
							.getConnectionInterface().getServiceURL());
				}
			}
			if (cce.CONNECTION_CLOSED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					statusBar
							.setText("status", LangModel.String("statusError"));
					statusBar.setText("server", LangModel
							.String("statusConnectionError"));

					statusBar.setText("status", LangModel
							.String("statusDisconnected"));
					statusBar.setText("server", LangModel
							.String("statusNoConnection"));

					setConnectionClosed();

				}
			}
			if (cce.CONNECTION_FAILED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					statusBar
							.setText("status", LangModel.String("statusError"));
					statusBar.setText("server", LangModel
							.String("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if (cce.DOMAIN_SELECTED) {
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
	}

	public void setConnectionClosed() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", true);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setConnectionFailed() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionNew", false);
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.fireModelChanged("");
	}

	public void setDomainSelected() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.enable("menuSessionClose");
		aModel.enable("menuSessionOptions");
		aModel.enable("menuSessionChangePassword");

		aModel.fireModelChanged("");

		String domain_id = aContext.getSessionInterface().getDomainId();
		statusBar.setText("domain", Pool.getName("domain", domain_id));

		paramsFrame.setVisible(true);
		propsFrame.setVisible(true);
		treeFrame.setVisible(true);
		timeFrame.setVisible(true);
		planFrame.setVisible(true);
		saveFrame.setVisible(true);
		tableFrame.setVisible(true);
	}

	public void setSessionOpened() {
		//this.checker = new Checker(aContext.getDataSourceInterface());

		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		aContext.getDispatcher().notify(
				new StatusMessageEvent(LangModelSchedule
						.getString("Loading_BD")));
		//		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		//		new SurveyDataSourceImage(dataSource).LoadTestTypes();
		//		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		//		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		//		new SurveyDataSourceImage(dataSource).LoadModelingTypes();
		//		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();

		new ConfigDataSourceImage(dataSource).LoadISM();
		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		SurveyDataSourceImage sdsi = new SurveyDataSourceImage(dataSource);
		sdsi.LoadTestTypes();
		sdsi.LoadAnalysisTypes();
		sdsi.LoadEvaluationTypes();

		aContext.getDispatcher().notify(
				new StatusMessageEvent(LangModelSchedule
						.getString("Loding_BD_finished")));

		treeFrame.init();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.enable("menuSessionDomain");
		aModel.setEnabled("menuSessionNew", false);
		aModel.fireModelChanged("");
		String domain_id = aContext.getSessionInterface().getDomainId();
		if (domain_id != null && !domain_id.equals(""))
				dispatcher.notify(new ContextChangeEvent(domain_id,
						ContextChangeEvent.DOMAIN_SELECTED_EVENT));
	}

	public void setSessionClosed() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);
		aModel.setEnabled("menuSessionNew", true);

		aModel.fireModelChanged("");

		statusBar.setText("domain", LangModel.String("statusNoDomain"));
	}

	protected void componentShown() {
		initModule();
		desktopPane.setPreferredSize(desktopPane.getSize());
		new ScheduleWindowArranger(this).arrange();
	}

	protected void this_windowClosing() {
		dispatcher.unregister(this, "contextchange");
		Environment.the_dispatcher.unregister(this, "contextchange");
		aContext.getApplicationModel().getCommand("menuExit").execute();
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			((IniFile) Pool.get("inifile", "schedule")).saveKeys();
			//cManager.saveIni();
			dispatcher.unregister(this, "contextchange");
			Environment.the_dispatcher.unregister(this, "contextchange");
			aContext.getApplicationModel().getCommand("menuExit").execute();
			return;
		}
		super.processWindowEvent(e);
	}
}

class ScheduleWindowArranger extends WindowArranger {

	public ScheduleWindowArranger(ScheduleMainFrame mainframe) {
		super(mainframe);
	}

	public void arrange() {
		ScheduleMainFrame f = (ScheduleMainFrame) mainframe;

		int w = f.desktopPane.getSize().width;
		int h = f.desktopPane.getSize().height;

		int min_w = Math.max(250, w / 5);

		normalize(f.planFrame);
		normalize(f.paramsFrame);
		normalize(f.propsFrame);
		normalize(f.treeFrame);
		normalize(f.timeFrame);
		normalize(f.saveFrame);
		normalize(f.tableFrame);

		f.treeFrame.setSize(min_w, h / 2);
		f.propsFrame.setSize(min_w, h / 4);
		f.timeFrame.setSize(min_w, Math.max(330, h / 2));
		f.saveFrame.setSize(min_w, 3 * h / 4 - f.timeFrame.getHeight());
		f.paramsFrame.setSize(min_w, h / 2);
		f.planFrame.setSize(w - 2 * min_w, h / 4 + f.timeFrame.getHeight());
		f.tableFrame.setSize(w - 2 * min_w, h - f.planFrame.getHeight());

		f.treeFrame.setLocation(0, 0);
		f.planFrame.setLocation(min_w, 0);
		f.propsFrame.setLocation(w - min_w, 0);
		f.timeFrame.setLocation(w - min_w, h / 4);
		f.saveFrame.setLocation(w - min_w, h / 4 + f.timeFrame.getHeight());
		f.paramsFrame.setLocation(0, f.treeFrame.getHeight());
		f.tableFrame.setLocation(min_w, f.planFrame.getHeight());

	}
}

class ScheduleMainFrame_this_componentAdapter extends
		java.awt.event.ComponentAdapter {

	ScheduleMainFrame	adaptee;

	ScheduleMainFrame_this_componentAdapter(ScheduleMainFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void componentShown(ComponentEvent e) {
		adaptee.componentShown();
	}
}