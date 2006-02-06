/*-
 * $Id: AbstractMainToolBar.java,v 1.17 2005/10/31 12:30:02 bass Exp $
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
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2005/10/31 12:30:02 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module commonclient
 */
public abstract class AbstractMainToolBar extends JToolBar {

	protected ApplicationModel applicationModel;

	protected ActionListener actionListener;

	protected List<ApplicationModelListener> applicationModelListeners;

	protected JButton openSessionButton;
	protected JButton closeSessionButton;
	protected JButton sessionDomainButton;

	public AbstractMainToolBar() {		
		this.actionListener = new ActionListener() {

			public synchronized void actionPerformed(final ActionEvent e) {
				final ApplicationModel model = AbstractMainToolBar.this.getApplicationModel();
				if (model == null) {
					return;
				}
				final AbstractButton jb = (AbstractButton) e.getSource();
				final String s = jb.getName();
				final Command command = model.getCommand(s);
				Log.debugMessage("AbstractMainToolBar$ActionListener.actionPerformed | command " 
						+ (command != null ? 
							'[' + command.getClass().getName() + ']' : 
							"'null'"), 
					Log.DEBUGLEVEL10);
				command.execute();
			}
		};

		this.openSessionButton = new JButton();
		this.openSessionButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_SESSION));
		this.openSessionButton.setToolTipText(I18N.getString(ApplicationModel.MENU_SESSION_NEW));
		this.openSessionButton.setName(ApplicationModel.MENU_SESSION_NEW);
		this.openSessionButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));		
		this.openSessionButton.addActionListener(this.actionListener);

		this.closeSessionButton = new JButton();
		this.closeSessionButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_CLOSE_SESSION));
		this.closeSessionButton.setToolTipText(I18N.getString(ApplicationModel.MENU_SESSION_CLOSE));
		this.closeSessionButton.setName(ApplicationModel.MENU_SESSION_CLOSE);
		this.closeSessionButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.closeSessionButton.addActionListener(this.actionListener);

		this.add(this.openSessionButton);
		this.add(this.closeSessionButton);

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(final String e[]) {
				this.modelChanged("");
			}

			public void modelChanged(final String elementName) {
				AbstractMainToolBar.this.openSessionButton.setVisible(AbstractMainToolBar.this.getApplicationModel().isVisible(ApplicationModel.MENU_SESSION_NEW));
				AbstractMainToolBar.this.openSessionButton.setEnabled(AbstractMainToolBar.this.getApplicationModel().isEnabled(ApplicationModel.MENU_SESSION_NEW));

				AbstractMainToolBar.this.closeSessionButton.setVisible(AbstractMainToolBar.this.getApplicationModel().isVisible(ApplicationModel.MENU_SESSION_CLOSE));
				AbstractMainToolBar.this.closeSessionButton.setEnabled(AbstractMainToolBar.this.getApplicationModel().isEnabled(ApplicationModel.MENU_SESSION_CLOSE));
			}
		});

	}

	public void setApplicationModel(final ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}

	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

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

	public List<ApplicationModelListener> getApplicationModelListeners() {
		return this.applicationModelListeners;
	}

}
