/*-
 * $Id: AbstractMainToolBar.java,v 1.5 2005/06/01 09:54:26 bob Exp $
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

import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/06/01 09:54:26 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainToolBar extends JToolBar {

	protected ApplicationModel	applicationModel;

	protected ActionListener	actionListener;

	protected List				applicationModelListeners;

	JButton				sessionOpen;

	public AbstractMainToolBar() {
		this.sessionOpen = new JButton();
		this.sessionOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_SESSION));
		this.sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		this.sessionOpen.setName(AbstractMainMenuBar.MENU_SESSION_NEW);
		this.sessionOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
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
				// command = (Command) command.clone();
				Log.debugMessage(".actionPerformed | command " + command, Log.FINEST);
				command.execute();
				this.executed = false;
			}
		};
		this.sessionOpen.addActionListener(this.actionListener);

		this.add(this.sessionOpen);
		this.addSeparator();

		this.addApplicationModelListener(new ApplicationModelListener() {

			public void modelChanged(String e[]) {
				this.modelChanged("");
			}

			public void modelChanged(String elementName) {
				AbstractMainToolBar.this.sessionOpen.setVisible(AbstractMainToolBar.this.getApplicationModel().isVisible(
					AbstractMainMenuBar.MENU_SESSION_NEW));
				AbstractMainToolBar.this.sessionOpen.setEnabled(AbstractMainToolBar.this.getApplicationModel().isEnabled(
					AbstractMainMenuBar.MENU_SESSION_NEW));
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
