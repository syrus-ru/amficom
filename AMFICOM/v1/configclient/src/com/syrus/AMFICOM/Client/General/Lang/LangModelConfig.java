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
// * Название: модуль организации многоязыковой поддержки для модуля      * //
// *         Конфигурирование клиентской части ПО АМФИКОМ                 * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelConfig.java                      * //
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

package com.syrus.AMFICOM.Client.General.Lang;

import java.util.Vector;
import java.awt.Component;
import java.lang.reflect.Field;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.text.DateFormatSymbols;

import com.syrus.AMFICOM.Client.General.Lang.*;

public class LangModelConfig extends LangModel
{
	public static Locale locale;
	public static String language;
	public static String country;
	public static ResourceBundle lang;
	public static DateFormatSymbols symbols;
	public static String resourceBundle;

	public LangModelConfig()
	{
		symbols = new DateFormatSymbols(locale);
	}

	public static void initialize()
	{
		initialize("com.syrus.AMFICOM.Client.General.Lang.LangModelConfig");
	}

	public static void initialize(String rb)
	{
		System.out.println("initialize lang - " + rb);
		resourceBundle = new String(rb);
		setLangModel("ru", "");
	}

	public static Vector getLangModels()
	{

		Vector vec = new Vector();
		return vec;
	}

	public static boolean setLangModel(String l, String c)
	{
		language = l;
		country = c;
		try
		{
			locale = new Locale(language, country);
			lang = ResourceBundle.getBundle(
					resourceBundle,
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

	public static String Text(String componentName)
	{
		return getComponentText(lang, componentName);
	}

	public static String ToolTip(String componentName)
	{
		return getComponentToolTip(lang, componentName);
	}

	public static String getComponentText(
			ResourceBundle lang,
			String componentName)
	{
		return String(componentName + "Text");
	}

	public static String getComponentToolTip(
			ResourceBundle lang,
			String componentName)
	{
		return String(componentName + "ToolTip");
	}

	public static String String(String keyName)
	{
		return String(lang, keyName);
	}

	public static String String(ResourceBundle lang, String keyName)
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
				System.out.println("LangModelConfig found: " + e + " for key Name " + keyName);
				return "ERROR key!" + keyName;
			}
		}
		catch(Exception e)
		{
			System.out.println("LangModelConfig found: " + e + " for key Name " + keyName);
			return "ERROR key!" + keyName;
		}
	}
}

 