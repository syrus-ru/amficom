package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

public class LangModelAnalyse
{
	private static final String BUNDLE_NAME =
		"com.syrus.AMFICOM.Client.General.Lang.analysis";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
		.getBundle(BUNDLE_NAME);

//	static public Locale locale;
//	static public String language;
//	static public String country;
//	static public ResourceBundle lang;
//	static public DateFormatSymbols symbols;
//	static public String resourceBundle;

	public LangModelAnalyse()
	{
		//symbols = new DateFormatSymbols(locale);
	}

	public static String getString(String keyName)
	{
		//System.out.println("keyName:" + keyName);
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
				string = RESOURCE_BUNDLE.getString(keyName + "Text");
			}
			catch (MissingResourceException mre)
			{
				try
				{
					throw new Exception("key '"
						+ keyName + "Text"
						+ "' "
						+ "not found");
				}
				catch (Exception exc)
				{
					exc.printStackTrace();
				}
			}
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}
		// workaround:
		// Редактор свойств для Eclipse от Долженко не
		// умеет сохранять пустые строки, но без них
		// наследие анализа не работает.
		// Пришлось заменить их все
		// ключевым словом __EMPTY__, и вот здесь
		// они переводятся обратно в пустые.
		// TODO: Исправить код GUI анализа так, чтобы
		// он не пользовался пустыми строками,
		// ЛИБО исправить редактор свойств,
		// чтобы умел сохранять ключи с пустыми строками.
		// //saa, 2004-12
		if (string.equals("__EMPTY__"))
		    string = "";
		return string;
	}
}