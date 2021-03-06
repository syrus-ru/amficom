package com.syrus.AMFICOM.Client.General.Lang;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class LangModelModel
{
	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.model";
	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);

	static public Locale locale;
	static public String language;
	static public String country;
	static public ResourceBundle lang;
	static public DateFormatSymbols symbols;
	static public String resourceBundle;

	public LangModelModel()
	{
		symbols = new DateFormatSymbols(locale);
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
												+ keyName
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
		return string;
	}
}
