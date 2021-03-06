package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

public class LangModelAdmin {

	private static final String BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.admin";

	private static final ResourceBundle	RESOURCE_BUNDLE		= ResourceBundle
																	.getBundle(BUNDLE_NAME);


	protected LangModelAdmin() 
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
				string = RESOURCE_BUNDLE.getString(keyName + "Text");
			}
			catch (MissingResourceException mre)
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
			catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}
		return string;
	}
}
