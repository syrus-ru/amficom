/*-
 * $Id: AbstractMainFrame.java,v 1.5 2005/06/10 07:39:39 bob Exp $
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
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.StatusBar;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/10 07:39:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainFrame extends JFrame implements PropertyChangeListener {

	protected ApplicationContext		aContext;
	protected JDesktopPane				desktopPane;

	protected Dispatcher				dispatcher;

	protected JPanel					mainPanel;

	protected JScrollPane				scrollPane;
	protected StatusBar					statusBar;
	protected JPanel					statusBarPanel;
	protected JViewport					viewport;
	protected WindowArranger			windowArranger;
	protected final AbstractMainMenuBar	menuBar;
	
	private final AbstractMainToolBar	toolBar;

	public AbstractMainFrame(final ApplicationContext aContext,
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
		this.statusBar = new StatusBar();
		this.statusBar.addDispatcher(this.dispatcher);
		this.statusBarPanel.add(this.statusBar.getPanel(), BorderLayout.CENTER);

		this.statusBar.add(StatusBar.FIELD_STATUS);
		this.statusBar.add(StatusBar.FIELD_SERVER);
		this.statusBar.add(StatusBar.FIELD_SESSION);
		this.statusBar.add(StatusBar.FIELD_USER);
		this.statusBar.add(StatusBar.FIELD_DOMAIN);
		this.statusBar.add(StatusBar.FIELD_TIME);

		this.viewport = new JViewport();
		this.viewport.setView(this.desktopPane);

		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewport(this.viewport);
		this.scrollPane.setAutoscrolls(true);

		this.mainPanel.add(this.toolBar, BorderLayout.NORTH);
		this.mainPanel.add(this.statusBarPanel, BorderLayout.SOUTH);
		this.mainPanel.add(this.scrollPane, BorderLayout.CENTER);

		GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle maximumWindowBounds = localGraphicsEnvironment.getMaximumWindowBounds();
		this.setSize(new Dimension(maximumWindowBounds.width, maximumWindowBounds.height));
		this.setLocation(maximumWindowBounds.x, maximumWindowBounds.y);

		Environment.addWindow(this);
		this.initModule();
		this.setContext(aContext);
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
		Log.debugMessage("AbstractMainFrame.propertyChange | propertyName " + propertyName, Log.FINEST);
//		if (propertyName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
//			StatusMessageEvent sme = (StatusMessageEvent) evt;
//			this.statusBar.setText(StatusBar.FIELD_STATUS, sme.getText());
//		} else 
		if (propertyName.equals(ContextChangeEvent.TYPE)) {
			Log.debugMessage("AbstractMainFrame.propertyChange | 1 ", Log.FINEST);
			ContextChangeEvent cce = (ContextChangeEvent) evt;
			if (cce.isSessionOpened()) {
				Log.debugMessage("AbstractMainFrame.propertyChange | cce.isSessionOpened() ", Log.FINEST);
				this.setSessionOpened();

				this.statusBar.setText(StatusBar.FIELD_STATUS, LangModel.getString("statusReady"));
				// SimpleDateFormat sdf = (SimpleDateFormat)
				// UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);
				// TODO
				// this.statusBar.setText("session", sdf.format(new
				// Date(this.aContext.getSessionInterface()
				// .getLogonTime())));
				{
					try {
						User user = (User) StorableObjectPool.getStorableObject(LoginManager.getUserId(), true);
						this.statusBar.setText("user", user.getName());
					} catch (ApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (cce.isSessionClosed()) {
				Log.debugMessage("AbstractMainFrame.propertyChange | cce.isSessionClosed() ", Log.FINEST);
				this.setSessionClosed();

				this.statusBar.setText(StatusBar.FIELD_STATUS, LangModel.getString("statusReady"));
				this.statusBar.setText(StatusBar.FIELD_SESSION, LangModel.getString("statusNoSession"));
				this.statusBar.setText(StatusBar.FIELD_USER, LangModel.getString("statusNoUser"));
			}
			if (cce.isConnectionOpened()) {
				Log.debugMessage("AbstractMainFrame.propertyChange | cce.isConnectionOpened() ", Log.FINEST);
				this.setConnectionOpened();

				this.statusBar.setText(StatusBar.FIELD_STATUS, LangModel.getString("statusReady"));
				// this.statusBar.setText("server",
				// ConnectionInterface.getInstance().getServerName());
			}
			if (cce.isConnectionClosed()) {
				Log.debugMessage("AbstractMainFrame.propertyChange | cce.isConnectionClosed() ", Log.FINEST);
				this.statusBar.setText(StatusBar.FIELD_STATUS, LangModel.getString("statusError"));
				this.statusBar.setText(StatusBar.FIELD_SERVER, LangModel.getString("statusConnectionError"));

				this.statusBar.setText(StatusBar.FIELD_STATUS, LangModel.getString("statusDisconnected"));
				this.statusBar.setText(StatusBar.FIELD_SERVER, LangModel.getString("statusNoConnection"));

				this.setConnectionClosed();
			}
			if (cce.isConnectionFailed()) {
				this.statusBar.setText(StatusBar.FIELD_STATUS, LangModel.getString("statusError"));
				this.statusBar.setText(StatusBar.FIELD_SERVER, LangModel.getString("statusConnectionError"));

				this.setConnectionFailed();
			}
			if (cce.isDomainSelected()) {
				Log.debugMessage("AbstractMainFrame.propertyChange | cce.isDomainSelected()() ", Log.FINEST);
				this.setDomainSelected();
			}
		}

	}

	public void setConnectionClosed() {
		Log.debugMessage("AbstractMainFrame.setConnectionClosed | ", Log.FINEST);
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, false);
		aModel.fireModelChanged("");
	}

	public void setConnectionFailed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.fireModelChanged("");
	}

	public void setConnectionOpened() {
		Log.debugMessage("AbstractMainFrame.setConnectionOpened | ", Log.FINEST);
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CONNECTION, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, true);
		aModel.fireModelChanged("");
	}

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		aContext.setDispatcher(this.dispatcher);
		this.setModel(aContext.getApplicationModel());
	}

	public void setDomainSelected() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, true);

		aModel.fireModelChanged("");

		try {
			Domain domain = (Domain) StorableObjectPool.getStorableObject(LoginManager.getDomainId(), true);
			this.statusBar.setText(StatusBar.FIELD_DOMAIN, domain.getName());
		} catch (ApplicationException e) {
			showErrorMessage(this, e);
		}

		this.windowArranger.arrange();

	}

	public void setModel(ApplicationModel aModel) {
		this.toolBar.setApplicationModel(aModel);
		this.menuBar.setApplicationModel(aModel);
		this.addListeners(aModel, this.menuBar.getApplicationModelListeners());
		this.addListeners(aModel, this.toolBar.getApplicationModelListeners());
		aModel.fireModelChanged("");
	}

	private void addListeners(	ApplicationModel aModel,
								List applicationModelListeners) {
		if (applicationModelListeners != null && !applicationModelListeners.isEmpty()) {
			for (Iterator iterator = applicationModelListeners.iterator(); iterator.hasNext();) {
				ApplicationModelListener listener = (ApplicationModelListener) iterator.next();
				aModel.addListener(listener);
			}
		}
	}

	public void setSessionClosed() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_DOMAIN, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, true);

		aModel.fireModelChanged("");

		this.statusBar.setText(StatusBar.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
	}

	public void setSessionOpened() {
		// this.checker = new
		// Checker(aContext.getDataSourceInterface());

		// Dispatcher dispatcher = this.aContext.getDispatcher();
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModel
				.getString("Loading_DB")));
		this.dispatcher.firePropertyChange(new StatusMessageEvent(this, StatusMessageEvent.STATUS_MESSAGE, LangModel
				.getString("Loding_DB_finished")));

		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ApplicationModel.MENU_SESSION_DOMAIN, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, false);
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
			this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			Environment.getDispatcher().removePropertyChangeListener(ContextChangeEvent.TYPE, this);
			this.aContext.getApplicationModel().getCommand(ApplicationModel.MENU_EXIT).execute();
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

		// this.statusBar.distribute();
		this.statusBar.setWidth(StatusBar.FIELD_STATUS, 300);
		this.statusBar.setWidth(StatusBar.FIELD_SERVER, 250);
		this.statusBar.setWidth(StatusBar.FIELD_SESSION, 200);
		this.statusBar.setWidth(StatusBar.FIELD_USER, 100);
		this.statusBar.setWidth(StatusBar.FIELD_DOMAIN, 150);
		this.statusBar.setWidth(StatusBar.FIELD_TIME, 50);

		this.statusBar.setText(StatusBar.FIELD_STATUS, LangModel.getString("statusReady"));
		this.statusBar.setText(StatusBar.FIELD_SERVER, LangModel.getString("statusNoConnection"));
		this.statusBar.setText(StatusBar.FIELD_SESSION, LangModel.getString("statusNoSession"));
		this.statusBar.setText(StatusBar.FIELD_USER, LangModel.getString("statusNoUser"));
		this.statusBar.setText(StatusBar.FIELD_DOMAIN, LangModel.getString("statusNoDomain"));
		this.statusBar.setText(StatusBar.FIELD_TIME, " ");
		// this.statusBar.organize();

		this.aContext.setDispatcher(this.dispatcher);
//		this.dispatcher.addPropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
		// this.dispatcher.addPropertyChangeListener(COMMAND_CHANGE_STATUSBAR_STATE,
		// this);

		this.dispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		Dispatcher theDispatcher = Environment.getDispatcher();
		
		this.statusBar.addDispatcher(theDispatcher);
		
		theDispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);

		aModel.setCommand(ApplicationModel.MENU_SESSION_NEW, new OpenSessionCommand(theDispatcher));
		aModel.setCommand(ApplicationModel.MENU_SESSION_CLOSE, new SessionCloseCommand(theDispatcher));
		aModel.setCommand(ApplicationModel.MENU_SESSION_OPTIONS, new SessionOptionsCommand(this.aContext));
		aModel.setCommand(ApplicationModel.MENU_SESSION_CONNECTION, new SessionConnectionCommand(theDispatcher,
																									this.aContext));
		aModel.setCommand(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD,
			new SessionChangePasswordCommand(theDispatcher, this.aContext));
		aModel.setCommand(ApplicationModel.MENU_SESSION_DOMAIN, new SessionDomainCommand(theDispatcher, this.aContext));
		aModel.setCommand(ApplicationModel.MENU_EXIT, new ExitCommand(this));

		// this.setWindowArranger(this.windowArranger);
		// if (this.windowArranger != null) {
		// aModel.setCommand(AbstractMainMenuBar.MENU_VIEW_ARRANGE, new
		// ArrangeWindowCommand(this.windowArranger));
		// }
		aModel.setCommand(ApplicationModel.MENU_HELP_ABOUT, new HelpAboutCommand(this));

		setDefaultModel(aModel);

		aModel.fireModelChanged("");

	}

	protected void setDefaultModel(ApplicationModel aModel) {
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CONNECTION, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, false);

		aModel.setEnabled(ApplicationModel.MENU_HELP, true);
		aModel.setEnabled(ApplicationModel.MENU_HELP_ABOUT, true);
	}

	public void setWindowArranger(WindowArranger windowArranger) {
		if (windowArranger != null) {
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setCommand(ApplicationModel.MENU_VIEW_ARRANGE, new ArrangeWindowCommand(windowArranger));
			this.windowArranger = windowArranger;
		}
	}
}
