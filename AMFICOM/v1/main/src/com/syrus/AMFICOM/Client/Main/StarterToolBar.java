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
// * Название: Панель ниструментов окна запуска модулей ПО АМФИКОМ        * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        Main\StarterToolBar.java                                      * //
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

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class StarterToolBar extends JToolBar implements ApplicationModelListener
{
	private ApplicationModel aModel;

	JButton buttonSessionOpen = new JButton();
	JButton buttonSessionClose = new JButton();
	JToggleButton buttonViewPanel = new JToggleButton();
	JButton buttonAdmin = new JButton();
	JButton buttonConfig = new JButton();
	JButton buttonComponents = new JButton();
	JButton buttonScheme = new JButton();
	JButton buttonMap = new JButton();
	JButton buttonTrace = new JButton();
	JButton buttonSchedule = new JButton();
	JButton buttonSurvey = new JButton();
	JButton buttonModel = new JButton();
	JButton buttonMonitor = new JButton();
	JButton buttonAnalyse = new JButton();
	JButton buttonNorms = new JButton();
	JButton buttonMaintain = new JButton();
	JButton buttonPrognosis = new JButton();
	JButton buttonReportBuilder = new JButton();

	JButton buttonHelp = new JButton();

	public final static int img_siz = 16;
	public final static int btn_siz = 24;

	public StarterToolBar()
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

	private void jbInit() throws Exception
	{
		StarterToolBar_this_actionAdapter actionAdapter =
				new StarterToolBar_this_actionAdapter(this);

		Dimension buttonSize = new Dimension(btn_siz, btn_siz);

		buttonSessionOpen = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/open_session.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonSessionOpen.setText("");
		buttonSessionOpen.setMaximumSize(buttonSize);
		buttonSessionOpen.setPreferredSize(buttonSize);
		buttonSessionOpen.setToolTipText(LangModelMain.ToolTip("menuSessionNew"));
		buttonSessionOpen.setName("menuSessionNew");
		buttonSessionOpen.addActionListener(actionAdapter);

		buttonSessionClose = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/close_session.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonSessionClose.setText("");
		buttonSessionClose.setMaximumSize(buttonSize);
		buttonSessionClose.setPreferredSize(buttonSize);
		buttonSessionClose.setToolTipText(LangModelMain.ToolTip("menuSessionClose"));
		buttonSessionClose.setName("menuSessionClose");
		buttonSessionClose.addActionListener(actionAdapter);

		buttonViewPanel = new JToggleButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/view_panel.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonViewPanel.setText("");
		buttonViewPanel.setMaximumSize(buttonSize);
		buttonViewPanel.setPreferredSize(buttonSize);
		buttonViewPanel.setToolTipText(LangModelMain.ToolTip("menuViewPanel"));
		buttonViewPanel.setName("menuViewPanel");
		buttonViewPanel.addActionListener(actionAdapter);

		buttonAdmin = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/administrate_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonAdmin.setText("");
		buttonAdmin.setMaximumSize(buttonSize);
		buttonAdmin.setPreferredSize(buttonSize);
		buttonAdmin.setToolTipText(LangModelMain.ToolTip("menuToolsAdmin"));
		buttonAdmin.setName("menuToolsAdmin");
		buttonAdmin.addActionListener(actionAdapter);

		buttonConfig = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/config_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonConfig.setText("");
		buttonConfig.setMaximumSize(buttonSize);
		buttonConfig.setPreferredSize(buttonSize);
		buttonConfig.setToolTipText(LangModelMain.ToolTip("menuToolsConfig"));
		buttonConfig.setName("menuToolsConfig");
		buttonConfig.addActionListener(actionAdapter);

		buttonComponents = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/components_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonComponents.setText("");
		buttonComponents.setMaximumSize(buttonSize);
		buttonComponents.setPreferredSize(buttonSize);
		buttonComponents.setToolTipText(LangModelMain.ToolTip("menuToolsComponents"));
		buttonComponents.setName("menuToolsComponents");
		buttonComponents.addActionListener(actionAdapter);

		buttonScheme = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/schematics_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonScheme.setText("");
		buttonScheme.setMaximumSize(buttonSize);
		buttonScheme.setPreferredSize(buttonSize);
		buttonScheme.setToolTipText(LangModelMain.ToolTip("menuToolsScheme"));
		buttonScheme.setName("menuToolsScheme");
		buttonScheme.addActionListener(actionAdapter);

		buttonMap = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/map_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonMap.setText("");
		buttonMap.setMaximumSize(buttonSize);
		buttonMap.setPreferredSize(buttonSize);
		buttonMap.setToolTipText(LangModelMain.ToolTip("menuToolsMap"));
		buttonMap.setName("menuToolsMap");
		buttonMap.addActionListener(actionAdapter);

		buttonTrace = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/design_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonTrace.setText("");
		buttonTrace.setMaximumSize(buttonSize);
		buttonTrace.setPreferredSize(buttonSize);
		buttonTrace.setToolTipText(LangModelMain.ToolTip("menuToolsTrace"));
		buttonTrace.setName("menuToolsTrace");
		buttonTrace.addActionListener(actionAdapter);

		buttonSchedule = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/scheduling_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonSchedule.setText("");
		buttonSchedule.setMaximumSize(buttonSize);
		buttonSchedule.setPreferredSize(buttonSize);
		buttonSchedule.setToolTipText(LangModelMain.ToolTip("menuToolsSchedule"));
		buttonSchedule.setName("menuToolsSchedule");
		buttonSchedule.addActionListener(actionAdapter);

		buttonSurvey = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/observe_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonSurvey.setText("");
		buttonSurvey.setMaximumSize(buttonSize);
		buttonSurvey.setPreferredSize(buttonSize);
		buttonSurvey.setToolTipText(LangModelMain.ToolTip("menuToolsSurvey"));
		buttonSurvey.setName("menuToolsSurvey");
		buttonSurvey.addActionListener(actionAdapter);

		buttonModel = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/model_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonModel.setText("");
		buttonModel.setMaximumSize(buttonSize);
		buttonModel.setPreferredSize(buttonSize);
		buttonModel.setToolTipText(LangModelMain.ToolTip("menuToolsModel"));
		buttonModel.setName("menuToolsModel");
		buttonModel.addActionListener(actionAdapter);

		buttonMonitor = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/analyse_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonMonitor.setText("");
		buttonMonitor.setMaximumSize(buttonSize);
		buttonMonitor.setPreferredSize(buttonSize);
		buttonMonitor.setToolTipText(LangModelMain.ToolTip("menuToolsMonitor"));
		buttonMonitor.setName("menuToolsMonitor");
		buttonMonitor.addActionListener(actionAdapter);

		buttonAnalyse = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/survey_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonAnalyse.setText("");
		buttonAnalyse.setMaximumSize(buttonSize);
		buttonAnalyse.setPreferredSize(buttonSize);
		buttonAnalyse.setToolTipText(LangModelMain.ToolTip("menuToolsAnalyse"));
		buttonAnalyse.setName("menuToolsAnalyse");
		buttonAnalyse.addActionListener(actionAdapter);

		buttonNorms = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/evaluation_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonNorms.setText("");
		buttonNorms.setMaximumSize(buttonSize);
		buttonNorms.setPreferredSize(buttonSize);
		buttonNorms.setToolTipText(LangModelMain.ToolTip("menuToolsNorms"));
		buttonNorms.setName("menuToolsNorms");
		buttonNorms.addActionListener(actionAdapter);

		buttonMaintain = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/maintenance_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonMaintain.setText("");
		buttonMaintain.setMaximumSize(buttonSize);
		buttonMaintain.setPreferredSize(buttonSize);
		buttonMaintain.setToolTipText(LangModelMain.ToolTip("menuToolsMaintain"));
		buttonMaintain.setName("menuToolsMaintain");
		buttonMaintain.addActionListener(actionAdapter);

		buttonPrognosis = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/prognosis_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonPrognosis.setText("");
		buttonPrognosis.setMaximumSize(buttonSize);
		buttonPrognosis.setPreferredSize(buttonSize);
		buttonPrognosis.setToolTipText(LangModelMain.ToolTip("menuToolsPrognosis"));
		buttonPrognosis.setName("menuToolsPrognosis");
		buttonPrognosis.addActionListener(actionAdapter);

		buttonReportBuilder = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/main/report_mini.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonReportBuilder.setText("");
		buttonReportBuilder.setMaximumSize(buttonSize);
		buttonReportBuilder.setPreferredSize(buttonSize);
		buttonReportBuilder.setToolTipText(LangModelMain.ToolTip("menuToolsReportBuilder"));
		buttonReportBuilder.setName("menuToolsReportBuilder");
		buttonReportBuilder.addActionListener(actionAdapter);

		buttonHelp = new JButton(new ImageIcon(
			Toolkit.getDefaultToolkit().getImage("images/help.gif").getScaledInstance(
					img_siz,
					img_siz,
					Image.SCALE_DEFAULT)));
		buttonHelp.setText("");
		buttonHelp.setMaximumSize(buttonSize);
		buttonHelp.setPreferredSize(buttonSize);
		buttonHelp.setToolTipText(LangModelMain.ToolTip("menuHelpAbout"));
		buttonHelp.setName("menuHelpAbout");
		buttonHelp.addActionListener(actionAdapter);

		add(buttonSessionOpen);
		add(buttonSessionClose);
		addSeparator();
		add(buttonViewPanel);
		addSeparator();
		add(buttonAdmin);
		add(buttonConfig);
		add(buttonComponents);
		add(buttonScheme);
		add(buttonMap);
		add(buttonTrace);
		add(buttonModel);
		add(buttonSchedule);
		add(buttonMonitor);
		add(buttonNorms);
		add(buttonAnalyse);
		add(buttonSurvey);
		add(buttonMaintain);
		add(buttonPrognosis);
		add(buttonReportBuilder);
		addSeparator();
		add(buttonHelp);
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

//		System.out.println("changed model in tool bar");

		buttonSessionOpen.setVisible(aModel.isVisible("menuSessionNew"));
		buttonSessionOpen.setEnabled(aModel.isEnabled("menuSessionNew"));

		buttonSessionClose.setVisible(aModel.isVisible("menuSessionClose"));
		buttonSessionClose.setEnabled(aModel.isEnabled("menuSessionClose"));

		buttonViewPanel.setVisible(aModel.isVisible("menuViewPanel"));
		buttonViewPanel.setEnabled(aModel.isEnabled("menuViewPanel"));
		buttonViewPanel.setSelected(aModel.isSelected("menuViewPanel"));

		buttonAdmin.setVisible(aModel.isVisible("menuToolsAdmin"));
		buttonAdmin.setEnabled(aModel.isEnabled("menuToolsAdmin"));

		buttonConfig.setVisible(aModel.isVisible("menuToolsConfig"));
		buttonConfig.setEnabled(aModel.isEnabled("menuToolsConfig"));

		buttonComponents.setVisible(aModel.isVisible("menuToolsComponents"));
		buttonComponents.setEnabled(aModel.isEnabled("menuToolsComponents"));

		buttonScheme.setVisible(aModel.isVisible("menuToolsScheme"));
		buttonScheme.setEnabled(aModel.isEnabled("menuToolsScheme"));

		buttonMap.setVisible(aModel.isVisible("menuToolsMap"));
		buttonMap.setEnabled(aModel.isEnabled("menuToolsMap"));

		buttonTrace.setVisible(aModel.isVisible("menuToolsTrace"));
		buttonTrace.setEnabled(aModel.isEnabled("menuToolsTrace"));

		buttonSchedule.setVisible(aModel.isVisible("menuToolsSchedule"));
		buttonSchedule.setEnabled(aModel.isEnabled("menuToolsSchedule"));

		buttonSurvey.setVisible(aModel.isVisible("menuToolsSurvey"));
		buttonSurvey.setEnabled(aModel.isEnabled("menuToolsSurvey"));

		buttonModel.setVisible(aModel.isVisible("menuToolsModel"));
		buttonModel.setEnabled(aModel.isEnabled("menuToolsModel"));

		buttonMonitor.setVisible(aModel.isVisible("menuToolsMonitor"));
		buttonMonitor.setEnabled(aModel.isEnabled("menuToolsMonitor"));

		buttonAnalyse.setVisible(aModel.isVisible("menuToolsAnalyse"));
		buttonAnalyse.setEnabled(aModel.isEnabled("menuToolsAnalyse"));

		buttonNorms.setVisible(aModel.isVisible("menuToolsNorms"));
		buttonNorms.setEnabled(aModel.isEnabled("menuToolsNorms"));

		buttonMaintain.setVisible(aModel.isVisible("menuToolsMaintain"));
		buttonMaintain.setEnabled(aModel.isEnabled("menuToolsMaintain"));

		buttonPrognosis.setVisible(aModel.isVisible("menuToolsPrognosis"));
		buttonPrognosis.setEnabled(aModel.isEnabled("menuToolsPrognosis"));

		buttonReportBuilder.setVisible(aModel.isVisible("menuToolsReportBuilder"));
		buttonReportBuilder.setEnabled(aModel.isEnabled("menuToolsReportBuilder"));

		buttonHelp.setVisible(aModel.isVisible("menuHelpAbout"));
		buttonHelp.setEnabled(aModel.isEnabled("menuHelpAbout"));
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

class StarterToolBar_this_actionAdapter implements java.awt.event.ActionListener
{
	StarterToolBar adaptee;

	StarterToolBar_this_actionAdapter(StarterToolBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

