package com.syrus.AMFICOM.Client.ReportBuilder;

import java.awt.event.ActionEvent;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractButton;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey;

import com.syrus.AMFICOM.Client.General.Model.ApplicationModel;
import com.syrus.AMFICOM.Client.General.Model.ApplicationModelListener;

/**
 * <p>Description: Меню для работы с шаблонами</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Syrus Systems</p>
 * @author Песковский Пётр
 * @version 0.1
 */

public class ReportMenuBar extends JMenuBar
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

	JMenu menuWindow = new JMenu();
	JMenuItem menuWindowClose = new JMenuItem();
	JMenuItem menuWindowCloseAll = new JMenuItem();
	JMenuItem menuWindowTileHorizontal = new JMenuItem();
	JMenuItem menuWindowTileVertical = new JMenuItem();
	JMenuItem menuWindowCascade = new JMenuItem();
	JMenuItem menuWindowArrange = new JMenuItem();
	JMenuItem menuWindowArrangeIcons = new JMenuItem();
	JMenuItem menuWindowMinimizeAll = new JMenuItem();
	JMenuItem menuWindowRestoreAll = new JMenuItem();
	JMenuItem menuWindowList = new JMenuItem();

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

	public ReportMenuBar()
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

	public ReportMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		TemplateMenuBar_this_actionAdapter actionAdapter =
				new TemplateMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.getString("menuSession"));
		menuSession.setName("menuSession");
		menuSessionNew.setText(LangModelSurvey.getString("NewSession"));
		menuSessionNew.setName("menuSessionNew");
		menuSessionNew.addActionListener(actionAdapter);
		menuSessionClose.setText(LangModel.getString("menuSessionClose"));
		menuSessionClose.setName("menuSessionClose");
		menuSessionClose.addActionListener(actionAdapter);
		menuSessionOptions.setText(LangModel.getString("menuSessionOptions"));
		menuSessionOptions.setName("menuSessionOptions");
		menuSessionOptions.addActionListener(actionAdapter);
		menuSessionConnection.setText(LangModel.getString("menuSessionConnection"));
		menuSessionConnection.setName("menuSessionConnection");
		menuSessionConnection.addActionListener(actionAdapter);
		menuSessionChangePassword.setText(LangModel.getString("menuSessionChangePassword"));
		menuSessionChangePassword.setName("menuSessionChangePassword");
		menuSessionChangePassword.addActionListener(actionAdapter);
		menuSessionSave.setText(LangModel.getString("menuSessionSave"));
		menuSessionSave.setName("menuSessionSave");
		menuSessionSave.addActionListener(actionAdapter);
		menuSessionUndo.setText(LangModel.getString("menuSessionUndo"));
		menuSessionUndo.setName("menuSessionUndo");
		menuSessionUndo.addActionListener(actionAdapter);
		menuSessionDomain.setText(LangModel.getString("menuSessionDomain"));
		menuSessionDomain.setName("menuSessionDomain");
		menuSessionDomain.addActionListener(actionAdapter);
		menuExit.setText(LangModel.getString("menuExit"));
		menuExit.setName("menuExit");
		menuExit.addActionListener(actionAdapter);

		menuWindow.setText(LangModelSurvey.getString("Window"));
		menuWindow.setName("Window");
		menuWindowClose.setText(LangModelSurvey.getString("Close"));
		menuWindowClose.setName("Close");
		menuWindowClose.addActionListener(actionAdapter);
		menuWindowCloseAll.setText(LangModelSurvey.getString("CloseAll"));
		menuWindowCloseAll.setName("CloseAll");
		menuWindowCloseAll.addActionListener(actionAdapter);
		menuWindowTileHorizontal.setText(LangModelSurvey.getString("TileHorizontal"));
		menuWindowTileHorizontal.setName("TileHorizontal");
		menuWindowTileHorizontal.addActionListener(actionAdapter);
		menuWindowTileVertical.setText(LangModelSurvey.getString("TileVertical"));
		menuWindowTileVertical.setName("TileVertical");
		menuWindowTileVertical.addActionListener(actionAdapter);
		menuWindowCascade.setText(LangModelSurvey.getString("Cascade"));
		menuWindowCascade.setName("Cascade");
		menuWindowCascade.addActionListener(actionAdapter);
		menuWindowArrange.setText(LangModelSurvey.getString("Arrange"));
		menuWindowArrange.setName("Arrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowArrangeIcons.setText(LangModelSurvey.getString("ArrangeIcons"));
		menuWindowArrangeIcons.setName("ArrangeIcons");
		menuWindowArrangeIcons.addActionListener(actionAdapter);
		menuWindowMinimizeAll.setText(LangModelSurvey.getString("MinimizeAll"));
		menuWindowMinimizeAll.setName("MinimizeAll");
		menuWindowMinimizeAll.addActionListener(actionAdapter);
		menuWindowRestoreAll.setText(LangModelSurvey.getString("RestoreAll"));
		menuWindowRestoreAll.setName("RestoreAll");
		menuWindowRestoreAll.addActionListener(actionAdapter);
		menuWindowList.setText(LangModelSurvey.getString("List"));
		menuWindowList.setName("List");
		menuWindowList.addActionListener(actionAdapter);

		menuHelp.setText(LangModel.getString("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModel.getString("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModel.getString("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModel.getString("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModel.getString("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModel.getString("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModel.getString("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModel.getString("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModel.getString("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModel.getString("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

		menuSession.add(menuSessionNew);
		menuSession.add(menuSessionClose);
		menuSession.add(menuSessionOptions);
		menuSession.add(menuSessionChangePassword);
		menuSession.addSeparator();
		menuSession.add(menuSessionConnection);
//		menuSession.addSeparator();
//		menuSession.add(menuSessionSave);
//		menuSession.add(menuSessionUndo);
//		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuWindow.add(menuWindowClose);
		menuWindow.add(menuWindowCloseAll);
		menuWindow.addSeparator();
		menuWindow.add(menuWindowTileHorizontal);
		menuWindow.add(menuWindowTileVertical);
		menuWindow.add(menuWindowCascade);
		menuWindow.add(menuWindowArrange);
		menuWindow.add(menuWindowArrangeIcons);
		menuWindow.add(menuWindowMinimizeAll);
		menuWindow.add(menuWindowRestoreAll);
		menuWindow.addSeparator();
		menuWindow.addSeparator();
		menuWindow.add(menuWindowList);

		menuHelp.add(menuHelpContents);
		menuHelp.add(menuHelpFind);
		menuHelp.add(menuHelpTips);
		menuHelp.add(menuHelpStart);
		menuHelp.add(menuHelpCourse);
//		menuHelp.addSeparator();
		menuHelp.add(menuHelpHelp);
//		menuHelp.addSeparator();
		menuHelp.add(menuHelpSupport);
		menuHelp.add(menuHelpLicense);
//		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);

		this.add(menuSession);
		this.add(menuWindow);
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

//    System.out.println("changed model in menu bar");

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

		menuWindow.setVisible(aModel.isVisible("menuWindow"));
		menuWindow.setEnabled(aModel.isEnabled("menuWindow"));

		menuWindowClose.setVisible(aModel.isVisible("menuWindowClose"));
		menuWindowClose.setEnabled(aModel.isEnabled("menuWindowClose"));

		menuWindowCloseAll.setVisible(aModel.isVisible("menuWindowCloseAll"));
		menuWindowCloseAll.setEnabled(aModel.isEnabled("menuWindowCloseAll"));

		menuWindowTileHorizontal.setVisible(aModel.isVisible("menuWindowTileHorizontal"));
		menuWindowTileHorizontal.setEnabled(aModel.isEnabled("menuWindowTileHorizontal"));

		menuWindowTileVertical.setVisible(aModel.isVisible("menuWindowTileVertical"));
		menuWindowTileVertical.setEnabled(aModel.isEnabled("menuWindowTileVertical"));

		menuWindowCascade.setVisible(aModel.isVisible("menuWindowCascade"));
		menuWindowCascade.setEnabled(aModel.isEnabled("menuWindowCascade"));

		menuWindowArrange.setVisible(aModel.isVisible("menuWindowArrange"));
		menuWindowArrange.setEnabled(aModel.isEnabled("menuWindowArrange"));

		menuWindowArrangeIcons.setVisible(aModel.isVisible("menuWindowArrangeIcons"));
		menuWindowArrangeIcons.setEnabled(aModel.isEnabled("menuWindowArrangeIcons"));

		menuWindowMinimizeAll.setVisible(aModel.isVisible("menuWindowMinimizeAll"));
		menuWindowMinimizeAll.setEnabled(aModel.isEnabled("menuWindowMinimizeAll"));

		menuWindowRestoreAll.setVisible(aModel.isVisible("menuWindowRestoreAll"));
		menuWindowRestoreAll.setEnabled(aModel.isEnabled("menuWindowRestoreAll"));

		menuWindowList.setVisible(aModel.isVisible("menuWindowList"));
		menuWindowList.setEnabled(aModel.isEnabled("menuWindowList"));

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

class TemplateMenuBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	ReportMenuBar adaptee;

	TemplateMenuBar_this_actionAdapter(ReportMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}
