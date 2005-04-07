package com.syrus.AMFICOM.Client.Schedule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;

public class ScheduleMainToolBar extends JToolBar implements ApplicationModelListener {

	ApplicationModel	aModel;

	private JButton		sessionOpen;

	//public static final int IMAGE_SIZE = 16;

	public ScheduleMainToolBar() {
		this.sessionOpen = new JButton();
		this.sessionOpen.setIcon(UIManager.getIcon(ResourceKeys.ICON_OPEN_SESSION));
		this.sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		this.sessionOpen.setName(ScheduleMainMenuBar.MENU_SESSION_NEW);
		this.sessionOpen.setMargin(UIManager.getInsets(ResourceKeys.INSETS_ICONED_BUTTON));
		this.sessionOpen.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (ScheduleMainToolBar.this.aModel == null)
					return;
				AbstractButton jb = (AbstractButton) e.getSource();
				String s = jb.getName();
				Command command = ScheduleMainToolBar.this.aModel.getCommand(s);
				command = (Command) command.clone();
				command.execute();
			}
		});

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
		this.sessionOpen.setVisible(this.aModel.isVisible(ScheduleMainMenuBar.MENU_SESSION_NEW));
		this.sessionOpen.setEnabled(this.aModel.isEnabled(ScheduleMainMenuBar.MENU_SESSION_NEW));
	}

}