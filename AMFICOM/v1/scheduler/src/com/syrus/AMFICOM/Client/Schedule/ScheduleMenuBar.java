package com.syrus.AMFICOM.Client.Schedule;

// Copyright (c) Syrus Systems 2000 Syrus Systems
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class ScheduleMenuBar extends JMenuBar
		implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionSave = new JMenuItem();
	JMenuItem menuSessionUndo = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JMenu menuView = new JMenu();
	JMenuItem menuViewPlan = new JMenuItem();
	JMenuItem menuViewTable = new JMenuItem();
	JMenuItem menuViewTree = new JMenuItem();
	JMenuItem menuViewTime = new JMenuItem();
	JMenuItem menuViewParam = new JMenuItem();
	JMenuItem menuViewSave = new JMenuItem();
	JMenuItem menuViewScheme = new JMenuItem();
	JMenuItem menuViewMap = new JMenuItem();
	JMenuItem menuViewAll = new JMenuItem();

	JMenu menuHelp = new JMenu();
	JMenuItem menuHelpContents = new JMenuItem();
	JMenuItem menuHelpFind = new JMenuItem();
	JMenuItem menuHelpTips = new JMenuItem();
	JMenuItem menuHelpStart = new JMenuItem();
	JMenuItem menuHelpCourse = new JMenuItem();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpSupport = new JMenuItem();
	JMenuItem menuHelpLicense = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();

	public ScheduleMenuBar()
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

	public ScheduleMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		ScheduleMenuBar_this_actionAdapter actionAdapter =
				new ScheduleMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModelScheduleOld.Text("menuSession"));
		menuSession.setName("menuSession");
//		System.out.println(LangModelScheduleOld.Text("menuSession"));
		menuSessionNew.setText(LangModelScheduleOld.Text("menuSessionNew"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModelScheduleOld.Text("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModelScheduleOld.Text("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModelScheduleOld.Text("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(
				LangModelScheduleOld.Text("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionSave.setText(LangModelScheduleOld.Text("menuSessionSave"));
		menuSessionSave.setName("menuSessionSave");
		menuSessionSave.addActionListener(actionAdapter);
		menuSessionUndo.setText(LangModelScheduleOld.Text("menuSessionUndo"));
		menuSessionUndo.setName("menuSessionUndo");
		menuSessionUndo.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.Text("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuExit.setText(LangModelScheduleOld.Text("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuView.setText(LangModelScheduleOld.Text("menuView"));
		menuView.setName("menuView");

		menuViewPlan.setText(LangModelScheduleOld.Text("menuViewPlan"));
		menuViewPlan.setName("menuViewPlan");
		menuViewPlan.addActionListener(actionAdapter);

		menuViewTable.setText(LangModelScheduleOld.Text("menuViewTable"));
		menuViewTable.setName("menuViewTable");
		menuViewTable.addActionListener(actionAdapter);

		menuViewTree.setText(LangModelScheduleOld.Text("menuViewTree"));
		menuViewTree.setName("menuViewTree");
		menuViewTree.addActionListener(actionAdapter);

		menuViewTime.setText(LangModelScheduleOld.Text("menuViewTime"));
		menuViewTime.setName("menuViewTime");
		menuViewTime.addActionListener(actionAdapter);

		menuViewParam.setText(LangModelScheduleOld.Text("menuViewParam"));
		menuViewParam.setName("menuViewParam");
		menuViewParam.addActionListener(actionAdapter);

		menuViewSave.setText(LangModelScheduleOld.Text("menuViewSave"));
		menuViewSave.setName("menuViewSave");
		menuViewSave.addActionListener(actionAdapter);

		menuViewScheme.setText(LangModelScheduleOld.Text("menuViewScheme"));
		menuViewScheme.setName("menuViewScheme");
		menuViewScheme.addActionListener(actionAdapter);

		menuViewMap.setText(LangModelScheduleOld.Text("menuViewMap"));
		menuViewMap.setName("menuViewMap");
		menuViewMap.addActionListener(actionAdapter);

		menuViewAll.setText(LangModelScheduleOld.Text("menuViewAll"));
		menuViewAll.setName("menuViewAll");
		menuViewAll.addActionListener(actionAdapter);

		menuHelp.setText(LangModelScheduleOld.Text("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModelScheduleOld.Text("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModelScheduleOld.Text("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModelScheduleOld.Text("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModelScheduleOld.Text("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModelScheduleOld.Text("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModelScheduleOld.Text("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModelScheduleOld.Text("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModelScheduleOld.Text("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModelScheduleOld.Text("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
		//menuSession.addSeparator();
		menuSession.add(menuSessionSave);
		menuSession.add(menuSessionUndo);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuView.add(menuViewPlan);
		menuView.add(menuViewTable);
		menuView.add(menuViewTree);
		menuView.add(menuViewTime);
		menuView.add(menuViewParam);
		menuView.add(menuViewSave);
		menuView.add(menuViewScheme);
		menuView.add(menuViewMap);
		menuView.addSeparator();
		menuView.add(menuViewAll);

		menuHelp.add(menuHelpContents);
		menuHelp.add(menuHelpFind);
		menuHelp.add(menuHelpTips);
		menuHelp.add(menuHelpStart);
		menuHelp.add(menuHelpCourse);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpSupport);
		menuHelp.add(menuHelpLicense);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);

		this.add(menuSession);
		this.add(menuView);
		this.add(menuHelp);
	}

	public void setModel(ApplicationModel a)
	{
		aModel = a;
	}

	public ApplicationModel getModel()
	{
		return aModel;
	}

	public void modelChanged(String e[])
	{
		int count = e.length;
		int i;

		//System.out.println("changed model in menu bar");

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

		menuSessionSave.setVisible(aModel.isVisible("menuSessionSave"));
		menuSessionSave.setEnabled(aModel.isEnabled("menuSessionSave"));

		menuSessionUndo.setVisible(aModel.isVisible("menuSessionUndo"));
		menuSessionUndo.setEnabled(aModel.isEnabled("menuSessionUndo"));

		menuSessionDomain.setVisible(aModel.isVisible("menuSessionDomain"));
		menuSessionDomain.setEnabled(aModel.isEnabled("menuSessionDomain"));

		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));

		menuViewPlan.setVisible(aModel.isVisible("menuViewPlan"));
		menuViewPlan.setEnabled(aModel.isEnabled("menuViewPlan"));

		menuViewTable.setVisible(aModel.isVisible("menuViewTable"));
		menuViewTable.setEnabled(aModel.isEnabled("menuViewTable"));

		menuViewTree.setVisible(aModel.isVisible("menuViewTree"));
		menuViewTree.setEnabled(aModel.isEnabled("menuViewTree"));

		menuViewTime.setVisible(aModel.isVisible("menuViewTime"));
		menuViewTime.setEnabled(aModel.isEnabled("menuViewTime"));

		menuViewParam.setVisible(aModel.isVisible("menuViewParam"));
		menuViewParam.setEnabled(aModel.isEnabled("menuViewParam"));

		menuViewSave.setVisible(aModel.isVisible("menuViewSave"));
		menuViewSave.setEnabled(aModel.isEnabled("menuViewSave"));

		menuViewScheme.setVisible(aModel.isVisible("menuViewScheme"));
		menuViewScheme.setEnabled(aModel.isEnabled("menuViewScheme"));

		menuViewMap.setVisible(aModel.isVisible("menuViewMap"));
		menuViewMap.setEnabled(aModel.isEnabled("menuViewMap"));

		menuViewAll.setVisible(aModel.isVisible("menuViewAll"));
		menuViewAll.setEnabled(aModel.isEnabled("menuViewAll"));

		menuHelp.setVisible(aModel.isVisible("menuHelp"));
		menuHelp.setEnabled(aModel.isEnabled("menuHelp"));

		menuHelpContents.setVisible(aModel.isVisible("menuHelpContents"));
		menuHelpContents.setEnabled(aModel.isEnabled("menuHelpContents"));

		menuHelpFind.setVisible(aModel.isVisible("menuHelpFind"));
		menuHelpFind.setEnabled(aModel.isEnabled("menuHelpFind"));

		menuHelpTips.setVisible(aModel.isVisible("menuHelpTips"));
		menuHelpTips.setEnabled(aModel.isEnabled("menuHelpTips"));

		menuHelpStart.setVisible(aModel.isVisible("menuHelpStart"));
		menuHelpStart.setEnabled(aModel.isEnabled("menuHelpStart"));

		menuHelpCourse.setVisible(aModel.isVisible("menuHelpCourse"));
		menuHelpCourse.setEnabled(aModel.isEnabled("menuHelpCourse"));

		menuHelpHelp.setVisible(aModel.isVisible("menuHelpHelp"));
		menuHelpHelp.setEnabled(aModel.isEnabled("menuHelpHelp"));

		menuHelpSupport.setVisible(aModel.isVisible("menuHelpSupport"));
		menuHelpSupport.setEnabled(aModel.isEnabled("menuHelpSupport"));

		menuHelpLicense.setVisible(aModel.isVisible("menuHelpLicense"));
		menuHelpLicense.setEnabled(aModel.isEnabled("menuHelpLicense"));

		menuHelpAbout.setVisible(aModel.isVisible("menuHelpAbout"));
		menuHelpAbout.setEnabled(aModel.isEnabled("menuHelpAbout"));
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

class ScheduleMenuBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	ScheduleMenuBar adaptee;

	ScheduleMenuBar_this_actionAdapter(ScheduleMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

