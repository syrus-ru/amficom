package com.syrus.AMFICOM.Client.Schedule;

import java.awt.event.ActionEvent;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;

public class ScheduleMainMenuBar extends JMenuBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	public ScheduleMainMenuBar()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ScheduleMainMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		ScheduleMainMenuBar_this_actionAdapter actionAdapter =
				new ScheduleMainMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.Text("menuSession"));
		menuSession.setName("menuSession");
		menuSessionNew.setText(LangModel.Text("menuSessionNew"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModel.Text("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModel.Text("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModel.Text("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(LangModel.Text("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.Text("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuExit.setText(LangModel.Text("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);


		this.add(menuSession);
	}

	public void modelChanged(String e[])
	{
		menuSession.setVisible(aModel.isVisible("menuSession"));
		menuSession.setEnabled(aModel.isEnabled("menuSession"));
		menuSessionNew.setVisible(aModel.isVisible("menuSessionNew"));
		menuSessionNew.setEnabled(aModel.isEnabled("menuSessionNew"));
		menuSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
		menuSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));
		menuSessionOptions.setVisible(aModel.isVisible("menuSessionOptions"));
		menuSessionOptions.setEnabled(aModel.isEnabled("menuSessionOptions"));
		menuSessionConnection.setVisible(aModel.isVisible("menuSessionConnection"));
		menuSessionConnection.setEnabled(aModel.isEnabled("menuSessionConnection"));
		menuSessionChangePassword.setVisible(aModel.isVisible("menuSessionChangePassword"));
		menuSessionChangePassword.setEnabled(aModel.isEnabled("menuSessionChangePassword"));
		menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
		menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));
	}

	public void setModel (ApplicationModel aModel)
	{
		this.aModel = aModel;
	}

	public void this_actionPerformed(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
		command = (Command )command.clone();
		command.execute();
	}
}

class ScheduleMainMenuBar_this_actionAdapter implements java.awt.event.ActionListener
{
	ScheduleMainMenuBar adaptee;

	ScheduleMainMenuBar_this_actionAdapter(ScheduleMainMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}
