package com.syrus.AMFICOM.Client.Schedule;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;

public class ScheduleMainToolBar extends JToolBar implements ApplicationModelListener {

	ApplicationModel	aModel;

	JButton						sessionOpen	= new JButton();

	public static final int		IMAGE_SIZE	= 16;

	public ScheduleMainToolBar() {

		sessionOpen.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/open_session.gif")
				.getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
		UIStorage.setRigidSize(sessionOpen,UIStorage.BUTTON_SIZE);
		sessionOpen.setToolTipText(LangModel.getString("menuSessionNew"));
		sessionOpen.setName("menuSessionNew");
		sessionOpen.addActionListener(new ActionListener() {

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

		add(sessionOpen);
		addSeparator();
	}

	public void setModel(ApplicationModel aModel) {
		this.aModel = aModel;
	}

	public ApplicationModel getModel() {
		return aModel;
	}

	public void modelChanged(String e[]) {
		sessionOpen.setVisible(aModel.isVisible("menuSessionNew"));
		sessionOpen.setEnabled(aModel.isEnabled("menuSessionNew"));
	}

}
