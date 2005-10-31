/*-
 * $Id: AbstractMainFrame.java,v 1.32 2005/10/31 12:30:02 bass Exp $
 *
 * Copyright © 2004-2005 Syrus Systems.
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
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.SystemUser;
import com.syrus.AMFICOM.administration.PermissionAttributes.PermissionCodename;
import com.syrus.AMFICOM.client.UI.ArrangeWindowCommand;
import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.StatusBar;
import com.syrus.AMFICOM.client.UI.WindowArranger;
import com.syrus.AMFICOM.client.event.ContextChangeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Checker;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Application;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.32 $, $Date: 2005/10/31 12:30:02 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public abstract class AbstractMainFrame extends JFrame 
implements PropertyChangeListener {

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
	protected final AbstractMainToolBar	toolBar;

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

		final GraphicsEnvironment localGraphicsEnvironment = 
			GraphicsEnvironment.getLocalGraphicsEnvironment();
		final Rectangle maximumWindowBounds = 
			localGraphicsEnvironment.getMaximumWindowBounds();
		this.setSize(new Dimension(maximumWindowBounds.width, 
			maximumWindowBounds.height));
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

	public void propertyChange(final PropertyChangeEvent evt) {
		final String propertyName = evt.getPropertyName().intern();
		if (propertyName == ContextChangeEvent.TYPE) {			
			final ContextChangeEvent cce = (ContextChangeEvent) evt;
			if (cce.isLoggedIn()) {
				this.loggedIn0();
			}
			
			if (cce.isLoggedOut()) {
				this.loggedOut0();
			}
		}
	}
	
	private final void loggedIn0() {
		if (this.checkEnter()) {
			final ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, false);
			aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, true);
			aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, true);
			aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, true);

			aModel.fireModelChanged();

			final SimpleDateFormat sdf = (SimpleDateFormat) UIManager.get(ResourceKeys.SIMPLE_DATE_FORMAT);

			final Identifier domainId = LoginManager.getDomainId();
			final Identifier userId = LoginManager.getUserId();
			try {
				Domain domain = StorableObjectPool.getStorableObject(
						domainId, 
						true);
				this.statusBar.setText(StatusBar.FIELD_DOMAIN, domain.getName());
				
				final SystemUser user = StorableObjectPool.getStorableObject(userId, true);
				this.statusBar.setText(StatusBar.FIELD_USER, user.getName());
			} catch (final ApplicationException ae) {
				Log.errorMessage(ae);
				showErrorMessage(I18N.getString("Error.CannotAcquireObject"));
				return;
			}
			
			this.statusBar.setText(StatusBar.FIELD_STATUS, 
				I18N.getString("Common.StatusBar.Ready"));
			
			final ClientSessionEnvironment clientSessionEnvironment = 
				ClientSessionEnvironment.getInstance();
			this.statusBar.setText(StatusBar.FIELD_SESSION, 
				sdf.format(clientSessionEnvironment.getSessionEstablishDate()));
			this.statusBar.setText(StatusBar.FIELD_SERVER, 
				clientSessionEnvironment.getServerName());
			

			this.windowArranger.arrange();
			this.loggedIn();
		}
	}
	
	public abstract void loggedIn();
	
	private final void loggedOut0() {
		ApplicationModel aModel = this.aContext.getApplicationModel();
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CLOSE, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_OPTIONS, false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD, false);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, false);
		aModel.fireModelChanged();
		this.loggedOut();
	}
	
	public abstract void loggedOut();

	public void setContext(ApplicationContext aContext) {
		this.aContext = aContext;
		aContext.setDispatcher(this.dispatcher);
		this.setModel(aContext.getApplicationModel());
	}

	private final boolean checkEnter() {
		final String action;
		if ((action = this.isEnterModuleEnable()) != null) {
			showErrorMessage("<html>" 
				+ I18N.getString("Common.Permission.DenyAccess") 
				+ " : <br>" 
				+ action
				+ "</html>");
			return false;
		}
		return true;
	}
	
	private final String isEnterModuleEnable() {
		final String codename = Application.getApplicationName().toUpperCase() + "_ENTER";		
		final PermissionCodename permissionCodename = 
			Enum.valueOf(PermissionCodename.class, codename);
		
		try {
			if (Checker.isPermitted(permissionCodename)) {
				return null;
			}
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
			// and return problems
		}
		return permissionCodename.getDescription() 
			+ " \"" 
			+ permissionCodename.getModule().getDescription() 
			+ "\".";
	}

	public void setModel(final ApplicationModel aModel) {
		this.toolBar.setApplicationModel(aModel);
		this.menuBar.setApplicationModel(aModel);
		this.addListeners(aModel, this.menuBar.getApplicationModelListeners());
		this.addListeners(aModel, this.toolBar.getApplicationModelListeners());
		aModel.fireModelChanged();
	}

	private void addListeners(final ApplicationModel aModel,
			final List<ApplicationModelListener> applicationModelListeners) {
		if (applicationModelListeners != null && 
				!applicationModelListeners.isEmpty()) {
			for (final ApplicationModelListener listener : applicationModelListeners) {
				aModel.addListener(listener);
			}
		}
	}

	@Override
	protected void processWindowEvent(final WindowEvent e) {
		super.processWindowEvent(e);
		int id = e.getID();
		switch(id) {
			case WindowEvent.WINDOW_ACTIVATED:
				Environment.setActiveWindow(this);
				break;
			case WindowEvent.WINDOW_CLOSING:
				this.disposeModule();
				break;
		}		
	}

	public static void showErrorMessage(final Component component,
										final Exception exception) {
		JOptionPane.showMessageDialog(component, 
			CommonUIUtilities.convertToHTMLString(exception.getMessage()),
			I18N.getString("Error.ErrorOccur"),
			JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorMessage(final String errorMessage) {
		JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
			CommonUIUtilities.convertToHTMLString(errorMessage),
			I18N.getString("Error.ErrorOccur"),
			JOptionPane.ERROR_MESSAGE);
	}
	
	protected Command getShowWindowLazyCommand(final UIDefaults frames,
	                                           final Object windowKey) {
		final String commandKey = windowKey.toString() + "_COMMAND";
		frames.put(commandKey, new UIDefaults.LazyValue() {

			public Object createValue(UIDefaults defaults) {
				return new ShowWindowCommand((JInternalFrame) defaults.get(windowKey));
			}
		});
		return new LazyCommand(frames, commandKey);
	}

	protected void initModule() {
		ApplicationModel aModel = this.aContext.getApplicationModel();

		this.statusBar.setWidth(StatusBar.FIELD_STATUS, 300);
		this.statusBar.setWidth(StatusBar.FIELD_SERVER, 250);
		this.statusBar.setWidth(StatusBar.FIELD_SESSION, 200);
		this.statusBar.setWidth(StatusBar.FIELD_USER, 100);
		this.statusBar.setWidth(StatusBar.FIELD_DOMAIN, 150);
		this.statusBar.setWidth(StatusBar.FIELD_TIME, 50);

		this.statusBar.setText(StatusBar.FIELD_STATUS, 
				I18N.getString("Common.StatusBar.Ready"));
		this.statusBar.setText(StatusBar.FIELD_SERVER, 
				I18N.getString("Common.StatusBar.NoConnection"));
		this.statusBar.setText(StatusBar.FIELD_SESSION, 
				I18N.getString("Common.StatusBar.NoSession"));
		this.statusBar.setText(StatusBar.FIELD_USER, 
				I18N.getString("Common.StatusBar.NoUser"));
		this.statusBar.setText(StatusBar.FIELD_DOMAIN, 
				I18N.getString("Common.StatusBar.NoDomain"));
		this.statusBar.setText(StatusBar.FIELD_TIME, " ");

		this.aContext.setDispatcher(this.dispatcher);

		this.dispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);
		Dispatcher theDispatcher = Environment.getDispatcher();
		
		this.statusBar.addDispatcher(theDispatcher);
		
		theDispatcher.addPropertyChangeListener(ContextChangeEvent.TYPE, this);

		aModel.setCommand(ApplicationModel.MENU_SESSION_NEW, 
				new OpenSessionCommand(theDispatcher));
		aModel.setCommand(ApplicationModel.MENU_SESSION_CLOSE, 
				new SessionCloseCommand(theDispatcher));
		aModel.setCommand(ApplicationModel.MENU_SESSION_OPTIONS, 
				new SessionOptionsCommand(this.aContext));
		aModel.setCommand(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD,
				new SessionChangePasswordCommand(theDispatcher, this.aContext));
//		aModel.setCommand(ApplicationModel.MENU_SESSION_DOMAIN, 
//				new SessionDomainCommand(theDispatcher));
		aModel.setCommand(ApplicationModel.MENU_EXIT, new ExitCommand(this, this.dispatcher));

		// this.setWindowArranger(this.windowArranger);
		// if (this.windowArranger != null) {
		// aModel.setCommand(AbstractMainMenuBar.MENU_VIEW_ARRANGE, new
		// ArrangeWindowCommand(this.windowArranger));
		// }
		aModel.setCommand(ApplicationModel.MENU_HELP_ABOUT, 
				new HelpAboutCommand(this));

		setDefaultModel(aModel);

		aModel.fireModelChanged();

	}
	
	protected void setFramesVisible(boolean b) {
		for (Component component : this.desktopPane.getComponents()) {
			component.setVisible(b);
		}
	}
	
	protected void disposeModule() {
		this.statusBar.removeDispatcher(this.dispatcher);
		this.statusBar.removeDispatcher(Environment.getDispatcher());
		this.dispatcher.removePropertyChangeListener(ContextChangeEvent.TYPE, 
				this);
		Environment.getDispatcher().removePropertyChangeListener(
				ContextChangeEvent.TYPE, this);
		this.aContext.getApplicationModel().getCommand(
			ApplicationModel.MENU_EXIT).execute();
		Environment.disposeWindow(this);
	}

	protected void setDefaultModel(ApplicationModel aModel) {
		aModel.setAllItemsEnabled(false);
		aModel.setEnabled(ApplicationModel.MENU_SESSION, true);
		aModel.setEnabled(ApplicationModel.MENU_SESSION_NEW, true);
		aModel.setEnabled(ApplicationModel.MENU_VIEW_ARRANGE, false);
		aModel.setEnabled(ApplicationModel.MENU_EXIT, true);

		aModel.setEnabled(ApplicationModel.MENU_HELP, true);
		aModel.setEnabled(ApplicationModel.MENU_HELP_ABOUT, true);
	}

	public void setWindowArranger(WindowArranger windowArranger) {
		if (windowArranger != null) {
			ApplicationModel aModel = this.aContext.getApplicationModel();
			aModel.setCommand(ApplicationModel.MENU_VIEW_ARRANGE, 
				new ArrangeWindowCommand(windowArranger));
			this.windowArranger = windowArranger;
		}
	}
}
