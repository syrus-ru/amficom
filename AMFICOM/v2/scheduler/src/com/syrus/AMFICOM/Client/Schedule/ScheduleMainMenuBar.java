package com.syrus.AMFICOM.Client.Schedule;

import java.awt.event.ActionEvent;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ScheduleMainMenuBar extends JMenuBar implements
		ApplicationModelListener {

	private ApplicationModel	aModel;

	JMenu						menuSession					= new JMenu();
	JMenuItem					menuSessionNew				= new JMenuItem();
	JMenuItem					menuSessionClose			= new JMenuItem();
	JMenuItem					menuSessionOptions			= new JMenuItem();
	JMenuItem					menuSessionConnection		= new JMenuItem();
	JMenuItem					menuSessionChangePassword	= new JMenuItem();
	JMenuItem					menuSessionDomain			= new JMenuItem();
	JMenuItem					menuExit					= new JMenuItem();

	public ScheduleMainMenuBar() {
		super();
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ScheduleMainMenuBar(ApplicationModel aModel) {
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception {
		ScheduleMainMenuBar_this_actionAdapter actionAdapter = new ScheduleMainMenuBar_this_actionAdapter(
				this);

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
		this.menuSessionConnection.setText(LangModel
				.Text("menuSessionConnection"));
		this.menuSessionConnection.setName("menuSessionConnection");
		this.menuSessionConnection.addActionListener(actionAdapter);
		this.menuSessionChangePassword.setText(LangModel
				.Text("menuSessionChangePassword"));
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

	public void modelChanged(String e[]) {
		this.menuSession.setVisible(this.aModel.isVisible("menuSession"));
		this.menuSession.setEnabled(this.aModel.isEnabled("menuSession"));
		this.menuSessionNew.setVisible(this.aModel.isVisible("menuSessionNew"));
		this.menuSessionNew.setEnabled(this.aModel.isEnabled("menuSessionNew"));
		this.menuSessionClose.setVisible(this.aModel
				.isVisible("menuSessionClose"));
		this.menuSessionClose.setEnabled(this.aModel
				.isEnabled("menuSessionClose"));
		this.menuSessionOptions.setVisible(this.aModel
				.isVisible("menuSessionOptions"));
		this.menuSessionOptions.setEnabled(this.aModel
				.isEnabled("menuSessionOptions"));
		this.menuSessionConnection.setVisible(this.aModel
				.isVisible("menuSessionConnection"));
		this.menuSessionConnection.setEnabled(this.aModel
				.isEnabled("menuSessionConnection"));
		this.menuSessionChangePassword.setVisible(this.aModel
				.isVisible("menuSessionChangePassword"));
		this.menuSessionChangePassword.setEnabled(this.aModel
				.isEnabled("menuSessionChangePassword"));
		this.menuSessionDomain.setVisible(this.aModel
				.isVisible("menuSessionDomain"));
		this.menuSessionDomain.setEnabled(this.aModel
				.isEnabled("menuSessionDomain"));
	}

	public void setModel(ApplicationModel aModel) {
		this.aModel = aModel;
	}

	public void this_actionPerformed(ActionEvent e) {
		if (this.aModel == null) return;
		AbstractButton jb = (AbstractButton) e.getSource();
		String s = jb.getName();
		Command command = this.aModel.getCommand(s);
		command = (Command) command.clone();
		command.execute();
	}
}

class ScheduleMainMenuBar_this_actionAdapter implements
		java.awt.event.ActionListener {

	ScheduleMainMenuBar	adaptee;

	ScheduleMainMenuBar_this_actionAdapter(ScheduleMainMenuBar adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		this.adaptee.this_actionPerformed(e);
	}
}