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
// * Название: Реализация серверной части интерфейса прототипа РИСД       * //
// *           (включает реализацию пакета pmServer и класса pmRISDImpl)  * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Configure\Application\ConfiguringMenuBar.java          * //
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

package com.syrus.AMFICOM.Client.Configure.Map.Editor;

// Copyright (c) Syrus Systems 2000 Syrus Systems
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class MapMenuBar extends JMenuBar
		implements ApplicationModelListener
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

	JMenu menuView = new JMenu();
	JMenuItem menuViewNavigator = new JMenuItem();
	JMenuItem menuViewAttributes = new JMenuItem();
	JMenuItem menuViewElements = new JMenuItem();
	JMenuItem menuViewSetup = new JMenuItem();
	JMenuItem menuViewMap = new JMenuItem();
	JMenuItem menuViewMapScheme = new JMenuItem();
	JMenuItem menuViewAll = new JMenuItem();

	JMenu menuMap = new JMenu();
	JMenuItem menuMapNew = new JMenuItem();
	JMenuItem menuMapOpen = new JMenuItem();
	JMenuItem menuMapClose = new JMenuItem();
	JMenuItem menuMapSave = new JMenuItem();
	JMenuItem menuMapSaveAs = new JMenuItem();
	JMenuItem menuMapOptions = new JMenuItem();
	JMenuItem menuMapCatalogue = new JMenuItem();

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

	public MapMenuBar()
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

	public MapMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		MapMenuBar_this_actionAdapter actionAdapter =
				new MapMenuBar_this_actionAdapter(this);

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

		menuView.setText(LangModelConfig.Text("menuView"));
		menuView.setName("menuView");
		menuViewNavigator.setText(LangModelConfig.Text("menuViewNavigator"));
		menuViewNavigator.setName("menuViewNavigator");
		menuViewNavigator.addActionListener(actionAdapter);
		menuViewAttributes.setText(LangModelConfig.Text("menuViewAttributes"));
		menuViewAttributes.setName("menuViewAttributes");
		menuViewAttributes.addActionListener(actionAdapter);
		menuViewElements.setText(LangModelConfig.Text("menuViewElements"));
		menuViewElements.setName("menuViewElements");
		menuViewElements.addActionListener(actionAdapter);
		menuViewSetup.setText(LangModelConfig.Text("menuViewSetup"));
		menuViewSetup.setName("menuViewSetup");
		menuViewSetup.addActionListener(actionAdapter);
		menuViewMap.setText(LangModelConfig.Text("menuViewMap"));
		menuViewMap.setName("menuViewMap");
		menuViewMap.addActionListener(actionAdapter);
		menuViewMapScheme.setText(LangModelConfig.Text("menuViewMapScheme"));
		menuViewMapScheme.setName("menuViewMapScheme");
		menuViewMapScheme.addActionListener(actionAdapter);
		menuViewAll.setText(LangModelConfig.Text("menuViewAll"));
		menuViewAll.setName("menuViewAll");
		menuViewAll.addActionListener(actionAdapter);

		menuMap.setText(LangModelMap.Text("menuMap"));
		menuMap.setName("menuMap");
		menuMapNew.setText(LangModelMap.Text("menuMapNew"));
		menuMapNew.setName("menuMapNew");
		menuMapNew.addActionListener(actionAdapter);
		menuMapOpen.setText(LangModelMap.Text("menuMapOpen"));
		menuMapOpen.setName("menuMapOpen");
		menuMapOpen.addActionListener(actionAdapter);
		menuMapClose.setText(LangModelMap.Text("menuMapClose"));
		menuMapClose.setName("menuMapClose");
		menuMapClose.addActionListener(actionAdapter);
		menuMapSave.setText(LangModelMap.Text("menuMapSave"));
		menuMapSave.setName("menuMapSave");
		menuMapSave.addActionListener(actionAdapter);
		menuMapSaveAs.setText(LangModelMap.Text("menuMapSaveAs"));
		menuMapSaveAs.setName("menuMapSaveAs");
		menuMapSaveAs.addActionListener(actionAdapter);
		menuMapOptions.setText(LangModelMap.Text("menuMapOptions"));
		menuMapOptions.setName("menuMapOptions");
		menuMapOptions.addActionListener(actionAdapter);
		menuMapCatalogue.setText(LangModelConfig.Text("menuMapCatalogue"));
		menuMapCatalogue.setName("menuMapCatalogue");
		menuMapCatalogue.addActionListener(actionAdapter);

		menuHelp.setText(LangModel.Text("menuHelp"));
		menuHelp.setName("menuHelp");
		menuHelpContents.setText(LangModel.Text("menuHelpContents"));
		menuHelpContents.setName("menuHelpContents");
		menuHelpContents.addActionListener(actionAdapter);
		menuHelpFind.setText(LangModel.Text("menuHelpFind"));
		menuHelpFind.setName("menuHelpFind");
		menuHelpFind.addActionListener(actionAdapter);
		menuHelpTips.setText(LangModel.Text("menuHelpTips"));
		menuHelpTips.setName("menuHelpTips");
		menuHelpTips.addActionListener(actionAdapter);
		menuHelpStart.setText(LangModel.Text("menuHelpStart"));
		menuHelpStart.setName("menuHelpStart");
		menuHelpStart.addActionListener(actionAdapter);
		menuHelpCourse.setText(LangModel.Text("menuHelpCourse"));
		menuHelpCourse.setName("menuHelpCourse");
		menuHelpCourse.addActionListener(actionAdapter);
		menuHelpHelp.setText(LangModel.Text("menuHelpHelp"));
		menuHelpHelp.setName("menuHelpHelp");
		menuHelpHelp.addActionListener(actionAdapter);
		menuHelpSupport.setText(LangModel.Text("menuHelpSupport"));
		menuHelpSupport.setName("menuHelpSupport");
		menuHelpSupport.addActionListener(actionAdapter);
		menuHelpLicense.setText(LangModel.Text("menuHelpLicense"));
		menuHelpLicense.setName("menuHelpLicense");
		menuHelpLicense.addActionListener(actionAdapter);
		menuHelpAbout.setText(LangModel.Text("menuHelpAbout"));
		menuHelpAbout.setName("menuHelpAbout");
		menuHelpAbout.addActionListener(actionAdapter);

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

		menuView.add(menuViewNavigator);
		menuView.add(menuViewAttributes);
		menuView.add(menuViewElements);
		menuView.add(menuViewSetup);
		menuView.add(menuViewMap);
		menuView.add(menuViewMapScheme);
		menuView.addSeparator();
		menuView.add(menuViewAll);

		menuMap.add(menuMapNew);
		menuMap.add(menuMapOpen);
		menuMap.addSeparator();
		menuMap.add(menuMapSave);
		menuMap.add(menuMapSaveAs);
		menuMap.add(menuMapClose);
 //   menuMap.add(menuMapOptions);
//    menuMap.add(menuMapCatalogue);

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
		this.add(menuMap);
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

//		System.out.println("changed model in menu bar");

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

		menuExit.setVisible(aModel.isVisible("menuExit"));
		menuExit.setEnabled(aModel.isEnabled("menuExit"));

		menuView.setVisible(aModel.isVisible("menuView"));
		menuView.setEnabled(aModel.isEnabled("menuView"));

		menuViewNavigator.setVisible(aModel.isVisible("menuViewNavigator"));
		menuViewNavigator.setEnabled(aModel.isEnabled("menuViewNavigator"));

		menuViewAttributes.setVisible(aModel.isVisible("menuViewAttributes"));
		menuViewAttributes.setEnabled(aModel.isEnabled("menuViewAttributes"));

		menuViewElements.setVisible(aModel.isVisible("menuViewElements"));
		menuViewElements.setEnabled(aModel.isEnabled("menuViewElements"));

		menuViewSetup.setVisible(aModel.isVisible("menuViewSetup"));
		menuViewSetup.setEnabled(aModel.isEnabled("menuViewSetup"));

		menuViewMap.setVisible(aModel.isVisible("menuViewMap"));
		menuViewMap.setEnabled(aModel.isEnabled("menuViewMap"));

		menuViewMapScheme.setVisible(aModel.isVisible("menuViewMapScheme"));
		menuViewMapScheme.setEnabled(aModel.isEnabled("menuViewMapScheme"));

		menuViewAll.setVisible(aModel.isVisible("menuViewAll"));
		menuViewAll.setEnabled(aModel.isEnabled("menuViewAll"));

		menuMap.setVisible(aModel.isVisible("menuMap"));
		menuMap.setEnabled(aModel.isEnabled("menuMap"));

		menuMapNew.setVisible(aModel.isVisible("menuMapNew"));
		menuMapNew.setEnabled(aModel.isEnabled("menuMapNew"));

		menuMapOpen.setVisible(aModel.isVisible("menuMapOpen"));
		menuMapOpen.setEnabled(aModel.isEnabled("menuMapOpen"));

		menuMapClose.setVisible(aModel.isVisible("menuMapClose"));
		menuMapClose.setEnabled(aModel.isEnabled("menuMapClose"));

		menuMapSave.setVisible(aModel.isVisible("menuMapSave"));
		menuMapSave.setEnabled(aModel.isEnabled("menuMapSave"));

		menuMapSaveAs.setVisible(aModel.isVisible("menuMapSaveAs"));
		menuMapSaveAs.setEnabled(aModel.isEnabled("menuMapSaveAs"));

		menuMapOptions.setVisible(aModel.isVisible("menuMapOptions"));
		menuMapOptions.setEnabled(aModel.isEnabled("menuMapOptions"));

		menuMapCatalogue.setVisible(aModel.isVisible("menuMapCatalogue"));
		menuMapCatalogue.setEnabled(aModel.isEnabled("menuMapCatalogue"));

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

class MapMenuBar_this_actionAdapter
		implements java.awt.event.ActionListener
{
	MapMenuBar adaptee;

	MapMenuBar_this_actionAdapter(MapMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

