package com.syrus.AMFICOM.Client.Schedule;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;

public class ScheduleMainMenuBar extends JMenuBar implements ApplicationModelListener {

	ApplicationModel	aModel;

	JMenu						menuSession					= new JMenu();
	JMenuItem					menuSessionNew				= new JMenuItem();
	JMenuItem					menuSessionClose			= new JMenuItem();
	JMenuItem					menuSessionOptions			= new JMenuItem();
	JMenuItem					menuSessionConnection		= new JMenuItem();
	JMenuItem					menuSessionChangePassword	= new JMenuItem();
	JMenuItem					menuSessionDomain			= new JMenuItem();
	JMenuItem					menuExit					= new JMenuItem();

	public ScheduleMainMenuBar() {

		ActionListener actionAdapter = new ActionListener(){			
			public void actionPerformed(ActionEvent e) {
				if (ScheduleMainMenuBar.this.aModel == null)
					return;
				AbstractButton jb = (AbstractButton) e.getSource();
				String s = jb.getName();
				Command command = ScheduleMainMenuBar.this.aModel.getCommand(s);
				command = (Command) command.clone();
				command.execute();
			}
		};

		this.menuSession.setText(LangModel.Text("menuSession"));
		this.menuSession.setName("menuSession");
		this.menuSessionNew.setText(LangModel.Text("menuSessionNew"));
		this.menuSessionNew.setName("menuSessionNew");
		this.menuSessionNew.addActionListener(actionAdapter);
		this.menuSessionClose.setText(LangModel.Text("menuSessionClose"));
		this.menuSessionClose.setName("menuSessionClose");
		this.menuSessionClose.addActionListener(actionAdapter);
		this.menuSessionOptions.setText(LangModel.Text("menuSessionOptions"));
		this.menuSessionOptions.setName("menuSessionOptions");
		this.menuSessionOptions.addActionListener(actionAdapter);
		this.menuSessionConnection.setText(LangModel.Text("menuSessionConnection"));
		this.menuSessionConnection.setName("menuSessionConnection");
		this.menuSessionConnection.addActionListener(actionAdapter);
		this.menuSessionChangePassword.setText(LangModel.Text("menuSessionChangePassword"));
		this.menuSessionChangePassword.setName("menuSessionChangePassword");
		this.menuSessionChangePassword.addActionListener(actionAdapter);
		this.menuSessionDomain.setText(LangModel.Text("menuSessionDomain"));
		this.menuSessionDomain.setName("menuSessionDomain");
		this.menuSessionDomain.addActionListener(actionAdapter);
		this.menuExit.setText(LangModel.Text("menuExit"));
		this.menuExit.setName("menuExit");
		this.menuExit.addActionListener(actionAdapter);

		this.menuSession.add(this.menuSessionNew);
		this.menuSession.add(this.menuSessionClose);
		this.menuSession.add(this.menuSessionOptions);
		this.menuSession.add(this.menuSessionChangePassword);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionConnection);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuSessionDomain);
		this.menuSession.addSeparator();
		this.menuSession.add(this.menuExit);

		this.add(this.menuSession);
	}

	public ScheduleMainMenuBar(ApplicationModel aModel) {
		this();
		this.aModel = aModel;
	}

	public void modelChanged(String e[]) {
		this.menuSession.setVisible(this.aModel.isVisible("menuSession"));
		this.menuSession.setEnabled(this.aModel.isEnabled("menuSession"));
		this.menuSessionNew.setVisible(this.aModel.isVisible("menuSessionNew"));
		this.menuSessionNew.setEnabled(this.aModel.isEnabled("menuSessionNew"));
		this.menuSessionClose.setVisible(this.aModel.isVisible("menuSessionClose"));
		this.menuSessionClose.setEnabled(this.aModel.isEnabled("menuSessionClose"));
		this.menuSessionOptions.setVisible(this.aModel.isVisible("menuSessionOptions"));
		this.menuSessionOptions.setEnabled(this.aModel.isEnabled("menuSessionOptions"));
		this.menuSessionConnection.setVisible(this.aModel.isVisible("menuSessionConnection"));
		this.menuSessionConnection.setEnabled(this.aModel.isEnabled("menuSessionConnection"));
		this.menuSessionChangePassword.setVisible(this.aModel.isVisible("menuSessionChangePassword"));
		this.menuSessionChangePassword.setEnabled(this.aModel.isEnabled("menuSessionChangePassword"));
		this.menuSessionDomain.setVisible(this.aModel.isVisible("menuSessionDomain"));
		this.menuSessionDomain.setEnabled(this.aModel.isEnabled("menuSessionDomain"));
	}

	public void setModel(ApplicationModel aModel) {
		this.aModel = aModel;
	}
}
