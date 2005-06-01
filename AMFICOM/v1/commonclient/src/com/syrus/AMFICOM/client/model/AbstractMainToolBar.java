/*-
 * $Id: AbstractMainToolBar.java,v 1.3 2005/06/01 06:22:31 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.client.resource.LangModel;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/01 06:22:31 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler_v1
 */
public abstract class AbstractMainToolBar extends JToolBar implements ApplicationModelListener {

	protected ApplicationModel	aModel;

	protected ActionListener	actionListener;

	private JButton				sessionOpen;

	public AbstractMainToolBar() {
		this.sessionOpen = new JButton();
		this.sessionOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_SESSION));
		this.sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		this.sessionOpen.setName(AbstractMainMenuBar.MENU_SESSION_NEW);
		this.sessionOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.actionListener = new ActionListener() {

			private boolean	executed	= false;

			public void actionPerformed(ActionEvent e) {
				ApplicationModel model = AbstractMainToolBar.this.getModel();
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

		add(this.sessionOpen);
		addSeparator();
	}

	public void setModel(ApplicationModel aModel) {
		this.aModel = aModel;
	}

	public ApplicationModel getModel() {
		return this.aModel;
	}

	public void modelChanged(String e[]) {
		this.modelChanged("");
	}

	public void modelChanged(String elementName) {
		this.sessionOpen.setVisible(this.aModel.isVisible(AbstractMainMenuBar.MENU_SESSION_NEW));
		this.sessionOpen.setEnabled(this.aModel.isEnabled(AbstractMainMenuBar.MENU_SESSION_NEW));
	}
}
