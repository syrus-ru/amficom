package com.syrus.AMFICOM.Client.General.Lang;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

public class LangModelSchematics extends LangModel
{
	private static final String			BUNDLE_NAME			= "com.syrus.AMFICOM.Client.General.Lang.schematics";

	static public Locale locale;
	static public String language;
	static public String country;
	static public ResourceBundle lang;
	static public DateFormatSymbols symbols;
	static public String resourceBundle;

	public LangModelSchematics()
	{
		symbols = new DateFormatSymbols(locale);
	}

	protected static String getBundleName()
	{
		return BUNDLE_NAME;
	}
}
