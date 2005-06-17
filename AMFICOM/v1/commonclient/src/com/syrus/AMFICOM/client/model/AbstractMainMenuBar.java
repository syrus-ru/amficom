/*-
 * $Id: AbstractMainMenuBar.java,v 1.6 2005/06/17 14:32:06 bob Exp $
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

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/06/17 14:32:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainMenuBar extends JMenuBar {

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

		menuSession.setText(LangModelGeneral.getString("Menu.Session"));
		menuSession.setName(ApplicationModel.MENU_SESSION);

		final JMenuItem menuSessionNew = new JMenuItem();
		menuSessionNew.setText(LangModelGeneral.getString("Menu.Session.NewSession"));
		menuSessionNew.setName(ApplicationModel.MENU_SESSION_NEW);
		menuSessionNew.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionClose = new JMenuItem();
		menuSessionClose.setText(LangModelGeneral.getString("Menu.Session.CloseSession"));
		menuSessionClose.setName(ApplicationModel.MENU_SESSION_CLOSE);
		menuSessionClose.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionOptions = new JMenuItem();
		menuSessionOptions.setText(LangModelGeneral.getString("Menu.Session.SessionOptions"));
		menuSessionOptions.setName(ApplicationModel.MENU_SESSION_OPTIONS);
		menuSessionOptions.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionChangePassword = new JMenuItem();
		menuSessionChangePassword.setText(LangModelGeneral.getString("Menu.Session.ChangePassword"));
		menuSessionChangePassword.setName(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
		menuSessionChangePassword.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionDomain = new JMenuItem();
		menuSessionDomain.setText(LangModelGeneral.getString("Menu.Session.SelectDomain"));
		menuSessionDomain.setName(ApplicationModel.MENU_SESSION_DOMAIN);
		menuSessionDomain.addActionListener(this.actionAdapter);

		JMenuItem menuExit = new JMenuItem();
		menuExit.setText(LangModelGeneral.getString("Menu.Exit"));
		menuExit.setName(ApplicationModel.MENU_EXIT);
		menuExit.addActionListener(this.actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		this.add(menuSession);

		this.addMenuItems();

		final JMenu menuHelp = new JMenu(LangModelGeneral.getString("Menu.Help"));
		menuHelp.setName(ApplicationModel.MENU_HELP);

		final JMenuItem menuHelpAboutItem = new JMenuItem(LangModelGeneral.getString("Menu.Help.About"));
		menuHelpAboutItem.setName(ApplicationModel.MENU_HELP_ABOUT);
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
				menuSession.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_SESSION));
				menuSession.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_SESSION));
				menuSessionNew.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_SESSION_NEW));
				menuSessionNew.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_SESSION_NEW));
				menuSessionClose.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_SESSION_CLOSE));
				menuSessionClose.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_SESSION_CLOSE));
				menuSessionOptions.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_SESSION_OPTIONS));
				menuSessionOptions.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_SESSION_OPTIONS));
				menuSessionChangePassword.setVisible(AbstractMainMenuBar.this.applicationModel
						.isVisible(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD));
				menuSessionChangePassword.setEnabled(AbstractMainMenuBar.this.applicationModel
						.isEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD));
				menuSessionDomain.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_SESSION_DOMAIN));
				menuSessionDomain.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_SESSION_DOMAIN));

				menuHelpAboutItem.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_HELP_ABOUT));
				menuHelpAboutItem.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_HELP_ABOUT));
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
