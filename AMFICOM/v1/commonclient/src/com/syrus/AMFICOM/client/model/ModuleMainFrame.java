/*-
 * $Id: ModuleMainFrame.java,v 1.1 2005/05/27 16:16:11 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.StatusBarModel;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;

/**
 * @version $Revision: 1.1 $, $Date: 2005/05/27 16:16:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class ModuleMainFrame extends JFrame implements PropertyChangeListener {

	protected ApplicationContext		aContext;
	protected JDesktopPane				desktopPane;

	protected Dispatcher				dispatcher;

	protected JPanel					mainPanel;

	protected JScrollPane				scrollPane;
	protected StatusBarModel			statusBar;
	protected JPanel					statusBarPanel;
	protected JViewport					viewport;
	protected WindowArranger			windowArranger;
	protected final AbstractMainMenuBar	menuBar;
	private final AbstractMainToolBar	toolBar;

	public ModuleMainFrame(final ApplicationContext aContext,
			final String applicationTitle,
			final AbstractMainMenuBar menuBar,
			final AbstractMainToolBar toolBar) {
		this.aContext = aContext;
		this.menuBar = menuBar;
		this.toolBar = toolBar;
		this.dispatcher = aContext.getDispatcher();

		this.mainPanel = new JPanel(new BorderLayout());

		this.setContentPane(this.mainPanel);
		this.setResizable(true);
		this.setTitle(applicationTitle);
		this.setJMenuBar(menuBar);

		this.desktopPane = new JDesktopPane();
		this.desktopPane.setBackground(SystemColor.control.darker().darker());

		this.statusBarPanel = new JPanel(new BorderLayout());
		this.statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		this.statusBar = new StatusBarModel(0);
		this.statusBarPanel.add(this.statusBar, BorderLayout.CENTER);

		this.statusBar.add("status");
		this.statusBar.add("server");
		this.statusBar.add("session");
		this.statusBar.add("user");
		this.statusBar.add("domain");
		this.statusBar.add("time");

		this.viewport = new JViewport();
		this.viewport.setView(this.desktopPane);

		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.mainPanel.add(this.toolBar, BorderLayout.NORTH);
		this.mainPanel.add(this.statusBarPanel, BorderLayout.SOUTH);
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);

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
		if (propertyName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
			StatusMessageEvent sme = (StatusMessageEvent) evt;
			this.statusBar.setText("status", sme.getText());
		} else if (propertyName.equals(ContextChangeEvent.TYPE)) {
			ContextChangeEvent cce = (ContextChangeEvent) evt;
			if (cce.isSessionOpened()) {
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
			if (cce.isSessionClosed()) {
				setSessionClosed();

				this.statusBar.setText("status", LangModel.getString("statusReady"));
				this.statusBar.setText("session", LangModel.getString("statusNoSession"));
				this.statusBar.setText("user", LangModel.getString("statusNoUser"));
			}
			if (cce.isConnectionOpened()) {
				setConnectionOpened();

				this.statusBar.setText("status", LangModel.getString("statusReady"));
				// this.statusBar.setText("server",
				// ConnectionInterface.getInstance().getServerName());
			}
			if (cce.isConnectionClosed()) {
				this.statusBar.setText("status", LangModel.getString("statusError"));
				this.statusBar.setText("server", LangModel.getString("statusConnectionError"));

				this.statusBar.setText("status", LangModel.getString("statusDisconnected"));
				this.statusBar.setText("server", LangModel.getString("statusNoConnection"));

				setConnectionClosed();
			}
			if (cce.isConnectionFailed()) {
				this.statusBar.setText("status", LangModel.getString("statusError"));
				this.statusBar.setText("server", LangModel.getString("statusConnectionError"));

				setConnectionFailed();
			}
			if (cce.isDomainSelected()) {
				setDomainSelected();
			}
		}

	}

	public void setConnectionClosed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_NEW, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_VIEW_ARRANGE, false);
		aModel.fireModelChanged("");
	}

	public void setConnectionFailed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_NEW, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.fireModelChanged("");
	}

	public void setConnectionOpened() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_NEW, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CONNECTION, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_VIEW_ARRANGE, true);
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
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CLOSE, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_OPTIONS, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, true);

		aModel.fireModelChanged("");

		try {
			Domain domain = (Domain) StorableObjectPool.getStorableObject(LoginManager.getDomainId(), true);
			this.statusBar.setText("domain", domain.getName());
		} catch (ApplicationException e) {
			showErrorMessage(this, e);
		}

		this.windowArranger.arrange();

	}

	public void setModel(ApplicationModel aModel) {
		// TODO
		this.toolBar.setModel(aModel);
		this.menuBar.setModel(aModel);
		aModel.addListener(this.menuBar.getApplicationModelListener());
		aModel.addListener(this.toolBar);
		aModel.fireModelChanged("");
	}

	public void setSessionClosed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_DOMAIN, false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_NEW, true);

		aModel.fireModelChanged("");

		this.statusBar.setText("domain", LangModel.getString("statusNoDomain"));
	}

	public void setSessionOpened() {
		// this.checker = new
		// Checker(aContext.getDataSourceInterface());

//		Dispatcher dispatcher = this.aContext.getDispatcher();
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModel
				.getString("Loading_DB")));
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModel
				.getString("Loding_DB_finished")));

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_DOMAIN, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_NEW, false);
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
			this.aContext.getApplicationModel().getCommand(AbstractMainMenuBar.MENU_EXIT).execute();
			return;
		}
		super.processWindowEvent(e);
	}

	public static void showErrorMessage(Component component,
										Exception exception) {
		exception.printStackTrace();
		JOptionPane.showMessageDialog(component, exception.getMessage(), LangModel.getString("Error"),
			JOptionPane.OK_OPTION);
	}

	protected void initModule() {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		this.statusBar.distribute();
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
		this.dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
		// this.dispatcher.addPropertyChangeListener(COMMAND_CHANGE_STATUSBAR_STATE,
		// this);

		this.dispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		Dispatcher dispatcher1 = Environment.getDispatcher();
		dispatcher1.addPropertyChangeListener(ContextChangeEvent.TYPE, this);

		aModel.setCommand(AbstractMainMenuBar.MENU_SESSION_NEW, new OpenSessionCommand(dispatcher1));
		// TODO FIXXX
		aModel.setCommand(AbstractMainMenuBar.MENU_SESSION_CLOSE, new SessionCloseCommand(dispatcher1));
		aModel.setCommand(AbstractMainMenuBar.MENU_SESSION_OPTIONS, new SessionOptionsCommand(this.aContext));
		aModel.setCommand(AbstractMainMenuBar.MENU_SESSION_CONNECTION, new SessionConnectionCommand(dispatcher1,
																									this.aContext));
		aModel.setCommand(AbstractMainMenuBar.MENU_SESSION_CHANGE_PASSWORD,
			new SessionChangePasswordCommand(dispatcher1, this.aContext));
		aModel
				.setCommand(AbstractMainMenuBar.MENU_SESSION_DOMAIN, new SessionDomainCommand(dispatcher1,
																								this.aContext));
		aModel.setCommand(AbstractMainMenuBar.MENU_EXIT, new ExitCommand(this));

		aModel.setCommand(AbstractMainMenuBar.MENU_VIEW_ARRANGE, new ArrangeWindowCommand(this.windowArranger));
		/**
		 * TODO remove when all ok
		 */
		// CreateScheduleReportCommand csrCommand = new
		// CreateScheduleReportCommand(aContext);
		// csrCommand.setParameter(this);
		// aModel.setCommand(ScheduleMainMenuBar.MENU_TEMPLATE_REPORT,
		// csrCommand);
		aModel.setCommand(AbstractMainMenuBar.MENU_HELP_ABOUT, new HelpAboutCommand(this));

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

	protected void setDefaultModel(ApplicationModel aModel) {
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_NEW, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_SESSION_CONNECTION, true);
		aModel.setEnabled(AbstractMainMenuBar.MENU_VIEW_ARRANGE, false);
	}

	public void setWindowArranger(WindowArranger windowArranger) {
		this.windowArranger = windowArranger;
	}
}
