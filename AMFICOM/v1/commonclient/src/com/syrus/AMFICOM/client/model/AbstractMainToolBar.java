/*-
 * $Id: AbstractMainToolBar.java,v 1.7 2005/06/14 11:25:17 bob Exp $
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

import com.syrus.AMFICOM.client.resource.LangModelGeneral;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.7 $, $Date: 2005/06/14 11:25:17 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainToolBar extends JToolBar {

	protected ApplicationModel	applicationModel;

	protected ActionListener	actionListener;

	protected List				applicationModelListeners;

	protected JButton			openSessionButton;
	protected JButton			closeSessionButton;
	protected JButton			sessionDomainButton;

	public AbstractMainToolBar() {		
		this.actionListener = new ActionListener() {

			private boolean	executed	= false;

			public void actionPerformed(ActionEvent e) {
				ApplicationModel model = AbstractMainToolBar.this.getApplicationModel();
				if (this.executed || model == null)
					return;
				this.executed = true;
				AbstractButton jb = (AbstractButton) e.getSource();
				String s = jb.getName();
				Command command = model.getCommand(s);
				Log.debugMessage("AbstractMainToolBar$ActionListener.actionPerformed | command " + command, Log.FINEST);
				command.execute();
				this.executed = false;
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

			public void modelChanged(String e[]) {
				this.modelChanged("");
			}

			public void modelChanged(String elementName) {
				AbstractMainToolBar.this.openSessionButton.setVisible(AbstractMainToolBar.this.getApplicationModel()
						.isVisible(ApplicationModel.MENU_SESSION_NEW));
				AbstractMainToolBar.this.openSessionButton.setEnabled(AbstractMainToolBar.this.getApplicationModel()
						.isEnabled(ApplicationModel.MENU_SESSION_NEW));

				AbstractMainToolBar.this.closeSessionButton.setVisible(AbstractMainToolBar.this.getApplicationModel()
						.isVisible(ApplicationModel.MENU_SESSION_CLOSE));
				AbstractMainToolBar.this.closeSessionButton.setEnabled(AbstractMainToolBar.this.getApplicationModel()
						.isEnabled(ApplicationModel.MENU_SESSION_CLOSE));
				AbstractMainToolBar.this.sessionDomainButton.setVisible(AbstractMainToolBar.this.getApplicationModel()
						.isVisible(ApplicationModel.MENU_SESSION_DOMAIN));
				AbstractMainToolBar.this.sessionDomainButton.setEnabled(AbstractMainToolBar.this.getApplicationModel()
						.isEnabled(ApplicationModel.MENU_SESSION_DOMAIN));

			}
		});

	}

	public void setApplicationModel(ApplicationModel applicationModel) {
		this.applicationModel = applicationModel;
	}

	public ApplicationModel getApplicationModel() {
		return this.applicationModel;
	}

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

	public List getApplicationModelListeners() {
		return this.applicationModelListeners;
	}

}
