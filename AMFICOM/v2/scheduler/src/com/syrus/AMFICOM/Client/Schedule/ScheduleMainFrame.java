package com.syrus.AMFICOM.Client.Schedule;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Command.Session.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Object.Domain;
import com.syrus.AMFICOM.Client.Resource.Result.Analysis;
import com.syrus.AMFICOM.Client.Resource.Result.Evaluation;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.AMFICOM.Client.Resource.Result.TestArgumentSet;
import com.syrus.AMFICOM.Client.Resource.Result.TestRequest;
import com.syrus.AMFICOM.Client.Schedule.UI.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.util.Log;

public class ScheduleMainFrame extends JFrame implements OperationListener {

	ApplicationContext			aContext;

	Dispatcher					dispatcher;

	public static final String	SCHEDULER_INI_FILE	= "schedule.ini";

	JPanel						mainPanel			= new JPanel();

	ScheduleMainToolBar			toolBar				= new ScheduleMainToolBar();
	JScrollPane					scrollPane			= new JScrollPane();
	JViewport					viewport			= new JViewport();
	JDesktopPane				desktopPane			= new JDesktopPane();

	JPanel						statusBarPanel		= new JPanel();

	StatusBarModel				statusBar			= new StatusBarModel(0);
	ScheduleMainMenuBar			menuBar				= new ScheduleMainMenuBar();

	PlanFrame					planFrame;
	TestParametersFrame			paramsFrame;
	TestRequestFrame			propsFrame;
	TimeParametersFrame			timeFrame;
	ElementsTreeFrame			treeFrame;
	SaveParametersFrame			saveFrame;
	TableFrame					tableFrame;
	//TestFilterFrame				testFilterFrame;

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
				ScheduleMainFrame.this.dispatcher.unregister(ScheduleMainFrame.this, "contextchange");
				Environment.the_dispatcher.unregister(ScheduleMainFrame.this, "contextchange");
				ScheduleMainFrame.this.aContext.getApplicationModel().getCommand("menuExit").execute();
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
		
//		this.testFilterFrame = new TestFilterFrame(aContext);
//		this.desktopPane.add(this.testFilterFrame);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = new Dimension(screenSize.width, screenSize.height - 24);

		setSize(frameSize);
		setLocation(0, 0);

		try {
			Properties properties = new Properties();
			properties.load(new FileInputStream(SCHEDULER_INI_FILE));
			Pool.put("inifile", "schedule", properties);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		Environment.addWindow(this);
		initModule();
		setContext(aContext);
	}

	public ScheduleMainFrame() {
		this(new ApplicationContext());
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

		this.dispatcher.register(this, "contextchange");
		Environment.the_dispatcher.register(this, "contextchange");

		aModel.setCommand("menuSessionNew", new SessionOpenCommand(Environment.the_dispatcher, this.aContext));
		aModel.setCommand("menuSessionClose", new SessionCloseCommand(Environment.the_dispatcher, this.aContext));
		aModel.setCommand("menuSessionOptions", new SessionOptionsCommand(this.aContext));
		aModel.setCommand("menuSessionConnection", new SessionConnectionCommand(Environment.the_dispatcher,
																				this.aContext));
		aModel.setCommand("menuSessionChangePassword", new SessionChangePasswordCommand(Environment.the_dispatcher,
																						this.aContext));
		aModel.setCommand("menuSessionDomain", new SessionDomainCommand(Environment.the_dispatcher, this.aContext));
		aModel.setCommand("menuExit", new ExitCommand(this));

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

		if (ConnectionInterface.getActiveConnection() != null) {
			this.aContext.setConnectionInterface(ConnectionInterface.getActiveConnection());
			if (this.aContext.getConnectionInterface().isConnected())
				this.dispatcher.notify(new ContextChangeEvent(this.aContext.getConnectionInterface(),
																ContextChangeEvent.CONNECTION_OPENED_EVENT));
		} else {
			aContext.setConnectionInterface(Environment.getDefaultConnectionInterface());
			ConnectionInterface.setActiveConnection(aContext.getConnectionInterface());
			//			new CheckConnectionCommand(internal_dispatcher,
			// aContext).execute();
		}
		if (SessionInterface.getActiveSession() != null) {
			aContext.setSessionInterface(SessionInterface.getActiveSession());
			aContext.setConnectionInterface(aContext.getSessionInterface().getConnectionInterface());
			if (aContext.getSessionInterface().isOpened())
				dispatcher.notify(new ContextChangeEvent(aContext.getSessionInterface(),
															ContextChangeEvent.SESSION_OPENED_EVENT));
		} else {
			aContext.setSessionInterface(Environment.getDefaultSessionInterface(aContext.getConnectionInterface()));
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
		Object obj = ae.getSource();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + commandName, getClass().getName());
		if (commandName.equals(SchedulerModel.COMMAND_CHANGE_STATUSBAR_STATE)) {
			boolean value = ((Boolean) obj).booleanValue();
			//Environment.log(Environment.LOG_LEVEL_INFO, "progressBar:" + value);
			this.statusBar.setProgressBarEnable(value);
		}
		if (commandName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
			StatusMessageEvent sme = (StatusMessageEvent) ae;
			statusBar.setText("status", sme.getText());
		} else if (commandName.equals("contextchange")) {
			ContextChangeEvent cce = (ContextChangeEvent) ae;
			System.out.println("perform context change \"" + Long.toHexString(cce.change_type) + "\" at "
					+ this.getTitle());
			// ApplicationModel aModel = aContext.getApplicationModel();
			if (cce.SESSION_OPENED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					aContext.setDataSourceInterface(aContext.getApplicationModel()
							.getDataSource(aContext.getSessionInterface()));

					setSessionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(aContext
							.getSessionInterface().getLogonTime())));
					statusBar.setText("user", aContext.getSessionInterface().getUser());
				}
			}
			if (cce.SESSION_CLOSED) {
				SessionInterface ssi = (SessionInterface) cce.getSource();
				if (aContext.getSessionInterface().equals(ssi)) {
					aContext.setDataSourceInterface(null);

					setSessionClosed();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("session", LangModel.getString("statusNoSession"));
					statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if (cce.CONNECTION_OPENED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					setConnectionOpened();

					statusBar.setText("status", LangModel.getString("statusReady"));
					statusBar.setText("server", aContext.getConnectionInterface().getServiceURL());
				}
			}
			if (cce.CONNECTION_CLOSED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

					statusBar.setText("status", LangModel.getString("statusDisconnected"));
					statusBar.setText("server", LangModel.getString("statusNoConnection"));

					setConnectionClosed();

				}
			}
			if (cce.CONNECTION_FAILED) {
				ConnectionInterface cci = (ConnectionInterface) cce.getSource();
				if (aContext.getConnectionInterface().equals(cci)) {
					statusBar.setText("status", LangModel.getString("statusError"));
					statusBar.setText("server", LangModel.getString("statusConnectionError"));

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
		DataSourceInterface dataSource = aContext.getDataSourceInterface();
		new ConfigDataSourceImage(dataSource).LoadISM();

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", true);
		aModel.setEnabled("menuSessionOptions", true);
		aModel.setEnabled("menuSessionChangePassword", true);

		aModel.fireModelChanged("");

		String domainId = aContext.getSessionInterface().getDomainId();
		statusBar.setText("domain", Pool.getName(Domain.typ, domainId));

		treeFrame.init();
		Hashtable unsavedTestArgumentSet = Pool.getChangedHash(TestArgumentSet.typ);
		Hashtable unsavedAnalysis = Pool.getChangedHash(Analysis.typ);
		Hashtable unsavedEvaluation = Pool.getChangedHash(Evaluation.typ);
		Hashtable unsavedTestRequest = Pool.getChangedHash(TestRequest.typ);
		Hashtable unsavedTest = Pool.getChangedHash(Test.TYPE);

		for (int i = 0; i < 5; i++) {
			Hashtable table;
			switch (i) {
				case 0:
					table = unsavedTestArgumentSet;
					break;
				case 1:
					table = unsavedAnalysis;
					break;
				case 2:
					table = unsavedEvaluation;
					break;
				case 3:
					table = unsavedTestRequest;
					break;
				case 4:
					table = unsavedTest;
					break;
				default:
					table = null;
					break;
			}
			if (table != null) {
				Set keys = table.keySet();
				for (Iterator it = keys.iterator(); it.hasNext();) {
					String key = (String) it.next();
					ObjectResource obj = (ObjectResource) table.get(key);
					obj.setChanged(false);
				}
			}
		}

		dispatcher.notify(new OperationEvent(this, 0, SchedulerModel.COMMAND_CLEAN));

		paramsFrame.setVisible(true);
		propsFrame.setVisible(true);
		treeFrame.setVisible(true);
		timeFrame.setVisible(true);
		planFrame.setVisible(true);
		saveFrame.setVisible(true);
		tableFrame.setVisible(true);
//		testFilterFrame.setVisible(true);
	}

	public void setSessionOpened() {
		//this.checker = new Checker(aContext.getDataSourceInterface());

		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		aContext.getDispatcher().notify(
										new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
												.getString("Loading_BD")));
		//		new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		//		new SurveyDataSourceImage(dataSource).LoadTestTypes();
		//		new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		//		new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		//		new SurveyDataSourceImage(dataSource).LoadModelingTypes();
		//		new SchemeDataSourceImage(dataSource).LoadAttributeTypes();

		new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		SurveyDataSourceImage sdsi = new SurveyDataSourceImage(dataSource);
		sdsi.LoadTestTypes();
		sdsi.LoadAnalysisTypes();
		sdsi.LoadEvaluationTypes();

		aContext.getDispatcher().notify(
										new StatusMessageEvent(StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
												.getString("Loding_BD_finished")));

		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionDomain", true);
		aModel.setEnabled("menuSessionNew", false);
		aModel.fireModelChanged("");
		String domainId = aContext.getSessionInterface().getDomainId();
		if (domainId != null && !domainId.equals(""))
			dispatcher.notify(new ContextChangeEvent(domainId, ContextChangeEvent.DOMAIN_SELECTED_EVENT));
	}

	public void setSessionClosed() {
		ApplicationModel aModel = aContext.getApplicationModel();
		aModel.setEnabled("menuSessionClose", false);
		aModel.setEnabled("menuSessionOptions", false);
		aModel.setEnabled("menuSessionChangePassword", false);
		aModel.setEnabled("menuSessionDomain", false);
		aModel.setEnabled("menuSessionNew", true);

		aModel.fireModelChanged("");

		statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			try {
				((Properties) Pool.get("inifile", "schedule")).store(new FileOutputStream(SCHEDULER_INI_FILE), null);
			} catch (FileNotFoundException fnfe) {
				Log.errorMessage("FileNotFoundException while closing main frame:" + fnfe.getMessage());
			} catch (IOException ioe) {
				Log.errorMessage("IOException while closing main frame:" + ioe.getMessage());
			}

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