package com.syrus.AMFICOM.Client.General.Lang;

import java.util.*;

import java.text.DateFormatSymbols;

/**
 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N
 */
public class LangModelSurvey extends LangModel 
{
	static public Locale locale;
	static public String language;
	static public String country;
	static public ResourceBundle lang;
	static public DateFormatSymbols symbols;
	static public String resourceBundle;
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N
	 */
	public LangModelSurvey()
	{
		symbols = new DateFormatSymbols(locale);
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N	
	 */
	static public void initialize()
	{
		initialize("com.syrus.AMFICOM.Client.General.Lang.LangModelSurvey");
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N	
	 */
	static public void initialize(String rb)
	{
		System.out.println("initialize lang - " + rb);
		resourceBundle = new String(rb);
		setLangModel("ru", "");
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N	 
	 */
	static public Vector getLangModels()
	{

		Vector vec = new Vector();
		return vec;
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N
	 */
	static public boolean setLangModel(String l, String c)
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
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()	 
	 */
	static public String Text(String componentName)
	{
		return getComponentText(lang, componentName);
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()	 
	 */
	static public String ToolTip(String componentName)
	{
		return getComponentToolTip(lang, componentName);
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()	 
	 */
	static public String getComponentText(
			ResourceBundle lang,
			String componentName)
	{
		return String(componentName + "Text");
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()	 
	 */
	static public String getComponentToolTip(
			ResourceBundle lang,
			String componentName)
	{
		return String(componentName + "ToolTip");
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()	 
	 */
	static public String String(String keyName)
	{
		return String(lang, keyName);
	}
	/**
	 * @deprecated use com.syrus.AMFICOM.Client.General.Survey.I18N.getString()	 
	 */
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
				System.out.println("LangModelSurvey found: " + e + " for key Name " + keyName);
				return "ERROR key!" + keyName;
			}
		}
		catch(Exception e)
		{
			System.out.println("LangModelSurvey found: " + e + " for key Name " + keyName);
			return "ERROR key!" + keyName;
		}
	}
}