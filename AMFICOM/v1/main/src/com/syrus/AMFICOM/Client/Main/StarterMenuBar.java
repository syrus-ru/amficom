//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Панель меню окна запуска модулей ПО АМФИКОМ                * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        Main\StarterMenuBar.java                                      * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Main;

import javax.swing.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class StarterMenuBar extends JMenuBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JMenu menuSession = new JMenu();
	JMenu menuView = new JMenu();
	JMenu menuTools = new JMenu();
	JMenu menuHelp = new JMenu();

	JMenuItem menuSessionNew = new JMenuItem();
	JMenuItem menuSessionClose = new JMenuItem();
	JMenuItem menuSessionOptions = new JMenuItem();
	JMenuItem menuSessionConnection = new JMenuItem();
	JMenuItem menuSessionChangePassword = new JMenuItem();
	JMenuItem menuSessionSave = new JMenuItem();
	JMenuItem menuSessionUndo = new JMenuItem();
	JMenuItem menuSessionDomain = new JMenuItem();
	JMenuItem menuExit = new JMenuItem();

	JCheckBoxMenuItem menuViewPanel = new JCheckBoxMenuItem();

	JMenuItem menuToolsAdmin = new JMenuItem();
	JMenuItem menuToolsConfig = new JMenuItem();
	JMenuItem menuToolsComponents = new JMenuItem();
	JMenuItem menuToolsScheme = new JMenuItem();
	JMenuItem menuToolsMap = new JMenuItem();
	JMenuItem menuToolsTrace = new JMenuItem();
	JMenuItem menuToolsSchedule = new JMenuItem();
	JMenuItem menuToolsSurvey = new JMenuItem();
	JMenuItem menuToolsModel = new JMenuItem();
	JMenuItem menuToolsMonitor = new JMenuItem();
	JMenuItem menuToolsAnalyse = new JMenuItem();
	JMenuItem menuToolsNorms = new JMenuItem();
	JMenuItem menuToolsPrognosis = new JMenuItem();
	JMenuItem menuToolsMaintain = new JMenuItem();
	JMenuItem menuToolsReportBuilder = new JMenuItem();

	JMenuItem menuHelpContents = new JMenuItem();
	JMenuItem menuHelpFind = new JMenuItem();
	JMenuItem menuHelpTips = new JMenuItem();
	JMenuItem menuHelpStart = new JMenuItem();
	JMenuItem menuHelpCourse = new JMenuItem();
	JMenuItem menuHelpHelp = new JMenuItem();
	JMenuItem menuHelpSupport = new JMenuItem();
	JMenuItem menuHelpLicense = new JMenuItem();
	JMenuItem menuHelpAbout = new JMenuItem();

	public StarterMenuBar()
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

	public StarterMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		StarterMenuBar_this_actionAdapter actionAdapter =
				new StarterMenuBar_this_actionAdapter(this);

		menuSession.setText(LangModel.getString("menuSession"));
		menuSession.setName("menuSession");
		menuSessionNew.setText(LangModel.getString("menuSessionNew"));
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

		menuView.setText(LangModelMain.getString("menuView"));
		menuView.setName("menuView");
		menuViewPanel.setEnabled(true);
		menuViewPanel.setText(LangModelMain.getString("menuViewPanel"));
		menuViewPanel.setName("menuViewPanel");
		menuViewPanel.addActionListener(actionAdapter);

		menuTools.setText(LangModelMain.getString("menuTools"));
		menuTools.setName("menuTools");
		menuToolsAdmin.setText(LangModelMain.getString("menuToolsAdmin"));
		menuToolsAdmin.setName("menuToolsAdmin");
		menuToolsAdmin.addActionListener(actionAdapter);
		menuToolsConfig.setText(LangModelMain.getString("menuToolsConfig"));
		menuToolsConfig.setName("menuToolsConfig");
		menuToolsConfig.addActionListener(actionAdapter);
		menuToolsComponents.setText(LangModelMain.getString("menuToolsComponents"));
		menuToolsComponents.setName("menuToolsComponents");
		menuToolsComponents.addActionListener(actionAdapter);
		menuToolsScheme.setText(LangModelMain.getString("menuToolsScheme"));
		menuToolsScheme.setName("menuToolsScheme");
		menuToolsScheme.addActionListener(actionAdapter);
		menuToolsMap.setText(LangModelMain.getString("menuToolsMap"));
		menuToolsMap.setName("menuToolsMap");
		menuToolsMap.addActionListener(actionAdapter);
		menuToolsTrace.setText(LangModelMain.getString("menuToolsTrace"));
		menuToolsTrace.setName("menuToolsTrace");
		menuToolsTrace.addActionListener(actionAdapter);
		menuToolsSchedule.setText(LangModelMain.getString("menuToolsSchedule"));
		menuToolsSchedule.setName("menuToolsSchedule");
		menuToolsSchedule.addActionListener(actionAdapter);
		menuToolsSurvey.setText(LangModelMain.getString("menuToolsSurvey"));
		menuToolsSurvey.setName("menuToolsSurvey");
		menuToolsSurvey.addActionListener(actionAdapter);
		menuToolsModel.setText(LangModelMain.getString("menuToolsModel"));
		menuToolsModel.setName("menuToolsModel");
		menuToolsModel.addActionListener(actionAdapter);
		menuToolsMonitor.setText(LangModelMain.getString("menuToolsMonitor"));
		menuToolsMonitor.setName("menuToolsMonitor");
		menuToolsMonitor.addActionListener(actionAdapter);
		menuToolsAnalyse.setText(LangModelMain.getString("menuToolsAnalyse"));
		menuToolsAnalyse.setName("menuToolsAnalyse");
		menuToolsAnalyse.addActionListener(actionAdapter);
		menuToolsNorms.setText(LangModelMain.getString("menuToolsNorms"));
		menuToolsNorms.setName("menuToolsNorms");
		menuToolsNorms.addActionListener(actionAdapter);
		menuToolsMaintain.setText(LangModelMain.getString("menuToolsMaintain"));
		menuToolsMaintain.setName("menuToolsMaintain");
		menuToolsMaintain.addActionListener(actionAdapter);
		menuToolsPrognosis.setText(LangModelMain.getString("menuToolsPrognosis"));
		menuToolsPrognosis.setName("menuToolsPrognosis");
		menuToolsPrognosis.addActionListener(actionAdapter);
		menuToolsReportBuilder.setText(LangModelMain.getString("menuToolsReportBuilder"));
		menuToolsReportBuilder.setName("menuToolsReportBuilder");
		menuToolsReportBuilder.addActionListener(actionAdapter);

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
		menuSession.addSeparator();
//		menuSession.add(menuSessionSave);
//		menuSession.add(menuSessionUndo);
//		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
//		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuView.add(menuViewPanel);

		menuTools.add(menuToolsAdmin);
		menuTools.add(menuToolsConfig);
		menuTools.add(menuToolsComponents);
		menuTools.add(menuToolsScheme);
		menuTools.add(menuToolsMap);
		menuTools.add(menuToolsTrace);
		menuTools.add(menuToolsModel);
		menuTools.add(menuToolsSchedule);
		menuTools.add(menuToolsMonitor);
		menuTools.add(menuToolsNorms);
		menuTools.add(menuToolsAnalyse);
		menuTools.add(menuToolsSurvey);
		menuTools.add(menuToolsPrognosis);
		menuTools.add(menuToolsMaintain);
		menuTools.add(menuToolsReportBuilder);

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
		this.add(menuTools);
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

//		System.out.println("changed model in menu bar");

		menuSessionClose.setVisible(aModel.isVisible("menuSession"));
		menuSessionClose.setEnabled(aModel.isEnabled("menuSession"));

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

		menuViewPanel.setVisible(aModel.isVisible("menuViewPanel"));
		menuViewPanel.setEnabled(aModel.isEnabled("menuViewPanel"));
		menuViewPanel.setSelected(aModel.isSelected("menuViewPanel"));

		menuTools.setVisible(aModel.isVisible("menuTools"));
		menuTools.setEnabled(aModel.isEnabled("menuTools"));

		menuToolsAdmin.setVisible(aModel.isVisible("menuToolsAdmin"));
		menuToolsAdmin.setEnabled(aModel.isEnabled("menuToolsAdmin"));

		menuToolsConfig.setVisible(aModel.isVisible("menuToolsConfig"));
		menuToolsConfig.setEnabled(aModel.isEnabled("menuToolsConfig"));

		menuToolsComponents.setVisible(aModel.isVisible("menuToolsComponents"));
		menuToolsComponents.setEnabled(aModel.isEnabled("menuToolsComponents"));

		menuToolsScheme.setVisible(aModel.isVisible("menuToolsScheme"));
		menuToolsScheme.setEnabled(aModel.isEnabled("menuToolsScheme"));

		menuToolsMap.setVisible(aModel.isVisible("menuToolsMap"));
		menuToolsMap.setEnabled(aModel.isEnabled("menuToolsMap"));

		menuToolsTrace.setVisible(aModel.isVisible("menuToolsTrace"));
		menuToolsTrace.setEnabled(aModel.isEnabled("menuToolsTrace"));

		menuToolsSchedule.setVisible(aModel.isVisible("menuToolsSchedule"));
		menuToolsSchedule.setEnabled(aModel.isEnabled("menuToolsSchedule"));

		menuToolsSurvey.setVisible(aModel.isVisible("menuToolsSurvey"));
		menuToolsSurvey.setEnabled(aModel.isEnabled("menuToolsSurvey"));

		menuToolsModel.setVisible(aModel.isVisible("menuToolsModel"));
		menuToolsModel.setEnabled(aModel.isEnabled("menuToolsModel"));

		menuToolsMonitor.setVisible(aModel.isVisible("menuToolsMonitor"));
		menuToolsMonitor.setEnabled(aModel.isEnabled("menuToolsMonitor"));

		menuToolsAnalyse.setVisible(aModel.isVisible("menuToolsAnalyse"));
		menuToolsAnalyse.setEnabled(aModel.isEnabled("menuToolsAnalyse"));

		menuToolsNorms.setVisible(aModel.isVisible("menuToolsNorms"));
		menuToolsNorms.setEnabled(aModel.isEnabled("menuToolsNorms"));

		menuToolsMaintain.setVisible(aModel.isVisible("menuToolsMaintain"));
		menuToolsMaintain.setEnabled(aModel.isEnabled("menuToolsMaintain"));

		menuToolsPrognosis.setVisible(aModel.isVisible("menuToolsPrognosis"));
		menuToolsPrognosis.setEnabled(aModel.isEnabled("menuToolsPrognosis"));

		menuToolsReportBuilder.setVisible(aModel.isVisible("menuToolsReportBuilder"));
		menuToolsReportBuilder.setEnabled(aModel.isEnabled("menuToolsReportBuilder"));

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

	public void menuSelected(ActionEvent e)
	{
		if(aModel == null)
			return;
		AbstractButton jb = (AbstractButton )e.getSource();
		String s = jb.getName();
		Command command = aModel.getCommand(s);
//		command = (Command )command.clone();
		command.execute();
	}
}

class StarterMenuBar_this_actionAdapter implements java.awt.event.ActionListener
{
	StarterMenuBar adaptee;

	StarterMenuBar_this_actionAdapter(StarterMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.menuSelected(e);
	}
}

