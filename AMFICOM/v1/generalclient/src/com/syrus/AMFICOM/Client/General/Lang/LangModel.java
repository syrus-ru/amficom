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
// * Название: Базовый модуль многоязыковой поддержки системы АМФИКОМ     * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\           * //
// *        Client\General\Lang\LangModel.java                            * //
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
// *        модуль описывает основные методы, необходимые для поддержки   * //
// *        нескольких языков в ПО АМФИКОМ. Доступ к строковым константам * //
// *        в модулях АМФИКОМ должен организовываться следующим образом:  * //
// *        Каждый модуль использует языковую модель LangModel<Name>, где * //
// *        <Name> - имя модуля. Для этого в проект включается класс      * //
// *        LangModel<Name>.java, который наследуется от класса           * //
// *        LangModel.java. Класс должен содержать функцию                * //
// *            static public void initialize()                           * //
// *            {                                                         * //
// *                initialize("ClassPath.LangModel<Name>");              * //
// *            }                                                         * //
// *        Где ClassPath указывает путь к классу LangModel<Name>.        * //
// *        Сами языковые константы находятся, соответственно, в классах  * //
// *        LangModel<Name>_ru.                                           * //
// *        Доступ к константам осуществляется через функции:             * //
// *            static public String Text(String componentName)           * //
// *              для получения текста компоненты                         * //
// *              массив contents должен содержать строку                 * //
// *                { "<componentName>Text", "<текст>" }                  * //
// *            static public String ToolTip(String componentName)        * //
// *              для получения всплувающей подсказки компоненты          * //
// *              массив contents должен содержать строку                 * //
// *                { "<componentName>ToolTip", "<подсказка>" }           * //
// *            static public String String(String keyName)               * //
// *              для получения простого текста                           * //
// *              массив contents должен содержать строку                 * //
// *                { "<keyName>", "<текст>" }                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Lang;

import java.awt.Component;

import java.lang.reflect.Field;

import java.text.DateFormatSymbols;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

public class LangModel //extends ListResourceBundle
{
	static public Locale locale;
	static public String language;
	static public String country;
	static public ResourceBundle lang;
	static public DateFormatSymbols symbols;
	static public String resourceBundle;

	public LangModel()
	{
		symbols = new DateFormatSymbols(locale);
	}

	static public void initialize()
	{
		initialize("com.syrus.AMFICOM.Client.General.Lang.LangModel");
	}

	static public void initialize(String rb)
	{
		System.out.println("initialize lang - " + rb);
		resourceBundle = new String(rb);
		setLangModel("ru", "");
	}

	static public Vector getLangModels()
	{

//		Locale locs[] = Locale.getAvailableLocales();
		Vector vec = new Vector();
/*
		Class cl = this.getClass();
		Package pkg = cl.getPackage();
		String s = cl.getResource();
		File file = new File(s);
		String ss = ClassLoader.getSystemResource(pkg.getName());
		if (ss.startsWith("file:///"))
		{
			String path = ss.substring(8);
			Directory dir =
		}
*/
		return vec;
	}

	static public boolean setLangModel(String l, String c)
	{
		language = l;
		country = c;
		try
		{
//			lang = ResourceBundle.getBundle(resourceBundle);

			locale = new Locale(language, country);
			lang = ResourceBundle.getBundle(
					resourceBundle,
//					"com.syrus.AMFICOM.Client.General.Lang.LangModel",
					locale);

			System.out.println("initialize locale - " + locale.toString());
			symbols = new DateFormatSymbols(locale);
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static public String Text(String componentName)
	{
		return getComponentText(lang, componentName);
	}

	static public String ToolTip(String componentName)
	{
		return getComponentToolTip(lang, componentName);
	}

	static public String getComponentText(
			ResourceBundle lang,
			String componentName)
	{
		return String(componentName + "Text");
/*
		try
		{
			return lang.getString(componentName + "Text");
		}
		catch(java.util.MissingResourceException mre)
		{
			try
			{
			Locale loc2 = lang.getLocale();
			Locale loc;
			if(loc2.getCountry() != null && !(loc2.getCountry().equals("")))
				loc = new Locale(loc2.getLanguage(), "");
			else
				loc = new Locale("", "");
			ResourceBundle lang2 =
					ResourceBundle.getBundle(resourceBundle, loc);
//			"LangModel", loc);
			return getComponentText(lang2, componentName);
			}
			catch(Exception e)
			{
				System.out.println(e);
				return "ERROR text!" + componentName;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			return "ERROR text!" + componentName;
		}
*/
	}

	static public String getComponentToolTip(
			ResourceBundle lang,
			String componentName)
	{
		return String(componentName + "ToolTip");
/*
		try
		{
			return lang.getString(componentName + "ToolTip");
		}
		catch(java.util.MissingResourceException mre)
		{
			try
			{
				Locale loc2 = lang.getLocale();
				Locale loc;
				if(loc2.getCountry() != null && !(loc2.getCountry().equals("")))
					loc = new Locale(loc2.getLanguage(), "");
				else
					loc = new Locale("", "");
				ResourceBundle lang2 =
						ResourceBundle.getBundle(resourceBundle, loc);
//						"LangModel", loc);
				return getComponentText(lang2, componentName);
			}
			catch(Exception e)
			{
				System.out.println(e);
				return "ERROR text!" + componentName;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			return "ERROR tooltip!" + componentName;
		}
*/
	}

	static public String getString(String keyName)
	{
		return String(lang, keyName);
	}

	static public String String(String keyName)
	{
		return String(lang, keyName);
	}

	static public String String(ResourceBundle lang, String keyName)
	{
		try
		{
			return lang.getString(keyName);
		}
		catch(java.util.MissingResourceException mre)
		{
			try
			{
				Locale loc2 = lang.getLocale();
				Locale loc;
				if(loc2.getCountry() != null && !(loc2.getCountry().equals("")))
					loc = new Locale(loc2.getLanguage(), "");
				else
				if(loc2.getLanguage() != null && !(loc2.getLanguage().equals("")))
					loc = new Locale("", "");
				else
					throw mre;
				ResourceBundle lang2 =
						ResourceBundle.getBundle(resourceBundle, loc);
				return String(lang2, keyName);
			}
			catch(Exception e)
			{
				System.out.println("LangModel found: " + e + " for key Name " + keyName);
				return "ERROR key!" + keyName;
			}
		}
		catch(Exception e)
		{
			System.out.println("LangModel found: " + e + " for key Name " + keyName);
			return "ERROR key!" + keyName;
		}
	}

	public Object[][] contents = {

		{ "language", "Default" },
		{ "country", "Default" },

		{ "eLanguage", "Default" },
		{ "eCountry", "Default" },


		{ "menuSessionText", "menuSession" },
		{ "menuSessionNewText", "menuSessionNew" },
		{ "menuSessionCloseText", "menuSessionClose" },
		{ "menuSessionOptionsText", "menuSessionOptions" },
		{ "menuSessionConnectionText", "menuSessionConnection" },
		{ "menuSessionChangePasswordText", "menuSessionChangePassword" },
		{ "menuSessionSaveText", "menuSessionSave" },
		{ "menuSessionUndoText", "menuSessionUndo" },
		{ "menuExitText", "menuExit" },

		{ "menuViewText", "menuView" },
		{ "menuViewNavigatorText", "menuViewNavigator" },
		{ "menuViewMessagesText", "menuViewMessages" },
		{ "menuViewToolbarText", "menuViewToolbar" },
		{ "menuViewRefreshText", "menuViewRefresh" },
		{ "menuViewCatalogueText", "menuViewCatalogue" },
		{ "menuViewElementsNavigatorText", "menuViewElementsNavigator" },

		{ "menuNetText", "menuNet" },
		{ "menuNetDirText", "menuNetDir" },
		{ "menuNetDirAddressText", "menuNetDirAddress" },
		{ "menuNetDirResourceText", "menuNetDirResource" },
		{ "menuNetDirEquipmentText", "menuNetDirEquipment" },
		{ "menuNetDirProtocolText", "menuNetDirProtocol" },
		{ "menuNetDirLinkText", "menuNetDirLink" },
		{ "menuNetDirTechnologyText", "menuNetDirTechnology" },
		{ "menuNetDirInterfaceText", "menuNetDirInterface" },
		{ "menuNetDirPortText", "menuNetDirPort" },
		{ "menuNetDirStackText", "menuNetDirStack" },
		{ "menuNetCatText", "menuNetCat" },
		{ "menuNetCatEquipmentText", "menuNetCatEquipment" },
		{ "menuNetCatLinkText", "menuNetCatLink" },
		{ "menuNetCatResourceText", "menuNetCatResource" },
		{ "menuNetCatTPGroupText", "menuNetCatTPGroup" },
		{ "menuNetCatTestPointText", "menuNetCatTestPoint" },
		{ "menuNetLocationText", "menuNetLocation" },

		{ "menuJText", "menuJ" },
		{ "menuJDirText", "menuJDir" },
		{ "menuJDirKISText", "menuJDirKIS" },
		{ "menuJDirAccessPointText", "menuJDirAccessPoint" },
		{ "menuJDirLinkText", "menuJDirLink" },
		{ "menuJCatText", "menuJCat" },
		{ "menuJCatKISText", "menuJCatKIS" },
		{ "menuJCatAccessPointText", "menuJCatAccessPoint" },
		{ "menuJCatResourceText", "menuJCatResource" },

		{ "menuSchemeText", "menuScheme" },
		{ "menuSchemeMapBitmapsText", "menuSchemeMapBitmaps" },
		{ "menuSchemeMapIconsText", "menuSchemeMapIcons" },
		{ "menuSchemeMapStyleText", "menuSchemeMapStyle" },
		{ "menuSchemeMapText", "menuSchemeMap" },
		{ "menuSchemeMapGISText", "menuSchemeMapGIS" },
		{ "menuSchemeMapCoordText", "menuSchemeMapCoord" },
		{ "menuSchemeNetText", "menuSchemeNet" },
		{ "menuSchemeNetSchemeText", "menuSchemeNetScheme" },
		{ "menuSchemeNetAttributeText", "menuSchemeNetAttribute" },
		{ "menuSchemeNetElTypeText", "menuSchemeNetElType" },
		{ "menuSchemeNetElementText", "menuSchemeNetElement" },
		{ "menuSchemeNetCatalogueText", "menuSchemeNetCatalogue" },
		{ "menuSchemeNetViewText", "menuSchemeNetView" },
		{ "menuSchemeJText", "menuSchemeJ" },
		{ "menuSchemeJSchemeText", "menuSchemeJScheme" },
		{ "menuSchemeJAttributeText", "menuSchemeJAttribute" },
		{ "menuSchemeJElTypeText", "menuSchemeJElType" },
		{ "menuSchemeJElementText", "menuSchemeJElement" },
		{ "menuSchemeJLayoutText", "menuSchemeJLayout" },
		{ "menuSchemeJCatalogueText", "menuSchemeJCatalogue" },

		{ "menuMaintainText", "menuMaintain" },
		{ "menuMaintainTypeText", "menuMaintainType" },
		{ "menuMaintainEventText", "menuMaintainEvent" },
		{ "menuMaintainAlarmRuleText", "menuMaintainAlarmRule" },
		{ "menuMaintainMessageRuleText", "menuMaintainMessageRule" },
		{ "menuMaintainAlertRuleText", "menuMaintainAlertRule" },
		{ "menuMaintainReactRuleText", "menuMaintainReactRule" },
		{ "menuMaintainRuleText", "menuMaintainRule" },
		{ "menuMaintainCorrectRuleText", "menuMaintainCorrectRule" },

		{ "menuToolsText", "menuTools" },
		{ "menuToolsSortText", "menuToolsSort" },
		{ "menuToolsSortNewText", "menuToolsSortNew" },
		{ "menuToolsSortSaveText", "menuToolsSortSave" },
		{ "menuToolsSortOpenText", "menuToolsSortOpen" },
		{ "menuToolsSortListText", "menuToolsSortList" },
		{ "menuToolsFilterText", "menuToolsFilter" },
		{ "menuToolsFilterNewText", "menuToolsFilterNew" },
		{ "menuToolsFilterSaveText", "menuToolsFilterSave" },
		{ "menuToolsFilterOpenText", "menuToolsFilterOpen" },
		{ "menuToolsFilterListText", "menuToolsFilterList" },
		{ "menuToolsFindText", "menuToolsFind" },
		{ "menuToolsFindFastText", "menuToolsFindFast" },
		{ "menuToolsFindWordText", "menuToolsFindWord" },
		{ "menuToolsFindFieldText", "menuToolsFindField" },
		{ "menuToolsFindNextText", "menuToolsFindNext" },
		{ "menuToolsFindQueryText", "menuToolsFindQuery" },
		{ "menuToolsBookmarkText", "menuToolsBookmark" },
		{ "menuToolsBookmarkSetText", "menuToolsBookmarkSet" },
		{ "menuToolsBookmarkGotoText", "menuToolsBookmarkGoto" },
		{ "menuToolsBookmarkListText", "menuToolsBookmarkList" },
		{ "menuToolsBookmarkRemoveText", "menuToolsBookmarkRemove" },
		{ "menuToolsBookmarkEditText", "menuToolsBookmarkEdit" },
		{ "menuToolsDictionaryText", "menuToolsDictionary" },
		{ "menuToolsLanguageText", "menuToolsLanguage" },
		{ "menuToolsLockText", "menuToolsLock" },
		{ "menuToolsStyleText", "menuToolsStyle" },
		{ "menuToolsStyleTextText", "menuToolsStyleText" },
		{ "menuToolsStyleGraphText", "menuToolsStyleGraph" },
		{ "menuToolsStyleLineText", "menuToolsStyleLine" },
		{ "menuToolsStyleTableText", "menuToolsStyleTable" },
		{ "menuToolsStyleSchemeText", "menuToolsStyleScheme" },
		{ "menuToolsStyleMapText", "menuToolsStyleMap" },
		{ "menuToolsStyleSoundText", "menuToolsStyleSound" },
		{ "menuToolsStyleColorText", "menuToolsStyleColor" },
		{ "menuToolsStyleLinkText", "menuToolsStyleLink" },
		{ "menuToolsOptionsText", "menuToolsOptions" },

		{ "menuWindowText", "menuWindow" },
		{ "menuWindowCloseText", "menuWindowClose" },
		{ "menuWindowCloseAllText", "menuWindowCloseAll" },
		{ "menuWindowTileHorizontalText", "menuWindowTileHorizontal" },
		{ "menuWindowTileVerticalText", "menuWindowTileVertical" },
		{ "menuWindowCascadeText", "menuWindowCascade" },
		{ "menuWindowArrangeText", "menuWindowArrange" },
		{ "menuWindowArrangeIconsText", "menuWindowArrangeIcons" },
		{ "menuWindowMinimizeAllText", "menuWindowMinimizeAll" },
		{ "menuWindowRestoreAllText", "menuWindowRestoreAll" },
		{ "menuWindowListText", "menuWindowList" },

		{ "menuHelpText", "menuHelp" },
		{ "menuHelpContentsText", "menuHelpContents" },
		{ "menuHelpFindText", "menuHelpFind" },
		{ "menuHelpTipsText", "menuHelpTips" },
		{ "menuHelpStartText", "menuHelpStart" },
		{ "menuHelpCourseText", "menuHelpCourse" },
		{ "menuHelpHelpText", "menuHelpHelp" },
		{ "menuHelpSupportText", "menuHelpSupport" },
		{ "menuHelpAboutText", "menuHelpAbout" },
		{ "menuJInstallText", "menuJInstall" },

		{ "appText", "app" }
		};

	public Object[][] getContents()
	{
		return contents;
	}

}
