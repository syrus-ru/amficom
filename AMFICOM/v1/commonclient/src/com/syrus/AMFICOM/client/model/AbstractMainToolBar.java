/*-
 * $Id: AbstractMainToolBar.java,v 1.12 2005/09/07 02:37:31 arseniy Exp $
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
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.12 $, $Date: 2005/09/07 02:37:31 $
 * @author $Author: arseniy $
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

			private boolean	executed	= false;

			public void actionPerformed(final ActionEvent e) {
				final ApplicationModel model = AbstractMainToolBar.this.getApplicationModel();
				if (this.executed || model == null) {
					return;
				}
				this.executed = true;
				final AbstractButton jb = (AbstractButton) e.getSource();
				final String s = jb.getName();
				final Command command = model.getCommand(s);
				Log.debugMessage("AbstractMainToolBar$ActionListener.actionPerformed | command " + (command != null ? '[' + command.getClass().getName() + ']' : "'null'"), Level.FINEST);
				try {
					command.execute();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					this.executed = false;
				}
			}
		};

		this.openSessionButton = new JButton();
		this.openSessionButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_SESSION));
		this.openSessionButton.setToolTipText(LangModelGeneral.getString("Menu.Session.NewSession"));
		this.openSessionButton.setName(ApplicationModel.MENU_SESSION_NEW);
		this.openSessionButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));		
		this.openSessionButton.addActionListener(this.actionListener);

		this.closeSessionButton = new JButton();
		this.closeSessionButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_CLOSE_SESSION));
		this.closeSessionButton.setToolTipText(LangModelGeneral.getString("Menu.Session.CloseSession"));
		this.closeSessionButton.setName(ApplicationModel.MENU_SESSION_CLOSE);
		this.closeSessionButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.closeSessionButton.addActionListener(this.actionListener);

		this.sessionDomainButton = new JButton();
		this.sessionDomainButton.setIcon(UIManager.getIcon(ResourceKeys.ICON_DOMAIN_SELECTION));
		this.sessionDomainButton.setToolTipText(LangModelGeneral.getString("Menu.Session.SelectDomain"));
		this.sessionDomainButton.setName(ApplicationModel.MENU_SESSION_DOMAIN);
		this.sessionDomainButton.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.sessionDomainButton.addActionListener(this.actionListener);

		this.add(this.openSessionButton);
		this.add(this.closeSessionButton);
		this.addSeparator();
		this.add(this.sessionDomainButton);

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(final String e[]) {
				this.modelChanged("");
			}

			public void modelChanged(final String elementName) {
				AbstractMainToolBar.this.openSessionButton.setVisible(AbstractMainToolBar.this.getApplicationModel().isVisible(ApplicationModel.MENU_SESSION_NEW));
				AbstractMainToolBar.this.openSessionButton.setEnabled(AbstractMainToolBar.this.getApplicationModel().isEnabled(ApplicationModel.MENU_SESSION_NEW));

				AbstractMainToolBar.this.closeSessionButton.setVisible(AbstractMainToolBar.this.getApplicationModel().isVisible(ApplicationModel.MENU_SESSION_CLOSE));
				AbstractMainToolBar.this.closeSessionButton.setEnabled(AbstractMainToolBar.this.getApplicationModel().isEnabled(ApplicationModel.MENU_SESSION_CLOSE));
				AbstractMainToolBar.this.sessionDomainButton.setVisible(AbstractMainToolBar.this.getApplicationModel().isVisible(ApplicationModel.MENU_SESSION_DOMAIN));
				AbstractMainToolBar.this.sessionDomainButton.setEnabled(AbstractMainToolBar.this.getApplicationModel().isEnabled(ApplicationModel.MENU_SESSION_DOMAIN));

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

	public List getApplicationModelListeners() {
		return this.applicationModelListeners;
	}

}
