/*-
 * $Id: AbstractMainMenuBar.java,v 1.3 2005/06/01 10:01:33 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/01 10:01:33 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainMenuBar extends JMenuBar {

	public static final String	MENU_SESSION					= "menuSession";
	public static final String	MENU_SESSION_NEW				= "menuSessionNew";
	public static final String	MENU_SESSION_CLOSE				= "menuSessionClose";
	public static final String	MENU_SESSION_OPTIONS			= "menuSessionOptions";
	public static final String	MENU_SESSION_CONNECTION			= "menuSessionConnection";
	public static final String	MENU_SESSION_CHANGE_PASSWORD	= "menuSessionChangePassword";
	public static final String	MENU_SESSION_DOMAIN				= "menuSessionDomain";

	public static final String	MENU_VIEW_ARRANGE				= "menuViewArrange";

	public static final String	MENU_EXIT						= "menuExit";

	public static final String	MENU_HELP						= "menuHelp";
	public static final String	MENU_HELP_ABOUT					= "menuHelpAbout";

	protected List				applicationModelListeners;
	protected ApplicationModel	applicationModel;

	protected ActionListener	actionAdapter;

	public AbstractMainMenuBar(final ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;

		this.actionAdapter = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (applicationModel == null)
					return;
				AbstractButton jb = (AbstractButton) e.getSource();
				String s = jb.getName();
				Command command = applicationModel.getCommand(s);
				Log.debugMessage(
					"AbstractMainMenuBar$ActionListener.actionPerformed | " + command.getClass().getName(), Log.FINEST);
				command.execute();
			}
		};

		this.createMenuItems();
	}

	private void createMenuItems() {
		final JMenu menuSession = new JMenu();

		menuSession.setText(LangModel.getString("menuSession"));
		menuSession.setName(MENU_SESSION);

		final JMenuItem menuSessionNew = new JMenuItem();
		menuSessionNew.setText(LangModel.getString("menuSessionNew"));
		menuSessionNew.setName(MENU_SESSION_NEW);
		menuSessionNew.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionClose = new JMenuItem();
		menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		menuSessionClose.setName(MENU_SESSION_CLOSE);
		menuSessionClose.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionOptions = new JMenuItem();
		menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		menuSessionOptions.setName(MENU_SESSION_OPTIONS);
		menuSessionOptions.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionConnection = new JMenuItem();
		menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		menuSessionConnection.setName(MENU_SESSION_CONNECTION);
		menuSessionConnection.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionChangePassword = new JMenuItem();
		menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		menuSessionChangePassword.setName(MENU_SESSION_CHANGE_PASSWORD);
		menuSessionChangePassword.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionDomain = new JMenuItem();
		menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName(MENU_SESSION_DOMAIN);
		menuSessionDomain.addActionListener(this.actionAdapter);

		JMenuItem menuExit = new JMenuItem();
		menuExit.setText(LangModel.getString("menuExit"));
		menuExit.setName(MENU_EXIT);
		menuExit.addActionListener(this.actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		this.add(menuSession);

		this.addMenuItems();

		final JMenu menuHelp = new JMenu(LangModel.getString("Help"));
		menuHelp.setName(MENU_HELP);

		final JMenuItem menuHelpAboutItem = new JMenuItem(LangModel.getString("About_program"));
		menuHelpAboutItem.setName(MENU_HELP_ABOUT);
		menuHelpAboutItem.addActionListener(this.actionAdapter);
		menuHelp.add(menuHelpAboutItem);

		this.add(menuHelp);

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String elementName) {
				this.modelChanged();
			}

			public void modelChanged(String[] elementNames) {
				this.modelChanged();
			}

			private void modelChanged() {
				menuSession.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(MENU_SESSION));
				menuSession.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(MENU_SESSION));
				menuSessionNew.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(MENU_SESSION_NEW));
				menuSessionNew.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(MENU_SESSION_NEW));
				menuSessionClose.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(MENU_SESSION_CLOSE));
				menuSessionClose.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(MENU_SESSION_CLOSE));
				menuSessionOptions.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(MENU_SESSION_OPTIONS));
				menuSessionOptions.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(MENU_SESSION_OPTIONS));
				menuSessionConnection.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(MENU_SESSION_CONNECTION));
				menuSessionConnection.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(MENU_SESSION_CONNECTION));
				menuSessionChangePassword.setVisible(AbstractMainMenuBar.this.applicationModel
						.isVisible(MENU_SESSION_CHANGE_PASSWORD));
				menuSessionChangePassword.setEnabled(AbstractMainMenuBar.this.applicationModel
						.isEnabled(MENU_SESSION_CHANGE_PASSWORD));
				menuSessionDomain.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(MENU_SESSION_DOMAIN));
				menuSessionDomain.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(MENU_SESSION_DOMAIN));

				menuHelpAboutItem.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(MENU_HELP_ABOUT));
				menuHelpAboutItem.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(MENU_HELP_ABOUT));
			}
		});
	}

	protected abstract void addMenuItems();

	protected void addApplicationModelListener(ApplicationModelListener listener) {
		if (this.applicationModelListeners == null) {
			this.applicationModelListeners = new LinkedList();
		}

		if (!this.applicationModelListeners.contains(listener)) {
			this.applicationModelListeners.add(listener);
		}
	}
	
	protected void removeApplicationModelListener(ApplicationModelListener listener) {
		if (this.applicationModelListeners != null) {
			this.applicationModelListeners.remove(listener);
		}
	}
	
	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}
	
	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}
	
	public List getApplicationModelListeners() {
		return this.applicationModelListeners;
	}

}
