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
// * Название: модуль организации многоязыковой поддержки для основного   * //
// *         окна клиентской части ПО АМФИКОМ                             * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\Lang\LangModelMain.java                        * //
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
import java.util.MissingResourceException;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.ListResourceBundle;
import java.text.DateFormatSymbols;

import com.syrus.AMFICOM.Client.General.Lang.*;

public class LangModelMain extends LangModel
{
	static public DateFormatSymbols symbols;

	private static final String BUNDLE_NAME = 
			"com.syrus.AMFICOM.Client.General.Lang.main";

	private static final ResourceBundle	RESOURCE_BUNDLE = 
			ResourceBundle.getBundle(getBundleName());

	protected LangModelMain()
	{
	}

	public static String getString(String keyName)
	{
		keyName = keyName.replaceAll(" ", "_");
		String string = null;
		try
		{
			string = RESOURCE_BUNDLE.getString(keyName);
		}
		catch (MissingResourceException e)
		{
			try
			{
				throw new Exception("key '" + keyName + "' not found");
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}
		return string;
	}

}

 