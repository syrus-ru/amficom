
package com.syrus.AMFICOM.Client.Schedule;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIDefaults;

import com.syrus.AMFICOM.Client.General.lang.LangModelSchedule;
import com.syrus.AMFICOM.Client.Schedule.UI.ElementsTreeFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.PlanFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.SaveParametersFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TableFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TestParametersFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TestRequestFrame;
import com.syrus.AMFICOM.Client.Schedule.UI.TimeParametersFrame;
import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.StatusBarModel;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.ApplicationModel;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.model.ExitCommand;
import com.syrus.AMFICOM.client.model.HelpAboutCommand;
import com.syrus.AMFICOM.client.model.LazyCommand;
import com.syrus.AMFICOM.client.model.OpenSessionCommand;
import com.syrus.AMFICOM.client.model.SessionChangePasswordCommand;
import com.syrus.AMFICOM.client.model.SessionCloseCommand;
import com.syrus.AMFICOM.client.model.SessionConnectionCommand;
import com.syrus.AMFICOM.client.model.SessionDomainCommand;
import com.syrus.AMFICOM.client.model.SessionOptionsCommand;
import com.syrus.AMFICOM.client.model.ShowWindowCommand;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;

public class ScheduleMainFrame extends JFrame implements PropertyChangeListener {

	private static final long	serialVersionUID		= 3257563988626848055L;

	ApplicationContext			aContext;
	JDesktopPane				desktopPane				= new JDesktopPane();

	Dispatcher					dispatcher;

	JPanel						mainPanel				= new JPanel();
	ScheduleMainMenuBar			menuBar					= new ScheduleMainMenuBar();

	public static final String	PARAMETERS_FRAME		= "parametersFrame";
	public static final String	PLAN_FRAME				= "planFrame";
	public static final String	PROPERTIES_FRAME		= "propertiesFrame";
	public static final String	SAVE_PARAMETERS_FRAME	= "saveParametersFrame";
	public static final String	TABLE_FRAME				= "tableFrame";
	public static final String	TIME_PARAMETERS_FRAME	= "timeParametersFrame";
	public static final String	TREE_FRAME				= "treeFrame";

	UIDefaults					frames;

	JScrollPane					scrollPane				= new JScrollPane();
	StatusBarModel				statusBar				= new StatusBarModel(0);
	JPanel						statusBarPanel			= new JPanel();
	ScheduleMainToolBar			toolBar					= new ScheduleMainToolBar();
	JViewport					viewport				= new JViewport();
	WindowArranger				scheduleWindowArranger;

	public ScheduleMainFrame() {
		this(new ApplicationContext());
	}

	public ScheduleMainFrame(final ApplicationContext aContext) {
		this.aContext = aContext;
		this.dispatcher = aContext.getDispatcher();

		this.addComponentListener(new ComponentAdapter() {

			public void componentShown(ComponentEvent e) {
				ScheduleMainFrame.this.desktopPane.setPreferredSize(ScheduleMainFrame.this.desktopPane.getSize());
			}
		});
		this.addWindowListener(new java.awt.event.WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				ScheduleMainFrame.this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE,
					ScheduleMainFrame.this);
				Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE,
					ScheduleMainFrame.this);
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

		// PlanLayeredPanel panel = new PlanLayeredPanel();
		this.frames = new UIDefaults();
		this.frames.put(PLAN_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				PlanFrame frame = new PlanFrame(aContext);
				ScheduleMainFrame.this.desktopPane.add(frame);
				return frame;
			}
		});

		this.frames.put(PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TestParametersFrame frame = new TestParametersFrame(aContext);
				ScheduleMainFrame.this.desktopPane.add(frame);
				return frame;
			}
		});

		this.frames.put(PROPERTIES_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TestRequestFrame frame = new TestRequestFrame(aContext);
				ScheduleMainFrame.this.desktopPane.add(frame);
				return frame;
			}
		});

		this.frames.put(TIME_PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TimeParametersFrame frame = new TimeParametersFrame(aContext);
				ScheduleMainFrame.this.desktopPane.add(frame);
				return frame;
			}
		});

		this.frames.put(TREE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				ElementsTreeFrame frame = new ElementsTreeFrame(aContext);
				ScheduleMainFrame.this.desktopPane.add(frame);
				return frame;
			}
		});

		this.frames.put(SAVE_PARAMETERS_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				SaveParametersFrame frame = new SaveParametersFrame(aContext);
				ScheduleMainFrame.this.desktopPane.add(frame);
				return frame;
			}
		});

		this.frames.put(TABLE_FRAME, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults table) {
				TableFrame frame = new TableFrame(aContext);
				ScheduleMainFrame.this.desktopPane.add(frame);
				return frame;
			}
		});

		// this.testFilterFrame = new TestFilterFrame(aContext);
		// this.desktopPane.add(this.testFilterFrame);

		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = localGraphicsEnvironment.getMaximumWindowBounds();
		this.setSize(new Dimension(maximumWindowBounds.width, maximumWindowBounds.height));
		this.setLocation(maximumWindowBounds.x, maximumWindowBounds.y);

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

	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		Object obj = evt.getNewValue();
		Environment.log(Environment.LOG_LEVEL_INFO, "commandName:" + propertyName, getClass().getName());
		if (propertyName.equals(SchedulerModel.COMMAND_CHANGE_STATUSBAR_STATE)) {
			boolean value = ((Boolean) obj).booleanValue();
			// Environment.log(Environment.LOG_LEVEL_INFO,
			// "progressBar:" +
			// value);
			this.statusBar.setProgressBarEnable(value);
		}
		if (propertyName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
			StatusMessageEvent sme = (StatusMessageEvent) evt;
			this.statusBar.setText("status", sme.getText());
		} else if (propertyName.equals(ContextChangeEvent.TYPE)) {
			ContextChangeEvent cce = (ContextChangeEvent) evt;
			// System.out.println("perform context change \"" +
			// Long.toHexString(cce.change_type) + "\" at "
			// + this.getTitle());
			// ApplicationModel aModel =
			// aContext.getApplicationModel();
			if (cce.isSessionOpened()) {
				// SessionInterface ssi = (SessionInterface) cce.getSource();
				// if (this.aContext.getSessionInterface().equals(ssi))
				{
					setSessionOpened();

					this.statusBar.setText("status", LangModel.getString("statusReady"));
					// SimpleDateFormat sdf = (SimpleDateFormat)
					// UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
					// this.statusBar.setText("session", sdf.format(new
					// Date(this.aContext.getSessionInterface()
					// .getLogonTime())));
					// this.statusBar.setText("user",
					// this.aContext.getSessionInterface().getUser());
				}
			}
			if (cce.isSessionClosed()) {
				// SessionInterface ssi = (SessionInterface) cce.getSource();
				// if (this.aContext.getSessionInterface().equals(ssi))
				{

					setSessionClosed();

					this.statusBar.setText("status", LangModel.getString("statusReady"));
					this.statusBar.setText("session", LangModel.getString("statusNoSession"));
					this.statusBar.setText("user", LangModel.getString("statusNoUser"));
				}
			}
			if (cce.isConnectionOpened()) {
				// ConnectionInterface cci = (ConnectionInterface)
				// cce.getSource();
				// if (ConnectionInterface.getInstance().equals(cci))
				{
					setConnectionOpened();

					this.statusBar.setText("status", LangModel.getString("statusReady"));
					// this.statusBar.setText("server",
					// ConnectionInterface.getInstance().getServerName());
				}
			}
			if (cce.isConnectionClosed()) {
				// ConnectionInterface cci = (ConnectionInterface)
				// cce.getSource();
				// if (this.aContext.getConnectionInterface().equals(cci))
				{
					this.statusBar.setText("status", LangModel.getString("statusError"));
					this.statusBar.setText("server", LangModel.getString("statusConnectionError"));

					this.statusBar.setText("status", LangModel.getString("statusDisconnected"));
					this.statusBar.setText("server", LangModel.getString("statusNoConnection"));

					setConnectionClosed();

				}
			}
			if (cce.isConnectionFailed()) {
				// ConnectionInterface cci = (ConnectionInterface)
				// cce.getSource();
				// if (this.aContext.getConnectionInterface().equals(cci))
				{
					this.statusBar.setText("status", LangModel.getString("statusError"));
					this.statusBar.setText("server", LangModel.getString("statusConnectionError"));

					setConnectionFailed();
				}
			}
			if (cce.isDomainSelected()) {
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
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_ARRANGE, false);
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
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_ARRANGE, true);
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
		// DataSourceInterface dataSource = this.aContext.getDataSource();
		// new ConfigDataSourceImage(dataSource).LoadISM();

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CLOSE, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_OPTIONS, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, true);

		aModel.fireModelChanged("");

		try {
			Domain domain = (Domain) StorableObjectPool.getStorableObject(LoginManager.getDomainId(), true);
			this.statusBar.setText("domain", domain.getName());
		} catch (ApplicationException e) {
			SchedulerModel.showErrorMessage(this, e);
		}

		this.scheduleWindowArranger.arrange();
		ElementsTreeFrame treeFrame = (ElementsTreeFrame) this.frames.get(TREE_FRAME);
		treeFrame.init();

		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.TEST_ENTITY_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.MEASUREMENT_SETUP_ENTITY_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.SET_ENTITY_CODE);
		StorableObjectPool.cleanChangedStorableObjects(ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE);

		this.dispatcher.firePropertyChange(new PropertyChangeEvent(this, SchedulerModel.COMMAND_CLEAN, null, null));

		((Component) this.frames.get(PARAMETERS_FRAME)).setVisible(true);
		((Component) this.frames.get(PROPERTIES_FRAME)).setVisible(true);
		treeFrame.setVisible(true);
		((Component) this.frames.get(TIME_PARAMETERS_FRAME)).setVisible(true);
		((Component) this.frames.get(PLAN_FRAME)).setVisible(true);
		((Component) this.frames.get(SAVE_PARAMETERS_FRAME)).setVisible(true);
		((Component) this.frames.get(TABLE_FRAME)).setVisible(true);
		// testFilterFrame.setVisible(true);
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
		// this.checker = new
		// Checker(aContext.getDataSourceInterface());

		Dispatcher dispatcher = this.aContext.getDispatcher();
		dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Loading_DB")));
		// new SurveyDataSourceImage(dataSource).LoadParameterTypes();
		// new SurveyDataSourceImage(dataSource).LoadTestTypes();
		// new SurveyDataSourceImage(dataSource).LoadAnalysisTypes();
		// new SurveyDataSourceImage(dataSource).LoadEvaluationTypes();
		// new SurveyDataSourceImage(dataSource).LoadModelingTypes();
		// new SchemeDataSourceImage(dataSource).LoadAttributeTypes();

		// new SchemeDataSourceImage(dataSource).LoadISMDirectory();
		// SurveyDataSourceImage sdsi = new SurveyDataSourceImage(dataSource);
		// sdsi.LoadTestTypes();
		// sdsi.LoadAnalysisTypes();
		// sdsi.LoadEvaluationTypes();

		dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModelSchedule
				.getString("Loding_DB_finished")));

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_DOMAIN, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW, false);
		aModel.fireModelChanged("");
		Identifier domainId = LoginManager.getDomainId();
		if (domainId != null && !domainId.isVoid()) {
			this.dispatcher.firePropertyChange(new ContextChangeEvent(domainId,
																		ContextChangeEvent.DOMAIN_SELECTED_EVENT));
		}
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_ACTIVATED) {
			Environment.setActiveWindow(this);
		}
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			// cManager.saveIni();
			this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			this.aContext.getApplicationModel().getCommand(ScheduleMainMenuBar.MENU_EXIT).execute();
			return;
		}
		super.processWindowEvent(e);
	}

	void initModule() {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		this.statusBar.distribute();
		// statusBar.setWidth("status", 100);
		// statusBar.setWidth("server", 250);
		// statusBar.setWidth("session", 200);
		// statusBar.setWidth("user", 100);
		// statusBar.setWidth("time", 50);
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

		if (this.scheduleWindowArranger == null) {
			this.scheduleWindowArranger = new WindowArranger(this) {

				public void arrange() {
					ScheduleMainFrame f = (ScheduleMainFrame) this.mainframe;

					int w = f.desktopPane.getSize().width;
					int h = f.desktopPane.getSize().height;

					// int minWidth = w / 5;

					JInternalFrame paramsFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.PARAMETERS_FRAME));
					JInternalFrame propsFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.PROPERTIES_FRAME));
					JInternalFrame treeFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.TREE_FRAME));
					JInternalFrame timeFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.TIME_PARAMETERS_FRAME));
					JInternalFrame planFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.PLAN_FRAME));
					JInternalFrame saveFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.SAVE_PARAMETERS_FRAME));
					JInternalFrame tableFrame = ((JInternalFrame) f.frames.get(ScheduleMainFrame.TABLE_FRAME));

					normalize(planFrame);
					normalize(paramsFrame);
					normalize(propsFrame);
					normalize(treeFrame);
					normalize(timeFrame);
					normalize(saveFrame);
					normalize(tableFrame);

					treeFrame.setSize(w / 5, h);

					propsFrame.pack();
					propsFrame.setSize(w / 5, propsFrame.getHeight());
					saveFrame.pack();
					saveFrame.setSize(w / 5, saveFrame.getHeight());
					tableFrame.setSize(w - propsFrame.getWidth() - treeFrame.getWidth(), 2 * h / 5);
					timeFrame.setSize(w / 5, h / 3);
					planFrame.setSize(w - propsFrame.getWidth() - treeFrame.getWidth(), h - tableFrame.getHeight());

					treeFrame.setLocation(0, 0);
					timeFrame.setLocation(w - timeFrame.getWidth(), h - timeFrame.getHeight());
					saveFrame.setLocation(w - saveFrame.getWidth(), h - timeFrame.getHeight() - saveFrame.getHeight());
					propsFrame.setLocation(w - propsFrame.getWidth(), 0);
					paramsFrame.setSize(w / 5, saveFrame.getY() - (propsFrame.getY() + propsFrame.getHeight()));
					paramsFrame.setLocation(w - paramsFrame.getWidth(), propsFrame.getY() + propsFrame.getHeight());
					tableFrame.setLocation(treeFrame.getX() + treeFrame.getWidth(), planFrame.getHeight());

					planFrame.setLocation(treeFrame.getX() + treeFrame.getWidth(), 0);

				}
			};
		}

		this.aContext.setDispatcher(this.dispatcher);
		this.dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
		// this.dispatcher.register(this, RefChangeEvent.typ);
		// this.dispatcher.register(this, RefUpdateEvent.typ);
		this.dispatcher.addPropertyChangeListener(SchedulerModel.COMMAND_CHANGE_STATUSBAR_STATE, this);

		this.dispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		Dispatcher dispatcher1 = Environment.getDispatcher();
		dispatcher1.addPropertyChangeListener(ContextChangeEvent.TYPE, this);

		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_NEW, new OpenSessionCommand(dispatcher1));
		// TODO FIXXX
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_CLOSE, new SessionCloseCommand(dispatcher1));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_OPTIONS, new SessionOptionsCommand(this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_CONNECTION, new SessionConnectionCommand(dispatcher1,
																									this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_SESSION_CHANGE_PASSWORD,
			new SessionChangePasswordCommand(dispatcher1, this.aContext));
		aModel
				.setCommand(ScheduleMainMenuBar.MENU_SESSION_DOMAIN, new SessionDomainCommand(dispatcher1,
																								this.aContext));
		aModel.setCommand(ScheduleMainMenuBar.MENU_EXIT, new ExitCommand(this));

		/* TODO FIXXX */
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PLAN, this.getLazyCommand(PLAN_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TREE, this.getLazyCommand(TREE_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PARAMETERS, this.getLazyCommand(PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_SAVE_PARAMETERS, this.getLazyCommand(SAVE_PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_PROPERTIES, this.getLazyCommand(PROPERTIES_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TIME, this.getLazyCommand(TIME_PARAMETERS_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_TABLE, this.getLazyCommand(TABLE_FRAME));
		aModel.setCommand(ScheduleMainMenuBar.MENU_VIEW_ARRANGE, new ArrangeWindowCommand(this.scheduleWindowArranger));
		/**
		 * TODO remove when all ok
		 */
		// CreateScheduleReportCommand csrCommand = new
		// CreateScheduleReportCommand(aContext);
		// csrCommand.setParameter(this);
		// aModel.setCommand(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT,
		// csrCommand);
		aModel.setCommand(ScheduleMainMenuBar.MENU_HELP_ABOUT, new HelpAboutCommand(this));

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

		// ConnectionInterface connectionInterface =
		// ConnectionInterface.getInstance();
		// if (connectionInterface != null) {
		// if (connectionInterface.isConnected())
		// this.dispatcher.notify(new ContextChangeEvent(connectionInterface,
		// ContextChangeEvent.CONNECTION_OPENED_EVENT));
		// }
		// if (SessionInterface.getActiveSession() != null) {
		// this.aContext.setSessionInterface(SessionInterface.getActiveSession());
		// if (this.aContext.getSessionInterface().isOpened())
		// this.dispatcher.notify(new
		// ContextChangeEvent(this.aContext.getSessionInterface(),
		// ContextChangeEvent.SESSION_OPENED_EVENT));
		// } else {
		// this.aContext.setSessionInterface(Environment.getDefaultSessionInterface(connectionInterface));
		// SessionInterface.setActiveSession(this.aContext.getSessionInterface());
		// }
	}

	private Command getLazyCommand(final Object windowKey) {
		// return new LazyCommand()
		final String commandKey = windowKey.toString() + "_COMMAND";
		this.frames.put(commandKey, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults defaults) {
				return new ShowWindowCommand(defaults.get(windowKey));
			}
		});
		return new LazyCommand(this.frames, commandKey);
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
		aModel.setEnabled(ScheduleMainMenuBar.MENU_VIEW_ARRANGE, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_REPORT, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT, false);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_HELP, true);
		aModel.setEnabled(ScheduleMainMenuBar.MENU_HELP_ABOUT, true);

	}
}
