package com.syrus.AMFICOM.Client.General.Lang;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

public class LangModelAnalyse extends LangModel
{
	static public Locale locale;
	static public String language;
	static public String country;
	static public ResourceBundle lang;
	static public DateFormatSymbols symbols;
	static public String resourceBundle;

	public LangModelAnalyse()
	{
		symbols = new DateFormatSymbols(locale);
	}

}