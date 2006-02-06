/*-
 * $Id: AbstractMainMenuBar.java,v 1.13 2005/10/31 12:30:01 bass Exp $
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
import java.util.logging.Level;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/31 12:30:01 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public abstract class AbstractMainMenuBar extends JMenuBar {

	protected List<ApplicationModelListener> applicationModelListeners;
	protected ApplicationModel applicationModel;

	protected ActionListener actionAdapter;

	public AbstractMainMenuBar(final ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;

		this.actionAdapter = new ActionListener() {

			public void actionPerformed(final ActionEvent e) {
				if (applicationModel == null) {
					return;
				}
				final AbstractButton jb = (AbstractButton) e.getSource();
				final String s = jb.getName();
				final Command command = applicationModel.getCommand(s);
				Log.debugMessage("AbstractMainMenuBar$ActionListener.actionPerformed | " + command.getClass().getName(), Level.FINEST);
				command.execute();
			}
		};

		this.createMenuItems();
	}

	private void createMenuItems() {
		final JMenu menuSession = new JMenu();

		menuSession.setText(I18N.getString(ApplicationModel.MENU_SESSION));
		menuSession.setName(ApplicationModel.MENU_SESSION);

		final JMenuItem menuSessionNew = new JMenuItem();
		menuSessionNew.setText(I18N.getString(ApplicationModel.MENU_SESSION_NEW));
		menuSessionNew.setName(ApplicationModel.MENU_SESSION_NEW);
		menuSessionNew.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionClose = new JMenuItem();
		menuSessionClose.setText(I18N.getString(ApplicationModel.MENU_SESSION_CLOSE));
		menuSessionClose.setName(ApplicationModel.MENU_SESSION_CLOSE);
		menuSessionClose.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionOptions = new JMenuItem();
		menuSessionOptions.setText(I18N.getString(ApplicationModel.MENU_SESSION_OPTIONS));
		menuSessionOptions.setName(ApplicationModel.MENU_SESSION_OPTIONS);
		menuSessionOptions.addActionListener(this.actionAdapter);

		final JMenuItem menuSessionChangePassword = new JMenuItem();
		menuSessionChangePassword.setText(I18N.getString(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD));
		menuSessionChangePassword.setName(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD);
		menuSessionChangePassword.addActionListener(this.actionAdapter);

		final JMenuItem menuExit = new JMenuItem();
		menuExit.setText(I18N.getString(ApplicationModel.MENU_EXIT));
		menuExit.setName(ApplicationModel.MENU_EXIT);
		menuExit.addActionListener(this.actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		this.add(menuSession);

		this.addMenuItems();

		final JMenu menuHelp = new JMenu(I18N.getString(ApplicationModel.MENU_HELP));
		menuHelp.setName(ApplicationModel.MENU_HELP);

		final JMenuItem menuHelpAboutItem = new JMenuItem(I18N.getString(ApplicationModel.MENU_HELP_ABOUT));
		menuHelpAboutItem.setName(ApplicationModel.MENU_HELP_ABOUT);
		menuHelpAboutItem.addActionListener(this.actionAdapter);
		menuHelp.add(menuHelpAboutItem);

		this.add(menuHelp);

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(final String elementName) {
				this.modelChanged();
			}

			public void modelChanged(final String[] elementNames) {
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
				menuSessionChangePassword.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD));
				menuSessionChangePassword.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_SESSION_CHANGE_PASSWORD));

				menuHelpAboutItem.setVisible(AbstractMainMenuBar.this.applicationModel.isVisible(ApplicationModel.MENU_HELP_ABOUT));
				menuHelpAboutItem.setEnabled(AbstractMainMenuBar.this.applicationModel.isEnabled(ApplicationModel.MENU_HELP_ABOUT));
			}
		});
	}

	protected abstract void addMenuItems();

	protected void addApplicationModelListener(final ApplicationModelListener listener) {
		if (this.applicationModelListeners == null) {
			this.applicationModelListeners = new LinkedList<ApplicationModelListener>();
		}

		if (!this.applicationModelListeners.contains(listener)) {
			this.applicationModelListeners.add(listener);
		}
	}

	protected void removeApplicationModelListener(final ApplicationModelListener listener) {
		if (this.applicationModelListeners != null) {
			this.applicationModelListeners.remove(listener);
		}
	}

	public void setApplicationModel(final ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}

	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

	public List<ApplicationModelListener> getApplicationModelListeners() {
		return this.applicationModelListeners;
	}

}
