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

package com.syrus.AMFICOM.Client.Configure;

// Copyright (c) Syrus Systems 2000 Syrus Systems
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Command.*;

public class ConfigureMenuBar extends JMenuBar
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
	JMenuItem menuViewNavigator = new JMenuItem();
	JMenuItem menuViewMessages = new JMenuItem();
	JMenuItem menuViewToolbar = new JMenuItem();
	JMenuItem menuViewAttributes = new JMenuItem();
	JMenuItem menuViewElements = new JMenuItem();
	JMenuItem menuViewMapScheme = new JMenuItem();
	JMenuItem menuViewRefresh = new JMenuItem();
	JMenuItem menuViewCatalogue = new JMenuItem();
	JMenuItem menuViewAll = new JMenuItem();

	JMenu menuScheme = new JMenu();
	JMenu menuSchemeMap = new JMenu();
	JMenuItem menuSchemeMapBitmaps = new JMenuItem();
	JMenuItem menuSchemeMapIcons = new JMenuItem();
	JMenuItem menuSchemeMapStyle = new JMenuItem();
	JMenuItem menuSchemeMapGIS = new JMenuItem();
	JMenuItem menuSchemeMapCoord = new JMenuItem();
	JMenu menuSchemeNet = new JMenu();
	JMenuItem menuSchemeNetScheme = new JMenuItem();
	JMenuItem menuSchemeNetAttribute = new JMenuItem();
	JMenuItem menuSchemeNetElType = new JMenuItem();
	JMenuItem menuSchemeNetElement = new JMenuItem();
	JMenuItem menuSchemeNetCatalogue = new JMenuItem();
	JMenuItem menuSchemeNetView = new JMenuItem();
	JMenuItem menuSchemeNetOpen = new JMenuItem();
	JMenuItem menuSchemeNetOpenScheme = new JMenuItem();
	JMenu menuSchemeJ = new JMenu();
	JMenuItem menuSchemeJScheme = new JMenuItem();
	JMenuItem menuSchemeJAttribute = new JMenuItem();
	JMenuItem menuSchemeJElType = new JMenuItem();
	JMenuItem menuSchemeJElement = new JMenuItem();
	JMenuItem menuSchemeJCatalogue = new JMenuItem();
	JMenuItem menuSchemeJLayout = new JMenuItem();
	JMenuItem menuSchemeJOpen = new JMenuItem();

	JMenu menuObject = new JMenu();
	JMenuItem menuObjectDomain = new JMenuItem();
	JMenuItem menuObjectNetDir = new JMenuItem();
	JMenuItem menuObjectNetCat = new JMenuItem();
	JMenuItem menuObjectJDir = new JMenuItem();
	JMenuItem menuObjectJCat = new JMenuItem();

	JMenu menuNet = new JMenu();
	JMenu menuNetDir = new JMenu();
	JMenuItem menuNetDirTechnology = new JMenuItem();
	JMenuItem menuNetDirProtocol = new JMenuItem();
	JMenuItem menuNetDirStack = new JMenuItem();
	JMenuItem menuNetDirAddress = new JMenuItem();
	JMenuItem menuNetDirInterface = new JMenuItem();
	JMenuItem menuNetDirLink = new JMenuItem();
	JMenuItem menuNetDirEquipment = new JMenuItem();
	JMenuItem menuNetDirPort = new JMenuItem();
	JMenuItem menuNetDirResource = new JMenuItem();
	JMenu menuNetCat = new JMenu();
	JMenuItem menuNetCatEquipment = new JMenuItem();
	JMenuItem menuNetCatLink = new JMenuItem();
	JMenuItem menuNetCatCable = new JMenuItem();
	JMenuItem menuNetCatTPGroup = new JMenuItem();
	JMenuItem menuNetCatTestPoint = new JMenuItem();
	JMenuItem menuNetCatResource = new JMenuItem();
	JMenuItem menuNetLocation = new JMenuItem();

	JMenu menuJ = new JMenu();
	JMenu menuJDir = new JMenu();
	JMenuItem menuJDirKIS = new JMenuItem();
	JMenuItem menuJDirAccessPoint = new JMenuItem();
	JMenuItem menuJDirLink = new JMenuItem();
	JMenu menuJCat = new JMenu();
	JMenuItem menuJCatKIS = new JMenuItem();
	JMenuItem menuJCatAccessPoint = new JMenuItem();
	JMenuItem menuJCatPath = new JMenuItem();
	JMenuItem menuJCatResource = new JMenuItem();
	JMenuItem menuJInstall = new JMenuItem();

	JMenu menuMaintain = new JMenu();
	JMenuItem menuMaintainType = new JMenuItem();
	JMenuItem menuMaintainEvent = new JMenuItem();
	JMenuItem menuMaintainAlarmRule = new JMenuItem();
	JMenuItem menuMaintainMessageRule = new JMenuItem();
	JMenuItem menuMaintainAlertRule = new JMenuItem();
	JMenuItem menuMaintainRule = new JMenuItem();
	JMenuItem menuMaintainReactRule = new JMenuItem();
	JMenuItem menuMaintainCorrectRule = new JMenuItem();

	JMenu menuTools = new JMenu();
	JMenu menuToolsSort = new JMenu();
	JMenuItem menuToolsSortNew = new JMenuItem();
	JMenuItem menuToolsSortSave = new JMenuItem();
	JMenuItem menuToolsSortOpen = new JMenuItem();
	JMenuItem menuToolsSortList = new JMenuItem();
	JMenu menuToolsFilter = new JMenu();
	JMenuItem menuToolsFilterNew = new JMenuItem();
	JMenuItem menuToolsFilterSave = new JMenuItem();
	JMenuItem menuToolsFilterOpen = new JMenuItem();
	JMenuItem menuToolsFilterList = new JMenuItem();
	JMenu menuToolsFind = new JMenu();
	JMenuItem menuToolsFindFast = new JMenuItem();
	JMenuItem menuToolsFindWord = new JMenuItem();
	JMenuItem menuToolsFindField = new JMenuItem();
	JMenuItem menuToolsFindNext = new JMenuItem();
	JMenuItem menuToolsFindQuery = new JMenuItem();
	JMenu menuToolsBookmark = new JMenu();
	JMenuItem menuToolsBookmarkSet = new JMenuItem();
	JMenuItem menuToolsBookmarkGoto = new JMenuItem();
	JMenuItem menuToolsBookmarkList = new JMenuItem();
	JMenuItem menuToolsBookmarkRemove = new JMenuItem();
	JMenuItem menuToolsBookmarkEdit = new JMenuItem();
	JMenuItem menuToolsDictionary = new JMenuItem();
	JMenuItem menuToolsLanguage = new JMenuItem();
	JMenuItem menuToolsLock = new JMenuItem();
	JMenu menuToolsStyle = new JMenu();
	JMenuItem menuToolsStyleText = new JMenuItem();
	JMenuItem menuToolsStyleGraph = new JMenuItem();
	JMenuItem menuToolsStyleLine = new JMenuItem();
	JMenuItem menuToolsStyleTable = new JMenuItem();
	JMenuItem menuToolsStyleScheme = new JMenuItem();
	JMenuItem menuToolsStyleMap = new JMenuItem();
	JMenuItem menuToolsStyleSound = new JMenuItem();
	JMenuItem menuToolsStyleColor = new JMenuItem();
	JMenuItem menuToolsStyleLink = new JMenuItem();
	JMenuItem menuToolsOptions = new JMenuItem();

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

	public ConfigureMenuBar()
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

	public ConfigureMenuBar(ApplicationModel aModel)
	{
		this();
		this.aModel = aModel;
	}

	private void jbInit() throws Exception
	{
		ConfigureMenuBar_this_actionAdapter actionAdapter =
				new ConfigureMenuBar_this_actionAdapter(this);

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
		menuSessionSave.setText(LangModel.Text("menuSessionSave"));
		menuSessionSave.setName("menuSessionSave");
		menuSessionSave.addActionListener(actionAdapter);
		menuSessionUndo.setText(LangModel.Text("menuSessionUndo"));
		menuSessionUndo.setName("menuSessionUndo");
		menuSessionUndo.addActionListener(actionAdapter);
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
		menuViewMessages.setText(LangModelConfig.Text("menuViewMessages"));
		menuViewMessages.setName("menuViewMessages");
		menuViewMessages.addActionListener(actionAdapter);
		menuViewToolbar.setText(LangModelConfig.Text("menuViewToolbar"));
		menuViewToolbar.setName("menuViewToolbar");
		menuViewToolbar.addActionListener(actionAdapter);
		menuViewAttributes.setText(LangModelConfig.Text("menuViewAttributes"));
		menuViewAttributes.setName("menuViewAttributes");
		menuViewAttributes.addActionListener(actionAdapter);
		menuViewElements.setText(LangModelConfig.Text("menuViewElements"));
		menuViewElements.setName("menuViewElements");
		menuViewElements.addActionListener(actionAdapter);
		menuViewMapScheme.setText(LangModelConfig.Text("menuViewMapScheme"));
		menuViewMapScheme.setName("menuViewMapScheme");
		menuViewMapScheme.addActionListener(actionAdapter);
		menuViewRefresh.setText(LangModelConfig.Text("menuViewRefresh"));
		menuViewRefresh.setName("menuViewRefresh");
		menuViewRefresh.addActionListener(actionAdapter);
		menuViewCatalogue.setText(LangModelConfig.Text("menuViewCatalogue"));
		menuViewCatalogue.setName("menuViewCatalogue");
		menuViewCatalogue.addActionListener(actionAdapter);
		menuViewAll.setText(LangModelConfig.Text("menuViewAll"));
		menuViewAll.setName("menuViewAll");
		menuViewAll.addActionListener(actionAdapter);

		menuObject.setText(LangModelConfig.Text("menuObject"));
		menuObject.setName("menuObject");
		menuObjectDomain.setText(LangModelConfig.Text("menuObjectDomain"));
		menuObjectDomain.setName("menuObjectDomain");
		menuObjectDomain.addActionListener(actionAdapter);
		menuObjectNetDir.setText(LangModelConfig.Text("menuNetDir"));
		menuObjectNetDir.setName("menuObjectNetDir");
		menuObjectNetDir.addActionListener(actionAdapter);
		menuObjectNetCat.setText(LangModelConfig.Text("menuNetCat"));
		menuObjectNetCat.setName("menuObjectNetCat");
		menuObjectNetCat.addActionListener(actionAdapter);
		menuObjectJDir.setText(LangModelConfig.Text("menuJDir"));
		menuObjectJDir.setName("menuObjectJDir");
		menuObjectJDir.addActionListener(actionAdapter);
		menuObjectJCat.setText(LangModelConfig.Text("menuJCat"));
		menuObjectJCat.setName("menuObjectJCat");
		menuObjectJCat.addActionListener(actionAdapter);

		menuNet.setText(LangModelConfig.Text("menuNet"));
		menuNet.setName("menuNet");
		menuNetDir.setText(LangModelConfig.Text("menuNetDir"));
		menuNetDir.setName("menuNetDir");
		menuNetDirAddress.setText(LangModelConfig.Text("menuNetDirAddress"));
		menuNetDirAddress.setName("menuNetDirAddress");
		menuNetDirAddress.addActionListener(actionAdapter);
		menuNetDirResource.setText(LangModelConfig.Text("menuNetDirResource"));
		menuNetDirResource.setName("menuNetDirResource");
		menuNetDirResource.addActionListener(actionAdapter);
		menuNetDirEquipment.setText(LangModelConfig.Text("menuNetDirEquipment"));
		menuNetDirEquipment.setName("menuNetDirEquipment");
		menuNetDirEquipment.addActionListener(actionAdapter);
		menuNetDirProtocol.setText(LangModelConfig.Text("menuNetDirProtocol"));
		menuNetDirProtocol.setName("menuNetDirProtocol");
		menuNetDirProtocol.addActionListener(actionAdapter);
		menuNetDirLink.setText(LangModelConfig.Text("menuNetDirLink"));
		menuNetDirLink.setName("menuNetDirLink");
		menuNetDirLink.addActionListener(actionAdapter);
		menuNetDirTechnology.setText(LangModelConfig.Text("menuNetDirTechnology"));
		menuNetDirTechnology.setName("menuNetDirTechnology");
		menuNetDirTechnology.addActionListener(actionAdapter);
		menuNetDirInterface.setText(LangModelConfig.Text("menuNetDirInterface"));
		menuNetDirInterface.setName("menuNetDirInterface");
		menuNetDirInterface.addActionListener(actionAdapter);
		menuNetDirPort.setText(LangModelConfig.Text("menuNetDirPort"));
		menuNetDirPort.setName("menuNetDirPort");
		menuNetDirPort.addActionListener(actionAdapter);
		menuNetDirStack.setText(LangModelConfig.Text("menuNetDirStack"));
		menuNetDirStack.setName("menuNetDirStack");
		menuNetDirStack.addActionListener(actionAdapter);
		menuNetCat.setText(LangModelConfig.Text("menuNetCat"));
		menuNetCat.setName("menuNetCat");
		menuNetCatEquipment.setText(LangModelConfig.Text("menuNetCatEquipment"));
		menuNetCatEquipment.setName("menuNetCatEquipment");
		menuNetCatEquipment.addActionListener(actionAdapter);
		menuNetCatLink.setText(LangModelConfig.Text("menuNetCatLink"));
		menuNetCatLink.setName("menuNetCatLink");
		menuNetCatLink.addActionListener(actionAdapter);
		menuNetCatCable.setText(LangModelConfig.Text("menuNetCatCable"));
		menuNetCatCable.setName("menuNetCatCable");
		menuNetCatCable.addActionListener(actionAdapter);
		menuNetCatResource.setText(LangModelConfig.Text("menuNetCatResource"));
		menuNetCatResource.setName("menuNetCatResource");
		menuNetCatResource.addActionListener(actionAdapter);
		menuNetCatTPGroup.setText(LangModelConfig.Text("menuNetCatTPGroup"));
		menuNetCatTPGroup.setName("menuNetCatTPGroup");
		menuNetCatTPGroup.addActionListener(actionAdapter);
		menuNetCatTestPoint.setText(LangModelConfig.Text("menuNetCatTestPoint"));
		menuNetCatTestPoint.setName("menuNetCatTestPoint");
		menuNetCatTestPoint.addActionListener(actionAdapter);
		menuNetLocation.setText(LangModelConfig.Text("menuNetLocation"));
		menuNetLocation.setName("menuNetLocation");
		menuNetLocation.addActionListener(actionAdapter);

		menuJ.setText(LangModelConfig.Text("menuJ"));
		menuJ.setName("menuJ");
		menuJDir.setText(LangModelConfig.Text("menuJDir"));
		menuJDir.setName("menuJDir");
		menuJDirKIS.setText(LangModelConfig.Text("menuJDirKIS"));
		menuJDirKIS.setName("menuJDirKIS");
		menuJDirKIS.addActionListener(actionAdapter);
		menuJDirAccessPoint.setText(LangModelConfig.Text("menuJDirAccessPoint"));
		menuJDirAccessPoint.setName("menuJDirAccessPoint");
		menuJDirAccessPoint.addActionListener(actionAdapter);
		menuJDirLink.setText(LangModelConfig.Text("menuJDirLink"));
		menuJDirLink.setName("menuJDirLink");
		menuJDirLink.addActionListener(actionAdapter);
		menuJCat.setText(LangModelConfig.Text("menuJCat"));
		menuJCat.setName("menuJCat");
		menuJCatKIS.setText(LangModelConfig.Text("menuJCatKIS"));
		menuJCatKIS.setName("menuJCatKIS");
		menuJCatKIS.addActionListener(actionAdapter);
		menuJCatAccessPoint.setText(LangModelConfig.Text("menuJCatAccessPoint"));
		menuJCatAccessPoint.setName("menuJCatAccessPoint");
		menuJCatAccessPoint.addActionListener(actionAdapter);
		menuJCatPath.setText(LangModelConfig.Text("menuJCatPath"));
		menuJCatPath.setName("menuJCatPath");
		menuJCatPath.addActionListener(actionAdapter);
		menuJCatResource.setText(LangModelConfig.Text("menuJCatResource"));
		menuJCatResource.setName("menuJCatResource");
		menuJCatResource.addActionListener(actionAdapter);
		menuJInstall.setText(LangModelConfig.Text("menuJInstall"));
		menuJInstall.setName("menuJInstall");
		menuJInstall.addActionListener(actionAdapter);

		menuScheme.setText(LangModelConfig.Text("menuScheme"));
		menuScheme.setName("menuScheme");
		menuSchemeMap.setText(LangModelConfig.Text("menuSchemeMap"));
		menuSchemeMap.setName("menuSchemeMap");
		menuSchemeMapBitmaps.setText(LangModelConfig.Text("menuSchemeMapBitmaps"));
		menuSchemeMapBitmaps.setName("menuSchemeMapBitmaps");
		menuSchemeMapBitmaps.addActionListener(actionAdapter);
		menuSchemeMapIcons.setText(LangModelConfig.Text("menuSchemeMapIcons"));
		menuSchemeMapIcons.setName("menuSchemeMapIcons");
		menuSchemeMapIcons.addActionListener(actionAdapter);
		menuSchemeMapStyle.setText(LangModelConfig.Text("menuSchemeMapStyle"));
		menuSchemeMapStyle.setName("menuSchemeMapStyle");
		menuSchemeMapStyle.addActionListener(actionAdapter);
		menuSchemeMapGIS.setText(LangModelConfig.Text("menuSchemeMapGIS"));
		menuSchemeMapGIS.setName("menuSchemeMapGIS");
		menuSchemeMapGIS.addActionListener(actionAdapter);
		menuSchemeMapCoord.setText(LangModelConfig.Text("menuSchemeMapCoord"));
		menuSchemeMapCoord.setName("menuSchemeMapCoord");
		menuSchemeMapCoord.addActionListener(actionAdapter);
		menuSchemeNet.setText(LangModelConfig.Text("menuSchemeNet"));
		menuSchemeNet.setName("menuSchemeNet");
		menuSchemeNetScheme.setText(LangModelConfig.Text("menuSchemeNetScheme"));
		menuSchemeNetScheme.setName("menuSchemeNetScheme");
		menuSchemeNetScheme.addActionListener(actionAdapter);
		menuSchemeNetAttribute.setText(LangModelConfig.Text("menuSchemeNetAttribute"));
		menuSchemeNetAttribute.setName("menuSchemeNetAttribute");
		menuSchemeNetAttribute.addActionListener(actionAdapter);
		menuSchemeNetElType.setText(LangModelConfig.Text("menuSchemeNetElType"));
		menuSchemeNetElType.setName("menuSchemeNetElType");
		menuSchemeNetElType.addActionListener(actionAdapter);
		menuSchemeNetElement.setText(LangModelConfig.Text("menuSchemeNetElement"));
		menuSchemeNetElement.setName("menuSchemeNetElement");
		menuSchemeNetElement.addActionListener(actionAdapter);
		menuSchemeNetCatalogue.setText(LangModelConfig.Text("menuSchemeNetCatalogue"));
		menuSchemeNetCatalogue.setName("menuSchemeNetCatalogue");
		menuSchemeNetCatalogue.addActionListener(actionAdapter);
		menuSchemeNetView.setText(LangModelConfig.Text("menuSchemeNetView"));
		menuSchemeNetView.setName("menuSchemeNetView");
		menuSchemeNetView.addActionListener(actionAdapter);
		menuSchemeNetOpen.setText(LangModelConfig.Text("menuSchemeNetOpen"));
		menuSchemeNetOpen.setName("menuSchemeNetOpen");
		menuSchemeNetOpen.addActionListener(actionAdapter);
		menuSchemeNetOpenScheme.setText(LangModelConfig.Text("menuSchemeNetOpenScheme"));
		menuSchemeNetOpenScheme.setName("menuSchemeNetOpenScheme");
		menuSchemeNetOpenScheme.addActionListener(actionAdapter);
		menuSchemeJ.setText(LangModelConfig.Text("menuSchemeJ"));
		menuSchemeJ.setName("menuSchemeJ");
		menuSchemeJScheme.setText(LangModelConfig.Text("menuSchemeJScheme"));
		menuSchemeJScheme.setName("menuSchemeJScheme");
		menuSchemeJScheme.addActionListener(actionAdapter);
		menuSchemeJAttribute.setText(LangModelConfig.Text("menuSchemeJAttribute"));
		menuSchemeJAttribute.setName("menuSchemeJAttribute");
		menuSchemeJAttribute.addActionListener(actionAdapter);
		menuSchemeJElType.setText(LangModelConfig.Text("menuSchemeJElType"));
		menuSchemeJElType.setName("menuSchemeJElType");
		menuSchemeJElType.addActionListener(actionAdapter);
		menuSchemeJElement.setText(LangModelConfig.Text("menuSchemeJElement"));
		menuSchemeJElement.setName("menuSchemeJElement");
		menuSchemeJElement.addActionListener(actionAdapter);
		menuSchemeJLayout.setText(LangModelConfig.Text("menuSchemeJLayout"));
		menuSchemeJLayout.setName("menuSchemeJLayout");
		menuSchemeJLayout.addActionListener(actionAdapter);
		menuSchemeJCatalogue.setText(LangModelConfig.Text("menuSchemeJCatalogue"));
		menuSchemeJCatalogue.setName("menuSchemeJCatalogue");
		menuSchemeJCatalogue.addActionListener(actionAdapter);
		menuSchemeJOpen.setText(LangModelConfig.Text("menuSchemeJOpen"));
		menuSchemeJOpen.setName("menuSchemeJOpen");
		menuSchemeJOpen.addActionListener(actionAdapter);

		menuMaintain.setText(LangModelConfig.Text("menuMaintain"));
		menuMaintain.setName("menuMaintain");
		menuMaintainType.setText(LangModelConfig.Text("menuMaintainType"));
		menuMaintainType.setName("menuMaintainType");
		menuMaintainType.addActionListener(actionAdapter);
		menuMaintainEvent.setText(LangModelConfig.Text("menuMaintainEvent"));
		menuMaintainEvent.setName("menuMaintainEvent");
		menuMaintainEvent.addActionListener(actionAdapter);
		menuMaintainAlarmRule.setText(LangModelConfig.Text("menuMaintainAlarmRule"));
		menuMaintainAlarmRule.setName("menuMaintainAlarmRule");
		menuMaintainAlarmRule.addActionListener(actionAdapter);
		menuMaintainMessageRule.setText(LangModelConfig.Text("menuMaintainMessageRule"));
		menuMaintainMessageRule.setName("menuMaintainMessageRule");
		menuMaintainMessageRule.addActionListener(actionAdapter);
		menuMaintainAlertRule.setText(LangModelConfig.Text("menuMaintainAlertRule"));
		menuMaintainAlertRule.setName("menuMaintainAlertRule");
		menuMaintainAlertRule.addActionListener(actionAdapter);
		menuMaintainReactRule.setText(LangModelConfig.Text("menuMaintainReactRule"));
		menuMaintainReactRule.setName("menuMaintainReactRule");
		menuMaintainReactRule.addActionListener(actionAdapter);
		menuMaintainRule.setText(LangModelConfig.Text("menuMaintainRule"));
		menuMaintainRule.setName("menuMaintainRule");
		menuMaintainRule.addActionListener(actionAdapter);
		menuMaintainCorrectRule.setText(LangModelConfig.Text("menuMaintainCorrectRule"));
		menuMaintainCorrectRule.setName("menuMaintainCorrectRule");
		menuMaintainCorrectRule.addActionListener(actionAdapter);

		menuTools.setText(LangModelConfig.Text("menuTools"));
		menuTools.setName("menuTools");
		menuToolsSort.setText(LangModelConfig.Text("menuToolsSort"));
		menuToolsSort.setName("menuToolsSort");
		menuToolsSortNew.setText(LangModelConfig.Text("menuToolsSortNew"));
		menuToolsSortNew.setName("menuToolsSortNew");
		menuToolsSortNew.addActionListener(actionAdapter);
		menuToolsSortSave.setText(LangModelConfig.Text("menuToolsSortSave"));
		menuToolsSortSave.setName("menuToolsSortSave");
		menuToolsSortSave.addActionListener(actionAdapter);
		menuToolsSortOpen.setText(LangModelConfig.Text("menuToolsSortOpen"));
		menuToolsSortOpen.setName("menuToolsSortOpen");
		menuToolsSortOpen.addActionListener(actionAdapter);
		menuToolsSortList.setText(LangModelConfig.Text("menuToolsSortList"));
		menuToolsSortList.setName("menuToolsSortList");
		menuToolsSortList.addActionListener(actionAdapter);
		menuToolsFilter.setText(LangModelConfig.Text("menuToolsFilter"));
		menuToolsFilter.setName("menuToolsFilter");
		menuToolsFilterNew.setText(LangModelConfig.Text("menuToolsFilterNew"));
		menuToolsFilterNew.setName("menuToolsFilterNew");
		menuToolsFilterNew.addActionListener(actionAdapter);
		menuToolsFilterSave.setText(LangModelConfig.Text("menuToolsFilterSave"));
		menuToolsFilterSave.setName("menuToolsFilterSave");
		menuToolsFilterSave.addActionListener(actionAdapter);
		menuToolsFilterOpen.setText(LangModelConfig.Text("menuToolsFilterOpen"));
		menuToolsFilterOpen.setName("menuToolsFilterOpen");
		menuToolsFilterOpen.addActionListener(actionAdapter);
		menuToolsFilterList.setText(LangModelConfig.Text("menuToolsFilterList"));
		menuToolsFilterList.setName("menuToolsFilterList");
		menuToolsFilterList.addActionListener(actionAdapter);
		menuToolsFind.setText(LangModelConfig.Text("menuToolsFind"));
		menuToolsFind.setName("menuToolsFind");
		menuToolsFindFast.setText(LangModelConfig.Text("menuToolsFindFast"));
		menuToolsFindFast.setName("menuToolsFindFast");
		menuToolsFindFast.addActionListener(actionAdapter);
		menuToolsFindWord.setText(LangModelConfig.Text("menuToolsFindWord"));
		menuToolsFindWord.setName("menuToolsFindWord");
		menuToolsFindWord.addActionListener(actionAdapter);
		menuToolsFindField.setText(LangModelConfig.Text("menuToolsFindField"));
		menuToolsFindField.setName("menuToolsFindField");
		menuToolsFindField.addActionListener(actionAdapter);
		menuToolsFindNext.setText(LangModelConfig.Text("menuToolsFindNext"));
		menuToolsFindNext.setName("menuToolsFindNext");
		menuToolsFindNext.addActionListener(actionAdapter);
		menuToolsFindQuery.setText(LangModelConfig.Text("menuToolsFindQuery"));
		menuToolsFindQuery.setName("menuToolsFindQuery");
		menuToolsFindQuery.addActionListener(actionAdapter);
		menuToolsBookmark.setText(LangModelConfig.Text("menuToolsBookmark"));
		menuToolsBookmark.setName("menuToolsBookmark");
		menuToolsBookmarkSet.setText(LangModelConfig.Text("menuToolsBookmarkSet"));
		menuToolsBookmarkSet.setName("menuToolsBookmarkSet");
		menuToolsBookmarkSet.addActionListener(actionAdapter);
		menuToolsBookmarkGoto.setText(LangModelConfig.Text("menuToolsBookmarkGoto"));
		menuToolsBookmarkGoto.setName("menuToolsBookmarkGoto");
		menuToolsBookmarkGoto.addActionListener(actionAdapter);
		menuToolsBookmarkList.setText(LangModelConfig.Text("menuToolsBookmarkList"));
		menuToolsBookmarkList.setName("menuToolsBookmarkList");
		menuToolsBookmarkList.addActionListener(actionAdapter);
		menuToolsBookmarkRemove.setText(LangModelConfig.Text("menuToolsBookmarkRemove"));
		menuToolsBookmarkRemove.setName("menuToolsBookmarkRemove");
		menuToolsBookmarkRemove.addActionListener(actionAdapter);
		menuToolsBookmarkEdit.setText(LangModelConfig.Text("menuToolsBookmarkEdit"));
		menuToolsBookmarkEdit.setName("menuToolsBookmarkEdit");
		menuToolsBookmarkEdit.addActionListener(actionAdapter);
		menuToolsDictionary.setText(LangModelConfig.Text("menuToolsDictionary"));
		menuToolsDictionary.setName("menuToolsDictionary");
		menuToolsDictionary.addActionListener(actionAdapter);
		menuToolsLanguage.setText(LangModelConfig.Text("menuToolsLanguage"));
		menuToolsLanguage.setName("menuToolsLanguage");
		menuToolsLanguage.addActionListener(actionAdapter);
		menuToolsLock.setText(LangModelConfig.Text("menuToolsLock"));
		menuToolsLock.setName("menuToolsLock");
		menuToolsLock.addActionListener(actionAdapter);
		menuToolsStyle.setText(LangModelConfig.Text("menuToolsStyle"));
		menuToolsStyle.setName("menuToolsStyle");
		menuToolsStyleText.setText(LangModelConfig.Text("menuToolsStyleText"));
		menuToolsStyleText.setName("menuToolsStyleText");
		menuToolsStyleText.addActionListener(actionAdapter);
		menuToolsStyleGraph.setText(LangModelConfig.Text("menuToolsStyleGraph"));
		menuToolsStyleGraph.setName("menuToolsStyleGraph");
		menuToolsStyleGraph.addActionListener(actionAdapter);
		menuToolsStyleLine.setText(LangModelConfig.Text("menuToolsStyleLine"));
		menuToolsStyleLine.setName("menuToolsStyleLine");
		menuToolsStyleLine.addActionListener(actionAdapter);
		menuToolsStyleTable.setText(LangModelConfig.Text("menuToolsStyleTable"));
		menuToolsStyleTable.setName("menuToolsStyleTable");
		menuToolsStyleTable.addActionListener(actionAdapter);
		menuToolsStyleScheme.setText(LangModelConfig.Text("menuToolsStyleScheme"));
		menuToolsStyleScheme.setName("menuToolsStyleScheme");
		menuToolsStyleScheme.addActionListener(actionAdapter);
		menuToolsStyleMap.setText(LangModelConfig.Text("menuToolsStyleMap"));
		menuToolsStyleMap.setName("menuToolsStyleMap");
		menuToolsStyleMap.addActionListener(actionAdapter);
		menuToolsStyleSound.setText(LangModelConfig.Text("menuToolsStyleSound"));
		menuToolsStyleSound.setName("menuToolsStyleSound");
		menuToolsStyleSound.addActionListener(actionAdapter);
		menuToolsStyleColor.setText(LangModelConfig.Text("menuToolsStyleColor"));
		menuToolsStyleColor.setName("menuToolsStyleColor");
		menuToolsStyleColor.addActionListener(actionAdapter);
		menuToolsStyleLink.setText(LangModelConfig.Text("menuToolsStyleLink"));
		menuToolsStyleLink.setName("menuToolsStyleLink");
		menuToolsStyleLink.addActionListener(actionAdapter);
		menuToolsOptions.setText(LangModelConfig.Text("menuToolsOptions"));
		menuToolsOptions.setName("menuToolsOptions");
		menuToolsOptions.addActionListener(actionAdapter);

		menuWindow.setText(LangModelConfig.Text("menuWindow"));
		menuWindow.setName("menuWindow");
		menuWindowClose.setText(LangModelConfig.Text("menuWindowClose"));
		menuWindowClose.setName("menuWindowClose");
		menuWindowClose.addActionListener(actionAdapter);
		menuWindowCloseAll.setText(LangModelConfig.Text("menuWindowCloseAll"));
		menuWindowCloseAll.setName("menuWindowCloseAll");
		menuWindowCloseAll.addActionListener(actionAdapter);
		menuWindowTileHorizontal.setText(LangModelConfig.Text("menuWindowTileHorizontal"));
		menuWindowTileHorizontal.setName("menuWindowTileHorizontal");
		menuWindowTileHorizontal.addActionListener(actionAdapter);
		menuWindowTileVertical.setText(LangModelConfig.Text("menuWindowTileVertical"));
		menuWindowTileVertical.setName("menuWindowTileVertical");
		menuWindowTileVertical.addActionListener(actionAdapter);
		menuWindowCascade.setText(LangModelConfig.Text("menuWindowCascade"));
		menuWindowCascade.setName("menuWindowCascade");
		menuWindowCascade.addActionListener(actionAdapter);
		menuWindowArrange.setText(LangModelConfig.Text("menuWindowArrange"));
		menuWindowArrange.setName("menuWindowArrange");
		menuWindowArrange.addActionListener(actionAdapter);
		menuWindowArrangeIcons.setText(LangModelConfig.Text("menuWindowArrangeIcons"));
		menuWindowArrangeIcons.setName("menuWindowArrangeIcons");
		menuWindowArrangeIcons.addActionListener(actionAdapter);
		menuWindowMinimizeAll.setText(LangModelConfig.Text("menuWindowMinimizeAll"));
		menuWindowMinimizeAll.setName("menuWindowMinimizeAll");
		menuWindowMinimizeAll.addActionListener(actionAdapter);
		menuWindowRestoreAll.setText(LangModelConfig.Text("menuWindowRestoreAll"));
		menuWindowRestoreAll.setName("menuWindowRestoreAll");
		menuWindowRestoreAll.addActionListener(actionAdapter);
		menuWindowList.setText(LangModelConfig.Text("menuWindowList"));
		menuWindowList.setName("menuWindowList");
		menuWindowList.addActionListener(actionAdapter);

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
//		menuSession.addSeparator();
//		menuSession.add(menuSessionSave);
//		menuSession.add(menuSessionUndo);
		menuSession.addSeparator();
		menuSession.add(menuSessionDomain);
		menuSession.addSeparator();
		menuSession.add(menuExit);

		menuView.add(menuViewNavigator);
		menuView.add(menuViewMessages);
		menuView.add(menuViewToolbar);
		menuView.add(menuViewCatalogue);
		menuView.addSeparator();
		menuView.add(menuViewAttributes);
		menuView.add(menuViewElements);
		menuView.add(menuViewMapScheme);
		menuView.addSeparator();
		menuView.add(menuViewRefresh);
		menuView.add(menuViewAll);

		menuObject.add(menuObjectDomain);
		menuObject.add(menuObjectNetDir);
		menuObject.add(menuObjectNetCat);
		menuObject.add(menuObjectJDir);
		menuObject.add(menuObjectJCat);

		menuNet.add(menuNetDir);
		menuNetDir.add(menuNetDirTechnology);
		menuNetDir.add(menuNetDirProtocol);
		menuNetDir.add(menuNetDirStack);
		menuNetDir.add(menuNetDirInterface);
		menuNetDir.add(menuNetDirAddress);
		menuNetDir.add(menuNetDirLink);
		menuNetDir.add(menuNetDirEquipment);
		menuNetDir.add(menuNetDirPort);
		menuNetDir.add(menuNetDirResource);
		menuNet.add(menuNetCat);
		menuNetCat.add(menuNetCatEquipment);
		menuNetCat.add(menuNetCatLink);
		menuNetCat.add(menuNetCatCable);
		menuNetCat.add(menuNetCatResource);
		menuNetCat.add(menuNetCatTPGroup);
		menuNetCat.add(menuNetCatTestPoint);
		menuNet.add(menuNetLocation);

		menuJ.add(menuJDir);
		menuJDir.add(menuJDirLink);
		menuJDir.add(menuJDirKIS);
		menuJDir.add(menuJDirAccessPoint);
		menuJ.add(menuJCat);
		menuJ.add(menuJInstall);
		menuJCat.add(menuJCatKIS);
		menuJCat.add(menuJCatAccessPoint);
		menuJCat.add(menuJCatPath);
		menuJCat.add(menuJCatResource);

		menuScheme.add(menuSchemeNetElement);
		menuScheme.add(menuSchemeNetScheme);
		menuScheme.add(menuSchemeNetView);
		menuScheme.addSeparator();
		menuScheme.add(menuSchemeNetOpenScheme);
		menuScheme.add(menuSchemeNetOpen);

//		menuScheme.add(menuSchemeNetElType);
//		menuScheme.add(menuSchemeNetAttribute);
//		menuScheme.add(menuSchemeNetCatalogue);
//		menuScheme.add(menuSchemeNetOpen);

/*
		menuScheme.add(menuSchemeMap);
		menuScheme.add(menuSchemeNet);
		menuScheme.add(menuSchemeJ);
		menuSchemeMap.add(menuSchemeMapGIS);
		menuSchemeMap.add(menuSchemeMapCoord);
		menuSchemeMap.add(menuSchemeMapBitmaps);
		menuSchemeMap.add(menuSchemeMapIcons);
		menuSchemeMap.add(menuSchemeMapStyle);
		menuSchemeNet.add(menuSchemeNetScheme);
		menuSchemeNet.add(menuSchemeNetAttribute);
		menuSchemeNet.add(menuSchemeNetElType);
		menuSchemeNet.add(menuSchemeNetElement);
		menuSchemeNet.add(menuSchemeNetCatalogue);
		menuSchemeNet.add(menuSchemeNetView);
		menuSchemeNet.add(menuSchemeNetOpen);
*/
		menuSchemeJ.add(menuSchemeJScheme);
		menuSchemeJ.add(menuSchemeJAttribute);
		menuSchemeJ.add(menuSchemeJElType);
		menuSchemeJ.add(menuSchemeJElement);
		menuSchemeJ.add(menuSchemeJCatalogue);
		menuSchemeJ.add(menuSchemeJLayout);
		menuSchemeJ.add(menuSchemeJOpen);

		menuMaintain.add(menuMaintainType);
		menuMaintain.add(menuMaintainEvent);
		menuMaintain.add(menuMaintainAlarmRule);
		menuMaintain.add(menuMaintainMessageRule);
		menuMaintain.add(menuMaintainAlertRule);
		menuMaintain.add(menuMaintainReactRule);
		menuMaintain.add(menuMaintainRule);
		menuMaintain.add(menuMaintainCorrectRule);

		menuTools.add(menuToolsSort);
		menuTools.add(menuToolsFilter);
		menuTools.addSeparator();
		menuTools.add(menuToolsFind);
		menuTools.add(menuToolsBookmark);
		menuTools.addSeparator();
		menuTools.add(menuToolsDictionary);
		menuTools.add(menuToolsLanguage);
		menuTools.addSeparator();
		menuTools.add(menuToolsLock);
		menuTools.addSeparator();
		menuTools.add(menuToolsStyle);
		menuTools.add(menuToolsOptions);
		menuToolsSort.add(menuToolsSortNew);
		menuToolsSort.add(menuToolsSortSave);
		menuToolsSort.add(menuToolsSortOpen);
		menuToolsSort.add(menuToolsSortList);
		menuToolsSort.addSeparator();
		menuToolsFilter.add(menuToolsFilterNew);
		menuToolsFilter.add(menuToolsFilterSave);
		menuToolsFilter.add(menuToolsFilterOpen);
		menuToolsFilter.add(menuToolsFilterList);
		menuToolsFilter.addSeparator();
		menuToolsFind.add(menuToolsFindFast);
		menuToolsFind.add(menuToolsFindWord);
		menuToolsFind.add(menuToolsFindField);
		menuToolsFind.add(menuToolsFindNext);
		menuToolsFind.addSeparator();
		menuToolsFind.add(menuToolsFindQuery);
		menuToolsBookmark.add(menuToolsBookmarkSet);
		menuToolsBookmark.add(menuToolsBookmarkRemove);
		menuToolsBookmark.add(menuToolsBookmarkGoto);
		menuToolsBookmark.add(menuToolsBookmarkList);
		menuToolsBookmark.add(menuToolsBookmarkEdit);
		menuToolsBookmark.addSeparator();
		menuToolsStyle.add(menuToolsStyleText);
		menuToolsStyle.add(menuToolsStyleGraph);
		menuToolsStyle.add(menuToolsStyleLine);
		menuToolsStyle.add(menuToolsStyleTable);
		menuToolsStyle.add(menuToolsStyleScheme);
		menuToolsStyle.add(menuToolsStyleMap);
		menuToolsStyle.add(menuToolsStyleSound);
		menuToolsStyle.add(menuToolsStyleColor);
		menuToolsStyle.add(menuToolsStyleLink);

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
		menuHelp.addSeparator();
		menuHelp.add(menuHelpHelp);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpSupport);
		menuHelp.add(menuHelpLicense);
		menuHelp.addSeparator();
		menuHelp.add(menuHelpAbout);

		this.add(menuSession);
		this.add(menuView);
		this.add(menuScheme);
		this.add(menuObject);
		this.add(menuNet);
		this.add(menuJ);
		this.add(menuMaintain);
		this.add(menuTools);
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

		menuViewNavigator.setVisible(aModel.isVisible("menuViewNavigator"));
		menuViewNavigator.setEnabled(aModel.isEnabled("menuViewNavigator"));

		menuViewMessages.setVisible(aModel.isVisible("menuViewMessages"));
		menuViewMessages.setEnabled(aModel.isEnabled("menuViewMessages"));

		menuViewToolbar.setVisible(aModel.isVisible("menuViewToolbar"));
		menuViewToolbar.setEnabled(aModel.isEnabled("menuViewToolbar"));

		menuViewAttributes.setVisible(aModel.isVisible("menuViewAttributes"));
		menuViewAttributes.setEnabled(aModel.isEnabled("menuViewAttributes"));

		menuViewElements.setVisible(aModel.isVisible("menuViewElements"));
		menuViewElements.setEnabled(aModel.isEnabled("menuViewElements"));

		menuViewMapScheme.setVisible(aModel.isVisible("menuViewMapScheme"));
		menuViewMapScheme.setEnabled(aModel.isEnabled("menuViewMapScheme"));

		menuViewRefresh.setVisible(aModel.isVisible("menuViewRefresh"));
		menuViewRefresh.setEnabled(aModel.isEnabled("menuViewRefresh"));

		menuViewCatalogue.setVisible(aModel.isVisible("menuViewCatalogue"));
		menuViewCatalogue.setEnabled(aModel.isEnabled("menuViewCatalogue"));

		menuViewAll.setVisible(aModel.isVisible("menuViewAll"));
		menuViewAll.setEnabled(aModel.isEnabled("menuViewAll"));

		menuScheme.setVisible(aModel.isVisible("menuScheme"));
		menuScheme.setEnabled(aModel.isEnabled("menuScheme"));

		menuSchemeMap.setVisible(aModel.isVisible("menuSchemeMap"));
		menuSchemeMap.setEnabled(aModel.isEnabled("menuSchemeMap"));

		menuSchemeMapBitmaps.setVisible(aModel.isVisible("menuSchemeMapBitmaps"));
		menuSchemeMapBitmaps.setEnabled(aModel.isEnabled("menuSchemeMapBitmaps"));

		menuSchemeMapIcons.setVisible(aModel.isVisible("menuSchemeMapIcons"));
		menuSchemeMapIcons.setEnabled(aModel.isEnabled("menuSchemeMapIcons"));

		menuSchemeMapStyle.setVisible(aModel.isVisible("menuSchemeMapStyle"));
		menuSchemeMapStyle.setEnabled(aModel.isEnabled("menuSchemeMapStyle"));

		menuSchemeMapGIS.setVisible(aModel.isVisible("menuSchemeMapGIS"));
		menuSchemeMapGIS.setEnabled(aModel.isEnabled("menuSchemeMapGIS"));

		menuSchemeMapCoord.setVisible(aModel.isVisible("menuSchemeMapCoord"));
		menuSchemeMapCoord.setEnabled(aModel.isEnabled("menuSchemeMapCoord"));

		menuSchemeNet.setVisible(aModel.isVisible("menuSchemeNet"));
		menuSchemeNet.setEnabled(aModel.isEnabled("menuSchemeNet"));

		menuSchemeNetScheme.setVisible(aModel.isVisible("menuSchemeNetScheme"));
		menuSchemeNetScheme.setEnabled(aModel.isEnabled("menuSchemeNetScheme"));

		menuSchemeNetAttribute.setVisible(aModel.isVisible("menuSchemeNetAttribute"));
		menuSchemeNetAttribute.setEnabled(aModel.isEnabled("menuSchemeNetAttribute"));

		menuSchemeNetElType.setVisible(aModel.isVisible("menuSchemeNetElType"));
		menuSchemeNetElType.setEnabled(aModel.isEnabled("menuSchemeNetElType"));

		menuSchemeNetElement.setVisible(aModel.isVisible("menuSchemeNetElement"));
		menuSchemeNetElement.setEnabled(aModel.isEnabled("menuSchemeNetElement"));

		menuSchemeNetCatalogue.setVisible(aModel.isVisible("menuSchemeNetCatalogue"));
		menuSchemeNetCatalogue.setEnabled(aModel.isEnabled("menuSchemeNetCatalogue"));

		menuSchemeNetView.setVisible(aModel.isVisible("menuSchemeNetView"));
		menuSchemeNetView.setEnabled(aModel.isEnabled("menuSchemeNetView"));

		menuSchemeNetOpen.setVisible(aModel.isVisible("menuSchemeNetOpen"));
		menuSchemeNetOpen.setEnabled(aModel.isEnabled("menuSchemeNetOpen"));

		menuSchemeNetOpenScheme.setVisible(aModel.isVisible("menuSchemeNetOpenScheme"));
		menuSchemeNetOpenScheme.setEnabled(aModel.isEnabled("menuSchemeNetOpenScheme"));

		menuNetCatResource.setVisible(aModel.isVisible("menuNetCatResource"));
		menuNetCatResource.setEnabled(aModel.isEnabled("menuNetCatResource"));

		menuSchemeJ.setVisible(aModel.isVisible("menuSchemeJ"));
		menuSchemeJ.setEnabled(aModel.isEnabled("menuSchemeJ"));

		menuSchemeJScheme.setVisible(aModel.isVisible("menuSchemeJScheme"));
		menuSchemeJScheme.setEnabled(aModel.isEnabled("menuSchemeJScheme"));

		menuSchemeJAttribute.setVisible(aModel.isVisible("menuSchemeJAttribute"));
		menuSchemeJAttribute.setEnabled(aModel.isEnabled("menuSchemeJAttribute"));

		menuSchemeJElType.setVisible(aModel.isVisible("menuSchemeJElType"));
		menuSchemeJElType.setEnabled(aModel.isEnabled("menuSchemeJElType"));

		menuSchemeJElement.setVisible(aModel.isVisible("menuSchemeJElement"));
		menuSchemeJElement.setEnabled(aModel.isEnabled("menuSchemeJElement"));

		menuSchemeJCatalogue.setVisible(aModel.isVisible("menuSchemeJCatalogue"));
		menuSchemeJCatalogue.setEnabled(aModel.isEnabled("menuSchemeJCatalogue"));

		menuSchemeJLayout.setVisible(aModel.isVisible("menuSchemeJLayout"));
		menuSchemeJLayout.setEnabled(aModel.isEnabled("menuSchemeJLayout"));

		menuSchemeJOpen.setVisible(aModel.isVisible("menuSchemeJOpen"));
		menuSchemeJOpen.setEnabled(aModel.isEnabled("menuSchemeJOpen"));

		menuObject.setVisible(aModel.isVisible("menuObject"));
		menuObject.setEnabled(aModel.isEnabled("menuObject"));

		menuObjectDomain.setVisible(aModel.isVisible("menuObjectDomain"));
		menuObjectDomain.setEnabled(aModel.isEnabled("menuObjectDomain"));

		menuObjectNetDir.setVisible(aModel.isVisible("menuObjectNetDir"));
		menuObjectNetDir.setEnabled(aModel.isEnabled("menuObjectNetDir"));

		menuObjectNetCat.setVisible(aModel.isVisible("menuObjectNetCat"));
		menuObjectNetCat.setEnabled(aModel.isEnabled("menuObjectNetCat"));

		menuObjectJDir.setVisible(aModel.isVisible("menuObjectJDir"));
		menuObjectJDir.setEnabled(aModel.isEnabled("menuObjectJDir"));

		menuObjectJCat.setVisible(aModel.isVisible("menuObjectJCat"));
		menuObjectJCat.setEnabled(aModel.isEnabled("menuObjectJCat"));

		menuNet.setVisible(aModel.isVisible("menuNet"));
		menuNet.setEnabled(aModel.isEnabled("menuNet"));

		menuNetDir.setVisible(aModel.isVisible("menuNetDir"));
		menuNetDir.setEnabled(aModel.isEnabled("menuNetDir"));

		menuNetDirTechnology.setVisible(aModel.isVisible("menuNetDirTechnology"));
		menuNetDirTechnology.setEnabled(aModel.isEnabled("menuNetDirTechnology"));

		menuNetDirProtocol.setVisible(aModel.isVisible("menuNetDirProtocol"));
		menuNetDirProtocol.setEnabled(aModel.isEnabled("menuNetDirProtocol"));

		menuNetDirStack.setVisible(aModel.isVisible("menuNetDirStack"));
		menuNetDirStack.setEnabled(aModel.isEnabled("menuNetDirStack"));

		menuNetDirAddress.setVisible(aModel.isVisible("menuNetDirAddress"));
		menuNetDirAddress.setEnabled(aModel.isEnabled("menuNetDirAddress"));

		menuNetDirInterface.setVisible(aModel.isVisible("menuNetDirInterface"));
		menuNetDirInterface.setEnabled(aModel.isEnabled("menuNetDirInterface"));

		menuNetDirLink.setVisible(aModel.isVisible("menuNetDirLink"));
		menuNetDirLink.setEnabled(aModel.isEnabled("menuNetDirLink"));

		menuNetDirEquipment.setVisible(aModel.isVisible("menuNetDirEquipment"));
		menuNetDirEquipment.setEnabled(aModel.isEnabled("menuNetDirEquipment"));

		menuNetDirPort.setVisible(aModel.isVisible("menuNetDirPort"));
		menuNetDirPort.setEnabled(aModel.isEnabled("menuNetDirPort"));

		menuNetDirResource.setVisible(aModel.isVisible("menuNetDirResource"));
		menuNetDirResource.setEnabled(aModel.isEnabled("menuNetDirResource"));

		menuNetCat.setVisible(aModel.isVisible("menuNetCat"));
		menuNetCat.setEnabled(aModel.isEnabled("menuNetCat"));

		menuNetCatEquipment.setVisible(aModel.isVisible("menuNetCatEquipment"));
		menuNetCatEquipment.setEnabled(aModel.isEnabled("menuNetCatEquipment"));

		menuNetCatLink.setVisible(aModel.isVisible("menuNetCatLink"));
		menuNetCatLink.setEnabled(aModel.isEnabled("menuNetCatLink"));

		menuNetCatCable.setVisible(aModel.isVisible("menuNetCatCable"));
		menuNetCatCable.setEnabled(aModel.isEnabled("menuNetCatCable"));

		menuNetCatTPGroup.setVisible(aModel.isVisible("menuNetCatTPGroup"));
		menuNetCatTPGroup.setEnabled(aModel.isEnabled("menuNetCatTPGroup"));

		menuNetCatTestPoint.setVisible(aModel.isVisible("menuNetCatTestPoint"));
		menuNetCatTestPoint.setEnabled(aModel.isEnabled("menuNetCatTestPoint"));

		menuNetLocation.setVisible(aModel.isVisible("menuNetLocation"));
		menuNetLocation.setEnabled(aModel.isEnabled("menuNetLocation"));

		menuJ.setVisible(aModel.isVisible("menuJ"));
		menuJ.setEnabled(aModel.isEnabled("menuJ"));

		menuJDir.setVisible(aModel.isVisible("menuJDir"));
		menuJDir.setEnabled(aModel.isEnabled("menuJDir"));

		menuJDirKIS.setVisible(aModel.isVisible("menuJDirKIS"));
		menuJDirKIS.setEnabled(aModel.isEnabled("menuJDirKIS"));

		menuJDirAccessPoint.setVisible(aModel.isVisible("menuJDirAccessPoint"));
		menuJDirAccessPoint.setEnabled(aModel.isEnabled("menuJDirAccessPoint"));

		menuJDirLink.setVisible(aModel.isVisible("menuJDirLink"));
		menuJDirLink.setEnabled(aModel.isEnabled("menuJDirLink"));

		menuJCat.setVisible(aModel.isVisible("menuJCat"));
		menuJCat.setEnabled(aModel.isEnabled("menuJCat"));

		menuJCatKIS.setVisible(aModel.isVisible("menuJCatKIS"));
		menuJCatKIS.setEnabled(aModel.isEnabled("menuJCatKIS"));

		menuJCatAccessPoint.setVisible(aModel.isVisible("menuJCatAccessPoint"));
		menuJCatAccessPoint.setEnabled(aModel.isEnabled("menuJCatAccessPoint"));

		menuJCatPath.setVisible(aModel.isVisible("menuJCatPath"));
		menuJCatPath.setEnabled(aModel.isEnabled("menuJCatPath"));

		menuJCatResource.setVisible(aModel.isVisible("menuJCatResource"));
		menuJCatResource.setEnabled(aModel.isEnabled("menuJCatResource"));

		menuJInstall.setVisible(aModel.isVisible("menuJInstall"));
		menuJInstall.setEnabled(aModel.isEnabled("menuJInstall"));

		menuMaintain.setVisible(aModel.isVisible("menuMaintain"));
		menuMaintain.setEnabled(aModel.isEnabled("menuMaintain"));

		menuMaintainType.setVisible(aModel.isVisible("menuMaintainType"));
		menuMaintainType.setEnabled(aModel.isEnabled("menuMaintainType"));

		menuMaintainEvent.setVisible(aModel.isVisible("menuMaintainEvent"));
		menuMaintainEvent.setEnabled(aModel.isEnabled("menuMaintainEvent"));

		menuMaintainAlarmRule.setVisible(aModel.isVisible("menuMaintainAlarmRule"));
		menuMaintainAlarmRule.setEnabled(aModel.isEnabled("menuMaintainAlarmRule"));

		menuMaintainMessageRule.setVisible(aModel.isVisible("menuMaintainMessageRule"));
		menuMaintainMessageRule.setEnabled(aModel.isEnabled("menuMaintainMessageRule"));

		menuMaintainAlertRule.setVisible(aModel.isVisible("menuMaintainAlertRule"));
		menuMaintainAlertRule.setEnabled(aModel.isEnabled("menuMaintainAlertRule"));

		menuMaintainRule.setVisible(aModel.isVisible("menuMaintainRule"));
		menuMaintainRule.setEnabled(aModel.isEnabled("menuMaintainRule"));

		menuMaintainReactRule.setVisible(aModel.isVisible("menuMaintainReactRule"));
		menuMaintainReactRule.setEnabled(aModel.isEnabled("menuMaintainReactRule"));

		menuMaintainCorrectRule.setVisible(aModel.isVisible("menuMaintainCorrectRule"));
		menuMaintainCorrectRule.setEnabled(aModel.isEnabled("menuMaintainCorrectRule"));

		menuTools.setVisible(aModel.isVisible("menuTools"));
		menuTools.setEnabled(aModel.isEnabled("menuTools"));

		menuToolsSort.setVisible(aModel.isVisible("menuToolsSort"));
		menuToolsSort.setEnabled(aModel.isEnabled("menuToolsSort"));

		menuToolsSortNew.setVisible(aModel.isVisible("menuToolsSortNew"));
		menuToolsSortNew.setEnabled(aModel.isEnabled("menuToolsSortNew"));

		menuToolsSortSave.setVisible(aModel.isVisible("menuToolsSortSave"));
		menuToolsSortSave.setEnabled(aModel.isEnabled("menuToolsSortSave"));

		menuToolsSortOpen.setVisible(aModel.isVisible("menuToolsSortOpen"));
		menuToolsSortOpen.setEnabled(aModel.isEnabled("menuToolsSortOpen"));

		menuToolsSortList.setVisible(aModel.isVisible("menuToolsSortList"));
		menuToolsSortList.setEnabled(aModel.isEnabled("menuToolsSortList"));

		menuToolsFilter.setVisible(aModel.isVisible("menuToolsFilter"));
		menuToolsFilter.setEnabled(aModel.isEnabled("menuToolsFilter"));

		menuToolsFilterNew.setVisible(aModel.isVisible("menuToolsFilterNew"));
		menuToolsFilterNew.setEnabled(aModel.isEnabled("menuToolsFilterNew"));

		menuToolsFilterSave.setVisible(aModel.isVisible("menuToolsFilterSave"));
		menuToolsFilterSave.setEnabled(aModel.isEnabled("menuToolsFilterSave"));

		menuToolsFilterOpen.setVisible(aModel.isVisible("menuToolsFilterOpen"));
		menuToolsFilterOpen.setEnabled(aModel.isEnabled("menuToolsFilterOpen"));

		menuToolsFilterList.setVisible(aModel.isVisible("menuToolsFilterList"));
		menuToolsFilterList.setEnabled(aModel.isEnabled("menuToolsFilterList"));

		menuToolsFind.setVisible(aModel.isVisible("menuToolsFind"));
		menuToolsFind.setEnabled(aModel.isEnabled("menuToolsFind"));

		menuToolsFindFast.setVisible(aModel.isVisible("menuToolsFindFast"));
		menuToolsFindFast.setEnabled(aModel.isEnabled("menuToolsFindFast"));

		menuToolsFindWord.setVisible(aModel.isVisible("menuToolsFindWord"));
		menuToolsFindWord.setEnabled(aModel.isEnabled("menuToolsFindWord"));

		menuToolsFindField.setVisible(aModel.isVisible("menuToolsFindField"));
		menuToolsFindField.setEnabled(aModel.isEnabled("menuToolsFindField"));

		menuToolsFindNext.setVisible(aModel.isVisible("menuToolsFindNext"));
		menuToolsFindNext.setEnabled(aModel.isEnabled("menuToolsFindNext"));

		menuToolsFindQuery.setVisible(aModel.isVisible("menuToolsFindQuery"));
		menuToolsFindQuery.setEnabled(aModel.isEnabled("menuToolsFindQuery"));

		menuToolsBookmark.setVisible(aModel.isVisible("menuToolsBookmark"));
		menuToolsBookmark.setEnabled(aModel.isEnabled("menuToolsBookmark"));

		menuToolsBookmarkSet.setVisible(aModel.isVisible("menuToolsBookmarkSet"));
		menuToolsBookmarkSet.setEnabled(aModel.isEnabled("menuToolsBookmarkSet"));

		menuToolsBookmarkGoto.setVisible(aModel.isVisible("menuToolsBookmarkGoto"));
		menuToolsBookmarkGoto.setEnabled(aModel.isEnabled("menuToolsBookmarkGoto"));

		menuToolsBookmarkList.setVisible(aModel.isVisible("menuToolsBookmarkList"));
		menuToolsBookmarkList.setEnabled(aModel.isEnabled("menuToolsBookmarkList"));

		menuToolsBookmarkRemove.setVisible(aModel.isVisible("menuToolsBookmarkRemove"));
		menuToolsBookmarkRemove.setEnabled(aModel.isEnabled("menuToolsBookmarkRemove"));

		menuToolsBookmarkEdit.setVisible(aModel.isVisible("menuToolsBookmarkEdit"));
		menuToolsBookmarkEdit.setEnabled(aModel.isEnabled("menuToolsBookmarkEdit"));

		menuToolsDictionary.setVisible(aModel.isVisible("menuToolsDictionary"));
		menuToolsDictionary.setEnabled(aModel.isEnabled("menuToolsDictionary"));

		menuToolsLanguage.setVisible(aModel.isVisible("menuToolsLanguage"));
		menuToolsLanguage.setEnabled(aModel.isEnabled("menuToolsLanguage"));

		menuToolsLock.setVisible(aModel.isVisible("menuToolsLock"));
		menuToolsLock.setEnabled(aModel.isEnabled("menuToolsLock"));

		menuToolsStyle.setVisible(aModel.isVisible("menuToolsStyle"));
		menuToolsStyle.setEnabled(aModel.isEnabled("menuToolsStyle"));

		menuToolsStyleText.setVisible(aModel.isVisible("menuToolsStyleText"));
		menuToolsStyleText.setEnabled(aModel.isEnabled("menuToolsStyleText"));

		menuToolsStyleGraph.setVisible(aModel.isVisible("menuToolsStyleGraph"));
		menuToolsStyleGraph.setEnabled(aModel.isEnabled("menuToolsStyleGraph"));

		menuToolsStyleLine.setVisible(aModel.isVisible("menuToolsStyleLine"));
		menuToolsStyleLine.setEnabled(aModel.isEnabled("menuToolsStyleLine"));

		menuToolsStyleTable.setVisible(aModel.isVisible("menuToolsStyleTable"));
		menuToolsStyleTable.setEnabled(aModel.isEnabled("menuToolsStyleTable"));

		menuToolsStyleScheme.setVisible(aModel.isVisible("menuToolsStyleScheme"));
		menuToolsStyleScheme.setEnabled(aModel.isEnabled("menuToolsStyleScheme"));

		menuToolsStyleMap.setVisible(aModel.isVisible("menuToolsStyleMap"));
		menuToolsStyleMap.setEnabled(aModel.isEnabled("menuToolsStyleMap"));

		menuToolsStyleSound.setVisible(aModel.isVisible("menuToolsStyleSound"));
		menuToolsStyleSound.setEnabled(aModel.isEnabled("menuToolsStyleSound"));

		menuToolsStyleColor.setVisible(aModel.isVisible("menuToolsStyleColor"));
		menuToolsStyleColor.setEnabled(aModel.isEnabled("menuToolsStyleColor"));

		menuToolsStyleLink.setVisible(aModel.isVisible("menuToolsStyleLink"));
		menuToolsStyleLink.setEnabled(aModel.isEnabled("menuToolsStyleLink"));

		menuToolsOptions.setVisible(aModel.isVisible("menuToolsOptions"));
		menuToolsOptions.setEnabled(aModel.isEnabled("menuToolsOptions"));

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

class ConfigureMenuBar_this_actionAdapter 
		implements java.awt.event.ActionListener
{
	ConfigureMenuBar adaptee;

	ConfigureMenuBar_this_actionAdapter(ConfigureMenuBar adaptee)
	{
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e)
	{
		adaptee.this_actionPerformed(e);
	}
}

